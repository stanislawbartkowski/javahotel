var HELLOCHANGE = (function() {
   var my = {};
   
   my.hello = function (s) {
     var o = eval('(' + s + ')');
     alert(s);
     var res = {};
     return res;
   };
   return my;
}());