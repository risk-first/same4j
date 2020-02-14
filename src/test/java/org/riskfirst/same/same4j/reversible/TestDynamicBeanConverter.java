package org.riskfirst.same.same4j.reversible;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.riskfirst.same.same4j.atom.FieldAtom;
import org.riskfirst.same.same4j.atom.IntAtom;
import org.riskfirst.same.same4j.atom.TypedAtom;
import org.riskfirst.same.same4j.reversible.types.Collections;
import org.riskfirst.same.same4j.reversible.types.Maps;
import org.riskfirst.same.same4j.reversible.types.Objects;

public class TestDynamicBeanConverter {
	
	static class Table {
		
		List<String> columnNames;
		
		List<List<Object>> values;
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Table other = (Table) obj;
			if (columnNames == null) {
				if (other.columnNames != null)
					return false;
			} else if (!columnNames.equals(other.columnNames))
				return false;
			if (values == null) {
				if (other.values != null)
					return false;
			} else if (!values.equals(other.values))
				return false;
			return true;
		}



		@Override
		public String toString() {
			return "Table [columnNames=" + columnNames + ", values=" + values + "]";
		}

		public Table(List<String> columnNames, List<List<Object>> values) {
			super();
			this.columnNames = columnNames;
			this.values = values;
		}
		
		
	}

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
	
	static ReversibleFunction<SomeBean, Stream<FieldAtom>> SOMEBEAN_STREAM = 
		Objects.fieldStream(Objects.nonStatic(), SomeBean::new);

	static ReversibleFunction<SomeBean, SomeBean> SOMEBEAN_COPY = Reversible.combine(
			SOMEBEAN_STREAM, 
			SOMEBEAN_STREAM.reverse());
	
	static ReversibleFunction<FieldAtom, TypedAtom<String, Object>> FIELD_TO_MAP_ENTRY = Reversible.function(
		fi -> TypedAtom.of(fi.getProperty().getName(), fi.getValue()),
		me -> FieldAtom.of(null, getField(me.getProperty()), me.getValue()));

	static ReversibleFunction<SomeBean, Map<String, Object>> SOMEBEAN_TO_MAP = 
			Reversible.combine(
					SOMEBEAN_STREAM, 
					Reversible.stream(FIELD_TO_MAP_ENTRY), 
					Reversible.reverse(Maps.mapToStream()));

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
	
	static List<String> columns = Arrays.asList("age", "name", "hasHair");
	
	
	static ReversibleFunction<FieldAtom, IntAtom<Object>> namesToOrdinalFields(List<String> columns) {
		return Reversible.function(
			fi -> IntAtom.of(columns.indexOf(fi.getProperty().getName()), fi.getValue()),
			da -> FieldAtom.of(null, getField(columns.get(da.getProperty())), da.getValue()));
	}
	
	// single bean to List
	ReversibleFunction<SomeBean, List<Object>> beanToList(List<String> columns) {
		return Reversible.combine(
			SOMEBEAN_STREAM, 
			Reversible.stream(namesToOrdinalFields(columns)),
			Reversible.reverse(Collections.listToAtomStream(
				() -> new ArrayList<Object>())));
		
	}
	
	ReversibleFunction<List<SomeBean>, List<List<Object>>> beansToListList(List<String> columns) {
		return Reversible.combine(
			Collections.listToValueStream(),
			Reversible.stream(beanToList(columns)),
			Reversible.reverse(Collections.listToValueStream()));
	}
		
	ReversibleFunction<List<SomeBean>, Table> beansToTable(List<String> columns) {
		
		return Reversible.function(
				lsb -> new Table(columns, beansToListList(columns).apply(lsb)),
				t -> beansToListList(columns).inverse(t.values));
	}

	@Test
	public void testBeansToTable() {
		SomeBean abc = new SomeBean("abc", 44, false);
		SomeBean def = new SomeBean("def", 22, true);
		SomeBean ghi = new SomeBean("ghi", 11, false);
		
		// single bean to FieldAtoms
		System.out.println("Fields: ");
		SOMEBEAN_STREAM.apply(abc).forEach(c -> System.out.println(c));
		
		// single bean to IntAtoms
		System.out.println("Ordinals: ");
		Reversible.combine(SOMEBEAN_STREAM, Reversible.stream(namesToOrdinalFields(columns)))
			.apply(abc).forEach(c -> System.out.println(c));
		
		System.out.println("Single Bean: \n" + beanToList(columns).apply(abc));
		
		// now wrap it to do the whole table.
		List<SomeBean> beans = Arrays.asList(abc,def,ghi);
		System.out.println("All Beans: \n" + beansToListList(columns).apply(beans));
		
		// now the whole table
		Table out = beansToTable(columns).apply(beans);
		System.out.println(out);
		
		List<SomeBean> beansBack = beansToTable(columns).inverse(out);
		Assert.assertEquals(beansBack, beans);
	}
	
}
