package bluney.sample.sample.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Date Util
 * 
 */
public class SimpleDateUtil {

	public final static String BOTH = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE = "yyyy-MM-dd";
	public final static String TIME = "HH:mm:ss";
	public static long MILLION_SECONDS_OF_DAY = 24 * 60 * 60 * 1000L;// 86400000
	public static long MILLION_SECONDS_OF_HOUR = 60 * 60 * 1000L;// 3600000;

	private SimpleDateUtil() {

	}

	/**
	 * isValidDate
	 * 
	 * @param str
	 * @param fmt
	 * @return
	 */
	public static boolean isValidDate(String str, String fmt) {
		Date date = parse(str, fmt);
		if (date == null) {
			return false;
		}
		String dateStr = format(date, fmt);
		if (dateStr.equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * parse
	 * 
	 * @param str
	 * @param fmt
	 * @return
	 */
	public static Date parse(String str, String fmt) {
		if (NullJudgeUtil.isNotNull(fmt) && NullJudgeUtil.isNotNull(fmt)) {
			SimpleDateFormat simDateFormat = new SimpleDateFormat(fmt);
			Date date = null;
			try {
				date = simDateFormat.parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return date;
		}
		return null;		
	}

	/**
	 * format
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String format(Date date, String fmt) {
		if (NullJudgeUtil.isNotNull(fmt) && NullJudgeUtil.isNotNull(fmt)) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
		return simpleDateFormat.format(date);
		}else{
			return null;
		}
	}

	/**
	 * addMonth
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonth(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	/**
	 * addDay
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDay(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * addDay
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDay(String str, int days, String fmt) {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(str, fmt));
		c.add(Calendar.DAY_OF_MONTH, days);
		Date date = c.getTime();
		return format(date, fmt);
	}

	/**
	 * addMinutes
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date date, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	/**
	 * subDateDays
	 * 
	 * @param sd
	 * @param ed
	 * @return
	 */
	public static int subDateDays(Date sd, Date ed) {
		Long eds = ed.getTime();
		Long sds = sd.getTime();
		return (int) ((eds - sds) / MILLION_SECONDS_OF_DAY);
	}

	/**
	 * subDateDays
	 * 
	 * @param sd
	 * @param ed
	 * @return
	 */
	public static int subDateDays(String sd, String ed) {
		Long eds = parse(ed, DATE).getTime();
		Long sds = parse(sd, DATE).getTime();
		return (int) ((eds - sds) / MILLION_SECONDS_OF_DAY);
	}

	/**
	 * isContain
	 * 
	 * @param begin
	 * @param end
	 * @param tBegin
	 * @param tEnd
	 * @param fmt
	 * @return
	 */
	public static boolean isContain(String begin, String end, String tBegin, String tEnd, String fmt) {
		long beginTime = parse(begin, fmt).getTime();
		long endTime = parse(end, fmt).getTime();
		long bTime = parse(tBegin, fmt).getTime();
		long eTime = parse(tEnd, fmt).getTime();
		if (bTime > endTime || eTime <= beginTime) {
			return false;
		}
		return true;
	}

	/**
	 * toXMLGC
	 * 
	 * @param str
	 * @param fmt
	 * @return
	 */
	public static XMLGregorianCalendar toXMLGC(String str, String fmt) {
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar gc = null;
		try {
			Date date = parse(str, fmt);
			cal.setTime(date);
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * toDate
	 * 
	 * @param str
	 * @return
	 */
	public static Date toDate(String str) {
		return DatatypeConverter.parseDate(str).getTime();
	}
}
