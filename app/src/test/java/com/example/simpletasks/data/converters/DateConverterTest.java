package com.example.simpletasks.data.converters;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateConverterTest {

    private DateConverter dateConverter;
    private long dateAsLong;
    private Date date;

    @Before
    public void setUp() {
        dateConverter = new DateConverter();
        dateAsLong = Date.UTC(2000, 12, 5, 9, 29, 40);
        date = new Date(dateAsLong);
    }

    @Test
    public void testDateToLong() {
        final Long actualLong = dateConverter.dateToLong(date);

        assertEquals(dateAsLong, (long) actualLong);
    }

    @Test
    public void testLongToDate() {
        final Date actualDate = dateConverter.longToDate(dateAsLong);

        assertEquals(date, actualDate);
    }
}