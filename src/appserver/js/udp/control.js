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
        {name:'task',			mapping:'task'},
        {name:'ip',			mapping:'ip'},
        {name:'port',			mapping:'port'},
        {name:'worktime',			mapping:'worktime'},
        {name:'run',			mapping:'run'},
        {name:'describe',			mapping:'describe'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../UdpControlAction_getTask.action"
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
//    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();
    var cm = new Ext.grid.ColumnModel([rowNumber,{
            header: "任务号",
            dataIndex:'task',
            align:'center',
            width: 50
        },{
            header: "本地监听地址",
            dataIndex: 'ip',
            width: 50,
            align:'center'
        } ,{
            header: "监听端口",
            dataIndex: 'port',
            align:'center',
            width: 50
        } ,{
            header: "工作时间段",
            dataIndex: 'worktime',
            align:'center',
            width: 50,
            renderer:def
        },{
            header:"运行",
            dataIndex:'run',
            align:'center',
            menuDisabled:true,
            sortable:true,
            width: 50
        },{
            header: "下一步",
            align:'center',
            width: 50,
            renderer:next
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
//        sm:boxM,
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

function show_null(value){
    if(value == 'null'){
        return '';
    } else {
        return value;
    }
}
function def(string){
    /* Ext.getCmp('external.is_pk.info').getRawValue();*/
    if(string ==''){
        return '00:00-23:59';
    }
    else{
        return string;
    }
}
function next(){
    return '<a href="javascript:;" onclick="add();">访问配置</a>';
}
function add(){
    var start = 0;
    var pageSize = 15;
    var add_proxy = new Ext.data.HttpProxy({
        url:"../../UdpControlAction_getControl.action"
    });

    var add_record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'clientip',		mapping:'clientip'},
        {name:'clientport',	mapping:'clientport'},
        {name:'targetip',		mapping:'targetip'},
        {name:'targetport',	mapping:'targetport'},
        {name:'allow',        mapping:'allow'}
    ]);
    var add_reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },add_record);
    var add_store = new Ext.data.GroupingStore({
        id:"add_store.info",
        proxy : add_proxy,
        reader : add_reader
    });
    var add_display  = new Ext.form.DisplayField({
        width: 100,
        id:'display',
        disabled:true
    });
    var add_policy = new Ext.form.ComboBox({
        id:'add_policy',
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['允许','允许'],['禁止','禁止']]
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
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();
    var add_cm = new Ext.grid.ColumnModel([boxM,rowNumber,{
        header: "客户端地址",
        dataIndex: 'clientip',
        align:'center',
        width: 160,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    },{
        header: "客户端端口",
        dataIndex: 'clientport',
        align:'center',
        width:160,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    }, {
        header: "目的地址",
        dataIndex: 'targetip',
        align:'center',
        width: 160,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    },{
        header: "目的端口",
        dataIndex: 'targetport',
        align:'center',
        width: 160,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
    },{
        header: "允许/禁止",
        dataIndex: 'allow',
        width: 170,
        align:'center',
        editor:add_policy
    }
    ]);
    var addpage_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:add_store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });
    var add_panel = new Ext.grid.EditorGridPanel({
        id: 'add_grid.info',
        plain:true,
        height:580,
        width:860,
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        store: add_store, //EditorGridPanel使用的store
        stripeRows:true,
        trackMouseOver: true,
        disableSelection: false,
        clicksToEdit: 1, //设置点击几次才可编辑
        cm: add_cm,
        sm:boxM,
        bbar:addpage_toolbar,
        tbar:[new Ext.Button({
            id:'btnAdd.info',
            text:'新增',
            iconCls:'add',
            handler:function(){
                add_panel.stopEditing();
                add_panel.getStore().insert(
                    0,
                    new add_record({
                        clientip:'',
                        clientport:'',
                        targetip:'',
                        targetport:'',
                        allow:'',
                        flag:2
                    })
                );
                add_panel.startEditing(0,0);
            }
        }),
            {xtype:"tbseparator"},
            new Ext.Button ({
                id : 'btnRemove.info',
                text : '删除',
                iconCls : 'delete',
                handler : function() {
                    deleteGridRow(add_panel,add_store);
                }
            }),
            {xtype:"tbseparator"},
            new Ext.Button({
                id:'btnSave.info',
                text:'保存',
                iconCls:'add',
                handler:function(){
                    insertGridFormWin(add_panel,add_store);
                }
            })
        ]
    });
    var window = new Ext.Window({
        title:'访问配置',
        width:870,
        height:610,
        modal:true,
        items:[add_panel]
    }).show();
    add_store.load({
        params:{
            start:start,limit:pageSize
        }
    });
}
function addSpace(string){
    for(var i = 0 ; i < 50 ;i++){
        string = string+'&nbsp;'
    }
    return string;
}
function insertGridFormWin(grid,store){
    var selModel = grid.getSelectionModel();
    var count = selModel.getCount();
    if(count==0){
        Ext.MessageBox.show({
            title:'信息',
            width:200,
            msg:'您没有勾选任何记录!',
            animEl:'btnSave.info',
            buttons:{'ok':'确定'},
            icon:Ext.MessageBox.WARNING,
            closable:false
        });

    }else if(count > 0){
        var dataArray = new Array();
        var id = "";
        var record = grid.getSelectionModel().getSelections();
        for(var i = 0; i < record.length; i++){
            var clientip = record[i].get('clientip');
            var clientport = record[i].get('clientport');
            var targetip = record[i].get('targetip');
            var targetport = record[i].get('targetport');
            var allow = record[i].get('allow');
            id = record[i].get('id');

            dataArray[i] = clientip+"&"+clientport+"&"+targetip+"&"+targetport+"&"+allow+"&id="+id;

        }
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
                        url : '../../UdpControlAction_save.action',
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
                        url : '../../UdpControlAction_delete.action',
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