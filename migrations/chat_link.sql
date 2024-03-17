CREATE TABLE IF NOT EXISTS chat_link
(
    Chat_id BIGINT references chat(id) ON DELETE CASCADE,
    Link_id BIGINT references link(id) ON DELETE CASCADE,
    PRIMARY KEY(Chat_id, Link_id)
);
