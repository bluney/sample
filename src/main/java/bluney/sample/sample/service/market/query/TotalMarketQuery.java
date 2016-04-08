package bluney.sample.sample.service.market.query;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.u2ware.springfield.repository.QueryMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@QueryMethod("findByDateAndGinSellingGreaterThanEqualAndGinLeaseGreaterThanEqualAndGinRateGreaterThanEqualOrderByClassificationAscDateDesc")
public @ToString @NoArgsConstructor @AllArgsConstructor class TotalMarketQuery {
	private @Getter @Setter @NotNull java.util.Date date;	//날짜
	
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


