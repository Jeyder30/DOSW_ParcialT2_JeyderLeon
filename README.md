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

## Punto 11

- Red (falla): Se escribe primero un test que describa el comportamiento deseado (por ejemplo “crea un pedido cuando hay stock”). Se ejecut la suite y ves que falla porque aún no hay implementación.
- Green (pasa): Se implementa la mínima lógica necesaria para que ese test pase (crear entidad Order, guardar ítems, devolver id). Ejecutamos y confirmamos que el test ahora pasa.
- Refactor (mejora): Se limpia el código sin romper tests: Se extraen métodos, mejoras nombres, mueves validaciones a servicios. Volvemos a correr todos los tests para asegurarnos de que todo sigue verde.