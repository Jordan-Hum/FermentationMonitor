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

const String DEVICE_ID = "testDevice";
const long utcOffsetInSeconds = -18000;
double initial = 0;
double offset;
double sg;
bool isSetup = false;

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);

FirebaseData firebaseData;

boolean messageReady = false;
String message = "";
String devicePath = "/Devices/" + DEVICE_ID;
String batchId = "";


double convertData(double );
double temperatureCorrectionSG(double , double );
double fahrenheitToCel(double );


void sendRequest();
void extractData(DynamicJsonDocument);
void sendData(double,double);
void fetchSG();

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
    //Serial.print(".");
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
  
  
  while(!isSetup){
	  fetchSG();
  }
  
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
    extractData(doc);
    messageReady = false;
  }
}

void fetchSG(){
	
	DynamicJsonDocument doc(1024);
	
	//Get BatchID for userPath
	if (Firebase.getString(firebaseData,devicePath+"/currentBatchId"))
	{
		batchId = firebaseData.stringData();
	}
	else
	{
		Serial.println("FAILED");
		Serial.println("REASON: " + firebaseData.errorReason());
		Serial.println("------------------------------------");
		Serial.println();
		return;
    }

	String dataPath = "/SensorData/"+batchId+"/idealSg";
  
	if (Firebase.getString(firebaseData,dataPath)) {
		sg = firebaseData.stringData().toDouble();
	}
	else
	{
		Serial.println("FAILED");
		Serial.println("REASON: " + firebaseData.errorReason());
		Serial.println("------------------------------------");
		Serial.println();
		return;
	}

  Serial.println(sg);
  isSetup = true;
}

void extractData(DynamicJsonDocument res){
  
  double dis = res["distance"];
  double temp = res["tempOfLiquid"];

  if(initial == 0){
    offset = sg + (0.0100 * dis);
    Serial.println(offset);
  }
  
  initial = dis;

  Serial.println("Initial: "+ String(initial));
  sendData(initial, temp);
}

void sendData(double dist, double temp){
  
  FirebaseJson json1;

  timeClient.update();
  String FormattedDate = timeClient.getFormattedDate();
  int splitT = FormattedDate.indexOf("T");
  String dayStamp = FormattedDate.substring(0, splitT);
  String timeStamp = FormattedDate.substring(splitT+1, FormattedDate.length()-1);

   json1.set("specificGravity", String(temperatureCorrectionSG(dist,temp)));
   json1.set("tempOfLiquid", String(fahrenheitToCel(temp)));
   json1.set("time", timeStamp);
   json1.set("date", dayStamp);
  

  //Get BatchID for userPath
  if (Firebase.getString(firebaseData,devicePath+"/currentBatchId"))
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

double convertData(double dist){ // convert delta to SG
   double sg_value;
   sg_value = (-0.0100 * dist) + offset;
   return sg_value;
}

double temperatureCorrectionSG(double dist, double mesuredTemp){
  
  Serial.println("distance: " + String(dist));
  double measuredGravity = convertData(dist);
  Serial.println("measured SG:" + String(measuredGravity));
  double tempeartureAtReading = mesuredTemp; //Fahrenheit from sensor
  double calibrationTemperature = 60 ; //In Fahrenheit, value for our hydrometer
  
  double correctedSG =  measuredGravity * ((1.00130346 - 0.000134722124 * tempeartureAtReading + 0.00000204052596 * tempeartureAtReading - 0.00000000232820948 * tempeartureAtReading) / (1.00130346 - 0.000134722124 * calibrationTemperature + 0.00000204052596 * calibrationTemperature - 0.00000000232820948 * calibrationTemperature));
  Serial.println(correctedSG);
  return correctedSG;
 }

double fahrenheitToCel(double tempF) {
  return (tempF - 32)/ 0.556 ; // temperature from fahrenheit to celcius
 }
