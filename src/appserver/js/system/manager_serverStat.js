
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../Images/ext/s.gif';
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var record = new Ext.data.Record.create([
	  {name:'server',	mapping:'server'},
	  {name:'flag',		mapping:'flag'}
	]);
	var proxy = new Ext.data.HttpProxy({
		url:"../../ServerStatAction_execute.action"
	});
	var reader = new Ext.data.JsonReader({
		totalProperty:"total",
		root:"rows"
	},record);
	var store = new Ext.data.Store({
		proxy : proxy,
		reader : reader
	});
	store.load();
	var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        boxM,
        rowNumber,
        {header:"服务",		dataIndex:"server",	align:'center'},
        {header:'操作标记',	dataIndex:'flag',	align:'center',		renderer:showURL_flag}

    ]);

    colM.defaultSortable = true;
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.server_stat.info',
        animCollapse:true,
        hieght:setHeight(),
        width:setWidth(),
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:2,
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop: true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        }
    });
	new Ext.Viewport({
    	layout :'fit',
    	renderTo:Ext.getBody(),
    	items:[grid_panel]
    });
	function showURL_flag(value){
		if(value == 'open'){
			return "<a href='javascript:;' onclick='server_close();'>关闭服务</a>";
		}else if(value == 'close'){
			return "<a href='javascript:;' onclick='server_open();'>启动服务</a>";
		}else {
			return value;
		}
	}
}); // / Ext onReady end!
function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function server_close(){
	var grid = Ext.getCmp('grid.server_stat.info');
	var store = Ext.getCmp('grid.server_stat.info').getStore();
	var selModel = grid.getSelectionModel();
	var server;
	if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
        	server = item.data.server;
        });
    }
    Ext.MessageBox.show({
    	title:'信息',
    	msg:'<font color="green">确定要关闭'+server+'？</font>',
    	width:250,
    	buttons:Ext.Msg.YESNO,
    	buttons:{'ok':'确定','no':'取消'},
    	icon:Ext.MessageBox.INFO,
    	closable:false,
    	fn:function(e){
    		if(e == 'ok'){
    			var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg: '正在关闭'+server+',请稍后...',
					removeMask: true 
				});
				myMask.show();
    			Ext.Ajax.request({
    				url : '../../ServerDoneAction_execute.action',
    				params :{server : server.split('服务')[0],type:'close'},
    				loadMask:{msg:'正在处理数据，请稍后...'},
    				success : function(action){
    					myMask.hide();
    					var json = Ext.decode(action.responseText);
    					Ext.MessageBox.show({
    						title:'信息',
    						width:250,
    						msg:json.msg,
    						buttons:Ext.MessageBox.OK,
    						buttons:{'ok':'确定'},
    						icon:Ext.MessageBox.INFO,
    						closable:false,
    						fn:function(e){
    							if(e=='ok'){
    								grid.render();
    								store.reload();
    							}
    						}
    					});
    				},
    				failure : function(){
    					myMask.hide();
    					Ext.MessageBox.show({
    						title:'信息',
    						width:250,
    						msg:'请与后台服务人员联系!',
    						buttons:Ext.MessageBox.OK,
    						buttons:{'ok':'确定'},
    						icon:Ext.MessageBox.ERROR,
    						closable:false
    					});
    				}
    			});
    		}
    	}
    });
}

function server_open(){
	var grid = Ext.getCmp('grid.server_stat.info');
	var store = Ext.getCmp('grid.server_stat.info').getStore();
	var selModel = grid.getSelectionModel();
	var server;
	if(selModel.hasSelection()){
		var selections = selModel.getSelections();
		Ext.each(selections,function(item){
			server = item.data.server;
		});
	}
	Ext.MessageBox.show({
		title:'信息',
		msg:'<font color="green">确定要打开'+server+'？</font>',
		width:250,
		buttons:Ext.Msg.YESNO,
		buttons:{'ok':'确定','no':'取消'},
		icon:Ext.MessageBox.INFO,
		closable:false,
		fn:function(e){
			if(e == 'ok'){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg: '正在打开'+server+',请稍后...',
					removeMask: true //完成后移除
				});
				myMask.show();
				Ext.Ajax.request({
					url : '../../ServerDoneAction_execute.action',
					params :{server : server.split('服务')[0],type:'open'},
					loadMask:{msg:'正在处理数据，请稍后...'},
					success : function(action){
						myMask.hide();
						var json = Ext.decode(action.responseText);
						Ext.MessageBox.show({
							title:'信息',
							width:250,
							msg:json.msg,
							buttons:Ext.MessageBox.OK,
							buttons:{'ok':'确定'},
							icon:Ext.MessageBox.INFO,
							closable:false,
							fn:function(e){
								if(e=='ok'){
									grid.render();
									store.reload();
								}
							}
						});
					},
					failure : function(){
						myMask.hide();
						Ext.MessageBox.show({
							title:'信息',
							width:250,
							msg:'请与后台服务人员联系!',
							buttons:Ext.MessageBox.OK,
							buttons:{'ok':'确定'},
							icon:Ext.MessageBox.ERROR,
							closable:false
						});
					}
				});
			}
		}
	});
}
