package bluney.sample.sample.domain.market.earning;

import java.util.List;

import bluney.sample.sample.domain.market.TotalMarket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @NoArgsConstructor @AllArgsConstructor class EarningStat implements Comparable<EarningStat> {
	
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
	
	@Override
	public int compareTo(EarningStat o) {
		if(getAveragePerYear() == o.getAveragePerYear()) {
			if(getAverageTotal() == o.getAverageTotal()) {
				if(getRate() == o.getRate()) {
					if(getSelling() == o.getSelling()) {
						if(getLease() == o.getLease()) {
							return 0;
						}
						return getLease() > o.getLease() ? 1 : -1;
					}
					return getSelling() > o.getSelling() ? 1 : -1;
				}
				return getRate() > o.getRate() ? 1 : -1;
			}
			return getAverageTotal() > o.getAverageTotal() ? 1 : -1;
		}
		return getAveragePerYear() > o.getAveragePerYear() ? 1 : -1;
	}
}
