// 9.

package FirstPartialExam;

import java.util.*;


class Triple<E extends Number> {
    E num1;
    E num2;
    E num3;
    List<E> numbers = new ArrayList<>();

    public Triple(E num1, E num2, E num3) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        numbers.add(num1);
        numbers.add(num2);
        numbers.add(num3);
    }

    public double max() {
        return Math.max(Math.max(num1.doubleValue(), num2.doubleValue()), num3.doubleValue());
    }

    public double avarage() {
        double n1 = num1.doubleValue();
        double n2 = num2.doubleValue();
        double n3 = num3.doubleValue();
        return (n1 + n2 + n3) / 3;

    }

    public void sort() {

        Collections.sort(numbers, (n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()));

//		for(int i=0; i<numbers.size()-1; i++){
//			double n1= numbers.get(i).doubleValue();
//			double n2= numbers.get(i+1).doubleValue();
//			if(n1>n2){
//				E num =numbers.remove(i+1);
//				numbers.add(i, num);
//			}
//		}
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < numbers.size() - 1; i++) {
            sb.append(String.format("%.2f ", numbers.get(i).doubleValue()));
        }
        sb.append(String.format("%.2f", numbers.get(i).doubleValue()));
        return sb.toString();
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


