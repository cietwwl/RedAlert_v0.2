cd /d "%~dp0"
protoc.exe --java_out=./ *.proto
rem xcopy .\com ..\src\com /y /s /e /h
rem copy /y *.proto ..\..\..\doc\proto
pause