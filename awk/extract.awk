# -------------------------------------------
# extracts single object from input file
# -------------------------------------------

@include "fun.awk"

BEGIN {
  OBJTYPE = recognizeobject(objtype)
  PRINT = 0  
  WASINDEX=0  
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Vv][Ii][Ee][Ww]/ {   
    if (OBJTYPE == 1 && toobjname($3) == objname) PRINT = 1
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {   
    if (OBJTYPE == 2 && toobjname($3) == objname) PRINT = 1
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Re][Ee]/ {   
    if (OBJTYPE == 3 && toobjname($3) == objname) PRINT = 1
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn]/ {   
    if (OBJTYPE == 4 && toobjname($3) == objname) PRINT = 1
}    


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Aa][Bb][Ll][Ee]/ {   
    if (OBJTYPE == 0 && toobjname($3) == objname) PRINT = 1
}    

/[Aa][Ll][Tt][Ee][Rr]\ +[Tt][Aa][Bb][Ll][Ee]/ {   
    if (OBJTYPE == 5 && toobjname($3) == objname) PRINT = 1
}    


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ii][Nn][Dd][Ee][Xx]/ {   
    if (OBJTYPE == 5) { WASINDEX=1;  CREATEINDEX=$0; }
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Uu][Nn][Ii][Qq][Uu][Ee]\ +[Ii][Nn][Dd][Ee][Xx]/ {   
    if (OBJTYPE == 5) { WASINDEX=1;  CREATEINDEX=$0; }
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Yy][Pp][Ee]/ {
    if (OBJTYPE == 6 && toobjname($3) == objname) PRINT = 1
}


/[\t|\ +]ON/ {
  if (WASINDEX==1) {
    aobjname = toobjname($2)
    if (aobjname == objname) {
      print CREATEINDEX
      PRINT=1
    }
  }    
}

/^@/ { 
     outputline()
     if (PRINT == 1) print ""
     PRINT=0
     WASINDEX=0   
}

/^GO/ {
    if (PRINT == 1) print "@"
    PRINT=0  
}

{
  outputline()
}

function outputline() {
  if (PRINT == 1) print $0
}

