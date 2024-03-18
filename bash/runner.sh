#!/bin/bash

# Array de tipos de notificaciones
types=("status" "news" "marketing" "update" "invitation")

# Número de repeticiones para cada tipo de notificación
repetitions=10

# URL del endpoint para enviar notificaciones
endpoint="http://localhost:8080/sendNotification"

# Función para enviar una solicitud HTTP y medir el tiempo de respuesta
send_request() {
    local type=$1
    local start_time=$(($(date +%s%N) / 1000000)) # Hora de inicio en milisegundos

    # Realizar la solicitud HTTP y capturar la salida
    response=$(curl -s -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "{\"type\":\"$type\",\"userId\":\"user\",\"message\":\"Test message\"}" $endpoint)

    local end_time=$(($(date +%s%N) / 1000000)) # Hora de finalización en milisegundos
    local duration=$((end_time - start_time)) # Duración en milisegundos

    echo "Type: $type"
    echo "HTTP Response: ${response: -3}" # Últimos tres caracteres (código de estado HTTP)
    echo "Duration: $duration ms"
    echo ""
}

# Función para mostrar los datos en un gráfico
show_graph() {
    local data=$1
    echo "$data" | gnuplot -persist <<-EOFMarker
    set terminal dumb
    set title "Respon

