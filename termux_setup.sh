#!/data/data/com.termux/files/usr/bin/bash
set -e
echo "FRIDAY project setup"
if ! command -v git >/dev/null 2>&1; then
  echo "Install basic tools with: pkg update && pkg install git unzip openjdk-17"
else
  echo "Git found."
fi
if command -v java >/dev/null 2>&1; then
  java -version
else
  echo "JDK not found. Try: pkg install openjdk-17"
fi
echo
echo "This script does not build an APK by itself."
echo "Android SDK + platform/build tools + Gradle are also required."
echo "See README.md for build instructions."
