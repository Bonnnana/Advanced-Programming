// 2.

package FirstPartialExam;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String id, double area) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, area));
    }
}

interface Shape {
    double calculateArea();
}

class Square implements Shape {
    int side;

    public Square(int side) {
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * side;
    }
}

class Circle implements Shape {
    int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * (radius * radius);
    }
}

class Canvas implements Comparable<Canvas> {

    String canvasID;
    List<Square> squares;
    List<Circle> circles;
    List<Shape> allShapes;

    public Canvas(String canvasID, List<Square> squares, List<Circle> circles, List<Shape> allShapes) {
        this.canvasID = canvasID;
        this.squares = squares;
        this.circles = circles;
        this.allShapes = allShapes;
    }

    public static Canvas createCanvas(double maxArea, String line) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");
        String id = parts[0];

        List<Square> squaress = new ArrayList<>();
        List<Circle> circless = new ArrayList<>();
        List<Shape> allShapess = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            String type = parts[i];
            int dimension = Integer.parseInt(parts[i + 1]);

            Shape shape = null;
            if (type.equals("C"))
                shape = new Circle(dimension);
            else if (type.equals("S"))
                shape = new Square(dimension);

            if (shape.calculateArea() > maxArea)
                throw new IrregularCanvasException(id, maxArea);

            allShapess.add(shape);
            i++;
        }
        circless = allShapess.stream()
                .filter(shape -> shape instanceof Circle)
                .map(shape -> (Circle) shape)
                .collect(Collectors.toList());

        squaress = allShapess.stream()
                .filter(shape -> shape instanceof Square)
                .map(shape -> (Square) shape)
                .collect(Collectors.toList());

        return new Canvas(id, squaress, circless, allShapess);
    }

    public double maxArea() {
        Shape maxAreaShape = allShapes.stream()
                .max(Comparator.comparing(Shape::calculateArea))
                .orElse(null);

        if (maxAreaShape != null)
            return maxAreaShape.calculateArea();
        return 0;
    }

    public double minArea() {
        Shape minAreaShape = allShapes.stream()
                .min(Comparator.comparing(Shape::calculateArea))
                .orElse(null);

        if (minAreaShape != null)
            return minAreaShape.calculateArea();
        return 0;
    }

    public double averageArea() {
        double sum = allShapes.stream().mapToDouble(shape -> shape.calculateArea()).sum();
        return sum / allShapes.size();
    }

    public double sumOfAreas() {
        return allShapes.stream().mapToDouble(Shape::calculateArea).sum();
    }

    @Override
    public int compareTo(Canvas other) {
        return Double.compare(other.sumOfAreas(), this.sumOfAreas());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                canvasID,
                allShapes.size(),
                circles.size(),
                squares.size(),
                minArea(),
                maxArea(),
                averageArea());
    }
}

class ShapesApplication {
    List<Canvas> canvases;
    double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    public void readCanvases(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        canvases = br.lines().map(line -> {
                    try {
                        return Canvas.createCanvas(maxArea, line);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(canvas -> canvas != null)
                .collect(Collectors.toList());


    }

    public void printCanvases(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        canvases.stream()
                .sorted()
                .forEach(pw::println);
        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}