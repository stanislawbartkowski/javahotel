source hostsproc.rc

#runallhosts checkfirewall.sh
#runallhostscommand "ls"
#runallhostscommand "hostname"
#runallhostscommand "df -h; nproc; free -h"
#runallhostscommand "ifconfig | grep inet6"
#copyfiles "/etc/sysctl.conf"
#runallhostscommand "sysctl -p ; ifconfig | grep inet6"
#runallhostscommand "cat /sys/kernel/mm/transparent_hugepage/enabled"
#copyfiles "/etc/rc.local"
#runallhostscommand "chmod 755 /etc/rc.d/rc.local; /etc/rc.local"
#runallhostscommand "blkid"
#runallhostscommand "yum list installed | grep -i ambari"
#copyfiles "/etc/security/limits.conf"
#runallhostscommand "ntpstat; echo $?"
#runallhostscommand "getenforce"
#runallhostscommand "ls -l /etc/localtime"
#runallhostscommand "yum repolist"
#copyfiles "/etc/yum.repos.d/IOP.repo"
#copyfiles "/etc/yum.repos.d/IOP-UTILS.repo"

#runallhostscommand "yum -y install zip unzip bind-utils nc psmisc '/lib/lsb/init-functions' fuse fuse-libs python-devel ksh libcgroup-tools perl-Sys-Syslog"


#runallhostscommand "yum -y install mariadb"
#runallhostscommand "yum -y install mariadb-server"
#runallhostscommand "yum -y install mysql-connector-java"
#runallhostscommand "yum -y install yum-utils"

#copyfiles "id_rsa.pub"

#runallhostscommand "cat id_rsa.pub >>.ssh/authorized_keys"

#runallhostscommand "yum -y install mlocate"
#runallhostscommand "yum -y install redhat-lsb"
#runallhostscommand "yum-complete-transaction"
#runallhostscommand "groupadd biuser"
#runallhostscommand "adduser sb; usermod -a -G  biuser sb"

#copyfiles "/etc/sqoop/conf/sqoop-site.xml"

#runallhostscommand "cat /tmp/output; cat /tmp/variable"
#runallhostscommand "rm /tmp/output; rm /tmp/variable"

#runallhostscommand "yum-config-manager --enable rhel-7-server-optional-rpms"
#runallhostscommand "yum -y install compat-libstdc++-33"
#runallhostscommand "yum -y install libstdc++.i686"
#runallhostscommand "yum -y install pam.i686"

#copyfiles "id_rsa.pub"
#runallhostscommand "cat id_rsa.pub >>.ssh/authorized_keys"
#runallhostscommand "ls /etc/yum.repos.d"

# copydir /root IBM-SPSS-AnalyticServer

#runallhostscommand "rm /root/scripts"

#copyfiles "/etc/pki/rpm-gpg/RPM-GPG-KEY-EPEL-7"
#copyfiles "/etc/yum.repos.d/epel.repo"

#runallhostscommand "yum -y --enablerepo=epel --enablerepo=\"rhel-7-server-optional-rpms\" install R"
#runallhostscommand "yum clean all; yum repolist"
#runallhostscommand "which R"

copyfiles "/etc/hosts"

