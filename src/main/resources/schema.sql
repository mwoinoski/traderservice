drop table AUDIT if exists;
create table AUDIT (RECORD_ID UUID NOT NULL, OPERATION varchar NOT NULL, AUDIT_TIMESTAMP BIGINT NOT NULL);

drop table MESSAGE if exists;
create table MESSAGE (MESSAGE_ID UUID NOT NULL, FROM_USER varchar, TO_USER varchar, TEXT varchar NOT NULL,
                      TIMESTMP BIGINT NOT NULL);

