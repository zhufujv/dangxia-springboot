package com.zyz.dangxia.util.mqtt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyz.dangxia.common.converstion.MessageDto;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 在此处，只需要用来发送消息就行
 *
 * @author LichFaker on 16/3/24.
 * @Email lichfaker@gmail.com
 */
public class MqttManager {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //mqtt服务器端口
    private static String port = "1883";

    // 连接时的ID
    @Value("${mqtt.id:-3}")
    private String ID = "-3";

    // 单例
    private static MqttManager mInstance = null;

    // 回调
    private MqttCallback mCallback;

    // Private instance variables
    private MqttClient client;
    private MqttConnectOptions conOpt;
    private ObjectMapper mapper;

    private boolean clean = true;

    private MqttManager() {
        mapper = new ObjectMapper();
        mCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        };
    }

    public static MqttManager getInstance() {
        if (null == mInstance) {
            mInstance = new MqttManager();
            mInstance.creatConnect();
        }
        return mInstance;
    }
//    @PostConstruct
//    public  void startQueue() {
//        Thread thread = new Thread(() -> {
//            while(true){
//                    try {
//                        final MqttMsgBean topicMessage = SubPubQueue.getMsgQueue().take();
//                            logger.info("广播消息:" + topicMessage.getMqttMessage().toString());
//                            //广播消息
//                            //将msgbean发送
//
//
//
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//
//        });
//        logger.info("队列开始启动");
//        thread.start();
//    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取url
     *
     * @return
     */
    private static String getUrl() {
//        return "tcp://"+ UrlHandler.getIp()+":"+port;
        return "tcp://" + "121.40.140.223" + ":" + port;
    }


//    /**
//     * 获取特定mqtt主题-设备信息
//     */
//    public static String getDeviceInfoTopic() {
//        return "/lpwa/app/"+appId+"/info/"+sp.getLong("user_id",-1L);
//    }

    /**
     * 获取特定mqtt主题-聊天消息
     */
//    public static String getMsgTopic() {
//        return "/dangxia/app/msg/"+sp.getInt("user_id",-1);
//    }

    /**
     * 使用默认信息进行mqtt连接
     */
    public boolean creatConnect() {
        return creatConnect(getUrl(),
                /*sp.getString("account",""),*/
                /*sp.getString("password",""),*/
                null,
                null,
                ID);
    }

    /**
     * 创建Mqtt 连接
     *
     * @param brokerUrl Mqtt服务器地址(tcp://xxxx:1863)
     * @param userName  用户名
     * @param password  密码
     * @param clientId  clientId
     * @return
     */
    public boolean creatConnect(String brokerUrl, String userName, String password, String clientId) {
        boolean flag = false;
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            conOpt.setCleanSession(clean);
            if (password != null) {
                conOpt.setPassword(password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(userName);
            }

            // Construct an MQTT blocking mode client
            client = new MqttClient(brokerUrl, clientId, dataStore);

            // Set this wrapper as the callback handler
            client.setCallback(mCallback);
            flag = doConnect();
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }

        return flag;
    }

    /**
     * 建立连接
     *
     * @return
     */
    public boolean doConnect() {
        boolean flag = false;
        if (client != null) {
            try {
                client.connect(conOpt);
                logger.info("Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * Publish / send a message to an MQTT server
     *
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    public boolean publish(String topicName, int qos, byte[] payload) {

        boolean flag = false;

        if (client != null && client.isConnected()) {

            logger.debug("Publishing to topic \"" + topicName + "\" qos " + qos);

            // Create and configure a message
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);

            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                logger.info("topic = " + topicName + "- msg = " + message);
                client.publish(topicName, message);
                flag = true;
            } catch (MqttException e) {

            }

        }

        return flag;
    }

    public boolean publishMsg(MessageDto messageDto, int receiverId) {
        try {
            String json = mapper.writeValueAsString(messageDto);
            return publish("/dangxia/app/msg/" + receiverId, 0, json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

//    /**
//     * 使用默认信息进行mqtt主题注册
//     * 目前为根据用户id注册对应的data、info主题
//     */
//    public boolean subscribe() {
//        return subscribe(getMsgTopic(),0);
//    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    public boolean subscribe(String topicName, int qos) {

        boolean flag = false;

        if (client != null && client.isConnected()) {
            // Subscribe to the requested topic
            // The QoS specified is the maximum level that messages will be sent to the client at.
            // For instance if QoS 1 is specified, any messages originally published at QoS 2 will
            // be downgraded to 1 when delivering to the client but messages published at 1 and 0
            // will be received at the same level they were published at.
            logger.info("Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {

            }
        }

        return flag;

    }

    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    /**
     * 重新连接
     */
    public void reConnect() throws MqttException {
        disConnect();
        creatConnect();
    }

    /**
     * 是否连接
     */
    public boolean isConnecting() {
        if (client != null) {
            return client.isConnected();
        }
        return false;
    }
}
