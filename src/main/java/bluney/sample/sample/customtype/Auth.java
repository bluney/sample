package bluney.sample.sample.customtype;

public enum Auth {
	GUEST("200", "타부서 인원"), 
	MEMBER("400", "훈련체계 인원"), 
	ADMIN("404", "관리자");

	private String code;
	private String message;
	
	Auth(String code, String message){
		this.code = code;
		this.message = message;
	}
	public String getCode(){
		return code;
	}
	public String getMessage(){
		return message;
	}
}


