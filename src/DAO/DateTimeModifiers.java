package DAO;

import javafx.util.Pair;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;
public class DateTimeModifiers {
    private static ZoneId userZoneID = ZoneId.systemDefault();

    /**
     * Determines the current users timestamp
     * @return
     */
    public static java.sql.Timestamp getTimestamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp timeStamp = Timestamp.valueOf(localDateTime);
        return timeStamp;
    }

    /**
     * takes the current users timestamp and zoneID and converst to UTC time for proper database updates
     * @param date
     * @param time
     * @return
     * @throws ParseException
     */
    public static String convertUTC(String date, String time) throws ParseException {
        String timestamp = date + " " + time + ":00";
        DateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date timeStampLocal = dateFormatLocal.parse(timestamp);
        ZonedDateTime zoneTimeUTC = timeStampLocal.toInstant().atZone(ZoneOffset.UTC);
        String timeUTC = zoneTimeUTC.toString();
        String formatTimeUTC = timeUTC.replace("T"," ") + ":00";
        String formatTimeUTC2 = formatTimeUTC.replace("Z","");

        return formatTimeUTC2;
    }

    /**
     * takes UTC time from database and converts to users timezone
     * @param timestamp
     * @return
     * @throws ParseException
     */
    public static String convertToLocalTime(String timestamp) throws ParseException {
        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date timestampUTC = dateFormatUTC.parse(timestamp);
        String timeLocal = dateFormatLocal.format(timestampUTC);

        return timeLocal;
    }

    /**
     * Takes the SQL timestamp format and formats to other formats within the table
     * @param timestamp
     * @return
     */
    public static Pair<LocalDate, String> timestampToDateAndTime(String timestamp) {
        String[] split = timestamp.split("\\s+");
        LocalDate date = LocalDate.parse(split[0]);
        String time = split[1].substring(0,5);
        return new Pair<LocalDate, String>(date, time);
    }

    /**
     * Takes timestamp and convers to HH:mm format from UTC time
     * @param time
     * @return
     * @throws ParseException
     */
    public static String timeslotConverter(String time) throws ParseException {
        DateFormat timeFormatUTC = new SimpleDateFormat("HH:mm");
        DateFormat timeFormatLocal = new SimpleDateFormat("HH:mm");
        timeFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormatLocal.setTimeZone(TimeZone.getTimeZone(userZoneID));
        java.util.Date timeInUTC = timeFormatUTC.parse(time);
        String timeInLocal = timeFormatLocal.format(timeInUTC);
        return timeInLocal;
    }

    /**
     * determines the current date for tables and SQL updates/adds
     * @return
     */
    public static java.sql.Date getDate() {
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
        return date;
    }
}
