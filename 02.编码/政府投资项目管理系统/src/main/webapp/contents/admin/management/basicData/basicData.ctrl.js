(function () {
    'use strict';

    angular
        .module('app')
        .controller('basicDataCtrl', basicData);

    basicData.$inject = ['$location','basicDataSvc','$state','$scope']; 

    function basicData($location, basicDataSvc,$state,$scope) {
        /* jshint validthis:true */
    	var vm = this; 
        vm.init=function(){  
        	vm.model={};
        	init_ztree();
        	
        }//end init
        
        function init_ztree(){
        	var basicData=common.getBasicData();
        	var zTreeObj;
			var setting = {
				callback:{
					onClick:function(e,id,node){
						$scope.$apply(function(){
							vm.model.id=node.id;
							vm.model.description=node.name;
							vm.model.identity=node.identity;
							vm.model.pId=node.pId;
							
						});
						
					}
				}
			};
			var  findChildren=function(pId){
				return $linq(basicData)
				.where(function(x){return x.pId==pId;})
				.select(function(x){return {id:x.id,name:x.description,pId:x.pId,identity:x.identity,children:findChildren(x.id)};})
				.toArray()
			}
			var zNodes = $linq(basicData)
				.where(function(x){return x.pId=="";})
				.select(
					function(x) {
						return {
							id : x.id,
							name : x.description,
							pId:x.pId,
							identity:x.identity,
							children:findChildren(x.id)
						};
					}).toArray();
			var rootNode = {
					id : '',
					name : '基础数据',
					children : zNodes
				};
			zTreeObj = $.fn.zTree.init($("#zTree"), setting,
					rootNode);
			console.log(zNodes);
        }
        
        
        activate();
        function activate() {
        	vm.init();
        }
    }
})();
