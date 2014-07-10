var LISTROOMS = (function() {
   var my = {};
   
   my.roominfo = function (s) {
     var o = eval('(' + s + ')');     
     var r = o.row.name;
     return RUTIL.gotodialog(r,"hotel/reservation/roominfo.xml");
   };
   return my;
}());
