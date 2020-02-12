package org.riskfirst.same.same4j;

import java.lang.reflect.Field;
import java.util.Map;
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
	
	static ReversibleFunction<FieldInstance, Map.Entry<String, Object>> FIELD_TO_MAP_ENTRY = Same.reversible(
		fi -> Same.mapEntry(fi.getField().getName(), fi.getValue()),
		me -> Objects.fieldInstance(null, getField(me.getKey()), me.getValue()));

	static ReversibleFunction<SomeBean, Map<String, Object>> SOMEBEAN_TO_MAP = 
			Same.combine(SOMEBEAN_STREAM, 
			Same.stream(FIELD_TO_MAP_ENTRY), 
			Same.reverse(Same.mapToStream()));

	public static Field getField(String name) {
		try {
			return SomeBean.class.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new UnsupportedOperationException("Couldn't examine", e);
		}
	}
			

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
	
	@Test
	public void testBeanToMap() {
		SomeBean sb1 = new SomeBean("karhot", 44, false);
		Map<String, Object> map = SOMEBEAN_TO_MAP.apply(sb1);
		System.out.println(sb1);
		System.out.println(map);
		
		SomeBean sb2 = SOMEBEAN_TO_MAP.inverse(map);
		Assert.assertEquals(sb1, sb2);
	}
}
