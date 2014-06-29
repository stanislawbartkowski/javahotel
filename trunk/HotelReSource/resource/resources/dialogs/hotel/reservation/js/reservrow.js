var ROW = (function() {
   var my = {};
   
   my.reservrow = function (s) {
     var o = eval('(' + s + ')');
//     alert(o.row.avail);
     if (o.row.avail) return "";
     return "alreadyreserved";
   };
   return my;
}());

var RESECHANGEAFTER = (function() {
   var my = {};
   
   my.setcontinue = function(o) {
     o.JYTHONCONTINUE = true;
   }
   
   my.afterchange = function (s) {
//     alert(s)
     var o = eval('(' + s + ')');
//     alert(o.row.avail);
      var res = {};
      if (o.row.changeafterfocus) {
         my.setcontinue(res);
      }
      return res;
   };
   return my;
}());
