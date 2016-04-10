package bluney.sample.sample.controller.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.u2ware.springfield.service.EntityService;

import bluney.sample.sample.common.util.DataConvertUtil;
import bluney.sample.sample.common.util.DateUtil;
import bluney.sample.sample.customtype.market.Market;
import bluney.sample.sample.domain.lease.PyeongLeasePrice;
import bluney.sample.sample.domain.market.TotalMarket;
import bluney.sample.sample.domain.market.gin.lease.MarketGinSelling;
import bluney.sample.sample.domain.market.gin.selling.MarketGinLease;
import bluney.sample.sample.domain.market.rate.LeasePerPrice;
import bluney.sample.sample.domain.selling.PyeongSellingPrice;
import bluney.sample.sample.service.market.MarketService;
import bluney.sample.sample.service.market.query.TotalMarketQuery;
import lombok.Getter;

@Controller
@RequestMapping(value = "/service/market")
public class MarketController {

	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);
	
	public final static String PYEONG_SELLING_PRICE_ENTITY = "pyeong_selling_price_entity";
	public final static String PYEONG_LEASE_PRICE_ENTITY = "pyeong_lease_price_entity";
	public final static String LEASE_PER_SELLING_ENTITY = "lease_per_selling_entity";
	public final static String MARKET_GIN_SELLING_ENTITY = "market_gin_selling_entity";
	public final static String MARKET_GIN_LEASE_ENTITY = "market_gin_lease_entity";
	public final static String EARNING_RATE_ENTITY = "earning_rate_entity";
	public final static String MARKET_ENTITY = "market_entity";
	public final static String DATES_ENTITY_FOR_MARKET = "dates_entity_for_market";
	
	
	@Resource(name="marketService")
	private @Getter MarketService service;
	
	@Resource(name="totalMarketService")
	private EntityService<TotalMarket, TotalMarketQuery> totalMarketService;

	
	@RequestMapping(value = "/market.html", method = RequestMethod.GET)
	public String sampleMarket(@RequestParam HashMap<String, String> map, Model model) {
//	public String sampleMarket(Locale locale, Model model) {
		logger.warn("request method: find()");
//		logger.warn("request model : "+query);	
		
		TotalMarketQuery query = new TotalMarketQuery();
		query.setDate(DateUtil.getDate(2016, 3, 21, 0, 0, 0));
		
		@SuppressWarnings("unchecked")
		Iterable<TotalMarket> marketList = (Iterable<TotalMarket>) totalMarketService.find(query, null);
		model.addAttribute(MARKET_ENTITY, marketList);

		List<PyeongSellingPrice> pyeongSellingList = new ArrayList<PyeongSellingPrice>();
		List<PyeongLeasePrice> pyeongLeaseList = new ArrayList<PyeongLeasePrice>();
		List<LeasePerPrice> leasePerPriceList = new ArrayList<LeasePerPrice>();
		List<MarketGinSelling> marketGinSellingList = new ArrayList<MarketGinSelling>();
		List<MarketGinLease> marketGinLeaseList = new ArrayList<MarketGinLease>();
		List<Date> dateList = new ArrayList<Date>();
		
		for(TotalMarket entity : marketList) {
			if(entity.getSellingPrice() != null) {
				PyeongSellingPrice item = new PyeongSellingPrice();
				item.setMarket(entity);
				item.setValue(entity.getSellingPrice());
				pyeongSellingList.add(item);				
			}
			if(entity.getLeasePrice() != null) {
				PyeongLeasePrice item = new PyeongLeasePrice();
				item.setMarket(entity);
				item.setValue(entity.getLeasePrice());
				pyeongLeaseList.add(item);				
			}
			if(entity.getGinRate() != null) {
				LeasePerPrice item = new LeasePerPrice();
				item.setMarket(entity);
				item.setValue(entity.getGinRate());
				leasePerPriceList.add(item);
			}
			if(entity.getGinSelling() != null) {
				MarketGinSelling item = new MarketGinSelling();
				item.setMarket(entity);
				item.setValue(entity.getGinSelling());
				marketGinSellingList.add(item);
			}
			if(entity.getGinLease() != null) {
				MarketGinLease item = new MarketGinLease();
				item.setMarket(entity);
				item.setValue(entity.getGinLease());
				marketGinLeaseList.add(item);
			}
			
			dateList.add(entity.getDate());
		}
		
		Collections.sort(pyeongSellingList, Market.getValueSorter());
		Collections.sort(pyeongLeaseList, Market.getValueSorter());
		Collections.sort(leasePerPriceList, Market.getValueSorter());
		Collections.sort(marketGinSellingList, Market.getValueSorter());
		Collections.sort(marketGinLeaseList, Market.getValueSorter());
		Collections.sort(dateList, Collections.reverseOrder());
		
//		Iterable<?> pyeongSellingList = pyeongSellingPriceService.find(query, null);
//		Iterable<?> pyeongLeaseList = pyeongLeasePriceService.find(query, null);
//		Iterable<?> leasePerPriceList = leasePerPriceService.find(query, null);
//		Iterable<?> marketGinSellingList = marketGinSellingService.find(query, null);
//		Iterable<?> marketGinLeaseList = marketGinLeaseService.find(query, null);
//		
		model.addAttribute(PYEONG_SELLING_PRICE_ENTITY, pyeongSellingList);
		model.addAttribute(PYEONG_LEASE_PRICE_ENTITY, pyeongLeaseList);
		model.addAttribute(LEASE_PER_SELLING_ENTITY, leasePerPriceList);
		model.addAttribute(MARKET_GIN_SELLING_ENTITY, marketGinSellingList);
		model.addAttribute(MARKET_GIN_LEASE_ENTITY, marketGinLeaseList);
		model.addAttribute(DATES_ENTITY_FOR_MARKET, dateList);
		
		return "/service/market/market.html";
	}
	
	
	@RequestMapping(value = "/analyzeMarketTimeSeries", method = RequestMethod.GET)
	public String analyzeMarketTimeSeries(Locale locale, Model model) {
		
		// 평당 가격 구하기
		service.analyzeMarketTimeSeries();
		
		return "redirect:/service/market/market.html";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/calcurateRateOfEarning", method = RequestMethod.GET)
	public String calcurateRateOfEarning(@RequestParam HashMap<String, String> map, Model model) {
		
		Double pRate = DataConvertUtil.stringToDouble(map.get("rate"));
		Double pSelling = DataConvertUtil.stringToDouble(map.get("selling"));
		Double pLease = DataConvertUtil.stringToDouble(map.get("lease"));
		int pTiming = DataConvertUtil.stringToInteger(map.get("timing"));
		
		logger.debug("calcurateRateOfEarning: CONDITION - rate=" + pRate + ", selling=" + pSelling + ", lease=" + pLease);
		logger.debug("calcurateRateOfEarning:           - timing=" + pTiming);
//		List<LeasePerPrice> leasePerPriceList = null;
//		List<MarketGinSelling> marketGinSellingList = null;
//		List<MarketGinLease> marketGinLeaseList  = null;
//		
//		if(pRate != null) {
//			MarketRankQuery query = new MarketRankQuery();
//			query.setValue(pRate);
//			leasePerPriceList = (List<LeasePerPrice>) leasePerPriceService.find(query, null);	
//		}
//		
//		if(pSelling != null) {
//			MarketRankQuery query = new MarketRankQuery();
//			query.setValue(pSelling);
//			marketGinSellingList = (List<MarketGinSelling>) marketGinSellingService.find(query, null);
//		}
//		
//		if(pSelling != null) {
//			MarketRankQuery query = new MarketRankQuery();
//			query.setValue(pLease);
//			marketGinLeaseList = (List<MarketGinLease>) marketGinLeaseService.find(query, null);
//		}
//		
//		Iterable<?> resultList = service.calcurateRateOfEarning(leasePerPriceList, marketGinSellingList, marketGinLeaseList);
		
		TotalMarketQuery query = new TotalMarketQuery();
		query.setGinRate(pRate);
		query.setGinSelling(pSelling);
		query.setGinLease(pLease);
		
		List<TotalMarket> totalMarketList = (List<TotalMarket>) totalMarketService.find(query, null);
		List<TotalMarket> resultList = service.calcurateRateOfEarning(totalMarketList, pTiming);
		Collections.sort(resultList, Market.getValueSorter());
		Double average = 0.0;
		int numOfIncrease = 0;
		int numOfDecrease = 0;
		for (TotalMarket item : resultList) {
			Double value = item.getValue(); 
			average += value;
			if (value > 0.0) {
				numOfIncrease++;
			}else if (value < 0.0) {
				numOfDecrease++;
			}
		}
		
		average /= (double) resultList.size();
		
		model.addAttribute(EARNING_RATE_ENTITY, resultList);
		model.addAttribute("earning_rate_total", average*100.0);
		model.addAttribute("earning_rate_average", average*100.0/((double)pTiming/(365.0/7.0)));
		model.addAttribute("earning_rate_increase_num", numOfIncrease);
		model.addAttribute("earning_rate_decrease_num", numOfDecrease);
		return "/service/market/EarningRate.html";
	}
	
}
