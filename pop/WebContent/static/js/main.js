/*下拉菜单封装 by weijx*/
(function(){
	$.fn.extend({
		ztreeComb:function(options){
			var defaults = {
				listwidth:187,
				listheight:300,
				lazyInit:false,
				treeData:[],
				useCheckbox:false,
				useSearchBar:true,
				defaultValue:"",
				hiddenName:"ztreeHidden",
				hiddenId:"ztreeHidden",
				onNodesCheck:function(e, treeId, treeNode){},
				onNodesClick:function(e, treeId, treeNode){},
				getsearchData:function(_txt,treeId){this.searchAjaxDone("ajaxData")},
				searchAjaxDone:function(ajaxdata){
					$.fn.zTree.init(ztreect.find("ul"), setting, ajaxdata);
				},
				selectClick:function(treeId,obj,setting){
				},
				useCombBtn:true,
				usebAddBtn:false,
				addBtnClick:function(){},
				selectedItems:[],
				selectedItemsName:'',
				staticSearchData:false,//搜索静态数据
				checkAllCick:function(treeId){}
			}
			var _this=$(this);
			var options = $.extend(defaults,options);
			var zTree ="";
			var ztreeBtn=$('<a class="ztreeCombBtn" href="javascript:void(0)">&nbsp;</a>');
			var ztreeHidden=$('<input type="hidden" name="'+options.hiddenName+'" id="'+options.hiddenId+'" value="'+options.selectedItems.join(',')+'"/>');
			var uuid=newGuid();
			var treeUuid="ztreeComb_"+uuid;
			var ztreect=$('<div class="menuContent" style="display:none; position: absolute; background:#fff; border:1px solid #ccc;"><ul id="'+treeUuid+'" class="ztree" style="margin-top:0; width:'+options.listwidth+'px; height: '+options.listheight+'px;"></ul></div>');
			var ztreeSearch=$('<div class="ztreeSearch"><input type="text" id="searchText'+uuid+'" class="searchBtn"/><input type="button" class="gosearch" value="搜索" /><input type="button" class="cleanUp" value="清除"/><input type="button" class="addBtn" value="添加" style="display:none;"/></div>');
			_this.attr("treeid",treeUuid);
			_this.attr("value", options.selectedItemsName);
			
			function beforeClick(treeId, treeNode) {
				if(options.useCheckbox==false){
					var check = (treeNode && !treeNode.isParent);
					if (!check) return false;
					return check;
				}else{
					zTree.checkNode(treeNode, !treeNode.checked, null, true);
					return false;
				}
			}
			function onCheck(e, treeId, treeNode) {
				if(options.useCheckbox==false) return;
				nodes = zTree.getCheckedNodes(true),
				v = "";
				var selectedName="";
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					selectedName+=nodes[i].id+",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if(selectedName.length >0) selectedName = selectedName.substring(0, selectedName.length-1)
				_this.attr("value", v);
				ztreeHidden.attr("value",selectedName);
				//
				options.onNodesCheck(e, treeId, treeNode);
			}
			function onClick(e, treeId, treeNode) {
				if(options.useCheckbox==true) return;
				nodes = zTree.getSelectedNodes(),
				v = "";
				var selectedName="";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
					selectedName+=nodes[i].id+",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				if(selectedName.length > 0) selectedName = selectedName.substring(0,selectedName.length-1);
				_this.attr("value", v);
				ztreeHidden.attr("value",selectedName);
				ztreect.fadeOut("fast");
				$("body").unbind("mousedown", onBodyDown);
				options.onNodesClick(e, treeId, treeNode);
			}
			function showMenu() {
				options.selectClick(treeUuid,ztreect.find("ul"),setting);
				$("body > .menuContent").hide();
				var cityOffset = _this.offset();
				ztreect.css({left:cityOffset.left + "px", top:cityOffset.top + _this.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
			}
			function hideMenu() {
				ztreect.fadeOut("fast");
				$("body").unbind("mousedown", onBodyDown);
			}
			function onBodyDown(event) {
				if (!($(event.target).hasClass("ztreeCombBtn") || event.target.id == _this.attr("id") || $(event.target).hasClass("menuContent") || $(event.target).parents(".menuContent").length>0)) {
					hideMenu();
				}
			}
			function nodeCreated(event, treeId, treeNode){
				for(i=0;i<options.selectedItems.length;i++){
					if(treeNode.id == options.selectedItems[i]){
						$.fn.zTree.getZTreeObj(treeId).checkNode(treeNode, true, true);
						break;
					}
				}
			}
			var setting = {
				check: {
					enable: options.useCheckbox,
					chkboxType: {"Y":"", "N":""}
				},
				view: {
					dblClickExpand: false
				},
				data: {
					key: {
						checked: "isChecked",
						id:"id"
					},
					simpleData: {
						//enable: true
					}
				},
				callback: {
					beforeClick: beforeClick,
					onClick: onClick,
					onCheck: onCheck,
					onNodeCreated:nodeCreated
				}
			};
			//生成唯一标识
			function newGuid(){ 
				var guid = ""; 
				for (var i = 1; i <= 32; i++){ 
					var n = Math.floor(Math.random()*16.0).toString(16); 
					guid += n; 
					if((i==8)||(i==12)||(i==16)||(i==20)) 
						guid += "-"; 
				} 
				return guid; 
			} 
			return this.each(function(){
				$("body").append(ztreect);
				$.fn.zTree.init(ztreect.find("ul"), setting, options.treeData);
				zTree= $.fn.zTree.getZTreeObj(treeUuid);
				ztreeBtn.click(function(){
					showMenu();
					return false;});
				_this.click(function(){showMenu();return false;})
				options.useCombBtn && _this.after(ztreeBtn);//将是否显示下拉按钮变为可选项
				_this.after(ztreeHidden);
				
				if(options.useCheckbox && !options.lazyInit){
					var checkAll = "<input type='checkbox' class='checkAll' /> ";
					$(ztreeSearch).html(checkAll+$(ztreeSearch).html());
				}
				
				
				/* 为树节点赋上默认值   begin  */
				if(options.defaultValue != '') {
					var defaultValueArr = options.defaultValue.split(",");
					var defaultDisplayValue = "";
					$.each(defaultValueArr, function (index, tx){
						if(options.useCheckbox) {
							zTree.checkNode(zTree.getNodeByParam("id",options.defaultValue, null));
						} else {
							zTree.selectNode(zTree.getNodeByParam("id",options.defaultValue, null)); 
						}
						defaultDisplayValue += zTree.getNodeByParam("id",options.defaultValue, null).name+",";
					});
					ztreeHidden.attr("value",options.defaultValue);
					if(defaultDisplayValue.length > 0){
						defaultDisplayValue = defaultDisplayValue.substring(0, defaultDisplayValue.length-1);
						_this.attr("value",defaultDisplayValue);
					}
				}
				
				/* 为树节点赋上默认值   end  */
				
				if(options.useSearchBar) {
					//搜索
					ztreect.prepend(ztreeSearch);
					ztreeSearch.find(".checkAll").click(function(){
						if($(this).attr("checked")){
							zTree.checkAllNodes(true);
						} else {
							zTree.checkAllNodes(false);
						}
						
						nodes = zTree.getCheckedNodes(true),
						v = "";
						var selectedName="";
						for (var i=0, l=nodes.length; i<l; i++) {
							v += nodes[i].name + ",";
							selectedName+=nodes[i].id+",";
						}
						if (v.length > 0 ) v = v.substring(0, v.length-1);
						if(selectedName.length >0) selectedName = selectedName.substring(0, selectedName.length-1)
						_this.attr("value", v);
						ztreeHidden.attr("value",selectedName);
						options.checkAllCick(treeUuid);
					});
					ztreeSearch.find(".gosearch").click(function(){
						//alert($(this).prev().val()=="");
						var _txt=$(this).prev().val();
						//不是静态搜索时销毁树，ajax新数据
						if(options.staticSearchData == false){
							zTree.destroy();
							$.fn.zTree.init(ztreect.find("ul"), setting, []);
						}
						var searchData=options.getsearchData(_txt,treeUuid);//searchzNodes;
						
					});
					ztreeSearch.find(".cleanUp").click(function(){
						_this.attr("value", "");
						ztreeHidden.attr("value","");
						var searchTxtId = 'searchText'+uuid;
						$('#'+searchTxtId).val('');
						zTree.checkAllNodes(false);
					});
					if(options.useAddBtn){
						ztreeSearch.find(".addBtn").show().click(options.addBtnClick);
					}
				}
			})
		}
	})
})($);
var searchzNodes =[
	{id:"test1", name:"搜索返回数据", open:true,
		children: [
			{id:"test2", name:"属性1",
				children: [
					{id:"test01", name:"符号1",isChecked:true}
				]
			}
		]
	}
];
//备份window自带的alert
_alert=window.alert;
//定义新alert
/*
 * msg:消息
 * f:消息滞留时间
 * */
window.alert=function(msg,f,w) {
	/*w=w?w:window;
	//向上逐级递归寻找到含有#main-content的top页面；不用window.top.alert是因为嵌套在其它项目里时，top!=mcd index页面；
	if($(w.document).find("#main-content").length>0 && $(w.document).find(".home_sidebar").length>0){
		w.top_alert(msg,f);
	}else{
		w=w.parent;
		alert(msg,f,w);
	}*/
	var st = "<div title='消息'>"+msg+"</div>"
    var d = $(st).dialog();
	setTimeout(function(){$(d).dialog("close")},3000);
};


(function(){
	//页面记载完成后执行
	$(function(){
		//帮助提示插件初始化 add by lixq8 20150430
		$('input[data-helptip],textarea[data-helptip]').each(function(){
			var $this = $(this);
			$('<i class="icon-question-sign"></i>').appendTo($this.parent()).tooltip({title:$this.attr('data-helptip'),placement:"right"});
		});
	});
})();
