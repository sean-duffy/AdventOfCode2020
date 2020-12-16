import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Math;

public class Day13 {
	private static Integer getEarliestBusId(List<Integer> buses, Integer startTime) {
		List<Integer> busesAtThisTime = new ArrayList<>();
		Integer timeStamp = startTime;

		while (busesAtThisTime.isEmpty()) {
			Integer finalTimeStamp = timeStamp;
			busesAtThisTime = buses.stream().filter(n -> finalTimeStamp % n == 0).collect(Collectors.toList());
			timeStamp++;
		}

		return busesAtThisTime.get(0) * ((timeStamp - 1) - startTime);
	}

	private static Long findEarliestTimestamp(List<SimpleEntry<Integer, Integer>> buses) {
		long timeStamp = 0l;

		int matched = 1;
		while (matched < buses.size()) {
			timeStamp += buses.subList(0, matched).stream().map(e -> Long.valueOf(e.getValue())).reduce(1l, Math::multiplyExact);
			if (isSpecialTimestamp(buses.subList(0, matched + 1), timeStamp)) {
				matched++;
			}
		}

		return timeStamp;
	}

	static private boolean isSpecialTimestamp(List<SimpleEntry<Integer, Integer>> buses, Long timeStamp) {
		for (var bus : buses) {
			int i = bus.getKey();
			int n = bus.getValue();

			if ((timeStamp + i) % n != 0) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) throws FileNotFoundException {
		final String inputFileName = "day13.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Scanner scanner = new Scanner(new File(classLoader.getResource(inputFileName).getFile()));

		Integer timestamp = scanner.nextInt();
		String busesString = scanner.next();

		List<Integer> buses = Arrays.stream(busesString.split(",")).filter(s -> !s.equals("x")).map(Integer::parseInt).collect(Collectors.toList());

		Integer earliestId = getEarliestBusId(buses, timestamp);
		System.out.println(earliestId);

		List<Integer> allBuses = Arrays.stream(busesString.split(",")).map(s -> s.equals("x") ? -1 : Integer.parseInt(s)).collect(Collectors.toList());

		String[] busesArray = busesString.split(",");
		List<SimpleEntry<Integer, Integer>> busesWithOffsets = IntStream
				.range(0, busesString.split(",").length).boxed()
				.map(i -> new SimpleEntry<>(i, busesArray[i]))
				.filter(e -> !e.getValue().equals("x"))
				.map(e -> new SimpleEntry<>(e.getKey(), Integer.parseInt(e.getValue())))
				.collect(Collectors.toList());

		Long earliestPartTwo = findEarliestTimestamp(busesWithOffsets);
		System.out.println(earliestPartTwo);
	}
}
