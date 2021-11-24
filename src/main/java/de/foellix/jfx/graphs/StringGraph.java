package de.foellix.jfx.graphs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringGraph extends Graph {
	private static final CharSequence PLACEHOLDER = "#JFX-PLACEHOLDER#";
	private static final char NODE = 'N';
	private static final char RIGHT = ',';
	private static final char CHILD = '{';
	private static final char PARENT = '}';
	private String graphString;

	/**
	 * Create a graph on the basis of the given string. The nodes can have values provided in enclosing "'" (If "'" is wanted inside it must be escaped with "\"). Example: 'A' { 'B' { 'D1', 'E1' }, 'C' { 'D2', 'E2' } }
	 *
	 * @param graphString
	 *            the string used as basis.
	 */
	public StringGraph(String graphString) {
		super();
		this.graphString = graphString;
		parse();
	}

	private void parse() {
		Node currentNode = this.getRoot();

		// Step 1: Get node values
		final Queue<String> nodeValues = getNodeValues();

		// Step 2: Sanitize string
		sanitize();

		// Step 3: Create graph
		for (final char c : this.graphString.toCharArray()) {
			if (c == NODE) {
				currentNode.setValue(nodeValues.poll());
			} else if (c == RIGHT) {
				currentNode = currentNode.getParent().appendChild(NODE);
			} else if (c == CHILD) {
				currentNode = currentNode.appendChild(NODE);
			} else if (c == PARENT) {
				currentNode = currentNode.getParent();
			}
		}
	}

	private LinkedList<String> getNodeValues() {
		final LinkedList<String> values = new LinkedList<>();
		this.graphString = this.graphString.replace("\\'", PLACEHOLDER);
		final Pattern pattern = Pattern.compile("'([^'])+'");
		final Matcher matcher = pattern.matcher(this.graphString);
		while (matcher.find()) {
			final String value = matcher.group();
			this.graphString = this.graphString.replace(value, String.valueOf(NODE));
			values.add(value.substring(1, value.length() - 1).replace(PLACEHOLDER, "'"));
		}

		return values;
	}

	private void sanitize() {
		this.graphString = this.graphString.replace("\s", "");
	}
}