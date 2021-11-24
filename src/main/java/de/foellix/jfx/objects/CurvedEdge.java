package de.foellix.jfx.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

public class CurvedEdge extends Edge {
	public CurvedEdge(double nodeFromX, double nodeFromY, double nodeToX, double nodeToY) {
		this(nodeFromX, nodeFromY, nodeToX, nodeToY, true);
	}

	public CurvedEdge(double nodeFromX, double nodeFromY, double nodeToX, double nodeToY, boolean arrowHead) {
		final double distanceX = (nodeToX - nodeFromX) / 3d;
		final double distanceY = (nodeToX - nodeFromX) / 5d;

		final CubicCurve line = new CubicCurve(nodeFromX, nodeFromY, nodeFromX + distanceX, nodeFromY - distanceY,
				nodeToX - distanceX, nodeToY - distanceY, nodeToX, nodeToY);
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(1);
		line.setFill(null);

		if (arrowHead) {
			drawArrowHead(nodeFromX, nodeFromY, nodeToX, nodeToY);
		}

		this.getChildren().add(line);
	}
}