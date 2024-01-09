package com.interview.bt.model;

public class Person {

	String personName;
	String exchange;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public Person(String personName, String exchange) {
		super();
		this.personName = personName;
		this.exchange = exchange;
	}

}
