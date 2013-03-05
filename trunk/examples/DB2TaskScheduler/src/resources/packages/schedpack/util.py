import sys
from datasource import DataSource
from com.jythonui.shared import ICommonConsts
import db2action
import re
#import logging

__DEFADATASOURCE="defdatasource"

def logaction(action,var) :
#   logging.debug("dialogaction " + action)
#   for key in var :
#     logging.debug(key + " = " +  str(var[key]))
  pass
  
def setdefadatasource(var) :
  da = DataSource()
  datasource = da.getDefaultPar(__DEFADATASOURCE)
  if datasource == "" : return
  var['datasource'] = datasource
  var['JCOPY_datasource'] = True
  
def savedefadatasource(var) :
  da = DataSource()
  datasource = var['datasource']
  da.setDefaultPar(__DEFADATASOURCE,datasource)
  

  