# -------------------------------------
# split input file and apply modifiers
# -------------------------------------

@include "fun.awk"

BEGIN {
   # EXTRACTTYPE = 0 : tables only
   # EXTRACTTYPE = 1 : MSSQL views, procedures, functions and triggers
   if (extracttype == "TABLES") EXTRACTTYPE = 0
   else if (extracttype == "ALLOTHERS") EXTRACTTYPE = 1
   else {
      print "extracttype value invalid " extracttype " : TABLES or ALLOTHERS expected."
      exit
   }
      
   system ("rm -rf tables views procedures functions triggers constraints tempconstraints tempviews temptriggers temp1triggers tempprocedures tempfunctions temptables temptypes temptypes1 types")
   system ("mkdir  tables views procedures functions triggers constraints tempconstraints tempviews temptriggers temp1triggers tempprocedures tempfunctions temptables temptypes temptypes1 types")
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
ls types


/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Aa][Bb][Ll][Ee]\ +/ { 
    if (EXTRACTTYPE == 0) extractobj1("TABLE",$3,"tables")   
    if (EXTRACTTYPE == 0) extractobj1("CONSTRAINT",$3,"constraints")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Vv][Ii][Ee][Ww]/ {
     if (EXTRACTTYPE == 1) extractobj2("VIEW",$3,"tempviews","modifmsobj","views")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ {
     if (EXTRACTTYPE == 1) extractobj2("TRIGGER",$3,"temptriggers","modifmsobj","triggers")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Re][Ee]/ {
     if (EXTRACTTYPE == 1) extractobj2("PROCEDURE",$3,"tempprocedures","modifmsobj","procedures")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn]/ {
     if (EXTRACTTYPE == 1) extractobj2("FUNCTION",$3,"tempfunctions","modifmsobj","functions")
}

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Yy][Pp][Ee]/ {
     if (EXTRACTTYPE == 1) extractobj("TYPES",$3,"temptypes","modifmsobj","temptypes1","modiftypes","types")
}
