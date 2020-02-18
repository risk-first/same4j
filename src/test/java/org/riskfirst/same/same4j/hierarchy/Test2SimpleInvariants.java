package org.riskfirst.same.same4j.hierarchy;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.Same;
import org.riskfirst.same.same4j.builder.ReversibleExpectation;
import org.riskfirst.same.same4j.builder.Site;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Objects;


public class Test2SimpleInvariants {
	
	@Test
	public void testObjectInvariantsTopLevel() {
		Site<TestObject1, TestObject2> i = Same.with(TestObject1.class, TestObject2.class);
		ReversibleFunction<TestObject1, TestObject2> f = i.use(Objects.shallow(TestObject1.class, TestObject2.class)).done();
		Assert.assertTrue(f.apply(new TestObject1()) instanceof TestObject2);
	}
	
	@Test
	public void testOb1Ob2FieldInvariant() {
		Site<TestObject1, TestObject2> i = 
			Same.with(TestObject1.class, TestObject2.class);
	
		i.use(Objects.shallow(TestObject1.class, TestObject2.class))
			.and(
				i.a().getA(), 
				i.b().getD())
			.identical();
		
		ReversibleFunction<TestObject1, TestObject2> f = i.done();
		
		TestObject1 from = new TestObject1("a", null, null);
		TestObject2 to = new TestObject2("a", null, null, null);
		
		TestObject2 toA = f.apply(from);
		Assert.assertEquals(to, toA);
		TestObject1 fromA = f.inverse(to);
		Assert.assertEquals(from, fromA);
	}
	
	@Test
	public void testOb1Ob2LambdaInvariants() {
		
		ReversibleFunction<TestObject1, TestObject2> f = 
			Same.with(TestObject1.class, TestObject2.class)
				.and(a -> a.getA(), b -> b.getD())
				.use(a -> a.toUpperCase(), 
						  b -> b.toLowerCase())
				.rootDone();
		
		TestObject1 from = new TestObject1("a", null, null);
		TestObject2 to = new TestObject2("A", null, null, null);
	
		TestObject2 toA = f.apply(from);
		Assert.assertEquals(to, toA);
		TestObject1 fromA = f.inverse(to);
		Assert.assertEquals(from, fromA);
		
		// should also be null-safe
		Assert.assertEquals(null, f.apply(null));
	}
	
	@Test
	public void testOb1Ob2FieldNavigation() {
		
		ReversibleFunction<TestObject1, TestObject2> f = 
			Same.with(TestObject1.class, TestObject2.class)
				.and(Prop.of(a -> a.a, (a, v) -> a.a = v), Prop.of(b -> b.d, (b,v) -> b.d = v))
				.use(a -> a.toUpperCase(), 
						  b -> b.toLowerCase())
				.rootDone();
		
		TestObject1 from = new TestObject1("a", null, null);
		TestObject2 to = new TestObject2("A", null, null, null);
	
		TestObject2 toA = f.apply(from);
		Assert.assertEquals(to, toA);
		TestObject1 fromA = f.inverse(to);
		Assert.assertEquals(from, fromA);
		
		// should also be null-safe
		Assert.assertEquals(null, f.apply(null));
	}
		
		
/*		
		// alternate syntax (functions, better), declares that b is the upper case of a's lower case
		i.and(a -> a.getA(), b -> b.getD())
			.use(a -> a.toUpperCase(), b -> b.toLowerCase());
		
		// do lower-level invariant setting, third syntax ftw.
		
		Label x = Label.;
		
		Invariant<>i.and(TestObject1::getC, TestObject2::getF)
			.and(a -> a.get(x._int()), b -> b.get(x._int()));
		
		
		ReversibleFunction<TestObject1, TestObject2> rf = i.build();
		
		TestObject1 to = new TestObject1(a, b, c)
		
		.use(Numbers.STRING_TO_INTEGER);
		
	}*/
}
