package com.ailk.bdx.pop.adapter.bean;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;

public class ScanPeriod {
	private int period;
	private String timeUnit;

	public ScanPeriod() {
	}

	public ScanPeriod(int period, String timeUnit) {
		this.period = period;
		this.timeUnit = timeUnit;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

 
	public int getDelayPeriodUnitForHdfs(){
		int unit = 0;
		if(!Strings.isNullOrEmpty(timeUnit)){
			if("SECONDS".equals(timeUnit)){
				unit = Calendar.MINUTE;
			}else if("MINUTES".equals(timeUnit)){
				unit = Calendar.HOUR;
			}else if("HOURS".equals(timeUnit)){
				unit = Calendar.DAY_OF_MONTH;
			}else if("DAYS".equals(timeUnit)){
				unit = Calendar.MONTH;
			}
		}
		return unit;
	}
	
	public long getDelayPeriodUnit() {
		long delay = 0L;
		if (!Strings.isNullOrEmpty(timeUnit)) {
			if("SECONDS".equals(timeUnit)){
				delay = TimeUnit.MINUTES.toMillis(1);
			}else if("MINUTES".equals(timeUnit)){
				delay = TimeUnit.HOURS.toMillis(1);
			}else if("HOURS".equals(timeUnit)){
				delay = TimeUnit.DAYS.toMillis(1);
			}
		}
		return delay;
	}
 
 
 
	
	

}
