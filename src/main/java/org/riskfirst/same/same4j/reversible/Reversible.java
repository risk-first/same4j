package org.riskfirst.same.same4j.reversible;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.riskfirst.same.same4j.Same4JException;

/**
 * Marker interface for all reversible computations.
 * 
 * @author robmoffat
 *
 */
public interface Reversible {

	/**
	 * Builds a reversible function.
	 */
	public static <T, R> ReversibleFunction<T, R> of(
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

			@Override
			public Predicate<T> domain() {
				return inSplitter.domain();
			}

			@Override
			public Predicate<R> range() {
				return outSplitter.range();
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
			

			@Override
			public Predicate<T> domain() {
				return inSplitter.domain();
			}

			@Override
			public Predicate<R> range() {
				return outSplitter.range();
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
			

			@Override
			public Predicate<T> domain() {
				return t -> from.isAssignableFrom(t.getClass());
			}

			@Override
			public Predicate<R> range() {
				return r -> to.isAssignableFrom(r.getClass());
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

			@Override
			public Predicate<R> domain() {
				return orig.range();
			}

			@Override
			public Predicate<T> range() {
				return orig.domain();
			}
		};
	}

	/**
	 * Sets the domain and range for the reversible function.
	 */
	public static <T, R> ReversibleFunction<T, R> allows(ReversibleFunction<T, R> orig, Predicate<T> domain, Predicate<R> range) {
				
		return new ReversibleFunction<T, R>() {
	
			@Override
			public R apply(T t) {
				return orig.apply(t);
			}
	
			@Override
			public T inverse(R in) {
				return orig.inverse(in);
			}
	
			@Override
			public Predicate<T> domain() {
				return domain;
			}
	
			@Override
			public Predicate<R> range() {
				return range;
			}
		};
	}
	
	/**
	 * Checks the domain/range of the function before calling.
	 */
	public static <T, R> ReversibleFunction<T, R> guard(ReversibleFunction<T, R> orig) {
		
		return new ReversibleFunction<T, R>() {

			@Override
			public R apply(T t) {
				if (orig.domain().test(t)) {
					return orig.apply(t);
				} else {
					throw new Same4JException("Not within domain: "+t);
				}
			}

			@Override
			public T inverse(R in) {
				if (orig.range().test(in)) {
					return orig.inverse(in);
				} else {
					throw new Same4JException("Not within range: "+in);
				}
			}

			@Override
			public Predicate<T> domain() {
				return orig.domain();
			}

			@Override
			public Predicate<R> range() {
				return orig.range();
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
	}
	
	public static <T, R> ReversibleFunction<T, R> combine(
		ReversibleFunction<T, R> start,
		ReversibleConsumer<T, R> then) {
			
		return start;
		
		
	}
 
}
