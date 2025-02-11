create table PendingVaults (
    id varchar(36) not null,
    affirmed bit not null, billingType TEXT,
    creationTime datetime, description TEXT,
    estimate TEXT, grantEndDate date,
    name TEXT, notes TEXT, reviewDate date,
    sliceID TEXT, subunit TEXT, authoriser TEXT, schoolOrUnit TEXT, projectID TEXT,
    version bigint not null,
    group_id varchar(180),
    retentionPolicy_id integer,
    user_id varchar(36),
    primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `hibernate_sequence` (
    `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `PendingDataCreators` (
    `id` varchar(36) NOT NULL,
    `name` text NOT NULL,
    `version` bigint(20) NOT NULL,
    `pendingVault_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO hibernate_sequence (next_val) VALUES (1);
alter table PendingVaults add constraint FK1oqxrihj71uwiynk4m8u0n0vj foreign key (group_id) references Groups (id);
alter table PendingVaults add constraint FKtqmx003465315spj99suwf1ta foreign key (retentionPolicy_id) references RetentionPolicies (id);
alter table PendingVaults add constraint FKjj6cuk6m88qn7th5df64ui3cw foreign key (user_id) references Users (id);
alter table PendingDataCreators add constraint FK1oncea34ucfe9u54oi2mmj3qw foreign key (pendingVault_id) REFERENCES PendingVaults (id);
ALTER TABLE Role_assignments ADD COLUMN pending_vault_id varchar(36) default null;
ALTER TABLE Role_assignments ADD CONSTRAINT `FK_Pending_Vault_ID` FOREIGN KEY (`pending_vault_id`) REFERENCES `PendingVaults` (`id`);
ALTER TABLE Role_assignments ADD UNIQUE KEY `UK_Pending_Vault_ID` (`role_id`,`user_id`,`pending_vault_id`);
ALTER TABLE PendingVaults add column contact TEXT;
ALTER TABLE PendingVaults add column pureLink bit not null default false;
ALTER TABLE PendingVaults add column confirmed bit not null default false;
ALTER TABLE Vaults MODIFY snapshot longtext NULL DEFAULT null;

CREATE TABLE `DataCreators` (
                                       `id` varchar(36) NOT NULL,
                                       `name` text NOT NULL,
                                       `version` bigint(20) NOT NULL,
                                       `vault_id` varchar(36) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE Vaults add column affirmed bit not null default false;
update Vaults set affirmed = true where affirmed is false;
ALTER TABLE Vaults add column estimate TEXT;
ALTER TABLE Vaults add column notes TEXT;
ALTER TABLE Vaults add column pureLink bit not null default false;
ALTER TABLE Vaults add column contact TEXT;

ALTER TABLE BillingInfo add column billingType TEXT NOT NULL;
ALTER TABLE BillingInfo add column sliceID TEXT;
ALTER TABLE BillingInfo add column projectTitle TEXT;
/* might have to drop the not null and add back in do do this (worked fine when I deployed to demo though) */
update BillingInfo set billingType = '4' where billingType = '';

ALTER TABLE PendingVaults CHANGE projectID projectTitle TEXT;

