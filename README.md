#  SyncKar (1.0.0)

[![Java](https://img.shields.io/badge/Java-Temurin%2023-blue)](https://adoptium.net/en-GB/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Build](https://img.shields.io/badge/build-passing-brightgreen)]()

> A lightweight Java CLI tool to synchronize files between two folders with dry-run support and change detection.

---

##  Features

-  One-way synchronization (source ➜ target)
-  Dry-run mode: preview what would change
-  Deletes files in target that don't exist in source
-  Fast comparison using file size and modified time
-  Simple CLI flags for easy integration
-  Written in pure Java (Temurin 23)
  
---

## Usage

1. Download the latest release from the [Releases](https://github.com/pranjalg05/SyncKar/releases) section.
2. Run the app:
   
3. Provide the arguements directly
   
```   java -jar SyncKar.jar --source /path/to/source --target /path/to/target --dryrun   ```
   
5. Or use config.json
   
```   java -jar SyncKar.jar --config path/to/config.json   ```
   

## Future Ideas

- GUI support using JavaFx
- Multithreading for faster file actions



