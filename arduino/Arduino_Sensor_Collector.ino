
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <ArduinoJson.h>

String message = "";
bool messageReady = false;
const int timeUntilNextReading = 30000;

Adafruit_MLX90614 mlx = Adafruit_MLX90614();

const int sensorPin[] = {A0};
float distance[1];
const int AVERAGE_OF =50;
const float MCU_VOLTAGE = 5.0;
float initial = 0;
double sg;
bool isSetup = false;

float convertData(float);
void sendBrewData();
float readDistance(float);


void setup(){
  Serial.begin(9600); 
  mlx.begin(); 
  
  //Obtain SG from NodeMCU before starting
  while(!isSetup){
	getSG();
  }
}

void loop(){
   sendBrewData();
   delay(timeUntilNextReading);
}

void getSG(){
	
	// busy spin until response is received 
	while(messageReady == false) { 
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
	
	//set sg if the right message has been received
	if(doc["type"] == setup){
		sg = doc["initSG"];
		isSetup = true;
	}

}

void sendBrewData(){
  float temperatureValue = mlx.readObjectTempC();
  float delta ;
  DynamicJsonDocument doc(1024);
  
  if(initial == 0){
    initial = readDistance(0);
    delta = initial;
    }
   else {
        delta = initial - readDistance(0);
        initial -= delta;
    }
    
    doc["type"] = "response";
    // Get data from analog sensors
    doc["specificGravity"] = convertData(delta);
    doc["tempOfLiquid"] = temperatureValue;
    serializeJson(doc,Serial);
    
}

float readDistance(int sensor){ //Read Distance
      float voltage_temp_average=0;   
      for(int i=0; i < AVERAGE_OF; i++)
    {
      int sensorValue = analogRead(sensorPin[sensor] );
      delay(1);      
      voltage_temp_average +=sensorValue * (MCU_VOLTAGE / 1023.0);
    }
     voltage_temp_average /= AVERAGE_OF;

  // eqution of the fitting curve
  ////33.9 + -69.5x + 62.3x^2 + -25.4x^3 + 3.83x^4
  distance[sensor] = 33.9 + -69.5*(voltage_temp_average) + 62.3*pow(voltage_temp_average,2) + -25.4*pow(voltage_temp_average,3) + 3.83*pow(voltage_temp_average,4);
  return distance[sensor];
}

float convertData(float delta){ // convert delta to SG
   float sg_value;
   sg_value = 0.01*delta + 1;
   return sg_value;
}


