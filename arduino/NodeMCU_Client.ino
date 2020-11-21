//
// Code Based off: https://github.com/mobizt/Firebase-ESP8266/blob/master/examples/jsonObject/jsonObject.ino
//
// Modified by Group Five for the COEN390 Project November 2020
//
// Important: Remember to include esp8266_secrets inside the same directory as this code !
//
#include <FirebaseESP8266.h>
#include <FirebaseESP8266HTTPClient.h>
#include <FirebaseFS.h>
#include <FirebaseJson.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include "esp8266_secrets.h"
#include <NTPClient.h>
#include <WiFiUdp.h>

const String USER_ID = "sampleUserId";
const long utcOffsetInSeconds = -18000;

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);

FirebaseData firebaseData;

boolean messageReady = false;
String message = "";

void sendRequest();
void sendData(DynamicJsonDocument);

void setup() {
  timeClient.begin();
  Serial.begin(9600);
  delay(100);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.print("Netmask: ");
  Serial.println(WiFi.subnetMask());
  Serial.print("Gateway: ");
  Serial.println(WiFi.gatewayIP());

  Serial.println("Connecting to Firebase");
  //Set data to firebase here
  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
  
  Serial.println("");
}

void loop(){
	sendRequest();
}

void sendRequest(){
  DynamicJsonDocument doc(1024);
  
  while(messageReady == false) { // busy spin until response is received 
    if(Serial.available()) {
      message = Serial.readString();
      messageReady = true;
    }
  }
  // Attempt to deserialize the JSON-formatted message
  DeserializationError error = deserializeJson(doc,message);
  if(error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.c_str());
    return;
  }

  if(doc["type"] == "response"){ 
    sendData(doc);
    messageReady = false;
  }
}

void sendData(DynamicJsonDocument res){
  
  String userPath = "/Users/" + USER_ID;
  String batchId;
  FirebaseJson json1;

  timeClient.update();
  String FormattedDate = timeClient.getFormattedDate();
  
  float sg = res["specificGravity"];
  float temp = res["tempOfLiquid"];
  int splitT = FormattedDate.indexOf("T");
  String dayStamp = FormattedDate.substring(0, splitT);
  String timeStamp = FormattedDate.substring(splitT+1, FormattedDate.length()-1);

   json1.set("specificGravity", String(sg));
   json1.set("tempOfLiquid", String(temp));
   json1.set("time", timeStamp);
   json1.set("date", dayStamp);
  

  //Get BatchID for userPath
  if (Firebase.getString(firebaseData,userPath+"/currentBatchId"))
  {
      batchId = firebaseData.stringData();
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      Serial.println("------------------------------------");
      Serial.println();
    }


  String dataPath = "/SensorData/"+batchId+"/brewData";

  //Place brewData in the Correct Batch
  if (Firebase.pushJSON(firebaseData, dataPath, json1))
  {
      //Firebase.printResult(firebaseData);
  }
  else{
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      Serial.println("------------------------------------");
      Serial.println();
  }
}
