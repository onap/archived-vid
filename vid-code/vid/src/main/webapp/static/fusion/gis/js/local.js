var pointLayer= null;
var animateFlag = false;
var frame = 0;
var dates = [];
var delay = 1000;
var intervalId = null;
$( document ).ready(function() {
    map(data2.map.lat,data2.map.lon,data2.map.zoom);
    pointLayer = new L.layerGroup().addTo(map);
    var point = getDates(data2.points);
    drawPoints(data2,point);
    
    
    $('#timeUp').bind('click',function(){
        delay += 500; 
        if ( animateFlag ) {
           clearInterval(intervalId);
           intervalId = window.setInterval(function(){animate(data2);},delay);
        }
    });
    $('#timeDown').bind('click',function(){
        if ( delay === 500 ) { return false; }
        delay -= 500; 
        if ( animateFlag ) {
            clearInterval(intervalId);
           intervalId = window.setInterval(function(){animate(data2);},delay);
        }
    });
    
    
    $('#date').bind('change',function(){
        drawPoints(data2,$('#date').val());
    });
    $('#animate').bind('click',function(){
        if ( animateFlag ) {
            animateFlag = false; 
            $('#animate_color').html('Off');
            $('#animate_color').removeClass('animateOn');
            $('#animate_color').addClass('animateOff');
            clearInterval(intervalId);
        } else {
            animateFlag = true;
            $('#animate_color').html('On');
            $('#animate_color').removeClass('animateOff');
            $('#animate_color').addClass('animateOn');
            intervalId = window.setInterval(function(){animate(data2);},delay);
        }
    });

    
});

function abc() {
	var abc = 1;
	var xyz = "";
	return nul;
	
}



function animate(obj){
    var point =  dates[frame];
    drawPoints(obj,point);
    frame++;
    if ( frame >= dates.length ) {
        frame = 0;
    }
    $('#currDate').val(point);
    $('#showDelay').html(delay);
}

function someFunction() {
	
	var self = this;
	var x = 1;
	var z = self + x;
}


/**
* drawPoints - Draw the points provided by point(date)     
* obj JSON Object - Data for Map
* p String - Key (date) from the JSON object. 
*/
function drawPoints(obj,p){
    pointLayer.clearLayers();
    var lats = obj.points[p].lats;
    var lons = obj.points[p].lons;
    var txt = obj.points[p].text;
    var fills = obj.points[p].colors;
    points(lats, lons,txt,fills);
}

/**
* Get dates from the list provided in pIn and put them in Select.
* then return the fist date.
*
*/
function getDates(pIn) {
    var html = "<option value='na'>--Select--</option>";
    var c = 0;
    var ret = 0;
    for (var d in pIn ) {
        var val = d.replace('"','');
        if ( c === 0 ) { ret = val;};
        html += "<option value='" + d + "'>" + d + "</option>";   
        dates[c] = val;
        c++;
    }
    $('#date').html(html);
    return ret;
}

/**
* Map - pass argumets to build map as specified Lat/Lon and zoom level
* String lat - Latitude
* String lon - Longitude 
* String zoom - Zoom Level (1-18)
*
*/
function map(lat,lon,zoom){ 
    var map = L.map('map').setView([lat,lon],zoom);
    map.addControl(new L.control.scale());
    L.tileLayer('http://gis.openecomp.org/tiles-light/{z}/{x}/{y}.png', {maxZoom:18}).addTo(map);
    window.L=L;
    window.map=map;
}

/**
* points - pass argumets to points specified Lat/Lon. Text and color for point is also passed. 
* Input is either all singe values or all arrays. 
* String[] lat - Latitude
* String[] lon - Longitude 
* String[] text - Single/Array of Text for points
* String[] fill - Single/Array of Fill colors.
*/
function points(lat,lon,text,fill){
    if(lat.length) {
        for(i=0;i<lat.length;i++) {
            pointLayer.addLayer(L.circleMarker([lat[i], lon[i]], { 
                radius:"5", 
                color: fill[i], 
                fillColor: fill[i], 
                fillOpacity: "0.5" }).bindPopup(text[i]));

            /*
            L.circleMarker([lat[i], lon[i]], { 
                radius:"5", 
                color: fill[i], 
                fillColor: fill[i], 
                fillOpacity: "0.5" }).addTo(map).bindPopup(text[i]);

            */
        
        }
        
    } else {
        L.circleMarker([lat, lon], { 
            radius:"5", 
            color:fill, 
            fillColor:fill, 
            fillOpacity: "0.5" 
        }).addTo(map).bindPopup(text);
    }
}

/**
*  Get data from json file and creat return an object of data. 
* Returns json object. 
*
**/
function getData(){
    $.getJSON( "./data.json", function( data ) {
        return data;
    });
    
}
