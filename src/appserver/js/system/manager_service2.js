/**
 * 网卡配置
 */
Ext.onReady(function() {

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'id',mapping:'id'},
        {name:'type',mapping:'type'},
        {name:'localIp',mapping:'localIp'},
        {name:'targetIp',mapping:'targetIp'},
        {name:'gapip',mapping:'gapip'},
        {name:'emptyValue',mapping:'emptyValue'},
        {name:'videoip',mapping:'videoip'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../TunnelConfig_getServices.action"
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

    store.on('load',function(){

        var type = store.getAt(0).get('type');

        Ext.getCmp('gap.type.info').setValue(type);

        if(type =='前置网闸'){

            Ext.getCmp('1').show();

            Ext.getCmp('2').hide();

        }
        if(type =='后置网闸'){

            Ext.getCmp('2').show();

            Ext.getCmp('1').hide();



        }
    });

    var rowNumber = new Ext.grid.RowNumberer();

    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框

    var rowNumber2 = new Ext.grid.RowNumberer();

    var boxM2 = new Ext.grid.CheckboxSelectionModel();   //复选框


    var cm = new Ext.grid.ColumnModel([boxM,rowNumber,{
        header: "通道",
        dataIndex: 'id',
        renderer:_tunnel,
        align:'center',
        width:50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    },{
        header:"前置视频内网口",
        dataIndex:"videoip",
        align:'center',
       // renderer:_tunnel,
        width: 50,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    },{
        header: "前置网闸外网口",
        dataIndex:"gapip",
        align:'center',
        //renderer:_tunneltype,
        width: 50,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    },{
        header: "前置网闸内网口",
        dataIndex:"localIp",
        align:'center',
        //renderer:_run,
        width: 50,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    } ,{
        header: "后置网闸内网口",
        dataIndex: 'emptyValue',
        align:'center',
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    } ,{
        header: "后置网闸外网口",
        dataIndex: 'emptyValue',
        align:'center',
        width: 50,
        //renderer:_def,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })

    },{
        header:"后置视频内网口",
        dataIndex: 'emptyValue',
        align:'center',
        //renderer:_run,
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        } )
    }/* ,{
            header : '操作',
            dataIndex : '0',
            align : 'center',
            sortable : false,
            menuDisabled : true,
//            width : 50,
            renderer : function(store,grid_panel) {
                var block =null;

                var str = "<a onclick='javascript:updatePort("
                    + store+","+grid_panel
                    + ");' style='cursor:hand;cursor:pointer;color:blue;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;"
//						+ cert

                return str;
            }
        }*/
    ]);

    var cm2 = new Ext.grid.ColumnModel([boxM2,rowNumber2,{
        header: "通道",
        dataIndex: 'id',
        align:'center',
        renderer:_tunnel,
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    },{
        header:"前置视频内网口",
        dataIndex:"emptyValue",
        align:'center',
        // renderer:_tunnel,
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    },{
        header: "前置网闸外网口",
        dataIndex:"emptyValue",
        align:'center',
        //renderer:_tunneltype,
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    },{
        header: "前置网闸内网口",
        dataIndex:"emptyValue",
        align:'center',
        //renderer:_run,
        width: 50,
        editable:false,
        editor: new Ext.form.DisplayField({
            allowBlank: false
        })
    } ,{
        header: "后置网闸内网口",
        dataIndex: 'localIp',
        align:'center',
        width: 50,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    } ,{
        header: "后置网闸外网口",
        dataIndex: 'gapip',
        align:'center',
        width: 50,
        //renderer:_def,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        })

    },{
        header:"后置视频内网口",
        dataIndex: 'videoip',
        align:'center',
        //renderer:_run,
        width: 50,
        editable:true,
        editor: new Ext.form.TextField({
            allowBlank: false
        } )
    }/* ,{
        header : '操作',
        dataIndex : '0',
        align : 'center',
        sortable : false,
        menuDisabled : true,
//        width : 50,
        renderer : function(store,grid_panel2) {
            var block =null;

            var str = "<a onclick='javascript:updatePort("
                + store
                + ");' style='cursor:hand;cursor:pointer;color:blue;'>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;"
//						+ cert

            return str;
        }
    }*/

    ]);

    var grid_panel = new Ext.grid.EditorGridPanel({
        id: 'grid.info',
        plain:true,
        height:setHeight(),
        //width:900,
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
        tbar:[new Ext.Button({
            id:'btnSave.info',
            text:'保存',
            iconCls:'save',
            handler:function(){
                updatePort(store,grid_panel);
            }
        })]
    });

    var pic_panel = new Ext.Panel({
        id:'pic.panel.info',
        plain:true,
        height:340,
        width:setWidth(),
        items:[{
            xtype:'fieldset',
            title:'初始化拓扑',
            items:[{
                xtype: 'box',
                width: 900,
                height: 320,
                autoEl: {
                    tag: 'img',    //指定为img标签
                    src: '../../img/icon/tp.png'    //指定url路径
                }
            }]
        }]
    });

    var type_panel = new Ext.Panel({
        id:'type.panel.info',
        height:80,
        items:[{
            xtype:'fieldset',
            title:'网闸类型',
            items:[{
                id:'gap.type.info',
                fieldLabel:'网闸类型',
                xtype:'combo',
                name:'type',
                store :  new Ext.data.SimpleStore({
                    fields : ['value', 'key'],
                    data : [['前置网闸','前置网闸'],['后置网闸','后置网闸']]
                }),
                width:120,
                valueField : 'value',
                displayField : 'key',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                selectOnFocus : true,
                editable:true,
                allowBlank : false,
                listeners:{
                    'select':function(){
                        var type = Ext.getCmp('gap.type.info').getValue();
                        Ext.Ajax.request({
                            url : '../../TunnelConfig_SaveType.action',
                            params : {type:type},
                            method : 'POST',
                            success : function(response) {
                                store.reload();
                            },
                            failure : function() {
                                Ext.Msg.alert("提示", "方法调用失败");
                            }
                        });
                    }
                }
            }]
        }]
    });

    var pic_panel2 = new Ext.Panel({
        id:'pic.panel.info',
        plain:true,
        height:340,
        width:setWidth(),
        items:[{
            xtype:'fieldset',
            title:'初始化拓扑',
            items:[{
                xtype: 'box',
                width: 900,
                height: 320,
                autoEl: {
                    tag: 'img',    //指定为img标签
                    src: '../../img/icon/tp.png'    //指定url路径
                }
            }]
        }]
    });

    var grid_panel2 = new Ext.grid.EditorGridPanel({
        id: 'grid.info',
        plain:true,
        height:setHeight(),
        //width:900,
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        store: store, //EditorGridPanel使用的store
        stripeRows:true,
        trackMouseOver: true,
        disableSelection: false,
        clicksToEdit: 1, //设置点击几次才可编辑
        cm: cm2,
        sm:boxM2,
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        tbar:[
            {
                xtype:"tbseparator"
            },
            new Ext.Button({
                id:'btnSave.info',
                text:'保存',
                iconCls:'save',
                handler:function(){
                    updatePort(store,grid_panel2);
                }
            }
        )]
    });

    var panel = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:false,
        //width:setWidth(),
        items:[{
            xtype:'fieldset',
            title:'初始化配置',
            items:[grid_panel]
        }]
    });

    var panel2 = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:false,
        //width:setWidth(),
        items:[{
            xtype:'fieldset',
            title:'初始化配置',
            items:[grid_panel2]
        }]
    });

    var panel_ = new Ext.Panel({
        layout:'form',
        items:[{
            id:'1'  ,
            layout:'form',
            frame:true,
            buttonAlign :'left',
            autoScroll:false,
            items:[pic_panel,panel]
        },{
            id:'2',

            layout:'form',
            frame:true,
            buttonAlign :'left',
            autoScroll:false,
            items:[pic_panel2,panel2]
        }]

    });

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[
            {
                layout:'form',
                frame:true,
                buttonAlign :'left',
                autoScroll:true,
                items:[type_panel, panel_]
            }
           ]
    });

});

function updatePort(store,grid){
    Ext.Msg.alert("",grid.getSelectionModel().getSelections());
    var dataArray = new Array();
    var id = "";
    var record = grid.getSelectionModel().getSelections();

    for(var i = 0; i < record.length; i++){
        var localIp = record[i].get('localIp');
        var gapip = record[i].get('gapip');
        var videoip = record[i].get('videoip');
        id = record[i].get('id');
        if(gapip.length>0){
            dataArray[i] = localIp+"&"+gapip+"&"+videoip+"&"+id;
        }
    }
    Ext.Msg.alert("",dataArray[0]);
    Ext.MessageBox.show({
        title:'信息',
        width:230,
        msg:'确定要保存所选的所有记录?',
        animEl:'btnSave.info',
        buttons:{'ok':'确定','no':'取消'},
        icon:Ext.MessageBox.WARNING,
        closable:false,
        fn:function(e){
            if(e=='ok'){
                var myMask = new Ext.LoadMask(Ext.getBody(),{
                    msg : '正在保存,请稍后...',
                    removeMask : true
                });
                myMask.show();

                Ext.Ajax.request({
                    url : '../../TunnelConfig_SaveService.action',
                    params :{dataArray : dataArray },
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        myMask.hide();
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:msg,
                            animEl:'btnSave.info',
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
        width:120,
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
        width:120,
        valueField : 'value',
        displayField : 'key',
        typeAhead : true,
        mode : 'local',
        triggerAction : 'all',
        selectOnFocus : true,
        editable:true,
        allowBlank : false
    });

    var tunneltype = new Ext.form.ComboBox({
        fieldLabel : '通道类型',
        id:'tunneltype',
        name:'tunneltype',
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
    });

    var tunnel = new Ext.form.ComboBox({
        fieldLabel : '选择通道',
        id:'tunnel',
        name:'tunnel',
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
    });

    var add= new Ext.form.FormPanel({
        id:'add',
        name:'add',
        labelAlign : 'right',//位置
        labelWidth : 120,
        frame : true,
        width : 350,
        height:350,
        items :[
            {
                height:2,xtype:'displayfield'
            },
            {
                xtype : 'textfield',
                fieldLabel : '任务号' ,
                id:'id',
                width:120,
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
                width:120,
                name:'port'
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

    Ext.MessageBox.show({
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
    });
}