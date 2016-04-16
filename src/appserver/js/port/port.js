/**
 * 网卡配置
 */
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'port',			mapping:'port'},
        {name:'worktime',			mapping:'worktime'},
        {name:'run',			mapping:'run'},
        {name:'tunneltype',			mapping:'tunneltype'},
        {name:'tunnel',			mapping:'tunnel'},
        {name:'runstate',			mapping:'runstate'},
        {name:'type',			mapping:'type'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../PortAction_getTask.action"
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

    var rowNumber = new Ext.grid.RowNumberer();

    var cm = new Ext.grid.ColumnModel([boxM,rowNumber,{
            header: "任务号",
            dataIndex: 'id',
            align:'center',
            width: 20,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        },{
            header:"选择通道",
            dataIndex:"tunnel",
            align:'center',
            renderer:_tunnel,
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
            allowBlank: false
            })
        },{
            header: "通道类型",
            dataIndex:"tunneltype",
            align:'center',
            renderer:_tunneltype,
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        },{
            header: "协议类型",
            dataIndex:"type",
            align:'center',
            renderer:_run,
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        } ,{
            header: "监听端口",
            dataIndex: 'port',
            align:'center',
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        } ,{
            header: "工作时间段",
            dataIndex: 'worktime',
            align:'center',
            width: 50,
            renderer:_def,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })

        },{
            header:"允许运行",
            dataIndex:"run",
            align:'center',
            renderer:_run,
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        },{
            header: "运行状态",
            dataIndex:"runstate",
            align:'center',
            renderer:_runstate,
            width: 50,
            editable:false,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        } ,{
        header : '操作',
        dataIndex : '0',
        align : 'center',
        sortable : false,
        menuDisabled : true,
        width : 50,
        renderer : function(store) {
            var block =null;

            var str = "<a onclick='javascript:updatePort("
                + store
                + ");' style='cursor:hand;cursor:pointer;color:blue;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;"
//						+ cert

            return str;
        }
    }

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
    var grid_panel = new Ext.grid.EditorGridPanel({
        id: 'grid.info',
        plain:true,
        height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        store: store, //EditorGridPanel使用的store
        stripeRows:true,
        trackMouseOver: true,
        disableSelection: false,
        clicksToEdit: 1, //设置点击几次才可编辑
        cm: cm,
        sm:boxM,
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },

        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar,
        tbar:[new Ext.Button({
            id:'btnAdd.info',
            text:'新增',
            iconCls:'add',
            handler:function(){
                addGridFormWin(grid_panel,store);
            }
            }),
            {xtype:"tbseparator"},
            new Ext.Button ({
                id : 'btnRemove.info',
                text : '删除',
                iconCls : 'delete',
                handler : function() {
                    deleteGridRow(grid_panel,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnStart.info',
                text:'启用',
                iconCls:'change',
                handler:function(){
                    startPort(grid_panel,store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnStop.info',
                text:'清除',
                iconCls:'delete',
                handler:function(){
                    clearPort(grid_panel,store);
                }
            })
        ]
    });
    function afterEdit(obj){
        var r = obj.record;//获取被修改的行
        var l = obj.field;//获取被修改的列
        var id = r.get("id");
        var lie = r.get(l);
        Ext.Ajax.request({
            url: 'NetworkAction_update.action',
            params: "id=" + id + "&name=" + l + '&value=' + lie
        });
    }
    grid_panel.on("afteredit", afterEdit, grid_panel);
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

function updatePort(store){
    var grid = Ext.getCmp('grid.info');
    var recode = grid.getSelectionModel().getSelected();
    var selModel = grid.getSelectionModel();
    var formPanel;
    if(selModel.hasSelection()){
        var selections = selModel.getSelections();
        Ext.each(selections,function(item){
            formPanel = new Ext.form.FormPanel({
                defaultType : 'displayfield',
                labelWidth:130,
                autoScroll:true,
                frame:true,
                border:false,
                labelAlign:'right',
                items :[
                    {
                        height:2,xtype:'displayfield'
                    },
                    {
                        xtype : 'displayfield',
                        fieldLabel : '任务号' ,
                        id:'id',width:120,
                        name:'id',
                        value:item.data.id
                    },
                    {
                        height:2,xtype:'displayfield'
                    },
                    new Ext.form.ComboBox({
                        fieldLabel : '端口类型',
                        id:'type',
                        name:'type',
                        value:item.data.type,
                        store :  new Ext.data.SimpleStore({
                            fields : ['value', 'key'],
                            data : [['tcp','tcp'],['udp','udp']]
                        }),
                        width:120,
                        valueField : 'value',
                        displayField : 'key',
                        typeAhead : true,
                        mode : 'local',
                        triggerAction : 'all',
                        selectOnFocus : true,
                        editable:true,
                        allowBlank : false
                    }),
                    {
                        height:2,xtype:'displayfield'
                    },
                    {
                        xtype : 'textfield',
                        fieldLabel : '监听端口'  ,
                        id:'port',
                        width:120,
                        name:'port',
                        value:item.data.port
                    },
                    {
                        height:2,xtype:'displayfield'
                    },
                    {
                        xtype : 'textfield',
                        fieldLabel : '工作时间'  ,
                        id:'worktime',
                        width:120 ,
                        value:'00:00-23:59',
                        name:'worktime',
                        value:item.data.worktime
                    },
                    {
                        height:2,xtype:'displayfield'
                    },
                    new Ext.form.ComboBox({
                        fieldLabel : '是否运行',
                        id:'run_1',
                        name:'run',
                        value:item.data.run,
                        store :  new Ext.data.SimpleStore({
                            fields : ['value', 'key'],
                            data : [['是','是'],['否','否']]
                        }),
                        width:120,
                        valueField : 'value',
                        displayField : 'key',
                        typeAhead : true,
                        mode : 'local',
                        triggerAction : 'all',
                        selectOnFocus : true,
                        editable:true,
                        allowBlank : false
                    }),
                    {
                        height:2,xtype:'displayfield'
                    },
                    new Ext.form.ComboBox({
                        fieldLabel : '选择通道',
                        id:'tunnel',
                        name:'tunnel',
                        value:item.data.tunnel,
                        store :  new Ext.data.SimpleStore({
                            fields : ['value', 'key'],
                            data : [['1','通道一'],['2','通道二']]
                        }),
                        width:120,
                        valueField : 'value',
                        displayField : 'key',
                        typeAhead : true,
                        mode : 'local',
                        triggerAction : 'all',
                        selectOnFocus : true,
                        editable:true,
                        allowBlank : false
                    }),
                    {
                        height:2,xtype:'displayfield'
                    },
                     new Ext.form.ComboBox({
                        fieldLabel : '通道类型',
                        id:'tunneltype',
                        name:'tunneltype',
                         value:item.data.tunneltype,
                        width:120,
                        store :  new Ext.data.SimpleStore({
                            fields : ['value', 'key'],
                            data : [['1','前置-后置'],['2','后置-前置'],['3','双向']]
                        }),
                        valueField : 'value',
                        displayField : 'key',
                        typeAhead : true,
                        mode : 'local',
                        triggerAction : 'all',
                        selectOnFocus : true,
                        editable:true,
                        allowBlank : false
                    }),
                    {
                        height:2,xtype:'displayfield'
                    }
                    //{xtype : 'textfield',fieldLabel : '下载次数'  ,id:'downloadtimes' },
                    //{xtype : 'textfield',fieldLabel : '下载地址'  ,id:'downloadaddress'}
                ]
            });
        });
    }
    var win = new Ext.Window({
        title:"修改",
        width : 350,
        height:350,
        modal:true,
        layout:'fit',
        items:[formPanel],
        buttons:[
            {text:'保存',id:'addWindow',
                handler: function(){
                    if(formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../../PortAction_update.action',
                            method:'POST',
                            params:{
                                id:Ext.getCmp("id").getValue(),
                                port:Ext.getCmp("port").getValue(),
                                worktime:Ext.getCmp("worktime").getValue(),
                                run:Ext.getCmp("run_1").getValue(),
                                type:Ext.getCmp("type").getValue(),
                                tunneltype:Ext.getCmp("tunneltype").getValue(),
                                tunnel:Ext.getCmp("tunnel").getValue(),
                                oldid:recode.get("id"),
                                oldport:recode.get("port"),
                                oldworktime:recode.get("worktime"),
                                oldrun:recode.get("run"),
                                oldtunneltype:recode.get("tunneltype"),
                                oldtunnel:recode.get("tunnel"),
                                oldrunstate:recode.get("runstate"),
                                oldtype:recode.get("type")
                            },
                            waitTitle:'信息',
                            waitMsg:'正在测试通道请稍后...',
                            success:function(form,action) {
                                var msg = action.result.msg;
                                // Ext.getCmp('ping.result.info').setValue(flag);
                                //var respText = Ext.decode(response.responseText);
                                //后台回传的数据
                                // var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title:"信息",
                                    width:400,
                                    msg:msg,
                                    animEl:'init.restart.info',
                                    icon:Ext.MessageBox.INFO,
                                    buttons:{'ok':'确定'},
                                    closable:false,
                                    fn:function(e){
                                        if(e=='ok'){
                                            grid.render();
                                            grid.getStore().reload();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    win.close();
                }
            },
            {text:'关闭',
                handler: function(){
                    win.close();
                }}
        ]

    }).show();
}

function setHeight(){
	var h = document.body.clientHeight-8;
	return h;
}

function setWidth(){
    return document.body.clientWidth-8;
}

function show_null(value){
    if(value == 'null'){
        return '';
    } else {
        return value;
    }
}

function _def(string){
   /* Ext.getCmp('external.is_pk.info').getRawValue();*/
    if(string ==''){
        return '00:00-23:59';
    }
    else{
        return string;
    }
}

function _run(string){

    if(string == 'true'){
        return '是';
    }
    else if(string == 'false'){
        return '否';
    }
    else{
        return string ;
    }
}


function _tunneltype(string){

    if(string == '1'){
        return '前置-后置';
    }
    else if(string == '2'){
        return '后置-前置';
    }
    else if(string == '3'){
        return '双向';
    }
    else{
        return string ;
    }
}


function _runstate(string){

    if(string == '1'){
        return '已启用';
    }
    else if(string == '0'){
        return '未启用';
    }
    else{
        return string ;
    }
}


function _tunnel(string){

    if(string == '1'){
        return '通道一';
    }
    else if(string == '2'){
        return '通道二';
    }
    else{
        return string ;
    }
}

function addGridFormWin(grid,store){

    var run = new Ext.form.ComboBox({
        fieldLabel : '是否运行',
        id:'run_1',
        name:'run',
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['是','是'],['否','否']]
        }),
        width:150,
        valueField : 'value',
        displayField : 'key',
        typeAhead : true,
        mode : 'local',
        triggerAction : 'all',
        selectOnFocus : true,
        editable:true,
        allowBlank : false
    });

    var type = new Ext.form.ComboBox({
        fieldLabel : '端口类型',
        id:'type',
        name:'type',
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['tcp','tcp'],['udp','udp']]
        }),
        width:150,
        valueField : 'value',
        displayField : 'key',
        typeAhead : true,
        mode : 'local',
        triggerAction : 'all',
        selectOnFocus : true,
        editable:true,
        allowBlank : false
        ,listeners:{
            select:function(combo,record,index) {
                var value = combo.getValue();
                if(value=="tcp") {
                   tunneltype.setValue("3");
                }
            }
        }

    });

    var tunneltype = new Ext.form.ComboBox({
        fieldLabel : '通道类型',
        id:'tunneltype',
        name:'tunneltype',
        width:150,
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['1','前置-后置'],['2','后置-前置'],['3','双向']]
        }),
        valueField : 'value',
        displayField : 'key',
        typeAhead : true,
        mode : 'local',
        triggerAction : 'all',
        selectOnFocus : true,
        editable:true,
        allowBlank : false
    });

    var tunnel = new Ext.form.ComboBox({
        fieldLabel : '选择通道',
        id:'tunnel',
        name:'tunnel',
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['1','通道一'],['2','通道二']]
        }),
        width:150,
        valueField : 'value',
        displayField : 'key',
        typeAhead : true,
        mode : 'local',
        triggerAction : 'all',
        selectOnFocus : true,
        editable:true,
        allowBlank : false
    });

    var add= new Ext.form.FormPanel({
        id:'add',
        name:'add',
        labelAlign : 'right',//位置
        labelWidth : 100,
        frame : true,
        width : 350,
        height:400,
        items :[
            {
                height:2,xtype:'displayfield'
            },
            {
                xtype : 'textfield',
                fieldLabel : '任务号' ,
                id:'id',
                width:150,
                name:'id'
            },
            {
                height:2,xtype:'displayfield'
            },
            type,
            {
                height:2,xtype:'displayfield'
            },
            {
                xtype : 'textfield',
                fieldLabel : '监听端口'  ,
                id:'port',
                width:150,
                name:'port',
                emptyText:"例:10000或10000-20000"
            },
            {
                height:2,xtype:'displayfield'
            },
            {
                xtype : 'textfield',
                fieldLabel : '工作时间'  ,
                id:'worktime',
                width:150 ,
                value:'00:00-23:59',
                name:'worktime'
            },
            {
                height:2,xtype:'displayfield'
            },
            run,
            {
                height:2,xtype:'displayfield'
            },
            tunnel,
            {
                height:2,xtype:'displayfield'
            },
            tunneltype,
            {
                height:2,xtype:'displayfield'
            }
            //{xtype : 'textfield',fieldLabel : '下载次数'  ,id:'downloadtimes' },
            //{xtype : 'textfield',fieldLabel : '下载地址'  ,id:'downloadaddress'}
        ]
    });

    type.setValue("tcp");
    run.setValue("是");
    tunnel.setValue("1");
    tunneltype.setValue("3");

    var win = new Ext.Window({
        width:350,
        height:350,
        modal:true,
        title:"添加新服务端口",
        items:add,
        buttons:[
            {text:'保存',id:'addWindow',
                handler: function(){
                    if(add.form.isValid()) {
                        add.getForm().submit({
                            url: '../../PortAction_add.action',
                            method:'POST',
                            params:{
                                id:Ext.getCmp("id").getValue(),
                                port:Ext.getCmp("port").getValue(),
                                worktime:Ext.getCmp("worktime").getValue(),
                                run:Ext.getCmp("run_1").getValue(),
                                type:Ext.getCmp("type").getValue(),
                                tunneltype:Ext.getCmp("tunneltype").getValue(),
                                tunnel:Ext.getCmp("tunnel").getValue()
                            },
                            waitTitle:'信息',
                            waitMsg:'正在测试通道请稍后...',
                            success:function(form,action) {
                                var msg = action.result.msg;
                                // Ext.getCmp('ping.result.info').setValue(flag);
                                //var respText = Ext.decode(response.responseText);
                                //后台回传的数据
                                // var msg = respText.msg;
                                Ext.MessageBox.show({
                                    title:"信息",
                                    width:400,
                                    msg:msg,
                                    animEl:'init.restart.info',
                                    icon:Ext.MessageBox.INFO,
                                    buttons:{'ok':'确定'},
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
                    win.close();
                }
            },
            {text:'关闭',
                handler: function(){
                    win.close();
                }}
        ]
    });
    win.show();
}

function deleteGridRow(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'您没有勾选任何记录!',
            animEl:'btnRemove.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.WARNING,
            closable:false
        });

    }else if(count > 0){
        var idArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            idArray[i] = record[i].get('id');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要删除所选的所有记录?',
            animEl:'btnRemove.info',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg : '正在删除,请稍后...',
                        removeMask : true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../PortAction_delete.action',
                        params :{idArray : idArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnRemove.info',
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
}

function startPort(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'您没有勾选任何记录!',
            animEl:'btnStart.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.WARNING,
            closable:false
        });

    }else if(count > 0){
        var idArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            idArray[i] = record[i].get('id');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要启用所选的所有记录?',
            animEl:'btnStart.info',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg : '正在启用端口,请稍后...',
                        removeMask : true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../PortAction_startPort.action',
                        params :{idArray : idArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnStart.info',
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
}

function clearPort(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'您没有勾选任何记录!',
            animEl:'btnStart.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.WARNING,
            closable:false
        });

    }else if(count > 0){
        var idArray = new Array();
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            idArray[i] = record[i].get('id');
        }
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'确定要停用用所选的所有记录?',
            animEl:'btnStart.info',
            buttons:{'ok':'确定','no':'取消'},
            icon:Ext.MessageBox.WARNING,
            closable:false,
            fn:function(e){
                if(e=='ok'){
                    var myMask = new Ext.LoadMask(Ext.getBody(),{
                        msg : '正在停用端口,请稍后...',
                        removeMask : true
                    });
                    myMask.show();
                    Ext.Ajax.request({
                        url : '../../PortAction_clearPort.action',
                        params :{idArray : idArray },
                        success : function(r,o){
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            myMask.hide();
                            Ext.MessageBox.show({
                                title:'信息',
                                width:300,
                                msg:msg,
                                animEl:'btnStart.info',
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



    /*Ext.MessageBox.show({
        title:'信息',
        width:200,
        msg:'确定要停用所有端口?',
        animEl:'btnStop.info',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg : '正在删除,请稍后...',
                    removeMask : true
                });
                myMask.show();
                Ext.Ajax.request({
                    url : '../../PortAction_clearPort.action',
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:'信息',
                            width:300,
                            msg:msg,
                            animEl:'btnStop.info',
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
    });*/
}