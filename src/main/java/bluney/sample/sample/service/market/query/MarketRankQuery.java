package bluney.sample.sample.service.market.query;

import javax.validation.constraints.NotNull;

import com.u2ware.springfield.repository.QueryMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@QueryMethod("findByDateAndValueGreaterThanEqualOrderByValueDesc")
public @ToString @NoArgsConstructor @AllArgsConstructor class MarketRankQuery {
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	private @Getter @Setter @NotNull Double value;			//날짜
}