CREATE TABLE IF NOT EXISTS link
(
    ID         BIGINT      primary key,
    URL       varchar(255) NOT NULL,
    Last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);
