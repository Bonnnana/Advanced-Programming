package Labs.Lab_1.exercise_2;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */


    //4590
    public static String toRoman(int n) {
        // your solution here
        int[] arabic = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanian = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder number = new StringBuilder();
        int i = 0;
        while (n != 0) {
            if (n - arabic[i] >= 0) {
                number.append(romanian[i]);
                n -= arabic[i];
            } else {
                i++;
            }
        }
        String string = number.toString();
        return string;
    }

}
