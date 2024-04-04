CREATE TABLE IF NOT EXISTS chat
(
    ID         BIGINT PRIMARY KEY
--     Created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS link
(
    id           bigserial PRIMARY KEY,
--     type         VARCHAR(48),
    url          varchar(255) NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    checked_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    answer_count INT
);

CREATE TABLE IF NOT EXISTS chat_link
(
--     ID      bigserial PRIMARY KEY,
    Chat_id BIGINT REFERENCES chat (ID) ON DELETE CASCADE,
    Link_id BIGINT REFERENCES link (ID) ON DELETE CASCADE,
    UNIQUE (Chat_id, Link_id)
);

CREATE TABLE IF NOT EXISTS questions
(
    ID           bigserial PRIMARY KEY,
    Answer_count INT,
    Link_id      BIGINT,
    FOREIGN KEY (Link_id) REFERENCES LINK (ID) ON DELETE CASCADE
);
