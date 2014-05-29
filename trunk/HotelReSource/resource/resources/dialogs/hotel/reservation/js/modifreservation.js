var MODIFRESERVATION = (function() {
   var my = {};
   
   my.chooseroom = function (s) {
     var o = eval('(' + s + ')');
     var res = {};
     var roomname = o.row.resroomname;
     return RUTIL.chooseroom(roomname,"roomselected");  
   };
   return my;
}());