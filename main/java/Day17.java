import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day17 {
	public static class Coordinate {
		public int x;
		public int y;
		public int z;
		public int w;
		private boolean isHypercube = false;

		public Coordinate(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Coordinate(int x, int y, int z, int w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
			isHypercube = true;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Coordinate that = (Coordinate) o;

			if (x != that.x) return false;
			if (y != that.y) return false;
			if (isHypercube && w != that.w) return false;
			return z == that.z;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			result = 31 * result + z;
			if (isHypercube) {
				result = 31 * result + w;
			}
			return result;
		}

		@Override
		public String toString() {
			return "Coordinate{" +
					"x=" + x +
					", y=" + y +
					", z=" + z +
					'}';
		}
	}

	private static char getNewState(Coordinate position, Map<Coordinate, Character> cubes) {
		Character currentState = cubes.get(position);
		List<Character> neighbourStates = getNeighbourStates(position, cubes);
		long activeNeighbours = neighbourStates.stream().filter(c -> c != null && c == '#').count();

		if (currentState != null && currentState == '#') {
			if (activeNeighbours == 2 || activeNeighbours == 3) {
				return '#';
			} else {
				return '.';
			}
		} else {
			if (activeNeighbours == 3) {
				return '#';
			} else {
				return '.';
			}
		}
	}

	private static List<Character> getNeighbourStates(Coordinate position, Map<Coordinate, Character> cubes) {
		List<Character> neighbours = new ArrayList<>();

		for (int z = -1; z <= 1; z++) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {

					if (position.isHypercube) {

						for (int w = -1; w <= 1; w++) {
							Coordinate neighbourPosition = new Coordinate(position.x + x, position.y + y, position.z + z, position.w + w);

							if (!neighbourPosition.equals(position)) {
								neighbours.add(cubes.get(neighbourPosition));
							}
						}

					} else {
						Coordinate neighbourPosition = new Coordinate(position.x + x, position.y + y, position.z + z);
						if (!neighbourPosition.equals(position)) {
							neighbours.add(cubes.get(neighbourPosition));
						}
					}

				}
			}
		}

		return neighbours;
	}

	private static int applyOneCycle(int size, Map<Coordinate, Character> cubes) {
		Map<Coordinate, Character> currentState = new HashMap<>(cubes);
		int min = 0 - size/2;
		int max = 0 + size/2;

		for (int z = min; z <= max; z++) {
			for (int x = min; x <= max; x++) {
				for (int y = min; y <= max; y++) {
					Coordinate position = new Coordinate(x, y, z);
					cubes.put(position, getNewState(position, currentState));
				}
			}
		}

		return size + 2;
	}

	private static int applyOneCycle4D(int size, Map<Coordinate, Character> cubes) {
		Map<Coordinate, Character> currentState = new HashMap<>(cubes);
		int min = 0 - size/2;
		int max = 0 + size/2;

		for (int z = min; z <= max; z++) {
			for (int x = min; x <= max; x++) {
				for (int y = min; y <= max; y++) {
					for (int w = min; w <= max; w++) {
						Coordinate position = new Coordinate(x, y, z, w);
						cubes.put(position, getNewState(position, currentState));
					}
				}
			}
		}

		return size + 2;
	}

	private static long getActiveCubesCount(Map<Coordinate, Character> cubes) {
		return cubes.values().stream().filter(c -> c == '#').count();
	}

	private static void printCube(int size, Map<Coordinate, Character> cubes) {
		int min = 0 - size/2;
		int max = 0 + size/2;

		for (int z = min; z <= max; z++) {
			System.out.println("z=" + z);

			for (int x = min; x <= max; x++) {
				for (int y = min; y <= max; y++) {
					System.out.print(cubes.get(new Coordinate(x, y, z)));
				}
				System.out.print("\n");
			}

			System.out.print("\n");
		}
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day17.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());
		List<String> lines = Files.lines(path).collect(Collectors.toList());


		Map<Coordinate, Character> cubes = new HashMap<>();

		int size = lines.size();

		for (int row = 0; row < size; row++) {
			char[] line = lines.get(row).toCharArray();

			for (int col = 0; col < size; col++) {
				cubes.put(new Coordinate(row - size / 2, col - size / 2, 0), line[col]);
			}
		}

		int cycleCount = 1;

		while (cycleCount <= 6) {
			size += 2;
			applyOneCycle(size, cubes);

			cycleCount++;
		}

		System.out.println(getActiveCubesCount(cubes));

		cubes.clear();

		size = lines.size();

		for (int row = 0; row < size; row++) {
			char[] line = lines.get(row).toCharArray();

			for (int col = 0; col < size; col++) {
				cubes.put(new Coordinate(row - size / 2, col - size / 2, 0, 0), line[col]);
			}
		}

		cycleCount = 1;
		while (cycleCount <= 6) {
			size += 2;
			applyOneCycle4D(size, cubes);

			cycleCount++;
		}

		System.out.println(getActiveCubesCount(cubes));
	}
}
