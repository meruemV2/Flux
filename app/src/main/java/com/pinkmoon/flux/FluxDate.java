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

/**
 * This class contains several methods to work on and manipulate date-sting objects.
 */
public class FluxDate {

    // calendar method
    /**
     * Converts a LocalDate object into a String with selected month year only.
     * @param date selected date by the user (on the calendar topmost view)
     * @return string of format: MMMM yyyy (month and year; ex: November 2021)
     */
    public static String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // calendar method

    /**
     * Converts a LocalDate object into a String with selected month only.
     * @param date selected date by the user (on the calendar topmost view)
     * @return string of format: MMMM (full month name; ex: November)
     */
    public static String monthFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }

    // calendar method
    /**
     * Converts a LocalDate object into a String with selected year-month only.
     * @param date selected date by the user (on the calendar topmost view)
     * @return string of format: yyyy-MM (ex: 2021-11 for November, 2021)
     */
    public static String yearMonthFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return date.format(formatter);
    }

    // calendar method

    /**
     * Calculates the number of days in a month, and sets an ArrayList of empty
     * spaces based on the fixed size of 42; the order of the indices of the days
     * within the List will correspond to a Calendar aligned with the days of the week.
     * If a month does not contain certain days of the week, those indices will be denoted
     * by an empty string.
     * @param selectedDate selected date by the user (on the calendar topmost view)
     * @return list of spaces & days of the month in an ArrayList.
     */
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

    /**
     * Takes a LocalDate object, and converts it to a String of the format we use
     * to store dates in our database.
     * @param date local date object (likely selected by the user)
     * @param dayNum the day of the month, selected by the user upon tapping on the Calendar
     * @return a string date object of the format: yyyy-MM-dd
     */
    public static String formatDateForDB(LocalDate date, String dayNum){
        LocalDate moddedDate = date.withDayOfMonth(Integer.parseInt(dayNum));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return moddedDate.format(formatter);
    }

    /**
     * Takes a date String with a time-zone format (in this case, Canvas likes to use the
     * Zulu timezone I believe), converts it to local time (based on the device time zone,
     * set by the user's device).
     * @param assignmentDueDate generic time-zoned string date
     * @return local time zone date string
     */
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

    /**
     * Takes a string date object from our local database, and makes it "pretty" (i.e.: human readable).
     * @param dateTimeString date of the form yyyy-MM-dd HH:mm:ss
     * @return String of the form EEEE MMM d, yyyy - HH:mm:ss (i.e.: Thursday October 15, 2021 - 05:00:00)
     */
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

    public static String formatDateTimeFromMDPToDBFormat(String dateTimeString){
        DateFormat parser = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    /**
     * Takes in a string date object, extracts the day of the month from it.
     * @param dateTimeString string of the format yyyy-MM-dd
     * @return a string with the day of the month
     */
    public static String extractDayFromDate(String dateTimeString){
        if (!dateTimeString.isEmpty()) {
            String dateString = dateTimeString.split(" ")[0];
            /*
                Guess it's worth explaining the below line of code:
                Integer.parseInt(dateString.split("-")[2]) - we want to parse the string date,
                as an integer--this will drop our leading zero on single digit days (1-9) of the month
                We wrap the above using String.valueOf(), because we want to turn it back into a String.
                The reason we do all of the above, is because our CalendarView is set up to handle
                days in single digit format (for those that are 1-9, instead of 01, 02, 03, etc). Thus,
                if we attempt to fill the calendar within the setDueDateIndicator function without
                doing the above, we will never get passed the first if statement for days of the month
                1-9.
             */
            return String.valueOf(Integer.parseInt(dateString.split("-")[2])); // string array looks like: [year, month, day]
        }else{
            return "";
        }
    }

    // TODO: Consider getting rid of this function, as it may not be useful
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

    public static String singleDigitToDouble(int singleDigitTime){
        return "0" + singleDigitTime;
    }

    public static boolean isSingleDigitTime(String time){
        return time.length() <= 1;
    }

    /**
     * Takes a LocalDate object, and converts it to a String of the format we use
     * to store dates in our database.
     * @param date local date object (likely selected by the user)
     * @param dayNum the day of the month, selected by the user upon tapping on the Calendar
     * @return a string date object of the format: yyyy-MM-dd
     */
    public static String formatDateForDB(Calendar c){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        parser.setCalendar(c);
        return parser.format(c.getTime());
    }
}
