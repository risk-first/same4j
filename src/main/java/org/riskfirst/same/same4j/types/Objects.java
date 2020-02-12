package org.riskfirst.same.same4j.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.NonReversibleDataException;
import org.riskfirst.same.same4j.ReversibleFunction;
import org.riskfirst.same.same4j.Same;

/**
 * Provides functionality to build/deconstruct java objects.
 */
public class Objects {

	public static class FieldInstance {
		
		final Field f;
		final Object owner;
		final Object value;
		
		public FieldInstance(Object owner, Field f, Object value) {
			super();
			this.owner = owner;
			this.f = f;
			this.value = value;
		}
	}
	
	/**
	 * This knows how to turn the fields of an object into a stream of {@link FieldInstance}s,
	 * and also reconstruct those into a shallow copy of the original object.
	 */
	public static <K> ReversibleFunction<K, Stream<FieldInstance>> fieldStream(
			Predicate<FieldInstance> matcher,
			Supplier<K> constructor) {
		
		return Same.stream(
			o -> inspector(o),rebuilder(constructor));
	}
	
	protected static Stream<FieldInstance> inspector(Object o) {
		return getClassStack(o).stream()
			.flatMap(c -> Arrays.stream(c.getDeclaredFields()))
			.map(f -> { 
				try {
					f.setAccessible(true);
					return new FieldInstance(o, f, f.get(o));
				} catch (Exception e) {
					throw new NonReversibleDataException("Couldn't inspect field: "+f, e);
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

	protected static <K> Collector<FieldInstance, ArrayList<FieldInstance>, K> rebuilder(Supplier<K> con) {
		return Collector.of(
			ArrayList::new,
			ArrayList::add,
			( a, b ) -> { a.addAll(b); return b; },
			a -> {
				K out = con.get();
				for (FieldInstance fi : a) {
					try {
						fi.f.set(out, fi.value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new NonReversibleDataException("Couldn't set field: "+fi.f, e);
					}
				}
				return out;
			});
	}
	
	public static Predicate<FieldInstance> nonStatic() {
		return fi -> !Modifier.isStatic(fi.f.getModifiers());
	}
	
	public static Predicate<FieldInstance> nonTransient() {
		return fi -> !Modifier.isTransient(fi.f.getModifiers());
	}
}
