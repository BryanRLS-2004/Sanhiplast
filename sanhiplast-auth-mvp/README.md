# SanHi Plast - MVP Auth microservice

Microservicio de autenticacion (RF01, RF02, RF11) implementado en Spring Boot 3,
con Spring Security + JWT + BCrypt, sobre PostgreSQL (esquema `auth`).

## 1. Levantar la base de datos

Necesitas Docker Desktop (o Docker Engine) corriendo. Desde la carpeta `sanhiplast/`:

```bash
docker compose up -d
```

Esto crea un contenedor Postgres en el puerto 5432 con la base `sanhiplast`,
el usuario `sanhi_app` / password `SanHi2026!`, y los 3 esquemas del proyecto
(`auth`, `inventory`, `sales`).

Si ya tienes Postgres corriendo localmente en el 5432 y prefieres usarlo,
crea manualmente la base/usuario/esquema con esos mismos datos (ver
`init-db/01-schemas.sql`) y ajusta `auth-service/src/main/resources/application.yml`
si usas otras credenciales.

## 2. Levantar el microservicio

Desde la carpeta `sanhiplast/auth-service/`:

```bash
mvn spring-boot:run
```

La primera vez Maven va a descargar las dependencias de Spring Boot (necesita
internet). Cuando veas en el log algo como `Started AuthServiceApplication in
X seconds`, el servicio esta arriba en `http://localhost:8081`.

Al arrancar, el `DataSeeder` crea automaticamente los roles `ADMINISTRADOR` y
`VENDEDOR` en la tabla `auth.rol`.

## 3. Correr las pruebas de evidencia

En OTRA terminal (dejando el servicio corriendo), desde
`sanhiplast/auth-service/evidencia/`:

```bash
bash run-tests.sh | tee evidencia_auth.txt
```

Esto ejecuta 9 casos reales contra el servicio: registro de usuarios, login
correcto, login con password incorrecta, acceso a endpoint protegido con y sin
token, autorizacion por rol (admin vs vendedor), y medicion de tiempo de
respuesta.

## 4. Enviar evidencia

Pega aqui en el chat el contenido completo de `evidencia_auth.txt` (o adjunta
el archivo). Si puedes, toma tambien 1-2 capturas de pantalla de la terminal
con el servicio corriendo y de una respuesta en Postman/Insomnia/curl -- eso
sirve como evidencia visual para el Capitulo V del informe.

## 5. Subir a GitHub

```bash
cd sanhiplast
git init
git add .
git commit -m "feat: MVP auth-service (RF01, RF02, RF11) con JWT y BCrypt"
git branch -M main
git remote add origin <URL-DE-TU-REPO>
git push -u origin main
```

Pasame el link del repo para incluirlo en el Capitulo V.2 (Gestion del codigo
fuente) y en el Anexo 16.

## Estructura del proyecto

```
sanhiplast/
├── docker-compose.yml       # Postgres local (3 esquemas)
├── init-db/01-schemas.sql
└── auth-service/             # Microservicio Auth (Spring Boot)
    ├── pom.xml
    ├── src/main/java/pe/sanhiplast/auth/
    │   ├── AuthServiceApplication.java
    │   ├── config/            # SecurityConfig, JwtAuthFilter, DataSeeder
    │   ├── controller/         # AuthController, GlobalExceptionHandler
    │   ├── domain/             # Persona, Usuario, Rol (JPA)
    │   ├── dto/                 # LoginRequest/Response, RegisterRequest, UsuarioResponse
    │   ├── repository/          # Spring Data JPA
    │   └── security/            # JwtService
    ├── src/main/resources/application.yml
    └── evidencia/run-tests.sh
```
