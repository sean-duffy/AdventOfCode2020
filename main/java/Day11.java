import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {
	private static AbstractMap.SimpleEntry<Character[][], Integer> applyRules(Character[][] layout) {
		Character[][] newLayout = new Character[layout.length][layout[0].length];
		int changeCount = 0;

		for (int row = 0; row < layout.length; row++) {
			for (int col = 0; col < layout[0].length; col++) {
				newLayout[row][col] = layout[row][col];

				if (layout[row][col] != '.') {

					int adjacentOccupiedSeatsCount = getVisibleOccupiedSeatsCount(layout, row, col);
					if (adjacentOccupiedSeatsCount == 0 && layout[row][col] == 'L') {
						newLayout[row][col] = '#';
						changeCount++;
					} else if (adjacentOccupiedSeatsCount >= 5 && layout[row][col] == '#') {
						newLayout[row][col] = 'L';
						changeCount++;
					}
				}

			}
		}

		return new AbstractMap.SimpleEntry<>(newLayout, changeCount);
	}

	private static int getAdjacentOccupiedSeatsCount(Character[][] layout, int seatRow, int seatCol) {
		final int height = layout.length;
		final int width = layout[0].length;

		int count = 0;

		for (int row = Math.max(seatRow - 1, 0); row < Math.min(seatRow + 2, height); row++) {
			for (int col = Math.max(seatCol - 1, 0); col < Math.min(seatCol + 2, width); col++) {
				if (layout[row][col] == '#' && !(row == seatRow && col == seatCol)) {
					count++;
				}
			}
		}

		return count;
	}

	private static int getVisibleOccupiedSeatsCount(Character[][] layout, int seatRow, int seatCol) {
		final int height = layout.length;
		final int width = layout[0].length;

		int count = 0;

		for (int rowDirection = -1; rowDirection <= 1; rowDirection++) {
			for (int colDirection = -1; colDirection <= 1; colDirection++) {
				int row = seatRow + rowDirection;
				int col = seatCol + colDirection;

				while (!(row == seatRow && col == seatCol) && row >= 0 && row < height && col >= 0 && col < width) {

					if (layout[row][col] == '#') {
						count++;
						break;
					}

					if (layout[row][col] == 'L') {
						// An empty seat blocks this line of sight
						break;
					}

					if (row != seatRow) {
						row += Math.signum(row - seatRow);
					}

					if (col != seatCol) {
						col += Math.signum(col - seatCol);
					}

				}

			}
		}

		return count;
	}

	private static long getOccupiedSeatCount(Character[][] layout) {
		return Arrays.stream(layout)
				.flatMap(c -> Arrays.stream(c).map(d -> d))
				.filter(c -> c == '#')
				.count();
	}

	private static void printLayout(Character[][] layout) {
		for (Character[] line : layout) {
			Arrays.stream(line).forEach(System.out::print);
			System.out.println("");
		}
		System.out.println("");
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day11.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<String> lines = Files.lines(path).collect(Collectors.toList());

		Character[][] layout = new Character[lines.size()][lines.get(0).length()];
		for (int i = 0; i < lines.size(); i++) {
			char[] line = lines.get(i).toCharArray();
			for (int j = 0; j < lines.get(i).length(); j++) {
				layout[i][j] = line[j];
			}
		}

		int changeCount = 1;
		while (changeCount > 0) {
			AbstractMap.SimpleEntry<Character[][], Integer> result = applyRules(layout);
			layout = result.getKey();
			changeCount = result.getValue();
		}

		System.out.println(getVisibleOccupiedSeatsCount(layout, 0, 2));

		long stepOneAnswer = getOccupiedSeatCount(layout);
		System.out.println(stepOneAnswer);
	}
}
