package de.foellix.jfx.graphs.style;

import de.foellix.jfx.graphs.Node;
import de.foellix.jfx.graphs.Options;

public class FoelliXSkin extends Options {
	private static final String DEFAULT_NODE_STYLE = "green";

	public FoelliXSkin() {
		super();
		addPaneStyle(new Style("paneFX"));
		addEdgeStyle(new Style("edgeFX"));
		addNodeStyle(new Style(DEFAULT_NODE_STYLE));
		addNodeStyle(new Style("red", null, new StyleCondition() {
			@Override
			public boolean fulfilled(Node node) {
				try {
					return Integer.valueOf(node.getValue()) % 3 == 0;
				} catch (final NumberFormatException e) {
					return node.getValue().contains("C");
				}
			}
		}));
		addNodeStyle(new Style("blue", null, new StyleCondition() {
			@Override
			public boolean fulfilled(Node node) {
				try {
					return Integer.valueOf(node.getValue()) % 7 == 0;
				} catch (final NumberFormatException e) {
					return node.getValue().contains("D");
				}
			}
		}));
	}
}