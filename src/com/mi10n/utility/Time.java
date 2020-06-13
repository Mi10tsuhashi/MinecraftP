package com.mi10n.utility;

import java.util.concurrent.TimeUnit;

public class Time {
     public static String format(long time) {
    	    long day = TimeUnit.MILLISECONDS.toDays(time);
	        time -= TimeUnit.DAYS.toMillis(day);
	        long hours = TimeUnit.MILLISECONDS.toHours(time);
	        time -= TimeUnit.HOURS.toMillis(hours);
	        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
	        time -= TimeUnit.MINUTES.toMillis(minutes);
	        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
	        time -= TimeUnit.SECONDS.toMillis(seconds);
	        String mili = "";
	        if(Math.floor(Math.log10(time)+1.0)==1) {
	        	mili = "00"+String.valueOf(time);
	        } else if (Math.floor(Math.log10(time)+1.0)==2) {
	        	mili = "0"+String.valueOf(time);
	        } else {
	        	mili = String.valueOf(time);
	        }
	        if(day!=0) {
     	        return day+"d, "+hours+"h, "+minutes+"m, "+seconds+"."+mili+"s";
     	        }
     	       if(day==0&&hours!=0) {
     	    	  return hours+"h, "+minutes+"m, "+seconds+"."+mili+"s";
        	        }
     	      if(day==0&&hours==0&&minutes!=0) {
     	    	 return minutes+"m, "+seconds+"."+mili+"s";
       	        }
     	     if(day==0&&hours==0&&minutes==0) {
     	    	return seconds+"."+mili+"s";
        	        }
     	     return null;
     }
}
