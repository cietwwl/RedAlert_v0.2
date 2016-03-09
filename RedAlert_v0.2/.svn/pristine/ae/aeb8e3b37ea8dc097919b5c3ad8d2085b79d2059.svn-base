@echo on
echo Using JAVA_HOME:       %JAVA_HOME%

SetLocal EnableDelayedExpansion    

set CLASSPATH=

for %%i in (../lib/*.jar) do call set CLASSPATH=!CLASSPATH!../lib/%%i;

set CLASSPATH=../classes;../bin;../conf;%CLASSPATH%;

set JAVA_OPTS=-Xms256M -Xmx1024M 

"%JAVA_HOME%/bin/java" %JAVA_OPTS% -cp "%CLASSPATH%" com.youxigu.dynasty2.core.Shutdown  %1
EndLocal 

