function CingularButton() {
}

CingularButton.prototype = new GControl();

CingularButton.globals = {
	listOptionContainer: null,
	listOptionVisible: false
};

CingularButton.prototype.initialize = function(map) {
	var globals = CingularButton.globals;
	var cbContainer;
	var listOptionContainer;
	var button;
	var buttonText;
	
	cbContainer = document.createElement("div");
	cbContainer.style.zIndex = "150";

	listOptionContainer = document.createElement("div");
	this.setMenuContainerStyle(listOptionContainer);
	this.initializeMonthListMenu(listOptionContainer);
	
	//create button
	var gmwButtonContainer = document.createElement("div");

	button = document.createElement("div");
	button.appendChild(document.createTextNode("Previous"));
	gmwButtonContainer.appendChild(button);
	NovaButton.prototype.setButtonStyle(button, true);
	GEvent.addDomListener(button, "click", previousMonthClickEvent);
	
	button = document.createElement("div");
	button.appendChild(document.createTextNode("2008/01"));
	button.id = "currentMonthDiv";
	gmwButtonContainer.appendChild(button);
	NovaButton.prototype.setButtonStyle(button, true);
	GEvent.addDomListener(button, "click", currentMonthClickEvent);
	
	button = document.createElement("div");
	button.appendChild(document.createTextNode("Next"));
	gmwButtonContainer.appendChild(button);
	NovaButton.prototype.setButtonStyle(button, true);
	GEvent.addDomListener(button, "click", nextMonthClickEvent);
	
	cbContainer.appendChild(gmwButtonContainer);
	cbContainer.appendChild(listOptionContainer);

	map.getContainer().appendChild(cbContainer);
	
	//set global variables
	globals.listOptionContainer	= listOptionContainer;
	return cbContainer;
}

CingularButton.prototype.getDefaultPosition = function() {
	return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(5, 30));
}

CingularButton.prototype.initializeMonthListMenu = function(container) {
	var menuUL = container.appendChild(document.createElement("ul"));
	this.setMenuStyle(menuUL);
	
	var minMonth = 1;
	var minYear = 2008;
	
	var maxMonth = 12;
	var maxYear = 2010;
	
	for (currentYear = minYear; currentYear <= maxYear; currentYear++) {
	  for (currentMonth = minMonth; currentMonth <= maxMonth; currentMonth++) {
	    var menuItem = document.createElement("li");
	    this.setMenuItemStyle(menuItem);
	    
	    if (currentMonth < 10) {
	      menuItem.appendChild(document.createTextNode(currentYear + "/0" + currentMonth));
	      menuItem.id = currentYear + "/0" + currentMonth;
	    }
	    else {
	    	menuItem.appendChild(document.createTextNode(currentYear + "/" + currentMonth));
	      menuItem.id = currentYear + "/" + currentMonth;
	    }
	    
	    GEvent.addDomListener(menuItem, "click", menuItemOnClick);
	    GEvent.addDomListener(menuItem, "mouseover", menuItemOnMouseOver);
	    GEvent.addDomListener(menuItem, "mouseout", menuItemOnMouseOut);
	    menuUL.appendChild(menuItem);
	  }
	}
}

CingularButton.prototype.showListOption = function(posX, posY) {
	var global = CingularButton.globals;

	//position the menu at the given location
	if(typeof posX == "number") {
		global.listOptionContainer.style.left = posX + "px";
	}
	
	if(typeof posY == "number") {
		global.listOptionContainer.style.top = posY + "px";
	}
	
	//display the menu
	global.listOptionContainer.style.display = "block";
}

CingularButton.prototype.setButtonStyle = function(button) {
	button.style.color = "black";
	button.style.backgroundColor = "white";
	button.style.fontFamily = "Verdana";
	button.style.fontSize = "12px";
	button.style.fontWeight= "normal";
	button.style.border = "1px solid gray";
	button.style.padding = "0px";
	button.style.marginBottom = "0px";
	button.style.textAlign = "center";
	button.style.width = "7em";
	button.style.height = "15px";
	button.style.cursor = "pointer";
	
	if(button.addEventListener) {
		button.style.cssFloat	= "left";
	}
	else {
		button.style.styleFloat = "left";
	}
}

CingularButton.prototype.setMenuContainerStyle = function(menuContainer) {
	menuContainer.style.position = "absolute";
	menuContainer.style.overflow = "auto";
	menuContainer.style.height = "200px";
	menuContainer.style.display = "none";
	menuContainer.style.background	= "#FFFFF0";
}

CingularButton.prototype.setMenuStyle = function(menu) {
  menu.style.margin	= "2";
  menu.style.width = "100px";
  menu.style.padding	= "0px 30px 0px 0px";
  menu.style.listStyleType= "none";
  menu.style.background	= "#FFFFF0";
}

CingularButton.prototype.setMenuItemStyle = function(menuItem) {
	menuItem.style.padding	= "4px 3px 4px 33px";
	menuItem.style.fontFamily = "Vardana, Arial, Helvetica, sans-serif";
	menuItem.style.fontSize	= "10pt";
	menuItem.style.color	= "#333";
	menuItem.style.cursor	= "default";
	menuItem.style.background = "no-repeat left center";
}

var previousMonthClickEvent = function() {
  updateImage(-1);
  NovaButton.prototype.initializeLayerDiv(NovaButton.globals.layerOptionContainer);
}

var nextMonthClickEvent = function() {
	updateImage(1);
	NovaButton.prototype.initializeLayerDiv(NovaButton.globals.layerOptionContainer);
}

function currentMonthClickEvent(e) {
  if (CingularButton.globals.listOptionVisible) {
	  CingularButton.globals.listOptionVisible = false;
	  CingularButton.globals.listOptionContainer.style.display = "none";
	  return;
	}
	
	if (NovaButton.globals.layerOptionContainer.style.display != "none"){
		NovaButton.prototype.showLayerClickEvent();
  }
  if (NovaButton.globals.searchOptionContainer.style.display != "none"){
		NovaButton.prototype.searchClickEvent();
  }
	
  if (!e) {
  	e = window.event;
 	}
 	
	e.cancelBubble = true;
	
	if (e.stopPropagation) {
		e.stopPropagation();
	}
	
	var posX = this.offsetLeft;
	var posY = this.offsetTop;

	CingularButton.prototype.showListOption(posX, posY + this.offsetHeight);
	CingularButton.globals.listOptionVisible = true;
}

function menuItemOnClick() {
  updateImage(this.id);
  CingularButton.globals.listOptionVisible = false;
  CingularButton.globals.listOptionContainer.style.display = "none";
  NovaButton.prototype.initializeLayerDiv(NovaButton.globals.layerOptionContainer);
}


function menuItemOnMouseOver() {
	this.style.padding	= "3px 2px 3px 32px";
	this.style.color	= "#000";
	this.style.border	= "1px solid #06C";
	this.style.backgroundColor = "#fec";
}

function menuItemOnMouseOut() {
	this.style.padding	= "4px 3px 4px 33px";
	this.style.color	= "#333";
	this.style.border 	= "none";
	this.style.backgroundColor = "";
}
