FROM openjdk:8u151

# Usuario para java, se evita root
RUN groupadd -r java && useradd --no-log-init -r -g java java
USER java

# Copia del ejecutable
COPY target/*.jar /opt/app.jar

# Puerto a exponer
EXPOSE 8443

# Ejecutamos la aplicación al iniciar el contenedor
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/app.jar"]