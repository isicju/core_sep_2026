CREATE TABLE mortgage_applications (
                                       id                VARCHAR(100) PRIMARY KEY,
                                       first_name        VARCHAR(100) NOT NULL,
                                       last_name         VARCHAR(100) NOT NULL,
                                       monthly_income    NUMERIC(12, 2) NOT NULL,
                                       property_price    NUMERIC(14, 2) NOT NULL,
                                       loan_term_years   SMALLINT NOT NULL,
                                       status            VARCHAR(100) NOT NULL
                                           CHECK (status IN ('APPROVED', 'REJECTED', 'NEW', 'DRAFT')),
                                       analyst_comments  TEXT
);

INSERT INTO mortgage_applications (
    id, first_name, last_name, monthly_income,
    property_price, loan_term_years, status
) VALUES
      ('123', 'Vasya', 'Petrov', 120000.00, 6000000.00, 30, 'NEW'),
      ('124', 'Ivan', 'Sidorov', 80000.00, 3500000.00, 25, 'NEW'),
      ('125', 'Petr', 'Smirnov', 180000.00, 8500000.00, 30, 'NEW'),
      ('126', 'Anna', 'Kuznetsova', 60000.00, 2500000.00, 20, 'NEW'),
      ('127', 'Elena', 'Volkova', 150000.00, 7000000.00, 25, 'NEW'),
      ('128', 'Dmitry', 'Popov', 100000.00, 4500000.00, 30, 'NEW'),
      ('129', 'Maria', 'Sokolova', 200000.00, 9000000.00, 30, 'NEW'),
      ('130', 'Alexey', 'Morozov', 90000.00, 5500000.00, 20, 'NEW'),
      ('131', 'Olga', 'Lebedeva', 140000.00, 6500000.00, 25, 'NEW'),
      ('132', 'Sergey', 'Novikov', 50000.00, 2000000.00, 15, 'NEW'),
      ('133', 'Natalia', 'Fedorova', 110000.00, 7500000.00, 30, 'NEW'),
      ('134', 'Andrey', 'Pavlov', 170000.00, 4000000.00, 25, 'NEW'),
      ('135', 'Irina', 'Semenova', 70000.00, 3000000.00, 20, 'NEW'),
      ('136', 'Mikhail', 'Kozlov', 160000.00, 8000000.00, 30, 'NEW'),
      ('137', 'Ekaterina', 'Orlova', 130000.00, 5000000.00, 25, 'NEW');