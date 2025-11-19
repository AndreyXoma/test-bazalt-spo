#!/bin/bash
set -e

# --- 1. Пути на хосте для теста ---
HOST_DIR1="$(pwd)/test_bin_original"
HOST_DIR2="$(pwd)/test_bin"

mkdir -p "$HOST_DIR1" "$HOST_DIR2"

# --- 2. Тестовые файлы ---
echo "Hello World" > "$HOST_DIR1/file1.txt"
echo "Sample file" > "$HOST_DIR1/file2.txt"

echo "Hello World" > "$HOST_DIR2/file1.txt"
echo "Changed content" > "$HOST_DIR2/file2.txt"

# --- 3. Путь до JAR на хосте ---
JAR_PATH="$(pwd)/target/test-bazalt-spo-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR_PATH" ]; then
  echo "ERROR: JAR файл не найден в target/. Собери проект перед запуском."
  exit 1
fi

# --- 4. Запуск контейнера ---
docker run -it --rm \
  -p 8080:8080 \
  -v "$JAR_PATH":/opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar:ro \
  -v "$HOST_DIR1":/data/test_bin_original:rw \
  -v "$HOST_DIR2":/data/test_bin:rw \
  fedora:38 /bin/bash -c "
    # Установка JDK и rpm-build
    dnf install -y java-17-openjdk-headless rpm-build && \

    # Создание rpmbuild-структуры
    mkdir -p /root/rpmbuild/{BUILD,RPMS,SOURCES,SPECS,SRPMS} && \

    # Копируем SPEC в контейнер
    cp /opt/file-analyzer/file-analyzer.spec /root/rpmbuild/SPECS/ && \

    # Сборка RPM
    rpmbuild -ba /root/rpmbuild/SPECS/file-analyzer.spec && \

    # Установка RPM
    dnf install -y /root/rpmbuild/RPMS/noarch/file-analyzer-1.0.0-1.fc38.noarch.rpm && \

    # Создаём символические ссылки на тестовые директории
    mkdir -p /opt/file-analyzer/test_bin_original /opt/file-analyzer/test_bin && \
    ln -s /data/test_bin_original/* /opt/file-analyzer/test_bin_original/ 2>/dev/null || true && \
    ln -s /data/test_bin/* /opt/file-analyzer/test_bin/ 2>/dev/null || true && \

    # Запуск сервиса
    java -jar /opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar
  "
