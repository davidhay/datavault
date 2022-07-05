insert into Message (id, message, timestamp) values (UUID(), 'test-message', CURRENT_TIMESTAMP());

-- there should be a unique constraint on ip address and apiKey;
insert ignore into Clients (id, name, apiKey, ipAddress) values ('datavault-webapp', 'Datavault Webapp', 'datavault-webapp-', '127.0.0.1');
insert ignore into Clients (id, name, apiKey, ipAddress) values ('datavault-webappIPV6', 'Datavault Webapp IPV6', 'datavault-webapp-ipv6', '0:0:0:0:0:0:0:1');

insert ignore into Users (id, firstname, lastname, password, email) values ('admin1', 'admin user 1', 'Test', 'password1', 'admin@test.com');
insert ignore into Users (id, firstname, lastname, password, email) values ('user1', 'user 1', 'Test', 'user1pass', 'user1@test.com');

insert ignore into Groups (id, name, enabled) values ('grp-lfcs','LFCS','Y');

insert ignore into GroupOwners (group_id, user_id) values ('grp-lfcs','admin1');

-- create the admin user with 'IS Admin' role - all 31 permissions
insert ignore into Role_assignments (id,user_id, school_id, role_id) select 1,'admin1','grp-lfcs', max(id) from Roles where name='IS Admin';

-- create the non-admin user with 'DATA OWNER' role - just 6 data owner permissions
insert ignore into Role_assignments (id,user_id, school_id, role_id) select 2,'user1','grp-lfcs', max(id) from Roles where name='Data Owner';
