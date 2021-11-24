package de.foellix.jfx.graphs.tests;

import java.util.LinkedList;
import java.util.Random;

import de.foellix.jfx.graphs.ActionHandler;
import de.foellix.jfx.graphs.Graph;
import de.foellix.jfx.graphs.GraphDrawer;
import de.foellix.jfx.graphs.Node;
import javafx.event.Event;

public class FuzzedTestGraph extends Graph {
	private static final ActionHandler ACTION_LISTENER = new ActionHandler() {
		@Override
		public void handle(Event e, GraphDrawer graphDrawer, Node node) {
			System.out.println(node.info());
		}
	};

	private final Random random;

	public FuzzedTestGraph(int size) {
		super();
		this.random = new Random();
		final String sameObject = "Same Object!";
		this.root.appendChild("0");
		for (int i = 1; i <= size; i++) {
			final LinkedList<Node> allNodes = this.root.getDescendants();
			final int index = this.random.nextInt(allNodes.size());
			final Node child = allNodes.get(index).appendChild(String.valueOf(i));
			if (this.random.nextFloat() < 0.05f) {
				child.setObject(sameObject);
			}
		}

		for (final Node n : this.root.getDescendants()) {
			n.setOnClickListener(ACTION_LISTENER);
		}
	}

	public Random getRandom() {
		return this.random;
	}
}
