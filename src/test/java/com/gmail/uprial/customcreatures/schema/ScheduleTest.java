package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;

import static com.gmail.uprial.customcreatures.schema.Schedule.getFromConfig;
import static org.junit.Assert.*;

public class ScheduleTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testPassed() throws Exception {
        final Schedule schedule = getFromConfig(getPreparedConfig(
                        "s: ",
                        " timezone: Europe/London",
                        " days-of-the-week:",
                        " - MON"),
                getParanoiacCustomLogger(), "s", "schedule");

        final Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.AUGUST, 18, 1, 0, 0);

        assertTrue(schedule.isPassed(calendar));
    }

    @Test
    public void testNotPassedDate() throws Exception {
        Schedule schedule = getFromConfig(getPreparedConfig(
                        "s: ",
                        " timezone: Europe/London",
                        " days-of-the-week:",
                        " - TUE"),
                getParanoiacCustomLogger(), "s", "schedule");

        final Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.AUGUST, 18);

        assertFalse(schedule.isPassed(calendar));
    }

    @Test
    public void testEmptySchedule() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Empty schedule");
        getFromConfig(getPreparedConfig(
                "?:"),
                getParanoiacCustomLogger(), "r", "schedule");
    }

    @Test
    public void testEmptyTimezone() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Null timezone of schedule");

        getFromConfig(getPreparedConfig(
                "s: ",
                "  x: y"), getDebugFearingCustomLogger(), "s", "schedule");
    }

    @Test
    public void testWrongTimezone() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Wrong timezone of schedule");

        getFromConfig(getPreparedConfig(
                "s: ",
                "  timezone: y"), getDebugFearingCustomLogger(), "s", "schedule");
    }

    @Test
    public void testNoModifications() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("No modifications found of schedule");

        getFromConfig(getPreparedConfig(
                "s:",
                " timezone: Europe/London",
                " x: y"), getCustomLogger(), "s", "schedule");
    }


    @Test
    public void testWrongDayOfTheWeek() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Invalid com.gmail.uprial.customcreatures.schema.enums.DayOfTheWeek 'y' in days of the week of schedule at pos 0");

        getFromConfig(getPreparedConfig(
                "s: ",
                " timezone: Europe/London",
                " days-of-the-week:",
                " - y"), getDebugFearingCustomLogger(), "s", "schedule");
    }

    @Test
    public void testWholeSchedule() throws Exception {
        Schedule schedule = getFromConfig(getPreparedConfig(
                        "s: ",
                        " timezone: Europe/London",
                        " days-of-the-week:",
                        " - MON"),
                getParanoiacCustomLogger(), "s", "schedule");
        assertEquals("{timezone: Europe/London, days-of-the-week: [MON]}", schedule.toString());
    }
}