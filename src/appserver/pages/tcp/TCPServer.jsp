<%@include file="/include/common.jsp"%>
    <head>
        <title>网络地址</title>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/tcp/server.js"></script>
    </head>
    <body>

        <select name="light" id="light" style="display: none;">
        <option value="net">net</option>
        <option value="bridge1">bridge1</option>
        <option value="bridge2">bridge2</option>
        <option value="bond1">bond1</option>
        <option value="bond2">bond2</option>
    </select>
    <div id="editor-grid"></div>
    </body>
