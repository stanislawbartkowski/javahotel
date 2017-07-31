source hostsproc.rc
DIRS=temp_uhosts
SCRIPTNAME=getusers.sh
LOCALSCRIPT=local.users

IOPUSERS="flume hadoop ambari-qa hbase hdfs knox oozie spark zookeeper ams hcat hive kafka mapred solr sqoop titan yarn"
BIUSER="bigsheets tauser uiuser dsmuser bigsql"

USERS="$IOPUSERS $BIUSER"

clean() {
  rm -rf $DIRS
  mkdir -p $DIRS
  rm -f $SCRIPTNAME
  touch $SCRIPTNAME
  chmod 700 $SCRIPTNAME
}

createscript() {
  for user in $USERS; do
    log $user
    echo "echo $user `id -u $user`" >>$SCRIPTNAME
  done
}

gatherusers() {

    first=1
    while read host;  do
        #ignore empty lines
        if [ $first -eq "1" ]; then
         first=0
         ./$SCRIPTNAME >$DIRS/$LOCALSCRIPT
         continue
        fi
        runhost $host $SCRIPTNAME $DIRS/$host.users
   done <`dirname $0`/nodes.txt
}

comparethem()
{
    log "========= COMPARE ============"
    first=1
    while read host;  do
        #ignore empty lines
        if [ $first -eq "1" ]; then
         first=0
         continue
        fi
        log "Compare $host"
        if diff $DIRS/$LOCALSCRIPT $DIRS/$host.users; then 
           log "OK"
        else
           log "FAILED"
        fi
   done <`dirname $0`/nodes.txt

}

clean
createscript
gatherusers
comparethem


