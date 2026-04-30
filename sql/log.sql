CREATE TABLE log (
                     idlog              SERIAL PRIMARY KEY,
                     tableName          VARCHAR(50)  NOT NULL,
                     operation          VARCHAR(10)  NOT NULL CHECK (operation IN ('INSERT', 'UPDATE', 'DELETE')),
                     dateAction         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     loginUtilisateur   VARCHAR(100) NOT NULL DEFAULT 'inconnu',
                     ancienContenu      TEXT,
                     nouveauContenu     TEXT
);
