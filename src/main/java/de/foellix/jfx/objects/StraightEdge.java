package de.foellix.jfx.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class StraightEdge extends Edge {
	public StraightEdge(double nodeFromX, double nodeFromY, double nodeToX, double nodeToY) {
		this(nodeFromX, nodeFromY, nodeToX, nodeToY, true);
	}

	public StraightEdge(double nodeFromX, double nodeFromY, double nodeToX, double nodeToY, boolean arrowHead) {
		final Line line = new Line(nodeFromX, nodeFromY, nodeToX, nodeToY);
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(1);
		line.setFill(null);

		if (arrowHead) {
			drawArrowHead(nodeFromX, nodeFromY, nodeToX, nodeToY);
		}

		this.getChildren().add(line);
	}
}