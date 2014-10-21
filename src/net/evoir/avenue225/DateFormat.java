package net.evoir.avenue225;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {
	public String finalString;

	public String Format(String dateString) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date date = null;
		try {
		    date = sdf.parse(dateString);
		    String timeOfDay = new SimpleDateFormat("HH:mm").format(date);
		    String theDay = new SimpleDateFormat("dd/MM/yyy").format(date);
		    /*String timeOfDay = new SimpleDateFormat("yyyy/MM/dd").format(date);*/
		    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		    java.sql.Timestamp timeStampNow = new Timestamp((new java.util.Date()).getTime());

		    long secondDiff = timeStampNow.getTime() / 1000 - timeStampDate.getTime() / 1000;
		    int minuteDiff = (int) (secondDiff / 60);
		    int hourDiff = (int) (secondDiff / 3600);
		    int dayDiff = daysBetween(date, new Date()) - 1;
		    if (dayDiff > 20) {
		    	finalString = theDay + " @ " + timeOfDay;
		    }else if (dayDiff > 0 && dayDiff < 21) {
		    	finalString = dayDiff + " days ago @ " + timeOfDay;
		    } else if (hourDiff > 0) {
		    	finalString =  hourDiff + " hour(s) ago @ " + timeOfDay;
		    } else if (minuteDiff > 0) {
		    	finalString =  minuteDiff + " minute(s) ago @ " + timeOfDay;
		    } else if (secondDiff > 0) {
		    	finalString =  secondDiff + " second(s) ago @ " + timeOfDay;
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return finalString;
	}

public static int daysBetween(Date startDate, Date endDate) {
    int daysBetween = 0;
    while (startDate.before(endDate)) {
        startDate.setTime(startDate.getTime() + 86400000);
        daysBetween++;
    }
    return daysBetween;
}
}

