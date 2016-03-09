1。从运营环境备份出两个库.SOURCEDB---要被合并的DB,TARGETDB---要合并到的DB
2.将两个数据库倒入到内网,并恢复到某一个mysql实例中(建议这个mysql不要记录binlog，这样能快不少).
   mysql -h172.16.3.54 -uroot -proot --default-character-set=utf8 < ./sx2_20130627/data_20130627_0.bin
   mysql -h172.16.3.54 -uroot -proot --default-character-set=utf8 < ./sx1_20130627/data_20130627_0.bin
3.将最近版本的sql补丁在两个数据库上执行.
4.将mergedb目录copy到某个linux机器上
5.进入mergedb目录，执行sh ./mergedb.sh 数据库IP 数据库端口 数据库用户 密码 SOURCEDB TARGETDB
  ./mergedb.sh 172.16.3.54 3306 root root  dynasty2_lm1 dynasty2_lm3
6.正常执行完成会显示"合并数据库完成." , 如果中间出错,则需要找到原因，并从第二步开始重新执行。
