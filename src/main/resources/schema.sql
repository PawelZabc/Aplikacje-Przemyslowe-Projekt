DROP TABLE IF EXISTS extras;

CREATE TABLE extras (
                        id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        price_cents INTEGER NOT NULL CHECK (price_cents >= 0)
);