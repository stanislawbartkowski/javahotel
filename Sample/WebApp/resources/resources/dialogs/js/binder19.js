
var SLIDER19 = (function() {
   var my = {};      
   
   my.settext = function (s) {
     CUTIL.debug()
     var o = eval('(' + s + ')');
     var res = {};
     CUTIL.setLabelText(res,"ratingsLabel",o.row.ratings);
       
     return res;
   };
   return my;
}());