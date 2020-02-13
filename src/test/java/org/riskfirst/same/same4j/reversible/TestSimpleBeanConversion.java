package org.riskfirst.same.same4j.reversible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.atom.TypedAtom;
import org.riskfirst.same.same4j.reversible.types.Collections;

public class TestSimpleBeanConversion {

	public static class TestBean1 {
		 
		Map<String, String> namesToAges = new HashMap<>();

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestBean1 other = (TestBean1) obj;
			if (namesToAges == null) {
				if (other.namesToAges != null)
					return false;
			} else if (!namesToAges.equals(other.namesToAges))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestBean1 [namesToAges=" + namesToAges + "]";
		}

	}
	
	public static class Tuple<X, Y> {
		
		X a;
		Y b;
		
		public Tuple(X a, Y b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tuple other = (Tuple) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Tuple [a=" + a + ", b=" + b + "]";
		}
	}
	
	public static class TestBean2 {
		
		List<Tuple<String, Integer>> namesAndAges = new ArrayList<>();

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestBean2 other = (TestBean2) obj;
			if (namesAndAges == null) {
				if (other.namesAndAges != null)
					return false;
			} else if (!namesAndAges.equals(other.namesAndAges))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "TestBean2 [namesAndAges=" + namesAndAges + "]";
		}
		
	}
	
	public static TestBean1 EXAMPLE1 = new TestBean1();
	public static TestBean2 EXAMPLE2 = new TestBean2();
	
	static {
		EXAMPLE1.namesToAges.put("John", "54");
		EXAMPLE1.namesToAges.put("Ella", "16");
		EXAMPLE1.namesToAges.put("Fido", "6");
		
		EXAMPLE2.namesAndAges.add(new Tuple<String, Integer>("John", 54));
		EXAMPLE2.namesAndAges.add(new Tuple<String, Integer>("Ella", 16));
		EXAMPLE2.namesAndAges.add(new Tuple<String, Integer>("Fido", 6));
	}
	
	static ReversibleFunction<String, Integer> STRING_TO_INTEGER = Reversible.guard(
		Reversible.reversible(
			s -> Integer.parseInt(s),
			i -> i.toString()),
		s -> s.matches("^[-+]?\\d+$"), 
		i -> true );
	
	
	static ReversibleFunction<TypedAtom<String, String>, Tuple<String, Integer>> ENTRY_TO_TUPLE = Reversible.reversible(
		e -> new Tuple<String, Integer>(e.getProperty(), STRING_TO_INTEGER.apply(e.getValue())), 
		t -> TypedAtom.of(t.a, STRING_TO_INTEGER.inverse(t.b)));
	
	static ReversibleFunction<Stream<TypedAtom<String, String>>, Stream<Tuple<String, Integer>>> ENTRY_TO_TUPLE_STREAM = Reversible.stream(ENTRY_TO_TUPLE);
	
	static ReversibleFunction<Stream<Tuple<String, Integer>>, List<Tuple<String, Integer>>> TUPLE_STREAM_TO_LIST = Reversible.reverse(Collections.listToValueStream());
			
	static ReversibleFunction<Map<String, String>, List<Tuple<String, Integer>>> MAP_TO_LIST = Reversible.combine(
			Collections.mapToStream(),
			ENTRY_TO_TUPLE_STREAM,
			TUPLE_STREAM_TO_LIST);
	
	static ReversibleFunction<TestBean1, TestBean2> TEST_BEAN_1_TO_2 = Reversible.reversible(
		tb1 -> { TestBean2 out = new TestBean2(); out.namesAndAges = MAP_TO_LIST.apply(tb1.namesToAges); return out; },
		tb2 -> { TestBean1 out = new TestBean1(); out.namesToAges = MAP_TO_LIST.inverse(tb2.namesAndAges); return out; });
	
	@Test
	public void convertTestBean1ToTestBean2() {
		Assert.assertTrue(TEST_BEAN_1_TO_2.equals(EXAMPLE1, EXAMPLE2));
		
		String tb1 = EXAMPLE1.toString();
		String tb2 = EXAMPLE2.toString();
		
		String tb2b = TEST_BEAN_1_TO_2.apply(EXAMPLE1).toString();
		String tb1b = TEST_BEAN_1_TO_2.inverse(EXAMPLE2).toString();
		
		System.out.println(tb1);
		System.out.println(tb1b);
		System.out.println(tb2);
		System.out.println(tb2b);
	
		Assert.assertEquals(tb1, tb1b);
		Assert.assertEquals(tb2, tb2b);
	}
}
