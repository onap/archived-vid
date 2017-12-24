"use strict";

appDS2.directive('searchText', function() {
    return {
        restrict : "E",
        templateUrl: 'app/vid/scripts/view-models/search.htm',
        scope: {
            searchString : '='
        }
    }
});