
$(document).ready(function () {
    initializeUI();
    loadOrgTreeView();
    initializeUseUI();//useBizClassificationDetail.js
});
function initializeUI() {
    UICtrl.layout("#layout");
}

function loadOrgTreeView() {
    $('#orgTree').commonTree({
        loadTreesAction: '/org/queryOrgs.ajax',
        parentId: '',
        getParam: function (e) {
            if (e) {
                return {showDisabledOrg: 0, displayableOrgKinds: "ogn,dpt"};
            }
            return {showDisabledOrg: 0};
        },
        isLeaf: function (data) {
            data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status, false);
        },
        onClick: function (data) {
        	//组织改变事件
        	onOrgIdChange(data.id);
        },
        IsShowMenu: false
    });
}