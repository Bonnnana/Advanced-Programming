// 10.

package SecondPartialExam;// 10.

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

interface ILocation {
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}


class UserAlreadyExistException extends Exception {
    UserAlreadyExistException(String id) {
        super("User with id " + id + " already exists");
    }
}

class User implements Comparable<User> {
    String id;
    String name;
    Set<ILocation> locations;
    LocalDateTime timestamp;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        locations = new HashSet<>();
        timestamp = null;
    }

    public void addLocations(List<ILocation> locations) {
        this.locations.addAll(locations);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<ILocation> getUserLocations() {
        return locations;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(User o) {
        Comparator<User> comparator = Comparator.comparing(User::getName).thenComparing(User::getId);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getName(), getId(), getTimestamp());
    }

    public String notWholeIdFormat() {
        return String.format("%s %s***", name, id.substring(0, 4));
    }
}

class StopCoronaApp {

    Map<String, User> usersRegistered;  //key=ID, value=name
    Comparator<Map.Entry<User, Integer>> directContactsComparator = Comparator.comparing((Map.Entry<User, Integer> entry) -> entry.getValue()).reversed()
            .thenComparing(Map.Entry::getKey);

    public StopCoronaApp() {
        this.usersRegistered = new HashMap<>();
    }

    void addUser(String name, String id) throws UserAlreadyExistException {
        if (usersRegistered.containsKey(id))
            throw new UserAlreadyExistException(id);

        usersRegistered.put(id, new User(id, name));

    }

    void addLocations(String id, List<ILocation> iLocations) {
        usersRegistered.get(id).addLocations(iLocations);
    }

    void detectNewCase(String id, LocalDateTime timestamp) {

        usersRegistered.get(id).setTimestamp(timestamp);

    }

    Map<User, Integer> getDirectContacts(User u) {
        Set<ILocation> uLocations = u.getUserLocations();

        return usersRegistered.values().stream()
                .filter(otherUser -> !otherUser.equals(u))
                .collect(Collectors.toMap(
                        otherUser -> otherUser,
                        otherUser -> (int) uLocations.stream()
                                .flatMap(loc1 -> otherUser.getUserLocations().stream()
                                        .filter(loc2 -> getLocationDistance(loc1, loc2) <= 2 && getTimeDistance(loc1, loc2) <= 5))
                                .count(),
                        (oldValue, newValue) -> oldValue,
                        HashMap::new
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(directContactsComparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

    }

    //    Map<User, Integer> getDirectContacts(User u) {
//        Map<User, Integer> directContacts = new HashMap<>();
//        Set<ILocation> uLocations = u.getUserLocations();
//
//        for (User otherUser : usersRegistered.values()) {
//            if (!otherUser.equals(u)) {
//                int contactCount = 0;
//                Set<ILocation> otherLocations = otherUser.getUserLocations();
//
//                for (ILocation loc1 : uLocations) {
//                    for (ILocation loc2 : otherLocations) {
//                        if (getLocationDistance(loc1, loc2) <= 2 && getTimeDistance(loc1, loc2) <= 5) {
//                            contactCount++;
//                        }
//                    }
//                }
//                if (contactCount > 0) {
//                    directContacts.put(otherUser, contactCount);
//                }
//            }
//        }
//        return directContacts.entrySet().stream()
//                .sorted(directContactsComparator)
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (old, newValue) -> old,
//                        LinkedHashMap::new
//                ));
//    }
    private double getLocationDistance(ILocation u1, ILocation u2) {
        double dx = u1.getLongitude() - u2.getLongitude();
        double dy = u1.getLatitude() - u2.getLatitude();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private long getTimeDistance(ILocation u1, ILocation u2) {
        LocalDateTime lt1 = u1.getTimestamp();
        LocalDateTime lt2 = u2.getTimestamp();

        Duration duration = Duration.between(lt1, lt2);

        return Math.abs(duration.toMinutes());
    }

    Collection<User> getIndirectContacts(User u) {
        Set<User> directContacts = new HashSet<>(getDirectContacts(u).keySet());

        return directContacts.stream()
                .flatMap(directContact -> getDirectContacts(directContact).keySet().stream())
                .filter(contact -> !contact.equals(u) && !directContacts.contains(contact))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    void createReport() {
        List<User> userInfected = usersRegistered.values().stream()
                .filter(user -> user.getTimestamp() != null)
                .sorted(Comparator.comparing(User::getTimestamp))
                .collect(Collectors.toList());

        for (User infected : userInfected) {
            Map<User, Integer> directContacts = getDirectContacts(infected);

            System.out.println(infected);
            System.out.println("Direct contacts:");
            directContacts.entrySet().stream()
                    .forEach(entry -> {
                        User contact = entry.getKey();
                        int count = entry.getValue();
                        System.out.println(contact.notWholeIdFormat() + " " + count);
                    });

            System.out.println("Count of direct contacts: " + directContacts.values().stream().mapToInt(i -> i).sum());

            Collection<User> indirectContacts = getIndirectContacts(infected);
            System.out.println("Indirect contacts:");
            indirectContacts
                    .forEach(contact -> System.out.println(contact.notWholeIdFormat()));

            System.out.println("Count of indirect contacts: " + indirectContacts.size());

        }

        double directContacts = userInfected.stream().mapToInt(x -> getDirectContacts(x).values().stream().mapToInt(i -> i).sum()).sum();
        double indirectContacts = userInfected.stream().mapToInt(x -> getIndirectContacts(x).size()).sum();

        System.out.printf("Average direct contacts: %.4f%n", directContacts / userInfected.size());
        System.out.printf("Average indirect contacts: %.4f%n", indirectContacts / userInfected.size());
    }

}


public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}

