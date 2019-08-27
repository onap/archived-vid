function loadjscssfile(filename, filetype){
    if (filetype=="js"){ //if filename is a external JavaScript file
    	var done = false;
        var script = document.createElement('script');	
  		script.src = filename;
  		script.async = false; 			
 		document.head.appendChild(script);
    }else if (filetype=="css"){ //if filename is an external CSS file
        var fileref=document.createElement("link");
        fileref.setAttribute("rel", "stylesheet");
        fileref.setAttribute("type", "text/css");
        fileref.setAttribute("async", false);
        fileref.setAttribute("href", filename);
        document.head.appendChild(fileref);
    };
}

function loadResource(){
	/*********************AngularJs***************************/
	loadjscssfile("./app/fusion/external/angular-1.5/angular.min.js", "js");
	loadjscssfile("./app/fusion/external/angular-1.5/angular-messages.js", "js");
	loadjscssfile("./app/fusion/external/angular-1.5/angular-touch.js", "js");
	loadjscssfile("./app/fusion/external/angular-1.5/angular-sanitize.min.js", "js");	
	loadjscssfile("./app/fusion/external/angular-1.5/angular-route.min.js", "js");
	loadjscssfile("./app/fusion/external/angular-1.5/angular-cookies.min.js", "js");
	
	loadjscssfile("./app/fusion/external/ds2/js/b2b-angular/b2b-library.min.js", "js");
/*	loadjscssfile("./app/fusion/external/ds2/js/digital-ng-library/digital.ng.library.min.js", "js");
*/
	loadjscssfile("./app/fusion/external/jquery/dist/jquery.min.js", "js");
	
	/*********************Angular Gridster***************************/
	loadjscssfile("./app/fusion/external/angular-gridster/dist/angular-gridster.min.css", "css");	
	loadjscssfile("./app/fusion/external/angular-bootstrap/ui-bootstrap-csp.css", "css");	
	loadjscssfile("./app/fusion/external/javascript-detect-element-resize/jquery.resize.js", "js");
	loadjscssfile("./app/fusion/external/angular-bootstrap/ui-bootstrap-tpls.min.js", "js");
	loadjscssfile("./app/fusion/external/angular-gridster/dist/angular-gridster.min.js", "js");

	/*********************Angular UI grid***************************/
	loadjscssfile("./app/fusion/external/angular-ui-grid/ui-grid.js", "js");
	loadjscssfile("./app/fusion/external/angular-ui-grid/ui-grid.css", "css");

	loadjscssfile("./app/fusion/external/ds2/js/appDS2.js", "js");	

	loadjscssfile("./app/fusion/scripts/DS2-services/headerServiceDS2.js", "js");
	loadjscssfile("./app/fusion/scripts/DS2-services/leftMenuServiceDS2.js", "js");
	loadjscssfile("./app/fusion/scripts/DS2-services/manifestService.js", "js");
	loadjscssfile("./app/fusion/scripts/DS2-directives/footer.js", "js");
	loadjscssfile("./app/fusion/scripts/DS2-directives/ds2Header.js", "js");
	loadjscssfile("./app/fusion/scripts/DS2-directives/ds2LeftMenu.js", "js");
	loadjscssfile("./app/fusion/external/ds2/js/digital-ng-library/digital-design-library.js", "js");	

	/*******DS2 styles*******/
	loadjscssfile("./app/fusion/external/ds2/css/b2b-angular/b2b-angular.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-accordion.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-bootstrap-datepicker.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-cc-input-field.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-tooltip.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/extras/x-tabs-pills.css", "css");	
	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-accordion.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-bootstrap-datepicker.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-c2c.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-cc-input-field.css", "css");	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-filmstrip.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-filters.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-legacynav-fix.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-marquee.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-pagination.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-popover.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-progressbar.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ds2-tooltip.css", "css");
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/global.css", "css");

	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/digital-design-library.css", "css");	
	/*******DS2 ICONS*******/
	
	loadjscssfile("./app/fusion/external/ds2/css/digital-ng-library/ionicons.css", "css");	
	loadjscssfile("./app/fusion/styles/ecomp.css", "css");
	loadjscssfile("./app/fusion/external/angular-gridster/dist/angular-gridster.min.css", "css");

}

window.onload = loadResource();
/*window.onload = function(){
	var appLoadingInterval = setInterval(function(){ loadApp() }, 10000);
	var count=0;
	function loadApp(){	
		count++
		if(typeof angular !== 'undefined') {
			angular.bootstrap(document, ['abs']);
			clearInterval(appLoadingInterval);
		}else if(count>10){
			clearInterval(appLoadingInterval);
		}
	}
}*/