package com.hzih.gap;

import com.hzih.gap.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class GapType {

    private static final String path = StringContext.systemPath +"/repository/app.xml";

    //private static final String path = "F:/stp/repository/app.xml";

    private static Logger logger = Logger.getLogger(GapType.class);

    private static String GAP_BACK = "后置网闸";

    private static String GAP_PRE = "前置网闸";

    private static String getGapType(){
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        Element root = document.getRootElement();
        return root.attribute("type").getValue();
    }

    public static boolean checkGaptypeIsBack(){

        if(getGapType().equals(GAP_BACK)){
            return true;
        }
        return false;

    }

}
