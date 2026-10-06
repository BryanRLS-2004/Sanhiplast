#!/usr/bin/env bash
# Script de evidencia - RF01, RF02, RF11 (Auth microservice)
# Ejecutar con el servicio corriendo en http://localhost:8081
# Uso:  bash run-tests.sh | tee evidencia_auth.txt
set -e
BASE="http://localhost:8081/api/auth"

echo "========================================================"
echo "CASO 1 - Registrar usuario ADMINISTRADOR (Airton)"
echo "========================================================"
curl -s -i -X POST "$BASE/register" -H "Content-Type: application/json" -d '{
  "nombre": "Airton",
  "apellido": "Rodriguez Chacaliaza",
  "telefono": "987654321",
  "direccion": "Pisco, Peru",
  "username": "airton@sanhiplast.com",
  "password": "Admin123!",
  "rol": "ADMINISTRADOR"
}'
echo -e "\n"

echo "========================================================"
echo "CASO 2 - Registrar usuario VENDEDOR (Miguel)"
echo "========================================================"
curl -s -i -X POST "$BASE/register" -H "Content-Type: application/json" -d '{
  "nombre": "Miguel",
  "apellido": "Perez Gihua",
  "telefono": "912345678",
  "direccion": "Pisco, Peru",
  "username": "miguel@sanhiplast.com",
  "password": "Vendedor123!",
  "rol": "VENDEDOR"
}'
echo -e "\n"

echo "========================================================"
echo "CASO 3 - Login correcto (RF01) - administrador"
echo "========================================================"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE/login" -H "Content-Type: application/json" -d '{
  "username": "airton@sanhiplast.com",
  "password": "Admin123!"
}')
echo "$LOGIN_RESPONSE"
TOKEN_ADMIN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo -e "\nToken admin obtenido: ${TOKEN_ADMIN:0:40}...\n"

echo "========================================================"
echo "CASO 4 - Login con contrasena incorrecta (debe fallar con 401)"
echo "========================================================"
curl -s -i -X POST "$BASE/login" -H "Content-Type: application/json" -d '{
  "username": "airton@sanhiplast.com",
  "password": "clave-incorrecta"
}'
echo -e "\n"

echo "========================================================"
echo "CASO 5 - Acceso a endpoint protegido /me SIN token (debe fallar con 401/403)"
echo "========================================================"
curl -s -i -X GET "$BASE/me"
echo -e "\n"

echo "========================================================"
echo "CASO 6 - Acceso a endpoint protegido /me CON token valido (debe responder 200)"
echo "========================================================"
curl -s -i -X GET "$BASE/me" -H "Authorization: Bearer $TOKEN_ADMIN"
echo -e "\n"

echo "========================================================"
echo "CASO 7 - Login vendedor y acceso a endpoint SOLO ADMIN (debe fallar con 403 - RF02)"
echo "========================================================"
LOGIN_VENDEDOR=$(curl -s -X POST "$BASE/login" -H "Content-Type: application/json" -d '{
  "username": "miguel@sanhiplast.com",
  "password": "Vendedor123!"
}')
echo "$LOGIN_VENDEDOR"
TOKEN_VENDEDOR=$(echo "$LOGIN_VENDEDOR" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo -e "\n--- Vendedor intentando acceder a /admin/ping ---"
curl -s -i -X GET "$BASE/admin/ping" -H "Authorization: Bearer $TOKEN_VENDEDOR"
echo -e "\n"

echo "========================================================"
echo "CASO 8 - Administrador accediendo a /admin/ping (debe responder 200 - RF02)"
echo "========================================================"
curl -s -i -X GET "$BASE/admin/ping" -H "Authorization: Bearer $TOKEN_ADMIN"
echo -e "\n"

echo "========================================================"
echo "CASO 9 - Medicion de tiempo de respuesta (KPI01: <= 2s)"
echo "========================================================"
curl -s -o /dev/null -w "Tiempo total login: %{time_total}s\n" -X POST "$BASE/login" -H "Content-Type: application/json" -d '{
  "username": "airton@sanhiplast.com",
  "password": "Admin123!"
}'

echo -e "\nFIN DE PRUEBAS"
