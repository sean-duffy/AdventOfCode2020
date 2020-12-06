import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day1 {
	static final int target = 2020;

	private static int calculateResult(List<Integer> entries) {
		Set<Integer> entrySet = new HashSet<>(entries);

		for (int n : entries) {
			int otherNumber = target - n;

			if (entrySet.contains(otherNumber)) {
				return n * otherNumber;
			}
		}

		return -1;
	}

	private static int calculatePartTwo(List<Integer> entries) {
		Set<Integer> entrySet = new HashSet<>(entries);

		for (int n : entries) {
			int remaining = target - n;

			for (int m : entries) {
				int finalRemaining = remaining - m;

				if (entrySet.contains(finalRemaining)) {
					return n * m * finalRemaining;
				}
			}

		}

		return -1;
	}

	public static void main(String[] args) throws FileNotFoundException {
		final String inputFileName = "day1.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Scanner scanner = new Scanner(new File(classLoader.getResource(inputFileName).getFile()));

		List<Integer> entries = new ArrayList<>();
		scanner.forEachRemaining(line -> entries.add(Integer.parseInt(line)));

		Integer result = calculateResult(entries);
		System.out.println(result);

		Integer result2 = calculatePartTwo(entries);
		System.out.println(result2);
	}
}
