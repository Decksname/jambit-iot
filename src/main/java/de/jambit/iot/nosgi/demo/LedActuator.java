package de.jambit.iot.nosgi.demo;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;

public class LedActuator{
	
	private final String TAG = "LedActuator: ";
	
	private GpioController gpioController;
    private GpioPinDigitalMultipurpose ledPin;
    
    public static String TOPIC_SENSE = "jambit-iot/max/sensors/led/";
    public static String TOPIC_CONTROL = "jambit-iot/max/actuators/led/control";
    
    public LedActuator() {
        gpioController = GpioFactory.getInstance();
		
		ledPin = gpioController.provisionDigitalMultipurposePin(
		        RaspiPin.GPIO_00, "led", PinMode.DIGITAL_OUTPUT);
		
		ledPin.setShutdownOptions(true); // unexport on shutdown
		
		MqttHandler.getInstance().publishMessageOnTopic(TOPIC_SENSE, IoTDemo.TOPIC_ANNOUNCEMENT);
		
		System.out.println(TAG + "initialized");
    }

	public void turnOn() {
		// TODO Auto-generated method stub
		ledPin.toggle();
		System.out.println(TAG + "Led turned On");
	}
	
	public void turnOff() {
		// TODO Auto-generated method stub
		ledPin.low();
		System.out.println(TAG + "Led turned Off");
	}
	
	public void pulse() {
		// TODO Auto-generated method stub
		ledPin.low();
		ledPin.pulse(2000);
		System.out.println(TAG + "Led pulsing");
	}
	
	public String getStateAsString() {
		if(ledPin.isLow()){
			return "off";
		}else{
			return "on";
		}
		
	}
	
	public void publishState() {
		MqttHandler.getInstance().publishMessageOnTopic(TOPIC_SENSE, this.getStateAsString());
	}
	
	
}
