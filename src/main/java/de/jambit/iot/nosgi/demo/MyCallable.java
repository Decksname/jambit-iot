package de.jambit.iot.nosgi.demo;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.PinState;

public class MyCallable<T> implements Callable<Void> {

	private final String TAG = "LedActuator";
	
	private ButtonSensor buttonSensor;

	public MyCallable(ButtonSensor buttonSensor) {
		// TODO Auto-generated constructor stub
		this.buttonSensor = buttonSensor;

	}

	public Void call() throws Exception {
		System.out.println("TAG: --> GPIO TRIGGER CALLBACK RECEIVED "
				+ this.buttonSensor.myButton.getState());

		PinState oldState = this.buttonSensor.getState();
		PinState newState = this.buttonSensor.myButton.getState();

		if (oldState != newState) {
			this.buttonSensor.setState(newState);
			this.buttonSensor.publishState();
		}

		return null;
	}

}
