#!/bin/sh
#dyansty2启动程序
#startserver.sh nodeserver

HOME_DIR=`dirname $0`;
HOME_DIR=`cd $HOME_DIR/..;pwd`;

echo Using JAVA_HOME:       $JAVA_HOME


export CLASSPATH=.   
pathtmp=''  
for jarpath in `ls $HOME_DIR/lib/*.jar`
do   
   CLASSPATH=$CLASSPATH:$jarpath      
done   
CLASSPATH=$HOME_DIR/classes:$CLASSPATH   

case $1 in 

nodeserver)

JAVA_MEM_OPTS="-Xms3584M -Xmx3584M -Xmn1024M -XX:PermSize=64M -XX:MaxPermSize=128M"
CLASSPATH=$HOME_DIR/node_conf:$CLASSPATH   

rm -fr $HOME_DIR/node_conf
cp -fr $HOME_DIR/conf $HOME_DIR/node_conf
#rm $HOME_DIR/node_conf/wolf/*.xml
#cp $HOME_DIR/conf/wolf/app_nodeserver*.xml $HOME_DIR/node_conf/wolf



;;
mainserver)
JAVA_MEM_OPTS="-Xms2048M -Xmx2048M -Xmn768M -XX:PermSize=64M -XX:MaxPermSize=128M"

CLASSPATH=$HOME_DIR/main_conf:$CLASSPATH   

rm -fr $HOME_DIR/main_conf
cp -fr $HOME_DIR/conf $HOME_DIR/main_conf
rm $HOME_DIR/main_conf/wolf/*.xml
cp $HOME_DIR/conf/wolf/app_server*.xml $HOME_DIR/main_conf/wolf
cp $HOME_DIR/conf/wolf/app_nodeserver_*.xml $HOME_DIR/main_conf/wolf
cp $HOME_DIR/conf/wolf/service_online.xml $HOME_DIR/main_conf/wolf

;;

activityserver)
JAVA_MEM_OPTS="-Xms1048M -Xmx2048M -Xmn768M -XX:PermSize=64M -XX:MaxPermSize=128M -Ddynasty2.udp=false "

CLASSPATH=$HOME_DIR/activity_conf:$CLASSPATH   

rm -fr $HOME_DIR/activity_conf
cp -fr $HOME_DIR/conf $HOME_DIR/activity_conf
rm $HOME_DIR/activity_conf/wolf/*.xml
cp $HOME_DIR/conf/wolf/app_server*.xml $HOME_DIR/activity_conf/wolf
cp $HOME_DIR/conf/wolf/app_nodeserver_*.xml $HOME_DIR/activity_conf/wolf
cp $HOME_DIR/conf/wolf/service_activity.xml $HOME_DIR/activity_conf/wolf

;;

jobserver)
JAVA_MEM_OPTS="-Xms1024M -Xmx2048M -Xmn512M -XX:PermSize=64M -XX:MaxPermSize=128M"

CLASSPATH=$HOME_DIR/job_conf:$CLASSPATH   

rm -fr $HOME_DIR/job_conf
cp -fr $HOME_DIR/conf $HOME_DIR/job_conf
rm $HOME_DIR/job_conf/wolf/*.xml
cp $HOME_DIR/conf/wolf/app_server.xml $HOME_DIR/job_conf/wolf
#cp $HOME_DIR/conf/wolf/app_nodeserver_*.xml $HOME_DIR/job_conf/wolf
cp $HOME_DIR/conf/wolf/service_job.xml $HOME_DIR/job_conf/wolf
cp $HOME_DIR/conf/wolf/service_simpleCache.xml $HOME_DIR/job_conf/wolf
cp $HOME_DIR/conf/wolf/service_hall.xml $HOME_DIR/job_conf/wolf

;;

asyncdbserver)
JAVA_MEM_OPTS="-Xms1024M -Xmx2048M -Xmn512M -XX:PermSize=64M -XX:MaxPermSize=128M"

CLASSPATH=$HOME_DIR/async_conf:$CLASSPATH   

rm -fr $HOME_DIR/async_conf
cp -fr $HOME_DIR/conf $HOME_DIR/async_conf
rm $HOME_DIR/async_conf/applicationContext_*.xml
rm $HOME_DIR/async_conf/udp.xml
cp $HOME_DIR/conf/applicationContext_common.xml $HOME_DIR/async_conf/
#cp $HOME_DIR/conf/applicationContext_openapi.xml $HOME_DIR/async_conf/
rm $HOME_DIR/async_conf/wolf/*.xml
cp $HOME_DIR/conf/wolf/app_server.xml $HOME_DIR/async_conf/wolf
cp $HOME_DIR/conf/wolf/service_asyncdb.xml $HOME_DIR/async_conf/wolf


;;

esac

export CLASSPATH;

#-XX:+DisableExplicitGC 在使用java.nio.ByteUuffer中的native方式的时候不要加这个参数
#JAVA_GCLOG_OPTS="-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC  -XX:+PrintTenuringDistribution"
JAVA_GC_OPTS="-Xnoclassgc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=5 -XX:+CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:MaxTenuringThreshold=4 -XX:+UseNUMA"
JAVA_OPTS="-Dserver.name=$1 -server -Djava.net.preferIPv4Stack=true   $JAVA_MEM_OPTS -XX:MaxDirectMemorySize=512M $JAVA_GC_OPTS $JAVA_GCLOG_OPTS"
echo JAVA_OPTS:       $JAVA_OPTS
if [ -f /usr/local/sbin/cronolog ]
then
	nohup $JAVA_HOME/bin/java $JAVA_OPTS -cp "$CLASSPATH" com.youxigu.dynasty2.core.Start $1 |/usr/local/sbin/cronolog $HOME_DIR/bin/dynasty2.out.%Y%m%d%H >>/dev/null 2>&1 &
else
	$JAVA_HOME/bin/java $JAVA_OPTS -cp "$CLASSPATH" com.youxigu.dynasty2.core.Start $1 >>$HOME_DIR/bin/dynasty2.out 2>&1 &
fi



#echo CLASSPATH:       $CLASSPATH
#-XX:SurvivorRatio=65535 -XX:MaxTenuringThreshold=0 这样设置可以去掉新生代救助空间
#-XX:+CMSScavengeBeforeRemark 
#-XX:CMSFullGCsBeforeCompaction=5
#-XX:+UseNUMA
#-XX:+AlwaysPreTouch
#-XX:+PrintFlagsFinal 显示所有JVM参数
#+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled
#JAVA_GC_OPTS="-XX:+DisableExplicitGC -Xnoclassgc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=5 -XX:+CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:MaxTenuringThreshold=4 -XX:+UseNUMA"
#JAVA_GC_OPTS="-XX:+DisableExplicitGC -Xnoclassgc -XX:+UseParallelGC -XX:ParallelGCThreads=4 -XX:+UseNUMA"
#JAVA_GCLOG_OPTS="-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC  -XX:+PrintTenuringDistribution"
#JAVA_GCLOG_OPTS=-Xloggc:gc.log  
#-XX:+PrintClassHistogram
#
#-Dnet.spy.log.LoggerImpl=com.ibatis.sqlmap.engine.cache.memcached.Self4JLogger 
#-Djgroups.logging.log_factory_class=com.ibatis.sqlmap.engine.cache.memcached.broadcast.jgroup.Self4JLogFactory
#JAVA_OPTS="-server -Djava.net.preferIPv4Stack=true  -Xms512M -Xmx1024M -Xmn256M -XX:PermSize=64M -XX:MaxPermSize=128M -XX:SurvivorRatio=4 $JAVA_GC_OPTS $JAVA_GCLOG_OPTS"
#JAVA_OPTS="-server -Djava.net.preferIPv4Stack=true  $JAVA_MEM_OPTS $JAVA_GC_OPTS $JAVA_GCLOG_OPTS"
#echo JAVA_OPTS:       $JAVA_OPTS
#-Dxmemcached.jmx.enable=true -Dxmemcached.statistics.enable=true -Dxmemcached.rmi.port=7077 -Dxmemcached.rmi.name=xmemcachedServer
#service:jmx:rmi:///jndi/rmi://172.16.0.59:7077/xmemcachedServer
#$JAVA_HOME/bin/java $JAVA_OPTS -cp "$CLASSPATH" com.youxigu.dynasty2.core.Start $1 >>$HOME_DIR/bin/dynasty2.out 2>&1 &
#echo $[$! - 1] > $HOME_DIR/bin/$1.pid;
#echo -e "\033[32;31;1;2m start server success!! \033[m";
#tail -f $HOME_DIR/bin/dynasty2.out;
#;;
# esac	
