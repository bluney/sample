package bluney.sample.sample.service.market.query;

import javax.validation.constraints.NotNull;

import com.u2ware.springfield.repository.QueryMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@QueryMethod("findByClassificationAndDateOrderByClassificationAscDateAsc")
public @ToString @NoArgsConstructor @AllArgsConstructor class MarketQuery {

	private @Getter @Setter @NotNull String classification;	// 행정구역 식별자
	private @Getter @Setter @NotNull java.util.Date date;	//	
}


