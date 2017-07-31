# hosts
Simple tool to run commands accross Linux cluster. Tool does not require any dependencies.

## File description
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

## Execute





