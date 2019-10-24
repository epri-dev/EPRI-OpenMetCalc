function drawLineChart(param) {
	var chart = new Chart(document.getElementById(param.elementId),
		{
			type: "line",
			data: {
				labels: param.labels,
				datasets: param.datasets,
			},

			options: {
				maintainAspectRatio: false,
				legend: {
					display: true,
				},
				tooltips: {
					callbacks: {
						label: function (tooltipItem, data) {
							var index = tooltipItem.datasetIndex;
							var label = data.datasets[index].label + "(" + param.ids[index] + ") : ";
							label += tooltipItem.yLabel;
							return label;
						}
					}
				},

				scales: {
					yAxes: [{
						display: true,
						ticks: {
							min: 0,
							max: 10,
							stepSize: 2
						}
					}]
				}
			},
		});
	chart.update();
}