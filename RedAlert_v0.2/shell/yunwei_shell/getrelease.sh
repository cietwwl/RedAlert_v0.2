#!/bin/sh
#dynasty2 处理程序
#
function des (){

echo 'ERROR: 
  1、此脚本为版本包生成工具
  2、脚本使用：
     getrelease.sh  $1 $2
  3、说明： 
        参数1： 为版本号如：   v1.0
        参数2： 为设备名称如： ios 或者 android

'
}

if [ $# -lt 2 ];then
des;
exit 100;
fi


export MY_HOME=/var/www/app/7qmobile/release/
verssion=$1

case $2 in
ios|IOS)
ver_f="TK_$2_${verssion}.tgz"
ver_d=/var/bak/release/IOS/$1/
;;

android|ANDR)
ver_f="TK_$2_${verssion}.tgz"
ver_d=/var/bak/release/ANDR/$1/
;;


tw|taiwan)
ver_f="TK_$2_${verssion}.tgz"
ver_d=/var/bak/release/ANDR/$1/
;;

*)
des;
exit 100
;;

esac

########## 补丁生成  ##########################################
	sh /var/www/app/7qmobile/build.sh $verssion
	
        
	### 添加脚本权限
	file_p=`find /var/www/app/7qmobile/release/servers/ -type f | grep -E "*\.sh"`
	for i in $file_p
	do
	chmod +x $i
	ls -l $i
	done



	
	###传送文件到服务器
	cd /var/www/app/7qmobile/release
	rm -fr  /var/bak/release/$verssion
	mkdir  /var/bak/release/$verssion
	tar -cvzf  /var/bak/release/$verssion/$ver_f servers 
	if [ ! -d ${ver_d} ];then
         mkdir -p $ver_d
        fi
         cp /var/bak/release/$verssion/${ver_f} ${ver_d}
        echo -e "\033[33;33;3;1m版本在 $ver_d 目录生成 ！！！\033[0m"

###################################################################
if [[ "$3" = "release"  ]]
then
        mkdir /var/ftp/TK/$verssion/ -p
	cp  /var/bak/release/$verssion/$ver_f /var/ftp/TK/$verssion/
fi
