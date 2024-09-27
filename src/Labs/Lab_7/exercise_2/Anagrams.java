package Labs.Lab_7.exercise_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {
    public static void main(String[] args) {

        try {
            findAll(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void findAll(InputStream inputStream) throws IOException {
        // Vasiod kod ovde
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        Map<String, List<String>> groups = new LinkedHashMap<>();

        // sort the letters from one word -> they will be the same key if they are anagrams
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String sortedLetters = sortLetters(line);
                groups.computeIfAbsent(sortedLetters, k -> new ArrayList<>()).add(line);
            }
        }

        List<List<String>> foundedGroups = groups.values().stream()
                .filter(value -> value.size() >= 5)
                .map(group -> {
                    Collections.sort(group);
                    return group;
                })
                .collect(Collectors.toList());


        for (List<String> group : foundedGroups) {
            String g = String.join(" ", group);
            System.out.println(g);
        }

    }

    private static String sortLetters(String line) {
        char[] letters = line.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}
