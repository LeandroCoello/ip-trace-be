# IP Trace Backend 

IP Trace Backend es un servicio web que de acuerdo a una IP obtiene información del país de origen. 
Está desarrollado con Java 8 y Maven superior a 3.5.0.

# Entorno Local

Utilizando el plugin de App Engine para maven:

```bash
mvn package appengine:run
```

Compilando y ejecutando el archivo .war

```bash
mvn clean package 
java -jar target/meli-ip-trace-be.war
```

Utilizando Docker

```bash
sh build-run-image.sh
```

# APIs Utilizadas

<b>IP2Country:</b><br>
<i>Sitio: https://ip2country.info/<br>
Api: https://api.ip2country.info/ip<br></i>

<b>Rest Countries:</b><br>
<i>Sitio: http://restcountries.eu/<br>
Api: https://restcountries.eu/rest/v2/alpha/</i><br>

<b>Exchange Rate:</b><br>
<i>Sitio: https://www.exchangerate-api.com/<br>
Api: https://v6.exchangerate-api.com/v6/</i><br>
