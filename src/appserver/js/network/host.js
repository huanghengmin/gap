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
        {name:'name',			mapping:'name'},
        {name:'value',			mapping:'value'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../NetworkAction_equipment.action"
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
    var checkColumn = new Ext.grid.CheckColumn({
        header: "删除",
        dataIndex: 'indoor',
        width: 50,
        align:'center'

    });

    var cm = new Ext.grid.ColumnModel([{
            header: "配置项",
            dataIndex: 'name',
            align:'center',
            width: 50,
            editor: new Ext.form.DisplayField({
                allowBlank: false
            })
        },{
            header: "配置内容",
            dataIndex: 'name',
            align:'center',
            width: 50,
            editor: new Ext.form.DisplayField({
                allowBlank: false  ,
                value:'Inside'
            })
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
        plugins:checkColumn,
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
Ext.grid.CheckColumn = function(config)  {
    Ext.apply(this, config);
    if(!this.id)  {
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};
Ext.grid.CheckColumn.prototype =  {
    init : function(grid)  {
        this.grid = grid;
        this.grid.on('render', function()  {
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    onMouseDown : function(e, t)  {
        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1)  {
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            record.set(this.dataIndex, !record.data[this.dataIndex]);
        }
    },

    renderer : function(v, p, record)  {
        p.css += ' x-grid3-check-col-td';
        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&nbsp;</div>';
    }
}