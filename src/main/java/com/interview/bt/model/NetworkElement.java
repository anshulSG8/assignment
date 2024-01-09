package com.interview.bt.model;

public class NetworkElement {

	String name;
	String processingTime;
	String exchange;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public NetworkElement(String name, String processingTime, String exchange) {
		super();
		this.name = name;
		this.processingTime = processingTime;
		this.exchange = exchange;
	}

}
