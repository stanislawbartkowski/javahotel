function jsAddStyle(s) {
	  var o = eval('(' + s + ')');
	  var job = o.Employee.JOB;
	  if (job == 'MANAGER') { return 'addRow'; }
	     return null; 
	};

