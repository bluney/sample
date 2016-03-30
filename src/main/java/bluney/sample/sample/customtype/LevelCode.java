package bluney.sample.sample.customtype;

public enum LevelCode {
	LEVEL0("0","ZERO"),
	LEVEL1("1","ONE"), 
	LEVEL2("2","TWO"),
	LEVEL3("3","THREE"), 
	LEVEL4("4","FOUR"), 
	LEVEL5("5","FIVE");

	private String code;
	private String message;
	
	LevelCode(String code, String message){
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


