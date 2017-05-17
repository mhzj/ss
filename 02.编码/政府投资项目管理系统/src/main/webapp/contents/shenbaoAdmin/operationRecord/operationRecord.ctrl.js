(function () {
    'use strict';

    angular
        .module('app')
        .controller('logCtrl', log);

    log.$inject = ['$location','logSvc']; 

    function log($location, logSvc) {
        /* jshint validthis:true */
        var vm = this;
        vm.title = '日志列表';
        vm.titleLookOpration = "查看操作日志";
        

       
        activate();
        function activate() {
            logSvc.grid(vm);
        }
    }
})();
