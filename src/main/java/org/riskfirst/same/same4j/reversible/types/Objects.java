package org.riskfirst.same.same4j.reversible.types;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.Same4JException;
import org.riskfirst.same.same4j.atom.FieldAtom;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

/**
 * Provides functionality to build/deconstruct java objects.
 */
public class Objects {
	
	/**
	 * Identity relationship, where the in matches the out.
	 */
	public static <A> ReversibleFunction<A, A> identity() {
		return Reversible.function(
			a -> a,
			b -> b);
	}

	/**
	 * Returns an object if there is one present, otherwise returns null.
	 */
	public static <A, B> ReversibleFunction<A, B> existence(
			Supplier<A> ca, 
			Supplier<B> cb) {
		
		return Reversible.function(
			a -> a != null ? cb.get() : null,
			b -> b != null ? ca.get() : null);
	}
	
	/**
	 * Tries to create a copy of the object using no-args constructors
	 */
	public static <A, B> ReversibleFunction<A, B> shallow(Class<?> a, Class<?> b) {
		return existence(
				() -> newInstanceNoArgs(a), 
				() -> newInstanceNoArgs(b));
	}

	@SuppressWarnings("unchecked")
	protected static <A> A newInstanceNoArgs(Class<?> a) {
		try {
			return (A)  a.getConstructor().newInstance();
		} catch (Exception e) {
			throw new Same4JException("Couldn't contruct "+a, e);
		}
	}
	
	/**
	 * This knows how to turn the fields of an object into a stream of {@link FieldAtom}s,
	 * and also reconstruct those into a shallow copy of the original object.
	 */
	public static <K> ReversibleFunction<K, Stream<FieldAtom>> fieldStream(
			Predicate<FieldAtom> matcher,
			Supplier<K> constructor) {
		
		return Reversible.stream(
			o -> inspector(o),rebuilder(constructor));
	}
	
	protected static Stream<FieldAtom> inspector(Object o) {
		return getClassStack(o.getClass()).stream()
			.flatMap(c -> Arrays.stream(c.getDeclaredFields()))
			.map(f -> { 
				try {
					f.setAccessible(true);
					return FieldAtom.of(o, f, f.get(o));
				} catch (Exception e) {
					throw new Same4JException("Couldn't inspect field: "+f, e);
				}
			});
	}
	
	protected static List<Class<?>> getClassStack(Class<?> current) {
		List<Class<?>> out = new ArrayList<>();
		while ((current != null) && (current != Object.class)) {
			out.add(current);
			current = current.getSuperclass();
		}
		
		return out;
	}

	public static <K> Collector<FieldAtom, List<FieldAtom>, K> rebuilder(Supplier<K> con) {
		return Collections.atomCollector(con, a -> {
			K out = con.get();
			for (FieldAtom fi : a) {
				try {
					fi.getProperty().setAccessible(true);
					fi.getProperty().set(out, fi.getValue());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new Same4JException("Couldn't set field: "+fi.getProperty(), e);
				}
			}
		
			return out;
		});
	}
	
	public static Predicate<FieldAtom> nonStatic() {
		return fi -> !Modifier.isStatic(fi.getProperty().getModifiers());
	}
	
	public static Predicate<FieldAtom> nonTransient() {
		return fi -> !Modifier.isTransient(fi.getProperty().getModifiers());
	}
}
