from cutil import printVar
import cutil

DINOSAURS = [ "allosaurus", "brontosaurus",
            "carcharodontosaurus", "diplodocus", "ekrixinatosaurus",
            "fukuiraptor", "gallimimus", "hadrosaurus", "iguanodon",
            "jainosaurus", "kritosaurus", "liaoceratops", "megalosaurus",
            "nemegtosaurus", "ornithomimus", "protoceratops", "quetecsaurus",
            "rajasaurus", "stegosaurus", "triceratops", "utahraptor",
            "vulcanodon", "wannanosaurus", "xenoceratops", "yandusaurus",
            "zephyrosaurus"]

TABS =  [ "alpha", "beta", "gamma", "delta", "epsilon" ]

def setT(action,var,li) :
  seq = []
  for i in li :
        rec = {}
        rec['id'] = i
        rec['name'] = i
        seq.append(rec)
        
  cutil.setJMapList(var,action,seq)        


def dialogaction(action,var) :
  printVar("animenum",action,var)
  setT(action,var,DINOSAURS)

def dialogtab(action,var) :
  printVar("animenum",action,var)
  setT(action,var,TABS)
