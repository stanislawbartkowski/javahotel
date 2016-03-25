# -------------------------------------------
# extracts single object from input file
# -------------------------------------------

@include "fun.awk"

BEGIN {
  OBJTYPE = recognizeobject(objtype)
  PRINT = 0  
  WAITFORUSING=0
}

function finishobject() {
    if (PRINT == 1) printeo()
    PRINT=0  
}


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Aa][Bb][Ll][Ee]/ {
    if (toupper($1) == "CREATE") {
      finishobject()
      if (OBJTYPE == 0 && toobjname($3) == objname) PRINT = 1
    }
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ii][Nn][Dd][Ee][Xx]/ {   
    finishobject()
    if (OBJTYPE == 0 && toobjname($5) == objname) PRINT = 1
}    

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Uu][Nn][Ii][Qq][Uu][Ee]\ +[Ii][Nn][Dd][Ee][Xx]/ {   
    finishobject()
    if (OBJTYPE == 0 && toobjname($6) == objname) PRINT = 1
}


/[Aa][Ll][Tt][Ee][Rr]\ +[Tt][Aa][Bb][Ll][Ee].+[Aa][Dd][Dd]\ +[Cc][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt].+[Pp][Rr][Ii][Mm][Aa][Rr][Yy]\ +[Kk][Ee][Yy]/ {
    finishobject()
    if (OBJTYPE == 0 && toobjname($3) == objname) {
       INDEXLINE=$0
       WAITFORUSING=1
    }
}


/[Aa][Ll][Tt][Ee][Rr]\ +[Tt][As][Bb][Ll][Ee].+[Aa][Dd][Dd]\ +[Cc][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt].+[Ff][Oo][Rr][Ee][Ii][Gg][Nn]\ +[Kk][Ee][Yu]/ {
    finishobject()
    if (OBJTYPE == 5 && toobjname($3) == objname) PRINT = 1
}

/[Aa][Ll][Tt][Ee][Rr]\ +[Tt][As][Bb][Ll][Ee].+[Aa][Dd][Dd]/ {
    finishobject()
    if (OBJTYPE == 0 && toobjname($3) == objname) PRINT = 1
}



/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Mm][Aa][Tt][Ee][Rr][Ii][Aa][Ll][Ii][Zz][Ee][Dd]\ +[Vv][Ii][Ee][Ww]/ {
    finishobject()
    if (OBJTYPE == 1 && toobjname($4) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Vv][Ii][Ee][Ww]/ {
    finishobject()
    if (OBJTYPE == 1 && toobjname($4) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Oo][Rr][Cc][Ee]\ +[Vv][Ii][Ee][Ww]/ {
    finishobject()
    if (OBJTYPE == 1 && toobjname($4) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Oo][Rr]\ +[Rr][Ee][Pp][Ll][Aa][Cc][Ee]\ +[Ff][Oo][Rr][Cc][Ee]\ +[Vv][Ii][Ee][Ww]/ {
    finishobject()
    if (OBJTYPE == 1 && toobjname($6) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Yy][Pp][Ee]/ {
    finishobject()
    if (OBJTYPE == 6 && toobjname($3) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Re][Ee]/ {
    finishobject()
    if (OBJTYPE == 3 && toobjname($3) == objname) PRINT = 1
}


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ss][Ee][Qq][Uu][Ee][Nn][Cc][Ee]/ {
    finishobject()
    if (OBJTYPE == 9 && toobjname($3) == objname) PRINT = 1
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {
    finishobject()
    if (OBJTYPE == 2 && toobjname($3) == objname) PRINT = 1
}

/^\ *[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Oo][Rr]\ +[Rr][Ee][Pp][Ll][Aa][Cc][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {
    finishobject()
    if (OBJTYPE == 2 && toobjname($5) == objname) PRINT = 1
}


/;\ *$/ {
    if ((OBJTYPE == 1 && PRINT == 1) ||
        (OBJTYPE == 6 && PRINT == 1) ||
        (OBJTYPE == 9 && PRINT == 1))
        {
     line = gensub(/;/,"","g",$0)
     printline(line)
     finishobject()
    }      
}  
 

/;/ {
   if (OBJTYPE == 5 && PRINT == 1) {
     line = gensub(/ENABLE/,"","g",$0)   
     line = gensub(/;/,"","g",line)   
     printline(line)
     PRINT = 0
     printeo()
   }
   if (OBJTYPE == 0 && PRINT == 1) {
     line = gensub(/ENABLE/,"","g",$0)   
     line = gensub(/;/,"","g",line)   
     printline(line)
     PRINT = 0
     printeo()
   }

}   
  
/[Uu][Ss][Ii][Nn][Gg]\ +[Ii][Nn][Dd][Ee][Xx]/ {
  if (WAITFORUSING == 1) {
    printline("DROP INDEX " $3); printeo()
    printline(INDEXLINE); printeo()
  }
  WAITFORUSING=0
}  
   
/^\ *\)/ {
    if (OBJTYPE == 0 && PRINT == 1) {
      print ") IN PRIMARY"
      finishobject();
    }
}


# could be trigger terminator
/@\ *$/ {
    if (OBJTYPE == 2 && PRINT == 1) {
      finishobject();
    }
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Aa][Cc][Kk][Aa][Gg][Ee]/ {
  finishobject()
  if (OBJTYPE == 7 && toobjname($3) == objname && toupper($3) != "BODY") PRINT = 1
  if (OBJTYPE == 8 && toobjname($4) == objname && toupper($3) == "BODY") PRINT = 1
}    

# reject comment, after / should be end of line
/^\/ *$/ {
  if (OBJTYPE == 7 && PRINT == 1) finishobject()
  if (OBJTYPE == 8 && PRINT == 1) finishobject()
  if (OBJTYPE == 2 && PRINT == 1) finishobject()
  if (OBJTYPE == 3 && PRINT == 1) finishobject()
}     


{
  outputline()
}
