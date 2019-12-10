@echo Copy necessary files
copy ..\license.rtf ..\..\license.rtf
copy ..\MetCalc256.ico ..\..\MetCalc256.ico

@echo Run packaging..
"C:\Program Files (x86)\Caphyon\Advanced Installer 9.5\bin\x86\advinst.exe" /rebuild "setup.aip"

del ..\..\license.rtf
del ..\..\MetCalc256.ico

explorer.exe Setup Files