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

## Punto 7

Validador

- Qué es: componente que verifica que los datos cumplen reglas concretas (formatos, rangos, campos obligatorios).
- Responsabilidad: validar entrada y devolver errores claros; no realiza lógica de negocio ni efectos secundarios.
- Dónde se usa: capas de entrada (controladores, DTOs) o antes de ejecutar una operación de negocio.
- Ejemplo: comprobar que un email tenga formato válido o que una cantidad sea positiva.
- Prueba: tests unitarios centrados en reglas de validación.


Utilidad (helper)

- Qué es: función o clase pequeña que aporta una operación reutilizable y técnica, sin estado ni contexto de dominio.
- Responsabilidad: resolver tareas auxiliares (formatos, conversiones, cálculos sencillos) de forma pura y reutilizable.
- Dónde se usa: en cualquier capa que necesite la operación; no debería contener lógica de negocio.
- Ejemplo: formatear una fecha, calcular el hash de un string, convertir moneda.
- Prueba: tests unitarios que verifiquen entradas/ salidas para casos representativos.

Servicio

- Qué es: componente que encapsula la lógica de negocio, coordina validadores, utilidades y repositorios.
- Responsabilidad: implementar las reglas del dominio, gestionar transacciones y efectos secundarios (persistencia, envío de eventos).
- Dónde se usa: desde controladores o jobs; actúa como capa intermedia entre entrada y persistencia.
- Ejemplo: crear un pedido (verificar stock, reservar ítems, guardar el pedido y emitir evento).
- Prueba: tests unitarios sobre la lógica (mockeando repositorios) y pruebas de integración para efectos secundarios.