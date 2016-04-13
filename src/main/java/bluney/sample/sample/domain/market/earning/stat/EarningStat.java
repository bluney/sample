package bluney.sample.sample.domain.market.earning.stat;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import bluney.sample.sample.domain.market.TotalMarket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Springfield(strategy=Strategy.HIBERNATE)
@Entity
@Table(name="earning_stat")
public @ToString @NoArgsConstructor @AllArgsConstructor class EarningStat {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_MARKET_GIN_SELLING_PK_SYNC")
	@SequenceGenerator(name="SEQ_MARKET_GIN_SELLING_PK_SYNC", sequenceName="SEQ_MARKET_GIN_SELLING_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter double rate;
	private @Getter @Setter double selling;
	private @Getter @Setter double lease;
	private @Getter @Setter double interval;
	
	private @Getter @Setter int numOfIncrease;
	private @Getter @Setter int numOfDecrease;
	private @Getter @Setter double averageTotal;
	private @Getter @Setter double averagePerYear;
	
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL,mappedBy="id")
	@Fetch(FetchMode.SUBSELECT)
	private @Getter @Setter List<TotalMarket> totalMarketList;
}
