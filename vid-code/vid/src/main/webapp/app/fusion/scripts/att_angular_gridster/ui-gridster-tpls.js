/**
* FileName ui-gridster
* Version 0.0.1
* Build number ad58c6f4f8f8fd7f04ac457f95d76f09
* Date 08/17/2015
*/


(function(angular, window){
angular.module("att.gridster", ["att.gridster.tpls", "att.gridster.utilities","att.gridster.gridster"]);
angular.module("att.gridster.tpls", ["template/gridster/gridster.html","template/gridster/gridsterItem.html","template/gridster/gridsterItemBody.html","template/gridster/gridsterItemFooter.html","template/gridster/gridsterItemHeader.html"]);
angular.module('att.gridster.utilities', [])
        .factory('$extendObj', [function() {
                var _extendDeep = function(dst) {
                    angular.forEach(arguments, function(obj) {
                        if (obj !== dst) {
                            angular.forEach(obj, function(value, key) {
                                if (dst[key] && dst[key].constructor && dst[key].constructor === Object) {
                                    _extendDeep(dst[key], value);
                                } else {
                                    dst[key] = value;
                                }
                            });
                        }
                    });
                    return dst;
                };
                return {
                    extendDeep: _extendDeep
                };
            }]);

angular.module('att.gridster.gridster', ['attGridsterLib', 'att.gridster.utilities'])
        .config(['$compileProvider', function($compileProvider) {
                $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|javascript):/);
            }])
        .constant('attGridsterConfig',
                {
                    columns: 3,
                    margins: [10, 10],
                    outerMargin: true,
                    pushing: true,
                    floating: true,
                    swapping: true,
                    draggable: {
                        enabled: true
                    }
                })
        .directive('attGridster', ['attGridsterConfig', '$extendObj', function(attGridsterConfig, $extendObj) {
                return {
                    restrict: 'EA',
                    scope: {
                        attGridsterOptions: '=?'
                    },
                    templateUrl: 'template/gridster/gridster.html',
                    replace: false,
                    transclude: true,
                    controller: [function() {}],
                    link: function(scope) {
                        if (angular.isDefined(scope.attGridsterOptions)) {
                            attGridsterConfig = $extendObj.extendDeep(attGridsterConfig, scope.attGridsterOptions);
                        }
                        scope.attGridsterConfig = attGridsterConfig;
                    }
                };
            }])
        .directive('attGridsterItem', ['$timeout', function($timeout) {
                return {
                    restrict: 'EA',
                    require: ['^attGridster'],
                    scope: {
                        attGridsterItem: '='
                    },
                    templateUrl: 'template/gridster/gridsterItem.html',
                    replace: false,
                    transclude: true,
                    controller: [function() {}]
                };
            }])
        .directive('attGridsterItemHeader', [function() {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {
                        headerText: '@',
                        subHeaderText: '@?'
                    },
                    templateUrl: 'template/gridster/gridsterItemHeader.html',
                    replace: true,
                    transclude: true,
                    link: function(scope, element) {
                        if (angular.isDefined(scope.subHeaderText) && scope.subHeaderText) {
                            angular.element(element[0].querySelector('span.gridster-item-sub-header-content')).attr("tabindex", "0");
                            angular.element(element[0].querySelector('span.gridster-item-sub-header-content')).attr("aria-label", scope.subHeaderText);
                        }
                    }
                };
            }])
        .directive('attGridsterItemBody', [function() {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {},
                    templateUrl: 'template/gridster/gridsterItemBody.html',
                    replace: true,
                    transclude: true
                };
            }])
        .directive('attGridsterItemFooter', ['$location', function($location) {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {
                        attGridsterItemFooterLink: '@?'
                    },
                    templateUrl: 'template/gridster/gridsterItemFooter.html',
                    replace: true,
                    transclude: true,
                    controller: ['$scope', function($scope) {
                            $scope.clickOnFooterLink = function(evt) {
                                evt.preventDefault();
                                evt.stopPropagation();
                                if ($scope.attGridsterItemFooterLink) {
                                    $location.url($scope.attGridsterItemFooterLink);
                                }
                            };
                        }],
                    link: function(scope, element) {
                        if (angular.isDefined(scope.attGridsterItemFooterLink) && scope.attGridsterItemFooterLink) {
                            element.attr("role", "link");
                        }
                    }
                };
            }]);
angular.module("template/gridster/gridster.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridster.html",
    "<div gridster='attGridsterConfig'><div ng-transclude></div></div>");
}]);

angular.module("template/gridster/gridsterItem.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItem.html",
    "<div gridster-item='attGridsterItem' class=\"gridster-item-container\" ng-transclude></div>");
}]);

angular.module("template/gridster/gridsterItemBody.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemBody.html",
    "<div class=\"gridster-item-body\" ng-transclude></div>");
}]);

angular.module("template/gridster/gridsterItemFooter.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemFooter.html",
    "<div class=\"gridster-item-footer\" ng-click=\"clickOnFooterLink($event)\" tabindex=\"0\" ng-keydown=\"(($event.keyCode && $event.keyCode === 13) || ($event.which && $event.which === 13)) && clickOnFooterLink($event)\" >\n" +
    "    <span class=\"gridster-item-footer-content\" ng-transclude></span>\n" +
    "</div>");
}]);

angular.module("template/gridster/gridsterItemHeader.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemHeader.html",
    "<div class=\"gridster-item-header\">\n" +
    "    <img gridster-item-drag src=\"static/fusion/images/att_angular_gridster/grips.png\" alt=\"||\" aria-label=\"Tap/Click to move\" class=\"gridster-item-handle\" />\n" +
    "    <span class=\"gridster-item-header-content\" tabindex=\"0\" role=\"presentation\" aria-label=\"{{headerText}}\">{{headerText}}</span>\n" +
    "    <span class=\"gridster-item-sub-header-content\" role=\"presentation\">{{subHeaderText}}</span>\n" +
    "    <div class=\"gridster-item-header-buttons-container\" ng-transclude></div>\n" +
    "</div>");
}]);

return {}
})(angular, window);