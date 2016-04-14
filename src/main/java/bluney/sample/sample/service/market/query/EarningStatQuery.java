package bluney.sample.sample.service.market.query;

import com.u2ware.springfield.repository.QueryMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@QueryMethod("findBy"
				+ "NumofincreaseGreaterThanEqual"
				+ "AndNumofdecreaseGreaterThanEqual"
				+ "AndAveragetotalGreaterThanEqual"
				+ "AndAverageperyearGreaterThanEqual"
				+ "OrderByAverageperyearDescAveragetotalDesc")
public @ToString @NoArgsConstructor @AllArgsConstructor class EarningStatQuery {
	private @Getter @Setter int numOfIncrease;
	private @Getter @Setter int numOfDecrease;
	private @Getter @Setter double averageTotal;
	private @Getter @Setter double averagePerYear;
}
