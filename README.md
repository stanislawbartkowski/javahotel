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

In /tmp/i directory of your host machine unpack installation files for your DB2 version. There is a whole variety of different DB2 editions. Happily, there is also free of charge version, DB2 Express-C https://www.ibm.com/us-en/marketplace/db2-express-c. This version can be used for educational purposes as well for business purpose. There are limitations attached to this edition, before using it be familiar with them.





