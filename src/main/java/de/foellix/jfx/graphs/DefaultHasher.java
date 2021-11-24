package de.foellix.jfx.graphs;

public class DefaultHasher implements Hasher {
	@Override
	public int hashCode(Object obj) {
		return obj.hashCode();
	}
}