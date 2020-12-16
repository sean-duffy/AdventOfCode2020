import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {
	private static Map<Long, Integer> getJoltDifferences(List<Long> adapters) {
		Map<Long, Integer> differences = new HashMap<>();

		long currentJoltage = 0;
		for (long adapterJoltage : adapters) {
			long difference = adapterJoltage - currentJoltage;
			differences.put(difference, differences.getOrDefault(difference, 0) + 1);

			currentJoltage = adapterJoltage;
		}

		return differences;
	}

	private static long countArrangements(List<Long> adapters, long currentJoltage, Map<Long, Long> memo) {
		if (currentJoltage == adapters.get(adapters.size() - 1)) {
			return 1;
		} else {

			if (!memo.containsKey(currentJoltage)) {
				long arrangements = Stream.of(1, 2, 3).filter(n -> adapters.contains(currentJoltage + n)).mapToLong(n -> countArrangements(adapters, currentJoltage + n, memo)).sum();
				memo.put(currentJoltage, arrangements);
			}

			return memo.get(currentJoltage);
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		final String inputFileName = "day10.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<Long> adapters = Files.lines(path).map(Long::parseLong).collect(Collectors.toList());
		adapters.sort(Long::compareTo);

		Map<Long, Integer> differences = getJoltDifferences(adapters);
		int partOneResult = differences.get(1l) * (differences.get(3l) + 1);
		System.out.println(partOneResult);

		Map<Long, Long> memo = new HashMap<>();
		long partTwoResult = countArrangements(adapters, 0, memo);
		System.out.println(partTwoResult);
	}
}
