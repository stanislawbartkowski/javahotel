var DIAL28 = (function() {
   var my = {};      
   
   my.clickbutton = function (s) {
     CUTIL.debug(s)
//     alert(s)
     var o = eval('(' + s + ')');
     var res = {};
     if (o.row.jsaction == "button1a") image="image1a"; 
     if (o.row.jsaction == "button1b") image="image1b"; 
     if (o.row.jsaction == "button1c") image="image1c"; 
     if (o.row.jsaction == "button3a") image="image3a"; 
     if (o.row.jsaction == "button3b") image="image3b"; 
     if (o.row.jsaction == "button3c") image="image3c"; 
     if (o.row.jsaction == "button3aFade") image="image3aFade"; 
     if (o.row.jsaction == "button3bFade") image="image3bFade"; 
     if (o.row.jsaction == "button3cFade") image="image3cFade"; 
              
     var ra = Math.random()
     CUTIL.setSrc(res,image,"http://vaadin.github.io/gwt-polymer-elements/demo/gwtPolymerDemo/polymer.svg?" + ra)
     CUTIL.setLabelText(res,o.row.jsaction,"Reload image")
     return res;
   };
   return my;
}());