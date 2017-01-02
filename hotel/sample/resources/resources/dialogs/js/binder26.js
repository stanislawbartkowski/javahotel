var TOAST22 = (function() {
   var my = {};      
   
   my.opentoast1 = function (s) {
     CUTIL.debug(s)
     var res = {};
     CUTIL.setBinderOpen(res,"toast1");       
     return res;
   };
   my.opentoast2 = function (s) {
     CUTIL.debug(s)
     var res = {};
     CUTIL.setBinderOpen(res,"toast2");
     return res;
   };
   return my;
}());

MyElement = Polymer({

      is: 'my-element',

      // See below for lifecycle callbacks
      created: function() {
        this.textContent = 'My element!';
        alert("Hello");
      },
      
      ready: function() {
          alert("ready");
      }

 });

var el2 = new MyElement();
    
    