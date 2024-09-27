// 6.

package FirstPartialExam;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

interface Shape extends Scalable, Stackable {
    String getId();

    Color getColor();
}

class Circle implements Shape {
    private String id;
    private Color color;
    private float radius;

    public Circle(String id, Color color, float radius) {
        this.id = id;
        this.color = color;
        this.radius = radius;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", id, color.toString(), weight());
    }
}

class Rectangle implements Shape {

    private String id;
    private Color color;
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        this.id = id;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void scale(float scaleFactor) {
        height *= scaleFactor;
        width *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color.toString(), weight());
    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    void add(String id, Color color, float radius) {
        Circle circle = new Circle(id, color, radius);
        addShape(circle);

    }

    void add(String id, Color color, float width, float height) {
        Rectangle rectangle = new Rectangle(id, color, width, height);
        addShape(rectangle);
    }

    public void addShape(Shape shape) {

        if (shapes.isEmpty()) {
            shapes.add(shape);
            return;

        }

        for (Shape s : shapes) {
            if (shape.weight() > s.weight()) {
                int index = shapes.indexOf(s);
                shapes.add(index, shape);
                return;
            }
        }
        shapes.add(shape);
    }

    public void scale(String id, float ScaleFactor) {
        Shape shape = shapes.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);

        if (shape != null) {
            shapes.remove(shape);
            shape.scale(ScaleFactor);
            addShape(shape);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        shapes.forEach(shape -> sb.append(shape));
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

