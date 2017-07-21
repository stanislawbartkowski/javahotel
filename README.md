# Dockerize IBM Streams

## Prepare IBM Streams docker friendly image

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

## Create IBM Streams friendly docker image

> docker build -t ibmstreams -f Dockerfile.st .

### Create network (if not created)
Create a separate network, if not created already. It is convenient to have the IBM Streams container with static IP address.
> docker network create --subnet=172.18.0.0/16 dnet

### Prepare IBM Streams installation package

In /tmp/i directory of the host machine unpack the installation image. It is convenient to keep it externally to avoid pumping up the container. The installation directory can be removed when IBM Streams is installed.

> mkdir /tmp/i
> cd /tmp/i

Unpack the image, the following directoru structure should be created.

```
StreamsInstallFiles/
StreamsInstallFiles/IBMStreams_SampleResponseFile.properties
StreamsInstallFiles/com.ibm.streams.install.dependency.jar
StreamsInstallFiles/IBMStreamsSetup.bin
StreamsInstallFiles/IBMStreams-Install-Config.pdf
StreamsInstallFiles/ReleaseNotes.html
StreamsInstallFiles/dependency_checker.sh
StreamsInstallFiles/ibmdita.css
```
### Create container

> docker run -v /tmp/i:/tmp/i  -h ibmstreams.sb.com --net dnet --ip 172.18.0.15 --privileged=true --name ibmstreams ibmstreams
> ssh root@172.18.0.15

### Run IBM Streams prereqcheker

> cd /tmp/i/StreamsInstallFiles

> ./dependency_checker.sh

Two warnings are expected to pop up.

```
=== Summary of Errors and Warnings ===
* Warning:  CDISI3061W The ibmstreams.sb.com host name did not resolve to an IP address using DNS lookup.
    Cause: The host name does not satisfy the name resolution requirements for IBM Streams hosts in a multiple-host environment.
    Action: If you are running the product on a standalone host and do not intend to use this host in a multiple-host environment, you can ignore this message. If you are not using DNS, you can ignore this message. If you are running the product in a multiple-host environment, the host file or DNS might not be configured correctly. Before you use this host with the product, correct the name resolution issue. For more information about name resolution requirements, see the IBM Streams product documentation.
* Warning:  CDISI3059W You may be running a firewall which may prevent communication between the cluster hosts.
    Cause: The preferred configuration is to set up a firewall at the perimeter of the cluster to restrict network access to the cluster hosts but not communication between the cluster hosts.
    Action: If you are running the product on a standalone host and do not intend to use this host in a multiple-host environment, you can ignore this message. If you are running the product in a multiple-host environment and your security plan requires a firewall on the host operating system or between hosts, see the firewall configuration guidelines in the product documentation.

CDISI0005I The dependency checker evaluated the system and found 2 warnings.
```

For single host installation, it can be ignored.

### Install IBM Streams

> ./IBMStreamsSetup.bin 

Console mode installation should be launched, accept all defaults except:

```
===============================================================================
System Configuration Warning
----------------------------

The following warning conditions were detected. You can continue the 
installation, but the product might not function correctly. If you continue, 
check the installation summary log file for additional actions.

1. CDISI3061W The ibmstreams.sb.com host name did not resolve to an IP address
using DNS lookup.
2. CDISI3059W You may be running a firewall which may prevent communication 
between the cluster hosts.


  ->1- Cancel and exit (default)
    2- Continue

ENTER THE NUMBER OF THE DESIRED CHOICE, OR PRESS <ENTER> TO ACCEPT THE 
   DEFAULT: 
```
Choose __Continue__ here.

### Source IBM Streams environment

>ssh streamsadmin@172.18.0.15

>vi .bashrc

>source /opt/ibm/InfoSphere_Streams/4.2.1.1/bin/streamsprofile.sh

### Test __sc__ command

>sc

Expected:
```
[streamsadmin@ibmstreams ~]$ sc
Usage: sc [options] -M <main-composite>
SPL - Streams Compiler (Streams 4.2.1.1)
```
Failure
```
sc: exception! (type=runtime_error) what='locale::facet::_S_create_c_locale name not valid'. exiting...
```
In case of failure review https://developer.ibm.com/answers/questions/379852/job-submission-fails-with-error.html?smartspace=streamsdev

### Create IBM Streams domain and instance

> streamtool

>>genkey

>>mkdomain

>>startdomain

>>mkinstance

>>startinstance

>>geturl

### Test 
Try to log on to IBM Streams console using output URL from geturl command. U/P streamsadmin/streamsadmin. Connect remotely to the instance using IBM Streams Studio.
