/**
 *  业务手动重传
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

	Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var tb = new Ext.Toolbar({
        width : 1000,
        height : 30,
        items : ['起始日期：', {
            id : 'startDate.tb.info',
            xtype : 'datefield',
            name : 'startDate',
            emptyText : '点击输入日期',
            format : 'Y-m-d'
        }, {
            xtype : 'tbspacer',
            width : 10
        },'结束日期：', {
            id : 'endDate.tb.info',
            xtype : 'datefield',
            name : 'endDate',
            emptyText : '点击输入日期',
            format : 'Y-m-d'
        }, {
            xtype : 'tbspacer',
            width : 10
        }, '业务名',{
            id:'businessName.tb.info',
            xtype:'textfield',
            emptyText:'请输入业务名',
            width : 100
        },{
            xtype : 'tbspacer',
            width : 10
        },'业务类型',{
            id : 'businessType.tb.info',
            xtype : 'combo',
            store : new Ext.data.ArrayStore({
                autoDestroy : true,
                fields : ['value', 'key'],
                data : [
                    ['file', '文件同步'],
                    ['db', '数据同步'],
                    ['TCPProxy', 'TCP代理'],
                    ['UDPProxy', 'UDP代理']
                ]
            }),
            valueField : 'value',
            displayField : 'key',
            mode : 'local',
            forceSelection : true,
            triggerAction : 'all',
            emptyText : '--请选择--',
            value : '',
            selectOnFocus : true,
            width : 100
        }, {
            xtype : 'tbspacer',
            width : 10
        },'状态',{
            id : 'resetStatus.tb.info',
            xtype : 'combo',
            store : new Ext.data.ArrayStore({
                autoDestroy : true,
                fields : ['value', 'key'],
                data : [
                    ['全部', '全部'],
                    ['0', '需重传'],
                    ['1', '已重传']
                ]
            }),
            valueField : 'value',
            displayField : 'key',
            mode : 'local',
            forceSelection : true,
            triggerAction : 'all',
            emptyText : '--请选择--',
            value : '',
            selectOnFocus : true,
            width : 100
        }, {
            xtype : 'tbspacer',
            width : 10
        }, {
            text : '查询',
            iconCls:'query',
            listeners : {
                click : function() {
                    var businessType = Ext.fly('businessType.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('businessType.tb.info').getValue();
                    var resetStatus = Ext.fly('resetStatus.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('resetStatus.tb.info').getValue();
                    var startDate = Ext.fly("startDate.tb.info").dom.value == '点击输入日期'
                        ? null
                        : Ext.fly('startDate.tb.info').dom.value;
                    var endDate = Ext.fly('endDate.tb.info').dom.value == '点击输入日期'
                        ? null
                        : Ext.fly('endDate.tb.info').dom.value;
                    var businessName = Ext.fly('businessName.tb.info').dom.value == '请输入业务名'
                        ? null
                        :Ext.getCmp('businessName.tb.info').getValue();
                    if(startDate!=null && endDate!=null) {
                        var myMask = new Ext.LoadMask(Ext.getBody(),{
                            msg : '正在处理,请稍后...',
                            removeMask : true
                        });
                        myMask.show();
                        Ext.Ajax.request({
                            url : '../../AuditAction_checkDate.action',
                            params : {startDate:startDate,endDate:endDate},
                            method :'POST',
                            success:function(r,o){
                                myMask.hide();
                                var respText = Ext.util.JSON.decode(r.responseText);
                                var msg = respText.msg;
                                var clear = respText.clear;
                                if(!clear){
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:280,
                                        msg:'结束时间不能早于开始时间',
                                        animEl:'endDate.tb.info',
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.ERROR,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                Ext.getCmp('endDate.tb.info').setValue('');
                                            }
                                        }
                                    });
                                } else{
                                    store.setBaseParam('startDate', startDate);
                                    store.setBaseParam('endDate', endDate);
                                    store.setBaseParam('businessName', businessName);
                                    store.setBaseParam('businessType', businessType);
                                    store.setBaseParam('resetStatus', resetStatus);
                                    store.load({
                                        params : {
                                            start : start,
                                            limit : pageSize
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        store.setBaseParam('startDate', startDate);
                        store.setBaseParam('endDate', endDate);
                        store.setBaseParam('businessName', businessName);
                        store.setBaseParam('businessType', businessType);
                        store.setBaseParam('resetStatus', resetStatus);
                        store.load({
                            params : {
                                start : start,
                                limit : pageSize
                            }
                        });
                    }
                }
            }
        }, {
            xtype : 'tbspacer',
            width : 10
        },{
            id :'truncate.tb.info',
            text : '清空',
            iconCls:'removeall',
            listeners : {
                click : function() {
                    var resetStatus = Ext.fly('resetStatus.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('resetStatus.tb.info').getValue();
                    var businessType = Ext.fly('businessType.tb.info').dom.value == '--请选择--'
                        ? null
                        : Ext.getCmp('businessType.tb.info').getValue();
                    var startDate = Ext.fly("startDate.tb.info").dom.value == '点击输入日期'
                        ? null
                        : Ext.fly('startDate.tb.info').dom.value;
                    var endDate = Ext.fly('endDate.tb.info').dom.value == '点击输入日期'
                        ? null
                        : Ext.fly('endDate.tb.info').dom.value;
                    var businessName = Ext.fly('businessName.tb.info').dom.value == '请输入业务名'
                        ? null
                        :Ext.getCmp('businessName.tb.info').getValue();
                    Ext.MessageBox.show({
                        title:'信息',
                        msg:"<font color='green'>确定要清空?</font>",
                        animEl:'truncate.tb.info',
                        buttons:{'ok':'确定','no':'取消'},
                        icon:Ext.MessageBox.WARNING,
                        closable:false,
                        fn:function(e){
                            if(e=='ok'){
                                var myMask = new Ext.LoadMask(Ext.getBody(),{
                                msg : '正在处理,请稍后...',
                                removeMask : true
                            });
                            myMask.show();
                            Ext.Ajax.request({
                                url : '../../AuditResetAction_truncate.action',
                                params : {resetStatus:resetStatus,businessType:businessType,
                                    startDate:startDate,endDate:endDate,businessName:businessName},
                                method :'POST',
                                success:function(r,o){
                                    var respText = Ext.util.JSON.decode(r.responseText);
                                    var msg = respText.msg;
                                    myMask.hide();
                                    Ext.MessageBox.show({
                                        title:'信息',
                                        width:250,
                                        msg:msg,
                                        animEl:'truncate.tb.info',
                                        buttons:{'ok':'确定'},
                                        icon:Ext.MessageBox.INFO,
                                        closable:false,
                                        fn:function(e){
                                            if(e=='ok'){
                                                grid_panel.render();
                                                store.reload();
                                            }
                                        }
                                    });
                                }
                            });
                            }
                        }
                    });

                }
            }
        }]
    });
    var record = new Ext.data.Record.create([
        {name:'id',			   mapping:'id'},
        {name:'businessName',   mapping:'businessName'},
        {name:'businessType',   mapping:'businessType'},
        {name:'fileName',        mapping:'fileName'},
        {name:'fileSize',	       mapping:'fileSize'},
        {name:'resetStatus',	   mapping:'resetStatus'},
        {name:'resetCount',	   mapping:'resetCount'},
        {name:'importTime',	   mapping:'importTime'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../AuditResetAction_select.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:"导入时间",		dataIndex:"importTime",    align:'center',sortable:true,menuDisabled:true,width:100},
        {header:"业务名",			dataIndex:"businessName",  align:'center',sortable:true,menuDisabled:true,width:100},
        {header:"业务类型",		dataIndex:"businessType",  align:'center',sortable:true,menuDisabled:true,width:45,renderer:businessType_showUrl},
        {header:'业务对应文件',	dataIndex:'fileName',       align:'center',sortable:true,menuDisabled:true,width:200},
        {header:'文件大小',	    dataIndex:'fileSize',       align:'center',sortable:true,menuDisabled:true,width:50},
        {header:"状态",	    	dataIndex:"resetStatus",	   align:'center',sortable:true,menuDisabled:true,width:45,renderer:resetStatus_showUrl},
        {header:"重传次数",    	dataIndex:"resetCount",	   align:'center',sortable:true,menuDisabled:true,width:45},
        {header:'操作标记',  	    dataIndex:'id',              align:'center',sortable:true,menuDisabled:true,renderer:flag_showUrl}
    ]);

    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        plain:true,
        height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
//        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:'Position',
        disableSelection:true,
        bodyStyle:'width:100%',
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        tbar:[{
            id:'add.info',
            xtype:'button',
            text:'导入重传文件',
            iconCls:'add',
            handler:function(){
                import_reset_xml(grid_panel,store);
            }
        },{
            xtype : 'tbseparator'
        },tb],
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[grid_panel]
    });
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });
});
function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function businessType_showUrl(value){
    if(value=='file'){
        return '文件同步';
    } else if(value=='db'){
        return '数据同步';
    } else if(value=='TCPProxy'){
        return 'TCP代理';
    } else if(value=='UDPProxy'){
        return 'UDP代理';
    }
}

function resetStatus_showUrl(value){
    if(value==0){
        return "<font color='red'>需重传</font>";
    } else if(value==1){
        return "<font color='green'>已重传</font>";
    }
}

function flag_showUrl(value,p,r){
    if(r.get('resetStatus')==0){
        return "<a href='javascript:;' onclick='audit_reset(\""+value+"\")' style='color: green;'>重传</a>"
    } else if(r.get('resetStatus')==1) {
        return "<font color='gray'>重传</font> "
    }
}

function import_reset_xml(grid,store){
   var form = new Ext.form.FormPanel({
        frame:true,
        labelWidth:80,
        labelAlign:'right',
        fileUpload:true,
        border:false,
        defaults : {
            width : 300,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[{
            xtype:'displayfield',
            value:'导入文件为[*.xml]'
        },{
            id:'uploadFile',
            fieldLabel:"导入文件",
            name:'uploadFile',
            xtype:'textfield',
            inputType: 'file',
            listeners:{
                render:function(){
                    Ext.get('uploadFile').on("change",function(){
                        var file = this.getValue();
                        var fs = file.split('.');
                        if(fs[fs.length-1].toLowerCase()=='xml'){
                            Ext.MessageBox.show({
                                title:'信息',
                                msg:'<font color="green">确定要上传文件:'+file+'？</font>',
                                width:300,
                                buttons:{'ok':'确定','no':'取消'},
                                icon:Ext.MessageBox.WARNING,
                                closable:false,
                                fn:function(e){
                                    if(e == 'ok'){
                                        if (form.form.isValid()) {
                                            form.getForm().submit({
                                                url :'../../AuditResetAction_insert.action',
                                                method :'POST',
                                                waitTitle :'系统提示',
                                                waitMsg :'正在导入,请稍后...',
                                                success : function(form,action) {
                                                    var msg = action.result.msg;
                                                    Ext.MessageBox.show({
                                                        title:'信息',
                                                        width:250,
                                                        msg:msg,
//                                                    animEl:'insert.win.info',
                                                        buttons:{'ok':'确定','no':'取消'},
                                                        icon:Ext.MessageBox.INFO,
                                                        closable:false,
                                                        fn:function(e){
                                                            if(e=='ok'){
                                                                grid.render();
                                                                store.reload();
                                                                win.close();
                                                            } else {
                                                                Ext.getCmp('uploadFile').setValue('');
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            Ext.MessageBox.show({
                                                title:'信息',
                                                width:200,
                                                msg:'请填写完成再提交!',
//                                            animEl:'insert.win.info',
                                                buttons:{'ok':'确定'},
                                                icon:Ext.MessageBox.ERROR,
                                                closable:false
                                            });
                                        }
                                    }
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:200,
                                msg:'上传文件格式不对,请重新选择!',
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        Ext.getCmp('uploadFile').setValue('');
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }]
    });
   var win = new Ext.Window({
		title:'批量导入',
		width:440,
		height:150,
		layout:'fit',
        modal:true,
		items:[form],
		bbar:['->',{
//    		id:'insert.win.info',
//    		text:'导入',
//    		handler:function(){
//    			if (form.form.isValid()) {
//                	form.getForm().submit({
//		            	url :'../../AuditResetAction_insert.action',
//		                method :'POST',
//		                waitTitle :'系统提示',
//		                waitMsg :'正在导入...',
//		                success : function(form,action) {
//		                	var msg = action.result.msg;
//			                Ext.MessageBox.show({
//			                	title:'信息',
//			                    width:250,
//			                    msg:msg,
//		                        animEl:'insert.win.info',
//		                        buttons:Ext.MessageBox.OK,
//		                        buttons:{'ok':'确定'},
//		                        icon:Ext.MessageBox.INFO,
//		                        closable:false,
//		                        fn:function(e){
//		                        	if(e=='ok'){
//		                            	grid.render();
//		                            	store.reload();
//		                            	win.close();
//		                        	}
//		                        }
//		                	});
//		                }
//		        	});
//                } else {
//                    Ext.MessageBox.show({
//                        title:'信息',
//                        width:200,
//                        msg:'请填写完成再提交!',
//                        animEl:'insert.win.info',
//                        buttons:Ext.MessageBox.OK,
//                        buttons:{'ok':'确定'},
//                        icon:Ext.MessageBox.ERROR,
//                        closable:false
//                    });
//                }
//    		}
//    	},{
    		text:'关闭',
    		handler:function(){
    			win.close();
    		}
    	}]
	}).show();
}

function audit_reset(id){
    var grid = Ext.getCmp('grid.info');
    var store = grid.getStore();
    Ext.MessageBox.show({
        title:'信息',
        msg:'<font color="GREEN">确定要重传?</font> ',
        buttons:Ext.MessageBox.YESNO,
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.QUESTION,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg : '正在处理,请稍后...',
                    removeMask : true
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../AuditResetAction_update.action',
                    params : {id:id},
                    method :'POST',
                    success:function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:'信息',
                            width:250,
                            msg:msg,
                            animEl:'truncate.tb.info',
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
                    }
                });
            }
        }
    });


}