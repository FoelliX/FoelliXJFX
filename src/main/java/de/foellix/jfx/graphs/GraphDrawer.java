package de.foellix.jfx.graphs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.foellix.jfx.graphs.style.Style;
import de.foellix.jfx.graphs.tests.FuzzedTestGraph;
import de.foellix.jfx.objects.CurvedEdge;
import de.foellix.jfx.objects.StraightEdge;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class GraphDrawer {
	private static final Comparator<? super Node> NODE_COMPARATOR = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2) {
			if (n1.getParent().getX() > n2.getParent().getX()) {
				return 1;
			} else if (n1.getParent().getX() < n2.getParent().getX()) {
				return -1;
			} else {
				return 0;
			}
		}
	};

	private Graph g;
	private Pane p;
	private Options o;

	public GraphDrawer(Graph g, Pane p) {
		this(g, p, new Options());
	}

	public GraphDrawer(Graph g, Pane p, Options o) {
		this.g = g;
		this.p = p;
		this.o = o;
		drawGraph();
	}

	public Graph getGraph() {
		return this.g;
	}

	public Pane getPane() {
		return this.p;
	}

	public Options getOptions() {
		return this.o;
	}

	public void setGraph(Graph g) {
		this.g = g;
	}

	public void setPane(Pane p) {
		this.p = p;
	}

	public void setOptions(Options o) {
		this.o = o;
	}

	public void redraw() {
		this.g.resetDrawComponents();
		this.p.getChildren().clear();
		drawGraph();
	}

	private void drawGraph() {
		presortGraph();

		if (this.o.isClearDefaultStyles()) {
			this.p.getStyleClass().clear();
		}
		this.p.getStyleClass().add("pane");
		if (!this.o.getPaneStyles().isEmpty()) {
			for (final Style style : this.o.getPaneStyles()) {
				if (style.getCondition().fulfilled(null)) {
					this.p.getStyleClass().add(style.getStyleFulfilled());
				} else if (style.getStyleNotFulfilled() != null) {
					this.p.getStyleClass().add(style.getStyleNotFulfilled());
				}
			}
		}
		drawNode(this.g.getRoot());
		drawAllNodes();

		new Thread(() -> {
			while (!this.g.allNodesReadyForRefinement()) {
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					// do nothing
				}
			}

			Platform.runLater(() -> {
				drawRefined();
			});
		}).start();
	}

	private void drawRefined() {
		// Re-draw nodes
		drawAllNodes();

		// Refine positions
		int i = 0;
		while (fixOverlaps() || i < 2) {
			fixSingles();
			refine();
			i++;
		}
		finalTouch();

		// Draw Edges
		drawAllEdges();
	}

	private void refine() {
		for (int y = 0; y < this.g.getDepth(); y++) {
			for (final Node node : this.g.getLevel(y)) {
				if (node.getChildren().size() >= 2) {
					final double minX = node.getChildren().getFirst().getButton().getLayoutX();
					final double maxX = node.getChildren().getLast().getButton().getLayoutX()
							+ node.getChildren().getLast().getButton().getWidth();
					final double offset = minX + ((maxX - minX) / 2d) - (node.getButton().getWidth() / 2d);
					if (offset > node.getButton().getLayoutX()) {
						node.getButton().setLayoutX(offset);
					} else {
						final double offsetTurned = node.getButton().getLayoutX() - offset;
						for (final Node child : node.getChildren()) {
							child.getButton().setLayoutX(child.getButton().getLayoutX() + offsetTurned);
						}
					}
				}
			}
		}
	}

	private void presortGraph() {
		for (int y = 1; y < this.g.getDepth(); y++) {
			this.g.getLevel(y).sort(NODE_COMPARATOR);
		}
	}

	private void drawAllNodes() {
		for (int y = 0; y < this.g.getDepth(); y++) {
			for (final Node node : this.g.getLevel(y)) {
				drawNode(node);
			}
		}
	}

	private void drawNode(Node node) {
		// X
		double x;
		if (node.getParent() != null) {
			if (node.getLeft() != null) {
				x = node.getLeft().getButton().getLayoutX() + node.getLeft().getButton().getLayoutBounds().getWidth()
						+ this.o.getStepX();
				if (node.getParent().getButton().getLayoutX() > x) {
					x = node.getParent().getButton().getLayoutX();
				}
			} else {
				x = node.getParent().getButton().getLayoutX();
			}
		} else {
			x = 0;
		}

		// Y
		double y = 0d;
		for (int i = 0; i < node.getY(); i++) {
			y += this.g.getHeight(i);
			y += this.o.getStepY();
		}

		// Button
		final Button btn;
		if (node.getButton() == null) {
			btn = new Button(node.getValue());
			if (this.o.getFixedWidth() >= 0d) {
				btn.setPrefWidth(this.o.getFixedWidth());
			} else if (this.g instanceof FuzzedTestGraph) {
				btn.setPrefWidth(((FuzzedTestGraph) this.g).getRandom().nextInt(50) + this.o.getStepX());
			}
			if (this.o.getMinWidth() >= 0d) {
				btn.setMinWidth(this.o.getMinWidth());
			}
			if (this.o.getMaxWidth() >= 0d) {
				btn.setMaxWidth(this.o.getMaxWidth());
			}
			if (this.o.getFixedHeight() >= 0d) {
				btn.setPrefHeight(this.o.getFixedHeight());
			} else if (this.g instanceof FuzzedTestGraph) {
				btn.setPrefHeight(((FuzzedTestGraph) this.g).getRandom().nextInt(25) + 25d);
			}
			if (this.o.getMinHeight() >= 0d) {
				btn.setMinHeight(this.o.getMinHeight());
			}
			if (this.o.getMaxHeight() >= 0d) {
				btn.setMaxHeight(this.o.getMaxHeight());
			}
			btn.setOnAction((e) -> {
				if (node.getOnClickListener() != null) {
					node.getOnClickListener().handle(e, this, node);
				}
			});
			btn.setOnMouseEntered((e) -> {
				if (node.getOnHoverListener() != null) {
					node.getOnHoverListener().handle(e, this, node);
				}
			});
			if (this.o.isClearDefaultStyles()) {
				btn.getStyleClass().clear();
			}
			btn.getStyleClass().add("node");
			if (!this.o.getNodeStyles().isEmpty()) {
				for (final Style style : this.o.getNodeStyles()) {
					if (style.getCondition().fulfilled(node)) {
						btn.getStyleClass().add(style.getStyleFulfilled());
					} else if (style.getStyleNotFulfilled() != null) {
						btn.getStyleClass().add(style.getStyleNotFulfilled());
					}
				}
			}
			node.setButton(btn);
			this.p.getChildren().add(btn);
		} else {
			btn = node.getButton();
		}
		if (this.o.getFixedHeight() < 0d && !this.o.isVaryingHeightPerLevelEnabled()
				&& this.g.getHeight(node.getY()) > btn.getHeight()) {
			btn.setPrefHeight(this.g.getHeight(node.getY()));
		}
		btn.setLayoutX(x);
		btn.setLayoutY(y);
	}

	private boolean fixOverlaps() {
		boolean fixed = false;
		for (int y = 0; y < this.g.getDepth(); y++) {
			for (final Node node : this.g.getLevel(y)) {
				if (node.getLeft() != null) {
					if (node.getLeft().getButton().getLayoutX() + node.getLeft().getButton().getWidth()
							+ this.o.getStepX() > node.getButton().getLayoutX()) {
						node.getButton().setLayoutX(node.getLeft().getButton().getLayoutX()
								+ node.getLeft().getButton().getWidth() + this.o.getStepX());
						fixed = true;
					}
				}
			}
		}

		return fixed;
	}

	private void fixSingles() {
		for (int y = 0; y < this.g.getDepth(); y++) {
			for (final Node node : this.g.getLevel(y)) {
				if (node.getParent() != null && node.getParent().getChildren().size() == 1) {
					if (node.getParent().getButton().getLayoutX() > node.getButton().getLayoutX()) {
						node.getButton().setLayoutX(node.getParent().getButton().getLayoutX());
					} else if (node.getParent().getButton().getLayoutX() < node.getButton().getLayoutX()) {
						node.getParent().getButton().setLayoutX(node.getButton().getLayoutX());
					}
				}
			}
		}
	}

	private void finalTouch() {
		// Move parent with only one child above child if possible
		boolean changed = true;
		while (changed) {
			changed = false;
			for (final Node node : this.g.getRoot().getDescendants()) {
				final double nodeCenter = node.getButton().getLayoutX() + node.getButton().getWidth() / 2d;
				if (node.getChildren().size() == 1) {
					final Node child = node.getChildren().getFirst();
					final double childCenter = child.getButton().getLayoutX() + child.getButton().getWidth() / 2d;
					if (nodeCenter != childCenter) {
						final double nodeNewPotentialX = childCenter - (node.getButton().getWidth() / 2d);
						if (nodeNewPotentialX >= 0) {
							if (node.getRight() != null) {
								final double nodeMaxX = nodeNewPotentialX + node.getButton().getWidth()
										+ this.o.getStepX();
								final double rightX = node.getRight().getButton().getLayoutX();
								if (nodeMaxX < rightX) {
									node.getButton().setLayoutX(nodeNewPotentialX);
									changed = true;
								}
							} else {
								node.getButton().setLayoutX(nodeNewPotentialX);
								changed = true;
							}
						}
					}
				}
			}
		}

		// Move left if possible
		for (int i = 1; i < this.g.getLeafs().size(); i++) {
			final Node node = this.g.getLeafs().get(i);
			Node current = node;
			Node nodeToMove = null;
			do {
				final double xLeft = current.getButton().getLayoutX();
				if (current.getLeft() != null) {
					final double xRight = current.getLeft().getButton().getLayoutX()
							+ current.getLeft().getButton().getWidth();
					if (xRight >= xLeft - this.o.getStepX()) {
						nodeToMove = null;
						break;
					} else if (current.getParent().getChildren().contains(current.getLeft())) {
						nodeToMove = current;
						break;
					}
				} else if (xLeft < this.o.getStepX()) {
					break;
				}
				nodeToMove = current;
				current = current.getParent();
			} while (current != this.g.getRoot());

			if (nodeToMove != null) {
				double offset = this.o.getStepX();
				for (final Node move : nodeToMove.getDescendants()) {
					double smaller;
					if (move.getLeft() != null) {
						smaller = move.getLeft().getButton().getLayoutX() + move.getLeft().getButton().getWidth();
					} else {
						smaller = 0;
					}
					final double bigger = move.getButton().getLayoutX();
					if ((bigger - smaller) < 2 * this.o.getStepX() && (bigger - smaller) > this.o.getStepX()) {
						final double newOffset = bigger - (smaller + this.o.getStepX());
						if (newOffset < offset) {
							offset = newOffset;
						}
					}
				}
				for (final Node move : nodeToMove.getDescendants()) {
					move.getButton().setLayoutX(move.getButton().getLayoutX() - offset);
				}
				i--;
			}
		}

		// Refine
		boolean move = true;
		while (move) {
			move = false;
			for (int y = 0; y < this.g.getDepth(); y++) {
				for (final Node node : this.g.getLevel(y)) {
					if (node.getChildren().size() >= 2) {
						final double minX = node.getChildren().getFirst().getButton().getLayoutX();
						final double maxX = node.getChildren().getLast().getButton().getLayoutX()
								+ node.getChildren().getLast().getButton().getWidth();
						final double offset = minX + ((maxX - minX) / 2d) - (node.getButton().getWidth() / 2d);
						double leftMin = 0;
						if (node.getLeft() != null) {
							leftMin = node.getLeft().getButton().getLayoutX() + node.getLeft().getButton().getWidth()
									+ this.o.getStepX();
						}
						if (offset != node.getButton().getLayoutX() && offset >= leftMin) {
							move = true;
							node.getButton().setLayoutX(offset);
						}
					}
				}
			}
		}

		// Align awkward nodes
		for (final Node node : this.g.getRoot().getDescendants()) {
			final double nodeCenter = node.getButton().getLayoutX() + node.getButton().getWidth() / 2d;
			if (node.getChildren().size() == 1) {
				final Node child = node.getChildren().getFirst();
				final double childCenter = child.getButton().getLayoutX() + child.getButton().getWidth() / 2d;
				if (nodeCenter != childCenter) {
					if (child.getChildren().isEmpty() && node.getButton().getWidth() > child.getButton().getWidth()) {
						final double fix = nodeCenter - childCenter;
						if (child.getRight() == null || child.getButton().getLayoutX()
								+ fix < child.getRight().getButton().getLayoutX() - this.o.getStepX()) {
							child.getButton().setLayoutX(child.getButton().getLayoutX() + fix);
						}
					}
					if (node.getLeft() == null && node.getRight() == null
							&& node.getButton().getWidth() < child.getButton().getWidth()) {
						final double fix = nodeCenter - childCenter;
						node.getButton().setLayoutX(node.getButton().getLayoutX() - fix);
					}
				}
			}
		}

		// Apply left and top padding
		if (this.o.getPaddingLeft() != 0 || this.o.getPaddingTop() != 0) {
			for (final Node node : this.g.getRoot().getDescendants()) {
				node.getButton().setLayoutX(node.getButton().getLayoutX() + this.o.getPaddingLeft());
				node.getButton().setLayoutY(node.getButton().getLayoutY() + this.o.getPaddingTop());
			}
		}
	}

	private void drawAllEdges() {
		for (int y = 0; y < this.g.getDepth(); y++) {
			for (final Node node : this.g.getLevel(y)) {
				drawEdges(node);
			}
		}
		if (this.o.isSameObjectEdgesEnabled()) {
			drawAllSameObjectEdges();
		}
	}

	private void drawEdges(Node node) {
		if (node.getParent() != null) {
			final double parX = node.getParent().getButton().getLayoutX()
					+ (node.getParent().getButton().getWidth() / 2d);
			final double parY = node.getParent().getButton().getLayoutY() + node.getParent().getButton().getHeight();

			final double thisX = node.getButton().getLayoutX() + (node.getButton().getWidth() / 2d);
			final double thisY = node.getButton().getLayoutY();

			final StraightEdge edge = new StraightEdge(parX, parY, thisX, thisY);
			if (this.o.isClearDefaultStyles()) {
				edge.clearStyleClass();
			}
			edge.addStyleClass("edge");
			if (!this.o.getEdgeStyles().isEmpty()) {
				for (final Style style : this.o.getEdgeStyles()) {
					if (style.getCondition().fulfilled(node)) {
						edge.addStyleClass(style.getStyleFulfilled());
					} else if (style.getStyleNotFulfilled() != null) {
						edge.addStyleClass(style.getStyleNotFulfilled());
					}
				}
			}

			this.p.getChildren().add(edge);
			edge.toBack();
		}
	}

	private void drawAllSameObjectEdges() {
		final Map<Integer, Set<Node>> map = new HashMap<>();
		for (final Node node : this.g.getRoot().getDescendants()) {
			if (node.getObject() != null) {
				final int hash = this.o.getHasher().hashCode(node.getObject());
				if (!map.containsKey(hash)) {
					map.put(hash, new HashSet<>());
				}
				map.get(hash).add(node);
			}
		}

		final Set<Node> nodesHandled = new HashSet<>();
		for (final Node node : this.g.getRoot().getDescendants()) {
			if (node.getObject() != null) {
				final int hash = this.o.getHasher().hashCode(node.getObject());
				if (map.containsKey(hash)) {
					for (final Node sameObjNode : map.get(hash)) {
						if (sameObjNode == node || nodesHandled.contains(sameObjNode)) {
							continue;
						}
						nodesHandled.add(node);
						drawSameObjectEdge(node, sameObjNode);
					}
				}
			}
		}
	}

	private void drawSameObjectEdge(Node node, Node sameObjNode) {
		final double nodeX = node.getButton().getLayoutX() + (node.getButton().getWidth() / 2d) + 2d;
		final double nodeY = node.getButton().getLayoutY() + (node.getButton().getHeight() / 2d) + 2d;

		final double sameObjNodeX = sameObjNode.getButton().getLayoutX() + (sameObjNode.getButton().getWidth() / 2d)
				+ 2d;
		final double sameObjNodeY = sameObjNode.getButton().getLayoutY() + (sameObjNode.getButton().getHeight() / 2d)
				+ 2d;

		final CurvedEdge edge = new CurvedEdge(nodeX, nodeY, sameObjNodeX, sameObjNodeY, false);
		if (this.o.isClearDefaultStyles()) {
			edge.clearStyleClass();
		}
		edge.addStyleClass("edge");
		edge.addStyleClass("edgeSameObject");
		if (!this.o.getEdgeStyles().isEmpty()) {
			for (final Style style : this.o.getEdgeStyles()) {
				if (style.getCondition().fulfilled(node)) {
					edge.addStyleClass(style.getStyleFulfilled());
				} else if (style.getStyleNotFulfilled() != null) {
					edge.addStyleClass(style.getStyleNotFulfilled());
				}
			}
		}

		this.p.getChildren().add(edge);
		edge.toBack();
	}
}