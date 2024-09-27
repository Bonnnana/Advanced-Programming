// 2.

package SecondPartialExam.YEAR_2023;

import java.util.*;
import java.util.stream.Collectors;

class User implements Comparable<User> {
    String id;
    String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("User ID: %s Name: %s", id, name);
    }

    @Override
    public int compareTo(User o) {
        int id1 = Integer.parseInt(this.getId());
        int id2 = Integer.parseInt(o.getId());
        return Integer.compare(id1, id2);
    }
}

class Movie  implements Comparable<Movie>{
    String id;
    String name;
    List<Integer> ratings;
    Map<String, Integer> ratingByUser; // key -> userID

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
        ratings = new ArrayList<>();
        ratingByUser = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRating(int rating) {
        ratings.add(rating);
    }

    public void addUserRating(String userId, int rating){
        ratingByUser.put(userId,rating);
    }
    public double getAverageRating() {
        return ratings.stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);
    }

    public Map<String, Integer> getRatingByUser() {
        return ratingByUser;
    }

    @Override
    public String toString() {
        return String.format("Movie ID: %s Title: %s Rating: %.2f", id, name, getAverageRating());
    }


    @Override
    public int compareTo(Movie o) {
        Comparator<Movie> comparator = Comparator.comparing(Movie::getAverageRating).reversed()
                .thenComparing(Movie::getId);
        return comparator.compare(this,o);
    }
}

class StreamingPlatform {
    Map<String, Movie> moviesOnPlatform;
    Map<String, User> users;

    public StreamingPlatform() {
        moviesOnPlatform = new HashMap<>();
        users = new HashMap<>();
    }

    public void addMovie(String id, String name) {
        moviesOnPlatform.put(id, new Movie(id, name));
    }

    public void addUser(String id, String username) {
        users.put(id, new User(id, username));
    }

    public void addRating(String userId, String movieId, int rating) {
        moviesOnPlatform.get(movieId).setRating(rating);
        moviesOnPlatform.get(movieId).addUserRating(userId,rating);
    }

    public void topNMovies(int n) {
        moviesOnPlatform.values().stream()
                .sorted(Comparator.comparingDouble(Movie::getAverageRating).reversed())
                .limit(n)
                .forEach(System.out::println);
    }

    public void favouriteMoviesForUsers(List<String> userIds) {
        for(String userId: userIds){
            User user = users.get(userId);

            List<Movie> favMovies = moviesOnPlatform.values().stream()
                    .filter(movie->movie.ratingByUser.containsKey(userId))
                    .sorted()
                    .collect(Collectors.groupingBy(movie -> movie.ratingByUser.get(userId)))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByKey())  // find best rating - [1-10]
                    .map(Map.Entry::getValue)
                    .orElse(Collections.emptyList());

            System.out.println(user);
            favMovies.forEach(System.out::println);
            System.out.println();
        }
    }


    public void similarUsers(String userId) {
        Map<String, Integer> targetUserRatings = getUserRatings(userId);
        List<Map.Entry<String, Double>> userSimilarities = new ArrayList<>();

        for(String otherUser:users.keySet()){
            if(!otherUser.equals(userId)){
                Map<String, Integer> otherUserRatings = getUserRatings(otherUser);
                double similarity = CosineSimilarityCalculator.cosineSimilarity(targetUserRatings, otherUserRatings);
                userSimilarities.add(new AbstractMap.SimpleEntry<>(otherUser, similarity));
            }
        }

        userSimilarities.sort(Map.Entry.<String, Double>comparingByValue().reversed());

        for(Map.Entry<String, Double> entry : userSimilarities){
            User user = users.get(entry.getKey());
            System.out.println(user +" "+ entry.getValue());

        }
    }

    private Map<String, Integer> getUserRatings(String userId) {
       return moviesOnPlatform.values().stream()
                .collect(Collectors.toMap(
                        Movie::getId,
                        movie->movie.getRatingByUser().getOrDefault(userId, 0)
                ));
    }


}

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}


public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id, name);
            } else if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id, name);
            } else if (parts[0].equals("addRating")) {
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")) {
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
