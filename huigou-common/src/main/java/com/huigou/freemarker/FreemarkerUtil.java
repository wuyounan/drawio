package com.huigou.freemarker;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * 读取系统中freemark模板
 * 
 * @ClassName: FreemarkerUtil
 * @author xx
 */

public class FreemarkerUtil {
    private static FreeMarkerConfigurer freeMarkerConfigurer;

    private final static BeansWrapper wrapper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();

    private final static TemplateHashModel staticModels = wrapper.getStaticModels();

    @SuppressWarnings("static-access")
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    /**
     * 静态方法调用
     * 
     * @param packname
     * @return
     */
    protected static TemplateHashModel useStaticPacker(String packname) {
        TemplateHashModel fileStatics = null;
        try {
            fileStatics = (TemplateHashModel) staticModels.get(packname);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return fileStatics;
    }

    /**
     * 加载模板组合HTML
     * 
     * @author
     * @param template
     * @param variables
     * @return
     * @throws Exception
     *             String
     */
    public static String generate(String template, Map<String, Object> variables) throws Exception {
        Template tp = freeMarkerConfigurer.getConfiguration().getTemplate(template, "utf-8");
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        // 静态方法添加到环境中
        variables.put("MessageSourceContext", useStaticPacker("com.huigou.context.MessageSourceContext"));
        tp.process(variables, writer);
        String htmlStr = stringWriter.toString();
        writer.flush();
        writer.close();
        return htmlStr;
    }

}