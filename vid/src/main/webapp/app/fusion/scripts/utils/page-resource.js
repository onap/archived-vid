function loadjscssfile(filename, filetype){
    if (filetype=="js"){ //if filename is a external JavaScript file
    	var done = false;
        var script = document.createElement('script');	
  		script.src = filename;
  		script.async = false; 			
 		document.head.appendChild(script);
    }else if (filetype=="css"){ //if filename is an external CSS file
        var fileref=document.createElement("link")
        fileref.setAttribute("rel", "stylesheet")
        fileref.setAttribute("type", "text/css")
        fileref.setAttribute("async", false)
        fileref.setAttribute("href", filename)
        document.head.appendChild(fileref);
    } 
}

function loadResource(){
	//css
	loadjscssfile("app/fusion/external/ebz/fn-ebz.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/demo.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/base.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/btn.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/dtpk.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/frms.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/sldr.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/style.css", "css");	
	loadjscssfile("app/fusion/external/ebz/sandbox/styles/tbs.css", "css");	
	loadjscssfile("app/fusion/external/ebz/ebz_header/portal_ebz_header.css", "css");	
	loadjscssfile("static/fusion/css/jquery-ui.css", "css");	
	
	
	loadjscssfile("app/fusion/external/ebz/ebz_header/header.css", "css");	
	loadjscssfile("app/fusion/external/ebz/ebz_header/footer.css", "css");	
	// Basic AngularJS -->
	loadjscssfile("app/fusion/external/ebz/angular_js/angular.js", "js");
	loadjscssfile("app/fusion/external/ebz/angular_js/angular-sanitize.js", "js");
	loadjscssfile("app/fusion/external/ebz/angular_js/angular-route.min.js", "js");
	loadjscssfile("app/fusion/external/ebz/angular_js/angular-cookies.js", "js");
	loadjscssfile("app/fusion/external/ebz/angular_js/gestures.js", "js");
	loadjscssfile("app/fusion/external/ebz/angular_js/app.js", "js");

	loadjscssfile("app/fusion/external/ebz/sandbox/att-abs-tpls.js", "js");
		
	// jQuery -->	
	loadjscssfile("static/js/jquery-1.10.2.js", "js");
	loadjscssfile("static/js/jquery.mask.min.js", "js");	
	loadjscssfile("static/js/jquery-ui.js", "js");
	
	// AngularJS Gridster -->
	loadjscssfile("static/fusion/js/att_angular_gridster/ui-gridster-tpls.js", "js");
	loadjscssfile("static/fusion/js/att_angular_gridster/angular-gridster.js", "js");
	
	// AngularJS Config -->
	loadjscssfile("app/fusion/external/ebz/angular_js/checklist-model.js", "js");
	
	// Utility -->
	loadjscssfile("app/fusion/scripts/modalService.js", "js");
	loadjscssfile("app/fusion/external/angular-ui/ui-bootstrap-tpls-1.1.2.min.js", "js");	
	
	// Controller js -->
	loadjscssfile("app/fusion/scripts/controllers/profile-search-controller.js", "js");
	loadjscssfile("app/fusion/scripts/controllers/profile-controller.js", "js");
	loadjscssfile("app/fusion/scripts/controllers/post-search-controller.js", "js");
	loadjscssfile("app/fusion/scripts/controllers/self-profile-controller.js", "js");
	loadjscssfile("app/fusion/scripts/controllers/rolefunctionpopupController.js", "js");	
	loadjscssfile("app/fusion/scripts/controllers/modelpopupController.js", "js");
	
	// Header and Footer -->
	loadjscssfile("app/fusion/scripts/directives/footer.js", "js");
	loadjscssfile("app/fusion/external/ebz/js/footer.js", "js");
	loadjscssfile("app/fusion/scripts/directives/header.js", "js");		
	loadjscssfile("app/fusion/scripts/directives/leftMenu.js", "js");
	
	// Services -->
	loadjscssfile("app/fusion/scripts/services/profileService.js", "js");	
	loadjscssfile("app/fusion/scripts/services/userInfoService.js", "js");
	loadjscssfile("app/fusion/scripts/services/leftMenuService.js", "js");	
	loadjscssfile("app/fusion/scripts/controllers/profileController.js", "js");			
}

window.onload = loadResource();
window.onload = function(){
	var appLoadingInterval = setInterval(function(){ loadApp() }, 500);
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
}
