alter table bank_scam
    add column name_bank varchar(255) null,
    add column name_account varchar(255) null;

alter table report
    add column info_2 varchar(255) ,
    add column info_3 varchar(255) ;