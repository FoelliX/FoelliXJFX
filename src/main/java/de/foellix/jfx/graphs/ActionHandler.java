package de.foellix.jfx.graphs;

import javafx.event.Event;

public interface ActionHandler {
	public void handle(Event e, GraphDrawer graphDrawer, Node node);
}