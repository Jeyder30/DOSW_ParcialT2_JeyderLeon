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

# Punto 10

## Indice único sobre el código QR de productos

La búsqueda por QR es la operación más frecuente al escanear; este índice evita escaneos totales de tabla y devuelve el producto en O(log n). Al ser único también garantiza integridad del identificador QR. Tiene beneficio alto en latencia de lectura.

## Índice compuesto para localizar el pedido activo por usuario

Las comprobaciones “¿tiene el usuario un pedido activo?” y las consultas que filtran por usuario y estado (CREADO/EN_PREPARACION) serán muy rápidas porque el índice cubre el filtro; o hace ligeramente para devolver fecha sin ir a la fila completa. Esto reduce contención y latencia en el flujo de creación/validación de pedidos. Tiene beneficio porque implementa mejora significativa en rutas críticas del negocio.