var COMPARE_VALUE_THICK = 0.02;

function drawGauge(param) {
    var myChart = echarts.init(document.getElementById(param.elementId));

    var max = 0;
    var values = [param.value, param.referenceValue, param.targetValue];
    for (var i = 0; i < values.length; i++) {
        if (values[i] > max) {
            max = values[i];
        }
    }
    max = (parseInt(max / 100) + 1) * 100;

    var compareValues = [];
    var compareValueColors = [];
    if (param.referenceValue > param.targetValue) {
        compareValues[0] = param.targetValue;
        compareValueColors[0] = COLORS.targetValue;
        compareValues[1] = param.referenceValue;
        compareValueColors[1] = COLORS.refValue;
    } else {
        compareValues[0] = param.referenceValue;
        compareValueColors[0] = COLORS.refValue;
        compareValues[1] = param.targetValue;
        compareValueColors[1] = COLORS.targetValue;
    }

    var min = 0;
    var max = 10;

    var colors = [];
    console.log(Math.abs(compareValues[0] - compareValues[1])) / max;
    console.log( COMPARE_VALUE_THICK / 2);
    if (Math.abs(compareValues[0] - compareValues[1]) / max > COMPARE_VALUE_THICK) {
        colors = [
            [compareValues[0] / max - COMPARE_VALUE_THICK / 2, COLORS.value],
            [compareValues[0] / max + COMPARE_VALUE_THICK / 2, compareValueColors[0]],
            [compareValues[1] / max - COMPARE_VALUE_THICK / 2, COLORS.value],
            [compareValues[1] / max + COMPARE_VALUE_THICK / 2, compareValueColors[1]],
            [1, COLORS.value]
        ];
    } else {
        colors = [
            [compareValues[0] / max - COMPARE_VALUE_THICK / 2, COLORS.value],
            [compareValues[0] / max + COMPARE_VALUE_THICK / 2, compareValueColors[0]],
            [1, COLORS.value]
        ];
    }

    var option = {
        backgroundColor: '#fff',
        tooltip: {
            textStyle: {
                fontStyle: 'Verdana',
                fontSize: 12,
            },
            formatter: param.name + "(" + param.id + ")" + "<br>" + "Value : " + param.value + "<br>" + "Reference Value : " + param.referenceValue + "<br>" + "Target Value : " + param.targetValue,
        },
        series: [
            {
                name: param.name,
                type: 'gauge',
                min: min,
                max: max,
                splitNumber: 4,
                radius: '100%',
                axisLine: {
                    lineStyle: {
                        color: colors,
                        width: 8,
                    }
                },

                data: [{ value: param.value, name: param.name }],

                axisLabel: {
                    textStyle: {
                        fontWeight: 'bolder',
                        color: '#666',
                    }
                },
                axisTick: {
                    length: 20,
                    lineStyle: {
                        color: 'auto',
                    }
                },
                splitLine: {
                    length: 20,
                    lineStyle: {
                        width: 3,
                        color: '#888',
                    }
                },
                title: {
                    offsetCenter: [0, '0%'],
                    textStyle: {
                        fontWeight: 'bolder',
                        fontSize: 18,
                        color: '#333',
                    }
                },
                detail: {
                    offsetCenter: [0, '50%'],
                    textStyle: {
                        fontSize: 18,
                        fontWeight: 'bolder',
                        color: '#333'
                    }
                },
                pointer : {
                    color : COLORS.value,
                }
            },
        ]
    };

    myChart.setOption(option);
}