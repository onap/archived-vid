function grayOut(vis, options) {
  // Pass true to gray out screen, false to ungray
  // options are optional.  This is a JSON object with the following (optional) properties
  // opacity:0-100         // Lower number = less grayout higher = more of a blackout 
  // zindex: #             // HTML elements with a higher zindex appear on top of the gray out
  // bgcolor: (#xxxxxx)    // Standard RGB Hex color code
  // grayOut(true, {'zindex':'50', 'bgcolor':'#0000FF', 'opacity':'70'});
  // Because options is JSON opacity/zindex/bgcolor are all optional and can appear
  // in any order.  Pass only the properties you need to set.
  var options = options || {}; 
  var zindex = options.zindex || 50;
  var opacity = options.opacity || 20;
  var opaque = (opacity / 100);
  var bgcolor = options.bgcolor || '#000000';
  var dark=document.getElementById('darkenScreenObject');
  //var tbody = document.getElementsByTagName("body")[0];
  var tbody = document.body;
  var myAjax = document.getElementById('AjaxLoading');
  
  if (!dark) {
    // The dark layer doesn't exist, it's never been created.  So we'll
    // create it here and apply some basic styles.
    // If you are getting errors in IE see: http://support.microsoft.com/default.aspx/kb/927917
    var tnode = document.createElement('div');           // Create the layer.
        tnode.style.position='absolute';                 // Position absolutely
        tnode.style.top='0px';                           // In the top
        tnode.style.left='0px';                          // Left corner of the page
        tnode.style.overflow='hidden';                   // Try to avoid making scroll bars            
        tnode.style.display='none';                      // Start out Hidden
        tnode.id='darkenScreenObject';                   // Name it so we can find it later
    tbody.appendChild(tnode);                            // Add it to the web page
    dark=document.getElementById('darkenScreenObject');  // Get the object.
  }
  if (vis) {
    // Calculate the page width and height 
    if( document.body && ( document.body.scrollWidth || document.body.scrollHeight ) ) {
        var pageWidth = document.body.scrollWidth+'px';
        var pageHeight = document.body.scrollHeight+'px';
    } else if( document.body.offsetWidth ) {
      var pageWidth = document.body.offsetWidth+'px';
      var pageHeight = document.body.offsetHeight+'px';
    } else {
       var pageWidth='100%';
       var pageHeight='100%';
    }   
    //set the shader to cover the entire page and make it visible.
    dark.style.opacity=opaque;                      
    dark.style.MozOpacity=opaque;                   
    dark.style.filter='alpha(opacity='+opacity+')'; 
    dark.style.zIndex=zindex;        
    dark.style.backgroundColor=bgcolor;  
    dark.style.width= pageWidth;
    dark.style.height= pageHeight;
    dark.style.display='block';
    //MyAjax
    
    /* Display the "Loading" box */
    var tnode = document.createElement('div'); // Create the layer.
    tnode.style.position='absolute'; // Position absolutely
    tnode.style.top='0px'; // In the top
    tnode.style.left='0px'; // Left corner of the page
    tnode.style.overflow='hidden'; // Try to avoid making scroll bars
    tnode.style.display='none'; // Start out Hidden
    tnode.id='AjaxLoading'; // Name it so we can find it later
    tbody.appendChild(tnode); // Add it to the web page
    myAjax=document.getElementById('AjaxLoading'); // Get the object.

    // Content
    // Reset the opacity options. Want this on top
    var loadingOptions = loadingOptions || {};
    var zindex = loadingOptions.zindex || 100;
    var opacity = loadingOptions.opacity || 100;
    var opaque = (opacity / 100);
    var bgcolor = loadingOptions.bgcolor || '#FFFFFF';

    myAjax.style.opacity=opaque;
    myAjax.style.MozOpacity=opaque;
    myAjax.style.filter='alpha(opacity='+opacity+')';
    myAjax.style.zIndex=zindex;

    myAjax.style.height= '40px';
    myAjax.style.padding = '3px';
    myAjax.style.fontSize = '14px';
    myAjax.style.width = '100px';
    myAjax.style.textAlign = 'center';
    myAjax.style.left = '45%';
    myAjax.style.top = '5%';
    myAjax.style.position = 'absolute';
    myAjax.style.border = '2px solid #666';
    myAjax.style.backgroundColor = bgcolor;

    myAjax.innerHTML='Loading <img src="static/fusion/raptor/images/progress.gif" id="wait" />';
    myAjax.style.display='block';
    
  } else {
     if(dark)   dark.style.display='none';
     if(myAjax) myAjax.style.display='none';
  }
}
function checkDate(dateStr, isOptional) {
	if(dateStr.charAt(0) == '[' && dateStr.charAt(dateStr.length-1) == ']')
		return true;
	if(dateStr=="")
		return isOptional;
	
	if((dateStr.length<6)||(dateStr.length>10))
		return false;
	
	if(dateStr.charAt(1)=='/') {
		dmonth  = parseInt(dateStr.substr(0,1));
		dateStr = dateStr.substr(2,dateStr.length-2);
	} else {
		//For some strange reason "08" is parsed as 0, so I remove leading 0-s
		if(dateStr.charAt(0)=='0')
			dmonth = parseInt(dateStr.substr(1,1));
		else
			dmonth = parseInt(dateStr.substr(0,2));
		dateStr = dateStr.substr(3,dateStr.length-3);
	}
	
	if(dateStr.charAt(1)=='/') {
		dday = parseInt(dateStr.substr(0,1));
		dateStr = dateStr.substr(2,dateStr.length-2);
	} else {
		//For some strange reason "08" is parsed as 0, so I remove leading 0-s
		if(dateStr.charAt(0)=='0')
			dday = parseInt(dateStr.substr(1,1));
		else
			dday = parseInt(dateStr.substr(0,2));
		dateStr = dateStr.substr(3,dateStr.length-3);
	}
	
	if(dateStr.length==2) {
		if (parseInt(dateStr)>50)
			dateStr="19"+dateStr;
		else
			dateStr="20"+dateStr;
	}
	dyear = parseInt(dateStr);
	//alert("day: "+dday+", month: "+dmonth+", year: "+dyear);
	if((dyear<1990)||(dyear>2090)||(dmonth<1)||(dmonth>12)||(dday<1)||
		(dday>((dmonth==2)?(((dyear % 4 == 0) && ((!(dyear % 100 == 0))||(dyear % 400 == 0)))?29:28):(((dmonth==1)||(dmonth==3)||(dmonth==5)||(dmonth==7)||(dmonth==8)||(dmonth==10)||(dmonth==12))?31:30))))
		return false;
		
	return true;
}   // checkDate

function validateNumber(numValue, mustBeInteger, mustBePositive) {
	var decimalPointFound = false;
	for(var i=0; i<numValue.length; i++) {
		var ch = numValue.charAt(i);
		
		if(ch=='0'||ch=='1'||ch=='2'||ch=='3'||ch=='4'||ch=='5'||ch=='6'||ch=='7'||ch=='8'||ch=='9') {
			// Valid character - do nothing
		} else if(ch=='-') {
			if(mustBePositive||i>0)
				return false;
		} else if(ch=='.') {
			if(mustBeInteger||decimalPointFound)
				return false;
			else
				decimalPointFound = true;
		} else
			// Invalid character
			return false;
	}	// for

	return true;
}	// validateNumber

function checkInteger(numValue) {
	return validateNumber(numValue, true, false);
}	// checkInteger

function checkNonNegativeInteger(numValue) {
	return validateNumber(numValue, true, true);
}	// checkNonNegativeInteger

function checkPositiveInteger(numValue) {
	if(! checkNonNegativeInteger(numValue))
		return false;
		
	if(parseInt(numValue)==0)
		return false;
	
	return true;	
}	// checkPositiveInteger

function checkFloat(numValue) {
	return validateNumber(numValue, false, false);
}	// checkFloat

function checkNonNegativeFloat(numValue) {
	return validateNumber(numValue, false, true);
}	// checkNonNegativeFloat

function checkPositiveFloat(numValue) {
	if(! checkNonNegativeFloat(numValue))
		return false;

	if(parseFloat(numValue)<0.001&&parseFloat(numValue)>-0.001)
		return false;
	
	return true;	
}	// checkPositiveFloat

function getDocHeight(doc) {
var docHt = 0, sh, oh;
if (doc.height) docHt = doc.height;
else if (doc.body) {
if (doc.body.scrollHeight) docHt = sh = doc.body.scrollHeight;
if (doc.body.offsetHeight) docHt = oh = doc.body.offsetHeight;
if (sh && oh) docHt = Math.max(sh, oh);
}
return docHt;
}

function getDocWidth(doc) 
{ 
	var docWd = 0, sh, oh; 
	if (doc.width) 
		docWd = doc.width; 
	else if (doc.body) 
	{ 
		if (doc.body.scrollWidth) 
			docWd = sh = doc.body.scrollWidth; 
		if (doc.body.offsetWidth) 
			docWd = oh = doc.body.offsetWidth; 
		if (sh && oh) 
			docWd = Math.max(sh, oh); 
	} 
	return docWd; 
} 
 

function setIframeHeight(iframeName) 
{ 
	var oldY = document.body.scrollTop;
	var iframeWin = window.frames[iframeName]; 
	var iframeEl = document.getElementById? document.getElementById(iframeName): document.all? document.all[iframeName]: null; 
	if ( iframeEl && iframeWin ) { 
		iframeEl.style.height = "auto"; 
		// helps resize (for some) if new doc shorter than previous 
		var docHt = getDocHeight(iframeWin.document); 
		var docWd = getDocWidth(iframeWin.document); 
		// need to add to height to be sure it will all show 
		if (docHt) 
			iframeEl.style.height = docHt + 1 + "px"; 
		if (docWd) 
			iframeEl.style.width = docWd + 1 + "px"; 
	} 
	if (oldY != null && oldY != 'undefined')
		document.body.scrollTop = oldY;
}

function resizeFrames(){
	var isFolderAllowed = arguments[0];
	if (isFolderAllowed == null || isFolderAllowed == 'true'){
		if (document.getElementById('scrollableTable') && document.body.offsetHeight > 195)
			document.getElementById('scrollableTable').style.height=document.body.offsetHeight - 185;
		if (document.getElementById('contentDiv') && document.body.offsetHeight > 145)
			document.getElementById('contentDiv').style.height=document.body.offsetHeight - 135;
		if (document.getElementById('content_Iframe') && document.body.offsetHeight > 145)
			document.getElementById('content_Iframe').height=document.body.offsetHeight - 135;			
		
	}else{
		setIframeHeight('content_Iframe');
	}
}

function resizeWindow()	{
	var isFolderAllowed = arguments[0];
	if (isFolderAllowed != null)
		resizeFrames(isFolderAllowed);
	else
		resizeFrames(false);	
}

/**
* Usage : var myObje = $('theIdOfTheElementYouWantToGet');
* Simple gives you the element. Avoids using document.getElementById();
*/
	var $;
	if (!$ && document.getElementById) {
		$ = function() {
			var elements = new Array();
			for (var i = 0; i < arguments.length; i++) {
				var element = arguments[i];
				if (typeof element == 'string') {
					element = document.getElementById(element);
				}
				if (arguments.length == 1) {
					return element;
				}
				elements.push(element);
			}
			return elements;
		}
	} else if (!$ && document.all) {
		$ = function() {
			var elements = new Array();
			for (var i = 0; i < arguments.length; i++) {
				var element = arguments[i];
				if (typeof element == 'string') {
					element = document.all[element];
				}
				if (arguments.length == 1) {
					return element;
				}
				elements.push(element);
			}
			return elements;
		}
	}
