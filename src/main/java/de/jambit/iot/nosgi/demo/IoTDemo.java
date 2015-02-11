package de.jambit.iot.nosgi.demo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class IoTDemo implements MqttCallback {

	private final String TAG = "IoTDemo: ";
	
	public static final String TOPIC_ANNOUNCEMENT = "jambit-iot/announce/";
	
	private final LedActuator ledActuator = new LedActuator();
	
	public IoTDemo() throws InterruptedException {

		System.out.println(TAG + "Jambit IoT Demo: Initialize");
		
		MqttHandler.getInstance().connect();
		MqttHandler.getInstance().subscribeToTopic(ledActuator.TOPIC_CONTROL, this);
		
		ledActuator.pulse();

		Thread buttonSensorThread = new Thread(new ButtonSensor());
		buttonSensorThread.start();
		
		Thread tempSensorThread = new Thread(new TemperatureSensor());
        tempSensorThread.start();
		

	}

	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		System.out.println(TAG + "now you should handle a Connection Lost");
	}

	public void messageArrived(String topic, MqttMessage message)
			throws Exception {

		if (message.toString().equals("on")) {
			ledActuator.turnOn();
		} else if (message.toString().equals("off")) {
			ledActuator.turnOff();
		}
		else if (message.toString().equals("pulse")) {
			ledActuator.pulse();
		}else {
			System.out.println(TAG + "Unknown control message: '" + message + "'");
		}
	}

	
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// this one is annoying...
//		System.out.println("deliveryComplete");
	}
}
