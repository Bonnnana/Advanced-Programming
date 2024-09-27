// 1.

package FirstPartialExam.YEAR_2023;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Participant {
    String id;
    LocalTime startTime;
    LocalTime endTime;
    private LocalTime duration;

    public Participant(String id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime.minusHours(startTime.getHour())
                .minusMinutes(startTime.getMinute())
                .minusSeconds(startTime.getSecond());
    }

    public static Participant createParticipant(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        LocalTime start = LocalTime.parse(parts[1]);
        LocalTime end = LocalTime.parse(parts[2]);

        return new Participant(id, start, end);
    }

    public long getDurationToSec() {
        return duration.toSecondOfDay();
    }

    public LocalTime getDuration() {
        return duration;
    }


    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return id + " " + duration.format(dtf);
    }
}

class TeamRace {
    public static void findBestTeam(InputStream is, OutputStream os) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter pw = new PrintWriter(os);

        List<Participant> team = br.lines()
                .map(Participant::createParticipant)
                .collect(Collectors.toList());

        List<Participant> bestTeam = team.stream()
                .sorted(Comparator.comparing(Participant::getDurationToSec))
                .limit(4)
                .collect(Collectors.toList());

        LocalTime lt = bestTeam.stream()
                .map(Participant::getDuration)
                .reduce(
                        LocalTime.of(0, 0, 0),
                        (a, b) -> a.plusHours(b.getHour())
                                .plusMinutes(b.getMinute())
                                .plusSeconds(b.getSecond())

                );

        bestTeam.forEach(pw::println);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        pw.println(lt.format(dtf));

        pw.flush();

    }
}

public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}