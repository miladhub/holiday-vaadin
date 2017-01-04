package test;

import static org.junit.Assert.*;
import org.miladhub.holidays.*;

import org.junit.Test;

public class HolidayPlannerAcceptanceTest {
	private final HolidayConfiguration config = new HolidayConfiguration(10, 5, 20, 3, "Jan-2015");
	private final StringPlan plan = new StringPlan();
	private final HoursTaken hoursTaken = new InMemoryHoursTaken();
	private final HolidayPlanner planner = new HolidayPlanner(plan, hoursTaken, InMemoryHolidaySettings.of(config));
	
	@Test
	public void takeHoursOnGivenMonth() throws Exception {
		planner.takeHoursOff(3, "Jan-2015");
		planner.planUntil("Mar-2015");
		assertEquals(
				"Jan-2015 ...   7  ...  20\n" +
				"Feb-2015 ...  12  ...  23\n" +
				"Mar-2015 ...  17  ...  26\n", plan.dump());
	}
	
	@Test
	public void takeHoursAndVacationAcrossFourMonths() throws Exception {
		planner.takeHoursOff(3, "Jan-2015");
		planner.takeVacationHours(4, "Mar-2015");
		planner.planUntil("Jun-2015");
		assertEquals(
				"Jan-2015 ...   7  ...  20\n" +
				"Feb-2015 ...  12  ...  23\n" +
				"Mar-2015 ...  17  ...  22\n" +
				"Apr-2015 ...  22  ...  25\n" +
				"May-2015 ...  27  ...  28\n" +
				"Jun-2015 ...  32  ...  31\n", plan.dump());
	}
}
