var HELLO = (function() {
   var my = {};
   
   my.hello = function (s) {
     var o = eval('(' + s + ')');
//     alert(o.row);
     var res = {};
     res.JOK_MESSAGE = "Hello";
     res.ii = 34;
     res.ii_T = "int";
     return res;
   };
   return my;
}());


var RUTIL = (function() {
   var my = {};
   
   my.chooseroom = function(roomname,afterdialog) {
     var res = {};
     res.JUP_DIALOG ="hotel/roomlistsearch.xml";
     res.JUPDIALOG_START = roomname;
     res.JAFTERDIALOG_ACTION = afterdialog;
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
   
   return my;
}());
