var IMAGEDIAL = (function() {
   var my = {};
   
   my.toimage = function (s) {
//     alert(s);
     var o = eval('(' + s + ')');
//     alert(o.row.imfieldd);
     var val = o.row.imfieldd;
     var ima = "-";
     var imb = "-";
     var imc = "-";
//     alert(val);
     if (val === null) return "-,-,-";
     if(val.indexOf('A') != -1) { ima = "Letter-A-icon.png"; }
     if(val.indexOf('B') != -1) { imb = "Letter-B-icon.png"; }
     if(val.indexOf('C') != -1) { imc = "Letter-C-icon.png"; }
     return ima + "," + imb + "," + imc;
   };
   return my;
}());