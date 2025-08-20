package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.schema.enums.DayOfTheWeek;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import static com.gmail.uprial.customcreatures.common.Utils.joinPaths;
import static com.gmail.uprial.customcreatures.config.ConfigReaderEnums.getSet;
import static com.gmail.uprial.customcreatures.config.ConfigReaderSimple.getString;

public final class Schedule {
    private final TimeZone timeZone;
    private final Set<DayOfTheWeek> daysOfTheWeek;
    private final Set<Integer> codes;

    private Schedule(TimeZone timeZone, Set<DayOfTheWeek> daysOfTheWeek) {
        this.timeZone = timeZone;
        this.daysOfTheWeek = daysOfTheWeek;

        codes = new HashSet<>();
        for(final DayOfTheWeek dayOfTheWeek : daysOfTheWeek) {
            codes.add(dayOfTheWeek.getType());
        }
    }

    public boolean isPassed() {
        return isPassed(Calendar.getInstance(timeZone));
    }

    boolean isPassed(final Calendar calendar) {
        return codes.contains(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static Schedule getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        if (config.get(key) == null) {
            throw new InvalidConfigException(String.format("Empty %s", title));
        }

        String tz = getString(config, joinPaths(key, "timezone"), String.format("timezone of %s", title));

        TimeZone timeZone = TimeZone.getTimeZone(tz);

        if(timeZone.getID().equals("GMT")) {
            throw new InvalidConfigException(String.format("Wrong timezone of %s", title));
        }

        Set<DayOfTheWeek> daysOfTheWeek = getSet(DayOfTheWeek.class, config, customLogger,
                joinPaths(key, "days-of-the-week"), String.format("days of the week of %s", title));

        if (daysOfTheWeek == null) {
            throw new InvalidConfigException(String.format("No modifications found of %s", title));
        }

        return new Schedule(timeZone, daysOfTheWeek);
    }

    public String toString() {
        return String.format("{timezone: %s, days-of-the-week: %s}",
                timeZone.getID(), daysOfTheWeek);
    }
}
