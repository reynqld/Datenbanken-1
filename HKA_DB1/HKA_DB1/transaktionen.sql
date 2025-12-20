--3
begin transaction;

select count(*) from lieferung;

delete from lieferant;

-- select * from lieferant;
-- select * from lieferung;

select count(*) from lieferung;

rollback;

-- select * from lieferant;
-- select * from lieferung;

select count(*) from lieferung;