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

	my.setBinderAttr = function(v, id, attr, val) {
		// alert(id + attr + val);
		v["JSETATTR_BINDER_" + id + "_" + attr] = val
	};

	my.setBinderAction = function(v, id, attr, val) {
		// alert(id + attr + val);
		v["JACTION_BINDER_" + id + "_" + attr] = val
	};

	my.setBinderOpen = function(v, id) {
		my.setBinderAction(v, id, "open", "")
	};

	my.setElevationAttr = function(v, id, val) {
		my.setBinderAttr(v, id, "elevation", val)
	};

	my.setSpinnerActive = function(v, id, val) {
		my.setBinderAttr(v, id, "active", val)
	};

	my.setLabelText = function(v, id, val) {
		my.setBinderAttr(v, id, "text", val)
	};

	my.setOpened = function(v, id, val) {
		my.setBinderAttr(v, id, "opened", val)
	};

	my.setToastText = function(v, id, text) {
		my.setLabelText(v, id, text)
		my.setBinderOpen(v, id)
	};

	my.setSrc = function(v, id, val) {
		my.setBinderAttr(v, id, "src", val)
	};

	my.setCopyL = function() {
		v = arguments[0];
		for (var i = 1; i < arguments.length; i++) {
			var va = arguments[i];
			v["JCOPY_" + va] = true;
		}
	};

	my.setVarI = function(v, id, val) {
		v[id] = val;
		v[id + "_T"] = "int";
		my.setCopyL(v, id);
	};

	my.setVarB = function(v, id, val) {
		v[id] = val;
		v[id + "_T"] = "bool";
		my.setCopyL(v, id);
	};

	my.debug = function() {
	};

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
