call :logWork "COPY DIST"

rmdir /s /q .\source
mkdir source

copy "..\releng\com.epri.metric_calculator.product\target\products\EPRI Security Metric Calculator-win32.win32.x86.zip" .\source

7z.exe x "source\EPRI Security Metric Calculator-win32.win32.x86.zip" -osource\x86 -y




call :logWork PACK

cd "Advanced Installer"

_pack.bat

cd ..




:logWork
@echo ------------------------------------------------------------------------
@echo %~1
@echo ------------------------------------------------------------------------