package com.prueba.controlfichajes.model;

import java.time.DayOfWeek;

public final class ModelUtils {

    private ModelUtils() {
        // Empty constructor
    }

    public static DayType fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DayType.MONDAY;
            case TUESDAY:
                return DayType.TUESDAY;
            case WEDNESDAY:
                return DayType.WEDNESDAY;
            case THURSDAY:
                return DayType.THURSDAY;
            case FRIDAY:
                return DayType.FRIDAY;
            case SATURDAY:
                return DayType.SATURDAY;
            case SUNDAY:
                return DayType.SUNDAY;
        }
        return null;
    }
}
