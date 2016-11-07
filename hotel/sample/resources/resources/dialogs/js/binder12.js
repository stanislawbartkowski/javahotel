
var TAP = (function() {
   var my = {};      
   
   my.tap = function (s) {
     var o = eval('(' + s + ')');
//     alert(s);
     var action = o.row.jsaction;
     var vname = ["elevation","up"]
     if (action == "material") vname = ["elevation1","up1"];
     var res = {};
     var ele = o.row[vname[0]];
     var up = o.row[vname[1]];
          
     if (ele == 0) up = true;
     if (ele == 5) up = false;

     if (up) ele++; else ele--;

     CUTIL.setElevationAttr(res,action,ele)
     CUTIL.setVarI(res,vname[0],ele);
     CUTIL.setVarB(res,vname[1],up)
     return res;
   };
   return my;
}());