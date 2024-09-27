// 5.

package FirstPartialExam;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    T minimum;
    T maximum;

    int countUpdates;
    int countDuplicateMin;
    int countDuplicateMax;

    public MinMax() {
        minimum = null;
        maximum = null;

        countUpdates = 0;
        countDuplicateMax = 0;
        countDuplicateMin = 0;

    }

    public void update(T element) {

        if (maximum == null || element.compareTo(this.maximum) > 0) {
            maximum = element;
            countDuplicateMax = 1;
        } else if (element.compareTo(maximum) == 0) {
            countDuplicateMax++;
        }

        if (minimum == null || element.compareTo(this.minimum) < 0) {
            minimum = element;
            countDuplicateMin = 1;
        } else if (element.compareTo(this.minimum) == 0) {
            countDuplicateMin++;
        }

        countUpdates++;
    }

    public T max() {
        return maximum;
    }

    public T min() {
        return minimum;
    }

    @Override
    public String toString() {
        int counts = countUpdates - countDuplicateMax - countDuplicateMin;
        return minimum + " " + maximum + " " + counts + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}