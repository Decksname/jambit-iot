package de.jambit.iot.nosgi.demo;

import java.io.IOException;
import java.text.DecimalFormat;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import de.jambit.iot.nosgi.demo.MqttHandler;
 
public class TemperatureSensor implements Runnable {
 
	private final String TAG = "TemperatureSensor: ";
	
	private boolean running = false;
	
    private I2CBus i2cbus;
    private I2CDevice temperatureSensor;
    
    public static String TOPIC_SENSE = "jambit-iot/max/sensors/temperature/"; 
    
    private float currentTemperature;
 
    public TemperatureSensor() {
        try {
            i2cbus = I2CFactory.getInstance(I2CBus.BUS_1);
            temperatureSensor = i2cbus.getDevice(0x40);
 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    
    public String getStateAsString() {
    	return this.currentTemperature+"";
    }
    
    public void run() {
        try {
        	this.running = true;
            while(running){
            	currentTemperature= readTemperature();
            	MqttHandler.getInstance().publishMessageOnTopic(getStateAsString(), TOPIC_SENSE);
            	Thread.sleep(1000);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public void stop(){
    	this.running = false;
    	System.out.println(TAG + "Stopping temperatrue sensing!");
    		
    }
    
 
    /*
     * See sensor documentation here:
     * http://www.hoperf.cn/upload/sensor/TH02_V1.1.pdf
     */
    private synchronized float readTemperature() throws IOException {
        float temperature;
        // Set START (D0) and TEMP (D4) in CONFIG (register 0x03) to begin a
        // new conversion, i.e., write CONFIG with 0x11
        temperatureSensor.write(0x03, (byte) 0x11);
 
        // Poll RDY (D0) in STATUS (register 0) until it is low (=0)
        int status = -1;
        while ((status & 0x01) != 0) {
            status = temperatureSensor.read(0x00);
        }
 
        // Read the upper and lower bytes of the temperature value from
        // DATAh and DATAl (registers 0x01 and 0x02), respectively
        byte[] buffer = new byte[3];
        temperatureSensor.read(buffer, 0, 3);
 
        int dataH = buffer[1] & 0xff;
        int dataL = buffer[2] & 0xff;
         
 
        temperature = (dataH * 256 + dataL) >> 2;
        temperature = (temperature / 32f) - 50f;
 
         
        // truncate to 2 decimals
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(temperature));
    }
    

 
}