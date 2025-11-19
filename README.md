# File Analyzer Web Service

Web service for detecting damaged files and identifying exact damaged offsets.

## Table of Contents
1. [Overview](#overview)  
2. [Prerequisites](#prerequisites)  
3. [Installation via RPM](#installation-via-rpm)  
4. [Running the Service](#running-the-service)  
5. [Using the Web Interface](#using-the-web-interface)  
6. [API Endpoints](#api-endpoints)  
7. [Testing File Corruption](#testing-file-corruption)  
8. [License](#license)  

---

## Overview

The service compares two directories and detects files that have been modified or corrupted.  
It provides:

- Directory selection for analysis  
- File comparison and damaged offsets detection  
- Detailed view of damaged files  
- REST API for automation  
- Web interface for easy use  

---

## Prerequisites

- Java 17+ installed (OpenJDK recommended)  
- ALT Linux or any RPM-based Linux system for RPM installation  
- Python 3 for generating test corrupted files  

---

## Installation via RPM

1. Copy the RPM package to the target Linux system.  
2. Install the RPM:

```bash
sudo dnf install -y file-analyzer-1.0.0-1.fc38.noarch.rpm
```

This installs the service and places the JAR in `/opt/file-analyzer/`.

---

## Running the Service

Start the service:

```bash
java -jar /opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar
```

- By default, it runs on **port 8080**.  
- If the port is busy, use:

```bash
java -jar /opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar --server.port=8081
```

---

## Using the Web Interface

1. Open a browser and go to:  
   `http://<server-ip>:8080/`
2. Select directories to compare.  
3. Run the analysis.  
4. View the list of damaged files.  
5. Click on a file to see exact damaged offsets.  
6. Download files if needed.

---

## API Endpoints

- `POST /comparison` – start directory comparison  
- `GET /file/{fileName}` – get detailed info about a damaged file  
- `GET /download/{fileName}` – download a file  

**Example using `curl`:**

```bash
curl -X POST "http://localhost:8080/comparison" \
  -d "directory1=/tmp/test_bin_original" \
  -d "directory2=/tmp/test_bin"
```

---

## Testing File Corruption

Use `./scripts/file_scrumbler.py` to generate damaged files for testing:

```bash
./scripts/file_scrumbler.py /usr/bin --percent 1.0 --byte-percent 0.5 --seed 42 --recursive
```

- `--help` shows usage instructions.  
- Apply the script to any directory to simulate file corruption.  
- Then run the web service to detect damaged files.

---

## License

MIT License

