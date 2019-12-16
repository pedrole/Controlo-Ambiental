# Controlo-Ambiental
Aplicação Android para controlar temperatura e humidade até dois locais. Esta app tem um sistema de alertas para quanda temperatura atinja um determinado valor. Sendo esse valor configurável pelo utilizador.

## Prints
<img src="https://github.com/pedrole/Controlo-Ambiental/blob/master-android/images/controlo-ambiental-1.png" width="250" />&nbsp;
<img src="https://github.com/pedrole/Controlo-Ambiental/blob/master-android/images/controlo-ambiental-2.png" />
<img src="https://github.com/pedrole/Controlo-Ambiental/blob/master-android/images/controlo-ambiental-3.png" />

## Pré Requisitos
* [Web Service Controlo Ambiental](https://github.com/pedrole/Controlo-Ambiental/tree/master-server) 
* [Código Arduino](https://github.com/pedrole/Controlo-Ambiental/tree/master-arduino) 

## Como Utilizar
* Ara o projeto, e modifique a variável NetworkUtils.SERVIDOR para o IP onde o [web service](https://github.com/pedrole/Controlo-Ambiental/tree/master-server) foi instalado, ex: http://localhost:8080
* Corra a app, e clique no icone da roda dentada num dos locais, para configurar o tempo de envio dos dados meteorologicos e a temperatra minima e máxima.
