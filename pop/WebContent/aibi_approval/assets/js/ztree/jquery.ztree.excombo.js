(function($) {
	/****私有方法区*****/
	//转换data-options参数
	/**
	 * @param target jquery对象
	 */
	var parseOptions = function(target) {
			var _options = {};
			var s = $.trim(target.attr('data-options'));
			if (s) {
				if (s.substring(0, 1) != '{') {
					s = '{' + s + '}';
				}
				_options = (new Function('return ' + s))();
			}
			return _options;
		}
		//生成唯一标识
	var newGuid = function() {
			var guid = "";
			for (var i = 1; i <= 32; i++) {
				var n = Math.floor(Math.random() * 16.0).toString(16);
				guid += n;
				if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
					guid += "-";
			}
			return guid;
		}
		//默认配置
	var defaults = {
		listwidth: 187,
		listheight: 300,
		lazyInit: false,
		treeData: [],
		useCheckbox: false,
		useSearchBar: true,
		defaultValue: "",
		onNodesCheck: function(e, treeId, treeNode) {},
		onNodesClick: function(e, treeId, treeNode) {},
		getsearchData: function(_txt, treeId) {
			this.searchAjaxDone("ajaxData")
		},
		searchAjaxDone: function(ajaxdata) {
			$.fn.zTree.init(ztreect.find("ul"), setting, ajaxdata);
		},
		selectClick: function(treeId, obj, setting) {},
		useCombBtn: true,
		usebAddBtn: false,
		addBtnClick: function() {},
		selectedItems: [],
		staticSearchData: false, //搜索静态数据
		checkAllCick: function(treeId) {},
		url: '',
		selectParentNode: false,
		editable: true,
		initValue:{text:'',value:''}
	};

	//方法集合
	var methods = {
		//初始化
		init: function() {
			return this.each(function(index, item) {
				var $this = $(item);
				var options = $.extend(true, {}, defaults, parseOptions($this));
				options.index = index;
				
				//获取元素id用新生成的input来代替 
				var id = $this.attr('id');
				var _html,newId;
				if(id == null){
					id = newGuid();
					newId = 'display_' + id;
					_html = $this.prop('outerHTML').replace($this.attr('name'), hiddenName).replace('>',' id="'+newId+'">');
				}else{
					newId = 'display_' + id;
					_html = $this.prop('outerHTML').replace(id,newId).replace($this.attr('name'), hiddenName);
				}
				//隐藏字段name属性 hiddenId
				//显示字段name属性 hiddenName
				var hiddenName = options.hiddenName?options.hiddenName:$this.attr('name');
				var hiddenId = options.hiddenId?options.hiddenId:id;
				var _this = $(_html);
				$this.replaceWith(_this);
				var ztreeBtn = $('<a class="ztreeCombBtn" href="javascript:void(0)">&nbsp;</a>');
				var ztreeHidden = $('<input type="hidden" name="' + hiddenId + '" id="'+id+'" value="' + options.selectedItems.join(',') + '"/>');
				var uuid = newGuid();
				var treeUuid = "ztreeComb_" + uuid;
				var ztreect = $('<div class="menuContent" style="display:none; position: absolute; background:#fff; border:1px solid #ccc;z-index:999;"><ul id="' + treeUuid + '" class="ztree" style="margin-top:0; width:' + options.listwidth + 'px; height: ' + options.listheight + 'px;"></ul></div>');
				var ztreeSearch = $('<div class="ztreeSearch"><input type="text" id="searchText' + uuid + '" class="searchBtn"/><input type="button" class="gosearch" value="搜索" /><input type="button" class="cleanUp" value="清除"/><input type="button" class="addBtn" value="添加" style="display:none;"/></div>');
				_this.attr("treeid", treeUuid);
				_this.attr("value", options.initValue.text);
				_this.attr('editable', options.editable)
				_this.wrap('<div class="ztree_container"></div>');
				_this.parent('.ztree_container').append(ztreect);
				ztreeHidden.val(options.initValue.value);
				var setting = {
					check: {
						enable: options.useCheckbox,
						chkboxType: {
							"Y": "",
							"N": ""
						}
					},
					view: {
						dblClickExpand: false
					},
					data: {
						key: {
							checked: "checked",
							name: 'text',
							title: 'qtip'
						},
						simpleData: {
							enable: true,
							idKey: 'id',
							pIdKey: 'pid'
						}
					},
					callback: {
						beforeClick: beforeClick,
						onClick: onClick,
						onCheck: onCheck,
						onNodeCreated: nodeCreated
					},
					async: {
						enable: true,
						type: "post",
						url: options.url,
						autoParam: ["id", "name"]
					}
				};
				var zTree = $.fn.zTree.init(ztreect.find("ul"), setting, options.treeData);
				window._tZTree = zTree;
				ztreeBtn.click(function() {
					if (_this.attr('editable') == 'false') return false;
					showMenu();
					return false;
				});
				_this.click(function() {
					if (_this.attr('editable') == 'false') return false;
					showMenu();
					return false;
				})
				options.useCombBtn && _this.after(ztreeBtn); //将是否显示下拉按钮变为可选项
				_this.after(ztreeHidden);

				if (options.useCheckbox && !options.lazyInit) {
					var checkAll = "<input type='checkbox' class='checkAll' /> ";
					$(ztreeSearch).html(checkAll + $(ztreeSearch).html());
				}

				/* 为树节点赋上默认值   begin  */
				if (options.defaultValue != '') {
					var defaultValueArr = options.defaultValue.split(",");
					var defaultDisplayValue = "";
					$.each(defaultValueArr, function(index, tx) {
						if (options.useCheckbox) {
							zTree.checkNode(zTree.getNodeByParam("id", options.defaultValue, null));
						} else {
							zTree.selectNode(zTree.getNodeByParam("id", options.defaultValue, null));
						}
						defaultDisplayValue += zTree.getNodeByParam("id", options.defaultValue, null).name + ",";
					});
					ztreeHidden.val(options.defaultValue);
					if (defaultDisplayValue.length > 0) {
						defaultDisplayValue = defaultDisplayValue.substring(0, defaultDisplayValue.length - 1);
						_this.val(defaultDisplayValue);
					}
				}

				/* 为树节点赋上默认值   end  */
				if (options.useSearchBar) {
					//搜索
					ztreect.prepend(ztreeSearch);
					ztreeSearch.find(".checkAll").click(function() {
						if ($(this).attr("checked")) {
							zTree.checkAllNodes(true);
						} else {
							zTree.checkAllNodes(false);
						}

						nodes = zTree.getCheckedNodes(true),
							v = "";
						var selectedName = "";
						for (var i = 0, l = nodes.length; i < l; i++) {
							v += nodes[i].name + ",";
							selectedName += nodes[i].id + ",";
						}
						if (v.length > 0) v = v.substring(0, v.length - 1);
						if (selectedName.length > 0) selectedName = selectedName.substring(0, selectedName.length - 1)
						_this.val(v);
						ztreeHidden.val(selectedName);
						options.checkAllCick(treeUuid);
					});
					ztreeSearch.find(".gosearch").click(function() {
						//alert($(this).prev().val()=="");
						var _txt = $(this).prev().val();
						//不是静态搜索时销毁树，ajax新数据
						if (options.staticSearchData == false) {
							zTree.destroy();
							var _setting = $.extend(true, {}, setting);
							if (_txt.length > 0) { //搜索内容不空，全部加载
								_setting.async.enable = false;
							} else {
								_setting.async.enable = true;
							}
							$.fn.zTree.init(ztreect.find("ul"), _setting, []);
						}
						//ajax请求后台搜索
						$.ajax({
							type: 'POST',
							url: options.url,
							data: {
								"searchText": _txt
							},
							dataType: "json",
							async: true,
							success: function(data) {
								var _tree = $.fn.zTree.getZTreeObj(treeUuid);
								_tree.addNodes(null, data, true);
								if (_txt.length > 0) { //搜索内容不空，全部加载
									_tree.expandAll(true);
								}
							}
						});
					});
					ztreeSearch.find(".cleanUp").click(function() {
						_this.val("").attr('value',"");
						ztreeHidden.val("").attr('value',"");
						var searchTxtId = 'searchText' + uuid;
						$('#' + searchTxtId).val('');
						zTree.checkAllNodes(false);
					});
					if (options.useAddBtn) {
						ztreeSearch.find(".addBtn").show().click(options.addBtnClick);
					}
				}

				function beforeClick(treeId, treeNode) {
					if (options.useCheckbox == false) {
						var check = null;
						if (options.selectParentNode && treeNode) {
							return true;
						} else {
							check = (treeNode && !treeNode.isParent);
						}
						if (!check) return false;
						return check;
					} else {
						zTree.checkNode(treeNode, !treeNode.checked, null, true);
						return false;
					}
				}

				function onCheck(e, treeId, treeNode) {
					if (options.useCheckbox == false) return;
					nodes = zTree.getCheckedNodes(true),
						v = "";
					var selectedName = "";
					for (var i = 0, l = nodes.length; i < l; i++) {
						v += (nodes[i].name || nodes[i].text) + ",";
						selectedName += nodes[i].id + ",";
					}
					if (v.length > 0) v = v.substring(0, v.length - 1);
					if (selectedName.length > 0) selectedName = selectedName.substring(0, selectedName.length - 1)
					_this.attr("value", v).attr('value',v);
					ztreeHidden.attr("value", selectedName).attr('value',selectedName);
					options.onNodesCheck(e, treeId, treeNode);
				}

				function onClick(e, treeId, treeNode) {
					if (options.useCheckbox == true) return;
					nodes = zTree.getSelectedNodes(),
						v = "";
					var selectedName = "";
					nodes.sort(function compare(a, b) {
						return a.id - b.id;
					});
					for (var i = 0, l = nodes.length; i < l; i++) {
						v += (nodes[i].name || nodes[i].text) + ",";
						selectedName += nodes[i].id + ",";
					}
					if (v.length > 0) v = v.substring(0, v.length - 1);
					if (selectedName.length > 0) selectedName = selectedName.substring(0, selectedName.length - 1);
					_this.val(v).attr('value',v);
					ztreeHidden.val(selectedName).attr('value',selectedName);
					ztreect.fadeOut("fast");
					$("body").unbind("mousedown", onBodyDown);
					options.onNodesClick(e, treeId, treeNode);
				}

				function showMenu() {
					options.selectClick(treeUuid, ztreect.find("ul"), setting);
					$("body > .menuContent").hide();
					var cityOffset = _this.offset();
					ztreect.css({
						left: "1px",
						top: "29px"
					}).slideDown("fast");
					$("body").bind("mousedown", onBodyDown);
				}

				function hideMenu() {
					ztreect.fadeOut("fast");
					$("body").unbind("mousedown", onBodyDown);
				}

				function onBodyDown(event) {
					if (!($(event.target).hasClass("ztreeCombBtn") || event.target.id == _this.attr("id") || $(event.target).hasClass("menuContent") || $(event.target).parents(".menuContent").length > 0)) {
						hideMenu();
					}
				}

				function nodeCreated(event, treeId, treeNode) {
					for (i = 0; i < options.selectedItems.length; i++) {
						if (treeNode.id == options.selectedItems[i]) {
							$.fn.zTree.getZTreeObj(treeId).checkNode(treeNode, true, true);
							break;
						}
					}
				}

				return _this;
			});
		},
		destory: function() {
			this.each(function(){
				var treeUuid = $(this).attr("treeid");
				$.fn.zTree.getZTreeObj(treeUuid).destroy();
			});
		},
		setValue: function(value) {
			return this.val(value);
		},
		getValue: function() {
			return this.val();
		},
		setText: function(txt) {
			$('#display_' + this.attr('id')).val(txt);
			return this;
		},
		getText: function() {
			return $('#display_' + this.attr('id')).val();
		},
		setEditable: function(flag) {
			if(flag){
				$('#display_' + this.attr('id')).attr('editable', flag).removeClass('disabled');
			}else{
				$('#display_' + this.attr('id')).attr('editable', flag).addClass('disabled');
			}
			return this;
		},
		getZTree:function()
		{
			return window._tZTree;
		}
	};

	$.fn.ztreeComb = function() {
		var method = arguments[0];
		if (methods[method]) {
			method = methods[method];
			arguments = Array.prototype.slice.call(arguments, 1);
		} else if (typeof(method) == 'object' || !method) {
			method = methods.init;
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.pluginName');
			return this;
		}
		return method.apply(this, arguments);
	}
})(jQuery);