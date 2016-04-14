package bluney.sample.sample.domain.market.earning;

import java.util.List;

import bluney.sample.sample.domain.market.TotalMarket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @NoArgsConstructor @AllArgsConstructor class EarningStat {
	
	private @Getter @Setter double rate;
	private @Getter @Setter double selling;
	private @Getter @Setter double lease;
	private @Getter @Setter double interval;
	
	private @Getter @Setter int count;
	private @Getter @Setter int numOfIncrease;
	private @Getter @Setter int numOfDecrease;
	private @Getter @Setter double averageTotal;
	private @Getter @Setter double averagePerYear;
	
	private @Getter @Setter List<TotalMarket> totalMarketList;
	
	@Override
	public String toString() {
		return "EarningStat [rate=" + rate + ", selling=" + selling + ", lease=" + lease + ", interval=" + interval 
				 + ", numOfIncrease=" + numOfIncrease + ", numOfDecrease=" + numOfDecrease + ", averageTotal=" + averageTotal + ", averagePerYear=" + averagePerYear + "]";
	}
}
