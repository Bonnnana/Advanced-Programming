package Labs.Lab_3.exercise_1;

import java.util.*;

interface Item {
    public int getPrice();
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super("InvalidExtraTypeException");
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super("InvalidPizzaTypeException");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super(String.format("%s out of stock!", item));
    }
}

class EmptyOrder extends Exception {

}

class OrderLockedException extends Exception {

}

enum ExtraType {
    Coke,
    Ketchup
}

enum PizzaType {
    Standard,
    Pepperoni,
    Vegetarian
}

class ExtraItem implements Item {
    private ExtraType type;

    public ExtraItem(String type) throws InvalidExtraTypeException {

        try {
            this.type = ExtraType.valueOf(type);
        } catch (IllegalArgumentException e) {
            // If the conversion fails, throw an InvalidExtraTypeException
            throw new InvalidExtraTypeException();
        }

//        SECOND WAY:
//        boolean isValid = Arrays.stream(ExtraType.values())
//                .anyMatch(item -> item.name().equals(type));
//
//        if(!isValid)
//            throw new InvalidExtraTypeException();
//
//        this.type = ExtraType.valueOf(type);
    }

    @Override
    public int getPrice() {
        if (type == ExtraType.Coke)
            return 5;
        if (type == ExtraType.Ketchup)
            return 3;

        return 0;
    }

    @Override
    public String toString() {
        return type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return type == extraItem.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class PizzaItem implements Item {

    private PizzaType type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        try {
            this.type = PizzaType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidPizzaTypeException();
        }

    }

    @Override
    public int getPrice() {
        if (type == PizzaType.Standard)
            return 10;
        if (type == PizzaType.Pepperoni)
            return 12;
        if (type == PizzaType.Vegetarian)
            return 8;

        return 0;
    }

    @Override
    public String toString() {
        return type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return type == pizzaItem.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class Order {
    private static HashMap<Item, Integer> orders;
    private static List<Item> itemOrder;
    private boolean isLocked;

    public Order() {
        orders = new HashMap<Item, Integer>();
        itemOrder = new ArrayList<Item>();
        this.isLocked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (count > 10)
            throw new ItemOutOfStockException(item);

        if (!isLocked) {
            if (orders.containsKey(item)) {
                orders.put(item, count);
            } else {
                orders.put(item, count);
                itemOrder.add(item);
            }
        } else {
            throw new OrderLockedException();
        }
    }

    public int getPrice() {
        return orders.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public void displayOrder() {
        int index = 1;


        for (Item item : itemOrder) {
            ;
            int count = orders.get(item);
            int pricePerItem = item.getPrice() * count;

            System.out.printf("%3d.%-15sx%2d%5d$\n", index++, item, count, pricePerItem);
        }

        System.out.printf("%-22s%5d$\n", "Total:", getPrice());
    }

    public void removeItem(int idx) throws OrderLockedException {
        if (!isLocked) {
            if (idx < 0 || idx > itemOrder.size())
                throw new ArrayIndexOutOfBoundsException(idx);

            Item itemToBeRemoved = itemOrder.get(idx);

            itemOrder.remove(itemToBeRemoved);
            orders.remove(itemToBeRemoved);
        } else
            throw new OrderLockedException();
    }

    public void lock() throws EmptyOrder {
        if (itemOrder.isEmpty())
            throw new EmptyOrder();

        isLocked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}