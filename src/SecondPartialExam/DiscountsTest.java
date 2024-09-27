// 3.

package SecondPartialExam;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde

class Store {
    String storeName;

    List<String> prices;

    public Store(String data) {
        String[] parts = data.split("\\s+");
        this.storeName = parts[0];
        this.prices = Arrays.stream(parts).skip(1).collect(Collectors.toList());
    }

    public String getStoreName() {
        return storeName;
    }

    public double getAverageDiscount() {
        return prices.stream()
                .mapToInt(item -> getDiscount(item))
                .average()
                .orElse(0.0);

    }

    private int getDiscount(String line) {
        String[] pricesData = line.split(":");
        int priceOnDiscount = Integer.parseInt(pricesData[0]);
        int originalPrice = Integer.parseInt(pricesData[1]);

        return (originalPrice - priceOnDiscount) * 100 / originalPrice;

    }

    public int getTotalDiscount() {
        return prices.stream()
                .mapToInt(item -> getAbsoluteDiscount(item))
                .sum();

    }

    private int getAbsoluteDiscount(String line) {
        String[] pricesData = line.split(":");
        int priceOnDiscount = Integer.parseInt(pricesData[0]);
        int originalPrice = Integer.parseInt(pricesData[1]);

        return originalPrice - priceOnDiscount;
    }

    private String getPricesPrintFormat(String line) {
        String[] pricesData = line.split(":");
        int priceOnDiscount = Integer.parseInt(pricesData[0]);
        int originalPrice = Integer.parseInt(pricesData[1]);

        return priceOnDiscount + "/" + originalPrice;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();


        sb.append(storeName).append("\n");
        sb.append("Average discount: ").append(String.format("%.1f", getAverageDiscount())).append("%\n");
        sb.append("Total discount: ").append(getTotalDiscount()).append("\n");

        List<String> sorted = prices.stream().sorted(Comparator.comparing(this::getDiscount)
                        .thenComparing(this::getAbsoluteDiscount).reversed())
                .collect(Collectors.toList());

        int i;
        for (i = 0; i < sorted.size() - 1; i++) {
            sb.append(String.format("%2d", getDiscount(sorted.get(i)))).append("% ").append(getPricesPrintFormat(sorted.get(i))).append("\n");
        }
        sb.append(String.format("%2d", getDiscount(sorted.get(i)))).append("% ").append(getPricesPrintFormat(sorted.get(i)));


        return sb.toString();
    }
}

class Discounts {

    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        stores = br.lines()
                .map(line -> new Store(line))
                .collect(Collectors.toList());

        return stores.size();

    }

    public List<Store> byAverageDiscount() {
        Comparator<Store> avgDiscountComparator = Comparator.comparing(Store::getAverageDiscount).reversed()
                .thenComparing(Store::getStoreName);

        return stores.stream()
                .sorted(avgDiscountComparator)
                .limit(3)
                .collect(Collectors.toList());
    }


    public List<Store> byTotalDiscount() {
        Comparator<Store> totalDiscountComparator = Comparator.comparing(Store::getTotalDiscount)
                .thenComparing(Store::getStoreName);

        return stores.stream()
                .sorted(totalDiscountComparator)
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Store store : stores) {
            sb.append(store);
        }
        return sb.toString();
    }

}