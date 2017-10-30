#!/bin/bash
make clean > /dev/null
mysql -u dummyuser -p mydb < ../schema/markup.sql
make > /dev/null
for file in ../data/*.html
do
  java -classpath .:../vendor/* Solution "$file" &>/dev/null
done
java -classpath .:../vendor/* Interaction
