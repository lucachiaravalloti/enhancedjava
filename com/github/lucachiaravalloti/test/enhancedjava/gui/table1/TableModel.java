package com.github.lucachiaravalloti.test.enhancedjava.gui.table1;

import java.util.List;

import com.github.lucachiaravalloti.enhancedjava.gui.table.EnhancedAbstractTableModel;

public class TableModel extends EnhancedAbstractTableModel<TableFields, Person> {

	private static final long serialVersionUID = 1L;

	public TableModel(List<Person> persons) {

		super(TableFields.class, Person.class);
		
		addAll(persons);
	}

	@Override
	protected boolean isFieldPresent(TableFields field) {

		switch (field) {
		case CITY:
		case LASTNAME:
		case NAME:
		case PROG:
		case RECORD_DATE:
		case STATUS:
			return true;
		default:
			return false;
		}
	}
}
