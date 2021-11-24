package de.foellix.jfx.graphs;

import java.util.ArrayList;
import java.util.List;

import de.foellix.jfx.graphs.style.Style;

public class Options {
	private double stepX;
	private double stepY;
	private double fixedWidth;
	private double fixedHeight;
	private double minWidth;
	private double minHeight;
	private double maxWidth;
	private double maxHeight;
	private double paddingLeft;
	private double paddingTop;
	private boolean varyingHeightPerLevelEnabled;
	private boolean sameObjectEdgesEnabled;
	private List<Style> nodeStyles;
	private List<Style> edgeStyles;
	private List<Style> paneStyles;
	private boolean clearDefaultStyles;
	private Hasher hasher;

	public Options() {
		this.stepX = 25d;
		this.stepY = 40d;
		this.fixedWidth = -1d;
		this.fixedHeight = -1d;
		this.minWidth = -1d;
		this.minHeight = -1d;
		this.maxWidth = -1d;
		this.maxHeight = -1d;
		this.paddingLeft = 0d;
		this.paddingTop = 0d;
		this.varyingHeightPerLevelEnabled = false;
		this.sameObjectEdgesEnabled = false;
		this.nodeStyles = new ArrayList<>();
		this.edgeStyles = new ArrayList<>();
		this.paneStyles = new ArrayList<>();
		this.clearDefaultStyles = false;
		this.hasher = new DefaultHasher();
	}

	public double getStepX() {
		return this.stepX;
	}

	public double getStepY() {
		return this.stepY;
	}

	public double getFixedWidth() {
		return this.fixedWidth;
	}

	public double getFixedHeight() {
		return this.fixedHeight;
	}

	public double getMinWidth() {
		return this.minWidth;
	}

	public double getMinHeight() {
		return this.minHeight;
	}

	public double getMaxWidth() {
		return this.maxWidth;
	}

	public double getMaxHeight() {
		return this.maxHeight;
	}

	public double getPaddingLeft() {
		return this.paddingLeft;
	}

	public double getPaddingTop() {
		return this.paddingTop;
	}

	public boolean isVaryingHeightPerLevelEnabled() {
		return this.varyingHeightPerLevelEnabled;
	}

	public boolean isSameObjectEdgesEnabled() {
		return this.sameObjectEdgesEnabled;
	}

	public Hasher getHasher() {
		return this.hasher;
	}

	public Options setStepX(double stepX) {
		this.stepX = stepX;
		return this;
	}

	public Options setStepY(double stepY) {
		this.stepY = stepY;
		return this;
	}

	public Options setFixedWidth(double fixedWidth) {
		this.fixedWidth = fixedWidth;
		return this;
	}

	public Options setFixedHeight(double fixedHeight) {
		this.fixedHeight = fixedHeight;
		return this;
	}

	public Options setMinWidth(double minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	public Options setMinHeight(double minHeight) {
		this.minHeight = minHeight;
		return this;
	}

	public Options setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
		return this;
	}

	public Options setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
		return this;
	}

	public Options setPaddingLeft(double left) {
		this.paddingLeft = left;
		return this;
	}

	public Options setPaddingTop(double top) {
		this.paddingTop = top;
		return this;
	}

	public Options setPadding(double padding) {
		this.paddingLeft = padding;
		this.paddingTop = padding;
		return this;
	}

	public Options setPadding(double left, double top) {
		this.paddingLeft = left;
		this.paddingTop = top;
		return this;
	}

	public Options setVaryingHeightPerLevelEnabled(boolean varyingHeightPerLevelEnabled) {
		this.varyingHeightPerLevelEnabled = varyingHeightPerLevelEnabled;
		return this;
	}

	public Options setSameObjectEdgesEnabled(boolean sameObjectEdgesEnabled) {
		this.sameObjectEdgesEnabled = sameObjectEdgesEnabled;
		return this;
	}

	public Options setHasher(Hasher hasher) {
		this.hasher = hasher;
		return this;
	}

	/*
	 * Style
	 */
	public List<Style> getNodeStyles() {
		return this.nodeStyles;
	}

	public List<Style> getEdgeStyles() {
		return this.edgeStyles;
	}

	public List<Style> getPaneStyles() {
		return this.paneStyles;
	}

	public Options addNodeStyle(Style nodeStyle) {
		this.nodeStyles.add(nodeStyle);
		return this;
	}

	public Options addEdgeStyle(Style edgeStyle) {
		this.edgeStyles.add(edgeStyle);
		return this;
	}

	public Options addPaneStyle(Style paneStyle) {
		this.paneStyles.add(paneStyle);
		return this;
	}

	public boolean isClearDefaultStyles() {
		return this.clearDefaultStyles;
	}

	public Options setClearDefaultStyles(boolean clearDefaultStyles) {
		this.clearDefaultStyles = clearDefaultStyles;
		return this;
	}
}