package org.riskfirst.same.same4j.atom;

import java.lang.reflect.Field;

/**
 * This narrows datom return types to talk specifically about fields.
 */
public interface FieldAtom extends TypedAtom<Field, Object> {

	public static FieldAtom of(Object owner, Field f, Object value) {
		
		return new FieldAtom() {
	
			@Override
			public Object getEntity() {
				return owner;
			}
	
			@Override
			public Field getProperty() {
				return f;
			}
	
			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public String toString() {
				return Atom.stringify(this);
			}
			
			
		};
	}

}