package de.jambit.iot.nosgi.demo;

public class IoTDemo {

	private final String TAG = "IoTDemo: ";
	
	public static final String TOPIC_ANNOUNCEMENT = "jambit-iot/announce/";
	
	private final LedActuator ledActuator = new LedActuator();
	
	public IoTDemo() throws InterruptedException {

		System.out.println(TAG + "Jambit IoT Demo: Initialize");
		
		ledActuator.pulse();

		Thread buttonSensorThread = new Thread(new ButtonSensor());
		buttonSensorThread.start();
		
		

	}
}
