package bluney.sample.sample.customtype.market;

abstract public class Market {

	abstract public String getCode();
	abstract public void setCode(String code);
	
	abstract public String getClassification();
	abstract public void setClassification(String classification);
	
	abstract public java.util.Date getDate();
	abstract public void setDate(java.util.Date date);
	
	abstract public Double getValue();
	abstract public void setValue(Double value);

}
