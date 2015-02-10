package de.jambit.iot.nosgi.demo;

public class IoTDemo {

	private final String TAG = "IoTDemo: ";

	private final LedActuator ledActuator = new LedActuator();

	public IoTDemo() throws InterruptedException {

		System.out.println(TAG + "Jambit IoT Demo: Initialize");
		
		ledActuator.pulse();

		

	}
}
