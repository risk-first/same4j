package org.riskfirst.same.same4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Same {
	
	public static <T, R> ReversibleFunction<T, R> reversible(
		Function<T, R> out,
		Function<R, T> back) {
		
		return new ReversibleFunction<T, R>() {

			@Override
			public R apply(T t) {
				return out.apply(t);
			}

			@Override
			public T inverse(R in) {
				return back.apply(in);
			}
		};
		
	}
	
	/**
	 * A reversible function that converts to a stream and back again.
	 */
	public static <T, A, R> ReversibleFunction<T, Stream<R>> stream(
			Function<T, Stream<R>> splitter, 
			Collector<R, A, T> joiner) {
		
		return new ReversibleFunction<T, Stream<R>>() {

			@Override
			public Stream<R> apply(T t) {
				return splitter.apply(t);
			}

			@Override
			public T inverse(Stream<R> in) {
				return in.collect(joiner);
			}
		};		
	}
	
	/**
	 * Turns a reversible function that applies to individual elements into 
	 * one that works on streams.
	 */
	public static <T, R> ReversibleFunction<Stream<T>, Stream<R>> stream(
		ReversibleFunction<T, R> wrap) {
		
		return new ReversibleFunction<Stream<T>, Stream<R>>() {

			@Override
			public Stream<R> apply(Stream<T> t) {
				return t.map(o -> wrap.apply(o));
			}

			@Override
			public Stream<T> inverse(Stream<R> in) {
				return in.map(o -> wrap.inverse(o));
			}
		};	
	};
		
	/**
	 * A specific version of the above that breaks maps into entries 
	 * and reassembles them.
	 */
	public static <K, V> ReversibleFunction<Map<K, V>, Stream<Map.Entry<K, V>>> mapToStream() {
		
		Function<Map<K, V>, Stream<Map.Entry<K, V>>> splitter = m -> m.entrySet().stream();
		Collector<Map.Entry<K, V>, ?, Map<K, V>> joiner = 
			Collectors.toMap(e -> e.getKey(), e -> e.getValue());
		
		return Same.stream(splitter, joiner);
	}
	
	public static <V> ReversibleFunction<List<V>, Stream<V>> listToStream() {
		return Same.stream(m -> m.stream(), Collectors.toList());
	}
	
	/**
	 * Handles conversion with an intermediate representation.
	 */
	public static <T, R, I> ReversibleFunction<T, R> combine(
		ReversibleFunction<T, I> inSplitter,
		ReversibleFunction<I, R> outSplitter) {
		
		return new ReversibleFunction<T, R>() {

			@Override
			public R apply(T t) {
				return outSplitter.apply(inSplitter.apply(t));
			}

			@Override
			public T inverse(R in) {
				return inSplitter.inverse(outSplitter.inverse(in));
			}
		};
	}
	
	/*
	 * Handles conversion with an intermediate representation.
	 */
	public static <T, R, I, J> ReversibleFunction<T, R> combine(
		ReversibleFunction<T, I> inSplitter,
		ReversibleFunction<I, J> midSplitter,
		ReversibleFunction<J, R> outSplitter) {
		
		return new ReversibleFunction<T, R>() {

			@Override
			public R apply(T t) {
				return outSplitter.apply(midSplitter.apply(inSplitter.apply(t)));
			}

			@Override
			public T inverse(R in) {
				return inSplitter.inverse(midSplitter.inverse(outSplitter.inverse(in)));
			}
		};
	}
	
	
	public static <T, R> ReversibleFunction<T, R> cast(Class<T> from, Class<R> to) {
		
		return new ReversibleFunction<T, R>() {

			@Override
			@SuppressWarnings("unchecked")
			public R apply(T t) {
				if (to.isAssignableFrom(t.getClass())) {
					return (R) t;
				} else {
					throw new ClassCastException("Couldn't cast "+t+" to "+to);
				}
			}

			@Override
			@SuppressWarnings("unchecked")
			public T inverse(R in) {
				if (from.isAssignableFrom(in.getClass())) {
					return (T) in;
				} else {
					throw new ClassCastException("Couldn't cast "+in+" to "+from);
				}
			}
			
			
		};
	}

	/**
	 * Reverses the whole function, so that inverse and apply have the opposite meanings.
	 */
	public static <T, R> ReversibleFunction<R, T> reverse(ReversibleFunction<T, R> orig) {
				
		return new ReversibleFunction<R, T>() {

			@Override
			public T apply(R t) {
				return orig.inverse(t);
			}

			@Override
			public R inverse(T in) {
				return orig.apply(in);
			}
		};
	}
	
}
