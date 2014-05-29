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