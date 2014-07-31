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
   
   function addStyle(str) {
       var pa = document.getElementsByTagName('head')[0];
       var el = document.createElement('style');
       el.type = 'text/css';
       if (el.styleSheet)
           el.styleSheet.cssText = str;// IE method
       else
           el.appendChild(document.createTextNode(str));// others
       pa.appendChild(el);
       return el;
   }


        
   return my;
}());
