@echo off
REM Get the directory where the batch file itself is located
SET BATCH_DIR=%~dp0

REM Construct the full path to the JAR file
SET JAR_PATH=%BATCH_DIR%typewrite.jar

REM Run the JAR file using its full path
java -jar "%JAR_PATH%" %*