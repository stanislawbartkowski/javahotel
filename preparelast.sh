TMPFILE=`mktemp`
TERR=`mktemp`
LOGFILE=/tmp/loadhive

test_env() {
log "========== TEST ================="
#URL="jdbc:hive2://re64:10000"
#URL="jdbc:hive2://re64:10000/;principal=hive/nc9128110007.kraklab.pl.ibm.com@SB.COM"
URL="jdbc:hive2://re64:10000/"
TABLE=customers
ID=customerNumber
DATABASE=salesdb
HIVE_CREATESCRIPT=/tmp/creates.sql
HIVE_UPDATESCRIPT=/tmp/updates.sql
STAGETABLE=stagedb.customers

KERB_PRINC=sb@SB.COM
KERB_PASSWORD=secret
HIVE2_KERBEROS="hive/nc9128110007.kraklab.pl.ibm.com@SB.COM"
}

appendlog() {
  local T=$1
  hdfs dfs -appendToFile $T $LOGFILE
  rm -rf $T
}

log() {
  local mess="$1"
#  echo "$mess"
  local T=`mktemp`
  echo "`hostname` $mess" >$T
  appendlog $T
}
  

loginfo() {
  log "============ START ==========="
  local T=`mktemp`

cat >$T <<-EOF
echo "HIVE_CREATESCRPTI=$HIVE_CREATESCRIPT"
echo "HIVE_UPDATESCRIPT=$HIVE_UPDATESCRIPT"
echo "STAGETABLE=$STAGETABLE"
echo "URL=$URL"
echo "KERB_PRINC=$KERB_PRINC"
echo "HIVE2_KERBEROS=$HIVE2_KERBEROS"
EOF
  appendlog $T
}

failure() {
  local T=$1
  appendlog $T
  rm -f $T
  exit 1
}

  
kerb_auth() {
  [ -z "$KERB_PRINC" ] && return
  log "Autheticate with KERBEROS"
  echo "$KERB_PASSWORD" | kinit $KERB_PRINC >$TERR 2>&1
  [ $? -ne "0" ] && failure $TERR
  log "Authenticated"
}  

createscript() {
  log "create script $HIVE_CREATESCRIPT"
  local T=`mktemp`

  cat >$T <<-EOF
   use $DATABASE;
   create table $TABLE stored as parquet as select * from stagedb.$TABLE;
EOF
  log "hdfs dfs -copyFromLocal -f $T $HIVE_CREATESCRIPT"
  hdfs dfs -copyFromLocal -f $T $HIVE_CREATESCRIPT >$TERR 2>&1
  [ $? -ne "0" ] && failure $TERR

  
  log "create script $HIVE_UPDATESCRIPT"
  cat  >$T <<-EOFF
     use $DATABASE;
     insert into $TABLE select * from stagedb.$TABLE;
EOFF
  log "hdfs dfs -copyFromLocal -f $T $HIVE_UPDATESCRIPT"
  hdfs dfs -copyFromLocal -f $T $HIVE_UPDATESCRIPT >>$TERR 2>&1
  [ $? -ne "0" ] && failure $TERR

  rm -f $T
}


callbee() {
  local query="$1"
  log "$1"
  local KPART=
  [ -n "$HIVE2_KERBEROS" ] && KPART=";principal=$HIVE2_KERBEROS"
  log "$URL$KPART"
  beeline -u "$URL$KPART" -n $USER --outputFormat=csv2 --showHeader=fale -e "$query" >$TMPFILE 2>$TERR
  [ $? -ne "0" ] && failure $TERR
  RESULT=$(<$TMPFILE)
  RESULT=`echo $RESULT`
}

#test_env
loginfo
kerb_auth
createscript

callbee "drop table if exists $STAGETABLE; use $DATABASE; show tables like '$TABLE'"
if [ -z "$RESULT" ]; then 
   echo "LASTID=-1"
   echo "EXIST=0"
   log "========== END, NO TABLE =========="
   exit 0
fi

callbee "use $DATABASE; select max($ID) from $TABLE"
echo "EXIST=1"
if [ "$RESULT" = "NULL" ]; then echo "LASTID=-1"; exit 0; fi
echo "LASTID=$RESULT"
log "========== END ============"
rm -f $TMPFILE $TERR

