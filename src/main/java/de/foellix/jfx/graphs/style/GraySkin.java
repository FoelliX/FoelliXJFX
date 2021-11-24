package de.foellix.jfx.graphs.style;

import de.foellix.jfx.graphs.Options;

public class GraySkin extends Options {
	public GraySkin() {
		super();
		addEdgeStyle(new Style("grayEdge"));
		addNodeStyle(new Style("grayNode"));
		setMinWidth(40d);
		setMinHeight(40d);
	}
}