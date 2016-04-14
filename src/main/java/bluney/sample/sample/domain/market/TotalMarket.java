package bluney.sample.sample.domain.market;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import bluney.sample.sample.customtype.market.Market;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Springfield(strategy=Strategy.HIBERNATE,
		methodLevelMapping={ "*", "*.xls"}
)
@Entity
@Table(name="total_market")
public @ToString @NoArgsConstructor @AllArgsConstructor class TotalMarket extends Market {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_TOTAL_MARKET_PK_SYNC")
	@SequenceGenerator(name="SEQ_TOTAL_MARKET_PK_SYNC", sequenceName="SEQ_TOTAL_MARKET_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	private @Getter @Setter String code;			//법정동 코드 
	private @Getter @Setter @NotNull String classification;	//행정구역 식별자
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	private @Getter @Setter Double value;
	
	@Column(name="selling_price")
	private @Getter @Setter Double sellingPrice;
	
	@Column(name="lease_price")
	private @Getter @Setter Double leasePrice;
	
	@Column(name="gin_rate")
	private @Getter @Setter Double ginRate;

	@Column(name="gin_selling")
	private @Getter @Setter Double ginSelling;
	
	@Column(name="gin_lease")
	private @Getter @Setter Double ginLease;
	
}