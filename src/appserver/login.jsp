<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html;charset=utf-8" import="com.hzih.gap.web.SiteContext"%>
<%@include file="/taglib.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>管理中心</title>

        <script language="JavaScript">
            function reloadVerifyCode() {
                document.getElementById('verifyCodeImg').src = "<c:url value="/RandomCodeCtrl"/>"+"?tmp="+Math.random(100);
            }
            function checkForm(){
                var form = document.forms.loginForm;
                if(form.name.value==null||form.name.value.length==0){
                    alert("用户名不能为空！");
                    return false;
                }
                var password = form.pwd.value;
                if(password==null||password.length==0){
                    alert("密码不能为空！");
                    return false;
                }
                if(form.vcode.value==null||form.vcode.value.length==0){
                    alert("验证码不能为空！");
                    return false;
                } else if(form.vcode.value.length > 4){
                    alert("验证码不能大于4位！");
                    return false;
                } else if(form.vcode.value.length < 4){
                    alert("验证码不能小于4位！");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body style="background-color: #0161B7;text-align: center">

        <div style=" vertical-align: middle; width: 800px; height: 450px; border: 0px solid #000000; margin: 0 auto;
            margin-top: 50px;padding: 300px 0 0 200px; background: url(img/login.png) no-repeat;">

            <div style="padding: 100px 0 0 350px;border: 0px solid #acd67a;">
                <form name="loginForm" action="login.action" method="post" style="margin: 0; padding: 0;border:0;" onsubmit="return checkForm();">
                    <table cellspacing=3 cellpadding=0 border=0 id=myTable>
                        <tr>
                            <td align=right>&nbsp;用户名：</td>
                            <td colspan="1"><input type="text" name="name" autocomplete="off" style="width:140px;" /></td>
                        </tr>
                        <tr>
                            <td align=right>&nbsp;密&nbsp;&nbsp;码：</td>
                            <td colspan="1"><input type="password" name="pwd" autocomplete="off" style="width:140px;" /></td>
                        </tr>
                        <tr>
                            <td align=right>&nbsp;验证码：</td>
                            <td><INPUT TYPE="text" autocomplete="off" NAME="vcode" style="width: 80px;"><img
                                src="<c:url value="/RandomCodeCtrl"/>" height="23" width="60"
                                align="middle" id="verifyCodeImg" onclick="reloadVerifyCode();"
                                alt="单击更换验证码" /></td>
                        </tr>
                        <tr>
                            <td/>
                            <td colspan="1"><input type="submit" value="登&nbsp;&nbsp;录" />&nbsp;&nbsp;<input
                                type="reset" value="取&nbsp;&nbsp;消" /></td>
                        </tr>
                    </table>
                </form>
            </div>

        </div>
    </body>
</html>

