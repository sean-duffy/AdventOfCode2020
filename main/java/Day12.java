import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 {
	private enum MovementCommand {
		N,
		E,
		S,
		W,
		L,
		R,
		F
	}

	private static Integer calculateManhattanDistance(List<SimpleEntry<MovementCommand, Integer>> instructions) {
		int x = 0;
		int y = 0;

		Integer currentDirection = 1;

		for (var instruction : instructions) {
			//System.out.println(x + ", " + y + ", " + currentDirection + ", " + instruction);
			switch (instruction.getKey()) {
				case N:
				case S:
				case E:
				case W:
					var movement = getMovement(instruction);
					x += movement.getKey();
					y += movement.getValue();
					break;
				case L:
					currentDirection = Math.floorMod(currentDirection - instruction.getValue() / 90, 4);
					break;
				case R:
					currentDirection = Math.floorMod(currentDirection + instruction.getValue() / 90, 4);
					break;
				case F:
					var forwardMovement = getMovement(new SimpleEntry<>(
							MovementCommand.values()[currentDirection],
							instruction.getValue()
					));

					x += forwardMovement.getKey();
					y += forwardMovement.getValue();
					break;
			}
		}

		return Math.abs(x) + Math.abs(y);
	}

	private static Integer calculateActualManhattanDistance(List<SimpleEntry<MovementCommand, Integer>> instructions) {
		int x = 0;
		int y = 0;

		int waypointX = 10;
		int wayPointY = 1;

		for (var instruction : instructions) {
			//System.out.println("x: " + x + ", y: " + y + ", waypointX: " + waypointX + ", waypointY: " + wayPointY + ", " + instruction);
			switch (instruction.getKey()) {
				case N:
				case S:
				case E:
				case W:
					var movement = getMovement(instruction);
					waypointX += movement.getKey();
					wayPointY += movement.getValue();
					break;

				case F:
					x += waypointX * instruction.getValue();
					y += wayPointY * instruction.getValue();
					break;

				case R:
					int Rrotation = instruction.getValue();

					while (Rrotation > 0) {
						int temp = waypointX;
						waypointX = wayPointY;
						wayPointY = temp * -1;

						Rrotation -= 90;
					}
					break;

				case L:
					int Lrotation = instruction.getValue();

					while (Lrotation > 0) {
						int temp = waypointX;
						waypointX = wayPointY * -1;
						wayPointY = temp;

						Lrotation -= 90;
					}
					break;
			}
		}

		return Math.abs(x) + Math.abs(y);
	}

	private static SimpleEntry<Integer, Integer> getMovement(SimpleEntry<MovementCommand, Integer> instruction) {
		int x = 0;
		int y = 0;

		switch (instruction.getKey()) {
			case N:
				y += instruction.getValue();
				break;
			case S:
				y -= instruction.getValue();
				break;
			case E:
				x += instruction.getValue();
				break;
			case W:
				x -= instruction.getValue();
		}

		return new SimpleEntry<>(x, y);
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day12.txt";
		Pattern lineMatch = Pattern.compile("([NSEWLRF])(\\d*)");

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<SimpleEntry<MovementCommand, Integer>> instructions = Files.lines(path)
				.map(lineMatch::matcher)
				.filter(Matcher::matches)
				.map(m -> new SimpleEntry<MovementCommand, Integer>(MovementCommand.valueOf(m.group(1)), Integer.parseInt(m.group(2))))
				.collect(Collectors.toList());

		int manhattanDistance = calculateManhattanDistance(instructions);
		System.out.println(manhattanDistance);

		int actualManhattanDistance = calculateActualManhattanDistance(instructions);
		System.out.println(actualManhattanDistance);
	}
}
