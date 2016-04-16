package com.inetec.ichange.service.utils;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.*;
import org.apache.log4j.Category;

import java.io.InputStream;
import java.io.IOException;


import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;


public class XmlSaxParser extends DefaultHandler {

    /**
     * Validation feature id (http://xml.org/sax/features/validation).
     */
    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
    /**
     * LeExical handler property id (http://xml.org/sax/properties/lexical-handler).
     */
    protected static final String LEExICAL_HANDLER_PROPERTY_ID = "http://xml.org/sax/properties/lexical-handler";

    /**
     * Default parser name.
     */
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    private final static Category m_logger = Category.getInstance(XmlSaxParser.class);
    private TypeStatusSet m_rows = new TypeStatusSet();
    private TypeStatus m_curRow;
    private String m_lastData = "";
    private String m_external = "";
    private String m_internal = "";
    private String m_targets = "";
    private String m_source = "";
    private String m_target = "";
    private DbInfo m_curDbInfo = new DbInfo();


    //Constructor
    public XmlSaxParser() {
        super();
    }

    public void init() {
        m_rows = new TypeStatusSet();
        m_curRow = null;
        m_lastData = "";
        m_external = "";
        m_internal = "";
        m_targets = "";
        m_source = "";
        m_target = "";
        m_curDbInfo = new DbInfo();
    }

    public TypeStatusSet parse(InputStream is) throws Ex {
        init();
        try {
            XMLReader ExmlReader = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
            ExmlReader.setFeature(VALIDATION_FEATURE_ID, false);
            ExmlReader.setContentHandler(this);
            ExmlReader.setErrorHandler(this);
            InputSource inputSource = new InputSource(is);
            ExmlReader.parse(inputSource);
        } catch (IOException io) {
            throw new Ex().set(E.E_IOException, io);
        } catch (SAXException eExc) {
            throw new Ex().set(eExc);
        }
        return m_rows;
    }

    //Response the startDocument event
    public void startDocument() {
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("start document");
        }
    }

    public void endDocument() {
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("end document");
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("satart element:" + qName);
        }
        if (qName.equalsIgnoreCase("type")) {
            m_curRow = new TypeStatus();
            m_curRow.setName(attrs.getValue("value"));
            m_curRow.setDesc(attrs.getValue("desc"));
        }
        if (qName.equalsIgnoreCase("external")) {
            m_external = "external";
        }
        if (qName.equalsIgnoreCase("internal")) {
            m_internal = "internal";
        }
        if (qName.equalsIgnoreCase("targets")) {
            m_targets = "targets";
        }
        if (qName.equalsIgnoreCase("source")) {
            m_source = "source";
        }
        if (qName.equalsIgnoreCase("target")) {
            m_target = "target";
        }
        if (qName.equalsIgnoreCase("source_db")) {
            m_curDbInfo.setName(attrs.getValue("value"));
            m_curDbInfo.setDesc(attrs.getValue("desc"));
        }
        if (qName.equalsIgnoreCase("target_db")) {
            m_curDbInfo.setName(attrs.getValue("value"));
            m_curDbInfo.setDesc(attrs.getValue("desc"));
        }

    }

    public void characters(char[] ch, int start, int length) {
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("read data:" + new String(ch, start, length));
        }
        m_lastData = m_lastData + new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("end element:" + qName);
        }
        if (qName.equalsIgnoreCase("type")) {
            m_rows.setTypeStatus(m_curRow);
        }
        if (qName.equalsIgnoreCase("external")) {
            m_external = "";
        }
        if (qName.equalsIgnoreCase("internal")) {
            m_internal = "";
        }
        if (qName.equalsIgnoreCase("targets")) {
            m_targets = "";
        }
        if (qName.equalsIgnoreCase("source")) {
            m_source = "";
        }
        if (qName.equalsIgnoreCase("target")) {
            m_target = "";
        }
        if (qName.equalsIgnoreCase("source_status")) {
            int len = m_lastData.length();
            //m_lastData = m_lastData.trim();
            if (m_curDbInfo != null) {
                if (!m_lastData.equals("")) {
                    if (len >= 1) {

                        m_lastData = m_lastData.substring(0, len);
                        if (m_lastData.equals("2"))
                            m_curDbInfo.setCode("0");
                        else
                            m_curDbInfo.setCode(m_lastData);

                    } else {
                        m_lastData = "";
                        m_curDbInfo.setCode("-1");
                    }
                } // else {}
            }  // else {}
            m_lastData = "";
            if (m_source != null) {
                if (m_external.equals("external")) {
                    m_curRow.setExternalSourceDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();

                }
                if (m_internal.equals("internal")) {
                    m_curRow.setInternalSourceDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();

                }
            }
        }

        if (qName.equalsIgnoreCase("source_info")) {
            // if (!m_curField.isNull()) {
            int len = m_lastData.length();
            //m_lastData = m_lastData.trim();
            if (m_curDbInfo != null) {
                if (!m_lastData.equals("")) {
                    if (len >= 1) {
                        m_lastData = m_lastData.substring(0, len);
                        if (m_lastData.equals("CONFIGOK"))
                            m_curDbInfo.setInfo("OKAY");
                        else
                            m_curDbInfo.setInfo(m_lastData);
                    } else {
                        m_lastData = "";
                        m_curDbInfo.setInfo("");
                    }
                } // else {}
            }
            if (m_curDbInfo.getCode() != null) {
                if (m_external.equals("external")) {
                    m_curRow.setExternalSourceDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }
                if (m_internal.equals("internal")) {
                    m_curRow.setInternalSourceDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }

            }
            m_lastData = "";
        }
        if (qName.equalsIgnoreCase("target_status")) {
            int len = m_lastData.length();
            //m_lastData = m_lastData.trim();
            if (m_curDbInfo != null) {
                if (!m_lastData.equals("")) {
                    if (len >= 1) {
                        m_lastData = m_lastData.substring(0, len);
                        if (m_lastData.equals("2"))
                            m_curDbInfo.setCode("0");
                        else
                            m_curDbInfo.setCode(m_lastData);
                    } else {
                        m_lastData = "";
                        m_curDbInfo.setCode("-1");
                    }
                } // else {}
            }  // else {}
            m_lastData = "";
            if (m_curDbInfo.getInfo() != null) {
                if (m_external.equals("external")) {
                    m_curRow.setExternalTargetDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }
                if (m_internal.equals("internal")) {
                    m_curRow.setInternalTargetDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }
            }
        }

        if (qName.equalsIgnoreCase("target_info")) {
            // if (!m_curField.isNull()) {
            int len = m_lastData.length();
            //m_lastData = m_lastData.trim();
            if (m_curDbInfo != null) {
                if (!m_lastData.equals("")) {
                    if (len >= 1) {
                        m_lastData = m_lastData.substring(0, len);
                        m_curDbInfo.setInfo(m_lastData);
                        if (m_lastData.equals("CONFIGOK"))
                            m_curDbInfo.setInfo("OKAY");
                        else
                            m_curDbInfo.setInfo(m_lastData);
                    } else {
                        m_lastData = "";
                        m_curDbInfo.setInfo("");
                    }
                } // else {}
            }
            if (m_curDbInfo.getCode() != null) {
                if (m_external.equals("external")) {
                    m_curRow.setExternalTargetDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }
                if (m_internal.equals("internal")) {
                    m_curRow.setInternalTargetDb(m_curDbInfo);
                    m_curDbInfo = new DbInfo();
                }

            }
            m_lastData = "";
        }

    }

    public void fatalError(SAXParseException e) {
        m_logger.error("faltal error of xml sax parser.", e);
    }

    public void error(SAXParseException e) {
        m_logger.error("error of xml sax parser.", e);
    }

    public void warning(SAXParseException e) {
        m_logger.warn("faltal error of xml sax parser.", e);
    }
}
