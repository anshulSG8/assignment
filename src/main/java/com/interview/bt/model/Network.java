package com.interview.bt.model;

public class Network {
	String networkElement1;
	String networkElement2;
	String link;
	public String getNetworkElement1() {
		return networkElement1;
	}
	public void setNetworkElement1(String networkElement1) {
		this.networkElement1 = networkElement1;
	}
	public String getNetworkElement2() {
		return networkElement2;
	}
	public void setNetworkElement2(String networkElement2) {
		this.networkElement2 = networkElement2;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Network(String networkElement1, String networkElement2, String link) {
		super();
		this.networkElement1 = networkElement1;
		this.networkElement2 = networkElement2;
		this.link = link;
	}
	
	
}
