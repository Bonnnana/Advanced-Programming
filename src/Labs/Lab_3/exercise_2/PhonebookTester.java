package Labs.Lab_3.exercise_2;

import java.io.*;
import java.util.*;

class InvalidNameException extends Exception {
    public String name;

    public InvalidNameException(String name) {
        this.name = name;
    }
}

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
    }
}

class InvalidFormatException extends Exception {
    public InvalidFormatException() {
    }
}

class Contact {
    private String name;
    private List<String> phonenumbers;

    public Contact(String name, String... phonenumbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if (name.length() < 4 || name.length() > 10 || !name.chars().allMatch(Character::isLetterOrDigit))
            throw new InvalidNameException(name);
        if (phonenumbers.length > 5)
            throw new MaximumSizeExceddedException();

        for (String number : phonenumbers) {
            if (number.length() != 9 || !number.matches("\\d+"))   // "\\d" -> one digit from 0 to 9
                throw new InvalidNumberException();

            char thirdDigit = number.charAt(2);
            if (thirdDigit == '3' || thirdDigit == '4' || thirdDigit == '9')
                throw new InvalidNumberException();

        }

        this.name = name;
        this.phonenumbers = new ArrayList<>(Arrays.asList(phonenumbers));
        ;
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return phonenumbers.stream()
                .sorted()
                .toArray(String[]::new);
    }

    public void addNumber(String number) throws InvalidNumberException {
        if (number.length() != 9 || !number.matches("\\d+"))
            throw new InvalidNumberException();

        char thirdDigit = number.charAt(2);
        if (thirdDigit == 3 || thirdDigit == 4 || thirdDigit == 9)
            throw new InvalidNumberException();

        phonenumbers.add(number);
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        String[] parts = s.split("-");
        String newName = parts[0];
        String[] newNumbers = parts[1].split("_");

        try {
            return new Contact(newName, newNumbers);
        } catch (Exception e) {
            throw new InvalidFormatException();
        }
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(phonenumbers.size()).append("\n");

        for (String number : getNumbers()) {
            sb.append(number).append("\n");
        }

        return sb.toString();

    }
}

class PhoneBook {
    ArrayList<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.size() == 250)
            throw new MaximumSizeExceddedException();
        for (Contact c : contacts) {
            if (c.getName().equals(contact.getName()))
                throw new InvalidNameException(contact.getName());
        }

        contacts.add(contact);
    }

    public Contact getContactForName(String name) {
        Contact finded = null;
        for (Contact contact : contacts) {
            if (contact.getName().equals(name))
                finded = contact;
        }

        return finded;
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContacts() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getName))
                .toArray(Contact[]::new);
    }

    public boolean removeContact(String name) {
        return contacts.removeIf(c -> c.getName().equals(name));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Contact contact : getContacts())
            sb.append(contact).append("\n");

        return sb.toString();
    }

    public static boolean saveAsTextFile(PhoneBook phonebook, String path) {
        File file = new File(path);

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(phonebook.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidFormatException {
        File file = new File(path);
        PhoneBook phoneBook = new PhoneBook();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<Contact> contacts = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String name = line.trim();
            if (name.isEmpty())
                throw new InvalidFormatException();

            line = br.readLine();
            int numberOfPhones;
            try {
                numberOfPhones = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new InvalidFormatException();
            }

            String[] phoneNumbers = new String[0];
            for (int i = 0; i < numberOfPhones; i++) {
                line = br.readLine();
                if (line == null || line.trim().length() != 9 || !line.matches("\\d+"))
                    throw new InvalidFormatException();

                phoneNumbers[i] = line.trim();
            }


            try {
                phoneBook.addContact(new Contact(name,
                        phoneNumbers));
            } catch (Exception e) {
                throw new InvalidFormatException();
            }

            br.readLine();
        }

        return phoneBook;

    }

    public Contact[] getContactsForNumber(String number_prefix) {
        return contacts.stream()
                .filter(contact -> Arrays.stream(contact.getNumbers())
                        .anyMatch(phone -> phone.startsWith(number_prefix)))
                .distinct() // for no duplicates
                .sorted(Comparator.comparing(Contact::getName))
                .toArray(Contact[]::new);
    }
}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
