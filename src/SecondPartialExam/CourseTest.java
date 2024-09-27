// 31.

package SecondPartialExam;

import java.util.*;
import java.util.stream.Collectors;

class InvalidNumOfPointsException extends Exception {
    public InvalidNumOfPointsException() {
    }
}

class Student {
    String idNumber;
    String name;
    int firstMidtermPoints;
    int secondMidtermPoints;
    int labsPoints;

    public Student(String idNumber, String name) throws InvalidNumOfPointsException {
        this.idNumber = idNumber;
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public int getFirstMidtermPoints() {
        return firstMidtermPoints;
    }

    public int getSecondMidtermPoints() {
        return secondMidtermPoints;
    }

    public int getLabsPoints() {
        return labsPoints;
    }

    public void setFirstMidtermPoints(int firstMidtermPoints) throws InvalidNumOfPointsException {
        if (firstMidtermPoints > 100)
            throw new InvalidNumOfPointsException();
        this.firstMidtermPoints = firstMidtermPoints;
    }

    public void setSecondMidtermPoints(int secondMidtermPoints) throws InvalidNumOfPointsException {
        if (secondMidtermPoints > 100)
            throw new InvalidNumOfPointsException();
        this.secondMidtermPoints = secondMidtermPoints;
    }

    public void setLabsPoints(int labsPoints) throws InvalidNumOfPointsException {
        if (labsPoints > 10)
            throw new InvalidNumOfPointsException();
        this.labsPoints = labsPoints;
    }

    public double getSummaryPoints() {
        return (firstMidtermPoints * 0.45) + (secondMidtermPoints * 0.45) + labsPoints;
    }

    public int getGrade() {
        double totalPoints = getSummaryPoints();
        if (totalPoints >= 90)
            return 10;
        else if (totalPoints >= 80)
            return 9;
        else if (totalPoints >= 70)
            return 8;
        else if (totalPoints >= 60)
            return 7;
        else if (totalPoints >= 50)
            return 6;
        else
            return 5;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                idNumber, name, firstMidtermPoints, secondMidtermPoints, labsPoints, getSummaryPoints(), getGrade());
    }
}

class AdvancedProgrammingCourse {
    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent(Student s) {
        String id = s.getIdNumber();
        students.put(id, s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        try {
            if (activity.equals("midterm1")) {
                students.get(idNumber).setFirstMidtermPoints(points);
            } else if (activity.equals("midterm2")) {
                students.get(idNumber).setSecondMidtermPoints(points);

            } else {
                students.get(idNumber).setLabsPoints(points);
            }
        } catch (InvalidNumOfPointsException e) {
//            throw new RuntimeException(e);
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return students.values().stream()
                .sorted(Comparator.comparingDouble(Student::getSummaryPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());

    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> map = students.values().stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        Map<Integer, Integer> grades = new TreeMap<>();
        for(int i= 5; i<=10; i++)
            grades.put(i, map.getOrDefault(i, 0));

        return grades;

    }
    public void printStatistics(){
        DoubleSummaryStatistics stat  = students.values().stream()
                .filter(s -> s.getGrade()>5)
                .mapToDouble(Student::getSummaryPoints)
                .summaryStatistics();
        
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                stat.getCount(),stat.getMin(), stat.getAverage(), stat.getMax()));
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                try {
                    advancedProgrammingCourse.addStudent(new Student(id, name));
                } catch (InvalidNumOfPointsException e) {
//                    throw new RuntimeException(e);
                }
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
