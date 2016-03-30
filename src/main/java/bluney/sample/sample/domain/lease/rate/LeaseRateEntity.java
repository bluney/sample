package bluney.sample.sample.domain.lease.rate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Springfield(strategy=Strategy.HIBERNATE)
@Entity
@Table(name="lease_rate")
public @ToString @NoArgsConstructor @AllArgsConstructor class LeaseRateEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_LEASE_RATE_PK_SYNC")
	@SequenceGenerator(name="SEQ_LEASE_RATE_PK_SYNC", sequenceName="SEQ_LEASE_RATE_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter String code;			// 법정동 코드 
	private @Getter @Setter @NotNull String classification;	// 행정구역 식별자
	private @Getter @Setter @NotNull java.sql.Date date;	//
	private @Getter @Setter @NotNull Long value;			// 전세 증감률
}