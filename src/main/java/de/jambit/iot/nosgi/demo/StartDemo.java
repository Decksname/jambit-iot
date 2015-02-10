package de.jambit.iot.nosgi.demo;



public class StartDemo {

	static String testTopic = "/led";
	
	public static void main(String[] args) {
		
		try {
			IoTDemo demo = new IoTDemo();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		 
	}
	
}
