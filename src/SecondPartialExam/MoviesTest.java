// 24.

package SecondPartialExam;

import java.util.*;
import java.util.stream.Collectors;


class Movie{
    String title;
    int[] ratings;

    public Movie() {

    }

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public int getNumOfRatings() {
        return ratings.length;
    }

    public double getAvgRating() {
        return Arrays.stream(ratings)
                .average()
                .orElse(0.0);
    }

    public double getRatingCoef(int maxNumOfRatings) {
        return getAvgRating() * getNumOfRatings() / maxNumOfRatings;
    }

  @Override
  public String toString() {
    return String.format("%s (%.2f) of %d ratings", title, getAvgRating(), getNumOfRatings());
  }
}

class MoviesList {
    List<Movie> movies;


    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        Movie movie = new Movie(title, ratings);
        movies.add(movie);
    }


    public List<Movie> top10ByAvgRating() {
        Comparator<Movie> avgRatingComparator = Comparator.comparing(Movie::getAvgRating).reversed()
                .thenComparing(Movie::getTitle);

        return movies.stream()
                .sorted(avgRatingComparator)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int maxRatings = movies.stream().mapToInt(Movie::getNumOfRatings).sum();

        Comparator<Movie> ratingCoefComparator = (a, b) -> {
          int res = Double.compare(b.getRatingCoef(maxRatings), a.getRatingCoef(maxRatings));
          if (res ==0)
            return a.getTitle().compareTo(b.getTitle());
          else
            return res;

        };

        return movies.stream()
                .sorted(ratingCoefComparator)
                .limit(10)
                .collect(Collectors.toList());


    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

