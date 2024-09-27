// 22.

package SecondPartialExam;

import javax.swing.text.DateFormatter;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventCalendarTest {
	public static void main(String[] args) throws ParseException {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();
		int year = scanner.nextInt();
		scanner.nextLine();
		EventCalendar eventCalendar = new EventCalendar(year);
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		for (int i = 0; i < n; ++i) {
			String line = scanner.nextLine();
			String[] parts = line.split(";");
			String name = parts[0];
			String location = parts[1];
			Date date = df.parse(parts[2]);
			try {
				eventCalendar.addEvent(name, location, date);
			} catch (WrongDateException e) {
				System.out.println(e.getMessage());
			}
		}
		Date date = df.parse(scanner.nextLine());
		eventCalendar.listEvents(date);
		eventCalendar.listByMonth();
	}
}

// vashiot kod ovde

class WrongDateException extends Exception{
	public WrongDateException(Date date) {
		super("Wrong date: " + date);
	}
}
class Event implements Comparable<Event>{
	String event;
	String location;
	Date time;
	LocalDateTime lt;


	public Event(String event, String location, Date time) {
		this.event = event;
		this.location = location;
		this.time = time;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

		// Convert Date to String
		String dateString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(time);

		// Parse String to LocalDateTime
		this.lt =  LocalDateTime.parse(dateString, formatter);

	}

	public String getEvent() {
		return event;
	}

	public String getLocation() {
		return location;
	}

	public Date getTime() {
		return time;
	}

	public LocalDateTime getLTime() {
		return lt;
	}

	public int getMonth(){
		return lt.getMonthValue();
	}

	public int getYear(){
		return lt.getYear();
	}

	public int getDay(){
		return lt.getDayOfMonth();
	}

	@Override
	public int compareTo(Event o) {
		Comparator<Event> comparator = Comparator.comparing(Event::getTime).thenComparing(Event::getEvent);

		return comparator.compare(this, o);
	}

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
		return String.format("%s at %s, %s", df.format(time), location, event);
	}
}
class EventCalendar{
	int calendaryear;
	Set<Event> events;

	public EventCalendar(int year) {
		this.calendaryear = year;
		events = new TreeSet<>();
	}

	public void addEvent(String name, String location, Date date) throws WrongDateException {
		Event event = new Event(name, location, date);

		if(event.getYear() != calendaryear)
			throw new WrongDateException(date);

		events.add(event);
	}

	public void listEvents(Date date){

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm");
		String dateString = new SimpleDateFormat("dd MMM, yyyy HH:mm").format(date);
		LocalDateTime dateConverted = LocalDateTime.parse(dateString, dtf);

		List<Event> filtered = events.stream()
				.filter(event -> event.getYear() == dateConverted.getYear() &&
						event.getMonth() ==dateConverted.getMonthValue() &&
						event.getDay()==dateConverted.getDayOfMonth())
				.collect(Collectors.toList());

		if(filtered.isEmpty())
			System.out.println("No events on this day!");
		else
			filtered.forEach(System.out::println);
	}

	public void listByMonth(){
		Map<Integer, Long> map = events.stream()
				.collect(Collectors.groupingBy(
						Event::getMonth,
						TreeMap::new,
						Collectors.counting()
				));

		IntStream.rangeClosed(1, 12)
				.forEach(month -> System.out.println(String.format("%d : %d", month, map.getOrDefault(month, 0L))));
	}

}