import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {
    static final long numRows = 128;
    static final long numCols = 8;

    private static long getSeatId(String encoding) {
        String rowEncoding = encoding.substring(0, 7);
        String colEncoding = encoding.substring(7, 10);

        long minRow = 0;
        long maxRow = numRows - 1;

        long minCol = 0;
        long maxCol = numCols - 1;

        for (char c : encoding.toCharArray()) {
            if (c == 'F') {
                maxRow = minRow + ((maxRow - minRow) / 2);
            } else if (c == 'B') {
                minRow = minRow + ((maxRow - minRow) / 2) + 1;
            } else if (c == 'R') {
                minCol = minCol + ((maxCol - minCol) / 2) + 1;
            } else {
                maxCol = minCol + ((maxCol - minCol) / 2);
            }
        }

        return minRow * 8 + minCol;
    }

    private static Set<Long> getAllIds() {
        Set<Long> ids = new HashSet<>();

        for (long i = 0; i < numRows; i++) {

            for (long j = 0; j < numCols; j++) {
                ids.add(i * 8 + j);
            }

        }

        return ids;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        final String inputFileName = "day5.txt";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

        List<Long> seatIds = Files.lines(path).map(Day5::getSeatId).collect(Collectors.toList());

        long highestID = seatIds.stream().mapToLong(n -> n).max().getAsLong();
        System.out.println(highestID);

        Set<Long> allPossibleIds = getAllIds();
        allPossibleIds.removeAll(seatIds);

        for (long n : allPossibleIds) {
            if (seatIds.contains(n - 1) && seatIds.contains(n + 1)) {
                System.out.println(n);
            }
        }
    }
}
