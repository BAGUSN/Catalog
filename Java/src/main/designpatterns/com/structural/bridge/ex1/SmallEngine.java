package com.structural.bridge.ex1;

public class SmallEngine implements Engine {

	int horsepower;

	public SmallEngine() {
		horsepower = 100;
	}

	public int go() {
		System.out.println("The small engine is running");
		return horsepower;
	}

}

