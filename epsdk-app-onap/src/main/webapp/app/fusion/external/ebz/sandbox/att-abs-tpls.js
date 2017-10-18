/**
* FileName att-style-guide
* Version 2.17.0
* Build number 5c64ecd381d5984c483cdfaa078a1890
* Date 11/23/2015
*/

(function(angular, window){
angular.module("att.abs", ["att.abs.tpls", "att.abs.utilities","att.abs.position","att.abs.transition","att.abs.accordion","att.abs.alert","att.abs.boardStrip","att.abs.breadCrumbs","att.abs.buttons","att.abs.checkbox","att.abs.colorselector","att.abs.datepicker","att.abs.devNotes","att.abs.dividerLines","att.abs.dragdrop","att.abs.drawer","att.abs.message","att.abs.formField","att.abs.hourpicker","att.abs.iconButtons","att.abs.links","att.abs.loading","att.abs.modal","att.abs.pagination","att.abs.paneSelector","att.abs.tooltip","att.abs.popOvers","att.abs.profileCard","att.abs.progressBars","att.abs.radio","att.abs.scrollbar","att.abs.search","att.abs.select","att.abs.slider","att.abs.splitButtonDropdown","att.abs.splitIconButton","att.abs.stepSlider","att.abs.steptracker","att.abs.table","att.abs.tableMessages","att.abs.tabs","att.abs.tagBadges","att.abs.textOverflow","att.abs.toggle","att.abs.treeview","att.abs.typeAhead","att.abs.verticalSteptracker","att.abs.videoControls"]);
angular.module("att.abs.tpls", ["app/scripts/ng_js_att_tpls/accordion/accordion.html","app/scripts/ng_js_att_tpls/accordion/accordion_alt.html","app/scripts/ng_js_att_tpls/accordion/attAccord.html","app/scripts/ng_js_att_tpls/accordion/attAccordBody.html","app/scripts/ng_js_att_tpls/accordion/attAccordHeader.html","app/scripts/ng_js_att_tpls/alert/alert.html","app/scripts/ng_js_att_tpls/boardStrip/attAddBoard.html","app/scripts/ng_js_att_tpls/boardStrip/attBoard.html","app/scripts/ng_js_att_tpls/boardStrip/attBoardStrip.html","app/scripts/ng_js_att_tpls/buttons/buttonDropdown.html","app/scripts/ng_js_att_tpls/colorselector/colorselector.html","app/scripts/ng_js_att_tpls/datepicker/dateFilter.html","app/scripts/ng_js_att_tpls/datepicker/dateFilterList.html","app/scripts/ng_js_att_tpls/datepicker/datepicker.html","app/scripts/ng_js_att_tpls/datepicker/datepickerPopup.html","app/scripts/ng_js_att_tpls/dividerLines/dividerLines.html","app/scripts/ng_js_att_tpls/dragdrop/fileUpload.html","app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlert.html","app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlertPrv.html","app/scripts/ng_js_att_tpls/formField/creditCardImage.html","app/scripts/ng_js_att_tpls/formField/cvcSecurityImg.html","app/scripts/ng_js_att_tpls/hourpicker/hourpicker.html","app/scripts/ng_js_att_tpls/links/readMore.html","app/scripts/ng_js_att_tpls/loading/loading.html","app/scripts/ng_js_att_tpls/modal/backdrop.html","app/scripts/ng_js_att_tpls/modal/tabbedItem.html","app/scripts/ng_js_att_tpls/modal/tabbedOverlayItem.html","app/scripts/ng_js_att_tpls/modal/window.html","app/scripts/ng_js_att_tpls/pagination/pagination.html","app/scripts/ng_js_att_tpls/paneSelector/innerPane.html","app/scripts/ng_js_att_tpls/paneSelector/paneGroup.html","app/scripts/ng_js_att_tpls/paneSelector/sidePane.html","app/scripts/ng_js_att_tpls/tooltip/tooltip-popup.html","app/scripts/ng_js_att_tpls/popOvers/popOvers.html","app/scripts/ng_js_att_tpls/profileCard/addUser.html","app/scripts/ng_js_att_tpls/profileCard/profileCard.html","app/scripts/ng_js_att_tpls/progressBars/progressBars.html","app/scripts/ng_js_att_tpls/scrollbar/scrollbar.html","app/scripts/ng_js_att_tpls/search/search.html","app/scripts/ng_js_att_tpls/select/select.html","app/scripts/ng_js_att_tpls/select/textDropdown.html","app/scripts/ng_js_att_tpls/slider/maxContent.html","app/scripts/ng_js_att_tpls/slider/minContent.html","app/scripts/ng_js_att_tpls/slider/slider.html","app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdown.html","app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdownItem.html","app/scripts/ng_js_att_tpls/splitIconButton/splitIcon.html","app/scripts/ng_js_att_tpls/splitIconButton/splitIconButton.html","app/scripts/ng_js_att_tpls/splitIconButton/splitIconButtonGroup.html","app/scripts/ng_js_att_tpls/stepSlider/attStepSlider.html","app/scripts/ng_js_att_tpls/steptracker/step-tracker.html","app/scripts/ng_js_att_tpls/steptracker/step.html","app/scripts/ng_js_att_tpls/steptracker/timeline.html","app/scripts/ng_js_att_tpls/steptracker/timelineBar.html","app/scripts/ng_js_att_tpls/steptracker/timelineDot.html","app/scripts/ng_js_att_tpls/table/attTable.html","app/scripts/ng_js_att_tpls/table/attTableBody.html","app/scripts/ng_js_att_tpls/table/attTableHeader.html","app/scripts/ng_js_att_tpls/tableMessages/attTableMessage.html","app/scripts/ng_js_att_tpls/tableMessages/attUserMessage.html","app/scripts/ng_js_att_tpls/tabs/floatingTabs.html","app/scripts/ng_js_att_tpls/tabs/genericTabs.html","app/scripts/ng_js_att_tpls/tabs/menuTab.html","app/scripts/ng_js_att_tpls/tabs/parentmenuTab.html","app/scripts/ng_js_att_tpls/tabs/simplifiedTabs.html","app/scripts/ng_js_att_tpls/tabs/submenuTab.html","app/scripts/ng_js_att_tpls/tagBadges/tagBadges.html","app/scripts/ng_js_att_tpls/toggle/demoToggle.html","app/scripts/ng_js_att_tpls/typeAhead/typeAhead.html","app/scripts/ng_js_att_tpls/verticalSteptracker/vertical-step-tracker.html","app/scripts/ng_js_att_tpls/videoControls/photoControls.html","app/scripts/ng_js_att_tpls/videoControls/videoControls.html"]);
angular.module('att.abs.utilities', [])

.filter('unsafe',[ '$sce', function ($sce) {
        return function(val){
       return $sce.trustAsHtml(val);
    };
}])

.filter('highlight', function () {
    function escapeRegexp(queryToEscape) {
        return queryToEscape.replace(/([.?*+^$[\]\\(){}|-])/g, '\\$1');
    }
    return function (matchItem, query, className) {
        return query && matchItem ? matchItem.replace(new RegExp(escapeRegexp(query), 'gi'), '<span class=\"' + className + '\">$&</span>') : matchItem;
    };
})

.filter('attLimitTo', function() {
    return function(actualArray, _limit, _begin) {
        var finalArray = [];
        var limit = _limit;
        var begin = _begin;
        if(isNaN(begin)) {
            begin = 0;
        }
        if(actualArray && !isNaN(limit)) {
            finalArray = actualArray.slice(begin, begin+limit);
        } else {
            finalArray = actualArray;
        }
        return finalArray;
    };
})

.filter('startsWith', function() {
    if (typeof String.prototype.startsWith !== 'function') {
        // see below for better implementation!
        String.prototype.startsWith = function (str){
            return this.indexOf(str) === 0;
        };
    }

    return function(items, searchString) {
        if (searchString === undefined || searchString === "") {
            return items;
        }

        var filtered = [];
        angular.forEach(items, function(item) {
            if (item.title.toLowerCase().startsWith(searchString.toLowerCase())) {
                filtered.push(item);
            }
        });
        return filtered;
    };
})

.directive('attInputDeny', [function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            var regexExpression = null;
            attr.$observe('attInputDeny', function (value) {
                if (value) {
                    regexExpression = new RegExp(value, 'g');
                }
            });
            elem.bind('input', function () {
                var inputString = ctrl.$viewValue && ctrl.$viewValue.replace(regexExpression, '');
                if (inputString !== ctrl.$viewValue) {
                    ctrl.$setViewValue(inputString);
                    ctrl.$render();
                    scope.$apply();
                }
            });
        }
    };
}])

.directive('attAccessibilityClick', [function() {
    return {
        restrict: 'A',
        link: function (scope, elem, attr) {
            var keyCode = [];
            attr.$observe('attAccessibilityClick', function (value) {
                if (value) {
                    keyCode = value.split(',');
                }
            });
            elem.bind('keydown', function (ev) {
                var keyCodeCondition = function(){
                    var flag = false;
                    if(!(ev.keyCode)){
                        if(ev.which){
                            ev.keyCode = ev.which;
                        }
                        else if(ev.charCode){
                            ev.keyCode = ev.charCode;
                        }
                    }
                    if((ev.keyCode && keyCode.indexOf(ev.keyCode.toString()) > -1)){
                        flag = true;
                    }
                    return flag;
                };
                if(keyCode.length > 0 && keyCodeCondition()) {
                    elem[0].click();
                    ev.preventDefault();
                }
            });
        }
    };
}])

.directive('attElementFocus', [function() {
    return {
        restrict: 'A',
        link: function(scope, elem, attr) {
            scope.$watch(attr.attElementFocus, function (value) {
                if (value) {
                    elem[0].focus();
                }
            });
        }
    };
}])

.directive('focusOn', ['$timeout',
    function ($timeout) {
        var checkDirectivePrerequisites = function (attrs) {
          if (!attrs.focusOn && attrs.focusOn !== '') {
                throw 'FocusOnCondition missing attribute to evaluate';
          }
        };
        return {            
            restrict: 'A',
            link: function (scope, element, attrs) {
                checkDirectivePrerequisites(attrs);

                scope.$watch(attrs.focusOn, function (currentValue) {
                    if(currentValue) {
                        $timeout(function () {                                             
                            element[0].focus();
                        });
                    }
                });
            }
        };
    }
])
.constant('whenScrollEndsConstants', {
    'threshold': 100,
    'width': 0,
    'height': 0
})
.directive('whenScrollEnds', function(whenScrollEndsConstants, $log) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            /**
            * Exposed Attributes:
            *       threshold - integer - number of pixels before scrollbar hits end that callback is called
            *       width - integer - override for element's width (px)
            *       height - integer - override for element's height (px)
            *       axis - string - x/y for scroll bar axis
            */
            var threshold = parseInt(attrs.threshold, 10) || whenScrollEndsConstants.threshold;

            if (!attrs.axis || attrs.axis === '') {
                $log.warn('axis attribute must be defined for whenScrollEnds.');
                return;
            }

            if (attrs.axis === 'x') {
                visibleWidth = parseInt(attrs.width, 10) || whenScrollEndsConstants.width;
                if (element.css('width')) {
                    visibleWidth = element.css('width').split('px')[0];  
                }

                element[0].addEventListener('scroll', function() {
                    var scrollableWidth = element.prop('scrollWidth');
                    if (scrollableWidth === undefined) {
                        scrollableWidth = 1;
                    }
                    var hiddenContentWidth = scrollableWidth - visibleWidth;

                    if (hiddenContentWidth - element[0].scrollLeft <= threshold) {
                        /* Scroll almost at bottom, load more rows */
                        scope.$apply(attrs.whenScrollEnds);
                    }
                });
            } else if (attrs.axis === 'y') {
                visibleHeight = parseInt(attrs.height, 10) || whenScrollEndsConstants.height;
                if (element.css('width')) {
                    visibleHeight = element.css('height').split('px')[0]; 
                }

                element[0].addEventListener('scroll', function() {
                    var scrollableHeight = element.prop('scrollHeight');
                    if (scrollableHeight === undefined) {
                        scrollableHeight = 1;
                    }
                    var hiddenContentHeight = scrollableHeight - visibleHeight;

                    if (hiddenContentHeight - element[0].scrollTop <= threshold) {
                        /* Scroll almost at bottom, load more rows */
                        scope.$apply(attrs.whenScrollEnds);
                    }
                });
            }
        }
    };
})

.directive("validImei", function(){          
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, ele, attrs, ctrl)
        {       
            ctrl.$parsers.unshift(function(value) 
            {
                if(value){                    
                    scope.valid = false;
                    if (!isNaN(value) && value.length === 15)
                    {
                        var sumImeiVal = 0;	
                        var posIMEI = [];
                        for (var imeiIndex=0; imeiIndex<15; imeiIndex++)
                        {
                            posIMEI[imeiIndex] =  parseInt(value.substring(imeiIndex,imeiIndex + 1),10);
                            if (imeiIndex % 2 !== 0)
                            {
                                posIMEI[imeiIndex] = parseInt((posIMEI[imeiIndex] * 2),10);                                        
                            }
                            if (posIMEI[imeiIndex] > 9)
                            {    
                                posIMEI[imeiIndex] = parseInt((posIMEI[imeiIndex] % 10),10) + parseInt((posIMEI[imeiIndex] / 10),10);                                        
                            }
                            sumImeiVal=sumImeiVal+parseInt((posIMEI[imeiIndex]),10);
                        }

                        if((sumImeiVal % 10) === 0)
                        {
                            scope.valid = true;                            
                        }
                        else
                        {
                            scope.valid = false;                            
                        }                                
                    }
                    else 
                    {
                        scope.valid = false;                        
                    }                    
                    ctrl.$setValidity('invalidImei', scope.valid);
                }
                return scope.valid ? value : undefined;
            });
        }
    };
})

.directive("togglePassword", function(){
    return{
        restrict:'A',        
        transclude:false,
        link: function(scope, element, attr, ctrl)
        {
            element.bind("click", function() 
            {
                var ipwd = attr.togglePassword;
                element[0].innerHTML = element[0].innerHTML === "Show" ? "Hide" : "Show";                
                var e = angular.element(document.querySelector('#' + ipwd))[0].type;
                angular.element(document.querySelector('#' + ipwd))[0].type = e === "password"? "text" : "password";
                
            });      
                       
        }
    };
})

.factory('events', function(){
    var _stopPropagation =  function(evt){
        if(evt.stopPropagation) {
            evt.stopPropagation();
        } else {
            evt.returnValue = false;
        }
    };
    var _preventDefault = function(evt) {
        if (evt.preventDefault) {
            evt.preventDefault();
        } else {
            evt.returnValue = false;
        }
    }
    return {
        stopPropagation: _stopPropagation,
        preventDefault: _preventDefault
    };
})

.factory('$documentBind', ['$document', '$timeout', function ($document, $timeout) {
    var _click = function (flag, callbackFunc, scope) {
        scope.$watch(flag, function (val) {
            $timeout(function () {
                if (val) {
                    $document.bind('click', callbackFunc);
                } else {
                    $document.unbind('click', callbackFunc);
                }
            });
        });
    };

    var _event = function (event, flag, callbackFunc, scope, timeoutFlag, timeoutValue) {
        if (timeoutFlag) {
            if (!(timeoutValue)) {
                timeoutValue = 0;
            }
            scope.$watch(flag, function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    $timeout(function () {
                        if (newVal) {
                            $document.bind(event, callbackFunc);
                        } else {
                            $document.unbind(event, callbackFunc);
                        }
                    }, timeoutValue);
                }
            });
        } else {
            scope.$watch(flag, function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if (newVal) {
                        $document.bind(event, callbackFunc);
                    } else {
                        $document.unbind(event, callbackFunc);
                    }
                }
            });
        }
    };

    return {
        click: _click,
        event: _event
    };
}])

.factory('DOMHelper', function() {

    function isTabable(node) {
        var element = angular.element(node);
        var hasTabindex = (parseInt(element.attr('tabindex'), 10) >= 0) ? true : false;
        var tagName = element[0].tagName.toUpperCase();

        if (hasTabindex || tagName === 'A' || tagName === 'INPUT' || tagName === 'TEXTAREA') {
            return !(element[0].disabled || element[0].readOnly);
        }
        return false;
    }

    function isValidChild(child) {
        return child.nodeType == 1 && child.nodeName != 'SCRIPT' && child.nodeName != 'STYLE';
    }

    function traverse(obj) {
        var obj = obj || document.getElementsByTagName('body')[0];
        if (isValidChild(obj) && isTabable(obj)) {
            return obj;
        } 
        if (obj.hasChildNodes()) {
            var child = obj.firstChild;
            while(child) {
                var res =  traverse(child);
                if(res){
                    return res;
                }
                else{
                    child = child.nextSibling;
                }
            }
        }
        else{
            return undefined;
        }
    }

    var _firstTabableElement = function(el) {
        /* This will return the first tabable element from the parent el */
        var elem = el;
        if (el.hasOwnProperty('length')) {
            elem = el[0];
        }

        return traverse(elem);
    };

    return {
        firstTabableElement: _firstTabableElement
    };
})

.factory('keymap', function(){
    return {
    KEY:{
        TAB: 9,
        ENTER: 13,
        ESC: 27,
        SPACE: 32,
        LEFT: 37,
        UP: 38,
        RIGHT: 39,
        DOWN: 40,
        SHIFT: 16,
        CTRL: 17,
        ALT: 18,
        PAGE_UP: 33,
        PAGE_DOWN: 34,
        HOME: 36,
        END: 35,
        BACKSPACE: 8,
        DELETE: 46,
        COMMAND: 91
        },
        MAP: { 91 : "COMMAND", 8 : "BACKSPACE" , 9 : "TAB" , 13 : "ENTER" , 16 : "SHIFT" , 17 : "CTRL" , 18 : "ALT" , 19 : "PAUSEBREAK" , 20 : "CAPSLOCK" , 27 : "ESC" , 32 : "SPACE" , 33 : "PAGE_UP", 34 : "PAGE_DOWN" , 35 : "END" , 36 : "HOME" , 37 : "LEFT" , 38 : "UP" , 39 : "RIGHT" , 40 : "DOWN" , 43 : "+" , 44 : "PRINTSCREEN" , 45 : "INSERT" , 46 : "DELETE", 48 : "0" , 49 : "1" , 50 : "2" , 51 : "3" , 52 : "4" , 53 : "5" , 54 : "6" , 55 : "7" , 56 : "8" , 57 : "9" , 59 : ";", 61 : "=" , 65 : "A" , 66 : "B" , 67 : "C" , 68 : "D" , 69 : "E" , 70 : "F" , 71 : "G" , 72 : "H" , 73 : "I" , 74 : "J" , 75 : "K" , 76 : "L", 77 : "M" , 78 : "N" , 79 : "O" , 80 : "P" , 81 : "Q" , 82 : "R" , 83 : "S" , 84 : "T" , 85 : "U" , 86 : "V" , 87 : "W" , 88 : "X" , 89 : "Y" , 90 : "Z", 96 : "0" , 97 : "1" , 98 : "2" , 99 : "3" , 100 : "4" , 101 : "5" , 102 : "6" , 103 : "7" , 104 : "8" , 105 : "9", 106 : "*" , 107 : "+" , 109 : "-" , 110 : "." , 111 : "/", 112 : "F1" , 113 : "F2" , 114 : "F3" , 115 : "F4" , 116 : "F5" , 117 : "F6" , 118 : "F7" , 119 : "F8" , 120 : "F9" , 121 : "F10" , 122 : "F11" , 123 : "F12", 144 : "NUMLOCK" , 145 : "SCROLLLOCK" , 186 : ";" , 187 : "=" , 188 : "," , 189 : "-" , 190 : "." , 191 : "/" , 192 : "`" , 219 : "[" , 220 : "\\" , 221 : "]" , 222 : "'"
        },
        isControl: function (e) {
            var k = e.keyCode;
            switch (k) {
            case this.KEY.COMMAND:
            case this.KEY.SHIFT:
            case this.KEY.CTRL:
            case this.KEY.ALT:
                return true;
            default:;
            }

            if (e.metaKey) {
                return true;
            }

            return false;
        },
        isFunctionKey: function (k) {
            k = k.keyCode ? k.keyCode : k;
            return k >= 112 && k <= 123;
        },
        isVerticalMovement: function (k){
          return ~[this.KEY.UP, this.KEY.DOWN].indexOf(k);
        },
        isHorizontalMovement: function (k){
          return ~[this.KEY.LEFT, this.KEY.RIGHT, this.KEY.BACKSPACE, this.KEY.DELETE].indexOf(k);
        },
        isAllowedKey: function (k){
          return (~[this.KEY.SPACE, this.KEY.ESC, this.KEY.ENTER].indexOf(k)) || this.isHorizontalMovement(k) || this.isVerticalMovement(k);
        }
    };
})

.factory('keyMapAc', function(){
    return {
            keys:{ "32":" ", "33":"!", "34":"\"", "35":"#", "36":"$", "37":"%", "38":"&", "39":"'"
                , "40":"(", "41":")", "42":"*", "43":"+", "44":",", "45":"-", "46":".", "47":"\/", "58":":"
                , "59":";", "60":"<", "61":"=", "62":">", "63":"?", "64":"@", "91":"[", "92":"\\", "93":"]"
                , "94":"^", "95":"_", "96":"`", "123":"{", "124":"|", "125":"}", "126":"~"
            },
            keyRange:{"startNum":"48","endNum":"57","startSmallLetters":"97","endSmallLetters":"122"
                ,"startCapitalLetters":"65","endCapitalLetters":"90"},
            allowedKeys:{TAB:8, BACKSPACE:9, DELETE:16}
        };
})

.factory('$attElementDetach', function () {
    var _detach = function (element) {
        if (element && element.parentNode) {
            element.parentNode.removeChild(element);
        }
    };
    return _detach;
})

.factory('$ieVersion', function () {
	var ie = null;
	
	var loadIEVersion = function () {
		var isIE10 = (eval("/*@cc_on!@*/false") && document.documentMode === 10);
		if (isIE10) {
			return 10;
		}
		var v = 3,
			div = document.createElement('div'),
			all = div.getElementsByTagName('i');
		do {
			div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->';
		} while (all[0]);
		return v > 4 ? v : undefined;
	};
	
	return function () {
		if (ie===null) {
			ie = loadIEVersion();
		}
		return ie;
	};
});
(function () {
    String.prototype.toSnakeCase = function () {
        return this.replace(/([A-Z])/g, function ($1) {
            return "-" + $1.toLowerCase();
        });
    };
    var concat = function (character, times) {
        character = character || '';
        times = (!isNaN(times) && times) || 0;
        var finalChar = '';
        for (var i = 0; i < times; i++) {
            finalChar += character;
        }
        return finalChar;
    };

    // direction: true for left and false for right
    var pad = function (actualString, width, character, direction) {
        actualString = actualString || '';
        width = (!isNaN(width) && width) || 0;
        character = character || '';
        if (width > actualString.length) {
            if (direction) {
                return concat(character, (width - actualString.length)) + actualString;
            } else {
                return actualString + concat(character, (width - actualString.length));
            }
        }
        return actualString;
    };

    String.prototype.lPad = function (width, character) {
        return pad(this, width, character, true);
    };

    String.prototype.rPad = function (width, character) {
        return pad(this, width, character, false);
    };

    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function (val) {
            for (var index = 0; index < this.length; index++) {
                if (this[index] === val) {
                    return index;
                }
            }
            return -1;
        };
    }
})();

angular.module('att.abs.position', [])

.factory('$position', ['$document', '$window', function ($document, $window) {
    function getStyle(el, cssprop) {
        if (el.currentStyle) { //IE
            return el.currentStyle[cssprop];
        } else if ($window.getComputedStyle) {
            return $window.getComputedStyle(el)[cssprop];
        }
        // finally try and get inline style
        return el.style[cssprop];
    }

    /**
     * Checks if a given element is statically positioned
     * @param element - raw DOM element
     */
    function isStaticPositioned(element) {
        return (getStyle(element, "position") || 'static') === 'static';
    }

    /**
     * returns the closest, non-statically positioned parentOffset of a given element
     * @param element
     */
    var parentOffsetEl = function (element) {
        var docDomEl = $document[0];
        var offsetParent = element.offsetParent || docDomEl;
        while (offsetParent && offsetParent !== docDomEl && isStaticPositioned(offsetParent)) {
            offsetParent = offsetParent.offsetParent;
        }
        return offsetParent || docDomEl;
    };

    return {
        /**
         * Provides read-only equivalent of jQuery's position function:
         * http://api.jquery.com/position/
         */
        position: function (element) {
            var elBCR = this.offset(element);
            var offsetParentBCR = {
                top: 0,
                left: 0
            };
            var offsetParentEl = parentOffsetEl(element[0]);
            if (offsetParentEl !== $document[0]) {
                offsetParentBCR = this.offset(angular.element(offsetParentEl));
                offsetParentBCR.top += offsetParentEl.clientTop - offsetParentEl.scrollTop;
                offsetParentBCR.left += offsetParentEl.clientLeft - offsetParentEl.scrollLeft;
            }

            return {
                width: element.prop('offsetWidth'),
                height: element.prop('offsetHeight'),
                top: elBCR.top - offsetParentBCR.top,
                left: elBCR.left - offsetParentBCR.left
            };
        },

        /**
         * Provides read-only equivalent of jQuery's offset function:
         * http://api.jquery.com/offset/
         */
        offset: function (element) {
            var boundingClientRect = element[0].getBoundingClientRect();
            return {
                width: element.prop('offsetWidth'),
                height: element.prop('offsetHeight'),
                top: boundingClientRect.top + ($window.pageYOffset || $document[0].body.scrollTop || $document[0].documentElement.scrollTop),
                left: boundingClientRect.left + ($window.pageXOffset || $document[0].body.scrollLeft || $document[0].documentElement.scrollLeft)
            };
        }
    };
}])

.factory('$isElement', [function () {
    var isElement = function (currentElem, targetElem, alternateElem) {
        if (currentElem[0] === targetElem[0]) {
            return true;
        } else if (currentElem[0] === alternateElem[0]) {
            return false;
        } else {
            return isElement((currentElem.parent()[0] && currentElem.parent()) || targetElem, targetElem, alternateElem);
        }
    };

    return isElement;
}])

.directive('attPosition', ['$position', function ($position) {
    return {
        restrict: 'A',
        link: function (scope, elem, attr) {
            scope.$watchCollection(function () {
                return $position.position(elem);
            }, function (value) {
                scope[attr.attPosition] = value;
            });
        }
    };
}]);


angular.module('att.abs.transition', [])

.factory('$transition', ['$q', '$timeout', '$rootScope', function($q, $timeout, $rootScope) {

  var $transition = function(element, trigger, options) {
    options = options || {};
    var deferred = $q.defer();
    var endEventName = $transition[options.animation ? "animationEndEventName" : "transitionEndEventName"];

    var transitionEndHandler = function() {
      $rootScope.$apply(function() {
        element.unbind(endEventName, transitionEndHandler);
        deferred.resolve(element);
      });
    };

    if (endEventName) {
      element.bind(endEventName, transitionEndHandler);
    }

    // Wrap in a timeout to allow the browser time to update the DOM before the transition is to occur
    $timeout(function() {
      if ( angular.isString(trigger) ) {
        element.addClass(trigger);
      } else if ( angular.isFunction(trigger) ) {
        trigger(element);
      } else if ( angular.isObject(trigger) ) {
        element.css(trigger);
      }
      //If browser does not support transitions, instantly resolve
      if ( !endEventName ) {
        deferred.resolve(element);
      }
    }, 100);

    // Add our custom cancel function to the promise that is returned
    // We can call this if we are about to run a new transition, which we know will prevent this transition from ending,
    // i.e. it will therefore never raise a transitionEnd event for that transition
    deferred.promise.cancel = function() {
      if ( endEventName ) {
        element.unbind(endEventName, transitionEndHandler);
      }
      deferred.reject('Transition cancelled');
    };

    return deferred.promise;
  };

  // Work out the name of the transitionEnd event
  var transElement = document.createElement('trans');
  var transitionEndEventNames = {
    'WebkitTransition': 'webkitTransitionEnd',
    'MozTransition': 'transitionend',
    'OTransition': 'oTransitionEnd',
    'transition': 'transitionend'
  };
  var animationEndEventNames = {
    'WebkitTransition': 'webkitAnimationEnd',
    'MozTransition': 'animationend',
    'OTransition': 'oAnimationEnd',
    'transition': 'animationend'
  };
  function findEndEventName(endEventNames) {
    for (var name in endEventNames){
      if (transElement.style[name] !== undefined) {
        return endEventNames[name];
      }
    }
  }
  $transition.transitionEndEventName = findEndEventName(transitionEndEventNames);
  $transition.animationEndEventName = findEndEventName(animationEndEventNames);
  return $transition;
}])

.factory('$scrollTo', ['$window', function($window) {
    var $scrollTo = function(offsetLeft, offsetTop, duration) {
        TweenMax.to($window, duration || 1, {scrollTo: {y: offsetTop, x: offsetLeft}, ease: Power4.easeOut});
    };
    return $scrollTo;
}])
.factory('animation', function(){
    return TweenMax;
})
.factory('$progressBar', function(){

   //Provides a function to pass in code for closure purposes
   var loadingAnimationCreator = function(onUpdateCallback){

      //Use closure to setup some resuable code
      var loadingAnimation = function(callback, duration){
          TweenMax.to({}, duration, {
              onUpdateParams: ["{self}"],
              onUpdate: onUpdateCallback,
              onComplete: callback
          });
      };
      //Returns a function that takes a callback function and a duration for the animation
      return (function(){
        return loadingAnimation;
      })();
    };

  return loadingAnimationCreator;
})
.factory('$height', function(){
  var heightAnimation = function(element,duration,height,alpha){
    TweenMax.to(element,
      duration,
      {height:height, autoAlpha:alpha},
      0);
  };
  return heightAnimation;
});
angular.module('att.abs.accordion', ['att.abs.utilities', 'att.abs.position', 'att.abs.transition'])
.constant('accordionConfig', {
    closeOthers: false
}).controller('AccordionController', ['$scope', '$attrs', 'accordionConfig', '$log',
function ($scope, $attrs, accordionConfig, $log) {
// This array keeps track of the accordion groups
this.groups = [];
this.index = -1;
// Keep reference to user's scope to properly assign `is-open`
this.scope = $scope;
$scope.forceExpand = false;
// Ensure that all the groups in this accordion are closed, unless close-others explicitly says not to
this.closeOthers = function (openGroup) {
    var closeOthers = angular.isDefined($attrs.closeOthers) ? $scope.$eval($attrs.closeOthers) : accordionConfig.closeOthers;
    if (closeOthers && !$scope.forceExpand) {
        angular.forEach(this.groups, function (group) {
            if (group !== openGroup) {
                group.isOpen = false;
            }
        });
    }
    if (this.groups.indexOf(openGroup) === (this.groups.length - 1) && $scope.forceExpand) {
        $scope.forceExpand = false;
    }
};
this.expandAll = function () {
    $scope.forceExpand = true;
    angular.forEach(this.groups, function (group) {
        group.isOpen = true;
    });
};
this.collapseAll = function () {
    angular.forEach(this.groups, function (group) {
        group.isOpen = false;
    });
};
/**function focus @param focusGroup */
this.focus = function (focusGroup) {
    var self = this;
    angular.forEach(this.groups, function (group, index) {
        if (group !== focusGroup) {
            group.focused = false;
        } else {
            self.index = index;
            group.focused = true;
        }
    });
};
/** @param blurGroup*/
this.blur = function (blurGroup) {
    blurGroup.focused = false;
    this.index = -1;
    $log.log("accordion.blur()", blurGroup);
};
/** @param group - the group in current focus @param down - cycling down */
this.cycle = function (group, down, noRecycle) {
    if (!down) {
        if (this.index <= 0 && !noRecycle) {
            this.index = this.groups.length - 1;
        } else {
            this.index--;
        }
    } else {
        if (this.index === (this.groups.length - 1))
        {
            if (noRecycle) {
                this.index = 0;
                group.focused = false;
                $scope.$apply();
                return;
            }
            else
            {
                this.index = 0;
            }
        } else {
            this.index++;
        }
    }

    group.focused = false;
    this.groups[this.index].setFocus = true;
    this.groups[this.index].focused = true;
    $scope.$apply();
};
// This is called from the accordion-group directive to add itself to the accordion
this.addGroup = function (groupScope) {
    var that = this;
    groupScope.index = this.groups.length;
    groupScope.focused = false;
    this.groups.push(groupScope);

    if(this.groups.length > 0){
        this.index = 0;
    }

    groupScope.$on('$destroy', function () {
        that.removeGroup(groupScope);
    });
};
// This is called from the accordion-group directive when to remove itself
this.removeGroup = function (group) {
    var index = this.groups.indexOf(group);
    if (index !== -1) {
        this.groups.splice(this.groups.indexOf(group), 1);
    }
};
}])
//The accordion directive simply sets up the directive controller and adds an accordion CSS class to itself element.
.directive('accordion', function () {
    return {
        restrict: 'EA',
        controller: 'AccordionController',
        transclude: true,
        replace: false,
        scope: {
            cClass: '@css',
            expandAll: "=?",
            collapseAll: "=?"
        },
        template: '<div class="{{cClass}}" ng-transclude></div>',
        link: function (scope, elem, attribute, ctrl) {
            scope.$watch("expandAll", function (value) {
                if (value) {
                    ctrl.expandAll();
                    scope.expandAll = false;
                }
            });
            scope.$watch("collapseAll", function (value) {
                if (value) {
                    ctrl.collapseAll();
                    scope.collapseAll = false;
                }
            });
        }
    };
})
//The accordion-group directive indicates a block of html that will expand and collapse in an accordion
.directive('accordionGroup', [ function () {
        return {
            // We need this directive to be inside an accordion
            require: ['^accordion', 'accordionGroup'],
            restrict: 'EA',
            // It transcludes the contents of the directive into the template
            transclude: true,
            // The element containing the directive will be replaced with the template
            replace: true,
            templateUrl: 'app/scripts/ng_js_att_tpls/accordion/accordion.html',
            scope: {
                // Create an isolated scope and interpolate the heading attribute onto this scope
                heading: '@',
                isOpen: '=?'
            },
            controller: ['$scope', function ($scope)
                {
                    $scope.showicon = true;
                    this.setHeading = function (element)
                    {
                        this.heading = element;
                        $scope.showicon = false;
                    };
                    this.isIsOpen = function ()
                    {
                        return $scope.isOpen;
                    };
                }],
            link: function (scope, element, attrs, ctrl) {
                var accordionCtrl = ctrl[0];
                var accordionGroupCtrl = ctrl[1];
                var keys = {tab: 9, enter: 13, esc: 27, space: 32, pageup: 33, pagedown: 34, end: 35, home: 36, left: 37, up: 38, right: 39, down: 40};
                //not a fix
                var tab = element.children().eq(0);
                var parentHyperLink=attrs.parentLink;
                scope.setFocus = false;
                scope.childLength = attrs.childLength;
                scope.headingIconClass = attrs.imageSource;

                var handleKeydown = function (ev) {
                    var boolFlag = true;
                    switch (ev.keyCode)
                    {
                        case keys.enter:
                            ev.preventDefault();
                            scope.toggle();
                            scope.$apply();
                            break;
                        case keys.up:
                        case keys.left:
                            ev.preventDefault();
                            accordionCtrl.cycle(scope, false);
                            break;
                        case keys.down:
                        case keys.right:
                            ev.preventDefault();
                            accordionCtrl.cycle(scope, true);
                            break;
                        default:
                            boolFlag = false;
                            break;
                    }
                    ev.stopPropagation();
                    return boolFlag;
                };

                if (angular.isUndefined(scope.isOpen)) {
                    scope.isOpen = false;
                }

                tab.bind("keydown", handleKeydown);

                accordionCtrl.addGroup(scope);

                if (scope.index === 0) {
                    scope.focused = true;
                }

                accordionGroupCtrl.toggle = scope.toggle = function () {
                	/* if the menu item has children, toggle/expand child menu of this item */
                	if (scope.childLength>0) {
                        scope.isOpen = !scope.isOpen;
                        accordionCtrl.focus(scope);
                        return scope.isOpen;
                	}
                	/* if the menu item does not have children, redirect to parent action URL*/
                	else {
                		window.location.href = parentHyperLink;
                	}
                	
                };

                scope.$watch('isOpen', function (value) {
                    if (value) {
                        accordionCtrl.closeOthers(scope);
                    }
                });

                scope.$watch("focused", function (value) {
                    if (!!value) {
                        tab.attr("tabindex", "0");
                        if(scope.setFocus){
                            tab[0].focus();
                        }
                    }
                    else{
                        scope.setFocus = false;
                        tab.attr("tabindex", "-1");
                    }
                });
            }
        };
    }])
//Use accordion-heading below an accordion-group to provide a heading containing HTML
//<accordion-group>
//<accordion-heading>Heading containing HTML - <img src="..."></accordion-heading>
//</accordion-group>
.directive('accordionToggle', function () {
    return{
        restrict: 'EA',
        require: '^accordionGroup',
        scope: {
            expandIcon: '@',
            collapseIcon: '@'
        },
        link: function (scope, element, attr, accordionCtrl)
        {
            var setIcon = function (isOpen) {
                if (scope.expandIcon && scope.collapseIcon)
                {
                    if (isOpen) {
                        element.removeClass(scope.expandIcon);
                        element.addClass(scope.collapseIcon);
                    }
                    else {
                        element.removeClass(scope.collapseIcon);
                        element.addClass(scope.expandIcon);
                    }
                }
            };
            element.bind('click', function ()
            {
                accordionCtrl.toggle();
                scope.$apply();
            });
            scope.$watch(function () {
                return accordionCtrl.isIsOpen();
            }, function (value) {
                setIcon(value);
            });
        }
    };
}).directive('accordionHeading', function () {
return {
restrict: 'EA',
transclude: true,
template: '',
require: '^accordionGroup',
compile: function (element, attr, transclude) {
    var link = function (scope, element, attr, accordionGroupCtrl) {
        // Pass the heading to the accordion-group controller
        // so that it can be transcluded into the right place in the template
        // [The second parameter to transclude causes the elements to be cloned so that they work in ng-repeat]
        transclude(scope, function (clone) {
            element.append(clone);
            accordionGroupCtrl.setHeading(element);
        });
    };
    return link;
}
};
})
// Use in the accordion-group template to indicate where you want the heading to be transcluded
// You must provide the property on the accordion-group controller that will hold the transcluded element
        .directive('accordionTransclude', function () {
            return {
                require: '^accordionGroup',
                link: function (scope, element, attr, controller) {
                    scope.$watch(function () {
                        return controller[attr.accordionTransclude];
                    }, function (heading) {
                        if (heading) {
                            element.find("span").eq(0).prepend(heading);
                        }
                    });
                }
            };
        })
        .directive('attGoTop', ['$scrollTo', function ($scrollTo) {
                return {
                    restrict: 'A',
                    transclude: false,
                    link: function (scope, elem, attrs)
                    {
                        elem.bind('click', function ()
                        {
                            $scrollTo(0, attrs["attGoTop"]);
                        });
                    }
                };
            }])
        .directive('attGoTo', ['$anchorScroll', '$location', function ($anchorScroll, $location) {
                return {
                    restrict: 'A',
                    transclude: false,
                    link: function (scope, elem, attrs)
                    {
                        elem.bind('click', function ()
                        {
                            var newHash = attrs["attGoTo"];
                            if ($location.hash() !== newHash)
                            {
                                $location.hash(attrs["attGoTo"]);
                            }
                            else
                            {
                                $anchorScroll();
                            }
                        });
                    }
                };
            }])
        .directive('freeStanding', function () {
            return {
                restrict: 'EA',
                transclude: true,
                replace: true,
                scope: true,
                template: "<div><span class='att-accordion__freestanding' ng-show='showAccordion'></span>\n" +
                        "<div class='section-toggle'>\n" +
                        "<button class='section-toggle__button' ng-click='fsToggle()'>\n" +
                        "    {{btnText}}<i style='font-size:0.875rem' ng-class='{\"icon-chevron-up\": showAccordion,\"icon-chevron-down\": !showAccordion, }'></i> \n" +
                        "</button>\n" +
                        "</div></div>",
                compile: function (element, attr, transclude)
                {
                    var link = function (scope, elem, attrs) {
                        scope.content = "";
                        transclude(scope, function (clone)
                        {
                            elem.find("span").append(clone);
                        });
                        scope.showAccordion = false;
                        scope.btnText = scope.showAccordion ? attrs.hideMsg : attrs.showMsg;
                        scope.fsToggle = function ()
                        {
                            scope.showAccordion = !scope.showAccordion;
                            scope.btnText = scope.showAccordion ? attrs.hideMsg : attrs.showMsg;
                        };
                    };
                    return link;
                }
            };
        }).directive('expanders', function () {
    return{
        restrict: 'EA',
        replace: true,
        transclude: true,
        template: "<div ng-transclude></div>",
        controller: ['$scope', function ($scope){
                var bodyScope = null;
                this.setScope = function (scope) {
                    bodyScope = scope;
                };
                this.toggle = function () {
                    $scope.isOpen = bodyScope.isOpen = !bodyScope.isOpen;
                    return bodyScope.isOpen;
                };
            }],
        link: function (scope)
        {
            scope.isOpen = false;
        }
    };
}).directive('expanderHeading', function () {
    return{
        require: "^expanders",
        restrict: 'EA',
        replace: true,
        transclude: true,
        scope: true,
        template: "<div style='padding:10px !important' ng-transclude></div>"
    };
}).directive('expanderBody', function () {
    return{
        restrict: 'EA',
        require: "^expanders",
        replace: true,
        transclude: true,
        scope: {},
        template: "<div collapse='!isOpen'><div ng-transclude></div></div>",
        link: function (scope, elem, attr, myCtrl) {
            scope.isOpen = false;
            myCtrl.setScope(scope);
        }
    };
}).directive('expanderToggle', function () {
    return{
        restrict: 'EA',
        require: "^expanders",
        scope: {
            expandIcon: '@',
            collapseIcon: '@'
        },
        link: function (scope, element, attr, myCtrl)
        {
            var isOpen = false;
            var setIcon = function () {
                if (scope.expandIcon && scope.collapseIcon)
                {
                    if (isOpen) {
                        element.removeClass(scope.expandIcon);
                        element.addClass(scope.collapseIcon);
                    }
                    else {
                        element.removeClass(scope.collapseIcon);
                        element.addClass(scope.expandIcon);
                    }
                }
            };
            element.bind("keydown", function (e) {
                if (e.keyCode === 13)
                {
                    scope.toggleit();
                }
            });
            element.bind('click', function ()
            {
                scope.toggleit();
            });
            scope.toggleit = function ()
            {
                isOpen = myCtrl.toggle();
                setIcon();
                scope.$apply();
            };
            setIcon();
        }
    };
}).directive('collapse', ['$transition', function ($transition) {
        // CSS transitions don't work with height: auto, so we have to manually change the height to a
        // specific value and then once the animation completes, we can reset the height to auto.
        // Unfortunately if you do this while the CSS transitions are specified (i.e. in the CSS class
        // "collapse") then you trigger a change to height 0 in between.
        // The fix is to remove the "collapse" CSS class while changing the height back to auto - phew!
        var props = {
            open: {
                marginTop: null,
                marginBottom: null,
                paddingTop: null,
                paddingBottom: null,
                display: 'block'
            },
            closed: {
                marginTop: 0,
                marginBottom: 0,
                paddingTop: 0,
                paddingBottom: 0,
                display: 'none'
            }
        };
        var fixUpHeight = function (scope, element, height) {
            // We remove the collapse CSS class to prevent a transition when we change to height: auto
            element.removeClass('collapse');
            element.css({height: height});
            //adjusting for any margin or padding
            if (height === 0) {
                element.css(props.closed);
            } else {
                element.css(props.open);
            }
            // It appears that  reading offsetWidth makes the browser realise that we have changed the
            // height already :-/
            element.addClass('collapse');
        };
        return {
            link: function (scope, element, attrs) {
                var isCollapsed;
                var initialAnimSkip = true;
                scope.$watch(function () {
                    return element[0].scrollHeight;
                }, function () {
                    //The listener is called when scrollHeight changes
                    //It actually does on 2 scenarios:
                    // 1. Parent is set to display none
                    // 2. angular bindings inside are resolved
                    //When we have a change of scrollHeight we are setting again the correct height if the group is opened
                    if (element[0].scrollHeight !== 0 && !isCollapsed) {
                        if (initialAnimSkip) {
                            fixUpHeight(scope, element, element[0].scrollHeight + 'px');
                        } else {
                            fixUpHeight(scope, element, 'auto');
                        }
                    }
                });
                var currentTransition;
                var doTransition = function (change) {
                    if (currentTransition) {
                        currentTransition.cancel();
                    }
                    currentTransition = $transition(element, change);
                    currentTransition.then(
                            function () {
                                currentTransition = undefined;
                            },
                            function () {
                                currentTransition = undefined;
                            }
                    );
                    return currentTransition;
                };
                var expand = function () {
                    scope.postTransition = true;
                    if (initialAnimSkip) {
                        initialAnimSkip = false;
                        if (!isCollapsed) {
                            fixUpHeight(scope, element, 'auto');
                        }
                    } else {
                        doTransition(angular.extend({height: element[0].scrollHeight + 'px'}, props.open))
                                .then(function () {
                                    // This check ensures that we don't accidentally update the height if the user has closed
                                    // the group while the animation was still running
                                    if (!isCollapsed)
                                    {
                                        fixUpHeight(scope, element, 'auto');
                                    }
                                });
                    }
                    isCollapsed = false;
                };
                var collapse = function () {
                    isCollapsed = true;
                    if (initialAnimSkip) {
                        initialAnimSkip = false;
                        fixUpHeight(scope, element, 0);
                    } else {
                        fixUpHeight(scope, element, element[0].scrollHeight + 'px');
                        doTransition(angular.extend({height: 0}, props.closed)).then(function () {
                            scope.postTransition = false;
                        });
                    }
                };
                scope.$watch(attrs.collapse, function (value) {
                    if (value) {
                        collapse();
                    } else {
                        expand();
                    }
                });
            }
        };
    }])
        .directive('attAccord', function () {
            return {
                restrict: 'EA',
                transclude: true,
                replace: true,
                scope: {},
                controller: 'AttAccordCtrl',
                templateUrl: 'app/scripts/ng_js_att_tpls/accordion/attAccordHeader.html'
            };
        })
        .controller('AttAccordCtrl', [function () {
                this.type = 'attAccord';
                this.headerCtrl;
                this.bodyCtrl;
                var isOpen = true;
                this.toggleBody = function () {
                    if (isOpen) {
                        this.expandBody();
                    }
                    else {
                        this.collapseBody();
                    }
                    isOpen = !isOpen;
                };
                this.expandBody = function () {
                    this.bodyCtrl.expand();
                };
                this.collapseBody = function () {
                    this.bodyCtrl.collapse();
                };
            }])
        .controller('AttAccordHeaderCtrl', [function () {
                this.type = 'header';
            }])
        .directive('attAccordHeader', ['keymap', 'events', function (keymap, events) {
            return {
                restrict: 'EA',
                transclude: true,
                replace: true,
                require: ['^attAccord', 'attAccordHeader'],
                controller: 'AttAccordHeaderCtrl',
                templateUrl: 'app/scripts/ng_js_att_tpls/accordion/attAccordHeader.html',
                link: function (scope, element, attr, ctrls) {
                    var attAccordCtrl = ctrls[0];
                    var attAccordHeaderCtrl = ctrls[1];
                    attAccordCtrl.headerCtrl = attAccordHeaderCtrl;
                    var tab = element.children().eq(0);
                        
                    scope.clickFunc = function () {
                        attAccordCtrl.toggleBody();
                    };

                    var handleKeydown = function (ev) {
                        var boolFlag = true;
                        switch (ev.keyCode)
                        {
                            case keymap.KEY.ENTER:
                                ev.preventDefault();
                                scope.clickFunc();
                                scope.$apply();
                                break;
                            default:
                                boolFlag = false;
                                break;
                        }
                        ev.stopPropagation();
                        return boolFlag;
                    };
                        
                    if (angular.isUndefined(scope.isOpen)) {
                        scope.isOpen = false;
                    }

                    tab.bind("keydown", handleKeydown);
                }
            };
        }])
        .controller('AttAccordBodyCtrl', ['$scope', function ($scope) {
                this.type = 'body';
                this.expand = function () {
                    $scope.expand();
                };
                this.collapse = function () {
                    $scope.collapse();
                };
            }])
        .directive('attAccordBody', ['$timeout', '$height', function ($timeout, $height) {
                return {
                    restrict: 'EA',
                    transclude: true,
                    replace: true,
                    require: ['^attAccord', 'attAccordBody'],
                    controller: 'AttAccordBodyCtrl',
                    templateUrl: 'app/scripts/ng_js_att_tpls/accordion/attAccordBody.html',
                    link: function (scope, element, attr, ctrls) {
                        var attAccordCtrl = ctrls[0];
                        var attAccordBodyCtrl = ctrls[1];
                        attAccordCtrl.bodyCtrl = attAccordBodyCtrl;
                        var originalHeight;
                        $timeout(function () {
                            originalHeight = element[0].offsetHeight;
                            $height(element, 0, 0, 0);
                        });
                        scope.expand = function () {
                            $height(element, 0.05, originalHeight, 1);
                        };
                        scope.collapse = function () {
                            $height(element, 0.25, 0, 0);
                        };
                    }
                };
            }]);
angular.module('att.abs.alert', [])
.directive('attAlert', [function()
{
    return {
        restrict:'EA',
        replace : true,
        transclude : true,
        scope: {
            alertType : "@type",
            showTop : "@topPos",
            showAlert : "="
        },
       templateUrl : 'app/scripts/ng_js_att_tpls/alert/alert.html',
       link: function(scope)
        {
            if(scope.showTop === 'true'){
                scope.cssStyle = {'top':'50px'};
            }
            else{
               scope.cssStyle = {'top':'0px'};
            }
           scope.close = function(){
               scope.showAlert = false;
            };
        }
    };
}]);

angular.module('att.abs.boardStrip', ['att.abs.utilities'])
.constant('BoardStripConfig', {
    'maxVisibleBoards': 4,
    'boardsToScroll': 1,
    /* These parameters are non-configurable and remain unaltered, until there is a change in corresponding SCSS */
    'boardLength': 140,
    'boardMargin': 15
})
.directive('attBoard', [function() {
    return {
        restrict: 'AE',
        replace: true,
        transclude: true,
        require: '^attBoardStrip',
        scope : {
            boardIndex : '=',
            boardLabel : '='
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/boardStrip/attBoard.html',
        link: function(scope, element, attrs, ctrls) {

            var parentCtrl = ctrls;

            scope.getCurrentIndex = function() {
                return parentCtrl.getCurrentIndex();
            };
            scope.selectBoard = function(boardIndex) {
                if (!isNaN(boardIndex)) {
                    parentCtrl.setCurrentIndex(boardIndex);
                }
            };
            scope.isInView = function(boardIndex) {
                return parentCtrl.isInView(boardIndex);
            };
        }
    };
}])
.directive('attBoardStrip', ['BoardStripConfig', '$timeout', '$ieVersion', function(BoardStripConfig, $timeout, $ieVersion) {
    return {
        restrict: 'AE',
        replace: true,
        transclude: true,
        scope: {
            currentIndex: '=selectedIndex',
            boardsMasterArray : '=',
            onAddBoard : '&?'
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/boardStrip/attBoardStrip.html',
        controller: function($scope) {
            if(!angular.isDefined($scope.boardsMasterArray)){
                $scope.boardsMasterArray = [];
            }

            this.rectifyMaxVisibleBoards = function() {
                if (this.maxVisibleIndex >= $scope.boardsMasterArray.length) {
                    this.maxVisibleIndex = $scope.boardsMasterArray.length - 1;
                }

                if (this.maxVisibleIndex < 0) {
                    this.maxVisibleIndex = 0;
                }
            };

            this.resetBoardStrip = function(){
                $scope.currentIndex = 0;

                this.maxVisibleIndex = BoardStripConfig.maxVisibleBoards-1;
                this.minVisibleIndex = 0;

                this.rectifyMaxVisibleBoards();
            };


            if ($scope.currentIndex > 0) {
                var index = $scope.currentIndex;
                this.resetBoardStrip();
                if (index > $scope.boardsMasterArray.length) {
                    $scope.currentIndex = $scope.boardsMasterArray.length-1;
                } else {
                    $scope.currentIndex = index;
                }
            } else {
                this.resetBoardStrip();
            }
            

            this.getCurrentIndex = function() {
                return $scope.currentIndex;
            };
            this.setCurrentIndex = function(indx) {
                $scope.currentIndex = indx;
            };

            this.isInView = function(index) {
                return (index <= this.maxVisibleIndex && index >= this.minVisibleIndex);
            };

            this.getBoardsMasterArrayLength = function() {
                return $scope.boardsMasterArray.length;
            };
        },
        link: function(scope, element, attrs, ctrl) {
			var ieVersion = $ieVersion();

			var oldTimeout;
			var animationTimeout = 1000;
			
			if(ieVersion && ieVersion < 10) {
				animationTimeout = 0;
			}

			var getBoardViewportWidth = function (numberOfVisibleBoards) {
				return numberOfVisibleBoards * (BoardStripConfig.boardLength + BoardStripConfig.boardMargin);
			};
			if(element[0].querySelector(".board-viewport")) {
				angular.element(element[0].querySelector(".board-viewport")).css({"width": getBoardViewportWidth(BoardStripConfig.maxVisibleBoards) + "px"});
			}

			var getBoardstripContainerWidth = function (totalNumberOfBoards) {
				return totalNumberOfBoards * (BoardStripConfig.boardLength + BoardStripConfig.boardMargin);
			};
			if(element[0].querySelector(".boardstrip-container")) {
				angular.element(element[0].querySelector(".boardstrip-container")).css({"width": getBoardstripContainerWidth(ctrl.getBoardsMasterArrayLength()) + "px"});
				angular.element(element[0].querySelector(".boardstrip-container")).css({"left": "0px"});
			}

			var calculateAndGetBoardstripContainerAdjustment = function () {

				var calculatedAdjustmentValue;

				if(ctrl.getBoardsMasterArrayLength() <= BoardStripConfig.maxVisibleBoards) {
					calculatedAdjustmentValue = 0;
				}
				else{
					calculatedAdjustmentValue = (ctrl.minVisibleIndex * (BoardStripConfig.boardLength + BoardStripConfig.boardMargin))* -1;
				}
				
				return calculatedAdjustmentValue;
			};

            var updateBoardsTabIndex = function(boardArray, minViewIndex, maxViewIndex) {
                for (var i = 0; i < boardArray.length; i++) {
                    angular.element(boardArray[i]).attr('tabindex', '-1');
                }
                for (var i = minViewIndex; i <= maxViewIndex; i++) {
                    angular.element(boardArray[i]).attr('tabindex', '0');
                }
            };
			
            scope.$watchCollection('boardsMasterArray', function(newVal, oldVal){
                if(newVal !== oldVal){
					/* When a board is removed */
					if(newVal.length < oldVal.length){
						ctrl.resetBoardStrip();
						$timeout(function(){
							
							var currentBoardArray = element[0].querySelectorAll('[att-board]');
							if(currentBoardArray.length !== 0) {

								var oldContainerAdjustment = angular.element(element[0].querySelector(".boardstrip-container"))[0].style.left;
								var containerAdjustment = calculateAndGetBoardstripContainerAdjustment();
								if(oldContainerAdjustment !== containerAdjustment+'px') {
									angular.element(element[0].querySelector(".boardstrip-container")).css({"left": containerAdjustment + "px"});

									$timeout.cancel(oldTimeout);
									oldTimeout = $timeout(function(){
										currentBoardArray[0].focus();
									}, animationTimeout);
								}
								else{
									currentBoardArray[0].focus();
								}
							}
							else{
								element[0].querySelector('div.boardstrip-item--add').focus();
							}
							
							angular.element(element[0].querySelector(".boardstrip-container")).css({"width": getBoardstripContainerWidth(ctrl.getBoardsMasterArrayLength()) + "px"});
						});
					}
					/* When a board is added */
					else {
						ctrl.maxVisibleIndex = ctrl.getBoardsMasterArrayLength()-1;
						ctrl.minVisibleIndex = Math.max(ctrl.maxVisibleIndex - BoardStripConfig.maxVisibleBoards + 1, 0);

						ctrl.setCurrentIndex(ctrl.maxVisibleIndex);

						$timeout(function(){
							angular.element(element[0].querySelector(".boardstrip-container")).css({"width": getBoardstripContainerWidth(ctrl.getBoardsMasterArrayLength()) + "px"});
							
							var oldContainerAdjustment = angular.element(element[0].querySelector(".boardstrip-container"))[0].style.left;
							var containerAdjustment = calculateAndGetBoardstripContainerAdjustment();
							var currentBoardArray = element[0].querySelectorAll('[att-board]');
							if(oldContainerAdjustment !== containerAdjustment+'px') {
								angular.element(element[0].querySelector(".boardstrip-container")).css({"left": containerAdjustment + "px"});
								
								$timeout.cancel(oldTimeout);
								oldTimeout = $timeout(function(){
									currentBoardArray[currentBoardArray.length-1].focus();
								}, animationTimeout);
							}
							else{
								currentBoardArray[currentBoardArray.length-1].focus();
							}
                            /* Update tabindecies to ensure keyboard navigation behaves correctly */
                            updateBoardsTabIndex(currentBoardArray, ctrl.minVisibleIndex, ctrl.maxVisibleIndex);
						});
					}
                }
            });

            scope.nextBoard = function() {
                ctrl.maxVisibleIndex += BoardStripConfig.boardsToScroll;
                ctrl.rectifyMaxVisibleBoards();
                ctrl.minVisibleIndex = ctrl.maxVisibleIndex - (BoardStripConfig.maxVisibleBoards-1);

				$timeout.cancel(oldTimeout);
				angular.element(element[0].querySelector(".boardstrip-container")).css({"left": calculateAndGetBoardstripContainerAdjustment() + "px"});

                $timeout(function(){
                    var currentBoardArray = element[0].querySelectorAll('[att-board]');

                    /* Remove tabindex from non-visible boards */
                    updateBoardsTabIndex(currentBoardArray, ctrl.minVisibleIndex, ctrl.maxVisibleIndex);

                    if (!(scope.isNextBoard())) {
                        try {
                            currentBoardArray[currentBoardArray.length-1].focus();
                        } catch(e) {}
                    }
                }, animationTimeout);
            };
            scope.prevBoard = function() {

                ctrl.minVisibleIndex -= BoardStripConfig.boardsToScroll;
                if (ctrl.minVisibleIndex < 0) {
                    ctrl.minVisibleIndex = 0;
                }

                ctrl.maxVisibleIndex = ctrl.minVisibleIndex + BoardStripConfig.maxVisibleBoards-1;
                ctrl.rectifyMaxVisibleBoards();

				$timeout.cancel(oldTimeout);
				angular.element(element[0].querySelector(".boardstrip-container")).css({"left": calculateAndGetBoardstripContainerAdjustment() + "px"});

                $timeout(function(){
                    var currentBoardArray = element[0].querySelectorAll('[att-board]');

                    /* Remove tabindex from non-visible boards */
                    updateBoardsTabIndex(currentBoardArray, ctrl.minVisibleIndex, ctrl.maxVisibleIndex);

                    if (ctrl.minVisibleIndex === 0) {
                        try {
                            element[0].querySelector('div.boardstrip-item--add').focus();
                        } catch (e) {} /* IE8 may throw exception */
                    }
                });
            };

            scope.isPrevBoard = function() {
                return (ctrl.minVisibleIndex > 0);
            };
            scope.isNextBoard = function() {
                return (ctrl.getBoardsMasterArrayLength()-1 > ctrl.maxVisibleIndex);
            };
        }
    };
}])
.directive('attAddBoard', ['BoardStripConfig', '$parse', '$timeout', function(BoardStripConfig, $parse, $timeout) {
    return {
        restrict: 'AE',
        replace: true,
        require: '^attBoardStrip',
        scope : {
            onAddBoard : '&?'
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/boardStrip/attAddBoard.html',
        link: function(scope, element, attrs, ctrls) {
            var parentCtrl = ctrls;
            scope.addBoard = function() {
                if (attrs['onAddBoard'] ) {
                    scope.onAddBoard = $parse(scope.onAddBoard);
                    scope.onAddBoard();
                }
            };
        }
    };
}])
.directive('attBoardNavigation', ['keymap', 'events', function(keymap, events) {
    return {
        restrict: 'AE',
        link: function(scope, elem) {

            var prevElem = keymap.KEY.LEFT;
            var nextElem = keymap.KEY.RIGHT;

            elem.bind('keydown', function (ev) {

                if (!(ev.keyCode)) {
                    ev.keyCode = ev.which;
                }

                switch (ev.keyCode) {
                case nextElem:
                    events.preventDefault(ev);
                    events.stopPropagation(ev);

                    if (elem[0].nextElementSibling && parseInt(angular.element(elem[0].nextElementSibling).attr('tabindex')) >= 0) {
                        angular.element(elem[0])[0].nextElementSibling.focus();
                    } else {
                        /* IE8 fix */
                        var el = angular.element(elem[0])[0];
                        do {
                            if (el.nextSibling){
                                el = el.nextSibling;
                            }
                            else{
                                break;
                            }
                        } while (el && el.tagName !== 'LI');

                        if (el.tagName && el.tagName === 'LI' && parseInt(angular.element(el).attr('tabindex')) >= 0){
                            el.focus();
                        }
                    }

                    break;
                case prevElem:
                    events.preventDefault(ev);
                    events.stopPropagation(ev);

                    if (elem[0].previousElementSibling && parseInt(angular.element(elem[0].previousElementSibling).attr('tabindex')) >= 0) {
                        angular.element(elem[0])[0].previousElementSibling.focus();
                    } else {
                        /* IE8 fix */
                        var el1 = angular.element(elem[0])[0];
                        do {
                            if (el1.previousSibling){
                                el1 = el1.previousSibling;
                            }
                            else{
                                break;
                            }
                        } while (el1 && el1.tagName !== 'LI');

                        if (el1.tagName && el1.tagName === 'LI' && parseInt(angular.element(el).attr('tabindex')) >= 0){
                            el1.focus();
                        }
                    }
                    break;
                default:
                    break;
                }
            });
        }
    };
}]);

angular.module('att.abs.breadCrumbs', [])
    .constant("classConstant",{
            "defaultClass" : "breadcrumbs__link",
            "activeClass": "breadcrumbs__link--active"
        })
    .directive('attCrumb', ['classConstant', function(classConstant) {
        return {
            restrict: 'A',
            link: function(scope, elem, attr) {
                elem.addClass(classConstant.defaultClass);
                if(attr.attCrumb === 'active'){
                     elem.addClass(classConstant.activeClass);
                }
               if(!elem.hasClass('last')){
                   elem.after('<i class="breadcrumbs__item"></i>');
               }
            }
        };
    }
]);
angular.module('att.abs.buttons', ['att.abs.position', 'att.abs.utilities'])
        .constant('btnConfig', {
            btnClass: 'button',
            btnPrimaryClass: 'button--primary',
            btnSecondaryClass: 'button--secondary',
            btnDisabledClass: 'button--inactive',
            btnSmallClass: 'button--small'
        })
        .directive('attButton', ['btnConfig', function (btnConfig) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        element.addClass(btnConfig.btnClass);
                        if (attrs.size === 'small') {
                            element.addClass(btnConfig.btnSmallClass);
                        }
                        attrs.$observe('btnType', function (value) {
                            if (value === 'primary') {
                                element.addClass(btnConfig.btnPrimaryClass);
                                element.removeClass(btnConfig.btnSecondaryClass);
                                element.removeClass(btnConfig.btnDisabledClass);
                                element.removeAttr('disabled');
                            } else if (value === 'secondary') {
                                element.addClass(btnConfig.btnSecondaryClass);
                                element.removeClass(btnConfig.btnPrimaryClass);
                                element.removeClass(btnConfig.btnDisabledClass);
                                element.removeAttr('disabled');
                            } else if (value === 'disabled') {
                                element.addClass(btnConfig.btnDisabledClass);
                                element.removeClass(btnConfig.btnPrimaryClass);
                                element.removeClass(btnConfig.btnSecondaryClass);
                                element.attr('disabled', 'disabled');
                            }
                        });
                    }
                };
            }])
        .directive('attButtonLoader', [function () {
                return {
                    restrict: 'A',
                    replace: false,
                    scope: {
                        size: '@'
                    },
                    template: '<div ng-class="{\'button--loading\': size === \'large\',\'button--loading__small\': size === \'small\'}"><i></i><i class="second__loader"></i><i></i></div>',
                    link: function (scope, element) {
                        element.addClass('button button--inactive');
                    }
                };
            }])
        .directive('attButtonHero', [function () {
                return {
                    restrict: 'A',
                    replace: false,
                    transclude: true,
                    scope: {
                        icon: '@'
                    },
                    template: '<div class="button--hero__inner"><span ng-transclude></span> <i ng-class="{\'icon-arrow-right\': icon === \'arrow-right\',\'icon-cart\': icon === \'cart\'}"></i></div>',
                    link: function (scope, element) {
                        element.addClass('button button--hero');
                        element.attr("tabindex", "0");
                    }
                };
            }])
        .directive('attBtnDropdown', ['$document', '$timeout', '$isElement', '$documentBind', 'keymap', 'events', function ($document, $timeout, $isElement, $documentBind, keymap, events) {
                return {
                    restrict: 'EA',
                    scope: {
                        type: "@dropdowntype"
                    },
                    replace: true,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/buttons/buttonDropdown.html',
                    link: function (scope, element) {
                        scope.isOpen = false;
                        var currentIndex = -1;
                        // Capture all the li elements after compilation
                        var list = [], button = undefined;
                        $timeout(function() {
                            list = element.find('li');
                            button = element.find('button')[0];
                        }, 10);
                        var toggle = scope.toggle = function (show) {
                            if (angular.isUndefined(show) || show === '') {
                                scope.isOpen = !scope.isOpen;
                            }
                            else {
                                scope.isOpen = show;
                            }
                        };
                        var selectNext = function() {
                            if (currentIndex+1 < list.length) {
                                currentIndex++;
                                list[currentIndex].focus();
                            }
                        };
                        var selectPrev = function() {
                            if (currentIndex-1 >= 0) {
                                currentIndex--;
                                list[currentIndex].focus();
                            }
                        };
                        element.bind("keydown", function($event) {
                            var keyCode = $event.keyCode;
                            if (keymap.isAllowedKey(keyCode) || keymap.isControl($event) || keymap.isFunctionKey($event)) {
                                switch (keyCode) {
                                    case keymap.KEY.ENTER:
                                        if (currentIndex > 0) {
                                            button.focus();
                                            scope.$apply();
                                        }
                                        break;
                                    case keymap.KEY.ESC:
                                        toggle(false);
                                        currentIndex = -1;
                                        button.focus();
                                        scope.$apply();
                                        break;
                                    case keymap.KEY.DOWN:
                                        selectNext();
                                        scope.$apply();
                                        events.preventDefault($event);
                                        events.stopPropagation($event);
                                        break;
                                    case keymap.KEY.UP:
                                        selectPrev();
                                        scope.$apply();
                                        events.preventDefault($event);
                                        events.stopPropagation($event);
                                        break;
                                    default:
                                        break;
                                }
                            } else if (keyCode === keymap.KEY.TAB) {
                                toggle(false);
                                currentIndex = -1;
                                scope.$apply();
                            }
                        });
                        var outsideClick = function (e) {
                            var isElement = $isElement(angular.element(e.target), element, $document);
                            if (!isElement) {
                                toggle(false);
                                currentIndex = -1;
                                for (var i = 0; i < list.length; i++) {
                                    angular.element(list[i]).removeClass('selected');
                                }
                                button.focus();
                                scope.$apply();
                            }
                        };
                        $documentBind.click('isOpen', outsideClick, scope);
                    }
                };
            }]);
angular.module('att.abs.checkbox', [])
.constant("attCheckboxConfig", {
    activeClass : "att-checkbox--on",
    disabledClass : "att-checkbox--disabled"
})
.directive('checkboxLimit', function () {
return {
    scope: {
        checkboxLimit:'=',
        selectLimit:'@?',
        maxSelected:'&?'
    },
    restrict: 'A',
    require:'checkboxLimit',
    controller: ['$scope',function($scope)
    {
        $scope.limit=true;
        this.getMaxLimits=function(){
            return $scope.limit;
        };
        this.setMaxLimits=function(value){
            $scope.limit=value;
        };
        this.maxCheckboxSelected=function(){
            $scope.maxSelected();
        };
    }],
    link: function (scope, element, attribute, ctrl) {
        scope.$watch('checkboxLimit', function()
        {
            var countTrue = 0;
            for (var keys in scope.checkboxLimit) {
                if (scope.checkboxLimit.hasOwnProperty(keys) && scope.checkboxLimit[keys]) {
                        countTrue = countTrue + 1;
                }
            };
            if(countTrue>=parseInt(scope.selectLimit)){
                ctrl.setMaxLimits(false);
            }
            else{
                ctrl.setMaxLimits(true);
            }
            }, true);
    }
};
})
.directive('attCheckbox', ['$compile', "attCheckboxConfig", function ($compile, attCheckboxConfig) {
    return {
        scope: {},
        restrict: 'A',
        require: ['ngModel','^?checkboxLimit'],
        link: function (scope, element, attribute, ctrl) {
            var ngCtrl = ctrl[0];
            var checkboxLimitCtrl = ctrl[1];
            var parentDiv = $compile('<div tabindex="0" role="checkbox" att-accessibility-click="13,32" aria-label="Checkbox" ng-click="updateModel($event)" class="att-checkbox"></div>')(scope);
            element.css({display:'none'});
            element.wrap(parentDiv);
            element.parent().append('<div class="att-checkbox__indicator"></div>');
            element.parent().attr("title", attribute.title);
            element.parent().attr("aria-label", attribute.title);
            element.parent().attr("id", attribute.id);
            element.removeAttr("id");
            //element.removeAttr("title");
            //model -> UI
            ngCtrl.$render = function () {
                var selected = ngCtrl.$modelValue ? true : false;
                element.parent().toggleClass(attCheckboxConfig.activeClass, selected);
                element.parent().attr("aria-checked", selected);
            };

            //ui->model
            scope.updateModel = function (evt) {
                if (!scope.disabled) {
                    ngCtrl.$setViewValue(element.parent().hasClass(attCheckboxConfig.activeClass) ? false : true);
                    if(checkboxLimitCtrl && !(checkboxLimitCtrl.getMaxLimits())){
                        if(!ngCtrl.$modelValue){
                            ngCtrl.$render();
                        }
                        else{
                            checkboxLimitCtrl.maxCheckboxSelected();
                            ngCtrl.$setViewValue(element.parent().hasClass(attCheckboxConfig.activeClass) ? true : false);
                        }
                    }
                    else{
                        ngCtrl.$render();
                    }
                }
                evt.preventDefault();
            };

            attribute.$observe('disabled', function(val) {
                scope.disabled = (val || val === "disabled" || val === "true");
                element.parent().toggleClass(attCheckboxConfig.disabledClass, scope.disabled);
                element.parent().attr("tabindex", scope.disabled ? "-1" : "0");
            });
        }
    };
}])
.directive('checkboxGroup', ['$compile',function($compile) {
    return {
        scope:{
            checkboxGroup: "=",
            checkboxGroupValue: "=?"
        },
        restrict: 'A',
        link: function(scope, element, attribute){
                scope.checkboxState = 'none';
                if (scope.checkboxGroupValue === undefined) {
                    scope.checkboxGroupValue = "indeterminate";
                }
                element.css({display:'none'});
                element.wrap($compile('<div tabindex="0" role="checkbox" att-accessibility-click="13,32" ng-click="updateModel($event)" class="att-checkbox"></div>')(scope));
                element.parent().append('<div class="att-checkbox__indicator"></div>');
                element.parent().attr("title", attribute.title);
                element.parent().attr("aria-label", attribute.title);
                scope.$watch('checkboxState', function(val) {
                    if (val === 'all') {
                        element.parent().addClass('att-checkbox--on');
                        element.parent().removeClass('att-checkbox--indeterminate');
                        element.parent().attr("aria-checked", true);
                    }
                    else if (val === 'none') {
                        element.parent().removeClass('att-checkbox--on');
                        element.parent().removeClass('att-checkbox--indeterminate');
                        element.parent().attr("aria-checked", false);
                    }
                    else if (val === 'indeterminate') {
                        element.parent().removeClass('att-checkbox--on');
                        element.parent().addClass('att-checkbox--indeterminate');
                        element.parent().attr("aria-checked", true);
                    }
                });
                scope.updateModel = function(evt){
                    if (element.parent().hasClass('att-checkbox--on')) {
                            element.parent().removeClass('att-checkbox--on');
                            for (var keys in scope.checkboxGroup) {
                                if (scope.checkboxGroup.hasOwnProperty(keys)) {
                                    scope.checkboxGroup[keys] = false;
                                }
                            };
                            }
                    else {
                        element.parent().addClass('att-checkbox--on');
                        for (var key in scope.checkboxGroup) {
                            if (scope.checkboxGroup.hasOwnProperty(key)) {
                                scope.checkboxGroup[key] = true;
                            }
                        };
                    }
                    evt.preventDefault();
                };
                scope.$watch('checkboxGroupValue', function (value) {
                    if (value===false) {
                            element.parent().removeClass('att-checkbox--on');
                            for (var keys in scope.checkboxGroup) {
                                if (scope.checkboxGroup.hasOwnProperty(keys)) {
                                        scope.checkboxGroup[keys] = false;
                                }
                            };
                            }
                    else if (value === true){
                        element.parent().addClass('att-checkbox--on');
                        for (var key in scope.checkboxGroup) {
                            if (scope.checkboxGroup.hasOwnProperty(key)) {
                                    scope.checkboxGroup[key] = true;
                            }
                        };
                    }
                });
            scope.$watch('checkboxGroup', function(){
                var countTrue = 0;
                var countFalse = 0;
                var count = 0;
                for (var keys in scope.checkboxGroup) {
                    if (scope.checkboxGroup.hasOwnProperty(keys)) {
                        count = count + 1;
                        if (scope.checkboxGroup[keys]) {
                            countTrue = countTrue + 1;
                        }
                        else if (!scope.checkboxGroup[keys]) {
                            countFalse = countFalse + 1;
                        }
                    }
                };
                if (count === countTrue) {
                    scope.checkboxState = "all";
                     scope.checkboxGroupValue=true;
                }
                else if (count === countFalse) {
                    scope.checkboxState = "none";
                    scope.checkboxGroupValue=false;
                }
                else {
                    scope.checkboxState = "indeterminate";
                    scope.checkboxGroupValue="indeterminate";
                }
                }, true);
        }
    };
}]);

angular.module('att.abs.colorselector', [])
    .directive('colorSelectorWrapper', [function() {
        return {
            scope: {
                selected: '=',
                iconColor: '@'
                
            },
            restrict: 'AE',
            transclude: true,
            templateUrl: 'app/scripts/ng_js_att_tpls/colorselector/colorselector.html',
            link: function(scope) {
                scope.applycolor = {'background-color': scope.iconColor};
                scope.selectedcolor = function(iconColor) {
                    scope.selected = iconColor;
                };
            }
        };
     }])
    .directive('colorSelector', ['$compile', function($compile) {
        return{
            restrict: 'A',
            scope: {
                colorSelector: '@',                
                ngModel: '='
            },
            link: function(scope, element, attr) {
                element.removeAttr('color-selector');
                var colorTitle = attr.title;
                var wrapcont = angular.element('<color-selector-wrapper selected="ngModel" title="' + colorTitle + '" icon-color="{{colorSelector}}">' + element.prop('outerHTML') + '</color-selector-wrapper>');
                var newWrapcont = $compile(wrapcont)(scope);
                element.replaceWith(newWrapcont);
            }
        };
    }]);
angular.module('att.abs.datepicker', ['att.abs.position', 'att.abs.utilities'])

.constant('datepickerConfig', {
    dateFormat: 'MM/dd/yyyy',
    dayFormat: 'd',
    monthFormat: 'MMMM',
    yearFormat: 'yyyy',
    dayHeaderFormat: 'EEEE',
    dayTitleFormat: 'MMMM yyyy',
    disableWeekend: false,
    disableSunday: false,
    startingDay: 0,
    minDate: null,
    maxDate: null,
    mode: 0,
    dateFilter: {
        defaultText: 'Select from list'
    },
    datepickerEvalAttributes: ['dateFormat', 'dayFormat', 'monthFormat', 'yearFormat', 'dayHeaderFormat', 'dayTitleFormat', 'disableWeekend', 'disableSunday', 'startingDay', 'mode'],
    datepickerWatchAttributes: ['min', 'max']
})

.factory('datepickerService', ['datepickerConfig', 'dateFilter', function (datepickerConfig, dateFilter) {
    var setAttributes = function (attr, elem) {
        if (angular.isDefined(attr) && attr !== null && angular.isDefined(elem) && elem !== null) {
            var attributes = datepickerConfig.datepickerEvalAttributes.concat(datepickerConfig.datepickerWatchAttributes);
            for (var key in attr) {
                var val = attr[key];
                if (attributes.indexOf(key) !== -1 && angular.isDefined(val)) {
                    elem.attr(key.toSnakeCase(), key);
                }
            }
        }
    };

    var bindScope = function (attr, scope) {
        if (angular.isDefined(attr) && attr !== null && angular.isDefined(scope) && scope !== null) {
            var evalFunction = function (key, val) {
                scope[key] = scope.$parent.$eval(val);
            };

            var watchFunction = function (key, val) {
                scope.$parent.$watch(val, function (value) {
                    scope[key] = value;
                });
                scope.$watch(key, function (value) {
                    scope.$parent[val] = value;
                });
            };

            var evalAttributes = datepickerConfig.datepickerEvalAttributes;
            var watchAttributes = datepickerConfig.datepickerWatchAttributes;
            for (var key in attr) {
                var val = attr[key];
                if (evalAttributes.indexOf(key) !== -1 && angular.isDefined(val)) {
                    evalFunction(key, val);
                } else if (watchAttributes.indexOf(key) !== -1 && angular.isDefined(val)) {
                    watchFunction(key, val);
                }
            }
        }
    };

    var validateDateString = function (dateString, dateFormat) {
        if (dateString && dateFormat) {
            var delimiter;
            if (dateFormat.indexOf('/') !== -1) {
                delimiter = '/';
            } else if (dateFormat.indexOf('-') !== -1) {
                delimiter = '-';
            } else if (dateFormat.indexOf('.') !== -1) {
                delimiter = '.';
            }

            var dateStringArray = dateString.split(delimiter);
            var dateFormatArray = dateFormat.split(delimiter);
            if (dateStringArray.length !== dateFormatArray.length) {
                return false;
            }

            for (var i = 0; i < dateStringArray.length; i++) {
                dateStringArray[i] = dateStringArray[i].lPad(dateFormatArray[i].length, '0');
            }
            var intermediateDateString = dateStringArray.join(delimiter);

            var actualDateString = dateFilter(new Date(intermediateDateString), dateFormat);
            return intermediateDateString === actualDateString;
        }
    };

    return {
        setAttributes: setAttributes,
        bindScope: bindScope,
        validateDateString: validateDateString
    };
}])

.controller('DatepickerController', ['$scope', '$attrs', 'dateFilter', 'datepickerConfig', function($scope, $attrs, dateFilter, dtConfig) {
    var format = {
        date: getValue($attrs.dateFormat, dtConfig.dateFormat),
        day: getValue($attrs.dayFormat, dtConfig.dayFormat),
        month: getValue($attrs.monthFormat, dtConfig.monthFormat),
        year: getValue($attrs.yearFormat, dtConfig.yearFormat),
        dayHeader: getValue($attrs.dayHeaderFormat, dtConfig.dayHeaderFormat),
        dayTitle: getValue($attrs.dayTitleFormat, dtConfig.dayTitleFormat),
        disableWeekend: getValue($attrs.disableWeekend, dtConfig.disableWeekend),
        disableSunday: getValue($attrs.disableSunday, dtConfig.disableSunday)
    },
    startingDay = getValue($attrs.startingDay, dtConfig.startingDay);
    $scope.mode = getValue($attrs.mode, dtConfig.mode);

    $scope.minDate = dtConfig.minDate ? $scope.resetTime(dtConfig.minDate) : null;
    $scope.maxDate = dtConfig.maxDate ? $scope.resetTime(dtConfig.maxDate) : null;

    function getValue(value, defaultValue) {
        return angular.isDefined(value) ? $scope.$parent.$eval(value) : defaultValue;
    }

    function getDaysInMonth(year, month) {
        return new Date(year, month, 0).getDate();
    }

    function getDates(startDate, n) {
        var dates = [];
        var current = startDate, i = 0;
        while (i < n) {
            dates[i++] = new Date(current);
            current.setDate(current.getDate() + 1);
        }
        return dates;
    }

    var compare = this.compare = function(date1, date2) {
        return (new Date(date1.getFullYear(), date1.getMonth(), date1.getDate()) - new Date(date2.getFullYear(), date2.getMonth(), date2.getDate()));
    };

    function isSelected(dt) {
        if (dt && angular.isDate($scope.currentDate) && compare(dt, $scope.currentDate) === 0) {
            return true;
        }
        return false;
    }

    function isFromDate(dt) {
        if (dt && angular.isDate($scope.fromDate) && compare(dt, $scope.fromDate) === 0) {
            return true;
        }
        return false;
    }

    function isToDate(dt) {
        if (dt && angular.isDate($scope.fromDate) && angular.isDate($scope.currentDate) && compare(dt, $scope.currentDate) === 0) {
            return true;
        }
        return false;
    }

    function isDateRange(dt) {
        if (dt && angular.isDate($scope.fromDate) && angular.isDate($scope.currentDate) && (compare(dt, $scope.fromDate) >= 0) && (compare(dt, $scope.currentDate) <= 0)) {
            return true;
        }
        return false;
    }

    function isWeekend(date) {
        if (dateFilter(date, format.dayHeader) === "Saturday" || dateFilter(date, format.dayHeader) === "Sunday") {
            return true;
        }
        return false;
    }

    function isToday(date) {
        if (compare(date, $scope.resetTime(new Date())) === 0) {
            return true;
        }
        return false;
    }
    function isFocused(date) {
        if (date && angular.isDate($scope.focusedDate) && compare(date, $scope.focusedDate) === 0) {
            return true;
        }
        return false;
    }

    var isDisabled = this.isDisabled = function(date) {
        if (format.disableWeekend === true && (dateFilter(date, format.dayHeader) === "Saturday" || dateFilter(date, format.dayHeader) === "Sunday")) {
            return true;
        }
        if (format.disableSunday === true && (dateFilter(date, format.dayHeader) === "Sunday")) {
            return true;
        }
        return (($scope.minDate && compare(date, $scope.minDate) < 0) || ($scope.maxDate && compare(date, $scope.maxDate) > 0));
    };


    function isMinDateAvailable(startDate, endDate) {
       return ($scope.minDate && $scope.minDate.getTime() >= startDate.getTime()) && ($scope.minDate.getTime() <= endDate.getTime());
    }

    function isMaxDateAvailable(startDate, endDate) {
        return ($scope.maxDate && $scope.maxDate.getTime() >= startDate.getTime()) && ($scope.maxDate.getTime() <= endDate.getTime());
    }

    function getLabel(label) {
        if (label)
        {
            var labelObj = {
                pre: label.substr(0, 3),
                post: label
            };
            return labelObj;
        }
        return;
    }
    function makeDate(dateobj) {
        return {date: dateobj.date, label: dateFilter(dateobj.date, dateobj.formatDay), header: dateFilter(dateobj.date, dateobj.formatHeader), focused: !!dateobj.isFocused, selected: !!dateobj.isSelected, from: !!dateobj.isFromDate, to: !!dateobj.isToDate, dateRange: !!dateobj.isDateRange, oldMonth: !!dateobj.oldMonth, nextMonth: !!dateobj.newMonth, disabled: !!dateobj.isDisabled, today: !!dateobj.isToday, weekend: !!dateobj.isWeakend};
    }

    this.modes = [
        {
            name: 'day',
            getVisibleDates: function(date, calendar) {
                var year = date.getFullYear(), month = date.getMonth(), firstDayOfMonth = new Date(year, month, 1), lastDayOfMonth = new Date(year, month+1, 0);
                var difference = startingDay - firstDayOfMonth.getDay(),
                        numDisplayedFromPreviousMonth = (difference > 0) ? 7 - difference : -difference,
                        firstDate = new Date(firstDayOfMonth), numDates = 0;

                if (numDisplayedFromPreviousMonth > 0) {
                    firstDate.setDate(-numDisplayedFromPreviousMonth + 1);
                    numDates += numDisplayedFromPreviousMonth; // Previous
                }
                numDates += getDaysInMonth(year, month + 1); // Current
                numDates += (7 - numDates % 7) % 7; // Next

                var days = getDates(firstDate, numDates), labels = [];
                for (var i = 0; i < numDates; i++) {
                    var dt = new Date(days[i]);
                    days[i] = makeDate({date:dt,
                                formatDay:format.day,
                                formatHeader:format.dayHeader,
                                isFocused:isFocused(dt),
                                isSelected:isSelected(dt),
                                isFromDate:isFromDate(dt),
                                isToDate:isToDate(dt),
                                isDateRange:isDateRange(dt),
                                oldMonth:(new Date(dt.getFullYear(), dt.getMonth(), 1, 0, 0, 0).getTime() < new Date(year, month, 1, 0, 0, 0).getTime()),
                                newMonth:(new Date(dt.getFullYear(), dt.getMonth(), 1, 0, 0, 0).getTime() > new Date(year, month, 1, 0, 0, 0).getTime()),
                                isDisabled:isDisabled(dt),
                                isToday:isToday(dt),
                                isWeakend:isWeekend(dt)});
                }
                for (var j = 0; j < 7; j++) {
                    labels[j] = getLabel(dateFilter(days[j].date, format.dayHeader));
                }
                if (calendar === 'top') {
                    $scope.disablePrevTop = isMinDateAvailable(firstDayOfMonth, lastDayOfMonth);
                    $scope.disableNextTop = isMaxDateAvailable(firstDayOfMonth, lastDayOfMonth);
                } else if (calendar === 'bottom') {
                    $scope.disablePrevBottom = isMinDateAvailable(firstDayOfMonth, lastDayOfMonth);
                    $scope.disableNextBottom = isMaxDateAvailable(firstDayOfMonth, lastDayOfMonth);
                } else {
                    $scope.disablePrevTop = $scope.disablePrevBottom = isMinDateAvailable(firstDayOfMonth, lastDayOfMonth);
                    $scope.disableNextTop = $scope.disableNextBottom = isMaxDateAvailable(firstDayOfMonth, lastDayOfMonth);
                }
                $scope.disablePrev = $scope.disablePrevTop || $scope.disablePrevBottom;
                $scope.disableNext = $scope.disableNextTop || $scope.disableNextBottom;
                return {objects: days, title: dateFilter(date, format.dayTitle), labels: labels};
            },
            split: 7,
            step: {months: 1}
        },
        {
            name: 'month',
            getVisibleDates: function(date) {
                var months = [], labels = [], year = date.getFullYear();
                for (var i = 0; i < 12; i++) {
                   var dt = new Date(year,i,1);
                    months[i] = makeDate({date:dt,
                                formatDay:format.month,
                                formatHeader:format.month,
                                isFocused:isFocused(dt),
                                isSelected:isSelected(dt),
                                isFromDate:isFromDate(dt),
                                isToDate:isToDate(dt),
                                isDateRange:isDateRange(dt),
                                oldMonth:false,
                                newMonth:false,
                                isDisabled:isDisabled(dt),
                                isToday:isToday(dt),
                                isWeakend:false});
                }
                return {objects: months, title: dateFilter(date, format.year), labels: labels};
            },
            split:3,
            step: {years: 1}
        }
    ];

}])

.directive('datepicker', ['$timeout', function ($timeout) {
    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/datepicker/datepicker.html',
        scope: {
            currentDate: "=?current",
            fromDate: "=?from"
        },
        require: 'datepicker',
        controller: 'DatepickerController',
        link: function(scope, element, attrs, ctrl) {
            var datepickerCtrl = ctrl;
            var selected, calendarSelected = false;
            scope.focusedDate;

            scope.resetTime = function(date) {
                var dt;
                if (!isNaN(new Date(date))) {
                    dt = new Date(date);
                    if(scope.mode === 1){
                        dt = new Date(dt.getFullYear(), dt.getMonth());
                    }else{
                        dt = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate());
                    }
                } else {
                    return null;
                }
                return dt;
            };

            if (attrs.min) {
                scope.$parent.$watch(attrs.min, function(value) {
                    scope.minDate = value ? scope.resetTime(value) : null;
                    refill();
                });
            }
            if (attrs.max) {
                scope.$parent.$watch(attrs.max, function(value) {
                    scope.maxDate = value ? scope.resetTime(value) : null;
                    refill();
                });
            }

            // Split array into smaller arrays
            function split(arr, size) {
                var arrays = [];
                while (arr.length > 0) {
                    arrays.push(arr.splice(0, size));
                }
                return arrays;
            }
            var moveMonth = function(selectedDate, direction) {
                var step = datepickerCtrl.modes[scope.mode].step;
                selectedDate.setDate(1);
                selectedDate.setMonth(selectedDate.getMonth() + direction * (step.months || 0));
                selectedDate.setFullYear(selectedDate.getFullYear() + direction * (step.years || 0));

                return selectedDate;
            };

            function refill(date) {
                if (angular.isDate(date) && !isNaN(date)) {
                    selected = new Date(date);
                } else {
                    if (!selected) {
                        selected = new Date();
                    }
                }

                if (selected) {
                    var selectedCalendar;
                    if(scope.mode === 1){
                        selected = new Date();
                        selectedCalendar = moveMonth(angular.copy(selected), -1);
                    } else {
                        selectedCalendar = angular.copy(selected);
                    }

                    var currentMode = datepickerCtrl.modes[scope.mode];
                    var currentData = currentMode.getVisibleDates(selectedCalendar, 'top');
                    scope.currentRows = split(currentData.objects, currentMode.split);
                    scope.currentTitle = currentData.title;
                    scope.labels = currentData.labels || [];

                    var nextData = currentMode.getVisibleDates(moveMonth(angular.copy(selectedCalendar), 1), 'bottom');
                    scope.nextRows = split(nextData.objects, currentMode.split);
                    scope.nextTitle = nextData.title;
                }
            }

            var selectCurrentDate = function(date) {
                var dt = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                scope.currentDate = dt;
            };

            var selectFromDate = function(date) {
                var dt = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                scope.fromDate = dt;
            };

            scope.select = function(date) {
                calendarSelected = true;
                if(attrs.from) {
                    if(!(angular.isDate(scope.fromDate) && angular.isDate(scope.currentDate))) {
                        if(angular.isDate(scope.fromDate)) {
                            selectCurrentDate(date);
                        } else if(!angular.isDate(scope.fromDate)) {
                            selectFromDate(date);
                        }
                    }
                } else {
                    selectCurrentDate(date);
                }
                scope.focusedDate = date;
            };

            var swapDate = function(fromDate, currentDate) {
                selectFromDate(currentDate);
                $timeout(function () {
                    calendarSelected = true;
                    scope.focusedDate = currentDate;
                    selectCurrentDate(fromDate);
                });
            };

            scope.move = function(direction) {
                selected = moveMonth(angular.copy(selected), direction);
                refill();
            };

            scope.$watch('currentDate', function (value) {
                if(angular.isDate(value) && !isNaN(value) && datepickerCtrl.isDisabled(value)) {
                    scope.currentDate = null;
                    return;
                }

                if (attrs.from && !isNaN(value) && !isNaN(scope.fromDate) && datepickerCtrl.compare(value, scope.fromDate) < 0) {
                        swapDate(scope.fromDate, value);
                        return;
                }

                if (calendarSelected) {
                    refill();
                    calendarSelected = false;
                } else {
                    if (angular.isDefined(value) && value !== null) {
                        refill(value);
                    } else {
                        refill();
                    }
                }
                scope.focusedDate = undefined;
            });

            scope.$watch('fromDate', function (value) {
                if(angular.isDate(value) && !isNaN(value) && datepickerCtrl.isDisabled(value)) {
                    scope.fromDate = null;
                    return;
                }
                if (attrs.from) {
                    if (!isNaN(scope.currentDate) && !isNaN(value) && datepickerCtrl.compare(scope.currentDate, value) < 0) {
                        swapDate(value, scope.currentDate);
                        return;
                    }
                    if (calendarSelected) {
                        refill();
                        calendarSelected = false;
                    } else {
                        if (angular.isDefined(value) && value !== null) {
                            refill(value);
                        } else {
                            refill();
                        }
                    }
                }
                scope.focusedDate = undefined;
            });
        }
    };
}])
.directive('datepickerPopup', ['$document', 'datepickerService', '$isElement', '$documentBind', function($document, datepickerService, $isElement, $documentBind) {
    var link = function (scope, elem, attr) {
        datepickerService.bindScope(attr, scope);

        scope.isOpen = false;

        var toggle = scope.toggle = function (show) {
            if(show === true || show === false) {
                scope.isOpen = show;
            } else {
                scope.isOpen = !scope.isOpen;
            }
        };

        scope.$watch('current', function () {
            toggle(false);
        });

        var outsideClick = function (e) {
            var isElement = $isElement(angular.element(e.target), elem, $document);
            if(!isElement) {
                toggle(false);
                scope.$apply();
            }
        };

        $documentBind.click('isOpen', outsideClick, scope);
    };

    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/datepicker/datepickerPopup.html',
        scope: {
            current: "=current"
        },
        compile: function (elem, attr) {
            var wrapperElement = elem.find('span').eq(1);
            wrapperElement.attr('current', 'current');
            datepickerService.setAttributes(attr, wrapperElement);

            return link;
        }
    };
}])

.directive('attDatepicker', ['$log', function($log) {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {},
        controller: ['$scope', '$element', '$attrs', '$compile', 'datepickerConfig', 'datepickerService', function($scope, $element, $attrs, $compile, datepickerConfig, datepickerService) {
            var dateFormatString = angular.isDefined($attrs.dateFormat) ? $scope.$parent.$eval($attrs.dateFormat) : datepickerConfig.dateFormat;
            var selectedDateMessage = '<div class="sr-focus hidden-spoken" tabindex="-1">the date you selected is {{$parent.current | date : \'' + dateFormatString + '\'}}</div>';

            $element.removeAttr('att-datepicker');
            $element.removeAttr('ng-model');
            $element.attr('ng-model', '$parent.current');
            $element.attr('aria-describedby', 'datepicker');
            $element.attr('format-date', dateFormatString);
            $element.attr('att-input-deny', '[^0-9\/-]');
            $element.attr('maxlength', 10);
			$element.attr('readonly', 'readonly'); //Trinity for CATO
            var wrapperElement = angular.element('<div></div>');
            wrapperElement.attr('datepicker-popup', '');
            wrapperElement.attr('current', 'current');

            datepickerService.setAttributes($attrs, wrapperElement);
            datepickerService.bindScope($attrs, $scope);

            wrapperElement.html('');
            wrapperElement.append($element.prop('outerHTML'));
            if (navigator.userAgent.match(/MSIE 8/) === null) {
                wrapperElement.append(selectedDateMessage);
            }
            var elm = wrapperElement.prop('outerHTML');
            elm = $compile(elm)($scope);
            $element.replaceWith(elm);
        }],
        link: function(scope, elem, attr, ctrl) {
            if (!ctrl) {
                // do nothing if no ng-model
                $log.error("ng-model is required.");
                return;
            }

            scope.$watch('current', function(value) {
                ctrl.$setViewValue(value);
            });
            ctrl.$render = function() {
                scope.current = ctrl.$viewValue;
            };
        }
    };
}])

.directive('formatDate', ['dateFilter', 'datepickerService', function(dateFilter, datepickerService) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attr, ctrl) {
            var formatDate = "";
            attr.$observe('formatDate', function (value) {
                formatDate = value;
            });
            var dateToString = function(value) {
                if (value) {
                    ctrl.$setValidity('invalidDate', true);
                    return dateFilter(value, formatDate);
                } else {
                    ctrl.$setValidity('invalidDate', false);
                    return elem.val();
                }
            };
            var stringToDate = function(value) {
                if(datepickerService.validateDateString(value, formatDate)) {
                    ctrl.$setValidity('invalidDate', true);
                    return new Date(value);
                } else {
                    ctrl.$setValidity('invalidDate', false);
                    return null;
                }
            };
            ctrl.$formatters.unshift(dateToString);
            ctrl.$parsers.unshift(stringToDate);
        }
    };
}])

.directive('attDateFilter', ['$document', 'dateFilter', 'datepickerConfig', 'datepickerService', '$isElement', '$documentBind', function($document, dateFilter, datepickerConfig, datepickerService, $isElement, $documentBind) {

    var link = function (scope, elem, attr, ctrl) {
        datepickerService.bindScope(attr, scope);

        scope.selectedOption = datepickerConfig.dateFilter.defaultText;
        scope.showDropdownList = false;
        scope.showCalendar = false;
        scope.applyButtonType = "disabled";

        scope.currentSelection = "";
        var dateFormatString = angular.isDefined(attr.dateFormat) ? scope.$parent.$eval(attr.dateFormat) : datepickerConfig.dateFormat;
        var inputChange = false;

        var setDropdownText = function(value) {
            if(inputChange) {
                return;
            }

            var fromDateText = dateFormatString.toUpperCase();
            var currentDateText = dateFormatString.toUpperCase();

            if(!isNaN(new Date(scope.fromDate))) {
                fromDateText = dateFilter(scope.fromDate, dateFormatString);
            }
            if(!isNaN(new Date(scope.currentDate))) {
                currentDateText = dateFilter(scope.currentDate, dateFormatString);
            }

            if(value === 'Custom Single Date') {
                ctrl.$setValidity('invalidDate', true);
                scope.maxLength = 10;
                scope.selectedOption = currentDateText;
            } else if(value === 'Custom Range') {
                ctrl.$setValidity('invalidDate', true);
                ctrl.$setValidity('invalidDateRange', true);
                scope.maxLength = 21;
                scope.selectedOption = fromDateText + '-' + currentDateText;
            }
        };

        var clear = scope.clear = function(partial) {
            scope.fromDate = undefined;
            scope.currentDate = undefined;
            scope.applyButtonType = "disabled";
            if(!partial) {
                ctrl.$setValidity('invalidDate', true);
                ctrl.$setValidity('invalidDateRange', true);
                setDropdownText(scope.currentSelection);
            }
        };

        var showCalendar = function() {
            scope.showCalendar = true;
        };

        var hideCalendar = function() {
            scope.showCalendar = false;
            if(scope.currentSelection !== 'Custom Single Date' && scope.currentSelection !== 'Custom Range') {
                clear(true);
            }
        };

        var showDropdown = scope.showDropdown = function (show) {
            if(show === true || show === false) {
                scope.showDropdownList = show;
            } else {
                scope.showDropdownList = !scope.showDropdownList;
            }

            if (!scope.showDropdownList) {
                scope.focusInputButton = true;
                hideCalendar();
            } else {
                if (scope.currentSelection === 'Custom Single Date' || scope.currentSelection === 'Custom Range') {
                    showCalendar();
                }
            }
        };

        scope.resetTime = function(date) {
            var dt;
            if (!isNaN(new Date(date))) {
                dt = new Date(date);
            } else {
                return null;
            }
            return new Date(dt.getFullYear(), dt.getMonth(), dt.getDate());
        };

        scope.getDropdownText = function () {
            inputChange = true;
            var dropdownText = scope.selectedOption;

            if (scope.currentSelection === 'Custom Single Date') {
                if (!isNaN(new Date(dropdownText)) && datepickerService.validateDateString(dropdownText, dateFormatString)) {
                    ctrl.$setValidity('invalidDate', true);
                    scope.fromDate = undefined;
                    scope.currentDate = new Date(dropdownText);
                } else {
                    ctrl.$setValidity('invalidDate', false);
                    clear(true);
                }
            } else if (scope.currentSelection === 'Custom Range') {
                if (dropdownText.indexOf('-') !== -1 && (dropdownText.split('-').length === 2 || dropdownText.split('-').length === 6)) {
                    ctrl.$setValidity('invalidDateRange', true);
                    var resultDropdownText = dropdownText.split('-');
                    if (resultDropdownText.length === 2) {
                        resultDropdownText[0] = resultDropdownText[0].trim();
                        resultDropdownText[1] = resultDropdownText[1].trim();
                    } else if (resultDropdownText.length === 6) {
                        var firstDateString = resultDropdownText[0].trim() + '-' + resultDropdownText[1].trim() + '-' + resultDropdownText[2].trim();
                        var secondDateString = resultDropdownText[3].trim() + '-' + resultDropdownText[4].trim() + '-' + resultDropdownText[5].trim();
                        resultDropdownText[0] = firstDateString;
                        resultDropdownText[1] = secondDateString;
                    }

                    if (!isNaN(new Date(resultDropdownText[0])) && !isNaN(new Date(resultDropdownText[1])) && datepickerService.validateDateString(resultDropdownText[0], dateFormatString) && datepickerService.validateDateString(resultDropdownText[1], dateFormatString)) {
                        ctrl.$setValidity('invalidDate', true);
                        var fromDate = new Date(resultDropdownText[0]);
                        var currentDate = new Date(resultDropdownText[1]);
                        if(fromDate.getTime() < currentDate.getTime()) {
                            ctrl.$setValidity('invalidDateRange', true);
                            scope.fromDate = fromDate;
                            scope.currentDate = currentDate;
                        } else {
                            ctrl.$setValidity('invalidDateRange', false);
                            clear(true);
                        }
                    } else {
                        ctrl.$setValidity('invalidDate', false);
                        clear(true);
                    }
                } else {
                    ctrl.$setValidity('invalidDateRange', false);
                    clear(true);
                }
            }
        };

        scope.untrackInputChange = function() {
            inputChange = false;
        };

        scope.selectAdvancedOption = function (value, notClearFlag) {
            scope.currentSelection = value;
            if(!notClearFlag){
                clear();
                showCalendar();
            }
            scope.$watch('currentDate', function(val) {
                if(!isNaN(new Date(val))) {
                    scope.applyButtonType = "primary";
                    setDropdownText(value);
                    if (!inputChange) {
                        scope.focusApplyButton = true;
                    }
                }
            });
            scope.$watch('fromDate', function(val) {
                if(!isNaN(new Date(val))) {
                    setDropdownText(value);
                }
            });
            if (value === 'Custom Single Date') {
                scope.focusSingleDateCalendar = true;
            } else if (value === 'Custom Range') {
                scope.focusRangeCalendar = true;
            }
        };

        scope.resetFocus = function () {
            scope.focusSingleDateCalendar = false;
            scope.focusRangeCalendar = false;
            scope.focusApplyButton = false;
        };

        scope.apply = function() {
            scope.dateRange.selection = scope.selectedOption;
            if(!isNaN(new Date(scope.fromDate))) {
                scope.from = scope.fromDate;
                scope.dateRange.from = scope.fromDate;
            } else {
                scope.from = undefined;
                scope.dateRange.from = undefined;
            }
            if(!isNaN(new Date(scope.currentDate))) {
                scope.current = scope.currentDate;
                scope.dateRange.current = scope.currentDate;
            } else {
                scope.current = undefined;
                scope.dateRange.current = undefined;
            }

            showDropdown();
        };

        scope.$watchCollection(function() {
            return scope.dateRange;
        }, function(value) {
            if(ctrl) {
                var finalDateRange = angular.copy(value);
                ctrl.$setViewValue(finalDateRange);
            }
        });

        ctrl.$render = function () {
            if (ctrl.$viewValue) {
                var inputRange = ctrl.$viewValue;
                scope.selectedOption = inputRange.selection;
                scope.fromDate = inputRange.from;
                scope.currentDate = inputRange.current;
                if (scope.fromDate !== undefined && scope.currentDate !== undefined) {
                    scope.selectAdvancedOption('Custom Range', true);
                    scope.dateRange.from = scope.fromDate;
                    scope.dateRange.current = scope.currentDate;
                } else if (scope.currentDate !== undefined) {
                    scope.selectAdvancedOption('Custom Single Date', true);
                    scope.dateRange.from = undefined;
                    scope.dateRange.current = scope.currentDate;
                }
            }
        };
        
        scope.cancel = function() {
            scope.currentSelection = "";
            scope.selectedOption = datepickerConfig.dateFilter.defaultText;
            showDropdown();
        };

        var outsideClick = function (e) {
            var isElement = $isElement(angular.element(e.target), elem, $document);
            if(!isElement) {
                scope.cancel();
                scope.$apply();
            }
        };
        $documentBind.click('showDropdownList', outsideClick, scope);
    };

    return {
        restrict: 'EA',
        scope: {
            from: '=?from',
            current: "=?current"
        },
        replace: true,
        require: '?ngModel',
        transclude:true,
        templateUrl: 'app/scripts/ng_js_att_tpls/datepicker/dateFilter.html',
        controller:['$scope', '$element', '$attrs',function($scope){
            $scope.dateRange = {
                selection: undefined,
                from: undefined,
                current: undefined
            };
            this.selectOption = function (fromDate,toDate,caption) {
                $scope.selectedOption = caption;
                $scope.currentSelection =caption;
                $scope.dateRange.selection = caption;
                $scope.dateRange.current = $scope.resetTime(toDate);
                $scope.dateRange.from = $scope.resetTime(fromDate);
                $scope.showDropdown();
        };
         $scope.checkCurrentSelection=this.checkCurrentSelection = function(value) {
            if(value === $scope.currentSelection) {
                return true;
            }
            return false;
        };
        }],
        compile: function(elem, attr) {
            var singleDateCalendar = elem.find('span').eq(4);
            var rangeCalendar = elem.find('span').eq(5);
            rangeCalendar.attr('from', 'fromDate');
            singleDateCalendar.attr('current', 'currentDate');
            rangeCalendar.attr('current', 'currentDate');
            datepickerService.setAttributes(attr, singleDateCalendar);
            datepickerService.setAttributes(attr, rangeCalendar);

            return link;
        }
    };
}])
.directive('attDateFilterList',function(){
    return{
        restrict:'EA',
        scope:{
            fromDate:'=fromDate',
            toDate:'=toDate',
            caption:'=caption',
            disabled:'=disabled'
        },
        require:'^attDateFilter',
        transclude:true,
        replace:true,
        templateUrl:'app/scripts/ng_js_att_tpls/datepicker/dateFilterList.html',
        link:function(scope,elem,attr,ctrl){
            scope.selectOption=function(fromDate,toDate,caption){
                ctrl.selectOption(fromDate,toDate,caption);
            };
            scope.checkCurrentSelection=ctrl.checkCurrentSelection;
        }
    };
});
angular.module('att.abs.devNotes', [])

  .directive('attDevNotes', function() {
    return {
      restrict: 'EA',
      transclude: true,
      scope: {},
      controller: function($scope){
        var panes = $scope.panes = [];
        $scope.select = function(pane)
        {
            angular.forEach(panes, function(pane)
            {
                pane.selected = false;
            });
            pane.selected = true;
        };
        this.addPane = function(pane) {
            if (panes.length === 0) {
                $scope.select(pane);
            }
          panes.push(pane);
        };
      },
      template:'<div>'+
        '<ul class="tabs">' +
            '<li ng-repeat="pane in panes" ng-class="{active:pane.selected}">'+
              '<a href="javascript:void(0)" ng-click="select(pane)">{{pane.title}}</a>' +
            '</li>' +
          '</ul>' +
          '<div ng-transclude></div>'+
          '</div>',
          replace: true
    };
  })

  .directive('pane', function() {
    return {
      require: '^attDevNotes',
      restrict: 'EA',
      transclude: true,
      scope: {
          title: '@'
      },
      link: function(scope, element, attrs, tabsCtrl) {
        tabsCtrl.addPane(scope);
      },
      template:
        '<div class="tab-pane" ng-class="{active: selected}">' +
        '<pre ng-class="{\'language-markup\':title==\'HTML\',\'language-javascript\':title==\'JavaScript\',\'language-json\':title==\'JSON\'}"  class=" line-numbers">' +
         '<code ng-transclude></code>' +
         '</pre>' +
        '</div>',
      replace: true
    };
  });

angular.module('att.abs.dividerLines', [])
        .directive('attDividerLines', [function()
            {
                return {
                    scope: {
                        attDividerLines: '@'
                    },
                    restrict: 'A',
                    replace: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/dividerLines/dividerLines.html',
                    link: function(scope, element, attribute)
                    {
                        scope.lightContainer = attribute.attDividerLines;
                    }
                };
            }]);

angular.module('att.abs.dragdrop', [])
        .directive('attFileDrop', ['$parse', function($parse) {
                return {
                    restrict: 'A',
                    scope: {
                        fileModel : '=',
                        onDrop : '&',
                        attFileDrop : '&'
                    },
                    controller: ['$scope', '$attrs', function($scope, $attrs){
                        if($attrs.attFileDrop!==""){
                            $scope.onDrop=$scope.attFileDrop;
                        }
                        this.onDrop = $scope.onDrop;
                    }],
                    link: function(scope, element) {
                        element.addClass('dragdrop');
                        element.bind(
                            'dragover',
                            function(e) {
                                if(e.originalEvent){
                                    e.dataTransfer = e.originalEvent.dataTransfer;
                                }
                                e.dataTransfer.dropEffect = 'move';
                                // allows us to drop
                                if (e.preventDefault) {
                                    e.preventDefault();
                                }
                                element.addClass('dragdrop-over');
                                return false;
                            }
                        );
                        element.bind(
                            'dragenter',
                            function(e) {
                                // allows us to drop
                                if (e.preventDefault) {
                                    e.preventDefault();
                                }
                                element.addClass('dragdrop-over');
                                return false;
                            }
                        );
                        element.bind(
                            'dragleave',
                            function() {
                                element.removeClass('dragdrop-over');
                                return false;
                            }
                        );
                        element.bind(
                            'drop',
                            function(e) {
                                // Stops some browsers from redirecting.
                                if(e.preventDefault) {
                                    e.preventDefault();
                                }
                                if (e.stopPropagation) {
                                    e.stopPropagation();
                                }
                                if(e.originalEvent){
                                    e.dataTransfer = e.originalEvent.dataTransfer;
                                }
                                element.removeClass('dragdrop-over');
                                if(e.dataTransfer.files && e.dataTransfer.files.length > 0){
                                    scope.fileModel = e.dataTransfer.files[0];
                                    scope.$apply();
                                    if(typeof scope.onDrop === "function"){
                                        scope.onDrop = $parse(scope.onDrop);
                                        scope.onDrop();
                                    }
                                }
                                return false;
                            }
                        );
                    }
                };
            }])
        .directive('attFileLink', [ function() {
                return {
                    restrict: 'EA',
                    require: '^?attFileDrop',
                    replace: true,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/dragdrop/fileUpload.html',
                    scope: {
                        fileModel : '=?',
                        onFileSelect : '&',
                        attFileLink : '&'
                    },
                    controller: ['$scope', '$parse', function($scope, $parse){
                        this.setFileModel= function(fileModel){
                            if($scope.takeFileModelFromParent){
                                $scope.$parent.fileModel = fileModel;
                                $scope.$parent.$apply();
                            }
                            else{
                                $scope.fileModel = fileModel;
                                $scope.$apply();
                            }
                        };
                        this.callbackFunction= function(){
                            if(typeof $scope.onFileSelect === "function"){
                                $scope.onFileSelect = $parse($scope.onFileSelect);
                                $scope.onFileSelect();
                            }
                        };
                      
                    }],
                    link: function(scope, element, attr, attFileDropCtrl) {
                        scope.takeFileModelFromParent = false;
                        if(!(attr.fileModel) && attFileDropCtrl){
                            scope.takeFileModelFromParent = true;
                        }
                        if(attr.attFileLink!==""){
                            scope.onFileSelect=scope.attFileLink;
                        }
                        else if(!(attr.onFileSelect) && attFileDropCtrl){
                            scope.onFileSelect = attFileDropCtrl.onDrop;
                        }
                    }
                };
            }])
        .directive('attFileChange', ['$log','$rootScope',function($log,$rootScope) {
                return {
                    restrict: 'A',
                    require: '^attFileLink',
                    link: function(scope, element, attr, attFileLinkCtrl) {
                        element.bind('change',changeFileModel);
                        function changeFileModel(e) {
                            if (e.target.files && e.target.files.length > 0) {
                                attFileLinkCtrl.setFileModel(e.target.files[0]);
                                attFileLinkCtrl.callbackFunction();
                            }
                            else {
                                var strFileName = e.target.value;
                                try {
                                    var objFSO = new ActiveXObject("Scripting.FileSystemObject");
                                    attFileLinkCtrl.setFileModel(objFSO.getFile(strFileName));
                                    attFileLinkCtrl.callbackFunction();
                                }
                                catch (e) {
                                    var errMsg = "Error: Please follow the guidelines of Drag and Drop component on Sandbox demo page.";
                                    $log.error(errMsg);
                                    $rootScope.$broadcast('att-file-link-failure', errMsg);
                                }
                            }
                        }
                    }
                };
            }]);
angular.module("att.abs.drawer", ['att.abs.utilities'])
.directive('attDrawer', ['$document', '$timeout', 'DOMHelper', function ($document, $timeout, DOMHelper) {
        return {
            restrict: 'EA',
            replace: true,
            transclude: true,
            scope: {
                drawerOpen: "=?",
                drawerAutoClose: "&?"
            },
            template: '<div><div class="att-drawer" ng-transclude></div><div ng-class="{\'drawer-backdrop\':drawerOpen}"></div></div>',
            link: function ($scope, element, attrs) {
                var param = {};
                // First Element in Drawer component
                var firstElement = undefined;
                // Element drawer is toggled from
                var drawerLaunchingElement = undefined;
                // Override default parameters
                param.side = attrs.drawerSlide || 'top';
                param.speed = attrs.drawerSpeed || '0.25';
                param.size = attrs.drawerSize || '300px';
                param.zindex = attrs.drawerZindex || 1000;
                param.className = attrs.drawerClass || 'att-drawer';
                var slider = element.eq(0).children()[0];
                var content = angular.element(slider).children()[0];
                slider.className = param.className;
                /* Style setup */
                slider.style.transitionDuration = param.speed + 's';
                slider.style.webkitTransitionDuration = param.speed + 's';
                slider.style.zIndex = param.zindex;
                slider.style.position = 'fixed';
                slider.style.width = 0;
                slider.style.height = 0;
                slider.style.transitionProperty = 'width, height';
                if(param.side==='right'){
                    slider.style.height = attrs.drawerCustomHeight || '100%';
                    slider.style.top = attrs.drawerCustomTop ||  '0px';
                    slider.style.bottom = attrs.drawerCustomBottom ||  '0px';
                    slider.style.right = attrs.drawerCustomRight ||  '0px';
                }else if(param.side==='left'){      /*Added this part for ECOM*/
                	slider.style.height = attrs.drawerCustomHeight || '100%';
                	slider.style.top = attrs.drawerCustomTop ||  '0px';
                	slider.style.bottom = attrs.drawerCustomBottom ||  '0px';
                	slider.style.left = attrs.drawerCustomRight ||  '0px';
                }
                else if(param.side==='top' || param.side==='bottom'){
                    slider.style.width = attrs.drawerCustomWidth || '100%';
                    slider.style.left = attrs.drawerCustomLeft || '0px';
                    slider.style.top = attrs.drawerCustomTop || '0px';
                    slider.style.right = attrs.drawerCustomRight || '0px';
                }
                $timeout(function() {
                    firstElement = DOMHelper.firstTabableElement(element[0]);
                }, 10, false);
                /* Closed */
                function drawerClose(slider, param) {
                    if (slider && slider.style.width !== 0 && slider.style.height !== 0){
                        content.style.display = 'none';
                        if(param.side==='right' || param.side==='left'){
                            slider.style.width = '0px';
                        }
                        else if(param.side==='top' || param.side==='bottom'){
                            slider.style.height = '0px';
                        }
                    }
                    $scope.drawerOpen = false;
                    // Shift focus
                    if (angular.isDefined(drawerLaunchingElement) && drawerLaunchingElement != null) {
                        drawerLaunchingElement.focus();
                    }
                }
                /* Open */
                function drawerOpen(slider, param) {
                    // Before opening drawer, find the focused element
                    drawerLaunchingElement = document.activeElement;
                    if (slider.style.width !== 0 && slider.style.height !== 0){
                        if(param.side==='right' || param.side==='left'){
                            slider.style.width = param.size;
                        }
                        else if(param.side==='top' || param.side==='bottom'){
                            slider.style.height = param.size;
                        }
                        $timeout(function() {
                            content.style.display = 'block';
                            // Shift focus
                            if (angular.isDefined(firstElement) && firstElement != null) {
                                firstElement.focus();
                            }
                        },(param.speed * 1000));
                    }
                }
                function isFunction(functionToCheck) {
                    var getType = {};
                    return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
                }
                /*
                * Watchers
                * */
                if(attrs.drawerSize) {
                    $scope.$watch(function() {
                        return attrs.drawerSize;
                    }, function(newVal) {
                        param.size = newVal;
                        if($scope.drawerOpen) {
                            drawerOpen(slider,param);
                        }
                    });
                }
                $scope.$watch("drawerOpen", function (value){
                    if (!!value) {
                        // Open
                        drawerOpen(slider,param);
                    } else {
                        // Close
                        drawerClose(slider,param);
                    }
                });
                // close panel on location change
                if($scope.drawerAutoClose()) {
                    $scope.$on("$locationChangeStart", function(){
                        drawerClose(slider, param);
                        if(isFunction($scope.drawerAutoClose())) {
                            $scope.drawerAutoClose();
                        }
                    });
                    $scope.$on("$stateChangeStart", function(){
                        drawerClose(slider, param);
                        if(isFunction($scope.drawerAutoClose)) {
                            $scope.drawerAutoClose();
                        }
                    });
                }
            }
        };
    }
]);
angular.module('att.abs.message', [])

.directive('attMessages', [function() {
    return {
        restrict: 'EA',
        scope: {
            messageType: '=?'
        },
        controller: ['$scope', '$element', '$attrs', function($scope, $element, $attrs) {
            $scope.messageScope = [];
            this.registerScope = function(messageScope) {
                $scope.messageScope.push(messageScope);
            };
            $scope.$parent.$watchCollection($attrs['for'], function(errors) {
                for (var key in errors) {
                    if (errors[key]) {
                        $scope.error = key;
                        break;
                    } else {
                        $scope.error = null;
                    }
                }
                for (var i = 0; i < $scope.messageScope.length; i++) {
                    if($scope.messageScope[i].when === $scope.error) {
                        $scope.messageScope[i].show();
                        $scope.setMessageType($scope.messageScope[i].type);
                    } else {
                        $scope.messageScope[i].hide();
                    }
                }
                if($scope.error === null) {
                    $scope.setMessageType(null);
                }
            });
            $scope.setMessageType = this.setMessageType = function(messageType) {
                if($attrs.messageType) {
                    $scope.messageType = messageType;
                }
            };
        }]
    };
}])

.directive('attMessage', [function() {
    return {
        restrict: 'EA',
        scope: {},
        require: '^attMessages',
        link: function(scope, elem, attr, ctrl) {
            ctrl.registerScope(scope);
            elem.attr('role', 'alert'); //Trinity CATO
            scope.when = attr.when || attr.attMessage;
            scope.type = attr.type;
            scope.show = function() {
                elem.css({display: 'block'});
            };
            scope.hide = function() {
                elem.css({display: 'none'});
            };
            scope.hide();
        }
    };
}]);

angular.module('att.abs.formField', ['att.abs.message', 'att.abs.utilities'])
.directive('attFormField', [function() {
    return {
        priority: 101,
        restrict: 'A',
        controller:function() {
        },
        link: function(scope, elem, attr) {
            elem.wrap('<div class="form-field"></div>');
            elem.parent().append('<label class="form-field__label">' + attr.placeholder || attr.attFormField + '</label>');
            elem.wrap('<div class="form-field-input-container"></div>');

            elem.bind('keyup', function() {
                if (this.value !== '') {
                    elem.parent().parent().find('label').addClass('form-field__label--show').removeClass('form-field__label--hide');
                } else {
                    elem.parent().parent().find('label').addClass('form-field__label--hide').removeClass('form-field__label--show');
                }
            });

            elem.bind('blur', function() {
                if (this.value === '') {
                    elem.parent().parent().find('label').removeClass('form-field__label--hide');
                }
            });
        }
    };
}])
.directive('attFormFieldValidation', ['$compile', '$log', function($compile, $log) {
    return {
        priority: 102,
        scope: {},
        restrict: 'A',
        require: ['?ngModel', '?attFormField'],
        link: function(scope, elem, attr, ctrl) {
            var ngCtrl = ctrl[0];
            var attFormFieldCtrl = ctrl[1];
            scope.valid = "";
            if (!ngCtrl) {
                $log.error("att-form-field-validation :: ng-model directive is required.");
                return;
            }
            if (!attFormFieldCtrl) {
                $log.error("att-form-field-validation :: att-form-field directive is required.");
                return;
            }

            elem.parent().append($compile(angular.element('<i class="icon-info-alert error" ng-show="valid===false">&nbsp;</i>'))(scope));
            elem.parent().append($compile(angular.element('<i class="icon-info-success success" ng-show="valid===true">&nbsp;</i>'))(scope));

            scope.$watch('valid', function(value) {
                if (value) {
                    elem.parent().parent().addClass('success');
                } else if (value === false) {
                    elem.parent().parent().addClass('error');
                } else {
                    elem.parent().parent().removeClass('success').removeClass('error');
                }
            });

            elem.bind('keyup', function() {
                if (ngCtrl.$valid) {
                    scope.valid = true;
                } else if (ngCtrl.$invalid) {
                    scope.valid = false;
                } else {
                    scope.valid = "";
                }
                scope.$apply();
            });
        }
    };
}])
.directive('attFormFieldValidationAlert', ['$timeout', function($timeout) {
        return {
        scope: {
            messageType: '=?'
        },
        restrict: 'EA',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlert.html',
        link: function(scope, elem, attr, ctrl) {
            scope.showLabel = false;
            scope.hideLabel = false;
            scope.errorMessage = false;
            scope.warningMessage = false;
            var checkMessageType = function() {
                var messageType = scope.messageType;
                if (messageType === 'error') {
                    scope.errorMessage = true;
                    scope.warningMessage = false;
                } else if (messageType === 'warning') {
                    scope.errorMessage = false;
                    scope.warningMessage = true;
                } else {
                    scope.errorMessage = false;
                    scope.warningMessage = false;
                }
            };
            var oldIE = navigator.userAgent.toLowerCase().indexOf('msie 8.0') !== -1;
            elem.find('label').text(elem.find('input').attr('placeholder'));
            elem.find('input').bind('keyup', function() {
                if (this.value !== '') {
                    scope.showLabel = true;
                    scope.hideLabel = false;
                    if (oldIE) {
                        elem.find('label').css({top: '-20px'});
                    }
                } else {
                    scope.showLabel = false;
                    scope.hideLabel = true;
                    if (oldIE) {
                        elem.find('label').css({top: '0px'});
                    }
                }
                checkMessageType();
                scope.$apply();
            });

            elem.find('input').bind('blur', function() {
                if (this.value === '') {
                    scope.showLabel = false;
                    scope.hideLabel = false;
                }
                scope.$apply();
            });
            $timeout(function() {
                checkMessageType();
            }, 100);
        }
    };
}])
.constant("CoreFormsUiConfig", {
            phoneMask: '(___) ___-____'
        })
.directive('attPhoneMask', ['$parse', 'CoreFormsUiConfig', function($parse, CoreFormsUiConfig) {
                return {
                    require: 'ngModel',
                    scope: {
                        ngModel: '='
                    },
                    link: function(scope, iElement, iAttrs, ctrl) {
                        var B = navigator.userAgent.toLowerCase(), C = B.indexOf("android") > -1,
                                oldIE = B.indexOf('msie 8.0') !== -1;;
                        var A = '';
                        var validPhoneNumber = false;
                        if (C) {
                            A = "__________";
                        }
                        else {
                            A = CoreFormsUiConfig.phoneMask;
                        }
                        iElement.attr("maxlength", A.length);
                        var checkValidity = function(unmaskedValue) {
                        var valid = false;
                        if (unmaskedValue){
                        valid = (unmaskedValue.length === 10);}
                        ctrl.$setValidity('invalidPhoneNumber', validPhoneNumber);
                        ctrl.$setValidity('mask', valid);
                        return valid;
                        };
                        
                        var handleKeyup = function() {
                            var E,D = ctrl.$modelValue;
                            if (!D.length) {
                                return;
                            }
                            var L, K, G, J, I;
                            J = [];
                            G = A.split("");
                            I = G.length;
                            L = D.substring(0, A.length);
                            K = D.replace(/[^0-9]/g, "").split("");
                            for (E = 0; E < I; E++) {
                                J.push(G[E] === "_" ? K.shift() : G[E]);
                                if (K.length === 0) {
                                    break;
                                }
                            }
                            D = J.join("");
                            if (D === '('){
                                D = '';}
                            ctrl.$setViewValue(D);
                            ctrl.$render();
                            return D;
                        };


                        // since we are only allowing 0-9, why even let the keypress go forward?
                        // also added in delete... in case they want to delete :)
                        var handlePress = function(e) {
                            if (e.which) {
                                if ((e.which < 48 || e.which > 57) && (e.which < 96 || e.which > 105)) {
                                         if (e.which !== 8 && e.which !== 9 && e.which !== 46 && e.which !== 13 && e.which !== 37 && e.which !== 39 &&
                                            // Allow: Ctrl+V/v
                                            (e.ctrlKey !== true && (e.which !== '118' || e.which !== '86'))&&
                                             // Allow: Ctrl+C/c
                                            (e.ctrlKey !== true && (e.which !== '99' || e.which !== '67'))&&
                                            // Allow: Ctrl+X/x
                                            (e.ctrlKey !== true && (e.which !== '120' || e.which !== '88')))
                                            {
						e.preventDefault ? e.preventDefault() : e.returnValue = false;
						iElement.attr("aria-label","Only numbers are allowed");
						validPhoneNumber = false;
                                            }}
				else{
					iElement.removeAttr("aria-label");
					validPhoneNumber = true;
                                    }
                                   }
                            scope.$apply();
                        };
                        // i moved this out because i thought i might need focus as well..
                        // to handle setting the model as the view changes
                        var parser = function(fromViewValue) {
                            var letters = /^[A-Za-z]+$/;
                            var numbers = /^[0-9]+$/;
                            if(fromViewValue.match(letters))
                                {validPhoneNumber = false;}
                            if(fromViewValue.match(numbers))
                                {validPhoneNumber = true;}
                            var clean = "";
                            if (fromViewValue && fromViewValue.length > 0) {
                                clean = fromViewValue.replace(/[^0-9]/g, '');
                            }
                            checkValidity(clean);
                            return clean;
                        };

                        //to handle reading the model and formatting it
                        var formatter = function(fromModelView) {
                            var input = '';
                            checkValidity(fromModelView);
                            if (fromModelView){
                                input = handleKeyup();}
                            return input;
                        };
                        ctrl.$parsers.push(parser);
                        ctrl.$formatters.push(formatter);
                        iElement.bind('keyup', handleKeyup);
                        iElement.bind('keydown', handlePress);
                        iElement.bind('input', function(e){
                            handleKeyup(e);
                            handlePress(e);
                        });
                    }
                };
}])
.constant('validationTypeInt', {
            validationNum: {'number':'1','text':'2','email':'3'}
        })        
.directive('attFormFieldPrv', [ 'keyMapAc', 'validationTypeInt', function( keyMapAc, validationTypeInt ) {
    return {
        priority: 101,
        restrict: 'AE',
		controller:['$scope', function($scope) {
			this.showHideErrorMessage = function ( booleanValue ){
				if(  $scope.$$prevSibling != null && angular.isDefined( $scope.$$prevSibling )
					&& angular.isDefined( $scope.$$prevSibling.hideErrorMsg ) ){
					$scope.$$prevSibling.hideErrorMsg = booleanValue;
					$scope.$apply();
				}
			};
			this.findAllowedCharactor = function( keyCode ){
				var keyMapSc = keyMapAc.keys;
				if( angular.isDefined( $scope.allowedSpecialCharacters )
						&& angular.isDefined( $scope.allowedSpecialCharacters.length )
						&& $scope.allowedSpecialCharacters.length > 0 ){
					var allowedCharList = $scope.allowedSpecialCharacters;
					var charFound = false;
					for( var i=0 ; i < allowedCharList.length ; i++){
						if( allowedCharList[i] === keyMapSc[keyCode] ){
							charFound = true;
							break;
						}
					}
					return charFound;
				}else{
					return false;
				}
			};
			this.validateText = function( validationType, allowedChars, validationInput, outputSpecialChars ){
				if( angular.isDefined( allowedChars ) &&  allowedChars.length === 0 ){
					var expAlphanumeric = /^[a-zA-Z0-9]*$/i;
					return expAlphanumeric.test( validationInput );
				}else{
					var expAlphanumericSpecialChar = '^[a-zA-Z0-9' + outputSpecialChars + ']*$';
					var regularExp = new RegExp( expAlphanumericSpecialChar, 'i' );
					return regularExp.test( validationInput );
				}
			};
			this.validateNumber = function( validationType, allowedChars, validationInput, outputSpecialChars ){
				if( angular.isDefined( allowedChars ) &&  allowedChars.length === 0 ){
					var expNumber = /^[0-9\.]+$/;
					return expNumber.test( validationInput );
				}else{
					var expNumberSpecial = '^[0-9\.' + outputSpecialChars + ']*$';
					var regularExp = new RegExp( expNumberSpecial, 'i' );
					return regularExp.test( validationInput );
				}
			};
			this.validateEmail = function( validationType, allowedChars, validationInput, outputSpecialChars ){
				if( angular.isDefined( allowedChars ) &&  allowedChars.length === 0 ){
					var expEmail = /(([a-zA-Z0-9\-?\.?]+)@(([a-zA-Z0-9\-_]+\.)+)([a-z]{2,3}))+$/;
					return expEmail.test( validationInput );
				}else{
					var expEmailSpecial = '(([a-z' + outputSpecialChars + 'A-Z0-9\-?\.?]+)@(([a-z'
						+ outputSpecialChars + 'A-Z0-9\-_]+\.)+)([' + outputSpecialChars + 'a-z]{2,3}))+$';
					var regularExp = new RegExp( expEmailSpecial, 'i' );
					return regularExp.test( validationInput );
				}
			};
			this.validateInput = function( validationType, allowedChars, validationInput ){
				var outputSpecialChars = '';
				var result = false;
				if( angular.isDefined( allowedChars ) && angular.isDefined( allowedChars.length )
					&& allowedChars.length > 0 ){
					for( var i = 0; i < allowedChars.length; i++){
						outputSpecialChars += '\\'+allowedChars[i];
					}
				}
				switch ( validationTypeInt.validationNum[ validationType ] ) {
					case validationTypeInt.validationNum["text"]:
						result = this.validateText( validationType, allowedChars, validationInput, outputSpecialChars  );
						break;
					case validationTypeInt.validationNum["number"]:
						result = this.validateNumber( validationType, allowedChars, validationInput, outputSpecialChars  );
						break;
					case validationTypeInt.validationNum["email"]:
						result = this.validateEmail( validationType, allowedChars, validationInput, outputSpecialChars  );
						break;
					default:
						break;
				}
				return result;
			};
        }],
        link: function(scope, elem, attr ) {
            elem.parent().prepend('<label class="form-field__label">' + attr.placeholder + '</label>');
            elem.wrap('<div class="form-field-input-container"></div>');
			elem.parent().parent().find('label').addClass('form-field__label--show');
        }
    };
}])
.directive('attFormFieldValidationPrv', [ 'keyMapAc','validationTypeInt' , function( keyMapAc, validationTypeInt ) {
    return {
        priority: 202,
        scope: {
            validationType: '=',
            allowedChars: '='
		},
        restrict: 'A',
        require: ['?ngModel', '^attFormFieldPrv'],
        link: function(scope, elem, attr, ctrl) {
            var attFormFieldCtrl = ctrl[1];
            elem.bind('keyup', function() {
				/* email validation has tobe done on keyup */
				if( attFormFieldCtrl.validateInput( scope.validationType, scope.allowedChars, elem[0].value ) ){
					attFormFieldCtrl.showHideErrorMessage(false);
				}
				else{
					attFormFieldCtrl.showHideErrorMessage(true);
				}
            });
			var keyMapSc = keyMapAc.keyRange;
			var allowedKeys = keyMapAc.allowedKeys;
			var validateTextCode = function( charFound,event ){
				var resultOne = (event.which < keyMapSc['startNum'] || event.which > keyMapSc['endNum'] );
				var resultTwo = (event.which < keyMapSc['startCapitalLetters'] || event.which > keyMapSc['endCapitalLetters'] );
				var resultThree = (event.which < keyMapSc['startSmallLetters'] || event.which > keyMapSc['endSmallLetters'] );
				var result = ( resultOne && resultTwo &&  resultThree );
				return ( result && ( !charFound )  );
			};
			var validateNumberCode = function( charFound,event ){
				return ( ( event.which < keyMapSc['startNum'] || event.which > keyMapSc['endNum'] ) &&   ( !charFound ) );
			};
			var validateEmailCode = function( charFound,event ){
				var condOne = String.fromCharCode( event.which ) !== '-' && String.fromCharCode( event.which ) !== '_';
				var condTwo = String.fromCharCode( event.which ) !== '@' && String.fromCharCode( event.which ) !== '.';
				var ifAllowedChars = condOne && condTwo;
				var ifCharRange = validateTextCode( charFound,event );
				return (  ( !charFound ) && ifAllowedChars && ifCharRange );
			};
			var validateSwitch = function( validationTypeSwitch, charFound, event ){
				switch ( validationTypeSwitch ) {
						case validationTypeInt.validationNum["text"]:
							/* 97-122 65-90 48-57 if keyCode is outside range of alphanumeric chars and not found in list then prevent */
							if( validateTextCode( charFound, event ) ){
								return true;
							}
							break;
						case validationTypeInt.validationNum["number"]:
							/* if key code is outside number range and notfound then prevent */
							if( validateNumberCode( charFound, event ) ){
								return true;
							}
							break;
						case validationTypeInt.validationNum["email"]:
							/* if keyCode is outside charactor/number range and not _-@. then prevent */
							if( validateEmailCode( charFound, event ) ){
								return true;
							}
							break;
						default:
							break;
				}
				return false;
			};
			/* key stroke prevention has to be happen on numeric and alphanumeric fields */
			elem.bind('keypress', function( event ){
                                if(!(event.which)){
                                    if(event.keyCode){
                                        event.which = event.keyCode;
                                    }
                                    else if(event.charCode){
                                        event.which = event.charCode;
                                    }
                                }
				var charFound = attFormFieldCtrl.findAllowedCharactor( event.which );
				var insideCondOne = ( angular.isDefined( scope.validationType ) && scope.validationType !== '');
				var insideCondTwo = ( event.which !== allowedKeys['TAB']
					&& event.which !== allowedKeys['BACKSPACE'] && event.which!== allowedKeys['DELETE'] );
				var goInside = insideCondOne && insideCondTwo;
				if( goInside && validateSwitch( validationTypeInt.validationNum[ scope.validationType ], charFound, event ) ){
					event.preventDefault();
				}
			});
        }
    };
}])
.directive('attFormFieldValidationAlertPrv', [ function() {
        return {
			restrict: 'A',
			scope : { errorMessage : '=' },
			transclude: true,
			templateUrl: 'app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlertPrv.html',
			link: function( scope ) {
				scope.errorMessage = scope.errorMessage;
				if( angular.isDefined( scope.$parent.hideErrorMsg ) ){
					scope.hideErrorMsg = scope.$parent.hideErrorMsg;
				}
				else{
					scope.hideErrorMsg = true;
				}
			}
		};
}])
//Credit card validation directives starts here
.factory('Cards', [function() {
        var defaultFormat = /(\d{1,4})/g;
        var defaultInputFormat = /(?:^|\s)(\d{4})$/;
        var cards = [
            {
                type: 'discover',
                pattern: /^(6011|65|64[4-9]|622)/,
                format: defaultFormat,
                inputFormat: defaultInputFormat,
                length: [16],
                cvcLength: [3],
                cvcSecurityImg: 'visaI',
                zipLength: [5],
                luhn: true
            },
            {
                type: 'mc',
                pattern: /^5[1-5]/,
                format: defaultFormat,
                inputFormat: defaultInputFormat,
                length: [16],
                cvcLength: [3],
                cvcSecurityImg: 'visaI',
                zipLength: [5],
                luhn: true
            },
            {
                type: 'amex',
                pattern: /^3[47]/,
                format: /(\d{1,4})(\d{1,6})?(\d{1,5})?/,
                inputFormat: /^(\d{4}|\d{4}\s\d{6})$/,
                length: [15],
                cvcLength: [4],
                cvcSecurityImg: 'amexI',
                zipLength: [5],
                luhn: true
            },
            {
                type: 'visa',
                pattern: /^4/,
                format: defaultFormat,
                inputFormat: defaultInputFormat,
                length: [16],
                cvcLength: [3],
                cvcSecurityImg: 'visaI',
                zipLength: [5],
                luhn: true
            }
        ];

        var _fromNumber = function(num) {
            var card, i, len;

            num = (num + '').replace(/\D/g, '');

            for (i = 0, len = cards.length; i < len; i++) {

                card = cards[i];

                if (card.pattern.test(num)) {
                    return card;
                }

            }
        };

        var _fromType = function(type) {
            var card, i, len;

            for (i = 0, len = cards.length; i < len; i++) {

                card = cards[i];

                if (card.type === type) {
                    return card;
                }

            }
        };

        return {
            fromNumber: function(val) {
                return _fromNumber(val);
            },
            fromType: function(val) {
                return _fromType(val);
            },
            defaultFormat: function() {
                return defaultFormat;
            },
            defaultInputFormat: function() {
                return defaultInputFormat;
            }
        };

    }])
.factory('_Validate', ['Cards', '$parse', function(Cards, $parse) {
        var __indexOf = [].indexOf || function(item)
        {
            for (var i = 0, l = this.length; i < l; i++)
            {
                if (i in this && this[i] === item)
                {
                    return i;
                }
            }
            return -1;
        };

        var _luhnCheck = function(num) {
            var digit, digits, odd, sum, i, len;

            odd = true;
            sum = 0;
            digits = (num + '').split('').reverse();

            for (i = 0, len = digits.length; i < len; i++) {

                digit = digits[i];
                digit = parseInt(digit, 10);

                if ((odd = !odd)) {
                    digit *= 2;
                }

                if (digit > 9) {
                    digit -= 9;
                }

                sum += digit;

            }

            return sum % 10 === 0;
        };

        var _validators = {};

        _validators['cvc'] = function(cvc, ctrl, scope, attr) {
            var ref, ref1;

            // valid if empty - let ng-required handle empty
            if ((angular.isUndefined(cvc)) || (cvc === null) || (cvc.length === 0))
            {
                return true;
            }

            if (!/^\d+$/.test(cvc)) {
                return false;
            }

            var type;
            if (attr.paymentsTypeModel) {
                var typeModel = $parse(attr.paymentsTypeModel);
                type = typeModel(scope);
            }

            if (type)
            {
                ref1 = Cards.fromType(type);
                return (ref = cvc.length, __indexOf.call((ref1 !== null) ? ref1.cvcLength : void 0, ref)) >= 0;
            }
            else
            {
                return cvc.length >= 3 && cvc.length <= 4;
            }
        };
        _validators['zip'] = function(zip, ctrl, scope, attr) {
            var ref, ref1;

            // valid if empty - let ng-required handle empty
            if ((angular.isUndefined(zip)) || (zip === null) || (zip.length === 0))
            {
                return true;
            }

            if (!/^\d+$/.test(zip)) {
                return false;
            }

            var type;
            if (attr.paymentsTypeModel) {
                var typeModel = $parse(attr.paymentsTypeModel);
                type = typeModel(scope);
            }
            if (type)
            {
                ref1 = Cards.fromType(type);
                return (ref = zip.length, __indexOf.call(ref1 !== null ? ref1.zipLength : void 0, ref)) >= 0;
            }
            else
            {
                return zip.length < 6;
            }
        };
        _validators['card'] = function(num, ctrl, scope, attr) {
            var card, ref, typeModel;

            if (attr.paymentsTypeModel) {
                typeModel = $parse(attr.paymentsTypeModel);
            }

            var clearCard = function() {
                if (typeModel) {
                    typeModel.assign(scope, null);
                }
                ctrl.$card = null;
            };

            // valid if empty - let ng-required handle empty
            if ((angular.isUndefined(num)) || (num === null) || (num.length === 0)) {
                clearCard();
                return true;
            }

            num = (num + '').replace(/\s+|-/g, '');

            if (!/^\d+$/.test(num)) {
                clearCard();
                return false;
            }

            card = Cards.fromNumber(num);
            if (!card) {
                clearCard();
                return false;
            }
            ctrl.$card = angular.copy(card);

            if (typeModel) {
                typeModel.assign(scope, card.type);
            }

            ret = (ref = num.length, __indexOf.call(card.length, ref) >= 0) && (card.luhn === false || _luhnCheck(num));
            return ret;
        };
        return function(type, val, ctrl, scope, attr) {
            if (!_validators[type]) {

                types = Object.keys(_validators);

                errstr = 'Unknown type for validation: "' + type + '". ';
                errstr += 'Should be one of: "' + types.join('", "') + '"';

                throw errstr;
            }
            return _validators[type](val, ctrl, scope, attr);
        };
    }])
.factory('_ValidateWatch', ['_Validate', function(_Validate) {

        var _validatorWatches = {};

        _validatorWatches['cvc'] = function(type, ctrl, scope, attr) {
            if (attr.paymentsTypeModel) {
                scope.$watch(attr.paymentsTypeModel, function(newVal, oldVal) {
                    if (newVal !== oldVal) {
                        var valid = _Validate(type, ctrl.$modelValue, ctrl, scope, attr);
                        ctrl.$setValidity(type, valid);
                    }
                });
            }
        };
        _validatorWatches['zip'] = function(type, ctrl, scope, attr) {
            if (attr.paymentsTypeModel) {
                scope.$watch(attr.paymentsTypeModel, function(newVal, oldVal) {
                    if (newVal !== oldVal) {
                        var valid = _Validate(type, ctrl.$modelValue, ctrl, scope, attr);
                        ctrl.$setValidity(type, valid);
                    }
                });
            }
        };
        return function(type, ctrl, scope, attr) {
            if (_validatorWatches[type]) {
                return _validatorWatches[type](type, ctrl, scope, attr);
            }
        };
    }])
.directive('validateCard', ['$window', '_Validate', '_ValidateWatch', function($window, _Validate, _ValidateWatch) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, elem, attr, ctrl) {

                var type = attr.validateCard;
                _ValidateWatch(type, ctrl, scope, attr);
                var validateFn = function(val) {
                    var valid = _Validate(type, val, ctrl, scope, attr);
                    ctrl.$setValidity(type, valid);
                    if (type === 'card')
                    {
                        if (ctrl.$card === null)
                        {
                            if ((val == null) || (val === "") || (val === ''))
                            {
                                scope.invalidCardError = '';
                                scope.invalidCard = "";
                            }
                            else if (val.length >= 1)
                            {
                                scope.invalidCardError = 'error';
                                scope.invalidCard = "The number entered is not a recognized credit card number.";
                            }
                        }
                        else
                        {
                            if (!valid)
                            {
                                if (ctrl.$card.length.indexOf(val.length) >= 0)
                                {
                                    scope.invalidCardError = 'error';
                                    scope.invalidCard = "The number entered is not a recognized credit card number.";
                                }
                                else
                                {
                                    scope.invalidCardError = '';
                                    scope.invalidCard = "";
                                }
                            }
                            else
                            {
                                scope.invalidCardError = '';
                                scope.invalidCard = "";
                            }
                        }
                        elem.bind("blur", function()
                        {
                            if ((!valid) || (ctrl.$card === null))
                            {
                                scope.invalidCardError = 'error';
                                scope.invalidCard = "The number entered is not a recognized credit card number.";
                            }
                            else
                            {
                                scope.invalidCardError = '';
                                scope.invalidCard = "";
                            }
                        });
                    }
                    return valid ? val : undefined;
                };
                ctrl.$formatters.push(validateFn);
                ctrl.$parsers.push(validateFn);
            }
        };
    }])
.directive('creditCardImage', function() {
    return{
        templateUrl: 'app/scripts/ng_js_att_tpls/formField/creditCardImage.html',
        replace: false,
        transclude: false,
        link: function(scope, element, attr)
        {
            scope.$watch(attr.creditCardImage, function(newVal, oldVal)
            {
                if (newVal !== oldVal)
                {
                    scope.cvc = '';
                    if (!angular.isUndefined(newVal) && newVal !== null)
                    {
                        scope.newValCCI = 'show-' + newVal;
                    }
                    if (newVal === null)
                    {
                        scope.newValCCI = '';
                    }
                }
            });
        }
    };
})
.directive('securityCodeImage', ['$document', function($document) {
        return{
            templateUrl: 'app/scripts/ng_js_att_tpls/formField/cvcSecurityImg.html',
            replace: false,
            transclude: false,
            link: function(scope, element, attr)
            {
                scope.$watch(attr.securityCodeImage, function(newVal, oldVal)
                {
                    if (newVal !== oldVal)
                    {
                        if (!angular.isUndefined(newVal) && newVal !== null)
                        {
                            if (newVal === 'amexI')
                            {
                                scope.newValI = 'ccv2-security-amex';
                                scope.newValIAlt = "The 4 digit CVC security code is on the front of the card.";
                                scope.cvcPlaceholder = "4 digits";
                                scope.cvcMaxlength = 4;
                            }
                            else if (newVal === 'visaI')
                            {
                                scope.newValI = 'ccv2-security';
                                scope.newValIAlt = "The CVC security code is on the back of your card right after the credit card number.";
                                scope.cvcPlaceholder = "3 digits";
                                scope.cvcMaxlength = 3;
                            }
                        }
                        if (newVal === null)
                        {
                            scope.newValI = 'ccv2-security';
                            scope.cvcPlaceholder = "3 digits";
                            scope.cvcMaxlength = 3;
                            scope.newValIAlt = "The CVC security code is on the back of your card right after the credit card number.";
                        }

                    }
                });
                element.bind("click", function(ev) {
                    ev.preventDefault();
                    if (element.find("button").hasClass("active")) {
                        element.find("button").removeClass("active");
                    }
                    else {
                        element.find("button").addClass("active");
                    }
                });

                var window = angular.element($document);
                window.bind("click", function(ev) {
                    var targetClassname = ev.target.className;
                    if ((targetClassname !== "btn btn-alt btn-tooltip active")) {
                        if (element.find("button").hasClass("active")) {
                            element.find("button").removeClass("active");
                        }
                    }

                });
            }
        };
    }]);

angular.module('att.abs.hourpicker', ['att.abs.utilities'])
    .constant('hourpickerConfig', {
        days: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        customOption: 'Custom'
    })

.controller('hourPickerController', ['$scope', function ($scope) {

    $scope.options = [];
    this.setOptions = function (value, fromtime, totime, preselect, uncheckedFromTime, uncheckedToTime) {
        $scope.options.push(value);

        if (preselect !== undefined) {
            $scope.preselect = preselect;
        }

        var daycount;

        if (fromtime !== undefined) {
            $scope.fromtime = fromtime;
            for (daycount in $scope.days) {
                if ($scope.days.hasOwnProperty(daycount)) {
                    $scope.FrtimeList[$scope.days[daycount]] = {};
                    if (uncheckedFromTime !== undefined) {
                        $scope.FrtimeList[$scope.days[daycount]].value = uncheckedFromTime;
                        $scope.selectedFromOption[$scope.days[daycount]] = uncheckedFromTime;
                    } else {
                        $scope.FrtimeList[$scope.days[daycount]].value = fromtime[0].value;
                        $scope.selectedFromOption[$scope.days[daycount]] = fromtime[0].value;
                    }
                }
            }
        }
        if (totime !== undefined) {
            $scope.totime = totime;
            for (daycount in $scope.days) {
                if ($scope.days.hasOwnProperty(daycount)) {
                    $scope.TotimeList[$scope.days[daycount]] = {};
                    if (uncheckedToTime !== undefined) {
                        $scope.TotimeList[$scope.days[daycount]].value = uncheckedToTime;
                        $scope.selectedToOption[$scope.days[daycount]] = uncheckedToTime;
                    } else {
                        $scope.TotimeList[$scope.days[daycount]].value = totime[0].value;
                        $scope.selectedToOption[$scope.days[daycount]] = totime[0].value;
                    }
                    $scope.showToTimeErrorDay[$scope.days[daycount]] = false;
                }
            }
        }

        if (uncheckedFromTime !== undefined) {
            $scope.uncheckedFromTime = uncheckedFromTime;
        }
        if (uncheckedToTime !== undefined) {
            $scope.uncheckedToTime = uncheckedToTime;
        }
    };

    this.getSelectedOption = function () {
        return $scope.selectedOption;
    };

    this.setToTimeErrorDay = function (day, flag) {
        $scope.showToTimeErrorDay[day] = flag;
    };
}])

.directive('attHourpickerOption', [function () {
    return {
        restrict: 'EA',
        require: '^attHourpicker',
        scope: {
            option: "=option",
            fromtime: "=fromtime",
            totime: "=totime",
            preselect: "=preselect",
            uncheckedFromTime: "=",
            uncheckedToTime: "="
        },
        link: function (scope, element, attr, ctrl) {
            ctrl.setOptions(scope.option,
                scope.fromtime,
                scope.totime,
                scope.preselect,
                scope.uncheckedFromTime,
                scope.uncheckedToTime);
        }
    };
}])

.directive('attHourpicker', ["hourpickerConfig", "$document", "$log", "$documentBind", "$timeout", function (hourpickerConfig, $document, $log, $documentBind, $timeout) {
    return {
        require: 'ngModel',
        restrict: 'EA',
        controller: 'hourPickerController',
        transclude: true,
        scope: {
            model: "=ngModel",
            resetFlag: "=?"
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/hourpicker/hourpicker.html',
        link: function (scope, element, attr, ctrl) {
            var flag = false;
            scope.isFromDropDownOpen = false;
            scope.isToDropDownOpen = false;
            var dropDownOpenValue = "";
            var custTime = {};
            scope.days = hourpickerConfig.days;
            scope.daysList = {};
            scope.FrtimeList = {};
            scope.FrtimeListDay = {};
            scope.TotimeListDay = {};
            scope.selectedFromOption = {};
            scope.selectedToOption = {};
            scope.TotimeList = {};
            scope.selectedIndex = 0;
            scope.selectedOption = "Select from list";
            scope.customTime = [];

            scope.resetFlag = false;
            scope.showToTimeErrorDay = {};
            scope.validatedCustomPreselect = [];

            scope.$watch('resetFlag', function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if (newVal && scope.selectedOption === hourpickerConfig.customOption) {
                        //disable and reset all days checkbox
                        for (day in scope.daysList) {
                            if (scope.daysList.hasOwnProperty(day)) {
                                scope.daysList[day] = false;
                                scope.addSelectedValue(day);
                            }
                        }
                        scope.preselectUpdateFxn(scope.preselect);
                    }
                    scope.resetFlag = false;
                }
            });

            scope.$watch('selCategory', function (value) {
                if (value) {
                    ctrl.$setViewValue(value);
                }
            }, true);

            scope.updateData = function (value) {
                if (value.constructor === Array) {
                    scope.showDaysSelector = true;
                    scope.selectedOption = hourpickerConfig.customOption;
                    for (var arry in value) {
                        if (value.hasOwnProperty(arry)) {
                            var day = value[arry].day;
                            
                            if (typeof value[arry].preEnabled === 'boolean' && value[arry].preEnabled) {
                                scope.daysList[day] = true;
                            } else {
                                scope.daysList[day] = false;
                            }
                            
                            for (var fromcount in scope.fromtime) {
                                if (scope.fromtime[fromcount].value === value[arry].FromTime && !scope.uncheckedFromTime) {
                                    scope.FrtimeList[day].value = scope.fromtime[fromcount].value;
                                    scope.selectedFromOption[day] = scope.FrtimeList[day].value;
                                }
                            }
                            for (var tocount in scope.totime) {
                                if (scope.totime[tocount].value === value[arry].ToTime && !scope.uncheckedToTime) {
                                    scope.TotimeList[day].value = scope.totime[tocount].value;
                                    scope.selectedToOption[day] = scope.TotimeList[day].value;
                                }
                            }
                            
                            scope.addSelectedValue(day, value[arry].FromTime, value[arry].ToTime);

                            //for IE8 Fix
                            if (parseInt(arry) + 1 === value.length) {
                                break;
                            }
                        }
                    }
                } else {
                    scope.selectOption(value.day);
                }
            };

            scope.$watch('preselect', function (value) {
                scope.preselectUpdateFxn(value);
            });

            scope.preselectUpdateFxn = function (value) {
                if (value !== undefined) {
                    if (scope.options) {
                        value = scope.validatePreselectData(value);
                    }
                    if (value === "") {
                        return;
                    }
                    scope.updateData(value);
                }
            };

            scope.validatePreselectData = function (value) {
                if (value.constructor === Array) {
                    for (var arry in value) {
                        if (value.hasOwnProperty(arry)) {
                            var day = value[arry].day;
                            var isDayFound = false;
                            var isFrmFound = false;
                            var isToFound = false;
                            for (var daycount in scope.days) {
                                if (scope.days[daycount] === day) {
                                    isDayFound = true;
                                    break;
                                }
                            }
                            if (!isDayFound) {
                                value.splice(arry, 1);
                                continue;
                            }
                            for (var fromcount in scope.fromtime) {
                                if (scope.fromtime[fromcount].value === value[arry].FromTime) {
                                    isFrmFound = true;
                                    break;
                                }
                            }
                            if (!isFrmFound) {
                                value[arry].FromTime = scope.fromtime[0].value;
                            }
                            for (var tocount in scope.totime) {
                                if (scope.totime[tocount].value === value[arry].ToTime) {
                                    isToFound = true;
                                    break;
                                }
                            }
                            if (!isToFound) {
                                value[arry].ToTime = scope.totime[0].value;
                            }

                            if (typeof value[arry].preEnabled === 'boolean' && value[arry].preEnabled) {
                                value[arry].preEnabled = true;
                            } else {
                                value[arry].preEnabled = false;
                            }
                            
                            scope.validatedCustomPreselect[day] = {};
                            scope.validatedCustomPreselect[day].FromTime = value[arry].FromTime;
                            scope.validatedCustomPreselect[day].ToTime = value[arry].ToTime;

                            //for IE8 Fix
                            if (parseInt(arry) + 1 === value.length) {
                                break;
                            }
                        }
                    }
                } else {
                    var isOptionFound = false;
                    for (var optcount in scope.options) {
                        if (scope.options[optcount] === value.day) {
                            isOptionFound = true;
                            break;
                        }
                    }
                    if (!isOptionFound) {
                        value = "";
                    }
                }
                return value;
            };

            scope.selectPrevNextValue = function ($event, arrayValues, currValue) {

                var value;
                var index = 0;
                if ($event.keyCode === 38) {
                    value = -1;
                } else if ($event.keyCode === 40) {
                    value = 1;
                } else {
                    return currValue;
                }

                if (arrayValues.indexOf(currValue) !== -1) {
                    index = arrayValues.indexOf(currValue) + value;
                } else {
                    for (var count in arrayValues) {
                        if (arrayValues[count].value === currValue) {
                            index = parseInt(count) + value;
                            break;
                        }
                    }
                }

                if (index === arrayValues.length) {
                    index = index - 1;
                } else if (index === -1) {
                    index = index + 1;
                }

                $event.preventDefault();
                if (arrayValues[index].value) {
                    return arrayValues[index].value;
                } else {
                    return arrayValues[index];
                }
            };

            scope.showDropdown = function () {
                scope.showlist = !scope.showlist;
                flag = !flag;
            };

            scope.showfromDayDropdown = function (value) {
                //close dropdown if any other From drop down is opened
                for (count in scope.FrtimeListDay) {
                    if (count !== value && scope.FrtimeListDay[count]) {
                        scope.FrtimeListDay[count] = false;
                    }
                }
                for (count in scope.TotimeListDay) {
                    if (scope.TotimeListDay[count]) {
                        scope.TotimeListDay[count] = false;
                    }
                }
                scope.FrtimeListDay[value] = !scope.FrtimeListDay[value];
                flag = !flag;
                scope.showlist = false;

                //save model value so we can close current dropdown on click of other part of the document
                if (scope.FrtimeListDay[value]) {
                    scope.isFromDropDownOpen = true;
                    dropDownOpenValue = value;
                } else {
                    scope.isFromDropDownOpen = false;
                }

                $timeout(function () {
                    if (scope.FrtimeListDay[value]) {
                        var daysContainerDIV = angular.element(element)[0].querySelector(".customdays-width");
                        var containerUL = angular.element(daysContainerDIV.querySelector('.select2-container-active')).parent()[0].querySelector("ul");
                        var selectedElemTopPos = angular.element(containerUL.querySelector('.selectedItemInDropDown'))[0].offsetTop;
                        angular.element(containerUL)[0].scrollTop = selectedElemTopPos;
                    }
                });
            };

            scope.showtoDayDropdown = function (value) {
                //close dropdown if any other To drop down is opened
                for (count in scope.TotimeListDay) {
                    if (count !== value && scope.TotimeListDay[count]) {
                        scope.TotimeListDay[count] = false;
                    }
                }
                for (count in scope.FrtimeListDay) {
                    if (scope.FrtimeListDay[count]) {
                        scope.FrtimeListDay[count] = false;
                    }
                }
                scope.TotimeListDay[value] = !scope.TotimeListDay[value];
                flag = !flag;
                scope.showlist = false;

                //save model value so we can close current dropdown on click of other part of the document
                if (scope.TotimeListDay[value]) {
                    scope.isToDropDownOpen = true;
                    dropDownOpenValue = value;

                } else {
                    scope.isToDropDownOpen = false;
                }

                $timeout(function () {
                    if (scope.TotimeListDay[value]) {
                        var daysContainerDIV = angular.element(element)[0].querySelector(".customdays-width");
                        var containerUL = angular.element(daysContainerDIV.querySelector('.select2-container-active')).parent()[0].querySelector("ul");
                        var selectedElemTopPos = angular.element(containerUL.querySelector('.selectedItemInDropDown'))[0].offsetTop;
                        angular.element(containerUL)[0].scrollTop = selectedElemTopPos;
                    }
                });
            };

            scope.selectFromDayOption = function (day, value) {
                scope.selectedFromOption[day] = value;
                scope.FrtimeList[day].value = value;
                scope.FrtimeListDay[day] = false;
                scope.isFromDropDownOpen = false;
            };

            scope.selectToDayOption = function (day, value) {
                scope.selectedToOption[day] = value;
                scope.TotimeList[day].value = value;
                scope.TotimeListDay[day] = false;
                scope.isToDropDownOpen = false;
            };

            scope.addSelectedValue = function (value, fromtime, totime) {
                var count, len;
                if (scope.daysList[value] !== undefined && !scope.daysList[value]) {
                    for (count = 0, len = scope.customTime.length; count < len; count++) {
                        if (scope.customTime[count].day === value) {
                            if (scope.uncheckedFromTime) {
                                scope.selectedFromOption[scope.customTime[count].day] = scope.uncheckedFromTime;
                            } else {
                                scope.selectedFromOption[scope.customTime[count].day] = scope.FrtimeList[scope.customTime[count].day].value;
                            }

                            if (scope.uncheckedToTime) {
                                scope.selectedToOption[scope.customTime[count].day] = scope.uncheckedToTime;
                            } else {
                                scope.selectedToOption[scope.customTime[count].day] = scope.TotimeList[scope.customTime[count].day].value;
                            }

                            scope.customTime.splice(count, 1);
                            break;
                        }
                    }
                } else {
                    if (scope.selectedFromOption[value] === scope.uncheckedFromTime) {

                        if (angular.isDefined(scope.validatedCustomPreselect[value])) {
                            scope.selectedFromOption[value] = scope.validatedCustomPreselect[value].FromTime;
                            fromtime = scope.validatedCustomPreselect[value].FromTime;
                            scope.FrtimeList[value].value = scope.validatedCustomPreselect[value].FromTime;
                        } else {
                            scope.selectedFromOption[value] = scope.fromtime[0].value;
                            fromtime = scope.fromtime[0].value;
                            scope.FrtimeList[value].value = scope.fromtime[0].value;
                        }

                    }

                    if (scope.selectedToOption[value] === scope.uncheckedToTime) {

                        if (angular.isDefined(scope.validatedCustomPreselect[value])) {
                            scope.selectedToOption[value] = scope.validatedCustomPreselect[value].ToTime;
                            totime = scope.validatedCustomPreselect[value].ToTime;
                            scope.TotimeList[value].value = scope.validatedCustomPreselect[value].ToTime;
                        } else {
                            scope.selectedToOption[value] = scope.totime[0].value;
                            totime = scope.totime[0].value;
                            scope.TotimeList[value].value = scope.totime[0].value;
                        }
                    }

                    custTime["day"] = value;
                    custTime["FromTime"] = scope.FrtimeList[value].value;
                    custTime["ToTime"] = scope.TotimeList[value].value;

                    for (count = 0, len = scope.customTime.length; count < len; count++) {
                        if (scope.customTime[count].day === value) {
                            scope.customTime[count].FromTime = custTime["FromTime"];
                            scope.customTime[count].ToTime = custTime["ToTime"];
                            break;
                        }
                    }
                    if (count === len) {
                        var x = angular.copy(custTime);
                        scope.customTime.push(x);
                    }
                }
                scope.selCategory = scope.customTime;
            };


            var outsideClick = function () {
                if (scope.showlist) {
                    scope.$apply(function () {
                        scope.showlist = false;
                    });
                }
            };

            $documentBind.click('showlist', outsideClick, scope);

            var outsideClickFromDropdown = function () {
                scope.$apply(function () {
                    if (scope.isFromDropDownOpen) {
                        scope.FrtimeListDay[dropDownOpenValue] = false;
                        scope.isFromDropDownOpen = false;
                    }
                });
            };

            $documentBind.click('isFromDropDownOpen', outsideClickFromDropdown, scope);

            var outsideClickToDropdown = function () {
                scope.$apply(function () {
                    if (scope.isToDropDownOpen) {
                        scope.TotimeListDay[dropDownOpenValue] = false;
                        scope.isToDropDownOpen = false;
                    }
                });
            };

            $documentBind.click('isToDropDownOpen', outsideClickToDropdown, scope);

            scope.selectOption = function (sItem) {

                if (sItem === hourpickerConfig.customOption) {
                    scope.showDaysSelector = true;
                    scope.selCategory = scope.customTime;
                } else {
                    scope.showDaysSelector = false;
                    var fromTime = /[0-9]\s?am/i.exec(sItem);
                    var toTime = /[0-9]\s?pm/i.exec(sItem);
                    scope.selCategory = {
                        day: sItem,
                        FromTime: fromTime === null ? 'NA' : fromTime[0],
                        ToTime: toTime === null ? 'NA' : toTime[0]
                    };
                }

                scope.showlist = false;
                flag = false;
                scope.selectedOption = sItem;
            };
        }
    };
}])

.directive('attHourpickerValidator', ['hourpickerConfig', function (hourpickerConfig) {
    return {
        restrict: 'A',
        require: ['attHourpicker', 'ngModel'],
        link: function (scope, element, attr, ctrl) {

            var attHourpickerCtrl = ctrl[0];
            var ngModelCtrl = ctrl[1];

            //required format [h:MM tt] like '1:10 PM'
            var convertTimeStrngToMilitaryFormat = function (time) {
                var hours = Number(time.match(/^(\d+)/)[1]);
                var minutes = Number(time.match(/:(\d+)/)[1]);
                var AMPM = (time.match(/\s(.*)$/)[1]).toUpperCase();
                if (AMPM === 'PM' && hours < 12) {
                    hours = hours + 12;
                }
                if (AMPM === 'AM' && hours === 12) {
                    hours = hours - 12;
                }
                var sHours = hours.toString();
                var sMinutes = minutes.toString();
                if (hours < 10) {
                    sHours = '0' + sHours;
                }
                if (minutes < 10) {
                    sMinutes = '0' + sMinutes;
                }
                return parseInt(sHours + sMinutes, 10);
            };

            var compareTimeStrings = function (fromTimeString, toTimeString) {
                var fromMilitaryTime = convertTimeStrngToMilitaryFormat(fromTimeString);
                var toMilitaryTime = convertTimeStrngToMilitaryFormat(toTimeString);
                return (toMilitaryTime - fromMilitaryTime);
            };

            var validateCustomData = function (finalDataModal) {

                if (attHourpickerCtrl.getSelectedOption() === hourpickerConfig.customOption) {

                    var errorDaysCount = 0;

                    for (var item in finalDataModal) {
                        if (finalDataModal.hasOwnProperty(item)) {
                            if (compareTimeStrings(finalDataModal[item].FromTime, finalDataModal[item].ToTime) <= 0) {
                                attHourpickerCtrl.setToTimeErrorDay(finalDataModal[item].day, true);
                                errorDaysCount++;
                            } else {
                                attHourpickerCtrl.setToTimeErrorDay(finalDataModal[item].day, false);
                            }
                        }
                    }

                    if (errorDaysCount > 0) {
                        //validation error
                        ngModelCtrl.$setValidity('validationStatus', false);
                        return [];
                    } else {
                        //validation successful
                        ngModelCtrl.$setValidity('validationStatus', true);
                        return finalDataModal;
                    }
                } else {
                    //default case no validation
                    ngModelCtrl.$setValidity('validationStatus', true);
                    return finalDataModal;
                }

            };

            ngModelCtrl.$parsers.unshift(validateCustomData);
        }
    };
}]);
angular.module('att.abs.iconButtons', [])
        .constant('buttonConfig', {
            activeClass: 'active--button',
            toggleEvent: 'click'
        })
        .directive('attIconBtnRadio', ['buttonConfig', function(buttonConfig) {
                var activeClass = buttonConfig.activeClass || 'active--button';
                var toggleEvent = buttonConfig.toggleEvent || 'click';
                return {
                    require: 'ngModel',
                    link: function(scope, element, attrs, ngModelCtrl) {
                        element.attr("tabindex","0");
                        element.append("<span class='hidden-spoken'>"+attrs.attIconBtnRadio+"</span>");
                        //model -> UI
                        ngModelCtrl.$render = function() {
                            element.parent().toggleClass(activeClass, angular.equals(ngModelCtrl.$modelValue, attrs.attIconBtnRadio));
                        };
                        //ui->model
                        element.parent().bind(toggleEvent, function() {
                            if (!element.parent().hasClass(activeClass)) {
                                scope.$apply(function() {
                                    ngModelCtrl.$setViewValue(attrs.attIconBtnRadio);
                                    ngModelCtrl.$render();
                                });
                            }
                        });
                    }
                };
            }])
        .directive('attIconBtnCheckbox', ['buttonConfig', function(buttonConfig) {
                var activeClass = buttonConfig.activeClass || 'active--button';
                var toggleEvent = buttonConfig.toggleEvent || 'click';
                return {
                    require: 'ngModel',
                    link: function(scope, element, attrs, ngModelCtrl) {
                        element.attr("tabindex","0");
                        element.append("<span class='hidden-spoken'>"+attrs.attIconBtnCheckbox+"</span>");
                        function getTrueValue() {
                            var trueValue = scope.$eval(attrs.btnCheckboxTrue);
                            return angular.isDefined(trueValue) ? trueValue : true;
                        }
                        function getFalseValue() {
                            var falseValue = scope.$eval(attrs.btnCheckboxFalse);
                            return angular.isDefined(falseValue) ? falseValue : false;
                        }
                        //model -> UI
                        ngModelCtrl.$render = function() {
                            element.parent().toggleClass(activeClass, angular.equals(ngModelCtrl.$modelValue, getTrueValue()));
                        };
                        //ui->model
                        element.parent().bind(toggleEvent, function() {
                            scope.$apply(function() {
                                ngModelCtrl.$setViewValue(element.parent().hasClass(activeClass) ? getFalseValue() : getTrueValue());
                                ngModelCtrl.$render();
                            });
                        });
                    }
                };
            }]);

angular.module('att.abs.links', ['ngSanitize'])
        .directive('attLink', [function() {
                return {
                    restrict: 'A',
                    link: function(scope, elem) {
                        elem.addClass('link');
                        if(!(elem.attr('href'))){
                            elem.attr("tabindex", "0");
                        }
                    }
                };
            }])
        .directive('attLinkVisited', [function() {
                return {
                    restrict: 'A',
                    link: function(scope, elem) {
                        elem.addClass('link--visited');
                        if(!(elem.attr('href'))){
                            elem.attr("tabindex", "0");
                        }
                    }
                };
            }])
        .directive('attReadmore', ['$timeout',function($timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        lines:"@noOfLines",
                        textModel: "=",
                        //attribute to use readmore inside accordion
                        isOpen: "="
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/links/readMore.html',
                    link: function(scope, elem) {
                        var height = 1;
                        scope.$watch('textModel', function(val){
                            if(!val){
                                scope.textToDisplay = '';
                                scope.readMoreLink = false;
                                scope.readLessLink = false;
                                scope.readFlag = false;
                            }
                            else{
                                if (typeof String.prototype.trim !== 'function') {
                                    String.prototype.trim = function() {
                                       return this.replace(/^\s+|\s+$/g, '');
                                    };
                                }
                                scope.textToDisplay = val.trim();
                                scope.readFlag = true;
                                $timeout(function() {
                                    var readElem = elem[0].children[0].children[0];
                                    if(height===1){
                                        if(window.getComputedStyle){
                                            height = parseInt(scope.lines) * parseFloat(window.getComputedStyle(readElem,null).getPropertyValue("height"));
                                        }
                                        else {
                                            height = parseInt(scope.lines) * parseFloat(readElem.currentStyle.height);
                                        }
                                        scope.elemHeight = height;
                                        scope.readLinkStyle = {'height': scope.elemHeight + 'px'};
                                    }
                                });
                                scope.readMoreLink = true;
                                scope.readLessLink = false;
                            }
                        });
                        // Code to use readmore inside accordion
                        var parentElem = elem.parent();
                        if (parentElem.hasClass('att-accordion__body')) {
                            scope.$watch('isOpen', function(val) {
                                if (!val) {
                                    scope.readMoreLink = true;
                                    scope.readLessLink = false;
                                    scope.readLinkStyle = {'height': scope.elemHeight + 'px'};
                                    scope.readFlag = true;
                                }
                            });
                        }
                        scope.readMore = function() {
                            scope.readMoreLink = false;
                            scope.readLessLink = true;
                            scope.readLinkStyle = {'height': 'auto'};
                            scope.readFlag = false;
                            var moreLink = angular.element(elem).children().eq(1).find('a')[0];
                            $timeout(function()
                             {
                                moreLink.focus();
                             });
                        };
                        scope.readLess = function() {
                            scope.readMoreLink = true;
                            scope.readLessLink = false;
                            scope.readLinkStyle = {'height': scope.elemHeight + 'px'};
                            scope.readFlag = true;
                            var readLessLink = angular.element(elem).children().eq(0).find('a')[0];
                            $timeout(function()
                             {
                                readLessLink.focus();
                             });
                        };
                    }
                };
            }])
        .directive('attLinksList', [function() {
                return {
                    restrict: 'A',
                    controller: function() {
                    },
                    link: function(scope, elem) {
                        elem.addClass('links-list');
                    }
                };
            }])
        .directive('attLinksListItem', [function() {
                return {
                    restrict: 'A',
                    require: '^attLinksList',
                    link: function(scope, elem) {
                        elem.addClass('links-list__item');
                        if(!(elem.attr('href'))){
                            elem.attr("tabindex", "0");
                        }
                    }
                };
            }]);
angular.module('att.abs.loading', [])
        .directive('attLoading', ['$window',function($window) {
                return {
                    restrict: 'A',
                    replace: true,
                    scope: {
                        icon: '@attLoading',
                        progressStatus: '=?',
                        colorClass: '=?'
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/loading/loading.html',
                    link: function(scope, element) {
                        var progressvalue = scope.progressStatus;
                        scope.progressStatus = Math.min(100, Math.max(0, progressvalue));
                        if($window.navigator.userAgent.indexOf("MSIE 8.")!==-1){
                            var shiftX = 0, shiftY = scope.progressStatus * 36;
                            element.css({
                                'background-position-x' : shiftX,
                                'background-position-y' : -shiftY
                            });
                        }
                    }
            };
        }]);
angular.module('att.abs.modal', ['att.abs.utilities'])
/**
 * A helper, internal data structure that acts as a map but also allows getting / removing
 * elements in the LIFO order
 */
  .factory('$$stackedMap', function () {
    return {
      createNew: function () {
        var stack = [];

        return {
          add: function (key, value) {
            stack.push({
              key: key,
              value: value
            });
          },
          get: function (key) {
            for (var i = 0; i < stack.length; i++) {
              if (key === stack[i].key) {
                return stack[i];
              }
            }
          },
          keys: function() {
            var keys = [];
            for (var i = 0; i < stack.length; i++) {
              keys.push(stack[i].key);
            }
            return keys;
          },
          top: function () {
            return stack[stack.length - 1];
          },
          remove: function (key) {
            var idx = -1;
            for (var i = 0; i < stack.length; i++) {
              if (key === stack[i].key) {
                idx = i;
                break;
              }
            }
            return stack.splice(idx, 1)[0];
          },
          removeTop: function () {
            return stack.splice(stack.length - 1, 1)[0];
          },
          length: function () {
            return stack.length;
          }
        };
      }
    };
  })

/**
 * A helper directive for the $modal service. It creates a backdrop element.
 */
  .directive('modalBackdrop', ['$timeout', function ($timeout) {
    return {
      restrict: 'EA',
      replace: true,
      templateUrl: 'app/scripts/ng_js_att_tpls/modal/backdrop.html',
      link: function (scope) {
        scope.animate = false;
        //trigger CSS transitions
        $timeout(function () {
          scope.animate = true;
        });
    }
    };
  }])

  .directive('modalWindow', ['$modalStack','$timeout','$document', function ($modalStack,$timeout,$document) {
    return {
      restrict: 'EA',
      scope: {
        index: '@',
		modalTitle: '@?'
      },
      replace: true,
      transclude: true,
      templateUrl: 'app/scripts/ng_js_att_tpls/modal/window.html',
      link: function (scope, element, attrs) {
        scope.windowClass = attrs.windowClass || '';
		if (attrs['modalTitle'] && attrs['modalTitle']!=="") {
			element[0].setAttribute('aria-label', attrs['modalTitle']);
			element[0].removeAttribute('modal-title');
		}
        $timeout(function () {
            // trigger CSS transitions
            scope.focusModalFlag = true;
            scope.animate = true;
        });
		$document.on('focus keydown', function(e){
                    if (e.which ===9) {
			String.prototype.contains = function(it) {
                            return this.indexOf(it) !== -1;
                        };
			if (element[0] !== e.target && !element[0].contains( e.target )) {
				element[0].focus();
			}
			}
		});
        scope.close = function (evt) {
            var modal = $modalStack.getTop();
            if (modal && modal.value.backdrop && modal.value.backdrop != 'static'  && (evt.target === evt.currentTarget)) {
            // Check if preventDefault exists due to lack of support for IE8
            if (evt.preventDefault) {
              evt.preventDefault();
              evt.stopPropagation();
          } else {
              evt.returnValue = false;
            }
            $modalStack.dismiss(modal.key, 'backdrop click');
          }
        };
      }
    };
  }])

  .factory('$modalStack', ['$document', '$compile', '$rootScope', '$$stackedMap', 'events', 'keymap',
    function ($document, $compile, $rootScope, $$stackedMap, events, keymap) {
      var OPENED_MODAL_CLASS = 'modal-open';
      var backdropjqLiteEl, backdropDomEl;
      var backdropScope = $rootScope.$new(true);
      var openedWindows = $$stackedMap.createNew();
      var $modalStack = {};
      var modalLaunchingElement = undefined;
      function backdropIndex() {
        var topBackdropIndex = -1;
        var opened = openedWindows.keys();
        for (var i = 0; i < opened.length; i++) {
          if (openedWindows.get(opened[i]).value.backdrop) {
            topBackdropIndex = i;
          }
        }
        return topBackdropIndex;
      }

      $rootScope.$watch(backdropIndex, function(newBackdropIndex){
        backdropScope.index = newBackdropIndex;
      });

      function removeModalWindow(modalInstance) {

        var body = $document.find('body').eq(0);
        var html = $document.find('html').eq(0);
        var modalWindow = openedWindows.get(modalInstance).value;
        //clean up the stack
        openedWindows.remove(modalInstance);
        ////remove window DOM element
        modalWindow.modalDomEl.remove();
        body.toggleClass(OPENED_MODAL_CLASS, openedWindows.length() > 0);
        html.css({overflow: 'scroll'});

        //remove backdrop if no longer needed
        if (backdropDomEl && backdropIndex() == -1) {
          backdropDomEl.remove();
          backdropDomEl = undefined;
        }
        //destroy scope
        modalWindow.modalScope.$destroy();
          
        // Shift focus
        if (angular.isDefined(modalLaunchingElement) && modalLaunchingElement != null) {
            modalLaunchingElement.focus();
        }
      }
      $document.bind('keydown', function (evt) {
        var modal;
        if (evt.which === 27) {
          modal = openedWindows.top();
          if (modal && modal.value.keyboard) {
            $rootScope.$apply(function () {
              $modalStack.dismiss(modal.key);
            });
          }
        } else if (evt.keyCode === keymap.KEY.BACKSPACE) {
          var doPrevent = false;
          var d = evt.srcElement || evt.target;
          var type;
          if (d.type === undefined) { 
                doPrevent = true;
          } else if (d.tagName.toUpperCase() === 'INPUT' && 
            ( (type = d.type.toUpperCase()) === 'TEXT' || 
              type === 'PASSWORD' || 
              type === 'FILE' ||
              type === 'SEARCH' ||
              type === 'EMAIL' ||
              type === 'NUMBER' ||
              type === 'DATE' ||
              type === 'TEL' ||
              type === 'URL' ||
              type === 'TIME')
            || d.tagName.toUpperCase() === 'TEXTAREA') {
                doPrevent = d.readOnly || d.disabled;
          } else {
            doPrevent = true;
          }
          if (doPrevent) {
              events.preventDefault(evt);
          }
        }
      });

      $modalStack.open = function (modalInstance, modal) {
          openedWindows.add(modalInstance, {
          deferred: modal.deferred,
          modalScope: modal.scope,
          backdrop: modal.backdrop,
          keyboard: modal.keyboard
        });
        
        //Before opening modal, find the focused element
        modalLaunchingElement = document.activeElement;
        
        var body = $document.find('body').eq(0);
        var html = $document.find('html').eq(0);

        if (backdropIndex() >= 0 && !backdropDomEl) {
            backdropjqLiteEl = angular.element('<div modal-backdrop></div>');
            backdropDomEl = $compile(backdropjqLiteEl)(backdropScope);
            body.append(backdropDomEl);
        }
        var angularDomEl = angular.element('<div modal-window></div>');
        angularDomEl.attr('window-class', modal.windowClass);
        angularDomEl.attr('index', openedWindows.length() - 1);
		angularDomEl.attr('modal-title', modal.modalTitle);
        angularDomEl.html(modal.content);

        var modalDomEl = $compile(angularDomEl)(modal.scope);
        openedWindows.top().value.modalDomEl = modalDomEl;
        body.append(modalDomEl);
        body.addClass(OPENED_MODAL_CLASS);
        html.css({overflow: 'hidden'});
      };

      $modalStack.close = function (modalInstance, result) {
        var modal = openedWindows.get(modalInstance);
        if (modal) {
          modal.value.deferred.resolve(result);
          removeModalWindow(modalInstance);
        }
      };

      $modalStack.dismiss = function (modalInstance, reason) {
        var modalWindow = openedWindows.get(modalInstance).value;
        if (modalWindow) {
          modalWindow.deferred.reject(reason);
          removeModalWindow(modalInstance);
        }
      };

      $modalStack.getTop = function () {
        return openedWindows.top();
      };

      return $modalStack;
    }])

  .provider('$modal', function () {

    var $modalProvider = {
      options: {
        //can be also false or 'static'
        backdrop: true,
        keyboard: true
      },
      $get: ['$injector', '$rootScope', '$q', '$http', '$templateCache', '$controller', '$modalStack',
        function ($injector, $rootScope, $q, $http, $templateCache, $controller, $modalStack) {
            var $modal = {};
            function getTemplatePromise(options) {
            return options.template ? $q.when(options.template) :
              $http.get(options.templateUrl, {cache: $templateCache}).then(function (result) {
                return result.data;
              });
          }

          function getResolvePromises(resolves) {
            var promisesArr = [];
            angular.forEach(resolves, function (value) {
              if (angular.isFunction(value) || angular.isArray(value)) {
                promisesArr.push($q.when($injector.invoke(value)));
              }
            });
            return promisesArr;
          }
          $modal.open = function (modalOptions) {
            var modalResultDeferred = $q.defer();
            var modalOpenedDeferred = $q.defer();

            //prepare an instance of a modal to be injected into controllers and returned to a caller
            var modalInstance = {
              result: modalResultDeferred.promise,
              opened: modalOpenedDeferred.promise,
              close: function (result) {
                $modalStack.close(modalInstance, result);
              },
              dismiss: function (reason) {
                $modalStack.dismiss(modalInstance, reason);
              }
            };
            //merge and clean up options
            modalOptions = angular.extend({}, $modalProvider.options, modalOptions);
            modalOptions.resolve = modalOptions.resolve || {};

            //verify options
            if (!modalOptions.template && !modalOptions.templateUrl) {
              throw new Error('One of template or templateUrl options is required.');
            }

            var templateAndResolvePromise =
              $q.all([getTemplatePromise(modalOptions)].concat(getResolvePromises(modalOptions.resolve)));
              templateAndResolvePromise.then(function(tplAndVars) {
              var modalScope = (modalOptions.scope || $rootScope).$new();
              modalScope.$close = modalInstance.close;
              modalScope.$dismiss = modalInstance.dismiss;

              var ctrlInstance, ctrlLocals = {};
              var resolveIter = 1;

              //controllers
              if (modalOptions.controller) {
                ctrlLocals.$scope = modalScope;
                ctrlLocals.$modalInstance = modalInstance;
                angular.forEach(modalOptions.resolve, function (value, key) {
                  ctrlLocals[key] = tplAndVars[resolveIter++];
                });

                ctrlInstance = $controller(modalOptions.controller, ctrlLocals);
              }

              $modalStack.open(modalInstance, {
                scope: modalScope,
                deferred: modalResultDeferred,
                content: tplAndVars[0],
                backdrop: modalOptions.backdrop,
                keyboard: modalOptions.keyboard,
                windowClass: modalOptions.windowClass,
				modalTitle: modalOptions.modalTitle
              });

            }, function(reason) {
              modalResultDeferred.reject(reason);
            });

            templateAndResolvePromise.then(function () {
              modalOpenedDeferred.resolve(true);
            }, function () {
              modalOpenedDeferred.reject(false);
            });

            return modalInstance;
          };

          return $modal;
        }]
    };

    return $modalProvider;
  })

.directive("simpleModal", ["$modal", function($modal) {
        return {
            restrict: 'EA',
            scope: {
                simpleModal: '@',
                backdrop:'@',
                keyboard:'@',
                modalOk:'&',
                modalCancel:'&',
                windowClass:'@',
                controller:'@',
				modalTitle: '@?'
            },
            link: function(scope, elm) {
                elm.bind('click', function(ev) {
                    ev.preventDefault();
                    if (angular.isDefined(elm.attr("href")) && elm.attr("href") !== "") {
                        scope.simpleModal = elm.attr("href");
                    }

                    scope.backdrop === "false" ? scope.backdropclick = 'static' : scope.backdropclick = true;
                    scope.keyboard === "false" ? scope.keyboardev = false : scope.keyboardev = true;

                    $modal.open({
                        templateUrl: scope.simpleModal,
                        backdrop:scope.backdropclick,
                        keyboard:scope.keyboardev,
                        windowClass:scope.windowClass,
                        controller: scope.controller,
						modalTitle: scope.modalTitle
                    }).result.then(scope.modalOk, scope.modalCancel);
                });
            }
        };
    }])

.directive('tabbedItem', ['$modal', '$log',function ($modal, $log){
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            items: "=items",
            controller: "@",
            templateId:"@",
			modalTitle: '@?'
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/modal/tabbedItem.html',
        controller: ['$scope', '$rootScope', '$attrs', function ($scope) {
                $scope.clickTab = function (index) {
                    for (var i = 0; i < $scope.items.length; i++) {
                        if (i === index) {
                            $scope.items[i].isTabOpen = true;
                            $scope.items[i].showData = true;
                        }
                        else {
                            $scope.items[i].isTabOpen = false;
                            $scope.items[i].showData = false;
                        }
                    }
                    var modalInstance = $modal.open({
                        templateUrl: $scope.templateId,
                        controller: $scope.controller,
                        windowClass: 'tabbedOverlay_modal',
						modalTitle: $scope.modalTitle,
                        resolve: {
                            items: function () {
                                return $scope.items;
                            }
                        }
                    });
                    modalInstance.result.then(function (selectedItem) {
                        $scope.selected = selectedItem;
                    }, function () {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                };
                $scope.isActiveTab = function (index) {
                    return $scope.items && $scope.items[index] && $scope.items[index].isTabOpen;
                };
            }]
                };
}])

.directive('tabbedOverlay', [function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            items: "="
        },
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/modal/tabbedOverlayItem.html',
        controller: ['$scope', function ($scope) {
                $scope.clickTab = function (index) {
                    for (var i = 0; i < $scope.items.length; i++) {
                        if (i === index) {
                            $scope.items[i].isTabOpen = true;
                            $scope.items[i].showData = true;
                        }
                        else {
                            $scope.items[i].isTabOpen = false;
                            $scope.items[i].showData = false;
                        }
                    }
                };
                $scope.isActiveTab = function (index) {
                    return $scope.items && $scope.items[index] && $scope.items[index].isTabOpen;
                };
            }]
    };
}]);
angular.module('att.abs.pagination', ['att.abs.utilities'])
.directive('attPagination', [ function() {
     return {
        restrict: 'EA',
        scope: {
            totalPages: '=',
            currentPage: '=',
            showInput: '=',
            clickHandler: '=?'
        },
        replace: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/pagination/pagination.html',
        link: function(scope) {
            scope.focusedPage;
            scope.$watch('totalPages', function(value) {
                if(angular.isDefined(value) && value !== null){
                    scope.pages = [];
                    if (value < 1) {
                        scope.totalPages = 1;
                        return;
                    }
                    if (value <= 7) {
                        for (var i = 1; i <= value; i++) {
                            scope.pages.push(i);
                        }
                    } else if (value > 7) {
                        var midVal = Math.ceil(value / 2);
                        scope.pages = [midVal - 1, midVal, midVal + 1];
                    }
                    currentPageChanged(1);
                }
            });
            scope.$watch('currentPage', function(value) {
                currentPageChanged(value);
            });
            var callbackHandler = function(num) {
                if (angular.isFunction(scope.clickHandler)){
                    scope.clickHandler(num);
                }
            };
            function currentPageChanged(value) {
                if (angular.isDefined(value) && value !== null) {
                    if (!value || value < 1) {
                        value = 1;
                    }
                    if (value > scope.totalPages) {
                        value = scope.totalPages;
                    }
                    if(scope.currentPage !== value) {
                        scope.currentPage = value;
                        callbackHandler(scope.currentPage);
                    }
                    if (scope.totalPages > 7) {
                        if (value < scope.pages[0] && value > 3) {
                            scope.pages = [value, value + 1, value + 2];
                        } else if (value > scope.pages[2] && value < scope.totalPages - 2) {
                           scope.pages = [value - 2, value - 1, value];
                        } else if (value <= 3) {
                             scope.pages = [1, 2, 3];
                        } else if (value >= scope.totalPages - 2) {
                             scope.pages = [scope.totalPages - 2, scope.totalPages - 1, scope.totalPages];
                        }
                    }
                }
            }
            scope.next = function(event) {
                event.preventDefault();
                if (scope.currentPage < scope.totalPages) {
                    scope.currentPage += 1;
                    callbackHandler(scope.currentPage);
                }
            };
            scope.prev = function(event) {
                event.preventDefault();
                if (scope.currentPage > 1) {
                    scope.currentPage -= 1;
                    callbackHandler(scope.currentPage);
                }
            };
            scope.selectPage = function(value, event) {
                event.preventDefault();
                scope.currentPage = value;
                scope.focusedPage = value;
                callbackHandler(scope.currentPage);
            };
            scope.checkSelectedPage = function(value) {
                if(scope.currentPage === value) {
                    return true;
                }
                return false;
            };
            scope.isFocused = function(page) {
                 return scope.focusedPage === page;
            };
        }
    };
}]);

angular.module('att.abs.paneSelector',['att.abs.utilities'])
.constant('paneGroupConstants',{
    SIDE_WIDTH_DEFAULT: '33%',
    INNER_PANE_DEFAULT: '67%',
    SIDE_PANE_ID: 'sidePane',
    NO_DRILL_DOWN: 'none'
})
.factory('animation', function(){
    return TweenLite;
}).directive('attPaneAccessibility',['keymap','$window',function(keymap,$window) {
        return{
            restrict: 'A',
            require: ['^?sidePane','^?innerPane'],
            link: function (scope, elem,attr,ctrl) {
                var sidepaneCtrl = ctrl[0],innerPaneCtrl = ctrl[1],ieFlag=false;
                scope.ie = (function () {
                    var undef,v = 3,div = document.createElement('div'),
                            all = div.getElementsByTagName('i');
                    while (
                            div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i>< ![endif]-->',
                            all[0]
                            );
                    return v > 4 ? v : undef;
                }());
                 if(scope.ie === 8){
                        ieFlag = true;
                    }
                    else{
                        ieFlag = false;
                    }
                elem.bind('keydown',function(ev){
                    if (keymap.isAllowedKey(ev.keyCode) || keymap.isControl(ev) || keymap.isFunctionKey(ev)) {
                        ev.preventDefault();
                        ev.stopPropagation();
                        var el;
                    switch (ev.keyCode) {
                        case keymap.KEY.DOWN:
                            el = angular.element(elem[0])[0];
                            if(el && el.nextElementSibling){
                                el.nextElementSibling.focus();
                            }
                            /*IE8 fix*/
                            if(ieFlag){
                                do {
                                    if (el && el.nextSibling){
                                        el = el.nextSibling;
                                    }
                                    else{
                                        break;
                                    }
                                } while (el && el.tagName !== 'DIV');
                                el.focus();
                            }
                            break;
                        case keymap.KEY.UP:
                            el = angular.element(elem[0])[0];
                            if(el && el.previousElementSibling){
                                el.previousElementSibling.focus();
                            }
                             /*IE8 fix*/
                             if(ieFlag){
                                do {
                                    if (el && el.previousSibling){
                                        el = el.previousSibling;
                                    }
                                    else{
                                        break;
                                    }
                                } while (el && el.tagName !== 'DIV');
                                el.focus();
                            }
                            break;
                        case keymap.KEY.RIGHT:
                            if(angular.isDefined(sidepaneCtrl)){
                                el = sidepaneCtrl.getElement()[0];
                            }
                            if(angular.isDefined(innerPaneCtrl)){
                                el = innerPaneCtrl.getElement()[0];
                            }
                            do {
                                if (el && el.nextElementSibling){
                                    el = el.nextElementSibling;
                                }
                                else{
                                    break;
                                }
                            }while(window.getComputedStyle(el, null).getPropertyValue("display") === 'none');
                            /*IE8 fix*/
                            if(ieFlag){
                                do {
                                    if (el && el.nextSibling){
                                        el = el.nextSibling;
                                    }
                                    else{
                                        break;
                                    }
                                }while ((el && el.tagName == 'DIV') && el.currentStyle['display'] == 'none');
                            }
                           if (el){
                            el.querySelector("[att-pane-accessibility]").focus();
                        }
                        break;
                        case keymap.KEY.LEFT:
                            if(angular.isDefined(sidepaneCtrl)){
                                el = sidepaneCtrl.getElement()[0];
                            }
                            if(angular.isDefined(innerPaneCtrl)){
                                el = innerPaneCtrl.getElement()[0];
                            }
                            do {
                                if (el && el.previousElementSibling){
                                    el = el.previousElementSibling;
                                }
                                else{
                                    break;
                                }
                                }while (window.getComputedStyle(el, null).getPropertyValue("display") == 'none');
                           
                            /*IE8 fix*/
                            if(ieFlag){
                                do {
                                    if (el && el.previousSibling){
                                        el = el.previousSibling;
                                    }
                                    else{
                                        break;
                                    }
                                }while((el && el.tagName == 'DIV') && el.currentStyle['display'] == 'none');   
                            }
                            if (el){
                            el.querySelector("[att-pane-accessibility]").focus();
                        }
                        break;
                        default:
                            break;
                        }
                    }
                });
            }
        };
}])
.directive('sideRow', [function(){
    return {
        restrict: 'A',
        replace:true,
        require: ['^sidePane','^paneGroup'],
        link: function(scope,element,attr,ctrls){
            var sidePaneCtrl = ctrls[0];
            var paneGroupCtrl = ctrls[1];
            if(scope.$first){
                /*
                Reset the sidePaneId array if a new
                set of ngRepeat data appeared
                */
                sidePaneCtrl.sidePaneIds = [];
            }
            var paneId =attr['paneId'];
            var drillDownTo = attr['drillDownTo'];
            sidePaneCtrl.sidePaneRows.push({'paneId':paneId, 'drillDownTo':drillDownTo});
            element.on('click', function(){
                sidePaneCtrl.currentSelectedRowPaneId = paneId;
                paneGroupCtrl.slideOutPane(paneId,true);
            });
        }
    };
}])
.controller('SidePaneCtrl',['$scope', '$element','animation', 'paneGroupConstants',
 function($scope,$element,animation, paneGroupConstants){
        this.getElement = function(){
            return $element;
        };
        this.sidePaneTracker = {};
        this.currentWidth = paneGroupConstants.SIDE_WIDTH_DEFAULT;
        this.paneId = paneGroupConstants.SIDE_PANE_ID;
	this.currentSelectedRowPaneId;
	this.drillDownToMapper = {};
	this.sidePaneRows = [];
	this.init = function(){
            var sidePaneRows = this.sidePaneRows;
            if(sidePaneRows){
                for(var index in sidePaneRows){
                    if (sidePaneRows.hasOwnProperty(index)) {
                        var paneId = sidePaneRows[index].paneId;
                        var drillDownTo = sidePaneRows[index].drillDownTo;
                        this.drillDownToMapper[paneId] = drillDownTo;
                        if(index == 0){
                            this.currentSelectedRowPaneId = paneId;
                            this.sidePaneTracker[paneId] = [];
                        }
                    }
                }
            }
	};
	this.getSidePanesList = function(){
            return this.sidePaneTracker[this.currentSelectedRowPaneId];
	};
	this.addToSidePanesList = function(newPaneId){
            if(this.sidePaneTracker[this.currentSelectedRowPaneId] === undefined){
                this.sidePaneTracker[this.currentSelectedRowPaneId] = [];
            }
            else if(newPaneId){
                this.sidePaneTracker[this.currentSelectedRowPaneId].push(newPaneId);
            }
	};
	this.setWidth = function(val){
            if(val){
                this.currentWidth = val;
            }
            animation.set($element,{width:this.currentWidth});
	};
	this.resizeWidth = function(val){
            if(val){
                this.currentWidth = val;
            }
            animation.to($element,.5,{width:val});
	};

}])
.directive('sidePane', ['paneGroupConstants', function(paneGroupConstants){
    return {
        restrict: 'EA',
        transclude: true,
        replace: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/paneSelector/sidePane.html',
        require: ['^paneGroup', 'sidePane'],
        controller: 'SidePaneCtrl',
        scope:{},
        link: function(scope,element,attr, ctrls){
            var paneGroupCtrl = ctrls[0];
            var sidePaneCtrl = ctrls[1];
            paneGroupCtrl.addPaneCtrl(paneGroupConstants.SIDE_PANE_ID, sidePaneCtrl);
        }
    };
}])
.directive('drillDownRow', ['$parse', 'paneGroupConstants',function($parse,paneGroupConstants){
    return {
        restrict: 'A',
        replace:true,
        require: ['^innerPane','^paneGroup'],
        link: function(scope,element,attr,ctrls){
            var innerPaneCtrl = ctrls[0];
            var paneGroupCtrl = ctrls[1];
            element.on('click', function(){
                    var drillDownTo = innerPaneCtrl.drillDownTo;
                    if(innerPaneCtrl.drillDownTo !== paneGroupConstants.NO_DRILL_DOWN){
                        paneGroupCtrl.slideOutPane(drillDownTo);
                    }
                    element[0].focus();
            });
        }
    };
}])
.controller('InnerPaneCtrl', ['$scope', '$element','animation', 'paneGroupConstants',
	function($scope,$element,animation,paneGroupConstants){
        this.getElement = function(){
            return $element;
        };
	this.paneId = $scope.paneId;
	this.drillDownTo;
	this.currentWidth = paneGroupConstants.INNER_PANE_DEFAULT;
	this.setWidth = function(val){
            if(val){
                this.currentWidth = val;
            }
            animation.set($element,{width:this.currentWidth});
	};
	this.resizeWidth = function(val,callback){
            animation.to($element,.25,{width:val,onComplete: callback});
	};
	this.displayNone = function(){
            animation.set($element, {display:'none'});
	};
	this.displayBlock = function(){
            animation.set($element,{display:'block'});
            if(this){
                this.hideRightBorder();
            }
	};
	this.floatLeft = function(){
            animation.set($element,{float:'left'});
	};
	this.hideLeftBorder = function(){
            animation.set($element, {borderLeftWidth: '0px'});
	};
	this.showLeftBorder = function(){
            animation.set($element,{borderLeftWidth: '1px'});
	};
	this.hideRightBorder = function(){
            animation.set($element,{borderRightWidth: '0px'});
	};
	this.showRightBorder = function(){
            animation.set($element, {borderRightWidth: '1px'});
	};
	this.slideFromRight = function(){
            animation.set($element, {float:'right'});
            animation.set($element, {width: this.currentWidth});
	};
	this.startOpen = function(){
            return $scope.startOpen;
	};
}])
.directive('innerPane', function(){
    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/paneSelector/innerPane.html',
        require: ['^paneGroup', 'innerPane'],
        controller: 'InnerPaneCtrl',
        scope:{
                paneId:'@'
        },
        link: function(scope,element,attr,ctrls){
            if(attr.startOpen === ""){
                scope.startOpen  = true;
            }
            var paneGroupCtrl = ctrls[0];
            var innerPaneCtrl = ctrls[1];
            paneGroupCtrl.addPaneCtrl(scope.paneId,innerPaneCtrl);
        }
    };
})
.controller('PaneGroupCtrl', ['$scope', '$element', 'paneGroupConstants',function($scope,$element,paneGroupConstants){
        this.panes = {};
	this.accountLevelPaneModel = [];
	this.title = $scope.title;
	this.init = function(){
		var sidePane = this.panes[paneGroupConstants.SIDE_PANE_ID];
		if(sidePane){
                    sidePane.init();

                    //Show the other panes that may be set to startOpen
                    //numOpen starts at 1 because of the side pane
                    var numOpen = 1;
                    var key;
                    for(key in this.panes){
                        if(this.panes[key].startOpen && this.panes[key].startOpen()){
                                numOpen++;
                        }
                    }
                    var width;
                    if(numOpen >= 3){
                        width = ((100/numOpen)) + '%';
                    }
                    if(this.panes[sidePane.currentSelectedRowPaneId])
                    {
                        if(width){
                            sidePane.setWidth(width);
                            this.panes[sidePane.currentSelectedRowPaneId].setWidth(width);
                        }
                        else{
                            sidePane.setWidth();
                            this.panes[sidePane.currentSelectedRowPaneId].setWidth();
                        }
                        this.panes[sidePane.currentSelectedRowPaneId].displayBlock();
                        for(key in this.panes){
                            if(key !== paneGroupConstants.SIDE_PANE_ID && key !== sidePane.currentSelectedRowPaneId){
                                this.panes[key].displayNone();
                            }
                                this.panes[key].drillDownTo = sidePane.drillDownToMapper[key];
                        }
                    }
                    openOtherPanesOnStart(sidePane, this.panes);
                }

		function openOtherPanesOnStart(sidePane, panes){
                    //Build an array of the panes that need to be out
                    var otherPanesStartOpened = [];
                    var index;
                    for(index in sidePane.sidePaneRows){
                        if (sidePane.sidePaneRows.hasOwnProperty(index)) {
                            var pane = sidePane.sidePaneRows[index];

                            //Skip the first pane row since we handled it in the begining
                            if(index > 0 && panes[pane.paneId].startOpen && panes[pane.paneId].startOpen()){
                                    otherPanesStartOpened.push(pane);
                                    //Remember the panes that are opened for the first pane row Index
                                    sidePane.addToSidePanesList(pane.paneId);
                            }
                        }
                    }

                    if(width){
                        for(index in otherPanesStartOpened){
                            if (otherPanesStartOpened.hasOwnProperty(index)) {
                                var paneId = otherPanesStartOpened[index].paneId;
                                var paneCtrl = panes[paneId];
                                if(paneCtrl && paneCtrl.setWidth && paneCtrl.displayBlock){
                                    paneCtrl.setWidth(width);
                                    paneCtrl.displayBlock();
                                }
                            }
                        }
                    }

		}
	};

	/*
	  Resets all the panels to their original positions at the end of a sidebar click
	  By setting the sideBar to its default width
	  Setting all panes to float left and displaynone
	  Setting the pane that was clicked to default width and slide right
	*/

	this.resetPanes = function(){
            for(var key in this.panes){
                if(this.panes.hasOwnProperty(key)){
                    var pane = this.panes[key];
                    if(pane && (pane.paneId !== paneGroupConstants.SIDE_PANE_ID)){
                            pane.floatLeft();
                            pane.displayNone();
                    }
                }
            }

            if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                this.panes[paneGroupConstants.SIDE_PANE_ID].setWidth(paneGroupConstants.SIDE_WIDTH_DEFAULT);
            }
	};

	this.addPaneCtrl = function(paneId,paneCtrl){
            this.panes[paneId] = paneCtrl;
	};

	this._slideOutPane = function(paneId,isFromSidePane){
		this.resetPanes();
                //Check current side pane stack to see how many panes are already open for that side pane choice
		//then add the new pane that needs to be there
                var panesList;
                if(isFromSidePane){

                    //Check if the side pane id has already been clicked
                    if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                        panesList = this.panes[paneGroupConstants.SIDE_PANE_ID].getSidePanesList();
                    }
                    if(!panesList){
                        if(this.panes && this.panes[paneGroupConstants.SIDE_PANE_ID] && this.panes[paneId]){
                            this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId = paneId;
                            this.panes[paneGroupConstants.SIDE_PANE_ID].addToSidePanesList();

                            this.panes[paneId].slideFromRight();
                            this.panes[paneId].displayBlock();

                            this.panes[paneId].setWidth(paneGroupConstants.INNER_PANE_DEFAULT);
                        }
                    }
                    else if(this.panes && this.panes[paneGroupConstants.SIDE_PANE_ID]){
				//Restore the panes based on the panelist
                        if(panesList.length === 0 && this.panes[paneId]){
                            //Only one pane is out
                            this.panes[paneGroupConstants.SIDE_PANE_ID].setWidth(paneGroupConstants.SIDE_WIDTH_DEFAULT);
                            this.panes[paneId].displayBlock();
                            this.panes[paneId].setWidth(paneGroupConstants.INNER_PANE_DEFAULT);
                        }
                        else{
                            //Multiple panes out
                            var numPanes = panesList.length + 2;
                            var width = ((100/numPanes)) + '%';
                            this.panes[paneGroupConstants.SIDE_PANE_ID].setWidth(width);

                            //Set the sidePanes pane
                            //set the panes children list
                            if(this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId]){
                                this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].displayBlock();
                                this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].setWidth(width);
                            }
                            for(var i in panesList){
                                if(this.panes[panesList[i]]){
                                        this.panes[panesList[i]].displayBlock();
                                        this.panes[panesList[i]].setWidth(width);
                                }
                            }
                        }
                    }
                }
		else{

                    //Have to check the paneId that was given and where it is drilling down to
                    var isPaneInStack = false;
                    var stackPaneList;
                    if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                        stackPaneList = this.panes[paneGroupConstants.SIDE_PANE_ID].getSidePanesList();
                    }
                    for(var j in stackPaneList){
                        if(stackPaneList.hasOwnProperty(j)){
                            var pId = stackPaneList[j];
                            if(pId === paneId){
                                isPaneInStack = true;
                                break;
                            }
                        }
                    }
                    if(!isPaneInStack && this.panes[paneGroupConstants.SIDE_PANE_ID]){
                        this.panes[paneGroupConstants.SIDE_PANE_ID].addToSidePanesList(paneId);
                    }
                    var sidePanesListLength;
                    if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                        sidePanesListLength = this.panes[paneGroupConstants.SIDE_PANE_ID].getSidePanesList().length;
                    }
                    var numberPanes = sidePanesListLength + 2;
                    var widthToSet = ((100/numberPanes)) + '%';
                    if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                        this.panes[paneGroupConstants.SIDE_PANE_ID].setWidth(widthToSet);
                    }
                    var slideInPaneId;

                    if(this.panes[paneGroupConstants.SIDE_PANE_ID]){
                            slideInPaneId = this.panes[paneGroupConstants.SIDE_PANE_ID].getSidePanesList()[sidePanesListLength - 1];
                    }

                    var that = this;

                    if(that.panes[paneGroupConstants.SIDE_PANE_ID]){
                            panesList = that.panes[paneGroupConstants.SIDE_PANE_ID].getSidePanesList();
                    }

                    for(var p in panesList){
                        if(panesList.hasOwnProperty(p)){
                            var paneListPaneId = panesList[p];
                            var pane = this.panes[paneListPaneId];
                            if(paneListPaneId !== slideInPaneId && pane){
                                    pane.setWidth(widthToSet);
                                    pane.displayBlock();
                                    pane.floatLeft();
                            }
                        }
                    }

                    if(this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId]){
                            this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].displayBlock();
                            this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].showRightBorder();

                            this.panes[this.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].resizeWidth(widthToSet,function(){

                                    if(that.panes[slideInPaneId] && that.panes[that.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId]){
                                            that.panes[that.panes[paneGroupConstants.SIDE_PANE_ID].currentSelectedRowPaneId].hideRightBorder();
                                            that.panes[slideInPaneId].setWidth(widthToSet);
                                            that.panes[slideInPaneId].slideFromRight();
                                            that.panes[slideInPaneId].displayBlock();
                                            that.panes[slideInPaneId].floatLeft();
                                    }

                            });
                    }
                }
	};

	this.slideOutPane = function(paneId,isFromSidePane){
            this._slideOutPane(paneId,isFromSidePane);
	};
}])
.directive('paneGroup', ['$timeout',function($timeout){
	return {
		restrict: 'EA',
		transclude: true,
		replace: true,
		templateUrl: 'app/scripts/ng_js_att_tpls/paneSelector/paneGroup.html',
		scope:{
		},
		controller: 'PaneGroupCtrl',
		link: function(scope,element,attr,ctrl){
			$timeout(initialize,100);
			function initialize(){
				ctrl.init();
			}
		}
	};
}]);
angular.module('att.abs.tooltip', ['att.abs.position', 'att.abs.utilities', 'ngSanitize'])
        // The default options tooltip and popover.
        .constant('tooltipDefaultOptions', {
            placement: 'above',
            animation: false,
            popupDelay: 0,
            stylett: 'dark',
            appendToBody: true
        })

        /**
         * The $tooltip service creates tooltip- and popover-like directives as well as
         * houses global options for them.
         */
        .provider('$tooltip', ['tooltipDefaultOptions', function(tooltipDefaultOptions) {

            // Default hide triggers for each show trigger
            var triggerMap = {
                'mouseenter': 'mouseleave',
                'click': 'click',
                'focus': 'blur',
                'mouseover':'mouseout'
            };

            // The options specified to the provider globally.
            var globalOptions = {};

            this.options = function(value) {
                angular.extend(globalOptions, value);
            };

            /**
             * This allows you to extend the set of trigger mappings available. E.g.:
             *
             *   $tooltipProvider.setTriggers( 'openTrigger': 'closeTrigger' );
             */
            this.setTriggers = function(triggers) {
                angular.extend(triggerMap, triggers);
            };

            /**
             * This is a helper function for translating camel-case to snake-case.
             */
            function snakeCase(name) {
                var regexp = /[A-Z]/g;
                var separator = '-';
                return name.replace(regexp, function(letter, pos) {
                    return (pos ? separator : '') + letter.toLowerCase();
                });
            }

            /**
             * Returns the actual instance of the $tooltip service.
             */
            this.$get = ['$window', '$compile', '$timeout', '$parse', '$document', '$position', '$interpolate', '$attElementDetach', function($window, $compile, $timeout, $parse, $document, $position, $interpolate, $attElementDetach) {
                    return function (type, prefix, defaultTriggerShow) {
                        var options = angular.extend({}, tooltipDefaultOptions, globalOptions);
                        /**
                         * Returns an object of show and hide triggers.
                         *
                         * If a trigger is supplied,
                         * it is used to show the tooltip; otherwise, it will use the `trigger`
                         * option passed to the `$tooltipProvider.options` method; else it will
                         * default to the trigger supplied to this directive factory.
                         *
                         * The hide trigger is based on the show trigger. If the `trigger` option
                         * was passed to the `$tooltipProvider.options` method, it will use the
                         * mapped trigger from `triggerMap` or the passed trigger if the map is
                         * undefined; otherwise, it uses the `triggerMap` value of the show
                         * trigger; else it will just use the show trigger.
                         */
                        function getTriggers(trigger) {
                            var show = trigger || options.trigger || defaultTriggerShow;
                            var hide = triggerMap[show] || show;
                            return {
                                show: show,
                                hide: hide
                            };
                        }

                        var directiveName = snakeCase(type);

                        var startSym = $interpolate.startSymbol();
                        var endSym = $interpolate.endSymbol();

                        return {
                            restrict: 'EA',
                            scope: true,
                            link: function (scope, element, attrs) {
                                /* Allows a developer to force element to be non-tabable */
                                if (!element.attr('tabindex')) {
                                    element.attr('tabindex', '0');
                                } 

                                var isElementHovered = false;
                                element.bind('mouseenter', function(){
                                    isElementHovered = true;
                                    element.removeAttr('title');
                                });
                                element.bind('mouseleave', function(){
                                    isElementHovered = false;
                                 //   setTooltipAriaLabel();
                                });

                                /* We store our attributes on our scope so any user of $tooltip service can access attributes */
                                scope.parentAttrs = attrs;
                                var template =
                                        '<div ' + directiveName + '-popup ' +
                                        'title="' + startSym + 'tt_title' + endSym + '" ' +
                                        'content="' + startSym + 'tt_content' + endSym + '" ' +
                                        'placement="' + startSym + 'tt_placement' + endSym + '" ' +
                                        'animation="tt_animation()" ' +
                                        'is-open="tt_isOpen" ' +
                                        'stylett="' + startSym + 'tt_style' + endSym + '" ' +
                                        '>' +
                                        '</div>';

                                var tooltip = $compile(template)(scope);
                                var transitionTimeout;
                                var popupTimeout;
                                var $body;
                                var appendToBody = angular.isDefined(options.appendToBody) ? options.appendToBody : false;
                                var triggers = getTriggers(undefined);
                                var hasRegisteredTriggers = false;
                                var hasEnableExp = angular.isDefined(attrs[prefix + 'Enable']);
                                var tooltipOffset = 0;
                                var tooltipAriaLabelDefined = false;

                                // By default, the tooltip is not open.
                                // add ability to start tooltip opened
                                scope.tt_isOpen = false;

                                //Adding a scope watch, to remove the created popup from DOM, incase it is updated outside the provider code.
                                scope.$watch('tt_isOpen', function(newVal, oldVal){
                                    if(newVal !== oldVal && !newVal){
                                        $attElementDetach(tooltip[0]);
                                    }
                                });

                                function toggleTooltipBind() {
                                    if (!scope.tt_isOpen) {
                                        showTooltipBind();
                                    } else {
                                        hideTooltipBind();
                                    }
                                }

                                // Show the tooltip with delay if specified, otherwise show it immediately
                                function showTooltipBind() {
                                    if (hasEnableExp && !scope.$eval(attrs[prefix + 'Enable'])) {
                                        return;
                                    }
                                    if (scope.tt_popupDelay) {
                                        popupTimeout = $timeout(show, scope.tt_popupDelay);
                                    } else {
                                        scope.$apply(show);
                                    }
                                }

                                function hideTooltipBind() {
                                    scope.$apply(function() {
                                        hide();
                                    });
                                }

                                // Show the tooltip popup element.
                                function show() {
                                    var position,
                                            ttWidth,
                                            ttHeight,
                                            ttPosition;

                                    // Don't show empty tooltips.
                                    if (!scope.tt_content) {
                                        return;
                                    }

                                    // If there is a pending remove transition, we must cancel it, lest the
                                    // tooltip be mysteriously removed.
                                    if (transitionTimeout) {
                                        $timeout.cancel(transitionTimeout);
                                    }

                                    // Set the initial positioning.
                                    tooltip.css({top: 0, left: 0, display: 'block', 'z-index': 9999});

                                    // Now we add it to the DOM because need some info about it. But it's not
                                    // visible yet anyway.
                                    if (appendToBody) {
                                        $body = $body || $document.find('body');
                                        $body.append(tooltip);
                                    } else {
                                        element.after(tooltip);
                                    }

                                    // Get the position of the directive element.
                                    position = appendToBody ? $position.offset(element) : $position.position(element);

                                    // Get the height and width of the tooltip so we can center it.
                                    ttWidth = tooltip.prop('offsetWidth');
                                    ttHeight = tooltip.prop('offsetHeight');

                                    // Calculate the tooltip's top and left coordinates to center it with
                                    // this directive.
                                    var ttArrowOffset = 10;
                                    switch (scope.tt_placement) {
                                        case 'right':
                                            if(appendToBody){
                                                ttPosition = {
                                                    top: position.top + position.height / 2 - ttHeight / 2,
                                                    left: (position.left + position.width) + tooltipOffset
                                                };
                                            }else{
                                                ttPosition = {
                                                    top: position.top + position.height / 2 - ttHeight / 2,
                                                    left: (position.left + position.width + ttArrowOffset) + tooltipOffset
                                                };
                                            }
                                            break;
                                        case 'below':
                                            if(appendToBody){
                                                ttPosition = {
                                                    top: (position.top + position.height) + tooltipOffset,
                                                    left: position.left + position.width / 2 - ttWidth / 2
                                                };
                                            }else{
                                                ttPosition = {
                                                    top: (position.top + position.height + ttArrowOffset) + tooltipOffset,
                                                    left: position.left + position.width / 2 - ttWidth / 2
                                                };
                                            }
                                            break;
                                        case 'left':
                                            if(appendToBody){
                                                ttPosition = {
                                                    top: position.top + position.height / 2 - ttHeight / 2,
                                                    left: (position.left - ttWidth) - tooltipOffset
                                                };
                                            }else{
                                                ttPosition = {
                                                    top: position.top + position.height / 2 - ttHeight / 2,
                                                    left: (position.left - ttWidth - ttArrowOffset) - tooltipOffset
                                                };
                                            }
                                            break;
                                        default:
                                            if(appendToBody){
                                                ttPosition = {
                                                    top: (position.top - ttHeight) - tooltipOffset,
                                                    left: position.left + position.width / 2 - ttWidth / 2
                                                };
                                            }else{
                                                ttPosition = {
                                                    top: (position.top - ttHeight - ttArrowOffset) - tooltipOffset,
                                                    left: position.left + position.width / 2 - ttWidth / 2
                                                };
                                            }
                                            break;
                                    }

                                    ttPosition.top += 'px';
                                    ttPosition.left += 'px';

                                    // Now set the calculated positioning.
                                    tooltip.css(ttPosition);

                                    // And show the tooltip.
                                    scope.tt_isOpen = true;
                                }

                                // Hide the tooltip popup element.
                                function hide() {
                                    // First things first: we don't show it anymore.
                                    scope.tt_isOpen = false;

                                    //if tooltip is going to be shown after delay, we must cancel this
                                    $timeout.cancel(popupTimeout);

                                    // And now we remove it from the DOM. However, if we have animation, we
                                    // need to wait for it to expire beforehand.
                                    // This is a placeholder for a port of the transitions library.
                                    if (angular.isDefined(scope.tt_animation) && scope.tt_animation()) {
                                        transitionTimeout = $timeout(function() {
                                            $attElementDetach(tooltip[0]);
                                        }, 500);
                                    } else {
                                        $attElementDetach(tooltip[0]);
                                    }
                                }

                                function setTooltipAriaLabel() {
                                    element.removeAttr('title');
                                    if(!isElementHovered){
                                        if (tooltipAriaLabelDefined) {
                                            element.attr('title', scope.tooltipAriaLabel);
                                        } else {
                                            element.attr('title', scope.tt_content);
                                        }
                                    }
                                }

                                /**
                                 * Observe the relevant attributes.
                                 */
                                attrs.$observe(type, function(val) {
                                    if (val) {
                                        scope.tt_content = val;
                                       // setTooltipAriaLabel();
                                    } else {
                                        if (scope.tt_isOpen) {
                                            hide();
                                        }
                                    }
                                });

                                attrs.$observe(prefix + 'Title', function(val) {
                                    scope.tt_title = val;
                                });

                                attrs.$observe(prefix + 'Placement', function(val) {
                                    scope.tt_placement = angular.isDefined(val) ? val : options.placement;
                                });

                                attrs.$observe(prefix + 'Style', function(val) {
                                    scope.tt_style = angular.isDefined(val) ? val : options.stylett;
                                });

                                attrs.$observe(prefix + 'Animation', function(val) {
                                    scope.tt_animation = angular.isDefined(val) ? $parse(val) : function() {
                                        return options.animation;
                                    };
                                });

                                attrs.$observe(prefix + 'PopupDelay', function(val) {
                                    var delay = parseInt(val, 10);
                                    scope.tt_popupDelay = !isNaN(delay) ? delay : options.popupDelay;
                                });

                                attrs.$observe(prefix + 'Trigger', function(val) {

                                    if (hasRegisteredTriggers) {
                                        element.unbind(triggers.show, showTooltipBind);
                                        element.unbind(triggers.hide, hideTooltipBind);
                                    }

                                    triggers = getTriggers(val);

                                    /* This fixes issue in which a click on input field with trigger as focus 
                                        causes focus to fire following click thus making tooltip flash. */
                                    if (triggers.show === 'focus') {
                                        element.bind('focus', showTooltipBind);
                                        element.bind('blur', hideTooltipBind);
                                        element.bind('click', function(e) {
                                            e.stopPropagation();
                                        });
                                    } else if (triggers.show === triggers.hide) {
                                        element.bind(triggers.show, toggleTooltipBind);
                                    } else {
                                        element.bind(triggers.show, showTooltipBind);
                                        element.bind(triggers.hide, hideTooltipBind);
                                    }

                                    hasRegisteredTriggers = true;
                                });

                                attrs.$observe(prefix + 'AppendToBody', function (val) {
                                    appendToBody = angular.isDefined(val) ? $parse(val)(scope) : appendToBody;
                                });

                                attrs.$observe(prefix + 'Offset', function (val) {
                                    tooltipOffset = angular.isDefined(val) ? parseInt(val, 10) : 0;
                                });

                                attrs.$observe(prefix + 'AriaLabel', function (val) {
                                    if (angular.isDefined(val)) {
                                        scope.tooltipAriaLabel = val;
                                        tooltipAriaLabelDefined = true;
                                    } else {
                                        tooltipAriaLabelDefined = false;
                                    }
                                    setTooltipAriaLabel();
                                });

                                // if a tooltip is attached to <body> we need to remove it on
                                // location change as its parent scope will probably not be destroyed
                                // by the change.
                                if (appendToBody) {
                                    scope.$on('$locationChangeSuccess', function() {
                                        if (scope.tt_isOpen) {
                                            hide();
                                        }
                                    });
                                }

                                // Make sure tooltip is destroyed and removed.
                                scope.$on('$destroy', function() {
                                    if (scope.tt_isOpen) {
                                        hide();
                                    } else {
                                        tooltip.remove();
                                    }
                                });
                            }
                        };
                    };
                }];
        }])

        .directive('tooltipPopup', ['$document', '$documentBind', function($document, $documentBind) {
            return {
                restrict: 'EA',
                replace: true,
                transclude: true,
                scope: {content: '@', placement: '@', animation: '&', isOpen: '=', stylett: '@'},
                templateUrl: 'app/scripts/ng_js_att_tpls/tooltip/tooltip-popup.html',
                link: function(scope, elem) {
                    scope.$watch("isOpen", function() {
                        scope.isOpen;
                    });
                    elem.bind('click', function (e) {
                        e.stopPropagation();
                    });
                    var outsideClick = function() {
                        scope.$apply(function() {
                            scope.isOpen = false;
                        });
                    };

                    $documentBind.event('click', 'isOpen', outsideClick, scope, true, 10);
                }
            };
        }])

        .directive('tooltip', ['$tooltip', function($tooltip) {
            return $tooltip('tooltip', 'tooltip', 'mouseenter');
        }])

        .directive('tooltipCondition', [ '$timeout',function($timeout) {
                return  {
                    restrict: 'EA',
                    replace: true,
                    scope:{
                        tooltipCondition:"@?"
                    },
                    template:'<p><span tooltip=\"{{tooltipCondition}}\" ng-if=\"showpop\">{{tooltipCondition}}</span><span id=\"innerElement\" ng-hide=\"showpop\">{{tooltipCondition}}</span></p>',
                    link: function(scope, elem, attr){
                        scope.showpop=false;
                        if(attr.height==='true'){
                            $timeout(function () {
                               var maxHeight=(elem[0].offsetHeight);
                               var elemHeight=elem.children(0)[0].offsetHeight;
                               if(elemHeight > maxHeight){
                                   scope.showpop=true;
                               }
                            });
                        }
                        else if(scope.tooltipCondition.length>=25){
                        scope.showpop=true;
                        }
                    }
                };
        }]);
angular.module('att.abs.popOvers', ['att.abs.tooltip', 'att.abs.utilities', 'ngSanitize'])
        .directive('popover', ['$tooltip', function($tooltip) {
                return $tooltip('popover', 'popover', 'click');
            }])
        .directive('popoverPopup', ['$document', '$documentBind', '$timeout', 'events', 'DOMHelper', function($document, $documentBind, $timeout, events, DOMHelper) {
                return {
                    restrict: 'EA',
                    replace: true,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/popOvers/popOvers.html',
                    scope: {content: '@', placement: '@', animation: '&', isOpen: '=', stylett: '@'},
                    link: function(scope, elem, attr, ctrl) {

                        scope.closeable = false;
                        try {
                            scope.closeable = scope.$parent.parentAttrs['closeable'] === '' ? true : false;
                        } catch (e) {}

                        /* Before opening modal, find the focused element */
                        var launchingElement = undefined, 
                            firstTabableElement = undefined;

                        var outsideClick = function(evt) {
                            scope.$apply(function() {
                                scope.isOpen = false;
                            });
                        };
                        var escKeydown = function(evt) {
                            if (evt.which === 27 || evt.keyCode === 27) {
                                    console.log('ESC was pressed!');
                                    scope.$apply(function() {
                                        scope.isOpen = false;
                                });
                            }
                        };

                        $timeout(function() {
                            firstTabableElement = DOMHelper.firstTabableElement(elem);
                        }, 10, false);
                        
                        scope.$watch('isOpen', function(value) {
                            if (scope.isOpen) {
                                launchingElement = document.activeElement;
                                /* Focus on first tabbable element */
                                if (angular.isDefined(firstTabableElement)) {
                                    try {
                                        firstTabableElement.focus();
                                    } catch(e) {}
                                }
                            } else {
                                if (angular.isDefined(launchingElement)) {
                                    try {
                                        launchingElement.focus();
                                    } catch (e) {} /* IE8 will throw exception */
                                }
                            }
                        });
                        
                        scope.$watch("stylett", function(value) {
                            scope.popOverStyle = value;
                        });

                        scope.$watch("placement", function(value) {
                            scope.popOverPlacement = value;
                        });
                        
                        scope.closeMe = function(){
                           scope.isOpen = false;
                        };
                        
                        elem.bind('click', function (e) {
                            events.stopPropagation(e);
                        });
                        
                        $documentBind.event('click', 'isOpen', outsideClick, scope, true, 10);
                        $documentBind.event('keydown', 'isOpen', escKeydown, scope, true, 10);
                    }
                };
            }]);

angular.module('att.abs.profileCard', [])
            .constant('profileStatus',{
                status:{
                    ACTIVE:{status:"Active",color:"green"},
                    DEACTIVATED:{status:"Deactivated",color:"red"},
                    LOCKED:{status:"Locked",color:"red"},
                    IDLE:{status:"Idle",color:"yellow"},
                    PENDING:{status:"Pending",color:"blue"}
                    },
                role:"COMPANY ADMINISTRATOR"
                })
            .directive('profileCard',['$http','$q','profileStatus', function($http,$q,profileStatus) {
               return {
                    restrict: 'EA',
                    replace:'true',
                    templateUrl:function(element, attrs){
                        if(!attrs.addUser){
                            return 'app/scripts/ng_js_att_tpls/profileCard/profileCard.html';
                        }
                        else{
                            return 'app/scripts/ng_js_att_tpls/profileCard/addUser.html';
                        }
                    },
                    scope:{
                        profile:'='
                    },
                    link: function(scope, elem, attr){
                        scope.image=true;
                        function isImage(src) {
                            var deferred = $q.defer();
                            var image = new Image();
                            image.onerror = function() {
                                deferred.reject(false);
                            };
                            image.onload = function() {
                                deferred.resolve(true);
                            };
                            if(src!==undefined && src.length>0 ){
                                image.src = src;
                            }else{
                                 deferred.reject(false);
                            }
                            return deferred.promise;
                        }
                        if(!attr.addUser){
                        scope.image=false;
                        isImage(scope.profile.img).then(function(img) {
                            scope.image=img;
                        });
                        var splitName=(scope.profile.name).split(' ');
                        scope.initials='';
                        for(var i=0;i<splitName.length;i++){
                            scope.initials += splitName[i][0];
                        }
                        if(scope.profile.role.toUpperCase()===profileStatus.role){
                            scope.badge=true;
                        }
                        var profileState=profileStatus.status[scope.profile.state.toUpperCase()];
                        if(profileState) {
                            scope.profile.state=profileStatus.status[scope.profile.state.toUpperCase()].status;
                            scope.colorIcon=profileStatus.status[scope.profile.state.toUpperCase()].color;
                            if(scope.profile.state.toUpperCase()===profileStatus.status.PENDING.status.toUpperCase()||scope.profile.state.toUpperCase()===profileStatus.status.LOCKED.status.toUpperCase()){
                                    scope.profile.lastLogin=scope.profile.state;
                            }
                        }
                        var today=new Date().getTime();
                        var lastlogin=new Date(scope.profile.lastLogin).getTime();
                        var diff=(today-lastlogin)/(1000*60*60*24);
                        if(diff<=1){
                            scope.profile.lastLogin="Today";
                        }
                        else if(diff<=2){
                            scope.profile.lastLogin="Yesterday";
                        }
                    }
                }
            };
        }]);
angular.module('att.abs.progressBars', [])

.directive('attProgressBar', [function(){
    return {
        restrict: 'A',
        replace: true,
        templateUrl : 'app/scripts/ng_js_att_tpls/progressBars/progressBars.html'
    };
}]);
angular.module('att.abs.radio', [])
    .constant('attRadioConfig', {
        activeClass : "att-radio--on",
        disabledClass : "att-radio--disabled"
    })
.directive('attRadio', ['$compile','attRadioConfig', function ($compile, attRadioConfig) {
    return {
        scope: {},
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attr, ctrl) {
            var ngCtrl = ctrl;
            scope.radioVal='';
            var parentDiv = angular.element('<div att-accessibility-click="13,32" ng-click="updateModel($event)" class="att-radio"></div>');
            element.attr("value",attr.attRadio);
            element.removeAttr("att-radio");
            element.removeAttr("title");
            element.attr("ng-model","radioVal");
            parentDiv.append(element.prop('outerHTML'));
            parentDiv.append('<div class="att-radio__indicator"></div>');
            parentDiv.attr("title", attr.title);

            var elm = parentDiv.prop('outerHTML');
            elm = $compile(elm)(scope);
            element = element.replaceWith(elm);
            var radioElm = elm.find("input");

            radioElm.on('focus', function() {                
                elm.css("outline","2px solid #5E9ED6");
               // elm.css("outline","-mos-focus-ring-color auto 5px");
                elm.css("outline","-webkit-focus-ring-color auto 5px");
               
            });
            radioElm.on('blur', function() {
               elm.css("outline","none");
            });

            ngCtrl.$render = function () {
                scope.radioVal = ngCtrl.$modelValue;
                var selected = angular.equals(ngCtrl.$modelValue, attr.attRadio);                
                elm.toggleClass(attRadioConfig.activeClass, selected);
            };

            scope.updateModel = function () {
               radioElm[0].focus();
                var isActive = elm.hasClass(attRadioConfig.activeClass);

                if (!isActive && !scope.disabled) {
                    ngCtrl.$setViewValue(isActive ? null : attr.attRadio);	
                    ngCtrl.$render();
                }
            };

            attr.$observe('disabled', function (val) {
                scope.disabled = (val || val === "disabled" || val === "true");
                if (scope.disabled){
                    elm.addClass(attRadioConfig.disabledClass);
                    elm.attr("tabindex", "-1");
                }else { 
                    elm.removeClass(attRadioConfig.disabledClass); 
                    elm.attr("tabindex", "0"); 
                }
            });
        }
    };
}]);
angular.module('att.abs.scrollbar', ['att.abs.position'])

.constant('attScrollbarConstant', {
    defaults: {
        // Vertical or horizontal scrollbar? ( x || y ).
        axis: 'y',
        // Whether navigation pane is required of not.
        navigation: false,
        // Enable or disable the mousewheel.
        wheel: true,
        // How many pixels must the mouswheel scroll at a time.
        wheelSpeed: 40,
        // Lock default scrolling window when there is no more content.
        wheelLock: true,
        //// Enable invert style scrolling
        scrollInvert: false,
        // Set the size of the scrollbar to auto or a fixed number.
        trackSize: false,
        // Set the size of the thumb to auto or a fixed number.
        thumbSize: false,
        // Set to false to hide the scrollbar if not being used
        alwaysVisible: true
    }
})

.directive('attScrollbar', ['$window', '$timeout', '$parse', '$animate', 'attScrollbarConstant', '$position', function ($window, $timeout, $parse, $animate, attScrollbarConstant, $position) {
    return {
        restrict: 'A',
        scope: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/scrollbar/scrollbar.html',
        controller: ['$scope', '$element', '$attrs', function ($scope, $element, $attrs) {
            var defaults = {
                axis: $attrs.attScrollbar || attScrollbarConstant.defaults.axis,
                navigation: $attrs.navigation || attScrollbarConstant.defaults.navigation,
                wheel: attScrollbarConstant.defaults.wheel,
                wheelSpeed: attScrollbarConstant.defaults.wheelSpeed,
                wheelLock: attScrollbarConstant.defaults.wheelLock,
                scrollInvert: attScrollbarConstant.defaults.scrollInvert,
                trackSize: attScrollbarConstant.defaults.trackSize,
                thumbSize: attScrollbarConstant.defaults.thumbSize,
                alwaysVisible: attScrollbarConstant.defaults.alwaysVisible
            };
            var options = $attrs.scrollbar;
            if (options) {
                options = $parse(options)($scope);
            } else {
                options = {};
            }
            this.options = angular.extend({}, defaults, options);
            this._defaults = defaults;

            var self = this,
                $body = angular.element(document.querySelectorAll('body')[0]),
                $document = angular.element(document),
                $viewport = angular.element($element[0].querySelectorAll('.scroll-viewport')[0]),
                $overview = angular.element($element[0].querySelectorAll('.scroll-overview')[0]),
                $scrollbar = angular.element($element[0].querySelectorAll('.scroll-bar')[0]),
                $thumb = angular.element($element[0].querySelectorAll('.scroll-thumb')[0]),
                mousePosition = 0,
                isHorizontal = this.options.axis === 'x',
                hasTouchEvents = false,
                // Modern browsers support "wheel"
                wheelEvent = ("onwheel" in document ? "wheel" :
                    // Webkit and IE support at least "mousewheel"
                    document.onmousewheel !== undefined ? "mousewheel" :
                    // let's assume that remaining browsers are older Firefox
                    "DOMMouseScroll"),
                sizeLabel = isHorizontal ? 'width' : 'height',
                sizeLabelCap = sizeLabel.charAt(0).toUpperCase() + sizeLabel.slice(1).toLowerCase(),
                posiLabel = isHorizontal ? 'left' : 'top',
                // moveEvent = document.createEvent('HTMLEvents'),
                restoreVisibilityAfterWheel,
                thumbscrolltouch = false,
                documnetscrolltouch = false;
            if (('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch) {
                hasTouchEvents = true;
            }

            //moveEvent.initEvent('move', true, true);
            this.contentPosition = 0;
            this.viewportSize = 0;
            this.contentSize = 0;
            this.contentRatio = 0;
            this.trackSize = 0;
            this.trackRatio = 0;
            this.thumbSize = 0;
            this.thumbPosition = 0;

            this.initialize = function () {
                if (!this.options.alwaysVisible) {
                    $scrollbar.css('opacity', 0);
                }
                self.update();
                setEvents();
                return self;
            };

            this.setSizeData = function () {
                this.viewportSize = $viewport.prop('offset' + sizeLabelCap) || 1;
                this.contentSize = $overview.prop('scroll' + sizeLabelCap) || 1;
                this.contentRatio = this.viewportSize / this.contentSize;
                this.trackSize = this.options.trackSize || this.viewportSize;
                this.thumbSize = Math.min(this.trackSize, Math.max(0, (this.options.thumbSize || (this.trackSize * this.contentRatio))));
                this.trackRatio = this.options.thumbSize ? (this.contentSize - this.viewportSize) / (this.trackSize - this.thumbSize) : (this.contentSize / this.trackSize);
            };

            this.update = function (scrollTo) {
                self.setSizeData();
                mousePosition = $scrollbar.prop('offsetTop');

                $scrollbar.toggleClass('disable', this.contentRatio >= 1 || isNaN(this.contentRatio));

                if (!this.options.alwaysVisible && this.contentRatio < 1 && this.viewportSize > 0) {
                    //flash the scrollbar when update happens
                    $animate.addClass($scrollbar, 'visible').then(function () {
                        $animate.removeClass($scrollbar, 'visible');
                        $scope.$digest();
                    });
                }

                if (scrollTo !== null) {
                    if (scrollTo === 'bottom') {
                        this.contentPosition = this.contentSize - this.viewportSize;
                    } else {
                        this.contentPosition = parseInt(scrollTo, 10) || 0;
                    }
                }

                ensureContentPosition();
                $thumb.css(posiLabel, self.contentPosition / self.trackRatio + 'px');
                $scrollbar.css(sizeLabel, self.trackSize + 'px');
                $thumb.css(sizeLabel, self.thumbSize + 'px');
                $overview.css(posiLabel, -self.contentPosition + 'px');

                return this;
            };

            fireEvent = function (obj, evt) {
                var fireOnThis = obj;
                var evtObj;
                if (document.createEvent) {
                    // alert("FF");
                    evtObj = document.createEvent('HTMLEvents');
                    evtObj.initEvent(evt, true, false);
                    fireOnThis.dispatchEvent(evtObj);
                } else if (document.createEventObject) {
                    // alert("IE8");
                    evtObj = document.createEventObject();
                    fireOnThis.fireEvent('on' + evt, evtObj);
                }
            };

            function ensureContentPosition() {
                // if scrollbar is on, ensure the bottom of the content does not go above the bottom of the viewport
                if (self.contentRatio <= 1 && self.contentPosition > self.contentSize - self.viewportSize) {
                    self.contentPosition = self.contentSize - self.viewportSize;
                }
                // if scrollbar is off, ensure the top of the content does not go below the top of the viewport
                else if (self.contentRatio > 1 && self.contentPosition > 0) {
                    self.contentPosition = 0;
                }

                if (self.contentPosition <= 0) {
                    $scope.prevAvailable = false;
                } else {
                    $scope.prevAvailable = true;
                }

                if (self.contentPosition >= (self.contentSize - self.viewportSize)) {
                    $scope.nextAvailable = false;
                } else {
                    $scope.nextAvailable = true;
                }
            }

            function setEvents() {
                if (hasTouchEvents) {
                    $viewport.on('touchstart', touchstart);
                    $thumb.on('touchstart', touchstart);
                } else {
                    $thumb.on('mousedown', start);
                    $scrollbar.on('mousedown', drag);
                }

                angular.element($window).on('resize', resize);

                if (self.options.wheel) {
                    $element.on(wheelEvent, wheel);
                }
            }

            function resize() {
                self.update();
            }

            function touchstart(event) {
                if (1 === event.touches.length) {
                    event.stopPropagation();
                    start(event.touches[0]);
                }
            }

            function start(event) {
                $body.addClass('scroll-no-select');
                $element.addClass('scroll-no-select');

                if (!self.options.alwaysVisible) {
                    $scrollbar.addClass('visible');
                }
                mousePosition = isHorizontal ? event.clientX : event.clientY;
                self.thumbPosition = parseInt($thumb.css(posiLabel), 10) || 0;

                if (hasTouchEvents) {
                    documnetscrolltouch = false;
                    thumbscrolltouch = false;
                    $viewport.on('touchmove', touchdrag);
                    $viewport.on('touchend', end);
                    $thumb.on('touchmove', touchdragthumb);
                    $thumb.on('touchend', end);
                } else {
                    $document.on('mousemove', drag);
                    $document.on('mouseup', end);
                    $thumb.on('mouseup', end);
                }
            }

            function wheel(event) {
                if (self.contentRatio >= 1) {
                    return;
                }

                if (!self.options.alwaysVisible) {
                    //cancel removing visibility if wheel event is triggered before the timeout
                    if (restoreVisibilityAfterWheel) {
                        $timeout.cancel(restoreVisibilityAfterWheel);
                    }
                    $scrollbar.addClass('visible');

                    restoreVisibilityAfterWheel = $timeout(function () {
                        $scrollbar.removeClass('visible');
                    }, 100);
                }

                var evntObj = (event && event.originalEvent) || event || $window.event,
                    deltaDir = self.options.axis.toUpperCase(),
                    delta = {
                        X: evntObj.deltaX || 0,
                        Y: evntObj.deltaY || 0
                    },
                    wheelSpeed = evntObj.deltaMode === 0 ? self.options.wheelSpeed : 1;

                if (self.options.scrollInvert) {
                    wheelSpeed *= -1;
                }

                if (wheelEvent === 'mousewheel') {
                    delta.Y = -1 * evntObj.wheelDelta / 40;
                    if (evntObj.wheelDeltaX) {
                        delta.X = -1 * evntObj.wheelDeltaX / 40;
                    }
                }
                delta.X *= -1 / wheelSpeed;
                delta.Y *= -1 / wheelSpeed;

                var wheelSpeedDelta = delta[deltaDir];

                self.contentPosition -= wheelSpeedDelta * self.options.wheelSpeed;
                self.contentPosition = Math.min((self.contentSize - self.viewportSize), Math.max(0, self.contentPosition));

                fireEvent($element[0], 'move');

                ensureContentPosition();
                $thumb.css(posiLabel, self.contentPosition / self.trackRatio + 'px');
                $overview.css(posiLabel, -self.contentPosition + 'px');

                if (self.options.wheelLock || (self.contentPosition !== (self.contentSize - self.viewportSize) && self.contentPosition !== 0)) {
                    evntObj.preventDefault();
                }

                $scope.$apply();
            }

            function touchdrag(event) {
                event.preventDefault();
                documnetscrolltouch = true;
                drag(event.touches[0]);
            }

            function touchdragthumb(event) {
                event.preventDefault();
                thumbscrolltouch = true;
                drag(event.touches[0]);
            }

            function drag(event) {
                if (self.contentRatio >= 1) {
                    return;
                }

                var mousePositionNew = isHorizontal ? event.clientX : event.clientY,
                    thumbPositionDelta = mousePositionNew - mousePosition;

                if ((self.options.scrollInvert && !hasTouchEvents) ||
                    (hasTouchEvents && !self.options.scrollInvert)) {
                    thumbPositionDelta = mousePosition - mousePositionNew;
                }
                if (documnetscrolltouch && hasTouchEvents) {
                    thumbPositionDelta = mousePosition - mousePositionNew;
                }
                if (thumbscrolltouch && hasTouchEvents) {
                    thumbPositionDelta = mousePositionNew - mousePosition;
                }
                var thumbPositionNew = Math.min((self.trackSize - self.thumbSize), Math.max(0, self.thumbPosition + thumbPositionDelta));
                self.contentPosition = thumbPositionNew * self.trackRatio;

                fireEvent($element[0], 'move');

                ensureContentPosition();
                $thumb.css(posiLabel, thumbPositionNew + 'px');
                $overview.css(posiLabel, -self.contentPosition + 'px');

                $scope.$apply();
            }

            $scope.customScroll = function (direction) {
                if (self.contentRatio >= 1) {
                    return;
                }

                var customScrollDelta;
                var viewportDimension = $position.position($viewport);

                if (isHorizontal) {
                    customScrollDelta = viewportDimension.width;
                } else {
                    customScrollDelta = viewportDimension.height;
                }

                if (direction) {
                    self.contentPosition += customScrollDelta;
                } else {
                    self.contentPosition -= customScrollDelta;
                }
                self.contentPosition = Math.min((self.contentSize - self.viewportSize), Math.max(0, self.contentPosition));

                fireEvent($element[0], 'move');

                ensureContentPosition();
                $thumb.css(posiLabel, self.contentPosition / self.trackRatio + 'px');
                $overview.css(posiLabel, -self.contentPosition + 'px');
            };

            function end() {
                $body.removeClass('scroll-no-select');
                $element.removeClass('scroll-no-select');
                if (!self.options.alwaysVisible) {
                    $scrollbar.removeClass('visible');
                }
                $document.off('mousemove', drag);
                $document.off('mouseup', end);
                $thumb.off('mouseup', end);
                $document.off('touchmove', touchdrag);
                $document.off('ontouchend', end);
                $thumb.off('touchmove', touchdragthumb);
                $thumb.off('touchend', end);
            }

            this.cleanup = function () {
                $viewport.off('touchstart', touchstart);
                $thumb.off('mousedown', start);
                $scrollbar.off('mousedown', drag);
                $thumb.off('touchmove', touchdragthumb);
                $thumb.off('touchend', end);
                angular.element($window).off('resize', resize);
                $element.off(wheelEvent, wheel);
                //ensure scrollbar isn't activated
                self.options.alwaysVisible = true;
                end();
            };
        }],
        link: function (scope, iElement, iAttrs, controller) {
            scope.navigation = controller.options.navigation;
            scope.viewportHeight = iAttrs.viewportHeight;
            scope.viewportWidth = iAttrs.viewportWidth;
            scope.scrollbarAxis = controller.options.axis;
            if (scope.scrollbarAxis === 'x') {
                iElement.addClass('horizontal');
            } else if (scope.scrollbarAxis === 'y') {
                iElement.addClass('vertical');
            }

            var position = iElement.css('position');
            if (position !== 'relative' && position !== 'absolute') {
                iElement.css('position', 'relative');
            }

            scope.$watch(function () {
                $timeout(refreshScrollbar, 100, false);
            });

            var refreshScrollbar = function () {
                var $overview = angular.element(iElement[0].querySelectorAll('.scroll-overview')[0]);
                var newValue = $overview.prop('scrollHeight');
                var oldValue = scope.oldValue;
                if (newValue !== oldValue) {
                    scope.oldValue = newValue;
                    controller.update();
                }
            };

            controller.initialize();
            iElement.on('$destroy', function () {
                controller.cleanup();
            });
        }
    };
}]);

angular.module('att.abs.search', ['att.abs.utilities', 'att.abs.position', 'att.abs.utilities'])
.directive('attSearch', ['$document', '$filter', '$isElement', '$documentBind', '$timeout', '$log', 'keymap', function($document, $filter, $isElement, $documentBind, $timeout, $log, keymap){
    return{
        restrict: 'A',
        scope:{cName: '=attSearch'},
        transclude: false,
        replace: false,
        require:'ngModel',
        templateUrl: 'app/scripts/ng_js_att_tpls/search/search.html',
        link: function(scope, element, attr, ctrl) {
            scope.selectedIndex = -1;
            scope.selectedOption = attr.placeholder;
            scope.isDisabled = false;
            scope.className = "select2-match";
            scope.showSearch = false;
            scope.showlist = false;

            // This is used to jump to elements in list
            var search = '';
            // This is used to ensure searches only persist so many ms.
            var prevSearchDate = new Date();
            // This is used to shift focus back after closing dropdown
            var dropdownElement = undefined;
            // Used to ensure focus on dropdown elements
            var list = [];
            $timeout(function() {
                list = element.find('li');
            }, 10);

            $log.warn('attSearch is deprecated, please use attSelect instead. This component will be removed by version 2.7.')
  			//scope.noFilter = true;
            if (attr.noFilter || attr.noFilter === 'true') {
                scope.noFilter = true;
            } else {
                scope.noFilter = false;
            }
            if (attr.placeholderAsOption === 'false') {
                //scope.selectMsg = '';
                scope.selectedOption = attr.placeholder;
            } else {
                scope.selectMsg = attr.placeholder;
            }
            if (attr.startsWithFilter || attr.startsWithFilter === 'true') {
                scope.startsWithFilter = true;
            }
            if (attr.showInputFilter === 'true') {
                scope.showSearch = false;
                $log.warn('showInputFilter functionality has been removed from the library.');
                // This is deprecated
            }
            if (attr.disabled) {
                scope.isDisabled = true;
            }
            dropdownElement = angular.element(element).children().eq(0).find('a')[0];
            var prevIndex = 0;
            var selectOptionFromSearch = function() {
                if (!scope.noFilter) {
                    return;
                }

                // Find next element that matches search criteria. 
                // If no element is found, loop to beginning and search.
                var criteria = search;
                var i = 0;
                for (i = prevIndex; i < scope.cName.length; i++) {
                    // Need to ensure we keep searching until all startsWith have passed before looping
                    if (scope.cName[i].title.startsWith(criteria) && i !== scope.selectedIndex) {
                        scope.selectOption(scope.cName[i], i, scope.showlist);
                        prevIndex = i;
                        search = '';
                        break;
                    }
                }
                if ((i >= scope.cName.length || !scope.cName[i+1].title.startsWith(criteria)) && prevIndex > 0) {
                    prevIndex = 0;
                }
            };
            scope.showDropdown = function() {
                if (!(attr.disabled)) {
                    scope.showlist = !scope.showlist;
                    scope.setSelectTop();
                }
            };
            element.bind('keydown', function(e) {
                if (keymap.isAllowedKey(e.keyCode) || keymap.isControl(e) || keymap.isFunctionKey(e)) {
                    e.preventDefault();
                    e.stopPropagation();

                    switch (e.keyCode) {
                        case keymap.KEY.DOWN:
                            scope.selectNext();
                            break;
                        case keymap.KEY.UP:
                            scope.selectPrev();
                            search = '';
                            break;
                        case keymap.KEY.ENTER:
                            scope.selectCurrent();
                            search = '';
                            break;
                        case keymap.KEY.BACKSPACE:
                            scope.title = '';
                            search = '';
                            scope.$apply();
                            break;
                        case keymap.KEY.SPACE:
                            if (!scope.noFilter) {
                                scope.title += ' ';
                            }
                            scope.$apply();
                            break;
                        case keymap.KEY.ESC:
                            if (scope.title === '' || scope.title === undefined) {
                                scope.showlist = false;
                                dropdownElement.focus();
                                scope.$apply();
                            } else {
                                scope.title = '';
                                scope.$apply();
                            }
                            if (scope.noFilter) {
                                search = '';
                                dropdownElement.focus();
                                scope.showlist = false;
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    if (e.keyCode !== 9)
                    {
                        if (!scope.noFilter) {
                            scope.showlist = true;
                            scope.title = scope.title ? scope.title + String.fromCharCode(e.keyCode) : String.fromCharCode(e.keyCode);
                        } else {
                            var date = new Date();
                            var delta = Math.abs(prevSearchDate.getMilliseconds() - date.getMilliseconds());
                            prevSearchDate = date;
                            if (delta > 100) {
                                search = '';
                            }
                            search = search ? search + String.fromCharCode(e.keyCode) : String.fromCharCode(e.keyCode);
                            if (search.length > 2) {
                                search = search.substring(0, 2);
                            }
                            selectOptionFromSearch();
                        }
                        scope.$apply();
                    } else if (e.keyCode === 9) {
                        scope.showlist = false;
                        scope.title = '';
                        scope.$apply();
                    }
                }
            });
            scope.selectOption = function(sTitle, sIndex, keepOpen) {
                if (sIndex === -1 || sIndex === '-1') {
                    scope.selCategory = '';
                    scope.selectedIndex = -1;
                    ctrl.$setViewValue('');
                    if(attr.placeholderAsOption !== 'false')
                    {
                        scope.selectedOption = scope.selectMsg;
                    }
                } else {
                    scope.selCategory = scope.cName[sIndex];
                    scope.selectedIndex = sIndex;
                    ctrl.$setViewValue(scope.selCategory);
                    scope.selectedOption = scope.selCategory.title;
                    if (angular.isDefined(list[sIndex])) {
                        list[sIndex].focus();
                    }
                }
                scope.title = '';
                if (!keepOpen) {
                    scope.showlist = false;
                    dropdownElement.focus();
                }
                scope.$apply();
            };
            scope.selectCurrent = function() {
                if (scope.showlist) {
                    scope.selectOption(scope.selectMsg, scope.selectedIndex, false);
                    scope.$apply();
                } else {
                    scope.showlist = true;
                    scope.setSelectTop();
                    scope.$apply();
                }
            };
            scope.hoverIn = function(cItem) {
                scope.selectedIndex = cItem;
                scope.focusme();
            };
            scope.setSelectTop = function() {
                $timeout(function() {
                    if (scope.showlist && !scope.noFilter)
                    {
                        var containerUL = angular.element(element)[0].querySelector(".select2-results");
                        if(angular.element(containerUL.querySelector('.select2-result-current'))[0])
                        {
                            var selectedElemTopPos = angular.element(containerUL.querySelector('.select2-result-current'))[0].offsetTop;
                        }
                        angular.element(containerUL)[0].scrollTop = selectedElemTopPos;
                    }
                });
            };
            scope.setCurrentTop = function() {
                $timeout(function() {
                    if (scope.showlist) {
                        var containerUL = angular.element(element)[0].querySelector(".select2-results");
                        if(angular.element(containerUL.querySelector('.hovstyle'))[0])
                        {
                            var selectedElemTopPos = angular.element(containerUL.querySelector('.hovstyle'))[0].offsetTop;
                        }
                        if (selectedElemTopPos < (angular.element(containerUL)[0].scrollTop)) {
                            angular.element(containerUL)[0].scrollTop -= 30;
                        } else if ((selectedElemTopPos + 30) > (angular.element(containerUL)[0].clientHeight)) {
                            angular.element(containerUL)[0].scrollTop += 30;
                        }

                    }
                });
            };
            scope.selectNext = function() {
                if ((scope.selectedIndex + 1) <= (scope.cName.length - 1)) {
                    scope.selectedIndex += 1;
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex, false);
                    }
                    scope.focusme();
                    scope.$apply();
                }
                scope.setCurrentTop();
            };
            scope.selectPrev = function() {
                if ((scope.selectedIndex - 1) >= 0) {
                    scope.selectedIndex -= 1;
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex, false);
                    }
                    scope.focusme();
                    scope.$apply();
                } else if (scope.selectedIndex - 1 < 0) {
                    // If placeholderAsOption is true or undefined (default), ensure we can select it on up key.
                    if (attr.placeholderAsOption === undefined || attr.placeholderAsOption === 'true') {
                        scope.selectedIndex = -1;
                    } else {
                        scope.selectedIndex = 0;
                    }
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex, false);
                    }
                    scope.focusme();
                    scope.$apply();
                }
                scope.setCurrentTop();
            };
            scope.updateSelection = function(sItem) {
                scope.selectedOption = sItem.title;
                scope.title = "";
            };
            scope.focusme = function() {
                $timeout(function() {
                    var list = angular.element(element).find('ul').find('li');
                    var index = scope.selectedIndex + 2;
                    if (scope.noFilter) {
                        index = scope.selectedIndex;
                    }
                    if (angular.isDefined(list[index])) {
                        list[index].focus();
                    }
                });
            };
            scope.$watch('selCategory', function(value) {
                if (value) {
                    scope.updateSelection(value);
                };
            });
            ctrl.$viewChangeListeners.push(function() {
                scope.$eval(attr.ngChange);
            });
            ctrl.$render = function() {
                scope.selCategory = ctrl.$viewValue;
            };
            var outsideClick = function(e) {
                var isElement = $isElement(angular.element(e.target), element, $document);
                if (!isElement) {
                    scope.showlist = false;
                    dropdownElement.focus();
                    scope.$apply();
                }
            };
            $documentBind.click('showlist', outsideClick, scope);
        }
    };
}]);
angular.module('att.abs.select', ['att.abs.utilities', 'att.abs.position', 'att.abs.utilities'])
.directive('attSelect', ["$document", "$filter", "$isElement", '$documentBind', '$timeout', 'keymap', '$log', function($document, $filter, $isElement, $documentBind, $timeout, keymap, $log) {
    return {
        restrict: 'A',
        scope: {
            cName: '=attSelect'
        },
        transclude: false,
        replace: false,
        require: 'ngModel',
        templateUrl: 'app/scripts/ng_js_att_tpls/select/select.html',
        link: function(scope, element, attr, ctrl) {
            scope.selectedIndex = -1;
            scope.selectedOption = attr.placeholder;
            scope.isDisabled = false;
            scope.className = "select2-match";
            scope.showSearch = false;
            scope.showlist = false;
			scope.titleName = attr.titlename;
            scope.$watch('ngModel', function() {
               // console.log('sv:', ctrl.$modelValue);
            });

            // This is used to jump to elements in list
            var search = '';
            // This is used to ensure searches only persist so many ms.
            var prevSearchDate = new Date();
            // This is used to shift focus back after closing dropdown
            var dropdownElement = undefined;
            // Used to ensure focus on dropdown elements
            var list = [];
            $timeout(function() {
                list = element.find('li');
            }, 10);
			//scope.noFilter = true;
            if (attr.noFilter || attr.noFilter === 'true') {
                scope.noFilter = true;
            } else {
                scope.noFilter = false;
            }
            if (attr.placeholderAsOption === 'false') {
                scope.selectedOption = attr.placeholder;
            } else {
                scope.selectMsg = attr.placeholder;
            }
            if (attr.startsWithFilter || attr.startsWithFilter === 'true') {
                scope.startsWithFilter = true;
            }
            if (attr.showInputFilter === 'true') {
                scope.showSearch = false;
                /* This is deprecated */
                $log.warn('showInputFilter functionality has been removed from the library.');   
            }
            if (attr.disabled) {
                scope.isDisabled = true;
            }
            var getFilterType = function() {
                if (scope.startsWithFilter) {
                    return 'startsWith';
                } else {
                    return 'filter';
                }
            };
            dropdownElement = angular.element(element).children().eq(0).find('span')[0];
            var prevIndex = 0;
            var selectOptionFromSearch = function() {
                if (!scope.noFilter) {
                    return;
                }

                // Find next element that matches search criteria.
                // If no element is found, loop to beginning and search.
                var criteria = search;
                var i = 0;
                for (i = prevIndex; i < scope.cName.length; i++) {
                    // Need to ensure we keep searching until all startsWith have passed before looping
                    if (scope.cName[i].title.startsWith(criteria) && i !== scope.selectedIndex) {
                        scope.selectOption(scope.cName[i], i, scope.showlist);
                        prevIndex = i;
                        search = '';
                        break;
                    }
                }
                if ((i >= scope.cName.length || !scope.cName[i+1].title.startsWith(criteria)) && prevIndex > 0) {
                    prevIndex = 0;
                }
            };
            scope.showDropdown = function() {
                if (!(attr.disabled)) {
                    scope.showlist = !scope.showlist;
                    scope.setSelectTop();
                    /* Ensure selected element is focused upon opening dropdown */
                    scope.focusme();
                }
            };
            element.bind('keydown', function(e) {
                if (keymap.isAllowedKey(e.keyCode) || keymap.isControl(e) || keymap.isFunctionKey(e)) {
                    e.preventDefault();
                    e.stopPropagation();

                    switch (e.keyCode) {
                        case keymap.KEY.DOWN:
                            scope.selectNext();
                            break;
                        case keymap.KEY.UP:
                            scope.selectPrev();
                            search = '';
                            break;
                        case keymap.KEY.ENTER:
                            scope.selectCurrent();
                            search = '';
                            break;
                        case keymap.KEY.BACKSPACE:
                            scope.title = '';
                            search = '';
                            scope.$apply();
                            break;
                        case keymap.KEY.SPACE:
                            if (!scope.noFilter) {
                                scope.title += ' ';
                            }
                            scope.$apply();
                            break;
                        case keymap.KEY.ESC:
                            if (scope.title === '' || scope.title === undefined) {
                                scope.showlist = false;
                                dropdownElement.focus();
                                scope.$apply();
                            } else {
                                scope.title = '';
                                scope.$apply();
                            }
                            if (scope.noFilter) {
                                search = '';
                                dropdownElement.focus();
                                scope.showlist = false;
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    if (e.keyCode !== keymap.KEY.TAB)
                    {
                        if (!scope.noFilter) {
                            scope.showlist = true;
                            scope.title = scope.title ? scope.title + String.fromCharCode(e.keyCode) : String.fromCharCode(e.keyCode);

                            /* Perform index correction */
                            if (scope.title != '') {
                                var filteredArray = $filter(getFilterType())(scope.cName, scope.title);

                                for (var i = 0; i < filteredArray.length; i++) {
                                    for (var j = 0; j < scope.cName.length; j++) {
                                        if (!angular.isDefined(scope.cName[scope.selectedIndex])) {
                                            break;
                                        }
                                        if (filteredArray[i]['title'] === scope.cName[scope.selectedIndex]['title']) {
                                            scope.selectedIndex = i;
                                            scope.focusme();
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            var date = new Date();
                            var delta = Math.abs(prevSearchDate.getMilliseconds() - date.getMilliseconds());
                            prevSearchDate = date;
                            if (delta > 100) {
                                search = '';
                            }
                            search = search ? search + String.fromCharCode(e.keyCode) : String.fromCharCode(e.keyCode);
                            if (search.length > 2) {
                                search = search.substring(0, 2);
                            }
                            selectOptionFromSearch();
                        }
                        scope.$apply();
                    } else if (e.keyCode === keymap.KEY.TAB) {
                        scope.showlist = false;
                        scope.title = '';
                        scope.$apply();
                    }
                }
            });
            scope.selectOption = function(sTitle, sIndex, keepOpen) {

                if (sIndex === -1 || sIndex === '-1') {
                    scope.selCategory = '';
                    scope.selectedIndex = -1;
                    ctrl.$setViewValue('');
                    if(attr.placeholderAsOption !== 'false')
                    {
                        scope.selectedOption = scope.selectMsg;
                    }
                }
                else {
                    /* Apply filter here to remap the selected index and shift focus*/
                    if (scope.title != '') {
                        var filteredArray = $filter(getFilterType())(scope.cName, scope.title);

                        if (angular.isDefined(filteredArray) && angular.isDefined(filteredArray[sIndex]))
                        {
                            for (var i = 0; i < scope.cName.length; i++) {
                                if (filteredArray[sIndex]['title'] === scope.cName[i]['title']) {
                                    sIndex = i;
                                    break;
                                }
                            }
                        }
                    }
                    scope.selCategory = scope.cName[sIndex];
                    scope.selectedIndex = sIndex;
                    ctrl.$setViewValue(scope.selCategory);
                    scope.selectedOption = scope.selCategory.title;
                    ctrl.$render();

                    $timeout(function(){
                        if (angular.isDefined(list[sIndex])) {
                            try{
                                list[index].focus();
                            } catch (e) {} /* IE8 will throw exception if display:none or not in DOM */
                        }
                    });
                }
                scope.title = '';
                if (!keepOpen) {
                    scope.showlist = false;
                    dropdownElement.focus();
                }
            };
            scope.selectCurrent = function() {
                if (scope.showlist) {
                    scope.selectOption(scope.selectMsg,scope.selectedIndex,false);
                } else {
                    scope.showlist = true;
                    scope.setSelectTop();
                }
                scope.$apply();
            };
            scope.hoverIn = function(cItem) {
                scope.selectedIndex = cItem;
                scope.focusme();
            };
            scope.setSelectTop = function() {
                $timeout(function() {
                    if (scope.showlist && !scope.noFilter)
                    {
                        var containerUL = angular.element(element)[0].querySelector(".select2-results");
                        if(angular.element(containerUL.querySelector('.select2-result-current'))[0])
                        {
                            var selectedElemTopPos = angular.element(containerUL.querySelector('.select2-result-current'))[0].offsetTop;
                        }
                        angular.element(containerUL)[0].scrollTop = selectedElemTopPos;
                    }
                });
            };
            scope.setCurrentTop = function() {
                $timeout(function() {
                    if (scope.showlist) {
                        var containerUL = angular.element(element)[0].querySelector(".select2-results");
                        if(angular.element(containerUL.querySelector('.hovstyle'))[0])
                        {
                            var selectedElemTopPos = angular.element(containerUL.querySelector('.hovstyle'))[0].offsetTop;
                        }
                        if (selectedElemTopPos < (angular.element(containerUL)[0].scrollTop)) {
                            angular.element(containerUL)[0].scrollTop -= 30;
                        } else if ((selectedElemTopPos + 30) > (angular.element(containerUL)[0].clientHeight)) {
                            angular.element(containerUL)[0].scrollTop += 30;
                        }

                    }
                });
            };
            scope.selectNext = function() {
                var length = scope.cName.length;
                
                if ((scope.selectedIndex + 1) <= (scope.cName.length - 1)) {
                    scope.selectedIndex += 1;
                    var nextDisabled = scope.cName[scope.selectedIndex].disabled;
                    if (nextDisabled) {
                        scope.selectedIndex += 1;
                    }
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex,false);
                    }
                    scope.focusme();
                    scope.$apply();
                }
                scope.setCurrentTop();
            };
            scope.selectPrev = function() {
                if ((scope.selectedIndex - 1) >= 0) {
                    scope.selectedIndex -= 1;
                    var prevDisabled = scope.cName[scope.selectedIndex].disabled;
                    if (prevDisabled) {
                        scope.selectedIndex -= 1;
                    }
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex,false);
                    }
                    scope.focusme();
                    scope.$apply();
                } else if (scope.selectedIndex - 1 < 0) {
                    // If placeholderAsOption is true or undefined (default), ensure we can select it on up key.
                    if (attr.placeholderAsOption === undefined || attr.placeholderAsOption === 'true') {
                        if(attr.placeholder === undefined ){
                            scope.selectedIndex = 0;
                        }
                        else{
                            scope.selectedIndex = -1;
                        }
                    } else {
                        scope.selectedIndex = 0;
                    }
                    if (!scope.showlist) {
                        scope.selectOption(scope.selectMsg, scope.selectedIndex,false);
                    }
                    scope.focusme();
                    scope.$apply();
                }
                scope.setCurrentTop();
            };
            scope.updateSelection = function(sItem) {
                scope.selectedOption = sItem.title;
                scope.title = '';

                if (sItem.index < 0) {
                    scope.selectOption(scope.selectMsg, sItem.index, scope.showlist);
                }
            };
            scope.focusme = function() {
                $timeout(function() {
                    var list = angular.element(element).find('ul').find('li');
                    var index = scope.selectedIndex + 2;
                    if (scope.noFilter) {
                        index = scope.selectedIndex;
                    }
                    if (angular.isDefined(list[index])) {
                        try {
                            list[index].focus();
                        } catch (e) {} /* IE8 will throw exception if display:none or not in DOM */  
                    }
                });
            };
            scope.$watch('selCategory', function(value) {
                if (value) {
                    scope.updateSelection(value);
                };
            });
            ctrl.$viewChangeListeners.push(function() {
                scope.$eval(attr.ngChange);
            });
            ctrl.$render = function() {
                scope.selCategory = ctrl.$viewValue;
            };
            var outsideClick = function(e) {
                var isElement = $isElement(angular.element(e.target), element, $document);
                if (!isElement) {
                    scope.showlist = false;
                    dropdownElement.focus();
                    scope.$apply();
                }
            };
            $documentBind.click('showlist', outsideClick, scope);
        }
    };
}])
.directive('textDropdown', ['$document', '$isElement', '$documentBind', "keymap", function($document, $isElement, $documentBind, keymap) {
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            actions: '=actions',
            defaultAction: '=defaultAction',
            onActionClicked: '=?'
        },
        templateUrl: 'app/scripts/ng_js_att_tpls/select/textDropdown.html',
        link: function(scope, element, attr) {
            scope.selectedIndex = 0;
            scope.selectedOption = attr.placeholder;
            scope.isDisabled = false;
            scope.isActionsShown = false;
            var dropdownElement = undefined;
            if (attr.disabled) {
                scope.isDisabled = true;
            }

            dropdownElement = element.find('div')[0];

            // Set default Action
            if (!angular.isDefined(scope.defaultAction)) {
                scope.currentAction = scope.actions[0];
                scope.selectedIndex = 0;
            } else if (angular.isDefined(scope.defaultAction) || scope.defaultAction !== '') {
                for (var act in scope.actions) {
                    if (scope.actions[act] === scope.defaultAction) {
                        scope.currentAction = scope.actions[act];
                        scope.selectedIndex = scope.actions.indexOf(act);
                        scope.isActionsShown = false;
                        break;
                    }
                }
            } else {
                scope.currentAction = scope.actions[0];
            }
            scope.toggle = function() {
                scope.isActionsShown = !scope.isActionsShown;
            };
            scope.chooseAction = function($event, action, $index) {
                if ($event != null) {
                    scope.currentAction = action;
                    scope.selectedIndex = $index;
                } else {
                    scope.currentAction = scope.actions[scope.selectedIndex];
                }
                if (angular.isFunction(scope.onActionClicked)) {
                    scope.onActionClicked(scope.currentAction);
                }
                scope.toggle();
            };
            scope.isCurrentAction = function(action) {
                return (action === scope.currentAction);
            };
            element.bind("keydown", function(e) {
                if (keymap.isAllowedKey(e.keyCode) || keymap.isControl(e) || keymap.isFunctionKey(e)) {
                    e.preventDefault();
                    e.stopPropagation();
                    switch (e.keyCode) {
                        case keymap.KEY.DOWN:
                            scope.selectNext();
                            break;
                        case keymap.KEY.UP:
                            scope.selectPrev();
                            break;
                        case keymap.KEY.ENTER:
                            scope.selectCurrent();
                            break;
                        case keymap.KEY.ESC:
                            scope.isActionsShown = false;
                            dropdownElement.focus();
                            scope.$apply();
                            break;
                        default:
                            break;
                    }
                    scope.$apply();
                    return;
                } else if (e.keyCode === keymap.KEY.TAB) {
                    scope.isActionsShown = false;
                    scope.$apply();
                }
            });
            scope.selectCurrent = function() {
                if (scope.selectedIndex < 0) {
                    scope.selectedIndex = 0;
                }
                if (!scope.isActionsShown) {
                    scope.toggle();
                } else {
                    scope.chooseAction(null, scope.currentAction);
                }
            };
            scope.selectNext = function() {
                if (scope.isActionsShown) {
                    if ((scope.selectedIndex + 1) < scope.actions.length) {
                        scope.selectedIndex += 1;
                    } else {
                        scope.selectedIndex = (scope.actions.length - 1);
                    }
                    scope.$apply();
                }
            };
            scope.selectPrev = function() {
                if (scope.isActionsShown) {
                    if ((scope.selectedIndex - 1) >= 0) {
                        scope.selectedIndex -= 1;
                    } else if (scope.selectedIndex - 1 < 0) {
                        scope.selectedIndex = 0;
                    }
                    scope.$apply();
                }
            };
            scope.hoverIn = function(cItem) {
                scope.selectedIndex = cItem;
            };
            //end cato
            var outsideClick = function(e) {
                var isElement = $isElement(angular.element(e.target), element, $document);
                if (!isElement) {
                    scope.toggle();
                    scope.$apply();
                }
            };
            $documentBind.click('isActionsShown', outsideClick, scope);
        }
    };
}]);
angular.module('att.abs.slider', ['att.abs.position'])
        .constant('sliderDefaultOptions', {
            width: 300,
            step: 1,
            precision: 0,
            disabledWidth: 116
        })
        .directive('attSlider', ['sliderDefaultOptions','$position','$document', function(sliderDefaultOptions,$position,$document)
            {
                return {
                    restrict: 'EA',
                    replace: true,
                    transclude: true,
                    scope: {
                        floor: "=",
                        ceiling: "=",
                        step: "@",
                        precision: "@",
                        width: "@",
                        textDisplay: "=",
                        value: "=",
                        ngModelSingle: '=?',
                        ngModelLow: '=?',
                        ngModelHigh: '=?',
                        ngModelDisabled: '=?'
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/slider/slider.html',
                    link: function(scope, elem, attr)
                    {
                        var minOffset, maxOffset, newOffset, newOffset1, newOffset2, offsetRange, valueRange, start_x = 0, disabledRange, disabled, evFlag = false, minValue, maxValue, range, refLow, refHigh, maxPtr, minPtr, singlePtr, getHandles;
                        scope.minPtrOffset = 0;
                        scope.maxPtrOffset = 0;
                        var disableWidth = sliderDefaultOptions.disabledWidth;
                        //Get handles
                        var obj = elem.children();
                        disabledRange = obj[0].children;
                        disabledRange = angular.element(disabledRange[0]);
                        getHandles = obj[1].children;
                        singlePtr = angular.element(getHandles[0]);
                        minPtr = angular.element(getHandles[1]);
                        maxPtr = angular.element(getHandles[2]);
                        disabled = ((attr.ngModelSingle == null) && (attr.ngModelLow == null) && (attr.ngModelHigh == null)) && (attr.ngModelDisabled != null);
                        range = (attr.ngModelSingle == null) && ((attr.ngModelLow != null) && (attr.ngModelHigh != null));
                        refLow = 'ngModelLow';
                        refHigh = 'ngModelHigh';
                        if (!range) {
                            minPtr.remove();
                            maxPtr.remove();
                        }
                        else {
                            singlePtr.remove();
                        }
                        if (!disabled) {
                            disabledRange.remove();
                        }
                        else {
                            scope.disabledStyle = {width: disableWidth + 'px', zIndex: 1};
                            scope.handleStyle = {left: disableWidth + 'px'};
                        }
                        minValue = parseFloat(scope.floor);
                        maxValue = parseFloat(scope.ceiling);
                        valueRange = maxValue - minValue;
                        minOffset = 0;
                        if (attr.width !== undefined) {
                            maxOffset = attr.width;
                        }
                        else {
                            if (elem[0].clientWidth !== 0) {
                                maxOffset = elem[0].clientWidth;
                            }
                            else {
                                maxOffset = sliderDefaultOptions.width;
                            }
                        }
                        offsetRange = maxOffset - minOffset;
                        //Key Down Event
                         scope.keyDown = function(ev){
                               if(ev.keyCode === 39){
                                var elemLeft = $position.position(elem).left;
                                if (newOffset){
                                    if(scope.ref === "ngModelLow"){
                                        newOffset1 = sliderDefaultOptions.step + newOffset1;
                                        newOffset = newOffset1;
                                    }
                                    else if(scope.ref === "ngModelHigh"){
                                        newOffset2 = sliderDefaultOptions.step + newOffset2;
                                        newOffset = newOffset2;
                                    }
                                    else{newOffset = sliderDefaultOptions.step + newOffset;}
                                }
                                else {
                                    if(range &&scope.ref === "ngModelLow"){
                                        return;
                                    }
                                    else {
                                        newOffset = sliderDefaultOptions.step + elemLeft;
                                        newOffset1 = newOffset2 = newOffset;
                                    }
                                }
                                }
                            else if(ev.keyCode === 37){
                                var ptrLeft = $position.position(singlePtr).left;
                                if(newOffset){
                                if (!(newOffset<=0)){
                                    if(scope.ref === "ngModelLow"){
                                        newOffset1 = newOffset1 - sliderDefaultOptions.step;
                                        newOffset = newOffset1;
                                    }
                                    else if(scope.ref === "ngModelHigh"){
                                        newOffset2 = newOffset2 - sliderDefaultOptions.step;
                                        newOffset = newOffset2;
                                    }
                                    else {
                                        newOffset = newOffset - sliderDefaultOptions.step;
                                        newOffset1 = newOffset2 = newOffset;
                                    }
                                }
                                }
                                else {
                                    newOffset = ptrLeft - sliderDefaultOptions.step;
                                }
                                }
                            if(newOffset>=0){
                                scope.ptrOffset(newOffset);
                            }
                        };
                        //Mouse Down Event                         
                        scope.mouseDown = function(e, ref) {
                            scope.ref = ref;
                            evFlag = true;
                            if (!range){
                                if (newOffset)
                                {
                                    start_x = e.clientX - newOffset;
                                }
                                else {
                                    start_x = e.clientX;
                                }
                            } else {
                                if (scope.ref === refLow) {
                                    start_x = e.clientX - scope.minPtrOffset;
                                }
                                else {
                                    start_x = e.clientX - scope.maxPtrOffset;
                                }
                            }
                            if (disabled) {
                                scope.ref= 'ngModelDisabled';
                                scope.disabledStyle = {width: disableWidth + 'px', zIndex: 1};
                            }
                        };
                        // Mouse Move Event
                        scope.moveElem = function(ev) {
                            if (evFlag) {
                                var eventX;
                                eventX = ev.clientX;
                                newOffset = eventX - start_x;
                                scope.ptrOffset(newOffset);
                            }
                        };
                        scope.focus=function(ev,ref){
                            console.log(ref);
                            scope.ref=ref;
                        }
                        // Mouse Up Event
                        scope.mouseUp = function(ev) {
                            evFlag = false;
                            minPtr.removeClass('dragging');
                            maxPtr.removeClass('dragging');
                            singlePtr.removeClass('dragging');
                            $document.off('mousemove');
                        };
                        // key Up Event
                        scope.keyUp = function(ev) {
                            evFlag = false;
                            minPtr.removeClass('dragging');
                            maxPtr.removeClass('dragging');
                            singlePtr.removeClass('dragging');
                            $document.off('mousemove');
                        };
                        //Function to calculate the current PositionValue
                        scope.calStep = function(value, precision, step, floor) {
                            var decimals, remainder, roundedValue, steppedValue;
                            if (floor === null) {
                                floor = 0;
                            }
                            if (step === null) {
                                step = 1 / Math.pow(10, precision);
                            }
                            remainder = (value - floor) % step;
                            steppedValue = remainder > (step / 2) ? value + step - remainder : value - remainder;
                            decimals = Math.pow(10, precision);
                            roundedValue = steppedValue * decimals / decimals;
                            return roundedValue.toFixed(precision);
                        };
                        //Function to calculate Offset Percent
                        scope.percentOffset = function(offset) {
                            return ((offset - minOffset) / offsetRange) * 100;
                        };
                        //Function to calculate Offset position
                        scope.ptrOffset = function(newOffset){
                            var newPercent, newValue;
                                newOffset = Math.max(Math.min(newOffset, maxOffset), minOffset);
                                newPercent = scope.percentOffset(newOffset);
                                newValue = minValue + (valueRange * newPercent / 100.0);
                                if (range) {
                                    var rangeSliderWidth;
                                    if (scope.ref === refLow) {
                                        scope.minHandleStyle = {left: newOffset + "px"};
                                        scope.minNewVal = newValue;
                                        scope.minPtrOffset = newOffset;
                                        minPtr.addClass('dragging');
                                        if (newValue > scope.maxNewVal) {
                                            scope.ref = refHigh;
                                            maxPtr[0].focus();
                                            scope.maxNewVal = newValue;
                                            scope.maxPtrOffset = newOffset;
                                            maxPtr.addClass('dragging');
                                            minPtr.removeClass('dragging');
                                            scope.maxHandleStyle = {left: newOffset + "px"};
                                        }
                                    }
                                    else {
                                        scope.maxHandleStyle = {left: newOffset + "px"};
                                        scope.maxNewVal = newValue;
                                        scope.maxPtrOffset = newOffset;
                                        maxPtr.addClass('dragging');
                                        if (newValue < scope.minNewVal) {
                                            scope.ref = refLow;
                                            minPtr[0].focus();
                                            scope.minVal = newValue;
                                            scope.minPtrOffset = newOffset;
                                            minPtr.addClass('dragging');
                                            maxPtr.removeClass('dragging');
                                            scope.minHandleStyle = {left: newOffset + "px"};
                                        }
                                    }
                                    rangeSliderWidth = parseInt(scope.maxPtrOffset) - parseInt(scope.minPtrOffset);
                                    scope.rangeStyle = {width: rangeSliderWidth + "px", left: scope.minPtrOffset + "px"};
                                }
                                else {
                                    if (disabled && newOffset > disableWidth) {
                                        scope.rangeStyle = {width: newOffset + "px", zIndex: 0};
                                    }
                                    else {
                                        singlePtr.addClass('dragging');
                                        scope.rangeStyle = {width: newOffset + "px"};
                                    }
                                    scope.handleStyle = {left: newOffset + "px"};
                                }
                                if ((scope.precision === undefined) || (scope.step === undefined)) {
                                    scope.precision = sliderDefaultOptions.precision;
                                    scope.step = sliderDefaultOptions.step;
                                }
                                newValue = scope.calStep(newValue, parseInt(scope.precision), parseFloat(scope.step), parseFloat(scope.floor));
                                scope[scope.ref] = newValue;
                        };
                    }
                };
            }
        ]).directive('attSliderMin',[function()
            {
             return{
                 require: '^attSlider',
                 restrict: 'EA',
                 replace: true,
                 transclude: true,
                 templateUrl: 'app/scripts/ng_js_att_tpls/slider/minContent.html'
               };
         }
    ]).directive('attSliderMax',[function()
            {
             return{
                 require: '^attSlider',
                 restrict: 'EA',
                 replace: true,
                 transclude: true,
                 templateUrl: 'app/scripts/ng_js_att_tpls/slider/maxContent.html'
             };
         }
    ]);
angular.module('att.abs.splitButtonDropdown', ['att.abs.utilities','att.abs.position'])
    .directive('attButtonDropdown', ['$document', '$parse', '$documentBind', '$timeout','$isElement', function ($document, $parse, $documentBind, $timeout,$isElement) {
        return {
            restrict: 'EA',
            replace: true,
            transclude: true,            
            templateUrl: 'app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdown.html',
            scope: {
                btnText: "@",
                btnType: "@",
                btnLink: "@",
                btnClick: "&",
                toggleTitle:"@",                
            },
            controller: ['$scope', '$element', function ($scope, $element) {
                    
                this.cSelected = 0;    
                this.closeAndFocusDropdown = function () {
                    if ($scope.isDropDownOpen) {
                        $scope.$apply(function () {
                        $scope.isDropDownOpen = false;
                        angular.element($element[0].querySelector('a.dropdown-toggle'))[0].focus();                        
                        });
                    }
                };             
                
                this.focusNext = function () {                                            
                    this.cSelected = this.cSelected + 1 >= this.childScopes.length ?($scope.cycleSelection === true ? 0 : this.childScopes.length-1): this.cSelected +1;
                    this.childScopes[this.cSelected].sFlag = true;                    
                    this.resetFlag(this.cSelected);                    
                }; 
                
                this.focusPrev = function () {                        
                    this.cSelected = this.cSelected -1 < 0 ? ($scope.cycleSelection === true ? this.childScopes.length-1 : 0) : this.cSelected - 1 ;
                    this.childScopes[this.cSelected].sFlag = true;                    
                    this.resetFlag(this.cSelected);                    
                };                 
                                                
                this.childScopes = [];
                this.registerScope = function(childScope)
                {
                    this.childScopes.push(childScope);                    
                };
                
                this.resetFlag = function(index){                                        
                    for(var i=0; i < this.childScopes.length; i++) 
                    {
                        if(i !== index)
                        {
                            this.childScopes[i].sFlag = false;
                        }
                    }
                };
                
            }],
            link: function (scope, element, attr) {                
                scope.isSmall = attr.small === "" ? true : false;
                scope.multiselect = attr.multiselect === ""? true : false;
                scope.cycleSelection = attr.cycleSelection === "" ? true : false;
                scope.isDropDownOpen = false;
                scope.isActionDropdown = false;
               
                if (!(scope.btnText)) {
                    scope.isActionDropdown = true;
                }

                scope.clickFxn = function () {
                    if (typeof scope.btnClick === "function" && !scope.btnLink) {
                        scope.btnClick = $parse(scope.btnClick);
                        scope.btnClick();
                    }                    
                    if(scope.multiselect === true)
                    {
                        scope.isDropDownOpen = false;
                    }
                };

                scope.toggleDropdown = function () {
                    if (!(scope.btnType === 'disabled')) {
                        scope.isDropDownOpen = !scope.isDropDownOpen;
                        if (scope.isDropDownOpen) {
                            $timeout(function () {
                                angular.element(element[0].querySelector('li'))[0].focus();
                            });
                        }
                    }
                };

                scope.btnTypeSelector = function (directiveValue, attrValue) {
                    if (directiveValue !== "") {
                        scope.btnTypeFinal = directiveValue;
                    } else {
                        scope.btnTypeFinal = attrValue;
                    }
                };

                var outsideClick = function(e) {
                    var isElement = $isElement(angular.element(e.target), element.find('ul').eq(0), $document);
                    if (!isElement) {
                        scope.isDropDownOpen = false;
                        scope.$apply();
                    }
                };

                $documentBind.click('isDropDownOpen', outsideClick, scope);

                attr.$observe('btnType', function (val) {
                    scope.btnType = val;
                });
                attr.$observe('attButtonDropdown', function (val) {
                    attr.attButtonDropdown = val;
                    scope.btnTypeSelector(attr.attButtonDropdown, scope.btnType);
                });
            }
        };

            }])
       
.directive('attButtonDropdownItem', ['$location','keymap', function ($location,keymap) {
        return {
            restrict: 'EA',
            require: ['^attButtonDropdown','?ngModel'],
            replace: true,
            transclude: true,            
            templateUrl:'app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdownItem.html',                                                 scope: {
                itemLink: "@"
            },
            link: function (scope, element, attr, ctrl) {
                var rootLink = angular.element(element[0].querySelector('a'));
                scope.sFlag = false;
                ctrl[0].registerScope(scope);
                var clickOnLink = function () {
                    if (scope.itemLinkFinal) {
                        $location.url(scope.itemLinkFinal);
                    }
                };
                
                if(ctrl[1]){
                    scope.isSelected = ctrl[1].$viewValue;
                }else{
                    scope.isSelected = false;
                }                
                
                element.bind('keydown', function(e) {
                if (keymap.isAllowedKey(e.keyCode) || keymap.isControl(e) || keymap.isFunctionKey(e)) {
                    e.preventDefault();
                    e.stopPropagation();
                    switch (e.keyCode) {
                        case keymap.KEY.DOWN: 
                            ctrl[0].focusNext();                             
                            break;
                        case keymap.KEY.UP:                            
                            ctrl[0].focusPrev();
                            break;
                        case keymap.KEY.ENTER:                                                        
                            scope.selectItem();
                            break;
                        case keymap.KEY.ESC:
                            ctrl[0].closeAndFocusDropdown();
                            break;
                        default:
                            break;
                    }
                    
                }
                    scope.$apply();
                }); 
                                 
                scope.selectItem = function()
                {
                    if(ctrl[1]){
                        scope.$evalAsync(function(){ctrl[1].$setViewValue(!ctrl[1].$viewValue)});
                    }
                }; 

            }
        };
    }]);

angular.module('att.abs.splitIconButton', ['att.abs.utilities'])
.constant('iconStateConstants', {
    MIDDLE: 'middle',
    LEFT: 'left',
    RIGHT: 'right',
    NEXT_TO_DROPDOWN:'next-to-dropdown',
    LEFT_NEXT_TO_DROPDOWN:'left-next-to-dropdown',
    DIR_TYPE: {
        LEFT: 'left',
        RIGHT:  'right',
        BUTTON: 'button'
    },
    SPLIT_ICON_BTN_EVENT_EMITTER_KEY: 'splitIconButtonTap'
})
.directive('expandableLine', [function(){
    return {
        restrict: 'EA',
        replace: true,
        priority: 300,
        require: ['^attSplitIconButton', 'expandableLine'],
        controller: ['$scope', function($scope){
            $scope.isActive = false;
            this.setActiveState = function(isActive){
                $scope.isActive = isActive;
            };
            this.isActive = $scope.isActive;
            this.dirType = $scope.dirType;
        }],
        template: '<div ng-class="{\'expand-line-container\': !isActive, \'expand-line-container-active\': isActive}"> <div ng-class="{\'hovered-line\':isActive, \'vertical-line\':!isActive}"> </div></div>',
        scope:{
            dirType: '@'
        },
        link: function(scope,element,attr,ctrls) {
            var attSplitIconButtonCtrl = ctrls[0];
            var expandableLineCtrl = ctrls[1];
            attSplitIconButtonCtrl.addSubCtrl(expandableLineCtrl);
        }
    };
}])
.controller('AttSplitIconCtrl', ['$scope', function($scope){
    this.setType = function(type){
        $scope.type = type;
    };  
    this.isDropdown = function(isDropdown){
         $scope.isDropdown = isDropdown;
    };
    this.dropDownClicked = function(){
        if($scope.dropDownClicked) {
            $scope.dropDownClicked();
        }
    };
    this.dirType = $scope.dirType;
}])
.directive('attSplitIcon', ['$document', '$timeout','iconStateConstants','$documentBind','events', 'keymap',
 function($document,$timeout,iconStateConstants,$documentBind, events, keymap){
    return {
        restrict: 'EA',
        replace: true,
        priority: 200,
        transclude: true,
        require: ['^attSplitIconButton','attSplitIcon'],
        templateUrl: 'app/scripts/ng_js_att_tpls/splitIconButton/splitIcon.html',
        scope:{
            icon: '@',
            iconTitle: '@title',
            hoverWatch: '=',
            dropDownWatch: '=',
            dirType: '@'
        },
        controller:'AttSplitIconCtrl',
        link: function(scope,element,attr,ctrls){
            var attSplitIconButtonCtrl = ctrls[0];
            var attSplitIconCtrl = ctrls[1];
            attSplitIconButtonCtrl.addSubCtrl(attSplitIconCtrl);
            scope.iconStateConstants = iconStateConstants;
            var currentIndex = 0;
            var isMyElement = false;
            var listElements;
            scope.isDropdown = false;
            scope.isDropdownOpen = false;
            var outsideClick = function(e) {
                if(scope.isDropdown){
                    if (isMyElement) {
                        isMyElement = false;
                        scope.toggleDropdown();
                    } else{
                        scope.toggleDropdown(false);
                    }
                    scope.$apply();
                }
            };
            if(attr.dropDownId && attr.dropDownId !== ''){
                scope.dropDownId = attr.dropDownId;
                scope.isDropdown = true;
            }
            scope.$on(iconStateConstants.SPLIT_ICON_BTN_EVENT_EMITTER_KEY, function(evnt, data){
                if(typeof data === 'boolean' && data) {
                    scope.dropDownClicked();     
                    /*
                        Check if the dropdown is open and if we are selecting one
                        of the items, so that when pressing enter it will trigger it.
                    */
                    if(scope.isDropDownOpen) {
                        listElements[currentIndex].eq(0).find('a')[0].click();
                    }
                } else {
                    var e = data;
                    //Only trigger the keyboard event if the icon button is a dropdown type
                    if(scope.isDropdown) {
                        triggerKeyboardEvents(e);   
                    }
                }
                function triggerKeyboardEvents(e) {
                    switch(e.which) {
                        case (keymap.KEY.TAB):
                            scope.toggleDropdown(false);
                            scope.$digest();
                            break;
                        case (keymap.KEY.ESC): 
                            outsideClick();
                            break;
                        case (keymap.KEY.ENTER):
                            if (scope.isDropDownOpen) {
                                listElementsInit();
                            }
                            break;
                        case (keymap.KEY.UP):
                            e.preventDefault();
                            events.stopPropagation(e);
                            if(scope.isDropDownOpen) {
                                scope.previousItemInDropdown();
                            }
                            break;
                        case (keymap.KEY.DOWN):
                            e.preventDefault();
                            events.stopPropagation(e);
                            //Dropdown is open and the user taps down again
                            if(scope.isDropDownOpen) {
                                //Now we need to go through the rows in the dropdown
                                scope.nextItemInDropdown();
                            } else {
                                isMyElement = true;
                                outsideClick();
                                listElementsInit();
                            }
                            break;
                        default:
                            break;
                    }
                }
                function listElementsInit() {
                    if(listElements === undefined) {
                        listElements = [];
                        var liTemps = element.find('li');
                        for(var i = 0; i < liTemps.length; i++) {
                            listElements.push(liTemps.eq(i));
                        }
                        listElements[currentIndex].children().eq(0).addClass('selected-item');
                    }
                }
            });
            scope.nextItemInDropdown = function(){
                if(listElements && currentIndex < listElements.length - 1){
                    currentIndex++;
                    listElements[currentIndex - 1].children().eq(0).removeClass('selected-item');
                    listElements[currentIndex].children().eq(0).addClass('selected-item');
                }
            };
            scope.previousItemInDropdown = function(){
                if(currentIndex > 0) {
                    currentIndex--;
                    listElements[currentIndex].children().eq(0).addClass('selected-item');

                    if(currentIndex + 1 < listElements.length)
                        listElements[currentIndex + 1].children().eq(0).removeClass('selected-item');
                }
            };
            scope.$watch('isIconHovered', function(val) {
                scope.hoverWatch = val;
            });
            scope.$watch('type', function(val) {
                function toggleValues(isMiddle,isNextToDropDown,isRight,isLeft,isLeftNextDropdown){
                    scope['isMiddle']  = isMiddle;
                    scope['isNextToDropDown'] = isNextToDropDown;
                    scope['isRight']  = isRight;
                    scope['isLeft'] = isLeft;
                    scope['isLeftNextDropdown'] = isLeftNextDropdown;
                };
                switch(val) {
                    case (scope.iconStateConstants.MIDDLE):
                        toggleValues(true,false,false,true,false);
                        break;
                    case (scope.iconStateConstants.LEFT):
                        toggleValues(false,false,false,true,false);
                        break;
                    case (scope.iconStateConstants.RIGHT):
                        toggleValues(false,false,true,false,false);
                        break;
                    case (scope.iconStateConstants.NEXT_TO_DROPDOWN):
                        toggleValues(false,true,true,true,false);
                        break;
                    case (scope.iconStateConstants.LEFT_NEXT_TO_DROPDOWN):
                        toggleValues(false,false,false,true,true);
                        break;
                    default:
                        break;
                }
            });
            scope.dropDownClicked = function() {
                isMyElement = true;
            };
            scope.toggleDropdown = function(val) {
                if(val !== undefined) {
                    scope.isDropDownOpen = val;
                } else {
                    scope.isDropDownOpen = !scope.isDropDownOpen; 
                }
                scope.dropDownWatch = scope.isDropDownOpen;
            };
            $documentBind.click('isDropdown', outsideClick, scope);
        }
    };
}])
.controller('AttSplitIconButtonCtrl',['$scope', 'iconStateConstants',function($scope,iconStateConstants){
    this.subCtrls = [];
    $scope.isLeftLineShown=true;
    $scope.isRightLineShown=true;
    $scope.childrenScopes = [];
    var that = this;

    function getDirIndex(dirType) {
        var index = -1;
        for(var c in that.subCtrls) {
            var ctrl = that.subCtrls[c];
            if(ctrl.dirType === dirType) {
                index = c;
                break;
            }
        }
        return index;
    }
    this.addSubCtrl =  function(sub) {
        this.subCtrls.push(sub);
    };
    this.isLeftLineShown = function(isShown) {
        if(isShown === undefined) {
            return $scope.isLeftLineShown;
        } else {
            $scope.isLeftLineShown = isShown;
        }
    };
    this.isRightLineShown = function(isShown) {
        if(isShown === undefined) {
            return $scope.isRightLineShown;
        } else {
            $scope.isRightLineShown = isShown;
        }
    };
    this.setLeftLineHover = function(isHovered) {
        var leftLineIndex = getDirIndex(iconStateConstants.DIR_TYPE.LEFT);

        if($scope.isLeftLineShown && this.subCtrls[leftLineIndex] && this.subCtrls[leftLineIndex].setActiveState) {
            this.subCtrls[leftLineIndex].setActiveState(isHovered);
        }
    };
    this.setRightLineHover = function(isHovered) {
        var rightLineIndex = getDirIndex(iconStateConstants.DIR_TYPE.RIGHT);
        if($scope.isRightLineShown && this.subCtrls[rightLineIndex] && this.subCtrls[rightLineIndex].setActiveState){
            this.subCtrls[rightLineIndex].setActiveState(isHovered);
        }
    };
    this.toggleLines = function(isHovered, buttonGroupCtrl, buttonCtrl, isDropDownOpen) {  
        var subIconButtons = buttonGroupCtrl.subIconButtons;
        var subIconButtonsLength = subIconButtons.length;
        var leftLineIndex =  getDirIndex(iconStateConstants.DIR_TYPE.LEFT);
        var rightLineIndex =  getDirIndex(iconStateConstants.DIR_TYPE.RIGHT);
        function noVerticalLineToggle() {
            for(var i =0; i < subIconButtonsLength; i++) {
                if(subIconButtons[i] === buttonCtrl) {
                    if(i + 1 <= subIconButtonsLength - 1 && subIconButtons[i+1].isLeftLineShown() 
                            && subIconButtons[i+1].subCtrls[leftLineIndex] 
                            && subIconButtons[i+1].subCtrls[leftLineIndex].setActiveState) {
                        subIconButtons[i+1].subCtrls[leftLineIndex].setActiveState(isHovered);
                    }
                    if(i - 1 >= 0 && subIconButtons[i-1].isRightLineShown() 
                            && subIconButtons[i-1].subCtrls[rightLineIndex] 
                            && subIconButtons[i-1].subCtrls[rightLineIndex].setActiveState) {
                        subIconButtons[i-1].subCtrls[rightLineIndex].setActiveState(isHovered);
                    }
                    break;
                }
            }
        }   
        if(isDropDownOpen) {
            /*
              If the button is next to the dropdown button then just keep the 
              buttons left line or its left neighbors right line toggled on
              If the button is the dropdown button don't do anything
              else do things normally witht the button
            */
            /*if(subIconButtons[subIconButtonsLength-1] === buttonCtrl) {

            }
            else */
            if(subIconButtons[subIconButtonsLength-2]==buttonCtrl) {
                if(subIconButtons[subIconButtonsLength-2].isLeftLineShown()) {
                    subIconButtons[subIconButtonsLength-2].subCtrls[leftLineIndex].setActiveState(isHovered);
                } else if(subIconButtonsLength  - 3 >= 0) {
                    if(subIconButtons[subIconButtonsLength-3].isRightLineShown()) {
                        subIconButtons[subIconButtonsLength-3].subCtrls[rightLineIndex].setActiveState(isHovered);
                    }
                }
            } else {
                noVerticalLineToggle();

                if($scope.isLeftLineShown) {
                    this.subCtrls[leftLineIndex].setActiveState(isHovered);
                }
                if($scope.isRightLineShown) {
                    this.subCtrls[rightLineIndex].setActiveState(isHovered);
                }
            }
        } else { // End of if(isDropDownOpen)
            //Handle Special cases where they aren't showing any vertical lines
            //and the dropdown isn't down
            if(!$scope.isLeftLineShown && !$scope.isRightLineShown) {
                noVerticalLineToggle();
            }   
            if($scope.isLeftLineShown && this.subCtrls[leftLineIndex].setActiveState) {
                this.subCtrls[leftLineIndex].setActiveState(isHovered);
            }
            if($scope.isRightLineShown && this.subCtrls[rightLineIndex].setActiveState){
                this.subCtrls[rightLineIndex].setActiveState(isHovered);
            }
        }
    };
    this.setButtonType = function(type){
        var buttonIndex = getDirIndex(iconStateConstants.DIR_TYPE.BUTTON);
        if(this.subCtrls[buttonIndex] && this.subCtrls[buttonIndex].setType) {
            this.subCtrls[buttonIndex].setType(type);
        }
    };
}])
.directive('attSplitIconButton', ['$document', 'iconStateConstants', 'keymap',
    function($document, iconStateConstants, keymap){
    return {
        restrict: 'EA',
        replace: true,
        priority: 100,
        transclude: true,
        require: ['^attSplitIconButtonGroup', 'attSplitIconButton'],
        controller: 'AttSplitIconButtonCtrl',
        templateUrl: 'app/scripts/ng_js_att_tpls/splitIconButton/splitIconButton.html',
        scope:{
            icon: '@',
            title: '@',
            dropDownId: '@'
        },
        link: function(scope,element,attr,ctrls) {
            if(!scope.title) {
                scope.title = scope.icon;
            }
            var attSplitButtonGroupCtrl = ctrls[0];
            var attSplitIconButtonCtrl = ctrls[1];
            attSplitButtonGroupCtrl.addIconButton(attSplitIconButtonCtrl);
            element.bind('keydown', function(e){
                //Check if the key is the up or down key
                if(e.which === keymap.KEY.ESC ||
                    e.which === keymap.KEY.DOWN ||
                    e.which === keymap.KEY.ENTER ||
                    e.which === keymap.KEY.UP ||
                    e.which === keymap.KEY.TAB ) {
                    scope.clickHandler();
                    scope.$broadcast(iconStateConstants.SPLIT_ICON_BTN_EVENT_EMITTER_KEY, e);
                }
            });
            scope.dropDownWatch = false;
            scope.iconStateConstants = iconStateConstants;
            scope.clickHandler = function() {
                attSplitButtonGroupCtrl.hideLeftLineRightButton(attSplitIconButtonCtrl);
            };
            scope.$watch('isHovered', function(val){
                if(val) {
                    attSplitIconButtonCtrl.toggleLines(val,attSplitButtonGroupCtrl,attSplitIconButtonCtrl,attSplitButtonGroupCtrl.isDropDownOpen);
                } else{
                    attSplitIconButtonCtrl.toggleLines(val,attSplitButtonGroupCtrl,attSplitIconButtonCtrl,attSplitButtonGroupCtrl.isDropDownOpen);
                }
            });
            scope.$watch('dropDownWatch', function(val) {
                attSplitButtonGroupCtrl.isDropDownOpen = val;
                attSplitButtonGroupCtrl.toggleDropdownState(val);
            });
        }
    }
}])
.controller('AttSplitIconButtonGroupCtrl',   ['$scope','iconStateConstants',function($scope,iconStateConstants){
    this.subIconButtons = [];
    this.addIconButton = function(iconButton){
       this.subIconButtons.push(iconButton);
    };
    this.isDropDownOpen = false;
    this.hideLeftLineRightButton = function(btn){
        var numButtons = this.subIconButtons.length;
        var buttonLeftOfRightMost = this.subIconButtons[numButtons - 2];
        var rightMostButton = this.subIconButtons[numButtons -1];

        if (btn != buttonLeftOfRightMost && btn != rightMostButton ){
            rightMostButton.setLeftLineHover(false);
        }
    };
    this.toggleDropdownState = function(isDropDownOpen){
        var numButtons = this.subIconButtons.length;
        if(numButtons > 2) {
            if(isDropDownOpen) {
                if(this.subIconButtons[numButtons - 2].isRightLineShown()) {
                    this.subIconButtons[numButtons - 2].setRightLineHover(true);
                } else {
                    this.subIconButtons[numButtons - 1].setLeftLineHover(true);
                }
                this.subIconButtons[numButtons - 2].setButtonType(iconStateConstants.NEXT_TO_DROPDOWN);
            } else {
                this.subIconButtons[numButtons - 1].setLeftLineHover(false);
                this.subIconButtons[numButtons - 2].setButtonType(iconStateConstants.MIDDLE);
            }
        } else {
            if(isDropDownOpen) {
                this.subIconButtons[0].setRightLineHover(true);
                this.subIconButtons[0].setButtonType(iconStateConstants.LEFT_NEXT_TO_DROPDOWN);
            } else {
                this.subIconButtons[0].setButtonType(iconStateConstants.LEFT);
            }
        }
    };
}])
.directive('attSplitIconButtonGroup', ['$document', '$timeout',  'iconStateConstants' ,function($document,$timeout,iconStateConstants){
    return {
        restrict: 'EA',
        replace: true,
        priority: 50,
        transclude: true,
        require: 'attSplitIconButtonGroup',
        controller: 'AttSplitIconButtonGroupCtrl',
        templateUrl: 'app/scripts/ng_js_att_tpls/splitIconButton/splitIconButtonGroup.html',
        scope:{},
        link: function(scope,element,attr,ctrls){
            $timeout(initialize,100);
            function initialize(){
                var subIconButtonCtrls = ctrls.subIconButtons;
                var leftMostButtonIndex = 0;
                var rightMostButtonIndex =subIconButtonCtrls.length-1;
                //left most button config
                subIconButtonCtrls[leftMostButtonIndex].setButtonType(iconStateConstants.LEFT);
                subIconButtonCtrls[leftMostButtonIndex].isLeftLineShown(false);
                subIconButtonCtrls[leftMostButtonIndex].isRightLineShown(true);
                //right most button config
                subIconButtonCtrls[rightMostButtonIndex].setButtonType(iconStateConstants.RIGHT);
                subIconButtonCtrls[rightMostButtonIndex].isRightLineShown(false);
                subIconButtonCtrls[rightMostButtonIndex].isLeftLineShown(false);
                //middle buttons config
                if(rightMostButtonIndex >= 2) {
                    var index = 1;
                    while(index < rightMostButtonIndex) {
                        subIconButtonCtrls[index].setButtonType(iconStateConstants.MIDDLE);
                        subIconButtonCtrls[index].isRightLineShown(false);
                        subIconButtonCtrls[index].isLeftLineShown(false);
                        index++;
                    }
                    var skipIndex = 2;
                    while(skipIndex <= rightMostButtonIndex){
                        if(skipIndex == rightMostButtonIndex) {
                            subIconButtonCtrls[skipIndex].isLeftLineShown(true);
                        } else {
                            subIconButtonCtrls[skipIndex].isRightLineShown(true);
                            subIconButtonCtrls[skipIndex].isLeftLineShown(true); 
                        } 
                        skipIndex = skipIndex + 2;
                    }
                }
                //reposition the dropdown
                var ulElem = element.find('ul');
                if(ulElem.length > 0) {
                    var numButtons = rightMostButtonIndex+1;
                    if(numButtons > 2) {
                        var offset = (numButtons)*34-70+(numButtons/1.5) + 0.5;
                        var offSetStr = offset+'px';
                        angular.element(ulElem).css('left',offSetStr);
                        angular.element(ulElem).css('border-top-left-radius','0px');
                    } else {
                        angular.element(ulElem).css('left','0px');
                    }
                }
            }
        }
    }
}]);

angular.module('att.abs.stepSlider', ['att.abs.position'])
        .constant('sliderConstants', {
            /*
             The MIT License (MIT)
             Copyright (c) 2013 Julien Valéry
             */
            SLIDER: {
                settings: {
                    from: 1,
                    to: 40,
                    step: 1,
                    smooth: true,
                    limits: true,
                    value: "3",
                    dimension: "",
                    vertical: false
                },
                className: "jslider",
                selector: ".jslider-"
            },
            EVENTS: {
            },
            COLORS: {
                GREEN: 'green',
                BLUE_HIGHLIGHT: 'blue',
                MAGENTA: 'magenta',
                GOLD: 'gold',
                PURPLE: 'purple',
                DARK_BLUE: 'dark-blue',
                REGULAR: 'regular',
                WHITE: 'white'
            }
        })
        .factory('utils', function () {
            /*
             The MIT License (MIT)
             Copyright (c) 2013 Julien Valéry
             */
            return {
                offset: function (elm) {
                    var rawDom = elm[0];
                    var _x = 0;
                    var _y = 0;
                    var body = document.documentElement || document.body;
                    var scrollX = window.pageXOffset || body.scrollLeft;
                    var scrollY = window.pageYOffset || body.scrollTop;
                    _x = rawDom.getBoundingClientRect().left + scrollX;
                    _y = rawDom.getBoundingClientRect().top + scrollY;
                    return {left: _x, top: _y};
                },
                roundUpToScale: function (mousePrc, scale, cutOffWidth, cutOffIndex) {
                    var lowerVal;
                    var higherVal;
                    var newMousePrc;
                    var middle;

                    for (var index = 1; index < scale.length; index++) {
                        lowerVal = scale[index - 1];
                        higherVal = scale[index];
                        middle = ((higherVal - lowerVal) * .5) + lowerVal;
                        /*
                         Handles a situation where the user clicks close to the start point of
                         the slider but the pointer doesn't move
                         */
                        if ((lowerVal === 0 && mousePrc <= middle) || checkEquality(lowerVal, mousePrc)) {
                            newMousePrc = lowerVal;
                            break;
                        }
                        else if (lowerVal < mousePrc && (mousePrc < higherVal ||
                                checkEquality(mousePrc, higherVal)))
                        {
                            newMousePrc = higherVal;
                            break;
                        }
                    }
                    //Check if the newMousePrc is <= the cuttOffPoint
                    if (cutOffWidth && newMousePrc < cutOffWidth) {
                        return scale[cutOffIndex];
                    }
                    else {
                        return newMousePrc;
                    }
                    /*
                     Checks to see if 2 points are so close that they are
                     basically equal.
                     */
                    function checkEquality(point1, point2) {
                        var precision = 0.1;
                        if (Math.abs(point2 - point1) <= precision) {
                            return true;
                        }
                        return false;
                    }
                },
                valueForDifferentScale: function (from, to, prc, prcToValueMapper) {
                    var decimalPrc = prc / 100;
                    if (decimalPrc === 0) {
                        return from;
                    }
                    return prcToValueMapper[prc];
                },
                /* converts the default value Kbps to Mbps or Gbps */
                convertToMbpsGbps: function (unitValue, unitLabel, configDecimalPlaces) {
                    var defaultDecimalPlaces = 3; /* this is the default decimal places as per business requirements */
                    if (configDecimalPlaces) {
                        defaultDecimalPlaces = configDecimalPlaces;
                    }

                    if ((unitValue > 1024 && unitValue < 1000000) && angular.equals(unitLabel, 'Kbps')) {
                        unitValue = truncator((unitValue/1000), defaultDecimalPlaces);
                        unitLabel = 'Mbps';
                    } else if ((unitValue > 1024 && unitValue < 1000000) && angular.equals(unitLabel, 'Mbps')){
                        unitValue = truncator((unitValue/1000), defaultDecimalPlaces);
                        unitLabel = 'Mbps';
                    } else if (unitValue <= 1024 && angular.equals(unitLabel, 'Mbps')) {
                        unitLabel = 'Kbps';
                    } else {
                        unitLabel = 'Kbps';
                    }

                    if (unitValue >= 1000000 && angular.equals(unitLabel, 'Kbps')) {
                        unitValue = truncator((unitValue/1000000), defaultDecimalPlaces);
                        unitLabel = 'Gbps';
                    }
                    return {
                        unitValue: unitValue, 
                        unitLabel: unitLabel
                    };
                    
                    function truncator(numToTruncate, intDecimalPlaces) {    
                        var cnvrtdNum = Math.pow(10, intDecimalPlaces);
                        return ~~(numToTruncate * cnvrtdNum)/cnvrtdNum;
                    }
                },
                getConversionFactorValue: function (value, conversion, firstDimension) {
                    /*
                     Loop through the conversion array and keep checking the
                     startVal
                     */
                    if (value <= conversion[0].startVal) {
                        return{
                            scaledVal: value,
                            scaledDimension: firstDimension
                        };
                    }
                    var endIndex = 0;
                    for (var index in conversion) {
                        var c = conversion[index];
                        if (value > c.startVal) {
                            endIndex = index;
                        }
                    }
                    var scaleFactor = conversion[endIndex].scaleFactor;
                    var scaledVal = value / scaleFactor;
                    var scaledDimension = conversion[endIndex].dimension;
                    return {
                        scaledVal: scaledVal,
                        scaledDimension: scaledDimension
                    };
                }
            };
        })
        .factory('sliderDraggable', ['utils', function (utils) {
                /*
                 The MIT License (MIT)
                 Copyright (c) 2013 Julien Valéry
                 */
                function Draggable() {
                    this._init.apply(this, arguments);
                }
                Draggable.prototype.oninit = function () {
                };
                Draggable.prototype.events = function () {
                };
                Draggable.prototype.onmousedown = function () {
                    this.ptr.css({position: "absolute"});
                };
                Draggable.prototype.onmousemove = function (evt, x, y) {
                    this.ptr.css({left: x, top: y});
                };
                Draggable.prototype.onmouseup = function () {
                };
                Draggable.prototype.isDefault = {
                    drag: false,
                    clicked: false,
                    toclick: true,
                    mouseup: false
                };
                Draggable.prototype._init = function () {
                    if (arguments.length > 0) {
                        this.ptr = arguments[0];
                        this.parent = arguments[2];
                        if (!this.ptr) {
                            return;
                        }
                        this.is = {};
                        angular.extend(this.is, this.isDefault);
                        var offset = utils.offset(this.ptr);
                        this.d = {
                            left: offset.left,
                            top: offset.top,
                            width: this.ptr[0].clientWidth,
                            height: this.ptr[0].clientHeight
                        };
                        this.oninit.apply(this, arguments);
                        this._events();
                    }
                };
                Draggable.prototype._getPageCoords = function (event) {
                    var value = {};
                    if (event.targetTouches && event.targetTouches[0]) {
                        value = {x: event.targetTouches[0].pageX, y: event.targetTouches[0].pageY};
                    } else {
                        value = {x: event.pageX, y: event.pageY};
                    }
                    return value;
                };
                Draggable.prototype._bindEvent = function (ptr, eventType, handler) {
                    if (this.supportTouches_) {
                        ptr[0].attachEvent(this.events_[ eventType ], handler);
                    }
                    else {
                        if (ptr.bind) {
                            ptr.bind(this.events_[ eventType ], handler);
                        }
                    }
                };
                Draggable.prototype._events = function () {
                    var self = this;
                    this.supportTouches_ = 'ontouchend' in document;
                    this.events_ = {
                        "click": this.supportTouches_ ? "touchstart" : "click",
                        "down": this.supportTouches_ ? "touchstart" : "mousedown",
                        "move": this.supportTouches_ ? "touchmove" : "mousemove",
                        "up": this.supportTouches_ ? "touchend" : "mouseup",
                        "mousedown": this.supportTouches_ ? "mousedown" : "mousedown"
                    };
                    var documentElt = angular.element(window.document);
                    this._bindEvent(documentElt, "move", function (event) {
                        if (self.is.drag) {
                            event.stopPropagation();
                            event.preventDefault();
                            if (!self.parent.disabled) {
                                self._mousemove(event);
                            }
                        }
                    });
                    this._bindEvent(documentElt, "down", function (event) {
                        if (self.is.drag) {
                            event.stopPropagation();
                            event.preventDefault();
                        }
                    });
                    this._bindEvent(documentElt, "up", function (event) {
                        self._mouseup(event);
                    });
                    this._bindEvent(this.ptr, "down", function (event) {
                        self._mousedown(event);
                        return false;
                    });
                    this._bindEvent(this.ptr, "up", function (event) {
                        self._mouseup(event);
                    });
                    this.events();
                };
                Draggable.prototype._mousedown = function (evt) {
                    this.is.drag = true;
                    this.is.clicked = false;
                    this.is.mouseup = false;
                    var coords = this._getPageCoords(evt);
                    this.cx = coords.x - this.ptr[0].offsetLeft;
                    this.cy = coords.y - this.ptr[0].offsetTop;
                    angular.extend(this.d, {
                        left: this.ptr[0].offsetLeft,
                        top: this.ptr[0].offsetTop,
                        width: this.ptr[0].clientWidth,
                        height: this.ptr[0].clientHeight
                    });
                    if (this.outer && this.outer.get(0)) {
                        this.outer.css({height: Math.max(this.outer.height(), $(document.body).height()), overflow: "hidden"});
                    }
                    this.onmousedown(evt);
                };
                Draggable.prototype._mousemove = function (evt) {
                    if (this.uid === 0) {
                        return;
                    }
                    this.is.toclick = false;
                    var coords = this._getPageCoords(evt);
                    this.onmousemove(evt, coords.x - this.cx, coords.y - this.cy);
                };
                Draggable.prototype._mouseup = function (evt) {
                    if (this.is.drag) {
                        this.is.drag = false;
                        if (this.outer && this.outer.get(0)) {
                            if ($.browser.mozilla) {
                                this.outer.css({overflow: "hidden"});
                            } else {
                                this.outer.css({overflow: "visible"});
                            }
                            if ($.browser.msie && $.browser.version === '6.0') {
                                this.outer.css({height: "100%"});
                            } else {
                                this.outer.css({height: "auto"});
                            }
                        }
                        this.onmouseup(evt);
                    }
                };
                return Draggable;
            }])
        .factory('sliderPointer', ['sliderDraggable', 'utils', function (Draggable, utils) {
                /*
                 The MIT License (MIT)
                 Copyright (c) 2013 Julien Valéry
                 */
                function SliderPointer() {
                    Draggable.apply(this, arguments);
                }
                SliderPointer.prototype = new Draggable();
                SliderPointer.prototype.oninit = function (ptr, id, _constructor) {
                    this.uid = id;
                    this.parent = _constructor;
                    this.value = {};
                    this.settings = angular.copy(_constructor.settings);
                };
                SliderPointer.prototype.onmousedown = function (evt) {
                    var off = utils.offset(this.parent.domNode);
                    var offset = {
                        left: off.left,
                        top: off.top,
                        width: this.parent.domNode[0].clientWidth,
                        height: this.parent.domNode[0].clientHeight
                    };
                    this._parent = {
                        offset: offset,
                        width: offset.width,
                        height: offset.height
                    };
                    this.ptr.addClass("jslider-pointer-hover");
                    this.setIndexOver();
                };
                SliderPointer.prototype.onmousemove = function (evt, x, y) {
                    var coords = this._getPageCoords(evt);
                    //val is the percent where the slider pointer is located
                    var val = this.calc(coords.x);
                    if (!this.parent.settings.smooth) {
                        val = utils.roundUpToScale(val,
                                this.parent.settings.scale,
                                this.parent.settings.cutOffWidth,
                                this.parent.settings.cutOffIndex);
                    }
                    var cutOffWidth = this.parent.settings.cutOffWidth;
                    if (cutOffWidth && val < cutOffWidth) {
                        val = cutOffWidth;
                    }
                    this._set(val);
                };
                SliderPointer.prototype.onmouseup = function (evt) {
                    if (this.settings.callback && angular.isFunction(this.settings.callback)) {
                        var val = this.parent.getValue();
                        this.settings.callback.call(this.parent, val);
                    }
                    this.ptr.removeClass("jslider-pointer-hover");
                };
                SliderPointer.prototype.setIndexOver = function () {
                    this.parent.setPointersIndex(1);
                    this.index(2);
                };
                SliderPointer.prototype.index = function (i) {
                };
                SliderPointer.prototype.limits = function (x) {
                    return this.parent.limits(x, this);
                };
                SliderPointer.prototype.calc = function (coords) {
                    var diff = coords - this._parent.offset.left;
                    var val = this.limits((diff * 100) / this._parent.width);
                    return val;
                };
                SliderPointer.prototype.set = function (value, opt_origin) {
                    this.value.origin = this.parent.round(value);
                    this._set(this.parent.valueToPrc(value, this), opt_origin);
                };
                SliderPointer.prototype._set = function (prc, opt_origin) {
                    if (!opt_origin) {
                        this.value.origin = this.parent.prcToValue(prc);
                    }
                    this.value.prc = prc;
                    //Sets the location of the SliderPointer
                    this.ptr.css({left: prc + '%'});
                    this.parent.redraw(this);
                };
                return SliderPointer;
            }])
        .factory('slider', ['sliderPointer', 'sliderConstants', 'utils', function (SliderPointer, sliderConstants, utils) {
                /*
                 The MIT License (MIT)
                 Copyright (c) 2013 Julien Valéry
                 */
                var cutOffDom;
                function Slider() {
                    return this.init.apply(this, arguments);
                }
                function changeCutOffWidth(width) {
                    cutOffDom.css('width', width);
                }
                ;
                Slider.prototype.changeCutOffWidth = changeCutOffWidth;
                Slider.prototype.init = function (inputNode, templateNode, settings) {
                    this.settings = sliderConstants.SLIDER.settings;
                    angular.extend(this.settings, angular.copy(settings));
                    this.inputNode = inputNode;
                    this.inputNode.addClass("ng-hide");
                    this.settings.interval = this.settings.to - this.settings.from;
                    if (this.settings.calculate && $.isFunction(this.settings.calculate)) {
                        this.nice = this.settings.calculate;
                    }
                    if (this.settings.onstatechange && $.isFunction(this.settings.onstatechange)) {
                        this.onstatechange = this.settings.onstatechange;
                    }
                    this.is = {init: false};
                    this.o = {};
                    this.create(templateNode);
                };
                Slider.prototype.create = function (templateNode) {
                    var $this = this;
                    this.domNode = templateNode;
                    var off = utils.offset(this.domNode);
                    var offset = {
                        left: off.left,
                        top: off.top,
                        width: this.domNode[0].clientWidth,
                        height: this.domNode[0].clientHeight
                    };
                    this.sizes = {domWidth: this.domNode[0].clientWidth, domOffset: offset};
                    angular.extend(this.o, {
                        pointers: {},
                        labels: {
                            0: {
                                o: angular.element(this.domNode.find('div')[5])
                            },
                            1: {
                                o: angular.element(this.domNode.find('div')[6])
                            }
                        },
                        limits: {
                            0: angular.element(this.domNode.find('div')[3]),
                            1: angular.element(this.domNode.find('div')[5])
                        }
                    });
                    angular.extend(this.o.labels[0], {
                        value: this.o.labels[0].o.find("span")
                    });
                    angular.extend(this.o.labels[1], {
                        value: this.o.labels[1].o.find("span")
                    });
                    if (!$this.settings.value.split(";")[1]) {
                        this.settings.single = true;
                    }
                    var domNodeDivs = this.domNode.find('div');
                    cutOffDom = angular.element(domNodeDivs[8]);
                    if (cutOffDom && cutOffDom.css) {
                        cutOffDom.css('width', '0%');
                    }
                    var pointers = [angular.element(domNodeDivs[1]), angular.element(domNodeDivs[2])];
                    angular.forEach(pointers, function (pointer, key) {
                        $this.settings = angular.copy($this.settings);
                        var value = $this.settings.value.split(';')[key];
                        if (value) {
                            $this.o.pointers[key] = new SliderPointer(pointer, key, $this);
                            var prev = $this.settings.value.split(';')[key - 1];
                            if (prev && parseInt(value, 10) < parseInt(prev, 10)) {
                                value = prev;
                            }
                            var value1 = value < $this.settings.from ? $this.settings.from : value;
                            value1 = value > $this.settings.to ? $this.settings.to : value;
                            $this.o.pointers[key].set(value1, true);
                            if (key === 0) {
                                $this.domNode.bind('mousedown', $this.clickHandler.apply($this));
                            }
                        }
                    });
                    this.o.value = angular.element(this.domNode.find("i")[2]);
                    this.is.init = true;
                    angular.forEach(this.o.pointers, function (pointer) {
                        $this.redraw(pointer);
                    });
                };
                Slider.prototype.clickHandler = function () {
                    var self = this;
                    return function (evt) {
                        if (self.disabled) {
                            return;
                        }
                        var className = evt.target.className;
                        var targetIdx = 0;
                        if (className.indexOf('jslider-pointer-to') > 0) {
                            targetIdx = 1;
                        }
                        var _off = utils.offset(self.domNode);
                        var offset = {
                            left: _off.left,
                            top: _off.top,
                            width: self.domNode[0].clientWidth,
                            height: self.domNode[0].clientHeight
                        };
                        targetIdx = 1;
                        var targetPtr = self.o.pointers[targetIdx];
                        targetPtr._parent = {offset: offset, width: offset.width, height: offset.height};
                        targetPtr._mousemove(evt);
                        targetPtr.onmouseup();
                        return false;
                    };
                };
                Slider.prototype.disable = function (bool) {
                    this.disabled = bool;
                };
                Slider.prototype.nice = function (value) {
                    return value;
                };
                Slider.prototype.onstatechange = function () {
                };
                Slider.prototype.limits = function (x, pointer) {
                    if (!this.settings.smooth) {
                        var step = this.settings.step * 100 / (this.settings.interval);
                        x = Math.round(x / step) * step;
                    }
                    var another = this.o.pointers[1 - pointer.uid];
                    if (another && pointer.uid && x < another.value.prc) {
                        x = another.value.prc;
                    }
                    if (another && !pointer.uid && x > another.value.prc) {
                        x = another.value.prc;
                    }
                    if (x < 0) {
                        x = 0;
                    }
                    if (x > 100) {
                        x = 100;
                    }
                    var val = Math.round(x * 10) / 10;
                    return val;
                };
                Slider.prototype.setPointersIndex = function (i) {
                    angular.forEach(this.getPointers(), function (pointer, i) {
                        pointer.index(i);
                    });
                };
                Slider.prototype.getPointers = function () {
                    return this.o.pointers;
                };
                Slider.prototype.onresize = function () {
                    var self = this;
                    this.sizes = {
                        domWidth: this.domNode[0].clientWidth,
                        domHeight: this.domNode[0].clientHeight,
                        domOffset: {
                            left: this.domNode[0].offsetLeft,
                            top: this.domNode[0].offsetTop,
                            width: this.domNode[0].clientWidth,
                            height: this.domNode[0].clientHeight
                        }
                    };
                    angular.forEach(this.o.pointers, function (ptr, key) {
                        self.redraw(ptr);
                    });
                };
                Slider.prototype.update = function () {
                    this.onresize();
                    this.drawScale();
                };
                Slider.prototype.drawScale = function () {
                };
                Slider.prototype.redraw = function (pointer) {
                    if (!this.settings.smooth) {
                        var newMousePrc = utils.roundUpToScale(pointer.value.prc,
                                this.settings.scale,
                                this.settings.cutOffWidth,
                                this.settings.cutOffIndex);
                        pointer.value.origin = newMousePrc;
                        pointer.value.prc = newMousePrc;
                    }

                    if (!this.is.init) {
                        return false;
                    }
                    this.setValue();
                    var width = this.o.pointers[1].value.prc;
                    var newPos = {left: '0%', width: width + '%'};
                    this.o.value.css(newPos);
                    var htmlValue = this.nice(pointer.value.origin);
                    var scaledDimension = this.settings.firstDimension;
                    if (this.settings.stepWithDifferentScale && !this.settings.smooth) {
                        htmlValue = utils.valueForDifferentScale(this.settings.from,
                                this.settings.to, htmlValue, this.settings.prcToValueMapper);
                    }
                    //This is the base value before the conversion
                    if (this.settings.realtimeCallback && angular.isFunction(this.settings.realtimeCallback)
                            && this.settings.cutOffVal !== undefined && pointer.uid === 1) {
                        this.settings.realtimeCallback(htmlValue);
                    }
                    //Need to change this to the correct value for the scale
                    if (this.settings.conversion) {
                        var conversionObj = utils.getConversionFactorValue(parseInt(htmlValue),
                                this.settings.conversion,
                                this.settings.firstDimension);
                        htmlValue = conversionObj.scaledVal;
                        scaledDimension = conversionObj.scaledDimension;
                    }

                    htmlValue = parseFloat(htmlValue);
                    var tooltipLabel = utils.convertToMbpsGbps(htmlValue, scaledDimension, this.settings.decimalPlaces);

                    this.o.labels[pointer.uid].value.html(tooltipLabel.unitValue + ' ' + tooltipLabel.unitLabel);
                    //Top tooltip label
                    this.redrawLabels(pointer);
                };
                Slider.prototype.redrawLabels = function (pointer) {
                    function setPosition(label, sizes, prc) {
                        sizes.margin = -sizes.label / 2;
                        var domSize = self.sizes.domWidth;
                        var label_left = sizes.border + sizes.margin;
                        if (label_left < 0) {
                            sizes.margin -= label_left;
                        }
                        if (sizes.border + sizes.label / 2 > domSize) {
                            sizes.margin = 0;
                            sizes.right = true;
                        } else
                            sizes.right = false;
                        //Adjust the tooltip location
                        sizes.margin = -((label.o[0].clientWidth / 2) - label.o[0].clientWidth / 20);
                        label.o.css({left: prc + "%", marginLeft: sizes.margin, right: "auto"});
                        if (sizes.right)
                            label.o.css({left: "auto", right: 0});
                        return sizes;
                    }
                    var self = this;
                    var label = this.o.labels[pointer.uid];
                    var prc = pointer.value.prc;
                    var sizes = {
                        label: label.o[0].offsetWidth,
                        right: false,
                        border: (prc * domSize) / 100
                    };
                    var another_label = null;
                    var another = null;
                    if (!this.settings.single) {
                        another = this.o.pointers[1 - pointer.uid];
                        another_label = this.o.labels[another.uid];
                        switch (pointer.uid) {
                            case 0:
                                if (sizes.border + sizes.label / 2 > another_label.o[0].offsetLeft - this.sizes.domOffset.left) {
                                    another_label.o.css({visibility: "hidden"});
                                    another_label.value.html(this.nice(another.value.origin));
                                    label.o.css({visibility: "hidden"});
                                    prc = (another.value.prc - prc) / 2 + prc;
                                    if (another.value.prc !== pointer.value.prc) {
                                        label.value.html(this.nice(pointer.value.origin) + "&nbsp;&ndash;&nbsp;" + this.nice(another.value.origin));
                                        sizes.label = label.o[0].clientWidth;
                                        sizes.border = (prc * domSize) / 100;
                                    }
                                } else {
                                    another_label.o.css({visibility: "visible"});
                                }
                                break;
                            case 1:
                                if (sizes.border - sizes.label / 2 < another_label.o[0].offsetLeft - this.sizes.domOffset.left + another_label.o[0].clientWidth) {
                                    another_label.o.css({visibility: "hidden"});
                                    another_label.value.html(this.nice(another.value.origin));
                                    label.o.css({visibility: "visible"});
                                    prc = (prc - another.value.prc) / 2 + another.value.prc;
                                    if (another.value.prc !== pointer.value.prc) {
                                        label.value.html(this.nice(another.value.origin) + "&nbsp;&ndash;&nbsp;" + this.nice(pointer.value.origin));
                                        sizes.label = label.o[0].clientWidth;
                                        sizes.border = (prc * domSize) / 100;
                                    }
                                } else {
                                    another_label.o.css({visibility: "visible"});
                                }
                                break;
                        }
                    }
                    sizes = setPosition(label, sizes, prc);
                    var domSize = self.sizes.domWidth;
                    //This is the 0th pointer
                    if (another_label) {
                        sizes = {
                            label: another_label.o[0].clientWidth,
                            right: false,
                            border: (another.value.prc * this.sizes.domWidth) / 100
                        };
                        sizes = setPosition(another_label, sizes, another.value.prc);
                    }
                };
                Slider.prototype.redrawLimits = function () {
                    if (this.settings.limits) {
                        var limits = [true, true];
                        for (var key in this.o.pointers) {
                            if (!this.settings.single || key === 0) {
                                var pointer = this.o.pointers[key];
                                var label = this.o.labels[pointer.uid];
                                var label_left = label.o[0].offsetLeft - this.sizes.domOffset.left;
                                var limit = this.o.limits[0];
                                if (label_left < limit[0].clientWidth)
                                    limits[0] = false;
                                limit = this.o.limits[1];
                                if (label_left + label.o[0].clientWidth > this.sizes.domWidth - limit[0].clientWidth)
                                    limits[1] = false;
                            }
                        }
                        for (var i = 0; i < limits.length; i++) {
                            if (limits[i]){
                                angular.element(this.o.limits[i]).addClass("animate-show");}
                            else{
                                angular.element(this.o.limits[i]).addClass("animate-hidde");}
                        }
                    }
                };
                Slider.prototype.setValue = function () {
                    var value = this.getValue();
                    this.inputNode.attr("value", value);
                    this.onstatechange.call(this, value, this.inputNode);
                };
                Slider.prototype.getValue = function () {
                    if (!this.is.init){
                        return false;}
                    var $this = this;
                    var value = "";
                    angular.forEach(this.o.pointers, function (pointer, key) {
                        if (pointer.value.prc !== undefined && !isNaN(pointer.value.prc)) {
                            var pointerPrc = pointer.value.prc;
                            var myValue = $this.prcToValue(pointerPrc);
                            if (!$this.settings.smooth) {
                                var myValue = utils.valueForDifferentScale($this.settings.from,
                                        $this.settings.to,
                                        pointerPrc,
                                        $this.settings.prcToValueMapper);
                            }
                            value += (key > 0 ? ";" : "") + myValue;
                        }
                    });
                    return value;
                };
                Slider.prototype.getPrcValue = function () {
                    if (!this.is.init)
                        return false;
                    var value = "";
                    $.each(this.o.pointers, function (i) {
                        if (this.value.prc !== undefined && !isNaN(this.value.prc))
                            value += (i > 0 ? ";" : "") + this.value.prc;
                    });
                    return value;
                };
                Slider.prototype.prcToValue = function (prc) {
                    var value;
                    if (this.settings.heterogeneity && this.settings.heterogeneity.length > 0) {
                        var h = this.settings.heterogeneity;
                        var _start = 0;
                        var _from = this.settings.from;
                        for (var i = 0; i <= h.length; i++) {
                            var v;
                            if (h[i]){
                                v = h[i].split("/");}
                            else{
                                v = [100, this.settings.to];}
                            if (prc >= _start && prc <= v[0]) {
                                value = _from + ((prc - _start) * (v[1] - _from)) / (v[0] - _start);
                            }
                            _start = v[0];
                            _from = v[1];
                        }
                    }
                    else {
                        value = this.settings.from + (prc * this.settings.interval) / 100;
                    }
                    var roundedValue = this.round(value);
                    return roundedValue;
                };
                Slider.prototype.valueToPrc = function (value, pointer) {
                    var prc;
                    if (this.settings.heterogeneity && this.settings.heterogeneity.length > 0) {
                        var h = this.settings.heterogeneity;
                        var _start = 0;
                        var _from = this.settings.from;
                        for (var i = 0; i <= h.length; i++) {
                            var v;
                            if (h[i])
                                v = h[i].split("/");
                            else
                                v = [100, this.settings.to];
                            if (value >= _from && value <= v[1]) {
                                prc = pointer.limits(_start + (value - _from) * (v[0] - _start) / (v[1] - _from));
                            }
                            _start = v[0];
                            _from = v[1];
                        }
                    } else {
                        prc = pointer.limits((value - this.settings.from) * 100 / this.settings.interval);
                    }
                    return prc;
                };
                Slider.prototype.round = function (value) {
                    value = Math.round(value / this.settings.step) * this.settings.step;
                    if (this.settings.round){
                        value = Math.round(value * Math.pow(10, this.settings.round)) / Math.pow(10, this.settings.round);}
                    else{
                        value = Math.round(value);}
                    return value;
                };
                return Slider;
            }])
        .directive('attStepSlider', [
            '$compile', '$templateCache', '$timeout', '$window', 'slider', 'sliderConstants', 'utils',
            function (compile, templateCache, timeout, win, Slider, sliderConstants, utils) {
                /*
                 The MIT License (MIT)
                 Copyright (c) 2013 Julien Valéry
                 */
                var templateUrl = 'app/scripts/ng_js_att_tpls/stepSlider/attStepSlider.html';
                return {
                    restrict: 'AE',
                    require: '?ngModel',
                    scope: {
                        options: '=',
                        cutOff: '='
                    },
                    priority: 1,
                    templateUrl: templateUrl,
                    link: function (scope, element, attrs, ngModel) {
                        if (!ngModel)
                            return;
                        scope.mainSliderClass = 'step-slider';
                        element.after(compile(templateCache.get(templateUrl))(scope, function (clonedElement, scope) {
                            scope.tmplElt = clonedElement;
                        }));
                        ngModel.$render = function () {
                            if (ngModel.$viewValue.split && ngModel.$viewValue.split(";").length === 1) {
                                ngModel.$viewValue = '0;' + ngModel.$viewValue;
                            } else if (typeof (ngModel.$viewValue) === 'number') {
                                ngModel.$viewValue = '0;' + ngModel.$viewValue;
                            }
                            if (!ngModel.$viewValue && ngModel.$viewValue !== 0) {
                                return;
                            }
                            if (typeof (ngModel.$viewValue) === 'number') {
                                ngModel.$viewValue = '' + ngModel.$viewValue;
                            }
                            if (scope.slider) {
                                var firstPointer = '0';
                                scope.slider.getPointers()[0].set(firstPointer, true);
                                if (ngModel.$viewValue.split(";")[1]) {
                                    var value = ngModel.$viewValue.split(";")[1];
                                    if (value.length >= 4) {
                                        value = value.substring(0, 2);
                                    }
                                    if (!scope.options.realtime)
                                        scope.options.callback(parseFloat(ngModel.$viewValue.split(";")[1]));
                                    scope.slider.getPointers()[1].set(ngModel.$viewValue.split(";")[1], true);
                                }
                            }
                        };
                        var init = function () {
                            scope.from = '' + scope.options.from;
                            scope.to = '' + scope.options.to;
                            if (scope.options.calculate && typeof scope.options.calculate === 'function') {
                                scope.from = scope.options.calculate(scope.from);
                                scope.to = scope.options.calculate(scope.to);
                            }
                            scope.showDividers = scope.options.showDividers;
                            scope.COLORS = sliderConstants.COLORS;
                            scope.sliderColor = scope.options.sliderColor;
                            if (!scope.sliderColor)
                                scope.sliderColor = sliderConstants.COLORS.REGULAR;
                            var scaleArray = scope.options.scale;
                            /* Make a copy of the scaleArray before converting it to percentage for the bars */
                            var nonPercentScaleArray = [];
                            /* Define variable for displaying lower range values */
                            var scaledUpValueArray=[];
                            /* Create Mapper for the percentage to value */
                            var prcToValueMapper = {};
                            for (var i in scaleArray) {
                                var s = scaleArray[i];
                                nonPercentScaleArray.push(s);
                            }
                            function addScaleArrayStartAndEnd() {
                                if (scaleArray[0] !== 0) {
                                    scaleArray.splice(0, 0, 0);
                                }
                                if (scaleArray[scaleArray.length - 1] !== 100) {
                                    scaleArray.splice(scaleArray.length, 0, 100);
                                }
                            }
                            function convertScaleArrayToPercentage() {
                                if (scaleArray[scaleArray.length - 1] !== scope.options.to) {
                                    scaleArray.splice(scaleArray.length, 0, scope.options.to);
                                }

                                if(scope.options.displayScaledvalues){
                                    for(var i in scaleArray){
                                        scaledUpValueArray.push(Math.log2(scaleArray[i]));
                                    }
                                    var maxScaledUpValue=scaledUpValueArray[scaledUpValueArray.length-1];
                                }

                                for (var i in scaleArray) {
                                    var prcValue;
                                    var fromValueCheck = (scaleArray[i] / scope.options.from);
                                    var toValueCheck = (scaleArray[i] / scope.options.to);

                                    if (scope.options.displayScaledvalues){
                                        prcValue = (scaledUpValueArray[i] /maxScaledUpValue)*100;
                                    } else {
                                        prcValue = ((scaleArray[i] - scope.options.from) / (scope.options.to - scope.options.from)) * 100;
                                    }

                                    var realValue = scaleArray[i];

                                    if (toValueCheck === 1) {
                                        prcValue = 100;
                                    }
                                    else if (fromValueCheck === 1) {
                                        prcValue = 0;
                                    }
                                    scaleArray[i] = prcValue;
                                    prcToValueMapper['' + prcValue] = realValue;
                                }
                            }
                            if ((scope.options.from !== 0 || scope.options.to !== 100)
                                    && scope.options.smooth) {
                                /*
                                 scale array is in real values.
                                 */
                                addScaleArrayStartAndEnd();
                                scope.options.stepWithDifferentScale = true;
                            }
                            else if ((scope.options.from !== 0 || scope.options.to !== 100)
                                    && !scope.options.smooth) {
                                /*
                                 Case for different from and to values other than 0 and 100
                                 so we have to do some different calculations
                                 */
                                scope.options.stepWithDifferentScale = true;
                                convertScaleArrayToPercentage();
                                addScaleArrayStartAndEnd();
                            }
                            else {
                                /*
                                 This is the normal case where the from and to values are 0 and
                                 100 respectively.        
                                 */
                                //Check that the scale starts at 0 and 100
                                convertScaleArrayToPercentage();
                                addScaleArrayStartAndEnd();
                            }
                            var decimalPlaces = 0;
                            if (scope.options.decimalPlaces) {
                                decimalPlaces = scope.options.decimalPlaces;
                            }
                            //Modify the endDimension based on whether converison was passed in
                            //Also change the toStr value to scale to the last factor
                            scope.endDimension = scope.options.dimension;
                            if (scope.options.conversion) {
                                //Get the dimension of the last conversion
                                var lastIndex = scope.options.conversion.length - 1;
                                var lastDimension = scope.options.conversion[lastIndex].dimension;
                                var lastScaleFactor = scope.options.conversion[lastIndex].scaleFactor;
                                scope.endDimension = ' ' + lastDimension;

                                var toVal = (scope.to / lastScaleFactor).toFixed(decimalPlaces);
                                scope.toStr = toVal;
                            } else {
                                scope.toStr = scope.options.to;
                            }

                            var tooltipLabel = utils.convertToMbpsGbps(scope.toStr, scope.endDimension, scope.options.decimalPlaces);
                            scope.toStr = tooltipLabel.unitValue;
                            scope.endDimension = ' ' + tooltipLabel.unitLabel;

                            var OPTIONS = {
                                from: scope.options.from,
                                to: scope.options.to,
                                step: scope.options.step,
                                smooth: scope.options.smooth,
                                limits: true,
                                stepWithDifferentScale: scope.options.stepWithDifferentScale,
                                round: scope.options.round || false,
                                value: ngModel.$viewValue,
                                scale: scope.options.scale,
                                nonPercentScaleArray: nonPercentScaleArray,
                                prcToValueMapper: prcToValueMapper,
                                firstDimension: scope.options.dimension,
                                decimalPlaces: decimalPlaces,
                                conversion: scope.options.conversion,
                                realtimeCallback: scope.options.callback
                            };
                            if (angular.isFunction(scope.options.realtime)) {
                                OPTIONS.realtimeCallback = function (value) {
                                    ngModel.$setViewValue(value);
                                    scope.options.callback(value);
                                };
                            }
                            else {
                                OPTIONS.callback = forceApply;
                            }
                            OPTIONS.calculate = scope.options.calculate || undefined;
                            OPTIONS.onstatechange = scope.options.onstatechange || undefined;
                            timeout(function () {
                                var scaleDiv = scope.tmplElt.find('div')[7];
                                if (!OPTIONS.conversion) {
                                    scope.tmplElt.find('div').eq(6).find('span').eq(0).css('padding-left', '10px');
                                    scope.tmplElt.find('div').eq(6).find('span').eq(0).css('padding-right', '15px');
                                }
                                scope.slider = angular.element.slider(element, scope.tmplElt, OPTIONS);
                                angular.element(scaleDiv).html(scope.generateScale());
                                scope.drawScale(scaleDiv);
                                initListener();
                                scope.$watch('options.disable', function (val) {
                                    if (scope.slider) {
                                        scope.tmplElt.toggleClass('disabled', val);
                                        scope.slider.disable(val);
                                    }
                                });
                                scope.$watch('cutOff', function (cutOffVal) {
                                    if (cutOffVal && cutOffVal > 0) {
                                        var cutOffPrc = (cutOffVal - scope.slider.settings.from) / (scope.slider.settings.to -
                                                scope.slider.settings.from);
                                        cutOffPrc = cutOffPrc * 100;
                                        scope.isCutOffSlider = true;
                                        scope.slider.settings.cutOffWidth = cutOffPrc;
                                        //cutOffVal is the actual value of the cutoff point
                                        scope.cutOffVal = cutOffVal;
                                        if (scope.options.conversion) {
                                            var convertedVal = utils.getConversionFactorValue(cutOffVal, scope.options.conversion, scope.options.dimension);
                                            convertedVal.scaledVal = parseFloat(convertedVal.scaledVal).toFixed(scope.options.decimalPlaces);
                                            scope.cutOffVal = convertedVal.scaledVal + ' ' + convertedVal.scaledDimension;
                                        }
                                        scope.slider.settings.cutOffVal = cutOffVal;
                                        //Calculate the cutOff percentage
                                        scope.slider.changeCutOffWidth(cutOffPrc + '%');
                                        var scale = scope.slider.settings.nonPercentScaleArray;
                                        //Calculate where the cutOff point in relation to the scale array
                                        for (var i in scale) {
                                            if (i >= 1) {
                                                var lowerVal = scale[i - 1];
                                                var higherVal = scale[i];
                                                if (cutOffVal > lowerVal && cutOffVal <= higherVal) {
                                                    scope.slider.settings.cutOffIndex = i;
                                                }
                                            }
                                        }
                                    } else {
                                        scope.slider.settings.cutOffVal = 0;
                                    }
                                });
                            });
                        };
                        function initListener() {
                            angular.element(win).bind('resize', function (event) {
                                scope.slider.onresize();
                            });
                        }
                        scope.generateScale = function () {
                            if (scope.options.scale && scope.options.scale.length > 0) {
                                var str = "";
                                var s = scope.options.scale;
                                var position = 'left';
                                for (var i = 0; i < s.length; i++) {
                                    if (i !== 0 && i !== s.length - 1) {
                                        var scaledPosition = ((s[i] - scope.from) / (scope.to - scope.from)) * 100;
                                        if (scope.options.stepWithDifferentScale && !scope.options.smooth) {
                                            scaledPosition = s[i];
                                        }
                                        str += '<span style="' + position + ': ' + scaledPosition + '%"></span>';
                                    }
                                }
                                return str;
                            } else
                                return "";
                            return "";
                        };
                        scope.drawScale = function (scaleDiv) {
                            angular.forEach(angular.element(scaleDiv).find('ins'), function (scaleLabel, key) {
                                scaleLabel.style.marginLeft = -scaleLabel.clientWidth / 2;
                            });
                        };
                        var forceApply = function (value) {
                            var val = value.split(";")[1];
                            scope.$apply(function () {
                                ngModel.$setViewValue(parseInt(val));
                            });
                            if (scope.options.callback) {
                                scope.options.callback(parseInt(val));
                            }
                        };
                        scope.$watch('options', function (value) {
                            init();
                        });
                        angular.element.slider = function (inputElement, element, settings) {
                            if (!element.data('jslider'))
                                element.data('jslider', new Slider(inputElement, element, settings));
                            var sliderObj = element.data('jslider');
                            return sliderObj;
                        };
                    }
                };
            }]);
angular.module('att.abs.steptracker', ['att.abs.transition'])
        .directive('steptracker', ['$timeout', function ($timeout) {
                return {
                    // This allows dev's clickHandler to cancel an operation
                    priority: 100,
                    scope: {
                        sdata: "=sdata",
                        cstep: "=currentStep",
                        clickHandler: '=?',
                        disableClick: '=?'
                    },
                    restrict: 'EA',
                    replace: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/steptracker/step-tracker.html',
                    link: function (scope, elem) {
                        if (scope.disableClick === undefined) {
                            scope.disableClick = false;
                        }
                        $timeout(function () {
                            if (scope.cstep < 1) {
                                scope.cstep = 1;
                            }
                            else if (scope.cstep > scope.sdata.length) {
                                scope.cstep = scope.sdata.length;
                            }
                            var divs = elem.find('div');
                            var slidertracks = [];
                            for (var i in divs) {
                                if (divs.eq(i)[0]) {
                                    var el = divs.eq(i)[0].className;
                                    if (el.indexOf('track ng-scope') > -1) {
                                        slidertracks.push(divs.eq(i));
                                    }
                                }
                            }
                            var currentPage,totalPage,currentTrack = updateCurrentTrack(scope.cstep);
                            function updateCurrentTrack(step) {
                                // Always return the step-1 because array starts at 0
                                return angular.element(slidertracks[step - 1]);
                            }
                            function updateTrackWidth() {
                                if (scope.cstep > 0 && scope.cstep <= scope.sdata.length - 1 && currentPage > 0) {
                                    var newWidth = ((currentPage / totalPage) * 100) + "%";
                                    currentTrack = updateCurrentTrack(scope.cstep);
                                    currentTrack.css('width', newWidth);
                                }
                            }
                            function updatePages() {
                                if (scope.cstep <= scope.sdata.length) {
                                    currentPage = scope.sdata[scope.cstep - 1]['currentPage'];
                                    totalPage = scope.sdata[scope.cstep - 1]['totalPages'];
                                }
                            }
                            // dynamically add width for steps, depending on the number of steps.
                            scope.set_width = function (indexval) {
                                var setwidth = (100 / (scope.sdata.length - 1)) + "%";
                                // skip last element and add width for all other element
                                if ((scope.sdata.length - 1) > indexval) {
                                    return {'width': setwidth};
                                }
                            };
                            scope.$watch('sdata', function () {
                                updatePages();
                                var prevStep = scope.cstep;
                                // Before anything, ensure currentPage is never below 1
                                if (currentPage < 1) {
                                    currentPage = 1;
                                    if (scope.cstep !== 1) {
                                        // Decrease step, current track width is 0%, new step width updates
                                        scope.cstep--;
                                        updatePages();
                                    }
                                }
                                // Move to next step, reset currentPage, totalPage, and ensure previous steps are completed
                                if (currentPage > totalPage) {
                                    if (scope.cstep > scope.sdata.length - 1) {
                                        scope.cstep++;
                                        return;
                                    } else {
                                        currentPage = totalPage;
                                        updateTrackWidth();
                                        scope.cstep++;
                                        updatePages();
                                        updateTrackWidth();
                                    }
                                }
                                if (currentPage < 1 && prevStep === scope.cstep) {
                                    currentPage = 1;
                                    if (scope.cstep > 1) {
                                        scope.cstep--;
                                        scope.sdata[scope.cstep - 1]['currentPage'] = scope.sdata[scope.cstep - 1]['totalPages'];
                                        scope.sdata[scope.cstep]['currentPage'] = 1;
                                    }
                                }
                                updateTrackWidth();
                            }, true);
                            //add the active class for current step
                            scope.activestep = function (index) {
                                return (index === scope.cstep - 1);
                            };
                            //add the done class for finished step
                            scope.donesteps = function (index) {
                                return (index < scope.cstep - 1);
                            };
                            //add the last class for final step
                            scope.laststep = function (index) {
                                return (index === scope.sdata.length - 1);
                            };
                            scope.isIncomplete = function (index) {
                                if (index === scope.cstep - 1) {
                                    return false;
                                }
                                if (index >= 0 && index < scope.sdata.length - 1) {
                                    var step = scope.sdata[index];
                                    return (step['currentPage'] <= step['totalPages']);
                                }
                            };
                            //click event
                            scope.stepclick = function ($event, steps) {
                                // If we are decreasing steps, reset all currentPage counts to 1
                                if (steps < scope.cstep) {
                                    for (var i = scope.cstep - 1; i > steps; i--) {
                                        scope.sdata[i]['currentPage'] = 1;
                                    }
                                    scope.sdata[steps]['currentPage']--;
                                }
                                if (angular.isFunction(scope.clickHandler)) {
                                    scope.clickHandler($event, steps);
                                }
                                scope.cstep = steps + 1;
                                // In the case we decremented previously from this step, we need to reset currentpage to default
                                if (scope.cstep <= scope.sdata.length && scope.sdata[scope.cstep]['currentPage'] < 1) {
                                    scope.sdata[scope.cstep]['currentPage'] = 1;
                                }
                                updatePages();
                                updateTrackWidth();
                            };
                        }, 100);
                    }
                };
            }
        ])
        .constant('timelineConstants', {
            STEP_TYPE: {
                ALERT: 'alert',
                COMPLETED: 'completed',
                CANCELLED: 'cancelled'
            }
        })
        .controller('AttTimelineCtrl', ['$scope', '$timeout', function ($scope, $timeout) {
                var timelineBarCtrls = [];
                var timelineDotCtrls = [];
                this.numSteps = 0;
                this.isAlternate = function () {
                    return $scope.alternate;
                };
                this.addTimelineBarCtrls = function (t) {
                    timelineBarCtrls.push(t);
                };
                this.addTimelineDotCtrls = function (b) {
                    timelineDotCtrls.push(b);
                };
                $timeout(init, 200);
                function init() {
                    function compare(a, b) {
                        if (a.order < b.order) {
                            return -1;
                        }
                        if (a.order > b.order) {
                            return 1;
                        }
                        return 0;
                    }
                    timelineDotCtrls.sort(compare);
                    timelineBarCtrls.sort(compare);
                    if ($scope.$parent.animate) {
                        animateSequence();
                    }
                    $scope.$watch('trigger', function (val) {
                        if (val) {
                            $scope.resetTimeline();
                        } else {
                            $scope.$parent.animate = false;
                        }
                    });
                }
                function animateSequence() {
                    var dotsDuration = .25;
                    var timelineBarProgressDuration = .25;
                    if (typeof $scope.barAnimateDuration === 'number') {
                        timelineBarProgressDuration = $scope.barAnimateDuration;
                    }
                    var start = createAnimation(0, timelineBarProgressDuration);
                    function setToInactiveStates() {
                        for (var i in timelineDotCtrls) {
                            var dotCtrl = timelineDotCtrls[i];
                            if (i % 2 === 0) {
                                dotCtrl.unhoveredStateForBelow(.25);
                            } else {
                                dotCtrl.unhoveredStateForAbove(.25);
                            }
                            if (dotCtrl.isStop()) {
                                break;
                            }
                        }
                    }
                    function createAnimation(i, duration) {
                        if (i === 0) {
                            return function () {
                                if (timelineDotCtrls[i + 1].isStop() && timelineDotCtrls[i + 1].isCancelled()) {
                                    timelineBarCtrls[i].isCancelled(true);
                                }
                                timelineBarCtrls[i].animate(createAnimation(i + 1, duration), duration);
                            };
                        } else if (i === timelineBarCtrls.length - 1) {
                            return function () {
                                //Removes the bolded text from the start
                                if (timelineDotCtrls[0].isCurrentStep()) {
                                    timelineDotCtrls[0].isCurrentStep(false);
                                }
                                if (timelineDotCtrls[i].isStop()) {
                                    timelineDotCtrls[i - 1].shrinkAnimate(dotsDuration);
                                    timelineDotCtrls[i].isCurrentStep(true);
                                } else {
                                    timelineDotCtrls[i - 1].shrinkAnimate(dotsDuration);
                                    timelineBarCtrls[i].animate(createAnimation(i + 1, duration), duration);
                                }
                                timelineDotCtrls[i].expandedAnimate(dotsDuration);
                                $timeout(function () {
                                    setToInactiveStates();
                                }, 500);
                            };
                        }
                        //End Dot
                        else if (i === timelineBarCtrls.length) {
                            return function () {
                                //Removes the bolded text from the start
                                if (timelineDotCtrls[0].isCurrentStep()) {
                                    timelineDotCtrls[0].isCurrentStep(false);
                                }
                                timelineDotCtrls[i - 1].shrinkAnimate(dotsDuration);
                                timelineDotCtrls[i].expandedAnimate(dotsDuration);
                                timelineDotCtrls[i].isCurrentStep(true);
                                $timeout(function () {
                                    setToInactiveStates();
                                }, 500);
                            };
                        }
                        else {
                            return function () {
                                //Removes the bolded text from the start
                                if (timelineDotCtrls[0].isCurrentStep()) {
                                    timelineDotCtrls[0].isCurrentStep(false);
                                }
                                if (timelineDotCtrls[i].isStop()) {
                                    timelineDotCtrls[i - 1].shrinkAnimate(dotsDuration);
                                    timelineDotCtrls[i].expandedAnimate(dotsDuration);
                                    timelineDotCtrls[i].isCurrentStep(true);
                                    $timeout(function () {
                                        setToInactiveStates();
                                    }, 500);
                                } else {
                                    if (timelineDotCtrls[i + 1].isStop() && timelineDotCtrls[i + 1].isCancelled()) {
                                        timelineBarCtrls[i].isCancelled(true);
                                    }
                                    timelineDotCtrls[i - 1].shrinkAnimate(dotsDuration);
                                    timelineBarCtrls[i].animate(createAnimation(i + 1, duration), duration);
                                    timelineDotCtrls[i].expandedAnimate(dotsDuration);
                                }
                            };
                        }
                    }
                    start();
                }
            }])
        .directive('attTimeline', ['$timeout', '$compile', function ($timeout, $compile) {
                return {
                    restrict: 'EA',
                    replace: true,
                    scope: {
                        steps: '=',
                        trigger: '=',
                        alternate: '=',
                        barAnimateDuration: '='
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/steptracker/timeline.html',
                    controller: 'AttTimelineCtrl',
                    link: function (scope, element, attrs, ctrl) {
                        var init = function () {
                            var steps = scope.steps;
                            var middleSteps = [];
                            for (var i = 1; i < steps.length; i++) {
                                var aStep = steps[i];
                                middleSteps.push(aStep);
                            }
                            scope.middleSteps = middleSteps;
                            //Used in calculating the width of the loading bars
                            ctrl.numSteps = steps.length - 1;
                        };
                        init();
                        //Recompile in case of scope changes
                        scope.resetTimeline = function () {
                            scope.animate = true;
                            $compile(element)(scope);
                        };
                    }
                };
            }])
        .controller('TimelineBarCtrl', ['$scope', function ($scope) {
                this.type = 'timelinebar';
                this.order = parseInt($scope.order);
                this.animate = function (callback, duration) {
                    $scope.loadingAnimation(callback, duration);
                };
                this.isCancelled = function (isCancelled) {
                    $scope.isCancelled = isCancelled;
                };
            }])
        .directive('timelineBar', ['animation', '$progressBar', function (animation, $progressBar) {
                return {
                    restrict: 'EA',
                    replace: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/steptracker/timelineBar.html',
                    scope: {
                        order: '@'
                    },
                    require: ['^attTimeline', 'timelineBar'],
                    controller: 'TimelineBarCtrl',
                    link: function (scope, element, attrs, ctrls) {
                        var attTimelineCtrl = ctrls[0];
                        var timelineBarCtrl = ctrls[1];
                        attTimelineCtrl.addTimelineBarCtrls(timelineBarCtrl);
                        scope.isCompleted = true;
                        var widthPerc = (100 / attTimelineCtrl.numSteps) - 3;
                        element.css('width', widthPerc + '%');
                        var elem = element.find('div').eq(0);
                        animation.set(elem, {opacity: 0.0});
                        var updateCallback = function (selfElement) {
                            animation.set(elem, {opacity: 1.0});
                            animation.set(elem, {
                                scaleX: selfElement.progress(),
                                transformOrigin: "left"
                            });
                        };
                        scope.loadingAnimation = $progressBar(updateCallback);
                    }
                };
            }])
        .controller('TimelineDotCtrl', ['$scope', '$timeout', 'timelineConstants', function ($scope, $timeout, timelineConstants) {
                this.type = 'dot';
                this.order = parseInt($scope.order);
                var self = this;
                $timeout(function () {
                    if (self.order !== 0) {
                        if (self.order % 2 !== 0) {
                            $scope.initializeAboveForAnimation();
                        }
                        else {
                            $scope.initializeBelowForAnimation();
                        }
                    }
                });
                this.expandedAnimate = function (duration) {
                    $scope.setColor();
                    $scope.expandedAnimate(duration);
                    if (self.order !== 0 && !$scope.isStepsLessThanFive()) {
                        if (self.order % 2 !== 0) {
                            $scope.expandContentForAbove(duration);
                        } else {
                            $scope.expandContentForBelow(duration);
                        }
                    }
                };
                this.unhoveredStateForAbove = function (duration) {
                    $scope.unhoveredStateForAbove(duration);
                };
                this.unhoveredStateForBelow = function (duration) {
                    $scope.unhoveredStateForBelow(duration);
                };
                this.shrinkAnimate = function (duration) {
                    $scope.shrinkAnimate(duration);
                };
                this.setExpanded = function () {
                    $scope.setSize(3);
                };
                this.isStop = function () {
                    return $scope.isStop;
                };
                this.isCancelled = function () {
                    return ($scope.type === timelineConstants.STEP_TYPE.CANCELLED);
                };
                this.isAlert = function () {
                    return ($scope.type === timelineConstants.STEP_TYPE.ALERT);
                };
                //Sets the bolded text
                this.isCurrentStep = function (isCurrentStep) {
                    if (isCurrentStep !== undefined) {
                        $scope.isCurrentStep = isCurrentStep;
                    }
                    return $scope.isCurrentStep;
                };
            }])
        .directive('timelineDot', ['animation', 'timelineConstants',
            function (animation, timelineConstants) {
                return {
                    restrict: 'EA',
                    replace: true,
                    scope: {
                        order: '@',
                        title: '@',
                        description: '@',
                        by: '@',
                        date: '@',
                        type: '@'
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/steptracker/timelineDot.html',
                    require: ['^attTimeline', 'timelineDot'],
                    controller: 'TimelineDotCtrl',
                    link: function (scope, element, attrs, ctrls) {
                        var attTimelineCtrl = ctrls[0];
                        var timelineDotCtrl = ctrls[1];
                        attTimelineCtrl.addTimelineDotCtrls(timelineDotCtrl);
                        scope.numSteps = attTimelineCtrl.numSteps + 1;
                        scope.isCurrentStep = false;
                        scope.isCompleted = false;
                        scope.isStop = false;
                        if (scope.type === timelineConstants.STEP_TYPE.ALERT || scope.type === timelineConstants.STEP_TYPE.CANCELLED) {
                            scope.isStop = true;
                        }
                        scope.isInactive = true;
                        var divs = element.find('div');
                        var biggerCircleElem = divs.eq(0);
                        var expandableCircleElem = divs.eq(2);
                        var infoboxElem = divs.eq(3);
                        var titleElem = divs.eq(5);
                        var contentElem = divs.eq(6);
                        var dateElem = divs.eq(9);
                        function isEmptyStep() {
                            if (!scope.description && !scope.by && !scope.date) {
                                return true;
                            }
                            return false;
                        }
                        scope.isStepsLessThanFive = function () {
                            if (scope.numSteps < 5) {
                                return true;
                            }
                            return false;
                        };
                        scope.titleMouseover = function (num) {
                            if (!scope.isStepsLessThanFive() && !isEmptyStep()) {
                                if (num === 1 && scope.order % 2 === 0) {
                                    scope.expandContentForBelow(.25);
                                }
                                if (num === 2 && scope.order % 2 !== 0) {
                                    scope.expandContentForAbove(.25);
                                }
                            }
                        };
                        scope.titleMouseleave = function () {
                            if (scope.order % 2 === 0) {
                                scope.unhoveredStateForBelow(.25);
                            }
                            else {
                                scope.unhoveredStateForAbove(.25);
                            }
                        };
                        scope.initializeAboveForAnimation = function () {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.set(contentElem, {opacity: 0});
                                animation.set(dateElem, {opacity: 0});
                                if (!isEmptyStep()) {
                                    var yOffset = contentElem[0].offsetHeight + dateElem[0].offsetHeight;
                                    animation.set(titleElem, {'top': yOffset});
                                }
                            }
                        };
                        scope.expandContentForAbove = function (duration) {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.to(titleElem, duration, {'top': 0});
                                animation.to(contentElem, duration, {opacity: 1});
                                animation.to(dateElem, duration, {opacity: 1});
                            }
                        };
                        scope.unhoveredStateForAbove = function (duration) {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.set(contentElem, {opacity: 0});
                                animation.set(dateElem, {opacity: 1});
                                var yOffset = contentElem[0].offsetHeight;
                                animation.to(titleElem, duration, {'top': yOffset});
                            }
                        };
                        scope.initializeBelowForAnimation = function () {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.set(contentElem, {height: '0%', opacity: 0, top: '-20px'});
                                animation.set(dateElem, {opacity: 0});
                            }
                        };
                        scope.expandContentForBelow = function (duration) {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.set(dateElem, {opacity: 1});
                                animation.to(contentElem, duration, {height: 'auto', opacity: 1, top: '0px'});
                            }
                        };
                        scope.unhoveredStateForBelow = function (duration) {
                            if (!scope.isStepsLessThanFive() && attTimelineCtrl.isAlternate()) {
                                animation.to(contentElem, duration, {height: '0%', opacity: 0, top: '-20px', position: 'relative'});
                                animation.set(dateElem, {opacity: 1});
                            }
                        };
                        /*Default Initializaztion*/
                        //If the info box is above and the description and date and by are empty then we have do reset its position
                        if (isEmptyStep() && (scope.order % 2 !== 0 && attTimelineCtrl.isAlternate())) {
                            infoboxElem.css('top', '-47px');
                        }
                        //Check if the order is odd and set the appropiate above or below and other effects
                        if (scope.order % 2 === 0 || !attTimelineCtrl.isAlternate()) {
                            scope.isBelowInfoBoxShown = true;
                        }
                        else {
                            scope.isBelowInfoBoxShown = false;
                        }
                        //modify some css for steps less than 5 and not alternating
                        if (scope.isStepsLessThanFive() && !attTimelineCtrl.isAlternate()) {
                            animation.set(dateElem, {marginTop: 10});
                        }
                        //For IE 8 fix
                        animation.set(biggerCircleElem, {opacity: '.5'});
                        //shrink the expandableCircle to we can expand it later
                        animation.set(expandableCircleElem, {opacity: '0.0'});
                        animation.set(expandableCircleElem, {scale: .10});
                        if (scope.order === 0) {
                            animation.set(expandableCircleElem, {opacity: '1.0'});
                            animation.set(expandableCircleElem, {scale: 1});
                            animation.set(biggerCircleElem, {scale: 3});
                            scope.isCurrentStep = true;
                            scope.isInactive = false;
                            scope.isCompleted = true;
                        }
                        scope.setColor = function () {
                            scope.isInactive = false;
                            if (scope.type === timelineConstants.STEP_TYPE.CANCELLED) {
                                scope.isCancelled = true;
                            }
                            else if (scope.type === timelineConstants.STEP_TYPE.ALERT) {
                                scope.isAlert = true;
                            }
                            else {
                                scope.isCompleted = true;
                            }
                            if (!scope.$phase) {
                                scope.$apply();
                            }
                        };
                        scope.setSize = function (size) {
                            animation.set(biggerCircle, {scale: size});
                        };
                        scope.setExpandedCircle = function () {
                            animation.set(expandableCircleElem, {opacity: '1.0'});
                            animation.set(expandableCircleElem, {scale: 1});
                        };
                        scope.expandedAnimate = function (duration) {
                            animation.to(biggerCircleElem, duration, {scale: 3});
                            animation.set(expandableCircleElem, {opacity: '1.0'});
                            animation.to(expandableCircleElem, duration, {scale: 1});
                        };
                        scope.shrinkAnimate = function (duration) {
                            animation.to(biggerCircleElem, duration, {scale: 1});
                        };
                    }
                };
            }]);
angular.module('att.abs.table', ['att.abs.utilities'])
.constant('tableConfig', {
    //true for descending & false for ascending
    defaultSortPattern: false,
    highlightSearchStringClass: 'tablesorter-search-highlight'
})

.directive('attTable', ['$filter', function($filter) {
    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        scope: {
            tableData: "=",
            viewPerPage: "=",
            currentPage: "=",
            totalPage: "=",
            searchCategory: "=",
            searchString: "="
        },
        require: 'attTable',
        templateUrl: 'app/scripts/ng_js_att_tpls/table/attTable.html',
        controller: ['$scope', function($scope) {
            this.headers = [];
            this.currentSortIndex = null;
            this.setIndex = function(headerScope) {
                this.headers.push(headerScope);
            };
            this.getIndex = function(headerName) {
                for (var i = 0; i < this.headers.length; i++) {
                    if (this.headers[i].headerName === headerName) {
                        return this.headers[i].index;
                    }
                }
                return null;
            };
            this.sortData = function(columnIndex, reverse) {
                $scope.$parent.columnIndex = columnIndex;
                $scope.$parent.reverse = reverse;
                this.currentSortIndex = columnIndex;
                $scope.currentPage = 1;
                this.resetSortPattern();
            };
            this.getSearchString = function() {
                return $scope.searchString;
            };
            this.resetSortPattern = function() {
                for(var i = 0; i < this.headers.length; i++) {
                    var currentScope = this.headers[i];
                    if(currentScope.index !== this.currentSortIndex) {
                        currentScope.resetSortPattern();
                    }
                }
            };
        }],
        link: function(scope, elem, attr, ctrl) {
            scope.searchCriteria = {};
            scope.$watchCollection('tableData', function(value) {
                if(value && !isNaN(value.length)) {
                    scope.totalRows = value.length;
                }
            });
            scope.$watch('currentPage', function(val) {
                scope.$parent.currentPage = val;
            });
            scope.$watch('viewPerPage', function(val) {
                scope.$parent.viewPerPage = val;
            });
            scope.$watch(function() {
                return scope.totalRows / scope.viewPerPage;
            }, function(value) {
                if(!isNaN(value)) {
                    scope.totalPage = Math.ceil(value);
                    scope.currentPage = 1;
                }
            });
            var searchValCheck = function(val){
                if(angular.isDefined(val) && val !== null && val !== ""){
                    return true;
                }
            };
            var setSearchCriteria = function(v1,v2){
                if(searchValCheck(v1) && searchValCheck(v2)){
                    var index = ctrl.getIndex(v2);
                    scope.searchCriteria = {};
                    if (index !== null) {
                        scope.searchCriteria[index] = v1;
                    }
                }else if(searchValCheck(v1) && (!angular.isDefined(v2) || v2 === null || v2 === "")){
                    scope.searchCriteria = {
                        $: v1
                    };
                }else {
                    scope.searchCriteria = {};
                }
            };
            scope.$watch('searchCategory', function(newVal,oldVal) {
                if(newVal !== oldVal){
                    setSearchCriteria(scope.searchString,newVal);
                }
            });
            scope.$watch('searchString', function (newVal,oldVal) {
                if(newVal !== oldVal){
                    setSearchCriteria(newVal,scope.searchCategory);
                }
            });
            scope.$watchCollection('searchCriteria', function(val) {
                scope.$parent.searchCriteria = val;
                scope.totalRows = scope.tableData && ($filter('filter')(scope.tableData, val, false)).length || 0;
                scope.currentPage = 1;
            });
        }
    };
}])

.directive('attTableRow', [function() {
    return {
        restrict: 'EA',
        compile: function (elem, attr) {
            if (attr.type === 'header') {
                elem.find('tr').eq(0).addClass('tablesorter-headerRow');
            } else if (attr.type === 'body') {
                var html = elem.children();
                if(attr.rowRepeat){
                    if (attr.trackBy) {
                        html.attr('ng-repeat', attr.rowRepeat.concat(" | orderBy : columnIndex : reverse | filter : searchCriteria : false | attLimitTo : viewPerPage : viewPerPage*(currentPage-1) track by " + attr.trackBy));
                    } else {
                        html.attr('ng-repeat', attr.rowRepeat.concat(" | orderBy : columnIndex : reverse | filter : searchCriteria : false | attLimitTo : viewPerPage : viewPerPage*(currentPage-1) track by $index"));
                    }
                }
                html.attr('ng-class', "{'alt-row': $even,'normal-row': $odd}");
                elem.append(html);
            }
        }
    };
}])

.directive('attTableHeader', ['tableConfig', function(tableConfig) { 
    return { 
        restrict: 'EA', 
        replace: true, 
        transclude: true, 
        scope: { 
            sortable: '@', 
            defaultSort: '@', 
            index: '@key' 
        }, 
        require: '^attTable', 
        templateUrl: 'app/scripts/ng_js_att_tpls/table/attTableHeader.html', 
        link: function(scope, elem, attr, ctrl) { 
            var reverse = tableConfig.defaultSortPattern; 
            scope.headerName = elem.text(); 
            scope.sortPattern = null; 
            ctrl.setIndex(scope); 
 
            scope.$watch(function() { 
                return elem.text(); 
            }, function(value) { 
                scope.headerName = value; 
            }); 
            scope.sort = function(sortType) { 
                if(typeof sortType === 'boolean') { 
                    reverse = sortType; 
                } 
                ctrl.sortData(scope.index, reverse); 
                scope.sortPattern = reverse ? 'descending' : 'ascending'; 
                reverse = !reverse; 
            }; 
            scope.$watch(function() { 
                return ctrl.currentSortIndex; 
            }, function(value) { 
                if (value !== scope.index) { 
                    scope.sortPattern = null; 
                } 
            }); 
 
            if (scope.sortable === undefined || scope.sortable === 'true' || scope.sortable === true) { 
                scope.sortable = 'true'; 
            } else if (scope.sortable === false || scope.sortable === 'false') { 
                scope.sortable = 'false'; 
            } 
 
            if(scope.sortable !== 'false') { 
                if(scope.defaultSort === 'A' || scope.defaultSort === 'a') { 
                    scope.sort(false); 
                } else if(scope.defaultSort === 'D' || scope.defaultSort === 'd') { 
                    scope.sort(true); 
                } 
            } 
            scope.resetSortPattern = function() { 
                reverse = tableConfig.defaultSortPattern; 
            }; 
        } 
    }; 
}])

.directive('attTableBody', ['$filter', '$timeout', 'tableConfig', function($filter, $timeout, tableConfig) {
    return {
        restrict: 'EA',
        require: '^attTable',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/table/attTableBody.html',
        link: function (scope, elem, attr, ctrl) {
            var highlightSearchStringClass = tableConfig.highlightSearchStringClass;
            var searchString = "";
            var wrapElement = function (elem) {
                var text = elem.text();
                elem.html($filter('highlight')(text, searchString, highlightSearchStringClass));
            };
            var traverse = function (elem) {
                var innerHtml = elem.children();
                if (innerHtml.length > 0) {
                    for (var i = 0; i < innerHtml.length; i++) {
                        traverse(innerHtml.eq(i));
                    }
                } else {
                    wrapElement(elem);
                    return;
                }
            };
            var clearWrap = function (elem) {
                var elems = elem.find('*');
                for (var i = 0; i < elems.length; i++) {
                    if (elems.eq(i).attr('class') && elems.eq(i).attr('class').indexOf(highlightSearchStringClass) !== -1) {
                        var text = elems.eq(i).text();
                        elems.eq(i).replaceWith(text);
                    }
                }
            };
            $timeout(function () {
                var actualHtml = elem.children();
                scope.$watch(function () {
                    return ctrl.getSearchString();
                }, function (val) {
                    searchString = val;
                    clearWrap(elem);
                    if (actualHtml.length > 0) {
                        traverse(elem);
                    } else {
                        wrapElement(elem);
                    }
                });
            }, 50);
        }
    };
}]);

angular.module('att.abs.tableMessages', ['att.abs.utilities'])
    .constant('messageConstants', {
            TABLE_MESSAGE_TYPES: {
                noMatching: 1,
                errorLoading: 2,
                magnifySearch: 3,
                isLoading: 4},
        USER_MESSAGE_TYPES: {
            success: 1,
            error: 0
            }
        })
        .directive('attTableMessage', ['messageConstants', function(messageConstants) {
                return {
                    restrict: 'AE',
                    replace: true,
                    transclude: true,
                    scope: {
                        msgType: '=',
                        onRefreshClick: '&'
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/tableMessages/attTableMessage.html',
                    link: function(scope) {
                        scope.messageConstants = messageConstants;
                        scope.refreshAction = function(evt) {
                            scope.onRefreshClick(evt);
                        };
                    }
                };
        }]).directive('attUserMessage', ['messageConstants', '$timeout', 'DOMHelper', function(messageConstants, $timeout, DOMHelper) {
            return {
                restrict: 'AE',
                replace: true,
                transclude: true,
                scope: {
                    thetitle: '=',
                    type: '=',
                    message: '=',
                    trigger: '='
                },
                templateUrl: 'app/scripts/ng_js_att_tpls/tableMessages/attUserMessage.html',
                link: function(scope, element) {
                    var prevActiveElement = undefined;
                    var firstElement = undefined;
                    scope.messageConstants = messageConstants;

                    $timeout(function() {
                        firstElement = DOMHelper.firstTabableElement(element[0]);
                    }, 10);

                    
                    scope.$watch('trigger', function() {
                        if (scope.trigger) {
                            prevActiveElement = document.activeElement;
                            if (angular.isDefined(firstElement)) {
                                firstElement.focus();
                            }
                        } else {
                            if (angular.isDefined(prevActiveElement)) {
                                prevActiveElement.focus();
                            }
                        }
                    })

                    
                }
            };
    }]);
angular.module('att.abs.tabs', ['att.abs.utilities'])
    .directive('attTabs', function () {
        return{
            restrict: 'EA',
            transclude: false,
            replace: true,
            scope: {
                tabs: "=title"
            },
            controller: ['$scope', function ($scope) {
                this.getData = function () {
                    return $scope.tabs;
                };
                this.onClickTab = function (tab) {
                    $scope.currentTab = tab.url;
                    return $scope.currentTab;
                };
                this.isActiveTab = function (tab) {
                    return (tab === $scope.currentTab);
                };
            }],
            link: function (scope) {
                for (var i = 0; i < scope.tabs.length; i++) {
                    if ((scope.tabs[i].selected) && (scope.tabs[i].url)) {
                        scope.currentTab = scope.tabs[i].url;
                    }
                }
            }
        };
    })
    .directive('floatingTabs', function () {
        return {
            require: '^attTabs',
            restrict: 'EA',
            transclude: false,
            replace: true,
            scope: {
                size: "@"
            },
            templateUrl: 'app/scripts/ng_js_att_tpls/tabs/floatingTabs.html',
            link: function (scope, elem, attr, attTabsCtrl) {
                scope.tabs = attTabsCtrl.getData();
                scope.onClickTab = attTabsCtrl.onClickTab;
                scope.isActiveTab = attTabsCtrl.isActiveTab;
            }
        };
    })
    .directive('simplifiedTabs', function () {
        return {
            require: '^attTabs',
            restrict: 'EA',
            transclude: false,
            replace: true,
            scope: {
                ctab: "=ngModel"
            },
            templateUrl: 'app/scripts/ng_js_att_tpls/tabs/simplifiedTabs.html',
            link: function (scope, elem, attr, attTabsCtrl) {
                scope.tabs = attTabsCtrl.getData();
                scope.clickTab = function (tab) {
                    scope.ctab = tab.id;
                    return scope.ctab;
                };
                scope.isActive = function (tab) {
                    return (tab === scope.ctab);
                };
            }
        };
    })
    .directive('genericTabs', function () {
        return {
            require: '^attTabs',
            restrict: 'EA',
            transclude: false,
            replace: true,
            scope: {
                ctab: "=ngModel"
            },
            templateUrl: 'app/scripts/ng_js_att_tpls/tabs/genericTabs.html',
            link: function (scope, elem, attr, attTabsCtrl) {
                scope.tabs = attTabsCtrl.getData();
                scope.clickTab = function (tab) {
                    scope.ctab = tab.id;
                    return scope.ctab;
                };
                scope.isActive = function (tab) {
                    return (tab === scope.ctab);
                };
            }
        };
    })
    .directive('skipNavigation', function(){
        return{
            link: function(scope,elem,attr){
                elem.bind('click', function(){
                    var skiptoBody = angular.element(elem.parent().parent().parent().parent())[0].querySelector('a.skiptoBody');
                    (angular.element(skiptoBody)).attr('tabindex',-1);
                    skiptoBody.focus();
                });

            }
        }
    })
    .directive('parentTab', [function () {
        return {
            restrict: 'EA',
            scope: {
                menuItems: '=',
                activeSubMenu: '=',
                activeMenu: '='
            },
            controller: ['$scope', function ($scope) {
                $scope.megaMenu = $scope.menuItems;
                $scope.megaMenuTab;
                $scope.megaMenuHoverTab;
                this.setMenu = function () {
                    $scope.menuItems = $scope.megaMenu;
                    $scope.activeSubMenu.scroll=false;
                    for (var i = 0; i < $scope.menuItems.length; i++) {
                        if ($scope.menuItems[i].active) {
                            $scope.activeMenu = $scope.menuItems[i];
                        }
                    }
                    this.setSubMenuStatus(false);
                    $scope.$apply();
                };
                this.setActiveMenu = function () {
                    if (!($scope.megaMenuTab === undefined || $scope.megaMenuTab === null)) {
                        $scope.menuItems = [$scope.megaMenuTab];
                        $scope.megaMenuTab.scroll = true;
                        $scope.activeMenu = {};
                        $scope.activeSubMenu = $scope.megaMenuTab;
                        this.setSubMenuStatus(true);
                    }
                    else{
                        for(var i=0; i<$scope.menuItems.length; i++){
                            ($scope.menuItems[i].active = false);
                            if($scope.menuItems[i].subItems)
                                for(var j=0; j<$scope.menuItems[i].subItems.length; j++){
                                    $scope.menuItems[i].subItems[j].active = false;
                                };
                        };
                        $scope.menuItems=$scope.megaMenu;
                    }
                    $scope.$apply();
                };
                var checkSubMenuStatus = false;
                this.setSubMenuStatus = function (value) {
                    checkSubMenuStatus = value;
                };
                this.getSubMenuStatus = function () {
                    return checkSubMenuStatus;
                };
                this.setActiveMenuTab = function (tab) {
                    $scope.megaMenuTab = tab;
                };
                this.setActiveMenuHoverTab = function (tab) {
                    $scope.megaMenuHoverTab = tab;
                };
                this.setActiveSubMenuTab = function () {
                    $scope.megaMenuTab = $scope.megaMenuHoverTab;
                };
                this.resetMenuTab = function () {
                    $scope.megaMenuTab = undefined;
                };
                this.clearSubMenu = function () {
                    /* Clears Sub-tems when focus shifts from Sub-menu to Mega menu*/
                    $scope.$evalAsync(function(){
                        $scope.megaMenuTab = undefined;
                        $scope.megaMenuHoverTab = undefined;
                    })
                };
            }]
        };
    }])
    .directive('parentmenuTabs', [function () {
        return {
            restrict: 'EA',
            transclude: true,
            replace: true,
            scope: {
                megaMenu: '@',
                menuItems: '='
            },
            controller: ['$scope', function ($scope) {
                this.getMenu = function () {
                    return $scope.menuItems;
                };
                this.setMenu = function (menuItem) {
                    $scope.menuItems = menuItem;
                };
            }],
            templateUrl: 'app/scripts/ng_js_att_tpls/tabs/parentmenuTab.html'
        };
    }])

    .directive('menuTabs', ["$window", "$document",'events','keymap', function (win, $document, events, keymap) {
        return {
            restrict: 'EA',
            transclude: true,
            replace: true,
            require: ['^?parentTab', '^?parentmenuTabs'],
            scope: {
                activeMenu: "=",
                menuItem: "=",
                subMenu: "@",
                subItemActive: "@",
                tabName: "=?",
                tabUrl: "=?"
            },
            templateUrl: function (element, attrs) {
                if (attrs.megaMenu) {
                    return 'app/scripts/ng_js_att_tpls/tabs/menuTab.html';
                }
                else {
                    return 'app/scripts/ng_js_att_tpls/tabs/submenuTab.html';
                }
            },
            link: function (scope, elem, attr, ctrl) {
                var parentCtrl = ctrl[0];
                var parentmenuCtrl = ctrl[1];
                scope.clickInactive = true;
                scope.showHoverChild = function (e) {
                    scope.clickInactive = false;
                    scope.hoverChild = ctrl[0].getSubMenuStatus();
                    if (e.type === "mouseover" && ctrl[0].getSubMenuStatus())
                    {
                        //scope.showChildren(e);
                    }
                    
                };
                scope.showChildren = function (e) {
                    scope.parentMenuItems = parentmenuCtrl.getMenu();
                    for (var i = 0; i < scope.parentMenuItems.length; i++) {
                        scope.parentMenuItems[i].active = false;
                        if (scope.parentMenuItems[i].subItems) {
                            for (var j = 0; j < scope.parentMenuItems[i].subItems.length; j++) {
                                scope.parentMenuItems[i].subItems[j].active = false;
                            }
                        }
                        scope.clickInactive = true;
                    }
                    scope.menuItem.active = true;
                    scope.activeMenu = scope.menuItem;
                    e.stopPropagation();
                };
                scope.$watch("subItemActive", function (value) {
                    if (value === "true" && scope.subMenu === 'true') {
                        parentCtrl.setActiveMenuHoverTab(scope.menuItem);
                    }
                });
                scope.showMenuClick = function (e) {
                    parentCtrl.setActiveMenuTab(scope.menuItem);
                    e.stopPropagation();
                };
                scope.showSubMenuClick = function (e) {
                    parentCtrl.setActiveSubMenuTab();
                    e.stopPropagation();
                };
                scope.resetMenu = function (e) {
                    parentCtrl.resetMenuTab();
                    e.stopPropagation();
                };
                function debounce(method, delay) {
                    clearTimeout(method._tId);
                    method._tId = setTimeout(function () {
                        parentCtrl.setMenu();
                    }, delay);
                }
                function debounce1(method, delay) {
                    clearTimeout(method._tId);
                    method._tId = setTimeout(function () {
                        parentCtrl.setActiveMenu();
                    }, delay);
                }
                $document.bind('scroll', function () {
                   /* if (win.pageYOffset === 0) {
                        debounce(parentCtrl.setMenu, 100);
                    }
                    else if (win.pageYOffset > 1 && win.pageYOffset < 1500) {
                        debounce1(parentCtrl.setActiveMenu, 100);
                    }*/
                });
                elem.bind('keydown', function(evt){
                    if (!(evt.keyCode)){
                        evt.keyCode = evt.which;
                    }
                    if(evt.keyCode !== keymap.KEY.TAB){
                        events.preventDefault(evt);
                        events.stopPropagation(evt);
                    }

                    switch (evt.keyCode) {
                        case keymap.KEY.ESC:
                            var skiptoBody;
                            if (!(elem.attr('mega-menu'))) {
                                if (elem.attr("sub-menu") === "true") {
                                    /* condition true when user navigates through Sub-menu*/

                                    skiptoBody = angular.element(elem.parent().parent().parent().parent().parent().parent().parent())[0].querySelector('a.skiptoBody');
                                    (angular.element(skiptoBody)).attr('tabindex',-1);
                                    skiptoBody.focus();
                                }
                                else if (elem.attr("sub-menu") === undefined) {
                                    skiptoBody = angular.element(elem.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent())[0].querySelector('a.skiptoBody');
                                    (angular.element(skiptoBody)).attr('tabindex',-1);
                                    skiptoBody.focus();
                                }
                            }
                            else
                            {
                                if (elem.attr("menu-item") === "item") {
                                    /* Works when user on Mega menu*/

                                    skiptoBody = angular.element(elem.parent().parent().parent().parent())[0].querySelector('a.skiptoBody');
                                    (angular.element(skiptoBody)).attr('tabindex',-1);
                                    skiptoBody.focus();
                                }
                            }
                            break;
                        case keymap.KEY.RIGHT:
                            if (!(elem.attr('mega-menu'))) {
                                 var el = angular.element(elem)[0];
                                if (elem.attr("sub-menu") === "true") {
                                    /* condition true when user navigates through Sub-menu*/
                                    if(el.nextElementSibling === null){ break;}
                                    if(el.nextElementSibling){
                                        el.nextElementSibling.querySelector("a").focus();
                                    }
                                    else{
                                        do{
                                            if (el && el.nextSibling){
                                                el = el.nextSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if (el.querySelector("a") == null){}
                                            else{
                                            el.querySelector("a").focus();}
                                        }
                                        events.preventDefault(evt);
                                        events.stopPropagation(evt);
                                    }
                                }
                                else if (elem.attr("sub-menu") === undefined) {
                                    if(el.nextElementSibling === null) break;
                                    if(el.nextElementSibling){
                                        el.nextElementSibling.querySelector("a").focus();
                                    }
                                    else{
                                        do{
                                            if (el && el.nextSibling){
                                                el = el.nextSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if (el.querySelector("a") == null){}
                                            else{
                                            el.querySelector("a").focus();}
                                        }

                                    }
                                }
                            }
                            else
                            {
                                if (elem.attr("menu-item") === "item") {
                                    /* When user navigates through on Mega menu*/

                                    var el = angular.element(elem)[0];

                                        if(el.nextElementSibling){
                                            if(el.nextElementSibling.querySelector("span") == null){
                                            }
                                            else {
                                                el.nextElementSibling.querySelector("span").focus();
                                            }
                                    }
                                    else{
                                        do{
                                            if (el && el.nextSibling){
                                                el = el.nextSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if(el.querySelector("span") === null){}
                                            else {
                                                el.querySelector("span").focus();
                                            }
                                        }
                                        events.preventDefault(evt);
                                        events.stopPropagation(evt);
                                    }
                                }
                            }
                            break;
                        case keymap.KEY.DOWN:


                            if (elem.attr('mega-menu')) {
                                /* When user navigates from top menu to Sub-menu*/
                                angular.element(elem)[0].querySelectorAll(".megamenu__items")[0].querySelector("a").focus();
                            }
                            else if(elem.attr("sub-menu") === undefined) {
                            /*When user navigates within Sub Items*/
                                var el = document.activeElement;
                                if(el.nextElementSibling === null) break;
                                 if(el.nextElementSibling) {
                                    el.nextElementSibling.focus();
                                }else{
                                     do {
                                         if (el && el.nextSibling){
                                             el = el.nextSibling;
                                         }
                                         else{
                                             break;
                                         }
                                     } while (el && el.tagName !== 'A');
                                     if(el.attributes !== null){                                        
                                         el.focus();
                                     }
                                     events.stopPropagation(evt);
                                 }

                            }
                            else if (elem.attr("sub-menu")=== "true" ) {
                                /* condition true when user navigates from sub menu to  Sub Item*/
                                var childItems = angular.element(elem)[0].querySelector("span").querySelector('a');
                                if(childItems === null) break;
                                childItems.focus();
                            }
                            break;

                        case keymap.KEY.LEFT:

                            if (!(elem.attr('mega-menu'))) {
                                var el = angular.element(elem)[0];
                                if (elem.attr("sub-menu") === "true") {
                                    /* condition true when user navigates through Sub-menu*/
                                    if(el.previousElementSibling === null) break;
                                    if(el.previousElementSibling){
                                        el.previousElementSibling.querySelector("a").focus();
                                    }
                                    else{
                                        do{
                                            if (el && el.previousSibling){
                                                el = el.previousSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if (el.querySelector("a") == null){}
                                            else{
                                            el.querySelector("a").focus();}
                                        }
                                        events.preventDefault(evt);
                                        events.stopPropagation(evt);
                                    }

                                    /*el.previousElementSibling.querySelector("span").focus();
                                    events.stopPropagation(evt);
                                    break;*/
                                }
                                else if (elem.attr("sub-menu") === undefined) {
                                    if(el.previousElementSibling === null) break;
                                    if(el.previousElementSibling){
                                        el.previousElementSibling.querySelector("a").focus();
                                    }
                                    else{
                                        do{
                                            if (el && el.previousSibling){
                                                el = el.previousSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if (el.querySelector("a") == null){}
                                            else{
                                            el.querySelector("a").focus();}
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if (elem.attr("menu-item") === "item") {
                                    /* Works when user on Mega menu*/

                                    var el = angular.element(elem)[0];
                                        if(el.previousElementSibling){

                                            if(el.previousElementSibling.querySelector("span") === null){
                                            }
                                            else {
                                                el.previousElementSibling.querySelector("span").focus();
                                            }
                                      
                                    }
                                    else{
                                        do{
                                            if (el && el.previousSibling){
                                                el = el.previousSibling;
                                            }
                                            else{
                                                break;
                                            }
                                        } while (el && el.tagName !== 'LI');
                                        if(el){
                                            if (el.querySelector("span") === null) {
                                            }
                                            else {
                                            el.querySelector("span").focus();
                                            }
                                        }
                                        events.preventDefault(evt);
                                        events.stopPropagation(evt);
                                    }
                                }
                            }
                            break;
                        case keymap.KEY.UP:

                            if (elem.attr("sub-menu") === "true") {
                                var el = document.activeElement;
                                var parent_menu = angular.element(elem.parent().parent().parent().parent())[0].querySelector("span");
                                parent_menu.focus();
                                parentCtrl.clearSubMenu();
                                scope.menuItem.active = false;
                                break;
                            }
                            else if(elem.attr("sub-menu") === undefined) {
                                /* condition true when user navigates within Sub Items*/
                                var el = document.activeElement;
                                var parent_menu = angular.element(elem.parent().parent().parent().parent())[0].querySelector("a");                                
                                if(document.activeElement === angular.element(el).parent().parent()[0].querySelectorAll('a')[0]){
                                    parent_menu.focus();
                                    break;
                                };

                                if(el.previousElementSibling) {
                                    var prev_a =  el.previousElementSibling;
                                    (el.previousElementSibling != null)? el.previousElementSibling.focus(): parent_menu.focus();
                                }else{
                                    do{
                                        if (el && el.previousSibling){
                                            el = el.previousSibling;
                                        }
                                        else{
                                            break;
                                        }
                                    } while (el && el.tagName !== 'A');
                                    if(el && (el.nodeType !== 3)){
                                        el.focus();
                                    }
                                    events.preventDefault(evt);
                                    events.stopPropagation(evt);
                                }

                                break;
                            }
                        default:
                            break;
                    }
                });
            }
        };
    }]);

angular.module('att.abs.tagBadges', [])
        .directive('tagBadges', ['$parse', '$timeout', function($parse, $timeout) {
                return {
                    restrict: 'EA',
                    replace: false,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/tagBadges/tagBadges.html',
                    scope: {
                        styleType: "@",
                        onClose: "&"
                    },
                    link: function(scope, elem, attr) {
                        scope.isSmall = false;
                        scope.isIcon = false;
                        scope.isColor = false;
                        scope.display = true;
                        scope.isClosable = false;
                        scope.isHighlight = false;
                        scope.customColor = false;

                        if (attr.small === "") {
                            scope.isSmall = true;
                        }
                        if (scope.styleType === "icon") {
                            scope.isIcon = true;
                        }
                        else if (scope.styleType === "color") {
                            scope.isColor = true;
                            if(attr.color !== undefined && attr.color !== "") {
                                scope.customColor = true;
                                attr.$observe("color", function(val) {
                                    scope.border_type_borderColor = val;
                                    scope.background_type_backgroundColor = val;
                                    scope.background_type_borderColor = val;
                                });
                            }
                        }
                        scope.activeHighlight = function(state){
                            if(scope.customColor){
                                if(state){
                                    scope.isHighlight = true;
                                }
                                else{
                                    scope.isHighlight = false;
                                }
                            }
                        };
                        if (attr.closable === "") {
                            scope.isClosable = true;
                            scope.closeMe = function() {
                                scope.display = false;
                                $timeout(function(){
                                    elem.attr("tabindex", "0");
                                    elem[0].focus();
                                    elem.bind('blur', function(){
                                        elem.remove();
                                    });
                                });
                                if(attr['onClose']){
                                    scope.onClose = $parse(scope.onClose);
                                    scope.onClose();
                                }
                            };
                        }
                    }
                };
            }]);
angular.module('att.abs.textOverflow', [])
        .constant('textDefaultOptions', {
            width: '50%'
        })
.directive('attTextOverflow', ['textDefaultOptions','$compile',function(textDefaultOptions,$compile)
{
    return {
        restrict: 'A',
        link: function(scope, elem, attrs)
        {
            var tooltipText = elem.text();
            elem.addClass('text-ellipsis');
            attrs.$observe('attTextOverflow', function(val){
                if(val){
                    elem.css({"width":val});
                }
                else{
                    elem.css({"width":textDefaultOptions.width});
                }
            });
            if(!(elem.attr('tooltip'))){
                elem.attr("tooltip", tooltipText);
                elem.attr("tooltip-placement", 'above');
                var newElem =  angular.element(elem);
                $compile(newElem)(scope);
            }
        }
    };
}]);

angular.module('att.abs.toggle', ['angular-gestures', 'att.abs.position'])
        .directive('attToggleTemplate', ['$compile', '$log', '$position', function ($compile, $log, $position)
            {
                return{
                    restrict: 'A',
                    require: 'ngModel',
                    transclude: true,
                    scope: {
                        modelVal: "=ngModel"
                    },
                    templateUrl: 'app/scripts/ng_js_att_tpls/toggle/demoToggle.html',
                    link: function (scope, element, attr) {
                        scope.initialDragPosition = 0;
                        var dragStatus = 0;
                        var switchMovementPath = ($position.offset(element.children().eq(1).children().eq(0)).width - 1);
                        var updateModelVal = function () {
                            if (scope.attrValue === attr.ngTrueValue || scope.attrValue)
                            {
                                scope.modelVal = false;
                            }
                            else
                            {
                                scope.modelVal = true;
                            }
                        };
                        scope.updateModel = function (env) {
                            {
                                if (dragStatus !== 1) {
                                    updateModelVal();
                                    dragStatus = 0;
                                }
                            }
                            env.preventDefault();
                        };
                        scope.drag = function (e) {
                            dragStatus = 1;
                            if (e.type === 'dragstart') {
                                scope.initialDragPosition = $position.position(element.children().eq(1)).left;
                                element.children().eq(1).addClass('dragging');
                            } else if (e.type === 'drag') {
                                var left = Math.min(0, Math.max(scope.initialDragPosition + e.gesture.deltaX, -switchMovementPath));
                                element.children().eq(1).css({
                                    left: left + 'px'
                                });
                            } else if (e.type === 'dragend') {
                                var isOn = $position.position(element.children().eq(1)).left > (switchMovementPath * -1) / 2;
                                element.children().eq(1).removeClass('dragging');
                                TweenMax.to(element.children().eq(1), .1, {left: isOn ? 0 : (switchMovementPath * -1), ease: Power4.easeOut,
                                    onComplete: function () {
                                        element.children().eq(1).css({left: ''});
                                    }});
                                if (isOn || (!isOn && e.gesture.direction === "left")) {
                                    updateModelVal();
                                }
                                dragStatus = 0;
                            }

                            return false;
                        };

                        scope.directiveValue = attr.attToggleTemplate;
                        scope.on = attr.trueValue;
                        scope.off = attr.falseValue;
                        var switchMovementPathPixels = ((switchMovementPath) * -1) + 'px';
                        scope.$watch('modelVal', function (newVal) {
                            scope.attrValue = newVal;
                            if (newVal === attr.ngTrueValue || newVal) {
                                element.children().eq(1).css({
                                    left: '0px'
                                });
                                element.addClass('att-checkbox--on');
                                var elem = element.find('div').find('div').eq(1);
                                elem.attr("aria-checked", true);
                                dragStatus = 0;
                            } else {
                                element.children().eq(1).css({
                                    left: switchMovementPathPixels
                                });
                                element.removeClass('att-checkbox--on');
                                 var elem = element.find('div').find('div').eq(1);
                                elem.attr("aria-checked", false);
                                dragStatus = 0;
                            }
                            element.children().eq(1).css({
                                left: ''
                            });
                        });
                    }
                };
            }
        ])

        .directive('attToggleMain', ['$compile', function ($compile)
            {
                return{
                    restrict: 'A',
                    require: 'ngModel',
                    transclude: true,
                    replace: true,
                    scope: {
                        modelValue: "=ngModel",
                        trueValue: "=ngTrueValue",
                        falseValue: "=ngFalseValue"
                    },
                    link: function (scope, element, attr) {
                        var html = "";
                        var attrVal = "";
                        element.removeAttr('att-toggle-main');
                        scope.on = attr.ngTrueValue;
                        scope.off = attr.ngFalseValue;
                        scope.largeValue = attr.attToggleMain;
                        if (angular.isDefined(attr.ngTrueValue)) {
                            html += ' true-value="{{on}}" false-value="{{off}}"';
                        }
                        if (scope.largeValue !== undefined)
                        {
                            attrVal += ' ="{{largeValue}}"';
                        }

                        element.css({display: 'none'});
                        var elm = angular.element('<div class="att-switch att-switch-alt" ng-class="{\'large\' : largeValue == \'large\'}" ng-model="modelValue"' + html + ' att-toggle-template' + attrVal + '>' + element.prop('outerHTML') + '</div>');
                        elm = $compile(elm)(scope);
                        element.replaceWith(elm);
                    }
                };
            }]);
angular.module('att.abs.treeview', [])
        .directive('treeView', function() {
            return{
                restrict: 'A',
                link: function(scope, elem) {
                    var el = elem.children('ul li');
                    var list = TweenMax.from(el, .2, {display: 'none', paused: true, reversed: true});
                    elem.attr("tabindex","0");
                    function toggleBranch() {
                        if (list.reversed())
                        {
                            list.play();
                        } else
                        {
                            list.reverse();
                        }
                    };
                    function toggleTree(e) {
                        e.stopPropagation();
                        if (angular.element(e.target).attr("tree-view") !== undefined)
                        {
                            if (elem.hasClass('minus'))
                            {
                                elem.removeClass('minus');
                            }
                            else
                            {
                                elem.addClass('minus');
                            }
                            toggleBranch();
                        }
                    }
                    elem.on('click', function(e) {
                        toggleTree(e);
                    });
                    elem.on('keypress', function (e) {
                        var activeCode = e.keyCode ? e.keyCode : e.charCode;
                        var keyCode = [13,32];
                        if (keyCode.length > 0 && ((activeCode && keyCode.indexOf(activeCode) > -1))) {
                            toggleTree(e);
                            e.preventDefault();
                        }
                    });
                }
            };
        });

angular.module('att.abs.typeAhead', ['att.abs.tagBadges'])

        .directive('focusMe',['$timeout', '$parse', function($timeout, $parse) {
            return {
                link: function(scope, element, attrs) {
                    var model = $parse(attrs.focusMe);
                    scope.$watch(model, function(value) {
                        if (value) {
                            $timeout(function() {
                                element[0].focus();
                                scope.inputActive=true;
                            });
                        }
                    });
                    element.bind('blur', function() {
                         model.assign(scope, false);
                         scope.inputActive=false;
                         scope.$digest();
                     });
                }
            };
        }])

        .directive('typeAhead', ['$timeout','$log', function($timeout,$log) {
                return {
                    restrict: 'EA',
                    templateUrl: 'app/scripts/ng_js_att_tpls/typeAhead/typeAhead.html',
                    replace: true,
                    scope: {
                        items: '=',
                        title: '@?',
                        titleName: '@',
                        subtitle: '@',
                        model: '=',
                        emailIdList:'=',
                        emailMessage:'='
                    },
                    link: function(scope, elem) {
                        if(!angular.isDefined(scope.titleName) && angular.isDefined(scope.title)){
                             $timeout(function(){
                            scope.titleName = scope.title;
                            $log.warn("title attribute is deprecated and title-name attribute is used instead as it is conflicting with html title attribute");
                             });
                        }
                        scope.lineItems = [];
                        scope.filteredListLength = -1;
                        scope.filteredList = [];
                        scope.setFocus = function() {
                        scope.clickFocus = true;
                        };
                        scope.setFocus();
                        scope.handleSelection = function(selectedItem,emailItem) {
                            scope.lineItems.push(selectedItem);
                            scope.emailIdList.push(emailItem);
                            scope.model = "";
                            scope.current = 0;
                            scope.selected = true;
                            scope.clickFocus = true;
                        };
                        scope.theMethodToBeCalled = function(index) {
                            var tempArr = scope.lineItems.slice();
                            scope.emailIdList.splice(index, 1);
                            tempArr.splice(index, 1);
                            $timeout(function() {
                                scope.lineItems = [];
                                scope.$apply();
                                scope.lineItems = scope.lineItems.concat(tempArr);
                            });
                        };

                        scope.current = 0;
                        scope.selected = true;

                        scope.isCurrent = function(index, itemName,itemEmail,dropdownLength) {
                            if (scope.current === index) {
                                scope.itemName = itemName;
                                scope.itemEmail = itemEmail;
                            }
                            scope.dropdownLength=dropdownLength;
                            return scope.current === index;
                        };

                        scope.setCurrent = function(index) {
                            scope.current = index;
                        };

                        scope.selectionIndex = function(evt) {
                            if (evt.keyCode === 38 && scope.current > 0) {
                               evt.preventDefault();
                                scope.current = scope.current - 1;
                                scope.isCurrent(scope.current);
                            } else if (evt.keyCode === 9) {
                                scope.selected = true;
                            } else if (evt.keyCode === 13 && scope.dropdownLength!==scope.items.length) {
                                scope.handleSelection(scope.itemName,scope.itemEmail);
                            } else if ((evt.keyCode === 8 && scope.model.length === 0) || evt.keyCode === 46) {
                                scope.theMethodToBeCalled(scope.lineItems.length - 1);
                            } else if (evt.keyCode === 40 && scope.current < scope.dropdownLength-1) {
                                evt.preventDefault();
                                scope.current = scope.current + 1;
                                scope.isCurrent(scope.current);
                            }
                            elem[0].querySelector('.list-scrollable').scrollTop = (scope.current - 1) * 35;
                        };
                    }
                };
            }]);
angular.module('att.abs.verticalSteptracker', ['ngSanitize'])
    .directive('verticalSteptracker', [ function(){
        return {
            restrict: 'EA',
            transclude: true,
            replace: false,
            scope: {},
            template: '<div class="vertical-nav"><ul ng-transclude arial-label="step list" role="presentation" class="tickets-list-height"></ul></div>',
            link: function () {}
        };
    }])
    .directive('verticalSteptrackerStep',[ function(){
        return {
            restrict: 'EA',
            transclude: true,
            replace: false,
            scope: {
                type: "=type",
                id: "=id"
            },
            templateUrl: 'app/scripts/ng_js_att_tpls/verticalSteptracker/vertical-step-tracker.html',
            link: function(){}
         };
    }])
    .directive('attAbsLink',[ function(){
        return{
            restrict: 'EA',
            transclude: true,
            replace: false,
            template: '<span ng-transclude class="view-log"></span>'
        };
    }]);
angular.module('att.abs.videoControls', [])
        .config(['$compileProvider' , function ($compileProvider) {
                $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|javascript):/);
            }])
        .directive('videoControls', [function() {
                return {
                    restrict: 'EA',
                    replace: true,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/videoControls/videoControls.html'
                };
            }])
        .directive('photoControls', [function() {
                return {
                    restrict: 'EA',
                    replace: true,
                    transclude: true,
                    templateUrl: 'app/scripts/ng_js_att_tpls/videoControls/photoControls.html',
                    scope: {
                        prevLink: "@",
                        nextLink: "@"
                    },
                    link: function(scope, elem, attr) {
                        if(!attr['prevLink']){
                            scope.prevLink = 'javascript:void(0)';
                        }
                        if(!attr['nextLink']){
                            scope.nextLink = 'javascript:void(0)';
                        }
                        scope.links = {
                            prevLink : scope.prevLink,
                            nextLink : scope.nextLink
                        };
                    }
                };
            }]);
angular.module("app/scripts/ng_js_att_tpls/accordion/accordion.html", []).run(["$templateCache", function($templateCache) {
	  $templateCache.put("app/scripts/ng_js_att_tpls/accordion/accordion.html",
	    "<div class=\"att-accordion__group tabpanel\" ng-class=\"{'att-accordion__group att-accordion__group--open':isOpen,'att-accordion__group':!isOpen }\">\n" +
	    "    <a  ng-show=\"showicon\" \n" +
	    "        class=\"toggle-header att-accordion__heading att-accordion__toggle noafter\" \n" +
	    "        aria-selected=\"{{focused}}\" \n" +
	    "        aria-controls=\"panel{{index}}\" \n" +
	    "        aria-expanded=\"{{isOpen}}\" \n" +
	    "        ng-class=\"{focus: focused, selected: focused}\"         \n" +
	    "        role=\"tab\" \n" +
	    "        ng-click=\"toggle()\" \n" +
	    "        accordion-transclude=\"heading\" \n" +
	    "        style=\"cursor:pointer; text-decoration:none\">\n" +
	    "        <span href=\"#\"><i class={{headingIconClass}}></i>&nbsp;&nbsp;{{heading}}</span>\n" +
	    "        <i i ng-show = 'childLength > 0' ng-class=\"{'icon-chevron-down':!isOpen,'icon-chevron-up':isOpen }\" class=\"pull-right\"></i>\n" +
	    "    </a>\n" +
	    "    <div ng-show=\"!showicon\" \n" +
	    "         ng-class=\"{focus: focused, selected: focused}\" \n" +
	    "         style=\"text-decoration:none\" \n" +
	    "         accordion-transclude=\"heading\"          \n" +
	    "         role=\"tab\"  \n" +
	    "         aria-expanded=\"{{isOpen}}\"\n" +
	    "         aria-selected=\"{{focused}}\" \n" +
	    "         aria-controls=\"panel{{index}}\" \n" +
	    "         class=\"toggle-header att-accordion__heading att-accordion__toggle noafter\">\n" +
	    "        <span>{{heading}}</span>\n" +
	    "    </div>    \n" +
	    "    <div aria-label=\"{{heading}}\" \n" +
	    "         aria-hidden=\"{{!isOpen}}\" \n" +
	    "         role=\"tabpanel\" \n" +
	    "         collapse=\"!isOpen\" \n" +
	    "         class=\"att-accordion__body\" \n" +
	    "         id=\"panel{{index}}\" \n" +
	    "         ng-transclude>\n" +
	    "    </div>\n" +
	    "    <div class=\"att-accordion__bottom--border\"></div>    \n" +
	    "</div> ");
}]);

angular.module("app/scripts/ng_js_att_tpls/accordion/accordion_alt.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/accordion/accordion_alt.html",
    "<div class=\"att-accordion__group tabpanel\" ng-class=\"{'att-accordion__group att-accordion__group--open':isOpen,'att-accordion__group':!isOpen }\">\n" +
    "    <a class=\"toggle-header att-accordion__heading att-accordion__toggle\" \n" +
    "       aria-selected=\"{{focused}}\" \n" +
    "       aria-controls=\"panel{{index}}\" \n" +
    "       ng-class=\"{focus: focused, selected: focused}\" \n" +
    "       aria-expanded=\"{{isOpen}}\" \n" +
    "       role=\"tab\" \n" +
    "       ng-click=\"toggle()\" \n" +
    "       accordion-transclude=\"heading\">        \n" +
    "    </a>\n" +
    "    <span>{{heading}}</span>\n" +
    "    <div aria-label=\"{{heading}}\" \n" +
    "         aria-hidden=\"{{!isOpen}}\" \n" +
    "         role=\"tabpanel\" \n" +
    "         collapse=\"!isOpen\" \n" +
    "         class=\"att-accordion__body\" \n" +
    "         id=\"panel{{index}}\" \n" +
    "         ng-transclude>\n" +
    "    </div>\n" +
    "</div> ");
}]);

angular.module("app/scripts/ng_js_att_tpls/accordion/attAccord.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/accordion/attAccord.html",
    "<div ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/accordion/attAccordBody.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/accordion/attAccordBody.html",
    "<div ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/accordion/attAccordHeader.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/accordion/attAccordHeader.html",
    "<div ng-click=\"clickFunc()\">\n" +
    "	<div ng-transclude>\n" +
    "		<i class=\"icon-chevron-down\"></i>\n" +
    "	</div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/alert/alert.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/alert/alert.html",
    "<div class=\"alert\" ng-class=\"{'alert-success': alertType === 'success', 'alert-warning': alertType === 'warning', 'alert-error': alertType === 'error', 'alert-info': alertType === 'info', 'alert-inplace': showTop !== 'true'}\" ng-show=\"showAlert\" ng-style=\"cssStyle\">\n" +
    "    <div class=\"container\">\n" +
    "        <a href=\"javascript:void(0)\" alt=\"close\" class=\"close-role\" ng-click=\"close()\" tabindex=\"0\" att-accessibility-click=\"32,13\">Dismiss <i class=\"icon-circle-action-close\"></i></a>\n" +
    "        <span ng-transclude> </span>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/boardStrip/attAddBoard.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/boardStrip/attAddBoard.html",
    "<div tabindex=\"0\" att-accessibility-click=\"13,32\" ng-click=\"addBoard()\" aria-label=\"Add Board\" class=\"boardstrip-item--add\">\n" +
    "    <i aria-hidden=\"true\" class=\"icon-add centered\"></i>\n" +
    "    <br/>\n" +
    "    <div class=\"centered\">Add board</div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/boardStrip/attBoard.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/boardStrip/attBoard.html",
    "<li att-board-navigation tabindex=\"0\" aria-label=\"{{boardLabel}}\" att-accessibility-click=\"13,32\" ng-click=\"selectBoard(boardIndex)\" class=\"board-item\" ng-class=\"{'selected': getCurrentIndex()===boardIndex}\">\n" +
    "    <div ng-transclude></div>\n" +
    "    <div class=\"board-caret\" ng-if=\"getCurrentIndex()===boardIndex\">\n" +
    "        <div class=\"board-caret-indicator\"></div>\n" +
    "        <div class=\"board-caret-arrow-up\"></div>\n" +
    "    </div>\n" +
    "</li>");
}]);

angular.module("app/scripts/ng_js_att_tpls/boardStrip/attBoardStrip.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/boardStrip/attBoardStrip.html",
    "<div class=\"att-boardstrip\">\n" +
    "	<div class=\"boardstrip-reel\">\n" +
    "		<div class=\"prev-items\" ng-if=\"isPrevBoard()\">\n" +
    "			<i tabindex=\"0\" aria-label=\"Scroll Boardstrip Left\" att-accessibility-click=\"13,32\" ng-click=\"prevBoard()\" class=\"arrow icon-arrow-left-circle\"></i>\n" +
    "		</div>\n" +
    "		<div att-add-board on-add-board=\"onAddBoard()\"></div>\n" +
    "		<div class=\"board-viewport\"><ul role=\"presentation\" class=\"boardstrip-container\" ng-transclude></ul></div>\n" +
    "		<div class=\"next-items\" ng-if=\"isNextBoard()\">\n" +
    "			<i tabindex=\"0\" aria-label=\"Scroll Boardstrip Right\" att-accessibility-click=\"13,32\" ng-click=\"nextBoard()\" class=\"arrow icon-arrow-right-circle\"></i>\n" +
    "		</div>\n" +
    "	</div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/buttons/buttonDropdown.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/buttons/buttonDropdown.html",
    "<div class=\"att-btn-dropdown\">\n" +
    "    <div class=\"buttons-dropdown--small btn-group\" ng-class=\"{'open': isOpen}\" att-accessibility-click=\"13,32\" ng-click=\"toggle()\">\n" +
    "        \n" +
    "        <button role=\"menu\" class=\"button button--secondary button--small buttons-dropdown__drop dropdown-toggle\" ng-if=\"type==='dots'\" alt=\"Click for Options\" >\n" +
    "            \n" +
    "            <div class=\"circle\"></div>\n" +
    "            <div class=\"circle\"></div>\n" +
    "            <div class=\"circle\"></div>\n" +
    "        </button>\n" +
    "        <button role=\"menu\" class=\"button button--secondary button--small buttons-dropdown__drop dropdown-toggle ng-isolate-scope actions-title\" ng-if=\"type === 'actions'\" alt=\"Actions dropdown Buttons\">Actions</button>\n" +
    "        \n" +
    "\n" +
    "        <ul ng-class=\"{'dropdown-menu dots-dropdwn': type==='dots', 'dropdown-menu actions-dropdwn': type === 'actions'}\" ng-transclude></ul>\n" +
    "    </div>\n" +
    "    \n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/colorselector/colorselector.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/colorselector/colorselector.html",
    "<div class=\"att-radio att-color-selector__item\"  \n" +
    "     ng-class=\"{'att-radio--on': (iconColor === selected)}\">\n" +
    "    <div class=\"att-radio__indicator\" tabindex=\"0\" att-accessibility-click=\"32,13\" ng-click=\"selectedcolor(iconColor)\" \n" +
    "         ng-style=\"applycolor\" ng-transclude></div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/datepicker/dateFilter.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/datepicker/dateFilter.html",
    "<div class=\"calendar\" ng-class=\"{'monthpicker':mode === 1}\">\n" +
    "    <div class=\"select2-container\" ng-class=\"{'select2-container-active select2-dropdown-open': showDropdownList}\" style=\"width: 100%; z-index:0\">\n" +
    "        <a tabindex=\"0\" id=\"select2-choice\" class=\"select2-choice\" href=\"javascript:void(0)\" att-element-focus=\"focusInputButton\" ng-show=\"!showCalendar\" att-accessibility-click=\"13,32\" ng-click=\"showDropdown()\" ng-blur=\"focusInputButton=false\">\n" +
    "            <span class=\"select2-chosen\" ng-show=\"!showCalendar\">{{selectedOption}}</span>\n" +
    "            <input type=\"text\" ng-show=\"showCalendar\" ng-blur=\"untrackInputChange($event)\" att-input-deny=\"[^0-9\\/-]\" maxlength=\"{{maxLength}}\" ng-model=\"selectedOption\" aria-labelledby=\"select2-choice\" ng-change=\"getDropdownText()\" />\n" +
    "            <abbr class=\"select2-search-choice-close\"></abbr>\n" +
    "            <span ng-class=\"{'select2-arrow': mode !== 1, 'calendar-icon': mode === 1}\"><b></b></span>\n" +
    "        </a>\n" +
    "        <a id=\"select2-chosen\" class=\"select2-choice\" href=\"javascript:void(0)\" ng-show=\"showCalendar\">\n" +
    "            <span class=\"select2-chosen\" ng-show=\"!showCalendar\">{{selectedOption}}</span>\n" +
    "            <input type=\"text\" ng-show=\"showCalendar\" ng-blur=\"untrackInputChange($event)\" att-input-deny=\"[^0-9\\/-]\" maxlength=\"{{maxLength}}\" ng-model=\"selectedOption\" aria-labelledby=\"select2-chosen\" ng-change=\"getDropdownText()\" />\n" +
    "            <abbr class=\"select2-search-choice-close\"></abbr>\n" +
    "            <span tabindex=\"0\" ng-class=\"{'select2-arrow': mode !== 1, 'calendar-icon': mode === 1}\" att-accessibility-click=\"13,32\" ng-click=\"showDropdown()\"><b></b></span>\n" +
    "        </a>\n" +
    "    </div>\n" +
    "    <div class=\"select2-drop select2-drop-active select2-display-none\" ng-style=\"{display: (showDropdownList && 'block') || 'none', 'border-radius': showCalendar && '0 0 0 6px'}\" style=\"width: 100%\">\n" +
    "        <div  id=\"dateFilterList\" att-scrollbar ><ul class=\"select2-results options\" ng-transclude></ul></div>\n" +
    "		<ul class=\"select2-results sttings\" style=\"margin-top:0px\">\n" +
    "			<li tabindex=\"0\" class=\"select2-result select2-highlighted greyBorder\" ng-class=\"{'select2-result-current': checkCurrentSelection('Custom Single Date')}\" att-accessibility-click=\"13,32\" ng-click=\"selectAdvancedOption('Custom Single Date')\">\n" +
    "                <div class=\"select2-result-label\" ng-if=\"mode !== 1\">Custom Single Date...</div>\n" +
    "				<div class=\"select2-result-label\" ng-if=\"mode === 1\">Custom single month...</div>\n" +
    "            </li>\n" +
    "            <li tabindex=\"0\" class=\"select2-result select2-highlighted\" ng-class=\"{'select2-result-current': checkCurrentSelection('Custom Range')}\" att-accessibility-click=\"13,32\" ng-click=\"selectAdvancedOption('Custom Range')\">\n" +
    "                <div class=\"select2-result-label\" ng-if=\"mode !== 1\">Custom Range...</div>\n" +
    "				<div class=\"select2-result-label\" ng-if=\"mode === 1\">Custom month range...</div>\n" +
    "            </li>\n" +
    "            <li class=\"select2-result select2-highlighted btnContainer\" ng-style=\"{display: (showCalendar && 'block') || 'none'}\">\n" +
    "                <button tabindex=\"0\" ng-blur=\"resetFocus($event)\" att-element-focus=\"focusApplyButton\" att-button=\"\" btn-type=\"{{applyButtonType}}\" size=\"small\" att-accessibility-click=\"13,32\" ng-click=\"apply()\">Apply</button>\n" +
    "                <button tabindex=\"0\" att-button=\"\" btn-type=\"secondary\" size=\"small\" att-accessibility-click=\"13,32\" ng-click=\"cancel()\">Cancel</button>\n" +
    "                <div>\n" +
    "                    <a tabindex=\"0\" href=\"javascript:void(0)\" ng-if=\"mode !== 1\" style=\"text-decoration:underline;\" att-accessibility-click=\"13,32\" ng-click=\"clear()\">Clear Dates</a>\n" +
    "                    <a tabindex=\"0\" href=\"javascript:void(0)\" ng-if=\"mode === 1\" style=\"text-decoration:underline;\" att-accessibility-click=\"13,32\" ng-click=\"clear()\">Clear Months</a>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "		</ul>\n" +
    "    </div>\n" +
    "    <div class=\"datepicker-wrapper show-right\" ng-style=\"{display: (showCalendar && 'block') || 'none'}\">\n" +
    "        <span datepicker ng-blur=\"resetFocus($event)\" att-element-focus=\"focusSingleDateCalendar\" ng-show=\"checkCurrentSelection('Custom Single Date')\"></span>\n" +
    "        <span datepicker ng-blur=\"resetFocus($event)\" att-element-focus=\"focusRangeCalendar\" ng-show=\"checkCurrentSelection('Custom Range')\"></span>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/datepicker/dateFilterList.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/datepicker/dateFilterList.html",
    "<li ng-click=\"!disabled && selectOption(fromDate,toDate,caption)\" att-accessibility-click=\"13,32\" ng-class=\"{'select2-result-current': checkCurrentSelection(caption)}\" class=\"select2-result select2-highlighted ng-scope\" tabindex=\"{{!disabled?'0':'-1'}}\">\n" +
    "                <div class=\"select2-result-label\" ng-class=\"{'disabled':disabled}\" ng-transclude></div>\n" +
    "</li>");
}]);

angular.module("app/scripts/ng_js_att_tpls/datepicker/datepicker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/datepicker/datepicker.html",
    "<div id=\"datepicker\" class=\"datepicker\" ng-class=\"{'monthpicker': mode === 1}\" aria-hidden=\"false\" role=\"dialog\" tabindex=\"-1\" aria-labelledby=\"datepicker\">\n" +
    "    <div class=\"datepicker-days\" style=\"display: block;\">\n" +
    "        <table class=\"table-condensed\">\n" +
    "            <thead>\n" +
    "                <tr>\n" +
    "                    <th id=\"month\" tabindex=\"0\" class=\"datepicker-switch\" colspan=\"{{(mode !== 1) && (currentRows[0].length - 2) || (currentRows[0].length)}}\" style=\"text-align:left\">{{currentTitle}}</th>\n" +
    "                    <th ng-if=\"mode !== 1\" id=\"prev\" aria-hidden=\"{{!disablePrev && 'false'|| 'true'}}\" tabindex=\"{{!disablePrev && '0'|| '-1'}}\" att-accessibility-click=\"13,32\" ng-click=\"!disablePrev && move(-1)\">\n" +
    "            <div class=\"icons-list\" data-size=\"medium\"><i class=\"icon-arrow-left-circle\" ng-class=\"{'disabled': disablePrev}\" alt=\"Left Arrow\"></i>\n" +
    "            </div><span class=\"hidden-spoken\">Previous Month</span>\n" +
    "            </th>\n" +
    "            <th ng-if=\"mode !== 1\" id=\"next\" aria-hidden=\"{{!disableNext && 'false'|| 'true'}}\" tabindex=\"{{!disableNext && '0'|| '-1'}}\" att-accessibility-click=\"13,32\" ng-click=\"!disableNext && move(1)\">\n" +
    "            <div class=\"icons-list\" data-size=\"medium\"><i class=\"icon-arrow-right-circle\" ng-class=\"{'disabled': disableNext}\" alt=\"Right Arrow\"></i>\n" +
    "            </div><span class=\"hidden-spoken\">Next Month</span>\n" +
    "            </th>\n" +
    "            </tr>\n" +
    "            <tr ng-if=\"labels.length > 0\">\n" +
    "                <th tabindex=\"-1\" class=\"dow weekday\" ng-repeat=\"label in labels\"><span>{{label.pre}}</span></th>\n" +
    "            </tr>\n" +
    "            </thead>\n" +
    "            <tbody>\n" +
    "                <tr>\n" +
    "                    <td id=\"datepickerBody\" att-scrollbar colspan=\"{{currentRows[0].length}}\" style=\"padding: 0px;\" headers=\"\">\n" +
    "                        <table ng-class=\"{'table-condensed': mode === 0, 'monthtable-condensed': mode === 1}\" style=\"padding: 0px;\">\n" +
    "                            <thead class=\"hidden-spoken\">\n" +
    "                                <tr ng-show=\"labels.length > 0\">\n" +
    "                                    <th id=\"{{label.post}}\" tabindex=\"-1\" class=\"dow weekday\" ng-repeat=\"label in labels\"></th>\n" +
    "                                </tr>\n" +
    "                            </thead>\n" +
    "                            <tbody>\n" +
    "                                <tr ng-repeat=\"row in currentRows\">\n" +
    "                                    <td headers=\"{{(mode === 0) && dt.header || 'month'}}\" att-element-focus=\"dt.focused\" aria-hidden=\"{{(!dt.oldMonth && !dt.nextMonth && !dt.disabled && 'false') || 'true'}}\" tabindex=\"{{(!dt.oldMonth && !dt.nextMonth && !dt.disabled && '0') || '-1'}}\" ng-repeat=\"dt in row\" class=\"days\" ng-class=\"{'active': dt.selected || dt.from || dt.to, 'from': dt.from, 'to': dt.to, 'range': dt.dateRange, 'prev-month ': dt.oldMonth, 'next-month': dt.nextMonth, 'disabled': dt.disabled, 'today': dt.today, 'weekend': dt.weekend}\" ng-click=\"!dt.selected && !dt.from && !dt.to && !dt.disabled && !dt.oldMonth && !dt.nextMonth && select(dt.date)\" att-accessibility-click=\"13,32\" aria-label=\"{{dt.date | date : 'EEEE, MMMM d'}}\"><span class=\"day\">{{dt.label}}</span></td>\n" +
    "                                </tr>\n" +
    "                                <tr ng-if=\"mode === 1\" class=\"divider\"><td colspan=\"{{nextRows[0].length}}\"><hr></td></tr>\n" +
    "                                <tr>\n" +
    "                                    <th id=\"month\" tabindex=\"0\" class=\"datepicker-switch internal\" colspan=\"{{nextRows[0].length}}\" style=\"text-align:left\">{{nextTitle}}</th>\n" +
    "                                </tr>\n" +
    "                                <tr ng-repeat=\"row in nextRows\">\n" +
    "                                    <td headers=\"{{(mode === 0) && dt.header || 'month'}}\" att-element-focus=\"dt.focused\" aria-hidden=\"{{(!dt.oldMonth && !dt.nextMonth && !dt.disabled && 'false') || 'true'}}\" tabindex=\"{{(!dt.oldMonth && !dt.nextMonth && !dt.disabled && '0') || '-1'}}\" ng-repeat=\"dt in row\" class=\"days\" ng-class=\"{'active': dt.selected || dt.from || dt.to, 'from': dt.from, 'to': dt.to, 'range': dt.dateRange, 'prev-month ': dt.oldMonth, 'next-month': dt.nextMonth, 'disabled': dt.disabled, 'today': dt.today, 'weekend': dt.weekend}\" ng-click=\"!dt.selected && !dt.from && !dt.to && !dt.disabled && !dt.oldMonth && !dt.nextMonth && select(dt.date)\" att-accessibility-click=\"13,32\" aria-label=\"{{dt.date | date : 'EEEE, MMMM d'}}\"><span class=\"day\">{{dt.label}}</span></td>\n" +
    "                                </tr>\n" +
    "                            </tbody>\n" +
    "                        </table>\n" +
    "                    </td>\n" +
    "                </tr>\n" +
    "            </tbody>\n" +
    "        </table>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/datepicker/datepickerPopup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/datepicker/datepickerPopup.html",
    "<div class=\"calendar\">\n" +
    "    <div class=\"box\" ng-class=\"{'active': isOpen}\">\n" +
    "        <span ng-transclude></span>\n" +
    "        <i class=\"calendar-icon\" tabindex=\"0\" att-accessibility-click=\"13,32\" ng-click=\"toggle()\" alt=\"Calendar Icon\"></i>\n" +
    "    </div>\n" +
    "    <div class=\"datepicker-wrapper datepicker-wrapper-display-none\" ng-style=\"{display: (isOpen && 'block') || 'none'}\" aria-hidden=\"false\" role=\"dialog\" tabindex=\"-1\">\n" +
    "        <span datepicker></span>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/dividerLines/dividerLines.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/dividerLines/dividerLines.html",
    "<div class=\"divider-container\" ng-class=\"{'divider-container-light': lightContainer}\">\n" +
    "    <hr ng-class=\"{'divider-light': lightContainer}\">\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/dragdrop/fileUpload.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/dragdrop/fileUpload.html",
    "<label class=\"fileContainer\"><span ng-transclude></span><input type=\"file\" att-file-change></label>");
}]);

angular.module("app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlert.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlert.html",
    "<div class=\"form-field\" ng-class=\"{'error': errorMessage, 'warning': warningMessage}\">\n" +
    "    <label class=\"form-field__label\" ng-class=\"{'form-field__label--show': showLabel, 'form-field__label--hide': hideLabel}\"></label>\n" +
    "    <div class=\"form-field-input-container\" ng-transclude></div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlertPrv.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/formField/attFormFieldValidationAlertPrv.html",
    "<div class=\"form-field\" ng-class=\"{'error':hideErrorMsg}\">\n" +
    "    <div class=\"form-field-input-container\" ng-transclude></div>\n" +
    "	<div class=\"form-field__message error\" type=\"error\" ng-show=\"hideErrorMsg\" >\n" +
    "		<i class=\"icon-info-alert\"></i>{{errorMessage}}\n" +
    "	</div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/formField/creditCardImage.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/formField/creditCardImage.html",
    "<span class=\"css-sprite pull-right\">\n" +
    "<span class=\"hidden-spoken\">We accept</span>\n" +
    "<ul class=\"{{newValCCI}}\">\n" +
    "        <li class=\"css-sprite-mc\"><span class=\"hidden-spoken\">MasterCard</span></li>\n" +
    "        <li class=\"css-sprite-visa\"><span class=\"hidden-spoken\">Visa</span></li>\n" +
    "        <li class=\"css-sprite-amex\"><span class=\"hidden-spoken\">American Express</span></li>\n" +
    "        <li class=\"css-sprite-discover\"><span class=\"hidden-spoken\">Discover</span></li>														\n" +
    "</ul>\n" +
    "</span>\n" +
    "<label for=\"ccForm.card\" class=\"pull-left\">Card number</label>");
}]);

angular.module("app/scripts/ng_js_att_tpls/formField/cvcSecurityImg.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/formField/cvcSecurityImg.html",
    "<div>\n" +
    "<button type=\"button\" class=\"btn btn-alt btn-tooltip\" style=\"padding-top:16px\" title=\"Help\"><i class=\"hidden-spoken\">Help</i></button>\n" +
    "<div class=\"helpertext\" role=\"tooltip\">\n" +
    "<div class=\"popover-title\"></div>\n" +
    "<div class=\"popover-content\">\n" +
    "    <p class=\"text-legal cvc-cc\">\n" +
    "        <img ng-src=\"images/{{newValI}}.png\" alt=\"{{newValIAlt}}\">\n" +
    "    </p>\n" +
    "</div>\n" +
    "</div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/hourpicker/hourpicker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/hourpicker/hourpicker.html",
    "<div class=\"hourpicker\">\n" +
    "    <div class=\"dropdown-width\">\n" +
    "        <div ng-model=\"showlist\" class=\"select2-container topDropDownWidth\" ng-class=\"{'select2-dropdown-open select2-container-active': showlist}\" >\n" +
    "            <a class=\"select2-choice\" href=\"javascript:void(0)\" id=\"customSelect\" ng-keydown=\"selectOption(selectPrevNextValue($event,options,selectedOption))\" att-accessibility-click=\"13\" ng-click=\"showDropdown()\">\n" +
    "                <span class=\"select2-chosen\">{{selectedOption}}</span>\n" +
    "                <span class=\"select2-arrow\"><b></b></span>\n" +
    "            </a>\n" +
    "        </div>             \n" +
    "        <div class=\"select2-display-none select2-with-searchbox select2-drop-active show-search resultTopWidth\" ng-show=\"showlist\">       \n" +
    "        <ul class=\"select2-results resultTopMargin\" >                       \n" +
    "            <li  ng-model=\"ListType\" ng-repeat=\"option in options\" att-accessibility-click=\"13\" ng-click=\"selectOption(option,$index)\" class=\"select2-results-dept-0 select2-result select2-result-selectable\"><div class=\"select2-result-label\"><span >{{option}}</span></div></li>                        \n" +
    "        </ul>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div ng-show=\"showDaysSelector\" class=\"customdays-width\">\n" +
    "        <div att-divider-lines class=\"divider-margin-f\"></div>  \n" +
    "        <div class=\"col-md-3 fromto-margin\">\n" +
    "            <div>From</div> <br>\n" +
    "            <div>To</div>\n" +
    "        </div>\n" +
    "        <div ng-repeat=\"day in days\">\n" +
    "            <div class=\"col-md-3 col-md-days\">\n" +
    "                <div  class=\"col-md-1 daysselect-margin\">\n" +
    "                    <input type=\"checkbox\" ng-model=\"daysList[day]\" title=\"Day selection {{$index}}\" att-checkbox ng-change=\"addSelectedValue(day)\">    \n" +
    "                </div>\n" +
    "                <span>{{day}}</span><br>\n" +
    "                \n" +
    "                <div class=\"dropDownMarginBottom\">\n" +
    "                    <div class=\"select2-container topDropDownWidth\" ng-class=\"{'select2-dropdown-open select2-container-active': FrtimeListDay[day]}\" >\n" +
    "                        <a class=\"select2-choice selectDropDown\" href=\"javascript:void(0)\" tabindex=\"{{daysList[day] ? '0' : '-1'}}\" att-accessibility-click=\"13\" ng-click=\"daysList[day] && showfromDayDropdown(day)\" ng-class=\"{'select2-chosen-disabled':!daysList[day]}\"  ng-keydown=\"daysList[day] && selectFromDayOption(day , selectPrevNextValue($event,fromtime,selectedFromOption[day]));daysList[day] && addSelectedValue(day);\">\n" +
    "                            <span class=\"select2-chosen dropDownMarginRight\" >{{selectedFromOption[day]}} <i ng-if=\"daysList[day]\" ng-class=\"FrtimeListDay[day] ? 'icon-dropdown-up' : 'icon-dropdown-down'\"></i></span>\n" +
    "                        </a>\n" +
    "                    </div>             \n" +
    "                    \n" +
    "                    <div class=\"select2-display-none select2-with-searchbox select2-drop-active show-search resultFromDropDown\"  ng-show=\"FrtimeListDay[day]\">       \n" +
    "                    <ul class=\"select2-results resultTopMargin\" >                       \n" +
    "                        <li ng-click=\"selectFromDayOption(day,time.value);addSelectedValue(day);\" ng-repeat=\"time in fromtime\"  class=\"select2-results-dept-0 select2-result select2-result-selectable\"><div class=\"select2-result-label\" ng-class=\"{'selectedItemInDropDown': (time.value==selectedFromOption[day])}\"><span >{{time.value}}</span></div></li>                        \n" +
    "                    </ul>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "                \n" +
    "                <div class=\"dropDownMarginBottom\">\n" +
    "                    <div class=\"select2-container topDropDownWidth\" ng-class=\"{'select2-dropdown-open select2-container-active': TotimeListDay[day]}\" >\n" +
    "                        <a class=\"select2-choice selectDropDown\" href=\"javascript:void(0)\" tabindex=\"{{daysList[day] ? '0' : '-1'}}\" att-accessibility-click=\"13\" ng-click=\"daysList[day] && showtoDayDropdown(day)\" ng-class=\"{'select2-chosen-disabled':!daysList[day], 'selectDropDown-error':daysList[day] && showToTimeErrorDay[day]}\"  ng-keydown=\"daysList[day] && selectToDayOption(day , selectPrevNextValue($event,totime,selectedToOption[day]));daysList[day] && addSelectedValue(day);\">\n" +
    "                            <span class=\"select2-chosen dropDownMarginRight\">{{selectedToOption[day]}} <i ng-if=\"daysList[day]\" ng-class=\"TotimeListDay[day] ? 'icon-dropdown-up' : 'icon-dropdown-down'\" ></i></span>\n" +
    "                        </a>\n" +
    "                    </div>\n" +
    "                    \n" +
    "                    <div class=\"select2-display-none select2-with-searchbox select2-drop-active show-search resultToDropDown\" ng-show=\"TotimeListDay[day]\">       \n" +
    "                    <ul class=\"select2-results resultTopMargin\" >                       \n" +
    "                        <li ng-click=\"selectToDayOption(day,time.value);addSelectedValue(day);\" ng-repeat=\"time in totime\"  class=\"select2-results-dept-0 select2-result select2-result-selectable\"><div class=\"select2-result-label\" ng-class=\"{'selectedItemInDropDown': (time.value==selectedToOption[day])}\"><span >{{time.value}}</span></div></li>                        \n" +
    "                    </ul>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>  \n" +
    "        </div>    \n" +
    "        <div att-divider-lines class=\"divider-margin-s\"></div>\n" +
    "    </div>\n" +
    "    <div ng-transclude></div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/links/readMore.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/links/readMore.html",
    "<div>\n" +
    "    <div ng-bind-html=\"textToDisplay\" ng-class=\"{'att--readMore': readFlag, 'att--readLess': !readFlag}\" ng-style=\"readLinkStyle\"></div>\n" +
    "    <span class=\"att--readmore__link\" ng-show=\"readMoreLink\">… <a href=\"javascript:void(0);\" ng-click=\"readMore()\" att-accessbility-click=\"32,13\">Read More</a>\n" +
    "    </span>\n" +
    "</div>\n" +
    "<span class=\"att--readless__link\" ng-show=\"readLessLink\">\n" +
    "    <a href=\"javascript:void(0);\" ng-click=\"readLess()\" att-accessbility-click=\"32,13\">Read Less</a>\n" +
    "</span>");
}]);

angular.module("app/scripts/ng_js_att_tpls/loading/loading.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/loading/loading.html",
    "<div data-progress=\"{{progressStatus}}\" class=\"{{colorClass}}\" ng-class=\"{'att-loading-count':icon == 'count','loading--small':icon == 'small','loading': icon != 'count'}\" alt=\"Loading\">\n" +
    "	<div class=\"att-loading-circle\" ng-if=\"icon == 'count'\">\n" +
    "		<div class=\"att-loading-circle__mask att-loading-circle__full\">\n" +
    "			<div class=\"att-loading-circle__fill\"></div>\n" +
    "		</div>\n" +
    "		<div class=\"att-loading-circle__mask att-loading-circle__half\">\n" +
    "			<div class=\"att-loading-circle__fill\"></div>\n" +
    "			<div class=\"att-loading-circle__fill att-loading-circle__fix\"></div>\n" +
    "		</div>\n" +
    "	</div>\n" +
    "	<div ng-class=\"{'att-loading-inset':icon == 'count','loading__inside':icon != 'count'}\"><div class=\"att-loading-inset__percentage\" ng-if=\"icon == 'count'\" alt=\"Loading with Count\"></div></div>\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/modal/backdrop.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/modal/backdrop.html",
    "<div class=\"overlayed\" ng-class=\"{show: animate}\" \n" +
    "     ng-style=\"{'z-index': 2000 + index*10,'overflow':'scroll'}\">         \n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/modal/tabbedItem.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/modal/tabbedItem.html",
    "<div>\n" +
    "    <ul class=\"tabs_overlay\">\n" +
    "        <li ng-repeat=\"item in items\" class=\"tabs_overlay__item two__item\" ng-class=\"{'active':isActiveTab($index)}\" ng-click=\"clickTab($index)\">\n" +
    "            <i class=\"{{item.iconClass}}\"></i>\n" +
    "            {{item.title}} ({{item.number}})\n" +
    "            <a class=\"viewLink\" att-link>Show</a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/modal/tabbedOverlayItem.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/modal/tabbedOverlayItem.html",
    "<div>\n" +
    "    <ul class=\"tabs_overlay\">\n" +
    "        <li ng-repeat=\"item in items\" class=\"tabs_overlay__item two__item\" ng-class=\"{'active':isActiveTab($index)}\" ng-click=\"clickTab($index)\">\n" +
    "            <i class=\"{{item.iconClass}}\"></i>\n" +
    "            {{item.title}} ({{item.number}})\n" +
    "            <a class=\"viewLink\" att-link>Show</a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/modal/window.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/modal/window.html",
    "<div tabindex=\"-1\" role=\"dialog\" att-element-focus=\"focusModalFlag\" class=\"modals {{ windowClass }}\" ng-class=\"{show: animate}\" \n" +
    "     ng-style=\"{'z-index': 2010 + index*10}\"  ng-click=\"close($event)\" ng-transclude>         \n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/pagination/pagination.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/pagination/pagination.html",
    "<div class=\"pager\">\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"pager__item--prev\" att-accessibility-click=\"13,32\" ng-click=\"prev($event)\" ng-if=\"currentPage > 1\"><i class=\"icon-arrow-left\"></i> Previous</a>\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"pager__item pager__item--link\" ng-if=\"totalPages > 7 && currentPage > 3\" att-accessibility-click=\"13,32\" ng-click=\"selectPage(1, $event)\">1</a>\n" +
    "    <span class=\"pager__item\" ng-if=\"totalPages > 7 && currentPage > 3\">...</span>\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"pager__item pager__item--link\" att-element-focus=\"isFocused(page)\" ng-repeat=\"page in pages\" ng-class=\"{'pager__item--active': checkSelectedPage(page)}\" att-accessibility-click=\"13,32\" ng-click=\"selectPage(page, $event)\">{{page}}</a>\n" +
    "    <span class=\"pager__item\" ng-if=\"totalPages > 7 && currentPage < totalPages - 2 && showInput !== true\">...</span>\n" +
    "    <span ng-show=\"totalPages > 7 && showInput === true\"><input class=\"pager__item--input\" type=\"text\" placeholder=\"...\" maxlength=\"2\" ng-model=\"currentPage\" aria-label=\"Current page count\"/></span>\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"pager__item pager__item--link\" ng-if=\"totalPages > 7 && currentPage < totalPages - 2\" att-accessibility-click=\"13,32\" ng-click=\"selectPage(totalPages, $event)\">{{totalPages}}</a>\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"pager__item--next\" att-accessibility-click=\"13,32\" ng-click=\"next($event)\" ng-if=\"currentPage < totalPages\">Next <i class=\"icon-arrow-right\"></i></a>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/paneSelector/innerPane.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/paneSelector/innerPane.html",
    "<div class='inner-pane' ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/paneSelector/paneGroup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/paneSelector/paneGroup.html",
    "<div class='pane-group' ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/paneSelector/sidePane.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/paneSelector/sidePane.html",
    "<div class='side-pane' ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/tooltip/tooltip-popup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tooltip/tooltip-popup.html",
    "<div class=\"att-tooltip \" \n" +
    "     ng-class=\"{ 'att-tooltip--on': isOpen, \n" +
    "                'att-tooltip--dark att-tooltip--dark--hover':stylett=='dark', \n" +
    "                'att-tooltip--light att-tooltip--light--hover':stylett=='light',\n" +
    "                'att-tooltip--left':placement=='left', \n" +
    "                'att-tooltip--above':placement=='above', \n" +
    "                'att-tooltip--right':placement=='right', \n" +
    "                'att-tooltip--below':placement=='below'}\" \n" +
    "    ng-bind-html=\"content | unsafe\" ></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/popOvers/popOvers.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/popOvers/popOvers.html",
    "<div class=\"att-popover popover-demo\" ng-style=\"{backgroundColor:popOverStyle}\"\n" +
    "     ng-class=\"{'att-tooltip--dark':popOverStyle==='grey',\n" +
    "                'att-pop-over--left':popOverPlacement==='left', \n" +
    "                'att-pop-over--above':popOverPlacement==='above', \n" +
    "                'att-pop-over--right':popOverPlacement==='right'}\" \n" +
    "    style='position: absolute; max-width: 490px;'>\n" +
    "    <div class=\"pop-over-caret\"\n" +
    "         ng-class=\"{'pop-over-caret-border--left':popOverPlacement==='left', \n" +
    "                'pop-over-caret-border--above':popOverPlacement==='above', \n" +
    "                'pop-over-caret-border--right':popOverPlacement==='right', \n" +
    "                'pop-over-caret-border--below':popOverPlacement==='below'}\">\n" +
    "    </div>\n" +
    "    <div class=\"pop-over-caret\" ng-style=\"popOverPlacement=='below' && {borderBottom:'6px solid ' +popOverStyle}||popOverPlacement=='left' && {borderLeft:'6px solid ' +popOverStyle}||popOverPlacement=='right' && {borderRight:'6px solid ' +popOverStyle}||popOverPlacement=='above' && {borderTop:'6px solid ' +popOverStyle}\"\n" +
    "         ng-class=\"{'pop-over-caret--left':popOverPlacement==='left', \n" +
    "                'pop-over-caret--above':popOverPlacement==='above', \n" +
    "                'pop-over-caret--right':popOverPlacement==='right', \n" +
    "                'pop-over-caret--below':popOverPlacement==='below'}\"></div>\n" +
    "    \n" +
    "    <div class=\"att-popover-content\">\n" +
    "	<a ng-if=\"closeable\" href=\"javascript:void(0)\" class=\"icon-close att-popover__close\" ng-click=\"closeMe();$event.preventDefault()\"></a>\n" +
    "        <div class=\"popover-packages__container\" ng-include=\"content\"></div>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/profileCard/addUser.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/profileCard/addUser.html",
    "<div  class=\"col-md-9 profile-card add-user\">\n" +
    "    <div class=\"atcenter\">\n" +
    "        <div><i class=\"icon-add\"></i></div>\n" +
    "        <span>add User</span>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/profileCard/profileCard.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/profileCard/profileCard.html",
    "<div class=\"col-md-9 profile-card\">\n" +
    "    <div class=\"top-block\">\n" +
    "       <div class=\"profile-image\">\n" +
    "            <img ng-if=\"image\" profile-name=\"{{profile.name}}\" ng-src=\"{{profile.img}}\" alt=\"{{profile.name}}\">\n" +
    "            <span ng-hide=\"image\" class=\"default-img\">{{initials}}</span>\n" +
    "            <p class=\"name\" tooltip-condition=\"{{profile.name}}\" height=\"true\"></p>\n" +
    "            <p class=\"status\">\n" +
    "                <span class=\"status-icon\" ng-class=\"{'icon-green':colorIcon==='green','icon-red':colorIcon==='red','icon-blue':colorIcon==='blue','icon-yellow':colorIcon==='yellow'}\">   \n" +
    "                </span>\n" +
    "                <span>{{profile.state}}<span ng-if=\"badge\" class=\"status-badge\">Admin</span></span>\n" +
    "            </p>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"bottom-block\">\n" +
    "         <div class=\"profile-details\">\n" +
    "            <label>Username</label>\n" +
    "            <p att-text-overflow=\"92%\" tooltip-condition=\"{{profile.userName}}\">{{profile.userName}}</p>\n" +
    "            <label>Email</label>\n" +
    "            <p att-text-overflow=\"92%\" tooltip-condition=\"{{profile.email}}\">{{profile.email}}</p>\n" +
    "            <label>Role</label>\n" +
    "            <p att-text-overflow=\"92%\" tooltip-condition=\"{{profile.role}}\">{{profile.role}}</p>\n" +
    "            <label>Last Login</label>\n" +
    "            <p att-text-overflow=\"92%\" tooltip-condition=\"{{profile.lastLogin}}\">{{profile.lastLogin}}</p>\n" +
    "         </div>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/progressBars/progressBars.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/progressBars/progressBars.html",
    "<div class=\"att-progress\">\n" +
    "    <div class=\"att-progress-value\">&nbsp;</div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/scrollbar/scrollbar.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/scrollbar/scrollbar.html",
    "<div class=\"scroll-bar\" style=\"position: absolute\">\n" +
    "    <div class=\"scroll-thumb\" style=\"position: absolute; overflow: hidden\"></div>\n" +
    "</div>\n" +
    "<div class=\"prev icons-list\" data-size=\"medium\" ng-show=\"navigation && prevAvailable\" ng-style=\"{height: scrollbarAxis === 'x' && position.height + 'px'}\">\n" +
    "    <a href=\"javascript:void(0);\" ng-click=\"customScroll(false)\" aria-label=\"Scroll\" aria-hidden=\"true\">\n" +
    "        <i ng-class=\"{'icon-chevron-up': (scrollbarAxis === 'y'), 'icon-chevron-left': (scrollbarAxis === 'x')}\"></i>\n" +
    "    </a>\n" +
    "</div>\n" +
    "<div class=\"scroll-viewport\" ng-style=\"{height: (scrollbarAxis === 'x' && position.height + 'px') || viewportHeight, width: viewportWidth}\" style=\"position: relative; overflow: hidden\">\n" +
    "    <div class=\"scroll-overview\" style=\"position: absolute; display: table; width: 100%\" att-position=\"position\" ng-transclude></div>\n" +
    "</div>\n" +
    "<div class='next icons-list' data-size=\"medium\" ng-show=\"navigation && nextAvailable\" ng-style=\"{height: scrollbarAxis === 'x' && position.height + 'px'}\">\n" +
    "    <a href=\"javascript:void(0);\" ng-click=\"customScroll(true)\" aria-label=\"Scroll\" aria-hidden=\"true\">\n" +
    "        <i ng-class=\"{'icon-chevron-down': (scrollbarAxis === 'y'), 'icon-chevron-right': (scrollbarAxis === 'x')}\"></i>\n" +
    "    </a>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/search/search.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/search/search.html",
    "<div class=\"select2-container show-search\" ng-class=\"{'select2-dropdown-open': (showlist && !isDisabled),'select2-container-disabled':isDisabled, 'select2-container-active': isact}\" ng-init=\"isact=false;\" style=\"width: 100%;\">\n" +
    "    <a href=\"javascript:void(0)\" class=\"select2-choice needsclick\" tabindex=\"0\" ng-click=\"showDropdown()\" ng-class=\"{'select2-chosen-disabled':isDisabled}\" role=\"combobox\" aria-expanded=\"{{showlist}}\" aria-owns=\"inList\" aria-label=\"{{selectedOption}} selected\" ng-focus=\"isact=true;\" ng-blur=\"isact=false;\">\n" +
    "        <span class=\"select2-chosen needsclick\" aria-hidden = \"true\">{{selectedOption}}</span>\n" +
    "        <abbr class=\"select2-search-choice-close needsclick\"></abbr>\n" +
    "        <span class=\"select2-arrow needsclick\" role=\"presentation\">\n" +
    "            <b role=\"presentation\" class=\"needsclick\"></b>\n" +
    "        </span>\n" +
    "    </a>    \n" +
    "    <input class=\"select2-focusser select2-offscreen\"            \n" +
    "           tabindex=\"-1\" \n" +
    "           type=\"text\" \n" +
    "           aria-hidden=\"true\" \n" +
    "           title=\"hidden\"           \n" +
    "           aria-haspopup=\"true\"           \n" +
    "           role=\"button\">    \n" +
    "</div>\n" +
    "\n" +
    "<div class=\"select2-drop select2-with-searchbox select2-drop-auto-width select2-drop-active\" ng-class=\"{'select2-display-none':(!showlist || isDisabled)}\" style=\"width:100%;z-index: 10\">\n" +
    "    <div class=\"select2-search\">\n" +
    "        <label ng-if=\"!noFilter\" class=\"select2-offscreen\" aria-label=\"Inline Search Field\" aria-hidden=\"true\">Inline Search Field</label>\n" +
    "        <input ng-if=\"!noFilter\" ng-model=\"title\" aria-label=\"title\" typeahead=\"c.title for c in cName | filter:$viewValue:startsWith\" type=\"text\" autocomplete=\"off\" autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" class=\"select2-input\" aria-autocomplete=\"list\" placeholder=\"\">\n" +
    "    </div>\n" +
    "    <ul id=\"inList\" class=\"select2-results\" role=\"listbox\">\n" +
    "        <li ng-show=\"filteredName.length === 0\" class=\"select2-no-results\" tabindex=\"-1\">No matches found</li>\n" +
    "        <li class=\"select2-results-dept-0 select2-result select2-result-selectable\" role=\"presentation\" ng-model=\"ListType\" ng-show=\"selectMsg && filteredName.length > 0\" ng-click=\"selectOption(selectMsg, '-1')\" ng-class=\"{'select2-result-current': selectedOption === selectMsg, 'hovstyle': selectedIndex === -1}\" ng-mouseover=\"hoverIn(-1)\" aria-label=\"{{selectMsg}}\" tabindex=\"-1\">\n" +
    "            <div ng-if=\"startsWithFilter\" class=\"select2-result-label\" ng-bind-html=\"selectMsg | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "            <div ng-if=\"!startsWithFilter\" class=\"select2-result-label\" ng-bind-html=\"selectMsg | highlight:title:className | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "\n" +
    "        <li role=\"menuitem\" ng-if=\"startsWithFilter\" class=\"select2-results-dept-0 select2-result select2-result-selectable\" role=\"presentation\" ng-model=\"ListType\" ng-repeat=\"(fIndex, item) in filteredName = (cName | startsWith:title:item)\" ng-class=\"{'select2-result-current': selectedOption === item.title,'hovstyle': selectedIndex === item.index,'disable': item.disabled}\" ng-click=\"item.disabled || selectOption(item.title,item.index)\" ng-mouseover=\"hoverIn(item.index)\" aria-label=\"{{item.title}}\" tabindex=\"-1\">\n" +
    "            <div class=\"select2-result-label\" ng-bind-html=\"item.title | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "\n" +
    "        <li role=\"menuitem\" ng-if=\"!startsWithFilter\" class=\"select2-results-dept-0 select2-result select2-result-selectable\" role=\"presentation\" ng-model=\"ListType\" ng-repeat=\"(fIndex, item) in filteredName = (cName | filter:title)\" ng-class=\"{'select2-result-current': selectedOption === item.title,'hovstyle': selectedIndex === item.index,'disable': item.disabled}\" ng-click=\"item.disabled || selectOption(item.title,item.index)\" ng-mouseover=\"hoverIn(item.index)\" aria-label=\"{{item.title}}\" tabindex=\"-1\">\n" +
    "            <div class=\"select2-result-label\" ng-bind-html=\"item.title | highlight:title:className | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/select/select.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/select/select.html",
    "<div class=\"select2-container show-search\" ng-class=\"{'select2-dropdown-open': (showlist && !isDisabled),'select2-container-disabled': isDisabled, 'select2-container-active': isact}\" ng-init=\"isact=false;\">\n" +
    "    <span class=\"select2-choice needsclick\" tabindex=\"{{isDisabled ? -1 : 0}}\" ng-click=\"showDropdown()\" ng-class=\"{'select2-chosen-disabled':isDisabled}\" role=\"combobox\" aria-expanded=\"{{showlist}}\" aria-owns=\"inList\" aria-label=\"{{titleName}} dropdown {{selectedOption}} selected\" ng-focus=\"isact=true;\" ng-blur=\"isact=false;\">\n" +
    "        <span class=\"select2-chosen needsclick\" aria-hidden=\"true\" ng-bind-html=\"selectedOption | unsafe\">{{selectedOption}}</span>\n" +
    "        <abbr class=\"select2-search-choice-close needsclick\"></abbr>\n" +
    "        <span class=\"select2-arrow needsclick\" role=\"presentation\">\n" +
    "            <b role=\"presentation\" class=\"needsclick\"></b>\n" +
    "        </span>\n" +
    "    </span>    \n" +
    "    <input class=\"select2-focusser select2-offscreen\"            \n" +
    "           tabindex=\"-1\" \n" +
    "           type=\"text\" \n" +
    "           aria-hidden=\"true\" \n" +
    "           title=\"hidden\"           \n" +
    "           aria-haspopup=\"true\"           \n" +
    "           role=\"button\">    \n" +
    "</div>\n" +
    "\n" +
    "<div class=\"select2-drop select2-with-searchbox select2-drop-auto-width select2-drop-active\" ng-class=\"{'select2-display-none':(!showlist || isDisabled)}\" style=\"width:100%;z-index: 10\">\n" +
    "    <div class=\"select2-search\">\n" +
    "        <label ng-if=\"!noFilter\" class=\"select2-offscreen\" aria-label=\"Inline Search Field\" aria-hidden=\"true\">Inline Search Field</label>\n" +
    "        <input ng-if=\"!noFilter\" ng-model=\"title\" aria-label=\"title\" typeahead=\"c.title for c in cName | filter:$viewValue:startsWith\" type=\"text\" autocomplete=\"off\" autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" class=\"select2-input\" aria-autocomplete=\"list\" placeholder=\"\">\n" +
    "    </div>\n" +
    "    <ul id=\"inList\" class=\"select2-results\" role=\"listbox\">\n" +
    "        <li ng-if=\"!noFilter\" ng-show=\"filteredName.length === 0\" class=\"select2-no-results\" tabindex=\"-1\">No matches found</li>\n" +
    "        <li ng-if=\"!noFilter\" class=\"select2-results-dept-0 select2-result select2-result-selectable\" role=\"presentation\" ng-model=\"ListType\" ng-show=\"selectMsg && filteredName.length > 0\" ng-click=\"selectOption(selectMsg, '-1')\" ng-class=\"{'select2-result-current': selectedOption === selectMsg, 'hovstyle': selectedIndex === -1}\" ng-mouseover=\"hoverIn(-1)\" aria-label=\"{{selectMsg}}\" tabindex=\"-1\">\n" +
    "            <div ng-if=\"startsWithFilter\" class=\"select2-result-label\" ng-bind-html=\"selectMsg | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "            <div ng-if=\"!startsWithFilter\" class=\"select2-result-label\" ng-bind-html=\"selectMsg | highlight:title:className | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "\n" +
    "        <li  role=\"menuitem\"  ng-if=\"startsWithFilter\" class=\"select2-results-dept-0 select2-result select2-result-selectable\" ng-model=\"ListType\" ng-repeat=\"(fIndex, item) in filteredName = (cName | startsWith:title:item)\" ng-class=\"{'select2-result-current': selectedOption === item.title,'hovstyle': selectedIndex === item.index,'disable': item.disabled}\" ng-click=\"item.disabled || selectOption(item.title,item.index)\" ng-mouseover=\"hoverIn(item.index)\"  tabindex=\"-1\">\n" +
    "            <div class=\"select2-result-label\" ng-bind-html=\"item.title | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "\n" +
    "        <li role=\"menuitem\" ng-if=\"!startsWithFilter\" class=\"select2-results-dept-0 select2-result select2-result-selectable\" ng-model=\"ListType\" ng-repeat=\"(fIndex, item) in filteredName = (cName | filter:title)\" ng-class=\"{'select2-result-current': selectedOption === item.title,'hovstyle': selectedIndex === item.index,'disable': item.disabled}\" ng-click=\"item.disabled || selectOption(item.title,item.index)\" ng-mouseover=\"hoverIn(item.index)\"  tabindex=\"-1\">\n" +
    "            <div class=\"select2-result-label\" ng-bind-html=\"item.title | highlight:title:className | unsafe\" role=\"option\">\n" +
    "                <span class=\"select2-match\"></span>\n" +
    "            </div>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/select/textDropdown.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/select/textDropdown.html",
    "<div tabindex=\"0\" class=\"text-dropdown\">\n" +
    "	<div class=\"dropdown\" ng-class=\"{'not-visible': isActionsShown}\" ng-click=\"toggle()\">\n" +
    "		<span class=\"action--selected\" ng-bind=\"currentAction\"></span>\n" +
    "		<i ng-class=\"isActionsShown ? 'icon-dropdown-up' : 'icon-dropdown-down'\"></i>\n" +
    "	</div>\n" +
    "	<ul ng-class=\"isActionsShown ? 'actionsOpened' : 'actionsClosed'\" ng-show=\"isActionsShown\">\n" +
    "		<li ng-class=\"{'highlight': selectedIndex==$index}\" ng-repeat=\"action in actions track by $index\" ng-click=\"chooseAction($event, action, $index)\" ng-mouseover=\"hoverIn($index)\">{{action}}<i ng-class=\"{'icon-included-checkmark': isCurrentAction(action)}\" att-accessibility-click=\"13,32\"></i></li>\n" +
    "	</ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/slider/maxContent.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/slider/maxContent.html",
    "<div class=\"att-slider__label att-slider__label--max att-slider__label--below\" ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/slider/minContent.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/slider/minContent.html",
    "<div class=\"att-slider__label att-slider__label--min att-slider__label--below\" ng-transclude></div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/slider/slider.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/slider/slider.html",
    "<div class=\"att-slider\" ng-mousemove=\"moveElem($event)\" ng-mouseup=\"mouseUp($event)\">\n" +
    "    <div class=\"att-slider__track\">\n" +
    "        <div class=\"att-slider__range att-slider__range--disabled\" ng-style=\"disabledStyle\"></div>\n" +
    "        <div class=\"att-slider__range\" ng-style=\"rangeStyle\"></div>\n" +
    "    </div>\n" +
    "    <div class=\"att-slider__handles-container\">\n" +
    "       <div role=\"menuitem\" aria-label=\"{{ngModelSingle}}\" class=\"att-slider__handle\" ng-style=\"handleStyle\" ng-mousedown=\"mouseDown($event,'ngModelSingle')\" ng-mousemove=\"moveElem($event)\" ng-mouseup=\"mouseUp($event)\" tabindex=\"0\" ng-keydown=\"keyDown($event,'ngModelSingle')\"></div>\n" +
    "       <div role=\"menuitem\" aria-label=\"Minimum Value- {{ngModelLow}}\" class=\"att-slider__handle\" ng-style=\"minHandleStyle\" ng-mousedown=\"mouseDown($event,'ngModelLow')\" ng-focus=\"focus($event,'ngModelLow')\" ng-mousemove=\"moveElem($event)\" ng-mouseup=\"mouseUp($event)\" tabindex=\"0\" ng-keyup=\"keyUp($event,'ngModelLow')\" ng-keydown=\"keyDown($event,'ngModelLow')\"></div>\n" +
    "       <div role=\"menuitem\" aria-label=\"Maximum Value- {{ngModelHigh}}\" class=\"att-slider__handle\" ng-style=\"maxHandleStyle\" ng-mousedown=\"mouseDown($event,'ngModelHigh')\" ng-focus=\"focus($event,'ngModelHigh')\" ng-mousemove=\"moveElem($event)\" ng-mouseup=\"mouseUp($event)\" tabindex=\"0\" ng-keyup=\"keyUp($event,'ngModelHigh')\" ng-keydown=\"keyDown($event,'ngModelHigh')\"></div>\n" +
    "    </div>\n" +
    "    <div ng-transclude></div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdown.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdown.html",
    "<div class=\" btn-group\" \n" +
    "     ng-class=\"{'buttons-dropdown--large':!isSmall, \n" +
    "                 'buttons-dropdown--small':isSmall, \n" +
    "                 'action-dropdown':(isActionDropdown), \n" +
    "                 'open':isDropDownOpen}\">\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" class=\"button btn buttons-dropdown__split\" \n" +
    "       ng-class=\"{'button--primary':(btnType==undefined || btnType=='primary'), \n" +
    "                   'button--secondary':btnType=='secondary', \n" +
    "                   'button--disabled':btnType=='disabled', \n" +
    "                   'button--small':isSmall}\" \n" +
    "       ng-if=\"!isActionDropdown\"\n" +
    "       ng-click=\"btnType==='disabled'?undefined:clickFxn()\" att-accessibility-click=\"13,32\">{{btnText}}</a>\n" +
    "    <a tabindex=\"0\" href=\"javascript:void(0)\" role=\"button\" aria-label=\"Toggle Dropdown\" aria-haspopup=\"true\" class=\"button buttons-dropdown__drop dropdown-toggle\" \n" +
    "       ng-class=\"{'button--primary':(btnType==undefined || btnType=='primary'), \n" +
    "               'button--secondary':btnType=='secondary', \n" +
    "               'button--disabled':btnType=='disabled', \n" +
    "               'button--small':isSmall}\" ng-click=\"toggleDropdown()\" att-accessibility-click=\"13,32\">{{toggleTitle}} </a>\n" +
    "    <ul class=\"dropdown-menu\" ng-class=\"{'align-right':multiselect ===true}\" aria-expanded=\"{{isDropDownOpen}}\" ng-click=\"hideDropdown()\" role=\"menu\" ng-transclude></ul>\n" +
    "</div>  ");
}]);

angular.module("app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdownItem.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/splitButtonDropdown/splitButtonDropdownItem.html",
    "<li role=\"menuitem\" att-element-focus=\"sFlag\" tabindex=\"0\" ng-transclude></li>");
}]);

angular.module("app/scripts/ng_js_att_tpls/splitIconButton/splitIcon.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/splitIconButton/splitIcon.html",
    "<div class='split-icon-button-container'>\n" +
    "\n" +
    "	<div class='split-icon-button' ng-class=\"{'icon-with-chevron': isRight && !isMiddle && !isLeftNextDropdown && !isNextToDropDown, 'split-icon-button-middle':isMiddle, 'split-icon-button-right':isRight, 'split-icon-button-left':isLeft, 'split-icon-button-left-dropdown': isLeftNextDropdown ,'split-icon-button-next-dropdown': isNextToDropDown,'split-icon-button-dropdown': isDropDownOpen,'split-icon-button-hover':isIconHovered || isDropDownOpen}\" ng-mouseover='isIconHovered = true;' ng-mouseleave='isIconHovered = false;' tabindex=\"-1\" att-accessibility-click=\"13,32\" ng-click='dropDownClicked();'>\n" +
    "	        <a class='{{icon}}' title='{{iconTitle}}' tabindex=\"0\"></a>\n" +
    "	        <i ng-if=\"isRight && !isMiddle && !isLeftNextDropdown && !isNextToDropDown\" \n" +
    "	        	ng-class=\"isDropDownOpen ? 'icon-dropdown-up' : 'icon-dropdown-down'\"> </i>\n" +
    "	</div> \n" +
    "\n" +
    "	 <ul ng-if='isDropdown' class='dropdown-menu {{dropDownId}}' ng-show='\n" +
    "	 isDropDownOpen' ng-click='toggleDropdown(false)' role=\"presentation\" ng-transclude>\n" +
    "	 </ul>\n" +
    "\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/splitIconButton/splitIconButton.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/splitIconButton/splitIconButton.html",
    "<div>\n" +
    "	<div ng-if='isLeftLineShown' dir-type='{{iconStateConstants.DIR_TYPE.LEFT}}' expandable-line></div>\n" +
    "	<div ng-click='clickHandler()' att-split-icon icon='{{icon}}' title='{{title}}' dir-type='{{iconStateConstants.DIR_TYPE.BUTTON}}' hover-watch='isHovered' drop-down-watch='dropDownWatch' drop-down-id='{{dropDownId}}'>\n" +
    "		<div ng-transclude>\n" +
    "		</div>\n" +
    "	</div>\n" +
    "	<div ng-if='isRightLineShown' dir-type='{{iconStateConstants.DIR_TYPE.RIGHT}}' expandable-line></div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/splitIconButton/splitIconButtonGroup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/splitIconButton/splitIconButtonGroup.html",
    "<div ng-transclude>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/stepSlider/attStepSlider.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/stepSlider/attStepSlider.html",
    "<span ng-class=\"mainSliderClass\">\n" +
    "    <table>\n" +
    "        <tr>\n" +
    "            <td>\n" +
    "                <div class=\"jslider-bg\">\n" +
    "                    <i class=\"l\"></i>\n" +
    "                    <i class=\"r\"></i>\n" +
    "                    <i class=\"v\" ng-class=\"{'step-slider-green':sliderColor == COLORS.GREEN, 'step-slider-blue': sliderColor == COLORS.BLUE_HIGHLIGHT, 'step-slider-magenta': sliderColor == COLORS.MAGENTA, 'step-slider-gold': sliderColor == COLORS.GOLD, 'step-slider-purple': sliderColor == COLORS.PURPLE, 'step-slider-dark-blue': sliderColor == COLORS.DARK_BLUE, 'step-slider-regular': sliderColor == COLORS.REGULAR, 'step-slider-white': sliderColor == COLORS.WHITE, 'cutoff-slider': isCutOffSlider}\"></i>\n" +
    "                </div>\n" +
    "                <div class=\"jslider-pointer\" id=\"left-pointer\"></div>\n" +
    "                <div class=\"jslider-pointer jslider-pointer-to\" ng-class=\"{'step-slider-green':sliderColor == COLORS.GREEN, 'step-slider-blue': sliderColor == COLORS.BLUE_HIGHLIGHT, 'step-slider-magenta': sliderColor == COLORS.MAGENTA, 'step-slider-gold': sliderColor == COLORS.GOLD, 'step-slider-purple': sliderColor == COLORS.PURPLE, 'step-slider-dark-blue': sliderColor == COLORS.DARK_BLUE, 'step-slider-regular': sliderColor == COLORS.REGULAR, 'step-slider-white':sliderColor == COLORS.WHITE ,'cutoff-slider': isCutOffSlider}\"></div>\n" +
    "                <div class=\"jslider-label\"><span ng-bind=\"from\"></span><span ng-bind=\"options.dimension\"></span></div>\n" +
    "                <div class=\"jslider-label jslider-label-to\"><span ng-bind=\"toStr\"></span><span ng-bind=\"endDimension\"></span></div>\n" +
    "                <div class=\"jslider-value\" id=\"jslider-value-left\"><span></span>{{options.dimension}}</div>\n" +
    "                <div class=\"jslider-value jslider-value-to\"><span></span>{{toolTipDimension}}</div>\n" +
    "                <div class=\"jslider-scale\" ng-class=\"{'show-dividers': showDividers, 'cutoff-slider-dividers':isCutOffSlider}\">\n" +
    "                </div>\n" +
    "                <div class=\"jslider-cutoff\">\n" +
    "                    <div class=\"jslider-label jslider-label-cutoff\">\n" +
    "                        <span ng-bind=\"cutOffVal\"></span>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "    </table>\n" +
    "</span>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/steptracker/step-tracker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/steptracker/step-tracker.html",
    "<div class=\"steptracker1\">\n" +
    "    <div class=\"steptracker-bg\">\n" +
    "        <div tabindex=\"0\" ng-click=\"stepclick($event, $index);\" att-accessibility-click=\"13,23\" class=\"steptracker-track size-onethird\" ng-repeat=\"step in sdata\"\n" +
    "             ng-style=\"set_width($index)\"\n" +
    "             ng-class=\"{'last':laststep($index),'done':donesteps($index),'active':activestep($index), 'incomplete': isIncomplete($index), 'disabled': disableClick}\">\n" +
    "            <div class=\"circle\">{{($index) + 1}}<span>{{step.title}}</span></div>\n" +
    "            <div ng-if=\"!laststep($index)\" class=\"track\"></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/steptracker/step.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/steptracker/step.html",
    "<div class=\"steptracker1\">\n" +
    "    <div class=\"steptracker-bg\">\n" +
    "        <div class=\"steptracker-track size-onethird\" \n" +
    "             ng-class=\"{'last':laststep($index),'done':donesteps($index),'active':activestep($index)}\">\n" +
    "            <div class=\"circle\" tabindex=\"0\" \n" +
    "                 ng-click=\"stepclick($event, $index);\" \n" +
    "                 att-accessibility-click=\"13,23\">{{($index) + 1}}<span>{{step.title}}</span></div>\n" +
    "            <div ng-if=\"!laststep($index)\" class=\"track\"></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/steptracker/timeline.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/steptracker/timeline.html",
    "<div class='att-timeline'>\n" +
    "	<div timeline-dot order='0' title='{{steps[0].title}}' description='{{steps[0].description}}' by='{{steps[0].by}}' date='{{steps[0].date}}' type='{{steps[0].type}}'></div>\n" +
    "\n" +
    "	<div ng-repeat=\"m in middleSteps track by $index\">\n" +
    "		<div timeline-bar order='{{$index}}'></div>\n" +
    "		<div timeline-dot order='{{$index + 1}}' title='{{m.title}}' description='{{m.description}}' by='{{m.by}}' date='{{m.date}}' type='{{m.type}}'>\n" +
    "		</div>\n" +
    "	</div>\n" +
    "\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/steptracker/timelineBar.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/steptracker/timelineBar.html",
    "<div class='timeline-bar'>\n" +
    "	<div class='progress-bar' ng-class=\"{'completed-color':isCompleted,'cancelled-color':isCancelled,'alert-color':isAlert}\">\n" +
    "	</div>\n" +
    "	<hr></hr>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/steptracker/timelineDot.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/steptracker/timelineDot.html",
    "<div class='timeline-dot'>\n" +
    "\n" +
    "	<div class='bigger-circle' ng-class=\"{'completed-color':isCompleted,'cancelled-color':isCancelled,'alert-color':isAlert}\">\n" +
    "	</div>\n" +
    "\n" +
    "	<div class='inactive-circle'>\n" +
    "	</div>\n" +
    "\n" +
    "	<div class='expandable-circle' ng-class=\"{'completed-color':isCompleted,'cancelled-color':isCancelled,'alert-color':isAlert}\">\n" +
    "	</div>\n" +
    "\n" +
    "	<div ng-class=\"{'below-info-box':isBelowInfoBoxShown, 'above-info-box': !isBelowInfoBoxShown}\" tabindex=\"0\">\n" +
    "		\n" +
    "		<div ng-if='isBelowInfoBoxShown' class='vertical-line'>\n" +
    "		</div>\n" +
    "\n" +
    "		<div class='info-container' ng-init='isContentShown=false'>\n" +
    "			<div ng-class=\"{'current-step-title':isCurrentStep, 'title':!isCurrentStep,'completed-color-text':isCompleted,'cancelled-color-text':isCancelled,'alert-color-text':isAlert, 'inactive-color-text':isInactive}\" ng-mouseover='titleMouseover(1)' ng-mouseleave='titleMouseleave()' ng-bind='title' ></div>\n" +
    "			<div class='content'>\n" +
    "				<div class='description' ng-bind='description'></div>\n" +
    "				<div class='submitter' ng-bind='by'></div>\n" +
    "			</div>\n" +
    "			<div class='date' ng-mouseover='titleMouseover(2)' ng-mouseleave='titleMouseleave()' ng-bind='date'></div>\n" +
    "		</div>\n" +
    "\n" +
    "		<div ng-if='!isBelowInfoBoxShown' class='vertical-line'>\n" +
    "		</div>\n" +
    "	</div>\n" +
    "\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/table/attTable.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/table/attTable.html",
    "<table class=\"tablesorter tablesorter-default\" ng-transclude></table>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/table/attTableBody.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/table/attTableBody.html",
    "<td ng-transclude></td>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/table/attTableHeader.html", []).run(["$templateCache", function($templateCache) { 
  $templateCache.put("app/scripts/ng_js_att_tpls/table/attTableHeader.html", 
    "<th role=\"columnheader\" scope=\"col\" aria-live=\"polite\" aria-sort=\"{{sortPattern !== 'null' && '' || sortPattern}}\" aria-label=\"{{headerName}} {{sortable !== 'false' && ': activate to sort' || ' '}} {{sortPattern !== 'null' && '' || sortPattern}}\" tabindex=\"{{sortable !== 'false' && '0' || '-1'}}\" class=\"tablesorter-header\" ng-class=\"{'tablesorter-headerAsc': sortPattern === 'ascending', 'tablesorter-headerDesc': sortPattern === 'descending', 'tablesort-sortable': sortable !== 'false', 'sorter-false': sortable === 'false'}\" att-accessibility-click=\"13,32\" ng-click=\"(sortable !== 'false') && sort();\"><div class=\"tablesorter-header-inner\" ng-transclude></div></th>"); 
}]);

angular.module("app/scripts/ng_js_att_tpls/tableMessages/attTableMessage.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tableMessages/attTableMessage.html",
    "<div class=\"att-table-message\">\n" +
    "   <div class=\"message\" ng-if=\"msgType==messageConstants.TABLE_MESSAGE_TYPES.noMatching\">\n" +
    "      <div class=\"img-magnify-glass\"></div> \n" +
    "      <div>\n" +
    "         <div ng-transclude></div>\n" +
    "      </div>\n" +
    "   </div>\n" +
    "   <div class=\"message\" ng-if=\"msgType==messageConstants.TABLE_MESSAGE_TYPES.errorLoading\">\n" +
    "      <div class=\"img-oops-exclamation\" tabindex=\"0\" aria-label=\"Oops! The information could not load at this time. Please click link to refresh the page.\"></div> \n" +
    "      <div>Oops!</div>\n" +
    "      <div>The information could not load at this time.</div>\n" +
    "      <div>Please <a href=\"javascript:void(0)\" ng-click=\"refreshAction($event)\">refresh the page</a>\n" +
    "      </div>\n" +
    "   </div>\n" +
    "   <div class=\"message\" ng-if=\"msgType==messageConstants.TABLE_MESSAGE_TYPES.magnifySearch\">\n" +
    "      <div class=\"img-magnify-glass\"></div>\n" +
    "      <div>\n" +
    "         <p class=\"title\" tabindex=\"0\">Please input values to <br/> begin your search.</p>\n" +
    "      </div>\n" +
    "   </div>\n" +
    "   <div class=\"message loading-message\" ng-if=\"msgType==messageConstants.TABLE_MESSAGE_TYPES.isLoading\">\n" +
    "      <div class=\"img-loading-dots\"></div>\n" +
    "      <div ng-transclude></div>\n" +
    "   </div>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/tableMessages/attUserMessage.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tableMessages/attUserMessage.html",
    "<div class=\"att-user-message\">\n" +
    "  <div ng-class=\"type==messageConstants.USER_MESSAGE_TYPES.error && trigger ? 'message-wrapper-error' : 'hidden'\">\n" +
    "      <div class=\"message-icon-error\"> <i class=\"icon-info-alert\"></i> </div>\n" +
    "\n" +
    "      <div class=\"message-body-wrapper\">\n" +
    "        <div class=\"message-title-error\" ng-if=\"thetitle && thetitle.length > 0\"> <span ng-bind=\"thetitle\" tabindex=\"0\" aria-label=\"{{thetitle}}\"></span> </div>\n" +
    "        <div class=\"message-msg\" ng-bind=\"message\" ng-if=\"message && message.length > 0\" tabindex=\"0\"></div>\n" +
    "        <div class=\"message-bottom\">\n" +
    "           <div ng-transclude></div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "   </div>\n" +
    "  <div ng-class=\"type==messageConstants.USER_MESSAGE_TYPES.success && trigger ? 'message-wrapper-success' : 'hidden'\">\n" +
    "      <div class=\"message-icon-success\"> <i class=\"icon-included-checkmark\"></i></div>\n" +
    "\n" +
    "      <div class=\"message-body-wrapper\">\n" +
    "        <div class=\"message-title-success\" ng-if=\"thetitle && thetitle.length > 0\" >\n" +
    "          <span ng-bind=\"thetitle\" tabindex=\"0\" aria-label=\"{{thetitle}}\"></span>\n" +
    "        </div>\n" +
    "        <div class=\"message-msg\" ng-bind=\"message\" ng-if=\"message && message.length > 0\" tabindex=\"0\"></div>\n" +
    "        <div class=\"message-bottom\">\n" +
    "           <div ng-transclude></div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\n" +
    "   </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/floatingTabs.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/floatingTabs.html",
    "<ul ng-class=\"{'tabsbid': size === 'large', 'tabsbid--small': size === 'small'}\">\n" +
    " <li ng-repeat=\"tab in tabs\" ng-class=\"{'tabsbid__item tabsbid__item--active': isActiveTab(tab.url), 'tabsbid__item': !isActiveTab(tab.url)}\" ng-click=\"onClickTab(tab)\">\n" +
    "        <a class=\"tabsbid__item-link\" href=\"{{tab.url}}\" tabindex=\"0\" att-accessibility-click=\"32,13\">{{tab.title}}</a>\n" +
    "  </li>\n" +
    "</ul>");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/genericTabs.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/genericTabs.html",
    "<ul ng-class=\"{'tabsbid': size === 'large', 'tabsbid--small': size === 'small'}\">\n" +
    "    <li ng-repeat=\"tab in tabs\" ng-class=\"{'tabsbid__item tabsbid__item--active': isActive(tab.id), 'tabsbid__item': !isActive(tab.id),'tabs__item--active': isActive(tab.id)}\" ng-click=\"clickTab(tab)\">\n" +
    "		<a class=\"tabsbid__item-link\" href=\"{{tab.url}}\" tabindex=\"0\" att-accessibility-click=\"32,13\">{{tab.title}}</a>\n" +
    "    </li>\n" +
    "</ul>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/menuTab.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/menuTab.html",
    "<li class=\"megamenu__item\" ng-mouseover=\"showHoverChild($event)\" ng-class=\"{'tabs__item--active': menuItem.active==true && !hoverChild==true}\">\n" +
    "    <span role=\"menuitem\" att-accessibility-click=\"13,32\" tabindex=\"0\" ng-click=\"showChildren($event);!clickInactive||resetMenu($event)\">{{tabName}}</span>\n" +
    "    <div ng-transclude></div>\n" +
    "</li>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/parentmenuTab.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/parentmenuTab.html",
    "<div ng-class=\"{'megamenu-tabs': megaMenu,'submenu-tabs': !megaMenu}\">\n" +
    "    <ul class=\"megamenu__items\" role=\"presentation\" ng-transclude>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/simplifiedTabs.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/simplifiedTabs.html",
    "<div class=\"simplified-tabs\">\n" +
    "<ul class=\"simplified-tabs__items\" role=\"tablist\">\n" +
    "    <li ng-repeat=\"tab in tabs\" role=\"tab\" class=\"simplified-tabs__item\" ng-class=\"{'tabs__item--active': isActive(tab.id)}\" ng-click=\"clickTab(tab)\" tabindex=\"0\" att-accessibility-click=\"32,13\">{{tab.title}}</li>\n" +
    "    <li class=\"tabs__pointer\"></li>\n" +
    "</ul>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/tabs/submenuTab.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tabs/submenuTab.html",
    "<li class=\"tabsbid__item megamenu__item\" ng-class=\"{'subMenuHover': menuItem.active==true}\">\n" +
    "<a ng-href=\"{{tabUrl}}\" role=\"menuitem\" ng-if=\"subMenu === true\" ng-mouseover=\"!subMenu || showChildren($event)\" ng-focus=\"!subMenu ||showChildren($event)\" tabindex=\"{{subMenu=='true'?0:-1}}\" ng-click=\"!subMenu ||showMenuClick($event) ; subMenu ||showSubMenuClick($event)\" att-accessibility-click=\"13,32\">{{tabName}}</a>\n" +
    "<a  ng-href=\"{{tabUrl}}\" role=\"menuitem\" ng-if=\"!menuItem.leafNode && subMenu !== true\" ng-mouseover=\"!subMenu || showChildren($event)\" ng-focus=\"!subMenu ||showChildren($event)\" tabindex=\"{{subMenu=='true'?0:-1}}\" ng-click=\"!subMenu ||showMenuClick($event) ; subMenu ||showSubMenuClick($event)\" att-accessibility-click=\"13,32\">{{tabName}}</a>\n" +
    "<span ng-transclude></span>\n" +
    "</li>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/tagBadges/tagBadges.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/tagBadges/tagBadges.html",
    "<div class=\"tags__item\" \n" +
    "     ng-class=\"{'tags__item--small':isSmall, \n" +
    "                 'tags__item--color':isColor, \n" +
    "                 'tags__item--cloud':!isClosable && !isColor,'active':applyActiveClass}\"\n" +
    "     ng-if=\"display\" \n" +
    "     ng-style=\"{borderColor: border_type_borderColor, background: isHighlight?'#bbb':undefined, color: isHighlight?'#444':undefined }\"\n" +
    "     ng-mousedown=\"activeHighlight(true)\" role=\"presentation\" ng-mouseup=\"activeHighlight(false)\">\n" +
    "    <i class=\"icon-filter tags__item--icon\" ng-if=\"isIcon\">&nbsp;</i>\n" +
    "    <i class=\"tags__item--color-icon\" ng-if=\"isColor\" ng-style=\"{backgroundColor: background_type_backgroundColor, borderColor: background_type_borderColor}\"></i>\n" +
    "    <span class=\"tags__item--title\" role=\"presentation\"  tabindex=0  ng-mousedown=\"activeHighlight(true)\" ng-mouseup=\"activeHighlight(false)\" ng-transclude></span>\n" +
    "    <a href=\"javascript:void(0)\" title=\"Dismiss Link\" class=\"tags__item--action\" ng-click=\"closeMe();$event.preventDefault()\" ng-if=\"isClosable\"\n" +
    "       ng-style=\"{color: (isHighlight && '#444') || '#888' , borderLeft: (isHighlight && '1px solid #444')|| '1px solid #888' }\">\n" +
    "        <i class=\"icon-erase\">&nbsp;</i>\n" +
    "    </a>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/toggle/demoToggle.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/toggle/demoToggle.html",
    "<span ng-transclude></span>\n" +
    "<div class=\"att-switch-content\" hm-drag = \"drag($event)\" att-accessibility-click=\"13,32\" ng-click=\"updateModel($event)\" hm-dragstart=\"alert('hello')\" hm-dragend=\"drag($event)\" ng-class=\"{'large' : directiveValue == 'large'}\"  style=\"-webkit-user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);\">\n" +
    "    <div class=\"att-switch-onText\" ng-style=\"\" ng-class=\"{'icon-included-checkmark ico' : on === undefined,'large' : directiveValue == 'large'}\">{{on}}<span class=\"hidden-spoken\">{{directiveValue}} when checked.</span></div>\n" +
    "    <div class=\"att-switch-thumb\" tabindex=\"0\" title=\"Toggle switch\" role=\"checkbox\"  ng-class=\"{'large' : directiveValue == 'large'}\"></div>\n" +
    "    <div class=\"att-switch-offText\" ng-class=\"{'icon-erase ico' : on === undefined,'large' : directiveValue == 'large'}\">{{off}}<span class=\"hidden-spoken\">{{directiveValue}} when unchecked.</span></div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/typeAhead/typeAhead.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/typeAhead/typeAhead.html",
    "<div class=\"typeahead mainContainerOuter\">\n" +
    "    <span class=\"message\">To</span>\n" +
    "    <div class='maincontainer' ng-click=\"setFocus()\" ng-focus=\"inputActive=true\" ng-class =\"{'typeahed_active':inputActive || (lineItems.length && inputActive)}\">\n" +
    "        <span tag-badges closable ng-repeat =\"lineItem in lineItems track by $index\" on-close=\"theMethodToBeCalled($index)\" >{{lineItem}}</span>\n" +
    "        <input type=\"text\"  focus-me=\"clickFocus\" ng-focus=\"inputActive=true\" ng-model=\"model\" ng-keydown=\"selected = false; selectionIndex($event)\"/><br/> \n" +
    "    </div>\n" +
    "    <div ng-hide=\"!model.length || selected\">\n" +
    "        <div class=\"filtercontainer list-scrollable\" ng-show=\"( items | filter:model).length\">\n" +
    "            <div  class=\"item\" ng-repeat=\"item in items| filter:model track by $index\"  ng-click=\"handleSelection(item[titleName],item[subtitle])\" att-accessibility-click=\"13,32\" ng-class=\"{active:isCurrent($index,item[titleName],item[subtitle],( items | filter:model).length)}\"ng-mouseenter=\"setCurrent($index)\">\n" +
    "                <span class=\"title\" >{{item[titleName]}}</span>\n" +
    "                <span class=\"subtitle\">{{item[subtitle]}}</span>\n" +
    "            </div>  \n" +
    "        </div>\n" +
    "    </div>\n" +
    "   \n" +
    "    <div class=\"textAreaEmailContentDiv\">\n" +
    "        <span class=\"message\">Message</span>\n" +
    "        <textarea rows=\"4\" cols=\"50\" role=\"textarea\" class=\"textAreaEmailContent\" ng-model=\"emailMessage\">To send \n" +
    " a text, picture, or video message1 to an AT&T wireless device from your email:my message.</textarea>\n" +
    "        \n" +
    "    </div>\n" +
    "    \n" +
    "</div>\n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/verticalSteptracker/vertical-step-tracker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/verticalSteptracker/vertical-step-tracker.html",
    "<li>\n" +
    "    <i ng-class=\"{'icon-tickets-active' : type == 'actual' && id =='Active','icon-tickets-referred' : type == 'actual' && id =='Requested Closed','icon-ticket-regular' : type == 'progress' && id =='In Progress','icon-tickets-contested' : type == 'actual' && id =='Contested','icon-tickets-returned' : type == 'actual' && id =='Deferred','icon-tickets-closed' : type == 'actual' && id =='Ready to Close','icon-tickets-cleared' : type == 'actual' && id =='Cleared'}\"></i>\n" +
    "    <span ng-transclude></span>\n" +
    "</li>\n" +
    "        \n" +
    "");
}]);

angular.module("app/scripts/ng_js_att_tpls/videoControls/photoControls.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/videoControls/photoControls.html",
    "<div>\n" +
    "    <a title=\"{{links.prevLink}}\" aria-label=\"Previous Link\"  ng-href=\"{{links.prevLink}}\"><i alt=\"previous\" class=\"icon-arrow-left\">&nbsp;</i></a>\n" +
    "    <span ng-transclude></span>\n" +
    "    <a title=\"{{links.nextLink}}\" aria-label=\"Next Link\"  ng-href=\"{{links.nextLink}}\"><i alt=\"next\" class=\"icon-arrow-right\">&nbsp;</i></a>\n" +
    "</div>");
}]);

angular.module("app/scripts/ng_js_att_tpls/videoControls/videoControls.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/videoControls/videoControls.html",
    "<div class=\"video-player\">\n" +
    "    <div class=\"video-player__control video-player__play-button\">\n" +
    "        <a class=\"video-player__button gigant-play\" data-toggle-buttons=\"icon-play, icon-pause\" data-target=\"i\"><i class=\"icon-play\"  alt=\"Play/Pause Button\"></i></a>\n" +
    "    </div>\n" +
    "    <div class=\"video-player__control video-player__track\">\n" +
    "\n" +
    "        <div class=\"video-player__track--inner\">\n" +
    "            <div class=\"video-player__track--loaded\" style=\"width: 75%\"></div>\n" +
    "            <div class=\"video-player__track--played\" style=\"width: 40%\">\n" +
    "                <div class=\"att-tooltip att-tooltip--on att-tooltip--dark att-tooltip--above video-player__track-tooltip\" ng-transclude></div>\n" +
    "                <div class=\"video-player__track-handle\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <a class=\"video-player__time\" ng-transclude></a>\n" +
    "    <div class=\"video-player__control video-player__volume_icon\">\n" +
    "        <a class=\"video-player__button\" data-toggle-buttons=\"icon-volume-mute, icon-volume-up\" data-target=\"i\"><i class=\"icon-volume-up\" alt=\"Volume Button\"></i></a>\n" +
    "    </div>\n" +
    "    <ul class=\"video-player__control video-player__volume\">\n" +
    "        <li class=\"video-player__volume-bar video-player__volume-bar--full\">&nbsp;</li>\n" +
    "        <li class=\"video-player__volume-bar video-player__volume-bar--full\">&nbsp;</li>\n" +
    "        <li class=\"video-player__volume-bar\">&nbsp;</li>\n" +
    "        <li class=\"video-player__volume-bar\">&nbsp;</li>\n" +
    "        <li class=\"video-player__volume-bar\">&nbsp;</li>\n" +
    "    </ul>\n" +
    "    <div class=\"video-player__control video-player__toggle-fullscreen-button\">\n" +
    "        <a class=\"video-player__button\" data-toggle-buttons=\"icon-full-screen, icon-normal-screen\" data-target=\"i\"><i class=\"icon-full-screen\" alt=\"Full Screen Button\">&nbsp;</i></a>\n" +
    "    </div>\n" +
    "</div>");
}]);

return {}
})(angular, window);