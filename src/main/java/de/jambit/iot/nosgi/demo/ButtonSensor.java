package de.jambit.iot.nosgi.demo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;

public class ButtonSensor implements Runnable {

	private final String TAG = "ButtonSensor: ";
	
	public static String TOPIC_SENSE = "jambit-iot/max/sensors/button/";

	private boolean running = false;

	private PinState state;

	public PinState getState() {
		return state;
	}

	public void setState(PinState state) {
		this.state = state;
	}

	// create gpio controller
	final GpioController gpio = GpioFactory.getInstance();

	// provision gpio pin #05 as an input pin with its internal pull down
	// resistor enabled
	final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(
			RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);

	public ButtonSensor() {
		myButton.addTrigger(new GpioCallbackTrigger(new MyCallable<Void>(this)));
		
		
	}

	public void run() {
		this.running = true;
		while (running) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		this.running = false;
		System.out.println("Stopping button sensing!");

	}

	public String getStateAsString() {
		return state.toString();
	}
}