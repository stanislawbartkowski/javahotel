var CHOOSEROOMS = (function() {
   var my = {};
   
   my.acceptbutton = function (s) {
     var o = eval('(' + s + ')');     
     var r = o.row.name;
     return RUTIL.closeifchoosen(o,"roomlist",r);
   };
   return my;
}());
