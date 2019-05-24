$(function() {
	var kindId = null;
	getQueryParameters();
	loadFolderTree({ kindId : kindId,  IsShowMenu : false });

	function getQueryParameters() {
		kindId = Public.getQueryStringByName("kindId");
	}
});

function getSelectedTreeNodeData() {
	var node = folderTreeManager.getSelected();
	if (!node) {
		Public.tip('请选择文件夹!');
		return;
	}
	if (!node.data.parentId) {
		Public.tip('不能选择根节点!');
		return;
	}
	return node.data;
}