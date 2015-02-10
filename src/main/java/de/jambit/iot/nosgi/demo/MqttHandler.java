package de.jambit.iot.nosgi.demo;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler {
	
	private final String TAG = "MqttHandler: ";

//	threadsave singleton pattern
	  private static MqttHandler instance;
	 
	  private MqttHandler () {}
	  
	  public static synchronized MqttHandler getInstance () {
	    if (MqttHandler.instance == null) {
	    	MqttHandler.instance = new MqttHandler ();
	    }
	    return MqttHandler.instance;
	  }
	  
	  	
	
    private final String mqttBroker       = "tcp://localhost:1883";
    private final String clientId     = "Max";
    private final int qos = 0;
    private final MemoryPersistence persistence = new MemoryPersistence();
    

    private MqttClient mqttClient;
    
    public boolean connect(){
    	try {
            this.mqttClient = new MqttClient(mqttBroker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println(TAG + "Connecting to broker: "+mqttBroker);
            mqttClient.connect(connOpts);
            System.out.println(TAG + "Connected");
            return true;

        } catch(MqttException me) {
            System.err.println("reason "+me.getReasonCode());
            System.err.println("msg "+me.getMessage());
            System.err.println("loc "+me.getLocalizedMessage());
            System.err.println("cause "+me.getCause());
            System.err.println("excep "+me);
            me.printStackTrace();
            
            return false;
        }
    }
    
    
    public boolean disconnect()
	{
        try {
			this.mqttClient.disconnect();
			System.out.println(TAG + "Disconnected");
	        return true;
		} catch(MqttException me) {
            System.err.println("reason "+me.getReasonCode());
            System.err.println("msg "+me.getMessage());
            System.err.println("loc "+me.getLocalizedMessage());
            System.err.println("cause "+me.getCause());
            System.err.println("excep "+me);
            me.printStackTrace();
            
            return false;
        }
        

    }
    
    public void publishMessageOnTopic(String messageString, String topic){
//    	System.out.println("Publishing message: "+messageString);
        MqttMessage message = new MqttMessage(messageString.getBytes());
        message.setQos(qos);
        try {
			this.mqttClient.publish(topic, message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void subscribeToTopic(String topic, MqttCallback callback){
    	try {
			this.mqttClient.subscribe(topic);
			this.mqttClient.setCallback(callback);
			
			System.out.println(TAG + "Subscribed to: '" + topic + "'" );
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
    	MqttHandler mqttHandler = MqttHandler.getInstance();
    	mqttHandler.connect();
    	mqttHandler.publishMessageOnTopic("This is just a test", "much/test/such/mqtt");
	}
	
}
