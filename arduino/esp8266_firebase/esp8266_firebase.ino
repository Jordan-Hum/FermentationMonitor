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
#include <ESP8266WiFi.h>
#include "esp8266_secrets.h"

const String USER_ID = "sampleUserId";

FirebaseData firebaseData;

void sendData();

void setup() {
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

  Serial.println("Sending Data");
  sendData();
  
}


int value = 0;

void loop(){

}

void sendData(){
  
  String userPath = "/Users/" + USER_ID;
  String batchId;
  FirebaseJson json1;

   json1.set("specificGravity", 1.001);
   json1.set("tempOfLiquid", 23.0);
   json1.set("time", "13:00");
   json1.set("date", "01/01/2020");

  /*  
  brewData.set("specificGravity", 1.001);
  brewData.set("tempOfLiquid", 20.3);
  brewData.set("time", "13:00");
  brewData.set("date", "01/02/2020");
  */
  

  //Also can use Firebase.get instead of Firebase.setInt
  if (Firebase.getString(firebaseData,userPath+"/currentBatchId"))
  {
      Serial.println("PASSED");
      Serial.println("PATH: " + firebaseData.dataPath());
      Serial.println("TYPE: " + firebaseData.dataType());
      Serial.println("ETag: " + firebaseData.ETag());
      Serial.print("VALUE: ");
      batchId = firebaseData.stringData();
      Serial.println(batchId);
      Serial.println("------------------------------------");
      Serial.println();
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      Serial.println("------------------------------------");
      Serial.println();
    }


  String dataPath = "/SensorData/"+batchId+"/brewData";

  if (Firebase.pushJSON(firebaseData, dataPath, json1))
  {
      Serial.println("PASSED");
      Serial.println("PATH: " + firebaseData.dataPath());
      Serial.println("TYPE: " + firebaseData.dataType());
      Serial.println("ETag: " + firebaseData.ETag());
      Serial.print("VALUE: ");
      //Firebase.printResult(firebaseData);
      Serial.println("------------------------------------");
      Serial.println();
  }
  else{
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      Serial.println("------------------------------------");
      Serial.println();
  }
}
