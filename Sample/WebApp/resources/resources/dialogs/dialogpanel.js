function tabpanel_mouseover(panelnum) {
	var numberOfPanels = 5;
	for (counter=1; counter <= numberOfPanels; counter++) {
		document.getElementById("panel_" + counter).style.display = "none"; // hide all panels
		document.getElementById("tabpanel_img_" + counter).style.color = "#505050";
	}

	document.getElementById("panel_" + panelnum).style.display = "block"; // display this panel
	document.getElementById("tabpanel_img_" + panelnum).style.color = "#1f70a7";
}
