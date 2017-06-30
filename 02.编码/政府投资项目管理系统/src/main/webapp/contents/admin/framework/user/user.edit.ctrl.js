(function () {
    'use strict';

    angular
        .module('app')
        .controller('userEditCtrl', user);

    user.$inject = ['$location','userSvc','$state']; 

    function user($location, userSvc,$state) {
        /* jshint validthis:true */
        var vm = this;
        vm.model={};
        vm.title = '添加用户';
        vm.isuserExist=false;
        vm.id = $state.params.id;
        if (vm.id) {
            vm.isUpdate = true;
            vm.title = '更新用户';
        }
        
        vm.create = function () {
        	userSvc.createUser(vm);
        };
        vm.update = function () {
        	userSvc.updateUser(vm);
        };      
        
        vm.initPassword=function(e){
        	var isChecked=$('#initPassword').is(":checked");
        	if(isChecked){
        		vm.model.password="888888";
            	vm.model.passwordConfirm="888888";
        	}else{
        		vm.model.password="";
            	vm.model.passwordConfirm="";
        	}
        	
        };

        activate();
        function activate() {
        	if (vm.isUpdate) {
        		userSvc.getUserById(vm);
            } else {
            	userSvc.initZtreeClient(vm);
            }
        }
    }
})();
