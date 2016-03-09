#!/bin/sh

HOME_DIR=`dirname $0`;
HOME_DIR=`cd $HOME_DIR/..;pwd`;

echo Using JAVA_HOME:       $JAVA_HOME

export CLASSPATH=.   
for jarpath in `ls $HOME_DIR/lib/*.jar`
do   
   CLASSPATH=$CLASSPATH:$jarpath      
done   
export CLASSPATH=$HOME_DIR/classes:$HOME_DIR/conf:$CLASSPATH   

#echo CLASSPATH:       $CLASSPATH
JAVA_OPTS="-Xms256M -Xmx256M"

$JAVA_HOME/bin/java $JAVA_OPTS -cp "$CLASSPATH" com.youxigu.dynasty2.core.Shutdown $1
