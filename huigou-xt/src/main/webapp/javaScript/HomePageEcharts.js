$(function() {
    var e = echarts.init(document.getElementById("echarts-line-chart")),
    a = {
        title: {
            text: "未来一周气温变化"
        },
        tooltip: {
            trigger: "axis"
        },
        legend: {
            data: ["最高气温", "最低气温"]
        },
        grid: {
            x: 40,
            x2: 40,
            y2: 24
        },
        calculable: !0,
        xAxis: [{
            type: "category",
            boundaryGap: !1,
            data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"]
        }],
        yAxis: [{
            type: "value",
            axisLabel: {
                formatter: "{value} °C"
            }
        }],
        series: [{
            name: "最高气温",
            type: "line",
            data: [11, 11, 15, 13, 12, 13, 10],
            markPoint: {
                data: [{
                    type: "max",
                    name: "最大值"
                },
                {
                    type: "min",
                    name: "最小值"
                }]
            },
            markLine: {
                data: [{
                    type: "average",
                    name: "平均值"
                }]
            }
        },
        {
            name: "最低气温",
            type: "line",
            data: [1, -2, 2, 5, 3, 2, 0],
            markPoint: {
                data: [{
                    name: "周最低",
                    value: -2,
                    xAxis: 1,
                    yAxis: -1.5
                }]
            },
            markLine: {
                data: [{
                    type: "average",
                    name: "平均值"
                }]
            }
        }]
    };
    e.setOption(a);
    
    var t = echarts.init(document.getElementById("echarts-bar-chart")),
    n = {
        title: {
            text: "蒸发量和降水量"
        },
        tooltip: {
            trigger: "axis"
        },
        legend: {
            data: ["蒸发量", "降水量"]
        },
        grid: {
            x: 30,
            x2: 40,
            y2: 24
        },
        calculable: !0,
        xAxis: [{
            type: "category",
            data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
        }],
        yAxis: [{
            type: "value"
        }],
        series: [{
            name: "蒸发量",
            type: "bar",
            data: [2, 4.9, 7, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20, 6.4, 3.3],
            markPoint: {
                data: [{
                    type: "max",
                    name: "最大值"
                },
                {
                    type: "min",
                    name: "最小值"
                }]
            },
            markLine: {
                data: [{
                    type: "average",
                    name: "平均值"
                }]
            }
        },
        {
            name: "降水量",
            type: "bar",
            data: [2.6, 5.9, 9, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6, 2.3],
            markPoint: {
                data: [{
                    name: "年最高",
                    value: 182.2,
                    xAxis: 7,
                    yAxis: 183,
                    symbolSize: 18
                },
                {
                    name: "年最低",
                    value: 2.3,
                    xAxis: 11,
                    yAxis: 3
                }]
            },
            markLine: {
                data: [{
                    type: "average",
                    name: "平均值"
                }]
            }
        }]
    };
    t.setOption(n);
    $(window).resize(function(){
    	e.resize();
    	t.resize();
    });
    setTimeout(function(){
    	e.resize();
        t.resize();
    },10);
});