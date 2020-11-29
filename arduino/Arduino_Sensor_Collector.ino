
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <ArduinoJson.h>

String message = "";
bool messageReady = false;
const long timeUntilNextReading = 120000;

Adafruit_MLX90614 mlx = Adafruit_MLX90614();

const int sensorPin[] = {A0};
double distance[1];
const int AVERAGE_OF =50;
const float MCU_VOLTAGE = 5.0;


void sendBrewData();
double readDistance(double);


void setup(){
  Serial.begin(9600); 
  mlx.begin(); 

  delay(20000);
}

void loop(){
   sendBrewData();
   delay(timeUntilNextReading);
}


void sendBrewData(){
    DynamicJsonDocument doc(1024);
    
    doc["type"] = "response";
    // Get data from analog sensors
    doc["distance"] = readDistance(0);
    doc["tempOfLiquid"] = mlx.readObjectTempF();
    serializeJson(doc,Serial);
}

double readDistance(int sensor){ //Read Distance
      double voltage_temp_average=0;   
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
