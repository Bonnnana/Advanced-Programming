package Labs.Lab_6.exercise_1;

import java.util.*;
import java.util.stream.Collectors;


class IntegerList {
    List<Integer> list;

    public IntegerList() {
        list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<>();
        list.addAll(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        if (idx > list.size()) {
            int newSize = Math.max(list.size() * 2, idx + 1);
            List<Integer> newList = new ArrayList<>(newSize);

            for (int i = 0; i < newSize; i++)
                newList.add(0);

            for (int i = 0; i < list.size(); i++)
                newList.set(i, list.get(i));

            newList.set(idx, el);
            this.list = newList;
        } else {
            list.add(idx, el);
        }
    }

    public int remove(int idx) {
        if (idx < 0 || idx > list.size())
            throw new ArrayIndexOutOfBoundsException();

        return list.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx < 0 || idx > list.size())
            throw new ArrayIndexOutOfBoundsException();

        list.set(idx, el);
    }

    public int get(int idx) {
        if (idx < 0 || idx > list.size())
            throw new ArrayIndexOutOfBoundsException();

        return list.get(idx);
    }

    public int size() {
        return list.size();
    }

    public int count(int el) {
        int counter = 0;
        for (int element : list) {
            if (element == el)
                counter++;
        }
        return counter;
    }

    public void removeDuplicates() {
        Set<Integer> distinct = new LinkedHashSet<>();


        for (int i = list.size() - 1; i >= 0; i--) {
            Integer num = list.get(i);
            if (!distinct.contains(num))
                distinct.add(num);

        }
        list.clear();
        list.addAll(distinct);
        Collections.reverse(list);
    }

    public int sumFirst(int k) {
        return list.stream()
                .limit(k)
                .mapToInt(i -> i)
                .sum();
    }

    public int sumLast(int k) {
        List<Integer> newList = new ArrayList<>();
        newList.addAll(list);
        Collections.reverse(newList);
        return newList.stream()
                .limit(k)
                .mapToInt(i -> i)
                .sum();

    }

    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx > list.size())
            throw new ArrayIndexOutOfBoundsException();

        k = k % list.size();

        int newIdx = (idx + k) % list.size();

        Integer element = list.get(idx);
        list.remove(idx);


        list.add(newIdx, element);
    }

    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx > list.size())
            throw new ArrayIndexOutOfBoundsException();

        k = k % list.size();

        int newIdx = (idx - k + list.size()) % list.size();

        Integer element = list.get(idx);
        list.remove(idx);
        list.add(newIdx, element);
    }

    public IntegerList addValue(int value) {
        IntegerList modified = new IntegerList();
        for (Integer num : list)
            modified.list.add(num + value);
        return modified;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}