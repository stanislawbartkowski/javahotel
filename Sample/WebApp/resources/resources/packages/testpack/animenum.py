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

def dialogaction(action,var) :
  printVar("animenum",action,var)
        
  seq = []
  for i in DINOSAURS :
        rec = {}
        rec['id'] = i
        rec['name'] = i
        seq.append(rec)
        
  cutil.setJMapList(var,action,seq)        
