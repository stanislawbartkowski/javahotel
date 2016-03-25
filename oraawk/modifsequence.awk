@include "fun.awk"

{
  line = gensub(/CREATE SEQUENCE/,"CREATE OR REPLACE SEQUENCE","g",$0)   
  line = gensub(/MAXVALUE\ +[0-9]+\ /,"NO MAXVALUE ","g",line)
  printline(line)
}