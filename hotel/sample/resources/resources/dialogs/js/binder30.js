var TOAST30 = (function() {
   var my = {};      
   
   my.signaltoast = function (s) {
     CUTIL.debug(s)
     var o = eval('(' + s + ')');
     var res = {};
     var fie = o.row.changefield
     var mess = fie + " " + o.row[fie]
     CUTIL.setToastText(res,"toast",mess);
     return res;
   };
   return my;
}());