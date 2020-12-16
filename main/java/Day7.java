import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 {

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day7.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		Pattern linePattern = Pattern.compile("([a-z\\s]*)s\\scontain\\s(no\\sother\\sbags|(?:(?:,\\s)?\\d+\\s[a-z\\s]*)*).");
		Pattern bagPattern = Pattern.compile("(?:, )?(\\d+) ([a-z\\s]*)");

		Map<String, List<String>> containerMap = new HashMap<>();
		Map<String, List<SimpleEntry<Long, String>>> contentsMap = new HashMap<>();

		for (String line : Files.lines(path).collect(Collectors.toList())) {
			Matcher lineMatch = linePattern.matcher(line);
			lineMatch.matches();

			String container = lineMatch.group(1);
			String contentsString = lineMatch.group(2);

			List<SimpleEntry<Long, String>> contentsList = new ArrayList<>();
			Matcher bagMatch = bagPattern.matcher(contentsString);
			while (bagMatch.find()) {
				long n = Long.parseLong(bagMatch.group(1));
				String bagName = bagMatch.group(2);

				if (n > 1) {
					bagName = bagName.substring(0, bagName.length() - 1);
				}

				contentsList.add(new SimpleEntry<>(n, bagName));
			}

			contentsMap.put(container, contentsList);

			for (SimpleEntry<Long, String> bagSimpleEntry : contentsList) {
				String bag = bagSimpleEntry.getValue();

				if (containerMap.containsKey(bag)) {
					containerMap.get(bag).add(container);
				} else {
					List<String> containers = new ArrayList<>();
					containers.add(container);
					containerMap.put(bag, containers);
				}
			}
		}

		System.out.println(contentsMap);

		Set<String> bagSet = new HashSet<>();
		final String start = "shiny gold bag";

		Queue<String> toVisit = new LinkedList<>();
		toVisit.add(start);
		while (!toVisit.isEmpty()) {
			String thisBag = toVisit.remove();
			bagSet.add(thisBag);

			toVisit.addAll(containerMap.getOrDefault(thisBag, new ArrayList<>()));
		}

		int canContainCount = bagSet.size() - 1;
		System.out.println(canContainCount);

		Queue<SimpleEntry<Long, String>> SimpleEntrysToVisit = new LinkedList<>();
		SimpleEntrysToVisit.add(new SimpleEntry(1l, start));

		long count = 0;
		while (!SimpleEntrysToVisit.isEmpty()) {
			SimpleEntry<Long, String> bagSimpleEntry = SimpleEntrysToVisit.remove();
			count += bagSimpleEntry.getKey();

			for (int i = 0; i < bagSimpleEntry.getKey(); i++) {
				SimpleEntrysToVisit.addAll(contentsMap.getOrDefault(bagSimpleEntry.getValue(), new ArrayList<>()));
			}
		}

		System.out.println(count - 1);
	}
}
