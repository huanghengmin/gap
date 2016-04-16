<%@ page language="java"  pageEncoding="UTF-8"%>

<%@ page import = "org.jfree.chart.ChartFactory,
                    org.jfree.chart.ChartPanel,
                    org.jfree.chart.JFreeChart,
                    org.jfree.chart.axis.DateAxis,
                    org.jfree.chart.axis.ValueAxis,
                    org.jfree.chart.plot.XYPlot,
                    org.jfree.data.time.*,
                    org.jfree.data.xy.XYDataset,
                    javax.swing.*,
                    java.awt.*,
                    java.text.SimpleDateFormat,
                    org.jfree.chart.servlet.ServletUtilities,
                    java.util.*"%>
<%@ page import="org.jfree.data.category.DefaultCategoryDataset" %>
<%@ page import="org.jfree.chart.plot.PlotOrientation" %>
<%@ page import="org.jfree.chart.axis.DateTickUnit" %>
<%@ page import="org.jfree.chart.renderer.xy.XYLineAndShapeRenderer" %>
<%@ page import="com.hzih.gap.myjfree.LiuLiangBean" %>
<%@ page import="com.hzih.gap.myjfree.CpuBean" %>
<%@ page import="com.hzih.gap.utils.DeviceInfo" %>
<%@ page import="com.hzih.gap.myjfree.LinuxUtil" %>

<%
    TimeSeries timeseries = new TimeSeries("下行流量",Minute.class);
    TimeSeries timeseriesTx = new TimeSeries("上行流量",Minute.class);
    GregorianCalendar gc = new GregorianCalendar();
    int year = gc.get(Calendar.YEAR);
    int month = gc.get(Calendar.MONTH);
    int day = gc.get(Calendar.DATE);
    int hour = gc.get(Calendar.HOUR_OF_DAY);
    int minute = gc.get(Calendar.MINUTE);
    int second = gc.get(Calendar.SECOND);

    ArrayList<LiuLiangBean> liuLiangBeanList = (ArrayList<LiuLiangBean>) session.getAttribute("liuLiangBeanList");
    ArrayList<LiuLiangBean> liuLiangBeanListTX = (ArrayList<LiuLiangBean>) session.getAttribute("liuLiangBeanListTX");
    String flagts = request.getParameter("flagts");
    if(null==flagts||null==liuLiangBeanList) {
        liuLiangBeanList = new ArrayList<LiuLiangBean>();
        liuLiangBeanListTX = new ArrayList<LiuLiangBean>();
    }
    double rxNetFlow = new LinuxUtil().getRxBytesNum();
    double txNetFlow = new LinuxUtil().getTxBytesNum();
    if(0==liuLiangBeanList.size()){
        liuLiangBeanList.add(new LiuLiangBean(minute,new Hour(),rxNetFlow,0.0,System.currentTimeMillis()));
        liuLiangBeanListTX.add(new LiuLiangBean(minute,new Hour(),txNetFlow,0.0,System.currentTimeMillis()));
    }else {
        liuLiangBeanList.add(new LiuLiangBean(minute,new Hour(),rxNetFlow,rxNetFlow-liuLiangBeanList.get(liuLiangBeanList.size()-1).getNetFlow(),System.currentTimeMillis()));
        liuLiangBeanListTX.add(new LiuLiangBean(minute,new Hour(),txNetFlow,txNetFlow-liuLiangBeanListTX.get(liuLiangBeanListTX.size()-1).getNetFlow(),System.currentTimeMillis()));
    }
    if(liuLiangBeanList.size()>1&&(liuLiangBeanList.get(liuLiangBeanList.size()-1).getCurrentMillis()-liuLiangBeanList.get(0).getCurrentMillis())>(1000*60*60)){
        liuLiangBeanList.remove(0);
    }else {
        timeseries.addOrUpdate(new Minute(minute-60,new Hour()), 0);
    }
    if(liuLiangBeanListTX.size()>1&&(liuLiangBeanListTX.get(liuLiangBeanListTX.size()-1).getCurrentMillis()-liuLiangBeanListTX.get(0).getCurrentMillis())>(1000*60*60)){
        liuLiangBeanListTX.remove(0);
    }else {
        timeseriesTx.addOrUpdate(new Minute(minute-60,new Hour()), 0);
    }
    session.setAttribute("liuLiangBeanList",liuLiangBeanList);
    session.setAttribute("liuLiangBeanListTX",liuLiangBeanListTX);
    timeseries.addOrUpdate(new Minute( liuLiangBeanList.get(0).getMinute()-1, liuLiangBeanList.get(0).getHour()), 0);
    timeseriesTx.addOrUpdate(new Minute( liuLiangBeanListTX.get(0).getMinute()-1, liuLiangBeanListTX.get(0).getHour()), 0);
    for(LiuLiangBean LiuLiangBean :liuLiangBeanList){
        timeseries.addOrUpdate(new Minute( LiuLiangBean.getMinute(), LiuLiangBean.getHour() ), LiuLiangBean.getLiuliangNum());
    }
    for(LiuLiangBean LiuLiangBean :liuLiangBeanListTX){
        timeseriesTx.addOrUpdate(new Minute( LiuLiangBean.getMinute(), LiuLiangBean.getHour() ), LiuLiangBean.getLiuliangNum());
    }

//    timeseries.add(new Minute(), Math.random()*600);
//    timeseries.addOrUpdate(new Minute(), Math.random() * 600);
//    for(int i=1;i<=60;i++){
//        timeseries.add(new Minute(minute-i, new Hour()), Math.random()*600);
//    }
    TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
    timeseriescollection.addSeries(timeseries);
    timeseriescollection.addSeries(timeseriesTx);

    XYDataset xydataset = timeseriescollection;
    JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("网络流量统计", "时间", "流量(MB/分钟)", xydataset, true, true, true);
    XYPlot xyplot = (XYPlot) jfreechart.getPlot();
    xyplot.setBackgroundPaint(Color.black);      //设置背景颜色
    xyplot.setDomainGridlinePaint(Color.green);  //设置网格竖线颜色
    xyplot.setRangeGridlinePaint(Color.green);   //设置网格横线颜色
    XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)xyplot.getRenderer();
    xylinerenderer.setSeriesPaint(0, new Color(0, 255 ,0 ));
    xylinerenderer.setSeriesPaint(1, new Color(255, 0 ,0 ));

    DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();//获取x轴
    dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
//    SimpleDateFormat frm = new SimpleDateFormat("k:mm");
//    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 5,frm));
//    Calendar date = Calendar.getInstance();
//    date.set(2013, 2, 2,15,0);
//    Calendar mdate = Calendar.getInstance();
//    mdate.set(2013, 2, 2,15,60);
//    dateaxis.setRange(date.getTime(),mdate.getTime());


    // frame1=new ChartPanel(jfreechart,true);
    dateaxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
    dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题

    ValueAxis rangeAxis=xyplot.getRangeAxis();//获取y轴
    rangeAxis.setRange(0, 100);
//    rangeAxis.setAutoRange(true);
    rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
    jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
    jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
    String filename = ServletUtilities.saveChartAsPNG(jfreechart, 500, 360, null, session);
    String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;

 //---------------------------------------------------------------------------------cpu---------------------------------------------------------------------------------------------------------------

    TimeSeries timeseries2 = new TimeSeries("CPU使用量",
            Minute.class);
    GregorianCalendar gc2 = new GregorianCalendar();
    int year2 = gc2.get(Calendar.YEAR);
    int month2 = gc2.get(Calendar.MONTH);
    int day2 = gc2.get(Calendar.DATE);
    int hour2 = gc2.get(Calendar.HOUR_OF_DAY);
    int miniute2 = gc2.get(Calendar.MINUTE);
    int second2 = gc2.get(Calendar.SECOND);

    ArrayList<CpuBean> cpuBeanList = (ArrayList<CpuBean>) session.getAttribute("cpuBeanList");
//    String flagts = request.getParameter("flagts");
    if(null==flagts||null==cpuBeanList) {
        cpuBeanList = new ArrayList<CpuBean>();
    }
    cpuBeanList.add(new CpuBean( miniute2, new Hour(), DeviceInfo.cpuuse, System.currentTimeMillis() ));
    if(cpuBeanList.size()>1&&(cpuBeanList.get(cpuBeanList.size()-1).getCurrentMillis()-cpuBeanList.get(0).getCurrentMillis())>(1000*60*60)){
        cpuBeanList.remove(0);
    }else{
        timeseries2.addOrUpdate(new Minute(miniute2-60,new Hour()), 0);
    }
    session.setAttribute("cpuBeanList",cpuBeanList);
    timeseries2.addOrUpdate(new Minute( cpuBeanList.get(0).getMinute()-1, cpuBeanList.get(0).getHour() ), 0);
    for(CpuBean cpuBean :cpuBeanList){
        timeseries2.addOrUpdate(new Minute(cpuBean.getMinute(),cpuBean.getHour()), cpuBean.getCpuNum());
    }
    TimeSeriesCollection timeseriescollection2 = new TimeSeriesCollection();
    timeseriescollection2.addSeries(timeseries2);

    XYDataset xydataset2 = timeseriescollection2;
    JFreeChart jfreechart2 = ChartFactory.createTimeSeriesChart("CPU使用量统计", "时间", "CPU使用所占百分数", xydataset2, true, true, true);
    XYPlot xyplot2 = (XYPlot) jfreechart2.getPlot();
    xyplot2.setBackgroundPaint(Color.black);      //设置背景颜色
    xyplot2.setDomainGridlinePaint(Color.green);  //设置网格竖线颜色
    xyplot2.setRangeGridlinePaint(Color.green);   //设置网格横线颜色
    XYLineAndShapeRenderer xylinerenderer2=(XYLineAndShapeRenderer)xyplot2.getRenderer();
    xylinerenderer2.setSeriesPaint(0, new Color(0, 255 ,0 ));

    DateAxis dateaxis2 = (DateAxis) xyplot2.getDomainAxis();//获取x轴
    dateaxis2.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
//    SimpleDateFormat frm = new SimpleDateFormat("k:mm");
//    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 5,frm));
//    Calendar date = Calendar.getInstance();
//    date.set(2013, 2, 2,15,0);
//    Calendar mdate = Calendar.getInstance();
//    mdate.set(2013, 2, 2,15,60);
//    dateaxis.setRange(date.getTime(),mdate.getTime());


    // frame1=new ChartPanel(jfreechart,true);
    dateaxis2.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
    dateaxis2.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题

    ValueAxis rangeAxis2=xyplot2.getRangeAxis();//获取y轴
    rangeAxis2.setRange(0, 100);
//    rangeAxis.setAutoRange(true);
    rangeAxis2.setLabelFont(new Font("黑体",Font.BOLD,15));
    jfreechart2.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
    jfreechart2.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
    String filename2 = ServletUtilities.saveChartAsPNG(jfreechart2, 500, 360, null, session);
    String graphURL2 = request.getContextPath() + "/DisplayChart?filename=" + filename2;

//---------------------------------------------------------------------------------内存---------------------------------------------------------------------------------------------------------------
    TimeSeries timeseriesMem = new TimeSeries("内存使用量",
            Minute.class);

    ArrayList<CpuBean> cpuBeanListMem = (ArrayList<CpuBean>) session.getAttribute("cpuBeanListMem");
//    String flagts = request.getParameter("flagts");
    if(null==flagts||null==cpuBeanListMem) {
        cpuBeanListMem = new ArrayList<CpuBean>();
    }
    cpuBeanListMem.add(new CpuBean( miniute2, new Hour(), DeviceInfo.memuse, System.currentTimeMillis() ));
    if(cpuBeanListMem.size()>1&&(cpuBeanListMem.get(cpuBeanListMem.size()-1).getCurrentMillis()-cpuBeanListMem.get(0).getCurrentMillis())>(1000*60*60)){
        cpuBeanListMem.remove(0);
    }else{
        timeseriesMem.addOrUpdate(new Minute(miniute2-60,new Hour()), 0);
    }
    session.setAttribute("cpuBeanListMem",cpuBeanListMem);
    timeseriesMem.addOrUpdate(new Minute( cpuBeanListMem.get(0).getMinute()-1, cpuBeanListMem.get(0).getHour() ), 0);
    for(CpuBean cpuBean :cpuBeanListMem){
        timeseriesMem.addOrUpdate(new Minute(cpuBean.getMinute(),cpuBean.getHour()), cpuBean.getCpuNum());
    }
    TimeSeriesCollection timeseriescollectionMem = new TimeSeriesCollection();
    timeseriescollectionMem.addSeries(timeseriesMem);

    XYDataset xydatasetMem = timeseriescollectionMem;
    JFreeChart jfreechartMem = ChartFactory.createTimeSeriesChart("内存使用量统计", "时间", "内存使用所占百分数", xydatasetMem, true, true, true);
    XYPlot xyplotMem = (XYPlot) jfreechartMem.getPlot();
    xyplotMem.setBackgroundPaint(Color.black);      //设置背景颜色
    xyplotMem.setDomainGridlinePaint(Color.green);  //设置网格竖线颜色
    xyplotMem.setRangeGridlinePaint(Color.green);   //设置网格横线颜色
    XYLineAndShapeRenderer xylinerendererMem=(XYLineAndShapeRenderer)xyplotMem.getRenderer();
    xylinerendererMem.setSeriesPaint(0, new Color(0, 255 ,0 ));

    DateAxis dateaxisMem = (DateAxis) xyplotMem.getDomainAxis();//获取x轴
    dateaxisMem.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
//    SimpleDateFormat frm = new SimpleDateFormat("k:mm");
//    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 5,frm));
//    Calendar date = Calendar.getInstance();
//    date.set(2013, 2, 2,15,0);
//    Calendar mdate = Calendar.getInstance();
//    mdate.set(2013, 2, 2,15,60);
//    dateaxis.setRange(date.getTime(),mdate.getTime());


    // frame1=new ChartPanel(jfreechart,true);
    dateaxisMem.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
    dateaxisMem.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题

    ValueAxis rangeAxisMem = xyplotMem.getRangeAxis();//获取y轴
    rangeAxisMem.setRange(0, 100);
//    rangeAxis.setAutoRange(true);
    rangeAxisMem.setLabelFont(new Font("黑体",Font.BOLD,15));
    jfreechartMem.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
    jfreechartMem.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
    String filenameMem = ServletUtilities.saveChartAsPNG(jfreechartMem, 500, 360, null, session);
    String graphURLMem = request.getContextPath() + "/DisplayChart?filename=" + filenameMem;

%>

<html>
<head>
    <title>My JSP 'index.jsp' starting page</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
     <link rel="stylesheet" type="text/css" href="styles.css">
     -->

    <script language="javascript">
//        location.href="myjfreezhexian.jsp";
//        setTimeout("self.location.reload();",1000);
//        setTimeout("location.href='myjfreezhexian.jsp?flagts=1'",1000);
        setTimeout("document.myform.submit();",60000);
        var xmlHttp;
        if(window.ActiveXObject)
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        else if(window.XMLHttpRequest)
            xmlHttp=new XMLHttpRequest();
        var queryString = "DeviceAction_start.action";
        xmlHttp.onreadystatechange=handleStateChange;
        xmlHttp.open("GET",queryString);
        xmlHttp.send(null);
        function handleStateChange(){
            if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
            }
         }
    </script>
</head>

<body>
    <img src="<%= graphURL %>" width=500 height=360 border=0 >
    <img src="<%= graphURL2 %>" width=500 height=360 border=0 > </br>
    <img src="<%= graphURLMem %>" width=500 height=360 border=0 >

    <form name="myform" method="post" action="myjfreezhexian.jsp">
        <input type="hidden" name="flagts" value="1">
    </form>
</body>
</html>