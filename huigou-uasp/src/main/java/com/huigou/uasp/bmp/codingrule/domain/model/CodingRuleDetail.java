package com.huigou.uasp.bmp.codingrule.domain.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.uasp.bmp.codingrule.application.DateTimeUtil;
import com.huigou.uasp.bmp.common.application.ListEnumData;
import com.huigou.util.StringUtil;

@Entity
@Table(name = "SA_codingRuleDetail")
public class CodingRuleDetail extends AbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -6947354059187207525L;

    /**
     * 编码规则_id
     **/
    @Column(name = "codingRule_id", length = 32)
    private String codingRuleId;

    /**
     * 属性类别
     **/
    @Column(name = "attribute_kind", length = 32)
    private String attributeKind;

    /**
     * 值属性
     **/
    @Column(name = "attribute_value", length = 128)
    private String attributeValue;

    /**
     * 属性使用方式
     **/
    @Column(name = "attribute_use_kind", length = 128)
    private String attributeUseKind;

    /**
     * 格式
     **/
    @Column(name = "format", length = 32)
    private String format;

    /**
     * 步长
     **/
    @Column(name = "step", length = 22)
    private Integer step;

    /**
     * 初始值
     **/
    @Column(name = "initial_value", length = 22)
    private Integer initialValue;

    /**
     * 长度
     **/
    @Column(name = "length", length = 22)
    private Integer length;

    /**
     * 是否可见
     **/
    @Column(name = "is_display", length = 22)
    private Integer isDisplay;

    /**
     * 补位符
     **/
    @Column(name = "fill_sign", length = 1)
    private String fillSign;

    /**
     * 补位方向
     **/
    @Column(name = "fill_sign_direction", length = 32)
    private String fillSignDirection;

    /**
     * 截取位置
     **/
    @Column(name = "intercept_pos", length = 22)
    private Integer interceptPos;

    /**
     * 截取方向
     **/
    @Column(name = "intercept_direction", length = 32)
    private String interceptDirection;

    /**
     * 截取长度
     **/
    @Column(name = "intercept_length", length = 22)
    private Integer interceptLength;

    /**
     * 是否段间分隔符
     **/
    @Column(name = "is_use_delimiter", length = 22)
    private Integer isUseDelimiter;

    /**
     * 分类排序
     **/
    @Column(name = "is_sort_by_item", length = 22)
    private Integer isSortByItem;

    /**
     * 描述
     **/
    @Column(name = "description", length = 512)
    private String description;

    /**
     * 排序号
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    public String getCodingRuleId() {
        return this.codingRuleId;
    }

    public void setCodingRuleId(String codingRuleId) {
        this.codingRuleId = codingRuleId;
    }

    public String getAttributeKind() {
        return this.attributeKind;
    }

    public void setAttributeKind(String attributeKind) {
        this.attributeKind = attributeKind;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeUseKind() {
        return this.attributeUseKind;
    }

    public void setAttributeUseKind(String attributeUseKind) {
        this.attributeUseKind = attributeUseKind;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getStep() {
        return this.step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getIsDisplay() {
        return this.isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getFillSign() {
        return this.fillSign;
    }

    public void setFillSign(String fillSign) {
        this.fillSign = fillSign;
    }

    public String getFillSignDirection() {
        return this.fillSignDirection;
    }

    public void setFillSignDirection(String fillSignDirection) {
        this.fillSignDirection = fillSignDirection;
    }

    public Integer getInterceptPos() {
        return this.interceptPos;
    }

    public void setInterceptPos(Integer interceptPos) {
        this.interceptPos = interceptPos;
    }

    public String getInterceptDirection() {
        return this.interceptDirection;
    }

    public void setInterceptDirection(String interceptDirection) {
        this.interceptDirection = interceptDirection;
    }

    public Integer getInterceptLength() {
        return this.interceptLength;
    }

    public void setInterceptLength(Integer interceptLength) {
        this.interceptLength = interceptLength;
    }

    public Integer getIsUseDelimiter() {
        return this.isUseDelimiter;
    }

    public void setIsUseDelimiter(Integer isUseDelimiter) {
        this.isUseDelimiter = isUseDelimiter;
    }

    public Integer getIsSortByItem() {
        return this.isSortByItem;
    }

    public void setIsSortByItem(Integer isSortByItem) {
        this.isSortByItem = isSortByItem;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public AttributeKind getAttributeKindEnum() {
        return AttributeKind.fromName(this.attributeKind);
    }

    public DirectionKind getFillSignDirectionEnum() {
        return DirectionKind.fromName(this.fillSignDirection);
    }

    public enum AttributeKind implements ListEnumData {
        SERIAL_NUMBER("SERIAL_NUMBER", "序列号"),
        SYSTEM_TIME("SYSTEM_TIME", "系统时间"),
        ATTRIBUTE("ATTRIBUTE", "属性值"),
        WHOLE("WHOLE", "系统属性"),
        FIXED_VALUE("FIXED_VALUE", "固定值"),
        CUSTOM("CUSTOM", "自定义值");

        private final String id;

        private final String displayName;

        private AttributeKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public static AttributeKind fromName(String name) {
            // 1、转换
            for (AttributeKind item : AttributeKind.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
            // 2、不能转换
            throw new IllegalArgumentException("无效的编码规则属性类别 : " + name);
        }

        public static Map<String, String> getData() {
            Map<String, String> result = new LinkedHashMap<String, String>(6);
            for (AttributeKind item : AttributeKind.values()) {
                result.put(item.getId(), item.getDisplayName());
            }
            return result;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DirectionKind implements ListEnumData {
        FORWARD("FORWARD", "向前"), BACKWARD("BACKWARD", "向后"), NONE("NONE", "无");

        private final String id;

        private final String displayName;

        private DirectionKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public static DirectionKind fromName(String name) {
            if (StringUtil.isBlank(name)){
                return NONE;
            }
            // 1、转换
            for (DirectionKind item : DirectionKind.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
            // 2、不能转换
            throw new IllegalArgumentException("无效的编码规则方向类别 : " + name);
        }

        public static Map<String, String> getData() {
            Map<String, String> result = new LinkedHashMap<String, String>(3);
            for (DirectionKind item : DirectionKind.values()) {
                if (item != NONE){
                    result.put(item.getId(), item.getDisplayName());
                }
            }
            return result;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }
        
        public boolean isForward(){
            return DirectionKind.FORWARD.equals(this);
        }
        
        public boolean isBackward(){
            return DirectionKind.BACKWARD.equals(this);
        }

    }

    public enum AttributeUseKind implements ListEnumData {
        FULL("FULL", "全部"), PART("PART", "部分"), REFERENCE("REFERENCE", "引用");
        private final String id;

        private final String displayName;

        private AttributeUseKind(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public static AttributeUseKind fromName(String name) {
            // 1、转换
            for (AttributeUseKind item : AttributeUseKind.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
            // 2、不能转换
            throw new IllegalArgumentException("无效的属性使用类别: " + name);
        }

        public static Map<String, String> getData() {
            Map<String, String> result = new LinkedHashMap<String, String>(3);
            for (AttributeUseKind item : AttributeUseKind.values()) {
                result.put(item.getId(), item.getDisplayName());
            }
            return result;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Format implements ListEnumData {
        NONE;
        private final static String[] formats = DateTimeUtil.getTimeFormat(new Date());

        public static Map<String, String> getData() {
            Map<String, String> result = new LinkedHashMap<String, String>(30);
            for (int i = 0; i < formats.length; i++) {
                result.put(String.valueOf(i), formats[i]);
            }
            return result;
        }
    }

}
