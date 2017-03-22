package cn.gitv.bi.userinfo.uifmaintain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateUtils {
	private static ThreadLocal<SimpleDateFormat> thread_smpf = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	public static Date parseTimestamp(String date) {
		Date nd = null;
		try {
			nd = thread_smpf.get().parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nd;
	}

}
