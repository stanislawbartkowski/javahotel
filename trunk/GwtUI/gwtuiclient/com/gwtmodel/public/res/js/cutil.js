var CUTIL = (function() {
   var my = {};
   
    my.gotodialog = function(startname,dialogname,afterdialog) {
     var res = {};
     res.JUP_DIALOG = dialogname;
     res.JUPDIALOG_START = startname;
     if (afterdialog) {
        res.JAFTERDIALOG_ACTION = afterdialog;
     }  
     return res;
   };

   my.closeifchoosen = function(o,list,closeval) {
     var key = list +"_lineset";
     var b = o.row[key]
//     alert(b);
     var res = {};
     if (b) {        
        res.JCLOSE_DIALOG = closeval;
     }
     return res;
   }
   
   my.setcontinue = function(o) {
	     o.JYTHONCONTINUE = true;
   }

        
   return my;
}());
