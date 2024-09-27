// 1.

package FirstPartialExam;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int side;

    public Square(int size) {
        this.side = size;
    }

    public int getPerimetar(){
        return 4* side;
    }
}

class Canvas implements Comparable<Canvas> {
    String canvasID;
    List<Square> squares;

    public Canvas(String canvasID, List<Square> squares) {
        this.canvasID = canvasID;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];

        List<Square> squares = Arrays.stream(parts)
                .skip(1)
                .map(Integer::parseInt)
                .map(Square::new)
                .collect(Collectors.toList());

        return new Canvas(id, squares);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",
                canvasID,
                squares.size(),
                sumOfPerimetar()
        );
    }

    private int sumOfPerimetar(){
        return  squares.stream().mapToInt(Square::getPerimetar).sum();
    }
    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.sumOfPerimetar(), o.sumOfPerimetar());
    }
}

class ShapesApplication {

    List<Canvas> canvases;

    public int readCanvases(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        canvases = bufferedReader.lines().map(Canvas::createCanvas).collect(Collectors.toList());

        return canvases.stream()
                .mapToInt(canvas-> canvas.squares.size())
                .sum();

    }

    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter writer = new PrintWriter(out);

        Canvas max=canvases.stream().max(Comparator.naturalOrder()).get();

        writer.println(max);
        writer.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}