// 1.

package SecondPartialExam.YEAR_2023;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Car{
    String registration;
    String spot;
    LocalDateTime start;
    LocalDateTime end;
    boolean entry;

    public Car(String registration, String spot) {
        this.registration = registration;
        this.spot = spot;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public LocalDateTime getEndTime() {
        return end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isEntry() {
        return entry;
    }

    @Override
    public String toString() {
        return String.format("Registration number: %s Spot: %s Start timestamp: %s",
                registration,
                spot,
                start.toString()
                );
    }
}

class Parking{
    List<Car> carsOnParking;
    List<Car> history;
    Map<String, Integer> carStatistics;
    int capacity;

    public Parking(int capacity) {
        carsOnParking = new ArrayList<>();
        this.capacity = capacity;
        carStatistics = new TreeMap<>();
        history = new ArrayList<>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry){
        Car car = new Car(registration, spot);

        if(entry){
            car.setStart(timestamp);
            carsOnParking.add(car);
            carStatistics.merge(registration, 1, Integer::sum);

        } else{
            car = carsOnParking.stream()
                    .filter(c-> c.getRegistration().equals(registration))
                    .findFirst()
                    .orElse(null);
            if(car!=null){
                car.setEnd(timestamp);
                carsOnParking.remove(car);
                history.add(car);
            }

        }

    }

    public void currentState(){
        double percent = (double) (carsOnParking.size() * 100) /capacity;
        System.out.println(String.format("Capacity filled: %.2f%%", percent));

        carsOnParking.stream()
                .sorted(Comparator.comparing(Car::getStartTime).reversed())
                .forEach(System.out::println);

    }
    public void history(){
        history.stream()
                .sorted(Comparator.comparing(car-> DateUtil.durationBetween(car.getStartTime(), car.getEndTime()), Comparator.reverseOrder()))
                .forEach(car-> {
                    long duration = DateUtil.durationBetween(car.getStartTime(), car.getEndTime());
                    System.out.println(String.format("%s End timestamp: %s Duration: %d minutes", car, car.getEndTime().toString(), duration));
                });

    }
    Map<String, Integer> carStatistics(){
        return carStatistics;
    }

    Map<String,Double> spotOccupancy (LocalDateTime start, LocalDateTime end){
        long totalDuration = DateUtil.durationBetween(start, end);
        Map<String, Long> occupancyMap = new HashMap<>();

        for(Car car: history){
            LocalDateTime carStart = car.getStartTime();
            LocalDateTime carEnd = car.getEndTime();

            if(carEnd.isAfter(start) && carStart.isBefore(end)){
                LocalDateTime overlapStart = carStart.isBefore(start)? start: carStart;
                LocalDateTime overlapEnd = carEnd.isAfter(end)? end:carEnd;
                long occupiedDuration = DateUtil.durationBetween(overlapStart, overlapEnd);

                occupancyMap.merge(car.getSpot(), occupiedDuration, Long::sum);
            }

        }
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Long> entry : occupancyMap.entrySet()) {
            double percentage = (entry.getValue()*100.0)/totalDuration;
            if (percentage < 100.0) {
                result.put(entry.getKey(), percentage);
            }
        }

        history.stream()
                .map(Car::getSpot)
                .distinct()
                .filter(spot -> !result.containsKey(spot))
                .forEach(spot -> result.put(spot, 0.0));

        return result;
       

    }
}
public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
