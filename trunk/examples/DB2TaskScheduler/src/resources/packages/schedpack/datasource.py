from com.jythonui.db2.scheduler.injector import ServiceInjector as factory
from java.io import FileReader
from java.util import Properties
from java.io import File
from java.io import FileWriter

__DEFPARAM='def_param'

class DataSource :

  def __init__(self) :
    pass
    
  def __getfilename__(self) :
    fname = factory.getPropertyFileName()
    return fname
    
  def __readmap__(self) :  
  
    fname = self.__getfilename__()
    file = File(fname)
    prop = Properties()
    if file.exists() :
      fReader = FileReader(file)
      prop.load(fReader)
    se = prop.keySet()
    i = se.iterator()
    dic = {}
    while i.hasNext() :
       key = i.next()
       val = prop.getProperty(key)
       ka = key.split('.')
       if len(ka) <= 1 : continue
       datasource = ka[0]
       prope = ka[1]
       if not dic.has_key(datasource) : dic[datasource] = {}
        
       map = dic[datasource]
       if prope == "port" : map[prope] = long(val)
       else : map[prope] = val
       
    return dic   
        
  def readList(self) :
    dic = self.__readmap__()
    seq = []
    for p in dic.keys() :
      if p == __DEFPARAM : continue
      map = dic[p]
      map['datasource'] = p
      seq.append(map)
    return seq
    
  def getDatasource(self,datasource) :
    dic = self.__readmap__()
    if not dic.has_key(datasource) : return None
    return dic[datasource]
    
    
  def __writedic__(self,dic,setK = ['port','host','user','password','database']) :
    prop = Properties()
    for p in dic.keys() :
      map = dic[p]
      for pro in map.keys() :
        if setK and not setK.__contains__(pro) : continue      
        key = p + '.' + pro
        val = map[pro]
        if pro == "port" : val = str(val)
        prop.setProperty(key,val)
          
    wri = FileWriter(self.__getfilename__())
    prop.store(wri,"Data source list")
    
  def addDataSource(self,datasource,da) :
    dic = self.__readmap__()
    dic[datasource] = da
    self.__writedic__(dic)
       
  def removeDataSource(self,datasource) :
    dic = self.__readmap__()
    if dic.has_key(datasource) :
      dic.pop(datasource)
      self.__writedic__(dic)
      
  def existDataSource(self,datasource) :    
    dic = self.__readmap__()
    return dic.has_key(datasource)
    
  def setDefaultPar(self,key,par) :
    dic = self.__readmap__()
    if dic.has_key(__DEFPARAM) : defa = dic[__DEFPARAM]
    else : defa = {}
    defa[key] = par
    dic[__DEFPARAM] = defa
    self.__writedic__(dic,None)
    
  def getDefaultPar(self,key,defa="") :
    dic = self.getDatasource(__DEFPARAM) 
    if dic == None : return defa
    if dic.has_key(key) : return dic[key]
    return defa
    
  def removeDefaultPar(self,key) :
    dic = self.__readmap__()
    if not dic.has_key(__DEFPARAM) : return
    defa = dic[__DEFPARAM]
    if not defa.has_key(key) : return
    defa.pop(key)
    self.__writedic__(dic,None)
  