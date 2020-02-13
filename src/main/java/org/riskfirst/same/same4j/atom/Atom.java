package org.riskfirst.same.same4j.atom;

/**
 * Utility classes for passing things through Reversible functions.
 * 
 * @author robmoffat
 *
 */
public interface Atom {
	
	public Object getEntity();
	public Object getProperty();
	public Object getValue();
	
	
	/**
	 * Helper function for passing around properties. 
	 */
	public static Atom of(Object entity, Object property, Object value) {
		return new Atom() {
	
			@Override
			public Object getEntity() {
				return entity;
			}
	
			@Override
			public Object getProperty() {
				return property;
			}
	
			@Override
			public Object getValue() {
				return value;
			}
		};
	}
	
	public static String stringify(Atom x) {
		return x.getClass().getSimpleName()+" ["+x.getEntity()+", "+x.getProperty()+", "+x.getValue()+"]";
	}
	
}