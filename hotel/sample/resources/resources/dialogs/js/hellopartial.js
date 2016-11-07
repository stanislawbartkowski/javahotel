var HELLOPARTIAL = (function() {
   var my = {};
   
   my.hello = function (s) {
     var o = eval('(' + s + ')');
//     alert(s);
     var res = {};
     if (o.row.calljython) {
       res.JOK_MESSAGE = "Call now Jython";
       res.JYTHONCONTINUE = true;
//       res.JYTHONCONTINUE_T = "bool";
     }
     else {
       res.JOK_MESSAGE = "Hello from JavaScript";
     }
     res.globint = 34;
     res.globint_T = "int";
     res.JCOPY_globint=true;
//     res.JCOPY_globint_T="bool";
     return res;
   };
   return my;
}());