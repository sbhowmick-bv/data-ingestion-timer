use notificationsreport

Create table FeedImportStatistics (importId bigint not null,
clientName varchar(255), feedType varchar(255),
jobStatus varchar(255), interactionsParsed bigint not null,
interactionsRejected bigint null,
runningDate varchar(255),
environment varchar(30),
primary key (importId)) engine=InnoDB;