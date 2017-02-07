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

/*!
 * VERSION: 1.7.3
 * DATE: 2014-01-14
 * UPDATES AND DOCS AT: http://www.greensock.com
 *
 * @license Copyright (c) 2008-2014, GreenSock. All rights reserved.
 * This work is subject to the terms at http://www.greensock.com/terms_of_use.html or for
 * Club GreenSock members, the software agreement that was issued with your membership.
 * 
 * @author: Jack Doyle, jack@greensock.com
 **/
(window._gsQueue || (window._gsQueue = [])).push( function() {

	"use strict";

	var _doc = document.documentElement,
		_window = window,
		_max = function(element, axis) {
			var dim = (axis === "x") ? "Width" : "Height",
				scroll = "scroll" + dim,
				client = "client" + dim,
				body = document.body;
			return (element === _window || element === _doc || element === body) ? Math.max(_doc[scroll], body[scroll]) - (_window["inner" + dim] || Math.max(_doc[client], body[client])) : element[scroll] - element["offset" + dim];
		},

		ScrollToPlugin = window._gsDefine.plugin({
			propName: "scrollTo",
			API: 2,
			version:"1.7.3",

			//called when the tween renders for the first time. This is where initial values should be recorded and any setup routines should run.
			init: function(target, value, tween) {
				this._wdw = (target === _window);
				this._target = target;
				this._tween = tween;
				if (typeof(value) !== "object") {
					value = {y:value}; //if we don't receive an object as the parameter, assume the user intends "y".
				}
				this._autoKill = (value.autoKill !== false);
				this.x = this.xPrev = this.getX();
				this.y = this.yPrev = this.getY();
				if (value.x != null) {
					this._addTween(this, "x", this.x, (value.x === "max") ? _max(target, "x") : value.x, "scrollTo_x", true);
					this._overwriteProps.push("scrollTo_x");
				} else {
					this.skipX = true;
				}
				if (value.y != null) {
					this._addTween(this, "y", this.y, (value.y === "max") ? _max(target, "y") : value.y, "scrollTo_y", true);
					this._overwriteProps.push("scrollTo_y");
				} else {
					this.skipY = true;
				}
				return true;
			},

			//called each time the values should be updated, and the ratio gets passed as the only parameter (typically it's a value between 0 and 1, but it can exceed those when using an ease like Elastic.easeOut or Back.easeOut, etc.)
			set: function(v) {
				this._super.setRatio.call(this, v);

				var x = (this._wdw || !this.skipX) ? this.getX() : this.xPrev,
					y = (this._wdw || !this.skipY) ? this.getY() : this.yPrev,
					yDif = y - this.yPrev,
					xDif = x - this.xPrev;

				if (this._autoKill) {
					//note: iOS has a bug that throws off the scroll by several pixels, so we need to check if it's within 7 pixels of the previous one that we set instead of just looking for an exact match.
					if (!this.skipX && (xDif > 7 || xDif < -7) && x < _max(this._target, "x")) {
						this.skipX = true; //if the user scrolls separately, we should stop tweening!
					}
					if (!this.skipY && (yDif > 7 || yDif < -7) && y < _max(this._target, "y")) {
						this.skipY = true; //if the user scrolls separately, we should stop tweening!
					}
					if (this.skipX && this.skipY) {
						this._tween.kill();
					}
				}
				if (this._wdw) {
					_window.scrollTo((!this.skipX) ? this.x : x, (!this.skipY) ? this.y : y);
				} else {
					if (!this.skipY) {
						this._target.scrollTop = this.y;
					}
					if (!this.skipX) {
						this._target.scrollLeft = this.x;
					}
				}
				this.xPrev = this.x;
				this.yPrev = this.y;
			}

		}),
		p = ScrollToPlugin.prototype;

	ScrollToPlugin.max = _max;

	p.getX = function() {
		return (!this._wdw) ? this._target.scrollLeft : (_window.pageXOffset != null) ? _window.pageXOffset : (_doc.scrollLeft != null) ? _doc.scrollLeft : document.body.scrollLeft;
	};

	p.getY = function() {
		return (!this._wdw) ? this._target.scrollTop : (_window.pageYOffset != null) ? _window.pageYOffset : (_doc.scrollTop != null) ? _doc.scrollTop : document.body.scrollTop;
	};

	p._kill = function(lookup) {
		if (lookup.scrollTo_x) {
			this.skipX = true;
		}
		if (lookup.scrollTo_y) {
			this.skipY = true;
		}
		return this._super._kill.call(this, lookup);
	};

}); if (window._gsDefine) { window._gsQueue.pop()(); }
/*!
 * VERSION: 1.12.1
 * DATE: 2014-06-26
 * UPDATES AND DOCS AT: http://www.greensock.com
 * 
 * Includes all of the following: TweenLite, TweenMax, TimelineLite, TimelineMax, EasePack, CSSPlugin, RoundPropsPlugin, BezierPlugin, AttrPlugin, DirectionalRotationPlugin
 *
 * @license Copyright (c) 2008-2014, GreenSock. All rights reserved.
 * This work is subject to the terms at http://www.greensock.com/terms_of_use.html or for
 * Club GreenSock members, the software agreement that was issued with your membership.
 * 
 * @author: Jack Doyle, jack@greensock.com
 **/

(window._gsQueue || (window._gsQueue = [])).push( function() {

	"use strict";

	window._gsDefine("TweenMax", ["core.Animation","core.SimpleTimeline","TweenLite"], function(Animation, SimpleTimeline, TweenLite) {

		var _slice = [].slice,
			TweenMax = function(target, duration, vars) {
				TweenLite.call(this, target, duration, vars);
				this._cycle = 0;
				this._yoyo = (this.vars.yoyo === true);
				this._repeat = this.vars.repeat || 0;
				this._repeatDelay = this.vars.repeatDelay || 0;
				this._dirty = true; //ensures that if there is any repeat, the totalDuration will get recalculated to accurately report it.
				this.render = TweenMax.prototype.render; //speed optimization (avoid prototype lookup on this "hot" method)
			},
			_tinyNum = 0.0000000001,
			TweenLiteInternals = TweenLite._internals,
			_isSelector = TweenLiteInternals.isSelector,
			_isArray = TweenLiteInternals.isArray,
			p = TweenMax.prototype = TweenLite.to({}, 0.1, {}),
			_blankArray = [];

		TweenMax.version = "1.12.1";
		p.constructor = TweenMax;
		p.kill()._gc = false;
		TweenMax.killTweensOf = TweenMax.killDelayedCallsTo = TweenLite.killTweensOf;
		TweenMax.getTweensOf = TweenLite.getTweensOf;
		TweenMax.lagSmoothing = TweenLite.lagSmoothing;
		TweenMax.ticker = TweenLite.ticker;
		TweenMax.render = TweenLite.render;

		p.invalidate = function() {
			this._yoyo = (this.vars.yoyo === true);
			this._repeat = this.vars.repeat || 0;
			this._repeatDelay = this.vars.repeatDelay || 0;
			this._uncache(true);
			return TweenLite.prototype.invalidate.call(this);
		};
		
		p.updateTo = function(vars, resetDuration) {
			var curRatio = this.ratio, p;
			if (resetDuration && this._startTime < this._timeline._time) {
				this._startTime = this._timeline._time;
				this._uncache(false);
				if (this._gc) {
					this._enabled(true, false);
				} else {
					this._timeline.insert(this, this._startTime - this._delay); //ensures that any necessary re-sequencing of Animations in the timeline occurs to make sure the rendering order is correct.
				}
			}
			for (p in vars) {
				this.vars[p] = vars[p];
			}
			if (this._initted) {
				if (resetDuration) {
					this._initted = false;
				} else {
					if (this._gc) {
						this._enabled(true, false);
					}
					if (this._notifyPluginsOfEnabled && this._firstPT) {
						TweenLite._onPluginEvent("_onDisable", this); //in case a plugin like MotionBlur must perform some cleanup tasks
					}
					if (this._time / this._duration > 0.998) { //if the tween has finished (or come extremely close to finishing), we just need to rewind it to 0 and then render it again at the end which forces it to re-initialize (parsing the new vars). We allow tweens that are close to finishing (but haven't quite finished) to work this way too because otherwise, the values are so small when determining where to project the starting values that binary math issues creep in and can make the tween appear to render incorrectly when run backwards. 
						var prevTime = this._time;
						this.render(0, true, false);
						this._initted = false;
						this.render(prevTime, true, false);
					} else if (this._time > 0) {
						this._initted = false;
						this._init();
						var inv = 1 / (1 - curRatio),
							pt = this._firstPT, endValue;
						while (pt) {
							endValue = pt.s + pt.c; 
							pt.c *= inv;
							pt.s = endValue - pt.c;
							pt = pt._next;
						}
					}
				}
			}
			return this;
		};
				
		p.render = function(time, suppressEvents, force) {
			if (!this._initted) if (this._duration === 0 && this.vars.repeat) { //zero duration tweens that render immediately have render() called from TweenLite's constructor, before TweenMax's constructor has finished setting _repeat, _repeatDelay, and _yoyo which are critical in determining totalDuration() so we need to call invalidate() which is a low-kb way to get those set properly.
				this.invalidate();
			}
			var totalDur = (!this._dirty) ? this._totalDuration : this.totalDuration(),
				prevTime = this._time,
				prevTotalTime = this._totalTime, 
				prevCycle = this._cycle,
				duration = this._duration,
				prevRawPrevTime = this._rawPrevTime,
				isComplete, callback, pt, cycleDuration, r, type, pow, rawPrevTime, i;
			if (time >= totalDur) {
				this._totalTime = totalDur;
				this._cycle = this._repeat;
				if (this._yoyo && (this._cycle & 1) !== 0) {
					this._time = 0;
					this.ratio = this._ease._calcEnd ? this._ease.getRatio(0) : 0;
				} else {
					this._time = duration;
					this.ratio = this._ease._calcEnd ? this._ease.getRatio(1) : 1;
				}
				if (!this._reversed) {
					isComplete = true;
					callback = "onComplete";
				}
				if (duration === 0) if (this._initted || !this.vars.lazy || force) { //zero-duration tweens are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
					if (this._startTime === this._timeline._duration) { //if a zero-duration tween is at the VERY end of a timeline and that timeline renders at its end, it will typically add a tiny bit of cushion to the render time to prevent rounding errors from getting in the way of tweens rendering their VERY end. If we then reverse() that timeline, the zero-duration tween will trigger its onReverseComplete even though technically the playhead didn't pass over it again. It's a very specific edge case we must accommodate.
						time = 0;
					}
					if (time === 0 || prevRawPrevTime < 0 || prevRawPrevTime === _tinyNum) if (prevRawPrevTime !== time) {
						force = true;
						if (prevRawPrevTime > _tinyNum) {
							callback = "onReverseComplete";
						}
					}
					this._rawPrevTime = rawPrevTime = (!suppressEvents || time || prevRawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
				}
				
			} else if (time < 0.0000001) { //to work around occasional floating point math artifacts, round super small values to 0.
				this._totalTime = this._time = this._cycle = 0;
				this.ratio = this._ease._calcEnd ? this._ease.getRatio(0) : 0;
				if (prevTotalTime !== 0 || (duration === 0 && prevRawPrevTime > 0 && prevRawPrevTime !== _tinyNum)) {
					callback = "onReverseComplete";
					isComplete = this._reversed;
				}
				if (time < 0) {
					this._active = false;
					if (duration === 0) if (this._initted || !this.vars.lazy || force) { //zero-duration tweens are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
						if (prevRawPrevTime >= 0) {
							force = true;
						}
						this._rawPrevTime = rawPrevTime = (!suppressEvents || time || prevRawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
					}
				} else if (!this._initted) { //if we render the very beginning (time == 0) of a fromTo(), we must force the render (normal tweens wouldn't need to render at a time of 0 when the prevTime was also 0). This is also mandatory to make sure overwriting kicks in immediately.
					force = true;
				}
			} else {
				this._totalTime = this._time = time;
				
				if (this._repeat !== 0) {
					cycleDuration = duration + this._repeatDelay;
					this._cycle = (this._totalTime / cycleDuration) >> 0; //originally _totalTime % cycleDuration but floating point errors caused problems, so I normalized it. (4 % 0.8 should be 0 but Flash reports it as 0.79999999!)
					if (this._cycle !== 0) if (this._cycle === this._totalTime / cycleDuration) {
						this._cycle--; //otherwise when rendered exactly at the end time, it will act as though it is repeating (at the beginning)
					}
					this._time = this._totalTime - (this._cycle * cycleDuration);
					if (this._yoyo) if ((this._cycle & 1) !== 0) {
						this._time = duration - this._time;
					}
					if (this._time > duration) {
						this._time = duration;
					} else if (this._time < 0) {
						this._time = 0;
					}
				}

				if (this._easeType) {
					r = this._time / duration;
					type = this._easeType;
					pow = this._easePower;
					if (type === 1 || (type === 3 && r >= 0.5)) {
						r = 1 - r;
					}
					if (type === 3) {
						r *= 2;
					}
					if (pow === 1) {
						r *= r;
					} else if (pow === 2) {
						r *= r * r;
					} else if (pow === 3) {
						r *= r * r * r;
					} else if (pow === 4) {
						r *= r * r * r * r;
					}

					if (type === 1) {
						this.ratio = 1 - r;
					} else if (type === 2) {
						this.ratio = r;
					} else if (this._time / duration < 0.5) {
						this.ratio = r / 2;
					} else {
						this.ratio = 1 - (r / 2);
					}

				} else {
					this.ratio = this._ease.getRatio(this._time / duration);
				}
				
			}
				
			if (prevTime === this._time && !force && prevCycle === this._cycle) {
				if (prevTotalTime !== this._totalTime) if (this._onUpdate) if (!suppressEvents) { //so that onUpdate fires even during the repeatDelay - as long as the totalTime changed, we should trigger onUpdate.
					this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
				}
				return;
			} else if (!this._initted) {
				this._init();
				if (!this._initted || this._gc) { //immediateRender tweens typically won't initialize until the playhead advances (_time is greater than 0) in order to ensure that overwriting occurs properly. Also, if all of the tweening properties have been overwritten (which would cause _gc to be true, as set in _init()), we shouldn't continue otherwise an onStart callback could be called for example.
					return;
				} else if (!force && this._firstPT && ((this.vars.lazy !== false && this._duration) || (this.vars.lazy && !this._duration))) { //we stick it in the queue for rendering at the very end of the tick - this is a performance optimization because browsers invalidate styles and force a recalculation if you read, write, and then read style data (so it's better to read/read/read/write/write/write than read/write/read/write/read/write). The down side, of course, is that usually you WANT things to render immediately because you may have code running right after that which depends on the change. Like imagine running TweenLite.set(...) and then immediately after that, creating a nother tween that animates the same property to another value; the starting values of that 2nd tween wouldn't be accurate if lazy is true.
					this._time = prevTime;
					this._totalTime = prevTotalTime;
					this._rawPrevTime = prevRawPrevTime;
					this._cycle = prevCycle;
					TweenLiteInternals.lazyTweens.push(this);
					this._lazy = time;
					return;
				}
				//_ease is initially set to defaultEase, so now that init() has run, _ease is set properly and we need to recalculate the ratio. Overall this is faster than using conditional logic earlier in the method to avoid having to set ratio twice because we only init() once but renderTime() gets called VERY frequently.
				if (this._time && !isComplete) {
					this.ratio = this._ease.getRatio(this._time / duration);
				} else if (isComplete && this._ease._calcEnd) {
					this.ratio = this._ease.getRatio((this._time === 0) ? 0 : 1);
				}
			}
			if (this._lazy !== false) {
				this._lazy = false;
			}

			if (!this._active) if (!this._paused && this._time !== prevTime && time >= 0) {
				this._active = true; //so that if the user renders a tween (as opposed to the timeline rendering it), the timeline is forced to re-render and align it with the proper time/frame on the next rendering cycle. Maybe the tween already finished but the user manually re-renders it as halfway done.
			}
			if (prevTotalTime === 0) {
				if (this._initted === 2 && time > 0) {
					//this.invalidate();
					this._init(); //will just apply overwriting since _initted of (2) means it was a from() tween that had immediateRender:true
				}
				if (this._startAt) {
					if (time >= 0) {
						this._startAt.render(time, suppressEvents, force);
					} else if (!callback) {
						callback = "_dummyGS"; //if no callback is defined, use a dummy value just so that the condition at the end evaluates as true because _startAt should render AFTER the normal render loop when the time is negative. We could handle this in a more intuitive way, of course, but the render loop is the MOST important thing to optimize, so this technique allows us to avoid adding extra conditional logic in a high-frequency area.
					}
				}
				if (this.vars.onStart) if (this._totalTime !== 0 || duration === 0) if (!suppressEvents) {
					this.vars.onStart.apply(this.vars.onStartScope || this, this.vars.onStartParams || _blankArray);
				}
			}
			
			pt = this._firstPT;
			while (pt) {
				if (pt.f) {
					pt.t[pt.p](pt.c * this.ratio + pt.s);
				} else {
					pt.t[pt.p] = pt.c * this.ratio + pt.s;
				}
				pt = pt._next;
			}
			
			if (this._onUpdate) {
				if (time < 0) if (this._startAt && this._startTime) { //if the tween is positioned at the VERY beginning (_startTime 0) of its parent timeline, it's illegal for the playhead to go back further, so we should not render the recorded startAt values.
					this._startAt.render(time, suppressEvents, force); //note: for performance reasons, we tuck this conditional logic inside less traveled areas (most tweens don't have an onUpdate). We'd just have it at the end before the onComplete, but the values should be updated before any onUpdate is called, so we ALSO put it here and then if it's not called, we do so later near the onComplete.
				}
				if (!suppressEvents) if (this._totalTime !== prevTotalTime || isComplete) {
					this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
				}
			}
			if (this._cycle !== prevCycle) if (!suppressEvents) if (!this._gc) if (this.vars.onRepeat) {
				this.vars.onRepeat.apply(this.vars.onRepeatScope || this, this.vars.onRepeatParams || _blankArray);
			}
			if (callback) if (!this._gc) { //check gc because there's a chance that kill() could be called in an onUpdate
				if (time < 0 && this._startAt && !this._onUpdate && this._startTime) { //if the tween is positioned at the VERY beginning (_startTime 0) of its parent timeline, it's illegal for the playhead to go back further, so we should not render the recorded startAt values.
					this._startAt.render(time, suppressEvents, force);
				}
				if (isComplete) {
					if (this._timeline.autoRemoveChildren) {
						this._enabled(false, false);
					}
					this._active = false;
				}
				if (!suppressEvents && this.vars[callback]) {
					this.vars[callback].apply(this.vars[callback + "Scope"] || this, this.vars[callback + "Params"] || _blankArray);
				}
				if (duration === 0 && this._rawPrevTime === _tinyNum && rawPrevTime !== _tinyNum) { //the onComplete or onReverseComplete could trigger movement of the playhead and for zero-duration tweens (which must discern direction) that land directly back on their start time, we don't want to fire again on the next render. Think of several addPause()'s in a timeline that forces the playhead to a certain spot, but what if it's already paused and another tween is tweening the "time" of the timeline? Each time it moves [forward] past that spot, it would move back, and since suppressEvents is true, it'd reset _rawPrevTime to _tinyNum so that when it begins again, the callback would fire (so ultimately it could bounce back and forth during that tween). Again, this is a very uncommon scenario, but possible nonetheless.
					this._rawPrevTime = 0;
				}
			}
		};
		
//---- STATIC FUNCTIONS -----------------------------------------------------------------------------------------------------------
		
		TweenMax.to = function(target, duration, vars) {
			return new TweenMax(target, duration, vars);
		};
		
		TweenMax.from = function(target, duration, vars) {
			vars.runBackwards = true;
			vars.immediateRender = (vars.immediateRender != false);
			return new TweenMax(target, duration, vars);
		};
		
		TweenMax.fromTo = function(target, duration, fromVars, toVars) {
			toVars.startAt = fromVars;
			toVars.immediateRender = (toVars.immediateRender != false && fromVars.immediateRender != false);
			return new TweenMax(target, duration, toVars);
		};
		
		TweenMax.staggerTo = TweenMax.allTo = function(targets, duration, vars, stagger, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			stagger = stagger || 0;
			var delay = vars.delay || 0,
				a = [],
				finalComplete = function() {
					if (vars.onComplete) {
						vars.onComplete.apply(vars.onCompleteScope || this, arguments);
					}
					onCompleteAll.apply(onCompleteAllScope || this, onCompleteAllParams || _blankArray);
				},
				l, copy, i, p;
			if (!_isArray(targets)) {
				if (typeof(targets) === "string") {
					targets = TweenLite.selector(targets) || targets;
				}
				if (_isSelector(targets)) {
					targets = _slice.call(targets, 0);
				}
			}
			l = targets.length;
			for (i = 0; i < l; i++) {
				copy = {};
				for (p in vars) {
					copy[p] = vars[p];
				}
				copy.delay = delay;
				if (i === l - 1 && onCompleteAll) {
					copy.onComplete = finalComplete;
				}
				a[i] = new TweenMax(targets[i], duration, copy);
				delay += stagger;
			}
			return a;
		};
		
		TweenMax.staggerFrom = TweenMax.allFrom = function(targets, duration, vars, stagger, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			vars.runBackwards = true;
			vars.immediateRender = (vars.immediateRender != false);
			return TweenMax.staggerTo(targets, duration, vars, stagger, onCompleteAll, onCompleteAllParams, onCompleteAllScope);
		};
		
		TweenMax.staggerFromTo = TweenMax.allFromTo = function(targets, duration, fromVars, toVars, stagger, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			toVars.startAt = fromVars;
			toVars.immediateRender = (toVars.immediateRender != false && fromVars.immediateRender != false);
			return TweenMax.staggerTo(targets, duration, toVars, stagger, onCompleteAll, onCompleteAllParams, onCompleteAllScope);
		};
				
		TweenMax.delayedCall = function(delay, callback, params, scope, useFrames) {
			return new TweenMax(callback, 0, {delay:delay, onComplete:callback, onCompleteParams:params, onCompleteScope:scope, onReverseComplete:callback, onReverseCompleteParams:params, onReverseCompleteScope:scope, immediateRender:false, useFrames:useFrames, overwrite:0});
		};
		
		TweenMax.set = function(target, vars) {
			return new TweenMax(target, 0, vars);
		};
		
		TweenMax.isTweening = function(target) {
			return (TweenLite.getTweensOf(target, true).length > 0);
		};
		
		var _getChildrenOf = function(timeline, includeTimelines) {
				var a = [],
					cnt = 0,
					tween = timeline._first;
				while (tween) {
					if (tween instanceof TweenLite) {
						a[cnt++] = tween;
					} else {
						if (includeTimelines) {
							a[cnt++] = tween;
						}
						a = a.concat(_getChildrenOf(tween, includeTimelines));
						cnt = a.length;
					}
					tween = tween._next;
				}
				return a;
			}, 
			getAllTweens = TweenMax.getAllTweens = function(includeTimelines) {
				return _getChildrenOf(Animation._rootTimeline, includeTimelines).concat( _getChildrenOf(Animation._rootFramesTimeline, includeTimelines) );
			};
		
		TweenMax.killAll = function(complete, tweens, delayedCalls, timelines) {
			if (tweens == null) {
				tweens = true;
			}
			if (delayedCalls == null) {
				delayedCalls = true;
			}
			var a = getAllTweens((timelines != false)),
				l = a.length,
				allTrue = (tweens && delayedCalls && timelines),
				isDC, tween, i;
			for (i = 0; i < l; i++) {
				tween = a[i];
				if (allTrue || (tween instanceof SimpleTimeline) || ((isDC = (tween.target === tween.vars.onComplete)) && delayedCalls) || (tweens && !isDC)) {
					if (complete) {
						tween.totalTime(tween._reversed ? 0 : tween.totalDuration());
					} else {
						tween._enabled(false, false);
					}
				}
			}
		};
		
		TweenMax.killChildTweensOf = function(parent, complete) {
			if (parent == null) {
				return;
			}
			var tl = TweenLiteInternals.tweenLookup,
				a, curParent, p, i, l;
			if (typeof(parent) === "string") {
				parent = TweenLite.selector(parent) || parent;
			}
			if (_isSelector(parent)) {
				parent = _slice.call(parent, 0);
			}
			if (_isArray(parent)) {
				i = parent.length;
				while (--i > -1) {
					TweenMax.killChildTweensOf(parent[i], complete);
				}
				return;
			}
			a = [];
			for (p in tl) {
				curParent = tl[p].target.parentNode;
				while (curParent) {
					if (curParent === parent) {
						a = a.concat(tl[p].tweens);
					}
					curParent = curParent.parentNode;
				}
			}
			l = a.length;
			for (i = 0; i < l; i++) {
				if (complete) {
					a[i].totalTime(a[i].totalDuration());
				}
				a[i]._enabled(false, false);
			}
		};

		var _changePause = function(pause, tweens, delayedCalls, timelines) {
			tweens = (tweens !== false);
			delayedCalls = (delayedCalls !== false);
			timelines = (timelines !== false);
			var a = getAllTweens(timelines),
				allTrue = (tweens && delayedCalls && timelines),
				i = a.length,
				isDC, tween;
			while (--i > -1) {
				tween = a[i];
				if (allTrue || (tween instanceof SimpleTimeline) || ((isDC = (tween.target === tween.vars.onComplete)) && delayedCalls) || (tweens && !isDC)) {
					tween.paused(pause);
				}
			}
		};
		
		TweenMax.pauseAll = function(tweens, delayedCalls, timelines) {
			_changePause(true, tweens, delayedCalls, timelines);
		};
		
		TweenMax.resumeAll = function(tweens, delayedCalls, timelines) {
			_changePause(false, tweens, delayedCalls, timelines);
		};

		TweenMax.globalTimeScale = function(value) {
			var tl = Animation._rootTimeline,
				t = TweenLite.ticker.time;
			if (!arguments.length) {
				return tl._timeScale;
			}
			value = value || _tinyNum; //can't allow zero because it'll throw the math off
			tl._startTime = t - ((t - tl._startTime) * tl._timeScale / value);
			tl = Animation._rootFramesTimeline;
			t = TweenLite.ticker.frame;
			tl._startTime = t - ((t - tl._startTime) * tl._timeScale / value);
			tl._timeScale = Animation._rootTimeline._timeScale = value;
			return value;
		};
		
	
//---- GETTERS / SETTERS ----------------------------------------------------------------------------------------------------------
		
		p.progress = function(value) {
			return (!arguments.length) ? this._time / this.duration() : this.totalTime( this.duration() * ((this._yoyo && (this._cycle & 1) !== 0) ? 1 - value : value) + (this._cycle * (this._duration + this._repeatDelay)), false);
		};
		
		p.totalProgress = function(value) {
			return (!arguments.length) ? this._totalTime / this.totalDuration() : this.totalTime( this.totalDuration() * value, false);
		};
		
		p.time = function(value, suppressEvents) {
			if (!arguments.length) {
				return this._time;
			}
			if (this._dirty) {
				this.totalDuration();
			}
			if (value > this._duration) {
				value = this._duration;
			}
			if (this._yoyo && (this._cycle & 1) !== 0) {
				value = (this._duration - value) + (this._cycle * (this._duration + this._repeatDelay));
			} else if (this._repeat !== 0) {
				value += this._cycle * (this._duration + this._repeatDelay);
			}
			return this.totalTime(value, suppressEvents);
		};

		p.duration = function(value) {
			if (!arguments.length) {
				return this._duration; //don't set _dirty = false because there could be repeats that haven't been factored into the _totalDuration yet. Otherwise, if you create a repeated TweenMax and then immediately check its duration(), it would cache the value and the totalDuration would not be correct, thus repeats wouldn't take effect.
			}
			return Animation.prototype.duration.call(this, value);
		};

		p.totalDuration = function(value) {
			if (!arguments.length) {
				if (this._dirty) {
					//instead of Infinity, we use 999999999999 so that we can accommodate reverses
					this._totalDuration = (this._repeat === -1) ? 999999999999 : this._duration * (this._repeat + 1) + (this._repeatDelay * this._repeat);
					this._dirty = false;
				}
				return this._totalDuration;
			}
			return (this._repeat === -1) ? this : this.duration( (value - (this._repeat * this._repeatDelay)) / (this._repeat + 1) );
		};
		
		p.repeat = function(value) {
			if (!arguments.length) {
				return this._repeat;
			}
			this._repeat = value;
			return this._uncache(true);
		};
		
		p.repeatDelay = function(value) {
			if (!arguments.length) {
				return this._repeatDelay;
			}
			this._repeatDelay = value;
			return this._uncache(true);
		};
		
		p.yoyo = function(value) {
			if (!arguments.length) {
				return this._yoyo;
			}
			this._yoyo = value;
			return this;
		};
		
		
		return TweenMax;
		
	}, true);








/*
 * ----------------------------------------------------------------
 * TimelineLite
 * ----------------------------------------------------------------
 */
	window._gsDefine("TimelineLite", ["core.Animation","core.SimpleTimeline","TweenLite"], function(Animation, SimpleTimeline, TweenLite) {

		var TimelineLite = function(vars) {
				SimpleTimeline.call(this, vars);
				this._labels = {};
				this.autoRemoveChildren = (this.vars.autoRemoveChildren === true);
				this.smoothChildTiming = (this.vars.smoothChildTiming === true);
				this._sortChildren = true;
				this._onUpdate = this.vars.onUpdate;
				var v = this.vars,
					val, p;
				for (p in v) {
					val = v[p];
					if (_isArray(val)) if (val.join("").indexOf("{self}") !== -1) {
						v[p] = this._swapSelfInParams(val);
					}
				}
				if (_isArray(v.tweens)) {
					this.add(v.tweens, 0, v.align, v.stagger);
				}
			},
			_tinyNum = 0.0000000001,
			_isSelector = TweenLite._internals.isSelector,
			_isArray = TweenLite._internals.isArray,
			_blankArray = [],
			_globals = window._gsDefine.globals,
			_copy = function(vars) {
				var copy = {}, p;
				for (p in vars) {
					copy[p] = vars[p];
				}
				return copy;
			},
			_pauseCallback = function(tween, callback, params, scope) {
				tween._timeline.pause(tween._startTime);
				if (callback) {
					callback.apply(scope || tween._timeline, params || _blankArray);
				}
			},
			_slice = _blankArray.slice,
			p = TimelineLite.prototype = new SimpleTimeline();

		TimelineLite.version = "1.12.1";
		p.constructor = TimelineLite;
		p.kill()._gc = false;

		p.to = function(target, duration, vars, position) {
			var Engine = (vars.repeat && _globals.TweenMax) || TweenLite;
			return duration ? this.add( new Engine(target, duration, vars), position) : this.set(target, vars, position);
		};

		p.from = function(target, duration, vars, position) {
			return this.add( ((vars.repeat && _globals.TweenMax) || TweenLite).from(target, duration, vars), position);
		};

		p.fromTo = function(target, duration, fromVars, toVars, position) {
			var Engine = (toVars.repeat && _globals.TweenMax) || TweenLite;
			return duration ? this.add( Engine.fromTo(target, duration, fromVars, toVars), position) : this.set(target, toVars, position);
		};

		p.staggerTo = function(targets, duration, vars, stagger, position, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			var tl = new TimelineLite({onComplete:onCompleteAll, onCompleteParams:onCompleteAllParams, onCompleteScope:onCompleteAllScope, smoothChildTiming:this.smoothChildTiming}),
				i;
			if (typeof(targets) === "string") {
				targets = TweenLite.selector(targets) || targets;
			}
			if (_isSelector(targets)) { //senses if the targets object is a selector. If it is, we should translate it into an array.
				targets = _slice.call(targets, 0);
			}
			stagger = stagger || 0;
			for (i = 0; i < targets.length; i++) {
				if (vars.startAt) {
					vars.startAt = _copy(vars.startAt);
				}
				tl.to(targets[i], duration, _copy(vars), i * stagger);
			}
			return this.add(tl, position);
		};

		p.staggerFrom = function(targets, duration, vars, stagger, position, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			vars.immediateRender = (vars.immediateRender != false);
			vars.runBackwards = true;
			return this.staggerTo(targets, duration, vars, stagger, position, onCompleteAll, onCompleteAllParams, onCompleteAllScope);
		};

		p.staggerFromTo = function(targets, duration, fromVars, toVars, stagger, position, onCompleteAll, onCompleteAllParams, onCompleteAllScope) {
			toVars.startAt = fromVars;
			toVars.immediateRender = (toVars.immediateRender != false && fromVars.immediateRender != false);
			return this.staggerTo(targets, duration, toVars, stagger, position, onCompleteAll, onCompleteAllParams, onCompleteAllScope);
		};

		p.call = function(callback, params, scope, position) {
			return this.add( TweenLite.delayedCall(0, callback, params, scope), position);
		};

		p.set = function(target, vars, position) {
			position = this._parseTimeOrLabel(position, 0, true);
			if (vars.immediateRender == null) {
				vars.immediateRender = (position === this._time && !this._paused);
			}
			return this.add( new TweenLite(target, 0, vars), position);
		};

		TimelineLite.exportRoot = function(vars, ignoreDelayedCalls) {
			vars = vars || {};
			if (vars.smoothChildTiming == null) {
				vars.smoothChildTiming = true;
			}
			var tl = new TimelineLite(vars),
				root = tl._timeline,
				tween, next;
			if (ignoreDelayedCalls == null) {
				ignoreDelayedCalls = true;
			}
			root._remove(tl, true);
			tl._startTime = 0;
			tl._rawPrevTime = tl._time = tl._totalTime = root._time;
			tween = root._first;
			while (tween) {
				next = tween._next;
				if (!ignoreDelayedCalls || !(tween instanceof TweenLite && tween.target === tween.vars.onComplete)) {
					tl.add(tween, tween._startTime - tween._delay);
				}
				tween = next;
			}
			root.add(tl, 0);
			return tl;
		};

		p.add = function(value, position, align, stagger) {
			var curTime, l, i, child, tl, beforeRawTime;
			if (typeof(position) !== "number") {
				position = this._parseTimeOrLabel(position, 0, true, value);
			}
			if (!(value instanceof Animation)) {
				if ((value instanceof Array) || (value && value.push && _isArray(value))) {
					align = align || "normal";
					stagger = stagger || 0;
					curTime = position;
					l = value.length;
					for (i = 0; i < l; i++) {
						if (_isArray(child = value[i])) {
							child = new TimelineLite({tweens:child});
						}
						this.add(child, curTime);
						if (typeof(child) !== "string" && typeof(child) !== "function") {
							if (align === "sequence") {
								curTime = child._startTime + (child.totalDuration() / child._timeScale);
							} else if (align === "start") {
								child._startTime -= child.delay();
							}
						}
						curTime += stagger;
					}
					return this._uncache(true);
				} else if (typeof(value) === "string") {
					return this.addLabel(value, position);
				} else if (typeof(value) === "function") {
					value = TweenLite.delayedCall(0, value);
				} else {
					throw("Cannot add " + value + " into the timeline; it is not a tween, timeline, function, or string.");
				}
			}

			SimpleTimeline.prototype.add.call(this, value, position);

			//if the timeline has already ended but the inserted tween/timeline extends the duration, we should enable this timeline again so that it renders properly. We should also align the playhead with the parent timeline's when appropriate.
			if (this._gc || this._time === this._duration) if (!this._paused) if (this._duration < this.duration()) {
				//in case any of the ancestors had completed but should now be enabled...
				tl = this;
				beforeRawTime = (tl.rawTime() > value._startTime); //if the tween is placed on the timeline so that it starts BEFORE the current rawTime, we should align the playhead (move the timeline). This is because sometimes users will create a timeline, let it finish, and much later append a tween and expect it to run instead of jumping to its end state. While technically one could argue that it should jump to its end state, that's not what users intuitively expect.
				while (tl._timeline) {
					if (beforeRawTime && tl._timeline.smoothChildTiming) {
						tl.totalTime(tl._totalTime, true); //moves the timeline (shifts its startTime) if necessary, and also enables it.
					} else if (tl._gc) {
						tl._enabled(true, false);
					}
					tl = tl._timeline;
				}
			}

			return this;
		};

		p.remove = function(value) {
			if (value instanceof Animation) {
				return this._remove(value, false);
			} else if (value instanceof Array || (value && value.push && _isArray(value))) {
				var i = value.length;
				while (--i > -1) {
					this.remove(value[i]);
				}
				return this;
			} else if (typeof(value) === "string") {
				return this.removeLabel(value);
			}
			return this.kill(null, value);
		};

		p._remove = function(tween, skipDisable) {
			SimpleTimeline.prototype._remove.call(this, tween, skipDisable);
			var last = this._last;
			if (!last) {
				this._time = this._totalTime = this._duration = this._totalDuration = 0;
			} else if (this._time > last._startTime + last._totalDuration / last._timeScale) {
				this._time = this.duration();
				this._totalTime = this._totalDuration;
			}
			return this;
		};

		p.append = function(value, offsetOrLabel) {
			return this.add(value, this._parseTimeOrLabel(null, offsetOrLabel, true, value));
		};

		p.insert = p.insertMultiple = function(value, position, align, stagger) {
			return this.add(value, position || 0, align, stagger);
		};

		p.appendMultiple = function(tweens, offsetOrLabel, align, stagger) {
			return this.add(tweens, this._parseTimeOrLabel(null, offsetOrLabel, true, tweens), align, stagger);
		};

		p.addLabel = function(label, position) {
			this._labels[label] = this._parseTimeOrLabel(position);
			return this;
		};

		p.addPause = function(position, callback, params, scope) {
			return this.call(_pauseCallback, ["{self}", callback, params, scope], this, position);
		};

		p.removeLabel = function(label) {
			delete this._labels[label];
			return this;
		};

		p.getLabelTime = function(label) {
			return (this._labels[label] != null) ? this._labels[label] : -1;
		};

		p._parseTimeOrLabel = function(timeOrLabel, offsetOrLabel, appendIfAbsent, ignore) {
			var i;
			//if we're about to add a tween/timeline (or an array of them) that's already a child of this timeline, we should remove it first so that it doesn't contaminate the duration().
			if (ignore instanceof Animation && ignore.timeline === this) {
				this.remove(ignore);
			} else if (ignore && ((ignore instanceof Array) || (ignore.push && _isArray(ignore)))) {
				i = ignore.length;
				while (--i > -1) {
					if (ignore[i] instanceof Animation && ignore[i].timeline === this) {
						this.remove(ignore[i]);
					}
				}
			}
			if (typeof(offsetOrLabel) === "string") {
				return this._parseTimeOrLabel(offsetOrLabel, (appendIfAbsent && typeof(timeOrLabel) === "number" && this._labels[offsetOrLabel] == null) ? timeOrLabel - this.duration() : 0, appendIfAbsent);
			}
			offsetOrLabel = offsetOrLabel || 0;
			if (typeof(timeOrLabel) === "string" && (isNaN(timeOrLabel) || this._labels[timeOrLabel] != null)) { //if the string is a number like "1", check to see if there's a label with that name, otherwise interpret it as a number (absolute value).
				i = timeOrLabel.indexOf("=");
				if (i === -1) {
					if (this._labels[timeOrLabel] == null) {
						return appendIfAbsent ? (this._labels[timeOrLabel] = this.duration() + offsetOrLabel) : offsetOrLabel;
					}
					return this._labels[timeOrLabel] + offsetOrLabel;
				}
				offsetOrLabel = parseInt(timeOrLabel.charAt(i-1) + "1", 10) * Number(timeOrLabel.substr(i+1));
				timeOrLabel = (i > 1) ? this._parseTimeOrLabel(timeOrLabel.substr(0, i-1), 0, appendIfAbsent) : this.duration();
			} else if (timeOrLabel == null) {
				timeOrLabel = this.duration();
			}
			return Number(timeOrLabel) + offsetOrLabel;
		};

		p.seek = function(position, suppressEvents) {
			return this.totalTime((typeof(position) === "number") ? position : this._parseTimeOrLabel(position), (suppressEvents !== false));
		};

		p.stop = function() {
			return this.paused(true);
		};

		p.gotoAndPlay = function(position, suppressEvents) {
			return this.play(position, suppressEvents);
		};

		p.gotoAndStop = function(position, suppressEvents) {
			return this.pause(position, suppressEvents);
		};

		p.render = function(time, suppressEvents, force) {
			if (this._gc) {
				this._enabled(true, false);
			}
			var totalDur = (!this._dirty) ? this._totalDuration : this.totalDuration(),
				prevTime = this._time,
				prevStart = this._startTime,
				prevTimeScale = this._timeScale,
				prevPaused = this._paused,
				tween, isComplete, next, callback, internalForce;
			if (time >= totalDur) {
				this._totalTime = this._time = totalDur;
				if (!this._reversed) if (!this._hasPausedChild()) {
					isComplete = true;
					callback = "onComplete";
					if (this._duration === 0) if (time === 0 || this._rawPrevTime < 0 || this._rawPrevTime === _tinyNum) if (this._rawPrevTime !== time && this._first) {
						internalForce = true;
						if (this._rawPrevTime > _tinyNum) {
							callback = "onReverseComplete";
						}
					}
				}
				this._rawPrevTime = (this._duration || !suppressEvents || time || this._rawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration timeline or tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
				time = totalDur + 0.0001; //to avoid occasional floating point rounding errors - sometimes child tweens/timelines were not being fully completed (their progress might be 0.999999999999998 instead of 1 because when _time - tween._startTime is performed, floating point errors would return a value that was SLIGHTLY off). Try (999999999999.7 - 999999999999) * 1 = 0.699951171875 instead of 0.7.

			} else if (time < 0.0000001) { //to work around occasional floating point math artifacts, round super small values to 0.
				this._totalTime = this._time = 0;
				if (prevTime !== 0 || (this._duration === 0 && this._rawPrevTime !== _tinyNum && (this._rawPrevTime > 0 || (time < 0 && this._rawPrevTime >= 0)))) {
					callback = "onReverseComplete";
					isComplete = this._reversed;
				}
				if (time < 0) {
					this._active = false;
					if (this._duration === 0) if (this._rawPrevTime >= 0 && this._first) { //zero-duration timelines are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
						internalForce = true;
					}
					this._rawPrevTime = time;
				} else {
					this._rawPrevTime = (this._duration || !suppressEvents || time || this._rawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration timeline or tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.

					time = 0; //to avoid occasional floating point rounding errors (could cause problems especially with zero-duration tweens at the very beginning of the timeline)
					if (!this._initted) {
						internalForce = true;
					}
				}

			} else {
				this._totalTime = this._time = this._rawPrevTime = time;
			}
			if ((this._time === prevTime || !this._first) && !force && !internalForce) {
				return;
			} else if (!this._initted) {
				this._initted = true;
			}

			if (!this._active) if (!this._paused && this._time !== prevTime && time > 0) {
				this._active = true;  //so that if the user renders the timeline (as opposed to the parent timeline rendering it), it is forced to re-render and align it with the proper time/frame on the next rendering cycle. Maybe the timeline already finished but the user manually re-renders it as halfway done, for example.
			}

			if (prevTime === 0) if (this.vars.onStart) if (this._time !== 0) if (!suppressEvents) {
				this.vars.onStart.apply(this.vars.onStartScope || this, this.vars.onStartParams || _blankArray);
			}

			if (this._time >= prevTime) {
				tween = this._first;
				while (tween) {
					next = tween._next; //record it here because the value could change after rendering...
					if (this._paused && !prevPaused) { //in case a tween pauses the timeline when rendering
						break;
					} else if (tween._active || (tween._startTime <= this._time && !tween._paused && !tween._gc)) {
						if (!tween._reversed) {
							tween.render((time - tween._startTime) * tween._timeScale, suppressEvents, force);
						} else {
							tween.render(((!tween._dirty) ? tween._totalDuration : tween.totalDuration()) - ((time - tween._startTime) * tween._timeScale), suppressEvents, force);
						}
					}
					tween = next;
				}
			} else {
				tween = this._last;
				while (tween) {
					next = tween._prev; //record it here because the value could change after rendering...
					if (this._paused && !prevPaused) { //in case a tween pauses the timeline when rendering
						break;
					} else if (tween._active || (tween._startTime <= prevTime && !tween._paused && !tween._gc)) {
						if (!tween._reversed) {
							tween.render((time - tween._startTime) * tween._timeScale, suppressEvents, force);
						} else {
							tween.render(((!tween._dirty) ? tween._totalDuration : tween.totalDuration()) - ((time - tween._startTime) * tween._timeScale), suppressEvents, force);
						}
					}
					tween = next;
				}
			}

			if (this._onUpdate) if (!suppressEvents) {
				this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
			}

			if (callback) if (!this._gc) if (prevStart === this._startTime || prevTimeScale !== this._timeScale) if (this._time === 0 || totalDur >= this.totalDuration()) { //if one of the tweens that was rendered altered this timeline's startTime (like if an onComplete reversed the timeline), it probably isn't complete. If it is, don't worry, because whatever call altered the startTime would complete if it was necessary at the new time. The only exception is the timeScale property. Also check _gc because there's a chance that kill() could be called in an onUpdate
				if (isComplete) {
					if (this._timeline.autoRemoveChildren) {
						this._enabled(false, false);
					}
					this._active = false;
				}
				if (!suppressEvents && this.vars[callback]) {
					this.vars[callback].apply(this.vars[callback + "Scope"] || this, this.vars[callback + "Params"] || _blankArray);
				}
			}
		};

		p._hasPausedChild = function() {
			var tween = this._first;
			while (tween) {
				if (tween._paused || ((tween instanceof TimelineLite) && tween._hasPausedChild())) {
					return true;
				}
				tween = tween._next;
			}
			return false;
		};

		p.getChildren = function(nested, tweens, timelines, ignoreBeforeTime) {
			ignoreBeforeTime = ignoreBeforeTime || -9999999999;
			var a = [],
				tween = this._first,
				cnt = 0;
			while (tween) {
				if (tween._startTime < ignoreBeforeTime) {
					//do nothing
				} else if (tween instanceof TweenLite) {
					if (tweens !== false) {
						a[cnt++] = tween;
					}
				} else {
					if (timelines !== false) {
						a[cnt++] = tween;
					}
					if (nested !== false) {
						a = a.concat(tween.getChildren(true, tweens, timelines));
						cnt = a.length;
					}
				}
				tween = tween._next;
			}
			return a;
		};

		p.getTweensOf = function(target, nested) {
			var disabled = this._gc,
				a = [],
				cnt = 0,
				tweens, i;
			if (disabled) {
				this._enabled(true, true); //getTweensOf() filters out disabled tweens, and we have to mark them as _gc = true when the timeline completes in order to allow clean garbage collection, so temporarily re-enable the timeline here.
			}
			tweens = TweenLite.getTweensOf(target);
			i = tweens.length;
			while (--i > -1) {
				if (tweens[i].timeline === this || (nested && this._contains(tweens[i]))) {
					a[cnt++] = tweens[i];
				}
			}
			if (disabled) {
				this._enabled(false, true);
			}
			return a;
		};

		p._contains = function(tween) {
			var tl = tween.timeline;
			while (tl) {
				if (tl === this) {
					return true;
				}
				tl = tl.timeline;
			}
			return false;
		};

		p.shiftChildren = function(amount, adjustLabels, ignoreBeforeTime) {
			ignoreBeforeTime = ignoreBeforeTime || 0;
			var tween = this._first,
				labels = this._labels,
				p;
			while (tween) {
				if (tween._startTime >= ignoreBeforeTime) {
					tween._startTime += amount;
				}
				tween = tween._next;
			}
			if (adjustLabels) {
				for (p in labels) {
					if (labels[p] >= ignoreBeforeTime) {
						labels[p] += amount;
					}
				}
			}
			return this._uncache(true);
		};

		p._kill = function(vars, target) {
			if (!vars && !target) {
				return this._enabled(false, false);
			}
			var tweens = (!target) ? this.getChildren(true, true, false) : this.getTweensOf(target),
				i = tweens.length,
				changed = false;
			while (--i > -1) {
				if (tweens[i]._kill(vars, target)) {
					changed = true;
				}
			}
			return changed;
		};

		p.clear = function(labels) {
			var tweens = this.getChildren(false, true, true),
				i = tweens.length;
			this._time = this._totalTime = 0;
			while (--i > -1) {
				tweens[i]._enabled(false, false);
			}
			if (labels !== false) {
				this._labels = {};
			}
			return this._uncache(true);
		};

		p.invalidate = function() {
			var tween = this._first;
			while (tween) {
				tween.invalidate();
				tween = tween._next;
			}
			return this;
		};

		p._enabled = function(enabled, ignoreTimeline) {
			if (enabled === this._gc) {
				var tween = this._first;
				while (tween) {
					tween._enabled(enabled, true);
					tween = tween._next;
				}
			}
			return SimpleTimeline.prototype._enabled.call(this, enabled, ignoreTimeline);
		};

		p.duration = function(value) {
			if (!arguments.length) {
				if (this._dirty) {
					this.totalDuration(); //just triggers recalculation
				}
				return this._duration;
			}
			if (this.duration() !== 0 && value !== 0) {
				this.timeScale(this._duration / value);
			}
			return this;
		};

		p.totalDuration = function(value) {
			if (!arguments.length) {
				if (this._dirty) {
					var max = 0,
						tween = this._last,
						prevStart = 999999999999,
						prev, end;
					while (tween) {
						prev = tween._prev; //record it here in case the tween changes position in the sequence...
						if (tween._dirty) {
							tween.totalDuration(); //could change the tween._startTime, so make sure the tween's cache is clean before analyzing it.
						}
						if (tween._startTime > prevStart && this._sortChildren && !tween._paused) { //in case one of the tweens shifted out of order, it needs to be re-inserted into the correct position in the sequence
							this.add(tween, tween._startTime - tween._delay);
						} else {
							prevStart = tween._startTime;
						}
						if (tween._startTime < 0 && !tween._paused) { //children aren't allowed to have negative startTimes unless smoothChildTiming is true, so adjust here if one is found.
							max -= tween._startTime;
							if (this._timeline.smoothChildTiming) {
								this._startTime += tween._startTime / this._timeScale;
							}
							this.shiftChildren(-tween._startTime, false, -9999999999);
							prevStart = 0;
						}
						end = tween._startTime + (tween._totalDuration / tween._timeScale);
						if (end > max) {
							max = end;
						}
						tween = prev;
					}
					this._duration = this._totalDuration = max;
					this._dirty = false;
				}
				return this._totalDuration;
			}
			if (this.totalDuration() !== 0) if (value !== 0) {
				this.timeScale(this._totalDuration / value);
			}
			return this;
		};

		p.usesFrames = function() {
			var tl = this._timeline;
			while (tl._timeline) {
				tl = tl._timeline;
			}
			return (tl === Animation._rootFramesTimeline);
		};

		p.rawTime = function() {
			return this._paused ? this._totalTime : (this._timeline.rawTime() - this._startTime) * this._timeScale;
		};

		return TimelineLite;

	}, true);
	







	
	
	
	
	
/*
 * ----------------------------------------------------------------
 * TimelineMax
 * ----------------------------------------------------------------
 */
	window._gsDefine("TimelineMax", ["TimelineLite","TweenLite","easing.Ease"], function(TimelineLite, TweenLite, Ease) {

		var TimelineMax = function(vars) {
				TimelineLite.call(this, vars);
				this._repeat = this.vars.repeat || 0;
				this._repeatDelay = this.vars.repeatDelay || 0;
				this._cycle = 0;
				this._yoyo = (this.vars.yoyo === true);
				this._dirty = true;
			},
			_tinyNum = 0.0000000001,
			_blankArray = [],
			_easeNone = new Ease(null, null, 1, 0),
			p = TimelineMax.prototype = new TimelineLite();

		p.constructor = TimelineMax;
		p.kill()._gc = false;
		TimelineMax.version = "1.12.1";

		p.invalidate = function() {
			this._yoyo = (this.vars.yoyo === true);
			this._repeat = this.vars.repeat || 0;
			this._repeatDelay = this.vars.repeatDelay || 0;
			this._uncache(true);
			return TimelineLite.prototype.invalidate.call(this);
		};

		p.addCallback = function(callback, position, params, scope) {
			return this.add( TweenLite.delayedCall(0, callback, params, scope), position);
		};

		p.removeCallback = function(callback, position) {
			if (callback) {
				if (position == null) {
					this._kill(null, callback);
				} else {
					var a = this.getTweensOf(callback, false),
						i = a.length,
						time = this._parseTimeOrLabel(position);
					while (--i > -1) {
						if (a[i]._startTime === time) {
							a[i]._enabled(false, false);
						}
					}
				}
			}
			return this;
		};

		p.tweenTo = function(position, vars) {
			vars = vars || {};
			var copy = {ease:_easeNone, overwrite:(vars.delay ? 2 : 1), useFrames:this.usesFrames(), immediateRender:false},//note: set overwrite to 1 (true/all) by default unless there's a delay so that we avoid a racing situation that could happen if, for example, an onmousemove creates the same tweenTo() over and over again.
				duration, p, t;
			for (p in vars) {
				copy[p] = vars[p];
			}
			copy.time = this._parseTimeOrLabel(position);
			duration = (Math.abs(Number(copy.time) - this._time) / this._timeScale) || 0.001;
			t = new TweenLite(this, duration, copy);
			copy.onStart = function() {
				t.target.paused(true);
				if (t.vars.time !== t.target.time() && duration === t.duration()) { //don't make the duration zero - if it's supposed to be zero, don't worry because it's already initting the tween and will complete immediately, effectively making the duration zero anyway. If we make duration zero, the tween won't run at all.
					t.duration( Math.abs( t.vars.time - t.target.time()) / t.target._timeScale );
				}
				if (vars.onStart) { //in case the user had an onStart in the vars - we don't want to overwrite it.
					vars.onStart.apply(vars.onStartScope || t, vars.onStartParams || _blankArray);
				}
			};
			return t;
		};

		p.tweenFromTo = function(fromPosition, toPosition, vars) {
			vars = vars || {};
			fromPosition = this._parseTimeOrLabel(fromPosition);
			vars.startAt = {onComplete:this.seek, onCompleteParams:[fromPosition], onCompleteScope:this};
			vars.immediateRender = (vars.immediateRender !== false);
			var t = this.tweenTo(toPosition, vars);
			return t.duration((Math.abs( t.vars.time - fromPosition) / this._timeScale) || 0.001);
		};

		p.render = function(time, suppressEvents, force) {
			if (this._gc) {
				this._enabled(true, false);
			}
			var totalDur = (!this._dirty) ? this._totalDuration : this.totalDuration(),
				dur = this._duration,
				prevTime = this._time,
				prevTotalTime = this._totalTime,
				prevStart = this._startTime,
				prevTimeScale = this._timeScale,
				prevRawPrevTime = this._rawPrevTime,
				prevPaused = this._paused,
				prevCycle = this._cycle,
				tween, isComplete, next, callback, internalForce, cycleDuration;
			if (time >= totalDur) {
				if (!this._locked) {
					this._totalTime = totalDur;
					this._cycle = this._repeat;
				}
				if (!this._reversed) if (!this._hasPausedChild()) {
					isComplete = true;
					callback = "onComplete";
					if (this._duration === 0) if (time === 0 || prevRawPrevTime < 0 || prevRawPrevTime === _tinyNum) if (prevRawPrevTime !== time && this._first) {
						internalForce = true;
						if (prevRawPrevTime > _tinyNum) {
							callback = "onReverseComplete";
						}
					}
				}
				this._rawPrevTime = (this._duration || !suppressEvents || time || this._rawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration timeline or tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
				if (this._yoyo && (this._cycle & 1) !== 0) {
					this._time = time = 0;
				} else {
					this._time = dur;
					time = dur + 0.0001; //to avoid occasional floating point rounding errors - sometimes child tweens/timelines were not being fully completed (their progress might be 0.999999999999998 instead of 1 because when _time - tween._startTime is performed, floating point errors would return a value that was SLIGHTLY off). Try (999999999999.7 - 999999999999) * 1 = 0.699951171875 instead of 0.7. We cannot do less then 0.0001 because the same issue can occur when the duration is extremely large like 999999999999 in which case adding 0.00000001, for example, causes it to act like nothing was added.
				}

			} else if (time < 0.0000001) { //to work around occasional floating point math artifacts, round super small values to 0.
				if (!this._locked) {
					this._totalTime = this._cycle = 0;
				}
				this._time = 0;
				if (prevTime !== 0 || (dur === 0 && prevRawPrevTime !== _tinyNum && (prevRawPrevTime > 0 || (time < 0 && prevRawPrevTime >= 0)) && !this._locked)) { //edge case for checking time < 0 && prevRawPrevTime >= 0: a zero-duration fromTo() tween inside a zero-duration timeline (yeah, very rare)
					callback = "onReverseComplete";
					isComplete = this._reversed;
				}
				if (time < 0) {
					this._active = false;
					if (dur === 0) if (prevRawPrevTime >= 0 && this._first) { //zero-duration timelines are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
						internalForce = true;
					}
					this._rawPrevTime = time;
				} else {
					this._rawPrevTime = (dur || !suppressEvents || time || this._rawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration timeline or tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
					time = 0; //to avoid occasional floating point rounding errors (could cause problems especially with zero-duration tweens at the very beginning of the timeline)
					if (!this._initted) {
						internalForce = true;
					}
				}

			} else {
				if (dur === 0 && prevRawPrevTime < 0) { //without this, zero-duration repeating timelines (like with a simple callback nested at the very beginning and a repeatDelay) wouldn't render the first time through.
					internalForce = true;
				}
				this._time = this._rawPrevTime = time;
				if (!this._locked) {
					this._totalTime = time;
					if (this._repeat !== 0) {
						cycleDuration = dur + this._repeatDelay;
						this._cycle = (this._totalTime / cycleDuration) >> 0; //originally _totalTime % cycleDuration but floating point errors caused problems, so I normalized it. (4 % 0.8 should be 0 but it gets reported as 0.79999999!)
						if (this._cycle !== 0) if (this._cycle === this._totalTime / cycleDuration) {
							this._cycle--; //otherwise when rendered exactly at the end time, it will act as though it is repeating (at the beginning)
						}
						this._time = this._totalTime - (this._cycle * cycleDuration);
						if (this._yoyo) if ((this._cycle & 1) !== 0) {
							this._time = dur - this._time;
						}
						if (this._time > dur) {
							this._time = dur;
							time = dur + 0.0001; //to avoid occasional floating point rounding error
						} else if (this._time < 0) {
							this._time = time = 0;
						} else {
							time = this._time;
						}
					}
				}
			}

			if (this._cycle !== prevCycle) if (!this._locked) {
				/*
				make sure children at the end/beginning of the timeline are rendered properly. If, for example,
				a 3-second long timeline rendered at 2.9 seconds previously, and now renders at 3.2 seconds (which
				would get transated to 2.8 seconds if the timeline yoyos or 0.2 seconds if it just repeats), there
				could be a callback or a short tween that's at 2.95 or 3 seconds in which wouldn't render. So
				we need to push the timeline to the end (and/or beginning depending on its yoyo value). Also we must
				ensure that zero-duration tweens at the very beginning or end of the TimelineMax work.
				*/
				var backwards = (this._yoyo && (prevCycle & 1) !== 0),
					wrap = (backwards === (this._yoyo && (this._cycle & 1) !== 0)),
					recTotalTime = this._totalTime,
					recCycle = this._cycle,
					recRawPrevTime = this._rawPrevTime,
					recTime = this._time;

				this._totalTime = prevCycle * dur;
				if (this._cycle < prevCycle) {
					backwards = !backwards;
				} else {
					this._totalTime += dur;
				}
				this._time = prevTime; //temporarily revert _time so that render() renders the children in the correct order. Without this, tweens won't rewind correctly. We could arhictect things in a "cleaner" way by splitting out the rendering queue into a separate method but for performance reasons, we kept it all inside this method.

				this._rawPrevTime = (dur === 0) ? prevRawPrevTime - 0.0001 : prevRawPrevTime;
				this._cycle = prevCycle;
				this._locked = true; //prevents changes to totalTime and skips repeat/yoyo behavior when we recursively call render()
				prevTime = (backwards) ? 0 : dur;
				this.render(prevTime, suppressEvents, (dur === 0));
				if (!suppressEvents) if (!this._gc) {
					if (this.vars.onRepeat) {
						this.vars.onRepeat.apply(this.vars.onRepeatScope || this, this.vars.onRepeatParams || _blankArray);
					}
				}
				if (wrap) {
					prevTime = (backwards) ? dur + 0.0001 : -0.0001;
					this.render(prevTime, true, false);
				}
				this._locked = false;
				if (this._paused && !prevPaused) { //if the render() triggered callback that paused this timeline, we should abort (very rare, but possible)
					return;
				}
				this._time = recTime;
				this._totalTime = recTotalTime;
				this._cycle = recCycle;
				this._rawPrevTime = recRawPrevTime;
			}

			if ((this._time === prevTime || !this._first) && !force && !internalForce) {
				if (prevTotalTime !== this._totalTime) if (this._onUpdate) if (!suppressEvents) { //so that onUpdate fires even during the repeatDelay - as long as the totalTime changed, we should trigger onUpdate.
					this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
				}
				return;
			} else if (!this._initted) {
				this._initted = true;
			}

			if (!this._active) if (!this._paused && this._totalTime !== prevTotalTime && time > 0) {
				this._active = true;  //so that if the user renders the timeline (as opposed to the parent timeline rendering it), it is forced to re-render and align it with the proper time/frame on the next rendering cycle. Maybe the timeline already finished but the user manually re-renders it as halfway done, for example.
			}

			if (prevTotalTime === 0) if (this.vars.onStart) if (this._totalTime !== 0) if (!suppressEvents) {
				this.vars.onStart.apply(this.vars.onStartScope || this, this.vars.onStartParams || _blankArray);
			}

			if (this._time >= prevTime) {
				tween = this._first;
				while (tween) {
					next = tween._next; //record it here because the value could change after rendering...
					if (this._paused && !prevPaused) { //in case a tween pauses the timeline when rendering
						break;
					} else if (tween._active || (tween._startTime <= this._time && !tween._paused && !tween._gc)) {
						if (!tween._reversed) {
							tween.render((time - tween._startTime) * tween._timeScale, suppressEvents, force);
						} else {
							tween.render(((!tween._dirty) ? tween._totalDuration : tween.totalDuration()) - ((time - tween._startTime) * tween._timeScale), suppressEvents, force);
						}

					}
					tween = next;
				}
			} else {
				tween = this._last;
				while (tween) {
					next = tween._prev; //record it here because the value could change after rendering...
					if (this._paused && !prevPaused) { //in case a tween pauses the timeline when rendering
						break;
					} else if (tween._active || (tween._startTime <= prevTime && !tween._paused && !tween._gc)) {
						if (!tween._reversed) {
							tween.render((time - tween._startTime) * tween._timeScale, suppressEvents, force);
						} else {
							tween.render(((!tween._dirty) ? tween._totalDuration : tween.totalDuration()) - ((time - tween._startTime) * tween._timeScale), suppressEvents, force);
						}
					}
					tween = next;
				}
			}

			if (this._onUpdate) if (!suppressEvents) {
				this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
			}
			if (callback) if (!this._locked) if (!this._gc) if (prevStart === this._startTime || prevTimeScale !== this._timeScale) if (this._time === 0 || totalDur >= this.totalDuration()) { //if one of the tweens that was rendered altered this timeline's startTime (like if an onComplete reversed the timeline), it probably isn't complete. If it is, don't worry, because whatever call altered the startTime would complete if it was necessary at the new time. The only exception is the timeScale property. Also check _gc because there's a chance that kill() could be called in an onUpdate
				if (isComplete) {
					if (this._timeline.autoRemoveChildren) {
						this._enabled(false, false);
					}
					this._active = false;
				}
				if (!suppressEvents && this.vars[callback]) {
					this.vars[callback].apply(this.vars[callback + "Scope"] || this, this.vars[callback + "Params"] || _blankArray);
				}
			}
		};

		p.getActive = function(nested, tweens, timelines) {
			if (nested == null) {
				nested = true;
			}
			if (tweens == null) {
				tweens = true;
			}
			if (timelines == null) {
				timelines = false;
			}
			var a = [],
				all = this.getChildren(nested, tweens, timelines),
				cnt = 0,
				l = all.length,
				i, tween;
			for (i = 0; i < l; i++) {
				tween = all[i];
				if (tween.isActive()) {
					a[cnt++] = tween;
				}
			}
			return a;
		};


		p.getLabelAfter = function(time) {
			if (!time) if (time !== 0) { //faster than isNan()
				time = this._time;
			}
			var labels = this.getLabelsArray(),
				l = labels.length,
				i;
			for (i = 0; i < l; i++) {
				if (labels[i].time > time) {
					return labels[i].name;
				}
			}
			return null;
		};

		p.getLabelBefore = function(time) {
			if (time == null) {
				time = this._time;
			}
			var labels = this.getLabelsArray(),
				i = labels.length;
			while (--i > -1) {
				if (labels[i].time < time) {
					return labels[i].name;
				}
			}
			return null;
		};

		p.getLabelsArray = function() {
			var a = [],
				cnt = 0,
				p;
			for (p in this._labels) {
				a[cnt++] = {time:this._labels[p], name:p};
			}
			a.sort(function(a,b) {
				return a.time - b.time;
			});
			return a;
		};


//---- GETTERS / SETTERS -------------------------------------------------------------------------------------------------------

		p.progress = function(value) {
			return (!arguments.length) ? this._time / this.duration() : this.totalTime( this.duration() * ((this._yoyo && (this._cycle & 1) !== 0) ? 1 - value : value) + (this._cycle * (this._duration + this._repeatDelay)), false);
		};

		p.totalProgress = function(value) {
			return (!arguments.length) ? this._totalTime / this.totalDuration() : this.totalTime( this.totalDuration() * value, false);
		};

		p.totalDuration = function(value) {
			if (!arguments.length) {
				if (this._dirty) {
					TimelineLite.prototype.totalDuration.call(this); //just forces refresh
					//Instead of Infinity, we use 999999999999 so that we can accommodate reverses.
					this._totalDuration = (this._repeat === -1) ? 999999999999 : this._duration * (this._repeat + 1) + (this._repeatDelay * this._repeat);
				}
				return this._totalDuration;
			}
			return (this._repeat === -1) ? this : this.duration( (value - (this._repeat * this._repeatDelay)) / (this._repeat + 1) );
		};

		p.time = function(value, suppressEvents) {
			if (!arguments.length) {
				return this._time;
			}
			if (this._dirty) {
				this.totalDuration();
			}
			if (value > this._duration) {
				value = this._duration;
			}
			if (this._yoyo && (this._cycle & 1) !== 0) {
				value = (this._duration - value) + (this._cycle * (this._duration + this._repeatDelay));
			} else if (this._repeat !== 0) {
				value += this._cycle * (this._duration + this._repeatDelay);
			}
			return this.totalTime(value, suppressEvents);
		};

		p.repeat = function(value) {
			if (!arguments.length) {
				return this._repeat;
			}
			this._repeat = value;
			return this._uncache(true);
		};

		p.repeatDelay = function(value) {
			if (!arguments.length) {
				return this._repeatDelay;
			}
			this._repeatDelay = value;
			return this._uncache(true);
		};

		p.yoyo = function(value) {
			if (!arguments.length) {
				return this._yoyo;
			}
			this._yoyo = value;
			return this;
		};

		p.currentLabel = function(value) {
			if (!arguments.length) {
				return this.getLabelBefore(this._time + 0.00000001);
			}
			return this.seek(value, true);
		};

		return TimelineMax;

	}, true);
	




	
	
	
	
	
	
	
/*
 * ----------------------------------------------------------------
 * BezierPlugin
 * ----------------------------------------------------------------
 */
	(function() {

		var _RAD2DEG = 180 / Math.PI,
			_r1 = [],
			_r2 = [],
			_r3 = [],
			_corProps = {},
			Segment = function(a, b, c, d) {
				this.a = a;
				this.b = b;
				this.c = c;
				this.d = d;
				this.da = d - a;
				this.ca = c - a;
				this.ba = b - a;
			},
			_correlate = ",x,y,z,left,top,right,bottom,marginTop,marginLeft,marginRight,marginBottom,paddingLeft,paddingTop,paddingRight,paddingBottom,backgroundPosition,backgroundPosition_y,",
			cubicToQuadratic = function(a, b, c, d) {
				var q1 = {a:a},
					q2 = {},
					q3 = {},
					q4 = {c:d},
					mab = (a + b) / 2,
					mbc = (b + c) / 2,
					mcd = (c + d) / 2,
					mabc = (mab + mbc) / 2,
					mbcd = (mbc + mcd) / 2,
					m8 = (mbcd - mabc) / 8;
				q1.b = mab + (a - mab) / 4;
				q2.b = mabc + m8;
				q1.c = q2.a = (q1.b + q2.b) / 2;
				q2.c = q3.a = (mabc + mbcd) / 2;
				q3.b = mbcd - m8;
				q4.b = mcd + (d - mcd) / 4;
				q3.c = q4.a = (q3.b + q4.b) / 2;
				return [q1, q2, q3, q4];
			},
			_calculateControlPoints = function(a, curviness, quad, basic, correlate) {
				var l = a.length - 1,
					ii = 0,
					cp1 = a[0].a,
					i, p1, p2, p3, seg, m1, m2, mm, cp2, qb, r1, r2, tl;
				for (i = 0; i < l; i++) {
					seg = a[ii];
					p1 = seg.a;
					p2 = seg.d;
					p3 = a[ii+1].d;

					if (correlate) {
						r1 = _r1[i];
						r2 = _r2[i];
						tl = ((r2 + r1) * curviness * 0.25) / (basic ? 0.5 : _r3[i] || 0.5);
						m1 = p2 - (p2 - p1) * (basic ? curviness * 0.5 : (r1 !== 0 ? tl / r1 : 0));
						m2 = p2 + (p3 - p2) * (basic ? curviness * 0.5 : (r2 !== 0 ? tl / r2 : 0));
						mm = p2 - (m1 + (((m2 - m1) * ((r1 * 3 / (r1 + r2)) + 0.5) / 4) || 0));
					} else {
						m1 = p2 - (p2 - p1) * curviness * 0.5;
						m2 = p2 + (p3 - p2) * curviness * 0.5;
						mm = p2 - (m1 + m2) / 2;
					}
					m1 += mm;
					m2 += mm;

					seg.c = cp2 = m1;
					if (i !== 0) {
						seg.b = cp1;
					} else {
						seg.b = cp1 = seg.a + (seg.c - seg.a) * 0.6; //instead of placing b on a exactly, we move it inline with c so that if the user specifies an ease like Back.easeIn or Elastic.easeIn which goes BEYOND the beginning, it will do so smoothly.
					}

					seg.da = p2 - p1;
					seg.ca = cp2 - p1;
					seg.ba = cp1 - p1;

					if (quad) {
						qb = cubicToQuadratic(p1, cp1, cp2, p2);
						a.splice(ii, 1, qb[0], qb[1], qb[2], qb[3]);
						ii += 4;
					} else {
						ii++;
					}

					cp1 = m2;
				}
				seg = a[ii];
				seg.b = cp1;
				seg.c = cp1 + (seg.d - cp1) * 0.4; //instead of placing c on d exactly, we move it inline with b so that if the user specifies an ease like Back.easeOut or Elastic.easeOut which goes BEYOND the end, it will do so smoothly.
				seg.da = seg.d - seg.a;
				seg.ca = seg.c - seg.a;
				seg.ba = cp1 - seg.a;
				if (quad) {
					qb = cubicToQuadratic(seg.a, cp1, seg.c, seg.d);
					a.splice(ii, 1, qb[0], qb[1], qb[2], qb[3]);
				}
			},
			_parseAnchors = function(values, p, correlate, prepend) {
				var a = [],
					l, i, p1, p2, p3, tmp;
				if (prepend) {
					values = [prepend].concat(values);
					i = values.length;
					while (--i > -1) {
						if (typeof( (tmp = values[i][p]) ) === "string") if (tmp.charAt(1) === "=") {
							values[i][p] = prepend[p] + Number(tmp.charAt(0) + tmp.substr(2)); //accommodate relative values. Do it inline instead of breaking it out into a function for speed reasons
						}
					}
				}
				l = values.length - 2;
				if (l < 0) {
					a[0] = new Segment(values[0][p], 0, 0, values[(l < -1) ? 0 : 1][p]);
					return a;
				}
				for (i = 0; i < l; i++) {
					p1 = values[i][p];
					p2 = values[i+1][p];
					a[i] = new Segment(p1, 0, 0, p2);
					if (correlate) {
						p3 = values[i+2][p];
						_r1[i] = (_r1[i] || 0) + (p2 - p1) * (p2 - p1);
						_r2[i] = (_r2[i] || 0) + (p3 - p2) * (p3 - p2);
					}
				}
				a[i] = new Segment(values[i][p], 0, 0, values[i+1][p]);
				return a;
			},
			bezierThrough = function(values, curviness, quadratic, basic, correlate, prepend) {
				var obj = {},
					props = [],
					first = prepend || values[0],
					i, p, a, j, r, l, seamless, last;
				correlate = (typeof(correlate) === "string") ? ","+correlate+"," : _correlate;
				if (curviness == null) {
					curviness = 1;
				}
				for (p in values[0]) {
					props.push(p);
				}
				//check to see if the last and first values are identical (well, within 0.05). If so, make seamless by appending the second element to the very end of the values array and the 2nd-to-last element to the very beginning (we'll remove those segments later)
				if (values.length > 1) {
					last = values[values.length - 1];
					seamless = true;
					i = props.length;
					while (--i > -1) {
						p = props[i];
						if (Math.abs(first[p] - last[p]) > 0.05) { //build in a tolerance of +/-0.05 to accommodate rounding errors. For example, if you set an object's position to 4.945, Flash will make it 4.9
							seamless = false;
							break;
						}
					}
					if (seamless) {
						values = values.concat(); //duplicate the array to avoid contaminating the original which the user may be reusing for other tweens
						if (prepend) {
							values.unshift(prepend);
						}
						values.push(values[1]);
						prepend = values[values.length - 3];
					}
				}
				_r1.length = _r2.length = _r3.length = 0;
				i = props.length;
				while (--i > -1) {
					p = props[i];
					_corProps[p] = (correlate.indexOf(","+p+",") !== -1);
					obj[p] = _parseAnchors(values, p, _corProps[p], prepend);
				}
				i = _r1.length;
				while (--i > -1) {
					_r1[i] = Math.sqrt(_r1[i]);
					_r2[i] = Math.sqrt(_r2[i]);
				}
				if (!basic) {
					i = props.length;
					while (--i > -1) {
						if (_corProps[p]) {
							a = obj[props[i]];
							l = a.length - 1;
							for (j = 0; j < l; j++) {
								r = a[j+1].da / _r2[j] + a[j].da / _r1[j];
								_r3[j] = (_r3[j] || 0) + r * r;
							}
						}
					}
					i = _r3.length;
					while (--i > -1) {
						_r3[i] = Math.sqrt(_r3[i]);
					}
				}
				i = props.length;
				j = quadratic ? 4 : 1;
				while (--i > -1) {
					p = props[i];
					a = obj[p];
					_calculateControlPoints(a, curviness, quadratic, basic, _corProps[p]); //this method requires that _parseAnchors() and _setSegmentRatios() ran first so that _r1, _r2, and _r3 values are populated for all properties
					if (seamless) {
						a.splice(0, j);
						a.splice(a.length - j, j);
					}
				}
				return obj;
			},
			_parseBezierData = function(values, type, prepend) {
				type = type || "soft";
				var obj = {},
					inc = (type === "cubic") ? 3 : 2,
					soft = (type === "soft"),
					props = [],
					a, b, c, d, cur, i, j, l, p, cnt, tmp;
				if (soft && prepend) {
					values = [prepend].concat(values);
				}
				if (values == null || values.length < inc + 1) { throw "invalid Bezier data"; }
				for (p in values[0]) {
					props.push(p);
				}
				i = props.length;
				while (--i > -1) {
					p = props[i];
					obj[p] = cur = [];
					cnt = 0;
					l = values.length;
					for (j = 0; j < l; j++) {
						a = (prepend == null) ? values[j][p] : (typeof( (tmp = values[j][p]) ) === "string" && tmp.charAt(1) === "=") ? prepend[p] + Number(tmp.charAt(0) + tmp.substr(2)) : Number(tmp);
						if (soft) if (j > 1) if (j < l - 1) {
							cur[cnt++] = (a + cur[cnt-2]) / 2;
						}
						cur[cnt++] = a;
					}
					l = cnt - inc + 1;
					cnt = 0;
					for (j = 0; j < l; j += inc) {
						a = cur[j];
						b = cur[j+1];
						c = cur[j+2];
						d = (inc === 2) ? 0 : cur[j+3];
						cur[cnt++] = tmp = (inc === 3) ? new Segment(a, b, c, d) : new Segment(a, (2 * b + a) / 3, (2 * b + c) / 3, c);
					}
					cur.length = cnt;
				}
				return obj;
			},
			_addCubicLengths = function(a, steps, resolution) {
				var inc = 1 / resolution,
					j = a.length,
					d, d1, s, da, ca, ba, p, i, inv, bez, index;
				while (--j > -1) {
					bez = a[j];
					s = bez.a;
					da = bez.d - s;
					ca = bez.c - s;
					ba = bez.b - s;
					d = d1 = 0;
					for (i = 1; i <= resolution; i++) {
						p = inc * i;
						inv = 1 - p;
						d = d1 - (d1 = (p * p * da + 3 * inv * (p * ca + inv * ba)) * p);
						index = j * resolution + i - 1;
						steps[index] = (steps[index] || 0) + d * d;
					}
				}
			},
			_parseLengthData = function(obj, resolution) {
				resolution = resolution >> 0 || 6;
				var a = [],
					lengths = [],
					d = 0,
					total = 0,
					threshold = resolution - 1,
					segments = [],
					curLS = [], //current length segments array
					p, i, l, index;
				for (p in obj) {
					_addCubicLengths(obj[p], a, resolution);
				}
				l = a.length;
				for (i = 0; i < l; i++) {
					d += Math.sqrt(a[i]);
					index = i % resolution;
					curLS[index] = d;
					if (index === threshold) {
						total += d;
						index = (i / resolution) >> 0;
						segments[index] = curLS;
						lengths[index] = total;
						d = 0;
						curLS = [];
					}
				}
				return {length:total, lengths:lengths, segments:segments};
			},



			BezierPlugin = window._gsDefine.plugin({
					propName: "bezier",
					priority: -1,
					version: "1.3.2",
					API: 2,
					global:true,

					//gets called when the tween renders for the first time. This is where initial values should be recorded and any setup routines should run.
					init: function(target, vars, tween) {
						this._target = target;
						if (vars instanceof Array) {
							vars = {values:vars};
						}
						this._func = {};
						this._round = {};
						this._props = [];
						this._timeRes = (vars.timeResolution == null) ? 6 : parseInt(vars.timeResolution, 10);
						var values = vars.values || [],
							first = {},
							second = values[0],
							autoRotate = vars.autoRotate || tween.vars.orientToBezier,
							p, isFunc, i, j, prepend;

						this._autoRotate = autoRotate ? (autoRotate instanceof Array) ? autoRotate : [["x","y","rotation",((autoRotate === true) ? 0 : Number(autoRotate) || 0)]] : null;
						for (p in second) {
							this._props.push(p);
						}

						i = this._props.length;
						while (--i > -1) {
							p = this._props[i];

							this._overwriteProps.push(p);
							isFunc = this._func[p] = (typeof(target[p]) === "function");
							first[p] = (!isFunc) ? parseFloat(target[p]) : target[ ((p.indexOf("set") || typeof(target["get" + p.substr(3)]) !== "function") ? p : "get" + p.substr(3)) ]();
							if (!prepend) if (first[p] !== values[0][p]) {
								prepend = first;
							}
						}
						this._beziers = (vars.type !== "cubic" && vars.type !== "quadratic" && vars.type !== "soft") ? bezierThrough(values, isNaN(vars.curviness) ? 1 : vars.curviness, false, (vars.type === "thruBasic"), vars.correlate, prepend) : _parseBezierData(values, vars.type, first);
						this._segCount = this._beziers[p].length;

						if (this._timeRes) {
							var ld = _parseLengthData(this._beziers, this._timeRes);
							this._length = ld.length;
							this._lengths = ld.lengths;
							this._segments = ld.segments;
							this._l1 = this._li = this._s1 = this._si = 0;
							this._l2 = this._lengths[0];
							this._curSeg = this._segments[0];
							this._s2 = this._curSeg[0];
							this._prec = 1 / this._curSeg.length;
						}

						if ((autoRotate = this._autoRotate)) {
							this._initialRotations = [];
							if (!(autoRotate[0] instanceof Array)) {
								this._autoRotate = autoRotate = [autoRotate];
							}
							i = autoRotate.length;
							while (--i > -1) {
								for (j = 0; j < 3; j++) {
									p = autoRotate[i][j];
									this._func[p] = (typeof(target[p]) === "function") ? target[ ((p.indexOf("set") || typeof(target["get" + p.substr(3)]) !== "function") ? p : "get" + p.substr(3)) ] : false;
								}
								p = autoRotate[i][2];
								this._initialRotations[i] = this._func[p] ? this._func[p].call(this._target) : this._target[p];
							}
						}
						this._startRatio = tween.vars.runBackwards ? 1 : 0; //we determine the starting ratio when the tween inits which is always 0 unless the tween has runBackwards:true (indicating it's a from() tween) in which case it's 1.
						return true;
					},

					//called each time the values should be updated, and the ratio gets passed as the only parameter (typically it's a value between 0 and 1, but it can exceed those when using an ease like Elastic.easeOut or Back.easeOut, etc.)
					set: function(v) {
						var segments = this._segCount,
							func = this._func,
							target = this._target,
							notStart = (v !== this._startRatio),
							curIndex, inv, i, p, b, t, val, l, lengths, curSeg;
						if (!this._timeRes) {
							curIndex = (v < 0) ? 0 : (v >= 1) ? segments - 1 : (segments * v) >> 0;
							t = (v - (curIndex * (1 / segments))) * segments;
						} else {
							lengths = this._lengths;
							curSeg = this._curSeg;
							v *= this._length;
							i = this._li;
							//find the appropriate segment (if the currently cached one isn't correct)
							if (v > this._l2 && i < segments - 1) {
								l = segments - 1;
								while (i < l && (this._l2 = lengths[++i]) <= v) {	}
								this._l1 = lengths[i-1];
								this._li = i;
								this._curSeg = curSeg = this._segments[i];
								this._s2 = curSeg[(this._s1 = this._si = 0)];
							} else if (v < this._l1 && i > 0) {
								while (i > 0 && (this._l1 = lengths[--i]) >= v) { }
								if (i === 0 && v < this._l1) {
									this._l1 = 0;
								} else {
									i++;
								}
								this._l2 = lengths[i];
								this._li = i;
								this._curSeg = curSeg = this._segments[i];
								this._s1 = curSeg[(this._si = curSeg.length - 1) - 1] || 0;
								this._s2 = curSeg[this._si];
							}
							curIndex = i;
							//now find the appropriate sub-segment (we split it into the number of pieces that was defined by "precision" and measured each one)
							v -= this._l1;
							i = this._si;
							if (v > this._s2 && i < curSeg.length - 1) {
								l = curSeg.length - 1;
								while (i < l && (this._s2 = curSeg[++i]) <= v) {	}
								this._s1 = curSeg[i-1];
								this._si = i;
							} else if (v < this._s1 && i > 0) {
								while (i > 0 && (this._s1 = curSeg[--i]) >= v) {	}
								if (i === 0 && v < this._s1) {
									this._s1 = 0;
								} else {
									i++;
								}
								this._s2 = curSeg[i];
								this._si = i;
							}
							t = (i + (v - this._s1) / (this._s2 - this._s1)) * this._prec;
						}
						inv = 1 - t;

						i = this._props.length;
						while (--i > -1) {
							p = this._props[i];
							b = this._beziers[p][curIndex];
							val = (t * t * b.da + 3 * inv * (t * b.ca + inv * b.ba)) * t + b.a;
							if (this._round[p]) {
								val = Math.round(val);
							}
							if (func[p]) {
								target[p](val);
							} else {
								target[p] = val;
							}
						}

						if (this._autoRotate) {
							var ar = this._autoRotate,
								b2, x1, y1, x2, y2, add, conv;
							i = ar.length;
							while (--i > -1) {
								p = ar[i][2];
								add = ar[i][3] || 0;
								conv = (ar[i][4] === true) ? 1 : _RAD2DEG;
								b = this._beziers[ar[i][0]];
								b2 = this._beziers[ar[i][1]];

								if (b && b2) { //in case one of the properties got overwritten.
									b = b[curIndex];
									b2 = b2[curIndex];

									x1 = b.a + (b.b - b.a) * t;
									x2 = b.b + (b.c - b.b) * t;
									x1 += (x2 - x1) * t;
									x2 += ((b.c + (b.d - b.c) * t) - x2) * t;

									y1 = b2.a + (b2.b - b2.a) * t;
									y2 = b2.b + (b2.c - b2.b) * t;
									y1 += (y2 - y1) * t;
									y2 += ((b2.c + (b2.d - b2.c) * t) - y2) * t;

									val = notStart ? Math.atan2(y2 - y1, x2 - x1) * conv + add : this._initialRotations[i];

									if (func[p]) {
										target[p](val);
									} else {
										target[p] = val;
									}
								}
							}
						}
					}
			}),
			p = BezierPlugin.prototype;


		BezierPlugin.bezierThrough = bezierThrough;
		BezierPlugin.cubicToQuadratic = cubicToQuadratic;
		BezierPlugin._autoCSS = true; //indicates that this plugin can be inserted into the "css" object using the autoCSS feature of TweenLite
		BezierPlugin.quadraticToCubic = function(a, b, c) {
			return new Segment(a, (2 * b + a) / 3, (2 * b + c) / 3, c);
		};

		BezierPlugin._cssRegister = function() {
			var CSSPlugin = window._gsDefine.globals.CSSPlugin;
			if (!CSSPlugin) {
				return;
			}
			var _internals = CSSPlugin._internals,
				_parseToProxy = _internals._parseToProxy,
				_setPluginRatio = _internals._setPluginRatio,
				CSSPropTween = _internals.CSSPropTween;
			_internals._registerComplexSpecialProp("bezier", {parser:function(t, e, prop, cssp, pt, plugin) {
				if (e instanceof Array) {
					e = {values:e};
				}
				plugin = new BezierPlugin();
				var values = e.values,
					l = values.length - 1,
					pluginValues = [],
					v = {},
					i, p, data;
				if (l < 0) {
					return pt;
				}
				for (i = 0; i <= l; i++) {
					data = _parseToProxy(t, values[i], cssp, pt, plugin, (l !== i));
					pluginValues[i] = data.end;
				}
				for (p in e) {
					v[p] = e[p]; //duplicate the vars object because we need to alter some things which would cause problems if the user plans to reuse the same vars object for another tween.
				}
				v.values = pluginValues;
				pt = new CSSPropTween(t, "bezier", 0, 0, data.pt, 2);
				pt.data = data;
				pt.plugin = plugin;
				pt.setRatio = _setPluginRatio;
				if (v.autoRotate === 0) {
					v.autoRotate = true;
				}
				if (v.autoRotate && !(v.autoRotate instanceof Array)) {
					i = (v.autoRotate === true) ? 0 : Number(v.autoRotate);
					v.autoRotate = (data.end.left != null) ? [["left","top","rotation",i,false]] : (data.end.x != null) ? [["x","y","rotation",i,false]] : false;
				}
				if (v.autoRotate) {
					if (!cssp._transform) {
						cssp._enableTransforms(false);
					}
					data.autoRotate = cssp._target._gsTransform;
				}
				plugin._onInitTween(data.proxy, v, cssp._tween);
				return pt;
			}});
		};

		p._roundProps = function(lookup, value) {
			var op = this._overwriteProps,
				i = op.length;
			while (--i > -1) {
				if (lookup[op[i]] || lookup.bezier || lookup.bezierThrough) {
					this._round[op[i]] = value;
				}
			}
		};

		p._kill = function(lookup) {
			var a = this._props,
				p, i;
			for (p in this._beziers) {
				if (p in lookup) {
					delete this._beziers[p];
					delete this._func[p];
					i = a.length;
					while (--i > -1) {
						if (a[i] === p) {
							a.splice(i, 1);
						}
					}
				}
			}
			return this._super._kill.call(this, lookup);
		};

	}());






	
	
	
	
	
	
	
	
/*
 * ----------------------------------------------------------------
 * CSSPlugin
 * ----------------------------------------------------------------
 */
	window._gsDefine("plugins.CSSPlugin", ["plugins.TweenPlugin","TweenLite"], function(TweenPlugin, TweenLite) {

		/** @constructor **/
		var CSSPlugin = function() {
				TweenPlugin.call(this, "css");
				this._overwriteProps.length = 0;
				this.setRatio = CSSPlugin.prototype.setRatio; //speed optimization (avoid prototype lookup on this "hot" method)
			},
			_hasPriority, //turns true whenever a CSSPropTween instance is created that has a priority other than 0. This helps us discern whether or not we should spend the time organizing the linked list or not after a CSSPlugin's _onInitTween() method is called.
			_suffixMap, //we set this in _onInitTween() each time as a way to have a persistent variable we can use in other methods like _parse() without having to pass it around as a parameter and we keep _parse() decoupled from a particular CSSPlugin instance
			_cs, //computed style (we store this in a shared variable to conserve memory and make minification tighter
			_overwriteProps, //alias to the currently instantiating CSSPlugin's _overwriteProps array. We use this closure in order to avoid having to pass a reference around from method to method and aid in minification.
			_specialProps = {},
			p = CSSPlugin.prototype = new TweenPlugin("css");

		p.constructor = CSSPlugin;
		CSSPlugin.version = "1.12.1";
		CSSPlugin.API = 2;
		CSSPlugin.defaultTransformPerspective = 0;
		CSSPlugin.defaultSkewType = "compensated";
		p = "px"; //we'll reuse the "p" variable to keep file size down
		CSSPlugin.suffixMap = {top:p, right:p, bottom:p, left:p, width:p, height:p, fontSize:p, padding:p, margin:p, perspective:p, lineHeight:""};


		var _numExp = /(?:\d|\-\d|\.\d|\-\.\d)+/g,
			_relNumExp = /(?:\d|\-\d|\.\d|\-\.\d|\+=\d|\-=\d|\+=.\d|\-=\.\d)+/g,
			_valuesExp = /(?:\+=|\-=|\-|\b)[\d\-\.]+[a-zA-Z0-9]*(?:%|\b)/gi, //finds all the values that begin with numbers or += or -= and then a number. Includes suffixes. We use this to split complex values apart like "1px 5px 20px rgb(255,102,51)"
			_NaNExp = /[^\d\-\.]/g,
			_suffixExp = /(?:\d|\-|\+|=|#|\.)*/g,
			_opacityExp = /opacity *= *([^)]*)/i,
			_opacityValExp = /opacity:([^;]*)/i,
			_alphaFilterExp = /alpha\(opacity *=.+?\)/i,
			_rgbhslExp = /^(rgb|hsl)/,
			_capsExp = /([A-Z])/g,
			_camelExp = /-([a-z])/gi,
			_urlExp = /(^(?:url\(\"|url\())|(?:(\"\))$|\)$)/gi, //for pulling out urls from url(...) or url("...") strings (some browsers wrap urls in quotes, some don't when reporting things like backgroundImage)
			_camelFunc = function(s, g) { return g.toUpperCase(); },
			_horizExp = /(?:Left|Right|Width)/i,
			_ieGetMatrixExp = /(M11|M12|M21|M22)=[\d\-\.e]+/gi,
			_ieSetMatrixExp = /progid\:DXImageTransform\.Microsoft\.Matrix\(.+?\)/i,
			_commasOutsideParenExp = /,(?=[^\)]*(?:\(|$))/gi, //finds any commas that are not within parenthesis
			_DEG2RAD = Math.PI / 180,
			_RAD2DEG = 180 / Math.PI,
			_forcePT = {},
			_doc = document,
			_tempDiv = _doc.createElement("div"),
			_tempImg = _doc.createElement("img"),
			_internals = CSSPlugin._internals = {_specialProps:_specialProps}, //provides a hook to a few internal methods that we need to access from inside other plugins
			_agent = navigator.userAgent,
			_autoRound,
			_reqSafariFix, //we won't apply the Safari transform fix until we actually come across a tween that affects a transform property (to maintain best performance).

			_isSafari,
			_isFirefox, //Firefox has a bug that causes 3D transformed elements to randomly disappear unless a repaint is forced after each update on each element.
			_isSafariLT6, //Safari (and Android 4 which uses a flavor of Safari) has a bug that prevents changes to "top" and "left" properties from rendering properly if changed on the same frame as a transform UNLESS we set the element's WebkitBackfaceVisibility to hidden (weird, I know). Doing this for Android 3 and earlier seems to actually cause other problems, though (fun!)
			_ieVers,
			_supportsOpacity = (function() { //we set _isSafari, _ieVers, _isFirefox, and _supportsOpacity all in one function here to reduce file size slightly, especially in the minified version.
				var i = _agent.indexOf("Android"),
					d = _doc.createElement("div"), a;

				_isSafari = (_agent.indexOf("Safari") !== -1 && _agent.indexOf("Chrome") === -1 && (i === -1 || Number(_agent.substr(i+8, 1)) > 3));
				_isSafariLT6 = (_isSafari && (Number(_agent.substr(_agent.indexOf("Version/")+8, 1)) < 6));
				_isFirefox = (_agent.indexOf("Firefox") !== -1);

				if ((/MSIE ([0-9]{1,}[\.0-9]{0,})/).exec(_agent)) {
					_ieVers = parseFloat( RegExp.$1 );
				}

				d.innerHTML = "<a style='top:1px;opacity:.55;'>a</a>";
				a = d.getElementsByTagName("a")[0];
				return a ? /^0.55/.test(a.style.opacity) : false;
			}()),
			_getIEOpacity = function(v) {
				return (_opacityExp.test( ((typeof(v) === "string") ? v : (v.currentStyle ? v.currentStyle.filter : v.style.filter) || "") ) ? ( parseFloat( RegExp.$1 ) / 100 ) : 1);
			},
			_log = function(s) {//for logging messages, but in a way that won't throw errors in old versions of IE.
				if (window.console) {
					//console.log(s);
				}
			},
			_prefixCSS = "", //the non-camelCase vendor prefix like "-o-", "-moz-", "-ms-", or "-webkit-"
			_prefix = "", //camelCase vendor prefix like "O", "ms", "Webkit", or "Moz".

			// @private feed in a camelCase property name like "transform" and it will check to see if it is valid as-is or if it needs a vendor prefix. It returns the corrected camelCase property name (i.e. "WebkitTransform" or "MozTransform" or "transform" or null if no such property is found, like if the browser is IE8 or before, "transform" won't be found at all)
			_checkPropPrefix = function(p, e) {
				e = e || _tempDiv;
				var s = e.style,
					a, i;
				if (s[p] !== undefined) {
					return p;
				}
				p = p.charAt(0).toUpperCase() + p.substr(1);
				a = ["O","Moz","ms","Ms","Webkit"];
				i = 5;
				while (--i > -1 && s[a[i]+p] === undefined) { }
				if (i >= 0) {
					_prefix = (i === 3) ? "ms" : a[i];
					_prefixCSS = "-" + _prefix.toLowerCase() + "-";
					return _prefix + p;
				}
				return null;
			},

			_getComputedStyle = _doc.defaultView ? _doc.defaultView.getComputedStyle : function() {},

			/**
			 * @private Returns the css style for a particular property of an element. For example, to get whatever the current "left" css value for an element with an ID of "myElement", you could do:
			 * var currentLeft = CSSPlugin.getStyle( document.getElementById("myElement"), "left");
			 *
			 * @param {!Object} t Target element whose style property you want to query
			 * @param {!string} p Property name (like "left" or "top" or "marginTop", etc.)
			 * @param {Object=} cs Computed style object. This just provides a way to speed processing if you're going to get several properties on the same element in quick succession - you can reuse the result of the getComputedStyle() call.
			 * @param {boolean=} calc If true, the value will not be read directly from the element's "style" property (if it exists there), but instead the getComputedStyle() result will be used. This can be useful when you want to ensure that the browser itself is interpreting the value.
			 * @param {string=} dflt Default value that should be returned in the place of null, "none", "auto" or "auto auto".
			 * @return {?string} The current property value
			 */
			_getStyle = CSSPlugin.getStyle = function(t, p, cs, calc, dflt) {
				var rv;
				if (!_supportsOpacity) if (p === "opacity") { //several versions of IE don't use the standard "opacity" property - they use things like filter:alpha(opacity=50), so we parse that here.
					return _getIEOpacity(t);
				}
				if (!calc && t.style[p]) {
					rv = t.style[p];
				} else if ((cs = cs || _getComputedStyle(t))) {
					rv = cs[p] || cs.getPropertyValue(p) || cs.getPropertyValue(p.replace(_capsExp, "-$1").toLowerCase());
				} else if (t.currentStyle) {
					rv = t.currentStyle[p];
				}
				return (dflt != null && (!rv || rv === "none" || rv === "auto" || rv === "auto auto")) ? dflt : rv;
			},

			/**
			 * @private Pass the target element, the property name, the numeric value, and the suffix (like "%", "em", "px", etc.) and it will spit back the equivalent pixel number.
			 * @param {!Object} t Target element
			 * @param {!string} p Property name (like "left", "top", "marginLeft", etc.)
			 * @param {!number} v Value
			 * @param {string=} sfx Suffix (like "px" or "%" or "em")
			 * @param {boolean=} recurse If true, the call is a recursive one. In some browsers (like IE7/8), occasionally the value isn't accurately reported initially, but if we run the function again it will take effect.
			 * @return {number} value in pixels
			 */
			_convertToPixels = _internals.convertToPixels = function(t, p, v, sfx, recurse) {
				if (sfx === "px" || !sfx) { return v; }
				if (sfx === "auto" || !v) { return 0; }
				var horiz = _horizExp.test(p),
					node = t,
					style = _tempDiv.style,
					neg = (v < 0),
					pix, cache, time;
				if (neg) {
					v = -v;
				}
				if (sfx === "%" && p.indexOf("border") !== -1) {
					pix = (v / 100) * (horiz ? t.clientWidth : t.clientHeight);
				} else {
					style.cssText = "border:0 solid red;position:" + _getStyle(t, "position") + ";line-height:0;";
					if (sfx === "%" || !node.appendChild) {
						node = t.parentNode || _doc.body;
						cache = node._gsCache;
						time = TweenLite.ticker.frame;
						if (cache && horiz && cache.time === time) { //performance optimization: we record the width of elements along with the ticker frame so that we can quickly get it again on the same tick (seems relatively safe to assume it wouldn't change on the same tick)
							return cache.width * v / 100;
						}
						style[(horiz ? "width" : "height")] = v + sfx;
					} else {
						style[(horiz ? "borderLeftWidth" : "borderTopWidth")] = v + sfx;
					}
					node.appendChild(_tempDiv);
					pix = parseFloat(_tempDiv[(horiz ? "offsetWidth" : "offsetHeight")]);
					node.removeChild(_tempDiv);
					if (horiz && sfx === "%" && CSSPlugin.cacheWidths !== false) {
						cache = node._gsCache = node._gsCache || {};
						cache.time = time;
						cache.width = pix / v * 100;
					}
					if (pix === 0 && !recurse) {
						pix = _convertToPixels(t, p, v, sfx, true);
					}
				}
				return neg ? -pix : pix;
			},
			_calculateOffset = _internals.calculateOffset = function(t, p, cs) { //for figuring out "top" or "left" in px when it's "auto". We need to factor in margin with the offsetLeft/offsetTop
				if (_getStyle(t, "position", cs) !== "absolute") { return 0; }
				var dim = ((p === "left") ? "Left" : "Top"),
					v = _getStyle(t, "margin" + dim, cs);
				return t["offset" + dim] - (_convertToPixels(t, p, parseFloat(v), v.replace(_suffixExp, "")) || 0);
			},

			// @private returns at object containing ALL of the style properties in camelCase and their associated values.
			_getAllStyles = function(t, cs) {
				var s = {},
					i, tr;
				if ((cs = cs || _getComputedStyle(t, null))) {
					if ((i = cs.length)) {
						while (--i > -1) {
							s[cs[i].replace(_camelExp, _camelFunc)] = cs.getPropertyValue(cs[i]);
						}
					} else { //Opera behaves differently - cs.length is always 0, so we must do a for...in loop.
						for (i in cs) {
							s[i] = cs[i];
						}
					}
				} else if ((cs = t.currentStyle || t.style)) {
					for (i in cs) {
						if (typeof(i) === "string" && s[i] === undefined) {
							s[i.replace(_camelExp, _camelFunc)] = cs[i];
						}
					}
				}
				if (!_supportsOpacity) {
					s.opacity = _getIEOpacity(t);
				}
				tr = _getTransform(t, cs, false);
				s.rotation = tr.rotation;
				s.skewX = tr.skewX;
				s.scaleX = tr.scaleX;
				s.scaleY = tr.scaleY;
				s.x = tr.x;
				s.y = tr.y;
				if (_supports3D) {
					s.z = tr.z;
					s.rotationX = tr.rotationX;
					s.rotationY = tr.rotationY;
					s.scaleZ = tr.scaleZ;
				}
				if (s.filters) {
					delete s.filters;
				}
				return s;
			},

			// @private analyzes two style objects (as returned by _getAllStyles()) and only looks for differences between them that contain tweenable values (like a number or color). It returns an object with a "difs" property which refers to an object containing only those isolated properties and values for tweening, and a "firstMPT" property which refers to the first MiniPropTween instance in a linked list that recorded all the starting values of the different properties so that we can revert to them at the end or beginning of the tween - we don't want the cascading to get messed up. The forceLookup parameter is an optional generic object with properties that should be forced into the results - this is necessary for className tweens that are overwriting others because imagine a scenario where a rollover/rollout adds/removes a class and the user swipes the mouse over the target SUPER fast, thus nothing actually changed yet and the subsequent comparison of the properties would indicate they match (especially when px rounding is taken into consideration), thus no tweening is necessary even though it SHOULD tween and remove those properties after the tween (otherwise the inline styles will contaminate things). See the className SpecialProp code for details.
			_cssDif = function(t, s1, s2, vars, forceLookup) {
				var difs = {},
					style = t.style,
					val, p, mpt;
				for (p in s2) {
					if (p !== "cssText") if (p !== "length") if (isNaN(p)) if (s1[p] !== (val = s2[p]) || (forceLookup && forceLookup[p])) if (p.indexOf("Origin") === -1) if (typeof(val) === "number" || typeof(val) === "string") {
						difs[p] = (val === "auto" && (p === "left" || p === "top")) ? _calculateOffset(t, p) : ((val === "" || val === "auto" || val === "none") && typeof(s1[p]) === "string" && s1[p].replace(_NaNExp, "") !== "") ? 0 : val; //if the ending value is defaulting ("" or "auto"), we check the starting value and if it can be parsed into a number (a string which could have a suffix too, like 700px), then we swap in 0 for "" or "auto" so that things actually tween.
						if (style[p] !== undefined) { //for className tweens, we must remember which properties already existed inline - the ones that didn't should be removed when the tween isn't in progress because they were only introduced to facilitate the transition between classes.
							mpt = new MiniPropTween(style, p, style[p], mpt);
						}
					}
				}
				if (vars) {
					for (p in vars) { //copy properties (except className)
						if (p !== "className") {
							difs[p] = vars[p];
						}
					}
				}
				return {difs:difs, firstMPT:mpt};
			},
			_dimensions = {width:["Left","Right"], height:["Top","Bottom"]},
			_margins = ["marginLeft","marginRight","marginTop","marginBottom"],

			/**
			 * @private Gets the width or height of an element
			 * @param {!Object} t Target element
			 * @param {!string} p Property name ("width" or "height")
			 * @param {Object=} cs Computed style object (if one exists). Just a speed optimization.
			 * @return {number} Dimension (in pixels)
			 */
			_getDimension = function(t, p, cs) {
				var v = parseFloat((p === "width") ? t.offsetWidth : t.offsetHeight),
					a = _dimensions[p],
					i = a.length;
				cs = cs || _getComputedStyle(t, null);
				while (--i > -1) {
					v -= parseFloat( _getStyle(t, "padding" + a[i], cs, true) ) || 0;
					v -= parseFloat( _getStyle(t, "border" + a[i] + "Width", cs, true) ) || 0;
				}
				return v;
			},

			// @private Parses position-related complex strings like "top left" or "50px 10px" or "70% 20%", etc. which are used for things like transformOrigin or backgroundPosition. Optionally decorates a supplied object (recObj) with the following properties: "ox" (offsetX), "oy" (offsetY), "oxp" (if true, "ox" is a percentage not a pixel value), and "oxy" (if true, "oy" is a percentage not a pixel value)
			_parsePosition = function(v, recObj) {
				if (v == null || v === "" || v === "auto" || v === "auto auto") { //note: Firefox uses "auto auto" as default whereas Chrome uses "auto".
					v = "0 0";
				}
				var a = v.split(" "),
					x = (v.indexOf("left") !== -1) ? "0%" : (v.indexOf("right") !== -1) ? "100%" : a[0],
					y = (v.indexOf("top") !== -1) ? "0%" : (v.indexOf("bottom") !== -1) ? "100%" : a[1];
				if (y == null) {
					y = "0";
				} else if (y === "center") {
					y = "50%";
				}
				if (x === "center" || (isNaN(parseFloat(x)) && (x + "").indexOf("=") === -1)) { //remember, the user could flip-flop the values and say "bottom center" or "center bottom", etc. "center" is ambiguous because it could be used to describe horizontal or vertical, hence the isNaN(). If there's an "=" sign in the value, it's relative.
					x = "50%";
				}
				if (recObj) {
					recObj.oxp = (x.indexOf("%") !== -1);
					recObj.oyp = (y.indexOf("%") !== -1);
					recObj.oxr = (x.charAt(1) === "=");
					recObj.oyr = (y.charAt(1) === "=");
					recObj.ox = parseFloat(x.replace(_NaNExp, ""));
					recObj.oy = parseFloat(y.replace(_NaNExp, ""));
				}
				return x + " " + y + ((a.length > 2) ? " " + a[2] : "");
			},

			/**
			 * @private Takes an ending value (typically a string, but can be a number) and a starting value and returns the change between the two, looking for relative value indicators like += and -= and it also ignores suffixes (but make sure the ending value starts with a number or +=/-= and that the starting value is a NUMBER!)
			 * @param {(number|string)} e End value which is typically a string, but could be a number
			 * @param {(number|string)} b Beginning value which is typically a string but could be a number
			 * @return {number} Amount of change between the beginning and ending values (relative values that have a "+=" or "-=" are recognized)
			 */
			_parseChange = function(e, b) {
				return (typeof(e) === "string" && e.charAt(1) === "=") ? parseInt(e.charAt(0) + "1", 10) * parseFloat(e.substr(2)) : parseFloat(e) - parseFloat(b);
			},

			/**
			 * @private Takes a value and a default number, checks if the value is relative, null, or numeric and spits back a normalized number accordingly. Primarily used in the _parseTransform() function.
			 * @param {Object} v Value to be parsed
			 * @param {!number} d Default value (which is also used for relative calculations if "+=" or "-=" is found in the first parameter)
			 * @return {number} Parsed value
			 */
			_parseVal = function(v, d) {
				return (v == null) ? d : (typeof(v) === "string" && v.charAt(1) === "=") ? parseInt(v.charAt(0) + "1", 10) * Number(v.substr(2)) + d : parseFloat(v);
			},

			/**
			 * @private Translates strings like "40deg" or "40" or 40rad" or "+=40deg" or "270_short" or "-90_cw" or "+=45_ccw" to a numeric radian angle. Of course a starting/default value must be fed in too so that relative values can be calculated properly.
			 * @param {Object} v Value to be parsed
			 * @param {!number} d Default value (which is also used for relative calculations if "+=" or "-=" is found in the first parameter)
			 * @param {string=} p property name for directionalEnd (optional - only used when the parsed value is directional ("_short", "_cw", or "_ccw" suffix). We need a way to store the uncompensated value so that at the end of the tween, we set it to exactly what was requested with no directional compensation). Property name would be "rotation", "rotationX", or "rotationY"
			 * @param {Object=} directionalEnd An object that will store the raw end values for directional angles ("_short", "_cw", or "_ccw" suffix). We need a way to store the uncompensated value so that at the end of the tween, we set it to exactly what was requested with no directional compensation.
			 * @return {number} parsed angle in radians
			 */
			_parseAngle = function(v, d, p, directionalEnd) {
				var min = 0.000001,
					cap, split, dif, result;
				if (v == null) {
					result = d;
				} else if (typeof(v) === "number") {
					result = v;
				} else {
					cap = 360;
					split = v.split("_");
					dif = Number(split[0].replace(_NaNExp, "")) * ((v.indexOf("rad") === -1) ? 1 : _RAD2DEG) - ((v.charAt(1) === "=") ? 0 : d);
					if (split.length) {
						if (directionalEnd) {
							directionalEnd[p] = d + dif;
						}
						if (v.indexOf("short") !== -1) {
							dif = dif % cap;
							if (dif !== dif % (cap / 2)) {
								dif = (dif < 0) ? dif + cap : dif - cap;
							}
						}
						if (v.indexOf("_cw") !== -1 && dif < 0) {
							dif = ((dif + cap * 9999999999) % cap) - ((dif / cap) | 0) * cap;
						} else if (v.indexOf("ccw") !== -1 && dif > 0) {
							dif = ((dif - cap * 9999999999) % cap) - ((dif / cap) | 0) * cap;
						}
					}
					result = d + dif;
				}
				if (result < min && result > -min) {
					result = 0;
				}
				return result;
			},

			_colorLookup = {aqua:[0,255,255],
				lime:[0,255,0],
				silver:[192,192,192],
				black:[0,0,0],
				maroon:[128,0,0],
				teal:[0,128,128],
				blue:[0,0,255],
				navy:[0,0,128],
				white:[255,255,255],
				fuchsia:[255,0,255],
				olive:[128,128,0],
				yellow:[255,255,0],
				orange:[255,165,0],
				gray:[128,128,128],
				purple:[128,0,128],
				green:[0,128,0],
				red:[255,0,0],
				pink:[255,192,203],
				cyan:[0,255,255],
				transparent:[255,255,255,0]},

			_hue = function(h, m1, m2) {
				h = (h < 0) ? h + 1 : (h > 1) ? h - 1 : h;
				return ((((h * 6 < 1) ? m1 + (m2 - m1) * h * 6 : (h < 0.5) ? m2 : (h * 3 < 2) ? m1 + (m2 - m1) * (2 / 3 - h) * 6 : m1) * 255) + 0.5) | 0;
			},

			/**
			 * @private Parses a color (like #9F0, #FF9900, or rgb(255,51,153)) into an array with 3 elements for red, green, and blue. Also handles rgba() values (splits into array of 4 elements of course)
			 * @param {(string|number)} v The value the should be parsed which could be a string like #9F0 or rgb(255,102,51) or rgba(255,0,0,0.5) or it could be a number like 0xFF00CC or even a named color like red, blue, purple, etc.
			 * @return {Array.<number>} An array containing red, green, and blue (and optionally alpha) in that order.
			 */
			_parseColor = function(v) {
				var c1, c2, c3, h, s, l;
				if (!v || v === "") {
					return _colorLookup.black;
				}
				if (typeof(v) === "number") {
					return [v >> 16, (v >> 8) & 255, v & 255];
				}
				if (v.charAt(v.length - 1) === ",") { //sometimes a trailing commma is included and we should chop it off (typically from a comma-delimited list of values like a textShadow:"2px 2px 2px blue, 5px 5px 5px rgb(255,0,0)" - in this example "blue," has a trailing comma. We could strip it out inside parseComplex() but we'd need to do it to the beginning and ending values plus it wouldn't provide protection from other potential scenarios like if the user passes in a similar value.
					v = v.substr(0, v.length - 1);
				}
				if (_colorLookup[v]) {
					return _colorLookup[v];
				}
				if (v.charAt(0) === "#") {
					if (v.length === 4) { //for shorthand like #9F0
						c1 = v.charAt(1),
						c2 = v.charAt(2),
						c3 = v.charAt(3);
						v = "#" + c1 + c1 + c2 + c2 + c3 + c3;
					}
					v = parseInt(v.substr(1), 16);
					return [v >> 16, (v >> 8) & 255, v & 255];
				}
				if (v.substr(0, 3) === "hsl") {
					v = v.match(_numExp);
					h = (Number(v[0]) % 360) / 360;
					s = Number(v[1]) / 100;
					l = Number(v[2]) / 100;
					c2 = (l <= 0.5) ? l * (s + 1) : l + s - l * s;
					c1 = l * 2 - c2;
					if (v.length > 3) {
						v[3] = Number(v[3]);
					}
					v[0] = _hue(h + 1 / 3, c1, c2);
					v[1] = _hue(h, c1, c2);
					v[2] = _hue(h - 1 / 3, c1, c2);
					return v;
				}
				v = v.match(_numExp) || _colorLookup.transparent;
				v[0] = Number(v[0]);
				v[1] = Number(v[1]);
				v[2] = Number(v[2]);
				if (v.length > 3) {
					v[3] = Number(v[3]);
				}
				return v;
			},
			_colorExp = "(?:\\b(?:(?:rgb|rgba|hsl|hsla)\\(.+?\\))|\\B#.+?\\b"; //we'll dynamically build this Regular Expression to conserve file size. After building it, it will be able to find rgb(), rgba(), # (hexadecimal), and named color values like red, blue, purple, etc.

		for (p in _colorLookup) {
			_colorExp += "|" + p + "\\b";
		}
		_colorExp = new RegExp(_colorExp+")", "gi");

		/**
		 * @private Returns a formatter function that handles taking a string (or number in some cases) and returning a consistently formatted one in terms of delimiters, quantity of values, etc. For example, we may get boxShadow values defined as "0px red" or "0px 0px 10px rgb(255,0,0)" or "0px 0px 20px 20px #F00" and we need to ensure that what we get back is described with 4 numbers and a color. This allows us to feed it into the _parseComplex() method and split the values up appropriately. The neat thing about this _getFormatter() function is that the dflt defines a pattern as well as a default, so for example, _getFormatter("0px 0px 0px 0px #777", true) not only sets the default as 0px for all distances and #777 for the color, but also sets the pattern such that 4 numbers and a color will always get returned.
		 * @param {!string} dflt The default value and pattern to follow. So "0px 0px 0px 0px #777" will ensure that 4 numbers and a color will always get returned.
		 * @param {boolean=} clr If true, the values should be searched for color-related data. For example, boxShadow values typically contain a color whereas borderRadius don't.
		 * @param {boolean=} collapsible If true, the value is a top/left/right/bottom style one that acts like margin or padding, where if only one value is received, it's used for all 4; if 2 are received, the first is duplicated for 3rd (bottom) and the 2nd is duplicated for the 4th spot (left), etc.
		 * @return {Function} formatter function
		 */
		var _getFormatter = function(dflt, clr, collapsible, multi) {
				if (dflt == null) {
					return function(v) {return v;};
				}
				var dColor = clr ? (dflt.match(_colorExp) || [""])[0] : "",
					dVals = dflt.split(dColor).join("").match(_valuesExp) || [],
					pfx = dflt.substr(0, dflt.indexOf(dVals[0])),
					sfx = (dflt.charAt(dflt.length - 1) === ")") ? ")" : "",
					delim = (dflt.indexOf(" ") !== -1) ? " " : ",",
					numVals = dVals.length,
					dSfx = (numVals > 0) ? dVals[0].replace(_numExp, "") : "",
					formatter;
				if (!numVals) {
					return function(v) {return v;};
				}
				if (clr) {
					formatter = function(v) {
						var color, vals, i, a;
						if (typeof(v) === "number") {
							v += dSfx;
						} else if (multi && _commasOutsideParenExp.test(v)) {
							a = v.replace(_commasOutsideParenExp, "|").split("|");
							for (i = 0; i < a.length; i++) {
								a[i] = formatter(a[i]);
							}
							return a.join(",");
						}
						color = (v.match(_colorExp) || [dColor])[0];
						vals = v.split(color).join("").match(_valuesExp) || [];
						i = vals.length;
						if (numVals > i--) {
							while (++i < numVals) {
								vals[i] = collapsible ? vals[(((i - 1) / 2) | 0)] : dVals[i];
							}
						}
						return pfx + vals.join(delim) + delim + color + sfx + (v.indexOf("inset") !== -1 ? " inset" : "");
					};
					return formatter;

				}
				formatter = function(v) {
					var vals, a, i;
					if (typeof(v) === "number") {
						v += dSfx;
					} else if (multi && _commasOutsideParenExp.test(v)) {
						a = v.replace(_commasOutsideParenExp, "|").split("|");
						for (i = 0; i < a.length; i++) {
							a[i] = formatter(a[i]);
						}
						return a.join(",");
					}
					vals = v.match(_valuesExp) || [];
					i = vals.length;
					if (numVals > i--) {
						while (++i < numVals) {
							vals[i] = collapsible ? vals[(((i - 1) / 2) | 0)] : dVals[i];
						}
					}
					return pfx + vals.join(delim) + sfx;
				};
				return formatter;
			},

			/**
			 * @private returns a formatter function that's used for edge-related values like marginTop, marginLeft, paddingBottom, paddingRight, etc. Just pass a comma-delimited list of property names related to the edges.
			 * @param {!string} props a comma-delimited list of property names in order from top to left, like "marginTop,marginRight,marginBottom,marginLeft"
			 * @return {Function} a formatter function
			 */
			_getEdgeParser = function(props) {
				props = props.split(",");
				return function(t, e, p, cssp, pt, plugin, vars) {
					var a = (e + "").split(" "),
						i;
					vars = {};
					for (i = 0; i < 4; i++) {
						vars[props[i]] = a[i] = a[i] || a[(((i - 1) / 2) >> 0)];
					}
					return cssp.parse(t, vars, pt, plugin);
				};
			},

			// @private used when other plugins must tween values first, like BezierPlugin or ThrowPropsPlugin, etc. That plugin's setRatio() gets called first so that the values are updated, and then we loop through the MiniPropTweens  which handle copying the values into their appropriate slots so that they can then be applied correctly in the main CSSPlugin setRatio() method. Remember, we typically create a proxy object that has a bunch of uniquely-named properties that we feed to the sub-plugin and it does its magic normally, and then we must interpret those values and apply them to the css because often numbers must get combined/concatenated, suffixes added, etc. to work with css, like boxShadow could have 4 values plus a color.
			_setPluginRatio = _internals._setPluginRatio = function(v) {
				this.plugin.setRatio(v);
				var d = this.data,
					proxy = d.proxy,
					mpt = d.firstMPT,
					min = 0.000001,
					val, pt, i, str;
				while (mpt) {
					val = proxy[mpt.v];
					if (mpt.r) {
						val = Math.round(val);
					} else if (val < min && val > -min) {
						val = 0;
					}
					mpt.t[mpt.p] = val;
					mpt = mpt._next;
				}
				if (d.autoRotate) {
					d.autoRotate.rotation = proxy.rotation;
				}
				//at the end, we must set the CSSPropTween's "e" (end) value dynamically here because that's what is used in the final setRatio() method.
				if (v === 1) {
					mpt = d.firstMPT;
					while (mpt) {
						pt = mpt.t;
						if (!pt.type) {
							pt.e = pt.s + pt.xs0;
						} else if (pt.type === 1) {
							str = pt.xs0 + pt.s + pt.xs1;
							for (i = 1; i < pt.l; i++) {
								str += pt["xn"+i] + pt["xs"+(i+1)];
							}
							pt.e = str;
						}
						mpt = mpt._next;
					}
				}
			},

			/**
			 * @private @constructor Used by a few SpecialProps to hold important values for proxies. For example, _parseToProxy() creates a MiniPropTween instance for each property that must get tweened on the proxy, and we record the original property name as well as the unique one we create for the proxy, plus whether or not the value needs to be rounded plus the original value.
			 * @param {!Object} t target object whose property we're tweening (often a CSSPropTween)
			 * @param {!string} p property name
			 * @param {(number|string|object)} v value
			 * @param {MiniPropTween=} next next MiniPropTween in the linked list
			 * @param {boolean=} r if true, the tweened value should be rounded to the nearest integer
			 */
			MiniPropTween = function(t, p, v, next, r) {
				this.t = t;
				this.p = p;
				this.v = v;
				this.r = r;
				if (next) {
					next._prev = this;
					this._next = next;
				}
			},

			/**
			 * @private Most other plugins (like BezierPlugin and ThrowPropsPlugin and others) can only tween numeric values, but CSSPlugin must accommodate special values that have a bunch of extra data (like a suffix or strings between numeric values, etc.). For example, boxShadow has values like "10px 10px 20px 30px rgb(255,0,0)" which would utterly confuse other plugins. This method allows us to split that data apart and grab only the numeric data and attach it to uniquely-named properties of a generic proxy object ({}) so that we can feed that to virtually any plugin to have the numbers tweened. However, we must also keep track of which properties from the proxy go with which CSSPropTween values and instances. So we create a linked list of MiniPropTweens. Each one records a target (the original CSSPropTween), property (like "s" or "xn1" or "xn2") that we're tweening and the unique property name that was used for the proxy (like "boxShadow_xn1" and "boxShadow_xn2") and whether or not they need to be rounded. That way, in the _setPluginRatio() method we can simply copy the values over from the proxy to the CSSPropTween instance(s). Then, when the main CSSPlugin setRatio() method runs and applies the CSSPropTween values accordingly, they're updated nicely. So the external plugin tweens the numbers, _setPluginRatio() copies them over, and setRatio() acts normally, applying css-specific values to the element.
			 * This method returns an object that has the following properties:
			 *  - proxy: a generic object containing the starting values for all the properties that will be tweened by the external plugin.  This is what we feed to the external _onInitTween() as the target
			 *  - end: a generic object containing the ending values for all the properties that will be tweened by the external plugin. This is what we feed to the external plugin's _onInitTween() as the destination values
			 *  - firstMPT: the first MiniPropTween in the linked list
			 *  - pt: the first CSSPropTween in the linked list that was created when parsing. If shallow is true, this linked list will NOT attach to the one passed into the _parseToProxy() as the "pt" (4th) parameter.
			 * @param {!Object} t target object to be tweened
			 * @param {!(Object|string)} vars the object containing the information about the tweening values (typically the end/destination values) that should be parsed
			 * @param {!CSSPlugin} cssp The CSSPlugin instance
			 * @param {CSSPropTween=} pt the next CSSPropTween in the linked list
			 * @param {TweenPlugin=} plugin the external TweenPlugin instance that will be handling tweening the numeric values
			 * @param {boolean=} shallow if true, the resulting linked list from the parse will NOT be attached to the CSSPropTween that was passed in as the "pt" (4th) parameter.
			 * @return An object containing the following properties: proxy, end, firstMPT, and pt (see above for descriptions)
			 */
			_parseToProxy = _internals._parseToProxy = function(t, vars, cssp, pt, plugin, shallow) {
				var bpt = pt,
					start = {},
					end = {},
					transform = cssp._transform,
					oldForce = _forcePT,
					i, p, xp, mpt, firstPT;
				cssp._transform = null;
				_forcePT = vars;
				pt = firstPT = cssp.parse(t, vars, pt, plugin);
				_forcePT = oldForce;
				//break off from the linked list so the new ones are isolated.
				if (shallow) {
					cssp._transform = transform;
					if (bpt) {
						bpt._prev = null;
						if (bpt._prev) {
							bpt._prev._next = null;
						}
					}
				}
				while (pt && pt !== bpt) {
					if (pt.type <= 1) {
						p = pt.p;
						end[p] = pt.s + pt.c;
						start[p] = pt.s;
						if (!shallow) {
							mpt = new MiniPropTween(pt, "s", p, mpt, pt.r);
							pt.c = 0;
						}
						if (pt.type === 1) {
							i = pt.l;
							while (--i > 0) {
								xp = "xn" + i;
								p = pt.p + "_" + xp;
								end[p] = pt.data[xp];
								start[p] = pt[xp];
								if (!shallow) {
									mpt = new MiniPropTween(pt, xp, p, mpt, pt.rxp[xp]);
								}
							}
						}
					}
					pt = pt._next;
				}
				return {proxy:start, end:end, firstMPT:mpt, pt:firstPT};
			},



			/**
			 * @constructor Each property that is tweened has at least one CSSPropTween associated with it. These instances store important information like the target, property, starting value, amount of change, etc. They can also optionally have a number of "extra" strings and numeric values named xs1, xn1, xs2, xn2, xs3, xn3, etc. where "s" indicates string and "n" indicates number. These can be pieced together in a complex-value tween (type:1) that has alternating types of data like a string, number, string, number, etc. For example, boxShadow could be "5px 5px 8px rgb(102, 102, 51)". In that value, there are 6 numbers that may need to tween and then pieced back together into a string again with spaces, suffixes, etc. xs0 is special in that it stores the suffix for standard (type:0) tweens, -OR- the first string (prefix) in a complex-value (type:1) CSSPropTween -OR- it can be the non-tweening value in a type:-1 CSSPropTween. We do this to conserve memory.
			 * CSSPropTweens have the following optional properties as well (not defined through the constructor):
			 *  - l: Length in terms of the number of extra properties that the CSSPropTween has (default: 0). For example, for a boxShadow we may need to tween 5 numbers in which case l would be 5; Keep in mind that the start/end values for the first number that's tweened are always stored in the s and c properties to conserve memory. All additional values thereafter are stored in xn1, xn2, etc.
			 *  - xfirst: The first instance of any sub-CSSPropTweens that are tweening properties of this instance. For example, we may split up a boxShadow tween so that there's a main CSSPropTween of type:1 that has various xs* and xn* values associated with the h-shadow, v-shadow, blur, color, etc. Then we spawn a CSSPropTween for each of those that has a higher priority and runs BEFORE the main CSSPropTween so that the values are all set by the time it needs to re-assemble them. The xfirst gives us an easy way to identify the first one in that chain which typically ends at the main one (because they're all prepende to the linked list)
			 *  - plugin: The TweenPlugin instance that will handle the tweening of any complex values. For example, sometimes we don't want to use normal subtweens (like xfirst refers to) to tween the values - we might want ThrowPropsPlugin or BezierPlugin some other plugin to do the actual tweening, so we create a plugin instance and store a reference here. We need this reference so that if we get a request to round values or disable a tween, we can pass along that request.
			 *  - data: Arbitrary data that needs to be stored with the CSSPropTween. Typically if we're going to have a plugin handle the tweening of a complex-value tween, we create a generic object that stores the END values that we're tweening to and the CSSPropTween's xs1, xs2, etc. have the starting values. We store that object as data. That way, we can simply pass that object to the plugin and use the CSSPropTween as the target.
			 *  - setRatio: Only used for type:2 tweens that require custom functionality. In this case, we call the CSSPropTween's setRatio() method and pass the ratio each time the tween updates. This isn't quite as efficient as doing things directly in the CSSPlugin's setRatio() method, but it's very convenient and flexible.
			 * @param {!Object} t Target object whose property will be tweened. Often a DOM element, but not always. It could be anything.
			 * @param {string} p Property to tween (name). For example, to tween element.width, p would be "width".
			 * @param {number} s Starting numeric value
			 * @param {number} c Change in numeric value over the course of the entire tween. For example, if element.width starts at 5 and should end at 100, c would be 95.
			 * @param {CSSPropTween=} next The next CSSPropTween in the linked list. If one is defined, we will define its _prev as the new instance, and the new instance's _next will be pointed at it.
			 * @param {number=} type The type of CSSPropTween where -1 = a non-tweening value, 0 = a standard simple tween, 1 = a complex value (like one that has multiple numbers in a comma- or space-delimited string like border:"1px solid red"), and 2 = one that uses a custom setRatio function that does all of the work of applying the values on each update.
			 * @param {string=} n Name of the property that should be used for overwriting purposes which is typically the same as p but not always. For example, we may need to create a subtween for the 2nd part of a "clip:rect(...)" tween in which case "p" might be xs1 but "n" is still "clip"
			 * @param {boolean=} r If true, the value(s) should be rounded
			 * @param {number=} pr Priority in the linked list order. Higher priority CSSPropTweens will be updated before lower priority ones. The default priority is 0.
			 * @param {string=} b Beginning value. We store this to ensure that it is EXACTLY what it was when the tween began without any risk of interpretation issues.
			 * @param {string=} e Ending value. We store this to ensure that it is EXACTLY what the user defined at the end of the tween without any risk of interpretation issues.
			 */
			CSSPropTween = _internals.CSSPropTween = function(t, p, s, c, next, type, n, r, pr, b, e) {
				this.t = t; //target
				this.p = p; //property
				this.s = s; //starting value
				this.c = c; //change value
				this.n = n || p; //name that this CSSPropTween should be associated to (usually the same as p, but not always - n is what overwriting looks at)
				if (!(t instanceof CSSPropTween)) {
					_overwriteProps.push(this.n);
				}
				this.r = r; //round (boolean)
				this.type = type || 0; //0 = normal tween, -1 = non-tweening (in which case xs0 will be applied to the target's property, like tp.t[tp.p] = tp.xs0), 1 = complex-value SpecialProp, 2 = custom setRatio() that does all the work
				if (pr) {
					this.pr = pr;
					_hasPriority = true;
				}
				this.b = (b === undefined) ? s : b;
				this.e = (e === undefined) ? s + c : e;
				if (next) {
					this._next = next;
					next._prev = this;
				}
			},

			/**
			 * Takes a target, the beginning value and ending value (as strings) and parses them into a CSSPropTween (possibly with child CSSPropTweens) that accommodates multiple numbers, colors, comma-delimited values, etc. For example:
			 * sp.parseComplex(element, "boxShadow", "5px 10px 20px rgb(255,102,51)", "0px 0px 0px red", true, "0px 0px 0px rgb(0,0,0,0)", pt);
			 * It will walk through the beginning and ending values (which should be in the same format with the same number and type of values) and figure out which parts are numbers, what strings separate the numeric/tweenable values, and then create the CSSPropTweens accordingly. If a plugin is defined, no child CSSPropTweens will be created. Instead, the ending values will be stored in the "data" property of the returned CSSPropTween like: {s:-5, xn1:-10, xn2:-20, xn3:255, xn4:0, xn5:0} so that it can be fed to any other plugin and it'll be plain numeric tweens but the recomposition of the complex value will be handled inside CSSPlugin's setRatio().
			 * If a setRatio is defined, the type of the CSSPropTween will be set to 2 and recomposition of the values will be the responsibility of that method.
			 *
			 * @param {!Object} t Target whose property will be tweened
			 * @param {!string} p Property that will be tweened (its name, like "left" or "backgroundColor" or "boxShadow")
			 * @param {string} b Beginning value
			 * @param {string} e Ending value
			 * @param {boolean} clrs If true, the value could contain a color value like "rgb(255,0,0)" or "#F00" or "red". The default is false, so no colors will be recognized (a performance optimization)
			 * @param {(string|number|Object)} dflt The default beginning value that should be used if no valid beginning value is defined or if the number of values inside the complex beginning and ending values don't match
			 * @param {?CSSPropTween} pt CSSPropTween instance that is the current head of the linked list (we'll prepend to this).
			 * @param {number=} pr Priority in the linked list order. Higher priority properties will be updated before lower priority ones. The default priority is 0.
			 * @param {TweenPlugin=} plugin If a plugin should handle the tweening of extra properties, pass the plugin instance here. If one is defined, then NO subtweens will be created for any extra properties (the properties will be created - just not additional CSSPropTween instances to tween them) because the plugin is expected to do so. However, the end values WILL be populated in the "data" property, like {s:100, xn1:50, xn2:300}
			 * @param {function(number)=} setRatio If values should be set in a custom function instead of being pieced together in a type:1 (complex-value) CSSPropTween, define that custom function here.
			 * @return {CSSPropTween} The first CSSPropTween in the linked list which includes the new one(s) added by the parseComplex() call.
			 */
			_parseComplex = CSSPlugin.parseComplex = function(t, p, b, e, clrs, dflt, pt, pr, plugin, setRatio) {
				//DEBUG: _log("parseComplex: "+p+", b: "+b+", e: "+e);
				b = b || dflt || "";
				pt = new CSSPropTween(t, p, 0, 0, pt, (setRatio ? 2 : 1), null, false, pr, b, e);
				e += ""; //ensures it's a string
				var ba = b.split(", ").join(",").split(" "), //beginning array
					ea = e.split(", ").join(",").split(" "), //ending array
					l = ba.length,
					autoRound = (_autoRound !== false),
					i, xi, ni, bv, ev, bnums, enums, bn, rgba, temp, cv, str;
				if (e.indexOf(",") !== -1 || b.indexOf(",") !== -1) {
					ba = ba.join(" ").replace(_commasOutsideParenExp, ", ").split(" ");
					ea = ea.join(" ").replace(_commasOutsideParenExp, ", ").split(" ");
					l = ba.length;
				}
				if (l !== ea.length) {
					//DEBUG: _log("mismatched formatting detected on " + p + " (" + b + " vs " + e + ")");
					ba = (dflt || "").split(" ");
					l = ba.length;
				}
				pt.plugin = plugin;
				pt.setRatio = setRatio;
				for (i = 0; i < l; i++) {
					bv = ba[i];
					ev = ea[i];
					bn = parseFloat(bv);

					//if the value begins with a number (most common). It's fine if it has a suffix like px
					if (bn || bn === 0) {
						pt.appendXtra("", bn, _parseChange(ev, bn), ev.replace(_relNumExp, ""), (autoRound && ev.indexOf("px") !== -1), true);

					//if the value is a color
					} else if (clrs && (bv.charAt(0) === "#" || _colorLookup[bv] || _rgbhslExp.test(bv))) {
						str = ev.charAt(ev.length - 1) === "," ? ")," : ")"; //if there's a comma at the end, retain it.
						bv = _parseColor(bv);
						ev = _parseColor(ev);
						rgba = (bv.length + ev.length > 6);
						if (rgba && !_supportsOpacity && ev[3] === 0) { //older versions of IE don't support rgba(), so if the destination alpha is 0, just use "transparent" for the end color
							pt["xs" + pt.l] += pt.l ? " transparent" : "transparent";
							pt.e = pt.e.split(ea[i]).join("transparent");
						} else {
							if (!_supportsOpacity) { //old versions of IE don't support rgba().
								rgba = false;
							}
							pt.appendXtra((rgba ? "rgba(" : "rgb("), bv[0], ev[0] - bv[0], ",", true, true)
								.appendXtra("", bv[1], ev[1] - bv[1], ",", true)
								.appendXtra("", bv[2], ev[2] - bv[2], (rgba ? "," : str), true);
							if (rgba) {
								bv = (bv.length < 4) ? 1 : bv[3];
								pt.appendXtra("", bv, ((ev.length < 4) ? 1 : ev[3]) - bv, str, false);
							}
						}

					} else {
						bnums = bv.match(_numExp); //gets each group of numbers in the beginning value string and drops them into an array

						//if no number is found, treat it as a non-tweening value and just append the string to the current xs.
						if (!bnums) {
							pt["xs" + pt.l] += pt.l ? " " + bv : bv;

						//loop through all the numbers that are found and construct the extra values on the pt.
						} else {
							enums = ev.match(_relNumExp); //get each group of numbers in the end value string and drop them into an array. We allow relative values too, like +=50 or -=.5
							if (!enums || enums.length !== bnums.length) {
								//DEBUG: _log("mismatched formatting detected on " + p + " (" + b + " vs " + e + ")");
								return pt;
							}
							ni = 0;
							for (xi = 0; xi < bnums.length; xi++) {
								cv = bnums[xi];
								temp = bv.indexOf(cv, ni);
								pt.appendXtra(bv.substr(ni, temp - ni), Number(cv), _parseChange(enums[xi], cv), "", (autoRound && bv.substr(temp + cv.length, 2) === "px"), (xi === 0));
								ni = temp + cv.length;
							}
							pt["xs" + pt.l] += bv.substr(ni);
						}
					}
				}
				//if there are relative values ("+=" or "-=" prefix), we need to adjust the ending value to eliminate the prefixes and combine the values properly.
				if (e.indexOf("=") !== -1) if (pt.data) {
					str = pt.xs0 + pt.data.s;
					for (i = 1; i < pt.l; i++) {
						str += pt["xs" + i] + pt.data["xn" + i];
					}
					pt.e = str + pt["xs" + i];
				}
				if (!pt.l) {
					pt.type = -1;
					pt.xs0 = pt.e;
				}
				return pt.xfirst || pt;
			},
			i = 9;


		p = CSSPropTween.prototype;
		p.l = p.pr = 0; //length (number of extra properties like xn1, xn2, xn3, etc.
		while (--i > 0) {
			p["xn" + i] = 0;
			p["xs" + i] = "";
		}
		p.xs0 = "";
		p._next = p._prev = p.xfirst = p.data = p.plugin = p.setRatio = p.rxp = null;


		/**
		 * Appends and extra tweening value to a CSSPropTween and automatically manages any prefix and suffix strings. The first extra value is stored in the s and c of the main CSSPropTween instance, but thereafter any extras are stored in the xn1, xn2, xn3, etc. The prefixes and suffixes are stored in the xs0, xs1, xs2, etc. properties. For example, if I walk through a clip value like "rect(10px, 5px, 0px, 20px)", the values would be stored like this:
		 * xs0:"rect(", s:10, xs1:"px, ", xn1:5, xs2:"px, ", xn2:0, xs3:"px, ", xn3:20, xn4:"px)"
		 * And they'd all get joined together when the CSSPlugin renders (in the setRatio() method).
		 * @param {string=} pfx Prefix (if any)
		 * @param {!number} s Starting value
		 * @param {!number} c Change in numeric value over the course of the entire tween. For example, if the start is 5 and the end is 100, the change would be 95.
		 * @param {string=} sfx Suffix (if any)
		 * @param {boolean=} r Round (if true).
		 * @param {boolean=} pad If true, this extra value should be separated by the previous one by a space. If there is no previous extra and pad is true, it will automatically drop the space.
		 * @return {CSSPropTween} returns itself so that multiple methods can be chained together.
		 */
		p.appendXtra = function(pfx, s, c, sfx, r, pad) {
			var pt = this,
				l = pt.l;
			pt["xs" + l] += (pad && l) ? " " + pfx : pfx || "";
			if (!c) if (l !== 0 && !pt.plugin) { //typically we'll combine non-changing values right into the xs to optimize performance, but we don't combine them when there's a plugin that will be tweening the values because it may depend on the values being split apart, like for a bezier, if a value doesn't change between the first and second iteration but then it does on the 3rd, we'll run into trouble because there's no xn slot for that value!
				pt["xs" + l] += s + (sfx || "");
				return pt;
			}
			pt.l++;
			pt.type = pt.setRatio ? 2 : 1;
			pt["xs" + pt.l] = sfx || "";
			if (l > 0) {
				pt.data["xn" + l] = s + c;
				pt.rxp["xn" + l] = r; //round extra property (we need to tap into this in the _parseToProxy() method)
				pt["xn" + l] = s;
				if (!pt.plugin) {
					pt.xfirst = new CSSPropTween(pt, "xn" + l, s, c, pt.xfirst || pt, 0, pt.n, r, pt.pr);
					pt.xfirst.xs0 = 0; //just to ensure that the property stays numeric which helps modern browsers speed up processing. Remember, in the setRatio() method, we do pt.t[pt.p] = val + pt.xs0 so if pt.xs0 is "" (the default), it'll cast the end value as a string. When a property is a number sometimes and a string sometimes, it prevents the compiler from locking in the data type, slowing things down slightly.
				}
				return pt;
			}
			pt.data = {s:s + c};
			pt.rxp = {};
			pt.s = s;
			pt.c = c;
			pt.r = r;
			return pt;
		};

		/**
		 * @constructor A SpecialProp is basically a css property that needs to be treated in a non-standard way, like if it may contain a complex value like boxShadow:"5px 10px 15px rgb(255, 102, 51)" or if it is associated with another plugin like ThrowPropsPlugin or BezierPlugin. Every SpecialProp is associated with a particular property name like "boxShadow" or "throwProps" or "bezier" and it will intercept those values in the vars object that's passed to the CSSPlugin and handle them accordingly.
		 * @param {!string} p Property name (like "boxShadow" or "throwProps")
		 * @param {Object=} options An object containing any of the following configuration options:
		 *                      - defaultValue: the default value
		 *                      - parser: A function that should be called when the associated property name is found in the vars. This function should return a CSSPropTween instance and it should ensure that it is properly inserted into the linked list. It will receive 4 paramters: 1) The target, 2) The value defined in the vars, 3) The CSSPlugin instance (whose _firstPT should be used for the linked list), and 4) A computed style object if one was calculated (this is a speed optimization that allows retrieval of starting values quicker)
		 *                      - formatter: a function that formats any value received for this special property (for example, boxShadow could take "5px 5px red" and format it to "5px 5px 0px 0px red" so that both the beginning and ending values have a common order and quantity of values.)
		 *                      - prefix: if true, we'll determine whether or not this property requires a vendor prefix (like Webkit or Moz or ms or O)
		 *                      - color: set this to true if the value for this SpecialProp may contain color-related values like rgb(), rgba(), etc.
		 *                      - priority: priority in the linked list order. Higher priority SpecialProps will be updated before lower priority ones. The default priority is 0.
		 *                      - multi: if true, the formatter should accommodate a comma-delimited list of values, like boxShadow could have multiple boxShadows listed out.
		 *                      - collapsible: if true, the formatter should treat the value like it's a top/right/bottom/left value that could be collapsed, like "5px" would apply to all, "5px, 10px" would use 5px for top/bottom and 10px for right/left, etc.
		 *                      - keyword: a special keyword that can [optionally] be found inside the value (like "inset" for boxShadow). This allows us to validate beginning/ending values to make sure they match (if the keyword is found in one, it'll be added to the other for consistency by default).
		 */
		var SpecialProp = function(p, options) {
				options = options || {};
				this.p = options.prefix ? _checkPropPrefix(p) || p : p;
				_specialProps[p] = _specialProps[this.p] = this;
				this.format = options.formatter || _getFormatter(options.defaultValue, options.color, options.collapsible, options.multi);
				if (options.parser) {
					this.parse = options.parser;
				}
				this.clrs = options.color;
				this.multi = options.multi;
				this.keyword = options.keyword;
				this.dflt = options.defaultValue;
				this.pr = options.priority || 0;
			},

			//shortcut for creating a new SpecialProp that can accept multiple properties as a comma-delimited list (helps minification). dflt can be an array for multiple values (we don't do a comma-delimited list because the default value may contain commas, like rect(0px,0px,0px,0px)). We attach this method to the SpecialProp class/object instead of using a private _createSpecialProp() method so that we can tap into it externally if necessary, like from another plugin.
			_registerComplexSpecialProp = _internals._registerComplexSpecialProp = function(p, options, defaults) {
				if (typeof(options) !== "object") {
					options = {parser:defaults}; //to make backwards compatible with older versions of BezierPlugin and ThrowPropsPlugin
				}
				var a = p.split(","),
					d = options.defaultValue,
					i, temp;
				defaults = defaults || [d];
				for (i = 0; i < a.length; i++) {
					options.prefix = (i === 0 && options.prefix);
					options.defaultValue = defaults[i] || d;
					temp = new SpecialProp(a[i], options);
				}
			},

			//creates a placeholder special prop for a plugin so that the property gets caught the first time a tween of it is attempted, and at that time it makes the plugin register itself, thus taking over for all future tweens of that property. This allows us to not mandate that things load in a particular order and it also allows us to log() an error that informs the user when they attempt to tween an external plugin-related property without loading its .js file.
			_registerPluginProp = function(p) {
				if (!_specialProps[p]) {
					var pluginName = p.charAt(0).toUpperCase() + p.substr(1) + "Plugin";
					_registerComplexSpecialProp(p, {parser:function(t, e, p, cssp, pt, plugin, vars) {
						var pluginClass = (window.GreenSockGlobals || window).com.greensock.plugins[pluginName];
						if (!pluginClass) {
							_log("Error: " + pluginName + " js file not loaded.");
							return pt;
						}
						pluginClass._cssRegister();
						return _specialProps[p].parse(t, e, p, cssp, pt, plugin, vars);
					}});
				}
			};


		p = SpecialProp.prototype;

		/**
		 * Alias for _parseComplex() that automatically plugs in certain values for this SpecialProp, like its property name, whether or not colors should be sensed, the default value, and priority. It also looks for any keyword that the SpecialProp defines (like "inset" for boxShadow) and ensures that the beginning and ending values have the same number of values for SpecialProps where multi is true (like boxShadow and textShadow can have a comma-delimited list)
		 * @param {!Object} t target element
		 * @param {(string|number|object)} b beginning value
		 * @param {(string|number|object)} e ending (destination) value
		 * @param {CSSPropTween=} pt next CSSPropTween in the linked list
		 * @param {TweenPlugin=} plugin If another plugin will be tweening the complex value, that TweenPlugin instance goes here.
		 * @param {function=} setRatio If a custom setRatio() method should be used to handle this complex value, that goes here.
		 * @return {CSSPropTween=} First CSSPropTween in the linked list
		 */
		p.parseComplex = function(t, b, e, pt, plugin, setRatio) {
			var kwd = this.keyword,
				i, ba, ea, l, bi, ei;
			//if this SpecialProp's value can contain a comma-delimited list of values (like boxShadow or textShadow), we must parse them in a special way, and look for a keyword (like "inset" for boxShadow) and ensure that the beginning and ending BOTH have it if the end defines it as such. We also must ensure that there are an equal number of values specified (we can't tween 1 boxShadow to 3 for example)
			if (this.multi) if (_commasOutsideParenExp.test(e) || _commasOutsideParenExp.test(b)) {
				ba = b.replace(_commasOutsideParenExp, "|").split("|");
				ea = e.replace(_commasOutsideParenExp, "|").split("|");
			} else if (kwd) {
				ba = [b];
				ea = [e];
			}
			if (ea) {
				l = (ea.length > ba.length) ? ea.length : ba.length;
				for (i = 0; i < l; i++) {
					b = ba[i] = ba[i] || this.dflt;
					e = ea[i] = ea[i] || this.dflt;
					if (kwd) {
						bi = b.indexOf(kwd);
						ei = e.indexOf(kwd);
						if (bi !== ei) {
							e = (ei === -1) ? ea : ba;
							e[i] += " " + kwd;
						}
					}
				}
				b = ba.join(", ");
				e = ea.join(", ");
			}
			return _parseComplex(t, this.p, b, e, this.clrs, this.dflt, pt, this.pr, plugin, setRatio);
		};

		/**
		 * Accepts a target and end value and spits back a CSSPropTween that has been inserted into the CSSPlugin's linked list and conforms with all the conventions we use internally, like type:-1, 0, 1, or 2, setting up any extra property tweens, priority, etc. For example, if we have a boxShadow SpecialProp and call:
		 * this._firstPT = sp.parse(element, "5px 10px 20px rgb(2550,102,51)", "boxShadow", this);
		 * It should figure out the starting value of the element's boxShadow, compare it to the provided end value and create all the necessary CSSPropTweens of the appropriate types to tween the boxShadow. The CSSPropTween that gets spit back should already be inserted into the linked list (the 4th parameter is the current head, so prepend to that).
		 * @param {!Object} t Target object whose property is being tweened
		 * @param {Object} e End value as provided in the vars object (typically a string, but not always - like a throwProps would be an object).
		 * @param {!string} p Property name
		 * @param {!CSSPlugin} cssp The CSSPlugin instance that should be associated with this tween.
		 * @param {?CSSPropTween} pt The CSSPropTween that is the current head of the linked list (we'll prepend to it)
		 * @param {TweenPlugin=} plugin If a plugin will be used to tween the parsed value, this is the plugin instance.
		 * @param {Object=} vars Original vars object that contains the data for parsing.
		 * @return {CSSPropTween} The first CSSPropTween in the linked list which includes the new one(s) added by the parse() call.
		 */
		p.parse = function(t, e, p, cssp, pt, plugin, vars) {
			return this.parseComplex(t.style, this.format(_getStyle(t, this.p, _cs, false, this.dflt)), this.format(e), pt, plugin);
		};

		/**
		 * Registers a special property that should be intercepted from any "css" objects defined in tweens. This allows you to handle them however you want without CSSPlugin doing it for you. The 2nd parameter should be a function that accepts 3 parameters:
		 *  1) Target object whose property should be tweened (typically a DOM element)
		 *  2) The end/destination value (could be a string, number, object, or whatever you want)
		 *  3) The tween instance (you probably don't need to worry about this, but it can be useful for looking up information like the duration)
		 *
		 * Then, your function should return a function which will be called each time the tween gets rendered, passing a numeric "ratio" parameter to your function that indicates the change factor (usually between 0 and 1). For example:
		 *
		 * CSSPlugin.registerSpecialProp("myCustomProp", function(target, value, tween) {
		 *      var start = target.style.width;
		 *      return function(ratio) {
		 *              target.style.width = (start + value * ratio) + "px";
		 *              console.log("set width to " + target.style.width);
		 *          }
		 * }, 0);
		 *
		 * Then, when I do this tween, it will trigger my special property:
		 *
		 * TweenLite.to(element, 1, {css:{myCustomProp:100}});
		 *
		 * In the example, of course, we're just changing the width, but you can do anything you want.
		 *
		 * @param {!string} name Property name (or comma-delimited list of property names) that should be intercepted and handled by your function. For example, if I define "myCustomProp", then it would handle that portion of the following tween: TweenLite.to(element, 1, {css:{myCustomProp:100}})
		 * @param {!function(Object, Object, Object, string):function(number)} onInitTween The function that will be called when a tween of this special property is performed. The function will receive 4 parameters: 1) Target object that should be tweened, 2) Value that was passed to the tween, 3) The tween instance itself (rarely used), and 4) The property name that's being tweened. Your function should return a function that should be called on every update of the tween. That function will receive a single parameter that is a "change factor" value (typically between 0 and 1) indicating the amount of change as a ratio. You can use this to determine how to set the values appropriately in your function.
		 * @param {number=} priority Priority that helps the engine determine the order in which to set the properties (default: 0). Higher priority properties will be updated before lower priority ones.
		 */
		CSSPlugin.registerSpecialProp = function(name, onInitTween, priority) {
			_registerComplexSpecialProp(name, {parser:function(t, e, p, cssp, pt, plugin, vars) {
				var rv = new CSSPropTween(t, p, 0, 0, pt, 2, p, false, priority);
				rv.plugin = plugin;
				rv.setRatio = onInitTween(t, e, cssp._tween, p);
				return rv;
			}, priority:priority});
		};








		//transform-related methods and properties
		var _transformProps = ("scaleX,scaleY,scaleZ,x,y,z,skewX,skewY,rotation,rotationX,rotationY,perspective").split(","),
			_transformProp = _checkPropPrefix("transform"), //the Javascript (camelCase) transform property, like msTransform, WebkitTransform, MozTransform, or OTransform.
			_transformPropCSS = _prefixCSS + "transform",
			_transformOriginProp = _checkPropPrefix("transformOrigin"),
			_supports3D = (_checkPropPrefix("perspective") !== null),
			Transform = _internals.Transform = function() {
				this.skewY = 0;
			},

			/**
			 * Parses the transform values for an element, returning an object with x, y, z, scaleX, scaleY, scaleZ, rotation, rotationX, rotationY, skewX, and skewY properties. Note: by default (for performance reasons), all skewing is combined into skewX and rotation but skewY still has a place in the transform object so that we can record how much of the skew is attributed to skewX vs skewY. Remember, a skewY of 10 looks the same as a rotation of 10 and skewX of -10.
			 * @param {!Object} t target element
			 * @param {Object=} cs computed style object (optional)
			 * @param {boolean=} rec if true, the transform values will be recorded to the target element's _gsTransform object, like target._gsTransform = {x:0, y:0, z:0, scaleX:1...}
			 * @param {boolean=} parse if true, we'll ignore any _gsTransform values that already exist on the element, and force a reparsing of the css (calculated style)
			 * @return {object} object containing all of the transform properties/values like {x:0, y:0, z:0, scaleX:1...}
			 */
			_getTransform = _internals.getTransform = function(t, cs, rec, parse) {
				if (t._gsTransform && rec && !parse) {
					return t._gsTransform; //if the element already has a _gsTransform, use that. Note: some browsers don't accurately return the calculated style for the transform (particularly for SVG), so it's almost always safest to just use the values we've already applied rather than re-parsing things.
				}
				var tm = rec ? t._gsTransform || new Transform() : new Transform(),
					invX = (tm.scaleX < 0), //in order to interpret things properly, we need to know if the user applied a negative scaleX previously so that we can adjust the rotation and skewX accordingly. Otherwise, if we always interpret a flipped matrix as affecting scaleY and the user only wants to tween the scaleX on multiple sequential tweens, it would keep the negative scaleY without that being the user's intent.
					min = 0.00002,
					rnd = 100000,
					minAngle = 179.99,
					minPI = minAngle * _DEG2RAD,
					zOrigin = _supports3D ? parseFloat(_getStyle(t, _transformOriginProp, cs, false, "0 0 0").split(" ")[2]) || tm.zOrigin  || 0 : 0,
					s, m, i, n, dec, scaleX, scaleY, rotation, skewX, difX, difY, difR, difS;
				if (_transformProp) {
					s = _getStyle(t, _transformPropCSS, cs, true);
				} else if (t.currentStyle) {
					//for older versions of IE, we need to interpret the filter portion that is in the format: progid:DXImageTransform.Microsoft.Matrix(M11=6.123233995736766e-17, M12=-1, M21=1, M22=6.123233995736766e-17, sizingMethod='auto expand') Notice that we need to swap b and c compared to a normal matrix.
					s = t.currentStyle.filter.match(_ieGetMatrixExp);
					s = (s && s.length === 4) ? [s[0].substr(4), Number(s[2].substr(4)), Number(s[1].substr(4)), s[3].substr(4), (tm.x || 0), (tm.y || 0)].join(",") : "";
				}
				//split the matrix values out into an array (m for matrix)
				m = (s || "").match(/(?:\-|\b)[\d\-\.e]+\b/gi) || [];
				i = m.length;
				while (--i > -1) {
					n = Number(m[i]);
					m[i] = (dec = n - (n |= 0)) ? ((dec * rnd + (dec < 0 ? -0.5 : 0.5)) | 0) / rnd + n : n; //convert strings to Numbers and round to 5 decimal places to avoid issues with tiny numbers. Roughly 20x faster than Number.toFixed(). We also must make sure to round before dividing so that values like 0.9999999999 become 1 to avoid glitches in browser rendering and interpretation of flipped/rotated 3D matrices. And don't just multiply the number by rnd, floor it, and then divide by rnd because the bitwise operations max out at a 32-bit signed integer, thus it could get clipped at a relatively low value (like 22,000.00000 for example).
				}
				if (m.length === 16) {

					//we'll only look at these position-related 6 variables first because if x/y/z all match, it's relatively safe to assume we don't need to re-parse everything which risks losing important rotational information (like rotationX:180 plus rotationY:180 would look the same as rotation:180 - there's no way to know for sure which direction was taken based solely on the matrix3d() values)
					var a13 = m[8], a23 = m[9], a33 = m[10],
						a14 = m[12], a24 = m[13], a34 = m[14];

					//we manually compensate for non-zero z component of transformOrigin to work around bugs in Safari
					if (tm.zOrigin) {
						a34 = -tm.zOrigin;
						a14 = a13*a34-m[12];
						a24 = a23*a34-m[13];
						a34 = a33*a34+tm.zOrigin-m[14];
					}

					//only parse from the matrix if we MUST because not only is it usually unnecessary due to the fact that we store the values in the _gsTransform object, but also because it's impossible to accurately interpret rotationX, rotationY, rotationZ, scaleX, and scaleY if all are applied, so it's much better to rely on what we store. However, we must parse the first time that an object is tweened. We also assume that if the position has changed, the user must have done some styling changes outside of CSSPlugin, thus we force a parse in that scenario.
					if (!rec || parse || tm.rotationX == null) {
						var a11 = m[0], a21 = m[1], a31 = m[2], a41 = m[3],
							a12 = m[4], a22 = m[5], a32 = m[6], a42 = m[7],
							a43 = m[11],
							angle = Math.atan2(a32, a33),
							xFlip = (angle < -minPI || angle > minPI),
							t1, t2, t3, cos, sin, yFlip, zFlip;
						tm.rotationX = angle * _RAD2DEG;
						//rotationX
						if (angle) {
							cos = Math.cos(-angle);
							sin = Math.sin(-angle);
							t1 = a12*cos+a13*sin;
							t2 = a22*cos+a23*sin;
							t3 = a32*cos+a33*sin;
							a13 = a12*-sin+a13*cos;
							a23 = a22*-sin+a23*cos;
							a33 = a32*-sin+a33*cos;
							a43 = a42*-sin+a43*cos;
							a12 = t1;
							a22 = t2;
							a32 = t3;
						}
						//rotationY
						angle = Math.atan2(a13, a11);
						tm.rotationY = angle * _RAD2DEG;
						if (angle) {
							yFlip = (angle < -minPI || angle > minPI);
							cos = Math.cos(-angle);
							sin = Math.sin(-angle);
							t1 = a11*cos-a13*sin;
							t2 = a21*cos-a23*sin;
							t3 = a31*cos-a33*sin;
							a23 = a21*sin+a23*cos;
							a33 = a31*sin+a33*cos;
							a43 = a41*sin+a43*cos;
							a11 = t1;
							a21 = t2;
							a31 = t3;
						}
						//rotationZ
						angle = Math.atan2(a21, a22);
						tm.rotation = angle * _RAD2DEG;
						if (angle) {
							zFlip = (angle < -minPI || angle > minPI);
							cos = Math.cos(-angle);
							sin = Math.sin(-angle);
							a11 = a11*cos+a12*sin;
							t2 = a21*cos+a22*sin;
							a22 = a21*-sin+a22*cos;
							a32 = a31*-sin+a32*cos;
							a21 = t2;
						}

						if (zFlip && xFlip) {
							tm.rotation = tm.rotationX = 0;
						} else if (zFlip && yFlip) {
							tm.rotation = tm.rotationY = 0;
						} else if (yFlip && xFlip) {
							tm.rotationY = tm.rotationX = 0;
						}

						tm.scaleX = ((Math.sqrt(a11 * a11 + a21 * a21) * rnd + 0.5) | 0) / rnd;
						tm.scaleY = ((Math.sqrt(a22 * a22 + a23 * a23) * rnd + 0.5) | 0) / rnd;
						tm.scaleZ = ((Math.sqrt(a32 * a32 + a33 * a33) * rnd + 0.5) | 0) / rnd;
						tm.skewX = 0;
						tm.perspective = a43 ? 1 / ((a43 < 0) ? -a43 : a43) : 0;
						tm.x = a14;
						tm.y = a24;
						tm.z = a34;
					}

				} else if ((!_supports3D || parse || !m.length || tm.x !== m[4] || tm.y !== m[5] || (!tm.rotationX && !tm.rotationY)) && !(tm.x !== undefined && _getStyle(t, "display", cs) === "none")) { //sometimes a 6-element matrix is returned even when we performed 3D transforms, like if rotationX and rotationY are 180. In cases like this, we still need to honor the 3D transforms. If we just rely on the 2D info, it could affect how the data is interpreted, like scaleY might get set to -1 or rotation could get offset by 180 degrees. For example, do a TweenLite.to(element, 1, {css:{rotationX:180, rotationY:180}}) and then later, TweenLite.to(element, 1, {css:{rotationX:0}}) and without this conditional logic in place, it'd jump to a state of being unrotated when the 2nd tween starts. Then again, we need to honor the fact that the user COULD alter the transforms outside of CSSPlugin, like by manually applying new css, so we try to sense that by looking at x and y because if those changed, we know the changes were made outside CSSPlugin and we force a reinterpretation of the matrix values. Also, in Webkit browsers, if the element's "display" is "none", its calculated style value will always return empty, so if we've already recorded the values in the _gsTransform object, we'll just rely on those.
					var k = (m.length >= 6),
						a = k ? m[0] : 1,
						b = m[1] || 0,
						c = m[2] || 0,
						d = k ? m[3] : 1;
					tm.x = m[4] || 0;
					tm.y = m[5] || 0;
					scaleX = Math.sqrt(a * a + b * b);
					scaleY = Math.sqrt(d * d + c * c);
					rotation = (a || b) ? Math.atan2(b, a) * _RAD2DEG : tm.rotation || 0; //note: if scaleX is 0, we cannot accurately measure rotation. Same for skewX with a scaleY of 0. Therefore, we default to the previously recorded value (or zero if that doesn't exist).
					skewX = (c || d) ? Math.atan2(c, d) * _RAD2DEG + rotation : tm.skewX || 0;
					difX = scaleX - Math.abs(tm.scaleX || 0);
					difY = scaleY - Math.abs(tm.scaleY || 0);
					if (Math.abs(skewX) > 90 && Math.abs(skewX) < 270) {
						if (invX) {
							scaleX *= -1;
							skewX += (rotation <= 0) ? 180 : -180;
							rotation += (rotation <= 0) ? 180 : -180;
						} else {
							scaleY *= -1;
							skewX += (skewX <= 0) ? 180 : -180;
						}
					}
					difR = (rotation - tm.rotation) % 180; //note: matching ranges would be very small (+/-0.0001) or very close to 180.
					difS = (skewX - tm.skewX) % 180;
					//if there's already a recorded _gsTransform in place for the target, we should leave those values in place unless we know things changed for sure (beyond a super small amount). This gets around ambiguous interpretations, like if scaleX and scaleY are both -1, the matrix would be the same as if the rotation was 180 with normal scaleX/scaleY. If the user tweened to particular values, those must be prioritized to ensure animation is consistent.
					if (tm.skewX === undefined || difX > min || difX < -min || difY > min || difY < -min || (difR > -minAngle && difR < minAngle && (difR * rnd) | 0 !== 0) || (difS > -minAngle && difS < minAngle && (difS * rnd) | 0 !== 0)) {
						tm.scaleX = scaleX;
						tm.scaleY = scaleY;
						tm.rotation = rotation;
						tm.skewX = skewX;
					}
					if (_supports3D) {
						tm.rotationX = tm.rotationY = tm.z = 0;
						tm.perspective = parseFloat(CSSPlugin.defaultTransformPerspective) || 0;
						tm.scaleZ = 1;
					}
				}
				tm.zOrigin = zOrigin;

				//some browsers have a hard time with very small values like 2.4492935982947064e-16 (notice the "e-" towards the end) and would render the object slightly off. So we round to 0 in these cases. The conditional logic here is faster than calling Math.abs(). Also, browsers tend to render a SLIGHTLY rotated object in a fuzzy way, so we need to snap to exactly 0 when appropriate.
				for (i in tm) {
					if (tm[i] < min) if (tm[i] > -min) {
						tm[i] = 0;
					}
				}
				//DEBUG: _log("parsed rotation: "+(tm.rotationX)+", "+(tm.rotationY)+", "+(tm.rotation)+", scale: "+tm.scaleX+", "+tm.scaleY+", "+tm.scaleZ+", position: "+tm.x+", "+tm.y+", "+tm.z+", perspective: "+tm.perspective);
				if (rec) {
					t._gsTransform = tm; //record to the object's _gsTransform which we use so that tweens can control individual properties independently (we need all the properties to accurately recompose the matrix in the setRatio() method)
				}
				return tm;
			},

			//for setting 2D transforms in IE6, IE7, and IE8 (must use a "filter" to emulate the behavior of modern day browser transforms)
			_setIETransformRatio = function(v) {
				var t = this.data, //refers to the element's _gsTransform object
					ang = -t.rotation * _DEG2RAD,
					skew = ang + t.skewX * _DEG2RAD,
					rnd = 100000,
					a = ((Math.cos(ang) * t.scaleX * rnd) | 0) / rnd,
					b = ((Math.sin(ang) * t.scaleX * rnd) | 0) / rnd,
					c = ((Math.sin(skew) * -t.scaleY * rnd) | 0) / rnd,
					d = ((Math.cos(skew) * t.scaleY * rnd) | 0) / rnd,
					style = this.t.style,
					cs = this.t.currentStyle,
					filters, val;
				if (!cs) {
					return;
				}
				val = b; //just for swapping the variables an inverting them (reused "val" to avoid creating another variable in memory). IE's filter matrix uses a non-standard matrix configuration (angle goes the opposite way, and b and c are reversed and inverted)
				b = -c;
				c = -val;
				filters = cs.filter;
				style.filter = ""; //remove filters so that we can accurately measure offsetWidth/offsetHeight
				var w = this.t.offsetWidth,
					h = this.t.offsetHeight,
					clip = (cs.position !== "absolute"),
					m = "progid:DXImageTransform.Microsoft.Matrix(M11=" + a + ", M12=" + b + ", M21=" + c + ", M22=" + d,
					ox = t.x,
					oy = t.y,
					dx, dy;

				//if transformOrigin is being used, adjust the offset x and y
				if (t.ox != null) {
					dx = ((t.oxp) ? w * t.ox * 0.01 : t.ox) - w / 2;
					dy = ((t.oyp) ? h * t.oy * 0.01 : t.oy) - h / 2;
					ox += dx - (dx * a + dy * b);
					oy += dy - (dx * c + dy * d);
				}

				if (!clip) {
					m += ", sizingMethod='auto expand')";
				} else {
					dx = (w / 2);
					dy = (h / 2);
					//translate to ensure that transformations occur around the correct origin (default is center).
					m += ", Dx=" + (dx - (dx * a + dy * b) + ox) + ", Dy=" + (dy - (dx * c + dy * d) + oy) + ")";
				}
				if (filters.indexOf("DXImageTransform.Microsoft.Matrix(") !== -1) {
					style.filter = filters.replace(_ieSetMatrixExp, m);
				} else {
					style.filter = m + " " + filters; //we must always put the transform/matrix FIRST (before alpha(opacity=xx)) to avoid an IE bug that slices part of the object when rotation is applied with alpha.
				}

				//at the end or beginning of the tween, if the matrix is normal (1, 0, 0, 1) and opacity is 100 (or doesn't exist), remove the filter to improve browser performance.
				if (v === 0 || v === 1) if (a === 1) if (b === 0) if (c === 0) if (d === 1) if (!clip || m.indexOf("Dx=0, Dy=0") !== -1) if (!_opacityExp.test(filters) || parseFloat(RegExp.$1) === 100) if (filters.indexOf("gradient(" && filters.indexOf("Alpha")) === -1) {
					style.removeAttribute("filter");
				}

				//we must set the margins AFTER applying the filter in order to avoid some bugs in IE8 that could (in rare scenarios) cause them to be ignored intermittently (vibration).
				if (!clip) {
					var mult = (_ieVers < 8) ? 1 : -1, //in Internet Explorer 7 and before, the box model is broken, causing the browser to treat the width/height of the actual rotated filtered image as the width/height of the box itself, but Microsoft corrected that in IE8. We must use a negative offset in IE8 on the right/bottom
						marg, prop, dif;
					dx = t.ieOffsetX || 0;
					dy = t.ieOffsetY || 0;
					t.ieOffsetX = Math.round((w - ((a < 0 ? -a : a) * w + (b < 0 ? -b : b) * h)) / 2 + ox);
					t.ieOffsetY = Math.round((h - ((d < 0 ? -d : d) * h + (c < 0 ? -c : c) * w)) / 2 + oy);
					for (i = 0; i < 4; i++) {
						prop = _margins[i];
						marg = cs[prop];
						//we need to get the current margin in case it is being tweened separately (we want to respect that tween's changes)
						val = (marg.indexOf("px") !== -1) ? parseFloat(marg) : _convertToPixels(this.t, prop, parseFloat(marg), marg.replace(_suffixExp, "")) || 0;
						if (val !== t[prop]) {
							dif = (i < 2) ? -t.ieOffsetX : -t.ieOffsetY; //if another tween is controlling a margin, we cannot only apply the difference in the ieOffsets, so we essentially zero-out the dx and dy here in that case. We record the margin(s) later so that we can keep comparing them, making this code very flexible.
						} else {
							dif = (i < 2) ? dx - t.ieOffsetX : dy - t.ieOffsetY;
						}
						style[prop] = (t[prop] = Math.round( val - dif * ((i === 0 || i === 2) ? 1 : mult) )) + "px";
					}
				}
			},

			_set3DTransformRatio = _internals.set3DTransformRatio = function(v) {
				var t = this.data, //refers to the element's _gsTransform object
					style = this.t.style,
					angle = t.rotation * _DEG2RAD,
					sx = t.scaleX,
					sy = t.scaleY,
					sz = t.scaleZ,
					perspective = t.perspective,
					a11, a12, a13, a14,	a21, a22, a23, a24, a31, a32, a33, a34,	a41, a42, a43,
					zOrigin, rnd, cos, sin, t1, t2, t3, t4;
				if (v === 1 || v === 0) if (t.force3D === "auto") if (!t.rotationY && !t.rotationX && sz === 1 && !perspective && !t.z) { //on the final render (which could be 0 for a from tween), if there are no 3D aspects, render in 2D to free up memory and improve performance especially on mobile devices
					_set2DTransformRatio.call(this, v);
					return;
				}
				if (_isFirefox) {
					var n = 0.0001;
					if (sx < n && sx > -n) { //Firefox has a bug (at least in v25) that causes it to render the transparent part of 32-bit PNG images as black when displayed inside an iframe and the 3D scale is very small and doesn't change sufficiently enough between renders (like if you use a Power4.easeInOut to scale from 0 to 1 where the beginning values only change a tiny amount to begin the tween before accelerating). In this case, we force the scale to be 0.00002 instead which is visually the same but works around the Firefox issue.
						sx = sz = 0.00002;
					}
					if (sy < n && sy > -n) {
						sy = sz = 0.00002;
					}
					if (perspective && !t.z && !t.rotationX && !t.rotationY) { //Firefox has a bug that causes elements to have an odd super-thin, broken/dotted black border on elements that have a perspective set but aren't utilizing 3D space (no rotationX, rotationY, or z).
						perspective = 0;
					}
				}
				if (angle || t.skewX) {
					cos = Math.cos(angle);
					sin = Math.sin(angle);
					a11 = cos;
					a21 = sin;
					if (t.skewX) {
						angle -= t.skewX * _DEG2RAD;
						cos = Math.cos(angle);
						sin = Math.sin(angle);
						if (t.skewType === "simple") { //by default, we compensate skewing on the other axis to make it look more natural, but you can set the skewType to "simple" to use the uncompensated skewing that CSS does
							t1 = Math.tan(t.skewX * _DEG2RAD);
							t1 = Math.sqrt(1 + t1 * t1);
							cos *= t1;
							sin *= t1;
						}
					}
					a12 = -sin;
					a22 = cos;

				} else if (!t.rotationY && !t.rotationX && sz === 1 && !perspective) { //if we're only translating and/or 2D scaling, this is faster...
					style[_transformProp] = "translate3d(" + t.x + "px," + t.y + "px," + t.z +"px)" + ((sx !== 1 || sy !== 1) ? " scale(" + sx + "," + sy + ")" : "");
					return;
				} else {
					a11 = a22 = 1;
					a12 = a21 = 0;
				}
				a33 = 1;
				a13 = a14 = a23 = a24 = a31 = a32 = a34 = a41 = a42 = 0;
				a43 = (perspective) ? -1 / perspective : 0;
				zOrigin = t.zOrigin;
				rnd = 100000;
				angle = t.rotationY * _DEG2RAD;
				if (angle) {
					cos = Math.cos(angle);
					sin = Math.sin(angle);
					a31 = a33*-sin;
					a41 = a43*-sin;
					a13 = a11*sin;
					a23 = a21*sin;
					a33 *= cos;
					a43 *= cos;
					a11 *= cos;
					a21 *= cos;
				}
				angle = t.rotationX * _DEG2RAD;
				if (angle) {
					cos = Math.cos(angle);
					sin = Math.sin(angle);
					t1 = a12*cos+a13*sin;
					t2 = a22*cos+a23*sin;
					t3 = a32*cos+a33*sin;
					t4 = a42*cos+a43*sin;
					a13 = a12*-sin+a13*cos;
					a23 = a22*-sin+a23*cos;
					a33 = a32*-sin+a33*cos;
					a43 = a42*-sin+a43*cos;
					a12 = t1;
					a22 = t2;
					a32 = t3;
					a42 = t4;
				}
				if (sz !== 1) {
					a13*=sz;
					a23*=sz;
					a33*=sz;
					a43*=sz;
				}
				if (sy !== 1) {
					a12*=sy;
					a22*=sy;
					a32*=sy;
					a42*=sy;
				}
				if (sx !== 1) {
					a11*=sx;
					a21*=sx;
					a31*=sx;
					a41*=sx;
				}
				if (zOrigin) {
					a34 -= zOrigin;
					a14 = a13*a34;
					a24 = a23*a34;
					a34 = a33*a34+zOrigin;
				}
				//we round the x, y, and z slightly differently to allow even larger values.
				a14 = (t1 = (a14 += t.x) - (a14 |= 0)) ? ((t1 * rnd + (t1 < 0 ? -0.5 : 0.5)) | 0) / rnd + a14 : a14;
				a24 = (t1 = (a24 += t.y) - (a24 |= 0)) ? ((t1 * rnd + (t1 < 0 ? -0.5 : 0.5)) | 0) / rnd + a24 : a24;
				a34 = (t1 = (a34 += t.z) - (a34 |= 0)) ? ((t1 * rnd + (t1 < 0 ? -0.5 : 0.5)) | 0) / rnd + a34 : a34;
				style[_transformProp] = "matrix3d(" + [ (((a11 * rnd) | 0) / rnd), (((a21 * rnd) | 0) / rnd), (((a31 * rnd) | 0) / rnd), (((a41 * rnd) | 0) / rnd), (((a12 * rnd) | 0) / rnd), (((a22 * rnd) | 0) / rnd), (((a32 * rnd) | 0) / rnd), (((a42 * rnd) | 0) / rnd), (((a13 * rnd) | 0) / rnd), (((a23 * rnd) | 0) / rnd), (((a33 * rnd) | 0) / rnd), (((a43 * rnd) | 0) / rnd), a14, a24, a34, (perspective ? (1 + (-a34 / perspective)) : 1) ].join(",") + ")";
			},

			_set2DTransformRatio = _internals.set2DTransformRatio = function(v) {
				var t = this.data, //refers to the element's _gsTransform object
					targ = this.t,
					style = targ.style,
					ang, skew, rnd, sx, sy;
				if (t.rotationX || t.rotationY || t.z || t.force3D === true || (t.force3D === "auto" && v !== 1 && v !== 0)) { //if a 3D tween begins while a 2D one is running, we need to kick the rendering over to the 3D method. For example, imagine a yoyo-ing, infinitely repeating scale tween running, and then the object gets rotated in 3D space with a different tween.
					this.setRatio = _set3DTransformRatio;
					_set3DTransformRatio.call(this, v);
					return;
				}
				if (!t.rotation && !t.skewX) {
					style[_transformProp] = "matrix(" + t.scaleX + ",0,0," + t.scaleY + "," + t.x + "," + t.y + ")";
				} else {
					ang = t.rotation * _DEG2RAD;
					skew = ang - t.skewX * _DEG2RAD;
					rnd = 100000;
					sx = t.scaleX * rnd;
					sy = t.scaleY * rnd;
					//some browsers have a hard time with very small values like 2.4492935982947064e-16 (notice the "e-" towards the end) and would render the object slightly off. So we round to 5 decimal places.
					style[_transformProp] = "matrix(" + (((Math.cos(ang) * sx) | 0) / rnd) + "," + (((Math.sin(ang) * sx) | 0) / rnd) + "," + (((Math.sin(skew) * -sy) | 0) / rnd) + "," + (((Math.cos(skew) * sy) | 0) / rnd) + "," + t.x + "," + t.y + ")";
				}
			};

		_registerComplexSpecialProp("transform,scale,scaleX,scaleY,scaleZ,x,y,z,rotation,rotationX,rotationY,rotationZ,skewX,skewY,shortRotation,shortRotationX,shortRotationY,shortRotationZ,transformOrigin,transformPerspective,directionalRotation,parseTransform,force3D,skewType", {parser:function(t, e, p, cssp, pt, plugin, vars) {
			if (cssp._transform) { return pt; } //only need to parse the transform once, and only if the browser supports it.
			var m1 = cssp._transform = _getTransform(t, _cs, true, vars.parseTransform),
				style = t.style,
				min = 0.000001,
				i = _transformProps.length,
				v = vars,
				endRotations = {},
				m2, skewY, copy, orig, has3D, hasChange, dr;
			if (typeof(v.transform) === "string" && _transformProp) { //for values like transform:"rotate(60deg) scale(0.5, 0.8)"
				copy = _tempDiv.style; //don't use the original target because it might be SVG in which case some browsers don't report computed style correctly.
				copy[_transformProp] = v.transform;
				copy.display = "block"; //if display is "none", the browser often refuses to report the transform properties correctly.
				copy.position = "absolute";
				_doc.body.appendChild(_tempDiv);
				m2 = _getTransform(_tempDiv, null, false);
				_doc.body.removeChild(_tempDiv);
			} else if (typeof(v) === "object") { //for values like scaleX, scaleY, rotation, x, y, skewX, and skewY or transform:{...} (object)
				m2 = {scaleX:_parseVal((v.scaleX != null) ? v.scaleX : v.scale, m1.scaleX),
					scaleY:_parseVal((v.scaleY != null) ? v.scaleY : v.scale, m1.scaleY),
					scaleZ:_parseVal(v.scaleZ, m1.scaleZ),
					x:_parseVal(v.x, m1.x),
					y:_parseVal(v.y, m1.y),
					z:_parseVal(v.z, m1.z),
					perspective:_parseVal(v.transformPerspective, m1.perspective)};
				dr = v.directionalRotation;
				if (dr != null) {
					if (typeof(dr) === "object") {
						for (copy in dr) {
							v[copy] = dr[copy];
						}
					} else {
						v.rotation = dr;
					}
				}
				m2.rotation = _parseAngle(("rotation" in v) ? v.rotation : ("shortRotation" in v) ? v.shortRotation + "_short" : ("rotationZ" in v) ? v.rotationZ : m1.rotation, m1.rotation, "rotation", endRotations);
				if (_supports3D) {
					m2.rotationX = _parseAngle(("rotationX" in v) ? v.rotationX : ("shortRotationX" in v) ? v.shortRotationX + "_short" : m1.rotationX || 0, m1.rotationX, "rotationX", endRotations);
					m2.rotationY = _parseAngle(("rotationY" in v) ? v.rotationY : ("shortRotationY" in v) ? v.shortRotationY + "_short" : m1.rotationY || 0, m1.rotationY, "rotationY", endRotations);
				}
				m2.skewX = (v.skewX == null) ? m1.skewX : _parseAngle(v.skewX, m1.skewX);

				//note: for performance reasons, we combine all skewing into the skewX and rotation values, ignoring skewY but we must still record it so that we can discern how much of the overall skew is attributed to skewX vs. skewY. Otherwise, if the skewY would always act relative (tween skewY to 10deg, for example, multiple times and if we always combine things into skewX, we can't remember that skewY was 10 from last time). Remember, a skewY of 10 degrees looks the same as a rotation of 10 degrees plus a skewX of -10 degrees.
				m2.skewY = (v.skewY == null) ? m1.skewY : _parseAngle(v.skewY, m1.skewY);
				if ((skewY = m2.skewY - m1.skewY)) {
					m2.skewX += skewY;
					m2.rotation += skewY;
				}
			}

			if (_supports3D && v.force3D != null) {
				m1.force3D = v.force3D;
				hasChange = true;
			}

			m1.skewType = v.skewType || m1.skewType || CSSPlugin.defaultSkewType;

			has3D = (m1.force3D || m1.z || m1.rotationX || m1.rotationY || m2.z || m2.rotationX || m2.rotationY || m2.perspective);
			if (!has3D && v.scale != null) {
				m2.scaleZ = 1; //no need to tween scaleZ.
			}

			while (--i > -1) {
				p = _transformProps[i];
				orig = m2[p] - m1[p];
				if (orig > min || orig < -min || _forcePT[p] != null) {
					hasChange = true;
					pt = new CSSPropTween(m1, p, m1[p], orig, pt);
					if (p in endRotations) {
						pt.e = endRotations[p]; //directional rotations typically have compensated values during the tween, but we need to make sure they end at exactly what the user requested
					}
					pt.xs0 = 0; //ensures the value stays numeric in setRatio()
					pt.plugin = plugin;
					cssp._overwriteProps.push(pt.n);
				}
			}

			orig = v.transformOrigin;
			if (orig || (_supports3D && has3D && m1.zOrigin)) { //if anything 3D is happening and there's a transformOrigin with a z component that's non-zero, we must ensure that the transformOrigin's z-component is set to 0 so that we can manually do those calculations to get around Safari bugs. Even if the user didn't specifically define a "transformOrigin" in this particular tween (maybe they did it via css directly).
				if (_transformProp) {
					hasChange = true;
					p = _transformOriginProp;
					orig = (orig || _getStyle(t, p, _cs, false, "50% 50%")) + ""; //cast as string to avoid errors
					pt = new CSSPropTween(style, p, 0, 0, pt, -1, "transformOrigin");
					pt.b = style[p];
					pt.plugin = plugin;
					if (_supports3D) {
						copy = m1.zOrigin;
						orig = orig.split(" ");
						m1.zOrigin = ((orig.length > 2 && !(copy !== 0 && orig[2] === "0px")) ? parseFloat(orig[2]) : copy) || 0; //Safari doesn't handle the z part of transformOrigin correctly, so we'll manually handle it in the _set3DTransformRatio() method.
						pt.xs0 = pt.e = orig[0] + " " + (orig[1] || "50%") + " 0px"; //we must define a z value of 0px specifically otherwise iOS 5 Safari will stick with the old one (if one was defined)!
						pt = new CSSPropTween(m1, "zOrigin", 0, 0, pt, -1, pt.n); //we must create a CSSPropTween for the _gsTransform.zOrigin so that it gets reset properly at the beginning if the tween runs backward (as opposed to just setting m1.zOrigin here)
						pt.b = copy;
						pt.xs0 = pt.e = m1.zOrigin;
					} else {
						pt.xs0 = pt.e = orig;
					}

				//for older versions of IE (6-8), we need to manually calculate things inside the setRatio() function. We record origin x and y (ox and oy) and whether or not the values are percentages (oxp and oyp).
				} else {
					_parsePosition(orig + "", m1);
				}
			}

			if (hasChange) {
				cssp._transformType = (has3D || this._transformType === 3) ? 3 : 2; //quicker than calling cssp._enableTransforms();
			}
			return pt;
		}, prefix:true});

		_registerComplexSpecialProp("boxShadow", {defaultValue:"0px 0px 0px 0px #999", prefix:true, color:true, multi:true, keyword:"inset"});

		_registerComplexSpecialProp("borderRadius", {defaultValue:"0px", parser:function(t, e, p, cssp, pt, plugin) {
			e = this.format(e);
			var props = ["borderTopLeftRadius","borderTopRightRadius","borderBottomRightRadius","borderBottomLeftRadius"],
				style = t.style,
				ea1, i, es2, bs2, bs, es, bn, en, w, h, esfx, bsfx, rel, hn, vn, em;
			w = parseFloat(t.offsetWidth);
			h = parseFloat(t.offsetHeight);
			ea1 = e.split(" ");
			for (i = 0; i < props.length; i++) { //if we're dealing with percentages, we must convert things separately for the horizontal and vertical axis!
				if (this.p.indexOf("border")) { //older browsers used a prefix
					props[i] = _checkPropPrefix(props[i]);
				}
				bs = bs2 = _getStyle(t, props[i], _cs, false, "0px");
				if (bs.indexOf(" ") !== -1) {
					bs2 = bs.split(" ");
					bs = bs2[0];
					bs2 = bs2[1];
				}
				es = es2 = ea1[i];
				bn = parseFloat(bs);
				bsfx = bs.substr((bn + "").length);
				rel = (es.charAt(1) === "=");
				if (rel) {
					en = parseInt(es.charAt(0)+"1", 10);
					es = es.substr(2);
					en *= parseFloat(es);
					esfx = es.substr((en + "").length - (en < 0 ? 1 : 0)) || "";
				} else {
					en = parseFloat(es);
					esfx = es.substr((en + "").length);
				}
				if (esfx === "") {
					esfx = _suffixMap[p] || bsfx;
				}
				if (esfx !== bsfx) {
					hn = _convertToPixels(t, "borderLeft", bn, bsfx); //horizontal number (we use a bogus "borderLeft" property just because the _convertToPixels() method searches for the keywords "Left", "Right", "Top", and "Bottom" to determine of it's a horizontal or vertical property, and we need "border" in the name so that it knows it should measure relative to the element itself, not its parent.
					vn = _convertToPixels(t, "borderTop", bn, bsfx); //vertical number
					if (esfx === "%") {
						bs = (hn / w * 100) + "%";
						bs2 = (vn / h * 100) + "%";
					} else if (esfx === "em") {
						em = _convertToPixels(t, "borderLeft", 1, "em");
						bs = (hn / em) + "em";
						bs2 = (vn / em) + "em";
					} else {
						bs = hn + "px";
						bs2 = vn + "px";
					}
					if (rel) {
						es = (parseFloat(bs) + en) + esfx;
						es2 = (parseFloat(bs2) + en) + esfx;
					}
				}
				pt = _parseComplex(style, props[i], bs + " " + bs2, es + " " + es2, false, "0px", pt);
			}
			return pt;
		}, prefix:true, formatter:_getFormatter("0px 0px 0px 0px", false, true)});
		_registerComplexSpecialProp("backgroundPosition", {defaultValue:"0 0", parser:function(t, e, p, cssp, pt, plugin) {
			var bp = "background-position",
				cs = (_cs || _getComputedStyle(t, null)),
				bs = this.format( ((cs) ? _ieVers ? cs.getPropertyValue(bp + "-x") + " " + cs.getPropertyValue(bp + "-y") : cs.getPropertyValue(bp) : t.currentStyle.backgroundPositionX + " " + t.currentStyle.backgroundPositionY) || "0 0"), //Internet Explorer doesn't report background-position correctly - we must query background-position-x and background-position-y and combine them (even in IE10). Before IE9, we must do the same with the currentStyle object and use camelCase
				es = this.format(e),
				ba, ea, i, pct, overlap, src;
			if ((bs.indexOf("%") !== -1) !== (es.indexOf("%") !== -1)) {
				src = _getStyle(t, "backgroundImage").replace(_urlExp, "");
				if (src && src !== "none") {
					ba = bs.split(" ");
					ea = es.split(" ");
					_tempImg.setAttribute("src", src); //set the temp <img>'s src to the background-image so that we can measure its width/height
					i = 2;
					while (--i > -1) {
						bs = ba[i];
						pct = (bs.indexOf("%") !== -1);
						if (pct !== (ea[i].indexOf("%") !== -1)) {
							overlap = (i === 0) ? t.offsetWidth - _tempImg.width : t.offsetHeight - _tempImg.height;
							ba[i] = pct ? (parseFloat(bs) / 100 * overlap) + "px" : (parseFloat(bs) / overlap * 100) + "%";
						}
					}
					bs = ba.join(" ");
				}
			}
			return this.parseComplex(t.style, bs, es, pt, plugin);
		}, formatter:_parsePosition});
		_registerComplexSpecialProp("backgroundSize", {defaultValue:"0 0", formatter:_parsePosition});
		_registerComplexSpecialProp("perspective", {defaultValue:"0px", prefix:true});
		_registerComplexSpecialProp("perspectiveOrigin", {defaultValue:"50% 50%", prefix:true});
		_registerComplexSpecialProp("transformStyle", {prefix:true});
		_registerComplexSpecialProp("backfaceVisibility", {prefix:true});
		_registerComplexSpecialProp("userSelect", {prefix:true});
		_registerComplexSpecialProp("margin", {parser:_getEdgeParser("marginTop,marginRight,marginBottom,marginLeft")});
		_registerComplexSpecialProp("padding", {parser:_getEdgeParser("paddingTop,paddingRight,paddingBottom,paddingLeft")});
		_registerComplexSpecialProp("clip", {defaultValue:"rect(0px,0px,0px,0px)", parser:function(t, e, p, cssp, pt, plugin){
			var b, cs, delim;
			if (_ieVers < 9) { //IE8 and earlier don't report a "clip" value in the currentStyle - instead, the values are split apart into clipTop, clipRight, clipBottom, and clipLeft. Also, in IE7 and earlier, the values inside rect() are space-delimited, not comma-delimited.
				cs = t.currentStyle;
				delim = _ieVers < 8 ? " " : ",";
				b = "rect(" + cs.clipTop + delim + cs.clipRight + delim + cs.clipBottom + delim + cs.clipLeft + ")";
				e = this.format(e).split(",").join(delim);
			} else {
				b = this.format(_getStyle(t, this.p, _cs, false, this.dflt));
				e = this.format(e);
			}
			return this.parseComplex(t.style, b, e, pt, plugin);
		}});
		_registerComplexSpecialProp("textShadow", {defaultValue:"0px 0px 0px #999", color:true, multi:true});
		_registerComplexSpecialProp("autoRound,strictUnits", {parser:function(t, e, p, cssp, pt) {return pt;}}); //just so that we can ignore these properties (not tween them)
		_registerComplexSpecialProp("border", {defaultValue:"0px solid #000", parser:function(t, e, p, cssp, pt, plugin) {
				return this.parseComplex(t.style, this.format(_getStyle(t, "borderTopWidth", _cs, false, "0px") + " " + _getStyle(t, "borderTopStyle", _cs, false, "solid") + " " + _getStyle(t, "borderTopColor", _cs, false, "#000")), this.format(e), pt, plugin);
			}, color:true, formatter:function(v) {
				var a = v.split(" ");
				return a[0] + " " + (a[1] || "solid") + " " + (v.match(_colorExp) || ["#000"])[0];
			}});
		_registerComplexSpecialProp("borderWidth", {parser:_getEdgeParser("borderTopWidth,borderRightWidth,borderBottomWidth,borderLeftWidth")}); //Firefox doesn't pick up on borderWidth set in style sheets (only inline).
		_registerComplexSpecialProp("float,cssFloat,styleFloat", {parser:function(t, e, p, cssp, pt, plugin) {
			var s = t.style,
				prop = ("cssFloat" in s) ? "cssFloat" : "styleFloat";
			return new CSSPropTween(s, prop, 0, 0, pt, -1, p, false, 0, s[prop], e);
		}});

		//opacity-related
		var _setIEOpacityRatio = function(v) {
				var t = this.t, //refers to the element's style property
					filters = t.filter || _getStyle(this.data, "filter"),
					val = (this.s + this.c * v) | 0,
					skip;
				if (val === 100) { //for older versions of IE that need to use a filter to apply opacity, we should remove the filter if opacity hits 1 in order to improve performance, but make sure there isn't a transform (matrix) or gradient in the filters.
					if (filters.indexOf("atrix(") === -1 && filters.indexOf("radient(") === -1 && filters.indexOf("oader(") === -1) {
						t.removeAttribute("filter");
						skip = (!_getStyle(this.data, "filter")); //if a class is applied that has an alpha filter, it will take effect (we don't want that), so re-apply our alpha filter in that case. We must first remove it and then check.
					} else {
						t.filter = filters.replace(_alphaFilterExp, "");
						skip = true;
					}
				}
				if (!skip) {
					if (this.xn1) {
						t.filter = filters = filters || ("alpha(opacity=" + val + ")"); //works around bug in IE7/8 that prevents changes to "visibility" from being applied properly if the filter is changed to a different alpha on the same frame.
					}
					if (filters.indexOf("pacity") === -1) { //only used if browser doesn't support the standard opacity style property (IE 7 and 8). We omit the "O" to avoid case-sensitivity issues
						if (val !== 0 || !this.xn1) { //bugs in IE7/8 won't render the filter properly if opacity is ADDED on the same frame/render as "visibility" changes (this.xn1 is 1 if this tween is an "autoAlpha" tween)
							t.filter = filters + " alpha(opacity=" + val + ")"; //we round the value because otherwise, bugs in IE7/8 can prevent "visibility" changes from being applied properly.
						}
					} else {
						t.filter = filters.replace(_opacityExp, "opacity=" + val);
					}
				}
			};
		_registerComplexSpecialProp("opacity,alpha,autoAlpha", {defaultValue:"1", parser:function(t, e, p, cssp, pt, plugin) {
			var b = parseFloat(_getStyle(t, "opacity", _cs, false, "1")),
				style = t.style,
				isAutoAlpha = (p === "autoAlpha");
			if (typeof(e) === "string" && e.charAt(1) === "=") {
				e = ((e.charAt(0) === "-") ? -1 : 1) * parseFloat(e.substr(2)) + b;
			}
			if (isAutoAlpha && b === 1 && _getStyle(t, "visibility", _cs) === "hidden" && e !== 0) { //if visibility is initially set to "hidden", we should interpret that as intent to make opacity 0 (a convenience)
				b = 0;
			}
			if (_supportsOpacity) {
				pt = new CSSPropTween(style, "opacity", b, e - b, pt);
			} else {
				pt = new CSSPropTween(style, "opacity", b * 100, (e - b) * 100, pt);
				pt.xn1 = isAutoAlpha ? 1 : 0; //we need to record whether or not this is an autoAlpha so that in the setRatio(), we know to duplicate the setting of the alpha in order to work around a bug in IE7 and IE8 that prevents changes to "visibility" from taking effect if the filter is changed to a different alpha(opacity) at the same time. Setting it to the SAME value first, then the new value works around the IE7/8 bug.
				style.zoom = 1; //helps correct an IE issue.
				pt.type = 2;
				pt.b = "alpha(opacity=" + pt.s + ")";
				pt.e = "alpha(opacity=" + (pt.s + pt.c) + ")";
				pt.data = t;
				pt.plugin = plugin;
				pt.setRatio = _setIEOpacityRatio;
			}
			if (isAutoAlpha) { //we have to create the "visibility" PropTween after the opacity one in the linked list so that they run in the order that works properly in IE8 and earlier
				pt = new CSSPropTween(style, "visibility", 0, 0, pt, -1, null, false, 0, ((b !== 0) ? "inherit" : "hidden"), ((e === 0) ? "hidden" : "inherit"));
				pt.xs0 = "inherit";
				cssp._overwriteProps.push(pt.n);
				cssp._overwriteProps.push(p);
			}
			return pt;
		}});


		var _removeProp = function(s, p) {
				if (p) {
					if (s.removeProperty) {
						if (p.substr(0,2) === "ms") { //Microsoft browsers don't conform to the standard of capping the first prefix character, so we adjust so that when we prefix the caps with a dash, it's correct (otherwise it'd be "ms-transform" instead of "-ms-transform" for IE9, for example)
							p = "M" + p.substr(1);
						}
						s.removeProperty(p.replace(_capsExp, "-$1").toLowerCase());
					} else { //note: old versions of IE use "removeAttribute()" instead of "removeProperty()"
						s.removeAttribute(p);
					}
				}
			},
			_setClassNameRatio = function(v) {
				this.t._gsClassPT = this;
				if (v === 1 || v === 0) {
					this.t.setAttribute("class", (v === 0) ? this.b : this.e);
					var mpt = this.data, //first MiniPropTween
						s = this.t.style;
					while (mpt) {
						if (!mpt.v) {
							_removeProp(s, mpt.p);
						} else {
							s[mpt.p] = mpt.v;
						}
						mpt = mpt._next;
					}
					if (v === 1 && this.t._gsClassPT === this) {
						this.t._gsClassPT = null;
					}
				} else if (this.t.getAttribute("class") !== this.e) {
					this.t.setAttribute("class", this.e);
				}
			};
		_registerComplexSpecialProp("className", {parser:function(t, e, p, cssp, pt, plugin, vars) {
			var b = t.getAttribute("class") || "", //don't use t.className because it doesn't work consistently on SVG elements; getAttribute("class") and setAttribute("class", value") is more reliable.
				cssText = t.style.cssText,
				difData, bs, cnpt, cnptLookup, mpt;
			pt = cssp._classNamePT = new CSSPropTween(t, p, 0, 0, pt, 2);
			pt.setRatio = _setClassNameRatio;
			pt.pr = -11;
			_hasPriority = true;
			pt.b = b;
			bs = _getAllStyles(t, _cs);
			//if there's a className tween already operating on the target, force it to its end so that the necessary inline styles are removed and the class name is applied before we determine the end state (we don't want inline styles interfering that were there just for class-specific values)
			cnpt = t._gsClassPT;
			if (cnpt) {
				cnptLookup = {};
				mpt = cnpt.data; //first MiniPropTween which stores the inline styles - we need to force these so that the inline styles don't contaminate things. Otherwise, there's a small chance that a tween could start and the inline values match the destination values and they never get cleaned.
				while (mpt) {
					cnptLookup[mpt.p] = 1;
					mpt = mpt._next;
				}
				cnpt.setRatio(1);
			}
			t._gsClassPT = pt;
			pt.e = (e.charAt(1) !== "=") ? e : b.replace(new RegExp("\\s*\\b" + e.substr(2) + "\\b"), "") + ((e.charAt(0) === "+") ? " " + e.substr(2) : "");
			if (cssp._tween._duration) { //if it's a zero-duration tween, there's no need to tween anything or parse the data. In fact, if we switch classes temporarily (which we must do for proper parsing) and the class has a transition applied, it could cause a quick flash to the end state and back again initially in some browsers.
				t.setAttribute("class", pt.e);
				difData = _cssDif(t, bs, _getAllStyles(t), vars, cnptLookup);
				t.setAttribute("class", b);
				pt.data = difData.firstMPT;
				t.style.cssText = cssText; //we recorded cssText before we swapped classes and ran _getAllStyles() because in cases when a className tween is overwritten, we remove all the related tweening properties from that class change (otherwise class-specific stuff can't override properties we've directly set on the target's style object due to specificity).
				pt = pt.xfirst = cssp.parse(t, difData.difs, pt, plugin); //we record the CSSPropTween as the xfirst so that we can handle overwriting propertly (if "className" gets overwritten, we must kill all the properties associated with the className part of the tween, so we can loop through from xfirst to the pt itself)
			}
			return pt;
		}});


		var _setClearPropsRatio = function(v) {
			if (v === 1 || v === 0) if (this.data._totalTime === this.data._totalDuration && this.data.data !== "isFromStart") { //this.data refers to the tween. Only clear at the END of the tween (remember, from() tweens make the ratio go from 1 to 0, so we can't just check that and if the tween is the zero-duration one that's created internally to render the starting values in a from() tween, ignore that because otherwise, for example, from(...{height:100, clearProps:"height", delay:1}) would wipe the height at the beginning of the tween and after 1 second, it'd kick back in).
				var s = this.t.style,
					transformParse = _specialProps.transform.parse,
					a, p, i, clearTransform;
				if (this.e === "all") {
					s.cssText = "";
					clearTransform = true;
				} else {
					a = this.e.split(",");
					i = a.length;
					while (--i > -1) {
						p = a[i];
						if (_specialProps[p]) {
							if (_specialProps[p].parse === transformParse) {
								clearTransform = true;
							} else {
								p = (p === "transformOrigin") ? _transformOriginProp : _specialProps[p].p; //ensures that special properties use the proper browser-specific property name, like "scaleX" might be "-webkit-transform" or "boxShadow" might be "-moz-box-shadow"
							}
						}
						_removeProp(s, p);
					}
				}
				if (clearTransform) {
					_removeProp(s, _transformProp);
					if (this.t._gsTransform) {
						delete this.t._gsTransform;
					}
				}

			}
		};
		_registerComplexSpecialProp("clearProps", {parser:function(t, e, p, cssp, pt) {
			pt = new CSSPropTween(t, p, 0, 0, pt, 2);
			pt.setRatio = _setClearPropsRatio;
			pt.e = e;
			pt.pr = -10;
			pt.data = cssp._tween;
			_hasPriority = true;
			return pt;
		}});

		p = "bezier,throwProps,physicsProps,physics2D".split(",");
		i = p.length;
		while (i--) {
			_registerPluginProp(p[i]);
		}








		p = CSSPlugin.prototype;
		p._firstPT = null;

		//gets called when the tween renders for the first time. This kicks everything off, recording start/end values, etc.
		p._onInitTween = function(target, vars, tween) {
			if (!target.nodeType) { //css is only for dom elements
				return false;
			}
			this._target = target;
			this._tween = tween;
			this._vars = vars;
			_autoRound = vars.autoRound;
			_hasPriority = false;
			_suffixMap = vars.suffixMap || CSSPlugin.suffixMap;
			_cs = _getComputedStyle(target, "");
			_overwriteProps = this._overwriteProps;
			var style = target.style,
				v, pt, pt2, first, last, next, zIndex, tpt, threeD;
			if (_reqSafariFix) if (style.zIndex === "") {
				v = _getStyle(target, "zIndex", _cs);
				if (v === "auto" || v === "") {
					//corrects a bug in [non-Android] Safari that prevents it from repainting elements in their new positions if they don't have a zIndex set. We also can't just apply this inside _parseTransform() because anything that's moved in any way (like using "left" or "top" instead of transforms like "x" and "y") can be affected, so it is best to ensure that anything that's tweening has a z-index. Setting "WebkitPerspective" to a non-zero value worked too except that on iOS Safari things would flicker randomly. Plus zIndex is less memory-intensive.
					this._addLazySet(style, "zIndex", 0);
				}
			}

			if (typeof(vars) === "string") {
				first = style.cssText;
				v = _getAllStyles(target, _cs);
				style.cssText = first + ";" + vars;
				v = _cssDif(target, v, _getAllStyles(target)).difs;
				if (!_supportsOpacity && _opacityValExp.test(vars)) {
					v.opacity = parseFloat( RegExp.$1 );
				}
				vars = v;
				style.cssText = first;
			}
			this._firstPT = pt = this.parse(target, vars, null);

			if (this._transformType) {
				threeD = (this._transformType === 3);
				if (!_transformProp) {
					style.zoom = 1; //helps correct an IE issue.
				} else if (_isSafari) {
					_reqSafariFix = true;
					//if zIndex isn't set, iOS Safari doesn't repaint things correctly sometimes (seemingly at random).
					if (style.zIndex === "") {
						zIndex = _getStyle(target, "zIndex", _cs);
						if (zIndex === "auto" || zIndex === "") {
							this._addLazySet(style, "zIndex", 0);
						}
					}
					//Setting WebkitBackfaceVisibility corrects 3 bugs:
					// 1) [non-Android] Safari skips rendering changes to "top" and "left" that are made on the same frame/render as a transform update.
					// 2) iOS Safari sometimes neglects to repaint elements in their new positions. Setting "WebkitPerspective" to a non-zero value worked too except that on iOS Safari things would flicker randomly.
					// 3) Safari sometimes displayed odd artifacts when tweening the transform (or WebkitTransform) property, like ghosts of the edges of the element remained. Definitely a browser bug.
					//Note: we allow the user to override the auto-setting by defining WebkitBackfaceVisibility in the vars of the tween.
					if (_isSafariLT6) {
						this._addLazySet(style, "WebkitBackfaceVisibility", this._vars.WebkitBackfaceVisibility || (threeD ? "visible" : "hidden"));
					}
				}
				pt2 = pt;
				while (pt2 && pt2._next) {
					pt2 = pt2._next;
				}
				tpt = new CSSPropTween(target, "transform", 0, 0, null, 2);
				this._linkCSSP(tpt, null, pt2);
				tpt.setRatio = (threeD && _supports3D) ? _set3DTransformRatio : _transformProp ? _set2DTransformRatio : _setIETransformRatio;
				tpt.data = this._transform || _getTransform(target, _cs, true);
				_overwriteProps.pop(); //we don't want to force the overwrite of all "transform" tweens of the target - we only care about individual transform properties like scaleX, rotation, etc. The CSSPropTween constructor automatically adds the property to _overwriteProps which is why we need to pop() here.
			}

			if (_hasPriority) {
				//reorders the linked list in order of pr (priority)
				while (pt) {
					next = pt._next;
					pt2 = first;
					while (pt2 && pt2.pr > pt.pr) {
						pt2 = pt2._next;
					}
					if ((pt._prev = pt2 ? pt2._prev : last)) {
						pt._prev._next = pt;
					} else {
						first = pt;
					}
					if ((pt._next = pt2)) {
						pt2._prev = pt;
					} else {
						last = pt;
					}
					pt = next;
				}
				this._firstPT = first;
			}
			return true;
		};


		p.parse = function(target, vars, pt, plugin) {
			var style = target.style,
				p, sp, bn, en, bs, es, bsfx, esfx, isStr, rel;
			for (p in vars) {
				es = vars[p]; //ending value string
				sp = _specialProps[p]; //SpecialProp lookup.
				if (sp) {
					pt = sp.parse(target, es, p, this, pt, plugin, vars);

				} else {
					bs = _getStyle(target, p, _cs) + "";
					isStr = (typeof(es) === "string");
					if (p === "color" || p === "fill" || p === "stroke" || p.indexOf("Color") !== -1 || (isStr && _rgbhslExp.test(es))) { //Opera uses background: to define color sometimes in addition to backgroundColor:
						if (!isStr) {
							es = _parseColor(es);
							es = ((es.length > 3) ? "rgba(" : "rgb(") + es.join(",") + ")";
						}
						pt = _parseComplex(style, p, bs, es, true, "transparent", pt, 0, plugin);

					} else if (isStr && (es.indexOf(" ") !== -1 || es.indexOf(",") !== -1)) {
						pt = _parseComplex(style, p, bs, es, true, null, pt, 0, plugin);

					} else {
						bn = parseFloat(bs);
						bsfx = (bn || bn === 0) ? bs.substr((bn + "").length) : ""; //remember, bs could be non-numeric like "normal" for fontWeight, so we should default to a blank suffix in that case.

						if (bs === "" || bs === "auto") {
							if (p === "width" || p === "height") {
								bn = _getDimension(target, p, _cs);
								bsfx = "px";
							} else if (p === "left" || p === "top") {
								bn = _calculateOffset(target, p, _cs);
								bsfx = "px";
							} else {
								bn = (p !== "opacity") ? 0 : 1;
								bsfx = "";
							}
						}

						rel = (isStr && es.charAt(1) === "=");
						if (rel) {
							en = parseInt(es.charAt(0) + "1", 10);
							es = es.substr(2);
							en *= parseFloat(es);
							esfx = es.replace(_suffixExp, "");
						} else {
							en = parseFloat(es);
							esfx = isStr ? es.substr((en + "").length) || "" : "";
						}

						if (esfx === "") {
							esfx = (p in _suffixMap) ? _suffixMap[p] : bsfx; //populate the end suffix, prioritizing the map, then if none is found, use the beginning suffix.
						}

						es = (en || en === 0) ? (rel ? en + bn : en) + esfx : vars[p]; //ensures that any += or -= prefixes are taken care of. Record the end value before normalizing the suffix because we always want to end the tween on exactly what they intended even if it doesn't match the beginning value's suffix.

						//if the beginning/ending suffixes don't match, normalize them...
						if (bsfx !== esfx) if (esfx !== "") if (en || en === 0) if (bn) { //note: if the beginning value (bn) is 0, we don't need to convert units!
							bn = _convertToPixels(target, p, bn, bsfx);
							if (esfx === "%") {
								bn /= _convertToPixels(target, p, 100, "%") / 100;
								if (vars.strictUnits !== true) { //some browsers report only "px" values instead of allowing "%" with getComputedStyle(), so we assume that if we're tweening to a %, we should start there too unless strictUnits:true is defined. This approach is particularly useful for responsive designs that use from() tweens.
									bs = bn + "%";
								}

							} else if (esfx === "em") {
								bn /= _convertToPixels(target, p, 1, "em");

							//otherwise convert to pixels.
							} else if (esfx !== "px") {
								en = _convertToPixels(target, p, en, esfx);
								esfx = "px"; //we don't use bsfx after this, so we don't need to set it to px too.
							}
							if (rel) if (en || en === 0) {
								es = (en + bn) + esfx; //the changes we made affect relative calculations, so adjust the end value here.
							}
						}

						if (rel) {
							en += bn;
						}

						if ((bn || bn === 0) && (en || en === 0)) { //faster than isNaN(). Also, previously we required en !== bn but that doesn't really gain much performance and it prevents _parseToProxy() from working properly if beginning and ending values match but need to get tweened by an external plugin anyway. For example, a bezier tween where the target starts at left:0 and has these points: [{left:50},{left:0}] wouldn't work properly because when parsing the last point, it'd match the first (current) one and a non-tweening CSSPropTween would be recorded when we actually need a normal tween (type:0) so that things get updated during the tween properly.
							pt = new CSSPropTween(style, p, bn, en - bn, pt, 0, p, (_autoRound !== false && (esfx === "px" || p === "zIndex")), 0, bs, es);
							pt.xs0 = esfx;
							//DEBUG: _log("tween "+p+" from "+pt.b+" ("+bn+esfx+") to "+pt.e+" with suffix: "+pt.xs0);
						} else if (style[p] === undefined || !es && (es + "" === "NaN" || es == null)) {
							_log("invalid " + p + " tween value: " + vars[p]);
						} else {
							pt = new CSSPropTween(style, p, en || bn || 0, 0, pt, -1, p, false, 0, bs, es);
							pt.xs0 = (es === "none" && (p === "display" || p.indexOf("Style") !== -1)) ? bs : es; //intermediate value should typically be set immediately (end value) except for "display" or things like borderTopStyle, borderBottomStyle, etc. which should use the beginning value during the tween.
							//DEBUG: _log("non-tweening value "+p+": "+pt.xs0);
						}
					}
				}
				if (plugin) if (pt && !pt.plugin) {
					pt.plugin = plugin;
				}
			}
			return pt;
		};


		//gets called every time the tween updates, passing the new ratio (typically a value between 0 and 1, but not always (for example, if an Elastic.easeOut is used, the value can jump above 1 mid-tween). It will always start and 0 and end at 1.
		p.setRatio = function(v) {
			var pt = this._firstPT,
				min = 0.000001,
				val, str, i;

			//at the end of the tween, we set the values to exactly what we received in order to make sure non-tweening values (like "position" or "float" or whatever) are set and so that if the beginning/ending suffixes (units) didn't match and we normalized to px, the value that the user passed in is used here. We check to see if the tween is at its beginning in case it's a from() tween in which case the ratio will actually go from 1 to 0 over the course of the tween (backwards).
			if (v === 1 && (this._tween._time === this._tween._duration || this._tween._time === 0)) {
				while (pt) {
					if (pt.type !== 2) {
						pt.t[pt.p] = pt.e;
					} else {
						pt.setRatio(v);
					}
					pt = pt._next;
				}

			} else if (v || !(this._tween._time === this._tween._duration || this._tween._time === 0) || this._tween._rawPrevTime === -0.000001) {
				while (pt) {
					val = pt.c * v + pt.s;
					if (pt.r) {
						val = Math.round(val);
					} else if (val < min) if (val > -min) {
						val = 0;
					}
					if (!pt.type) {
						pt.t[pt.p] = val + pt.xs0;
					} else if (pt.type === 1) { //complex value (one that typically has multiple numbers inside a string, like "rect(5px,10px,20px,25px)"
						i = pt.l;
						if (i === 2) {
							pt.t[pt.p] = pt.xs0 + val + pt.xs1 + pt.xn1 + pt.xs2;
						} else if (i === 3) {
							pt.t[pt.p] = pt.xs0 + val + pt.xs1 + pt.xn1 + pt.xs2 + pt.xn2 + pt.xs3;
						} else if (i === 4) {
							pt.t[pt.p] = pt.xs0 + val + pt.xs1 + pt.xn1 + pt.xs2 + pt.xn2 + pt.xs3 + pt.xn3 + pt.xs4;
						} else if (i === 5) {
							pt.t[pt.p] = pt.xs0 + val + pt.xs1 + pt.xn1 + pt.xs2 + pt.xn2 + pt.xs3 + pt.xn3 + pt.xs4 + pt.xn4 + pt.xs5;
						} else {
							str = pt.xs0 + val + pt.xs1;
							for (i = 1; i < pt.l; i++) {
								str += pt["xn"+i] + pt["xs"+(i+1)];
							}
							pt.t[pt.p] = str;
						}

					} else if (pt.type === -1) { //non-tweening value
						pt.t[pt.p] = pt.xs0;

					} else if (pt.setRatio) { //custom setRatio() for things like SpecialProps, external plugins, etc.
						pt.setRatio(v);
					}
					pt = pt._next;
				}

			//if the tween is reversed all the way back to the beginning, we need to restore the original values which may have different units (like % instead of px or em or whatever).
			} else {
				while (pt) {
					if (pt.type !== 2) {
						pt.t[pt.p] = pt.b;
					} else {
						pt.setRatio(v);
					}
					pt = pt._next;
				}
			}
		};

		/**
		 * @private
		 * Forces rendering of the target's transforms (rotation, scale, etc.) whenever the CSSPlugin's setRatio() is called.
		 * Basically, this tells the CSSPlugin to create a CSSPropTween (type 2) after instantiation that runs last in the linked
		 * list and calls the appropriate (3D or 2D) rendering function. We separate this into its own method so that we can call
		 * it from other plugins like BezierPlugin if, for example, it needs to apply an autoRotation and this CSSPlugin
		 * doesn't have any transform-related properties of its own. You can call this method as many times as you
		 * want and it won't create duplicate CSSPropTweens.
		 *
		 * @param {boolean} threeD if true, it should apply 3D tweens (otherwise, just 2D ones are fine and typically faster)
		 */
		p._enableTransforms = function(threeD) {
			this._transformType = (threeD || this._transformType === 3) ? 3 : 2;
			this._transform = this._transform || _getTransform(this._target, _cs, true); //ensures that the element has a _gsTransform property with the appropriate values.
		};

		var lazySet = function(v) {
			this.t[this.p] = this.e;
			this.data._linkCSSP(this, this._next, null, true); //we purposefully keep this._next even though it'd make sense to null it, but this is a performance optimization, as this happens during the while (pt) {} loop in setRatio() at the bottom of which it sets pt = pt._next, so if we null it, the linked list will be broken in that loop.
		};
		/** @private Gives us a way to set a value on the first render (and only the first render). **/
		p._addLazySet = function(t, p, v) {
			var pt = this._firstPT = new CSSPropTween(t, p, 0, 0, this._firstPT, 2);
			pt.e = v;
			pt.setRatio = lazySet;
			pt.data = this;
		};

		/** @private **/
		p._linkCSSP = function(pt, next, prev, remove) {
			if (pt) {
				if (next) {
					next._prev = pt;
				}
				if (pt._next) {
					pt._next._prev = pt._prev;
				}
				if (pt._prev) {
					pt._prev._next = pt._next;
				} else if (this._firstPT === pt) {
					this._firstPT = pt._next;
					remove = true; //just to prevent resetting this._firstPT 5 lines down in case pt._next is null. (optimized for speed)
				}
				if (prev) {
					prev._next = pt;
				} else if (!remove && this._firstPT === null) {
					this._firstPT = pt;
				}
				pt._next = next;
				pt._prev = prev;
			}
			return pt;
		};

		//we need to make sure that if alpha or autoAlpha is killed, opacity is too. And autoAlpha affects the "visibility" property.
		p._kill = function(lookup) {
			var copy = lookup,
				pt, p, xfirst;
			if (lookup.autoAlpha || lookup.alpha) {
				copy = {};
				for (p in lookup) { //copy the lookup so that we're not changing the original which may be passed elsewhere.
					copy[p] = lookup[p];
				}
				copy.opacity = 1;
				if (copy.autoAlpha) {
					copy.visibility = 1;
				}
			}
			if (lookup.className && (pt = this._classNamePT)) { //for className tweens, we need to kill any associated CSSPropTweens too; a linked list starts at the className's "xfirst".
				xfirst = pt.xfirst;
				if (xfirst && xfirst._prev) {
					this._linkCSSP(xfirst._prev, pt._next, xfirst._prev._prev); //break off the prev
				} else if (xfirst === this._firstPT) {
					this._firstPT = pt._next;
				}
				if (pt._next) {
					this._linkCSSP(pt._next, pt._next._next, xfirst._prev);
				}
				this._classNamePT = null;
			}
			return TweenPlugin.prototype._kill.call(this, copy);
		};



		//used by cascadeTo() for gathering all the style properties of each child element into an array for comparison.
		var _getChildStyles = function(e, props, targets) {
				var children, i, child, type;
				if (e.slice) {
					i = e.length;
					while (--i > -1) {
						_getChildStyles(e[i], props, targets);
					}
					return;
				}
				children = e.childNodes;
				i = children.length;
				while (--i > -1) {
					child = children[i];
					type = child.type;
					if (child.style) {
						props.push(_getAllStyles(child));
						if (targets) {
							targets.push(child);
						}
					}
					if ((type === 1 || type === 9 || type === 11) && child.childNodes.length) {
						_getChildStyles(child, props, targets);
					}
				}
			};

		/**
		 * Typically only useful for className tweens that may affect child elements, this method creates a TweenLite
		 * and then compares the style properties of all the target's child elements at the tween's start and end, and
		 * if any are different, it also creates tweens for those and returns an array containing ALL of the resulting
		 * tweens (so that you can easily add() them to a TimelineLite, for example). The reason this functionality is
		 * wrapped into a separate static method of CSSPlugin instead of being integrated into all regular className tweens
		 * is because it creates entirely new tweens that may have completely different targets than the original tween,
		 * so if they were all lumped into the original tween instance, it would be inconsistent with the rest of the API
		 * and it would create other problems. For example:
		 *  - If I create a tween of elementA, that tween instance may suddenly change its target to include 50 other elements (unintuitive if I specifically defined the target I wanted)
		 *  - We can't just create new independent tweens because otherwise, what happens if the original/parent tween is reversed or pause or dropped into a TimelineLite for tight control? You'd expect that tween's behavior to affect all the others.
		 *  - Analyzing every style property of every child before and after the tween is an expensive operation when there are many children, so this behavior shouldn't be imposed on all className tweens by default, especially since it's probably rare that this extra functionality is needed.
		 *
		 * @param {Object} target object to be tweened
		 * @param {number} Duration in seconds (or frames for frames-based tweens)
		 * @param {Object} Object containing the end values, like {className:"newClass", ease:Linear.easeNone}
		 * @return {Array} An array of TweenLite instances
		 */
		CSSPlugin.cascadeTo = function(target, duration, vars) {
			var tween = TweenLite.to(target, duration, vars),
				results = [tween],
				b = [],
				e = [],
				targets = [],
				_reservedProps = TweenLite._internals.reservedProps,
				i, difs, p;
			target = tween._targets || tween.target;
			_getChildStyles(target, b, targets);
			tween.render(duration, true);
			_getChildStyles(target, e);
			tween.render(0, true);
			tween._enabled(true);
			i = targets.length;
			while (--i > -1) {
				difs = _cssDif(targets[i], b[i], e[i]);
				if (difs.firstMPT) {
					difs = difs.difs;
					for (p in vars) {
						if (_reservedProps[p]) {
							difs[p] = vars[p];
						}
					}
					results.push( TweenLite.to(targets[i], duration, difs) );
				}
			}
			return results;
		};

		TweenPlugin.activate([CSSPlugin]);
		return CSSPlugin;

	}, true);

	
	
	
	
	
	
	
	
	
	
/*
 * ----------------------------------------------------------------
 * RoundPropsPlugin
 * ----------------------------------------------------------------
 */
	(function() {

		var RoundPropsPlugin = window._gsDefine.plugin({
				propName: "roundProps",
				priority: -1,
				API: 2,

				//called when the tween renders for the first time. This is where initial values should be recorded and any setup routines should run.
				init: function(target, value, tween) {
					this._tween = tween;
					return true;
				}

			}),
			p = RoundPropsPlugin.prototype;

		p._onInitAllProps = function() {
			var tween = this._tween,
				rp = (tween.vars.roundProps instanceof Array) ? tween.vars.roundProps : tween.vars.roundProps.split(","),
				i = rp.length,
				lookup = {},
				rpt = tween._propLookup.roundProps,
				prop, pt, next;
			while (--i > -1) {
				lookup[rp[i]] = 1;
			}
			i = rp.length;
			while (--i > -1) {
				prop = rp[i];
				pt = tween._firstPT;
				while (pt) {
					next = pt._next; //record here, because it may get removed
					if (pt.pg) {
						pt.t._roundProps(lookup, true);
					} else if (pt.n === prop) {
						this._add(pt.t, prop, pt.s, pt.c);
						//remove from linked list
						if (next) {
							next._prev = pt._prev;
						}
						if (pt._prev) {
							pt._prev._next = next;
						} else if (tween._firstPT === pt) {
							tween._firstPT = next;
						}
						pt._next = pt._prev = null;
						tween._propLookup[prop] = rpt;
					}
					pt = next;
				}
			}
			return false;
		};

		p._add = function(target, p, s, c) {
			this._addTween(target, p, s, s + c, p, true);
			this._overwriteProps.push(p);
		};

	}());










/*
 * ----------------------------------------------------------------
 * AttrPlugin
 * ----------------------------------------------------------------
 */
	window._gsDefine.plugin({
		propName: "attr",
		API: 2,
		version: "0.3.2",

		//called when the tween renders for the first time. This is where initial values should be recorded and any setup routines should run.
		init: function(target, value, tween) {
			var p, start, end;
			if (typeof(target.setAttribute) !== "function") {
				return false;
			}
			this._target = target;
			this._proxy = {};
			this._start = {}; // we record start and end values exactly as they are in case they're strings (not numbers) - we need to be able to revert to them cleanly.
			this._end = {};
			for (p in value) {
				this._start[p] = this._proxy[p] = start = target.getAttribute(p);
				end = this._addTween(this._proxy, p, parseFloat(start), value[p], p);
				this._end[p] = end ? end.s + end.c : value[p];
				this._overwriteProps.push(p);
			}
			return true;
		},

		//called each time the values should be updated, and the ratio gets passed as the only parameter (typically it's a value between 0 and 1, but it can exceed those when using an ease like Elastic.easeOut or Back.easeOut, etc.)
		set: function(ratio) {
			this._super.setRatio.call(this, ratio);
			var props = this._overwriteProps,
				i = props.length,
				lookup = (ratio === 1) ? this._end : ratio ? this._proxy : this._start,
				p;
			while (--i > -1) {
				p = props[i];
				this._target.setAttribute(p, lookup[p] + "");
			}
		}

	});










/*
 * ----------------------------------------------------------------
 * DirectionalRotationPlugin
 * ----------------------------------------------------------------
 */
	window._gsDefine.plugin({
		propName: "directionalRotation",
		API: 2,
		version: "0.2.0",

		//called when the tween renders for the first time. This is where initial values should be recorded and any setup routines should run.
		init: function(target, value, tween) {
			if (typeof(value) !== "object") {
				value = {rotation:value};
			}
			this.finals = {};
			var cap = (value.useRadians === true) ? Math.PI * 2 : 360,
				min = 0.000001,
				p, v, start, end, dif, split;
			for (p in value) {
				if (p !== "useRadians") {
					split = (value[p] + "").split("_");
					v = split[0];
					start = parseFloat( (typeof(target[p]) !== "function") ? target[p] : target[ ((p.indexOf("set") || typeof(target["get" + p.substr(3)]) !== "function") ? p : "get" + p.substr(3)) ]() );
					end = this.finals[p] = (typeof(v) === "string" && v.charAt(1) === "=") ? start + parseInt(v.charAt(0) + "1", 10) * Number(v.substr(2)) : Number(v) || 0;
					dif = end - start;
					if (split.length) {
						v = split.join("_");
						if (v.indexOf("short") !== -1) {
							dif = dif % cap;
							if (dif !== dif % (cap / 2)) {
								dif = (dif < 0) ? dif + cap : dif - cap;
							}
						}
						if (v.indexOf("_cw") !== -1 && dif < 0) {
							dif = ((dif + cap * 9999999999) % cap) - ((dif / cap) | 0) * cap;
						} else if (v.indexOf("ccw") !== -1 && dif > 0) {
							dif = ((dif - cap * 9999999999) % cap) - ((dif / cap) | 0) * cap;
						}
					}
					if (dif > min || dif < -min) {
						this._addTween(target, p, start, start + dif, p);
						this._overwriteProps.push(p);
					}
				}
			}
			return true;
		},

		//called each time the values should be updated, and the ratio gets passed as the only parameter (typically it's a value between 0 and 1, but it can exceed those when using an ease like Elastic.easeOut or Back.easeOut, etc.)
		set: function(ratio) {
			var pt;
			if (ratio !== 1) {
				this._super.setRatio.call(this, ratio);
			} else {
				pt = this._firstPT;
				while (pt) {
					if (pt.f) {
						pt.t[pt.p](this.finals[pt.p]);
					} else {
						pt.t[pt.p] = this.finals[pt.p];
					}
					pt = pt._next;
				}
			}
		}

	})._autoCSS = true;







	
	
	
	
/*
 * ----------------------------------------------------------------
 * EasePack
 * ----------------------------------------------------------------
 */
	window._gsDefine("easing.Back", ["easing.Ease"], function(Ease) {
		
		var w = (window.GreenSockGlobals || window),
			gs = w.com.greensock,
			_2PI = Math.PI * 2,
			_HALF_PI = Math.PI / 2,
			_class = gs._class,
			_create = function(n, f) {
				var C = _class("easing." + n, function(){}, true),
					p = C.prototype = new Ease();
				p.constructor = C;
				p.getRatio = f;
				return C;
			},
			_easeReg = Ease.register || function(){}, //put an empty function in place just as a safety measure in case someone loads an OLD version of TweenLite.js where Ease.register doesn't exist.
			_wrap = function(name, EaseOut, EaseIn, EaseInOut, aliases) {
				var C = _class("easing."+name, {
					easeOut:new EaseOut(),
					easeIn:new EaseIn(),
					easeInOut:new EaseInOut()
				}, true);
				_easeReg(C, name);
				return C;
			},
			EasePoint = function(time, value, next) {
				this.t = time;
				this.v = value;
				if (next) {
					this.next = next;
					next.prev = this;
					this.c = next.v - value;
					this.gap = next.t - time;
				}
			},

			//Back
			_createBack = function(n, f) {
				var C = _class("easing." + n, function(overshoot) {
						this._p1 = (overshoot || overshoot === 0) ? overshoot : 1.70158;
						this._p2 = this._p1 * 1.525;
					}, true),
					p = C.prototype = new Ease();
				p.constructor = C;
				p.getRatio = f;
				p.config = function(overshoot) {
					return new C(overshoot);
				};
				return C;
			},

			Back = _wrap("Back",
				_createBack("BackOut", function(p) {
					return ((p = p - 1) * p * ((this._p1 + 1) * p + this._p1) + 1);
				}),
				_createBack("BackIn", function(p) {
					return p * p * ((this._p1 + 1) * p - this._p1);
				}),
				_createBack("BackInOut", function(p) {
					return ((p *= 2) < 1) ? 0.5 * p * p * ((this._p2 + 1) * p - this._p2) : 0.5 * ((p -= 2) * p * ((this._p2 + 1) * p + this._p2) + 2);
				})
			),


			//SlowMo
			SlowMo = _class("easing.SlowMo", function(linearRatio, power, yoyoMode) {
				power = (power || power === 0) ? power : 0.7;
				if (linearRatio == null) {
					linearRatio = 0.7;
				} else if (linearRatio > 1) {
					linearRatio = 1;
				}
				this._p = (linearRatio !== 1) ? power : 0;
				this._p1 = (1 - linearRatio) / 2;
				this._p2 = linearRatio;
				this._p3 = this._p1 + this._p2;
				this._calcEnd = (yoyoMode === true);
			}, true),
			p = SlowMo.prototype = new Ease(),
			SteppedEase, RoughEase, _createElastic;

		p.constructor = SlowMo;
		p.getRatio = function(p) {
			var r = p + (0.5 - p) * this._p;
			if (p < this._p1) {
				return this._calcEnd ? 1 - ((p = 1 - (p / this._p1)) * p) : r - ((p = 1 - (p / this._p1)) * p * p * p * r);
			} else if (p > this._p3) {
				return this._calcEnd ? 1 - (p = (p - this._p3) / this._p1) * p : r + ((p - r) * (p = (p - this._p3) / this._p1) * p * p * p);
			}
			return this._calcEnd ? 1 : r;
		};
		SlowMo.ease = new SlowMo(0.7, 0.7);

		p.config = SlowMo.config = function(linearRatio, power, yoyoMode) {
			return new SlowMo(linearRatio, power, yoyoMode);
		};


		//SteppedEase
		SteppedEase = _class("easing.SteppedEase", function(steps) {
				steps = steps || 1;
				this._p1 = 1 / steps;
				this._p2 = steps + 1;
			}, true);
		p = SteppedEase.prototype = new Ease();
		p.constructor = SteppedEase;
		p.getRatio = function(p) {
			if (p < 0) {
				p = 0;
			} else if (p >= 1) {
				p = 0.999999999;
			}
			return ((this._p2 * p) >> 0) * this._p1;
		};
		p.config = SteppedEase.config = function(steps) {
			return new SteppedEase(steps);
		};


		//RoughEase
		RoughEase = _class("easing.RoughEase", function(vars) {
			vars = vars || {};
			var taper = vars.taper || "none",
				a = [],
				cnt = 0,
				points = (vars.points || 20) | 0,
				i = points,
				randomize = (vars.randomize !== false),
				clamp = (vars.clamp === true),
				template = (vars.template instanceof Ease) ? vars.template : null,
				strength = (typeof(vars.strength) === "number") ? vars.strength * 0.4 : 0.4,
				x, y, bump, invX, obj, pnt;
			while (--i > -1) {
				x = randomize ? Math.random() : (1 / points) * i;
				y = template ? template.getRatio(x) : x;
				if (taper === "none") {
					bump = strength;
				} else if (taper === "out") {
					invX = 1 - x;
					bump = invX * invX * strength;
				} else if (taper === "in") {
					bump = x * x * strength;
				} else if (x < 0.5) {  //"both" (start)
					invX = x * 2;
					bump = invX * invX * 0.5 * strength;
				} else {				//"both" (end)
					invX = (1 - x) * 2;
					bump = invX * invX * 0.5 * strength;
				}
				if (randomize) {
					y += (Math.random() * bump) - (bump * 0.5);
				} else if (i % 2) {
					y += bump * 0.5;
				} else {
					y -= bump * 0.5;
				}
				if (clamp) {
					if (y > 1) {
						y = 1;
					} else if (y < 0) {
						y = 0;
					}
				}
				a[cnt++] = {x:x, y:y};
			}
			a.sort(function(a, b) {
				return a.x - b.x;
			});

			pnt = new EasePoint(1, 1, null);
			i = points;
			while (--i > -1) {
				obj = a[i];
				pnt = new EasePoint(obj.x, obj.y, pnt);
			}

			this._prev = new EasePoint(0, 0, (pnt.t !== 0) ? pnt : pnt.next);
		}, true);
		p = RoughEase.prototype = new Ease();
		p.constructor = RoughEase;
		p.getRatio = function(p) {
			var pnt = this._prev;
			if (p > pnt.t) {
				while (pnt.next && p >= pnt.t) {
					pnt = pnt.next;
				}
				pnt = pnt.prev;
			} else {
				while (pnt.prev && p <= pnt.t) {
					pnt = pnt.prev;
				}
			}
			this._prev = pnt;
			return (pnt.v + ((p - pnt.t) / pnt.gap) * pnt.c);
		};
		p.config = function(vars) {
			return new RoughEase(vars);
		};
		RoughEase.ease = new RoughEase();


		//Bounce
		_wrap("Bounce",
			_create("BounceOut", function(p) {
				if (p < 1 / 2.75) {
					return 7.5625 * p * p;
				} else if (p < 2 / 2.75) {
					return 7.5625 * (p -= 1.5 / 2.75) * p + 0.75;
				} else if (p < 2.5 / 2.75) {
					return 7.5625 * (p -= 2.25 / 2.75) * p + 0.9375;
				}
				return 7.5625 * (p -= 2.625 / 2.75) * p + 0.984375;
			}),
			_create("BounceIn", function(p) {
				if ((p = 1 - p) < 1 / 2.75) {
					return 1 - (7.5625 * p * p);
				} else if (p < 2 / 2.75) {
					return 1 - (7.5625 * (p -= 1.5 / 2.75) * p + 0.75);
				} else if (p < 2.5 / 2.75) {
					return 1 - (7.5625 * (p -= 2.25 / 2.75) * p + 0.9375);
				}
				return 1 - (7.5625 * (p -= 2.625 / 2.75) * p + 0.984375);
			}),
			_create("BounceInOut", function(p) {
				var invert = (p < 0.5);
				if (invert) {
					p = 1 - (p * 2);
				} else {
					p = (p * 2) - 1;
				}
				if (p < 1 / 2.75) {
					p = 7.5625 * p * p;
				} else if (p < 2 / 2.75) {
					p = 7.5625 * (p -= 1.5 / 2.75) * p + 0.75;
				} else if (p < 2.5 / 2.75) {
					p = 7.5625 * (p -= 2.25 / 2.75) * p + 0.9375;
				} else {
					p = 7.5625 * (p -= 2.625 / 2.75) * p + 0.984375;
				}
				return invert ? (1 - p) * 0.5 : p * 0.5 + 0.5;
			})
		);


		//CIRC
		_wrap("Circ",
			_create("CircOut", function(p) {
				return Math.sqrt(1 - (p = p - 1) * p);
			}),
			_create("CircIn", function(p) {
				return -(Math.sqrt(1 - (p * p)) - 1);
			}),
			_create("CircInOut", function(p) {
				return ((p*=2) < 1) ? -0.5 * (Math.sqrt(1 - p * p) - 1) : 0.5 * (Math.sqrt(1 - (p -= 2) * p) + 1);
			})
		);


		//Elastic
		_createElastic = function(n, f, def) {
			var C = _class("easing." + n, function(amplitude, period) {
					this._p1 = amplitude || 1;
					this._p2 = period || def;
					this._p3 = this._p2 / _2PI * (Math.asin(1 / this._p1) || 0);
				}, true),
				p = C.prototype = new Ease();
			p.constructor = C;
			p.getRatio = f;
			p.config = function(amplitude, period) {
				return new C(amplitude, period);
			};
			return C;
		};
		_wrap("Elastic",
			_createElastic("ElasticOut", function(p) {
				return this._p1 * Math.pow(2, -10 * p) * Math.sin( (p - this._p3) * _2PI / this._p2 ) + 1;
			}, 0.3),
			_createElastic("ElasticIn", function(p) {
				return -(this._p1 * Math.pow(2, 10 * (p -= 1)) * Math.sin( (p - this._p3) * _2PI / this._p2 ));
			}, 0.3),
			_createElastic("ElasticInOut", function(p) {
				return ((p *= 2) < 1) ? -0.5 * (this._p1 * Math.pow(2, 10 * (p -= 1)) * Math.sin( (p - this._p3) * _2PI / this._p2)) : this._p1 * Math.pow(2, -10 *(p -= 1)) * Math.sin( (p - this._p3) * _2PI / this._p2 ) *0.5 + 1;
			}, 0.45)
		);


		//Expo
		_wrap("Expo",
			_create("ExpoOut", function(p) {
				return 1 - Math.pow(2, -10 * p);
			}),
			_create("ExpoIn", function(p) {
				return Math.pow(2, 10 * (p - 1)) - 0.001;
			}),
			_create("ExpoInOut", function(p) {
				return ((p *= 2) < 1) ? 0.5 * Math.pow(2, 10 * (p - 1)) : 0.5 * (2 - Math.pow(2, -10 * (p - 1)));
			})
		);


		//Sine
		_wrap("Sine",
			_create("SineOut", function(p) {
				return Math.sin(p * _HALF_PI);
			}),
			_create("SineIn", function(p) {
				return -Math.cos(p * _HALF_PI) + 1;
			}),
			_create("SineInOut", function(p) {
				return -0.5 * (Math.cos(Math.PI * p) - 1);
			})
		);

		_class("easing.EaseLookup", {
				find:function(s) {
					return Ease.map[s];
				}
			}, true);

		//register the non-standard eases
		_easeReg(w.SlowMo, "SlowMo", "ease,");
		_easeReg(RoughEase, "RoughEase", "ease,");
		_easeReg(SteppedEase, "SteppedEase", "ease,");

		return Back;
		
	}, true);


}); 











/*
 * ----------------------------------------------------------------
 * Base classes like TweenLite, SimpleTimeline, Ease, Ticker, etc.
 * ----------------------------------------------------------------
 */
(function(window) {

		"use strict";
		var _globals = window.GreenSockGlobals || window;
		if (_globals.TweenLite) {
			return; //in case the core set of classes is already loaded, don't instantiate twice.
		}
		var _namespace = function(ns) {
				var a = ns.split("."),
					p = _globals, i;
				for (i = 0; i < a.length; i++) {
					p[a[i]] = p = p[a[i]] || {};
				}
				return p;
			},
			gs = _namespace("com.greensock"),
			_tinyNum = 0.0000000001,
			_slice = [].slice,
			_emptyFunc = function() {},
			_isArray = (function() { //works around issues in iframe environments where the Array global isn't shared, thus if the object originates in a different window/iframe, "(obj instanceof Array)" will evaluate false. We added some speed optimizations to avoid Object.prototype.toString.call() unless it's absolutely necessary because it's VERY slow (like 20x slower)
				var toString = Object.prototype.toString,
					array = toString.call([]);
				return function(obj) {
					return obj != null && (obj instanceof Array || (typeof(obj) === "object" && !!obj.push && toString.call(obj) === array));
				};
			}()),
			a, i, p, _ticker, _tickerActive,
			_defLookup = {},

			/**
			 * @constructor
			 * Defines a GreenSock class, optionally with an array of dependencies that must be instantiated first and passed into the definition.
			 * This allows users to load GreenSock JS files in any order even if they have interdependencies (like CSSPlugin extends TweenPlugin which is
			 * inside TweenLite.js, but if CSSPlugin is loaded first, it should wait to run its code until TweenLite.js loads and instantiates TweenPlugin
			 * and then pass TweenPlugin to CSSPlugin's definition). This is all done automatically and internally.
			 *
			 * Every definition will be added to a "com.greensock" global object (typically window, but if a window.GreenSockGlobals object is found,
			 * it will go there as of v1.7). For example, TweenLite will be found at window.com.greensock.TweenLite and since it's a global class that should be available anywhere,
			 * it is ALSO referenced at window.TweenLite. However some classes aren't considered global, like the base com.greensock.core.Animation class, so
			 * those will only be at the package like window.com.greensock.core.Animation. Again, if you define a GreenSockGlobals object on the window, everything
			 * gets tucked neatly inside there instead of on the window directly. This allows you to do advanced things like load multiple versions of GreenSock
			 * files and put them into distinct objects (imagine a banner ad uses a newer version but the main site uses an older one). In that case, you could
			 * sandbox the banner one like:
			 *
			 * <script>
			 *     var gs = window.GreenSockGlobals = {}; //the newer version we're about to load could now be referenced in a "gs" object, like gs.TweenLite.to(...). Use whatever alias you want as long as it's unique, "gs" or "banner" or whatever.
			 * </script>
			 * <script src="js/greensock/v1.7/TweenMax.js"></script>
			 * <script>
			 *     window.GreenSockGlobals = null; //reset it back to null so that the next load of TweenMax affects the window and we can reference things directly like TweenLite.to(...)
			 * </script>
			 * <script src="js/greensock/v1.6/TweenMax.js"></script>
			 * <script>
			 *     gs.TweenLite.to(...); //would use v1.7
			 *     TweenLite.to(...); //would use v1.6
			 * </script>
			 *
			 * @param {!string} ns The namespace of the class definition, leaving off "com.greensock." as that's assumed. For example, "TweenLite" or "plugins.CSSPlugin" or "easing.Back".
			 * @param {!Array.<string>} dependencies An array of dependencies (described as their namespaces minus "com.greensock." prefix). For example ["TweenLite","plugins.TweenPlugin","core.Animation"]
			 * @param {!function():Object} func The function that should be called and passed the resolved dependencies which will return the actual class for this definition.
			 * @param {boolean=} global If true, the class will be added to the global scope (typically window unless you define a window.GreenSockGlobals object)
			 */
			Definition = function(ns, dependencies, func, global) {
				this.sc = (_defLookup[ns]) ? _defLookup[ns].sc : []; //subclasses
				_defLookup[ns] = this;
				this.gsClass = null;
				this.func = func;
				var _classes = [];
				this.check = function(init) {
					var i = dependencies.length,
						missing = i,
						cur, a, n, cl;
					while (--i > -1) {
						if ((cur = _defLookup[dependencies[i]] || new Definition(dependencies[i], [])).gsClass) {
							_classes[i] = cur.gsClass;
							missing--;
						} else if (init) {
							cur.sc.push(this);
						}
					}
					if (missing === 0 && func) {
						a = ("com.greensock." + ns).split(".");
						n = a.pop();
						cl = _namespace(a.join("."))[n] = this.gsClass = func.apply(func, _classes);

						//exports to multiple environments
						if (global) {
							_globals[n] = cl; //provides a way to avoid global namespace pollution. By default, the main classes like TweenLite, Power1, Strong, etc. are added to window unless a GreenSockGlobals is defined. So if you want to have things added to a custom object instead, just do something like window.GreenSockGlobals = {} before loading any GreenSock files. You can even set up an alias like window.GreenSockGlobals = windows.gs = {} so that you can access everything like gs.TweenLite. Also remember that ALL classes are added to the window.com.greensock object (in their respective packages, like com.greensock.easing.Power1, com.greensock.TweenLite, etc.)
							if (typeof(define) === "function" && define.amd){ //AMD
								define((window.GreenSockAMDPath ? window.GreenSockAMDPath + "/" : "") + ns.split(".").join("/"), [], function() { return cl; });
							} else if (typeof(module) !== "undefined" && module.exports){ //node
								module.exports = cl;
							}
						}
						for (i = 0; i < this.sc.length; i++) {
							this.sc[i].check();
						}
					}
				};
				this.check(true);
			},

			//used to create Definition instances (which basically registers a class that has dependencies).
			_gsDefine = window._gsDefine = function(ns, dependencies, func, global) {
				return new Definition(ns, dependencies, func, global);
			},

			//a quick way to create a class that doesn't have any dependencies. Returns the class, but first registers it in the GreenSock namespace so that other classes can grab it (other classes might be dependent on the class).
			_class = gs._class = function(ns, func, global) {
				func = func || function() {};
				_gsDefine(ns, [], function(){ return func; }, global);
				return func;
			};

		_gsDefine.globals = _globals;



/*
 * ----------------------------------------------------------------
 * Ease
 * ----------------------------------------------------------------
 */
		var _baseParams = [0, 0, 1, 1],
			_blankArray = [],
			Ease = _class("easing.Ease", function(func, extraParams, type, power) {
				this._func = func;
				this._type = type || 0;
				this._power = power || 0;
				this._params = extraParams ? _baseParams.concat(extraParams) : _baseParams;
			}, true),
			_easeMap = Ease.map = {},
			_easeReg = Ease.register = function(ease, names, types, create) {
				var na = names.split(","),
					i = na.length,
					ta = (types || "easeIn,easeOut,easeInOut").split(","),
					e, name, j, type;
				while (--i > -1) {
					name = na[i];
					e = create ? _class("easing."+name, null, true) : gs.easing[name] || {};
					j = ta.length;
					while (--j > -1) {
						type = ta[j];
						_easeMap[name + "." + type] = _easeMap[type + name] = e[type] = ease.getRatio ? ease : ease[type] || new ease();
					}
				}
			};

		p = Ease.prototype;
		p._calcEnd = false;
		p.getRatio = function(p) {
			if (this._func) {
				this._params[0] = p;
				return this._func.apply(null, this._params);
			}
			var t = this._type,
				pw = this._power,
				r = (t === 1) ? 1 - p : (t === 2) ? p : (p < 0.5) ? p * 2 : (1 - p) * 2;
			if (pw === 1) {
				r *= r;
			} else if (pw === 2) {
				r *= r * r;
			} else if (pw === 3) {
				r *= r * r * r;
			} else if (pw === 4) {
				r *= r * r * r * r;
			}
			return (t === 1) ? 1 - r : (t === 2) ? r : (p < 0.5) ? r / 2 : 1 - (r / 2);
		};

		//create all the standard eases like Linear, Quad, Cubic, Quart, Quint, Strong, Power0, Power1, Power2, Power3, and Power4 (each with easeIn, easeOut, and easeInOut)
		a = ["Linear","Quad","Cubic","Quart","Quint,Strong"];
		i = a.length;
		while (--i > -1) {
			p = a[i]+",Power"+i;
			_easeReg(new Ease(null,null,1,i), p, "easeOut", true);
			_easeReg(new Ease(null,null,2,i), p, "easeIn" + ((i === 0) ? ",easeNone" : ""));
			_easeReg(new Ease(null,null,3,i), p, "easeInOut");
		}
		_easeMap.linear = gs.easing.Linear.easeIn;
		_easeMap.swing = gs.easing.Quad.easeInOut; //for jQuery folks


/*
 * ----------------------------------------------------------------
 * EventDispatcher
 * ----------------------------------------------------------------
 */
		var EventDispatcher = _class("events.EventDispatcher", function(target) {
			this._listeners = {};
			this._eventTarget = target || this;
		});
		p = EventDispatcher.prototype;

		p.addEventListener = function(type, callback, scope, useParam, priority) {
			priority = priority || 0;
			var list = this._listeners[type],
				index = 0,
				listener, i;
			if (list == null) {
				this._listeners[type] = list = [];
			}
			i = list.length;
			while (--i > -1) {
				listener = list[i];
				if (listener.c === callback && listener.s === scope) {
					list.splice(i, 1);
				} else if (index === 0 && listener.pr < priority) {
					index = i + 1;
				}
			}
			list.splice(index, 0, {c:callback, s:scope, up:useParam, pr:priority});
			if (this === _ticker && !_tickerActive) {
				_ticker.wake();
			}
		};

		p.removeEventListener = function(type, callback) {
			var list = this._listeners[type], i;
			if (list) {
				i = list.length;
				while (--i > -1) {
					if (list[i].c === callback) {
						list.splice(i, 1);
						return;
					}
				}
			}
		};

		p.dispatchEvent = function(type) {
			var list = this._listeners[type],
				i, t, listener;
			if (list) {
				i = list.length;
				t = this._eventTarget;
				while (--i > -1) {
					listener = list[i];
					if (listener.up) {
						listener.c.call(listener.s || t, {type:type, target:t});
					} else {
						listener.c.call(listener.s || t);
					}
				}
			}
		};


/*
 * ----------------------------------------------------------------
 * Ticker
 * ----------------------------------------------------------------
 */
 		var _reqAnimFrame = window.requestAnimationFrame,
			_cancelAnimFrame = window.cancelAnimationFrame,
			_getTime = Date.now || function() {return new Date().getTime();},
			_lastUpdate = _getTime();

		//now try to determine the requestAnimationFrame and cancelAnimationFrame functions and if none are found, we'll use a setTimeout()/clearTimeout() polyfill.
		a = ["ms","moz","webkit","o"];
		i = a.length;
		while (--i > -1 && !_reqAnimFrame) {
			_reqAnimFrame = window[a[i] + "RequestAnimationFrame"];
			_cancelAnimFrame = window[a[i] + "CancelAnimationFrame"] || window[a[i] + "CancelRequestAnimationFrame"];
		}

		_class("Ticker", function(fps, useRAF) {
			var _self = this,
				_startTime = _getTime(),
				_useRAF = (useRAF !== false && _reqAnimFrame),
				_lagThreshold = 500,
				_adjustedLag = 33,
				_fps, _req, _id, _gap, _nextTime,
				_tick = function(manual) {
					var elapsed = _getTime() - _lastUpdate,
						overlap, dispatch;
					if (elapsed > _lagThreshold) {
						_startTime += elapsed - _adjustedLag;
					}
					_lastUpdate += elapsed;
					_self.time = (_lastUpdate - _startTime) / 1000;
					overlap = _self.time - _nextTime;
					if (!_fps || overlap > 0 || manual === true) {
						_self.frame++;
						_nextTime += overlap + (overlap >= _gap ? 0.004 : _gap - overlap);
						dispatch = true;
					}
					if (manual !== true) { //make sure the request is made before we dispatch the "tick" event so that timing is maintained. Otherwise, if processing the "tick" requires a bunch of time (like 15ms) and we're using a setTimeout() that's based on 16.7ms, it'd technically take 31.7ms between frames otherwise.
						_id = _req(_tick);
					}
					if (dispatch) {
						_self.dispatchEvent("tick");
					}
				};

			EventDispatcher.call(_self);
			_self.time = _self.frame = 0;
			_self.tick = function() {
				_tick(true);
			};

			_self.lagSmoothing = function(threshold, adjustedLag) {
				_lagThreshold = threshold || (1 / _tinyNum); //zero should be interpreted as basically unlimited
				_adjustedLag = Math.min(adjustedLag, _lagThreshold, 0);
			};

			_self.sleep = function() {
				if (_id == null) {
					return;
				}
				if (!_useRAF || !_cancelAnimFrame) {
					clearTimeout(_id);
				} else {
					_cancelAnimFrame(_id);
				}
				_req = _emptyFunc;
				_id = null;
				if (_self === _ticker) {
					_tickerActive = false;
				}
			};

			_self.wake = function() {
				if (_id !== null) {
					_self.sleep();
				} else if (_self.frame > 10) { //don't trigger lagSmoothing if we're just waking up, and make sure that at least 10 frames have elapsed because of the iOS bug that we work around below with the 1.5-second setTimout().
					_lastUpdate = _getTime() - _lagThreshold + 5;
				}
				_req = (_fps === 0) ? _emptyFunc : (!_useRAF || !_reqAnimFrame) ? function(f) { return setTimeout(f, ((_nextTime - _self.time) * 1000 + 1) | 0); } : _reqAnimFrame;
				if (_self === _ticker) {
					_tickerActive = true;
				}
				_tick(2);
			};

			_self.fps = function(value) {
				if (!arguments.length) {
					return _fps;
				}
				_fps = value;
				_gap = 1 / (_fps || 60);
				_nextTime = this.time + _gap;
				_self.wake();
			};

			_self.useRAF = function(value) {
				if (!arguments.length) {
					return _useRAF;
				}
				_self.sleep();
				_useRAF = value;
				_self.fps(_fps);
			};
			_self.fps(fps);

			//a bug in iOS 6 Safari occasionally prevents the requestAnimationFrame from working initially, so we use a 1.5-second timeout that automatically falls back to setTimeout() if it senses this condition.
			setTimeout(function() {
				if (_useRAF && (!_id || _self.frame < 5)) {
					_self.useRAF(false);
				}
			}, 1500);
		});

		p = gs.Ticker.prototype = new gs.events.EventDispatcher();
		p.constructor = gs.Ticker;


/*
 * ----------------------------------------------------------------
 * Animation
 * ----------------------------------------------------------------
 */
		var Animation = _class("core.Animation", function(duration, vars) {
				this.vars = vars = vars || {};
				this._duration = this._totalDuration = duration || 0;
				this._delay = Number(vars.delay) || 0;
				this._timeScale = 1;
				this._active = (vars.immediateRender === true);
				this.data = vars.data;
				this._reversed = (vars.reversed === true);

				if (!_rootTimeline) {
					return;
				}
				if (!_tickerActive) { //some browsers (like iOS 6 Safari) shut down JavaScript execution when the tab is disabled and they [occasionally] neglect to start up requestAnimationFrame again when returning - this code ensures that the engine starts up again properly.
					_ticker.wake();
				}

				var tl = this.vars.useFrames ? _rootFramesTimeline : _rootTimeline;
				tl.add(this, tl._time);

				if (this.vars.paused) {
					this.paused(true);
				}
			});

		_ticker = Animation.ticker = new gs.Ticker();
		p = Animation.prototype;
		p._dirty = p._gc = p._initted = p._paused = false;
		p._totalTime = p._time = 0;
		p._rawPrevTime = -1;
		p._next = p._last = p._onUpdate = p._timeline = p.timeline = null;
		p._paused = false;


		//some browsers (like iOS) occasionally drop the requestAnimationFrame event when the user switches to a different tab and then comes back again, so we use a 2-second setTimeout() to sense if/when that condition occurs and then wake() the ticker.
		var _checkTimeout = function() {
				if (_tickerActive && _getTime() - _lastUpdate > 2000) {
					_ticker.wake();
				}
				setTimeout(_checkTimeout, 2000);
			};
		_checkTimeout();


		p.play = function(from, suppressEvents) {
			if (from != null) {
				this.seek(from, suppressEvents);
			}
			return this.reversed(false).paused(false);
		};

		p.pause = function(atTime, suppressEvents) {
			if (atTime != null) {
				this.seek(atTime, suppressEvents);
			}
			return this.paused(true);
		};

		p.resume = function(from, suppressEvents) {
			if (from != null) {
				this.seek(from, suppressEvents);
			}
			return this.paused(false);
		};

		p.seek = function(time, suppressEvents) {
			return this.totalTime(Number(time), suppressEvents !== false);
		};

		p.restart = function(includeDelay, suppressEvents) {
			return this.reversed(false).paused(false).totalTime(includeDelay ? -this._delay : 0, (suppressEvents !== false), true);
		};

		p.reverse = function(from, suppressEvents) {
			if (from != null) {
				this.seek((from || this.totalDuration()), suppressEvents);
			}
			return this.reversed(true).paused(false);
		};

		p.render = function(time, suppressEvents, force) {
			//stub - we override this method in subclasses.
		};

		p.invalidate = function() {
			return this;
		};

		p.isActive = function() {
			var tl = this._timeline, //the 2 root timelines won't have a _timeline; they're always active.
				startTime = this._startTime,
				rawTime;
			return (!tl || (!this._gc && !this._paused && tl.isActive() && (rawTime = tl.rawTime()) >= startTime && rawTime < startTime + this.totalDuration() / this._timeScale));
		};

		p._enabled = function (enabled, ignoreTimeline) {
			if (!_tickerActive) {
				_ticker.wake();
			}
			this._gc = !enabled;
			this._active = this.isActive();
			if (ignoreTimeline !== true) {
				if (enabled && !this.timeline) {
					this._timeline.add(this, this._startTime - this._delay);
				} else if (!enabled && this.timeline) {
					this._timeline._remove(this, true);
				}
			}
			return false;
		};


		p._kill = function(vars, target) {
			return this._enabled(false, false);
		};

		p.kill = function(vars, target) {
			this._kill(vars, target);
			return this;
		};

		p._uncache = function(includeSelf) {
			var tween = includeSelf ? this : this.timeline;
			while (tween) {
				tween._dirty = true;
				tween = tween.timeline;
			}
			return this;
		};

		p._swapSelfInParams = function(params) {
			var i = params.length,
				copy = params.concat();
			while (--i > -1) {
				if (params[i] === "{self}") {
					copy[i] = this;
				}
			}
			return copy;
		};

//----Animation getters/setters --------------------------------------------------------

		p.eventCallback = function(type, callback, params, scope) {
			if ((type || "").substr(0,2) === "on") {
				var v = this.vars;
				if (arguments.length === 1) {
					return v[type];
				}
				if (callback == null) {
					delete v[type];
				} else {
					v[type] = callback;
					v[type + "Params"] = (_isArray(params) && params.join("").indexOf("{self}") !== -1) ? this._swapSelfInParams(params) : params;
					v[type + "Scope"] = scope;
				}
				if (type === "onUpdate") {
					this._onUpdate = callback;
				}
			}
			return this;
		};

		p.delay = function(value) {
			if (!arguments.length) {
				return this._delay;
			}
			if (this._timeline.smoothChildTiming) {
				this.startTime( this._startTime + value - this._delay );
			}
			this._delay = value;
			return this;
		};

		p.duration = function(value) {
			if (!arguments.length) {
				this._dirty = false;
				return this._duration;
			}
			this._duration = this._totalDuration = value;
			this._uncache(true); //true in case it's a TweenMax or TimelineMax that has a repeat - we'll need to refresh the totalDuration.
			if (this._timeline.smoothChildTiming) if (this._time > 0) if (this._time < this._duration) if (value !== 0) {
				this.totalTime(this._totalTime * (value / this._duration), true);
			}
			return this;
		};

		p.totalDuration = function(value) {
			this._dirty = false;
			return (!arguments.length) ? this._totalDuration : this.duration(value);
		};

		p.time = function(value, suppressEvents) {
			if (!arguments.length) {
				return this._time;
			}
			if (this._dirty) {
				this.totalDuration();
			}
			return this.totalTime((value > this._duration) ? this._duration : value, suppressEvents);
		};

		p.totalTime = function(time, suppressEvents, uncapped) {
			if (!_tickerActive) {
				_ticker.wake();
			}
			if (!arguments.length) {
				return this._totalTime;
			}
			if (this._timeline) {
				if (time < 0 && !uncapped) {
					time += this.totalDuration();
				}
				if (this._timeline.smoothChildTiming) {
					if (this._dirty) {
						this.totalDuration();
					}
					var totalDuration = this._totalDuration,
						tl = this._timeline;
					if (time > totalDuration && !uncapped) {
						time = totalDuration;
					}
					this._startTime = (this._paused ? this._pauseTime : tl._time) - ((!this._reversed ? time : totalDuration - time) / this._timeScale);
					if (!tl._dirty) { //for performance improvement. If the parent's cache is already dirty, it already took care of marking the ancestors as dirty too, so skip the function call here.
						this._uncache(false);
					}
					//in case any of the ancestor timelines had completed but should now be enabled, we should reset their totalTime() which will also ensure that they're lined up properly and enabled. Skip for animations that are on the root (wasteful). Example: a TimelineLite.exportRoot() is performed when there's a paused tween on the root, the export will not complete until that tween is unpaused, but imagine a child gets restarted later, after all [unpaused] tweens have completed. The startTime of that child would get pushed out, but one of the ancestors may have completed.
					if (tl._timeline) {
						while (tl._timeline) {
							if (tl._timeline._time !== (tl._startTime + tl._totalTime) / tl._timeScale) {
								tl.totalTime(tl._totalTime, true);
							}
							tl = tl._timeline;
						}
					}
				}
				if (this._gc) {
					this._enabled(true, false);
				}
				if (this._totalTime !== time || this._duration === 0) {
					this.render(time, suppressEvents, false);
					if (_lazyTweens.length) { //in case rendering caused any tweens to lazy-init, we should render them because typically when someone calls seek() or time() or progress(), they expect an immediate render.
						_lazyRender();
					}
				}
			}
			return this;
		};

		p.progress = p.totalProgress = function(value, suppressEvents) {
			return (!arguments.length) ? this._time / this.duration() : this.totalTime(this.duration() * value, suppressEvents);
		};

		p.startTime = function(value) {
			if (!arguments.length) {
				return this._startTime;
			}
			if (value !== this._startTime) {
				this._startTime = value;
				if (this.timeline) if (this.timeline._sortChildren) {
					this.timeline.add(this, value - this._delay); //ensures that any necessary re-sequencing of Animations in the timeline occurs to make sure the rendering order is correct.
				}
			}
			return this;
		};

		p.timeScale = function(value) {
			if (!arguments.length) {
				return this._timeScale;
			}
			value = value || _tinyNum; //can't allow zero because it'll throw the math off
			if (this._timeline && this._timeline.smoothChildTiming) {
				var pauseTime = this._pauseTime,
					t = (pauseTime || pauseTime === 0) ? pauseTime : this._timeline.totalTime();
				this._startTime = t - ((t - this._startTime) * this._timeScale / value);
			}
			this._timeScale = value;
			return this._uncache(false);
		};

		p.reversed = function(value) {
			if (!arguments.length) {
				return this._reversed;
			}
			if (value != this._reversed) {
				this._reversed = value;
				this.totalTime(((this._timeline && !this._timeline.smoothChildTiming) ? this.totalDuration() - this._totalTime : this._totalTime), true);
			}
			return this;
		};

		p.paused = function(value) {
			if (!arguments.length) {
				return this._paused;
			}
			if (value != this._paused) if (this._timeline) {
				if (!_tickerActive && !value) {
					_ticker.wake();
				}
				var tl = this._timeline,
					raw = tl.rawTime(),
					elapsed = raw - this._pauseTime;
				if (!value && tl.smoothChildTiming) {
					this._startTime += elapsed;
					this._uncache(false);
				}
				this._pauseTime = value ? raw : null;
				this._paused = value;
				this._active = this.isActive();
				if (!value && elapsed !== 0 && this._initted && this.duration()) {
					this.render((tl.smoothChildTiming ? this._totalTime : (raw - this._startTime) / this._timeScale), true, true); //in case the target's properties changed via some other tween or manual update by the user, we should force a render.
				}
			}
			if (this._gc && !value) {
				this._enabled(true, false);
			}
			return this;
		};


/*
 * ----------------------------------------------------------------
 * SimpleTimeline
 * ----------------------------------------------------------------
 */
		var SimpleTimeline = _class("core.SimpleTimeline", function(vars) {
			Animation.call(this, 0, vars);
			this.autoRemoveChildren = this.smoothChildTiming = true;
		});

		p = SimpleTimeline.prototype = new Animation();
		p.constructor = SimpleTimeline;
		p.kill()._gc = false;
		p._first = p._last = null;
		p._sortChildren = false;

		p.add = p.insert = function(child, position, align, stagger) {
			var prevTween, st;
			child._startTime = Number(position || 0) + child._delay;
			if (child._paused) if (this !== child._timeline) { //we only adjust the _pauseTime if it wasn't in this timeline already. Remember, sometimes a tween will be inserted again into the same timeline when its startTime is changed so that the tweens in the TimelineLite/Max are re-ordered properly in the linked list (so everything renders in the proper order).
				child._pauseTime = child._startTime + ((this.rawTime() - child._startTime) / child._timeScale);
			}
			if (child.timeline) {
				child.timeline._remove(child, true); //removes from existing timeline so that it can be properly added to this one.
			}
			child.timeline = child._timeline = this;
			if (child._gc) {
				child._enabled(true, true);
			}
			prevTween = this._last;
			if (this._sortChildren) {
				st = child._startTime;
				while (prevTween && prevTween._startTime > st) {
					prevTween = prevTween._prev;
				}
			}
			if (prevTween) {
				child._next = prevTween._next;
				prevTween._next = child;
			} else {
				child._next = this._first;
				this._first = child;
			}
			if (child._next) {
				child._next._prev = child;
			} else {
				this._last = child;
			}
			child._prev = prevTween;
			if (this._timeline) {
				this._uncache(true);
			}
			return this;
		};

		p._remove = function(tween, skipDisable) {
			if (tween.timeline === this) {
				if (!skipDisable) {
					tween._enabled(false, true);
				}
				tween.timeline = null;

				if (tween._prev) {
					tween._prev._next = tween._next;
				} else if (this._first === tween) {
					this._first = tween._next;
				}
				if (tween._next) {
					tween._next._prev = tween._prev;
				} else if (this._last === tween) {
					this._last = tween._prev;
				}

				if (this._timeline) {
					this._uncache(true);
				}
			}
			return this;
		};

		p.render = function(time, suppressEvents, force) {
			var tween = this._first,
				next;
			this._totalTime = this._time = this._rawPrevTime = time;
			while (tween) {
				next = tween._next; //record it here because the value could change after rendering...
				if (tween._active || (time >= tween._startTime && !tween._paused)) {
					if (!tween._reversed) {
						tween.render((time - tween._startTime) * tween._timeScale, suppressEvents, force);
					} else {
						tween.render(((!tween._dirty) ? tween._totalDuration : tween.totalDuration()) - ((time - tween._startTime) * tween._timeScale), suppressEvents, force);
					}
				}
				tween = next;
			}
		};

		p.rawTime = function() {
			if (!_tickerActive) {
				_ticker.wake();
			}
			return this._totalTime;
		};

/*
 * ----------------------------------------------------------------
 * TweenLite
 * ----------------------------------------------------------------
 */
		var TweenLite = _class("TweenLite", function(target, duration, vars) {
				Animation.call(this, duration, vars);
				this.render = TweenLite.prototype.render; //speed optimization (avoid prototype lookup on this "hot" method)

				if (target == null) {
					throw "Cannot tween a null target.";
				}

				this.target = target = (typeof(target) !== "string") ? target : TweenLite.selector(target) || target;

				var isSelector = (target.jquery || (target.length && target !== window && target[0] && (target[0] === window || (target[0].nodeType && target[0].style && !target.nodeType)))),
					overwrite = this.vars.overwrite,
					i, targ, targets;

				this._overwrite = overwrite = (overwrite == null) ? _overwriteLookup[TweenLite.defaultOverwrite] : (typeof(overwrite) === "number") ? overwrite >> 0 : _overwriteLookup[overwrite];

				if ((isSelector || target instanceof Array || (target.push && _isArray(target))) && typeof(target[0]) !== "number") {
					this._targets = targets = _slice.call(target, 0);
					this._propLookup = [];
					this._siblings = [];
					for (i = 0; i < targets.length; i++) {
						targ = targets[i];
						if (!targ) {
							targets.splice(i--, 1);
							continue;
						} else if (typeof(targ) === "string") {
							targ = targets[i--] = TweenLite.selector(targ); //in case it's an array of strings
							if (typeof(targ) === "string") {
								targets.splice(i+1, 1); //to avoid an endless loop (can't imagine why the selector would return a string, but just in case)
							}
							continue;
						} else if (targ.length && targ !== window && targ[0] && (targ[0] === window || (targ[0].nodeType && targ[0].style && !targ.nodeType))) { //in case the user is passing in an array of selector objects (like jQuery objects), we need to check one more level and pull things out if necessary. Also note that <select> elements pass all the criteria regarding length and the first child having style, so we must also check to ensure the target isn't an HTML node itself.
							targets.splice(i--, 1);
							this._targets = targets = targets.concat(_slice.call(targ, 0));
							continue;
						}
						this._siblings[i] = _register(targ, this, false);
						if (overwrite === 1) if (this._siblings[i].length > 1) {
							_applyOverwrite(targ, this, null, 1, this._siblings[i]);
						}
					}

				} else {
					this._propLookup = {};
					this._siblings = _register(target, this, false);
					if (overwrite === 1) if (this._siblings.length > 1) {
						_applyOverwrite(target, this, null, 1, this._siblings);
					}
				}
				if (this.vars.immediateRender || (duration === 0 && this._delay === 0 && this.vars.immediateRender !== false)) {
					this._time = -_tinyNum; //forces a render without having to set the render() "force" parameter to true because we want to allow lazying by default (using the "force" parameter always forces an immediate full render)
					this.render(-this._delay);
				}
			}, true),
			_isSelector = function(v) {
				return (v.length && v !== window && v[0] && (v[0] === window || (v[0].nodeType && v[0].style && !v.nodeType))); //we cannot check "nodeType" if the target is window from within an iframe, otherwise it will trigger a security error in some browsers like Firefox.
			},
			_autoCSS = function(vars, target) {
				var css = {},
					p;
				for (p in vars) {
					if (!_reservedProps[p] && (!(p in target) || p === "transform" || p === "x" || p === "y" || p === "width" || p === "height" || p === "className" || p === "border") && (!_plugins[p] || (_plugins[p] && _plugins[p]._autoCSS))) { //note: <img> elements contain read-only "x" and "y" properties. We should also prioritize editing css width/height rather than the element's properties.
						css[p] = vars[p];
						delete vars[p];
					}
				}
				vars.css = css;
			};

		p = TweenLite.prototype = new Animation();
		p.constructor = TweenLite;
		p.kill()._gc = false;

//----TweenLite defaults, overwrite management, and root updates ----------------------------------------------------

		p.ratio = 0;
		p._firstPT = p._targets = p._overwrittenProps = p._startAt = null;
		p._notifyPluginsOfEnabled = p._lazy = false;

		TweenLite.version = "1.12.1";
		TweenLite.defaultEase = p._ease = new Ease(null, null, 1, 1);
		TweenLite.defaultOverwrite = "auto";
		TweenLite.ticker = _ticker;
		TweenLite.autoSleep = true;
		TweenLite.lagSmoothing = function(threshold, adjustedLag) {
			_ticker.lagSmoothing(threshold, adjustedLag);
		};
		TweenLite.selector = window.$ || window.jQuery || function(e) { if (window.$) { TweenLite.selector = window.$; return window.$(e); } return window.document ? window.document.getElementById((e.charAt(0) === "#") ? e.substr(1) : e) : e; };

		var _lazyTweens = [],
			_lazyLookup = {},
			_internals = TweenLite._internals = {isArray:_isArray, isSelector:_isSelector, lazyTweens:_lazyTweens}, //gives us a way to expose certain private values to other GreenSock classes without contaminating tha main TweenLite object.
			_plugins = TweenLite._plugins = {},
			_tweenLookup = _internals.tweenLookup = {},
			_tweenLookupNum = 0,
			_reservedProps = _internals.reservedProps = {ease:1, delay:1, overwrite:1, onComplete:1, onCompleteParams:1, onCompleteScope:1, useFrames:1, runBackwards:1, startAt:1, onUpdate:1, onUpdateParams:1, onUpdateScope:1, onStart:1, onStartParams:1, onStartScope:1, onReverseComplete:1, onReverseCompleteParams:1, onReverseCompleteScope:1, onRepeat:1, onRepeatParams:1, onRepeatScope:1, easeParams:1, yoyo:1, immediateRender:1, repeat:1, repeatDelay:1, data:1, paused:1, reversed:1, autoCSS:1, lazy:1},
			_overwriteLookup = {none:0, all:1, auto:2, concurrent:3, allOnStart:4, preexisting:5, "true":1, "false":0},
			_rootFramesTimeline = Animation._rootFramesTimeline = new SimpleTimeline(),
			_rootTimeline = Animation._rootTimeline = new SimpleTimeline(),
			_lazyRender = function() {
				var i = _lazyTweens.length;
				_lazyLookup = {};
				while (--i > -1) {
					a = _lazyTweens[i];
					if (a && a._lazy !== false) {
						a.render(a._lazy, false, true);
						a._lazy = false;
					}
				}
				_lazyTweens.length = 0;
			};

		_rootTimeline._startTime = _ticker.time;
		_rootFramesTimeline._startTime = _ticker.frame;
		_rootTimeline._active = _rootFramesTimeline._active = true;
		setTimeout(_lazyRender, 1); //on some mobile devices, there isn't a "tick" before code runs which means any lazy renders wouldn't run before the next official "tick".

		Animation._updateRoot = TweenLite.render = function() {
				var i, a, p;
				if (_lazyTweens.length) { //if code is run outside of the requestAnimationFrame loop, there may be tweens queued AFTER the engine refreshed, so we need to ensure any pending renders occur before we refresh again.
					_lazyRender();
				}
				_rootTimeline.render((_ticker.time - _rootTimeline._startTime) * _rootTimeline._timeScale, false, false);
				_rootFramesTimeline.render((_ticker.frame - _rootFramesTimeline._startTime) * _rootFramesTimeline._timeScale, false, false);
				if (_lazyTweens.length) {
					_lazyRender();
				}
				if (!(_ticker.frame % 120)) { //dump garbage every 120 frames...
					for (p in _tweenLookup) {
						a = _tweenLookup[p].tweens;
						i = a.length;
						while (--i > -1) {
							if (a[i]._gc) {
								a.splice(i, 1);
							}
						}
						if (a.length === 0) {
							delete _tweenLookup[p];
						}
					}
					//if there are no more tweens in the root timelines, or if they're all paused, make the _timer sleep to reduce load on the CPU slightly
					p = _rootTimeline._first;
					if (!p || p._paused) if (TweenLite.autoSleep && !_rootFramesTimeline._first && _ticker._listeners.tick.length === 1) {
						while (p && p._paused) {
							p = p._next;
						}
						if (!p) {
							_ticker.sleep();
						}
					}
				}
			};

		_ticker.addEventListener("tick", Animation._updateRoot);

		var _register = function(target, tween, scrub) {
				var id = target._gsTweenID, a, i;
				if (!_tweenLookup[id || (target._gsTweenID = id = "t" + (_tweenLookupNum++))]) {
					_tweenLookup[id] = {target:target, tweens:[]};
				}
				if (tween) {
					a = _tweenLookup[id].tweens;
					a[(i = a.length)] = tween;
					if (scrub) {
						while (--i > -1) {
							if (a[i] === tween) {
								a.splice(i, 1);
							}
						}
					}
				}
				return _tweenLookup[id].tweens;
			},

			_applyOverwrite = function(target, tween, props, mode, siblings) {
				var i, changed, curTween, l;
				if (mode === 1 || mode >= 4) {
					l = siblings.length;
					for (i = 0; i < l; i++) {
						if ((curTween = siblings[i]) !== tween) {
							if (!curTween._gc) if (curTween._enabled(false, false)) {
								changed = true;
							}
						} else if (mode === 5) {
							break;
						}
					}
					return changed;
				}
				//NOTE: Add 0.0000000001 to overcome floating point errors that can cause the startTime to be VERY slightly off (when a tween's time() is set for example)
				var startTime = tween._startTime + _tinyNum,
					overlaps = [],
					oCount = 0,
					zeroDur = (tween._duration === 0),
					globalStart;
				i = siblings.length;
				while (--i > -1) {
					if ((curTween = siblings[i]) === tween || curTween._gc || curTween._paused) {
						//ignore
					} else if (curTween._timeline !== tween._timeline) {
						globalStart = globalStart || _checkOverlap(tween, 0, zeroDur);
						if (_checkOverlap(curTween, globalStart, zeroDur) === 0) {
							overlaps[oCount++] = curTween;
						}
					} else if (curTween._startTime <= startTime) if (curTween._startTime + curTween.totalDuration() / curTween._timeScale > startTime) if (!((zeroDur || !curTween._initted) && startTime - curTween._startTime <= 0.0000000002)) {
						overlaps[oCount++] = curTween;
					}
				}

				i = oCount;
				while (--i > -1) {
					curTween = overlaps[i];
					if (mode === 2) if (curTween._kill(props, target)) {
						changed = true;
					}
					if (mode !== 2 || (!curTween._firstPT && curTween._initted)) {
						if (curTween._enabled(false, false)) { //if all property tweens have been overwritten, kill the tween.
							changed = true;
						}
					}
				}
				return changed;
			},

			_checkOverlap = function(tween, reference, zeroDur) {
				var tl = tween._timeline,
					ts = tl._timeScale,
					t = tween._startTime;
				while (tl._timeline) {
					t += tl._startTime;
					ts *= tl._timeScale;
					if (tl._paused) {
						return -100;
					}
					tl = tl._timeline;
				}
				t /= ts;
				return (t > reference) ? t - reference : ((zeroDur && t === reference) || (!tween._initted && t - reference < 2 * _tinyNum)) ? _tinyNum : ((t += tween.totalDuration() / tween._timeScale / ts) > reference + _tinyNum) ? 0 : t - reference - _tinyNum;
			};


//---- TweenLite instance methods -----------------------------------------------------------------------------

		p._init = function() {
			var v = this.vars,
				op = this._overwrittenProps,
				dur = this._duration,
				immediate = !!v.immediateRender,
				ease = v.ease,
				i, initPlugins, pt, p, startVars;
			if (v.startAt) {
				if (this._startAt) {
					this._startAt.render(-1, true); //if we've run a startAt previously (when the tween instantiated), we should revert it so that the values re-instantiate correctly particularly for relative tweens. Without this, a TweenLite.fromTo(obj, 1, {x:"+=100"}, {x:"-=100"}), for example, would actually jump to +=200 because the startAt would run twice, doubling the relative change.
					this._startAt.kill();
				}
				startVars = {};
				for (p in v.startAt) { //copy the properties/values into a new object to avoid collisions, like var to = {x:0}, from = {x:500}; timeline.fromTo(e, 1, from, to).fromTo(e, 1, to, from);
					startVars[p] = v.startAt[p];
				}
				startVars.overwrite = false;
				startVars.immediateRender = true;
				startVars.lazy = (immediate && v.lazy !== false);
				startVars.startAt = startVars.delay = null; //no nesting of startAt objects allowed (otherwise it could cause an infinite loop).
				this._startAt = TweenLite.to(this.target, 0, startVars);
				if (immediate) {
					if (this._time > 0) {
						this._startAt = null; //tweens that render immediately (like most from() and fromTo() tweens) shouldn't revert when their parent timeline's playhead goes backward past the startTime because the initial render could have happened anytime and it shouldn't be directly correlated to this tween's startTime. Imagine setting up a complex animation where the beginning states of various objects are rendered immediately but the tween doesn't happen for quite some time - if we revert to the starting values as soon as the playhead goes backward past the tween's startTime, it will throw things off visually. Reversion should only happen in TimelineLite/Max instances where immediateRender was false (which is the default in the convenience methods like from()).
					} else if (dur !== 0) {
						return; //we skip initialization here so that overwriting doesn't occur until the tween actually begins. Otherwise, if you create several immediateRender:true tweens of the same target/properties to drop into a TimelineLite or TimelineMax, the last one created would overwrite the first ones because they didn't get placed into the timeline yet before the first render occurs and kicks in overwriting.
					}
				}
			} else if (v.runBackwards && dur !== 0) {
				//from() tweens must be handled uniquely: their beginning values must be rendered but we don't want overwriting to occur yet (when time is still 0). Wait until the tween actually begins before doing all the routines like overwriting. At that time, we should render at the END of the tween to ensure that things initialize correctly (remember, from() tweens go backwards)
				if (this._startAt) {
					this._startAt.render(-1, true);
					this._startAt.kill();
					this._startAt = null;
				} else {
					pt = {};
					for (p in v) { //copy props into a new object and skip any reserved props, otherwise onComplete or onUpdate or onStart could fire. We should, however, permit autoCSS to go through.
						if (!_reservedProps[p] || p === "autoCSS") {
							pt[p] = v[p];
						}
					}
					pt.overwrite = 0;
					pt.data = "isFromStart"; //we tag the tween with as "isFromStart" so that if [inside a plugin] we need to only do something at the very END of a tween, we have a way of identifying this tween as merely the one that's setting the beginning values for a "from()" tween. For example, clearProps in CSSPlugin should only get applied at the very END of a tween and without this tag, from(...{height:100, clearProps:"height", delay:1}) would wipe the height at the beginning of the tween and after 1 second, it'd kick back in.
					pt.lazy = (immediate && v.lazy !== false);
					pt.immediateRender = immediate; //zero-duration tweens render immediately by default, but if we're not specifically instructed to render this tween immediately, we should skip this and merely _init() to record the starting values (rendering them immediately would push them to completion which is wasteful in that case - we'd have to render(-1) immediately after)
					this._startAt = TweenLite.to(this.target, 0, pt);
					if (!immediate) {
						this._startAt._init(); //ensures that the initial values are recorded
						this._startAt._enabled(false); //no need to have the tween render on the next cycle. Disable it because we'll always manually control the renders of the _startAt tween.
					} else if (this._time === 0) {
						return;
					}
				}
			}
			if (!ease) {
				this._ease = TweenLite.defaultEase;
			} else if (ease instanceof Ease) {
				this._ease = (v.easeParams instanceof Array) ? ease.config.apply(ease, v.easeParams) : ease;
			} else {
				this._ease = (typeof(ease) === "function") ? new Ease(ease, v.easeParams) : _easeMap[ease] || TweenLite.defaultEase;
			}
			this._easeType = this._ease._type;
			this._easePower = this._ease._power;
			this._firstPT = null;

			if (this._targets) {
				i = this._targets.length;
				while (--i > -1) {
					if ( this._initProps( this._targets[i], (this._propLookup[i] = {}), this._siblings[i], (op ? op[i] : null)) ) {
						initPlugins = true;
					}
				}
			} else {
				initPlugins = this._initProps(this.target, this._propLookup, this._siblings, op);
			}

			if (initPlugins) {
				TweenLite._onPluginEvent("_onInitAllProps", this); //reorders the array in order of priority. Uses a static TweenPlugin method in order to minimize file size in TweenLite
			}
			if (op) if (!this._firstPT) if (typeof(this.target) !== "function") { //if all tweening properties have been overwritten, kill the tween. If the target is a function, it's probably a delayedCall so let it live.
				this._enabled(false, false);
			}
			if (v.runBackwards) {
				pt = this._firstPT;
				while (pt) {
					pt.s += pt.c;
					pt.c = -pt.c;
					pt = pt._next;
				}
			}
			this._onUpdate = v.onUpdate;
			this._initted = true;
		};

		p._initProps = function(target, propLookup, siblings, overwrittenProps) {
			var p, i, initPlugins, plugin, pt, v;
			if (target == null) {
				return false;
			}

			if (_lazyLookup[target._gsTweenID]) {
				_lazyRender(); //if other tweens of the same target have recently initted but haven't rendered yet, we've got to force the render so that the starting values are correct (imagine populating a timeline with a bunch of sequential tweens and then jumping to the end)
			}

			if (!this.vars.css) if (target.style) if (target !== window && target.nodeType) if (_plugins.css) if (this.vars.autoCSS !== false) { //it's so common to use TweenLite/Max to animate the css of DOM elements, we assume that if the target is a DOM element, that's what is intended (a convenience so that users don't have to wrap things in css:{}, although we still recommend it for a slight performance boost and better specificity). Note: we cannot check "nodeType" on the window inside an iframe.
				_autoCSS(this.vars, target);
			}
			for (p in this.vars) {
				v = this.vars[p];
				if (_reservedProps[p]) {
					if (v) if ((v instanceof Array) || (v.push && _isArray(v))) if (v.join("").indexOf("{self}") !== -1) {
						this.vars[p] = v = this._swapSelfInParams(v, this);
					}

				} else if (_plugins[p] && (plugin = new _plugins[p]())._onInitTween(target, this.vars[p], this)) {

					//t - target 		[object]
					//p - property 		[string]
					//s - start			[number]
					//c - change		[number]
					//f - isFunction	[boolean]
					//n - name			[string]
					//pg - isPlugin 	[boolean]
					//pr - priority		[number]
					this._firstPT = pt = {_next:this._firstPT, t:plugin, p:"setRatio", s:0, c:1, f:true, n:p, pg:true, pr:plugin._priority};
					i = plugin._overwriteProps.length;
					while (--i > -1) {
						propLookup[plugin._overwriteProps[i]] = this._firstPT;
					}
					if (plugin._priority || plugin._onInitAllProps) {
						initPlugins = true;
					}
					if (plugin._onDisable || plugin._onEnable) {
						this._notifyPluginsOfEnabled = true;
					}

				} else {
					this._firstPT = propLookup[p] = pt = {_next:this._firstPT, t:target, p:p, f:(typeof(target[p]) === "function"), n:p, pg:false, pr:0};
					pt.s = (!pt.f) ? parseFloat(target[p]) : target[ ((p.indexOf("set") || typeof(target["get" + p.substr(3)]) !== "function") ? p : "get" + p.substr(3)) ]();
					pt.c = (typeof(v) === "string" && v.charAt(1) === "=") ? parseInt(v.charAt(0) + "1", 10) * Number(v.substr(2)) : (Number(v) - pt.s) || 0;
				}
				if (pt) if (pt._next) {
					pt._next._prev = pt;
				}
			}

			if (overwrittenProps) if (this._kill(overwrittenProps, target)) { //another tween may have tried to overwrite properties of this tween before init() was called (like if two tweens start at the same time, the one created second will run first)
				return this._initProps(target, propLookup, siblings, overwrittenProps);
			}
			if (this._overwrite > 1) if (this._firstPT) if (siblings.length > 1) if (_applyOverwrite(target, this, propLookup, this._overwrite, siblings)) {
				this._kill(propLookup, target);
				return this._initProps(target, propLookup, siblings, overwrittenProps);
			}
			if (this._firstPT) if ((this.vars.lazy !== false && this._duration) || (this.vars.lazy && !this._duration)) { //zero duration tweens don't lazy render by default; everything else does.
				_lazyLookup[target._gsTweenID] = true;
			}
			return initPlugins;
		};

		p.render = function(time, suppressEvents, force) {
			var prevTime = this._time,
				duration = this._duration,
				prevRawPrevTime = this._rawPrevTime,
				isComplete, callback, pt, rawPrevTime;
			if (time >= duration) {
				this._totalTime = this._time = duration;
				this.ratio = this._ease._calcEnd ? this._ease.getRatio(1) : 1;
				if (!this._reversed ) {
					isComplete = true;
					callback = "onComplete";
				}
				if (duration === 0) if (this._initted || !this.vars.lazy || force) { //zero-duration tweens are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
					if (this._startTime === this._timeline._duration) { //if a zero-duration tween is at the VERY end of a timeline and that timeline renders at its end, it will typically add a tiny bit of cushion to the render time to prevent rounding errors from getting in the way of tweens rendering their VERY end. If we then reverse() that timeline, the zero-duration tween will trigger its onReverseComplete even though technically the playhead didn't pass over it again. It's a very specific edge case we must accommodate.
						time = 0;
					}
					if (time === 0 || prevRawPrevTime < 0 || prevRawPrevTime === _tinyNum) if (prevRawPrevTime !== time) {
						force = true;
						if (prevRawPrevTime > _tinyNum) {
							callback = "onReverseComplete";
						}
					}
					this._rawPrevTime = rawPrevTime = (!suppressEvents || time || prevRawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
				}

			} else if (time < 0.0000001) { //to work around occasional floating point math artifacts, round super small values to 0.
				this._totalTime = this._time = 0;
				this.ratio = this._ease._calcEnd ? this._ease.getRatio(0) : 0;
				if (prevTime !== 0 || (duration === 0 && prevRawPrevTime > 0 && prevRawPrevTime !== _tinyNum)) {
					callback = "onReverseComplete";
					isComplete = this._reversed;
				}
				if (time < 0) {
					this._active = false;
					if (duration === 0) if (this._initted || !this.vars.lazy || force) { //zero-duration tweens are tricky because we must discern the momentum/direction of time in order to determine whether the starting values should be rendered or the ending values. If the "playhead" of its timeline goes past the zero-duration tween in the forward direction or lands directly on it, the end values should be rendered, but if the timeline's "playhead" moves past it in the backward direction (from a postitive time to a negative time), the starting values must be rendered.
						if (prevRawPrevTime >= 0) {
							force = true;
						}
						this._rawPrevTime = rawPrevTime = (!suppressEvents || time || prevRawPrevTime === time) ? time : _tinyNum; //when the playhead arrives at EXACTLY time 0 (right on top) of a zero-duration tween, we need to discern if events are suppressed so that when the playhead moves again (next time), it'll trigger the callback. If events are NOT suppressed, obviously the callback would be triggered in this render. Basically, the callback should fire either when the playhead ARRIVES or LEAVES this exact spot, not both. Imagine doing a timeline.seek(0) and there's a callback that sits at 0. Since events are suppressed on that seek() by default, nothing will fire, but when the playhead moves off of that position, the callback should fire. This behavior is what people intuitively expect. We set the _rawPrevTime to be a precise tiny number to indicate this scenario rather than using another property/variable which would increase memory usage. This technique is less readable, but more efficient.
					}
				} else if (!this._initted) { //if we render the very beginning (time == 0) of a fromTo(), we must force the render (normal tweens wouldn't need to render at a time of 0 when the prevTime was also 0). This is also mandatory to make sure overwriting kicks in immediately.
					force = true;
				}
			} else {
				this._totalTime = this._time = time;

				if (this._easeType) {
					var r = time / duration, type = this._easeType, pow = this._easePower;
					if (type === 1 || (type === 3 && r >= 0.5)) {
						r = 1 - r;
					}
					if (type === 3) {
						r *= 2;
					}
					if (pow === 1) {
						r *= r;
					} else if (pow === 2) {
						r *= r * r;
					} else if (pow === 3) {
						r *= r * r * r;
					} else if (pow === 4) {
						r *= r * r * r * r;
					}

					if (type === 1) {
						this.ratio = 1 - r;
					} else if (type === 2) {
						this.ratio = r;
					} else if (time / duration < 0.5) {
						this.ratio = r / 2;
					} else {
						this.ratio = 1 - (r / 2);
					}

				} else {
					this.ratio = this._ease.getRatio(time / duration);
				}
			}

			if (this._time === prevTime && !force) {
				return;
			} else if (!this._initted) {
				this._init();
				if (!this._initted || this._gc) { //immediateRender tweens typically won't initialize until the playhead advances (_time is greater than 0) in order to ensure that overwriting occurs properly. Also, if all of the tweening properties have been overwritten (which would cause _gc to be true, as set in _init()), we shouldn't continue otherwise an onStart callback could be called for example.
					return;
				} else if (!force && this._firstPT && ((this.vars.lazy !== false && this._duration) || (this.vars.lazy && !this._duration))) {
					this._time = this._totalTime = prevTime;
					this._rawPrevTime = prevRawPrevTime;
					_lazyTweens.push(this);
					this._lazy = time;
					return;
				}
				//_ease is initially set to defaultEase, so now that init() has run, _ease is set properly and we need to recalculate the ratio. Overall this is faster than using conditional logic earlier in the method to avoid having to set ratio twice because we only init() once but renderTime() gets called VERY frequently.
				if (this._time && !isComplete) {
					this.ratio = this._ease.getRatio(this._time / duration);
				} else if (isComplete && this._ease._calcEnd) {
					this.ratio = this._ease.getRatio((this._time === 0) ? 0 : 1);
				}
			}
			if (this._lazy !== false) { //in case a lazy render is pending, we should flush it because the new render is occuring now (imagine a lazy tween instantiating and then immediately the user calls tween.seek(tween.duration()), skipping to the end - the end render would be forced, and then if we didn't flush the lazy render, it'd fire AFTER the seek(), rendering it at the wrong time.
				this._lazy = false;
			}
			if (!this._active) if (!this._paused && this._time !== prevTime && time >= 0) {
				this._active = true;  //so that if the user renders a tween (as opposed to the timeline rendering it), the timeline is forced to re-render and align it with the proper time/frame on the next rendering cycle. Maybe the tween already finished but the user manually re-renders it as halfway done.
			}
			if (prevTime === 0) {
				if (this._startAt) {
					if (time >= 0) {
						this._startAt.render(time, suppressEvents, force);
					} else if (!callback) {
						callback = "_dummyGS"; //if no callback is defined, use a dummy value just so that the condition at the end evaluates as true because _startAt should render AFTER the normal render loop when the time is negative. We could handle this in a more intuitive way, of course, but the render loop is the MOST important thing to optimize, so this technique allows us to avoid adding extra conditional logic in a high-frequency area.
					}
				}
				if (this.vars.onStart) if (this._time !== 0 || duration === 0) if (!suppressEvents) {
					this.vars.onStart.apply(this.vars.onStartScope || this, this.vars.onStartParams || _blankArray);
				}
			}

			pt = this._firstPT;
			while (pt) {
				if (pt.f) {
					pt.t[pt.p](pt.c * this.ratio + pt.s);
				} else {
					pt.t[pt.p] = pt.c * this.ratio + pt.s;
				}
				pt = pt._next;
			}

			if (this._onUpdate) {
				if (time < 0) if (this._startAt && this._startTime) { //if the tween is positioned at the VERY beginning (_startTime 0) of its parent timeline, it's illegal for the playhead to go back further, so we should not render the recorded startAt values.
					this._startAt.render(time, suppressEvents, force); //note: for performance reasons, we tuck this conditional logic inside less traveled areas (most tweens don't have an onUpdate). We'd just have it at the end before the onComplete, but the values should be updated before any onUpdate is called, so we ALSO put it here and then if it's not called, we do so later near the onComplete.
				}
				if (!suppressEvents) if (this._time !== prevTime || isComplete) {
					this._onUpdate.apply(this.vars.onUpdateScope || this, this.vars.onUpdateParams || _blankArray);
				}
			}

			if (callback) if (!this._gc) { //check _gc because there's a chance that kill() could be called in an onUpdate
				if (time < 0 && this._startAt && !this._onUpdate && this._startTime) { //if the tween is positioned at the VERY beginning (_startTime 0) of its parent timeline, it's illegal for the playhead to go back further, so we should not render the recorded startAt values.
					this._startAt.render(time, suppressEvents, force);
				}
				if (isComplete) {
					if (this._timeline.autoRemoveChildren) {
						this._enabled(false, false);
					}
					this._active = false;
				}
				if (!suppressEvents && this.vars[callback]) {
					this.vars[callback].apply(this.vars[callback + "Scope"] || this, this.vars[callback + "Params"] || _blankArray);
				}
				if (duration === 0 && this._rawPrevTime === _tinyNum && rawPrevTime !== _tinyNum) { //the onComplete or onReverseComplete could trigger movement of the playhead and for zero-duration tweens (which must discern direction) that land directly back on their start time, we don't want to fire again on the next render. Think of several addPause()'s in a timeline that forces the playhead to a certain spot, but what if it's already paused and another tween is tweening the "time" of the timeline? Each time it moves [forward] past that spot, it would move back, and since suppressEvents is true, it'd reset _rawPrevTime to _tinyNum so that when it begins again, the callback would fire (so ultimately it could bounce back and forth during that tween). Again, this is a very uncommon scenario, but possible nonetheless.
					this._rawPrevTime = 0;
				}
			}

		};

		p._kill = function(vars, target) {
			if (vars === "all") {
				vars = null;
			}
			if (vars == null) if (target == null || target === this.target) {
				this._lazy = false;
				return this._enabled(false, false);
			}
			target = (typeof(target) !== "string") ? (target || this._targets || this.target) : TweenLite.selector(target) || target;
			var i, overwrittenProps, p, pt, propLookup, changed, killProps, record;
			if ((_isArray(target) || _isSelector(target)) && typeof(target[0]) !== "number") {
				i = target.length;
				while (--i > -1) {
					if (this._kill(vars, target[i])) {
						changed = true;
					}
				}
			} else {
				if (this._targets) {
					i = this._targets.length;
					while (--i > -1) {
						if (target === this._targets[i]) {
							propLookup = this._propLookup[i] || {};
							this._overwrittenProps = this._overwrittenProps || [];
							overwrittenProps = this._overwrittenProps[i] = vars ? this._overwrittenProps[i] || {} : "all";
							break;
						}
					}
				} else if (target !== this.target) {
					return false;
				} else {
					propLookup = this._propLookup;
					overwrittenProps = this._overwrittenProps = vars ? this._overwrittenProps || {} : "all";
				}

				if (propLookup) {
					killProps = vars || propLookup;
					record = (vars !== overwrittenProps && overwrittenProps !== "all" && vars !== propLookup && (typeof(vars) !== "object" || !vars._tempKill)); //_tempKill is a super-secret way to delete a particular tweening property but NOT have it remembered as an official overwritten property (like in BezierPlugin)
					for (p in killProps) {
						if ((pt = propLookup[p])) {
							if (pt.pg && pt.t._kill(killProps)) {
								changed = true; //some plugins need to be notified so they can perform cleanup tasks first
							}
							if (!pt.pg || pt.t._overwriteProps.length === 0) {
								if (pt._prev) {
									pt._prev._next = pt._next;
								} else if (pt === this._firstPT) {
									this._firstPT = pt._next;
								}
								if (pt._next) {
									pt._next._prev = pt._prev;
								}
								pt._next = pt._prev = null;
							}
							delete propLookup[p];
						}
						if (record) {
							overwrittenProps[p] = 1;
						}
					}
					if (!this._firstPT && this._initted) { //if all tweening properties are killed, kill the tween. Without this line, if there's a tween with multiple targets and then you killTweensOf() each target individually, the tween would technically still remain active and fire its onComplete even though there aren't any more properties tweening.
						this._enabled(false, false);
					}
				}
			}
			return changed;
		};

		p.invalidate = function() {
			if (this._notifyPluginsOfEnabled) {
				TweenLite._onPluginEvent("_onDisable", this);
			}
			this._firstPT = null;
			this._overwrittenProps = null;
			this._onUpdate = null;
			this._startAt = null;
			this._initted = this._active = this._notifyPluginsOfEnabled = this._lazy = false;
			this._propLookup = (this._targets) ? {} : [];
			return this;
		};

		p._enabled = function(enabled, ignoreTimeline) {
			if (!_tickerActive) {
				_ticker.wake();
			}
			if (enabled && this._gc) {
				var targets = this._targets,
					i;
				if (targets) {
					i = targets.length;
					while (--i > -1) {
						this._siblings[i] = _register(targets[i], this, true);
					}
				} else {
					this._siblings = _register(this.target, this, true);
				}
			}
			Animation.prototype._enabled.call(this, enabled, ignoreTimeline);
			if (this._notifyPluginsOfEnabled) if (this._firstPT) {
				return TweenLite._onPluginEvent((enabled ? "_onEnable" : "_onDisable"), this);
			}
			return false;
		};


//----TweenLite static methods -----------------------------------------------------

		TweenLite.to = function(target, duration, vars) {
			return new TweenLite(target, duration, vars);
		};

		TweenLite.from = function(target, duration, vars) {
			vars.runBackwards = true;
			vars.immediateRender = (vars.immediateRender != false);
			return new TweenLite(target, duration, vars);
		};

		TweenLite.fromTo = function(target, duration, fromVars, toVars) {
			toVars.startAt = fromVars;
			toVars.immediateRender = (toVars.immediateRender != false && fromVars.immediateRender != false);
			return new TweenLite(target, duration, toVars);
		};

		TweenLite.delayedCall = function(delay, callback, params, scope, useFrames) {
			return new TweenLite(callback, 0, {delay:delay, onComplete:callback, onCompleteParams:params, onCompleteScope:scope, onReverseComplete:callback, onReverseCompleteParams:params, onReverseCompleteScope:scope, immediateRender:false, useFrames:useFrames, overwrite:0});
		};

		TweenLite.set = function(target, vars) {
			return new TweenLite(target, 0, vars);
		};

		TweenLite.getTweensOf = function(target, onlyActive) {
			if (target == null) { return []; }
			target = (typeof(target) !== "string") ? target : TweenLite.selector(target) || target;
			var i, a, j, t;
			if ((_isArray(target) || _isSelector(target)) && typeof(target[0]) !== "number") {
				i = target.length;
				a = [];
				while (--i > -1) {
					a = a.concat(TweenLite.getTweensOf(target[i], onlyActive));
				}
				i = a.length;
				//now get rid of any duplicates (tweens of arrays of objects could cause duplicates)
				while (--i > -1) {
					t = a[i];
					j = i;
					while (--j > -1) {
						if (t === a[j]) {
							a.splice(i, 1);
						}
					}
				}
			} else {
				a = _register(target).concat();
				i = a.length;
				while (--i > -1) {
					if (a[i]._gc || (onlyActive && !a[i].isActive())) {
						a.splice(i, 1);
					}
				}
			}
			return a;
		};

		TweenLite.killTweensOf = TweenLite.killDelayedCallsTo = function(target, onlyActive, vars) {
			if (typeof(onlyActive) === "object") {
				vars = onlyActive; //for backwards compatibility (before "onlyActive" parameter was inserted)
				onlyActive = false;
			}
			var a = TweenLite.getTweensOf(target, onlyActive),
				i = a.length;
			while (--i > -1) {
				a[i]._kill(vars, target);
			}
		};



/*
 * ----------------------------------------------------------------
 * TweenPlugin   (could easily be split out as a separate file/class, but included for ease of use (so that people don't need to include another <script> call before loading plugins which is easy to forget)
 * ----------------------------------------------------------------
 */
		var TweenPlugin = _class("plugins.TweenPlugin", function(props, priority) {
					this._overwriteProps = (props || "").split(",");
					this._propName = this._overwriteProps[0];
					this._priority = priority || 0;
					this._super = TweenPlugin.prototype;
				}, true);

		p = TweenPlugin.prototype;
		TweenPlugin.version = "1.10.1";
		TweenPlugin.API = 2;
		p._firstPT = null;

		p._addTween = function(target, prop, start, end, overwriteProp, round) {
			var c, pt;
			if (end != null && (c = (typeof(end) === "number" || end.charAt(1) !== "=") ? Number(end) - start : parseInt(end.charAt(0) + "1", 10) * Number(end.substr(2)))) {
				this._firstPT = pt = {_next:this._firstPT, t:target, p:prop, s:start, c:c, f:(typeof(target[prop]) === "function"), n:overwriteProp || prop, r:round};
				if (pt._next) {
					pt._next._prev = pt;
				}
				return pt;
			}
		};

		p.setRatio = function(v) {
			var pt = this._firstPT,
				min = 0.000001,
				val;
			while (pt) {
				val = pt.c * v + pt.s;
				if (pt.r) {
					val = Math.round(val);
				} else if (val < min) if (val > -min) { //prevents issues with converting very small numbers to strings in the browser
					val = 0;
				}
				if (pt.f) {
					pt.t[pt.p](val);
				} else {
					pt.t[pt.p] = val;
				}
				pt = pt._next;
			}
		};

		p._kill = function(lookup) {
			var a = this._overwriteProps,
				pt = this._firstPT,
				i;
			if (lookup[this._propName] != null) {
				this._overwriteProps = [];
			} else {
				i = a.length;
				while (--i > -1) {
					if (lookup[a[i]] != null) {
						a.splice(i, 1);
					}
				}
			}
			while (pt) {
				if (lookup[pt.n] != null) {
					if (pt._next) {
						pt._next._prev = pt._prev;
					}
					if (pt._prev) {
						pt._prev._next = pt._next;
						pt._prev = null;
					} else if (this._firstPT === pt) {
						this._firstPT = pt._next;
					}
				}
				pt = pt._next;
			}
			return false;
		};

		p._roundProps = function(lookup, value) {
			var pt = this._firstPT;
			while (pt) {
				if (lookup[this._propName] || (pt.n != null && lookup[ pt.n.split(this._propName + "_").join("") ])) { //some properties that are very plugin-specific add a prefix named after the _propName plus an underscore, so we need to ignore that extra stuff here.
					pt.r = value;
				}
				pt = pt._next;
			}
		};

		TweenLite._onPluginEvent = function(type, tween) {
			var pt = tween._firstPT,
				changed, pt2, first, last, next;
			if (type === "_onInitAllProps") {
				//sorts the PropTween linked list in order of priority because some plugins need to render earlier/later than others, like MotionBlurPlugin applies its effects after all x/y/alpha tweens have rendered on each frame.
				while (pt) {
					next = pt._next;
					pt2 = first;
					while (pt2 && pt2.pr > pt.pr) {
						pt2 = pt2._next;
					}
					if ((pt._prev = pt2 ? pt2._prev : last)) {
						pt._prev._next = pt;
					} else {
						first = pt;
					}
					if ((pt._next = pt2)) {
						pt2._prev = pt;
					} else {
						last = pt;
					}
					pt = next;
				}
				pt = tween._firstPT = first;
			}
			while (pt) {
				if (pt.pg) if (typeof(pt.t[type]) === "function") if (pt.t[type]()) {
					changed = true;
				}
				pt = pt._next;
			}
			return changed;
		};

		TweenPlugin.activate = function(plugins) {
			var i = plugins.length;
			while (--i > -1) {
				if (plugins[i].API === TweenPlugin.API) {
					_plugins[(new plugins[i]())._propName] = plugins[i];
				}
			}
			return true;
		};

		//provides a more concise way to define plugins that have no dependencies besides TweenPlugin and TweenLite, wrapping common boilerplate stuff into one function (added in 1.9.0). You don't NEED to use this to define a plugin - the old way still works and can be useful in certain (rare) situations.
		_gsDefine.plugin = function(config) {
			if (!config || !config.propName || !config.init || !config.API) { throw "illegal plugin definition."; }
			var propName = config.propName,
				priority = config.priority || 0,
				overwriteProps = config.overwriteProps,
				map = {init:"_onInitTween", set:"setRatio", kill:"_kill", round:"_roundProps", initAll:"_onInitAllProps"},
				Plugin = _class("plugins." + propName.charAt(0).toUpperCase() + propName.substr(1) + "Plugin",
					function() {
						TweenPlugin.call(this, propName, priority);
						this._overwriteProps = overwriteProps || [];
					}, (config.global === true)),
				p = Plugin.prototype = new TweenPlugin(propName),
				prop;
			p.constructor = Plugin;
			Plugin.API = config.API;
			for (prop in map) {
				if (typeof(config[prop]) === "function") {
					p[map[prop]] = config[prop];
				}
			}
			Plugin.version = config.version;
			TweenPlugin.activate([Plugin]);
			return Plugin;
		};


		//now run through all the dependencies discovered and if any are missing, log that to the console as a warning. This is why it's best to have TweenLite load last - it can check all the dependencies for you.
		a = window._gsQueue;
		if (a) {
			for (i = 0; i < a.length; i++) {
				a[i]();
			}
			for (p in _defLookup) {
				if (!_defLookup[p].func) {
					//window.console.log("GSAP encountered missing dependency: com.greensock." + p);
				}
			}
		}

		_tickerActive = false; //ensures that the first official animation forces a ticker.tick() to update the time when it is instantiated

})(window);

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
                        "    {{btnText}}<i style='font-size:0.875rem' ng-class='{\"ion-ios-arrow-up\": showAccordion,\"ion-ios-arrow-down\": !showAccordion, }'></i> \n" +
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
	    "        <i i ng-show = 'childLength > 0' ng-class=\"{'ion-ios-arrow-down':!isOpen,'ion-ios-arrow-up':isOpen }\" class=\"pull-right\"></i>\n" +
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
    "		<i class=\"ion-ios-arrow-down\"></i>\n" +
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
    "                            <span class=\"select2-chosen dropDownMarginRight\" >{{selectedFromOption[day]}} <i ng-if=\"daysList[day]\" ng-class=\"FrtimeListDay[day] ? 'ion-arrow-up-b' : 'ion-arrow-down-b'\"></i></span>\n" +
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
    "                            <span class=\"select2-chosen dropDownMarginRight\">{{selectedToOption[day]}} <i ng-if=\"daysList[day]\" ng-class=\"TotimeListDay[day] ? 'ion-arrow-up-b' : 'ion-arrow-down-b'\" ></i></span>\n" +
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
    "        <i ng-class=\"{'ion-ios-arrow-up': (scrollbarAxis === 'y'), 'icon-chevron-left': (scrollbarAxis === 'x')}\"></i>\n" +
    "    </a>\n" +
    "</div>\n" +
    "<div class=\"scroll-viewport\" ng-style=\"{height: (scrollbarAxis === 'x' && position.height + 'px') || viewportHeight, width: viewportWidth}\" style=\"position: relative; overflow: hidden\">\n" +
    "    <div class=\"scroll-overview\" style=\"position: absolute; display: table; width: 100%\" att-position=\"position\" ng-transclude></div>\n" +
    "</div>\n" +
    "<div class='next icons-list' data-size=\"medium\" ng-show=\"navigation && nextAvailable\" ng-style=\"{height: scrollbarAxis === 'x' && position.height + 'px'}\">\n" +
    "    <a href=\"javascript:void(0);\" ng-click=\"customScroll(true)\" aria-label=\"Scroll\" aria-hidden=\"true\">\n" +
    "        <i ng-class=\"{'ion-ios-arrow-down': (scrollbarAxis === 'y'), 'icon-chevron-right': (scrollbarAxis === 'x')}\"></i>\n" +
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
    "		<i ng-class=\"isActionsShown ? 'ion-arrow-up-b' : 'ion-arrow-down-b'\"></i>\n" +
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
    "	        	ng-class=\"isDropDownOpen ? 'ion-arrow-up-b' : 'ion-arrow-down-b'\"> </i>\n" +
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
    " a text, picture, or video message1 to a wireless device from your email:my message.</textarea>\n" +
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