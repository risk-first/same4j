package org.riskfirst.same.same4j.hierarchy.types;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.hierarchy.Prop;

/**
 * Contains helper functions for dealing with Java Beans (i.e. getters and
 * setters)
 * 
 * @author robmoffat
 *
 */
public class Beans {

	private static <O> Method findMethod(String pattern, Class<O> c, int args) {
		return Arrays.stream(c.getMethods())
				.filter(m -> m.getParameterCount() == args)
				.filter(m -> m.getName().matches(pattern))
				.findFirst()
				.orElseThrow(() -> new Same4JException("Couldn't find method on "+c+" matching "+pattern+" with "+args+" args"));
	}

	protected static String setterName(String name){
		return "^set"+Character.toUpperCase(name.charAt(0))+name.substring(1)+"$";
	}

	protected static String getterName(String name) {
		return "^(is|get)"+Character.toUpperCase(name.charAt(0))+name.substring(1)+"$";
	}

	/**
	 * Less efficient, uses reflection to figure out the
	 */
	public static <O, P> Prop<O, P> prop(Class<O> class1, String name) {

		Method setter = findMethod(setterName(name), class1, 1);
		Method getter = findMethod(getterName(name), class1, 0);
		
		return new Prop<O, P>() {
			

			@Override
			public void set(O o, P p) {
				try {
					setter.invoke(o, p);
				} catch (Exception e) {
					throw new Same4JException("Couldn't invoke javabean setter", e);
				} 
			}



			@SuppressWarnings("unchecked")
			@Override
			public P get(O o) {
				try {
					return (P) getter.invoke(o);
				} catch (Exception e) {
					throw new Same4JException("Couldn't invoke javabean getter", e);
				}
			}

			
		};
	}

}
