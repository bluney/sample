package bluney.sample.sample.customtype.market;

public enum MarketType {
	SELLING_PRICE(0, "매매가격"), 
	LEASE_PRICE(1, "전세가격"),
	SELLING_RATE(2, "매매증감"), 
	LEASE_RATE(3, "전세증감");

	private Integer code;
	private String message;
	
	MarketType(Integer code, String message){
		this.code = code;
		this.message = message;
	}
	public Integer getCode(){
		return code;
	}
	public String getMessage(){
		return message;
	}
}
