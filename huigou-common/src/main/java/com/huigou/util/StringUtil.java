package com.huigou.util;

import static java.lang.Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS;
import static java.lang.Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS;
import static java.lang.Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT;
import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A;
import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

/**
 * 封装常用String类型工具方法
 * 
 * @author gongmm
 */
public class StringUtil {
    // 简体中文的编码范围从B0A1（45217）一直到F7FE（63486）
    private static final int CODE_BEGIN = 45217;

    private static final int CODE_END = 63486;

    /**
     * 按照声母表示，这个表是在GB2312中的出现的第一个汉字，也就是说“啊”是代表首字母a的第一个汉字。 i, u, v都不做声母,
     * 自定规则跟随前面的字母
     */
    private final static char[] CHINESE_LETTER = { '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖',
                                            '昔', '压', '匝', };

    /**
     * 二十六个字母区间对应二十七个端点 GB2312码汉字区间十进制表示
     */
    private final static  int[] ENGLISH_LETTER = new int[27];

    /**
     * 对应首字母区间表
     */
    private final static char[] INITIAL_TABLE = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w',
                                           'x', 'y', 'z', };

    static {
        for (int i = 0; i < 26; i++) {
            /* 得到GB2312码的首字母区间端点表，十进制 */
            ENGLISH_LETTER[i] = getChineseCode(CHINESE_LETTER[i]);
        }
        ENGLISH_LETTER[26] = CODE_END;// 区间表结尾
    }

    /**
     * 正则表达式匹配输入字符串
     */
    private static final Map<String, String> matcher = new HashMap<String, String>();
    static {
        matcher.put("email", "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        matcher.put("url",
                    "^(http|www|ftp|)?(://)?(//w+(-//w+)*)(//.(//w+(-//w+)*))*((://d+)?)(/(//w+(-//w+)*))*(//.?(//w)*)(//?)?(((//w*%)*(//w*//?)*(//w*:)*(//w*//+)*(//w*//.)*(//w*&)*(//w*-)*(//w*=)*(//w*%)*(//w*//?)*(//w*:)*(//w*//+)*(//w*//.)*(//w*&)*(//w*-)*(//w*=)*)*(//w*)*)$");
        matcher.put("ip",
                    "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");
        matcher.put("phone", "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
        matcher.put("number", "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
        matcher.put("letter", "^[A-Za-z0-9]+$");
    }

    public static Pattern numberPattern = Pattern.compile("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");

    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串 最重要的一个方法
     * 
     * @Title: getFirstLetter
     * @author
     * @Description: TODO 一个个字符读入、判断、输出
     * @param @param s
     * @param @return 字符
     * @return String 返回类型
     * @throws
     */
    public static String getFirstLetter(String s) {
        StringBuffer sb = new StringBuffer();
        int StrLength = s.length();
        int i;
        try {
            for (i = 0; i < StrLength; i++) {
                sb.append(charToInitial(s.charAt(i)));
            }
        } catch (Exception e) {
            return s;
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 输入字符,得到他的声母,英文字母返回对应的大写字母,其他非简体汉字返回 '0'
     * 
     * @Title: charToInitial
     * @author
     * @Description: TODO 获取拼音首字母
     * @param @param ch
     * @param @return 字符
     * @return char 返回类型
     * @throws
     */
    private static char charToInitial(char ch) {
        // 对英文字母的处理：小写字母转换为大写，大写的直接返回
        if (ch >= 'a' && ch <= 'z') return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z') return ch;
        // 对非英文字母的处理：转化为首字母，然后判断是否在码表范围内，
        // 若不是，则直接返回。
        // 若是，则在码表内的进行判断。
        int gb = getChineseCode(ch);// 汉字转换首字母
        if ((gb < CODE_BEGIN) || (gb > CODE_END)) // 在码表区间之前，直接返回
        return ch;
        int i;
        for (i = 0; i < 26; i++) {// 判断匹配码表区间，匹配到就break,判断区间形如“[,)”
            if ((gb >= ENGLISH_LETTER[i]) && (gb < ENGLISH_LETTER[i + 1])) break;
        }
        if (gb == CODE_END) {// 补上GB2312区间最右端
            i = 25;
        }
        return INITIAL_TABLE[i]; // 在码表区间中，返回首字母
    }

    /**
     * 取出汉字的编码
     * 
     * @Title: gbValue
     * @author
     * @Description: TODO 将一个汉字（GB2312）转换为十进制表示
     * @param @param ch 汉字
     * @param @return 设定
     * @return int 返回类型
     * @throws
     */
    private static int getChineseCode(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * ajax 转码
     * 
     * @Title: decode
     * @author
     * @Description: TODO ajax输入的字符都应该执行解码
     * @param @param input
     * @param @return 设定
     * @return String 返回类型
     * @throws
     */
    public static String decode(String input) {
        if (isBlank(input)) {
            return "";
        }
        try {
            return URLDecoder.decode(input, "utf-8").trim();
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * encode 编码
     * 
     * @Title: encode
     * @author
     * @Description: TODO
     * @param @param input
     * @param @return 设定
     * @return String 返回类型
     * @throws
     */
    public static String encode(String input) {
        if (isBlank(input)) {
            return "";
        }
        try {
            return URLEncoder.encode(input, "utf-8").replaceAll("\\+", "%20").trim();
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * 用,分割数字整数部分，如1234567.33，返回1,234,567.33
     * 
     * @Title: formatToCurrency
     * @author
     * @Description: TODO 输入BigDecimal 转换为字符串
     * @param @param b
     * @param @return 设定
     * @return String 返回类型
     * @throws
     */
    public static String formatToCurrency(BigDecimal b) {
        if (b != null) {
            return formatToCurrency(b.toString());
        }
        return "";
    }

    /**
     * 用,分割数字整数部分，如1234567.33，返回1,234,567.33
     * 
     * @Title: formatToCurrency
     * @author
     * @Description: TODO
     * @param @param str
     * @param @return
     * @return String
     * @throws
     */
    public static String formatToCurrency(String str) {
        if (isBlank(str)) {
            return "";
        }
        String tmp = keepDigit(str.trim(), 2, true);
        boolean flag = tmp.indexOf("-") >= 0;// 是否是负数
        tmp = flag ? tmp.substring(1, tmp.length()) : tmp;// 负数取绝对值部分

        int i = tmp.lastIndexOf(".");
        i = i < 0 ? tmp.length() - 3 : i - 3;

        while (i > 0) {
            tmp = tmp.substring(0, i) + "," + tmp.substring(i);
            i -= 3;
        }
        if (flag) {// 负数增加负号
            tmp = "-" + tmp;
        }
        return tmp;
    }

    /**
     * 设置小数位数
     * 
     * @param value
     *            String 需要设置位数的数字
     * @param i
     *            int 保留的小数位数
     * @param recruitZero
     *            boolean 小数位数不足时，是否补零
     * @return String
     */
    public static String keepDigit(String value, int i, boolean recruitZero) {
        if (!isBlank(value)) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(i, 4);
            if (recruitZero) {
                return bd.toString();
            } else {// 不补零，则去掉最后的无意义0以及无意义小数点
                String tmp = bd.toString();
                while (tmp.indexOf(".") > 0 && (tmp.endsWith("0") || tmp.endsWith("."))) {
                    tmp = tmp.substring(0, tmp.length() - 1);
                }
                return tmp;
            }
        } else {
            return null;
        }
    }

    /**
     * 字符串压缩
     * 
     * @Title: compress
     * @author
     * @Description: TODO
     * @param @param str
     * @param @return
     * @param @throws IOException
     * @return byte[]
     * @throws
     */
    public static byte[] compress(String str) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return out.toByteArray();
    }

    /**
     * 根据类型(type)验证输入(value)
     * 
     * @Title: isMatcher
     * @author
     * @Description: TODO 使用正则表达式验证输入
     * @param type
     * @param value
     * @return boolean
     * @throws
     */
    public static boolean isMatcher(String type, String value) {
        if (isBlank(value) || isBlank(type)) return true;
        String match = matcher.get(type);
        if (isBlank(match)) {
            match = type;
        }
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 判断是否为数字
     * 
     * @author
     * @param str
     * @return
     * @throws
     */
    public static boolean isNumber(Object str) {
        if (str == null || str.toString().equals("")) return false;
        return numberPattern.matcher(str.toString()).matches();
    }

    /**
     * 判断字符串是否为空
     * 
     * @param value
     * @return
     */
    public static boolean isBlank(String value) {
        if (null == value || "".equals(value.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static String blank(String str) {
        if (null == str) return "";
        if ("".equals(str.trim())) return "";
        return str;
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static int toInt(String s) {
        if (s == null || s.equals("")) return -1;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @Title: StringFilter
     * @Description: TODO 清除掉所有特殊字符
     * @param str
     * @return
     * @return String
     */
    public static String stringFilter(String str) {
        // 清除掉所有特殊字符
        if (!isBlank(str)) {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        }
        return str;

    }

    /**
     * 判断查询的sql语句中是否存在field字段
     * 
     * @Title: hasField
     * @author
     * @Description: TODO 通过正则表达式判断
     * @param sql
     * @param field
     * @return boolean
     * @throws
     */
    public static boolean hasField(String sql, String field) {
        String reg = "(\\s|,|.)(" + field.toUpperCase() + ")(,|\\s)";
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql.toUpperCase());
        return m.find();
    }

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串
     * 
     * @Title: underscoreName
     * @author
     * @Description: TODO
     * @param name
     * @return String
     */
    public static String getUnderscoreName(String name) {
        if (StringUtil.isBlank(name)) {
            return name;
        }
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线 checkCharContainChinese排除中文
                if (s.equals(s.toUpperCase()) && !s.equals(",") && !s.equals("_") && !s.trim().equals("") && !Character.isDigit(s.charAt(0))
                    && !checkCharContainChinese(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 根据DB column name 得到属性名
     * 
     * @Title: getHumpName
     * @author
     * @Description: TODO 转换为驼峰命名
     * @param columnName
     * @return String
     */
    public static String getHumpName(String columnName) {
        if (columnName == null || columnName.length() == 0) {
            return "";
        }
        columnName = columnName.toLowerCase();
        StringBuffer sb = new StringBuffer();
        String regex = "_";
        String addString = "";
        String[] sbArr = columnName.split(regex);
        if (sbArr.length == 1 && sbArr[0].length() == 1) {
            sb.append(sbArr[0]);
        } else {
            for (int i = 0; i < sbArr.length; i++) {
                if (i == 0) {
                    if (sbArr[i].length() == 1) {
                        addString = sbArr[i].toUpperCase();
                    } else {
                        addString = sbArr[i];
                    }
                } else {
                    if (sbArr[i].length() == 1) {
                        addString = sbArr[i].toUpperCase();
                    } else {
                        addString = sbArr[i].substring(0, 1).toUpperCase() + sbArr[i].substring(1, sbArr[i].length());
                    }
                }
                sb.append(addString);
            }
        }
        return sb.toString();
    }

    public static String clobToString(Clob clob) throws SQLException, IOException {
        Reader reader = clob.getCharacterStream();
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer stringBuffer = new StringBuffer();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            stringBuffer.append(s);
        }
        bufferedReader.close();
        reader.close();
        return stringBuffer.toString();
    }

    /**
     * 判断输入的字符串是否是数字
     * 
     * @author
     * @param str
     * @return
     * @throws
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) return false;
        }
        return true;
    }

    /**
     * 通过正则表达式按名称替换输入字符串
     * 
     * @author
     * @param s
     * @param nameKeys
     * @return
     * @throws Exception
     *             String
     */
    public static String patternParser(String s, Map<?, ?> map) throws Exception {
        if (StringUtil.isBlank(s)) {
            return "";
        }
        String name = "", value = "";
        StringBuffer sb = new StringBuffer();
        boolean result = false;
        // 非贪婪匹配
        String reg = "\\{(.*?)\\}";
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        result = m.find();
        while (result) {
            if (m.group(1) != null) {
                name = m.group(1).trim();
                value = ClassHelper.convert(map.get(name), String.class);
                if (value != null) {
                    m.appendReplacement(sb, value);
                } else {
                    m.appendReplacement(sb, "");
                }
            }
            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 通过正则表达式顺序替换输入字符串
     * 
     * @author
     * @param s
     * @param list
     * @return
     * @throws Exception
     *             String
     */
    public static String patternParser(String s, List<Object> list) throws Exception {
        if (StringUtil.isBlank(s)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        boolean result = false;
        // 非贪婪匹配
        String reg = "\\{([0-9]+)\\}";
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        result = m.find();
        String value = null;
        int index = 0, length = list.size();
        while (result) {
            if (m.group(1) != null) {
                index = ClassHelper.convert(m.group(1).trim(), Integer.class);
                if (index < length) {
                    value = ClassHelper.convert(list.get(index), String.class);
                } else {
                    value = null;
                }
                if (value != null) {
                    m.appendReplacement(sb, value);
                } else {
                    m.appendReplacement(sb, "");
                }
            }
            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字符大写
     * 
     * @author
     * @param str
     * @return String
     */
    public static String initUpper(String str) {
        String tmp = str.length() > 1 ? str.substring(1) : "";
        return str.substring(0, 1).toUpperCase() + tmp;
    }

    /**
     * 首字符小写
     * 
     * @author
     * @param str
     * @return String
     */
    public static String initLower(String str) {
        String tmp = str.length() > 1 ? str.substring(1) : "";
        return str.substring(0, 1).toLowerCase() + tmp;
    }

    /**
     * 数字金额大写转换
     */
    public static String digitUppercase(BigDecimal n) {
        if (n == null) {
            return "";
        }
        String fraction[] = { "角", "分" };
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };

        String head = n.signum() < 0 ? "负" : "";
        n = n.abs();

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            // s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
            BigDecimal cn = n.movePointRight(i + 1).setScale(0, BigDecimal.ROUND_FLOOR).remainder(new BigDecimal("10"));
            s += (digit[cn.intValue()] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        // int integerPart = (int) Math.floor(n);
        BigDecimal integerPart = n.setScale(0, BigDecimal.ROUND_FLOOR);

        for (int i = 0; i < unit[0].length && integerPart.signum() > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n.signum() > 0; j++) {
                p = digit[integerPart.remainder(new BigDecimal("10")).intValue()] + unit[1][j] + p;
                integerPart = integerPart.divide(new BigDecimal("10"));
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }

    /**
     * 校验一个字符串是否有含有中文
     * 
     * @param checkStr
     * @return
     */
    public static boolean checkStringContainChinese(String checkStr) {
        if (StringUtil.isNotBlank(checkStr)) {
            char[] checkChars = checkStr.toCharArray();
            for (int i = 0; i < checkChars.length; i++) {
                char checkChar = checkChars[i];
                if (checkCharContainChinese(checkChar)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否为中文字符
     * 
     * @param checkChar
     * @return
     */
    public static boolean checkCharContainChinese(char checkChar) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        if (CJK_UNIFIED_IDEOGRAPHS == ub || CJK_COMPATIBILITY_IDEOGRAPHS == ub || CJK_COMPATIBILITY_FORMS == ub || CJK_RADICALS_SUPPLEMENT == ub
            || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub) {
            return true;
        }
        return false;
    }

    /**
     * 将字符串转成ASCII的java方法
     * 
     * @author
     * @param value
     * @return
     * @throws
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * 将ASCII转成字符串的java方法
     * 
     * @author
     * @param value
     * @return
     * @throws
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 返回不为空的字符串
     * 
     * @param args
     * @return
     */
    public static String tryThese(String... args) {
        for (String v : args) {
            if (StringUtil.isNotBlank(v)) {
                return v;
            }
        }
        return null;
    }

    /**
     * 防止xss跨脚本攻击（替换，根据实际情况调整）
     * 
     * @param value
     * @return
     */
    public static String stripXSS(String value) {
        if (value == null || value.trim().equals("")) return value;
        // Avoid anything between script tags
        // Avoid null characters
        value = value.replaceAll("", "");
        // Avoid anything between script tags
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // Remove any lonesome <script ...> tag
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid eval(...) e­xpressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid e­xpression(...) e­xpressions
        scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid javascript:... e­xpressions
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid vbscript:... e­xpressions
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // Avoid onload= e­xpressions
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+",
                                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = scriptPattern.matcher(value);
        while (matcher.find()) {
            value = matcher.replaceAll("＜" + matcher.group().replaceAll("<", "").replaceAll(">", "") + "＞");
        }
        scriptPattern = Pattern.compile("(window\\.location|document\\.cookie|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                                                                                                                  | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        return value;
    }

    /**
     * Test if the given String starts with the specified prefix, ignoring
     * upper/lower case.
     * 
     * @param str
     *            the String to check
     * @param prefix
     *            the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * Find if exist searchStr in str ignore case
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;
        final int length = searchStr.length();
        if (length == 0) return true;
        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) return true;
        }
        return false;
    }

    /**
     * Replace all sub strings ignore case <br/>
     * replaceIgnoreCase("AbcDECd", "Cd", "FF") = "AbFFEFF"
     */
    public static String replaceIgnoreCase(String text, String findtxt, String replacetxt) {
        if (text == null) return null;
        String str = text;
        if (findtxt == null || findtxt.length() == 0) {
            return str;
        }
        if (findtxt.length() > str.length()) {
            return str;
        }
        int counter = 0;
        String thesubstr;
        while ((counter < str.length()) && (str.substring(counter).length() >= findtxt.length())) {
            thesubstr = str.substring(counter, counter + findtxt.length());
            if (thesubstr.equalsIgnoreCase(findtxt)) {
                str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
                counter += replacetxt.length();
            } else {
                counter++;
            }
        }
        return str;
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     * 
     * @param originString
     *            The original String
     * @param oldPattern
     *            old String Pattern to replace
     * @param newPattern
     *            new String pattern to insert
     * @return a String with the replacements
     */
    public static String replace(String originString, String oldPattern, String newPattern) {
        if (!hasLength(originString) || !hasLength(oldPattern) || newPattern == null) {
            return originString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int index = originString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(originString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = originString.indexOf(oldPattern, pos);
        }
        sb.append(originString.substring(pos));
        return sb.toString();
    }

}
