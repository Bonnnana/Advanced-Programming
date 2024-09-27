package Labs.Lab_5.exercise_1;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.LinkedList;

class ResizableArray<T> {
    private T[] array;
    private int n;
    private int capacity;

    @SuppressWarnings("unchecked")
    ResizableArray() {
        capacity = 100;
        this.array = (T[]) new Object[capacity];
        n = 0;
    }

    public void addElement(T element) {
        if (n == capacity) {
            capacity += 25;
            array = Arrays.copyOf(array, capacity);
        }
        array[n++] = element;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < n; i++) {
            if (array[i].equals(element)) {
                for (int j = i; j < n - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[--n] = null;// Avoid memory leak
                if (n <= capacity / 4) {
                    capacity /= 2;
                    array = Arrays.copyOf(array, capacity);
                }
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        for (int i = 0; i < n; i++) {
            if (array[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        return array;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int count() {
        return n;
    }

    public T elementAt(int idx) {
        if (idx >= n)
            throw new ArrayIndexOutOfBoundsException();

        return array[idx];
    }


    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        for (int i = 0; i < src.count(); i++) {
            T element = src.elementAt(i); // Превземање на елементот од src
            dest.addElement(element); // Додавање на елементот во dest
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {
    public double sum() {
        double sum = 0;
        for (int i = 0; i < count(); i++) {
            sum += elementAt(i);
        }
        return sum;
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        int counter = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0)
                counter++;
        }
        return counter;
    }

    public IntegerArray distinct() {
        IntegerArray distinct = new IntegerArray();

        LinkedHashSet<Integer> seen = new LinkedHashSet<>();

        for (int i = 0; i < count(); i++) {
            Integer element = elementAt(i);
            if (seen.add(element))
                distinct.addElement(element);
        }

        return distinct;
    }

    public IntegerArray increment(int offset) {
        IntegerArray incremented = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            Integer element = elementAt(i) + offset;
            incremented.addElement(element);
        }
        return incremented;

    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
