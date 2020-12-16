import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6 {
	private static long getYesCount(List<String> answers) {
		return answers.stream().map(String::chars).flatMapToInt(x -> x).filter(Character::isLetter).distinct().count();
	}

	private static long getYesCountPartTwo(List<String> answers) {
		Map<Integer, Integer> charCount = new HashMap<>();
		answers.stream().map(String::chars).flatMapToInt(x -> x).filter(Character::isLetter).forEach(n -> charCount.put(n, charCount.getOrDefault(n, 0) + 1));

		return charCount.entrySet().stream().filter(entry -> entry.getValue() == answers.size()).count();
	}

	public static void main(String[] args) throws FileNotFoundException {
		final String inputFileName = "day6.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		Scanner scanner = new Scanner(new File(classLoader.getResource(inputFileName).getFile()));
		scanner.useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));

		long countSum = 0;
		long countSumPartTwo = 0;
		while (scanner.hasNext()) {
			String group = scanner.next();
			List<String> answers = Arrays.stream(group.trim().split("\\n")).collect(Collectors.toList());
			countSum += getYesCount(answers);
			countSumPartTwo += getYesCountPartTwo(answers);
		}

		System.out.println(countSum);
		System.out.println(countSumPartTwo);

	}
}
