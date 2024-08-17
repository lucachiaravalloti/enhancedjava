package com.github.lucachiaravalloti.test.enhancedjava.gui.table1;

import java.util.Date;

public class Person {

	String name, lastname, city;
	Date date;
	Status status;

	Person(String name, String lastname, String city, Status status, Date date) {

		this.name = name;
		this.lastname = lastname;
		this.city = city;
		this.status = status;
		this.date = date;
	}

	public Object getField(TableFields field) {

		switch (field) {
		case CITY:
			return city;
		case LASTNAME:
			return lastname;
		case NAME:
			return name;
		case RECORD_DATE:
			return date;
		case STATUS:
			return status;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	enum Status {

		ENABLED, DISABLED
	}
}
