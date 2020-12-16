import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
	private static List<SimpleEntry<Integer, Integer>> combineRules(List<SimpleEntry<Integer, Integer>> rules) {
		List<SimpleEntry<Integer, Integer>> combinedRules = new ArrayList<>();

		Queue<SimpleEntry<Integer, Integer>> toProcess = new LinkedList<>(rules);
		List<SimpleEntry<Integer, Integer>> redundant = new ArrayList<>();
		while (!toProcess.isEmpty()) {
			var rule = toProcess.remove();
			int min = rule.getKey();
			int max = rule.getValue();

			SimpleEntry<Integer, Integer> match = null;
			for (var otherRule : toProcess) {
				int otherMin = otherRule.getKey();
				int otherMax = otherRule.getValue();

				if (min > otherMin && max < otherMax) {
					redundant.add(rule);
					break;
				}

				if (max > otherMin && max < otherMax) {
					match = otherRule;
					break;
				}
			}

			if (match != null) {
				toProcess.remove(match);
				combinedRules.add(new SimpleEntry<>(min, match.getValue()));
			}
		}

		toProcess.removeAll(redundant);
		combinedRules.addAll(toProcess);

		return combinedRules;
	}

	private static boolean isValueValid(int value, List<SimpleEntry<Integer, Integer>> rules) {
		for (var rule : rules) {
			if (isValueValid(value, rule)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isValueValid(int value, SimpleEntry<Integer, Integer> rule) {
		return value >= rule.getKey() && value <= rule.getValue();
	}

	private static boolean isTicketValid(String ticket, List<SimpleEntry<Integer, Integer>> rules) {
		return Arrays.stream(ticket.split(","))
				.mapToInt(Integer::parseInt)
				.allMatch(n -> isValueValid(n, rules));
	}

	private static Map<Integer, String> findColumnPositions(List<String> tickets, Map<String, List<SimpleEntry<Integer, Integer>>> ruleMap) {
		Map<Integer, List<Integer>> columnValues = new HashMap<>();
		Map<Integer, List<String>> columnLabelPossibilities = new HashMap<>();
		Map<Integer, String> columnLabels = new HashMap<>();

		for (String ticket : tickets) {
			String[] values = ticket.split(",");
			for (int i = 0; i < values.length; i++) {
				int value = Integer.parseInt(values[i]);

				if (!columnValues.containsKey(i)) {
					columnValues.put(i, new ArrayList<>());
					columnLabelPossibilities.put(i, new ArrayList<>());
					columnLabels.put(i, null);
				}

				columnValues.get(i).add(value);
			}
		}

		for (var entry : columnValues.entrySet()) {
			final List<Integer> values = entry.getValue();
			List<String> columnLabelList = ruleMap.entrySet().stream()
					.filter(e -> values.stream().allMatch(n -> isValueValid(n, e.getValue().get(0)) || isValueValid(n, e.getValue().get(1))))
					.map(Map.Entry::getKey)
					.collect(Collectors.toList());

			columnLabelPossibilities.get(entry.getKey()).addAll(columnLabelList);
		}

		while (columnLabels.values().contains(null)) {
			for (var entry : columnLabelPossibilities.entrySet()) {
				if (entry.getValue().size() == 1) {
					columnLabels.put(entry.getKey(), entry.getValue().get(0));
				}
			}

			// Remove this label from the other possibilities
			for (int i : columnLabelPossibilities.keySet()) {
				for (String label : columnLabels.values()) {
					if (label != null && columnLabelPossibilities.get(i).contains(label)) {
						columnLabelPossibilities.get(i).remove(label);
					}
				}
			}
		}

		return columnLabels;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day16.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());
		String fileContent = Files.readString(path);

		String[] parts = fileContent.split("your ticket:|nearby tickets:");

		String rules = parts[0];
		String yourTicket = parts[1];
		String nearbyTickets = parts[2];

		Pattern rulePattern = Pattern.compile("([\\w\\s]*):\\s(\\d*)-(\\d*)\\sor\\s(\\d*)-(\\d*)");

		Map<String, List<SimpleEntry<Integer, Integer>>> ruleMap = new HashMap<>();
		List<SimpleEntry<Integer, Integer>> allRules = new ArrayList<>();
		Matcher ruleMatcher = rulePattern.matcher(rules);
		while (ruleMatcher.find()) {
			String name = ruleMatcher.group(1).strip();
			int low1 = Integer.parseInt(ruleMatcher.group(2));
			int high1 = Integer.parseInt(ruleMatcher.group(3));
			int low2 = Integer.parseInt(ruleMatcher.group(4));
			int high2 = Integer.parseInt(ruleMatcher.group(5));

			ruleMap.put(name, new ArrayList<>(Arrays.asList(new SimpleEntry<>(low1, high1), new SimpleEntry<>(low2, high2))));

			allRules.add(new SimpleEntry<>(low1, high1));
			allRules.add(new SimpleEntry<>(low2, high2));
		}

		int errorRate = Arrays.stream(nearbyTickets.split(",|\\R")).filter(Predicate.not(String::isBlank)).mapToInt(Integer::parseInt).filter(n -> !isValueValid(n, allRules)).sum();
		System.out.println(errorRate);

		List<String> validTickets = Arrays.stream(nearbyTickets.split("\\R")).filter(Predicate.not(String::isBlank)).filter(t -> isTicketValid(t, allRules)).collect(Collectors.toList());
		validTickets.add(yourTicket.strip());
		Map<Integer, String> columnPositions = findColumnPositions(validTickets, ruleMap);

		System.out.println(columnPositions);

		List<Integer> yourTicketValues = Arrays.stream(yourTicket.split(",")).map(String::strip).map(Integer::parseInt).collect(Collectors.toList());

		Long result = columnPositions.entrySet().stream()
				.filter(e -> e.getValue().startsWith("departure"))
				.map(e -> yourTicketValues.get(e.getKey()))
				.map(Long::valueOf)
				.reduce(1l, (a, b) -> a * b);

		System.out.println(result);
	}
}
