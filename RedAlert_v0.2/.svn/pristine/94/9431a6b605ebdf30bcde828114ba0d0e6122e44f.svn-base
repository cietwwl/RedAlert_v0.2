#!/bin/bash

case $1 in
start1)
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/mainserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/nodeserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/jobserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/asyncdbserver/bin/
 
memcached -d -m 1024 -n 128 -u root -p 11211 -t 8
cd /var/www/app/7qmobile/release/servers/asyncdbserver/bin
chmod +x ./startserver.sh
./startserver.sh  asyncdbserver
        sleep 10 
        ###启动mainserver
        cd /var/www/app/7qmobile/release/servers/mainserver/bin
        ./startserver.sh mainserver
        sleep 30

        ###启动jobserver
        cd /var/www/app/7qmobile/release/servers/jobserver/bin
        ./startserver.sh jobserver
        sleep 30

        ###启动nodeserver1
        cd /var/www/app/7qmobile/release/servers/nodeserver/bin
        ./startserver.sh nodeserver
        sleep 30
;; 
start)
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/mainserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/nodeserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/jobserver/bin/
cp /usr/local/svn_shell/startserver.sh /var/www/app/7qmobile/release/servers/asyncdbserver/bin/

ant -f /var/www/app/7qmobile/release/servers/build.xml load
memcached -d -m 1024 -n 128 -u root -p 11211 -t 8
cd /var/www/app/7qmobile/release/servers/asyncdbserver/bin 
chmod +x ./startserver.sh
echo ###启动asyncdbServer 
./startserver.sh  asyncdbserver
        sleep 10 

        echo ###启动mainserver
        cd /var/www/app/7qmobile/release/servers/mainserver/bin
        ./startserver.sh mainserver
        sleep 30

        echo ###启动jobserver
        cd /var/www/app/7qmobile/release/servers/jobserver/bin
        ./startserver.sh jobserver
        sleep 30

        echo ###启动nodeserver1
        cd /var/www/app/7qmobile/release/servers/nodeserver/bin
        ./startserver.sh nodeserver
        sleep 30
;;
stop)
###killall java;
pstree -ap | sed -n "/server.name/,/java/p" |awk -F, '/server.name/{print $2}' | awk '{print $1}' | xargs kill -9
killall memcached;
;;
esac
