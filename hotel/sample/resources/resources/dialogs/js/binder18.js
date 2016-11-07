
var SPINNER18 = (function() {
   var my = {};      
   
   my.click = function (s) {
//     CUTIL.debug()
     var o = eval('(' + s + ')');
//     alert(s);
     var action = o.row.jsaction;
     var vlist = null;
     var to = null;
     if (action == "toggleBtn1") {
       vlist =  ["toggle1_1","toggle1_2","toggle1_3","toggle1_4"]
       to = "toggle1";
     }
     if (action == "toggleBtn2") {
       vlist =  ["toggle2_1","toggle2_2","toggle2_3","toggle2_4"]
       to = "toggle2";
     }
     var res = {};
     if (vlist != null) {
        var active = ! o.row[to]
        for (var j=0, id; id = vlist[j]; j++) CUTIL.setSpinnerActive(res,id,active);
        CUTIL.setVarB(res,to,active);
     }
       
     return res;
   };
   return my;
}());