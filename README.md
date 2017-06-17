# Dockerize DB2

## Prepare DB2 friendly image for hosting DB2 installation

[Dockerfile](Dockerfile.db2)

### Several comments

> FROM centos/systemd

CentOS with systemd running. Docker is used as lightweight virtual machine.
> RUN yum -y update

Update to the latest
> RUN yum -y install openssh-server telnet which ksh mc wget mlocate zip unzip initscripts

Install several useeful tools
> RUN yum -y install file libaio numactl libstdc++.so.6 pam-devel

Install DB2 prerequisities
>  VOLUME ["/tmp/i"]

Directory on the host machine containing DB2 installation image. Used only during installation, can be removed later. Do not use docker container file system to avoid bumping up the size of the container.
>RUN echo "root:root" | chpasswd
>RUN useradd db2inst1
>RUN useradd db2fenc1

Set root password and create DB2 required users.
> CMD ["/usr/sbin/init"]

Start systemd, particularly sshd. This way container can be access by means of ssh client

## Create DB2 friendly image

In /tmp/db2 directory of your host machine unpack installation files for your DB2 version. There is a whole variety of different DB2 editions. Happily, there is also free of charge version, DB2 Express-C https://www.ibm.com/us-en/marketplace/db2-express-c. This version can be used for educational purposes as well for business purpose. There are limitations attached to this edition, before using it be familiar with them.
After unpacking v11.1_linuxx64_expc.tar.gz tar (the latest version of DB2 Express-C), there should be the following directory structure:
 
```
drwxr-xr-x.  3 root root   4096 gru  7  2016 ./
drwxrwxrwt. 23 root root 212992 cze 14 10:41 ../
drwxr-xr-x.  6 bin  bin    4096 gru  7  2016 db2/
-r-xr-xr-x.  1 bin  bin    4989 gru  7  2016 db2ckupgrade*
-r-xr-xr-x.  1 bin  bin    4961 gru  7  2016 db2_deinstall*
-r-xr-xr-x.  1 bin  bin    4831 gru  7  2016 db2_install*
-r-xr-xr-x.  1 bin  bin    4795 gru  7  2016 db2ls*
-r-xr-xr-x.  1 bin  bin    4817 gru  7  2016 db2prereqcheck*
-r-xr-xr-x.  1 bin  bin    4813 gru  7  2016 db2setup*
```
### Create docker image
>  docker build -t db2centos -f Dockerfile.db2 .
### Create network (if not created)
Create a separate network, if not created already. It is convenient to have the DB2 container with static IP address.
> docker network create --subnet=172.18.0.0/16 dnet

### Create container
Create container. Here static ip address is 172.18.0.11 and hostname db2cent.sb.com
>docker run -v /tmp/db2:/tmp/i  -h db2cent.sb.com --net dnet --ip 172.18.0.11 --privileged=true --name db2centos db2centos

### Logon and install DB2
These are steps to install DB2 Express-C. In a case of any other DB2 editions, use different steps according to the version being installed.
> ssh root@172.18.0.11

> cd cd /tmp/i/expc/db2/linuxamd64/install/

Install DB2
> ./db2_install

Create DB2 instance
> cd /opt/ibm/db2/V11.1/instance/

> ./db2icrt db2inst1 -u db2fenc1

Instance is created in /home/db2inst1 directory. Instance owner is db2inst1.
### Create SAMPLE database, test if DB2 is installed properly
Logon as db2inst1
> ssh db2inst1@172.18.0.11

Start DB2 instance
> db2start

Create SAMPLE database, it can take several minutes
> db2sampl

Check if created
> db2 connect to SAMPLE

> db2 list tables

### Stop container

> docker stop db2centos

### Start container and DB2

> docker start db2centos

### Start DB2 instance
 > ssh db2inst1@172.18.0.11
 
 > db2start
 
 DB2 is up and ready.
 
### Pending task

Currently it is necessary to start DB2 instance manually every time container is restarted. Looking for a way to start DB2 automatically.




