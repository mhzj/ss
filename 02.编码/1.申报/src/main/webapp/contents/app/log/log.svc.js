(function() {
	'use strict';

	angular.module('app').factory('logSvc', log);

	log.$inject = [ '$http','$compile' ];	
	function log($http,$compile) {	
		var url_log = "/log";
		var url_back = '#/log';
		var url_oprationLog = "/contents/app/log/data/oprationList.json";
			
		var service = {
			grid : grid			
		};		
		return service;	
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_log),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : true,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});
			var dataSource_opration = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_oprationLog),
				schema : common.kendoGridConfig().schema({
					id : "id",
					fields : {
						createdDate : {
							type : "date"
						}
					}
				}),
				serverPaging : true,
				serverSorting : true,
				serverFiltering : false,			
				pageSize: 10,
				sort : {
					field : "createdDate",
					dir : "desc"
				}
			});

			// End:dataSource

			// Begin:column
			var columns = [
					  {
						field : "id",
						title : "ID",
						width : 80,						
						filterable : false
					},{
						field : "level",
						title : "级别",
						width : 100,
						filterable : true
					} ,{
						field : "message",
						title : "日志内容",
						filterable : false
					},{
						field : "userId",
						title : "操作者",
						width : 150,
						filterable : true
					}, {
						field : "createdDate",
						title : "操作时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					}

			];
			var columns_opration = [
				{
					field : "id",
					title : "序号",
					width : 45,						
					filterable : false
				},{
					field : "describe",
					title : "描述",
					width : 200,
					filterable : true
				},{
					field : "userId",
					title : "操作者",
					width : 100,
					filterable : true
				}, 				{
					field : "userIP",
					title : "IP地址",
					width : 150,
					filterable : true
				},{
					field : "createdDate",
					title : "操作时间",
					width : 180,
					filterable : true,
					format : "{0:yyyy/MM/dd HH:mm:ss}"

				}				
			];
			// End:column
			
		
			vm.gridOptions={
					dataSource : common.gridDataSource(dataSource),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns,
					resizable: true
				};
			vm.gridOptions_opration={
					dataSource : common.gridDataSource(dataSource_opration),
					filterable : common.kendoGridConfig().filterable,
					pageable : common.kendoGridConfig().pageable,
					noRecords:common.kendoGridConfig().noRecordMessage,
					columns : columns_opration,
					resizable: true
				};
			
		}// end fun grid

		
		
		

	}
	
	
	
})();