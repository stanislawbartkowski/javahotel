var IMAGE = (function() {
   var my = {};
   
   my.toimage = function (s) {
     var o = eval('(' + s + ')');
//     alert(o.row.image);
     var f = "arrow_left.png";
     if (o.row.image % 2 == 1) f = "arrow_right.png"; 
     if (o.row.image < 0) { return "arrow_down.png," + f; }
     return "arrow_up.png," + f;
   };
   return my;
}());
   