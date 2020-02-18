package org.riskfirst.same.same4j.hierarchy;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.Reversible;
import org.riskfirst.same.same4j.hierarchy.ReversibleConsumer;
import org.riskfirst.same.same4j.hierarchy.types.Beans;
import org.riskfirst.same.same4j.reversible.ReversibleFunction;
import org.riskfirst.same.same4j.reversible.types.Objects;

public class Test1Beans {

	@Test
	public void testBeanOperations() {
		ReversibleFunction<TestObject1, TestObject2> f = 
				Objects.shallow(TestObject1.class, TestObject2.class);
		
		ReversibleConsumer<TestObject1, TestObject2> getOp = Reversible.of(
				Prop.of(a -> a.getA(), (a, b) -> a.setA(b)),
				Prop.of(a -> a.getD(), (a, b) -> a.setD(b)),
				Objects.identity());
		
		f = f.combine(getOp);
		
		TestObject1 to1 = new TestObject1("a", null, null);
		TestObject2 to2 = new TestObject2("a", null, null, null);
		
		Assert.assertEquals(to2, f.apply(to1));
		Assert.assertEquals(to1, f.inverse(to2));
	}

	@Test
	public void testBeanOpsDynamic() {
		ReversibleFunction<TestObject1, TestObject2> f = 
				Objects.shallow(TestObject1.class, TestObject2.class);
		
		ReversibleConsumer<TestObject1, TestObject2> getOp = Reversible.of(
				Beans.prop(TestObject1.class, "a"),
				Beans.prop(TestObject2.class, "d"),
				Objects.identity());
		
		f = f.combine(getOp);
		
		TestObject1 to1 = new TestObject1("a", null, null);
		TestObject2 to2 = new TestObject2("a", null, null, null);
		
		Assert.assertEquals(to2, f.apply(to1));
		Assert.assertEquals(to1, f.inverse(to2));
	}

	@Test
	public void testRawOperations() {
		ReversibleFunction<TestObject1, TestObject2> f = 
			Objects.shallow(TestObject1.class, TestObject2.class);
		
		f = f.combine(Reversible.consumer(
			(o1, o2) -> o2.setD(o1.getA()), 
			(o2, o1) -> o1.setA(o2.getD())));
				
		TestObject1 to1 = new TestObject1("a", null, null);
		TestObject2 to2 = new TestObject2("a", null, null, null);
		
		Assert.assertEquals(to2, f.apply(to1));
		Assert.assertEquals(to1, f.inverse(to2));
	}

}
