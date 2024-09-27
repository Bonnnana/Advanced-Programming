// 12.

package SecondPartialExam;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class Sentence {
    String wholeSentence;
    Map<String, Integer> wordFrequencies;

    public Sentence(String line) {
        wholeSentence = line;
        wordFrequencies = new HashMap<>();
        String sentence = line.replaceAll("[^a-zA-Z\\s+]", "");
        String[] words = sentence.split("\\s+");
        Arrays.stream(words)
                .map(String::toLowerCase)
                .forEach(word -> wordFrequencies.merge(word, 1, Integer::sum));
    }

    public Set<String> getSetOfWords() {
        return wordFrequencies.keySet();
    }

    public List<Integer> getVectorOfSentence(Set<String> words) {
        List<Integer> vector = new ArrayList<>();
        for (String w : words) {
            vector.add(wordFrequencies.getOrDefault(w, 0));
        }
        return vector;
    }

    @Override
    public String toString() {
        return wholeSentence;
    }
}

class TextProcessor {
    List<Sentence> sentences;
    Set<String> wordsOfAllTexts;
    Map<String, Integer> wordFrequencies;

    public TextProcessor() {
        sentences = new ArrayList<>();
        wordsOfAllTexts = new TreeSet<>();
        wordFrequencies = new HashMap<>();
    }

    public void readText(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            Sentence s = new Sentence(line);
            wordsOfAllTexts.addAll(s.getSetOfWords());
            sentences.add(s);

            s.getSetOfWords().forEach(word ->
                    wordFrequencies.merge(word, s.wordFrequencies.get(word), Integer::sum)
            );
        }
    }

    public void printTextsVectors(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        sentences.forEach(sentence -> {
            List<Integer> vector = sentence.getVectorOfSentence(wordsOfAllTexts);
            writer.println(vector);
        });
        writer.flush();

    }

    public void printCorpus(OutputStream os, int n, boolean ascending) {
        PrintWriter writer = new PrintWriter(os);

        Comparator<Map.Entry<String, Integer>> primaryComparator = ascending
                ? Map.Entry.comparingByValue()
                : Map.Entry.<String, Integer>comparingByValue().reversed();

        Comparator<Map.Entry<String, Integer>> secondaryComparator = Map.Entry.comparingByKey();
        Comparator<Map.Entry<String, Integer>> combinedComparator = primaryComparator.thenComparing(secondaryComparator);

        wordFrequencies.entrySet().stream()
                .sorted(combinedComparator)
                .limit(n)
                .forEach(entry -> writer.printf("%s : %d\n", entry.getKey(), entry.getValue()));

        writer.flush();
    }

    public void mostSimilarTexts(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        double maxSimilarity = -1;
        int textIndex1 = -1;
        int textIndex2 = -1;

        for (int i = 0; i < sentences.size(); i++) {
            List<Integer> vector1 = sentences.get(i).getVectorOfSentence(wordsOfAllTexts);
            for (int j = i + 1; j < sentences.size(); j++) {
                List<Integer> vector2 = sentences.get(j).getVectorOfSentence(wordsOfAllTexts);

                double similarity = CosineSimilarityCalculator.cosineSimilarity(vector1, vector2);
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    textIndex1 = i;
                    textIndex2 = j;
                }
            }
        }
        if (textIndex1 != -1) {
            writer.println(sentences.get(textIndex1).toString());
            writer.println(sentences.get(textIndex2).toString());
            writer.println(String.format("%.10f", maxSimilarity));
        } else {
            writer.println("No texts to compare.");
        }
        writer.flush();
    }
}

class CosineSimilarityCalculator {
    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        try {
            textProcessor.readText(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out, 20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}