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

## Punto 12

Las pruebas convierten las reglas de negocio en comprobaciones automáticas: unit tests validan la lógica, integration tests verifican efectos sobre datos (transacciones) y prueban el flujo completo; ejecutadas en CI detectan regresiones y preservan la integridad del sistema.

