package com.huigou.uasp.wsutil;

import java.net.Authenticator;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;

import com.huigou.exception.ApplicationException;

/**
 * WebService 通用调用
 * 
 * @author xx
 *         通过单列模式 控制Client类创建，避免每次调用都创建Client的问题
 */
public class WebServiceClientHelper {

    static class WebServiceClientHolder {
        static JaxWsDynamicClientFactory dynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
    }

    public static JaxWsDynamicClientFactory getDynamicClientFactory() {
        return WebServiceClientHolder.dynamicClientFactory;
    }

    public static Client getDynamicClient(String wsUrl) {
        return getDynamicClientFactory().createClient(wsUrl);
    }

    /**
     * 调用远程的webservice并返回数据
     * 
     * @author xiexin
     * @param wsUrl
     * @param method
     * @param arg
     * @return Object
     */
    public static Object callService(String wsUrl, String method, Object... arg) {
        Client client = null;
        Object[] res = null;
        try {
            client = getDynamicClient(wsUrl);
            // 下面一段处理 WebService接口和实现类namespace不同的情况
            // CXF动态客户端在处理此问题时，会报No operation was found with the name的异常
            Endpoint endpoint = client.getEndpoint();
            QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), method);
            BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
            if (bindingInfo.getOperation(opName) == null) {
                for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                    if (method.equals(operationInfo.getName().getLocalPart())) {
                        opName = operationInfo.getName();
                        break;
                    }
                }
            }
            res = client.invoke(opName, arg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(e.getMessage());
        } finally {
            if (client != null) {
                client.destroy();
                client = null;
            }
        }
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8080/tech/ws/testWebService?wsdl";
        Authenticator authenticator = new BasicAuthenticator("bpm", "bpm123456");
        Authenticator.setDefault(authenticator);
        // Object obj = callService(url, "testDataRead", "测试webservice");
        // System.out.println("return:" + obj);
        WebServiceClient client = new WebServiceClient(url, "testDataRead");
        client.setUsername("bpm");
        client.setPassword("bpm123456");
        client.setArgs("测试NAME1");
        // SDO sdo = client.callToSDO();
        // String s = client.call();
        // System.out.println("return:" + s);
    }
}
