import con

def _toL(*args):
     l = None
     for a in args :
          s = str(a)
          if l == None : l = s
          else : l = l + " " +s
     return l

class LOGGERJYTHON :
    
  def __init__(self):
    import logging

    logging.basicConfig(level=logging.INFO,format='%(message)s')

    self.logger = logging.getLogger('jython.hotel')
    self.logger.setLevel(logging.INFO)

  def info(self,*args):
     self.logger.info(_toL(*args))
     
  def debug(self,*args):
     self.logger.debug(_toL(*args))

class LOGGERJAVA:
    
    def __init__(self):
        from java.util.logging import Logger
        self.logger = Logger.getLogger('jython.hotel')
        
    def info(self,*args):
        self.logger.info(_toL(*args))

if con.isAppEngine() : LOGGER = LOGGERJAVA()
else : LOGGER = LOGGERJYTHON()

def info(*args):
    LOGGER.info(*args)
    
def debug(*args):
    LOGGER.debug(*args)             