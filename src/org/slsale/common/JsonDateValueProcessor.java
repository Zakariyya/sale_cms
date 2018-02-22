package org.slsale.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * JsonDateValueProcessor
 * JSON 日期格式处理
 *  (java转化json)
 * @author anan
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor{

	/**
	 * 日期模式
	 * datePattern
	 */
	private String datePattern = "yyyy-MM-dd";
	
	/**
	 * JsonDateValueProcessor
	 */
	public JsonDateValueProcessor() {
		super();
	}

	public JsonDateValueProcessor(String format){
		this.datePattern = format;
	}
	
	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		// TODO Auto-generated method stub
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		// TODO Auto-generated method stub
		return process(value);
	}
	
	/**
     * process
     * @param value
     * @return
     */
    private Object process(Object value) {
        try {
            if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern,
                        Locale.UK);
                return sdf.format((Date) value);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * @return the datePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * @param pDatePattern the datePattern to set
     */
    public void setDatePattern(String pDatePattern) {
        datePattern = pDatePattern;
    }
	
	
}
