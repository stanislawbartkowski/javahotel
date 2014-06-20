var HELLO = (function() {
   var my = {};
   
   my.hello = function (s) {
     var o = eval('(' + s + ')');
//     alert(s);
     var res = {};
     res.JOK_MESSAGE = "Hello";
     res.globint = 34;
     res.globint_T = "int";
     res.JCOPY_globint=true;
//     res.JCOPY_globint_T="bool";
     return res;
   };
   return my;
}());