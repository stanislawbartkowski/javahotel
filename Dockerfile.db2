FROM centos/systemd

MAINTAINER "sb" <stanislawbartkowski@gmail.com>

# update system, just in case
RUN yum -y update
# install sshd and several tools
RUN yum -y install openssh-server telnet which ksh mc wget mlocate zip unzip initscripts
# install DB2 dependencies
RUN yum -y install file libaio numactl libstdc++.so.6 pam-devel

# installation directory in host file system
VOLUME ["/tmp/i"]

# users and password
RUN echo "root:root" | chpasswd
RUN useradd db2inst1
RUN useradd db2fenc1

# start systemd
CMD ["/usr/sbin/init"]
