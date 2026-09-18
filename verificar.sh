#!/usr/bin/env bash
set -u
PASS=0; TOTAL=5
echo "== S32: endurecer la conexion REST (5 compuertas) =="
command -v docker >/dev/null 2>&1 || { echo "[X] Docker no instalado"; exit 1; }
CF=""; for f in compose.yaml compose.yml docker-compose.yaml; do [ -f "$f" ] && CF="$f" && break; done
[ -n "$CF" ] || { echo "[X] No hay compose aqui"; exit 1; }
echo "-- (1) up --build..."
if docker compose up -d --build >/tmp/c16l.log 2>&1; then echo "[OK] (1) arriba"; PASS=$((PASS+1)); sleep 14; else echo "[X] (1) up fallo:"; tail -5 /tmp/c16l.log; fi
echo "-- (2) flujo base CAM-002..."; curl -sf http://localhost:8081/notificaciones 2>/dev/null | grep -qi "CAM-002" && { echo "[OK] (2)"; PASS=$((PASS+1)); } || echo "[X] (2) flujo base roto"
echo "-- (3) RESILIENCIA: notificaciones CAIDO, enviar NO debe fallar..."
docker compose stop servicio-notificaciones >/dev/null 2>&1; sleep 3
CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/solicitudes/INC-001/enviar 2>/dev/null)
[ "$CODE" = "200" ] && { echo "[OK] (3) enviar=200 con notificaciones caido (desacoplado)"; PASS=$((PASS+1)); } || echo "[X] (3) enviar devolvio $CODE: falta try/catch en PublicadorRest"
docker compose start servicio-notificaciones >/dev/null 2>&1; sleep 10
echo "-- (4) SEGUNDO EVENTO aprobar(CAM-002)..."
curl -s -X POST http://localhost:8080/solicitudes/CAM-002/aprobar >/dev/null 2>&1; sleep 2
curl -sf http://localhost:8081/notificaciones 2>/dev/null | grep -qi "aprob" && { echo "[OK] (4) SolicitudAprobada cruzo por REST"; PASS=$((PASS+1)); } || echo "[X] (4) falta el segundo evento de punta a punta"
echo "-- (5) por nombre via config..."; URLLINE=$(grep -iE 'NOTIFICACIONES_URL' "$CF" | sed 's/#.*//')
{ echo "$URLLINE" | grep -qiE 'servicio-notificaciones' && ! echo "$URLLINE" | grep -qiE 'localhost|127'; } && { echo "[OK] (5)"; PASS=$((PASS+1)); } || echo "[X] (5) usa el nombre de servicio"
docker compose down >/dev/null 2>&1
echo ""; echo "PUNTAJE: ${PASS}/${TOTAL}"; [ "$PASS" -eq "$TOTAL" ] && echo "Conexion endurecida: resiliente y con dos eventos." || echo "Aun no."
