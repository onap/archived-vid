<%--
  ================================================================================
  eCOMP Portal SDK
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<!DOCTYPE html>

<html>
<head>
    
    <meta charset="UTF-8">

    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />

    <style>
        #map { 
            height: 450px;
            width: 800px;
        }
        .animateOff {
            color: red;   
        }
        .animateOn {
            color: green;   
        }
    </style>

    <script>

    var siteData = [
		{"id": "cb1ma", "name": "Cambridge, MA", "title": "100G", "lat": 42.3736, "lon": -71.11},
		{"id": "st6wa", "name": "Seattle, WA", "title": "40G", "lat": 47.6097, "lon": -122.33},
		{"id": "cgcil", "name": "Chicago, IL", "title": "310G", "lat": 41.8819, "lon": -87.627},
		{"id": "n54ny", "name": "New York, NY", "title": "160G", "lat": 40.7127, "lon": -74.005},
		{"id": "cl2oh", "name": "Columbus, OH", "title": "300G", "lat": 39.9833, "lon": -82.983},
		{"id": "phlpa", "name": "Philadelphia, PA", "title": "210G", "lat": 39.95, "lon": -75.166},
		{"id": "ptdor", "name": "Portland, OR", "title": "90G", "lat": 45.52, "lon": -122.68},
		{"id": "dvmco", "name": "Denver, CO", "title": "170G", "lat": 39.7391, "lon": -104.98},
		{"id": "kc9mo", "name": "Kansas City, MO", "title": "100G", "lat": 39.0997, "lon": -94.578},
		{"id": "sffca", "name": "San Fransisco, CA", "title": "130G", "lat": 37.7833, "lon": -122.41},
		{"id": "sl9mo", "name": "St Louis, MO", "title": "330G", "lat": 38.6272, "lon": -90.197},
		{"id": "wswdc", "name": "Washington, DC", "title": "210G", "lat": 38.8951, "lon": -77.036},
		{"id": "sc1ca", "name": "Sacramento, CA", "title": "250G", "lat": 38.5555, "lon": -121.46},
		{"id": "slkut", "name": "Salt Lake City, UT", "title": "250G", "lat": 40.75, "lon": -111.88},
		{"id": "rlgnc", "name": "Raleigh, NC", "title": "180G", "lat": 35.8188, "lon": -78.644},
		{"id": "nsvtn", "name": "Nashville, TN", "title": "210G", "lat": 36.1666, "lon": -86.783},
		{"id": "la2ca", "name": "Los Angeles, CA", "title": "180G", "lat": 34.05, "lon": -118.25},
		{"id": "dlstx", "name": "Dallas, TX", "title": "330G", "lat": 32.7758, "lon": -96.796},
		{"id": "attga", "name": "Atlanta, GA", "title": "280G", "lat": 33.755, "lon": -84.39},
		{"id": "nwrla", "name": "New Orleans, LA", "title": "180G", "lat": 29.9647, "lon": -90.07},
		{"id": "sd2ca", "name": "San Diego, CA", "title": "150G", "lat": 32.715, "lon": -117.16},
		{"id": "phmaz", "name": "Phoenix, AZ", "title": "210G", "lat": 33.45, "lon": -112.06},
		{"id": "santx", "name": "San Antonio, TX", "title": "220G", "lat": 29.4166, "lon": -98.5},
		{"id": "hs1tx", "name": "Houston, TX", "title": "290G", "lat": 29.7627, "lon": -95.383},
		{"id": "ormfl", "name": "Orlando, FL", "title": "130G", "lat": 28.4158, "lon": -81.298},
	];
    
    var pipeData = [
        {"id_a": "st6wa", "id_z": "ptdor", "name": "Seattle, WA to Portland, OR", "title": "60G", "lat_a": 47.6097, "lon_a": -122.33, "lat_z": 45.52, "lon_z": -122.68},
    	{"id_a": "sc1ca", "id_z": "slkut", "name": "Sacramento, CA to Salt Lake City, UT", "title": "200G", "lat_a": 38.5555, "lon_a": -121.46, "lat_z": 40.75, "lon_z": -111.88},
    	{"id_a": "hs1tx", "id_z": "ormfl", "name": "Houston, TX to Orlando, FL", "title": "130G", "lat_a": 29.7627, "lon_a": -95.383, "lat_z": 28.4158, "lon_z": -81.298},
    	{"id_a": "nwrla", "id_z": "ormfl", "name": "New Orleans, LA to Orlando, FL", "title": "90G", "lat_a": 29.9647, "lon_a": -90.07, "lat_z": 28.4158, "lon_z": -81.298},
    	{"id_a": "n54ny", "id_z": "phlpa", "name": "New York, NY to Philadelphia, PA", "title": "240G", "lat_a": 40.7127, "lon_a": -74.005, "lat_z": 39.95, "lon_z": -75.166},
    	{"id_a": "n54ny", "id_z": "wswdc", "name": "New York, NY to Washington, DC", "title": "380G", "lat_a": 40.7127, "lon_a": -74.005, "lat_z": 38.8951, "lon_z": -77.036},
    	{"id_a": "rlgnc", "id_z": "attga", "name": "Raleigh, NC to Atlanta, GA", "title": "160G", "lat_a": 35.8188, "lon_a": -78.644, "lat_z": 33.755, "lon_z": -84.39},
//    	{"id_a": "st6wa", "id_z": "sffca", "name": "Seattle, WA to San Fransisco, CA", "title": "40G", "lat_a": 47.6097, "lon_a": -122.33, "lat_z": 37.7833, "lon_z": -122.41},
    	{"id_a": "la2ca", "id_z": "sd2ca", "name": "Los Angeles, CA to San Diego, CA", "title": "160G", "lat_a": 34.05, "lon_a": -118.25, "lat_z": 32.715, "lon_z": -117.16},
    	{"id_a": "sd2ca", "id_z": "phmaz", "name": "San Diego, CA to Phoenix, AZ", "title": "200G", "lat_a": 32.715, "lon_a": -117.16, "lat_z": 33.45, "lon_z": -112.06},
    	{"id_a": "ptdor", "id_z": "sffca", "name": "Portland, OR to San Fransisco, CA", "title": "70G", "lat_a": 45.52, "lon_a": -122.68, "lat_z": 37.7833, "lon_z": -122.41},
    	{"id_a": "sffca", "id_z": "sc1ca", "name": "San Fransisco, CA to Sacramento, CA", "title": "80G", "lat_a": 37.7833, "lon_a": -122.41, "lat_z": 38.5555, "lon_z": -121.46},
    	{"id_a": "la2ca", "id_z": "slkut", "name": "Los Angeles, CA to Salt Lake City, UT", "title": "200G", "lat_a": 34.05, "lon_a": -118.25, "lat_z": 40.75, "lon_z": -111.88},
    	{"id_a": "cgcil", "id_z": "cl2oh", "name": "Chicago, IL to Columbus, OH", "title": "200G", "lat_a": 41.8819, "lon_a": -87.627, "lat_z": 39.9833, "lon_z": -82.983},
    	{"id_a": "cl2oh", "id_z": "phlpa", "name": "Columbus, OH to Philadelphia, PA", "title": "240G", "lat_a": 39.9833, "lon_a": -82.983, "lat_z": 39.95, "lon_z": -75.166},
    	{"id_a": "phlpa", "id_z": "wswdc", "name": "Philadelphia, PA to Washington, DC", "title": "240G", "lat_a": 39.95, "lon_a": -75.166, "lat_z": 38.8951, "lon_z": -77.036},
    	{"id_a": "sffca", "id_z": "la2ca", "name": "San Fransisco, CA to Los Angeles, CA", "title": "280G", "lat_a": 37.7833, "lon_a": -122.41, "lat_z": 34.05, "lon_z": -118.25},
    	{"id_a": "dvmco", "id_z": "cgcil", "name": "Denver, CO to Chicago, IL", "title": "200G", "lat_a": 39.7391, "lon_a": -104.98, "lat_z": 41.8819, "lon_z": -87.627},
    	{"id_a": "dlstx", "id_z": "hs1tx", "name": "Dallas, TX to Houston, TX", "title": "360G", "lat_a": 32.7758, "lon_a": -96.796, "lat_z": 29.7627, "lon_z": -95.383},
    	{"id_a": "nsvtn", "id_z": "cl2oh", "name": "Nashville, TN to Columbus, OH", "title": "200G", "lat_a": 36.1666, "lon_a": -86.783, "lat_z": 39.9833, "lon_z": -82.983},
    	{"id_a": "cb1ma", "id_z": "phlpa", "name": "Cambridge, MA to Philadelphia, PA", "title": "110G", "lat_a": 42.3736, "lon_a": -71.11, "lat_z": 39.95, "lon_z": -75.166},
//    	{"id_a": "sffca", "id_z": "cgcil", "name": "San Fransisco, CA to Chicago, IL", "title": "170G", "lat_a": 37.7833, "lon_a": -122.41, "lat_z": 41.8819, "lon_z": -87.627},
    	{"id_a": "sffca", "id_z": "dvmco", "name": "San Fransisco, CA to Denver, CO", "title": "90G", "lat_a": 37.7833, "lon_a": -122.41, "lat_z": 39.7391, "lon_z": -104.98},
    	{"id_a": "sffca", "id_z": "sl9mo", "name": "San Fransisco, CA to St Louis, MO", "title": "80G", "lat_a": 37.7833, "lon_a": -122.41, "lat_z": 38.6272, "lon_z": -90.197},
    	{"id_a": "santx", "id_z": "dlstx", "name": "San Antonio, TX to Dallas, TX", "title": "180G", "lat_a": 29.4166, "lon_a": -98.5, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "santx", "id_z": "hs1tx", "name": "San Antonio, TX to Houston, TX", "title": "240G", "lat_a": 29.4166, "lon_a": -98.5, "lat_z": 29.7627, "lon_z": -95.383},
    	{"id_a": "sl9mo", "id_z": "wswdc", "name": "St Louis, MO to Washington, DC", "title": "280G", "lat_a": 38.6272, "lon_a": -90.197, "lat_z": 38.8951, "lon_z": -77.036},
    	{"id_a": "nwrla", "id_z": "attga", "name": "New Orleans, LA to Atlanta, GA", "title": "200G", "lat_a": 29.9647, "lon_a": -90.07, "lat_z": 33.755, "lon_z": -84.39},
    	{"id_a": "la2ca", "id_z": "dlstx", "name": "Los Angeles, CA to Dallas, TX", "title": "280G", "lat_a": 34.05, "lon_a": -118.25, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "slkut", "id_z": "dvmco", "name": "Salt Lake City, UT to Denver, CO", "title": "200G", "lat_a": 40.75, "lon_a": -111.88, "lat_z": 39.7391, "lon_z": -104.98},
    	{"id_a": "dvmco", "id_z": "dlstx", "name": "Denver, CO to Dallas, TX", "title": "200G", "lat_a": 39.7391, "lon_a": -104.98, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "kc9mo", "id_z": "sl9mo", "name": "Kansas City, MO to St Louis, MO", "title": "280G", "lat_a": 39.0997, "lon_a": -94.578, "lat_z": 38.6272, "lon_z": -90.197},
    	{"id_a": "kc9mo", "id_z": "dlstx", "name": "Kansas City, MO to Dallas, TX", "title": "280G", "lat_a": 39.0997, "lon_a": -94.578, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "cgcil", "id_z": "wswdc", "name": "Chicago, IL to Washington, DC", "title": "200G", "lat_a": 41.8819, "lon_a": -87.627, "lat_z": 38.8951, "lon_z": -77.036},
    	{"id_a": "cgcil", "id_z": "sl9mo", "name": "Chicago, IL to St Louis, MO", "title": "370G", "lat_a": 41.8819, "lon_a": -87.627, "lat_z": 38.6272, "lon_z": -90.197},
    	{"id_a": "n54ny", "id_z": "cb1ma", "name": "New York, NY to Cambridge, MA", "title": "80G", "lat_a": 40.7127, "lon_a": -74.005, "lat_z": 42.3736, "lon_z": -71.11},
    	{"id_a": "st6wa", "id_z": "dvmco", "name": "Seattle, WA to Denver, CO", "title": "40G", "lat_a": 47.6097, "lon_a": -122.33, "lat_z": 39.7391, "lon_z": -104.98},
    	{"id_a": "la2ca", "id_z": "phmaz", "name": "Los Angeles, CA to Phoenix, AZ", "title": "260G", "lat_a": 34.05, "lon_a": -118.25, "lat_z": 33.45, "lon_z": -112.06},
    	{"id_a": "phmaz", "id_z": "santx", "name": "Phoenix, AZ to San Antonio, TX", "title": "160G", "lat_a": 33.45, "lon_a": -112.06, "lat_z": 29.4166, "lon_z": -98.5},
    	{"id_a": "sl9mo", "id_z": "dlstx", "name": "St Louis, MO to Dallas, TX", "title": "200G", "lat_a": 38.6272, "lon_a": -90.197, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "dlstx", "id_z": "nsvtn", "name": "Dallas, TX to Nashville, TN", "title": "160G", "lat_a": 32.7758, "lon_a": -96.796, "lat_z": 36.1666, "lon_z": -86.783},
    	{"id_a": "wswdc", "id_z": "attga", "name": "Washington, DC to Atlanta, GA", "title": "380G", "lat_a": 38.8951, "lon_a": -77.036, "lat_z": 33.755, "lon_z": -84.39},
    	{"id_a": "st6wa", "id_z": "cgcil", "name": "Seattle, WA to Chicago, IL", "title": "70G", "lat_a": 47.6097, "lon_a": -122.33, "lat_z": 41.8819, "lon_z": -87.627},
    	{"id_a": "dvmco", "id_z": "kc9mo", "name": "Denver, CO to Kansas City, MO", "title": "100G", "lat_a": 39.7391, "lon_a": -104.98, "lat_z": 39.0997, "lon_z": -94.578},
    	{"id_a": "phmaz", "id_z": "dlstx", "name": "Phoenix, AZ to Dallas, TX", "title": "210G", "lat_a": 33.45, "lon_a": -112.06, "lat_z": 32.7758, "lon_z": -96.796},
    	{"id_a": "cgcil", "id_z": "n54ny", "name": "Chicago, IL to New York, NY", "title": "280G", "lat_a": 41.8819, "lon_a": -87.627, "lat_z": 40.7127, "lon_z": -74.005},
    	{"id_a": "sl9mo", "id_z": "nsvtn", "name": "St Louis, MO to Nashville, TN", "title": "170G", "lat_a": 38.6272, "lon_a": -90.197, "lat_z": 36.1666, "lon_z": -86.783},
    	{"id_a": "dlstx", "id_z": "attga", "name": "Dallas, TX to Atlanta, GA", "title": "240G", "lat_a": 32.7758, "lon_a": -96.796, "lat_z": 33.755, "lon_z": -84.39},
    	{"id_a": "hs1tx", "id_z": "nwrla", "name": "Houston, TX to New Orleans, LA", "title": "170G", "lat_a": 29.7627, "lon_a": -95.383, "lat_z": 29.9647, "lon_z": -90.07},
    	{"id_a": "ormfl", "id_z": "attga", "name": "Orlando, FL to Atlanta, GA", "title": "210G", "lat_a": 28.4158, "lon_a": -81.298, "lat_z": 33.755, "lon_z": -84.39},
    	{"id_a": "nsvtn", "id_z": "attga", "name": "Nashville, TN to Atlanta, GA", "title": "240G", "lat_a": 36.1666, "lon_a": -86.783, "lat_z": 33.755, "lon_z": -84.39},
    	{"id_a": "wswdc", "id_z": "rlgnc", "name": "Washington, DC to Raleigh, NC", "title": "200G", "lat_a": 38.8951, "lon_a": -77.036, "lat_z": 35.8188, "lon_z": -78.644}
    ];
    </script>
   
    
</head>
<body>
  <div id="map"></div>
  <button id="forwardButton" onclick="stepForward();">Step Forward</button>
  <button id="playPause" onclick="playPause();">Play</button>
  <div>
    <table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">
      <thead att-table-row type="header">
        <tr>
          <th att-table-header key="id">Site</th>
          <th att-table-header key="lastName">Usage</th>        
        </tr>
      </thead>
      <tbody att-table-row type="body" id="topTenSites">
      </tbody>	  
    </table>
    <table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">
      <thead att-table-row type="header">
        <tr>
          <th att-table-header key="id">Link</th>
          <th att-table-header key="lastName">Usage</th>        
        </tr>
      </thead>
      <tbody att-table-row type="body" id="topTenLinks">
      </tbody>	  
    </table>
  </div>

  <script>
    var map = L.map('map').setView([40, -96], 4);
    L.tileLayer('', {maxZoom:18}).addTo(map); //TODO configure 
    
    var dataLayer = addDataLayers(map, null);

    function addDataLayers(map, dataLayer) {
    	if (dataLayer!=null) {
    		map.removeLayer(dataLayer);
    	}
    	
    	dataLayer = L.layerGroup();

    	var siteInfo = [];

        var pipeLayer = L.layerGroup();
        for (var i=0; i<pipeData.length; i++) {
        	var pipe = pipeData[i];
        	
        	var usage = pipe.usage;
        	if (!usage || Math.random()<0.05) {
        		//console.log("Rerolling " + pipe.name);
	        	usage = Math.floor(Math.random()*33 + Math.random()*33 + Math.random()*34);
        	} else {
        		//console.log("Adjusting " + pipe.name);
        		usage = Math.floor(usage + Math.random()*15 - Math.random()*15);
        	}
        	if (usage<0) usage = 0;
        	while (usage>100) usage -= Math.floor(20*Math.random());
        	if (usage>90) usage -= Math.floor(20*Math.random());
        	
        	pipe.usage = usage;

        	var color = "black";
        	if (usage>60) color = "yellow";
        	if (usage>70) color = "orange";
        	if (usage>80) color = "red";
        	pipeLayer.addLayer(L.polyline([[pipe.lat_a, pipe.lon_a], [pipe.lat_z, pipe.lon_z]], {"color": color, "title": pipe.name}).bindPopup(pipe.name + "<br/>" + pipe.title + "<br/>" + usage + "% usage"));

        	var siteA = siteInfo[pipe.id_a];
        	if (siteA) {
        		siteA.usage += usage;
        		siteA.maxUsage += 100;
        		//console.log("Site a id = " + pipe.id_a + ", object existed = " + siteA + ", usage = " + siteA.usage + ", max = " + siteA.maxUsage);
        	} else {
        		siteA = {};
        		siteA.usage = usage;
        		siteA.maxUsage = 100;
        		siteInfo[pipe.id_a] = siteA;
        		//console.log("Site a id = " + pipe.id_a + ", object is new = " + siteA + ", usage = " + siteA.usage + ", max = " + siteA.maxUsage);
        	}

        	var siteZ = siteInfo[pipe.id_z];
        	if (siteZ) {
        		siteZ.usage += usage;
        		siteZ.maxUsage += 100;
        		//console.log("Site z id = " + pipe.id_z + ", object existed = " + siteZ + ", usage = " + siteZ.usage + ", max = " + siteZ.maxUsage);
        	} else {
        		siteZ = {};
        		siteZ.usage = usage;
        		siteZ.maxUsage = 100;
        		siteInfo[pipe.id_z] = siteZ;
        		//console.log("Site z id = " + pipe.id_z + ", object is new = " + siteZ + ", usage = " + siteZ.usage + ", max = " + siteZ.maxUsage);
        	}
        }
        dataLayer.addLayer(pipeLayer);

        var dataCenterLayer = L.layerGroup();
        for (var i=0; i<siteData.length; i++) {
        	var site = siteData[i];
//    		{"id": "slkut", "name": "Salt Lake City, UT", "title": "250G", "lat": 40.75, "lon": -111.88},
        	var info = siteInfo[site.id];
        	var color = "black";
        	if (info.usage/info.maxUsage>.6) color = "yellow";
        	if (info.usage/info.maxUsage>.7) color = "orange";
        	if (info.usage/info.maxUsage>.8) color = "red";
        	var pct = Math.floor(100*info.usage/info.maxUsage);
        	site.usage = pct;
//        	dataCenterLayer.addLayer(L.marker([site.lat, site.lon], {"title": site.name}).bindPopup(site.name + "<br/>" + pipe.title + "<br/>" + info.usage + "/" + info.maxUsage));
			dataCenterLayer.addLayer(L.circleMarker([site.lat, site.lon], {"color": color, "title": site.name, "fillOpacity": .5}).bindPopup(site.name + "<br/>" + pipe.title + "<br/>" + pct + "%"));
        }
        dataLayer.addLayer(dataCenterLayer);

        dataLayer.addTo(map);

        siteData.sort(function(a,b){return b.usage-a.usage});
        var topTenHtml = "";
        for (var i=0; i<10; i++) {
        	topTenHtml = topTenHtml + "<tr><td att-table-body>" + siteData[i].name + "</td><td att-table-body>" + siteData[i].usage + "%</td></tr>";
        }
        document.getElementById("topTenSites").innerHTML = topTenHtml;

        pipeData.sort(function(a,b){return b.usage-a.usage});
        topTenHtml = "";
        for (var i=0; i<10; i++) {
        	topTenHtml = topTenHtml + "<tr><td att-table-body>" + pipeData[i].name + "</td><td att-table-body>" + pipeData[i].usage + "%</td></tr>";
        }
        document.getElementById("topTenLinks").innerHTML = topTenHtml;

        return dataLayer; 
    }
    
    function stepForward() {
    	dataLayer = addDataLayers(map, dataLayer);
    }

    var intervalObj = null;    
    function playPause() {
    	if (intervalObj==null) {
    		document.getElementById('playPause').innerHTML = "Pause";
    		document.getElementById('forwardButton').disabled = true;
    		intervalObj = window.setInterval(function(){dataLayer = addDataLayers(map, dataLayer);},1500);
    	} else {
    		document.getElementById('playPause').innerHTML = "Play";
    		document.getElementById('forwardButton').disabled = false;
    		clearInterval(intervalObj);
    		intervalObj = null;
    	}
    }
    
    function onMapClick(e) {
        //alert("You clicked the map at " + e.latlng);
        dataLayer = addDataLayers(map, dataLayer);
    }

    map.on('click', onMapClick);
  </script>

</body>
</html>
