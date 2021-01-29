echo "\n>>>>>>>>>> Compilando app <<<<<<<<<<\n"
mvn clean package 
echo "\n>>>>>>>>>> Deteniendo contenedor <<<<<<<<<<\n"
docker container stop ip_trace_container
echo "\n>>>>>>>>>> Generando imagen <<<<<<<<<<\n"
docker build -t meli-ip-trace-be .   
echo "\n>>>>>>>>>> Copiando imagen <<<<<<<<<<\n"
docker save meli-ip-trace-be -o meli-ip-trace-be
echo "\n>>>>>>>>>> Imagen copiada <<<<<<<<<<\n"
echo "\n>>>>>>>>>> Creando contenedor por primera vez <<<<<<<<<<\n"
docker run -dp 8080:8080 --name ip_trace_container meli-ip-trace-be || echo "\n>>>>>>>>>> Iniciando contenedor <<<<<<<<<<\n"; docker container start ip_trace_container