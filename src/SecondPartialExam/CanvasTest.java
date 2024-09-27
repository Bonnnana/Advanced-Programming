// 8.

package SecondPartialExam;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class InvalidIDException extends Exception {
    InvalidIDException(String id) {
        super("ID " + id + " is not valid");
    }

}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }
}

interface Scalable {
    void scaleShape(double coef);
}

interface Shape extends Scalable {
    public double getArea();

    public double getPerimeter();
}

class Circle implements Shape {
    double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void scaleShape(double coef) {
        radius *= coef;

    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return Math.PI * (2 * radius);
    }
    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                radius,
                getArea(),
                getPerimeter());
    }
}

class Square implements Shape {
    double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public void scaleShape(double coef) {
        side *= coef;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public double getPerimeter() {
        return 4 * side;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                side,
                getArea(),
                getPerimeter());
    }
}

class Rectangle implements Shape {
    double sideA;
    double sideB;

    public Rectangle(double sideA, double sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
    }

    @Override
    public void scaleShape(double coef) {
        sideA *= coef;
        sideB *= coef;
    }

    @Override
    public double getArea() {
        return sideA * sideB;
    }

    @Override
    public double getPerimeter() {
        return (2 * sideA) + (2 * sideB);

    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                sideA,
                sideB,
                getArea(),
                getPerimeter());
    }
}

class Canvas {
    Map<String, List<Shape>> shapesByUser;
    Set<Shape> allShapes;

    public Canvas() {
        shapesByUser = new HashMap<>();
        allShapes = new TreeSet<>(Comparator.comparing(Shape::getArea));
    }

    void readShapes(InputStream is) throws IOException, InvalidDimensionException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            Shape shape = null;
            String[] parts = line.split("\\s+");
            int type = Integer.parseInt(parts[0]);
            String studentID = parts[1];
            try {
                if (studentID.length() != 6 || !isValid(studentID))
                    throw new InvalidIDException(studentID);
            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
                continue;
            }

            double dimension1 = Double.parseDouble(parts[2]);
            if (dimension1 == 0)
                throw new InvalidDimensionException();

            if (type == 1)
                shape = new Circle(Double.parseDouble(parts[2]));
            if (type == 2)
                shape = new Square(Double.parseDouble(parts[2]));
            if (type == 3) {
                double dimension2 = Double.parseDouble(parts[3]);
                if (dimension2 == 0)
                    throw new InvalidDimensionException();
                shape = new Rectangle(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            }

            shapesByUser.putIfAbsent(studentID, new ArrayList<>());
            if (shape != null) {
                shapesByUser.get(studentID).add(shape);
                allShapes.add(shape);
            }
        }


    }

    private boolean isValid(String studentID) {
        return studentID.chars()
                .allMatch(Character::isLetterOrDigit);
    }

    void scaleShapes(String userID, double coef) {
        if(shapesByUser.get(userID)!=null) {
            shapesByUser.get(userID).stream()
                    .forEach(s -> s.scaleShape(coef));
        }

    }

    void printAllShapes(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        allShapes.forEach(pw::println);
        pw.flush();

    }

    void printByUserId (OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        Comparator<Map.Entry<String, List<Shape>>> comparator= Comparator.comparing((Map.Entry<String, List<Shape>> entry) ->entry.getValue().size()).reversed()
                .thenComparing(entry-> entry.getValue().stream().mapToDouble(Shape::getArea).sum());

       Map<String, Set<Shape>>  sorted = shapesByUser.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry-> entry.getValue().stream()
                                .collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Shape::getPerimeter)))),
                        (old, newValue )-> old,
                        LinkedHashMap::new
                ));


        for(Map.Entry<String, Set<Shape>> entry: sorted.entrySet()){
            pw.printf("Shapes of user: %s\n", entry.getKey());
            entry.getValue().stream().forEach(pw::println);
        }

        pw.flush();


    }

    void statistics (OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics areaStats = new DoubleSummaryStatistics();

        shapesByUser.values().stream()
                .flatMap(List::stream)
                .forEach(shape-> areaStats.accept(shape.getArea()));

        pw.printf("count: %d\n", areaStats.getCount());
        pw.printf("sum: %.2f\n", areaStats.getSum());
        pw.printf("min: %.2f\n", areaStats.getMin());
        pw.printf("average: %.2f\n", areaStats.getAverage());
        pw.printf("max: %.2f\n", areaStats.getMax());
        pw.flush();

    }


}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidDimensionException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}