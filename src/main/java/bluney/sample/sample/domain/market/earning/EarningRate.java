package bluney.sample.sample.domain.market.earning;

import javax.validation.constraints.NotNull;

import bluney.sample.sample.customtype.market.Market;
import lombok.Getter;
import lombok.Setter;

public class EarningRate extends Market {
	private @Getter @Setter String code;
	private @Getter @Setter @NotNull String classification;	//행정구역 식별자
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	private @Getter @Setter Double leasePerPrice;			//전세율
	private @Getter @Setter Double ginSelling;				//지인데이터1. 매매 증감율
	private @Getter @Setter Double ginLease;				//지인데이터2. 전세 증감율
	
	private @Getter @Setter Double value;				//수익율
}
