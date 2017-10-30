#!/bin/bash

#This file written by Don Landrum on October 29th, 2017

mysql -u "$1" -p < ../schema/markup.sql
make > /dev/null
for file in ../data/*.html
do
  java -classpath .:../vendor/* Solution "$file" "$1" "$2" &>/dev/null
done
java -classpath .:../vendor/* Interaction "$1" "$2"
make clean > /dev/null
