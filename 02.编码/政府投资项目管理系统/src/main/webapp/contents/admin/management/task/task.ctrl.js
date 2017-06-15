(function () {
    'use strict';

    angular
        .module('app')
        .controller('taskCtrl', task);

    task.$inject = ['$location','taskSvc','$state','$scope']; 

    function task($location, taskSvc,$state,$scope) {
        /* jshint validthis:true */
    	var vm = this;
    	vm.title = "";
    	vm.model={};
        vm.taskId=$state.params.taskId;        
        vm.relId=$state.params.relId;        
    	vm.page="todoList";
    	function init(){    		
    		if($state.current.name=='task_todo'){
    			vm.page='todoList'
    		}
    		if($state.current.name=='task_handle'){
    			vm.page='handle'
    		}
    		vm.formatDate=function(str){
    			return common.formatDate(str);
    		}
    		vm.getBasicDataDesc=function(str){
    			return common.getBasicDataDesc(str);
    		}
    	}
    	   	
    	activate();
        function activate() {        	
        	init(); 
        	if(vm.page=='todoList'){
        		init_todoList();
        	}
        	if(vm.page=='handle'){
        		init_handle();
        	}
        }
        function init_todoList(){
        	taskSvc.grid(vm);
        }//init_todoList
    	function init_handle(){
    	   vm.model.taskRecord={};
    	   vm.processState_qianShou=common.basicDataConfig().processState_qianShou;
    	   taskSvc.getTaskById(vm);
    	   taskSvc.getShenBaoInfoById(vm);
    	 
    	   vm.dialog_shenbaoInfo=function(){
    		   $('#shenbaoInfo').modal({
                   backdrop: 'static',
                   keyboard:false
               });
    		   //初始化tab
         	   vm.tabStripOptions={
         			//TODO
         	   };
         	   
    	   }//dialog
    	   
    	   //签收
    	   vm.handle=function(processState){
    		   console.log(processState)
    		   vm.model.taskRecord.processState=processState;
    		   taskSvc.handle(vm);
    	   }
    		
    	}//init_handle
    }
})();
