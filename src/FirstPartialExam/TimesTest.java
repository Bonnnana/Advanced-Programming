// 7.

package FirstPartialExam;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String line) {
        super(String.format("%s", line));
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String line) {
        super(String.format("%s", line));

    }
}

class TimeTable {
    List<LocalTime> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    public void readTimes(InputStream in) throws IOException, UnsupportedFormatException, InvalidTimeException {
        Scanner sc = new Scanner(in);


        while (sc.hasNext()) {
            String time = sc.next();
            if (!(time.contains(":")) && !(time.contains(".")))
                throw new UnsupportedFormatException(time);
            String[] parts = time.split("[:.]");
            int hour = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23)
                throw new InvalidTimeException(time);
            if (minutes < 0 || minutes > 59)
                throw new InvalidTimeException(time);

            LocalTime lt = LocalTime.of(hour, minutes);
            times.add(lt);

        }
    }

    public void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);

        DateTimeFormatter dtf = null;
        if (format.equals(TimeFormat.FORMAT_24))
            dtf = DateTimeFormatter.ofPattern("H:mm");
        if (format.equals(TimeFormat.FORMAT_AMPM))
            dtf = DateTimeFormatter.ofPattern("h:mm a");

        DateTimeFormatter finalDtf = dtf;

        int maxLength = times.stream()
                .map(time -> String.format("%s", time.format(finalDtf)))
                .mapToInt(String::length)
                .max()
                .orElse(0);

        times.stream()
                .sorted()
                .forEach(time -> {
                    String formattedTime = time.format(finalDtf);
                    String alignedTime = String.format("%" + maxLength + "s", formattedTime);  //%8s
                    pw.println(alignedTime);
                });
        pw.flush();
    }

}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        } catch (IOException e) {
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24,
    FORMAT_AMPM
}