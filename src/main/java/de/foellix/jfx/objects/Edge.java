package de.foellix.jfx.objects;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public abstract class Edge extends Group {
	public void clearStyleClass() {
		for (final Node child : this.getChildren()) {
			child.getStyleClass().clear();
		}
	}

	public void addStyleClass(String style) {
		for (final Node child : this.getChildren()) {
			child.getStyleClass().add(style);
		}
	}

	protected void drawArrowHead(double nodeFromX, double nodeFromY, double nodeToX, double nodeToY) {
		final double lineAngle = Math.atan((nodeFromY - nodeToY) / (nodeFromX - nodeToX));
		final double arrowAngle = nodeFromX >= nodeToX ? Math.toRadians(25) : -Math.toRadians(205);
		final double arrowHeadLength = 8d;

		Line arrowHeadLine = new Line();
		arrowHeadLine.setStartX(nodeToX);
		arrowHeadLine.setStartY(nodeToY);
		arrowHeadLine.setEndX(nodeToX + arrowHeadLength * Math.cos(lineAngle - arrowAngle));
		arrowHeadLine.setEndY(nodeToY + arrowHeadLength * Math.sin(lineAngle - arrowAngle));
		this.getChildren().add(arrowHeadLine);

		arrowHeadLine = new Line();
		arrowHeadLine.setStartX(nodeToX);
		arrowHeadLine.setStartY(nodeToY);
		arrowHeadLine.setEndX(nodeToX + arrowHeadLength * Math.cos(lineAngle + arrowAngle));
		arrowHeadLine.setEndY(nodeToY + arrowHeadLength * Math.sin(lineAngle + arrowAngle));
		this.getChildren().add(arrowHeadLine);
	}
}