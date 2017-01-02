var DIAL27 = (function() {
   var my = {};      
   
   my.clickbutton = function (s) {
     CUTIL.debug(s)
     var o = eval('(' + s + ')');
     var v = "value3";
     var c = "collapse3";
     if (o.row.jsaction == "heading1") { v = "value1"; c = "collapse1"; }
     else if (o.row.jsaction == "heading2") { v = "value2"; c = "collapse2"; }
     var active=!o.row[v];
     var res = {};
     CUTIL.setOpened(res,c,active)
     CUTIL.setVarB(res,v,active)
     CUTIL.setCopyL(res,v)
     return res;
   };
   return my;
}());
