package org.riskfirst.same.same4j.atom;

public interface StringAtom<V> extends TypedAtom<String, V> { 

	/**
	 * Helper function for passing around properties. 
	 */
	public static <V> StringAtom<V> of(Object entity, String property, V value) {
		return new StringAtom<V>() {
	
			@Override
			public Object getEntity() {
				return entity;
			}
	
			@Override
			public String getProperty() {
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
	public static <V> StringAtom<V> of(String property, V value) {
		return new StringAtom<V>() {
	
			@Override
			public Object getEntity() {
				return null;
			}
	
			@Override
			public String getProperty() {
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
