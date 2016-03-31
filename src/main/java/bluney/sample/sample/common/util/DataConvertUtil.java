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
	
	public static Long stringToLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double stringToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
