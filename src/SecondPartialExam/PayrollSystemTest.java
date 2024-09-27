// 6.

package SecondPartialExam;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
interface Employee extends Comparable<Employee> {
    public String getId();
    public double getSalary();

    public String getLevel();
}

class HourlyEmployee implements Employee {
    String Id;
    String level;
    double hours;

    public HourlyEmployee(String id, String level, double hours) {
        Id = id;
        this.level = level;
        this.hours = hours;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public double getSalary() {
        double value = PayrollSystem.getHourlyRateByLevel().get(level);
        if (hours >= 40) {
            double other = hours - 40;
            return 40 * value + other * (value * 1.5);
        } else {
            return value * hours;
        }
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        if (hours >= 40) {
            return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                    Id,
                    level,
                    getSalary(),
                    40.00,
                    hours - 40);

        } else {
            return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                    Id,
                    level,
                    getSalary(),
                    hours,
                    0.00);

        }

    }

    @Override
    public int compareTo(Employee o) {
        Comparator<Employee> comp = Comparator.comparing(Employee::getSalary).reversed().thenComparing(Employee::getLevel);
        return comp.compare(this, o);
    }
}

class FreelanceEmployee implements Employee {
    String Id;
    String level;

    List<Integer> points;

    public FreelanceEmployee(String id, String level, List<Integer> points) {
        Id = id;
        this.level = level;
        this.points = points;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public double getSalary() {
        double value = PayrollSystem.getTicketRateByLevel().get(level);

        int sum = points.stream()
                .mapToInt(i -> i)
                .sum();

        return sum * value;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                Id,
                level,
                getSalary(),
                points.size(),
                points.stream().mapToInt(i -> i).sum());
    }

    @Override
    public int compareTo(Employee o) {
        Comparator<Employee> comp = Comparator.comparing(Employee::getSalary).reversed().thenComparing(Employee::getLevel);
        return comp.compare(this, o);
    }
}

class PayrollSystem {
    static Map<String, Double> HOURLY_RATE_BY_LEVEL;
    static Map<String, Double> TICKETS_RATE_BY_LEVEL;

    Set<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        HOURLY_RATE_BY_LEVEL = hourlyRateByLevel;
        TICKETS_RATE_BY_LEVEL = ticketRateByLevel;
        employees = new HashSet<>();
    }

    public static Map<String, Double> getHourlyRateByLevel() {
        return HOURLY_RATE_BY_LEVEL;
    }

    public static Map<String, Double> getTicketRateByLevel() {
        return TICKETS_RATE_BY_LEVEL;
    }

    void readEmployees(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Employee employee = null;

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            String type = data[0];
            String id = data[1];
            String level = data[2];
            if (type.equals("F")) {
                List<Integer> points = Arrays.stream(data)
                        .skip(3)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                employee = new FreelanceEmployee(id, level, points);
            } else if (type.equals("H")) {
                double hours = Double.parseDouble(data[3]);

                employee = new HourlyEmployee(id, level, hours);
            }

            if (employee != null)
                employees.add(employee);

        }

    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        Map<String, Set<Employee>> groupedEmployees = employees.stream()
                .filter(employee -> levels.contains(employee.getLevel()))
                .collect(Collectors.groupingBy(
                        Employee::getLevel,
                        TreeMap::new,
                        Collectors.toCollection(TreeSet::new)
                ));



        PrintWriter writer = new PrintWriter(os);

//        groupedEmployees.forEach((level, employeeSet) ->{
//            employeeSet.forEach(writer::println);
//        });
//        writer.flush();

        return groupedEmployees;
    }

}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        try {
            payrollSystem.readEmployees(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}