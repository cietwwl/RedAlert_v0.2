@echo off
echo Using JAVA_HOME:       %JAVA_HOME%

SetLocal EnableDelayedExpansion    

set CLASSPATH=

for %%i in (../lib/*.jar) do call set CLASSPATH=!CLASSPATH!../lib/%%i;

set CLASSPATH=../classes;../conf;%CLASSPATH%;
REM -XX:+UseParallelGC -XX:ParallelGCThreads=4 
set JAVA_GC_OPTS=-XX:+DisableExplicitGC -Xnoclassgc -XX:-UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 
REM set JAVA_GCLOG_OPTS=-Xloggc:e:/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC  -XX:+PrintTenuringDistribution
REM set JAVA_GCLOG_OPTS=-Xloggc:e:/gc.log  

set JAVA_OPTS=-server -Djava.net.preferIPv4Stack=true -Dnet.spy.log.LoggerImpl=net.spy.memcached.compat.log.Log4JLogger -Xms512M -Xmx1024M -XX:PermSize=256M -XX:MaxPermSize=256M -XX:NewRatio=2 -XX:SurvivorRatio=4 %JAVA_GC_OPTS% %JAVA_GCLOG_OPTS%


"%JAVA_HOME%/bin/java" %JAVA_OPTS% -cp "%CLASSPATH%" com.youxigu.dynasty2.core.Start %1
EndLocal 

