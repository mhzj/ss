(function() {
	'use strict';

	angular.module('appSupervision').factory('orgSvc', org);

	org.$inject = [ '$http','$compile' ];	
	function org($http,$compile) {	
		var url_org = "/org";
		var url_back = '#/org';
		var url_user='/user';
			
		var service = {
			grid : grid,
			createOrg : createOrg,			
			getOrgById : getOrgById,
			updateOrg:updateOrg,
			deleteOrg:deleteOrg			
		};		
		return service;	
						
		function grid(vm) {
			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_org),
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

			// End:dataSource

			// Begin:column
			var columns = [
					{
						template : function(item) {
							return kendo
									.format(
											"<input type='checkbox'  relId='{0}' name='checkbox' class='checkbox' />",
											item.id);
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  {
						field : "orgIdentity",
						title : "部门标识",
						width : 200,						
						filterable : false
					}, {
						field : "name",
						title : "部门名称",
						width : 200,						
						filterable : false
					},{
						field : "comment",
						title : "描述",
						filterable : false
					}, {
						field : "createdDate",
						title : "创建时间",
						width : 180,
						filterable : false,
						format : "{0:yyyy/MM/dd HH:mm:ss}"

					},  {
						field : "",
						title : "操作",
						width : 280,
						template:function(item){							
							return common.format($('#columnBtns').html(),"vm.del('"+item.id+"')",item.id);
							
						}
						

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
			
		}// end fun grid

		function createOrg(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.isorgExist == false) {
				vm.isSubmit = true;
						               
				var httpOptions = {
					method : 'post',
					url : url_org,
					data : vm.model
				};

				var httpSuccess = function success(response) {									
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {							
							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');
									$('.modal-backdrop').remove();
									location.href = url_back;
								}
							});
						}						
					});
				};

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {				
//				common.alert({
//					vm:vm,
//					msg:"您填写的信息不正确,请核对后提交!"
//				})
			}
		}// end fun createorg

		

		function getOrgById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_org + "?$filter=id eq '{0}'", vm.id)
			};
			
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0] || {};
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			};
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun getorgById
		
		function updateOrg(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.isorgExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;// id
							               
				var httpOptions = {
					method : 'post',
					url : url_org+'/updateOrg',
					data : vm.model
				};

				var httpSuccess = function success(response) {					
					common.requestSuccess({
						vm:vm,
						response:response,
						fn:function() {							
							common.alert({
								vm:vm,
								msg:"操作成功",
								fn:function() {
									vm.isSubmit = false;
									$('.alertDialog').modal('hide');							
								}
							});
						}						
					});
				};

				common.http({
					vm:vm,
					$http:$http,
					httpOptions:httpOptions,
					success:httpSuccess
				});

			} else {
//				common.alert({
//				vm:vm,
//				msg:"您填写的信息不正确,请核对后提交!"
//			})
			}
		}// end fun updateorg
		
		function deleteOrg(vm,id) {
            vm.isSubmit = true;
            
            var httpOptions = {
                method: 'post',
                url:url_org+'/deleteOrg',
                data:id              
            };
            
            var httpSuccess = function success(response) {               
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }					
				});
            };
            
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deleteorg				
	}	
})();