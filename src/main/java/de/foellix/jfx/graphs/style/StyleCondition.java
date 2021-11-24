package de.foellix.jfx.graphs.style;

import de.foellix.jfx.graphs.Node;

public abstract class StyleCondition {
	public abstract boolean fulfilled(Node node);
}