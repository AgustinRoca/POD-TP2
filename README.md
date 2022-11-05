# TP2Hazelcast
## Compilación
En la carpeta base hacer
```
mvn clean package
```
## Ejecución servidores

Dentro de la carpeta ```server/target``` descomprimir el .tg.gz y correr el script ```run-server.sh```

Este script posee las siguientes opciones:
* ```-Di=ipAddr```: Permite cambiar las interfaces de red. Las mismas tiene que estar separadas por ;
* ```-DhzPath```: Path del archivo hazelcast.xml para la configuración del cluster ;

## Ejecución de los clientes

Dentro de la carpeta ```client/target``` descomprimir el .tg.gz y correr cualquiera de los siguientes scripts con sus respectivos parámetros:
* ```query1```
* ```query2```
    * ```-Dmin=numero```: Número entero mayor a cero. El mínimo número de árboles 
* ```query3```
    * ```-Dn=numero```: Número entero mayor a cero. El número máximo de especies a mostrar
* ```query4```
    * ```-Dmin=numero```: Número entero mayor a cero. El mínimo número de árboles
    * ```-Dname=nombre```: La especie a buscar. Si contiene espacios es importante el uso de comillas
* ```query5```

También es importante agregar los parámetros generales a todos los scripts:
* ```-Dcity=BUE|VAN```: La ciudad sobre la cual se desean hacer las queries
* ```-Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY'```: Las direcciones IP de los nodos
* ```-DinPath```: Path de los archivos de entrada de los barrios. Este debe contener los siguientes archivos: arbolesBUE.csv, arbolesVAN.csv, barriosBUE.csv y barriosVAN.csv
* ```-DoutPath```: Path donde se generaran los archivos de salida
