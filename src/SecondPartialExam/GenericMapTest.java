// 32.

package SecondPartialExam;

import java.util.*;

interface MergeStrategy<E>{
    E strategy(E el1, E el2);
}

class MapOps{
    public static <K extends Comparable<K>,V> Map<K, V> merge (Map<K, V> map1, Map<K, V> map2, MergeStrategy<V> mergingStrategy){
        Map<K, V> newMap = new TreeMap<>();

        for(Map.Entry<K, V> entry : map2.entrySet()){
            K key = entry.getKey();
            V value1 = entry.getValue();
            if(map1.containsKey(key)){
                V value2 = map1.get(key);
                V newValue = mergingStrategy.strategy(value2, value1);

                newMap.put(key, newValue);
            }else{
                newMap.put(key, value1);
            }
        }
        for(Map.Entry<K, V> entry : map1.entrySet()){
            K key = entry.getKey();
            V value = entry.getValue();
            if(!map2.containsKey(key))
                newMap.put(key,value);
        }

        return  newMap;
    }
}

public class GenericMapTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Mergeable integers
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which is their sum
             MergeStrategy<Integer> mergeStrategy = Integer::sum;

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 2) { // Mergeable strings
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which is their concatenation
             MergeStrategy<String> mergeStrategy = (str1, str2)-> str1+str2;

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 3) {
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which will be the max of the two objects
             MergeStrategy<Integer> mergeStrategy = (num1, num2)-> Math.max(num1, num2);

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 4) {
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which will mask the occurrences of the second string in the first string
            
            MergeStrategy<String> mergeStrategy = (str1, str2)-> str1.replace(str2,"*".repeat(str2.length()));
            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        }
    }

    private static void readIntMap(Scanner sc, Map<Integer, Integer> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            int k = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            map.put(k, v);
        }
    }

    private static void readStrMap(Scanner sc, Map<String, String> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            map.put(parts[0], parts[1]);
        }
    }

    private static void printMap(Map<?, ?> map) {
        map.forEach((k, v) -> System.out.printf("%s -> %s%n", k.toString(), v.toString()));
    }
}
