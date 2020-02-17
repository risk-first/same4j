package org.riskfirst.same.same4j.builder.bytebuddy;

import java.lang.reflect.Method;

import org.riskfirst.same.same4j.hierarchy.Prop;
import org.riskfirst.same.same4j.hierarchy.types.Beans;

class PropConstructor {
	
	private String name;
	private Class<?> returnType;
	private Class<?> targetType;
	
	public void append(Method m, Object[] args) {
		this.name = m.getName().substring(3);
		this.returnType = m.getReturnType();
		this.targetType = m.getDeclaringClass();
		
	}
	
	public Prop<?,?> getProp() {
		return Beans.prop(targetType, name);
	}

	public Class<?> getType() {
		return returnType;
	}
	
}