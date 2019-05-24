var treeManager, gridManager, parentId, orgKindId;

$(function () {
	initializateUI();
    bindEvents();
    loadOrgTreeView();
    initializeGrid();
    

    function initializateUI() {
    	UICtrl.initDefaultLayout();  
      
    }

    function bindEvents() {
        $("#btnQuery").click(function () {
            reloadGrid2();
        });

        $("#btnReset").click(function () {
            $(this.form).formClean();
        });

        $("#showDisabledOrg").click(function () {
            parentId = "orgRoot";
             $('#maintree').commonTree("refresh");
            reloadGrid2();
        });     
        
    }

    function initializeGrid() {
        var imageFilePath = web_app.name + '/themes/default/images/icons/';

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },	
                { display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "性别", name: "sex", width: 100, minWidth: 60, type: "string", align: "left",
                	render: function(item){
                		return item.sex == "1" ? "男" : item.sex == "0" ? "女" : ""; 
                	}},
                { display: "email", name: "email", width: 180, minWidth: 60, type: "string", align: "left" },
                { display: "移动电话", name: "mobilePhone", width: 140, minWidth: 60, type: "string", align: "left" },
                { display: "办公电话", name: "officePhone", width: 140, minWidth: 60, type: "string", align: "left" }
            ],
            dataAction: "server",
            url: web_app.name + "/org/slicedQueryPerson.ajax",
            parms: { parentId: "orgRoot" },
            sortName: 'fullSequence',
            sortOrder: 'asc',
            width: "99.8%",
            height: "100%",
            heightDiff: -8,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true
        });
        UICtrl.setSearchAreaToggle(gridManager, false);
    }
});

function isShowDisabledOrg(){
	return $("#showDisabledOrg").is(":checked") ? 1 : 0;
}

function isShowAllChildrenOrg(){
	return $("#showAllChildrenOrg").is(":checked") ? 1 : 0;
}

function loadOrgTreeView(){
	$('#maintree').commonTree({
        loadTreesAction: '/org/queryOrgs.ajax',
        parentId: 'orgRoot',
        getParam: function (e) {
            if (e) {
            	var params = {};
            	params.showDisabledOrg = isShowDisabledOrg();
            	params.displayableOrgKinds = "ogn,fld,prj,dpt";
            	params.showVirtualOrg =  1;
            	params.showPosition = 1;
                return  params;
            }
            return { showDisabledOrg: 0 };
        },
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        onClick: function (data) {
                if (!data) {
                    return;
                }

                parentId = data.id;
                
                if (isShowAllChildrenOrg() == 1){
                	gridManager.options.parms.parentIdOrFullId = data.fullId;
                }else{
                	gridManager.options.parms.parentIdOrFullId = parentId;
                }

                if (!data.parentId) {
            		$('#layout').layout('setCenterTitle', "组织机构列表");
                } else {
                	$('#layout').layout('setCenterTitle',  "<font style=\"color:Tomato;font-size:13px;\">["
                            + data.name + "]</font>组织机构列表");
                }
                reloadGrid2();
            },
        IsShowMenu: false
    });
}

function reloadGrid() {
    reloadGrid2();
}

function reloadGrid2() {
    var params = $("#queryMainForm").formToJSON();    
    gridManager.options.parms.showAllChildren = isShowAllChildrenOrg();    
    UICtrl.gridSearch(gridManager, params);
}
