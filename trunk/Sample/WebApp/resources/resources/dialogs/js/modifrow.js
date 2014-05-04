var ROW = (function() {
   var my = {};
   
   my.rowmodif = function (s) {
     var o = eval('(' + s + ')');
//     alert(o.row.id);     
     if (o.row.id%2 == 0) return "rowbold";
     else
       return "";
//     return "roomcolumnClass"; 
   };
   return my;
}());