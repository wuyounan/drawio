package com.huigou.util;

import java.util.Date;

/**
 * IP地址解析
 * 
 * @Title: IPRegExp.java
 * @Description: TODO 支持 IPv4 格式： <br>
 *               1. n.n.n.n/maskbit, such as 192.168.1.0/28, 10.213.4.0/24, ...
 *               2. single ip, such as 192.168.1.100, 10.213.4.250, ...
 * @author 网络
 * @date 2013-10-15 下午04:17:05
 * @version V1.0
 */
public class IPRegExp {
	private String reg;
	private Long address = 0L;
	private Long mask = 0L;
	private String timebegin;
	private String timeend;

	public String getTimebegin() {
		return timebegin;
	}

	public void setTimebegin(String timebegin) {
		this.timebegin = timebegin;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public Long ipStringToLong(String ip) {
		if (ip == null)
			return null;
		/**
		 * “ . ”在正则表达式中有特殊的含义,这里需要转义
		 * **/
		String[] ss = ip.split("\\.");
		if (ss.length != 4)
			return null;
		try {
			Long ret = new Long(Long.parseLong(ss[0])
					* Math.round(Math.pow(2, 24)) + Long.parseLong(ss[1])
					* Math.round(Math.pow(2, 16)) + Long.parseLong(ss[2])
					* Math.round(Math.pow(2, 8)) + Long.parseLong(ss[3]));
			return ret;
		} catch (Exception e) {
			return 0L;
		}
	}

	public IPRegExp parse(String rep) {
		this.reg = rep;
		if (rep == null)
			return null;
		String str = rep.trim();
		if (str.equals(""))
			return null;
		int p = str.indexOf('/');
		if (p > -1) {
			String addr = str.substring(0, p);
			String mask = str.substring(p + 1);
			this.address = ipStringToLong(addr);
			this.mask = Long.valueOf(mask);
			long lmask = 0;
			for (long i = 31; i > (32 - this.mask - 1); i--) {
				lmask = lmask + Math.round(Math.pow(2, i));
			}
			this.mask = lmask;
		} else {
			this.address = ipStringToLong(str);
			this.mask = new Long(0xffffffff);
		}
		if (this.address == null || this.mask == null)
			return null;
		this.address = this.address & this.mask;
		return this;
	}

	public boolean match(String ip) {
		return match(ipStringToLong(ip));
	}

	/**
	 * IP校验
	 * 
	 * @Title: match
	 * @author 
	 * @Description: TODO
	 * @param @param l
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean match(Long l) {
		if (l == null)
			return false;
		long laddress = l & this.mask;
		boolean flag = this.address.equals(laddress);
		if (flag) {// IP地址验证通过 验证登录时间
			Long nowtime = System.currentTimeMillis();// 当前时间
			String dateStr = DateUtil.getDateFormat(DateUtil.getDate());
			if (timebegin != null && !timebegin.equals("")) {
				String dateTime = dateStr + " " + timebegin + ":00";
				try {
					Date d = DateUtil.getDateParse(3, dateTime);
					if (d.getTime() > nowtime)
						return false;
				} catch (Exception e) {
					LogHome.getLog(this).error(e);
				}

			}
			if (timeend != null && !timeend.equals("")) {
				String dateTime = dateStr + " " + timeend + ":00";
				try {
					Date d = DateUtil.getDateParse(3, dateTime);
					if (d.getTime() < nowtime)
						return false;
				} catch (Exception e) {
					LogHome.getLog(this).error(e);
				}
			}
		}
		return flag;
	}

	public int hashCode() {
		return address.hashCode() * 198001 + mask.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof IPRegExp))
			return false;
		IPRegExp rhs = (IPRegExp) o;
		return address.equals(rhs.address) && mask.equals(rhs.mask);
	}

	public String toSting() {
		return new StringBuffer().append("[reg=" + reg)
				.append(",address=" + address).append(",mask=" + mask)
				.append("]").toString();
	}
}
