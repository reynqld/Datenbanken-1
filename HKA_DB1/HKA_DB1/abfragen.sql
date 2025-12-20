-- 1.1
select *
    from teilestamm;

-- 1.2
select *
   from teilestamm
        where bezeichnung like '%City%';

-- 1.3
select a.teilnr, sum(a.gesamtpreis)
from auftragsposten as a
group by a.teilnr
order by sum(a.gesamtpreis) desc
fetch first 1 row only;

--1.4
select count(nr) as zeile_kunde from kunde;
select count(persnr) as zeile_personal from personal;
select count(teilnr) as zeile_teilestamm from teilestamm;

--1.5
select min(datum) as von, max(datum) as bis
    from auftrag;

--1.6
select (select name from kunde where a.kundnr = nr) as Kundenname,
       (select name from personal where persnr = a.persnr) as Mitarbeitername,
       (select name from personal where persnr = p.vorgesetzt) as Vorgesetzt
       from auftrag as a, personal as p
       where a.auftrnr = 2 and p.persnr = a.persnr;

select name, auftrnr, p.persnr as persnr_P, a.persnr as persnr_A from auftrag as a, personal as p where auftrnr = 2;

--1.7
select * from lager as l where l.bestand > 0
    order by l.bestand;

--1.8
select distinct l.teilnr from lieferung as l
    order by l.teilnr desc;

--1.9
select t.teilnr as Teilenummer, bezeichnung as Bezeichnung, preis as Bruttopreis from teilestamm as t
    where t.preis > 30;

--1.10
select t.einzelteilnr as teilenummer from teilestruktur as t
    where t.oberteilnr = 300001 and t.anzahl > 100;


