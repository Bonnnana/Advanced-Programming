package Labs.Lab_2.exercise_3;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}


interface Movable {
    int getCurrentXPosition();

    int getCurrentYPosition();

    void moveUp() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;
}

class MovablePoint implements Movable {

    private int x;
    private int y;

    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }


    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int new_y = y + ySpeed;
        int border = MovablesCollection.getY_MAX();
        if (new_y < 0 || new_y > border)
            throw new ObjectCanNotBeMovedException(x, new_y);
        y = new_y;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int new_x = x - xSpeed;
        int border = MovablesCollection.getX_MAX();
        if (new_x < 0 || new_x > border)
            throw new ObjectCanNotBeMovedException(new_x, y);
        x = new_x;

    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int new_x = x + xSpeed;
        int border = MovablesCollection.getX_MAX();
        if (new_x < 0 || new_x > border)
            throw new ObjectCanNotBeMovedException(new_x, y);
        x = new_x;

    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int new_y = y - ySpeed;
        int border = MovablesCollection.getY_MAX();
        if (new_y < 0 || new_y > border)
            throw new ObjectCanNotBeMovedException(x, new_y);
        y = new_y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }
}


class MovableCircle implements Movable {
    private int radius;
    private MovablePoint centerPoint;

    public MovableCircle(int radius, MovablePoint centerPoint) {
        this.radius = radius;
        this.centerPoint = centerPoint;
    }

    @Override
    public int getCurrentXPosition() {
        return centerPoint.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return centerPoint.getCurrentYPosition();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int new_y = centerPoint.getCurrentYPosition() + centerPoint.getySpeed();
        int border = MovablesCollection.getY_MAX();
        if (new_y < 0 || new_y > border)
            throw new ObjectCanNotBeMovedException(centerPoint.getCurrentXPosition(), new_y);
        centerPoint.setY(new_y);

    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int new_x = centerPoint.getCurrentXPosition() - centerPoint.getxSpeed();
        int border = MovablesCollection.getX_MAX();
        if (new_x < 0 || new_x > border)
            throw new ObjectCanNotBeMovedException(new_x, centerPoint.getCurrentYPosition());
        centerPoint.setX(new_x);
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int new_x = centerPoint.getCurrentXPosition() + centerPoint.getxSpeed();
        int border = MovablesCollection.getX_MAX();
        if (new_x < 0 || new_x > border)
            throw new ObjectCanNotBeMovedException(new_x, centerPoint.getCurrentYPosition());
        centerPoint.setX(new_x);
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int new_y = centerPoint.getCurrentYPosition() - centerPoint.getySpeed();
        int border = MovablesCollection.getY_MAX();
        if (new_y < 0 || new_y > border)
            throw new ObjectCanNotBeMovedException(centerPoint.getCurrentXPosition(), new_y);
        centerPoint.setY(new_y);
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + getCurrentXPosition() + "," + getCurrentYPosition() + ") and radius " + radius;
    }
}

class MovablesCollection {
    private Movable[] movable;
    private static int x_MAX;
    private static int y_MAX;

    public MovablesCollection(int xMAX, int yMAX) {
        this.movable = new Movable[0];
        x_MAX = xMAX;
        y_MAX = yMAX;
    }

    public static void setxMax(int x) {
        x_MAX = x;
    }

    public static void setyMax(int y) {
        y_MAX = y;
    }

    public static int getX_MAX() {
        return x_MAX;
    }

    public static int getY_MAX() {
        return y_MAX;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m instanceof MovableCircle) {
            MovableCircle circle = (MovableCircle) m;
            int x = circle.getCurrentXPosition();
            int y = circle.getCurrentYPosition();
            int radius = (circle.getRadius());

            if (x - radius < 0 || x + radius > x_MAX)
                throw new MovableObjectNotFittableException("Movable circle with center (" + x + "," + y + ") and radius " + radius + " can not be fitted into the collection");
            if (y - radius < 0 || y + radius > y_MAX)
                throw new MovableObjectNotFittableException("Movable circle with center (" + x + "," + y + ") and radius " + radius + " can not be fitted into the collection");

        } else if (m instanceof MovablePoint) {
            MovablePoint point = (MovablePoint) m;
            int x = point.getCurrentXPosition();
            int y = point.getCurrentYPosition();


            if (x < 0 || x > x_MAX)
                throw new MovableObjectNotFittableException(point + " can not be fitted into the collection");
            if (y < 0 || y > x_MAX)
                throw new MovableObjectNotFittableException(point + " can not be fitted into the collection");
        }

        Movable[] newMovable = Arrays.copyOf(movable, movable.length + 1);
        newMovable[newMovable.length - 1] = m;
        movable = newMovable;
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {

        if (type.toString().equals("POINT")) {
            Arrays.stream(movable)
                    .filter(m -> m instanceof MovablePoint)
                    .forEach(m -> {
                        MovablePoint point = (MovablePoint) m;
                        try {
                            switch (direction) {
                                case UP:
                                    point.moveUp();
                                    break;
                                case LEFT:
                                    point.moveLeft();
                                    break;
                                case RIGHT:
                                    point.moveRight();
                                    break;
                                case DOWN:
                                    point.moveDown();
                                    break;
                            }
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                    });
        } else if (type.toString().equals("CIRCLE")) {
            Arrays.stream(movable)
                    .filter(m -> m instanceof MovableCircle)
                    .forEach(m -> {
                        MovableCircle point = (MovableCircle) m;
                        try {
                            switch (direction) {
                                case UP:
                                    point.moveUp();
                                    break;
                                case LEFT:
                                    point.moveLeft();
                                    break;
                                case RIGHT:
                                    point.moveRight();
                                    break;
                                case DOWN:
                                    point.moveDown();
                                    break;
                            }
                        } catch (ObjectCanNotBeMovedException e) {
                            System.out.println(e.getMessage());
                        }
                    });

        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Collection of movable objects with size ").append(movable.length).append(":\n");
        for (Movable m : movable) {
            str.append(m).append("\n");
        }
        return str.toString();
    }
}


public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            try {
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
            } catch (MovableObjectNotFittableException e) {
                System.out.println(e.getMessage());
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
