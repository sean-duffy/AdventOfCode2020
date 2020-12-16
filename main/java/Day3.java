import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

	public static long getNumberOfTreesPartOne(char[][] grid) {
		int currentRow = 0;
		int currentCol = 0;

		long treeCount = 0;

		while (currentRow < grid.length) {
			List<Character> path = new ArrayList<>();

			treeCount += hasTree(grid, currentRow, currentCol) ? 1 : 0;

			currentCol += 3;
			currentRow++;
		}

		return treeCount;
	}

	public static long getNumberOfTreesPartTwo(char[][] grid, SimpleEntry<Integer, Integer> slope) {
		int currentRow = 0;
		int currentCol = 0;

		long treeCount = 0;

		while (currentRow < grid.length) {
			List<Character> path = new ArrayList<>();

			treeCount += hasTree(grid, currentRow, currentCol) ? 1 : 0;

			currentCol += slope.getKey();
			currentRow += slope.getValue();
		}

		return treeCount;
	}

	private static boolean hasTree(char[][] grid, int row, int col) {
		return grid[row][col % grid[0].length] == '#';
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day3.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<String> lines = Files.lines(path).filter(s -> !s.isEmpty()).collect(Collectors.toList());

		int numRows = lines.size();
		int numCols = lines.get(0).length();

		char[][] grid = new char[numRows][numCols];

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				grid[row][col] = lines.get(row).charAt(col);
			}
		}

		System.out.println(getNumberOfTreesPartOne(grid));

		List<SimpleEntry> slopes = new ArrayList<>();
		slopes.add(new SimpleEntry(1, 1));
		slopes.add(new SimpleEntry(3, 1));
		slopes.add(new SimpleEntry(5, 1));
		slopes.add(new SimpleEntry(7, 1));
		slopes.add(new SimpleEntry(1, 2));

		long result = slopes.stream().mapToLong(slope -> getNumberOfTreesPartTwo(grid, slope)).reduce(1l, (a, b) -> a * b);
		System.out.println(result);
	}
}
