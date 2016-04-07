package bluney.sample.sample.controller.market;

import java.util.HashMap;
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

import bluney.sample.sample.common.util.DateUtil;
import bluney.sample.sample.domain.lease.PyeongLeasePrice;
import bluney.sample.sample.domain.market.LeasePerPrice;
import bluney.sample.sample.domain.market.gin.lease.MarketGinSelling;
import bluney.sample.sample.domain.market.gin.selling.MarketGinLease;
import bluney.sample.sample.domain.selling.PyeongSellingPrice;
import bluney.sample.sample.service.market.MarketService;
import bluney.sample.sample.service.market.query.MarketRankQuery;
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
	
	
	@Resource(name="marketService")
	private @Getter MarketService service;
	
	@Resource(name="pyeongSellingPriceService")
	private EntityService<PyeongSellingPrice, MarketRankQuery> pyeongSellingPriceService;
	
	@Resource(name="pyeongLeasePriceService")
	private EntityService<PyeongLeasePrice, MarketRankQuery> pyeongLeasePriceService;
	
	@Resource(name="leasePerPriceService")
	private EntityService<LeasePerPrice, MarketRankQuery> leasePerPriceService;
	
	@Resource(name="marketGinSellingService")
	private EntityService<MarketGinSelling, MarketRankQuery> marketGinSellingService;
	
	@Resource(name="marketGinLeaseService")
	private EntityService<MarketGinLease, MarketRankQuery> marketGinLeaseService;
	
	
	@RequestMapping(value = "/market.html", method = RequestMethod.GET)
	public String sampleMarket(@RequestParam HashMap<String, String> map, Model model) {
//	public String sampleMarket(Locale locale, Model model) {
		logger.warn("request method: find()");
//		logger.warn("request model : "+query);	
		
		MarketRankQuery query = new MarketRankQuery();
		query.setDate(DateUtil.getDate(2016, 3, 21, 0, 0, 0));
		
		Iterable<?> pyeongSellingList = pyeongSellingPriceService.find(query, null);
		Iterable<?> pyeongLeaseList = pyeongLeasePriceService.find(query, null);
		Iterable<?> leasePerPriceList = leasePerPriceService.find(query, null);
		Iterable<?> marketGinSellingList = marketGinSellingService.find(query, null);
		Iterable<?> marketGinLeaseList = marketGinLeaseService.find(query, null);
		
		model.addAttribute(PYEONG_SELLING_PRICE_ENTITY, pyeongSellingList);
		model.addAttribute(PYEONG_LEASE_PRICE_ENTITY, pyeongLeaseList);
		model.addAttribute(LEASE_PER_SELLING_ENTITY, leasePerPriceList);
		model.addAttribute(MARKET_GIN_SELLING_ENTITY, marketGinSellingList);
		model.addAttribute(MARKET_GIN_LEASE_ENTITY, marketGinLeaseList);		
		
		return "/service/market/market.html";
	}
	
	
	@RequestMapping(value = "/analyzeMarketTimeSeries", method = RequestMethod.GET)
	public String analyzeMarketTimeSeries(Locale locale, Model model) {
		
		// 평당 가격 구하기
		service.analyzeMarketTimeSeries();
		
		return "/service/market/market.html";
	}
	
}
