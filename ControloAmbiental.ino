#include <ArduinoJson.h>
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#define USE_SERIAL Serial
#include "DHT.h"
#define DHTPIN D2
#define DHTTYPE DHT22
float temperatura, humidade, temperaturaMinima, temperaturaMaxima;


int redLed = D0, greenLed = D1;
int localId=2;
long tempoInicial, tempoEspera;
String servidor = "http://185.203.118.18:8080";

ESP8266WiFiMulti WiFiMulti;
DHT dht(DHTPIN, DHTTYPE);

void setup() {

  pinMode(redLed, OUTPUT);
  pinMode(greenLed, OUTPUT);
  USE_SERIAL.begin(115200);
  USE_SERIAL.println();
  USE_SERIAL.println();
  USE_SERIAL.println();

  for (uint8_t t = 4; t > 0; t--) {
    USE_SERIAL.printf("[SETUP] WAIT %d...\n", t);
    USE_SERIAL.flush();
    delay(1000);
  }

  //WiFiMulti.addAP("Vodafone-654BFB", "kuu8vaXaKG");
 // WiFiMulti.addAP("Thomson73050F", "179F1106D2");
  WiFiMulti.addAP("IPG-Free", "");
  Serial.println("DHTxx test!");
  dht.begin();
}

void loop() {

  delay(2000);
  humidade = dht.readHumidity();
  temperatura = dht.readTemperature();

  if (isnan(humidade) || isnan(temperatura) ) {
    Serial.println("Falha na leitura do sensor DHT!");
    return;
  }

  getConfiguracoes();

  if ( temperatura > temperaturaMinima && temperatura < temperaturaMaxima) {
    analogWrite(greenLed, HIGH);
    analogWrite(redLed, LOW);
    
  } else {
    analogWrite(redLed, HIGH);
    analogWrite(greenLed, LOW);
  }

  

  if (millis() > tempoInicial + tempoEspera) {
    tempoInicial = millis();
    enviaTemperatura();

  }


}
void getConfiguracoes() {
  if ((WiFiMulti.run() == WL_CONNECTED)) {

    HTTPClient http;

    USE_SERIAL.print("[HTTP] begin...\n");
    
    http.begin(servidor + "/ControloAmbiental/webresources/pt.ipg.locais/" + String(localId)); //HTTP

    USE_SERIAL.print("[HTTP] GET...\n");
    // Envio do cabeçalho e começo da ligação
    http.addHeader("Content-Type", "application/json");
    http.addHeader("Accept", "application/json");
    int httpCode = http.GET();

    // httpCode vai ser negativo se retornar algum erro
    if (httpCode > 0) {
      // HTTP header has been send and Server response header has been handled
      USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

      
      if (httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        USE_SERIAL.println(payload);
        StaticJsonBuffer<300> jsonBuffer;
        JsonObject& root = jsonBuffer.parseObject(payload);
        tempoEspera = root["frequencia"];
        temperaturaMinima = root["temperaturaMinima"];
        temperaturaMaxima = root["temperaturaMaxima"];
        USE_SERIAL.println("temperatura minima: " +  String(temperaturaMinima) + "tempoEspera: " + String(tempoEspera));
      }
    } else {
      USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }

}


//  delay(50000);
void enviaTemperatura() {
  if ((WiFiMulti.run() == WL_CONNECTED)) {
    HTTPClient http;
    http.begin("http://185.203.118.18:8080/ControloAmbiental/webresources/pt.ipg.meteorologia/"); //HTTP
    USE_SERIAL.print("[HTTP] POST...\n");
    http.addHeader("Content-Type", "application/json");
    int httpCode =  http.POST("{\"temperatura\":" + String(temperatura) + ",\"humidade\":" + String(humidade) + ",\"local\":{\"id\":"+String(localId) +"}}");
    http.writeToStream(&Serial);
    http.end();

    if (httpCode > 0) {
      USE_SERIAL.printf("[HTTP] POST... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        USE_SERIAL.println(payload);
      }
    } else {
      USE_SERIAL.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }


}

