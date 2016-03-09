@echo off
echo Using JAVA_HOME:       %JAVA_HOME%

SetLocal EnableDelayedExpansion    

set CLASSPATH=

for %%i in (../lib/*.jar) do call set CLASSPATH=!CLASSPATH!../lib/%%i;

set CLASSPATH=../classes;../bin;../conf;%CLASSPATH%;
set JAVA_OPTS=-Xms128M -Xmx512M


"%JAVA_HOME%/bin/java" %JAVA_OPTS% -cp "%CLASSPATH%" com.youxigu.dynasty2.core.GMTool %1 %2 %3 %4 
EndLocal 

