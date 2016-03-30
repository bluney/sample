package bluney.sample.sample.common.util;

public class DataConvertUtil {

	public static Float stringToFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Integer stringToInteger(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
