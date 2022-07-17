CREATE TABLE IF NOT EXISTS t_message
(
    id           BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    exchange     VARCHAR(32)                        NOT NULL,
    routing_key  VARCHAR(32)                        NOT NULL,
    status       TINYINT  DEFAULT 0                 NOT NULL COMMENT '消息状态（0-就绪，1-成功，2-失败）',
    business_key VARBINARY(64)                      NOT NULL COMMENT '业务KEY,唯一的',
    operate      TINYINT  DEFAULT 1                 NOT NULL COMMENT '操作（1-写，2-更新）',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time  DATETIME                           NULL
)
    COMMENT '消息';

CREATE TABLE IF NOT EXISTS t_message_content
(
    id     BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    msg_id BIGINT NOT NULL COMMENT 't_message.id',
    msg    JSON   NOT NULL COMMENT '消息',
    CONSTRAINT msg_id
        UNIQUE (msg_id)
)
    COMMENT '消息内容';

CREATE TABLE IF NOT EXISTS t_order
(
    id      BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    content VARCHAR(255) NULL
);

