// 18.

package SecondPartialExam;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here

class Cluster<T extends Point2D> {
    List<T> elements;

    public Cluster() {
        this.elements = new ArrayList<>();
    }

    void addItem(T element) {
        elements.add(element);
    }

    void near(long id, int top) {
        T element = elements.stream().filter(el -> el.getId() == id).findFirst().orElse(null);

        Map<Long, Double> map = elements.stream()
                .filter(el -> el.getId() != id)
                .collect(Collectors.toMap(
                        Point2D::getId,
                        el -> element.distanceBetweenPoints(el)
                ));

        Map<Long, Double> sorted = map.entrySet().stream()
                .sorted(Entry.comparingByValue())
                .limit(top)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (old, newer) -> old,
                        LinkedHashMap::new
                ));

        int i = 1;
        for (Entry<Long, Double> entry : sorted.entrySet()) {
            System.out.println(String.format("%d. %d -> %.3f", i++, entry.getKey(), entry.getValue()));
        }

    }
}

class Point2D {
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double distanceBetweenPoints(Point2D other) {
      double dx = this.x - other.x;
      double dy = this.y - other.y;
      return Math.sqrt(dx * dx + dy * dy);
    }


}