package bluney.sample.sample.service.market;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.u2ware.springfield.repository.EntityRepository;

import bluney.sample.sample.customtype.market.Market;
import bluney.sample.sample.customtype.market.MarketGin;
import bluney.sample.sample.domain.lease.PyeongLeasePrice;
import bluney.sample.sample.domain.lease.price.LeasePriceEntity;
import bluney.sample.sample.domain.lease.rate.LeaseRateEntity;
import bluney.sample.sample.domain.market.TotalMarket;
import bluney.sample.sample.domain.market.earning.EarningStat;
import bluney.sample.sample.domain.market.earning.stat.EarningStatEntity;
import bluney.sample.sample.domain.market.gin.lease.MarketGinSelling;
import bluney.sample.sample.domain.market.gin.selling.MarketGinLease;
import bluney.sample.sample.domain.market.rate.LeasePerPrice;
import bluney.sample.sample.domain.selling.PyeongSellingPrice;
import bluney.sample.sample.domain.selling.price.SellingPriceEntity;
import bluney.sample.sample.domain.selling.rate.SellingRateEntity;

@Service("marketService")
public class MarketService {

	private static final Logger logger = LoggerFactory.getLogger(MarketService.class);	
	private static final int INTERVAL_FOR_GIN_VALUE = 26;
	private static final int INTERVAL_FOR_EARNING_RATE = 104;

	public static final double M2_TO_PYEONG = 3.305785;
	public static final long MILESECONDS_OF_ONE_WEEK = 604800000;	// 7*24*60*60*1000
	
	
	
	@Autowired @Qualifier("sellingPriceEntityRepository")
	private EntityRepository<SellingPriceEntity, Integer> sellingPriceRepository;

	@Autowired @Qualifier("sellingRateEntityRepository")
	private EntityRepository<SellingRateEntity, Integer> sellingRateRepository;

//	@Autowired @Qualifier("pyeongSellingPriceRepository")
//	private EntityRepository<PyeongSellingPrice, Integer> pyeongSellingRepository;

	@Autowired @Qualifier("leasePriceEntityRepository")
	private EntityRepository<LeasePriceEntity, Integer> leasePriceRepository;

	@Autowired @Qualifier("leaseRateEntityRepository")
	private EntityRepository<LeaseRateEntity, Integer> leaseRateRepository;

//	@Autowired @Qualifier("pyeongLeasePriceRepository")
//	private EntityRepository<PyeongLeasePrice, Integer> pyeongLeaseRepository;

//	@Autowired @Qualifier("leasePerPriceRepository")
//	private EntityRepository<LeasePerPrice, Integer> leasePerPriceRepository;

//	@Autowired @Qualifier("marketGinSellingRepository")
//	private EntityRepository<MarketGinSelling, Integer> marketGinSellingRepository;
//
//	@Autowired @Qualifier("marketGinLeaseRepository")
//	private EntityRepository<MarketGinLease, Integer> marketGinLeaseRepository;
		
	@Autowired @Qualifier("totalMarketRepository")
	private EntityRepository<TotalMarket, Integer> totalMarketRepository;
	
	@Autowired @Qualifier("earningStatEntityRepository")
	private EntityRepository<EarningStatEntity, Integer> earningStatRepository;

	public void analyzeMarketTimeSeries() {

		logger.debug("call analyzeMarketTimeSeries");
		
		//평당 매매 가격 분석
		logger.debug("begin analyze: pyeong selling price");
		AnalyzeMarket<SellingPriceEntity, SellingRateEntity, PyeongSellingPrice> analyzeSelling = 
				new AnalyzeMarket<SellingPriceEntity, SellingRateEntity, PyeongSellingPrice>(sellingPriceRepository, sellingRateRepository, PyeongSellingPrice.class);
		
		List<PyeongSellingPrice> sellingList = analyzeSelling.analyze();
		logger.debug("end analyze: pyeong selling price. sellingList.size()=" + sellingList.size());
		
		//평당 전세 가격 분석
		logger.debug("begin analyze: pyeong lease price");
		AnalyzeMarket<LeasePriceEntity, LeaseRateEntity, PyeongLeasePrice> analyzeLease = 
				new AnalyzeMarket<LeasePriceEntity, LeaseRateEntity, PyeongLeasePrice>(leasePriceRepository, leaseRateRepository, PyeongLeasePrice.class);
		
		List<PyeongLeasePrice> leaseList = analyzeLease.analyze();
		logger.debug("end analyze: pyeong lease price. leaseList.size()=" + leaseList.size());
		
		//전세율(전세/매매) 분석
		logger.debug("begin analyze: leasePerPricee");
		List<LeasePerPrice> leasePerPriceList = analyzeLeasePerPrice(sellingList, leaseList);
		logger.debug("end analyze: leasePerPrice. leasePerPriceList.size()=" + leasePerPriceList.size());
	
		//지인1
		logger.debug("begin analyze: gin selling rate");
		AnalyzeGin<PyeongSellingPrice, MarketGinSelling> analyzeGinSelling = 
				new AnalyzeGin<PyeongSellingPrice, MarketGinSelling>(sellingList, MarketGinSelling.class);
		
		List<MarketGinSelling> ginSellingList = analyzeGinSelling.analyze();
		logger.debug("end analyze: gin selling rate. ginSellingList.size()=" + ginSellingList.size());
		
		//지인2
		logger.debug("begin analyze: gin lease rate");
		AnalyzeGin<PyeongLeasePrice, MarketGinLease> analyzeGinLease = 
				new AnalyzeGin<PyeongLeasePrice, MarketGinLease>(leaseList, MarketGinLease.class);
		
		List<MarketGinLease> ginLeaseList = analyzeGinLease.analyze();
		logger.debug("end analyze: gin lease rate. ginLeaseList.size()=" + ginLeaseList.size());
		
		List<TotalMarket> resultList = makeTotalMarketEntities(sellingList, leaseList, leasePerPriceList, ginSellingList, ginLeaseList);
		totalMarketRepository.deleteAll();
		totalMarketRepository.save(resultList);
		
	}

	public List<TotalMarket> calcurateRateOfEarning(final List<TotalMarket> _list, int sellTiming) {
		
		Map<String, List<TotalMarket>> mapInput = groupByClassificationMap(_list);
		Map<String, Map<Date, TotalMarket>> mapDateAll = getMapDateAll();
		List<TotalMarket> listResult = stasticEarningRate(mapInput, sellTiming, mapDateAll);

		return listResult;
	}
	
	public List<TotalMarket> stasticEarningRate(final Map<String, List<TotalMarket>> mapInput, int sellTiming, Map<String, Map<Date, TotalMarket>> mapDateAll) {
		List<TotalMarket> listResult = new ArrayList<TotalMarket>();
		
		// 추출된 결과에서 불필요한 정보를 제거하자
		Map<String, List<TotalMarket>> mapTarget = new HashMap<String, List<TotalMarket>>();

		for (Entry<String, List<TotalMarket>> cls : mapInput.entrySet()) {
			List<TotalMarket> listTarget = new ArrayList<TotalMarket>();
			List<TotalMarket> list = cls.getValue();
			if(list==null || list.size()==0) {
				continue;
			}
			Collections.sort(list);

			listTarget.add(list.get(0));
			TotalMarket prev = list.get(0);
			TotalMarket curr;

			for (int i = 1; i < list.size(); i++) {
				curr = list.get(i);

				// 예외조건. 신호가 온 이후 26주간은 무시
				long interval = curr.getDate().getTime() - prev.getDate().getTime();

				if (interval > MILESECONDS_OF_ONE_WEEK * 26) {
					listTarget.add(curr);
					prev = curr;
				}
			}

			mapTarget.put(cls.getKey(), listTarget);
		}

		// 수익률 계산
		for (Entry<String, List<TotalMarket>> cls : mapTarget.entrySet()) {
			List<TotalMarket> list = cls.getValue();
			Map<Date, TotalMarket> mapSearch = mapDateAll.get(cls.getKey());
			for (TotalMarket item : list) {
				Date date = new Date(item.getDate().getTime() + (long)(sellTiming*MILESECONDS_OF_ONE_WEEK));
				TotalMarket toItem = mapSearch.get(date);
				if(toItem != null) {
					item.setValue( (toItem.getSellingPrice()-item.getSellingPrice()) / item.getSellingPrice());
					listResult.add(item);
				}
			}
		}

		return listResult;
	}
	
	public Map<String, Map<Date, TotalMarket>> getMapDateAll() {
		List<TotalMarket> listAll = totalMarketRepository.findAll();
		Map<String, List<TotalMarket>> mapAll = groupByClassificationMap(listAll);
		Map<String, Map<Date, TotalMarket>> mapDateAll = new HashMap<String, Map<Date, TotalMarket>>();
		for (Entry<String, List<TotalMarket>> cls : mapAll.entrySet()) {
			List<TotalMarket> list = cls.getValue();
			Map<Date, TotalMarket> m = new HashMap<Date, TotalMarket>();
			for(TotalMarket item : list) {
				m.put(item.getDate(), item);
			}
			mapDateAll.put(cls.getKey(), m);
		}
		return mapDateAll;
	}
	
//	@SuppressWarnings("unchecked")
//	public Iterable<?> calcurateRateOfEarning(final List<LeasePerPrice> _rateList, final List<MarketGinSelling> _sellingList, final List<MarketGinLease> _leaseList) {
//		List<EarningRate> extractList = null;
//		
//		Map<String, List<LeasePerPrice>> rateMap = null;
//		Map<String, List<MarketGinSelling>> sellingMap = null;
//		Map<String, List<MarketGinLease>> leaseMap = null;
//		
//		// 검색된 지역 전체에 대한 Set 만들기
//		Set<String> classSet = new HashSet<String>();
//		int numOfConditions = 0;
//		if(_rateList != null) {
//			rateMap = groupByClassificationMap(_rateList);
//			classSet.addAll(rateMap.keySet());
//			numOfConditions++;
//		}
//		if(_sellingList != null) {
//			sellingMap = groupByClassificationMap(_sellingList);
//			classSet.addAll(sellingMap.keySet());
//			numOfConditions++;
//		}
//		if(_leaseList != null) {
//			leaseMap = groupByClassificationMap(_leaseList);
//			classSet.addAll(leaseMap.keySet());
//			numOfConditions++;
//		}
//		
//		// resultList 구하기
//		if(numOfConditions==1) {
//			Map<String, List<Market>> map = (Map<String, List<Market>>) ((rateMap!=null) ? rateMap : ((sellingMap!=null) ? sellingMap : leaseMap));
//			extractList = calcurateEarningRate(map);
//			
//		} else if(numOfConditions==2) {
//			if(rateMap==null) {
//				extractList = calcurateEarningRate(sellingMap, leaseMap);
//			}else if(sellingMap==null) {
//				extractList = calcurateEarningRate(rateMap, leaseMap);
//			}else if(leaseMap==null) {
//				extractList = calcurateEarningRate(rateMap, sellingMap);
//			}
//						
//		} else if(numOfConditions==3) {
//			extractList = calcurateEarningRate(rateMap, sellingMap, leaseMap);
//			
//		} else {
//			logger.error("Invalid condition. 수익율 계산 조건이 없음");
//		}
//		
//		//추출된 결과에서 불필요한 정보를 제거하자
//		List<EarningRate> resultList = new ArrayList<EarningRate>();
//		Map<String, List<EarningRate>> map = groupByClassificationMap(resultList);
//		
//		for(Entry<String, List<EarningRate>> cls : map.entrySet()) {
//			List<EarningRate> list = cls.getValue();
//			Collections.sort(list);
//			
//			EarningRate curr=null;
//			EarningRate prev=list.get(0);
//			for(int i=1; i<list.size(); i++) {
//				curr = list.get(i);
//				//예외조건. 신호가 온 이후 연속해서 신호가 올 경우는 넣지 말자
//				double interval = curr.getDate().getTime()-prev.getDate().getTime();
//				
//				if(interval<=MILESECONDS_OF_ONE_WEEK) {
//					prev = curr;
//					continue;
//				}
//				resultList.add(curr);
//			}
//		}
//		
//		return resultList;
//	}
	public List<TotalMarket> getMarketList(List<TotalMarket> allList, double rate, double selling, double lease) {
		List<TotalMarket> listResult = new ArrayList<TotalMarket>();
		
		if(rate > 0.0 && selling > 0.0  && lease > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinRate() != null && item.getGinRate() >= rate
						&& item.getGinSelling() != null && item.getGinSelling() >= selling
						&& item.getGinLease() != null && item.getGinLease() >= lease) {
					listResult.add(item);
				}
			}
		} else if(rate > 0.0 && selling > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinRate() != null && item.getGinRate() >= rate
						&& item.getGinSelling() != null && item.getGinSelling() >= selling) {
					listResult.add(item);
				}
			}
		} else if(rate > 0.0 && lease > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinRate() != null && item.getGinRate() >= rate
						&& item.getGinLease() != null && item.getGinLease() >= lease) {
					listResult.add(item);
				}
			}
		} else if(selling > 0.0  && lease > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinSelling() != null && item.getGinSelling() >= selling
						&& item.getGinLease() != null && item.getGinLease() >= lease) {
					listResult.add(item);
				}
			}
		} else if(rate > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinRate() != null && item.getGinRate() >= rate) {
					listResult.add(item);
				}
			}
		} else if(selling > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinSelling() != null && item.getGinSelling() >= selling) {
					listResult.add(item);
				}
			}
		} else if(lease > 0.0) {
			for(TotalMarket item : allList) {
				if(item.getGinLease() != null && item.getGinLease() >= lease) {
					listResult.add(item);
				}
			}
		}
		
		return listResult;
	}
	
	public void processBestCase() {
		List<EarningStat> earningStatList = new ArrayList<EarningStat>();
		
		List<TotalMarket> listAll = totalMarketRepository.findAll();
		Map<String, List<TotalMarket>> mapAll = groupByClassificationMap(listAll);
		Map<String, Map<Date, TotalMarket>> mapDateAll = new HashMap<String, Map<Date, TotalMarket>>();
		for (Entry<String, List<TotalMarket>> cls : mapAll.entrySet()) {
			List<TotalMarket> list = cls.getValue();
			Map<Date, TotalMarket> m = new HashMap<Date, TotalMarket>();
			for(TotalMarket item : list) {
				m.put(item.getDate(), item);
			}
			mapDateAll.put(cls.getKey(), m);
		}
		
		List<EarningStat> lists = new ArrayList<EarningStat>(); 
		List<TotalMarket> totalMarketList = new ArrayList<TotalMarket>(listAll);
		List<TotalMarket> prevMarketListStep1, prevMarketListStep2, prevMarketListStep3;

		prevMarketListStep1 = null;
		for(double rate=(double) 60.0; rate<=80.0; rate+=1.0) {
			logger.debug("processBestCase: rate="+rate+", selling=NOT_DEFINE, lease=NOT_DEFINE");
			
			List<TotalMarket> marketListStep1 = getMarketList(totalMarketList, rate, 0.0, 0.0);
			if(marketListStep1.size() == 0) {
				break;
			}
			if(prevMarketListStep1 != null && marketListStep1.equals(prevMarketListStep1)) {
				continue;
			}

			EarningStat earningStatRate = new EarningStat();
			earningStatRate.setRate(rate);
			earningStatRate.setTotalMarketList(marketListStep1);
			lists.add(earningStatRate);

			prevMarketListStep1 = marketListStep1;
			
			prevMarketListStep2 = null;
			for(double selling = 3.0; selling<=7.0; selling+=0.1) {
				logger.debug("processBestCase: rate="+rate+", selling="+selling+", lease=NOT_DEFINE");
				
				List<TotalMarket> marketListStep2 = getMarketList(marketListStep1, 0.0, selling, 0.0);
				if(marketListStep2.size() == 0) {
					break;
				}
				if(prevMarketListStep2 != null && marketListStep2.equals(prevMarketListStep2)) {
					continue;
				}
				
				EarningStat earningStatSelling = new EarningStat();
				earningStatSelling.setRate(rate);
				earningStatSelling.setSelling(selling);
				earningStatSelling.setTotalMarketList(marketListStep2);
				lists.add(earningStatSelling);
				
				prevMarketListStep2 = marketListStep2;
				
				prevMarketListStep3 = null;
				for(double lease = 3.0; lease<=7.0; lease+=0.1) {
					logger.debug("processBestCase: rate="+rate+", selling="+selling+", lease="+lease);
					
					List<TotalMarket> marketListStep3 = getMarketList(marketListStep2, 0.0, 0.0, lease);
					if(marketListStep3.size() == 0) {
						break;
					}
					if(prevMarketListStep3 != null && marketListStep3.equals(prevMarketListStep3)) {
						continue;
					}
					
					EarningStat earningStatLease = new EarningStat();
					earningStatLease.setRate(rate);
					earningStatLease.setSelling(selling);
					earningStatLease.setLease(lease);
					earningStatLease.setTotalMarketList(marketListStep3);
					lists.add(earningStatLease);
					
					prevMarketListStep3 = marketListStep3;
				}
			}
		}


		prevMarketListStep1 = null;
		for(double selling = 3.0; selling<=7.0; selling+=0.1) {
			logger.debug("processBestCase: rate=NOT_DEFINE, selling="+selling+", lease=NOT_DEFINE");
			
			List<TotalMarket> marketListStep1 = getMarketList(totalMarketList, 0.0, selling, 0.0);
			if(marketListStep1.size() == 0) {
				break;
			}
			if(prevMarketListStep1 != null && marketListStep1.equals(prevMarketListStep1)) {
				continue;
			}
			
			EarningStat earningStatSelling = new EarningStat();
			earningStatSelling.setSelling(selling);
			earningStatSelling.setTotalMarketList(marketListStep1);
			lists.add(earningStatSelling);
			
			prevMarketListStep1 = marketListStep1;
			
			prevMarketListStep2 = null;
			for(double lease = 3.0; lease<=7.0; lease+=0.1) {
				logger.debug("processBestCase: rate=NOT_DEFINE, selling="+selling+", lease="+lease);
				
				List<TotalMarket> marketListStep2 = getMarketList(marketListStep1, 0.0, 0.0, lease);
				if(marketListStep2.size() == 0) {
					break;
				}
				if(prevMarketListStep2 != null && marketListStep2.equals(prevMarketListStep2)) {
					continue;
				}
				
				EarningStat earningStatLease = new EarningStat();
				earningStatLease.setSelling(selling);
				earningStatLease.setLease(lease);
				earningStatLease.setTotalMarketList(marketListStep2);
				lists.add(earningStatLease);

				prevMarketListStep2 = marketListStep2;
			}
		}
		
		prevMarketListStep1 = null;
		for(double lease = 3.0; lease<=7.0; lease+=0.1) {
			logger.debug("processBestCase: rate=NOT_DEFINE, selling=NOT_DEFINE, lease="+lease);
			
			List<TotalMarket> marketListStep1 = getMarketList(totalMarketList, 0.0, 0.0, lease);
			if(marketListStep1.size() == 0) {
				break;
			}
			if(prevMarketListStep1 != null && marketListStep1.equals(prevMarketListStep1)) {
				continue;
			}
			
			EarningStat earningStatLease = new EarningStat();
			earningStatLease.setLease(lease);
			earningStatLease.setTotalMarketList(marketListStep1);
			lists.add(earningStatLease);
			
			prevMarketListStep1 = marketListStep1;
			
			prevMarketListStep2 = null;
			for(double rate=(double) 60.0; rate<=80.0; rate+=1.0) {
				logger.debug("processBestCase: rate="+rate+", selling=NOT_DEFINE, lease="+lease);
				
				List<TotalMarket> marketListStep2 = getMarketList(marketListStep1, rate, 0.0, 0.0);
				if(totalMarketList.size() == 0) {
					break;
				}
				if(prevMarketListStep2 != null && marketListStep2.equals(prevMarketListStep2)) {
					continue;
				}
				
				EarningStat earningStatRate = new EarningStat();
				earningStatRate.setRate(rate);
				earningStatRate.setLease(lease);
				earningStatRate.setTotalMarketList(marketListStep2);
				lists.add(earningStatRate);
				
				prevMarketListStep2 = marketListStep2;
			}
		}
		
		
		earningStatRepository.deleteAll();

		for (EarningStat earningStat : lists) {
			logger.debug("processBestCase: getEarningStat - " + earningStat.toString());
			List<EarningStat> results = getEarningStat(earningStat, mapDateAll);
			if(results != null) {
				earningStatList.addAll(results);
//				List<EarningStatEntity> entityList = convert(results);
//				earningStatRepository.save(entityList);
			}
		}	
		
		// 불필요한 정보 삭제
		Collections.sort(earningStatList);
		List<EarningStat> filteredList = new ArrayList<EarningStat>();
		int size = earningStatList.size();
		EarningStat cur, prev;
		prev = earningStatList.get(0);
		filteredList.add(prev);
		for(int i=1; i<size; i++) {
			cur = earningStatList.get(i);
			if(prev.getAveragePerYear() == cur.getAveragePerYear()
					&& prev.getAverageTotal() == cur.getAverageTotal()) {
				if(prev.getRate() <= cur.getRate()
						|| prev.getSelling() <= cur.getSelling()
						|| prev.getLease() <= cur.getLease()) {
					continue;
				}
			}
			
			filteredList.add(cur);
			prev = cur;
		}
		
		logger.debug("processBestCase: start to save earningStatRepository");
		List<EarningStatEntity> entityList = convert(filteredList);
		earningStatRepository.deleteAll();
		earningStatRepository.save(entityList);
		logger.debug("processBestCase: end to save earningStatRepository");
		
	}
	
	private List<EarningStat> getEarningStat(EarningStat inputStat, Map<String, Map<Date, TotalMarket>> mapDateAll) {
		List<EarningStat> resultList = new ArrayList<EarningStat>();

		if(inputStat.getTotalMarketList()==null || inputStat.getTotalMarketList().size() <= 0) {
			return null;
		}
		
		for(TotalMarket e : inputStat.getTotalMarketList()) {
			if(e.getSellingPrice()==null) {
				logger.debug(e.toString());
			}
		}
		// 추출된 결과에서 불필요한 정보를 제거하자
		Map<String, List<TotalMarket>> mapInput = groupByClassificationMap(inputStat.getTotalMarketList());

		for(int i=52; i<156; i+=4) {
			List<TotalMarket> list = stasticEarningRate(mapInput, i, mapDateAll);
			
			if(list==null || list.size()==0) {
				break;
			}
			
			Collections.sort(list, Market.getValueSorter());
			Double averageTotal = 0.0;
			int numOfIncrease = 0;
			int numOfDecrease = 0;
			for (TotalMarket item : list) {
				Double value = item.getValue(); 
				averageTotal += value;
				if (value > 0.0) {
					numOfIncrease++;
				}else if (value < 0.0) {
					numOfDecrease++;
				}
			}
			
			averageTotal /= (double) list.size();
			Double averagePerYear = averageTotal / ((double) i / (365.0/7.0));
	

			EarningStat earningStat = new EarningStat();
			earningStat.setCount(list.size());
			earningStat.setNumOfIncrease(numOfIncrease);
			earningStat.setNumOfDecrease(numOfDecrease);
			earningStat.setAverageTotal(averageTotal);
			earningStat.setAveragePerYear(averagePerYear);
			earningStat.setTotalMarketList(list);
			
			earningStat.setRate(inputStat.getRate());
			earningStat.setSelling(inputStat.getSelling());
			earningStat.setLease(inputStat.getLease());
			earningStat.setInterval(i);
			resultList.add(earningStat);
		}
		
		return resultList;
	}
	
	private List<LeasePerPrice> analyzeLeasePerPrice(List<PyeongSellingPrice> _sellingList, List<PyeongLeasePrice> _leaseList) {
		List<LeasePerPrice> resultLists = new ArrayList<LeasePerPrice>();
		
		Map<String, List<PyeongSellingPrice>> sellingMap = groupByClassificationMap(_sellingList);
		Map<String, List<PyeongLeasePrice>> leaseMap = groupByClassificationMap(_leaseList);
		
		for(Entry<String, List<PyeongSellingPrice>> cls : sellingMap.entrySet()) {
			List<PyeongSellingPrice> sellingList = cls.getValue();
			List<PyeongLeasePrice> leaseList = leaseMap.get(cls.getKey());
			
			Collections.sort(sellingList, Collections.reverseOrder());
			Collections.sort(leaseList, Collections.reverseOrder());
			
			
			List<LeasePerPrice> resultList = (List<LeasePerPrice>) new ArrayList<LeasePerPrice>();
			
			int sellingSize = sellingList.size();
			int leaseSize = leaseList.size();
			for(int i=0, j=0; i<sellingSize || j<leaseSize; i++, j++) {
				PyeongSellingPrice selling = sellingList.get(i);
				PyeongLeasePrice lease = leaseList.get(j);
				
				if(selling.getDate().equals(lease.getDate())) {
					LeasePerPrice result = new LeasePerPrice();
					result.setMarket(selling);
					Double value = (lease.getValue()!=null && selling.getValue()!=null) ? (lease.getValue()/selling.getValue()) : null;
					result.setValue(value);
					
					resultList.add(result);

				} else {
					logger.error("Invalid Data: 지역=" + selling.getClassification() + ", 매매날짜=" + selling.getDate() + ", 전세날짜=" + lease.getDate());
					
					if(selling.getDate().before(lease.getDate())) {
						i--;
					} else {
						j--;
					}
				}
			}
			
			resultLists.addAll(resultList);
		}
		
		return resultLists;
	}
	
	private List<TotalMarket> makeTotalMarketEntities(List<PyeongSellingPrice> sellingList, List<PyeongLeasePrice> leaseList, 
														List<LeasePerPrice> leasePerPriceList, List<MarketGinSelling> ginSellingList, List<MarketGinLease> ginLeaseList) 
	{
		List<TotalMarket> resultList = new ArrayList<TotalMarket>();
		Map<String, List<PyeongSellingPrice>> mapSelling = groupByClassificationMap(sellingList);
		Map<String, List<PyeongLeasePrice>> mapLease = groupByClassificationMap(leaseList);
		Map<String, List<LeasePerPrice>> mapRate = groupByClassificationMap(leasePerPriceList);
		Map<String, List<MarketGinSelling>> mapGinSelling = groupByClassificationMap(ginSellingList);
		Map<String, List<MarketGinLease>> mapGinLease = groupByClassificationMap(ginLeaseList);
		
		// validation check
		Set<String> setSelling = mapSelling.keySet();
		Set<String> setLease = mapLease.keySet();
		Set<String> setRate = mapRate.keySet();
//		Set<String> setGinSelling = mapGinSelling.keySet();
//		Set<String> setGinLease = mapGinLease.keySet();
		if(!setSelling.equals(setLease) 
				|| !setSelling.equals(setRate)){
//				|| !setSelling.equals(setGinSelling)
//				|| !setSelling.equals(setGinLease)) {
			logger.error("ERROR : invalid classification");
			return null;
		}
		
		for(String key : setSelling) {
			Map<Date, TotalMarket> resultMap = new HashMap<Date, TotalMarket>();
			List<PyeongSellingPrice> sellings = mapSelling.get(key);
			Collections.sort(sellings, Collections.reverseOrder());
			for(PyeongSellingPrice item : sellings) {
				TotalMarket newItem = new TotalMarket();
				newItem.setMarket(item);
				newItem.setSellingPrice(item.getValue());
				
				resultMap.put(newItem.getDate(), newItem);
			}
			
			List<PyeongLeasePrice> leases = mapLease.get(key);
			if(leases != null) {
				for(PyeongLeasePrice item : leases) {
					TotalMarket newItem = resultMap.get(item.getDate());
					newItem.setLeasePrice(item.getValue());
				}
			}
			
			List<LeasePerPrice> rates = mapRate.get(key);
			if(rates != null) {
				for(LeasePerPrice item : rates) {
					TotalMarket newItem = resultMap.get(item.getDate());
					Double value = (item.getValue()!=null) ? item.getValue()*100.0 : null;
					newItem.setGinRate(value);
				}
			}
			
			List<MarketGinSelling> ginSellings = mapGinSelling.get(key);
			if(ginSellings != null) {
				for(MarketGinSelling item : ginSellings) {
					TotalMarket newItem = resultMap.get(item.getDate());
					newItem.setGinSelling(item.getValue());
				}
			}
			
			List<MarketGinLease> ginLeases = mapGinLease.get(key);
			if(ginLeases != null) {
				for(MarketGinLease item : ginLeases) {
					TotalMarket newItem = resultMap.get(item.getDate());
					newItem.setGinLease(item.getValue());
				}
			}
			
			resultList.addAll(resultMap.values());

		}
		
		return resultList;
	}
	
	
	private <Q extends Market> Map<String, List<Q>> groupByClassificationMap(List<Q> list) {
		Map<String, List<Q>> map = new HashMap<String, List<Q>>();
		for(Q e : list) {
			List<Q> subList;
			String key = e.getClassification();
			if(map.containsKey(key)) {
				subList = (List<Q>) map.get(key);
			} else {
				subList = new ArrayList<Q>();
				map.put(key, subList);
			}
			subList.add(e);
		}
		return map;
	}
	
//	private <T extends Market> List<EarningRate> calcurateEarningRate(Map<String, List<T>> map) {
//		List<EarningRate> resultList = new ArrayList<EarningRate>();
//		
//		for(Entry<String, List<T>> cls : map.entrySet()) {
//			List<T> list = cls.getValue();
//			Collections.sort(list);
//			Market entity;
//			for(int i=0; i<list.size(); i++) {
//				entity = list.get(i);
//				
////				//예외조건. 신호가 온 이후 연속해서 신호가 올 경우는 넣지 말자
////				if(entity!=null && prevEntity!=null && (entity.getDate().getTime()-prevEntity.getDate().getTime())==MILESECONDS_OF_ONE_WEEK) {
////					prevEntity = entity;
////					continue;
////				}
//				
//				EarningRate newItem = new EarningRate();
//				newItem.setClassification(entity.getClassification());
//				newItem.setDate(entity.getDate());
//				resultList.add(newItem);
//			}
//		}
//		
//		return resultList;
//	}
	
//	@SuppressWarnings("unchecked")
//	private <T extends Market, U extends Market> List<EarningRate> calcurateEarningRate(Map<String, List<T>> map1, Map<String, List<U>> map2) {
//		List<EarningRate> resultList = new ArrayList<EarningRate>();
//		
//		Set<String> classSet = new HashSet<String>();
//		classSet.addAll(map1.keySet());
//		classSet.addAll(map2.keySet());
//		
//		for(String key : classSet) {
//			List<Market> list1 = (List<Market>) map1.get(key);
//			List<Market> list2 = (List<Market>) map2.get(key);
//			
//			if(list1==null || list2==null) {
//				continue;
//			}
//			
//			Collections.sort(list1);
//			Collections.sort(list2);
//			
//			Market entity1, entity2;
//			int size1=list1.size();
//			int size2=list2.size();
//			for(int i=0,j=0; i<size1 && j<size2; ) {
//				entity1 = list1.get(i);
//				entity2 = list2.get(j);
//				
//				if(entity1.getDate().equals(entity2.getDate())) {
//					EarningRate newItem = new EarningRate();
//					newItem.setClassification(entity1.getClassification());
//					newItem.setDate(entity1.getDate());
//					resultList.add(newItem);
//					i++; j++;
//					
//				}else if(entity1.getDate().after(entity2.getDate())) {
//					j++;
//				}else {
//					i++;
//				}					
//			}
//		}
//		
//		return resultList;
//	}
//	
//	private <T extends Market, U extends Market, V extends Market> List<EarningRate> calcurateEarningRate(Map<String, List<T>> map1, Map<String, List<U>> map2, Map<String, List<V>> map3) {
//		List<EarningRate> earningRateList = calcurateEarningRate(map1, map2);
//		Map<String, List<EarningRate>> earningRateMap = groupByClassificationMap(earningRateList);
//		
//		List<EarningRate> resultList = calcurateEarningRate(earningRateMap, map3);
//		return resultList;
//	}
	
	class AnalyzeMarket<T extends Market, U extends Market, V extends Market> {
		private EntityRepository<T, Integer> priceRepository;
		private EntityRepository<U, Integer> rateRepository;
		private Class<V> clazz;
		
		public AnalyzeMarket(EntityRepository<T, Integer> priceRepository, EntityRepository<U, Integer> rateRepository, Class<V> clazz) {
			this.priceRepository = priceRepository; 
			this.rateRepository = rateRepository;
			this.clazz = clazz;
		}
		
		private V newInstance()  {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public List<V> analyze() {
			List<T> priceEntities = priceRepository.findAll();
			List<U> rateEntities = rateRepository.findAll();
			
			Map<String, List<T>> priceMap = groupByClassificationMap(priceEntities);
			Map<String, List<U>> rateMap = groupByClassificationMap(rateEntities);
			
			List<V> resultLists = (List<V>) new ArrayList<V>();
			
			for(Entry<String, List<U>> cls : rateMap.entrySet()) {
			
				List<V> resultList = (List<V>) new ArrayList<V>();
				List<U> rateList = cls.getValue();
				List<T> priceList = priceMap.get(cls.getKey());
				if(priceList == null) {
					logger.error("priceList empty. please check the rootine of importing excel file. 지역=" + cls.getKey());
					continue;
				}
				
				Collections.sort(rateList, Collections.reverseOrder());
				Collections.sort(priceList, Collections.reverseOrder());

				// m2당 가격을 평당 가격으로 변환 
				for(T e : priceList) {
					if(e.getValue()==null) {
						continue;
					}
					
					V result = (V) newInstance();
					result.setMarket(e);
					result.setValue(e.getValue()*M2_TO_PYEONG);

					// 날짜를 증감 마지막 주 날짜로 변경
					// ** 주의 ** 값 변환을 위해 원본 엑셀에서 가격 날짜를 +1월 추가헀음 
					Date date = null;
					for(U rateEntity : rateList) {
						if(e.getDate().after(rateEntity.getDate())) {
							Calendar calPrice = Calendar.getInstance();
							Calendar calRate = Calendar.getInstance();
							calPrice.setTime(e.getDate());
							calRate.setTime(rateEntity.getDate());
							
							date = rateEntity.getDate();
							break;
						}
					}
					result.setDate(date);
					
					resultList.add(result);
				}
				
				Collections.sort(resultList, Collections.reverseOrder());
				
				if(resultList.size() <= 0) {
					logger.error("resultList empty. please check the rootine of importing excel file. 지역=" + cls.getKey());
					continue;
				}
				
				// 위에서 변환한 평당가격과 증강을 이용하여 매주 평당가격 계산 
				int rateIndex = 0;
				int resultIndex = 0;
				int rateSize = rateList.size();
				int resultSize = resultList.size();
				Stack<U> stack = new Stack<U>();
				while(true) {
					if(rateIndex >= rateSize) {
						break;
					}
					if(resultIndex >= resultSize) {
						Double curPrice = resultList.get(resultIndex-1).getValue();
						if(curPrice == null) {
							stack.clear();
							break;
						}
						
						for(int i=rateIndex; i<rateSize; i++) {
							U rateItem = rateList.get(i);
							V newItem = newInstance();
							newItem.setMarket(rateItem);
							curPrice = (curPrice!=null && rateItem.getValue()!=null) ? (curPrice / (rateItem.getValue()/100.0+1)) : null;
							newItem.setValue(curPrice);
							
							resultList.add(newItem);
						}
						
						break;
					}
					
					U rateEntity = rateList.get(rateIndex);
					V resultEntity = resultList.get(resultIndex);
					
					if(rateEntity.getDate() == resultEntity.getDate()) {
						while(stack.size() > 0) {
							Double curPrice = resultEntity.getValue();
							if(curPrice == null) {
								stack.clear();
								break;
							}
							
							U rateItem = stack.pop();
							
							V newItem = newInstance();
							newItem.setMarket(rateItem);
							curPrice = (curPrice!=null && rateItem.getValue()!=null) ? (curPrice + (curPrice*rateItem.getValue()/100.0)) : null;
							newItem.setValue(curPrice);
							
							resultList.add(newItem);
						}
						
						resultIndex++;
						rateIndex++;
						
					} else if(rateEntity.getDate().after(resultEntity.getDate())) {
						stack.push(rateEntity);
						rateIndex++;
					} 
				
				}
				
				Collections.sort(resultList, Collections.reverseOrder());
				
				int size = resultList.size();
				for(int i=0; i<size-1; i++) {
					V curItem = resultList.get(i);
					V prevItem = resultList.get(i+1);
					
					if(curItem.getValue()==null || prevItem.getValue()==null) {
						break;
					}
					
					Long interval = curItem.getDate().getTime() - prevItem.getDate().getTime(); 
					if(interval == MILESECONDS_OF_ONE_WEEK) {
						continue;
					}

					logger.warn("Invalid value : date inteval is not 1 weeks. 지역=" + curItem.getClassification() + ", 날짜=" 
								+ curItem.getDate() + ", interval=" + interval/1000/60/60/24 + "day(s)");
					
					if(interval % MILESECONDS_OF_ONE_WEEK == 0) {
						double increaseValue = (curItem.getValue() - prevItem.getValue()) / (double)(interval / MILESECONDS_OF_ONE_WEEK); 
						while (interval > 0) {
							interval -= MILESECONDS_OF_ONE_WEEK;
							V newItem = newInstance();
							newItem.setMarket(prevItem);
							newItem.setDate(new Date(prevItem.getDate().getTime() + interval));
							Double value = prevItem.getValue()+increaseValue*(double)(interval/MILESECONDS_OF_ONE_WEEK);
							newItem.setValue(value);
							
							resultList.add(newItem);
						}
					}
					else {
						logger.error("Invalid value : cannot handling data. 지역=" + curItem.getClassification() + ", 날짜=" + curItem.getDate());
					}
				}
				resultLists.addAll(resultList);
			}
			
			return resultLists;
		}
		
	}
	
	class AnalyzeGin<T extends Market, U extends MarketGin> {
		private List<T> entities;
		private Class<U> clazz;
		
		public AnalyzeGin(List<T> entities, Class<U> clazz) {
			this.entities = entities;
			this.clazz = clazz;
		}
		
		private U newInstance()  {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public List<U> analyze() {
			List<U> resultLists = new ArrayList<U>();
			
			Map<String, List<T>> map = groupByClassificationMap(entities);
			
			for(Entry<String, List<T>> cls : map.entrySet()) {
				List<T> list = cls.getValue();
				Collections.sort(list, Collections.reverseOrder());
				
				
				List<U> resultList = new ArrayList<U>();
				
				int size = list.size();
				if(size - INTERVAL_FOR_GIN_VALUE <= 0) {
					logger.warn("invalid input value. 지역=" + list.get(0).getClassification() + ", size=" + size);
				}
				
				for(int i=0; i<size-INTERVAL_FOR_GIN_VALUE ; i++) {
					T to = list.get(i);
					T from = list.get(i+INTERVAL_FOR_GIN_VALUE);
					
					if(to.getValue()==null || from.getValue()==null) {
						continue;
					}
					
					U newItem = newInstance();
					newItem.setClassification(to.getClassification());
					newItem.setCode(to.getCode());
					newItem.setDate(to.getDate());
					newItem.setFromDate(from.getDate());
					
					Double value = (to.getValue()-from.getValue()) / from.getValue()*100.0;
					newItem.setValue(value);
					
					resultList.add(newItem);
				}
				
				resultLists.addAll(resultList);
			}
			
			return resultLists;
		}
		
	}
	
	private EarningStatEntity convert(EarningStat item) {
		EarningStatEntity result = new EarningStatEntity();
		result.setAveragePerYear(item.getAveragePerYear());
		result.setAverageTotal(item.getAverageTotal());
		result.setInterval(item.getInterval());
		result.setLease(item.getLease());
		result.setCount(item.getCount());
		result.setNumOfDecrease(item.getNumOfDecrease());
		result.setNumOfIncrease(item.getNumOfIncrease());
		result.setRate(item.getRate());
		result.setSelling(item.getSelling());
		
		return result;
	}
	
	private List<EarningStatEntity> convert(List<EarningStat> list) {
		List<EarningStatEntity> result = new ArrayList<EarningStatEntity>();
		for(EarningStat item : list) {
			EarningStatEntity entity = convert(item);
			result.add(entity);
		}
			
		return result;
	}

}
