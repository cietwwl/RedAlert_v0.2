#!/bin/sh
#gm工具启动程序
#gmtool.sh -h127.0.1.133 -P8739 -uroot -proot

HOME_DIR=`dirname $0`;
HOME_DIR=`cd $HOME_DIR/..;pwd`;

echo Using JAVA_HOME:       $JAVA_HOME


export CLASSPATH=.   
pathtmp=''  
for jarpath in `ls $HOME_DIR/lib/*.jar`
do   
   CLASSPATH=$CLASSPATH:$jarpath      
done   
export CLASSPATH=$HOME_DIR/classes:$HOME_DIR/conf:$CLASSPATH   

#echo CLASSPATH:       $CLASSPATH
JAVA_OPTS="-Xms128M -Xmx512M"
echo JAVA_OPTS:       $JAVA_OPTS

$JAVA_HOME/bin/java $JAVA_OPTS -cp "$CLASSPATH" com.youxigu.dynasty2.core.GMTool $1 $2 $3 $4
