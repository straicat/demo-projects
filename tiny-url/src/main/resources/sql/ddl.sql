create table tiny_url_mapping
(
    id        bigint auto_increment comment '主键ID'
        primary key,
    short_url varchar(12)    default '' not null comment '短URL',
    long_url  varchar(1024) default '' not null comment '长URL',
    ctime     bigint        default 0  not null comment '创建时间',
    constraint idx_short_url
        unique (short_url)
)
    comment 'URL映射表' collate = utf8mb4_unicode_ci;
