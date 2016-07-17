var CUTIL = (function() {
	var my = {};

	my.gotodialog = function(startname, dialogname, afterdialog) {
		var res = {};
		res.JUP_DIALOG = dialogname;
		res.JUPDIALOG_START = startname;
		if (afterdialog) {
			res.JAFTERDIALOG_ACTION = afterdialog;
		}
		return res;
	};

	my.closeifchoosen = function(o, list, closeval) {
		var key = list + "_lineset";
		var b = o.row[key]
		// alert(b);
		var res = {};
		if (b) {
			res.JCLOSE_DIALOG = closeval;
		}
		return res;
	}

	my.setcontinue = function(o) {
		o.JYTHONCONTINUE = true;
	}

	my.launchdialog = function(startparam, dialog) {
		return my.gotodialog(startparam, dialog, undefined);
	}

	my.download = function(param) {
		return my.launchdialog(param, "mail/attachdownload.xml");
	}

	return my;
}());

var JSAMPLE = (function() {
	var my = {};

	my.download = function(s) {
		var o = eval('(' + s + ')');
		var key = o.row.realm + ":" + o.row.key + ":" + o.row.filename;
		// alert(s + " " + key);
		return CUTIL.download(key);
	}
	return my;
}());

function addStyle(str) {
	var pa = document.getElementsByTagName('head')[0];
	var el = document.createElement('style');
	el.type = 'text/css';
	if (el.styleSheet)
		el.styleSheet.cssText = str;// IE method
	else
		el.appendChild(document.createTextNode(str));// others
	pa.appendChild(el);
	return el;
}

function addE(p, c) {
	Polymer.dom(p).appendChild(c);
}
