var thisProvinceCaseOutYoYStatistics;
   

$(document).ready(function () {
    initCtrls();
    bindEvents();
    loadData();
});

function resizeChart(statistics) {
    if (statistics && statistics.caseChart) {
        statistics.caseChart.resize();
    }
}

function onResize() {
    resizeChart(thisProvinceCaseOutYoYStatistics);
   
}

function initCtrls() {
    UICtrl.setSearchAreaToggle(undefined, false);
    var period = getPeriod();

    var options = {
        cboardCode: "cboardReport1", chartTitle: "输出CBorad's chart",
        amountKind: 'ThisAmount', additionalParams: { snw1: '省内', snw2: '省内' },
        afterBuildChart: buildThisProvinceCaseOutYoYTableView
    };
    thisProvinceCaseOutYoYStatistics = $("#chart1").caseStatistics(options);
  
}


function bindEvents() {
   

    $(window).resize(function () {
        onResize();
    });
}

function loadData() {
	loadThisProvinceCaseOutYoYData();
}

function getDateRange() {
    return { startDate: getStartDate(), endDate: getEndDate() };
}

function loadThisProvinceCaseOutYoYData() {
    this.thisProvinceCaseOutYoYStatistics.set(getDateRange());
    this.thisProvinceCaseOutYoYStatistics.loadYoY();//加载cboard定义的名称，获取数据，并执行回调：buildThisProvinceCaseOutYoYTableView
    resizeChart(thisProvinceCaseOutYoYStatistics);
}


function buildThisProvinceCaseOutYoYTableView(data, table) {
    var columnAndData = buildTableColumnAndData(data, 'this_qty');
    var tableDefinition = { columns: columnAndData.columns };
    this.buildTableViewForChart(columnAndData.tableData, tableDefinition, $('#chartData'));
}


function buildTableColumnAndData(data, nameField) {
    var thisPeriodFieldName = 'this_period_' + nameField;
    var lastPeriodFieldName = 'last_period_' + nameField;
    var columns = [{ field: 'year', title: '时间/单位', width: '100', valign: "middle", align: "center", sortable: false }];
    var thisYearAmount = [], lastYearAmount = [], yoyAmount = [];

    var period = getPeriod();

    thisYearAmount['year'] = period.thisPeriod;
    lastYearAmount['year'] = period.lastPeriod;
    yoyAmount['year'] = '同比';
    data.sort(function (o1, o2) {
        return o2[thisPeriodFieldName] - o1[thisPeriodFieldName];
    });
    $.each(data, function (i, o) {
        columns.push({ field: o.organ_name, title: o.organ_name, width: '100', valign: "middle", align: "center", sortable: false });
        thisYearAmount[o.organ_name] = o[thisPeriodFieldName];
        lastYearAmount[o.organ_name] = o[lastPeriodFieldName];
		if (o[lastPeriodFieldName]!=''&&o[lastPeriodFieldName]!=0){
        yoyAmount[o.organ_name] = (((o[thisPeriodFieldName] || 0) - (o[lastPeriodFieldName] || 0))/(o[lastPeriodFieldName] || 0)*100).toFixed(2);
		}
		else {
		yoyAmount[o.organ_name]=0;	
		}
    });
    var tableData = [thisYearAmount, lastYearAmount, yoyAmount];
    var columnAndData = { tableData: tableData, columns: columns };
    return columnAndData;
}

function query(obj) {
    loadData();
}

function resetForm(obj) {
    $(obj).formClean();
}