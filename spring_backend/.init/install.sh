#!/usr/bin/env bash
set -euo pipefail
WORKSPACE="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-management-system-40496-40506/spring_backend"
export DEBIAN_FRONTEND=noninteractive
mkdir -p "$WORKSPACE"
# Ensure en_US.UTF-8 locale exists
if ! locale -a 2>/dev/null | tr '[:upper:]' '[:lower:]' | grep -q '^en_us.utf-8$'; then
  sudo apt-get update -q >/dev/null
  sudo apt-get install -y -q locales --no-install-recommends >/dev/null
  sudo locale-gen en_US.UTF-8 >/dev/null || true
  sudo update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8 >/dev/null || true
fi
# Determine packages to install
PKGS=()
if ! command -v java >/dev/null 2>&1; then PKGS+=(openjdk-17-jdk); fi
# Maven: if present ensure version >=3.8.6
MAVEN_OK=0
if command -v mvn >/dev/null 2>&1; then
  MVN_VER=$(mvn -v 2>/dev/null | sed -n 's/Apache Maven \([0-9.]*\).*/\1/p' | head -n1 || true)
  if [ -n "$MVN_VER" ]; then
    if dpkg --compare-versions "$MVN_VER" ge "3.8.6" 2>/dev/null; then MAVEN_OK=1; fi
  fi
fi
if [ "$MAVEN_OK" -ne 1 ]; then PKGS+=(maven); fi
if ! command -v jq >/dev/null 2>&1; then PKGS+=(jq); fi
if [ ${#PKGS[@]} -ne 0 ]; then
  sudo apt-get update -q >/dev/null
  sudo apt-get install -y -q --no-install-recommends "${PKGS[@]}" >/dev/null
fi
# Validate java exists and major version >=17
if ! command -v java >/dev/null 2>&1; then echo "java not available" >&2; exit 11; fi
# Parse major version robustly for OpenJDK and other distributions
JAVA_FULL_VER=$(java -XshowSettings:properties -version 2>&1 | tr -d '\r' || true)
# Fallback parsing
JAVA_MAJOR=$(echo "$JAVA_FULL_VER" | sed -n 's/.*version "\([0-9][0-9]*\).*"/\1/p' | head -n1 || true)
if [ -z "$JAVA_MAJOR" ]; then
  # Try alternative: first number in version output
  JAVA_MAJOR=$(echo "$JAVA_FULL_VER" | sed -n 's/.*version "\([0-9][0-9]*\)\..*/\1/p' | head -n1 || true)
fi
if [ -z "$JAVA_MAJOR" ] || ! printf "%s" "$JAVA_MAJOR" | grep -qE '^[0-9]+$'; then
  echo "Unable to determine java major version from: $JAVA_FULL_VER" >&2
  exit 12
fi
if [ "${JAVA_MAJOR:-0}" -lt 17 ]; then
  echo "java major version <17 detected: $JAVA_MAJOR" >&2
  exit 12
fi
# Determine JAVA_HOME
JAVA_HOME=""
if command -v update-java-alternatives >/dev/null 2>&1; then
  JAVA_HOME=$(readlink -f "$(dirname "$(readlink -f "$(command -v java)")")/.." ) || true
fi
if [ -z "$JAVA_HOME" ] && [ -d /usr/lib/jvm ]; then
  JAVA_HOME=$(ls -d /usr/lib/jvm/java-*-openjdk* 2>/dev/null | head -n1 || true)
fi
JAVA_HOME=${JAVA_HOME:-}
# Persist environment to /etc/profile.d atomically
TMPFILE=$(mktemp)
cat > "$TMPFILE" <<'PROFILE'
export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"
export SPRING_PROFILES_ACTIVE="dev"
export SPRING_DATASOURCE_URL="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
export SERVER_PORT="8080"
PROFILE
if [ -n "$JAVA_HOME" ]; then
  echo "export JAVA_HOME=\"$JAVA_HOME\"" >> "$TMPFILE"
  echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> "$TMPFILE"
fi
sudo mv "$TMPFILE" /etc/profile.d/spring_backend_env.sh
sudo chmod 644 /etc/profile.d/spring_backend_env.sh
# Source for immediate use (do not fail if not readable)
# shellcheck disable=SC1091
source /etc/profile.d/spring_backend_env.sh >/dev/null 2>&1 || true
# Final validations
command -v mvn >/dev/null 2>&1 || { echo "mvn not available" >&2; exit 13; }
command -v jq >/dev/null 2>&1 || { echo "jq not available" >&2; exit 14; }
# Output versions
mvn -v | sed -n '1,2p' || true
java -version 2>&1 | sed -n '1,3p' || true
jq --version || true
