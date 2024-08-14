package com.github.lucachiaravalloti.enhancedjava.gui.table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public abstract class EnhancedAbstractTableModel<E extends Enum<?>, T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<T> records = new LinkedList<T>();
	private Map<Integer, E> fieldsByColumnIndex = new HashMap<>();
	private Field columnName, horizontalAlignment;
	private Method getField;
	private int progColumnIndex;

	public EnhancedAbstractTableModel(Class<E> enumClass, Class<T> recordClass) {

		super();

		try {

			columnName = enumClass.getDeclaredField("columnName");
		} catch (NoSuchFieldException | SecurityException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		try {

			horizontalAlignment = enumClass.getDeclaredField("horizontalAlignment");
		} catch (NoSuchFieldException | SecurityException e) {

			System.out.println(
					"EnhancedAbstractTableModel: horizontalAlignment field not present in " + enumClass.getName());
		}

		try {

			// public Object getField(enumClass field)
			getField = recordClass.getDeclaredMethod("getField", new Class[] { enumClass });
		} catch (NoSuchMethodException | SecurityException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		progColumnIndex = -1;
		int columnIndex = 0;
		for (E field : enumClass.getEnumConstants()) {

			if (isFieldPresent(field)) {

				if (field.name().equals("PROG"))
					progColumnIndex = columnIndex;

				fieldsByColumnIndex.put(columnIndex++, field);
			}
		}
	}

	protected abstract boolean isFieldPresent(E field);

	@Override
	public int getColumnCount() {

		return fieldsByColumnIndex.size();
	}

	@Override
	public int getRowCount() {

		return records.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		if (columnIndex == getProgColumnIndex())
			return null;

		try {

			return getField.invoke(getRecordAt(rowIndex), fieldsByColumnIndex.get(columnIndex));
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		if (records.isEmpty() || getValueAt(0, columnIndex) == null)

			return Object.class;
		else

			return getValueAt(0, columnIndex).getClass();
	}

	@Override
	public String getColumnName(int columnIndex) {

		try {

			return (String) columnName.get(fieldsByColumnIndex.get(columnIndex));
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Integer getHorizontalAlignment(int columnIndex) {

		if (horizontalAlignment == null)

			return null;

		try {

			return (Integer) horizontalAlignment.get(fieldsByColumnIndex.get(columnIndex));
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public E getColumnField(int columnIndex) {

		return fieldsByColumnIndex.get(columnIndex);
	}

	public int getProgColumnIndex() {

		return progColumnIndex;
	}

	public boolean isProgColumnPresent() {

		return progColumnIndex != -1;
	}

	public boolean add(T record) {

		if (!contains(record)) {

			records.add(record);
			fireTableRowsInserted(getRowCount(), getRowCount());
			return true;
		} else

			return false;
	}

	public boolean contains(T record) {

		return records.contains(record);
	}

	public void empty() {

		records = new LinkedList<T>();
		fireTableDataChanged();
	}

	public T getRecordAt(int rowIndex) {

		return records.get(rowIndex);
	}
}
