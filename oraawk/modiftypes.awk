BEGIN { PRINT = 1 }

function outputline(line) {
  if (PRINT == 1) print line
}

/^@/ { 
     PRINT = 0
     print "WITH WEAK TYPE RULES"
     print "@"
}

{
  line = gensub(/NOT NULL/,"","g",$0)   
  line = gensub(/NULL/,"","g",line)   
  line = gensub(/FROM/,"AS","g",line)
  line = gensub(/ [Bb][Ii][Tt]/," SMALLINT","g",line)

  outputline(line)ls types
}