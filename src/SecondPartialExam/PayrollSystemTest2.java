// 7.

package SecondPartialExam;

import java.util.*;
import java.util.stream.Collectors;

class BonusNotAllowedException extends Exception {
    public BonusNotAllowedException() {
        super("BonusNotAllowedException");
    }
}

abstract class Employee{
    String id;
    String level;
    String bonus;
    double baseSalary;

    public Employee(String id, String level, String bonus) {
        this.id = id;
        this.level = level;
        this.bonus = bonus;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getBaseSalary() {
        return baseSalary;
    }
    abstract double getSalary();
}

class HourlyEmployee extends Employee {
    double hours;
    double rate;


    public HourlyEmployee(String id, String level, double hours, String bonus, double rate) {
        super(id, level, bonus);
        this.hours = hours;
        this.rate=rate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getSalary() {
        double salary = (hours > 40) ? (40 * rate + (hours - 40) * rate * 1.5) : (hours * rate);
        baseSalary = salary;

        if (bonus.endsWith("%")) {
            int b = Integer.parseInt(bonus.substring(0, bonus.length() - 1));
             salary += salary*(b/100.0);
        } else if(!bonus.equals("none")){
            salary += Double.parseDouble(bonus);
        }

        return salary;
    }

    public double getOverTimeSalary(){
        return (hours - 40) * rate * 1.5;
    }

    @Override
    public String toString() {
        double bonusAmount = getSalary() - baseSalary;
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f Bonus: %.2f",
                id,
                level,
                getSalary(),
                hours > 40 ? 40.00 : hours,
                hours > 40 ? hours - 40 : 0.00,
                bonusAmount);
    }

}

class FreelanceEmployee extends Employee {
    List<Integer> points;
    int totalPoints;
    double rate;

    public FreelanceEmployee(String id, String level, List<Integer> points, String bonus, double rate) {
        super(id, level, bonus);
        this.points = points;
        this.totalPoints = points.stream().mapToInt(Integer::intValue).sum();
        this.rate = rate;
    }

    @Override
    public double getSalary() {
        double salary =  totalPoints * rate;
        baseSalary = salary;
        if (bonus.endsWith("%")) {
            int b = Integer.parseInt(bonus.substring(0, bonus.length() - 1));
            salary += salary*(b/100.0);
        } else if(!bonus.equals("none")){
            salary += Double.parseDouble(bonus);
        }
        return salary;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d Bonus: %.2f",
                getId(), getLevel(), getSalary(), points.size(), totalPoints, getSalary()-baseSalary);
    }

}

class PayrollSystem {
   Map<String, Double> hourlyRateByLevel;
   Map<String, Double> ticketRateByLevel;

    Set<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new HashSet<>();
    }

    public Employee createEmployee(String line) throws BonusNotAllowedException {
        String[] parts = line.split("\\s+");
        String employeeData = parts[0];
        String bonus = (parts.length > 1) ? parts[1] : "none";

        validateBonus(bonus);

        String[] data = employeeData.split(";");
        String type = data[0];
        String id = data[1];
        String level = data[2];

        Employee employee = null;

        if (type.equals("F")) {
            List<Integer> points = Arrays.stream(data, 3, data.length)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            Double rate = ticketRateByLevel.get(level);
            employee = new FreelanceEmployee(id, level, points, bonus, rate);
        } else if (type.equals("H")) {
            double hours = Double.parseDouble(data[3]);
            double rate= hourlyRateByLevel.get(level);
            employee = new HourlyEmployee(id, level, hours, bonus, rate);
        }

        if (employee != null) {
            employees.add(employee);
        }

        return employee;
    }
    private void validateBonus(String bonus) throws BonusNotAllowedException {
        if (bonus.endsWith("%")) {
            int percent = Integer.parseInt(bonus.substring(0, bonus.length() - 1));
            if (percent > 20) throw new BonusNotAllowedException();
        } else if (!bonus.equals("none")) {
            double fixedBonus = Double.parseDouble(bonus);
            if (fixedBonus > 1000) throw new BonusNotAllowedException();
        }
    }

    public Map<String, Double> getOvertimeSalaryForLevels (){
        Map<String, Double> salaryMap = new HashMap<>();

        for(Employee employee: employees){
            if(employee instanceof HourlyEmployee){
                HourlyEmployee hourlyEmployee = (HourlyEmployee) employee;
                if(hourlyEmployee.hours>40){
                    String level = hourlyEmployee.getLevel();
                    double overtimeSalary = hourlyEmployee.getOverTimeSalary();;

                    salaryMap.put(level, salaryMap.getOrDefault(level, 0.0) + overtimeSalary);
                }
            }
        }
        return salaryMap;

//        return employees.stream()
//                .filter(employee -> employee instanceof HourlyEmployee && ((HourlyEmployee) employee).hours > 40)
//                .map(employee -> (HourlyEmployee) employee)
//                .collect(Collectors.groupingBy(
//                        Employee::getLevel,
//                        Collectors.summingDouble(HourlyEmployee::getOverTimeSalary)
//                ));
    }


    public void printStatisticsForOvertimeSalary() {
        DoubleSummaryStatistics stats = employees.stream()
                .filter(employee->employee instanceof HourlyEmployee && ((HourlyEmployee) employee).hours > 40)
                .map(employee -> (HourlyEmployee) employee)
                .mapToDouble(HourlyEmployee::getOverTimeSalary)
                .summaryStatistics();

        System.out.printf("Min: %.2f Max: %.2f Sum: %.2f Avg: %.2f\n",
        stats.getMin(), stats.getMax(), stats.getSum(), stats.getAverage());
    }

    public Map<String, Integer> ticketsDoneByLevel() {
        return employees.stream()
                .filter(employee -> employee instanceof FreelanceEmployee)
                .map(employee -> (FreelanceEmployee) employee)
                .collect(Collectors.groupingBy(
                        Employee::getLevel,
                        Collectors.summingInt(employee -> employee.points.size())
                ));
    }
    Collection<Employee> getFirstNEmployeesByBonus (int n){
        return employees.stream()
                .sorted((e1, e2)->{
                    double bonus1 = e1.getSalary()-e1.getBaseSalary();
                    double bonus2 = e2.getSalary()-e2.getBaseSalary();
                    return Double.compare(bonus2, bonus1);
                })
                .limit(n)
                .collect(Collectors.toSet());
    }

}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            // case 2: //Testing getOvertimeSalaryForLevels()
            //     ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
            //         System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
            //     });
            //     break;
            // case 3: //Testing printStatisticsForOvertimeSalary()
            //     ps.printStatisticsForOvertimeSalary();
            //     break;
            // case 4: //Testing ticketsDoneByLevel
            //     ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
            //         System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
            //     });
            //     break;
            // case 5: //Testing getFirstNEmployeesByBonus (int n)
            //     ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
            //     break;
        }
    }
}