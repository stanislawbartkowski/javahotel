BEGIN { PRINT = 1 }

function outputline(line) {
  if (PRINT == 1) print line
}

/^@/ { 
     PRINT = 0
     print $0
}

{
  line = gensub(/\[|\]/,"","g",$0)   
  line = gensub(/@/,"v","g",line)   
  line = gensub(/+/,"||","g",line)   
  line = gensub(/ISNULL/,"COALESCE","g",line)   

  outputline(line)
}

