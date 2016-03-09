#!/bin/sh
#dynasty2 处理程序
#

export MY_HOME=/var/www/app/7qmobile
        echo "发布java 后台程序"
        rm -fr $MY_HOME/dynasty2
        mkdir $MY_HOME/dynasty2
        ###取得并编译后台代码
        cd $MY_HOME/dailyBuild
        ant -Dapp.version=$1 -f $MY_HOME/dailyBuild/build.xml
        cd $MY_HOME/dynasty2
        ant -f $MY_HOME/dynasty2/build.xml
        cd $MY_HOME
        rm -fr ./release
        mkdir ./release
        rm -fr ./dynasty2/bin/ ./dynasty2/dist
        mv ./dynasty2/servers ./release
        rm -fr ./dynasty2/servers



echo "部署完毕................."
