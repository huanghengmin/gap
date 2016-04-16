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
        {name:'tunnel',			mapping:'tunnel'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../TcpServerAction_getTask.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows",
        id:'id'
    },record);
    var boxM = new Ext.grid.CheckboxSelectionModel();   //复选框
    var rowNumber = new Ext.grid.RowNumberer();

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });
    var tunnel = new Ext.form.ComboBox({
        id:'tunnel',
        store :  new Ext.data.SimpleStore({
            fields : ['value', 'key'],
            data : [['通道一','通道一'],['通道二','通道二']]
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

    var cm = new Ext.grid.ColumnModel([boxM,rowNumber,{
        header: "任务号",
        dataIndex: 'task',
        align:'center',
        width:50,
        editor: new Ext.form.TextField({
            allowBlank: false
        })
        },{
            header: "服务器地址",
            dataIndex: 'ip',
            align:'center',
            width: 50,
            editor: new Ext.form.TextField({
            allowBlank: false
            })
        },{
            header: "服务器端口",
            dataIndex: 'port',
            align:'center',
            width: 50,
            editor: new Ext.form.TextField({
                allowBlank: false
            })
        },{
            header:"选择通道",
            dataIndex:"tunnel",
            align:'center',
            menuDisabled:true,
            sortable:true,
            renderer:_tunnel,
            width: 50,
            editor:tunnel
        }/*,{
        header: "地址绑定",
        dataIndex: 'band',
        width: 50,
        align:'center',
        editor: new Ext.form.ComboBox({
            typeAhead: true,
            triggerAction: 'all',
            transform:'light',
            lazyRender:true,
            listClass: 'x-combo-list-small'
        })
    }*/

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
                grid_panel.stopEditing();
                grid_panel.getStore().insert(
                    0,
                    new record({
                        task:'',
                        ip:'',
                        port:'',
                        tunnel:'',
                        flag:2
                    })
                );
                grid_panel.startEditing(0,0);
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
                id:'btnSave.info',
                text:'保存',
                iconCls:'add',
                handler:function(){
                    insertGridFormWin(grid_panel,store);
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
            var task = record[i].get('task');
            var ip = record[i].get('ip');
            var port = record[i].get('port');
            var tunnel = record[i].get('tunnel');
            id = record[i].get('id');
            if(ip.length>0){
                dataArray[i] = task+"&"+ip+"&"+port+"&"+tunnel+"&id="+id;
            }
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
                        url : '../../TcpServerAction_save.action',
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
                        url : '../../TcpServerAction_delete.action',
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