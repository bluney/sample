package bluney.sample.sample.domain.lease.price;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.u2ware.springfield.config.Springfield;
import com.u2ware.springfield.config.Springfield.Strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Springfield(strategy=Strategy.HIBERNATE)
@Entity
@Table(name="technology")
public @ToString @NoArgsConstructor @AllArgsConstructor class LeasePriceEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_LEASE_PRICE_PK_SYNC")
	@SequenceGenerator(name="SEQ_LEASE_PRICE_PK_SYNC", sequenceName="SEQ_LEASE_PRICE_PK_SYNC")
	private @Getter @Setter Integer id;				//순번 시퀀스
	
	private @Getter @Setter String code;			//
	private @Getter @Setter String classification;	//
	private @Getter @Setter java.sql.Date date;
	private @Getter @Setter Long value;				
}