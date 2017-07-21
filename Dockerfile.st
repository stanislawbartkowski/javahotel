FROM centos/systemd

MAINTAINER "SB" <stanislawbartkowski@gmail.com>

RUN sed -i 's/override_install_langs*/#override_install_langs/' /etc/yum.conf
RUN yum -y update
RUN yum -y install openssh-server openssh-clients telnet which net-tools ksh mc wget mlocate zip unzip initscripts sudo
RUN yum -y install gcc-c++ make numactl-libs openssl perl-XML-Simple xdg-utils libcurl-devel bind-utils
RUN yum -y install java-1.8.0-openjdk
RUN systemctl enable sshd.service
RUN chmod u+s `which ping`
RUN chmod u+s `which unix_chkpwd`
RUN echo "root:root" | chpasswd
RUN adduser streamsadmin
RUN echo "streamsadmin:streamsadmin" | chpasswd

CMD ["/usr/sbin/init"]
