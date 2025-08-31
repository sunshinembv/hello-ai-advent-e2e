#!/usr/bin/env bash
set -euo pipefail
NEW_VER="${1:-}"
[ -n "$NEW_VER" ] || { echo "Usage: $0 <versionName>"; exit 1; }

PROP_FILE="gradle.properties"
[ -f "$PROP_FILE" ] || touch "$PROP_FILE"

# Читаем текущий VERSION_CODE (по умолчанию 1)
CUR_CODE="$(grep -E '^VERSION_CODE=' "$PROP_FILE" | cut -d= -f2 || true)"
if [[ -z "$CUR_CODE" || ! "$CUR_CODE" =~ ^[0-9]+$ ]]; then CUR_CODE=0; fi
NEXT_CODE=$((CUR_CODE+1))

# Обновляем/добавляем свойства
if grep -q '^VERSION_NAME=' "$PROP_FILE"; then
  sed -i.bak -E "s/^VERSION_NAME=.*/VERSION_NAME=${NEW_VER}/" "$PROP_FILE"
else
  echo "VERSION_NAME=${NEW_VER}" >> "$PROP_FILE"
fi

if grep -q '^VERSION_CODE=' "$PROP_FILE"; then
  sed -i.bak -E "s/^VERSION_CODE=.*/VERSION_CODE=${NEXT_CODE}/" "$PROP_FILE"
else
  echo "VERSION_CODE=${NEXT_CODE}" >> "$PROP_FILE"
fi

echo "Set VERSION_NAME=${NEW_VER}, VERSION_CODE=${NEXT_CODE}"