import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day9 {
	private static long findContiguousSet(List<Long> numbers, long target) {
		for (int start = 0; start < numbers.size(); start++) {

			for (int end = start; end < numbers.size(); end ++) {
				List<Long> window = numbers.subList(start, end);
				if (window.stream().mapToLong(n -> n).sum() == target) {
					return window.stream().min(Long::compareTo).get() + window.stream().max(Long::compareTo).get();
				}
			}

		}

		return 0;
	}

	private static long findWeakness(List<Long> numbers, Integer windowSize) {
		Map<Integer, Long> slidingWindow = new HashMap<>();

		for (int i = 0; i < numbers.size(); i++) {
			long n = numbers.get(i);

			if (i > windowSize) {
				Set<Long> windowSet = new HashSet<>(slidingWindow.values());

				if (!isNumberValid(n, windowSet)) {
					return n;
				}
			}

			slidingWindow.put(i, n);

			if (i > windowSize) {
				slidingWindow.remove(i - windowSize);
			}
		}

		return 0;
	}

	private static boolean isNumberValid(long n, Set<Long> windowSet) {
		for (long numToTry : windowSet) {
			if (windowSet.contains(n - numToTry) && numToTry != n / 2) {
				return true;
			}
		}

		// If we get to here the window does not contain numbers adding up to n
		return false;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day9.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<Long> numbers = Files.lines(path).map(Long::parseLong).collect(Collectors.toList());

		long weakness = findWeakness(numbers, 25);
		System.out.println(weakness);

		long partTwo = findContiguousSet(numbers, weakness);
		System.out.println(partTwo);
	}
}
