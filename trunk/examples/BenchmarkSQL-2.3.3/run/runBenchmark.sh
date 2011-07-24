#PROF=-agentpath:/usr/local/netbeans-7.0/profiler/lib/deployed/jdk15/linux-amd64/libprofilerinterface.so=/usr/local/netbeans-7.0/profiler/lib,5140
JAVA_HOME=/home/opt/jdk1.6.0_25/jre
$JAVA_HOME/bin/java -cp ../lib/edb-jdbc14-8_0_3_14.jar:../lib/ojdbc14-10.2.jar:../lib/postgresql-8.0.309.jdbc3.jar:../lib/db2jcc.jar:../dist/BenchmarkSQL-2.3.jar $PROF -DNoW=10 -DNoT=10 -DNoM=5 -Dprop=$1 client.jTPCC
