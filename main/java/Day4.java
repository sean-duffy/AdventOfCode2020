
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
	private static boolean isPassportValid(Collection<String> requiredFields, String passport) {
		final Pattern p = Pattern.compile("(\\w{3}):[^\\s]*");
		Matcher matcher = p.matcher(passport);

		Set<String> includedFields = new HashSet<>();
		while (matcher.find()) {
			String fieldName = matcher.group(1);
			includedFields.add(fieldName);
		}

		return includedFields.containsAll(requiredFields);
	}

	private static boolean isPassportValid(Map<String, Function<String, Boolean>> requiredFields, String passport) {
		final Pattern p = Pattern.compile("(\\w{3}):([^\\s]*)");
		Matcher matcher = p.matcher(passport);

		final Map<String, String> passportData = new HashMap<>();
		while (matcher.find()) {
			passportData.put(matcher.group(1), matcher.group(2));
		}

		return requiredFields.entrySet().stream()
				.allMatch(entry -> passportData.containsKey(entry.getKey()) && entry.getValue().apply(passportData.get(entry.getKey())));
	}

	public static void main(String[] args) throws FileNotFoundException {
		final String inputFileName = "day4.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		Scanner scanner = new Scanner(new File(classLoader.getResource(inputFileName).getFile()));
		scanner.useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));

		Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

		Map<String, Function<String, Boolean>> requiredFields = new HashMap<>();

		requiredFields.put("byr", s -> s.matches("\\d{4}") && Integer.parseInt(s) >= 1920 && Integer.parseInt(s) <= 2002);
		requiredFields.put("iyr", s -> s.matches("\\d{4}") && Integer.parseInt(s) >= 2010 && Integer.parseInt(s) <= 2020);
		requiredFields.put("eyr", s -> s.matches("\\d{4}") && Integer.parseInt(s) >= 2020 && Integer.parseInt(s) <= 2030);

		requiredFields.put("hgt", s -> {
			Matcher m = Pattern.compile("(\\d*)(cm|in)").matcher(s);

			if (!m.matches()) {
				return false;
			}

			int height = Integer.parseInt(m.group(1));

			if (m.group(2).equals("cm")) {
				return 150 <= height && height <= 193;
			} else if (m.group(2).equals("in")) {
				return 59 <= height && height <= 76;
			}

			return false;
		});

		requiredFields.put("hcl", s -> s.matches("#[0-9|a-f]{6}"));
		requiredFields.put("ecl", s -> s.matches("amb|blu|brn|gry|grn|hzl|oth"));
		requiredFields.put("pid", s -> s.matches("\\d{9}"));

		long validCount = 0;
		long partTwoValidCount = 0;
		while (scanner.hasNext()) {
			String passportString = scanner.next();

			validCount += isPassportValid(requiredFields.keySet(), passportString) ? 1 : 0;
			partTwoValidCount += isPassportValid(requiredFields, passportString) ? 1 : 0;
		}

		System.out.println(validCount);
		System.out.println(partTwoValidCount);
	}

}
