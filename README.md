# Dockerize IBM Streams

## Prepare IBM Streams docker friendly container

[Dockerfile](Dockerfile.st)

### Several comments

> FROM centos/systemd

CentOS with systemd running. Docker is used as lightweight virtual machine.

> RUN sed -i 's/override_install_langs*/#override_install_langs/' /etc/yum.conf

Problem described [here](https://developer.ibm.com/answers/questions/379852/job-submission-fails-with-error.html?smartspace=streamsdev). __sc__ is not working otherwise.

> RUN yum -y update

Update to the latest
> RUN yum -y install openssh-server telnet which ksh mc wget mlocate zip unzip initscripts

Install several useful tools. Ssh server to logon as docker container user
> RUN yum -y install gcc-c++ make numactl-libs openssl perl-XML-Simple xdg-utils libcurl-devel bind-utils

IBM Streams prerequisities.

> RUN yum -y install java-1.8.0-openjdk

Java JRE is used only during IBM Streams installation. Can be removed when Streams is installed. IBM Streams comes with its own IBM Java 8.
> RUN systemctl enable sshd.service

Ssh server will start.

> RUN chmod u+s `which ping`
> RUN chmod u+s `which unix_chkpwd`

Necessary for Streams console to logon.

> RUN adduser streamsadmin
> RUN echo "streamsadmin:streamsadmin" | chpasswd

Create default __streamsadmin__ user





