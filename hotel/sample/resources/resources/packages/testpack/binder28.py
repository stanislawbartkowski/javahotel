import util
import cutil

def dialogaction(action,var) :
  cutil.printVar("binder1",action,var)
  
  image=None
  if action == "button1a" : image="image1a"
  
  if image == None : return

  cutil.setBinderSrcAttr(var,image,"http://vaadin.github.io/gwt-polymer-elements/demo/gwtPolymerDemo/polymer.svg")
  cutil.setLabelText(var,action,"Reload image")
      
  