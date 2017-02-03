function NovaButton() {

}

NovaButton.prototype = new GControl();

NovaButton.globals = {
	currentButton: null,
	layerOptionContainer: null,
	layerOptionVisible: false,
	searchOptionContainer: null,
	searchOptionVisible: false,
	//new changes
	searchResultContainer: null,
	//new changes
	searchTabSelected: null,
	map: null,
	originalLayerState: null
};

NovaButton.globals.dragzoom = {
  cornerTopDiv: null,
  cornerRightDiv: null,
  cornerBottomDiv: null,
  cornerLeftDiv: null,
  mapPosition: null,
  outlineDiv: null,
  mapWidth: 0,
  mapHeight: 0,
  mapRatio: 0,
  startX: 0,
  startY: 0,
  zoomArea: null,
  moving: false
};

// 	Creates a one DIV for each of the buttons and places them in a container
// 	DIV which is returned as our control element.
NovaButton.prototype.initialize = function(map) {
	var container = document.createElement("div");
	container.style.zIndex = "150";
	var that = this;
	
	// "Cursor" button
	var cursorDiv = document.createElement("div");
	cursorDiv.id = "cursorDiv";
	NovaButton.globals.currentButton = "cursorDiv";
	this.setButtonStyle(cursorDiv, true);
	cursorDiv.style.fontWeight = "bold";
	cursorDiv.appendChild(document.createTextNode("Cursor"));
	GEvent.addDomListener(cursorDiv, "click", function() {
		var globals = NovaButton.globals;
		if (globals.currentButton == "cursorDiv") {
			return;
		}
	  
	  this.style.fontWeight = "bold";
	  globals.currentButton = "cursorDiv";
	  if (!map.draggingEnabled()) {
  		map.enableDragging();
  	}
	  document.getElementById("detailDiv").style.fontWeight = "normal";
	  document.getElementById("zoomRectangleDiv").style.fontWeight = "normal";
	  
	  that.resetDragZoom();
	});
	
	// "detail" button
	var detailDiv = document.createElement("div");
	detailDiv.id = "detailDiv";
	this.setButtonStyle(detailDiv, true);
	detailDiv.appendChild(document.createTextNode("Detail"));
	GEvent.addDomListener(detailDiv, "click", function() {
	  var globals = NovaButton.globals;
		if (globals.currentButton == "detailDiv") {
			return;
		} 
		
	  this.style.fontWeight = "bold";
	  globals.currentButton = "detailDiv";
	  if (map.draggingEnabled()) {
  		map.disableDragging();
  	}
	  document.getElementById("cursorDiv").style.fontWeight = "normal";
	  document.getElementById("zoomRectangleDiv").style.fontWeight = "normal";
	  
	  that.resetDragZoom();
	});
	
	// zoom rectangle button
	var zoomRectangleDiv = document.createElement("div");
	zoomRectangleDiv.id = "zoomRectangleDiv";
	this.setButtonStyle(zoomRectangleDiv, true);
	zoomRectangleDiv.appendChild(document.createTextNode("Zoom Rect."));
	GEvent.addDomListener(zoomRectangleDiv, "click", function() {
		var globals = NovaButton.globals;
		if (globals.currentButton == "zoomRectangleDiv") {
			return;
		}
		
	  this.style.fontWeight = "bold";
	  globals.currentButton = "zoomRectangleDiv";
	  
	  if (map.draggingEnabled()) {
  		map.disableDragging();
  	}
  	document.getElementById("cursorDiv").style.fontWeight = "normal";
  	document.getElementById("detailDiv").style.fontWeight = "normal";
  	
    that.initCover();
	});
	
	// "Zoom In" button
	var zoomInDiv = document.createElement("div");
	this.setButtonStyle(zoomInDiv, true);
	zoomInDiv.appendChild(document.createTextNode("Zoom In"));
	// The "true" argument in the zoomIn() method allows continuous zooming
	GEvent.addDomListener(zoomInDiv, "click", function() {
		//map.zoomIn(null,null,true);
		map.zoomIn();
	});

	// "Zoom Out" button
	var zoomOutDiv = document.createElement("div");
	this.setButtonStyle(zoomOutDiv, true);
	zoomOutDiv.style.borderTop = 0+'px';
	zoomOutDiv.appendChild(document.createTextNode("Zoom Out"));
	// The "true" argument in the zoomOut() method allows continuous zooming
	GEvent.addDomListener(zoomOutDiv, "click", function() {
		//map.zoomOut(null,true);
		map.zoomOut();
	});
	
	// "Default View" button
	var defaultViewDiv = document.createElement("div");
	this.setButtonStyle(defaultViewDiv, true);
	defaultViewDiv.appendChild(document.createTextNode("Default View"));
	GEvent.addDomListener(defaultViewDiv, "click", function(e) {
	  that.defaultViewClickEvent();
	});
	
	// "layer" button
	var showLayerDiv = document.createElement("div");
	this.setButtonStyle(showLayerDiv, true);
	showLayerDiv.appendChild(document.createTextNode("Layers"));
	GEvent.addDomListener(showLayerDiv, "click", function(e) {
		that.showLayerClickEvent(e);
	});
	
	// initialize layer option
	var layerOptionContainer = document.createElement("div");
	this.setMenuContainerStyle(layerOptionContainer);
	this.initializeLayerDiv(layerOptionContainer);
	
	// "search" button
	var searchDiv = document.createElement("div");
	this.setButtonStyle(searchDiv, true);
	searchDiv.appendChild(document.createTextNode("Search"));
	GEvent.addDomListener(searchDiv, "click", function(e) {
		that.searchClickEvent(e);
	});
	
	var searchOptionContainer = document.createElement("div");
	searchOptionContainer.style.position = "absolute";
	searchOptionContainer.style.left = "100px";
	searchOptionContainer.style.top = "100px";
	searchOptionContainer.style.display = "none";
	this.initializeSearchDiv(searchOptionContainer);
	
	var buttonContainer = document.createElement("div");
	buttonContainer.appendChild(cursorDiv);
	buttonContainer.appendChild(detailDiv);
	//buttonContainer.appendChild(zoomRectangleDiv);
	//buttonContainer.appendChild(zoomInDiv);
	//buttonContainer.appendChild(zoomOutDiv);
	//buttonContainer.appendChild(defaultViewDiv);
	//buttonContainer.appendChild(showLayerDiv);
	//buttonContainer.appendChild(searchDiv);
	
	// zoom area div
  var zoomAreaDiv = document.createElement("div");
	this.setZoomAreaStyle(zoomAreaDiv);
  var DIVS_TO_CREATE = ['outlineDiv', 'cornerTopDiv', 'cornerLeftDiv', 'cornerRightDiv', 'cornerBottomDiv'];
  for (var i=0; i<DIVS_TO_CREATE.length; i++) {
    var id = DIVS_TO_CREATE[i];
    var div = document.createElement("div");
    div.style.position = "absolute";
    div.style.display = "none";
    zoomAreaDiv.appendChild(div);
    NovaButton.globals.dragzoom[id] = div;
  }
  
  GEvent.addDomListener(zoomAreaDiv, 'mousedown', function(e) {
  	that.mouseDown(e);
 	});
  GEvent.addDomListener(document, 'mousemove', function(e) {
  	that.mouseMove(e);
  });
  GEvent.addDomListener(document, 'mouseup', function(e) {
  	that.mouseUp(e);
  });

	// assign global variable
	NovaButton.globals.layerOptionContainer = layerOptionContainer;
	NovaButton.globals.map = map;
	NovaButton.globals.dragzoom.zoomArea = zoomAreaDiv;
	NovaButton.globals.dragzoom.mapPosition = NovaButtonUtils.getElementPosition(map.getContainer());
	NovaButton.globals.searchOptionContainer = searchOptionContainer;
  this.setDimensions();
  
  zoomAreaDiv.onselectstart = function() {return false;};
  
  //styles
  this.initZoomAreaStyles();
  
	container.appendChild(buttonContainer);
	map.getContainer().appendChild(container);
	//map.getContainer().appendChild(layerOptionContainer);
	//map.getContainer().appendChild(searchOptionContainer);
	document.body.appendChild(layerOptionContainer);
	document.body.appendChild(searchOptionContainer);
	map.getContainer().appendChild(zoomAreaDiv);
	
	return container;
}

// 	The control will appear in the top left corner of the map with 5 pixels of padding.
NovaButton.prototype.getDefaultPosition = function() {
	return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(70, 5));
}

NovaButton.prototype.setSearchResultStyle = function(container) {
  container.style.width = "300px";
  container.style.height = "150px";
  container.style.background = "#FFFFF0";
  container.style.fontFamily = "Verdana";
  container.style.fontSize = "12px";
  container.style.position = "absolute";
  container.style.border = "1px solid black";
  container.style.top = "201px";
	container.style.left = "0px";
	container.style.display = "none";
	container.style.overflow = "auto";
  container.align = "left";
}

// Sets the proper CSS for the given button element.
NovaButton.prototype.setButtonStyle = function(button, horizontalAlign) {
  button.style.color = "black";
	button.style.backgroundColor = "white";
	button.style.fontFamily = "Verdana";
	button.style.fontSize = "12px";
	button.style.fontWeight= "normal";
	button.style.border = "1px solid gray";
	button.style.padding = "0px";
	button.style.marginBottom = "0px";
	button.style.textAlign = "center";
	button.style.width = "80px";
	button.style.height = "15px";
	button.style.cursor = "pointer";
	//button.style.zIndex = "200"; 
	
	if (horizontalAlign) {
		if(button.addEventListener) {
			button.style.cssFloat	= "left" ;
		}
		else {
			button.style.styleFloat = "left" ;
		}
	}
}

NovaButton.prototype.setTabHeaderStyle = function(header) {
	//header.style.position = "absolute";
	//header.style.zIndex = "2";
	header.align = "left";
}

NovaButton.prototype.setTabButtonStyle = function(tab) {
	tab.style.padding = "3px 5px 3px 5px";
	tab.style.fontFamily = "Verdana";
	tab.style.fontSize = "12px";
	tab.style.textAlign = "center";
	tab.style.width = "7em";
	tab.style.height = "15px";
	tab.style.cursor = "pointer";
	tab.style.background = "#C6DEFF";
	
	if(tab.addEventListener) {
		tab.style.cssFloat	= "left" ;
	}
	else {
		tab.style.styleFloat = "left" ;
	}
}

NovaButton.prototype.setTabBodyStyle = function(body) {
	body.style.width = "400px";
	body.style.height = "150px";
	body.style.background = "#FFFFF0";
	body.style.fontFamily = "Verdana";
	body.style.fontSize = "12px";
	//body.style.zIndex = "1";
	body.style.padding = "3px 5px 3px 5px";
	body.style.top = "20px";
	body.style.left = "0px";
	body.style.position = "absolute";
	body.style.display = "none";
	body.align = "left";
}

NovaButton.prototype.setTabFooterStyle = function(footer) {
	footer.style.width = "400px";
	footer.style.height = "20px";
	footer.align = "center";
	footer.style.padding = "3px 5px 3px 5px";
	footer.style.background = "#FFFFF0";
	footer.style.position = "absolute";
	footer.style.top = "175px";
	footer.style.left = "0";
}

NovaButton.prototype.tabMouseOver = function(e) {
  var tab = Event.element(e);
  tab.style.background = "#ADDFFF";
}

NovaButton.prototype.tabMouseOut = function(e) {
  var tab = Event.element(e);
  tab.style.background = "#C6DEFF";
}

NovaButton.prototype.tabClickEvent = function(e) {
  var tab = Event.element(e);
  
  if (tab.id == NovaButton.globals.searchTabSelected) {
    return false;
  }
  
  NovaButton.globals.searchTabSelected = tab.id;
  tab.style.fontWeight = "bold";
  
  if (tab.id == "nodeTab") {
  	document.getElementById("nodeBody").style.display = "inline";
  	document.getElementById("addressBody").style.display = "none";
  	document.getElementById("addressTab").style.fontWeight = "normal";
  }
  else {
  	document.getElementById("addressBody").style.display = "inline";
  	document.getElementById("nodeBody").style.display = "none";
  	document.getElementById("nodeTab").style.fontWeight = "normal";
  }
}

NovaButton.prototype.initializeSearchDiv = function(searchOptionContainer) {
  var header = document.createElement("div");
  this.setTabHeaderStyle(header);
  var that = this;
  
  var nodeTab = header.appendChild(document.createElement("div"));
  nodeTab.id = "nodeTab";
  this.setTabButtonStyle(nodeTab);
  nodeTab.appendChild(document.createTextNode("Node"));
  GEvent.addDomListener(nodeTab, "mouseover", function(e) {
    that.tabMouseOver(e);
  });
  GEvent.addDomListener(nodeTab, "mouseout", function(e) {
  	that.tabMouseOut(e);
  });
  GEvent.addDomListener(nodeTab, "click", function(e) {
    that.tabClickEvent(e);
  });
  
  var addressTab = header.appendChild(document.createElement("div"));
  addressTab.id = "addressTab";
  this.setTabButtonStyle(addressTab);
  addressTab.appendChild(document.createTextNode("Address"));
  GEvent.addDomListener(addressTab, "mouseover", function(e) {
    that.tabMouseOver(e);
  });
  GEvent.addDomListener(addressTab, "mouseout", function(e) {
  	that.tabMouseOut(e);
  });
  GEvent.addDomListener(addressTab, "click", function(e) {
  	that.tabClickEvent(e);
  });
  
  var body = document.createElement("div");
  var nodeBody = body.appendChild(document.createElement("div"));
  nodeBody.id = "nodeBody";
  this.setTabBodyStyle(nodeBody);
 	this.initializeNodeBody(nodeBody);
  
  var addressBody = body.appendChild(document.createElement("div"));
  addressBody.id = "addressBody";
  this.setTabBodyStyle(addressBody);
  this.initializeAddressBody(addressBody);
  
  //new changes
  var searchResultContainer = body.appendChild(document.createElement("div"));
  searchResultContainer.style.opacity = "0.8";
  searchResultContainer.style.filter = 'alpha(opacity=' + (0.6 * 100) + ')';
  this.setSearchResultStyle(searchResultContainer);
  //new changes
  
  var footer = document.createElement("div");
  this.setTabFooterStyle(footer);
  var buttonDiv = footer.appendChild(document.createElement("div"));
  this.setButtonStyle(buttonDiv, false);
  buttonDiv.appendChild(document.createTextNode("Submit"));
 	GEvent.addDomListener(buttonDiv, "click", function(e) {
 		that.submitSearchClickEvent(e);
 	});

  searchOptionContainer.appendChild(header);
  searchOptionContainer.appendChild(body);
  searchOptionContainer.appendChild(footer);
  //searchOptionContainer.appendChild(nodeBody);
  //searchOptionContainer.appendChild(addressBody);
  
  // default tab
  nodeTab.style.fontWeight = "bold";
 	nodeBody.style.display = "inline";
  NovaButton.globals.searchTabSelected = "nodeTab";
  //new changes
  NovaButton.globals.searchResultContainer = searchResultContainer;
  //new chjanges
}

NovaButton.prototype.initializeNodeBody = function(nodeBody) {
 	var searchByNodeId = document.createElement("input");
 	searchByNodeId.type = "radio";
 	searchByNodeId.defaultChecked = true;
 	searchByNodeId.name = "nodeGroup";
 	searchByNodeId.id = "searchByNodeId";
 	searchByNodeId.value = "searchByNodeId";
 	searchByNodeId.onclick = function () {
 	  searchByLocation.checked = false;
 	  searchByNodeId.checked = true;
 	};
 	nodeBody.appendChild(searchByNodeId);
 	nodeBody.appendChild(document.createTextNode("Node ID"));
 	nodeBody.appendChild(document.createElement("br"));
 	
 	var searchByLocation = document.createElement("input");
 	searchByLocation.type = "radio";
 	searchByLocation.defaultChecked = false;
 	searchByLocation.name = "nodeGroup";
 	searchByLocation.id = "searchByLocation";
 	searchByLocation.value = "searchByLocation";
 	// ie bug
 	searchByLocation.onclick = function () {
 	  searchByLocation.checked = true;
 	  searchByNodeId.checked = false;
 	};
 	nodeBody.appendChild(searchByLocation);
 	nodeBody.appendChild(document.createTextNode("Node Location"));
 	nodeBody.appendChild(document.createElement("br"));
 	nodeBody.appendChild(document.createElement("br"));
 	
 	var exactMatchSearch = document.createElement("input");
 	exactMatchSearch.type = "checkbox";
 	exactMatchSearch.id = "exactMatch";
 	nodeBody.appendChild(exactMatchSearch);
 	nodeBody.appendChild(document.createTextNode("Exact Match"));
 	nodeBody.appendChild(document.createElement("br"));
 	
  var searchInput = document.createElement("input");
 	searchInput.id = "nodeSearchInput";
  searchInput.setAttribute("type", "text");
  searchInput.setAttribute("size", "60");

 	nodeBody.appendChild(searchInput);
}

NovaButton.prototype.initializeAddressBody = function(addressBody) {
  var addressLabel = addressBody.appendChild(document.createElement("div"));
  addressLabel.appendChild(document.createTextNode("Enter address (e.g. 180 park ave, florham park, nj 07932)"));

  var searchInput = document.createElement("input");
  searchInput.id = "addressSearchInput";
  searchInput.setAttribute("type", "text");
  searchInput.setAttribute("size", "60");
  
  addressBody.appendChild(searchInput);
}

//new changes start
NovaButton.prototype.setSearchResult = function(jsonData) {
  var resultContainer = NovaButton.globals.searchResultContainer;
  for (var i = resultContainer.childNodes.length - 1; i >= 0; i--) {
  	resultContainer.removeChild(resultContainer.lastChild);
  }
    
  var table = resultContainer.appendChild(document.createElement("table"));
  var tbody = table.appendChild(document.createElement("tbody"));
  var list = jsonData.list;
    
  if (list == null) {
    var tr = tbody.appendChild(document.createElement("tr"));			
    var td = tr.appendChild(document.createElement("td"));
    td.appendChild(document.createTextNode("Not Found"));
    td.style.fontSize = "12px";
    td.style.textDecoration = "none";
    td.font = "Arial,Helvetica,sans-serif";
    td.style.fontWeight = "normal";
  }
  else {
    for (var i = 0; i < list.length; i++) {
      var tr = tbody.appendChild(document.createElement("tr"));			
      var td = tr.appendChild(document.createElement("td"));
      
      td.appendChild(document.createTextNode(list[i].type + "(" + list[i].numberOfT1 + 
      	"): " + list[i].id));
      td.style.fontSize = "12px";
      td.style.textDecoration = "underline";
      td.style.color = "blue";
      td.style.fontWeight = "normal";
      td.style.cursor = "pointer";
      td.font = "Arial,Helvetica,sans-serif";
      td.id = list[i].latitude + ">>" + list[i].longitude;
      
      td.onmouseover = function() {
      	this.style.fontWeight = "bold";
      }
      
      td.onmouseout = function() {
      	this.style.fontWeight = "normal";
      }
      
      td.onclick = function() {
      	this.style.color = "red";
        var latitudeLongitude = this.id.split(">>");
        var latlng = new GLatLng(latitudeLongitude[0], latitudeLongitude[1]);
        map.setCenter(latlng, 17);
        updateImage(0); 
      }
    }
  }
  
  resultContainer.style.display = "";
}
//new changes end
NovaButton.prototype.initializeLayerDiv = function(layerDiv) {
  for (var i = layerDiv.childNodes.length - 1; i >= 0; i--) {
    layerDiv.removeChild(layerDiv.lastChild);
  }
  
  var colorList = document.getElementById("colorList").value;
  var selectedLayerList = document.getElementById("selectedLayerList").value;
  var colorArray = colorList.split("~!");
  var selectedColorArray = selectedLayerList.split("~!");
  //layerDiv.style.background = "white";
  var table = layerDiv.appendChild(document.createElement("table"));
  table.width="100%";
  table.height="100%";
  //this.setMenuStyle(table);
  /****Creating the data table****/
  var tbody = table.appendChild(document.createElement("tbody"));
  var tr;
  var td;
  var originalLayerState = new Array();
  for (i = 0; i < colorArray.length; i++) {
  	tr = tbody.appendChild(document.createElement("tr"));
  	tr.style.height="20px";
    var colorItem = colorArray[i].split(",");
    
    td = tr.appendChild(document.createElement("td"));
    var chkbox = document.createElement("input");
    chkbox.type="checkbox";
	  chkbox.name = "" + colorItem[0] + "chk";
    chkbox.value = colorItem[0];
    chkbox.id = colorItem[0] + "chk";
    if (this.checkIfColorSelected(selectedColorArray, colorItem[0]) == true){
    	chkbox.defaultChecked=true;
    	originalLayerState[i] = "true," + colorArray[i];
    }
    else {
      originalLayerState[i] = "false," + colorArray[i];
    }
    td.appendChild(chkbox);
	  
    td = tr.appendChild(document.createElement("td"));
    td.width="75%";
    td.appendChild(document.createTextNode(colorItem[0]));
	td.style.fontFamily = "Verdana";
	td.style.fontSize = "12px";
	
    td = tr.appendChild(document.createElement("td"));
    td.width="10%";
    //td.style.background = colorItem[1];
    td.innerHTML="<input type='text' id='" + colorItem[0] + "colorbx' value='" + colorItem[1] + "' size='6' style='background-color:#" + colorItem[1] + "' background-color='#" + colorItem[1] + "' onclick='openPicker(\"" + colorItem[0] + "colorbx\")'/>"
    tbody.appendChild(tr);
  }
  
  NovaButton.globals.originalLayerState = originalLayerState;

  /**to get rid of extra space**/
  /*var layerHeight = layerDiv.style.height.substring(0, layerDiv.style.height.length -2);
  if (colorArray.length * 30 < layerHeight){
  	tr = tbody.appendChild(document.createElement("tr"));
  	tr.height= (layerHeight - (colorArray.length * 40 ) ) + "px";
  	td = tr.appendChild(document.createElement("td"));
  }*/
  
  
  tr = tbody.appendChild(document.createElement("tr"));
  tr.vAlign="bottom";
  tr.style.height="20px";
  td = tr.appendChild(document.createElement("td"));
  td.colSpan="3";
  var buttonTable = td.appendChild(document.createElement("table"));
  buttonTable.height="100%";
  buttonTable.width="100%";
  
  var buttonBody = buttonTable.appendChild(document.createElement("tbody"));
  tr = buttonBody.appendChild(document.createElement("tr"));
  td = tr.appendChild(document.createElement("td"));
  td.width="20%";
  td = tr.appendChild(document.createElement("td"));
  td.appendChild(document.createTextNode("OK"));
  td.onclick = function ()
  	{
  		var selectedLayers = "";
  		var colorList = "";
  		var changed = false;
  		var originalLayerState = NovaButton.globals.originalLayerState;
  		//saving the user selected choices
  		for (i = 0; i < colorArray.length; i++) {
  			var colorItem = colorArray[i].split(",");
  			//saving the selected layers
  			var temp; // variable to hold changes value
  			if (document.getElementById("" + colorItem[0] + "chk").checked == true){
  				selectedLayers = selectedLayers + "~!" + colorItem[0];
  				temp = "true," + colorItem[0];
  			}
  			else {
  			 	temp = "false," + colorItem[0];
  			}
  			//saving the colours of all the layers
  			var userSelectedColor = document.getElementById("" + colorItem[0] + "colorbx").value;
  			if (userSelectedColor.charAt(0) == '#') {
  			  userSelectedColor = userSelectedColor.substring(1);
  			}
  			colorList = colorList + "~!" + colorItem[0] + "," + userSelectedColor;
  			temp = temp + "," + userSelectedColor;
	
  			//check to see if there is anything change and update 
  			// originalLayerState to reflect the changes
  			if (changed) {
  			  originalLayerState[i] = temp;
  			}
  			else if (originalLayerState[i] != temp) {
  			  changed = true;
  			  originalLayerState[i] = temp;
  			}
  		}
  		selectedLayers = selectedLayers.substring(2, selectedLayers.length);
  		colorList = colorList.substring(2, colorList.length);
  		document.getElementById("selectedLayerList").value = selectedLayers;
  		document.getElementById("colorList").value = colorList;
  		
  		// update if there is anything change
  		if (changed) {
  			updateImage(0);
  		}
  		
  		//to remove the layer
  		NovaButton.prototype.showLayerClickEvent();
  	}; 
  td.width="25%";
  this.setButtonStyle(td, true);
  td.style.cursor = "hand";

  td = tr.appendChild(document.createElement("td"));
  td.appendChild(document.createTextNode("Cancel"));
  td.width="25%";
  this.setButtonStyle(td, true);
  td.style.cursor = "hand";
  td.onclick = function ()
  	{
  		//remove all the contents
  		layerDiv.innerHTML=""; 
  		//re-initiate the layers to original values
  		NovaButton.prototype.initializeLayerDiv(layerDiv);
  		NovaButton.prototype.showLayerClickEvent();
  	};    
  td = tr.appendChild(document.createElement("td"));
  td.width="10%";
  
}

NovaButton.prototype.checkIfColorSelected= function(selectedColorArray, colorItem) {
	for (j = 0; j < selectedColorArray.length; j++) {
		if (selectedColorArray[j] ==  colorItem){
			return true;
		}
	}
	return false;
}

NovaButton.prototype.setMenuContainerStyle = function(menuContainer) {
	menuContainer.style.position = "absolute";
	menuContainer.style.background = "#FFFFF0"
	menuContainer.style.overflow = "auto";
	menuContainer.style.scrolling="yes";
	menuContainer.style.display = "none";
	menuContainer.style.width = "300px";
	menuContainer.style.height = "300px";
}

NovaButton.prototype.setMenuStyle = function(menu) {
  menu.style.width = "300px";
  menu.style.height = "300px";
	menu.style.background	= "#FFF";
}

NovaButton.prototype.setMenuItemStyle = function(menuItem) {
  menuItem.style.padding	= "4px 3px 4px 33px";
	menuItem.style.fontFamily = "Vardana, Arial, Helvetica, sans-serif" ;
	menuItem.style.fontSize	= "10pt";
	menuItem.style.color	= "#333" ;
	menuItem.style.cursor	= "default" ;
	menuItem.style.background = "no-repeat left center" ;
}

NovaButton.prototype.showLayerOption = function(posX, posY) {
	var globals = NovaButton.globals;

	//position the menu at the given location
	if(typeof posX == "number") {
		globals.layerOptionContainer.style.left = posX + "px";
	}
	
	if(typeof posY == "number") {
		globals.layerOptionContainer.style.top = posY + "px";
	}
	//new chanes start
	var resultContainer = globals.searchResultContainer;
	resultContainer.style.display = "none";
    //nw changes end
	//display the menu
	globals.layerOptionContainer.style.display = "block";
}

NovaButton.prototype.showSearchOption = function(posX, posY) {
  var globals = NovaButton.globals;

	//position the menu at the given location
	if(typeof posX == "number") {
		globals.searchOptionContainer.style.left = posX + "px";
	}
	
	if(typeof posY == "number") {
		globals.searchOptionContainer.style.top = posY + "px";
	}
	
	//display the menu
	globals.searchOptionContainer.style.display = "inline";
}

NovaButton.prototype.defaultViewClickEvent = function() {
  var defaultZoomLevel = document.getElementById("defaultZoomLevel").value * 1;
	map.setCenter(new GLatLng(document.getElementById("defaultCenterLatitude").value, 
  	document.getElementById("defaultCenterLongitude").value),
  	defaultZoomLevel);
  updateImage(0);
}

NovaButton.prototype.showLayerClickEvent = function(e) {
  if (NovaButton.globals.layerOptionVisible) {
  	//to remove the color picker
  	cancel();  		
	  NovaButton.globals.layerOptionVisible = false;
	  NovaButton.globals.layerOptionContainer.style.display = "none";
	  return;
	}
	
	if (NovaButton.globals.searchOptionContainer.style.display != "none"){
		NovaButton.prototype.searchClickEvent();
	}
	if (CingularButton.globals.listOptionContainer.style.display != "none"){
		currentMonthClickEvent();
	}
	
  if (!e) {
  	e = window.event;
 	}
 	
	e.cancelBubble = true;
	
	if (e.stopPropagation) {
		e.stopPropagation();
	}
	
	var button = Event.element(e);
	var posX = button.offsetLeft;
	var posY = button.offsetTop;
	this.showLayerOption(posX, posY + button.offsetHeight);
	NovaButton.globals.layerOptionVisible = true;
}

NovaButton.prototype.searchClickEvent = function(e) {
  if (NovaButton.globals.searchOptionVisible) {
	  NovaButton.globals.searchOptionVisible = false;
	  NovaButton.globals.searchOptionContainer.style.display = "none";
	  return;
	}
	
	if (NovaButton.globals.layerOptionContainer.style.display != "none"){
		NovaButton.prototype.showLayerClickEvent();
  }
  if (CingularButton.globals.listOptionContainer.style.display != "none"){
		currentMonthClickEvent();
  }
	
  if (!e) {
  	e = window.event;
 	}
 	
	e.cancelBubble = true;
	
	if (e.stopPropagation) {
		e.stopPropagation();
	}
	
	var button = Event.element(e);
	var posX = button.offsetLeft;
	var posY = button.offsetTop;
	this.showSearchOption(posX, posY + button.offsetHeight + 3);
	NovaButton.globals.searchOptionVisible = true;
}

NovaButton.prototype.submitSearchClickEvent = function(e) {
  if (NovaButton.globals.searchTabSelected == "nodeTab") {
    var input = document.getElementById("nodeSearchInput").value.trim();
    
    if (input.length == 0) {
      alert("Input field required");
      return;
    }
    
    var searchType;
    // ie bug
    var searchByNodeId = document.getElementById("searchByNodeId");
    
    if (searchByNodeId.checked) {
    	searchType = searchByNodeId.value;
    }
    else {
      searchType = document.getElementById("searchByLocation").value;
    }
    
    var checkbox = document.getElementById("exactMatch");
    
		searchObject(input, searchType, checkbox.checked, e.clientX, e.clientY);
  }
  else {
    var address = document.getElementById("addressSearchInput").value.trim();
    
    if (address.length == 0) {
      alert("Address field required");
      return;
    }

    geocoder.getLatLng(
        address,
        function(ll) {
            if (ll != null) {
        		  if (addressMarker == null) {
           			addressMarker = new GMarker(ll);
           			map.addOverlay(addressMarker);
           			GEvent.addListener(addressMarker, "click", marker_onclick);
            	}
            	else {
            		addressMarker.setLatLng(ll);
            	}
            	
            	map.setCenter(ll);
            	updateImage(0);
            }
        }
    );
  }
  
  this.searchClickEvent(e);
}

function marker_onclick() {
  map.removeOverlay(addressMarker);
  addressMarker = null;
}

// start drag zoom function
NovaButton.prototype.initCover = function() {
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  dragZoomGlobals.mapPosition = NovaButtonUtils.getElementPosition(NovaButton.globals.map.getContainer());
  this.setDimensions();
  
  dragZoomGlobals.zoomArea.style.display = "block";
  dragZoomGlobals.zoomArea.style.background = "#000";
  
  dragZoomGlobals.outlineDiv.style.width = "0px";
  dragZoomGlobals.outlineDiv.style.height = "0px";
};

NovaButton.prototype.initZoomAreaStyles = function(){
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  dragZoomGlobals.zoomArea.style.background = "#000";
  dragZoomGlobals.zoomArea.style.opacity = "0.2";
  dragZoomGlobals.zoomArea.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerTopDiv.style.background = "#000";
  dragZoomGlobals.cornerTopDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerTopDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerRightDiv.style.background = "#000";
  dragZoomGlobals.cornerRightDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerRightDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerBottomDiv.style.background = "#000";
  dragZoomGlobals.cornerBottomDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerBottomDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerLeftDiv.style.background = "#000";
  dragZoomGlobals.cornerLeftDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerLeftDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';

  dragZoomGlobals.outlineDiv.style.border = "2px solid blue";  
};

NovaButton.prototype.resetDragZoom = function() {
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  dragZoomGlobals.zoomArea.style.display = "none";
  dragZoomGlobals.zoomArea.style.opacity = "0.2";
  dragZoomGlobals.zoomArea.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerTopDiv.style.display = "none";
  dragZoomGlobals.cornerTopDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerTopDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerRightDiv.style.display = "none";
  dragZoomGlobals.cornerRightDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerRightDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerBottomDiv.style.display = "none";
  dragZoomGlobals.cornerBottomDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerBottomDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';
  
  dragZoomGlobals.cornerLeftDiv.style.display = "none";
  dragZoomGlobals.cornerLeftDiv.style.opacity = "0.2";
  dragZoomGlobals.cornerLeftDiv.style.filter = 'alpha(opacity=' + (0.2 * 100) + ')';

  dragZoomGlobals.outlineDiv.style.display = 'none';	
};

NovaButton.prototype.setZoomAreaStyle = function(zoomArea) {
	zoomArea.style.position = "absolute";
	zoomArea.style.display = "none";
	zoomArea.style.overflow = "hidden";
	zoomArea.style.cursor = "crosshair";
	zoomArea.style.zIndex = "101";
}

NovaButton.prototype.getRectangle = function(startX, startY, pos){
  var left = false;
  var top = false;
  var dX = pos.left - startX;
  var dY = pos.top - startY;	
  if (dX < 0) {
    dX = dX * -1;
    left = true;
  }
  if (dY < 0) {
    dY = dY * -1;
    top = true;
  }
  delta = dX > dY ? dX : dY;

  return {
    startX: startX,
    startY: startY,
    endX: startX + dX,
    endY: startY + dY,
    width: dX,
    height: dY,
    left:left,
    top:top
  }
};

NovaButton.prototype.mouseDown = function(e) {
	if (NovaButton.globals.currentButton != "zoomRectangleDiv") {
		return;
	}
	
	var dragZoomGlobals = NovaButton.globals.dragzoom;
  var pos = this.getRelPos(e);
  dragZoomGlobals.startX = pos.left;
  dragZoomGlobals.startY = pos.top;

	dragZoomGlobals.zoomArea.style.background = "transparent";
	dragZoomGlobals.zoomArea.style.opacity = "1";
	dragZoomGlobals.zoomArea.style.filter = 'alpha(opacity=100)';
	
	dragZoomGlobals.outlineDiv.style.left = dragZoomGlobals.startX + "px";
	dragZoomGlobals.outlineDiv.style.top = dragZoomGlobals.startY + "px";
	dragZoomGlobals.outlineDiv.style.display = "block";
	dragZoomGlobals.outlineDiv.style.width = "1px";
	dragZoomGlobals.outlineDiv.style.height = "1px";

  dragZoomGlobals.cornerTopDiv.style.top = (dragZoomGlobals.startY - dragZoomGlobals.mapHeight) + 'px';
  dragZoomGlobals.cornerTopDiv.style.display ='block';
  dragZoomGlobals.cornerLeftDiv.style.left = (dragZoomGlobals.startX - dragZoomGlobals.mapWidth) +'px';
  dragZoomGlobals.cornerLeftDiv.style.top = dragZoomGlobals.startY + 'px';
  dragZoomGlobals.cornerLeftDiv.style.display = 'block';

  dragZoomGlobals.cornerRightDiv.style.left = dragZoomGlobals.startX + 'px';
  dragZoomGlobals.cornerRightDiv.style.top = dragZoomGlobals.startY + 'px';
  dragZoomGlobals.cornerRightDiv.style.display = 'block';
  dragZoomGlobals.cornerBottomDiv.style.left = dragZoomGlobals.startX + 'px';
  dragZoomGlobals.cornerBottomDiv.style.top = dragZoomGlobals.startY + 'px';
  dragZoomGlobals.cornerBottomDiv.style.width = '0px';
  dragZoomGlobals.cornerBottomDiv.style.display = 'block';
  
  dragZoomGlobals.moving = true;

  return false;
}

NovaButton.prototype.mouseMove = function(e) {
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  
  if(dragZoomGlobals.moving) {
    var pos = this.getRelPos(e);
    var rect = this.getRectangle(dragZoomGlobals.startX, dragZoomGlobals.startY, pos);

    if (rect.left) {
      addX = -rect.width;			
    } else { 
      addX = 0;
    }

    if (rect.top) {
      addY = -rect.height;
    } else {
      addY = 0;
    }

		dragZoomGlobals.outlineDiv.style.left = dragZoomGlobals.startX + addX + "px";
		dragZoomGlobals.outlineDiv.style.top = dragZoomGlobals.startY + addY + "px";
		dragZoomGlobals.outlineDiv.style.display = "block";
    dragZoomGlobals.outlineDiv.style.width = rect.width + "px";
    dragZoomGlobals.outlineDiv.style.height = rect.height + "px";

    dragZoomGlobals.cornerTopDiv.style.height = ((dragZoomGlobals.startY + addY) - (dragZoomGlobals.startY - dragZoomGlobals.mapHeight)) + 'px';
    dragZoomGlobals.cornerLeftDiv.style.top = (dragZoomGlobals.startY + addY) + 'px';
    dragZoomGlobals.cornerLeftDiv.style.width = ((dragZoomGlobals.startX + addX) - (dragZoomGlobals.startX - dragZoomGlobals.mapWidth)) + 'px';
    dragZoomGlobals.cornerRightDiv.style.top = dragZoomGlobals.cornerLeftDiv.style.top;
    dragZoomGlobals.cornerRightDiv.style.left = (dragZoomGlobals.startX + addX + rect.width + 4) + 'px';
    dragZoomGlobals.cornerBottomDiv.style.top = (dragZoomGlobals.startY + addY + rect.height + 4) + 'px';
    dragZoomGlobals.cornerBottomDiv.style.left = (dragZoomGlobals.startX - dragZoomGlobals.mapWidth + ((dragZoomGlobals.startX + addX) - (dragZoomGlobals.startX - dragZoomGlobals.mapWidth))) + 'px';
    dragZoomGlobals.cornerBottomDiv.style.width = (rect.width + 4) + 'px';
		
    return false;
  }  
}

NovaButton.prototype.mouseUp = function(e) {
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  var map = NovaButton.globals.map;
  
  if (dragZoomGlobals.moving) {
    var pos = this.getRelPos(e);
    dragZoomGlobals.moving = false;
    
    var rect = this.getRectangle(dragZoomGlobals.startX, dragZoomGlobals.startY, pos);

    if (rect.left) {
    	rect.endX = rect.startX - rect.width;
    }
    
    if (rect.top) {
      rect.endY = rect.startY - rect.height;
    }
	
    this.resetDragZoom();
    
    if (rect.width >= 0 && rect.height >= 0) {
      var nwpx = new GPoint(rect.startX, rect.startY);
      var nepx = new GPoint(rect.endX, rect.startY);
      var sepx = new GPoint(rect.endX, rect.endY);
      var swpx = new GPoint(rect.startX, rect.endY);
      var nw = NovaButton.globals.map.fromContainerPixelToLatLng(nwpx); 
      var ne = NovaButton.globals.map.fromContainerPixelToLatLng(nepx); 
      var se = NovaButton.globals.map.fromContainerPixelToLatLng(sepx); 
      var sw = NovaButton.globals.map.fromContainerPixelToLatLng(swpx); 

      var zoomAreaPoly = new GPolyline([nw, ne, se, sw, nw], "blue", 3, .4);
      var polyBounds = zoomAreaPoly.getBounds();
      var ne = polyBounds.getNorthEast();
      var sw = polyBounds.getSouthWest();
      var se = new GLatLng(sw.lat(), ne.lng());
      var nw = new GLatLng(ne.lat(), sw.lng());
      var zoomLevel = map.getBoundsZoomLevel(polyBounds);
      var center = polyBounds.getCenter();
      map.setCenter(center, zoomLevel);
    }

    this.initCover();
  }
}

NovaButton.prototype.setDimensions = function() {
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  var mapSize = NovaButton.globals.map.getSize();
  dragZoomGlobals.mapWidth  = mapSize.width;
  dragZoomGlobals.mapHeight = mapSize.height;
  dragZoomGlobals.mapRatio  = dragZoomGlobals.mapHeight / dragZoomGlobals.mapWidth;
  // set left:0px in next <div>s in case we inherit text-align:center from map <div> in IE.
  dragZoomGlobals.zoomArea.style.top = "0px";
  dragZoomGlobals.zoomArea.style.left = "0px";
  dragZoomGlobals.zoomArea.style.width = dragZoomGlobals.mapWidth + "px";
  dragZoomGlobals.zoomArea.style.height = dragZoomGlobals.mapHeight + "px";
  
  dragZoomGlobals.cornerTopDiv.style.top = "0px";
  dragZoomGlobals.cornerTopDiv.style.left = "0px";
  dragZoomGlobals.cornerTopDiv.style.width = dragZoomGlobals.mapWidth + "px";
  dragZoomGlobals.cornerTopDiv.style.height = dragZoomGlobals.mapHeight + "px";
  
  dragZoomGlobals.cornerRightDiv.style.top = "0px";
  dragZoomGlobals.cornerRightDiv.style.left = "0px";
  dragZoomGlobals.cornerRightDiv.style.width = dragZoomGlobals.mapWidth + "px";
  dragZoomGlobals.cornerRightDiv.style.height = dragZoomGlobals.mapHeight + "px";
  
  dragZoomGlobals.cornerBottomDiv.style.top = "0px";
  dragZoomGlobals.cornerBottomDiv.style.left = "0px";
  dragZoomGlobals.cornerBottomDiv.style.width = dragZoomGlobals.mapWidth + "px";
  dragZoomGlobals.cornerBottomDiv.style.height = dragZoomGlobals.mapHeight + "px";
  
  dragZoomGlobals.cornerLeftDiv.style.top = "0px";
  dragZoomGlobals.cornerLeftDiv.style.left = "0px";
  dragZoomGlobals.cornerLeftDiv.style.width = dragZoomGlobals.mapWidth + "px";
  dragZoomGlobals.cornerLeftDiv.style.height = dragZoomGlobals.mapHeight + "px";
};

NovaButton.prototype.getRelPos = function(e) {
  var pos = NovaButtonUtils.getMousePosition(e);
  var dragZoomGlobals = NovaButton.globals.dragzoom;
  return {top: (pos.top - dragZoomGlobals.mapPosition.top), 
          left: (pos.left - dragZoomGlobals.mapPosition.left)};
};
// end drag zoom function

var NovaButtonUtils = {
};

NovaButtonUtils.getElementPosition = function(element) {
  var leftPos = element.offsetLeft;          // initialize var to store calculations
  var topPos = element.offsetTop;            // initialize var to store calculations
  var parElement = element.offsetParent;     // identify first offset parent element  
  while (parElement != null ) {                // move up through element hierarchy
    leftPos += parElement.offsetLeft;      // appending left offset of each parent
    topPos += parElement.offsetTop;  
    parElement = parElement.offsetParent;  // until no more offset parents exist
  }
  return {left: leftPos, top: topPos};
};

NovaButtonUtils.getMousePosition = function(e) {
  var posX = 0;
  var posY = 0;
  if (!e) var e = window.event;
  if (e.pageX || e.pageY) {
    posX = e.pageX;
    posY = e.pageY;
  } else if (e.clientX || e.clientY){
    posX = e.clientX + 
      (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
    posY = e.clientY + 
      (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
  }
  return {left: posX, top: posY};  
};