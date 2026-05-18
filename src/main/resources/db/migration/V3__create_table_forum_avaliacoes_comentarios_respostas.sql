CREATE TABLE foruns (
    id CHAR(36) PRIMARY KEY,
    id_intercambista CHAR(36) NOT NULL,
    titulo VARCHAR(80) NOT NULL,
    comentario TEXT,
    url_foto_forum VARCHAR(255),
    likes INT,
    dislikes INT,
    created_at DATETIME,
    deleted_at DATETIME,
    FOREIGN KEY (id_intercambista) REFERENCES intercambistas(id)
);

CREATE TABLE respostas (
    id CHAR(36) PRIMARY KEY,
    id_forum CHAR(36) NOT NULL,
    id_intercambista CHAR(36) NOT NULL,
    likes INT,
    dislikes INT,
    comentario TEXT,
    created_at DATETIME,
    deleted_at DATETIME,
    FOREIGN KEY (id_forum) REFERENCES foruns(id),
    FOREIGN KEY (id_intercambista) REFERENCES intercambistas(id)
);
