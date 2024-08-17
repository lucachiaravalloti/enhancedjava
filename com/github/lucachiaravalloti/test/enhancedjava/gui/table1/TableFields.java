package com.github.lucachiaravalloti.test.enhancedjava.gui.table1;

import javax.swing.SwingConstants;

public enum TableFields {

	PROG(null, null), NAME("Name", SwingConstants.RIGHT), LASTNAME("Lastname", SwingConstants.CENTER), CITY("City", null),
	RECORD_DATE("Record Date", SwingConstants.CENTER), STATUS("Status", null);

	public final String columnName;
	public final Integer horizontalAlignment;

	TableFields(String columnName, Integer horizontalAlignment) {

		this.columnName = columnName;
		this.horizontalAlignment = horizontalAlignment;
	}
}
