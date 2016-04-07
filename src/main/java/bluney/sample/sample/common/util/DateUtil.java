package bluney.sample.sample.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getDate(int year, int month, int date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, date, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
