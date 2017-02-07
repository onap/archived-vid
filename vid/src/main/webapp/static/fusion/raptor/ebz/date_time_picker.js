 String.prototype.paddingLeft = function (paddingValue) {
    return String(paddingValue + this).slice(-paddingValue.length);
 };

angular.module("app/scripts/ng_js_att_tpls/datepicker/dateTimePickerPopup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("app/scripts/ng_js_att_tpls/datepicker/dateTimePickerPopup.html",
    "<div class=\"calendar\">\n" +
    "    <div class=\"box\" ng-class=\"{'active': isOpen}\">\n" +
    "        <span ng-transclude></span>\n" +
    
    "        <i class=\"calendar-icon\" tabindex=\"0\" att-accessibility-click=\"13,32\" ng-click=\"toggle()\" alt=\"Calendar Icon\"  ></i>\n" +
    "    </div>\n" +

    	'<div class="datetimepicker datepicker-wrapper datepicker-wrapper-display-none" style="z-index:10000;width:292px" ng-style="{display: (isOpen && \'block\') || \'none\'}" aria-hidden=\"false\" role=\"dialog\" tabindex=\"-1\">'
        +   '<div class="datetimepicker-content">'
        
        +   '<ul class="tabsbid--small">'
        +   '<li class="tabsbid__item" ng-class="{\'tabsbid__item--active\':tab==\'date\'}" ng-click="setTab(\'date\')">'
        +   '<a class="tabsbid__item-link" href="" tabindex="0" att-accessibility-click="32,13">Date</a>'
        +   '</li>'
        +   '<li class="tabsbid__item " ng-class="{\'tabsbid__item--active\':tab==\'time\'}" ng-click="setTab(\'time\')">'
        +   '<a class="tabsbid__item-link" href="" tabindex="0" att-accessibility-click="32,13">Time</a>'
        +   '</li>'
        +   '</ul>'

        +     '<div class="datetimepicker-section datetimepicker-date-section" ng-if="tab==\'date\'">'
        +       '<div class="datetimepicker-month">'
        +'<div class="icons-list left" style="margin:5px;cursor: pointer;" data-size="medium" ng-click="addMonth(-1)"><i class="icon-arrow-left-circle" ng-class="{\'disabled\': disablePrev}" alt="Left Arrow"></i></div>'
        +'<div class="icons-list right" style="margin:5px;cursor: pointer;" data-size="medium" ng-click="addMonth(1)"><i class="icon-arrow-right-circle" ng-class="{\'disabled\': disableNext}" alt="Right Arrow"></i></div>'
        +         '<div class="datetimepicker-current-month">{{displayMonth}} {{year}}</div>'
        +       '</div>'
        +       '<div class="datetimepicker-calendar">'
        +         '<div class="datetimepicker-day" ng-repeat="day in dayNames">{{day | limitTo: 1}}</div>'
        +         '<div class="datetimepicker-day datetimepicker-leading-day" ng-repeat="d in days.leadingDays">{{d}}</div>'
        +         '<div class="datetimepicker-day datetimepicker-active-day" ng-class="{\'selected\':day==d}" ng-click="setDay(d)" ng-repeat="d in days.days">{{d}}</div>'
        +         '<div class="datetimepicker-day datetimepicker-trailing-day" ng-repeat="d in days.trailingDays">{{d}}</div>'
        +       '</div>'
        +     '</div>'
        
        +     '<div class="datetimepicker-section datetimepicker-date-time" ng-if="tab==\'time\'">'
        +       '<div class="datetimepicker-month">'
        +         '<div class="datetimepicker-current-month">{{hour}}:{{minute}}</div>'
        +       '</div>'        
        +       '<div class="time-circle-outer">'
        +         '<div class="time-meridian time-left" ng-click="setMeridian(\'AM\')" ng-class="{\'selected\':meridian==\'AM\'}">AM</div>'
        +         '<div class="time-meridian time-right" ng-click="setMeridian(\'PM\')" ng-class="{\'selected\':meridian==\'PM\'}">PM</div>'
        +         '<div class="time-circle-center"></div>'
        +         '<div class="time-circle-hand time-circle-hand-large deg-{{minute/5}}" ></div>'
        +         '<div class="time time-{{$index+1}}" ng-class="{\'selected\':minute==time}" ng-click="setMinutes(time)" ng-repeat="time in [5,10,15,20,25,30,35,40,45,50,55,0]">{{time}}</div>'
        +         '<div class="time-circle-inner">'
        +           '<div class="time-circle-hand deg-{{hour}}" ></div>'
        +           '<div class="time time-{{$index+1}}" ng-class="{\'selected\':hour==time}" ng-click="setHour(time)" ng-repeat="time in [1,2,3,4,5,6,7,8,9,10,11,12]">{{time}}</div>'
        +         '</div>'
        +       '</div>'
        +     '</div>'
        
        +   '</div>'
        + '</div>' +

    "</div>\n" +
    "");
}]);

angular.module('quantum').requires.push("app/scripts/ng_js_att_tpls/datepicker/dateTimePickerPopup.html");

angular.module('quantum')
.directive('dateTimePickerPopup', ['$document', 'datepickerService', '$isElement', '$documentBind', function($document, datepickerService, $isElement, $documentBind) {
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

//        scope.$watch('current', function () {
//            toggle(false);
//        });

        var outsideClick = function (e) {
            var isElement = $isElement(angular.element(e.target), elem, $document);
            if(!isElement) {
                toggle(false);
                scope.$apply();
            }
        };

        $documentBind.click('isOpen', outsideClick, scope);
        
    	scope.tabs = [{
            title: 'DATE',
            url: '#option1'
        }, {
            title: 'TIME',
            url: '#option2',
            selected: true
        }
    ];
        
        //--------------------------------------

        scope.state = false;
        scope.tab = 'time';
        scope.setTab = function(tab){
          scope.tab = tab;
        };
        scope.config = {
          modal: true,
          color:'rgba(5, 116, 172, 1)',
          backgroundColor: 'rgba(0,0,0,0.75)'
        };
        scope.months = ["January","February","March","April","May","June","July","Augusta","September","October","November","December"];
        scope.dayNames = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
        scope.$watch('current',function(value){
          var m;
          if(value)
            m = moment(value);
          else
            m = moment();
          m = m.minute(5*Math.ceil(m.minute()/5));
          scope.display = m.format('YYYY-MM-DD hh:mm A');
          scope.days = scope.getDaysInMonth(m.year(),m.month());
          scope.minute = m.minute();
          scope.meridian = m.format('A');
          scope.hour  = scope.meridian == 'PM' ? m.hour() - 12: m.hour();
          if(scope.hour==0) scope.hour = 12;
          scope.datePreview = m.format('YYYY-MM-DD');
          scope.timePreview = m.format('hh:mm A');
          scope.displayMonth = scope.months[m.month()];
          scope.day = m.date();
          scope.year = m.year();

        });

        scope.setDay = function(date){
          scope.current = moment(scope.current).date(date).toDate();
        };

        scope.setState = function(state){
          scope.state = false;
        };

        scope.setHour = function(hour){
          if(scope.meridian == 'PM' && hour < 12)
            hour = hour + 12;
          if(scope.meridian == 'AM' && hour == 12)
            hour = hour - 12;
          scope.current = moment(scope.current).hour(hour).toDate();
        };

        scope.setMeridian = function(meridian){
          var m = moment(scope.current);

          if(meridian == 'AM'){
            if(m.hours()>=12){
              m = m.add(-12,'hours');
              scope.current = m.toDate();
            }
          }else{
            if(m.hours()<12){
              m = m.add(12,'hours');
              scope.current = m.toDate();
            }
          }
        };

        scope.setMinutes = function(minutes){
          scope.current = moment(scope.current).minute(minutes).toDate();
        };

        var days = [];
        for(var i=1;i<=31;i++){
          days.push(i);
        }
        scope.getDaysInMonth = function(year,month){
          var firstDayOfWeek = 0;
          var firstDayOfMonth = new Date(year, month, 1),
              lastDayOfMonth = new Date(year, month + 1, 0),
              lastDayOfPreviousMonth = new Date(year, month, 0),
              daysInMonth = lastDayOfMonth.getDate(),
              daysInLastMonth = lastDayOfPreviousMonth.getDate(),
              dayOfWeek = firstDayOfMonth.getDay(),
              leadingDays = (dayOfWeek - firstDayOfWeek + 7) % 7 || 7,
              trailingDays = days.slice(0, 6 * 7 - (leadingDays + daysInMonth));
          if (trailingDays.length > 7) {
            trailingDays = trailingDays.slice(0, trailingDays.length-7);
          }

          return {
            year: year,
            month: month,
            days: days.slice(0, daysInMonth),
            leadingDays: days.slice(- leadingDays - (31 - daysInLastMonth), daysInLastMonth),
            trailingDays: trailingDays
          };
        };

        scope.addMonth = function(increment){
          scope.current = moment(scope.current).add(increment,'months').toDate();
        };
      
    };

    return {
        restrict: 'EA',
        replace: true,
        transclude: true,
        templateUrl: 'app/scripts/ng_js_att_tpls/datepicker/dateTimePickerPopup.html',
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
.directive('attDateTimePicker', ['$log', function($log) {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {},
        controller: ['$scope', '$element', '$attrs', '$compile', 'datepickerConfig', 'datepickerService', function($scope, $element, $attrs, $compile, datepickerConfig, datepickerService) {
            var dateFormatString = angular.isDefined($attrs.dateFormat) ? $scope.$parent.$eval($attrs.dateFormat) : datepickerConfig.dateFormat;
            var selectedDateMessage = '<div class="sr-focus hidden-spoken" tabindex="-1">the date you selected is {{$parent.current | date : \'' + dateFormatString + '\'}}</div>';

            $element.removeAttr('att-date-time-picker');
            $element.removeAttr('ng-model');
            $element.attr('ng-model', '$parent.current');
            $element.attr('aria-describedby', 'datepicker');
            $element.attr('format-date', dateFormatString);
            $element.attr('att-input-deny', '[^0-9ampAMP \/:-]');
            $element.attr('maxlength', 20);

            var wrapperElement = angular.element('<div></div>');
            wrapperElement.attr('date-time-picker-popup', '');
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
}]);


