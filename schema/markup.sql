/* This file written by Don Landrum on October 29th, 2017. */

DROP DATABASE IF EXISTS dldb;
CREATE DATABASE dldb;
USE dldb;
DROP TABLE IF EXISTS markup;
CREATE TABLE markup (
  id char(15) NOT NULL DEFAULT '',
  timestamp mediumtext,
  date char(12) NOT NULL DEFAULT '',
  score int(5) NOT NULL DEFAULT 0
);
