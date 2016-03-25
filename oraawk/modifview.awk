{
  line = gensub(/CREATE\ +OR\ +REPLACE\ +FORCE\ +VIEW/,"CREATE OR REPLACE VIEW","g",$0)   
  line = gensub(/CREATE\ +FORCE\ +VIEW/,"CREATE OR REPLACE VIEW","g",line)   
  print line
}