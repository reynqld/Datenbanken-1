-- =========================
-- DROP existing objects
-- =========================
ALTER TABLE Bueros DROP CONSTRAINT fk_bueros_raeume;
ALTER TABLE Gebaeude DROP CONSTRAINT fk_gebaeude_raeume;
ALTER TABLE Labore DROP CONSTRAINT fk_labore_veranstaltungen;
ALTER TABLE Lehrbeauftragte DROP CONSTRAINT fk_lehrbeauftragte_dozenten;
ALTER TABLE Mitarbeiter DROP CONSTRAINT fk_mitarbeiter_bueros;
ALTER TABLE Mitarbeiter DROP CONSTRAINT fk_mitarbeiter_dozenten;
ALTER TABLE Professoren DROP CONSTRAINT fk_professoren_bueros;
ALTER TABLE Professoren DROP CONSTRAINT fk_professoren_dozenten;
ALTER TABLE Stundenplaene DROP CONSTRAINT fk_stundenplaene_studiengaenge;
ALTER TABLE Stundenplaene DROP CONSTRAINT fk_stundenplaene_veranstaltungen;
ALTER TABLE Uebungen DROP CONSTRAINT fk_uebeungen_veranstaltungen;
ALTER TABLE Veranstaltungen_Dozenten DROP CONSTRAINT fk_veranstaltungen_dozenten_dozenten;
ALTER TABLE Veranstaltungen_Dozenten DROP CONSTRAINT fk_veranstaltungen_dozenten_veranstaltungen;
ALTER TABLE Veranstaltungen_Raeume DROP CONSTRAINT fk_veranstaltungen_raeume_raeume;
ALTER TABLE Veranstaltungen_Raeume DROP CONSTRAINT fk_veranstaltungen_raeume_veranstaltungen;
ALTER TABLE Veranstaltungen DROP CONSTRAINT fk_veranstaltungen_studiengaenge;
ALTER TABLE Vorlesungen DROP CONSTRAINT fk_vorlesungen_veranstaltungen;
DROP TABLE IF EXISTS Bueros CASCADE;
DROP TABLE IF EXISTS Dozenten CASCADE;
DROP TABLE IF EXISTS Gebaeude CASCADE;
DROP TABLE IF EXISTS Labore CASCADE;
DROP TABLE IF EXISTS Lehrbeauftragte CASCADE;
DROP TABLE IF EXISTS Mitarbeiter CASCADE;
DROP TABLE IF EXISTS Professoren CASCADE;
DROP TABLE IF EXISTS Raeume CASCADE;
DROP TABLE IF EXISTS Studiengaenge CASCADE;
DROP TABLE IF EXISTS Stundenplaene CASCADE;
DROP TABLE IF EXISTS Uebungen CASCADE;
DROP TABLE IF EXISTS Veranstaltungen CASCADE;
DROP TABLE IF EXISTS Veranstaltungen_Dozenten CASCADE;
DROP TABLE IF EXISTS Veranstaltungen_Raeume CASCADE;
DROP TABLE IF EXISTS Vorlesungen CASCADE;

-- =========================
-- CREATE tables
-- =========================
CREATE TABLE Bueros (
  RaeumeID int4 NOT NULL, 
  PRIMARY KEY (RaeumeID));
CREATE TABLE Dozenten (
  "Mail-Addresse"  varchar(254) NOT NULL, 
  Name             varchar(255) NOT NULL, 
  Vorname          varchar(255) NOT NULL, 
  AkademischerGrad varchar(255), 
  PRIMARY KEY ("Mail-Addresse"));
CREATE TABLE Gebaeude (
  Nummer   int2 NOT NULL, 
  Name     varchar(255) NOT NULL, 
  RaeumeID int4 NOT NULL, 
  PRIMARY KEY (Nummer));
CREATE TABLE Labore (
  VeranstaltungenID int4 NOT NULL, 
  PRIMARY KEY (VeranstaltungenID));
CREATE TABLE Lehrbeauftragte (
  "DozentenMail-Addresse" varchar(254) NOT NULL, 
  PRIMARY KEY ("DozentenMail-Addresse"));
CREATE TABLE Mitarbeiter (
  "DozentenMail-Addresse" varchar(254) NOT NULL, 
  Sprechzeiten            timestamp NOT NULL, 
  BuerosRaeumeID          int4 NOT NULL, 
  PRIMARY KEY ("DozentenMail-Addresse"));
CREATE TABLE Professoren (
  "DozentenMail-Addresse" varchar(254) NOT NULL, 
  Sprechzeiten            timestamp NOT NULL, 
  BuerosRaeumeID          int4 NOT NULL, 
  PRIMARY KEY ("DozentenMail-Addresse"));
CREATE TABLE Raeume (
  ID     SERIAL NOT NULL, 
  Nummer int4 NOT NULL, 
  Name   varchar(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Studiengaenge (
  Kuerzel varchar(10) NOT NULL, 
  Name    varchar(255) NOT NULL, 
  PRIMARY KEY (Kuerzel));
CREATE TABLE Stundenplaene (
  StudiengaengeKuerzel varchar(10) NOT NULL, 
  Fachsemester         int2 NOT NULL, 
  VeranstaltungenID    int4 NOT NULL, 
  PRIMARY KEY (StudiengaengeKuerzel, 
  Fachsemester));
CREATE TABLE Uebungen (
  VeranstaltungenID int4 NOT NULL, 
  PRIMARY KEY (VeranstaltungenID));
CREATE TABLE Veranstaltungen (
  ID                          SERIAL NOT NULL, 
  Name                        varchar(255) NOT NULL, 
  Wochentag                   varchar(10) NOT NULL, 
  Startzeit                   time(7) NOT NULL, 
  Endzeit                     time(7) NOT NULL, 
  Fachsemester                int2 NOT NULL, 
  HaeufigkeitDerDurchfuehrung varchar(255) NOT NULL, 
  StudiengaengeKuerzel        varchar(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Veranstaltungen_Dozenten (
  VeranstaltungenID       int4 NOT NULL, 
  "DozentenMail-Addresse" varchar(254) NOT NULL);
CREATE TABLE Veranstaltungen_Raeume (
  VeranstaltungenID int4 NOT NULL, 
  RaeumeID          int4 NOT NULL);
CREATE TABLE Vorlesungen (
  VeranstaltungenID int4 NOT NULL, 
  PRIMARY KEY (VeranstaltungenID));
ALTER TABLE Bueros ADD CONSTRAINT fk_bueros_raeume FOREIGN KEY (RaeumeID) REFERENCES Raeume (ID);
ALTER TABLE Gebaeude ADD CONSTRAINT fk_gebaeude_raeume FOREIGN KEY (RaeumeID) REFERENCES Raeume (ID);
ALTER TABLE Labore ADD CONSTRAINT fk_labore_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);
ALTER TABLE Lehrbeauftragte ADD CONSTRAINT fk_lehrbeauftragte_dozenten FOREIGN KEY ("DozentenMail-Addresse") REFERENCES Dozenten ("Mail-Addresse");
ALTER TABLE Mitarbeiter ADD CONSTRAINT fk_mitarbeiter_bueros FOREIGN KEY (BuerosRaeumeID) REFERENCES Bueros (RaeumeID);
ALTER TABLE Mitarbeiter ADD CONSTRAINT fk_mitarbeiter_dozenten FOREIGN KEY ("DozentenMail-Addresse") REFERENCES Dozenten ("Mail-Addresse");
ALTER TABLE Professoren ADD CONSTRAINT fk_professoren_bueros FOREIGN KEY (BuerosRaeumeID) REFERENCES Bueros (RaeumeID);
ALTER TABLE Professoren ADD CONSTRAINT fk_professoren_dozenten FOREIGN KEY ("DozentenMail-Addresse") REFERENCES Dozenten ("Mail-Addresse");
ALTER TABLE Stundenplaene ADD CONSTRAINT fk_stundenplaene_studiengaenge FOREIGN KEY (StudiengaengeKuerzel) REFERENCES Studiengaenge (Kuerzel);
ALTER TABLE Stundenplaene ADD CONSTRAINT fk_stundenplaene_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);
ALTER TABLE Uebungen ADD CONSTRAINT fk_uebeungen_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);
ALTER TABLE Veranstaltungen_Dozenten ADD CONSTRAINT fk_veranstaltungen_dozenten_dozenten FOREIGN KEY ("DozentenMail-Addresse") REFERENCES Dozenten ("Mail-Addresse");
ALTER TABLE Veranstaltungen_Dozenten ADD CONSTRAINT fk_veranstaltungen_dozenten_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);
ALTER TABLE Veranstaltungen_Raeume ADD CONSTRAINT fk_veranstaltungen_raeume_raeume FOREIGN KEY (RaeumeID) REFERENCES Raeume (ID);
ALTER TABLE Veranstaltungen_Raeume ADD CONSTRAINT fk_veranstaltungen_raeume_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);
ALTER TABLE Veranstaltungen ADD CONSTRAINT fk_veranstaltungen_studiengaenge FOREIGN KEY (StudiengaengeKuerzel) REFERENCES Studiengaenge (Kuerzel);
ALTER TABLE Vorlesungen ADD CONSTRAINT fk_vorlesungen_veranstaltungen FOREIGN KEY (VeranstaltungenID) REFERENCES Veranstaltungen (ID);