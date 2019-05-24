package com.huigou.uasp.handlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;

/**
 * spring mvc 请求路径响应Mapping
 * 
 * @author xx
 *         替换spring mvc 原有注解扫描功能，在Controller类上使用注解ControllerMapping后自动将类中方法加入到RequestMapping
 */
public class UaspRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = null;
        RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (methodAnnotation != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            info = createRequestMappingInfo(methodAnnotation, methodCondition);
            ControllerMapping controllerMapping = AnnotationUtils.findAnnotation(handlerType, ControllerMapping.class);
            if (controllerMapping != null) {
                info = createRequestMappingInfo(controllerMapping, handlerType).combine(info);
            } else {
                RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
                if (typeAnnotation != null) {
                    RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                    info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
                }
            }
        } else {
            ControllerMethodMapping controllerMethodMapping = AnnotationUtils.findAnnotation(method, ControllerMethodMapping.class);
            if (controllerMethodMapping != null) {
                info = createRequestMappingInfo(controllerMethodMapping, method);
                if (controllerMethodMapping.combine()) {// 是否需要结合类注解路径 默认false
                    ControllerMapping controllerMapping = AnnotationUtils.findAnnotation(handlerType, ControllerMapping.class);
                    if (controllerMapping != null) {
                        info = createRequestMappingInfo(controllerMapping, handlerType).combine(info);
                    }
                }
            } else {
                Class<?> returnType = method.getReturnType();
                Class<?>[] parametertypes = method.getParameterTypes();
                // public类型，返回是字符串，自动映射格式 XXXX/XXXMethod.do
                if (Modifier.isPublic(method.getModifiers()) && returnType.equals(java.lang.String.class) && parametertypes.length == 0) {
                    info = createRequestMappingInfo(method);
                    ControllerMapping controllerMapping = AnnotationUtils.findAnnotation(handlerType, ControllerMapping.class);
                    if (controllerMapping != null) {
                        info = createRequestMappingInfo(controllerMapping, handlerType).combine(info);
                    }
                }
            }
        }
        return info;
    }

    protected RequestMappingInfo createRequestMappingInfo(ControllerMapping controllerMapping, Class<?> handlerType) {
        String[] patterns = resolveEmbeddedValuesInPatterns(controllerMapping.value());
        if (patterns != null && (patterns.length == 0)) {
            patterns = new String[] { this.initLower(handlerType.getSimpleName()) };
        }
        RequestCondition<?> customCondition = getCustomTypeCondition(handlerType);
        return RequestMappingInfo.paths(patterns).customCondition(customCondition).options(this.config).build();
    }

    protected RequestMappingInfo createRequestMappingInfo(ControllerMethodMapping controllerMethodMapping, Method method) {
        String[] patterns = resolveEmbeddedValuesInPatterns(controllerMethodMapping.value());
        if (patterns != null && (patterns.length == 0)) {
            patterns = new String[] { method.getName() };
        }
        RequestCondition<?> methodCondition = getCustomMethodCondition(method);
        return RequestMappingInfo.paths(patterns).methods(controllerMethodMapping.method()).customCondition(methodCondition).options(this.config).build();
    }

    protected RequestMappingInfo createRequestMappingInfo(Method method) {
        String[] patterns = new String[] { method.getName() };
        RequestCondition<?> methodCondition = getCustomMethodCondition(method);
        return RequestMappingInfo.paths(patterns).customCondition(methodCondition).options(this.config).build();
    }

    private String initLower(String value) {
        String subString = value.length() > 1 ? value.substring(1) : "";
        return value.substring(0, 1).toLowerCase() + subString;
    }
}