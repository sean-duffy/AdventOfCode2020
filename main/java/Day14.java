import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 {
	private static Long sumOfMemoryValues(List<Matcher> instructions) {
		long[] memory = new long[100000];
		char[] mask = new char[36];

		for (Matcher m : instructions) {
			if (m.group(1).equals("mask")) {
				mask = m.group(2).toCharArray();
			} else {
				int address = Integer.parseInt(m.group(1));
				long value = Long.parseLong(m.group(2));
				long maskedValue = 0l;

				long selectorMask = 1l;
				for (int i = 0; i < 36; i++) {
					if (mask[mask.length - 1 - i] == 'X') {
						maskedValue += selectorMask & value;
					} else if (mask[mask.length - i - 1] == '1') {
						maskedValue += selectorMask;
					}

					selectorMask = selectorMask << 1;
				}

				memory[address] = maskedValue;
			}
		}

		return Arrays.stream(memory).sum();
	}

	private static Long sumOfMemoryValuesAddressDecoder(List<Matcher> instructions) {
		Map<Long, Long> memory = new HashMap<>();
		char[] mask = new char[36];

		for (Matcher m : instructions) {
			if (m.group(1).equals("mask")) {
				mask = m.group(2).toCharArray();
			} else {
				int address = Integer.parseInt(m.group(1));
				long value = Long.parseLong(m.group(2));

				List<Integer> floatingBits = new ArrayList<>();
				long maskedAddress = 0l;

				long selectorMask = 1l;
				for (int i = 0; i < 36; i++) {
					if (mask[mask.length - 1 - i] == 'X') {
						floatingBits.add(i);
					} else if (mask[mask.length - i - 1] == '1') {
						maskedAddress += selectorMask;
					} else {
						maskedAddress += selectorMask & address;
					}

					selectorMask = selectorMask << 1;
				}

				List<Long> allAddresses = getAllAddresses(maskedAddress, floatingBits);

				for (long addressToWrite : allAddresses) {
					memory.put(addressToWrite, value);
				}
			}
		}

		return memory.values().stream().mapToLong(l -> l).sum();
	}

	private static List<Long> getAllAddresses(Long maskedAddress, List<Integer> floatingBits) {
		List<Long> addresses = new ArrayList<>();
		addresses.add(maskedAddress);

		for (int floatingBitIndex : floatingBits) {
			List<Long> newAddresses = new ArrayList<>();
			for (long address : addresses) {

				long selectorMask = 1l;
				selectorMask = selectorMask << floatingBitIndex;

				newAddresses.add(address);
				newAddresses.add(address ^ selectorMask);
			}

			addresses = newAddresses;
		}

		return addresses;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day14.txt";
		Pattern lineMatch = Pattern.compile("(?:mem)?\\[?(mask|\\d*)\\]?\\s=\\s([^\\s]*)");


		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<Matcher> instructions = Files.lines(path).map(lineMatch::matcher).filter(Matcher::matches).collect(Collectors.toList());

		Long sum = sumOfMemoryValues(instructions);
		System.out.println(sum);

		Long partTwoSum = sumOfMemoryValuesAddressDecoder(instructions);
		System.out.println(partTwoSum);
	}
}
