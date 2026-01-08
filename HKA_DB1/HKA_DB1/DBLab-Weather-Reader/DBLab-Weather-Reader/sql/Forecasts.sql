DROP TABLE IF EXISTS forecasts;

CREATE TABLE forecasts (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY    PRIMARY KEY,
    station_id          INTEGER                                 NOT NULL,
    date_weather        TIMESTAMP WITH TIME ZONE                NOT NULL,
    min_temp            FLOAT,
    max_temp            FLOAT,
    precipitation       INTEGER,
    sunshine            INTEGER,
    date_retrieved      TIMESTAMP WITH TIME ZONE                NOT NULL
);

