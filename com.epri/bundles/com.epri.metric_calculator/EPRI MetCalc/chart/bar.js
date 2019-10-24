function drawBar(param) {

	var myChart = echarts.init(document.getElementById(param.elementId));

	var padding = 30;
	var option = {
		grid: {
			x: padding,
			y: padding - 10,
			x2: padding - 20,
			y2: padding - 10,
		},

		tooltip: {
			trigger: 'axis',
			textStyle: {
				fontFamily: 'Verdana',
				fontSize: 12,
				align: 'left',
			},
			formatter: function (params) {
				return param.names[params[0].seriesIndex] + "(" + params[0].name + ")"
					+ '<br/>'
					+ params[0].seriesName
					+ ' : '
					+ params[0].value
					+ '<br/>' + params[1].seriesName
					+ ' : ' + (params[1].value)
					+ '<br/>' + params[2].seriesName
					+ ' : ' + (params[2].value);
			}
		},

		calculable: true,

		xAxis: [{
			type: 'category',
			data: param.ids,
		}],

		yAxis: [{
			type: 'value',
			min: 0,
			max: 10
		}],

		series: [
			{
				name: 'Value',
				type: 'bar',
				itemStyle: {
					normal: {
						color: COLORS.value,
						label: {
							show: true,
							position: 'insideTop'
						}
					}
				},
				data: param.value
			},

			{
				name : 'Reference Value',
				tooltip : {
					show : false
				},
				type: 'scatter',
				symbol: 'none',
				data: [0],
				markLine: {
					symbol: 'none',
					itemStyle: {
						normal: {
							color: COLORS.refValue,
							lineStyle : {
								type : 'solid',
								width : 3
							}
						}
					},
					data: param.referenceValueLines,
				},
				data : param.referenceValues
			},

			{
				name : 'Target Value',
				tooltip : {
					show : false
				},
				type: 'scatter',
				symbol: 'none',
				data: [0],
				markLine: {
					symbol: 'none',
					itemStyle: {
						normal: {
							color: COLORS.targetValue,
							lineStyle : {
								type : 'solid',
								width : 3
							}
						}
					},
					data: param.targetValueLines,
				},
				data : param.targetValues
			},

		]
	};

	myChart.setOption(option);

}
