//Reusable Asset Library - CSS Positioning Version (v3.3)
//Release Date: 1/26/07


//begin: quick help =======================================
var uitmpl_qhId;
var uitmpl_cookieVal;
var qhRule;
var qhCookie;

//sets cookie value and qhId
qhCookie=document.cookie;
var pos=qhCookie.indexOf("qhId=");

if(pos==-1){
document.cookie="qhId=0";
uitmpl_qhId="0";
qhCookie=document.cookie;
pos=qhCookie.indexOf("qhId=");
}
//reads cookie if value is present and sets qhId accordingly
var start=pos+5;
var end=qhCookie.indexOf(";", start);
if (end==-1) end=qhCookie.length;
uitmpl_cookieVal=qhCookie.substring(start,end);
if(uitmpl_cookieVal!=0){
	uitmpl_qhId=1;
}else{
uitmpl_qhId=0;
}


//loops through all qh divs and toggles display based on qhId
function uitmpl_qhPageInit(){
var getTag = document.getElementsByTagName("div");
	for (i=0;i<getTag.length;i++){
		if (getTag[i].className=="mQH"){
			getTag[i].style.display=(uitmpl_qhId!="0")?"block":"none";
			//getTag[i].onmouseover="alert('foo')";
		}
	}
	//changes instruction in help menu if menu exists
	if (uitmpl_qhId==1&&document.getElementById("shHd")){
	document.getElementById("shHd").innerHTML = "Hide";
	}
}

function uitmpl_qhPageToggle(){
//change instruction in help menu
var getCurrent = document.getElementById("shHd").innerHTML;
var setCurrent = (getCurrent=="Show")?"Hide":"Show";
document.getElementById("shHd").innerHTML = setCurrent;
//change cookie value
document.cookie = (setCurrent=="Show")?"qhId=0":"qhId=1";
//change qhId value
uitmpl_qhId = (setCurrent=="Show")?"0":"1";
//rerun page init
uitmpl_qhPageInit();
}

//individual quick help toggle
function uitmpl_qh(obj){
	var state = document.getElementById(obj).style.display;
	document.getElementById(obj).style.display=(state!="block")?"block":"none";
	
}
//end: quick help =======================================

//begin: error validation =======================================
//string validation
function uitmpl_errStrVal(obj, errId){
	var x=obj.value;
	var regExp = {
	number:/(^\d+$)|(^\d+\.\d+$)/,
	email:/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
	//phone:/(^\d+$)|(^\d+\.\d+$)/,
	//time:/(^\d+$)|(^\d+\.\d+$)/
	};

	var alrtStr = {
	number:"Please input a valid number!",
	email:"Please input a valid email address!",
	date:"That date is invalid.  Please try again."
	//phone:"Please input a valid phone number!",
	//time:"Please input a valid time!"
	};

	var getReg = eval("regExp."+errId);
	var getAlrt = eval("alrtStr."+errId);

	//Number----------------------------------------------------------
	if (errId=="number"&&!x.match(getReg)){
		alert(getAlrt);
		obj.focus();
		obj.select();
	}
	//Email-----------------------------------------------------------
	else if (errId=="email"&&!x.match(getReg)){
		alert(getAlrt);
		obj.focus();
		obj.select();
	}
	//Date------------------------------------------------------------
	if (errId=="date"&&!uitmpl_chkdate(obj)){
		alert(getAlrt);
		obj.focus();
		obj.select();
		}
}

//supplemental date check function
function uitmpl_chkdate(objName) {
var strDatestyle = "US"; //United States date style
//var strDatestyle = "EU";  //European date style
var strDate;
var strDateArray;
var strDay;
var strMonth;
var strYear;
var intday;
var intMonth;
var intYear;
var booFound = false;

var datefield = objName;
var strSeparatorArray = new Array("-"," ","/",".");
var intElementNr;
var err = 0;
var strMonthArray = new Array(12);
strMonthArray[0] = "Jan";
strMonthArray[1] = "Feb";
strMonthArray[2] = "Mar";
strMonthArray[3] = "Apr";
strMonthArray[4] = "May";
strMonthArray[5] = "Jun";
strMonthArray[6] = "Jul";
strMonthArray[7] = "Aug";
strMonthArray[8] = "Sep";
strMonthArray[9] = "Oct";
strMonthArray[10] = "Nov";
strMonthArray[11] = "Dec";
strDate = datefield.value;
if (strDate.length < 1) {
return true;
}
for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) {
strDateArray = strDate.split(strSeparatorArray[intElementNr]);
if (strDateArray.length != 3) {
err = 1;
return false;
}
else {
strDay = strDateArray[0];
strMonth = strDateArray[1];
strYear = strDateArray[2];
}
booFound = true;
   }
}
if (booFound == false) {
if (strDate.length>5) {
strDay = strDate.substr(0, 2);
strMonth = strDate.substr(2, 2);
strYear = strDate.substr(4);
   }
}
if (strYear.length == 2) {
strYear = '20' + strYear;
}
// US style
if (strDatestyle == "US") {
strTemp = strDay;
strDay = strMonth;
strMonth = strTemp;
}
intday = parseInt(strDay, 10);
if (isNaN(intday)) {
err = 2;
return false;
}
intMonth = parseInt(strMonth, 10);
if (isNaN(intMonth)) {
for (i = 0;i<12;i++) {
if (strMonth.toUpperCase() == strMonthArray[i].toUpperCase()) {
intMonth = i+1;
strMonth = strMonthArray[i];
i = 12;
   }
}
if (isNaN(intMonth)) {
err = 3;
return false;
   }
}
intYear = parseInt(strYear, 10);
if (isNaN(intYear)) {
err = 4;
return false;
}
if (intMonth>12 || intMonth<1) {
err = 5;
return false;
}
if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
err = 6;
return false;
}
if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
err = 7;
return false;
}
if (intMonth == 2) {
if (intday < 1) {
err = 8;
return false;
}
if (LeapYear(intYear) == true) {
if (intday > 29) {
err = 9;
return false;
}
}
else {
if (intday > 28) {
err = 10;
return false;
}
}
}
//if (strDatestyle == "US") {
//datefield.value = strMonthArray[intMonth-1] + " " + intday+" " + strYear;
//}
//else {
//datefield.value = intday + " " + strMonthArray[intMonth-1] + " " + strYear;
//}
return true;
}
function LeapYear(intYear) {
if (intYear % 100 == 0) {
if (intYear % 400 == 0) { return true; }
}
else {
if ((intYear % 4) == 0) { return true; }
}
return false;
}

//range validation
function uitmpl_errRangeVal(obj, minNum, maxNum){
if (obj.value>=minNum&&obj.value<=maxNum||obj.value==""){
return true;
}else{
alert("Please input a number within the range of "+minNum+" and "+maxNum+"!");
obj.focus();
obj.select();
}
}

//end: error validation =======================================
//begin: popup windows =======================================
function uitmpl_popUpReg(url) {
	window.open(url);
}	
function uitmpl_popUpConfig(url, name, w, h, sc, rsz) {
	var features = "width="+w+",height="+h+",scrollbars="+sc+",resizable="+rsz+"menubar=0,status=1";
	var newWin = window.open(url, name, features);
	newWin.focus();
//popups from nav links:
}


function uitmpl_contact(){
uitmpl_popUpReg("");
}
function uitmpl_bizDirect(){
uitmpl_popUpReg("");
}

//function uitmpl_closeApp(){
//if(window.confirm("You did NOT save your data.  Do you want to close 'application name' without saving your data?")) {
//window.close();

function uitmpl_closeApp(name){
if (name != "") {
var cAppName=name;
}
else {
var cAppName="the current application";
}
if(window.confirm("You did NOT save your data.  Do you want to close\n" + cAppName + " without saving your data?")) {
window.close();
}
}
//end: popup windows =======================================	





//Select all checkboxes on a form=====================================
function uitmpl_chkBoxSelect(formName,btnOn,btnOff) {

	document.getElementById(btnOff).style.display = "block";
	document.getElementById(btnOn).style.display = "none";

	var f = formName;
	for (i=0; i < f.elements.length; i++) {
		if (f.elements[i].type=="checkbox") {
			var e = f.elements[i];
			e.checked = true;
		}
	}
}


//Clear all checkboxes on a form======================================
function uitmpl_chkBoxClear(formName,btnOn, btnOff) {

	document.getElementById(btnOff).style.display = "none";
	document.getElementById(btnOn).style.display = "block";

	var f = formName;
	for (i=0; i < f.elements.length; i++) {
		if (f.elements[i].type=="checkbox") {
			var e = f.elements[i];
			e.checked = false;
		}
	}
}

