#!/bin/bash
mysql -u username -p mydb < ../schema/markup.sql
make clean
make
for file in ../data/*.html
do
  java -classpath .:../vendor/* Solution "$file"
done
make clean
