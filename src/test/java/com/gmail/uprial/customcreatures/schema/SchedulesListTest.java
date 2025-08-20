package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.helpers.TestConfigBase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.schema.SchedulesList.getFromConfig;
import static org.junit.Assert.*;

public class SchedulesListTest extends TestConfigBase {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testWholeSchedulesList() throws Exception {
        SchedulesList schedulesList = getFromConfig(getPreparedConfig(
                "sl:",
                "  - s1",
                "  - s2",
                "s1:",
                "  timezone: Europe/London",
                "  days-of-the-week:",
                "  - MON",
                "s2:",
                "  timezone: Europe/Berlin",
                "  days-of-the-week:",
                "  - FRI"),
                getParanoiacCustomLogger(), "sl", "schedules list");
        assertNotNull(schedulesList);
        assertEquals("[{timezone: Europe/London, days-of-the-week: [MON]}," +
                " {timezone: Europe/Berlin, days-of-the-week: [FRI]}]", schedulesList.toString());
    }

    @Test
    public void testEmptySchedulesValue() throws Exception {
        SchedulesList schedulesList = getFromConfig(getPreparedConfig(
                "sl:"),
                getCustomLogger(), "sl", "schedules list");
        assertNull(schedulesList);
    }
}