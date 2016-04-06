package bluney.sample.sample.controller.market;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bluney.sample.sample.service.market.MarketService;
import lombok.Getter;

@Controller
@RequestMapping(value = "/service/market")
public class MarketController {

	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);
	
	public final static String SELLING_PRICE_ENTITY = "selling_price_entity";
	public final static String LEASE_PRICE_ENTITY = "lease_price_entity";
	public final static String LEASE_PER_SELLING_ENTITY = "lease_per_selling_entity";
	
	
	@Resource(name="marketService")
	private @Getter MarketService service;
	
	@RequestMapping(value = "/market.html", method = RequestMethod.GET)
	public String sampleMarket(Locale locale, Model model) {
	
		return "/service/market/market.html";
	}
	
	@RequestMapping(value = "/analyzeMarketTimeSeries", method = RequestMethod.GET)
	public String analyzeMarketTimeSeries(Locale locale, Model model) {
		
		// 평당 가격 구하기
		service.analyzeMarketTimeSeries();
		
		return "/service/market/market.html";
	}

}
