#!/bin/bash

# --- 1. Проверяем JAR на хосте ---
JAR_PATH="$(pwd)/target/test-bazalt-spo-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_PATH" ]; then
  echo "ERROR: JAR файл не найден в target/. Сборка должна быть выполнена до запуска скрипта."
  exit 1
fi

# --- 2. Создаем тестовые директории на хосте ---
HOST_DIR1="$(pwd)/test_bin_original"
HOST_DIR2="$(pwd)/test_bin"

mkdir -p "$HOST_DIR1" "$HOST_DIR2"

# Добавляем тестовые файлы
echo "Hello World" > "$HOST_DIR1/file1.txt"
echo "Sample file" > "$HOST_DIR1/file2.txt"

echo "Hello World" > "$HOST_DIR2/file1.txt"       # совпадает
echo "Changed content" > "$HOST_DIR2/file2.txt"  # отличается

# --- 3. Запускаем контейнер Fedora, собираем RPM и проверяем ---
docker run -it --rm \
  -v "$JAR_PATH":/root/rpmbuild/SOURCES/test-bazalt-spo-0.0.1-SNAPSHOT.jar:ro \
  -v "$(pwd)/rpm/file-analyzer.spec":/root/rpmbuild/SPECS/file-analyzer.spec:ro \
  -v "$HOST_DIR1":/tmp/test_bin_original:rw \
  -v "$HOST_DIR2":/tmp/test_bin:rw \
  -p 8080:8080 \
  --name test-bazalt-spo \
  fedora:38 /bin/bash -c "
    # Устанавливаем Java и инструменты для сборки RPM
    dnf install -y java-17-openjdk-headless rpm-build

    # Создаем структуру rpmbuild
    mkdir -p /root/rpmbuild/{BUILD,RPMS,SOURCES,SPECS,SRPMS}

    # Собираем RPM
    echo 'Собираем RPM...'
    rpmbuild -ba /root/rpmbuild/SPECS/file-analyzer.spec

    # Устанавливаем RPM
    echo 'Устанавливаем RPM...'
    dnf install -y /root/rpmbuild/RPMS/noarch/file-analyzer-1.0.0-1.fc38.noarch.rpm

    # Проверяем, что тестовые директории видны
    echo 'Содержимое /tmp/test_bin_original:'
    ls -l /tmp/test_bin_original
    echo 'Содержимое /tmp/test_bin:'
    ls -l /tmp/test_bin

    # Запуск сервиса
    echo 'Запуск сервиса...'
    java -jar /opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar
  "
