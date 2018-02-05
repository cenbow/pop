function loadChart_F() {
	require([ 'echarts', 'echarts/chart/line' ], function(echarts) {
		var _p_chart_ = echarts.init($$('_p_home_page_chart_1_'));
		var option = org_chart_1_();
		_p_chart_.setOption(option, true);
	});
}

function loadChart_S(url, params, eid, callback) {
	send_xhr_(url, params, eid, callback);
}

function org_chart_2_(jsonObj) {
	var option = {
		tooltip : {
			trigger : 'axis',
			textStyle : {
				fontSize : 15,
				color : 'yellow'
			}
		},
		legend : {
			textStyle : {
				color : 'black',
				fontSize : 15
			},
			data : jsonObj.legend.data
		},
		xAxis : [ {
			axisLabel : {
				textStyle : {
					color : 'black',
					fontSize : 10
				},
				rotate : 45
			},
			splitLine : {
				show : false,
				lineStyle : {
					type : 'dashed'
				}
			},
			data : jsonObj.xAxis[0].data
		} ],
		yAxis : [ {
			type : 'value',
			name : jsonObj.yAxis[0].name,
			nameTextStyle : {
				color : 'black',
				fontSize : 15
			},
			axisLabel : {
				textStyle : {
					color : 'black',
					fontSize : 15
				}
			},
			splitLine : {
				show : true,
				lineStyle : {
					type : 'dashed'
				}
			}
		} ],
		color : [ '#abcdef' ],
		series : jsonObj.series
	};
	return option;
}

function org_chart_1_(callback) {
	var option = {
		tooltip : {
			trigger : 'axis',
			textStyle : {
				fontSize : 15,
				color : 'yellow'
			}
		},
		legend : {
			textStyle : {
				color : 'black',
				fontSize : 15
			},
			data : [ '发送客户数', '反馈客户数' ]
		},
		xAxis : [ {
			axisLabel : {
				textStyle : {
					color : 'black',
					fontSize : 15
				}
			},
			splitLine : {
				show : false,
				lineStyle : {
					type : 'dashed'
				}
			},
			data : [ '1', '2', '3', '4', '5', '6', '7' ]
		} ],
		yAxis : [ {
			type : 'value',
			name : '人(单位)',
			nameTextStyle : {
				color : 'black',
				fontSize : 15
			},
			axisLabel : {
				textStyle : {
					color : 'black',
					fontSize : 15
				}
			},
			splitLine : {
				show : true,
				lineStyle : {
					type : 'dashed'
				}
			}
		} ],
		series : [ {
			name : '发送客户数',
			type : 'line',
			data : [ '20', '49', '76', '34', '20', '49', '76' ]
		}, {
			name : '反馈客户数',
			type : 'line',
			data : [ '47', '100', '67', '1', '120', '419', '761' ]
		} ]
	};
	return option;
}

function _p_INIT_ECHART_ENGINE(url, params) {
	var _chart_eles = $('._p_echart_');
	for ( var _ele = 0; _ele < _chart_eles.length; _ele++) {
		var $this = $(_chart_eles[_ele]);
		// console.log('echarts初始化组件' + $this.attr('id') + ', '
		// + $this.attr('mat'));
		send_xhr_(url, params, $this.attr('id'), $this.attr('mat'));
	}
}

function send_xhr_(url, params, eid, callback) {
	$.ajax({
		type : 'POST',
		url : url,
		data : params,
		dataType : 'json',
		success : function(data) {
			// console.log('sendXmlHttpRequest result : ' +
			// JSON.stringify(data));
			// console.log('sendXmlHttpRequest-status : 200, url : ' + url);
			if (callback != undefined) {
				data = window[callback](data);
			}
			render_chart(eid, data);
		},
		error : function(data) {
			// console.log('error code : ' + data);
		}
	});
}

function render_chart(eid, option) {
	require([ 'echarts', 'echarts/chart/bar' ], function(echarts) {
		var _p_chart_ = echarts.init($$(eid));
		_p_chart_.setOption(option);
	});

}

function $$(eid) {
	return window.document.getElementById(eid);
}

function $$$(obj) {
	// console.log(JSON.stringify(obj));
}