# -------------------------------
# Common functions
# -------------------------------

# add element to list
#  list : list to be extended
#  elem : element to be added
function addlist(list, elem) {
   for (i in list) count++;
   list[count] = elem;
}

# recognize command line parameter object type
#    objtype : name of the object type or ALL
#    returns : 0 - TABLE, 1 - VIEW, 2 - TRIGGER, 3 - PROCEDURE, 4 - FUNCTION, 5 - CONSTRAINT, 6 - TYPE, 10 - ALL
#  if objtype is notrecognizable the prints error code and exits

function recognizeobject(objtype) {
  #OBJTYPE: 0 - TABLE, 1 - VIEW, 2 - TRIGGER, 3 - PROCEDURE, 4 - FUNCTION, 10 - ALL
  # print objtype
  
  if (objtype == "") {
    print "objtype value expected."
    exit;
  }
  if (objtype == "TABLE") OBJTYPE=0
  else if (objtype == "VIEW") OBJTYPE=1
  else if (objtype == "TRIGGER") OBJTYPE=2
  else if (objtype == "PROCEDURE") OBJTYPE=3
  else if (objtype == "FUNCTION") OBJTYPE=4
  else if (objtype == "CONSTRAINT") OBJTYPE=5
  else if (objtype == "TYPES") OBJTYPE=6
  else if (objtype == "ALL") OBJTYPE=10
  else {
    print "Invalid objtype value:",objtype,"Expected : TABLE, VIEW, TRIGGER, PROCEDURE, FUNCTION, CONSTRAINT, TYPES or ALL"
    exit
  }  
  return OBJTYPE
}

# change object name to DB2 recognizable, removes, for instance [ or ]
#   name : MSSQL object name
#   returns: DB2 recognizable object name
function toobjname(name) {
   obj = gensub(/\"|\ |\t|\r|\[|\]|\(|\)/,"","g",name);
   # remove also parameter from function or procedure header
   obj = gensub(/@.*/,"","g",obj);
   return obj   
}
