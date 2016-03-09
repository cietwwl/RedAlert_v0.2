#!/bin/sh
#dyansty2数据库合并脚本
echo "sh ./mergedb.sh ip port usr pwd sourceDB targetDB"


#备份2个数据库
fileName=`date  +%Y%m%d%H`;
fileName=$5_$fileName.sql
#echo "备份$5为$fileName............."
#mysqldump  -h$1 -P$2 -u$3 -p$4 --default-character-set=utf8 $5 >$fileName

fileName=`date  +%Y%m%d%H`;
fileName=$6_$fileName.sql
#echo "备份$6为$fileName................"
#mysqldump  -h$1 -P$2 -u$3 -p$4 --default-character-set=utf8 $6 >$fileName


echo "清理死号:$5..............."
`sed "s/TARGETDB/$5/g" cleanDb.sql > tmp_$5.sql`
mysql -h$1 -P$2 -u$3 -p$4 --database=$5 --default-character-set=utf8 < tmp_$5.sql

echo "清理死号:$6..............."
`sed "s/TARGETDB/$6/g" cleanDb.sql > tmp_$6.sql`
mysql -h$1 -P$2 -u$3 -p$4 --database=$6 --default-character-set=utf8 < tmp_$6.sql

echo "合并数据开始,$5-->$6..............."
`sed "s/TARGETDB/$6/g" mergedb.sql | sed "s/SOURCEDB/$5/g" > tmp_$5_$6.sql`
mysql -h$1 -P$2 -u$3 -p$4 --database=$6 --default-character-set=utf8 < tmp_$5_$6.sql

echo "合并数据完毕，开始检查数据完整性........."
mysql -h$1 -P$2 -u$3 -p$4 --database=$6 --default-character-set=utf8 < checkList.sql >checkList.log

echo "合并数据结束。请检查checkList.log,如果有不为0的数据，合并后数据就可能有问题"
