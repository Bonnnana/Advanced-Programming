package Labs.Lab_2.exercise_1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

abstract class Contact {
    private int year;
    private int month;
    private int day;


    public Contact(String date) {
        //YYYY-MM-DD
        String[] parts = date.split("-");
        this.year = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.day = Integer.parseInt(parts[2]);
    }

    public boolean isNewerThan(Contact c) {
        long c1 = this.year * 365L + this.month * 30L + this.day;
        long c2 = c.year * 365L + c.month * 30L + c.day;
        return c1 > c2;
    }


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    abstract public String getType();
}

class EmailContact extends Contact {
    private String email;


    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

enum Operator {
    VIP,
    ONE,
    TMOBILE
}

class PhoneContact extends Contact {
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;

        if (phone.charAt(2) == '0' || phone.charAt(2) == '1' || phone.charAt(2) == '2')
            operator = Operator.TMOBILE;
        else if (phone.charAt(2) == '5' || phone.charAt(2) == '6')
            operator = Operator.ONE;
        else if (phone.charAt(2) == '7' || phone.charAt(2) == '8')
            operator = Operator.VIP;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }
}

class Student {
    private ArrayList<Contact> contacts;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;


    public Student(String firstName, String lastName, String city, int age, long index) {
        this.contacts = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;

    }

    public void addEmailContact(String date, String email) {
        EmailContact newEmailContact = new EmailContact(date, email);
        contacts.add(newEmailContact);
    }

    public void addPhoneContact(String date, String phone) {
        PhoneContact newPhoneContact = new PhoneContact(date, phone);
        contacts.add(newPhoneContact);
    }

    public Contact[] getEmailContacts() {
        return contacts.stream()
                .filter(contact -> contact.getType().equals("Email"))
                .toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts() {
        return contacts.stream()
                .filter(contact -> contact.getType().equals("Phone"))
                .toArray(Contact[]::new);
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact getLatestContact() {
        return contacts.stream()
                .max(Comparator.comparing(Contact::getYear)
                        .thenComparing(Contact::getMonth)
                        .thenComparing(Contact::getDay))
                .orElse(null);
    }

    @Override
    public String toString() {
        return "{" +
                "\"ime\"" + ":\"" + firstName + "\", " +
                "\"prezime\"" + ":\"" + lastName + "\", " +
                "\"vozrast\"" + ":" + age + ", " +
                "\"grad\"" + ":\"" + city + "\", " +
                "\"indeks\"" + ":" + index + ", " +
                "\"telefonskiKontakti\"" + ":" + Arrays.toString(getPhoneContacts()) + ", " +
                "\"emailKontakti\"" + ":" + Arrays.toString(getEmailContacts()) + "}";
    }
}

class Faculty {
    private String name;
    ArrayList<Student> students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = new ArrayList<>(Arrays.asList(students));
    }

    public int countStudentsFromCity(String cityName) {
        long counter = students.stream()
                .filter(student -> student.getCity().equals(cityName))
                .count();

        return (int) counter;
    }

    public Student getStudent(long index) {
        return students.stream()
                .filter(s -> s.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public double getAverageNumberOfContacts() {
        return students.stream()
                .mapToInt(s -> s.numberOfContacts())
                .average()
                .orElse(0.0);
    }

    public Student getStudentWithMostContacts() {
        return students.stream()
                .max(Comparator.comparing(Student::numberOfContacts)
                        .thenComparing(Student::getIndex))
                .orElse(null);
    }

    @Override
    public String toString() {
        return "{" +
                "\"fakultet\":\"" + name + '\"' +
                ", \"studenti\":" + students +
                '}';
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
