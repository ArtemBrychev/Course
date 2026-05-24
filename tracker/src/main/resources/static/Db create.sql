DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS boards CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
                       role VARCHAR(32) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE boards (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        owner_id BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),

                        CONSTRAINT fk_boards_owner
                            FOREIGN KEY (owner_id)
                                REFERENCES users(id)
                                ON DELETE CASCADE
);

CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       board_id BIGINT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       status VARCHAR(32) NOT NULL DEFAULT 'TODO',
                       assignee_id BIGINT,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                       CONSTRAINT fk_tasks_board
                           FOREIGN KEY (board_id)
                               REFERENCES boards(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_tasks_assignee
                           FOREIGN KEY (assignee_id)
                               REFERENCES users(id)
                               ON DELETE SET NULL,

                       CONSTRAINT chk_tasks_status
                           CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE'))
);

CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          task_id BIGINT NOT NULL,
                          author_id BIGINT NOT NULL,
                          text TEXT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_comments_task
                              FOREIGN KEY (task_id)
                                  REFERENCES tasks(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_comments_author
                              FOREIGN KEY (author_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

CREATE INDEX idx_boards_owner_id
    ON boards(owner_id);

CREATE INDEX idx_tasks_board_id
    ON tasks(board_id);

CREATE INDEX idx_tasks_assignee_id
    ON tasks(assignee_id);

CREATE INDEX idx_tasks_status
    ON tasks(status);

CREATE INDEX idx_comments_task_id
    ON comments(task_id);

CREATE INDEX idx_comments_author_id
    ON comments(author_id);