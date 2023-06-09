create table seckill_stock_info
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    sku_id      bigint default 0 not null comment '商品ID',
    activity_id bigint default 0 not null comment '秒杀活动ID',
    stock       int    default 0 not null comment '库存',
    lock_stock  int    default 0 not null comment '锁定库存'
) comment '库存信息表' collate = utf8mb4_unicode_ci;;
