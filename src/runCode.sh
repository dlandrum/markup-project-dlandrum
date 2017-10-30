#!/bin/bash
mysql -u dummyuser -p < ../schema/markup.sql
make > /dev/null
for file in ../data/*.html
do
  java -classpath .:../vendor/* Solution "$file" &>/dev/null
done
java -classpath .:../vendor/* Interaction
make clean > /dev/null
