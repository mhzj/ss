(function () {
    'use strict';

    angular
        .module('app')
        .controller('prophaseWorkPlanCtrl', prophaseWorkPlan);

    prophaseWorkPlan.$inject = ['$location','prophaseWorkPlanSvc']; 

    function prophaseWorkPlan($location, prophaseWorkPlanSvc) {
        /* jshint validthis:true */
    	var vm = this;
        vm.title = '前期工作计划列表';
        
        //项目编辑页面的切换
        vm.tabActive=1;
        vm.tab=function(tabActive){
          vm.tabActive=tabActive;          
        }
        
        //前期工作计划的项目编辑
       /* vm.projectInfoEdit = function(id){
        	//跳转到项目编辑页面
        	location.href = "#//"+id;
        }*/
        
        //前期工作计划的项目删除
        vm.projectInfoDel = function(id){
        	common.confirm({
        		vm:vm,
        		title:"温馨提示",
        		msg:"确定删除这一条数据吗?",
        		fn:function(){
        			$('.confirmDialog').modal('hide');
        			//DOTO
        		}
        	});
        }
        
        activate();
        function activate() {
            prophaseWorkPlanSvc.grid(vm);
        }
    }
})();
