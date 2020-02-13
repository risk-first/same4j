package org.riskfirst.same.same4j.atom;

public interface IntAtom<V> extends TypedAtom<Integer, V> {

	/**
	 * Helper function for passing around properties. 
	 */
	public static <V> IntAtom<V> of(Object entity, int i, V value) {
		return new IntAtom<V>() {
	
			@Override
			public Object getEntity() {
				return entity;
			}
	
			@Override
			public Integer getProperty() {
				return i;
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
	public static <V> IntAtom<V> of(int i, V value) {
		return new IntAtom<V>() {
	
			@Override
			public Object getEntity() {
				return null;
			}
	
			@Override
			public Integer getProperty() {
				return i;
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
