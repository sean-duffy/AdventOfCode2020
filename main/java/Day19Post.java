import generated.Day19Lexer;
import generated.Day19Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day19Post {

	private static boolean isValid(String message) {
		Day19Lexer lexer = new Day19Lexer(CharStreams.fromString(message));
		Day19Parser parser = new Day19Parser(new CommonTokenStream(lexer));
		parser.rule0();

		return parser.getNumberOfSyntaxErrors() == 0;
	}

	// Run this after the ANTLR grammar and the corresponding lexer and parser have been generated
	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day19.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		String fileContent = Files.readString(path);

		List<String> messages = Arrays.asList(fileContent.split("\\R\\R\\R\\R")[1].split("\n").clone());
		long validMessageCount = messages.stream().map(String::strip).map(Day19Post::isValid).filter(b -> b).count();
		System.out.println(validMessageCount);
	}
}
