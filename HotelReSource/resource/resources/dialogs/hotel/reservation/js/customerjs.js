var CUSTOMERLIST = (function() {
   var my = {};
      
   my.customerinfo =  function (s) {
     var o = eval('(' + s + ')');
//     alert(s);
     var name = o.row.name;
     return RUTIL.gotodialog(name,"hotel/reservation/customerresinfo.xml");
   }
      
   return my;
}());
