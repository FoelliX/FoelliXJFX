package de.foellix.jfx.graphs.style;

import de.foellix.jfx.graphs.Node;

public class Style {
	private static final StyleCondition ALWAYS_TRUE_CONDITION = new StyleCondition() {
		@Override
		public boolean fulfilled(Node node) {
			return true;
		}
	};

	private String styleFulfilled;
	private String styleNotFulfilled;
	private StyleCondition condition;

	public Style(String style) {
		this(style, null, ALWAYS_TRUE_CONDITION);
	}

	public Style(String styleFulfilled, String styleNotFulfilled, StyleCondition condition) {
		super();
		this.styleFulfilled = styleFulfilled;
		this.styleNotFulfilled = styleNotFulfilled;
		this.condition = condition;
	}

	public StyleCondition getCondition() {
		return this.condition;
	}

	public void setCondition(StyleCondition condition) {
		this.condition = condition;
	}

	public String getStyleFulfilled() {
		return this.styleFulfilled;
	}

	public String getStyleNotFulfilled() {
		return this.styleNotFulfilled;
	}

	public void setStyleFulfilled(String styleFulfilled) {
		this.styleFulfilled = styleFulfilled;
	}

	public void setStyleNotFulfilled(String styleNotFulfilled) {
		this.styleNotFulfilled = styleNotFulfilled;
	}
}