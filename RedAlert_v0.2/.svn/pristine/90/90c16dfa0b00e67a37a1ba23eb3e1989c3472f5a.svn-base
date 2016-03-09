drop table if exists Message_&;
create table Message_& (
   messageId            integer(16) not null auto_increment,
   sendUserId           integer(16) not null,
   receiveUserId        integer(16) not null,
   sendDttm             timestamp null default NULL,
   readDttm             timestamp null default NULL,
   comment              varchar(4000) null,
   title                varchar(100) null,
   messageSendType      integer(2) null default 0,
   messageReceiveType   integer(2) null default 0,
   messageType          integer(2) null default 0,
   childType            integer(2) null default 0,
   map                  blob null default NULL,
   appendixFlag         integer(2) null default 0,
   entityId             integer(16) null default 0,
   itemNum              integer(16) null default 0,
   equipStrongerId      integer(16) null default 0,
   orderId              varchar(20)  null comment '邮件订单号',
   KEY index_Message_receiveUserId_messageType(receiveUserId,messageType),
   primary key (messageId)
)
type = InnoDB;
