#!/bin/bash

set -e

echo "=== File Analyzer Test Environment ==="

# -------- 1. Проверяем, что file_scrumbler.py существует --------

if [ ! -f "file_scrumbler.py" ]; then
  echo "ERROR: file_scrumbler.py not found in current directory!"
  exit 1
fi

# -------- 2. Проверяем, что JAR собран --------

JAR="target/test-bazalt-spo-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR" ]; then
  echo "ERROR: JAR file not found!"
  echo "Run: mvn package"
  exit 1
fi

# -------- 3. Создаём рабочие директории --------

BASE="/tmp/file_analyzer_demo"

ORIGINAL="$BASE/original"
DAMAGED="$BASE/damaged"

rm -rf "$BASE"
mkdir -p "$ORIGINAL" "$DAMAGED"

echo "Created test directories:"
echo "  ORIGINAL = $ORIGINAL"
echo "  DAMAGED  = $DAMAGED"

# -------- 4. Копируем /usr/bin в рабочие папки --------

echo "Copying /usr/bin -> ORIGINAL and DAMAGED..."
cp -r /usr/bin/* "$ORIGINAL" || true
cp -r /usr/bin/* "$DAMAGED" || true

echo "Copy complete."

# -------- 5. Применяем file_scrumbler.py к DAMAGED --------

echo "Scrambling files in DAMAGED directory..."
python3 file_scrumbler.py "$DAMAGED" \
  --percent 1 \
  --byte-percent 0.5 \
  --mode replace \
  --recursive

echo "Scrambling complete."

# -------- 6. Запускаем веб‑сервис --------

echo "Starting Java Web Service..."
echo "Use this in browser:"
echo "    ORIGINAL: $ORIGINAL"
echo "    DAMAGED : $DAMAGED"
echo
echo "Open: http://localhost:8080"
echo

java -jar "$JAR"
