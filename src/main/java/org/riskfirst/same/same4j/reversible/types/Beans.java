package org.riskfirst.same.same4j.reversible.types;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.reversible.ReversibleConsumer;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

/**
 * Contains helper functions for dealing with Java Beans (i.e. getters and
 * setters)
 * 
 * @author robmoffat
 *
 */
public class Beans {

	public interface Prop<O, P> {

		public void set(O o, P p); 

		public P get(O o);
	
	}

	public static <A, B, R, T> ReversibleConsumer<A, B> of(Prop<A, R> propOnA, Prop<B, T> propOnB,
			ReversibleFunction<R, T> rf) {

		return new ReversibleConsumer<A, B>() {

			@Override
			public void accept(A t, B u) {
				if (t != null) {
					R in = propOnA.get(t);
					T out = rf.apply(in);
					propOnB.set(u, out);
				}
			}

			@Override
			public void inverse(B u, A t) {
				if (u != null) {
					T in = propOnB.get(u);
					R out = rf.inverse(in);
					propOnA.set(t, out);
				}
			}

		};

	}
	
	public static <O, P> Prop<O, P> prop(Function<O, P> getterOnO, BiConsumer<O, P> setterOnO) {

		return new Prop<O, P>() {

			@Override
			public void set(O o, P p) {
				setterOnO.accept(o, p);
			}

			@Override
			public P get(O o) {
				return getterOnO.apply(o);
			}

		};
	}

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
