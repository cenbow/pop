
 $(document).ready(function() { 
	/**
	*(暂时先不支持根据位置动态插入和更新时段)
	* 获取光标所在的字符位置
	* @param obj 要处理的控件, 支持文本域和输入框
	* @author hotleave
	*/
	function getPosition(obj){
		//alert(obj.tagName);
		var result = 0;
		if(obj.selectionStart){ //非IE浏览器
		   result = obj.selectionStart;
		}else{ //IE
		   var rng;
		   if(obj.tagName == "TEXTAREA"){ //如果是文本域
		   	rng = event.srcElement.createTextRange();
		   	rng.moveToPoint(event.x,event.y);
		   }else{ //输入框
		   	rng = document.selection.createRange();
		   }
		   rng.moveStart("character",-event.srcElement.value.length);
		   result = rng.text.length;
		}
		return result;
	}
	//			
	$('.timeRange').click(function(){
		var targetObj = $(this);
		var targetOffset = $(this).offset();
		//var defaultValue = $(this).attr("defaultValue"); html标签不能自己添加属性
		var curName = $(this).attr("name");
		var defaultValue = $("#default_"+curName).val();
		
		var defaultBeginHour = "09";
		var defaultBeginMin = "00";
		var defaultEndHour = "21";
		var defaultEndMin = "00";
		if(defaultValue != null && defaultValue!="undefine"){
			//defaultVaue 格式  09:00-21:00
			var defaultArr = defaultValue.split("-");
			if(defaultArr.length == 2){
				var beginArr = defaultArr[0].split(":");
				var endArr = defaultArr[1].split(":");
				defaultBeginHour = beginArr[0];
				defaultBeginMin = beginArr[1];
				defaultEndHour = endArr[0];
				defaultEndMin = endArr[1];
			}
		}
		
		//清除div，防止重复元素构造
		$('#timeRange_div').remove();
		$('#avoidTimeRange_div').remove();
		//构造元素
		
		var hourBeginOpts = "";
		var hourEndOpts = "";
		for(var i=0; i<=23; i++){
			var str;
			if(i<10){
				str = "0"+i;
			} else {
				str = ""+i;
			}
			if(str == defaultBeginHour){
				hourBeginOpts += '<option selected="selected">'+str+'</option>';
			} else {
				hourBeginOpts += '<option>'+str+'</option>';
			}
			if(str == defaultEndHour) {
				hourEndOpts += '<option selected="selected">'+str+'</option>';
			} else {
				hourEndOpts += '<option>'+str+'</option>';
			}
		}
				
		var minuteBeginOpts;
		var minuteEndOpts;
		for(var i=0; i<12; i++){
			var show = i*5;
			var str = show;
			if(show < 10) {
				str = "0"+show;
			}
			if(str == defaultBeginMin){
				minuteBeginOpts += '<option selected="selected">'+str+'</option>';
			} else {
				minuteBeginOpts += '<option>'+str+'</option>';
			}
			if(str == defaultEndMin) {
				minuteEndOpts += '<option selected="selected">'+str+'</option>';
			} else {
				minuteEndOpts += '<option>'+str+'</option>';
			}
		}		
		
		var html = $('<div id="timeRange_div" style="width:280px"><select id="timeRange_a" style="width:50px" class="commonSelect">'+hourBeginOpts+
			'</select>：<select id="timeRange_b" style="width:50px" class="commonSelect">'+minuteBeginOpts+
			'</select>至<select id="timeRange_c" style="width:50px" class="commonSelect">'+hourEndOpts+
			'</select>：<select id="timeRange_d" style="width:50px" class="commonSelect">'+minuteEndOpts+
			'</select><br/><br/><input type="button" class="btn btn-primary btn-small" value="确定" id="timeRange_btn" />&nbsp;&nbsp;<input type="button" class="btn btn-primary btn-small" value="清除" id="timeRange_clear" /></div>')
			.css({
				"position": "absolute",
				"z-index": "999",
				"padding": "5px",
				"border": "1px solid #AAA",
				"background-color": "#FFF",
				"box-shadow": "1px 1px 3px rgba(0,0,0,.4)"
			})
			.click(function(){return false;});
		html.css({left:"100px", top:"30px"}).slideDown("fast");
		// 获取文本框值
		var v = $(this).val();
		
		//获取已选时段的最后时间计算值
		function lastTimeValue(str_v){
			var result = 0;
			if(str_v){
				var periodArray = v.split(',');
				var lastPeriod = periodArray[periodArray.length-1];
				var timeArray = lastPeriod.split(/:|-/);
				result = parseInt(timeArray[2])*60+parseInt(timeArray[3]);
			}
			return result;
		}
		// 开始时间小于结束时间校验
		function endGEbeginCheck(){
			if(parseInt($('#timeRange_a').val(),10) * 60 + parseInt($('#timeRange_b').val(),10) >= parseInt($('#timeRange_c').val(),10) * 60 + parseInt($('#timeRange_d').val(),10)){
				alert("开始时间不能小于结束时间");
				return false;
			}
			return true;
		}
		//开始时间大于已选最后结束时间校验
		function beginGTlast(last){
			if(parseInt($('#timeRange_a').val(),10) * 60 + parseInt($('#timeRange_b').val(),10) < last){
				alert("开始时间不能大于策略场景的结束时间");
				return false;
			}
			return true;
		}
		
		// 绑定事件
		
		html.find('#timeRange_a').change(function(){
			beginGTlast(lastTimeValue(v));
		});
		html.find('#timeRange_b').change(function(){
			beginGTlast(lastTimeValue(v));
		});
		

		// 点击确定的时候
		var pObj = $(this);
		html.find('#timeRange_btn').click(function(){
			if(endGEbeginCheck()){
				var str = html.find('#timeRange_a').val()+':'
				+html.find('#timeRange_b').val()+'-'
				+html.find('#timeRange_c').val()+':'
				+html.find('#timeRange_d').val();
			  //判断是否是免打扰(多时段)
				if("avoidTimeRangeTmp" == pObj.attr("name")){
					var tplData = {tplDate:str};		
					$('#templateAvoidRange').tmpl(tplData).appendTo('#showAvoidRange');
					addedAvoidArray.push(str);
					$(":input[name='avoid_ranges']").val(addedAvoidArray.join(','));
				}else if("timeRangeTmp" == pObj.attr("name")){
					var tplData = {tplDate:str};		
					$('#templateTimeRange').tmpl(tplData).appendTo('#showTimeRange');
					addedAvoidArray.push(str);
					$(":input[name='time_ranges']").val(addedAvoidArray.join(','));
				}else{
				   pObj.val(str);
				}
				/*
				if(v){
					pObj.val(v + "," +str);
				}else{
					pObj.val(v +str);
				}
				*/	
				$('#timeRange_div').remove();
			}
		});
		
		html.find('#timeRange_clear').click(function(){
			pObj.val("");
			$('#timeRange_div').remove();
		});
		//弹出组件
		$(this).after(html);
		return false;
	});
	//取消组件显示并销毁构造元素
	$(document).click(function(){
		$('#timeRange_div').remove();
	});
	//
});