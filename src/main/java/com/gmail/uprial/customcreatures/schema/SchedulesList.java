package com.gmail.uprial.customcreatures.schema;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;
import static com.gmail.uprial.customcreatures.config.ConfigReaderLists.getItemsList;

public final class SchedulesList {

    private final List<Schedule> schedules;

    private SchedulesList(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public boolean isPassed() {
        for(final Schedule schedule : schedules) {
            if(!schedule.isPassed()) {
                return false;
            }
        }

        return true;
    }

    public static SchedulesList getFromConfig(FileConfiguration config, CustomLogger customLogger, String key, String title) throws InvalidConfigException {
        List<Schedule> schedules = new ArrayList<>();

        Set<String> subKeys = getItemsList(config, customLogger, key, title);
        if(subKeys != null) {
            for (String subKey : subKeys) {
                schedules.add(Schedule.getFromConfig(config, customLogger, subKey, String.format("schedule '%s' in %s", subKey, title)));
            }
        }

        return new SchedulesList(schedules);
    }

    public String toString() {
        return String.format("[%s]", joinStrings(", ", schedules));
    }
}
