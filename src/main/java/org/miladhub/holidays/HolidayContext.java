package org.miladhub.holidays;

public interface HolidayContext {
	HolidayConfiguration configuration();
	void save(HolidayConfiguration holidayConfiguration);
}
