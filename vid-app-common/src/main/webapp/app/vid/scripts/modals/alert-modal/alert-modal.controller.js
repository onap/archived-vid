(function () {
    'use strict';

    appDS2.controller("alertModalController", ["$uibModalInstance", "jobInfo",
        "$log", alertModalController]);

    function alertModalController($uibModalInstance, jobInfo,  $log) {
        var vm = this;
        var init = function() {
            if (jobInfo) {
                vm.content = jobInfo.message;
                vm.mode = jobInfo.status;
                if(vm.mode=="failed"){
                    vm.Header= "Failed"
                }else{
                    vm.Header= "Success"
                }
            }
        };

        

      

        vm.close = function () {
            $uibModalInstance.close();
        };

        init();
    }
})();