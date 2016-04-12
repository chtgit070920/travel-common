package com.travel.common.ws;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.travel.common.utils.DateUtil;

/**
 * 
 * @Description: cxf 传输java.util.Date时会自动包装为xml日期类型，故需进行转换；
 *                     请在model中get/set方法上加注解：@XmlJavaTypeAdapter(DateXmlAdapter)
 */
public class DateXmlAdapter extends XmlAdapter<String,Date>{

	@Override
	public Date unmarshal(String v) throws Exception {
		return DateUtil.string_date(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		return DateUtil.date_string(v);
	}

}
