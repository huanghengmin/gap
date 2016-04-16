package com.inetec.ichange.service.monitor.syslog.format;

public class LogFormatFactory {
    public static String S_LogType_KoalVpn = "KoalVpn";
    public static String S_LogType_KoalTbsg = "KoalTbsg";
    public static String S_LogType_NetChinaFirewall = "netchinafirewall";
    public static String S_LogType_NetChinaGap = "netchinagap";

    public static boolean checkFormat(String log) {

        return false;
    }

    public static ILogFormat getLogFormat(String log,String leve) {
        ILogFormat result = new SysLog();

        if (new VpnLinkStatusLog().validate(log)) {
            result = new VpnLinkStatusLog();
            result.process(log,leve);
        }
        if (result instanceof SysLog) {
            result.process(log,leve);
        }
        return result;
    }


}
