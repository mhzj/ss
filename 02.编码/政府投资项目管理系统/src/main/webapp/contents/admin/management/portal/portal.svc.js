(function() {
	'use strict';

	angular.module('app').factory('portalSvc', portal);

	portal.$inject = [ '$http','$compile' ];	
	function portal($http,$compile) {	
		var url_portal = "/portal";
		var url_back = '#/portal';
		var url_user='/user';
			
		var service = {
			grid : grid,
			createportal : createportal,			
			getportalById : getportalById,
			updateportal:updateportal,
			deleteportal:deleteportal			
		};		
		return service;	
		
		
		
		function grid(vm) {

			// Begin:dataSource
			var dataSource = new kendo.data.DataSource({
				type : 'odata',
				transport : common.kendoGridConfig().transport(url_portal),
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
											item.id)
						},
						filterable : false,
						width : 40,
						title : "<input id='checkboxAll' type='checkbox'  class='checkbox'  />"
						
					},  {
						field : "portalIdentity",
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

		function createportal(vm) {
			common.initJqValidation();
			var isValid = $('form').valid();
			if (isValid && vm.isportalExist == false) {
				vm.isSubmit = true;
				
		
	               
				var httpOptions = {
					method : 'post',
					url : url_portal,
					data : vm.model
				}

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
							})
						}
						
					});

				}

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
		}// end fun createportal

		

		function getportalById(vm) {
			var httpOptions = {
				method : 'get',
				url : common.format(url_portal + "?$filter=id eq '{0}'", vm.id)
			}
			var httpSuccess = function success(response) {
				vm.model = response.data.value[0];
				if (vm.isUpdate) {
					//initZtreeClient(vm);
				}
			}
			
			common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
		}// end fun getportalById
		
		function updateportal(vm){
			common.initJqValidation();			
			var isValid = $('form').valid();
			if (isValid && vm.isportalExist == false) {
				vm.isSubmit = true;
				vm.model.id=vm.id;// id
				
			
               
				var httpOptions = {
					method : 'put',
					url : url_portal,
					data : vm.model
				}

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
							})
						}
						
					})
				}

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
		}// end fun updateportal
		
		function deleteportal(vm,id) {
            vm.isSubmit = true;
            var httpOptions = {
                method: 'delete',
                url:url_portal,
                data:id
                
            }
            var httpSuccess = function success(response) {
                
                common.requestSuccess({
					vm:vm,
					response:response,
					fn:function () {
	                    vm.isSubmit = false;
	                    vm.gridOptions.dataSource.read();
	                }
					
				});

            }
            common.http({
				vm:vm,
				$http:$http,
				httpOptions:httpOptions,
				success:httpSuccess
			});
        }// end fun deleteportal
		
		
		
		

	}
	
	
	
})();