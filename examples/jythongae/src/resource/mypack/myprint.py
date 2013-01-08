# coding=utf-8

import java.lang.System

def myprint() :
  print "my print"

def myprintGG(s) :
  java.lang.System.out.println(s)

def myprintMap(m) :
  print "map"
  for s in m : 
    print s, m[s]

def getVal() :
  return "Hello from jython package !"
