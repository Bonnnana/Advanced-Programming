// 3.

package FirstPartialExam.YEAR_2023;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

interface Item {
    String getTitle();

    List<String> getGenres();

    double getRating();

}

class Movie implements Item {
    String title;
    List<String> genres;
    List<Integer> ratings;

    public Movie() {
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public Movie(String title, List<String> genres, List<Integer> ratings) {
        this.title = title;
        this.genres = genres;
        this.ratings = ratings;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getGenres() {
        return genres;
    }

    @Override
    public double getRating() {
        double avg = ratings.stream()
                .mapToInt(el -> el)
                .average()
                .orElse(0.0);

        return avg * Math.min(ratings.size() / 20.0, 1.0);
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f", title, getRating());
    }
}

class Serie implements Item {
    String title;
    List<String> genres;
    List<List<Integer>> ratingsOfEveryEpisode;

    public Serie() {
        genres = new ArrayList<>();
        ratingsOfEveryEpisode = new ArrayList<>();
    }

    public Serie(String title, List<String> genres, List<List<Integer>> ratingsOfEveryEpisode) {
        this.title = title;
        this.genres = genres;
        this.ratingsOfEveryEpisode = ratingsOfEveryEpisode;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getGenres() {
        return genres;
    }

    double getRatingOfEp(List<Integer> list) {
        double avg = list.stream()
                .mapToDouble(el -> el)
                .average()
                .orElse(0.0);

        return avg * Math.min(list.size() / 20.0, 1.0);


    }

    @Override
    public double getRating() {
        return ratingsOfEveryEpisode.stream()
                .mapToDouble(el -> getRatingOfEp(el))
                .boxed()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)", title, getRating(), ratingsOfEveryEpisode.size());
    }
}

class StreamingPlatform {

    List<Item> items;

    public StreamingPlatform() {
        items = new ArrayList<>();
    }

    public void addItem(String data) {
        String[] parts = data.split(";");
        String title = parts[0];
        List<String> genres = List.of(parts[1].split(","));
        List<List<Integer>> ratingdOfSerie = new ArrayList<>();
        List<Integer> ratingsOfMovie = new ArrayList<>();


        for (int i = 2; i < parts.length; i++) {
            if (parts[i].startsWith("S")) {
                List<Integer> ratingsOfEpisode = Arrays.stream(parts[i].split("\\s+"))
                        .skip(1)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                ratingdOfSerie.add(ratingsOfEpisode);
            } else {
                ratingsOfMovie = Arrays.stream(parts[i].split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }
        }

        Item item;
        if (parts[2].startsWith("S"))
            item = new Serie(title, genres, ratingdOfSerie);
        else
            item = new Movie(title, genres, ratingsOfMovie);

        items.add(item);
    }

    void listAllItems(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        items.stream()
                .sorted(Comparator.comparing(Item::getRating).reversed())
                .forEach(pw::println);

        pw.flush();
    }
    void listFromGenre (String genre, OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        items.stream()
                .filter(item -> item.getGenres().contains(genre))
                .sorted(Comparator.comparing(Item::getRating).reversed())
                .forEach(pw::println);

        pw.flush();
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}
