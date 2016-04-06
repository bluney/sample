package bluney.sample.sample.service.market;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.u2ware.springfield.repository.EntityRepository;

import bluney.sample.sample.customtype.market.Market;
import bluney.sample.sample.domain.lease.PyeongLeasePrice;
import bluney.sample.sample.domain.lease.price.LeasePriceEntity;
import bluney.sample.sample.domain.lease.rate.LeaseRateEntity;
import bluney.sample.sample.domain.market.LeasePerPrice;
import bluney.sample.sample.domain.selling.PyeongSellingPrice;
import bluney.sample.sample.domain.selling.price.SellingPriceEntity;
import bluney.sample.sample.domain.selling.rate.SellingRateEntity;

@Service("marketService")
public class MarketService {

	private static final Logger logger = LoggerFactory.getLogger(MarketService.class);	
	public static final double M2_TO_PYEONG = 3.305785;
	public static final long MILESECONDS_OF_ONE_WEEK = 604800000;	// 7*24*60*60*1000
	
	
	@Autowired @Qualifier("sellingPriceEntityRepository")
	private EntityRepository<SellingPriceEntity, Integer> sellingPriceRepository;

	@Autowired @Qualifier("sellingRateEntityRepository")
	private EntityRepository<SellingRateEntity, Integer> sellingRateRepository;

	@Autowired @Qualifier("pyeongSellingPriceRepository")
	private EntityRepository<PyeongSellingPrice, Integer> pyeongSellingRepository;

	@Autowired @Qualifier("leasePriceEntityRepository")
	private EntityRepository<LeasePriceEntity, Integer> leasePriceRepository;

	@Autowired @Qualifier("leaseRateEntityRepository")
	private EntityRepository<LeaseRateEntity, Integer> leaseRateRepository;

	@Autowired @Qualifier("pyeongLeasePriceRepository")
	private EntityRepository<PyeongLeasePrice, Integer> pyeongLeaseRepository;

	@Autowired @Qualifier("leasePerPriceRepository")
	private EntityRepository<LeasePerPrice, Integer> leasePerPriceRepository;
		
	public void analyzeMarketTimeSeries() {

		//평당 매매 가격 분석
		AnalyzeMarket<SellingPriceEntity, SellingRateEntity, PyeongSellingPrice> analyzeSelling = 
				new AnalyzeMarket<SellingPriceEntity, SellingRateEntity, PyeongSellingPrice>(sellingPriceRepository, sellingRateRepository, PyeongSellingPrice.class);
		
		List<PyeongSellingPrice> sellingList = analyzeSelling.analyze();
		pyeongSellingRepository.deleteAll();
		pyeongSellingRepository.save(sellingList);
		

		//평당 전세 가격 분석
		AnalyzeMarket<LeasePriceEntity, LeaseRateEntity, PyeongLeasePrice> analyzeLease = 
				new AnalyzeMarket<LeasePriceEntity, LeaseRateEntity, PyeongLeasePrice>(leasePriceRepository, leaseRateRepository, PyeongLeasePrice.class);
		
		List<PyeongLeasePrice> leaseList = analyzeLease.analyze();
		pyeongLeaseRepository.deleteAll();
		pyeongLeaseRepository.save(leaseList);
		
		
		//전세율(전세/매매) 분석
		List<LeasePerPrice> leasePerPriceList = analyzeLeasePerPrice(sellingList, leaseList);
		leasePerPriceRepository.deleteAll();
		leasePerPriceRepository.save(leasePerPriceList);
		
		
		
		
//		List<SellingPriceEntity> priceEntities = sellingPriceRepository.findAll();
//		List<SellingRateEntity> rateEntities = sellingRateRepository.findAll();
//		
//		Map<String, List<SellingPriceEntity>> priceMap = groupByClassificationMap(priceEntities);
//		Map<String, List<SellingRateEntity>> rateMap = groupByClassificationMap(rateEntities);
//		
//		Map<String, List<PyeongSellingPrice>> pyeongSellingPriceMap = new HashMap<String, List<PyeongSellingPrice>>();
//		
//		for(Entry<String, List<SellingRateEntity>> cls : rateMap.entrySet()) {
//		
//			List<PyeongSellingPrice> resultList = new ArrayList<PyeongSellingPrice>();
//			List<SellingRateEntity> rateList = cls.getValue();
//			List<SellingPriceEntity> priceList = priceMap.get(cls.getKey());
//			Collections.sort(rateList, Collections.reverseOrder());
//			Collections.sort(priceList, Collections.reverseOrder());
//			
//			for(SellingPriceEntity e : priceList) {
//				PyeongSellingPrice result = new PyeongSellingPrice();
//				result.setClassification(e.getClassification());
//				result.setCode(e.getCode());
//				result.setValue(e.getValue()*M2_TO_PYEONG);
//
//				Date date = null;
//				for(SellingRateEntity rateEntity : rateList) {
//					if(e.getDate().after(rateEntity.getDate())) {
//						Calendar calPrice = Calendar.getInstance();
//						Calendar calRate = Calendar.getInstance();
//						calPrice.setTime(e.getDate());
//						calRate.setTime(rateEntity.getDate());
//						
//						date = rateEntity.getDate();
//						break;
//					}
//				}
//				result.setDate(date);
//				
//				resultList.add(result);
//			}
//			
//			Collections.sort(resultList, Collections.reverseOrder());
//			
//			
//			int rateIndex=0, resultIndex=0;
//			int rateSize = rateList.size();
//			int resultSize = resultList.size();
//			Stack<SellingRateEntity> stack = new Stack<SellingRateEntity>();
//			while(true) {
//				if(rateIndex >= rateSize) {
//					break;
//				}
//				if(resultIndex >= resultSize) {
//					Double curPrice = resultList.get(resultIndex-1).getValue();
//					
//					for(int i=rateIndex; i<rateSize; i++) {
//						SellingRateEntity rateItem = rateList.get(i);
//						PyeongSellingPrice newItem = new PyeongSellingPrice();
//						newItem.setClassification(rateItem.getClassification());
//						newItem.setCode(rateItem.getCode());
//						newItem.setDate(rateItem.getDate());
//						curPrice = curPrice / (rateItem.getValue() / 100.0 + 1);
//						newItem.setValue(curPrice);
//						
//						resultList.add(newItem);
//					}
//					
//					break;
//				}
//				
//				SellingRateEntity rateEntity = rateList.get(rateIndex);
//				PyeongSellingPrice resultEntity = resultList.get(resultIndex);
//				
//				if(rateEntity.getDate() == resultEntity.getDate()) {
//					Double curPrice = resultEntity.getValue();
//
//					while(stack.size() > 0) {
//						SellingRateEntity rateItem = stack.pop();
//						
//						curPrice = curPrice + (curPrice * rateItem.getValue() / 100.0);
//						PyeongSellingPrice newItem = new PyeongSellingPrice();
//						newItem.setClassification(rateItem.getClassification());
//						newItem.setCode(rateItem.getCode());
//						newItem.setDate(rateItem.getDate());
//						newItem.setValue(curPrice);
//						
//						resultList.add(newItem);
//					}
//					
//					resultIndex++;
//					rateIndex++;
//					
//				} else if(rateEntity.getDate().after(resultEntity.getDate())) {
//					stack.push(rateEntity);
//					rateIndex++;
//					
//				} 
//			
//			}
//			
//			Collections.sort(resultList, Collections.reverseOrder());
//			pyeongSellingPriceMap.put(cls.getKey(), resultList);
//		}

	}

	private List<LeasePerPrice> analyzeLeasePerPrice(List<PyeongSellingPrice> _sellingList, List<PyeongLeasePrice> _leaseList) {
		List<LeasePerPrice> resultLists = new ArrayList<LeasePerPrice>();
		
		Map<String, List<PyeongSellingPrice>> sellingMap = groupByClassificationMap(_sellingList);
		Map<String, List<PyeongLeasePrice>> leaseMap = groupByClassificationMap(_leaseList);
		
		for(Entry<String, List<PyeongSellingPrice>> cls : sellingMap.entrySet()) {
			List<LeasePerPrice> resultList = (List<LeasePerPrice>) new ArrayList<LeasePerPrice>();
			List<PyeongSellingPrice> sellingList = cls.getValue();
			List<PyeongLeasePrice> leaseList = leaseMap.get(cls.getKey());
			
			Collections.sort(sellingList, Collections.reverseOrder());
			Collections.sort(leaseList, Collections.reverseOrder());
			
			int sellingSize = sellingList.size();
			int leaseSize = leaseList.size();
			for(int i=0, j=0; i<sellingSize || j<leaseSize; i++, j++) {
				PyeongSellingPrice selling = sellingList.get(i);
				PyeongLeasePrice lease = leaseList.get(j);
				
				if(selling.getDate().equals(lease.getDate())) {
					LeasePerPrice result = new LeasePerPrice();
					result.setMarket(selling);
					result.setValue(lease.getValue()/selling.getValue());
					
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
				Collections.sort(rateList, Collections.reverseOrder());
				Collections.sort(priceList, Collections.reverseOrder());

				// m2당 가격을 평당 가격으로 변환 
				for(T e : priceList) {
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
						
						for(int i=rateIndex; i<rateSize; i++) {
							U rateItem = rateList.get(i);
							V newItem = newInstance();
							newItem.setMarket(rateItem);
							curPrice = curPrice / (rateItem.getValue() / 100.0 + 1);
							newItem.setValue(curPrice);
							
							resultList.add(newItem);
						}
						
						break;
					}
					
					U rateEntity = rateList.get(rateIndex);
					V resultEntity = resultList.get(resultIndex);
					
					if(rateEntity.getDate() == resultEntity.getDate()) {
						Double curPrice = resultEntity.getValue();

						while(stack.size() > 0) {
							U rateItem = stack.pop();
							
							V newItem = newInstance();
							newItem.setMarket(rateItem);
							curPrice = curPrice + (curPrice * rateItem.getValue() / 100.0);
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
				
				//TODO 비어 있는 데이터 찾아서 넣어볼까?
				for(int i=0; i<resultList.size()-1; i++) {
					V curItem = resultList.get(i);
					V prevItem = resultList.get(i+1);
					
					if(curItem.getDate().getTime() - prevItem.getDate().getTime() != MILESECONDS_OF_ONE_WEEK) {
						logger.warn("Invalid value : date inteval is not 1 weeks. 지역=" + curItem.getClassification() + ", 날짜=" + curItem.getDate() + ", interval=" 
									+ (curItem.getDate().getTime() - prevItem.getDate().getTime())/1000/60/60/24 + "day(s)");
						 
					}
				}
				resultLists.addAll(resultList);
			}
			
			return resultLists;
		}
		
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
}
