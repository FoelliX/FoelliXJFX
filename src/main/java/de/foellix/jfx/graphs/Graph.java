package de.foellix.jfx.graphs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph {
	private Map<Integer, LinkedList<Node>> levelMap;
	protected Node root;

	public Graph() {
		this.levelMap = new HashMap<>();
		this.root = new Node(this, "Root", 0, null);
		this.levelMap.put(0, new LinkedList<>());
		this.levelMap.get(0).add(this.root);
	}

	public Node getRoot() {
		return this.root;
	}

	public int getDepth() {
		return this.levelMap.keySet().size();
	}

	public LinkedList<Node> getLevel(int level) {
		if (!this.levelMap.containsKey(level)) {
			this.levelMap.put(level, new LinkedList<>());
		}
		return this.levelMap.get(level);
	}

	public double getHeight(int level) {
		double maxHeight = 0d;
		for (final Node node : getLevel(level)) {
			if (node.getButton() != null) {
				if (node.getButton().getHeight() > maxHeight) {
					maxHeight = node.getButton().getHeight();
				}
			}
		}
		return maxHeight;
	}

	public void resetDrawComponents() {
		for (final Node node : this.root.getDescendants()) {
			node.setButton(null);
		}
	}

	public boolean allNodesReadyForRefinement() {
		for (final Node node : this.root.getDescendants()) {
			if (node.getButton() == null
					|| node.getButton().getHeight() == 0 && (node == this.root || node.getButton().getLayoutY() != 0)) {
				return false;
			}
		}
		return true;
	}

	public List<Node> getLeafs() {
		return getLeafs(this.root);
	}

	public List<Node> getLeafs(Node node) {
		final List<Node> leafs = new LinkedList<>();
		for (final Node candidate : node.getDescendants()) {
			if (candidate.getChildren() == null || candidate.getChildren().isEmpty()) {
				leafs.add(candidate);
			}
		}
		return leafs;
	}

	@Override
	public String toString() {
		return this.root.toString();
	}
}