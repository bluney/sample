package bluney.sample.sample.domain.market.gin.selling;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import bluney.sample.sample.customtype.market.MarketGin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Springfield(strategy=Strategy.HIBERNATE)
@Entity
@Table(name="market_gin_lease")
public @ToString @NoArgsConstructor @AllArgsConstructor class MarketGinLease extends MarketGin {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_MARKET_GIN_LEASE_PK_SYNC")
	@SequenceGenerator(name="SEQ_MARKET_GIN_LEASE_PK_SYNC", sequenceName="SEQ_MARKET_GIN_LEASE_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter String code;			//법정동 코드 
	private @Getter @Setter @NotNull String classification;	//행정구역 식별자
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	private @Getter @Setter Double value;			//지인 value : 전세가 증가율
	
	private @Getter @Setter @NotNull java.util.Date fromDate;	//시작날짜
}