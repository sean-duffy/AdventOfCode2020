import generated.Day18Lexer;
import generated.Day18Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class Day18 {
	private static long evaluate(String expression) {
		return evaluate(expression, false);
	}

	private static long evaluate(String expression, boolean addFirst) {
		Day18Lexer lexer = new Day18Lexer(CharStreams.fromString(expression));
		Day18Parser parser = new Day18Parser(new CommonTokenStream(lexer));

		if (addFirst) {
			return evaluate(parser.addExpr());
		} else {
			return evaluate(parser.expr());
		}
	}

	private static long evaluate(Day18Parser.ExprContext expr) {
		if (expr.NUM() != null) {
			return Long.parseLong(expr.NUM().getText());
		}

		if (expr.ADD() != null) {
			return evaluate(expr.expr(0)) + evaluate(expr.expr(1));
		}

		if (expr.MULT() != null) {
			return evaluate(expr.expr(0)) * evaluate(expr.expr(1));
		}

		return evaluate(expr.expr(0));
	}

	private static long evaluate(Day18Parser.AddExprContext expr) {
		if (expr.NUM() != null) {
			return Long.parseLong(expr.NUM().getText());
		}

		if (expr.ADD() != null) {
			return evaluate(expr.addExpr(0)) + evaluate(expr.addExpr(1));
		}

		if (expr.MULT() != null) {
			return evaluate(expr.addExpr(0)) * evaluate(expr.addExpr(1));
		}

		return evaluate(expr.addExpr(0));
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day18.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		assert evaluate("1 + 2 * 3 + 4 * 5 + 6") == 71;
		assert evaluate("2 * 3 + (4 * 5)") == 26;
		assert evaluate("5 + (8 * 3 + 9 + 3 * 4 * 3)") == 437;
		assert evaluate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") == 13632;

		long total = Files.lines(path).filter(Predicate.not(String::isBlank)).mapToLong(Day18::evaluate).sum();
		System.out.println(total);

		assert evaluate("1 + 2 * 3 + 4 * 5 + 6", true) == 231;
		assert evaluate("1 + (2 * 3) + (4 * (5 + 6))", true) == 51;
		assert evaluate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", true) == 669060;
		assert evaluate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", true) == 23340;

		total = Files.lines(path).filter(Predicate.not(String::isBlank)).mapToLong(s -> evaluate(s, true)).reduce(0l, Math::addExact);
		System.out.println(total);
	}
}
