CREATE TABLE IF NOT EXISTS user_link
(
    User_id BIGINT references "user"(id) not null,
    Link_id BIGINT references link(id) not null
);
