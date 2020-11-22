
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <ArduinoJson.h>

String message = "";
bool messageReady = false;
const int timeUntilNextReading = 30000;

Adafruit_MLX90614 mlx = Adafruit_MLX90614();

const int sensorPin[] = {A0};
double distance[1];
const int AVERAGE_OF =50;
const float MCU_VOLTAGE = 5.0;
double initial = 0;
double offset;
double sg;
bool isSetup = false;

doubel convertData(doubel);
void sendBrewData();
doubel readDistance(doubel);


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
  doubel temperatureValue = mlx.readObjectTempF();
  doubel delta ;
  DynamicJsonDocument doc(1024);
  
	if(initial == 0){
		double first = readDistance(0);
		offset = sg - (0.01*first);
		inital = first;
    }
   else {
	    //As the hydrometer sinks, it move away from the sensor, thus readDistance > inital 
        delta = readDistance(0) - inital;
        initial -= delta;
    }
    
    doc["type"] = "response";
    // Get data from analog sensors
    doc["specificGravity"] = temperatureCorrectionSG(initial, temperatureValue);
    doc["tempOfLiquid"] = fahrenheitToCel(temperatureValue)
    serializeJson(doc,Serial);
    
}

double readDistance(int sensor){ //Read Distance
      doubel voltage_temp_average=0;   
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

doubel convertData(doubel delta){ // convert delta to SG
   doubel sg_value;
   sg_value = 0.01 * delta + offset;
   return sg_value;
}

double temperatureCorrectionSG(double delta, double mesuredTemp){
	
	double measuredGravity = convertData(delta);
	double tempeartureAtReading = mesuredTemp; //Fahrenheit from sensor
	double calibrationTemperature = 60 ; //In Fahrenheit, value for our hydrometer
	
	return measuredGravity * ((1.00130346 - 0.000134722124 * tempeartureAtReading + 0.00000204052596 * tempeartureAtReading - 0.00000000232820948 * tempeartureAtReading) / (1.00130346 - 0.000134722124 * calibrationTemperature + 0.00000204052596 * calibrationTemperature - 0.00000000232820948 * calibrationTemperature));
 
 }

double fahrenheitToCel(double tempF) {
	return (tempF - 32)/ 0.556 ; // temperature from fahrenheit to celcius
 }


