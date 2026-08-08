CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE board_columns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    order_index INT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
);

CREATE TABLE cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    column_id BIGINT NOT NULL,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    block_reason TEXT,
    block_timestamp DATETIME,
    unblock_timestamp DATETIME,
    FOREIGN KEY (column_id) REFERENCES board_columns(id)
);

CREATE TABLE card_column_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    column_id BIGINT NOT NULL,
    arrival_timestamp DATETIME NOT NULL,
    departure_timestamp DATETIME,
    FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE,
    FOREIGN KEY (column_id) REFERENCES board_columns(id)
);

CREATE TABLE card_block_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    reason TEXT NOT NULL,
    block_timestamp DATETIME NOT NULL,
    unblock_timestamp DATETIME,
    FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);
