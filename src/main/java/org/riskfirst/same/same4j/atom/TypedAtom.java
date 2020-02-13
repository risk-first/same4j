package org.riskfirst.same.same4j.atom;

public interface TypedAtom<K, V> extends Atom {

	public Object getEntity();
	public K getProperty();
	public V getValue();
	
	/**
	 * Helper function for passing around properties. 
	 */
	public static <K, V> TypedAtom<K, V> of(Object entity, K property, V value) {
		return new TypedAtom<K, V>() {
	
			@Override
			public Object getEntity() {
				return entity;
			}
	
			@Override
			public K getProperty() {
				return property;
			}
	
			@Override
			public V getValue() {
				return value;
			}
			
			@Override
			public String toString() {
				return Atom.stringify(this);
			}

		};
	}
	
	/**
	 * Helper function for passing around properties. 
	 */
	public static <K, V> TypedAtom<K, V> of(K property, V value) {
		return new TypedAtom<K, V>() {
	
			@Override
			public Object getEntity() {
				return null;
			}
	
			@Override
			public K getProperty() {
				return property;
			}
	
			@Override
			public V getValue() {
				return value;
			}
			
			@Override
			public String toString() {
				return Atom.stringify(this);
			}

		};
	}
}
