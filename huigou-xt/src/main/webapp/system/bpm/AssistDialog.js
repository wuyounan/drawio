(function ($) {
	
	$.selectAssistCommon=function(params){
		return new assist(params);
	};
	
	function assist(params){
		this.bizId=params['bizId'];
		this.procUnitId=params['procUnitId'];
		this.chiefId=params['chiefId']||'0';
		this.minusData=[];
	};
	
	$.extend(assist.prototype,{
		initializeGrid:function(){
			var g=this,handlerGrid=$("div.handlerGrid",g.div).addClass('ui-component-wrap');
			Public.ajax(web_app.name + '/workflow/queryAssistHandler.ajax',{ bizId: g.bizId, procUnitId:g.procUnitId, chiefId:g.chiefId}, function (datas) {
				$.each(datas['Rows'],function(i,data){
					g.selectedData.push(data);
					handlerGrid.append(g.parseSelectedDataHtml(data));
				});
		    });
			//注册删除事件
			handlerGrid.on('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('i.icon-close')){
					var id=$clicked.parent().data('id');
					$clicked.parent().remove();
					g.deleteOneNode(id);
				}
			});
		},
		parseSelectedDataHtml:function(data){
			var g=this,iconUrl=OpmUtil.getOrgImgUrl(OrgKind.Psm,1);
			var html=['<a class="ui-component-item" href="javascript:void(0);" data-id="',data['handlerId'],'">'];
			html.push('<img class="icon-img" src="',iconUrl,'"/>');
			html.push('<span class="ui-component-text" title="',data['orgName']+'/'+data['deptName']+'/'+data['handlerName'],'">',data['handlerName'],'</span>');
			html.push('<span class="check-group">');
			html.push('<input type="checkbox" value="1" ',data['sendMessage']==1?'checked':'',' data-id="',data['handlerId'],'"/>&nbsp;','<i class="fa fa-paper-plane-o"></i>');
			html.push('</span>');
			html.push('<i class="icon-close"></i>');
			html.push('</a>');
			return html.join('');
		},
		addDataOneNode:function(data){
			var g=this,inputParams=g.inputParams;
		    if (inputParams.selectableOrgKinds.indexOf(data.orgKindId) == -1) {
		        return true;
		    }
		    var added = false;
		    for (var j = 0; j < g.selectedData.length; j++) {
		        if (g.selectedData[j].handlerId == data.id) {
		            added = true;
		            break;
		        }
		    }
		    if (!added) {
		        var org = $.extend({}, {
		            bizId: g.bizId,
		            procUnitId: g.procUnitId,
		            orgId: data.orgId,
		            orgName: data.orgName,
		            deptId: data.deptId,
		            deptName: data.deptName,
		            positionId: data.positionId,
		            positionName: data.positionName,
		            handlerName: data.name,
		            handlerId: data.id,
		            fullId: data.fullId,
		            fullName: data.fullName,
		            status: 0,
		            cooperationModelId: "assistant",
		            chiefId: 0,
		            assistantSequence: 0,
		            groupId:  1,
		            sequence: 1,
		            sendMessage: 1
		        });
		        g.selectedData[g.selectedData.length] = org;
		        $("div.handlerGrid",g.div).append(g.parseSelectedDataHtml(org));
		    }
		    g.cancelSelect(data);
		    return true;
		},
		deleteData:function(){
			var g=this;
			UICtrl.confirm("common.confirm.clean.org", function () {
				$.each(g.selectedData,function(j,o){
					if(o.id){
						g.minusData.push(o.id);
					}
				});
				g.selectedData=[];//清空已选
				$("div.handlerGrid",g.div).empty();
			});
		},
		deleteOneNode:function(id){
			for (var j = 0; j < this.selectedData.length; j++) {
				 if (this.selectedData[j].handlerId == id) {
					 if (this.selectedData[j].id) {
						 this.minusData.push(this.selectedData[j].id);
					 }
					 this.selectedData.splice(j, 1);
			         break;
			     }
			}
		},
		getAssistData:function(){
			var g=this,detailData=g.selectedData;
		    var executors = [];
		    var sendMessages = [];
		    if (detailData.length > 0) {
			    for (var i = 0; i < detailData.length; i++) {
			        if (!detailData[i].id) {
			            executors.push(detailData[i].handlerId);
			            sendMessages.push(g.getSendMessageFlag(detailData[i].handlerId));
			        }
			    }
		    }else{
		    	if(!g.minusData.length){
		    		return false;
		    	}
		    }
		    var data = {};
		    data.deleted = Public.encodeJSONURI(g.minusData);
		    data.executors = Public.encodeJSONURI(executors);
		    data.sendMessages = Public.encodeJSONURI(sendMessages);
		    return data;
		}
	});
	
})(jQuery);