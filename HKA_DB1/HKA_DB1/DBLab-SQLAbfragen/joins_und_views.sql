--2.1
select a.auftrnr, a.datum from auftrag as a
    where a.kundnr in (select k.nr from kunde as k where k.name = ('Fahrrad Shop'));

--2.2
select a.auftrnr, a.datum from auftrag as a
    where a.kundnr = some (select k.nr from kunde as k where k.name = 'Fahrrad Shop');

--2.3
select a.auftrnr, a.datum from auftrag as a
    where exists (select k.nr from kunde as k where k.name = 'Fahrrad Shop' and a.kundnr = nr);

--2.4
select distinct a.kundnr,
                count(auftrnr) as anzahl,
                (select datum from auftrag where kundnr = a.kundnr order by datum fetch first 1 row only) as von,
                (select datum from auftrag where kundnr = a.kundnr order by datum desc fetch first 1 row only) as bis from auftrag as a
group by a.kundnr;

--2.5
select distinct a.kundnr,
                count(auftrnr) as anzahl,
                (select datum
                 from auftrag
                 where kundnr = a.kundnr
                 order by datum
                 fetch first 1 row only) as von,
                (select datum
                 from auftrag
                 where kundnr = a.kundnr
                 order by datum desc
                 fetch first 1 row only) as bis
    from auftrag as a
    group by a.kundnr having count(auftrnr) = 1;

--2.6
select k.nr, k.name, a.auftrnr
from auftrag as a
join kunde as k on k.nr = a.kundnr
order by nr;

--2.7
select k.nr, k.name, a.auftrnr, p.name as mitarbeiter
from kunde as k
join auftrag as a on k.nr = a.kundnr
join personal as p on p.persnr = a.persnr
order by nr;

--2.8
select k.name, sum(p.gesamtpreis) as gesamtpreis
from kunde as k
join auftrag as a on k.nr = a.kundnr
join auftragsposten as p on p.auftrnr = a.auftrnr
group by k.name
order by gesamtpreis desc
fetch first 1 row only;

--2.9
with alle_kunde as (
    select k.name, sum(p.gesamtpreis) as gesamtpreis
    from kunde as k
    join auftrag as a on k.nr = a.kundnr
    join auftragsposten as p on p.auftrnr = a.auftrnr
    group by k.name
)

select * from alle_kunde as a
order by a.gesamtpreis desc
fetch first 1 row only;

--2.10
create view KundenUmsatz as
    select k.name, sum(p.gesamtpreis) as gesamtpreis
    from kunde as k
    join auftrag as a on k.nr = a.kundnr
    join auftragsposten as p on p.auftrnr = a.auftrnr
    group by k.name;

select * from KundenUmsatz;

--2.11
drop view KundenUmsatz;