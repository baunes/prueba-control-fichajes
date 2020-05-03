package com.prueba.controlfichajes.tests;

import org.exparity.hamcrest.date.ZonedDateTimeMatchers;
import org.exparity.hamcrest.date.core.TemporalMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TemporalStringMatcher extends BaseMatcher<String> {

    private final TemporalMatcher<ZonedDateTime> matcher;

    private TemporalStringMatcher(String dateTime) {
        this.matcher = ZonedDateTimeMatchers.within(0, ChronoUnit.SECONDS, ZonedDateTime.parse(dateTime));
    }

    @Override
    public boolean matches(Object actual) {
        return this.matcher.matches(ZonedDateTime.parse((String) actual));
    }

    @Override
    public void describeTo(Description description) {
        this.matcher.describeTo(description);
    }

    public static TemporalStringMatcher match(String dateTime) {
        return new TemporalStringMatcher(dateTime);
    }
}
