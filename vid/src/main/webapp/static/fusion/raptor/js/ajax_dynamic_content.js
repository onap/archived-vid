
/***********************************************
* Dynamic Ajax Content- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

var loadedobjects=""
var rootdomain="http://"+window.location.hostname

function ajaxpage(url, containerid){

var page_request = false;
   try {
    netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
   } catch (e) {
    //alert("Permission UniversalBrowserRead denied.");
   }
if (window.XMLHttpRequest) // if Mozilla, Safari etc
page_request = new XMLHttpRequest()
else if (window.ActiveXObject){ // if IE
try {
page_request = new ActiveXObject("Msxml2.XMLHTTP")
} 
catch (e1){
try{
page_request = new ActiveXObject("Microsoft.XMLHTTP")
}
catch (e1){ page_request = null; alert('permission denied');
}
}
}
else
return false
page_request.onreadystatechange=function(){
loadpage(page_request, containerid)
}
// This is a fix made since IE doesn't refresh the page 
var ajaxRightNow = new Date();
var noCacheAjaxurl = url + ((/\?/.test(url)) ? "&" : "?") + "ajaxRandomTimestamp=" + ajaxRightNow.getTime(); 
page_request.open('GET', noCacheAjaxurl, true)
page_request.send(null)
}

function loadpage(page_request, containerid){
var div = document.getElementById(containerid);
if (page_request.readyState == 4 && (page_request.status==200 || window.location.href.indexOf("http")==-1))
    div.innerHTML=page_request.responseText;
    var x = div.getElementsByTagName("script");   
    for(var i=0;i<x.length;i++)  
    {
       if(x[i].text.indexOf("resizeDivScrollbar")>=0) 
        eval(x[i].text);  
    }
}

function resizeDivScrollbar(){  
	var frame = document.getElementById("scrollableTableResult");  
	var parentBody = window.parent.document.body;
	var parentMenu = window.parent.document.getElementById("application");
	if(frame!=null) {
		//alert(frame.clientHeight + " " + window.parent.document.body.clientHeight);
		if (frame.clientHeight > window.parent.document.body.clientHeight) {
				frame.style.height = window.parent.document.body.clientHeight-350;
		} else
				frame.style.height = window.parent.document.body.clientHeight;
		parentMenu.style.width = frame.clientWidth+200;
	}			
} 


function loadobjs(){
if (!document.getElementById)
return
for (i=0; i<arguments.length; i++){
var file=arguments[i]
var fileref=""
if (loadedobjects.indexOf(file)==-1){ //Check to see if this object has not already been added to page before proceeding
if (file.indexOf(".js")!=-1){ //If object is a js file
fileref=document.createElement('script')
fileref.setAttribute("type","text/javascript");
fileref.setAttribute("src", file);
}
else if (file.indexOf(".css")!=-1){ //If object is a css file
fileref=document.createElement("link")
fileref.setAttribute("rel", "stylesheet");
fileref.setAttribute("type", "text/css");
fileref.setAttribute("href", file);
}
}
if (fileref!=""){
document.getElementsByTagName("head").item(0).appendChild(fileref)
loadedobjects+=file+" " //Remember this object as being already added to page
}
}
}

