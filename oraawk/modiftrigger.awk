{
  line = gensub(/CREATE\ +TRIGGER/,"CREATE OR REPLACE TRIGGER","g",$0)   
  print line
}