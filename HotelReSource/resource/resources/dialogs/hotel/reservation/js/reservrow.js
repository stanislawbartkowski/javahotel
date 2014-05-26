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