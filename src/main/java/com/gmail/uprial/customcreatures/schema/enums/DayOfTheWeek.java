package com.gmail.uprial.customcreatures.schema.enums;

import java.util.Calendar;

public enum DayOfTheWeek implements ITypedEnum<Integer> {
    MON(Calendar.MONDAY),
    TUE(Calendar.TUESDAY),
    WED(Calendar.WEDNESDAY),
    THU(Calendar.THURSDAY),
    FRI(Calendar.FRIDAY),
    SAT(Calendar.SATURDAY),
    SUN(Calendar.SUNDAY);

    private final int type;

    DayOfTheWeek(final Integer type) {
        this.type = type;
    }

    @Override
    public Integer getType() {
        return type;
    }
}