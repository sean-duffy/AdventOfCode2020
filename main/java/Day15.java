import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15 {
	private static Integer findNthNumber(List<Integer> startingNumbers, int n) {
		int count = 1;
		int lastNumber = 0;
		Map<Integer, List<Integer>> lastSeen = new HashMap<>();

		for (int number : startingNumbers) {
			lastSeen.put(number, new ArrayList<>(Arrays.asList(count)));
			count++;
			lastNumber = number;
		}

		while (count < n + 1) {
			int next = nextNumber(count, lastNumber, lastSeen);

			if (lastSeen.containsKey(next)) {
				lastSeen.get(next).add(count);
			} else {
				lastSeen.put(next, new ArrayList<>(Arrays.asList(count)));
			}

			lastNumber = next;
			count++;
		}

		return lastNumber;
	}

	private static Integer nextNumber(int count, int lastNumber, Map<Integer, List<Integer>> lastSeen) {
		if (lastSeen.containsKey(lastNumber) && lastSeen.get(lastNumber).size() > 1) {
			int size = lastSeen.get(lastNumber).size();
			return lastSeen.get(lastNumber).get(size - 1) - lastSeen.get(lastNumber).get(size - 2);
		}

		return 0;
	}

	public static void main(String[] args) throws URISyntaxException {
		final String puzzleInputString = "1,20,8,12,0,14";

		List<Integer> startingNumbers = Arrays.stream(puzzleInputString.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		int partOne = findNthNumber(startingNumbers, 30000000);
		System.out.println(partOne);
	}
}
