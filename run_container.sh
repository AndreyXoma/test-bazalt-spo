#!/bin/bash

# --- 1. Создаём директории для проверки ---
HOST_DIR1="$(pwd)/test_bin_original"
HOST_DIR2="$(pwd)/test_bin"

mkdir -p "$HOST_DIR1" "$HOST_DIR2"

# --- 2. Добавляем тестовые файлы ---
echo "Hello World" > "$HOST_DIR1/file1.txt"
echo "Sample file" > "$HOST_DIR1/file2.txt"

echo "Hello World" > "$HOST_DIR2/file1.txt"       # совпадает
echo "Changed content" > "$HOST_DIR2/file2.txt"  # отличается

# --- 3. Проверяем jar на хосте ---
JAR_PATH="$(pwd)/target/test-bazalt-spo-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_PATH" ]; then
  echo "ERROR: JAR файл не найден в target/. Сборка должна быть выполнена до запуска скрипта."
  exit 1
fi

# --- 4. Запускаем контейнер Fedora ---
docker run -it \
  --rm \
  -p 8080:8080 \
  -v "$JAR_PATH":/opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar:ro \
  -v "$HOST_DIR1":/tmp/test_bin_original:rw \
  -v "$HOST_DIR2":/tmp/test_bin:rw \
  --name test-bazalt-spo \
  fedora:38 /bin/bash -c "\
    dnf install -y java-17-openjdk-headless && \
    java -jar /opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar"
