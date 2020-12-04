delimiter |
drop procedure if exists biggo |
create procedure biggo()
	begin
		set @date=(select curdate());
		update DataBaseClass.items set status=1 where endtime <= @data and state = 0;
    end
|

set global event_scheduler = 1;
create event if not exists events on schedule every 1 second on completion preserve do call biggo();
alter event events on completion preserve enable;


delimiter |
drop trigger if exists deal |
create trigger deal after update on DataBaseClass.items for each row 
begin
	update DataBaseClass.user set balance = (select (balance + income*0.98) from (select user.id,balance,sum(bidprice)as income from user join items on items.sellerid=user.id and items.state = 1 group by user.id)as a where a.id=user.id);
    update DataBaseClass.admin set balance = (select income*0.02 from (select user.id,balance,sum(bidprice)as income from user join items on items.sellerid=user.id and items.state = 1 group by user.id)as a where a.id=user.id) + admin.balance;
    update DataBaseClass.user set numberOfItemsSold = (select count from items where sellerid = user.id);
    update DataBaseClass.user set balance = (select (balance - cost) from (select id,balance,sum(bidprice)as cost from user join (select bider,bidprice from items join bidinformation on price = bidprice where items.state=1) as buyer on buyer.bider = user.id group by bider)as b);
    update DataBaseClass.user set numberOfItemsPurchased = (select count from items join bidinformation on buyprice = price where user.id = bidinformation.bider);
    update DataBaseClass.user set state = 2 where state = 1;
end
|
|

delimiter |
drop trigger if exists increase |
create trigger deal after insert on DataBaseClass.bidinformation for each row
begin
	update DataBaseClass.items bidprice = (select price from bidinformation order by price desc limit 1 where itemid = items.id);
end
|
|