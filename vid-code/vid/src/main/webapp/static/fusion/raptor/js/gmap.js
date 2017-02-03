var map;
var popupTable;
var networkOverlay;
var selectedNetworkOverlay;
var flashTimeout;
//var infoDiv;
var mainInfoDiv;
var locationInfoDiv;
var loadingAnimationDiv;
var geocoder;
var addressMarker;
var popupDiv;
//var novaButton;

function novaMapInit() {
	if (GBrowserIsCompatible()) {
		var dmap = document.getElementById("map");
		map = new GMap2(dmap);
		var defaultCenterLongitude = document.getElementById("defaultCenterLongitude").value;
		var defaultCenterLatitude = document.getElementById("defaultCenterLatitude").value;
		var defaultZoomLevel = document.getElementById("defaultZoomLevel").value * 1;
		
		map.setCenter(new GLatLng(defaultCenterLatitude, defaultCenterLongitude), defaultZoomLevel); 
		
	    map.enableScrollWheelZoom();
	    map.enableContinuousZoom();
	    map.addControl(new GLargeMapControl());
	    map.addControl(new GMapTypeControl());
	    
	    geocoder = new GClientGeocoder();
	
	    GEvent.addListener(map, "dragend", mapDragEnd);
	    GEvent.addListener(map, "zoomend", mapZoomChanged);
	    GEvent.addListener(map, "click", mapClicked);
	    GEvent.addListener(map, "moveend", mapMoveEnd)
	    GEvent.addListener(map, "singlerightclick", mapRightClicked);
	    GEvent.addListener(map, "mousemove", function(latlng) {
	    	locationInfoDiv.innerHTML = latlng.lng().toFixed(3) + ", " + latlng.lat().toFixed(3);
	    });
	
	    var n = document.getElementById("popupmenudiv2");
	    popupDiv = n.cloneNode(true);
	    map.getContainer().appendChild(popupDiv);
	    
	    popupTable = document.getElementById("popupmenudivtb");
	    
	    // detail info div
	  	/*infoDiv = document.createElement("div");
	  	infoDiv.style.display = "none";
	  	infoDiv.style.position = "absolute";
	  	infoDiv.style.overflow = "auto";
	  	infoDiv.style.top = "0px"; 
	  	infoDiv.style.left = "0px";
	  	infoDiv.style.zIndex = "1001";
	  	infoDiv.style.border = "1px outset #000";
	  	infoDiv.style.background = "#C6DEFF";
	  	infoDiv.style.height = "150px";
	  	infoDiv.style.width = "300px";
	  	infoDiv.align = "center";*/
	  	
	  	mainInfoDiv = document.createElement("div");
	  	mainInfoDiv.style.display = "none";
	  	mainInfoDiv.style.position = "absolute";
	  	mainInfoDiv.style.overflow = "auto";
	  	mainInfoDiv.style.top = "0px"; 
	  	mainInfoDiv.style.left = "0px";
	  	mainInfoDiv.style.zIndex = "1001";
	  	mainInfoDiv.style.border = "1px outset #000";
	  	mainInfoDiv.style.background = "#C6DEFF";
	  	mainInfoDiv.style.height = "150px";
	  	mainInfoDiv.style.width = "300px";
	  	mainInfoDiv.align = "center";

	  	//mainInfoDiv = infoDiv.appendChild(document.createElement("div"));
	  	mainInfoDiv.style.overflow = "auto";
	  	mainInfoDiv.style.height = "150px";
	  	mainInfoDiv.style.width = "300px";
	  	//mainInfoDiv.align = "center";
	  	
	  	/*var closeInfoDiv = infoDiv.appendChild(document.createElement("div"));
	  	closeInfoDiv.appendChild(document.createTextNode("Close"));
	  	
	  	closeInfoDiv.style.background = "#FFFFF0";
	  	closeInfoDiv.style.color = "black";
		closeInfoDiv.style.fontFamily = "Verdana";
		closeInfoDiv.style.fontSize = "12px";
		closeInfoDiv.style.fontWeight= "bold";
		closeInfoDiv.style.border = "2px solid black";
		closeInfoDiv.style.padding = "0px";
		closeInfoDiv.style.marginBottom = "0px";
		closeInfoDiv.style.textAlign = "center";
		closeInfoDiv.style.width = "80px";
		closeInfoDiv.style.height = "15px";
		closeInfoDiv.style.cursor = "pointer";
	  	
	  	GEvent.addDomListener(closeInfoDiv, "click", function() {
	  	  infoDiv.style.display = "none";
	  	});	*/
	  	
	  	// longitude latitude div
	  	var locationInfoWidth = 200;
	  	var locationInfoHeight = 15;
	  	locationInfoDiv = document.createElement("div");
	  	locationInfoDiv.style.display = "inline";
	  	locationInfoDiv.style.position = "absolute";
	  	locationInfoDiv.style.zIndex = "1";
	  	locationInfoDiv.style.height = locationInfoHeight + "px";
	  	locationInfoDiv.style.width = locationInfoWidth + "px";
	  	locationInfoDiv.align = "center";
	  	locationInfoDiv.style.color = "#4CC417";
	  	locationInfoDiv.style.fontWeight = "bold";
	  	locationInfoDiv.appendChild(document.createTextNode("Longitude Latitude"));
	
	  	var mapWidth = map.getSize().width;
	  	var mapHeight = map.getSize().height;
	  	
	  	locationInfoDiv.style.top = (mapHeight - locationInfoHeight - 5) + "px";
	  	locationInfoDiv.style.left = (mapWidth / 2 - locationInfoWidth / 2) + "px";
	  	
	  	// loading image animation
	  	loadingAnimationDiv = document.createElement("div");
	  	loadingAnimationDiv.style.position = "absolute";
	  	loadingAnimationDiv.style.zIndex = "100000";
	  	loadingAnimationDiv.style.display = "none";
	  	loadingAnimationDiv.style.width = "50px";
	  	loadingAnimationDiv.style.height = "50px";
	  	loadingAnimationDiv.style.top = (mapHeight / 2 - 50 / 2) + "px";
	  	loadingAnimationDiv.style.left = (mapWidth / 2 - 50 / 2) + "px";
	  	var imgDiv = loadingAnimationDiv.appendChild(document.createElement("img"));
	  	imgDiv.src = document.getElementById("imgFolder").value + "loading.gif";
	  	
	  
	  	//map.getContainer().appendChild(infoDiv);
		map.getContainer().appendChild(mainInfoDiv);
	  	map.getContainer().appendChild(locationInfoDiv);
	  	map.getContainer().appendChild(loadingAnimationDiv);
	  	updateImage(0);
	}
}

function mapRightClicked(point, src) {
	var latlng = map.fromContainerPixelToLatLng(new GPoint(point.x , point.y));
	alert(latlng.lng() + ", " + latlng.lat());
}
/*
function searchObject(searchInput, searchType, exactMatch, clickX, clickY) {
  loadingAnimationDiv.style.display = "inline";
  var baseURL = document.getElementById("baseURL").value;
  //var url = baseURL + "/gmap_controller.htm?action=searchObject";
  //var url = baseURL + "report.gmap.search_object";
  var url = baseURL + "/gmapservlet?action=searchObject&nextpage=report.gmap.search_object";
  url += "&search_input=" + searchInput;
  url += "&search_type=" + searchType;
  url += "&exact_match=" + exactMatch;
  url += "&object_type=CELLSITE";
  
  if (clickX == null || clickY == null) {
    var mapWidth = map.getSize().width;
  	var mapHeight = map.getSize().height;
  	clickX = mapWidth / 2;
  	clickY = mapHeight / 2;
  }
  
  url += "&client_x=" + clickX;
  url += "&client_y=" + clickY;
  
  new Ajax.Request(url, {
  	method: 'get',
  	onSuccess: function(transport) {
  	  loadingAnimationDiv.style.display = "none";
  	  var jsonData = transport.responseText.evalJSON();
  	  var list = jsonData.list;
  	  
  	  if (list == null) {
  	    alert("Not found");
  	  }
  	  else {
  	    if (list.length == 1) {
  	      var longitude = list[0].longitude;
  	      var latitude = list[0].latitude;
  	      map.setCenter(new GLatLng(latitude, longitude));
  	      updateImage(0);
  	    }
  	    else {
  	      for (var i = popupTable.childNodes.length - 1; i >= 0; i--) {
            popupTable.removeChild(popupTable.lastChild);
          }
            
  	      for (var i = 0; i < list.length; i++) {
					  var tr = popupTable.appendChild(document.createElement("tr"));			
          	var td = tr.appendChild(document.createElement("td"));
          	td.appendChild(document.createTextNode(list[i].type + "(" + list[i].numberOfT1 + 
        	  	"): " + list[i].id));
          	td.style.border = "solid";
          	td.style.borderWidth = "thin";
          	td.style.borderRight = "none";
          	td.style.borderLeft = "none";
        	
          	if (i == 0) {
            	td.style.borderTop = "none";
          	}
        	
          	if (i == list.length - 1) {
          		td.style.borderBottom = "none";
          	}
        	
          	td.style.color = "#0000FF";
          	td.style.background = "#FFFFF0";
           	td.style.fontSize = "12px";
          	td.font = "Arial,Helvetica,sans-serif";
          	td.id = list[i].latitude + ">>" + list[i].longitude;
        	
          	td.onmouseover = function(e) {
              this.style.background = "#0000FF";
              this.style.color = "#FFFFF0";
            }
        	
          	td.onmouseout = function(e) {
              this.style.background = "#FFFFF0";
              this.style.color = "#0000FF";
            }
          
            td.onclick = function(e) {
              //var popupDiv = document.getElementById("popupmenudiv");
              popupDiv.style.display = "none";
              var latitudeLongitude = this.id.split(">>");
              var latlng = new GLatLng(latitudeLongitude[0], latitudeLongitude[1]);
            	map.setCenter(latlng);
            	updateImage(0); 
            }
				  }
				  
				  //var popupDiv = document.getElementById("popupmenudiv");
				  popupDiv.style.display = "";
 	
      	  var textWidth = popupDiv.offsetWidth;
     		  var textHeight = popupDiv.offsetHeight;
         
      	  if (textHeight >= 200) {
      	  	popupDiv.style.overflow = "auto";
      	  	popupDiv.style.height = "200px";
      	  	textHeight = 200;
      	  }
      	
      	  if (textWidth >= 250) {
      	  	popupDiv.style.width = "250px";
      	  	//textWidth = 250;
      	  }
      	
        	var clientX = jsonData.clientX;
        	var clientY = jsonData.clientY;
      	
        	if ((clientX * 1 + textWidth + 20) >= screen.availWidth) {
     	    	clientX = clientX - textWidth - 20;
     	  	}
     	
     	  	if ((clientY * 1 + textHeight) >= document.getElementById("map").offsetHeight) {
     	  		clientY = document.getElementById("map").offsetHeight - textHeight;
     		  
     	  	}
     	  	
        	popupDiv.style.top = clientY + "px";
        	popupDiv.style.left = clientX + "px";
  	    }
  	  }
  	}
  });	
}
*/
function getInfo(info, x, y) {
  var baseURL = document.getElementById("baseURL").value;
  //var url = baseURL + "/gmap_controller.htm?action=getInfo";
  //var url = baseURL + "report.gmap.get_info";
  var url = baseURL + "/gmapservlet?action=getInfo&nextpage=report.gmap.get_info";
  url += "&info=" + info;
  url += "&client_x=" + x;
  url += "&client_y=" + y;
  
  new Ajax.Request(url, {
  	method: 'get',
  	onSuccess: function(transport) {
  		var jsonData = transport.responseText.evalJSON();
  		var clientX = jsonData.clientX;
  		var clientY = jsonData.clientY;
  		var attributes = jsonData.attributes;
  		
  		for (var i = mainInfoDiv.childNodes.length - 1; i >= 0; i--) {
      	mainInfoDiv.removeChild(mainInfoDiv.lastChild);
      }
      
     	mainInfoDiv.align="left";
     	var menuUL = mainInfoDiv.appendChild(document.createElement("ul"));
     	var menuItem = document.createElement("li");
     	menuItem.appendChild(document.createTextNode("Row : " + jsonData.id));
     	menuUL.appendChild(menuItem);
     	
     	menuItem = document.createElement("li");
     	menuItem.appendChild(document.createTextNode("Longitude: " + jsonData.longitude));
     	menuUL.appendChild(menuItem);
     	
     	menuItem = document.createElement("li");
     	menuItem.appendChild(document.createTextNode("Latitude: " + jsonData.latitude));
     	menuUL.appendChild(menuItem);
     	
     	var table = mainInfoDiv.appendChild(document.createElement("table"));
     	table.width="100%";
     	table.align="left";
     	var tbody = table.appendChild(document.createElement("tbody"));
     	//tbody.style.borderCollapse = "collapse";
     	//var tr = tbody.appendChild(document.createElement("tr"));
     	var td;
     	for (var i = 0; i < attributes.length ; i++) {
     		tr = tbody.appendChild(document.createElement("tr"));
     		td = tr.appendChild(document.createElement("td"));
     		td.align="left";
     		td.width="25%"
     		td.style.padding = "2px";
     		td.appendChild(document.createTextNode(attributes[i].key));
     		td = tr.appendChild(document.createElement("td"));
     		td.style.padding = "2px";
     		td.appendChild(document.createTextNode(attributes[i].value));
     	}
     	
     	var textWidth = 300;
     	var textHeight = 150 + 15;
     	
		if ((clientX * 1 + textWidth + 20) >= screen.availWidth) {
     	  clientX = clientX - textWidth - 20;
     	}
     	
     	if ((clientY * 1 + textHeight) >= document.getElementById("map").offsetHeight) {
     		clientY = document.getElementById("map").offsetHeight - textHeight;		  
     	}
      
	mainInfoDiv.style.top = clientY + "px";
      	mainInfoDiv.style.left = clientX + "px";
	mainInfoDiv.style.display = "inline";
  	}
  });
}

function mapClicked(overlay, latlng, overlaylatlng) {
	var baseURL = document.getElementById("baseURL").value;
  //var url = baseURL + "/gmap_controller.htm?action=singleLeftClick";
  //var url = baseURL + "report.gmap.single_left_click";
  var url = baseURL + "/gmapservlet?action=singleLeftClick&nextpage=report.gmap.single_left_click";
  var mapSize = map.getSize();
  var mapBounds = map.getBounds();
  var pointSW = mapBounds.getSouthWest();
  var pointNE = mapBounds.getNorthEast();
  url += "&click_longitude=" + latlng.lng();
  url += "&click_latitude=" + latlng.lat();
  url += "&zoom_level=" + map.getZoom();
  url += "&map_size=" + mapSize.width + "," + mapSize.height;
  url += "&map_bounds=" + pointSW.lng() + "," + pointSW.lat() + "," + pointNE.lng() + "," + pointNE.lat();
  var point = map.fromLatLngToContainerPixel(latlng);
  var clickX = point.x;
  var clickY = point.y;
  url += "&client_x=" + clickX;
  url += "&client_y=" + clickY;
  
  //document.getElementById("popupmenudiv").style.display = "none";
  popupDiv.style.display="none";
  mainInfoDiv.style.display="none";
  new Ajax.Request(url, {
  	method: 'get',
  	onSuccess: function(transport) {
  		var jsonData = transport.responseText.evalJSON();
			var list = jsonData.list;
			var selectedImageURL = jsonData.selectedImageURL;
			var legendImageURL = jsonData.legendImageURL;
			//alert(legendImageURL);
			if (legendImageURL != null) {
				repaintLegend(legendImageURL);
			}
			
			if (list != null) {
				for (var i = popupTable.childNodes.length - 1; i >= 0; i--) {
          			popupTable.removeChild(popupTable.lastChild);
        		}
    	
				for (var i = 0; i < list.length; i++) {
					var tr = popupTable.appendChild(document.createElement("tr"));			
		        	var td = tr.appendChild(document.createElement("td"));
		        	td.appendChild(document.createTextNode("Row : " + list[i].id));
		        	td.style.border = "solid";
		        	td.style.borderWidth = "thin";
		        	td.style.borderRight = "none";
		        	td.style.borderLeft = "none";
		        	
		        	if (i == 0) {
		        		td.style.borderTop = "none";
		        	}
		        	
		        	if (i == list.length - 1) {
		        		td.style.borderBottom = "none";
		        	}
		        	
		        	td.style.color = "#0000FF";
		        	td.style.background = "#FFFFF0";
		        	td.style.fontSize = "12px";
		        	td.font = "Arial,Helvetica,sans-serif";
		        	td.id = jsonData.type + ">>" + list[i].id + ">>" + list[i].type;
		        	
		        	td.onmouseover = function(e) {
		            this.style.background = "#0000FF";
		            this.style.color = "#FFFFF0";
				}
        	
	        	td.onmouseout = function(e) {
	            this.style.background = "#FFFFF0";
	            this.style.color = "#0000FF";
          }
          
          td.onclick = function(e) {
	          //var popupDiv = document.getElementById("popupmenudiv");
	          getInfo(this.id, clickX, clickY);
	            
	          popupDiv.style.display = "none";
		  }
    }
							
	//var popupDiv = document.getElementById("popupmenudiv");
	popupDiv.style.display = "";
 	
    var textWidth = popupDiv.offsetWidth;
    var textHeight = popupDiv.offsetHeight;
         
    if (textHeight >= 200) {
    	popupDiv.style.overflow = "auto";
      	popupDiv.style.height = "200px";
      		textHeight = 200;
	}
      	
    if (textWidth >= 250) {
    	popupDiv.style.width = "250px";
      	//textWidth = 250;
	}
      	
    var clientX = jsonData.clientX;
    var clientY = jsonData.clientY;
      	
    if ((clientX * 1 + textWidth + 20) >= screen.availWidth) {
    	clientX = clientX - textWidth - 20;
     }
     	
     if ((clientY * 1 + textHeight) >= document.getElementById("map").offsetHeight) {
     	clientY = document.getElementById("map").offsetHeight - textHeight;
     		  
     }
      
     popupDiv.style.top = clientY + "px";
     popupDiv.style.left = clientX + "px";
	}
	else if (selectedImageURL != null) {
		repaintSelected(selectedImageURL);
	}
  	}
  });
}

//new changes

function mapMoveEnd() {
  if (networkOverlay != null) {
		map.removeOverlay(networkOverlay);
  	networkOverlay = null;
  }

  
	updateImage(0);
}

function mapDragEnd() {
}

function mapZoomChanged(oldZoomLevel, newZoomLevel) {
}
/*
function updateSelectedImage() {
	var baseURL = document.getElementById("baseURL").value;
	var url = baseURL + "/gmap_controller.htm?action=nova.gmap.fetch.selected.image";
	var mapSize = map.getSize();
	var mapBounds = map.getBounds();
	var pointSW = mapBounds.getSouthWest();
	var pointNE = mapBounds.getNorthEast();
	url += "&zoomLevel=" + map.getZoom();
	url += "&mapSize=" + mapSize.width + "," + mapSize.height;
	url += "&mapBounds=" + pointSW.lng() + "," + pointSW.lat() + "," + pointNE.lng() + "," + pointNE.lat();
	
	new Ajax.Request(url, {     
		method: 'get',
		asynchronous: false,
		onSuccess: function(transport) {      
			var url = transport.responseXML.getElementsByTagName("image-url")[0].childNodes[0].nodeValue;
			
			if (url != null) {
				repaintSelected(url);
			}
		}
	}); 
}
*/

function updateImage(incDecValue) {
	//alert();
	loadingAnimationDiv.style.display = "inline";
	var baseURL = document.getElementById("baseURL").value;
	//var url = baseURL + "/gmap_controller.htm?action=getImage";
	//var url = baseURL + "report.gmap.get_image";
	var url = baseURL + "/gmapservlet?action=getImage&nextpage=report.gmap.get_image";
	var mapSize = map.getSize();
	var mapBounds = map.getBounds();
	var pointSW = mapBounds.getSouthWest();
	var pointNE = mapBounds.getNorthEast();
	url += "&zoom_level=" + map.getZoom();
	url += "&map_size=" + mapSize.width + "," + mapSize.height;
	url += "&map_bounds=" + pointSW.lng() + "," + pointSW.lat() + "," + pointNE.lng() + "," + pointNE.lat();
 	url += "&increment_decrement_value=" + incDecValue;
 	url += "&selected_layer_list=" + document.getElementById("selectedLayerList").value; 
 	url += "&color_list=" + document.getElementById("colorList").value;
 	var randomNumber = Math.floor(Math.random() * 5000000)
 	url += "&dummy=" + randomNumber;
 
  //disableUserInput();
  new Ajax.Request(url, {     
  	method: 'get',  	
  	onSuccess: function(transport) {  
  		
  	  loadingAnimationDiv.style.display = "none";
  		var jsonData = transport.responseText.evalJSON();
			var imageURL = jsonData.imageURL;
			var legendImageURL = jsonData.legendImageURL;
			var selectedImageURL = jsonData.selectedImageURL;
			var currentMonth = jsonData.currentMonth;
			var colorList = jsonData.colorList;
			var selectedLayerList = jsonData.selectedLayerList;
			
			if (legendImageURL != null) {
				repaintLegend(legendImageURL);
			}
			if (imageURL != null) {
				repaint(imageURL);
			}
			
			if (selectedImageURL != null) {
				repaintSelected(selectedImageURL);
			}
		
			if (currentMonth != null) {
			  document.getElementById("currentMonthDiv").innerHTML = currentMonth;
			}
			
			if (colorList != null) {
			  document.getElementById("colorList").value = colorList;
			  document.getElementById("selectedLayerList").value = selectedLayerList;
			  NovaButton.prototype.initializeLayerDiv(NovaButton.globals.layerOptionContainer);
			}
  		
  		
  	}
  }); 
}

	function repaintLegend(url) {
		var mapLegend = document.getElementById("mapLegend");
		var legendImg = document.getElementById("legendImg");
		
		mapLegend.removeChild(legendImg);
	    var newpic=document.createElement('img');
	    newpic.src=url;
	    newpic.id="legendImg";
	    //alert(url);
	    mapLegend.appendChild(newpic);
	      
	}


function repaint(url) {
  if (networkOverlay != null) {
  	map.removeOverlay(networkOverlay);
  }
  
  networkOverlay = new ProjectedOverlay(url, map.getBounds()) ;
  map.addOverlay(networkOverlay);
}

function repaintSelected(url) {
  var mapBounds = map.getBounds();
  var pointSW = mapBounds.getSouthWest();
  var pointNE = mapBounds.getNorthEast();
  
  if (selectedNetworkOverlay != null) {
  	map.removeOverlay(selectedNetworkOverlay);	
  }
  
  selectedNetworkOverlay = new GGroundOverlay(url, new GLatLngBounds(pointSW, pointNE)) ;
  map.addOverlay(selectedNetworkOverlay);
  
  if (flashTimeout != null) {
  	clearTimeout(flashTimeout);
  }
  
  flashImage();
}

function flashImage() {
	if (selectedNetworkOverlay.isHidden()) {
	  selectedNetworkOverlay.show();
	}
	else {
		selectedNetworkOverlay.hide();
	}
	
	flashTimeout = setTimeout(flashImage, 500);
}

function disableUserInput() {
	map.disableDragging();
	map.disableDoubleClickZoom();
	map.disableScrollWheelZoom();
	map.disableContinuousZoom();
}

function enableUserInput() {
	map.enableDragging();
	map.enableDoubleClickZoom();
	map.enableScrollWheelZoom();
	map.enableContinuousZoom();
}

String.prototype.trim = function() {
  return this.replace(/^\s+|\s+$/g, "");
}