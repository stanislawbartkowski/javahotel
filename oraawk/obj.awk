# -------------------------------------
# split input file and apply modifiers
# -------------------------------------

@include "fun.awk"

BEGIN {
   # EXTRACTTYPE = 0 : tables only
   # EXTRACTTYPE = 1 : views, procedures, functions and triggers
  
   if (extracttype != "") {
     if (extracttype == "TABLES") EXTRACTTYPE = 0
     else if (extracttype == "ALLOTHERS") EXTRACTTYPE = 1     
     else if (extracttype == "LIST") {
       EXTRACTTYPE = 2
       OBJTYPE = recognizeobject(objtype);
     }       
     else {
      print "extracttype value invalid " extracttype " TABLES, LIST, ALLOTHERS expected "
      exit
     }
   }
   else {
     EXTRACTTYPE = -1
     OBJTYPE = recognizeobject(objtype);
   }
      
   system ("rm -rf tables temptables")
   system ("mkdir  tables temptables")
   system ("rm -rf foreign")
   system ("mkdir  foreign")
   system ("rm -rf packages temppackage")
   system ("mkdir  packages temppackage")
   system ("rm -rf packagesbody")
   system ("mkdir  packagesbody")
   system ("rm -rf views tempviews")
   system ("mkdir  views tempviews")
   system ("rm -rf types")
   system ("mkdir  types")
   system ("rm -rf functions")
   system ("mkdir  functions")
   system ("rm -rf procedures")
   system ("mkdir  procedures")
   system ("rm -rf sequences tempsequences")
   system ("mkdir  sequences tempsequences")
   system ("rm -rf triggers temptriggers")
   system ("mkdir triggers temptriggers")
}


# convert object name to file name
#   o : object name
#   returns : file name corresponding to object name
function tofilename(o) {
    obj = tolower(toobjname(o))
    filename = gensub(/\./,"_","g",obj) ".sql"
    return filename
}


function fileempty(outputname) {
   cmd = "stat --printf=\"%s\" "  outputname
   print cmd
   cmd | getline filesize
   if (filesize == 0) system("rm " outputname)
   return filesize
}  

# extracts object from input file and apply modifiers
#  objtype : object type
#  objname : object name
#   folder : output folder
#   modif,newfolder : modifier 1
#   modif1,newfilder1 : modifier 2
function extractobj(objtype,objname,folder,modif,newfolder,modif1,newfolder1) {
   outputname = folder "/" tofilename(objname)
   cmd = "awk -f extract.awk -v objtype="  objtype " -v objname=" toobjname(objname)  " "  FILENAME " >" outputname
   print cmd
   system(cmd)
   filesize = fileempty(outputname)
   if (filesize == 0) return
   if (modif != "") {
     outputname = newfolder "/" tofilename(objname)
     cmd = "awk -f " modif ".awk " folder "/"  tofilename(objname) " >" outputname
     print cmd
     system(cmd)
     # remove empty file
     cmd = "stat --printf=\"%s\" "  outputname
     print cmd
     cmd | getline filesize
     if (filesize == 0) system("rm " outputname)
     if (filesize > 0 && modif1 != "") {
       cmd = "awk -f " modif1 ".awk " outputname " >" newfolder1 "/"  tofilename(objname)
       print cmd
       system(cmd)
     }
               
   }
    
}

function extractobj1(objtype,objname,folder) {
    extractobj(objtype,objname,folder,"","","","")
}

function extractobj2(objtype,objname,folder,modif,newfolder) {
    extractobj(objtype,objname,folder,modif,newfolder,"","")
}


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Aa][Bb][Ll][Ee]\ +/ { 
    if (EXTRACTTYPE == 0 || OBJTYPE == 0) extractobj2("TABLE",$3,"temptables","modiftable","tables")   
    if (EXTRACTTYPE == 0 || OBJTYPE == 0) extractobj1("FOREIGN",$3,"foreign")
}

# ============================================


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Vv][Ii][Ee][Ww]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 1 ) extractobj2("VIEW",$3,"tempviews","modifview","views")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Mm][Aa][Tt][Ee][Rr][Ii][Aa][Ll][Ii][Zz][Ee][Dd]\ +[Vv][Ii][Ee][Ww]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 1) extractobj2("VIEW",$4,"tempviews","modifview","views")
}


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Oo][Rr][Cc][Ee]\ +[Vv][Ii][Ee][Ww]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 1) extractobj2("VIEW",$4,"tempviews","modifview","views")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Oo][Rr]\ +[Rr][Ee][Pp][Ll][Aa][Cc][Ee]\ +[Ff][Oo][Rr][Cc][Ee]\ +[Vv][Ii][Ee][Ww]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 1) extractobj2("VIEW",$6,"tempviews","modifview","views")
}

/^\ *[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 2) extractobj2("TRIGGER",$3,"temptriggers","modiftrigger","triggers")
}

/^\ *[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Oo][Rr]\ +[Rr][Ee][Pp][Ll][Aa][Cc][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 2) extractobj2("TRIGGER",$5,"temptriggers","modiftrigger","triggers")
}


/^\ *[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Re][Ee]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 3) extractobj1("PROCEDURE",$3,"procedures")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 4) extractobj1("FUNCTION",$3,"functions")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Yy][Pp][Ee]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 6) extractobj1("TYPES",$3,"types")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ss][Ee][Qq][Uu][Ee][Nn][Cc][Ee]/ {
     if (EXTRACTTYPE == 1 || OBJTYPE == 9) extractobj2("SEQUENCE",$3,"tempsequences","modifsequence","sequences")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Aa][Cc][Kk][Aa][Gg][Ee]/ {
     extractp = ((EXTRACTTYPE == 1 || OBJTYPE == 7)) && (EXTRACTTYPE != 2)
     extractpb = ((EXTRACTTYPE == 1 || OBJTYPE == 8)) && (EXTRACTTYPE != 2)
     if (extractp && toupper($3) != "BODY") extractobj2("PACKAGE",$3,"temppackage","modifpackage","packages");
     if (extractpb && toupper($3) == "BODY") extractobj2("PACKAGEBODY",$4,"temppackage","modifpackage","packagesbody");
     if (EXTRACTTYPE == 2) {
       if (OBJTYPE == 7 && toupper($3) != "BODY") print tolower($3)
       if (OBJTYPE == 8 && toupper($3) == "BODY") print tolower($4)
     }	 
}     
  