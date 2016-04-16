
Ext.onReady(function() {

	Ext.BLANK_IMAGE_URL = '..../Images/ext/s.gif';
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
    var record = new Ext.data.Record.create([
        {name:'id',mapping:'id'},
        {name:'type',mapping:'type'},
        {name:'gapip',mapping:'gapip'},
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
	store.on('load',function(){
        var count = store.getCount();
        if(count == 2){
            var id = store.getAt(0).get('id');
            var type = store.getAt(0).get('type');
            var gapip = store.getAt(0).get('gapip');
            var videoip = store.getAt(0).get('videoip');
            if(id == 1){
                Ext.getCmp('gap.gapip.info1').setValue(gapip);
                Ext.getCmp('gap.videoip.info1').setValue(videoip);
            }
            if(id == 2){
                Ext.getCmp('gap.gapip.info2').setValue(gapip);
                Ext.getCmp('gap.videoip.info2').setValue(videoip);
            }
            var id = store.getAt(1).get('id');
            var gapip = store.getAt(1).get('gapip');
            var videoip = store.getAt(1).get('videoip');
            if(id == 1){
                Ext.getCmp('gap.gapip.info1').setValue(gapip);
                Ext.getCmp('gap.videoip.info1').setValue(videoip);
            }
            if(id == 2){
                Ext.getCmp('gap.gapip.info2').setValue(gapip);
                Ext.getCmp('gap.videoip.info').setValue(videoip);
            }
            Ext.getCmp('gap.type.info').setValue(type);
        }
        if(count == 1){
            var id = store.getAt(0).get('id');
            var type = store.getAt(0).get('type');
            var gapip = store.getAt(0).get('localIp');
            var videoip = store.getAt(0).get('videoip');
            if(id == 1){
                Ext.getCmp('gap.gapip.info1').setValue(gapip);
                Ext.getCmp('gap.videoip.info1').setValue(videoip);
            }
            if(id == 2){
                Ext.getCmp('gap.gapip.info2').setValue(gapip);
                Ext.getCmp('gap.videoip.info2').setValue(videoip);
            }
            Ext.getCmp('gap.type.info').setValue(type);
        }
	});
    store.load();
    var ping_formPanel = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            {
                height:2,xtype:'displayfield'
            },
            {
                id:'gap.type.info',
                fieldLabel:'网闸类型',
                xtype:'displayfield',
                name:'type',
               // regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
               // regexText:'这个不是Ip',
                editable:false,
                //emptyText:'请输入Ip'

            },
            {
                height:2,xtype:'displayfield'
            }
        ]
    });
    var panel_ping = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:true,
        width:600,
        items:[{
            xtype:'fieldset',
            title:'平台类型',
            items:[ping_formPanel]
        }]
    });
    var ping_formPanel2 = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            {
                height:20,xtype:'displayfield'
            },
            {
                id:'gap.gapip.info1',
                fieldLabel:'网闸ip',
                xtype:'textfield',
                name:'gapip1',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是Ip',
                editable:false,
                emptyText:'请输入Ip'
            },
            {
                height:20,xtype:'displayfield'
            },

            {
                id:'gap.videoip.info1',
                fieldLabel:'视频ip',
                xtype:'textfield',
                name:'videoip1',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是Ip',
                editable:false,
                emptyText:'请输入Ip'
            },
            {
                height:20,xtype:'displayfield'
            }
        ],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),{
                id:'ping.save.info1',
                text:'保存',
                handler:function(){
                    // Ext.getCmp('ping.result.info').setValue('');
                    if(ping_formPanel2.form.isValid()) {
                        ping_formPanel2.getForm().submit({
                            url: '../../TunnelConfig_SaveService.action',
                            method:'POST',
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
                                    buttons:{'ok':'确定'}
                                });
                            }
                        });
                    }
                }
            }]
    });
    var panel_ping2 = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:true,
        width:600,
        items:[{
            xtype:'fieldset',
            title:'通道一初始化',
            items:[ping_formPanel2]
        }]
    });

    var ping_formPanel3 = new Ext.form.FormPanel({
        plain:true,
        labelWidth:100,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:'right',
        buttonAlign:'left',
        defaults : {
            width : 200,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            {
                height:20,xtype:'displayfield'
            },
            {
                id:'gap.gapip.info2',
                fieldLabel:'网闸ip',
                xtype:'textfield',
                name:'gapip2',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是Ip',
                editable:false,
                emptyText:'请输入Ip'
            },
            {
                height:20,xtype:'displayfield'
            },

            {
                id:'gap.videoip.info2',
                fieldLabel:'视频ip',
                xtype:'textfield',
                name:'videoip2',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'这个不是Ip',
                editable:false,
                emptyText:'请输入Ip'
            },
            {
                height:20,xtype:'displayfield'
            }
        ],
        buttons:[
            new Ext.Toolbar.Spacer({width:100}),{
                id:'ping.save.info2',
                text:'保存',
                handler:function(){
                    // Ext.getCmp('ping.result.info').setValue('');
                    if(ping_formPanel3.form.isValid()) {
                        ping_formPanel3.getForm().submit({
                            url: '../../TunnelConfig_SaveService.action',
                            method:'POST',
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
                                    buttons:{'ok':'确定'}
                                });
                            }
                        });
                    }
                }
            }]
    });
    var panel_ping3 = new Ext.Panel({
        plain:true,
        border:false,
        autoScroll:true,
        width:600,
        items:[{
            xtype:'fieldset',
            title:'通道二初始化',
            items:[ping_formPanel3]
        }]
    });

    new Ext.Viewport({
    	layout :'fit',
    	renderTo:Ext.getBody(),
    	items:[
            {
                layout:'form',
                frame:true,
                buttonAlign :'left',
                autoScroll:true,
                items:[panel_ping,panel_ping2,panel_ping3]
               /* buttons:[
                    new Ext.Toolbar.Spacer({width:100}),
                    {
                        text:"保存",
                        id:'init.save.info',
                        handler:function(){
                            var localip = Ext.getCmp("ping.localip.info1").getValue();
                            var targetip = Ext.getCmp("ping.targetIp.info1").getValue();
                            var localip2 = Ext.getCmp("ping.localip.info2").getValue();
                            var targetip2 = Ext.getCmp("ping.targetIp.info2").getValue();
                            var dataArray = new Array();
                            dataArray[0] = localip+"&"+targetip+"&"+localip2+"&"+targetip2;
                            Ext.Ajax.request({
                                url: '../../TunnelConfig_save.action',
                                method: 'post',    //这里也可以是get方法，后台接收根据程序语言的不同而不同
                                params:{ dataArray:dataArray},    //传递的参数，这里的参数一般是根据元素id来获取值，因为你没有创建单表
                                success: function(response){
                                    var respText = Ext.decode(response.responseText);
                                    var msg = respText.msg;
                                    Ext.MessageBox.show({
                                        title:"信息",
                                        width:200,
                                        msg:msg,
                                        animEl:'init.restart.info',
                                        icon:Ext.MessageBox.INFO,
                                        buttons:{'ok':'确定'}
                                    });
                                }
                            });
                        }
                    },{
                        text:'重新启动',
                        id:'init.restart.info',
                        handler:function(){
                            Ext.MessageBox.show({
                                title:"信息",
                                msg:"确定要重启系统吗?",
                                animEl:'init.restart.info',
                                icon:Ext.MessageBox.QUESTION,
                                buttons:{'ok':'确定','no':'取消'},
                                fn:function(e){
                                    if(e=='ok'){ 
                                    	var myMask = new Ext.LoadMask(Ext.getBody(), {
                        					msg: '正在处理,请稍后...',
                        					removeMask: true //完成后移除
                        				});
                        				myMask.show();
                                        Ext.Ajax.request({
                                            url:'PlatformAction_initBoot.action',
                                            method:'POST',
                                            success:function(r,o){
                                                var respText = Ext.util.JSON.decode(r.responseText);
                                                var msg = respText.msg;
                                            	myMask.hide();
                                                Ext.MessageBox.show({
                                                    title:"信息",
                                                    width:200,
                                                    msg:msg,
                                                    animEl:'init.restart.info',
                                                    icon:Ext.MessageBox.INFO,
                                                    buttons:{'ok':'确定'}
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                ]*/
            }
        ]
    });
}); // / Ext onReady end!


