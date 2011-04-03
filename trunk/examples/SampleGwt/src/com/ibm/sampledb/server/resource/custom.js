function jsAddStyle(s) {
	  var o = eval('(' + s + ')');
	  var job = o.Employee.job;
	  if (job == 'MANAGER') { return 'addRow'; }
	     return null; 
	};

