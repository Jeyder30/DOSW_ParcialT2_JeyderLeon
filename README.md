# DOSW_ParcialT2_JeyderLeon

# Jeyder Nicolay leon Lancheros
# GRUPO 1

![Draw.io](docs/images/Draw.png)
![Figma](docs/images/figma.png)
- Compilar proyecto: mvn clean compile
- Ejecutar tests (JUnit + Mockito): mvn test
- Ejecutar verificación completa (incluye JaCoCo report): mvn clean verify
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Ejecutar análisis SonarQube: mvn clean verify sonar:sonar 
- Dsonar.projectKey=DOSW-ParcialT2 
- Dsonar.host.url=http://localhost:9000 
- Dsonar.login=TU_TOKEN
- SonarQube UI: http://localhost:9000
- Levantar la app Spring Boot: mvn spring-boot:run
- App local: http://localhost:8080



# Punto 2

Input: comprobaciones tecnicas sobre los datos que llegan. 

Ejemplos: campos obligatorios, formato de email, tipos, longitudes, nuumeros positivos. Se hacen en la entrada (controlador/DTO) y evitan errores básicos y ataques.

Negocio: reglas propias del dominio que dependen del estado y la loggica de la app. 

Ejemplos: stock suficiente antes de confirmar un pedido, un usuario solo puede tener un pedido activo, solo el personal puede marcar ENTREGADO. Se validan en la capa de servicio/domain y protegen la coherencia del negocio para que vaya acorde a lo establecido previamente.

# Punto 3

Autenticación: comprobar quién es el usuario

Autorización: decidir qué puede hacer el usuario.

Integridad: asegurar que los datos no fueron alterados.