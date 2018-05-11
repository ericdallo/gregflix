DELIMITER $$
create PROCEDURE createUser(username varchar(30), name varchar(50), password varchar(70))
  MODIFIES SQL DATA
begin
  insert into user values(null, username, name, password, null, null);
end;$$

DELIMITER $$
create PROCEDURE audit()
    select count(la.id) as logins,
        (select count(distinct(la2.ip))  from login_audit la2 where la2.user_id = u.id and la2.created_at > curdate() - INTERVAL DAYOFWEEK(curdate())+14 DAY ) as different_ips,
        u.name,
        la.device as last_device,
        (select created_at from login_audit where user_id = u.id order by id desc limit 1) as last_login_at 
        from login_audit la 
        inner join user u on (la.user_id = u.id) 
        where u.id != 1 
        and la.created_at > curdate() - INTERVAL DAYOFWEEK(curdate())+14 DAY 
        group by u.id 
        order by logins desc, la.created_at desc;
$$
