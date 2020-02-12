package org.riskfirst.same.same4j;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.types.Objects;
import org.riskfirst.same.same4j.types.Objects.FieldInstance;

public class TestDynamicBeanConverter {

	static class SomeBean {
		
		public SomeBean(String name, int age, boolean hasHair) {
			super();
			this.name = name;
			this.age = age;
			this.hasHair = hasHair;
		}

		public SomeBean() {
			super();
		}

		String name;
		int age;
		boolean hasHair;
		
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SomeBean other = (SomeBean) obj;
			if (age != other.age)
				return false;
			if (hasHair != other.hasHair)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}


		@Override
		public String toString() {
			return "SomeBean [name=" + name + ", age=" + age + ", hasHair=" + hasHair + "]";
		}
	}
	
	static ReversibleFunction<SomeBean, Stream<FieldInstance>> SOMEBEAN_STREAM = 
		Objects.fieldStream(Objects.nonStatic(), SomeBean::new);

	static ReversibleFunction<SomeBean, SomeBean> SOMEBEAN_COPY = Same.combine(
			SOMEBEAN_STREAM, 
			SOMEBEAN_STREAM.reverse());

	@Test
	public void testShallowCopy() {
		SomeBean sb1 = new SomeBean("karhot", 44, false);
		SomeBean sb2 = SOMEBEAN_COPY.apply(sb1);
		System.out.println(sb1);
		System.out.println(sb2);
		Assert.assertEquals(sb1, sb2);
		Assert.assertEquals(sb1.toString(), sb2.toString());
		Assert.assertFalse(sb1==sb2);
	}
}
