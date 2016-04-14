package bluney.sample.sample.domain.selling;

import javax.validation.constraints.NotNull;

import bluney.sample.sample.customtype.market.Market;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Springfield(strategy=Strategy.HIBERNATE)
//@Entity
//@Table(name="pyeong_selling_price")
public @ToString @NoArgsConstructor @AllArgsConstructor class PyeongSellingPrice extends Market {
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_PYEONG_SELLING_PRICE_PK_SYNC")
//	@SequenceGenerator(name="SEQ_PYEONG_SELLING_PRICE_PK_SYNC", sequenceName="SEQ_PYEONG_SELLING_PRICE_PK_SYNC")
//	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter String code;			//법정동 코드 
	private @Getter @Setter @NotNull String classification;	//행정구역 식별자
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	private @Getter @Setter Double value;			//평당 매매 가격
	
}
