#
# Copyright 2017 stanislawbartkowski@gmail.com 
# Licensed under the Apache License, Version 2.0 (the "License"); 
# you may not use this file except in compliance with the License. 
# You may obtain a copy of the License at 
# http://www.apache.org/licenses/LICENSE-2.0 
# Unless required by applicable law or agreed to in writing, software 
# distributed under the License is distributed on an "AS IS" BASIS, 
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
# See the License for the specific language governing permissions and 
# limitations under the License.


source `dirname $0`/export.rc

LOADSQL=db2load.sql
LOADHIVE=hive.load
# internal log for the script
logfile=$LOGDIR/log.txt
# DB2 load msg directory
MSGDIR=$EXPORT_DIR/msg
# DB2 load dump directory for rejected rows
DUMPDIR=$EXPORT_DIR/dump 
# file containing list of tables
TABLELIST=table.list
# file containing schema list
SCHEMALIST=schema.list
# file containing hive create
CREATELIST=create.list

# file with exporte table list
EXPTABLELIST=$EXPORT_DIR/lasttablename

# common log procedure
# args
#  $1 messtype : INFO, FATAL
#  $2 mess : message to be logged
log() {
  local messtype=$1
  local mess=$2
  local messlog="$(date +%m-%d-%Y\ %H:%M:%S) $messtype $mess"
  echo $messlog
  echo $messlog >>$logfile
}

logecho() {
  local messlog=$1
  echo $messlog
  echo $messlog >>$logfile
}

# information log
# args
#  $1 message
loginfo() {  
  log "INFO" "$1"
}


# fatal failure log, exit
# args
#  $1 message
logfatal() {
  log "FATAL" "$1"
  exit 1
}

# clears and prepares DB2 load stage directory
cleardir() {
  rm -rf $EXPORT_DIR
  rm -f $LOADHIVE
  mkdir -p $EXPORT_DIR
  rm -f $LOADSQL
  mkdir -p $LOGDIR
  mkdir $MSGDIR
  mkdir $DUMPDIR
  rm -f $EXPTABLELIST
}

# makes DB2 load statement
# args:
#  $1 : table name
#  $2 : file name with exported rows
#  $3 : directory for exported blob data, if exists add blob statement
#  $4 : basefilename for creating msg and dump file
makeload() {
  local tablename=$1
  local filename=$2
  local blobdir=$3
  local basefilename=$4
  echo "LOAD FROM $filename" >>$LOADSQL
  echo "OF DEL" >>$LOADSQL
  if [ -d $blobdir ]; then
    echo "LOBS FROM $blobdir" >>$LOADSQL
  fi
  echo 'MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR' >>$LOADSQL
  echo "DUMPFILE=$DUMPDIR/$basefilename" >>$LOADSQL
  echo "MESSAGES $MSGDIR/$basefilename" >>$LOADSQL
  echo "REPLACE INTO $tablename" >>$LOADSQL
  echo ";" >>$LOADSQL
  echo >>$LOADSQL  
  
#  echo "SET INTEGRITY FOR $tablename   IMMEDIATE CHECKED;" >>$LOADSQL
#  echo >>$LOADSQL      
}

changetocan() {
  local tablename=$1
  local nametolow=${tablename,,}
  local namecan=${nametolow/./_}
  echo $namecan
}


# makes DB2 load statement, prepares data for makeload
# args:
#  $1 : tablename
addloadstatement() {
  local tablename=$1
  local namecan=`changetocan $tablename`
  local filename=$EXPORT_DIR/$namecan.txt
  local blobdir=$EXPORT_DIR/$namecan
    
# prepare load
  makeload $tablename $filename $blobdir "$namecan.txt"
}

# create run time parameters for java call
# args:
#  $1 java main method
javacall() {
  while read JAR 
  do  
     CLASSPATH=$JAR:$CLASSPATH; 
  done <<!
     $( ls ./jars/*.jar ./jdbc/*.jar)
!
  
  echo "-cp $CLASSPATH $1"
}


# export single table from Oracle
# args:
#  $1 : table name
exporttable() {
  local tablename=$1
      
  EXPCONFIG=`javacall com.export.db2.main.ExportMain`
  
  ! java $EXPCONFIG $CONNPROP $tablename $EXPORT_DIR && logfatal "Failed" 
  
# prepare load
  addloadstatement $tablename
}

# extract data from all file in the list
extractfromlist() {
  EXPCONFIG=`javacall com.export.db2.main.ExportList`
  ! java $EXPCONFIG $CONNPROP $TABLELIST $EXPORT_DIR && logfatal "Failed" 
  while read tablename; do 
    addloadstatement $tablename
  done < $TABLELIST
}

# ------------------
# hive
# -----------------

addhiveloadstatement() {
  local filename=$1
  local namecan=`changetocan $filename`
  local loadname=$EXPORT_DIR/$namecan.txt
  echo "LOAD DATA LOCAL INPATH '$loadname' OVERWRITE INTO TABLE $filename ;" >>$LOADHIVE
  
}

# export single hive table from Oracle/MSSql
exporthivetable() {
  local tablename=$1
      
  EXPCONFIG=`javacall com.export.db2.main.ExportMain`
  
  ! java $EXPCONFIG $CONNPROP $tablename $EXPORT_DIR && logfatal "Failed" 
  # get table file
  exptablename=`cat $EXPTABLELIST`
  addhiveloadstatement $exptablename
}

extracthivefromlist() {
  EXPCONFIG=`javacall com.export.db2.main.ExportListMain`
  ! java $EXPCONFIG $CONNPROP $TABLELIST $EXPORT_DIR && logfatal "Failed" 
  while read tablename; do 
    addhiveloadstatement $tablename
  done < $EXPTABLELIST
}

# create hive tables definitions
extracthivetables() {
  rm -f $CREATELIST
  EXPCONFIG=`javacall com.export.db2.main.ExportListCreateHive`
  ! java $EXPCONFIG $CONNPROP $TABLELIST $CREATELIST && logfatal "Failed" 
}


#extract number of records
extractnumberofrecords() {
  local outfile=$1
  EXPCONFIG=`javacall com.export.db2.main.ExportNumberOfRecords`
  ! java $EXPCONFIG $CONNPROP $TABLELIST $outfile && logfatal "Failed"   
}

# ------------------------


# export schema, creates a file with the list of filenames from the schema
#  $1 : schema name
exportschema() {
  local schema=$1
  EXPCONFIG=`javacall com.export.db2.main.ExportSchema`
  ! java $EXPCONFIG $CONNPROP $schema $TABLELIST && logfatal "Failed" 
}

# extract schema name
extractschemas() {
  EXPCONFIG=`javacall com.export.db2.main.ExportSchemas`
  ! java $EXPCONFIG $CONNPROP $SCHEMALIST && logfatal "Failed" 
}

printhelp() {
  echo "Parameters: /action/ /params/"
  echo ""
  echo "action : listoftables params: oracle schema(s)"
  echo " Creates list of table to $TABLELIST"
  echo " Example: extract.sh listoftables DATA ACCOUNT"
  echo ""
  echo "action: extracttable params: list of tables"
  echo " Extracts data for oracle tables"
  echo " Example : extract.sh extracttable data.invoices data.lines account.customers"
  echo ""
  echo "action: extractfromlist"
  echo " Extract data from all tables found in the $TABLELIST"
  echo " Example : extract.sh extractfromlist"
  echo ""
  echo "action: extractschemas"
  echo " Extract schema names from database to $SCHEMALIST"
  echo " Example : extract.sh extractschemas"
  echo ""
  echo "action: ectracthivetables"
  echo " Create hive tables for names in $TABLELIST to $CREATELIST"
  echo " Example : extract.sh extracthivetables"
  echo ""
  echo "action: extracthivetable"
  echo " Extract list of tables as param"
  echo " Example : extract.sh extracthivetable Sales.CountryRegionCurrency Sales.CreditCard Sales.Currency"
  echo ""
  echo "action: extracthivefromlist"
  echo " Extract hive hive tables found in the $TABLELIST"
  echo " Example : extract.sh extracthivefromlist "
  echo ""
  echo "action: extractnumberofrecords"
  echo " Extracts number of records for tables found in $TABLELIST"
  echo " Example : extract.sh extractnumberofrecords /output file/ "
}


main() {
  cleardir

  action=$1
    
  case $action in
  -h|--help|-?) printhelp; exit 10;; 
  
  extracttable) 
    shift;
    while [ -n "$1" ]; do
      exporttable $1
      shift
    done
    ;;
    
  listoftables) 
    shift;
    rm -f $TABLELIST
    while [ -n "$1" ]; do
      exportschema $1
      shift
    done
    ;;
    
  extractfromlist) 
    extractfromlist
    ;;
    
  extractschemas) 
    extractschemas
    ;;
    
  extracthivetables) 
    extracthivetables
    ;;
    
  extracthivetable)  
    shift;
    while [ -n "$1" ]; do
      exporthivetable $1
      shift
    done
    ;;
    
  extracthivefromlist) 
    extracthivefromlist
    ;;
    
  extractnumberofrecords) 
    [ -z "$2" ] && logfatal "Output file name parameter is empty"
    extractnumberofrecords $2
    ;;        
    
  *) printhelp; exit 10;;
  esac

  echo "Done"
}

main $@
