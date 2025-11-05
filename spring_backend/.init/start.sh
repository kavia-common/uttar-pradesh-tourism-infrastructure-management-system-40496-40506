#!/usr/bin/env bash
set -euo pipefail
WORKSPACE="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-management-system-40496-40506/spring_backend"
cd "$WORKSPACE"
# source persisted env if available
[ -f /etc/profile.d/spring_backend_env.sh ] && source /etc/profile.d/spring_backend_env.sh || true
: "${SERVER_PORT:=8080}"
: "${SPRING_DATASOURCE_URL:=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1}"
: "${SPRING_PROFILES_ACTIVE:=dev}"
# check port free
if ss -ltn 2>/dev/null | grep -q ":${SERVER_PORT} "; then echo "port ${SERVER_PORT} in use" >&2; exit 2; fi
# locate jar (deterministic finalName)
JAR="$WORKSPACE/target/spring-backend-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR" ]; then JAR=$(ls -1 target/*.jar 2>/dev/null | grep -v 'original' | head -n1 || true); fi
[ -f "$JAR" ] || { echo "jar not found" >&2; exit 3; }
mkdir -p target
JAVA_OPTS="-Dspring.datasource.url=${SPRING_DATASOURCE_URL} -Dserver.port=${SERVER_PORT} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Xmx512m"
setsid sh -c "exec java $JAVA_OPTS -jar '$JAR'" > target/app.log 2>&1 &
APP_PID=$!
echo "$APP_PID" > target/app.pid
sleep 1
# record pgid
PGID=$(ps -o pgid= -p "$APP_PID" | tr -d ' ')
echo "$PGID" > target/app.pgid || true
