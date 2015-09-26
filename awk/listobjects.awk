# -----------------------------------------
# list objects found in the input script
# object recognized and listed : TABLES, VIEWS, FUNCTIONS, PROCEDURES and TRIGGERS. ALL : all objects
#  
# example : awk -v objtype=ALL  -f listobjects.awk iscript.sql
# --------------------------------------

@include "fun.awk"

BEGIN {
    OBJTYPE = recognizeobject(objtype)
}


function printres(title, list, objexpected) {
  if (OBJTYPE == 10) {
    print("");
    print("");
    print(title);
    print("--------------");
  }
  if (OBJTYPE == 10 || OBJTYPE == objexpected) {
    for (i in list) {
        obj = list[i];
        print(toobjname(obj));
    }
  }
}  

/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn]/ { addlist(funs,$3);  }
/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Aa][Bb][Ll][Ee]/ { addlist(tables,$3); }
/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Vv][Ii][Ee][Ww]/ { addlist(views,$3);  }
/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Re][Ee]/ { addlist(procs,$3); }
/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Rr][Ii][Gg][Gg][Ee][Rr]/ { addlist(triggers,$3); }
/[Cc][Rr][Ee][Aa][Tt][Ee]\ +[Tt][Yy][Pp][Ee]/ { addlist(types,$3); }

END {
   printres("TABLES",tables,0);
   printres("VIEWS",views,1);
   printres("FUNCTIONS",funs,4);
   printres("PROCEDURES",procs,3);   
   printres("TRIGGERS",triggers,2);   
   printres("TYPES",types,2);   
}