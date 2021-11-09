package com.pinkmoon.flux;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FluxDate {

    // calendar method
    public static String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // calendar method
    public static ArrayList<String> daysInMonthArray(LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1); // get first day of the month
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i < 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add(" "); // we add a blank
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    public static String formatDateForDB(LocalDate date, String dayNum){
        LocalDate moddedDate = date.withDayOfMonth(Integer.parseInt(dayNum));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return moddedDate.format(formatter);
    }

    public static String convertToLocalTime(String assignmentDueDate) {
        ZoneId localTimeZone = ZoneId.systemDefault();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        String localDBFormat;
        if(assignmentDueDate != null){
            localDBFormat = Instant.parse(assignmentDueDate)
                    .atZone(localTimeZone)
                    .format(dtf);
        }else{
            localDBFormat = "";
        }
        return localDBFormat;
    }

    public static String formatDatePretty(String dateTimeString){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("EEEE MMM d, yyyy - HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    public static String extractDayFromDate(String dateTimeString){
        if (dateTimeString != null) {
            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = (Date) parser.parse(dateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat formatter = new SimpleDateFormat("d");
            if(date != null) {
                String day = formatter.format(date);
                return day;
            }else{
                return "";
            }
        } else {
            return "";
        }
    }

    public static String extractMonthFromDate(String dateTimeString){
        if (dateTimeString != null) {
            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = (Date) parser.parse(dateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat formatter = new SimpleDateFormat("MMMM");
            if(date != null) {
                String month = formatter.format(date);
                return month;
            }else{
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Used to convert a date in String format to a Calendar object.
     * This object can then be used to set notifications to trigger
     * at a designated time in the future, by converting the Calendar
     * object into milliseconds.
     * @param dateTime - date string to convert: format of yyyy-MN-dd HH:mm:ss
     * @return a Calendar object from the passed in parameter.
     */
    public static Calendar convertToDateTime(String dateTime){
        Calendar convertedDateTime = Calendar.getInstance();

        String[] dateTimeSplit = dateTime.split(" ");

        String[] dateSplit = dateTimeSplit[0].split("-");
        String[] timeSplit = dateTimeSplit[1].split(":");

        convertedDateTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]) - 1); // -1 because months are 0th indexed
        convertedDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[2]));
        convertedDateTime.set(Calendar.YEAR, Integer.parseInt(dateSplit[0]));

        convertedDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
        convertedDateTime.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
        convertedDateTime.set(Calendar.SECOND, 0);

        return convertedDateTime;
    }
}
