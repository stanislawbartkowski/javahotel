from com.gwthotel.hotel.server.service import H
from java.util import ArrayList

class MESS :

  def __init__(self):
      self.M = H.getM()

  def __call__(self,key) :
      return self.M.getMessN(key)
  
def printvar(method,action,var): 
  print method, action
  for k in var.keys() : 
    print k, var[k]
 
def createArrayList() :
  return ArrayList()   