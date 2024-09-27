// 35.

package SecondPartialExam;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.*;

class StudentNotFoundException extends Exception{
    public StudentNotFoundException(String index) {
        super(String.format("Student %s not found!", index));
    }
}
class Item {
    String index;
    String name;
    long price;

    public Item(String data) {
        String[] parts = data.split(";");
        this.index = parts[0];
        this.name = parts[1];
        this.price = Long.parseLong(parts[2]);
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}

class OnlinePayments {
    List<Item> items;

    public OnlinePayments() {
        items = new ArrayList<>();
    }

    void readItems(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        br.lines().forEach(line -> items.add(new Item(line)));
    }

    void printStudentReport(String index, OutputStream os) throws StudentNotFoundException {
        PrintWriter pw = new PrintWriter(os);
        List<Item> studentItems = items.stream()
                .filter(i -> i.getIndex().equals(index))
                .collect(Collectors.toList());


        if (studentItems.isEmpty()) {
            throw new StudentNotFoundException(index);
        }




        pw.printf("Student: %s Net: %d Fee: %d Total: %d\n",
                index,
                getNeto(studentItems),
                getProvision(studentItems),
                getNeto(studentItems)+getProvision(studentItems)
        );

        pw.println("Items: ");
        List<Item> sorted = studentItems.stream().sorted(Comparator.comparing(Item::getPrice).reversed()).collect(Collectors.toList());
        IntStream.range(1, studentItems.size()+1)
                        .forEach(i-> pw.println(String.format("%d. %s %d", i, sorted.get(i-1).getName(), sorted.get(i-1).getPrice())));

        pw.flush();

    }
    private long getNeto(List<Item> items){
        return items.stream()
                .mapToLong(Item::getPrice)
                .sum();
    }
    private int getProvision(List<Item> items){
        long sum = getNeto(items);

        double provision = sum*(1.14/100);

        if(provision<=3)
            provision = 3;
        if(provision>=300)
            provision = 300;

        return (int) Math.round(provision);
    }

}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);


        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> {
            try {
                onlinePayments.printStudentReport(id, System.out);
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}