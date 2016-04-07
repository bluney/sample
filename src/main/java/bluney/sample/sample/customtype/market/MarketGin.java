package bluney.sample.sample.customtype.market;

import lombok.AllArgsConstructor;

abstract public @AllArgsConstructor class MarketGin extends Market {
	abstract public java.util.Date getFromDate();
	abstract public void setFromDate(java.util.Date date);
}
