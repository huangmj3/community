use `community`;

alter table comment
    add content varchar(1024) null comment '回复内容';