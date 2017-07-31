# hosts
Simple tool to run commands across Linux cluster. The tool does not require any dependencies.

## Files description
* run.sh Main file
* hostsproc.rc Utility procs
* nodes.txt List of all slave nodes
* users.sh Utility script comparing id of users across cluster
* checkfilewall.sh Script checking firewall status
* getusers.sh Script checking id of users across cluster

## Configuration
* Select one host as master, the other hosts are slaves
* Setup root passwordless connection between master and slave hosts
* Prepare nodes.txt file containing host names/IP addresses of all slave hosts. [Example](nodes.txt)

## hostsproc.rc Procs description

Proc name | Parameters | Action | Example
----------|------------|--------|--------
runallhosts | Script name | Copies and executes script on all hosts | runallhosts checkfilewall.sh
runcommand | Command and parameters | Executes single command on all hosts | runallhostscommand "ntpstat; echo $?"
copyfiles | File name | Copies file from master to all slave hosts | copyfiles "/etc/yum.repos.d/epel.repo"

## Execute

Add next command to run.sh file and execute ./run.sh

[More samples](run.sh)

## users.sh

For BigInights it is a requirement to have consistent user's id on all hosts. It is particularly important for *bigsql* user. The tool verifies all users on all hosts.

[user.sh](users.sh)







