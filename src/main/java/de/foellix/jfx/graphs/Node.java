package de.foellix.jfx.graphs;

import java.util.LinkedList;

import javafx.scene.control.Button;

public class Node {
	private Graph g;
	private String value;
	private int level;
	private Node parent;
	private LinkedList<Node> children;
	private Button button;
	private Object object;
	private ActionHandler onClickListener;
	private ActionHandler onHoverListener;

	public Node(Graph g, String value, int level, Node parent) {
		this.g = g;
		this.value = value;
		this.level = level;
		this.parent = parent;
		this.children = new LinkedList<>();
		this.onClickListener = null;
		this.onHoverListener = null;
	}

	public Node appendChild(char label) {
		return appendChild(String.valueOf(label));
	}

	public Node appendChild(char label, Object object) {
		return appendChild(String.valueOf(label), object);
	}

	public Node appendChild(String label) {
		return appendChild(label, null);
	}

	public Node appendChild(String label, Object object) {
		final Node child = new Node(this.g, label, this.level + 1, this);
		this.children.add(child);
		this.g.getLevel(this.level + 1).add(child);
		child.setObject(object);
		return child;
	}

	public Node getParent() {
		return this.parent;
	}

	public LinkedList<Node> getChildren() {
		return this.children;
	}

	public LinkedList<Node> getDescendants() {
		final LinkedList<Node> descendants = new LinkedList<>();
		descendants.add(this);
		for (final Node child : this.children) {
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}

	public Node getLeft() {
		final int index = this.g.getLevel(this.level).indexOf(this);
		if (index > 0) {
			return this.g.getLevel(this.level).get(index - 1);
		}
		return null;
	}

	public Node getRight() {
		final int index = this.g.getLevel(this.level).indexOf(this);
		if (index < this.g.getLevel(this.level).size() - 1) {
			return this.g.getLevel(this.level).get(index + 1);
		}
		return null;
	}

	public int getX() {
		return this.g.getLevel(this.level).indexOf(this);
	}

	public int getY() {
		return this.level;
	}

	public String getValue() {
		return this.value;
	}

	public Button getButton() {
		return this.button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String info() {
		final StringBuilder sb = new StringBuilder();
		if (!this.children.isEmpty()) {
			for (final Node child : this.children) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(child.getValue());
			}
		} else {
			sb.append("-");
		}
		return (this.value + " = { Children: " + sb.toString() + ", Left: "
				+ (getLeft() != null ? getLeft().getValue() : "-") + ", Right: "
				+ (getRight() != null ? getRight().getValue() : "-") + ", Level: " + this.level + ", Object: "
				+ this.object + " }").replace("\n", "");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}

	public void toString(StringBuilder sb) {
		sb.append(
				"'" + this.getValue().replace("'", "\\'").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t")
						+ "'");
		if (!this.getChildren().isEmpty()) {
			sb.append(" { ");
			for (final Node child : this.getChildren()) {
				child.toString(sb);
			}
			sb.append(" }");
		}
		if (this.getRight() != null && this.getParent().getChildren().contains(this.getRight())) {
			sb.append(", ");
		}
	}

	/*
	 * Object
	 */
	public Object getObject() {
		return this.object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	/*
	 * Listener
	 */
	public ActionHandler getOnClickListener() {
		return this.onClickListener;
	}

	public void setOnClickListener(ActionHandler listener) {
		this.onClickListener = listener;
	}

	public ActionHandler getOnHoverListener() {
		return this.onHoverListener;
	}

	public void setOnHoverListener(ActionHandler listener) {
		this.onHoverListener = listener;
	}
}