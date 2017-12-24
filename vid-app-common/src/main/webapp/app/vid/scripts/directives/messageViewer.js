"use strict";

appDS2.directive('messageViewer', function() {
    return {
        restrict : "E",
        templateUrl: 'app/vid/scripts/view-models/messageViewer.htm',
        scope: {
            primaryMessage:'@',
            icon:'@',
            secondaryMessage:'@',
            tryAgain:'&'
        }
    }
});