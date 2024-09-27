// 27.

package SecondPartialExam;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.*;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde

class Names {
    Map<String, Integer> countedNames;
    Set<String> uniqueNames;

    public Names() {
        countedNames = new TreeMap<>();
        uniqueNames = new TreeSet<>();
    }

    public void addName(String name) {
    // countedNames.computeIfPresent(name, (key, value) -> value+1);
    // countedNames.putIfAbsent(name, 1);

        countedNames.merge(name, 1, Integer::sum);
        uniqueNames.add(name);
    }

    public void printN(int n) {
        countedNames.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .forEach(entry -> System.out.println(String.format("%s (%d) %d",
                        entry.getKey(),
                        entry.getValue(),
                        getUniqueLetters(entry.getKey()))));

    }

    private int getUniqueLetters(String word) {
        Set<Character> unique = new HashSet<>();

        word.chars().forEach(c -> unique.add((char) Character.toLowerCase(c)));

        return unique.size();
    }

    public String findName(int len, int x) {
        List<String> list = uniqueNames.stream().filter(name -> name.length() < len).sorted().collect(Collectors.toList());

        if (list.isEmpty())
            return "List is empty";

        int index = x % list.size();


        return list.get(index);
    }


}