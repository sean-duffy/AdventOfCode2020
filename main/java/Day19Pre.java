import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19Pre {

	private static final Pattern terminalPattern = Pattern.compile("(\\d*):\\s\"(\\w)\"");
	private static final Pattern rulePattern = Pattern.compile("(\\d+):\\s((?:(?:\\d+|\\|)\\s?)+)");

	// Run this initially to generate the antlr grammar
	public static void main(String[] args) throws IOException, URISyntaxException {
		final String inputFileName = "day19.txt";
		final String outputFileName = "src/main/java/generated/Day19.g4";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		String fileContent = Files.readString(path);

		List<String> ruleStrings = Arrays.asList(fileContent.split("\\R\\R\\R\\R")[0].split("\n").clone());

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		writer.write("grammar Day19;");
		writer.newLine();

		boolean partTwo = true;

		for (String ruleString : ruleStrings) {
			Matcher terminalMatcher = terminalPattern.matcher(ruleString.trim());
			Matcher ruleMatcher = rulePattern.matcher(ruleString.trim());

			if (terminalMatcher.matches()) {
				writer.write(String.format("rule%s: '%s';", terminalMatcher.group(1), terminalMatcher.group(2)));
			} else if (ruleMatcher.matches()) {
				StringBuilder ruleContent = new StringBuilder();
				Scanner scanner = new Scanner(ruleMatcher.group(2));
				while (scanner.hasNext()) {
					String next = scanner.next();
					if (next.equals("|")) {
						ruleContent.append(" |");
					} else {
						ruleContent.append(String.format(" rule%s", next));
					}
				}

				// For rule 0, match EOF so we can check if the whole input matches the grammar
				if (ruleMatcher.group(1).equals("0")) {
					writer.write(String.format("rule%s: %s EOF;", ruleMatcher.group(1), ruleContent));
				} else if (partTwo && ruleMatcher.group(1).equals("8")) {
					writer.write("rule8: rule42 | rule42 rule8;");
				} else if (partTwo && ruleMatcher.group(1).equals("11")) {
					writer.write("rule11: rule42 rule31 | rule42 rule11 rule31;");
				} else {
					writer.write(String.format("rule%s: %s;", ruleMatcher.group(1), ruleContent));
				}
			}

			writer.newLine();
		}

		writer.close();
	}
}
