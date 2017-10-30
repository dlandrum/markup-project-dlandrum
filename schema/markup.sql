DROP TABLE IF EXISTS markup;
CREATE TABLE markup (
  id char(15) NOT NULL DEFAULT '',
  timestamp mediumtext,
  score int(5) NOT NULL DEFAULT 0
);
