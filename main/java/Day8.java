import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {
	final private static Pattern instructionPattern = Pattern.compile("(nop|acc|jmp)\\s([+-]\\d+)");

	private enum Operation {
		NOP("nop"),
		ACC("acc"),
		JMP("jmp");

		private final String name;

		Operation(String name) {
			this.name = name;
		}

		public static Operation getOperation(String opCode) {
			return Arrays.stream(values()).filter(i -> i.name.equals(opCode)).findFirst().get();
		}
	}

	private static int getLastAccValue(List<SimpleEntry<Operation, Integer>> instructions) {
		return getLastAccValue(instructions, 0);
	}

	private static int getLastAccValue(List<SimpleEntry<Operation, Integer>> instructions, int pc) {
		int acc = 0;

		Set<Integer> pcHistory = new HashSet<>();

		SimpleEntry<Integer, Integer> state = executeNext(instructions, pc, acc, pcHistory);

		return state.getValue();
	}

	private static int getLastAccValuePartTwo(List<SimpleEntry<Operation, Integer>> instructions, int pc) {
		int acc = 0;

		Set<Integer> pcHistory = new HashSet<>();

		SimpleEntry<Integer, Integer> state = executeNextPartTwo(instructions, pc, acc, pcHistory, false);

		return state.getValue();
	}

	private static SimpleEntry<Integer, Integer> executeNext(List<SimpleEntry<Operation, Integer>> instructions, int pc, int acc, Set<Integer> pcHistory) {
		if (pcHistory.contains(pc)) {
			return new SimpleEntry<>(pc, acc);
		}

		pcHistory = new HashSet<>(pcHistory);
		pcHistory.add(pc);

		SimpleEntry<Operation, Integer> instruction = instructions.get(pc);
		switch (instruction.getKey()) {
			case NOP:
				pc++;
				break;

			case ACC:
				acc += instruction.getValue();
				pc++;
				break;

			case JMP:
				pc += instruction.getValue();
				break;
		}

		return executeNext(instructions, pc, acc, pcHistory);
	}

	private static SimpleEntry<Integer, Integer> executeNextPartTwo(List<SimpleEntry<Operation, Integer>> instructions, int pc, int acc, Set<Integer> pcHistory, boolean changed) {
		if (pc == instructions.size()) {
			return new SimpleEntry<>(pc, acc);
		}

		if (pcHistory.contains(pc)) {
			return null;
		}

		pcHistory = new HashSet<>(pcHistory);
		pcHistory.add(pc);

		SimpleEntry<Integer, Integer> stateLeft;
		SimpleEntry<Integer, Integer> stateRight;

		SimpleEntry<Operation, Integer> instruction = instructions.get(pc);
		switch (instruction.getKey()) {
			case ACC:
				return executeNextPartTwo(instructions, pc + 1, acc + instruction.getValue(), pcHistory, changed);

			case NOP:
				stateLeft = executeNextPartTwo(instructions, pc + 1, acc, pcHistory, changed);
				if (!changed) {
					stateRight = executeNextPartTwo(instructions, pc + instruction.getValue(), acc, pcHistory, true);
				} else {
					stateRight = null;
				}

				if (stateLeft != null) {
					return stateLeft;
				} else if (stateRight != null) {
					return stateRight;
				} else {
					return null;
				}
			case JMP:
				stateLeft = executeNextPartTwo(instructions, pc + instruction.getValue(), acc, pcHistory, changed);
				if (!changed) {
					stateRight = executeNextPartTwo(instructions, pc + 1, acc, pcHistory, true);
				} else {
					stateRight = null;
				}

				if (stateLeft != null) {
					return stateLeft;
				} else if (stateRight != null) {
					return stateRight;
				} else {
					return null;
				}
		}

		return null;
	}

	private static int getFixedLastAccValue(List<SimpleEntry<Operation, Integer>> instructions) {
		int pc = 0;
		int acc = 0;

		return acc;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String inputFileName = "day8.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Path path = Paths.get(classLoader.getResource(inputFileName).toURI());

		List<SimpleEntry<Operation, Integer>> instructions = Files.lines(path)
				.map(instructionPattern::matcher)
				.filter(Matcher::matches)
				.map(m -> new SimpleEntry<>(Operation.getOperation(m.group(1)), Integer.parseInt(m.group(2))))
				.collect(Collectors.toList());

		test();

		System.out.println(getLastAccValue(instructions));

		System.out.println(getLastAccValuePartTwo(instructions, 0));
	}

	private static void test() {
		String sampleInput = "nop +0\n" +
				"acc +1\n" +
				"jmp +4\n" +
				"acc +3\n" +
				"jmp -3\n" +
				"acc -99\n" +
				"acc +1\n" +
				"jmp -4\n" +
				"acc +6";

		List<SimpleEntry<Operation, Integer>> instructions = Arrays.stream(sampleInput.split("\n"))
				.map(instructionPattern::matcher)
				.filter(Matcher::matches)
				.map(m -> new SimpleEntry<>(Operation.getOperation(m.group(1)), Integer.parseInt(m.group(2))))
				.collect(Collectors.toList());

		assert getLastAccValue(instructions) == 5;
		assert getLastAccValuePartTwo(instructions, 0) == 8;
	}
}
