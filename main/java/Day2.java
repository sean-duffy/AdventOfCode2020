import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
	private static boolean isPasswordValid(char letter, int min, int max, String password) {
		long letterCount = password.chars().filter(c -> c == letter).count();
		return letterCount >= min && letterCount <= max;
	}

	private static boolean isPasswordValid2(char letter, int min, int max, String password) {
		return password.charAt(min - 1) == letter ^ password.charAt(max - 1) == letter;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		final String inputFileName = "day2.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		final Pattern p = Pattern.compile("(\\d*)-(\\d*)\\s(\\w):\\s(\\w*)");

		long validPasswordCount = Files.lines(path)
				.map(p::matcher)
				.filter(Matcher::matches)
				.map(m -> isPasswordValid(m.group(3).charAt(0), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), m.group(4)))
				.filter(bool -> bool)
				.count();

		System.out.println(validPasswordCount);

		long validPasswordCount2 = Files.lines(path)
				.map(p::matcher)
				.filter(Matcher::matches)
				.map(m -> isPasswordValid2(m.group(3).charAt(0), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), m.group(4)))
				.filter(bool -> bool)
				.count();

		System.out.println(validPasswordCount2);
	}
}
