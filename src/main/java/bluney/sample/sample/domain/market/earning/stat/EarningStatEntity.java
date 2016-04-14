package bluney.sample.sample.domain.market.earning.stat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.Getter;
import lombok.Setter;

@Springfield(strategy=Strategy.HIBERNATE)
@Entity
@Table(name="earning_stat")
public class EarningStatEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_EARNING_STAT_PK_SYNC")
	@SequenceGenerator(name="SEQ_EARNING_STAT_PK_SYNC", sequenceName="SEQ_EARNING_STAT_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter double rate;
	private @Getter @Setter double selling;
	private @Getter @Setter double lease;
	private @Getter @Setter double interval;
	
	private @Getter @Setter int count;
	private @Getter @Setter int numOfIncrease;
	private @Getter @Setter int numOfDecrease;
	private @Getter @Setter double averageTotal;
	private @Getter @Setter double averagePerYear;
		
	@Transient
	@Override
	public String toString() {
		return "EarningStat [id=" + id
				 + ", rate=" + rate + ", selling=" + selling + ", lease=" + lease + ", interval=" + interval 
				 + ", numOfIncrease=" + numOfIncrease + ", numOfDecrease=" + numOfDecrease + ", averageTotal=" + averageTotal + ", averagePerYear=" + averagePerYear + "]";
	}
}
