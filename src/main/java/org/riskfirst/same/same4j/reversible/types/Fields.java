package org.riskfirst.same.same4j.reversible.types;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.Same4JDataException;
import org.riskfirst.same.same4j.atom.FieldAtom;
import org.riskfirst.same.same4j.reversible.Reversible;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;

/**
 * Provides functionality to build/deconstruct java objects.
 */
public class Fields {

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
		return getClassStack(o).stream()
			.flatMap(c -> Arrays.stream(c.getDeclaredFields()))
			.map(f -> { 
				try {
					f.setAccessible(true);
					return FieldAtom.of(o, f, f.get(o));
				} catch (Exception e) {
					throw new Same4JDataException("Couldn't inspect field: "+f, e);
				}
			});
	}
	
	protected static List<Class<?>> getClassStack(Object o) {
		List<Class<?>> out = new ArrayList<>();
		Class<?> current = o.getClass();
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
					throw new Same4JDataException("Couldn't set field: "+fi.getProperty(), e);
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
