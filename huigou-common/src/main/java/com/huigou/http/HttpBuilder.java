package com.huigou.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.huigou.util.ClassHelper;
import com.huigou.util.HttpClientUtil;
import com.huigou.util.JSONUtil;
import com.huigou.util.LogHome;

/**
 * http 请求执行类
 * 
 * @author xx
 */
public class HttpBuilder {
    private Logger logger = LogHome.getLog(HttpBuilder.class);

    private String url;

    private HttpMethod httpMethod;

    private Map<String, Object> headers;

    private Map<String, Object> params;

    private HttpEntity httpEntity;

    private InputStream inputStream;

    private String entity;

    private String charset;

    private String contentType;

    private Cancellable cancellable;

    private HttpClientBuilder httpClientBuilder;

    private RequestConfig requestConfig;

    public HttpBuilder() {
        // 创建HttpClientBuilder
        httpClientBuilder = HttpClientBuilder.create();
        this.setRequestConfig(HttpClientUtil.REQUEST_CONFIG);
    }

    public HttpBuilder(String user, String pwd) {
        this();
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        // Create credential pair，在此处填写用户名和密码
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pwd);
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // Set the default credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(provider);
    }

    public HttpClientBuilder getHttpBuilder() {
        return httpClientBuilder;
    }

    public HttpBuilder setRequestConfig(RequestConfig requestConfig) {
        if (requestConfig != null) {
            this.requestConfig = requestConfig;
        }
        return this;
    }

    public HttpBuilder setUrl(String url) {
        if (url != null) {
            this.url = url;
        }
        return this;
    }

    public HttpBuilder get() {
        return get(null);
    }

    public HttpBuilder get(String url) {
        setUrl(url);
        setMethod(HttpMethod.GET);
        return this;
    }

    public HttpBuilder post() {
        return post(null);
    }

    public HttpBuilder post(String url) {
        setUrl(url);
        setMethod(HttpMethod.POST);
        return this;
    }

    public HttpBuilder setMethod(HttpMethod method) {
        if (method != null) {
            this.httpMethod = method;
        } else {
            this.httpMethod = HttpMethod.GET;
        }
        return this;
    }

    public HttpBuilder setParams(Map<String, Object> params) {
        this.params = params;
        this.contentType = HttpClientUtil.CONTENT_TYPE;
        this.charset = HttpClientUtil.CHARSET_NAME;
        return this;
    }

    public HttpBuilder setHeaders(Map<String, Object> headers) {
        this.headers = headers;
        return this;
    }

    public HttpBuilder setCancellable(Cancellable cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param entity
     * @return
     */
    public HttpBuilder setEntity(HttpEntity entity) {
        this.httpEntity = entity;
        return this;
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param entity
     * @return
     */
    public HttpBuilder setEntity(String entity) {
        return setEntity(entity, HttpClientUtil.CHARSET_NAME, HttpClientUtil.CONTENT_TYPE);
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param entity
     * @param charset
     *            编码
     * @return
     */
    public HttpBuilder setEntity(String entity, String charset) {
        return setEntity(entity, charset, HttpClientUtil.CONTENT_TYPE);
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param entity
     * @param charset
     *            编码
     * @return
     */
    public HttpBuilder setEntity(String entity, String charset, String contentType) {
        this.entity = entity;
        this.charset = charset;
        this.contentType = contentType;
        return this;
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param is
     * @return
     */
    public HttpBuilder setEntity(InputStream is) {
        return setEntity(is, null);
    }

    /**
     * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
     *
     * @param contentType
     * @param is
     * @return
     */
    public HttpBuilder setEntity(InputStream is, String contentType) {
        this.contentType = contentType;
        this.inputStream = is;
        return this;
    }

    /**
     * 实际执行方法,此方法应放最后执行
     *
     * @param callback
     */
    public <T> T execute(HttpCallback<T> callback) {
        if (url == null) {
            throw new RuntimeException("URL UNDEFINED!");
        }
        if (httpMethod == null) {
            throw new RuntimeException("HttpMethod UNDEFINED!");
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpRequestBase httpRequestBase = null;
        try {
            logger.info(url);
            URI uri = new URI(url);
            switch (httpMethod) {
            case GET:
                if (params != null && !params.isEmpty()) {
                    URIBuilder uriBuilder = new URIBuilder(uri);
                    List<NameValuePair> nameValuePairs = new LinkedList<>();
                    for (String key : params.keySet()) {
                        nameValuePairs.add(new BasicNameValuePair(key, ClassHelper.convert(params.get(key), String.class)));
                    }
                    uriBuilder.setParameters(nameValuePairs);
                    // 根据带参数的URI对象构建GET请求对象
                    httpRequestBase = new HttpGet(uriBuilder.build());
                    logger.info(JSONUtil.toString(params));
                } else {
                    httpRequestBase = new HttpGet(uri);
                }
                break;
            case POST:
                httpRequestBase = new HttpPost(uri);
                if (httpEntity != null) {
                    ((HttpPost) httpRequestBase).setEntity(httpEntity);
                } else if (entity != null) {
                    StringEntity stringEntity = null;
                    if (charset == null) {
                        stringEntity = new StringEntity(entity);
                    } else {
                        stringEntity = new StringEntity(entity, charset);
                    }
                    if (contentType != null) {
                        stringEntity.setContentType(contentType);
                    }
                    ((HttpPost) httpRequestBase).setEntity(stringEntity);
                    logger.info(entity);
                } else if (inputStream != null) {
                    BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
                    basicHttpEntity.setContent(inputStream);
                    if (contentType != null) {
                        basicHttpEntity.setContentType(contentType);
                    }
                    ((HttpPost) httpRequestBase).setEntity(basicHttpEntity);
                }
                if (params != null && !params.isEmpty()) {
                    // 创建参数队列
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    for (String key : params.keySet()) {
                        nameValuePairs.add(new BasicNameValuePair(key, ClassHelper.convert(params.get(key), String.class)));
                    }
                    StringEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, charset);
                    if (contentType != null) {
                        urlEncodedFormEntity.setContentType(contentType);
                    }
                    ((HttpPost) httpRequestBase).setEntity(urlEncodedFormEntity);
                    logger.info(JSONUtil.toString(params));
                }
                break;
            }
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    httpRequestBase.addHeader(key, ClassHelper.convert(headers.get(key), String.class));
                }
            }
            if (cancellable != null) {
                httpRequestBase.setCancellable(cancellable);
            }
            httpClient = this.httpClientBuilder.build();
            if (requestConfig != null) {
                httpRequestBase.setConfig(requestConfig);
            }
            response = httpClient.execute(httpRequestBase);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return callback.callback(response);
            } else {
                throw new RuntimeException(String.format("Request Error Code:%s %s", statusCode, response.getStatusLine().getReasonPhrase()));
            }
        } catch (Exception e) {
            logger.error(e);
            callback.exception(e);
        } finally {
            callback.finallz();
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
