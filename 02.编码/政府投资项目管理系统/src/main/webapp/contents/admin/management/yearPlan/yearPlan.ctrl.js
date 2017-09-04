(function () {
    'use strict';

    angular
        .module('app')
        .controller('yearPlanCtrl', yearPlan);

    yearPlan.$inject = ['$location','yearPlanSvc','$state','$scope','$sce']; 

    function yearPlan($location, yearPlanSvc,$state,$scope,$sce) {
        /* jshint validthis:true */
    	var vm = this;    	
    	vm.model={};
    	vm.basicData={};
    	vm.title='申报信息编辑';
    	vm.search={};
        vm.id=$state.params.id;
        vm.investmentType=$state.params.projectInvestmentType;
        vm.stage=$state.params.stage;
    	vm.page="shenbaoInfoList";//默认为申报信息列表页面
        
    	function init(){
    		if($state.current.name=='yearPlan_shenbaoInfoEdit'){//申报信息新增页面
    			vm.page='shenbaoInfoAdd';
    		}
    		if($state.current.name=='yearPlan_shenbaoInfoEdit' && vm.id){//申报信息编辑页面
    			vm.page='shenbaoInfoEdit';
    		}
    		if($state.current.name=='yearPlan_planList'){
    			vm.page='planList';
    		}
    		if($state.current.name=='yearPlan_planEdit'){
    			vm.page='plan_create';
    		}
    		if($state.current.name=='yearPlan_planEdit'&&vm.id){
    			vm.page='plan_update';
    		}
    		if($state.current.name=='yearPlan_planBZ'){
    			vm.page='planBZ';
    		}
    		
    		vm.checkLength = function(obj,max,id){
      			 common.checkLength(obj,max,id);
           	};
           	
           	vm.getBasicDataDesc = function(str){
           		return common.getBasicDataDesc(str);
           	};
           	
           	vm.html = function(val){
           		return $sce.trustAsHtml(val);
           	};
           	
           	//编制列表全选框选择
        	$(document).on('click', '#checkboxAll_shenBaoList', function () {
                var isSelected = $(this).is(':checked');
                $('.yearPlanCapitalGrid').find('tr td:nth-child(1)').find('input:checkbox').prop('checked', isSelected);
            });
           	//条件查询、编辑--基础数据
           	vm.basicData.projectCategory=common.getBacicDataByIndectity(common.basicDataConfig().projectCategory);//项目类别	   		
	   		vm.basicData.projectConstrChar=common.getBacicDataByIndectity(common.basicDataConfig().projectConstrChar);//项目建设性质	
	   		vm.basicData.auditState=common.getBacicDataByIndectity(common.basicDataConfig().auditState);//审核状态
      	   	vm.basicData.projectStage = common.getBacicDataByIndectity(common.basicDataConfig().projectStage);//项目阶段 	   		
   	   		vm.basicData.projectType=common.getBacicDataByIndectity(common.basicDataConfig().projectType);//项目类	   			   			       		   		
   	   		vm.basicData.projectCategory=common.getBacicDataByIndectity(common.basicDataConfig().projectCategory);//项目类别	   		
   	   		vm.basicData.projectConstrChar=common.getBacicDataByIndectity(common.basicDataConfig().projectConstrChar);//项目建设性质	   			   		
   	   		vm.basicData.unitProperty=common.getBacicDataByIndectity(common.basicDataConfig().unitProperty);//单位性质
   	   		vm.basicData.projectIndustryAll=common.getBacicDataByIndectity(common.basicDataConfig().projectIndustry);//项目行业分类
   	   		vm.basicData.projectIndustry_ZF=$linq(common.getBasicData())
   	   			.where(function(x){return x.identity==common.basicDataConfig().projectIndustry&&x.pId==common.basicDataConfig().projectIndustry_ZF;})
   	   			.toArray();//政府投资项目行业
   	   		vm.basicData.area_Street=$linq(common.getBasicData())
   	   			.where(function(x){return x.identity==common.basicDataConfig().area&&x.pId==common.basicDataConfig().area_GM;})
   	   			.toArray(); //行政区划街道
   	   		vm.basicData.userUnit=common.getUserUnits();//建设单位信息
   	   		vm.basicData.roles=common.getRoles();//角色
   	   		vm.basicData.users=[];
   	   		
   	   		vm.searchIndustryChange=function(){
   	   			vm.searchIndustryIsZF = false;
   	   			vm.searchIndustryIsSH = false;
   	   			if(vm.searchIndustryFather == common.basicDataConfig().projectIndustry_ZF){
   	   				vm.searchIndustryIsZF = true;
   	   			}else if(vm.searchIndustryFather == common.basicDataConfig().projectIndustry_SH){
   	   				vm.searchIndustryIsSH = true;
   	   			}
   	   		};
   	   		
   	   		vm.searchRoleChange=function(){
   	   			vm.searchUser = false;
   	   			vm.basicData.users=vm.basicData.users.splice(0,vm.basicData.users.length);
   	   			if(vm.search.role != ''){
   	   				vm.search.roleJSON=eval('(' + vm.search.role + ')');//将json字符串转换为json对象
   					vm.basicData.users=vm.search.roleJSON.userDtos;//获取角色用户
   	   				vm.searchUser = true;
   	   			}
   	   		};
    	}
    	init();    	
    	activate();
        function activate() {        	
        	if(vm.page=='shenbaoInfoList'){
        		init_shenbaoInfoList();
        	}
        	if(vm.page=='shenbaoInfoAdd'){
        		vm.shenBaoInfoAdd = true;
        		init_shenbaoInfoEdit();
        		
        	}
        	if(vm.page=='shenbaoInfoEdit'){
        		vm.shenBaoInfoEdit = true;
        		init_shenbaoInfoEdit();
        	}
        	if(vm.page=='planList'){
        		init_planList();
        	}
        	if(vm.page=='plan_create'){
        		init_planCreate();
        	}
        	if(vm.page=='plan_update'){
        		vm.isPlanEdit=true;
        		init_planUpadte();
        	}
        	if(vm.page=='planBZ'){
        		init_planBZ();
        	}
        	
        	//全选框选择
        	$(document).on('click', '#checkboxAll_shenBaoList', function () {
                var isSelected = $(this).is(':checked');
                $('.yearPlanCapitalGrid').find('tr td:nth-child(1)').find('input:checkbox').prop('checked', isSelected);
            });
        }
        
    	function init_shenbaoInfoList(){
    		yearPlanSvc.grid_shenbaoInfoList(vm);
    		
    		//查询
    		vm.search=function(){
    			var filters = [];
				filters.push({field:'projectShenBaoStage',operator:'eq',value:common.basicDataConfig().projectShenBaoStage_nextYearPlan});//默认条件--申报阶段为下一年度计划
				filters.push({field:'processState',operator:'eq',value:common.basicDataConfig().processState_qianShou});//默认条件--申报信息的状态为签收状态   
				if(vm.search.projectName !=null && vm.search.projectName !=''){//查询条件--项目名称
	     			   filters.push({field:'projectName',operator:'contains',value:vm.search.projectName});
	     		   }
     		   if(vm.search.projectCategory !=null && vm.search.projectCategory !=''){//查询条件--项目类别
     			   filters.push({field:'projectCategory',operator:'eq',value:vm.search.projectCategory});
     		   }
     		   if(vm.search.planYear !=null && vm.search.planYear !=''){//查询条件--计划年度
     			  filters.push({field:'planYear',operator:'eq',value:parseInt(vm.search.planYear)});
     		   }
     		   if(vm.search.constructionUnit !=null && vm.search.constructionUnit !=''){//查询条件--建设单位名称
     			  filters.push({field:'constructionUnit',operator:'contains',value:vm.search.constructionUnit});
     		   }
     		   if(vm.search.auditState !=null && vm.search.auditState !=''){//查询条件--审核状态
     			  filters.push({field:'auditState',operator:'eq',value:vm.search.auditState});
     		   }
     		   if(vm.search.projectConstrChar !=null && vm.search.projectConstrChar !=''){//查询条件--建设性质
     			  filters.push({field:'projectConstrChar',operator:'eq',value:vm.search.projectConstrChar});
     		   }
     		   if(vm.search.projectIndustry !=null && vm.search.projectIndustry !=''){//查询条件--项目行业
     			  filters.push({field:'projectIndustry',operator:'eq',value:vm.search.projectIndustry});
     		   }
     		  if(vm.search.receiver !=null && vm.search.receiver !=''){//查询条件--签收人
     			  if(vm.search.receiver == "me"){
     				 filters.push({field:'receiver',operator:'eq',value:window.profile_userId});
     			  }else if(vm.search.receiver == "other"){
     				 filters.push({field:'receiver',operator:'ne',value:window.profile_userId});
     			  }else if(vm.search.receiver == "all"){}
     		   }
     		  vm.gridOptions.dataSource.filter(filters);
    		};
    		//清空查询条件
    		vm.filterClear=function(){
    			location.reload();
    		};
    		//申报详情模态框
    		vm.dialog_shenbaoInfo = function(id){
    			vm.id = id;
    			yearPlanSvc.getShenBaoInfoById(vm);
    			$('#shenbaoInfo').modal({
                    backdrop: 'static',
                    keyboard:false
                });
    			//初始化tab
          	   vm.tabStripOptions={
          			//TODO
          	   };
    		};
    		//列表退文按钮
    		vm.retreat = function(id){
    			common.confirm({
    				vm:vm,
    				msg:"确定需要退文吗？",
    				fn:function(){
    					$('.confirmDialog').modal('hide');
    					//退文信息收集模态框弹出
    					$("#shenbaoInfoTuiWen").modal({
    				        backdrop: 'static',
    				        keyboard:false
    				    });
    					vm.model.relId = id;
    	    			vm.model.processState = common.basicDataConfig().processState_tuiWen;
    					//退文信息收集模态框确认
    					vm.retreatSubmit=function(){
    						$("#shenbaoInfoTuiWen").modal('hide');
    						yearPlanSvc.updateShenBaoInfoState(vm);
    					};
    				}
    			});
    		};
    		//新增年度计划项目信息按钮
    		vm.addShenBaoInfo=function(){
    			var projectInvestmentType=common.basicDataConfig().projectInvestmentType_ZF;//默认为政府投资类型
    			var stage=common.basicDataConfig().projectShenBaoStage_nextYearPlan;//默认申报阶段为下一年度计划
    			//跳转到编辑页面
    			$location.path("/yearPlan/shenbaoInfoEdit//"+projectInvestmentType+"/"+stage);
    		};
    		
    	}//end#init_shenbaoInfoList
    	
    	function init_shenbaoInfoEdit(){
    		vm.auditState_auditPass=common.basicDataConfig().auditState_auditPass;//审核通过
    		vm.auditState_auditNotPass=common.basicDataConfig().auditState_auditNotPass;//审核未通过
    		//初始化页面
    		var init_page = function(){
	 		  vm.isYearPlan=vm.stage==common.basicDataConfig().projectShenBaoStage_nextYearPlan;//申报阶段为下一年度计划
	 		  if(vm.isYearPlan){
	 			 vm.isYearPlan = true;
   			   //初始化项目材料清单
   			   vm.materialsType=common.uploadFileTypeConfig().projectShenBaoStage_YearPlan;
   			   vm.uploadType=[['JYS','项目建议书'],['KXXYJBG','可行性研究报告'],['CBSJYGS','初步设计与概算']];
	 		  }
	 	
	 		//如果是新增下一年度计划信息--禁止点击Tab切换
	 		  if(vm.shenBaoInfoAdd){
	 			 vm.isShenBaoInfoAdd=true;//审核按钮不能点击
	 			 $("#tab1").attr("disabled","true");
		 		  $("#tab2").attr("disabled","true");
		 		  $("#tab3").attr("disabled","true");
		 		  $("#tab4").attr("disabled","true");
	 		  }
    		};
    		//初始化基础数据
    		var init_basicData = function(){
    			if(vm.investmentType == common.basicDataConfig().projectInvestmentType_ZF){//如果为政府投资
         		   vm.isZFInvestment = true; 
       			 	//基础数据--项目分类
         		   vm.basicData.projectClassify=$linq(common.getBasicData())
     	       		.where(function(x){return x.identity==common.basicDataConfig().projectClassify&&x.pId==common.basicDataConfig().projectClassify_ZF;})
     	       		.toArray();
         		   //基础数据--行业归口
         		   vm.basicData.projectIndustry=$linq(common.getBasicData())
     	       		.where(function(x){return x.identity==common.basicDataConfig().projectIndustry&&x.pId==common.basicDataConfig().projectIndustry_ZF;})
     	       		.toArray();
         	   }else if(vm.investmentType == common.basicDataConfig().projectInvestmentType_SH){//如果为社会投资
         		   vm.isSHInvestment = true;
      			   //基础数据--项目分类
         		   vm.basicData.projectClassify=$linq(common.getBasicData())
     	       		.where(function(x){return x.identity==common.basicDataConfig().projectClassify&&x.pId==common.basicDataConfig().projectClassify_SH;})
     	       		.toArray();
      			  //基础数据--行业归口
      			   vm.basicData.projectIndustry=$linq(common.getBasicData())
     	       		.where(function(x){return x.identity==common.basicDataConfig().projectIndustry&&x.pId==common.basicDataConfig().projectIndustry_SH;})
     	       		.toArray();
      			   vm.projectIndustryChange=function(){    		
 	    	       		vm.basicData.projectIndustryChildren=$linq(common.getBasicData())
 	    	       		.where(function(x){return x.identity==common.basicDataConfig().projectIndustry&&x.pId==vm.model.projectIndustryParent;})
 	    	       		.toArray();
      			   };
         	   }
    	};
    	
    	init_page();
    	init_basicData();
    	
    	yearPlanSvc.getShenBaoInfoById(vm);
    	
    	//项目所属单位发生变化
    	vm.unitNameChange=function(){
    		yearPlanSvc.getUserUnit(vm);
    	};
    	
   		//获取项目类型， 多选
   		vm.updateSelection = function(id){
   			if(vm.projectTypes.constructor == String){
   				vm.projectTypes=common.stringToArray(vm.projectTypes);
   			}
        	var index = vm.projectTypes.indexOf(id);
        	if(index == -1){
        		vm.projectTypes.push(id);
	       	}else{
	       		vm.projectTypes.splice(index,1);
	       	}	        	
        };
        
        //申报年份发生变化时触发
        vm.changeYear = function(){
  		   vm.planYear = parseInt(vm.model.shenBaoInfo.planYear);
  	    };
        
        //展示批复文件选择模态框
   		vm.choseDocument = function(e){
   			vm.pifuType=$(e.target).parents('.uploadBox').attr('data-type');
     	   $("#documentRecords").modal({
		        backdrop: 'static',
		        keyboard:false  			  
     	   });
     	   vm.grid_documentRecords.dataSource.read();//批复文件列表数据刷新
   		};
   		yearPlanSvc.documentRecordsGird(vm);//查询批复文件
 	   		
   		//批复文件选择模态框确认
   		vm.pifuChoseConfirm = function(){
   			//关闭模态框
   			$("#documentRecords").modal('hide');
   			$(".modal-backdrop").remove();
   			//获取选择框中的信息
   			var select = common.getKendoCheckId('.grid');
        	var fileName = select[0].value;
        	if(fileName){
        		var file = common.stringToArray(fileName,",");
        		var number = file[0];
        		var name = file[1];
        		var url =file[2];
        		vm.model.shenBaoInfo['pifu'+vm.pifuType+'_wenhao'] = number;
        		if(vm.model.shenBaoInfo.attachmentDtos){
     				  vm.model.shenBaoInfo.attachmentDtos.push({name:name,url:url,type:vm.pifuType});
     			 }else{
     				  vm.model.shenBaoInfo.attachmentDtos=[{name:name,url:url,type:vm.pifuType}];
     			 }
        	}
        };
     	  
   		//文件上传
 	   vm.uploadSuccess=function(e){
			var type=$(e.sender.element).parents('.uploadBox').attr('data-type');
           	 if(e.XMLHttpRequest.status==200){
           		 var fileName=e.XMLHttpRequest.response;
           		 $scope.$apply(function(){
           			 if(vm.model.shenBaoInfo.attachmentDtos){
           				 vm.model.shenBaoInfo.attachmentDtos.push({name:fileName.split('_')[2],url:fileName,type:type});
           			 }else{
           				 vm.model.shenBaoInfo.attachmentDtos=[{name:fileName.split('_')[2],url:fileName,type:type}];
           			 }                			           			
           		 });
           	 }
   		};
   		
   		vm.onSelect=function(e){
			$.each(e.files, function (index, value) {
	            if(value.size > common.basicDataConfig().uploadSize){
	            	$scope.$apply(function(){
		   				common.alert({
			        		vm : vm,
							msg : "上传文件过大！"
			            });               			           			
	          		 });
	            }
	            
	        });
    	};
		//批复文件上传配置
		vm.uploadOptions_pifu={
			async:{saveUrl:'/common/save',removeUrl:'/common/remove',autoUpload:true},
			error:vm.uploadSuccess,	   				
			localization:{select:'上传文件'},
			showFileList:false,
			multiple:false,
			validation: {
                maxFileSize: common.basicDataConfig().uploadSize
            },
            select:vm.onSelect
		};
		//相关附件上传配置
		vm.uploadOptions={
			async:{saveUrl:'/common/save',removeUrl:'/common/remove',autoUpload:true},
			error:vm.uploadSuccess,	   				
			localization:{select:'上传文件'},
			showFileList:false,
			multiple:true,
			validation: {
                maxFileSize: common.basicDataConfig().uploadSize
            },
            select:vm.onSelect
		};
    		
     	   //删除上传文件
   		 vm.delFile=function(idx){
   			var file = vm.model.shenBaoInfo.attachmentDtos[idx];
  			 if(file){//删除上传文件的同时删除批复文号
  				var pifuType = file.type;
  				vm.model.shenBaoInfo['pifu'+pifuType+'_wenhao'] = "";
  				vm.model.shenBaoInfo.attachmentDtos.splice(idx,1);
  			 }
   		 };
   		 	 
   		 //tab切换(上一步)
   		 vm.tabReturn = function(tabId){
     			var activeTab = $("#tab"+tabId);
 				vm.tabStrip.activateTab(activeTab);
      		};
   		
   		 //tab切换(下一步)
   		 vm.tabChange = function(tabId){
      			//验证表单
      			common.initJqValidation();
     			var isValid = $('form').valid();
     			var activeTab = $("#tab"+tabId);
     			if(isValid){//通过则跳转到下一页面
     				vm.tabStrip.activateTab(activeTab);
     			}
      		};
      	//添加建设单位
 		vm.addUnit=function(){
 			if(vm.constructionUnits.constructor == String){
 				vm.constructionUnits=common.stringToArray(vm.constructionUnits);
 			}
 			vm.constructionUnits.push('');
 			if(vm.constructionUnits.length >1){
				vm.canDelete = true;
			}
 		};
     	//删除建设单位
	   vm.deleteUnit=function(idx){
		   if(vm.canDelete){
			   if(vm.constructionUnits.constructor == String){
	 				vm.constructionUnits=common.stringToArray(vm.constructionUnits);
	 			}
			   vm.constructionUnits.splice(idx,1);
				if(vm.constructionUnits.length <=1){
					vm.canDelete = false;
				}
			}
	   };
      	//项目纳入项目库
  		vm.addProjectToLibray=function(){
  			yearPlanSvc.addProjectToLibrary(vm);
  		};
      	//更新项目信息
  		vm.updateProject=function(){
  			yearPlanSvc.updateProject(vm);
  		};
  		//确认更新
     	vm.update = function(){
     		vm.model.shenBaoInfo.auditState=common.basicDataConfig().auditState_noAudit;//后台修改保存申报信息之后默认为未审核状态
     		yearPlanSvc.updateShenBaoInfo(vm);
     	};
     	//更新审核状态
     	vm.updateAuditState=function(auditState){
     		vm.isAudit = true;//用于设置跳转到列表页面
     		vm.model.shenBaoInfo.auditState = auditState;
     		if(auditState == common.basicDataConfig().auditState_auditPass){//如果审核通过
     			vm.model.shenBaoInfo.isIncludLibrary = true;
     		}
     		yearPlanSvc.updateShenBaoInfo(vm);
     	};
     	//确认创建
     	vm.create=function(){
     		yearPlanSvc.createShenBaoInfo(vm);
     	};
     	
    }//end#init_shenbaoInfoEdit
    	
    	//init_planList
    	function init_planList(){
    		yearPlanSvc.grid_planList(vm);
    	}//init_planBZList    
    	
    	function init_planCreate(){
    		vm.create=function(){    		
    			yearPlanSvc.plan_create(vm);
    		};
    	}//init_planBZList 
    	
    	function init_planUpadte(){
    		yearPlanSvc.getPlanById(vm);
    		vm.update=function(){
    			yearPlanSvc.plan_update(vm);
    		};
    	}//init_planUpadte
    	
    	function init_planBZ(){   		
    		yearPlanSvc.grid_yearPlan_shenbaoInfoList(vm);//查询年度计划编制中的申报信息列表
    		yearPlanSvc.grid_yearPlan_addShenbaoInfoList(vm);//查询所有的可添加的申报信息列表 
    		
    		//添加项目计划弹出模态框
    		vm.dialog_addPlan=function(){
    			 $('#addPlanList').modal({
                     backdrop: 'static',
                     keyboard:false
                 });
    		};
    		//年度筛选
    		vm.search=function(){
    			var filters = [];
				filters.push({field:'projectShenBaoStage',operator:'eq',value:common.basicDataConfig().projectShenBaoStage_nextYearPlan});//默认条件--申报阶段为下一年度计划
				filters.push({field:'processState',operator:'eq',value:common.basicDataConfig().processState_qianShou});//默认条件--申报信息的状态为签收状态   
				if(vm.search.projectName !=null && vm.search.projectName !=''){//查询条件--项目名称
	     			   filters.push({field:'projectName',operator:'contains',value:vm.search.projectName});
	     		   }
     		   if(vm.search.projectIndustry !=null && vm.search.projectIndustry !=''){//查询条件--项目行业
     			   filters.push({field:'projectIndustry',operator:'eq',value:vm.search.projectIndustry});
     		   }
     		   if(vm.search.planYear !=null && vm.search.planYear !=''){//查询条件--计划年度
     			  filters.push({field:'planYear',operator:'eq',value:parseInt(vm.search.planYear)});
     		   }
     		   if(vm.search.constructionUnit !=null && vm.search.constructionUnit !=''){//查询条件--建设单位名称
     			  filters.push({field:'constructionUnit',operator:'contains',value:vm.search.constructionUnit});
     		   }
     		   if(vm.search.projectConstrChar !=null && vm.search.projectConstrChar !=''){//查询条件--建设性质
     			  filters.push({field:'projectConstrChar',operator:'eq',value:vm.search.projectConstrChar});
     		   }
     		  vm.addPlanGridOptions.dataSource.filter(filters);
    		};
    		//清空筛选条件
    		vm.filterClear=function(){
    			//清空人员设置的过滤条件
    			vm.search.projectName = '';
    			vm.search.projectIndustry = '';
    			vm.search.planYear = '';
    			vm.search.constructionUnit = '';
    			vm.search.projectConstrChar = '';
    			//设置列表过滤项为默认的
    			var filters = [];
				filters.push({field:'projectShenBaoStage',operator:'eq',value:common.basicDataConfig().projectShenBaoStage_nextYearPlan});//默认条件--申报阶段为下一年度计划
				filters.push({field:'processState',operator:'eq',value:common.basicDataConfig().processState_qianShou});//默认条件--申报信息的状态为签收状态  
				vm.addPlanGridOptions.dataSource.filter(filters);
    		};
    		//模态框点击确认
    		vm.dialogConfirmSubmit=function(){
    			//获取选中的申报信息的id
    			var selectIds = common.getKendoCheckId('.grid');
                if (selectIds.length == 0) {
                	return;
                } else {
                	var ids=[];
                    for (var i = 0; i < selectIds.length; i++) {
                    	ids.push(selectIds[i].value);
    				}
                    var idStr=ids.join(',');                  
                    $('#addPlanList').modal('toggle');//关闭模态框
                    yearPlanSvc.addShenBaoInfoconfirm(vm,idStr);//添加申报信息到计划中                  
                }   
    		};
    		//编制计划中的申报信息
    		 vm.popOver=function(e,id){
    			 //根据申报信息id查询出年度计划编制
    			 vm.currentCapitalId=id;
    			 yearPlanSvc.getYearPlanCapitalById(vm,id);
    			 //编制信息输入显示
    	    	   vm.isPopOver=true;
    	    	   var minClick=$(document).height()-50-230;
    	    	   if(e.pageY>minClick){
    	    		   e.pageY=minClick;
    	    	   }
    	    	   vm.popStyle={    	    			  
    	    			   left:e.pageX+'px',
    	    			   top:e.pageY+'px'
    	    	   };  
    	       };//popOver
    	     //更新编制信息  
    		 vm.updateCapital = function(){
    			 yearPlanSvc.updateYearPlanCapital(vm);
    		 };
    		 //移除计划中的申报信息
    		 vm.removeYearPlanCapital=function(){
    	    		var selectIds = common.getKendoCheckId('.yearPlanCapitalGrid');
    	            if (selectIds.length == 0) {
    	            	common.alert({
    	                	vm:vm,
    	                	msg:'请选择数据'              	
    	                });
    	            } else {
    	            	var ids=[];
    	                for (var i = 0; i < selectIds.length; i++) {
    	                	ids.push(selectIds[i].value);
    					}
    	                var idStr=ids.join(',');  
    	                yearPlanSvc.removeYearPlanCapital(vm,idStr);
    	            }
    	    	};//removeYearPlanCapital   			
    	}//init_planBZ   	    	    	   	
    } //yearPlan
})();
