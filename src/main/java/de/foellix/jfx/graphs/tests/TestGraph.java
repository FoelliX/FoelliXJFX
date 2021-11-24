package de.foellix.jfx.graphs.tests;

import de.foellix.jfx.graphs.ActionHandler;
import de.foellix.jfx.graphs.Graph;
import de.foellix.jfx.graphs.GraphDrawer;
import de.foellix.jfx.graphs.Node;
import javafx.event.Event;

public class TestGraph extends Graph {
	private static final String APPEND_STRING = "Appended by clicking parent!";
	private static final ActionHandler ACTION_LISTENER = new ActionHandler() {
		@Override
		public void handle(Event e, GraphDrawer graphDrawer, Node node) {
			final Node newNode = node.appendChild("F\n(" + ++counter + ")", APPEND_STRING);
			newNode.setOnClickListener(ACTION_LISTENER);
			System.out.println(node.info());
			graphDrawer.redraw();
		}
	};
	private static int counter = 0;

	public TestGraph() {
		super();
		this.root.setValue("A");
		final Node a = this.root.appendChild("B");
		final Node b = this.root.appendChild("C");
		a.appendChild("D\n(1)");
		a.appendChild("E\n(1)");
		b.appendChild("D\n(2)");
		b.appendChild("E\n(2)");

		final String object = "Same Object!";
		a.setObject(object);
		b.setObject(object);

		for (final Node n : this.root.getDescendants()) {
			n.setOnClickListener(ACTION_LISTENER);
		}
	}
}
