package bluney.sample.sample.customtype.market;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;

abstract public @AllArgsConstructor class Market implements Comparable<Market>{

	abstract public String getCode();
	abstract public void setCode(String code);
	
	abstract public String getClassification();
	abstract public void setClassification(String classification);
	
	abstract public java.util.Date getDate();
	abstract public void setDate(java.util.Date date);
	
	abstract public Double getValue();
	abstract public void setValue(Double value);

	@Override
	@Transient
	public int compareTo(Market o) {
		if (getDate() == null || o.getDate() == null)
			return 0;
		return getDate().compareTo(o.getDate());
	}
	
	@Transient
	public void setMarket(Market o) {
		this.setCode(o.getCode());
		this.setClassification(o.getClassification());
		this.setDate(o.getDate());
		this.setValue(o.getValue());
	}
}
