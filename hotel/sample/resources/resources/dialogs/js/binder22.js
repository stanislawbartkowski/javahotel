var TOAST22 = (function() {
   var my = {};      
   
   my.opentoast1 = function (s) {
     CUTIL.debug(s)
     var res = {};
     CUTIL.setBinderOpen(res,"toast1");       
     return res;
   };
   my.opentoast2 = function (s) {
     CUTIL.debug(s)
     var res = {};
     CUTIL.setBinderOpen(res,"toast2");
     return res;
   };
   return my;
}());