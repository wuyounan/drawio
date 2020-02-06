package com.huigou.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huigou.exception.ApplicationException;
import com.huigou.util.CommonUtil;

@SuppressWarnings("deprecation")
public final class HttpClientUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    
    private HttpClientUtil() {
    }
    
    /**
     * post 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @return 响应的内容
     */
    public static String post(String url, HttpEntity entity, Charset charset) {
        return post(url, entity, charset, null, null, null);
    }

    /**
     * post 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie存储器
     * @return 响应的内容
     */
    public static String post(String url, HttpEntity entity, Charset charset, CookieStore cookieStore) {
        return post(url, entity, charset, null, cookieStore, null);
    }

    /**
     * post 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @return 响应的内容
     */
    public static String post(String url, HttpEntity entity, Charset charset, Map<String, String> headers) {
        return post(url, entity, charset, headers, null, null);
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            return buffer.toString();
            // jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static CloseableHttpClient createHttpsClient(HttpClientBuilder httpClientBuilder) {
        X509TrustManager x509mgr = new X509TrustManager() {

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { x509mgr }, new java.security.SecureRandom());
        } catch (Exception e) {
            // Assert.customException("sslContext 创建失败");
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return httpClientBuilder.setSSLSocketFactory(sslsf).build();
    }

    /**
     * post 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储器
     * @param resHeaders
     *            响应头
     * @return 响应的内容
     */
    public static String post(String url, HttpEntity entity, Charset charset, Map<String, String> headers, CookieStore cookieStore, List<Header> resHeaders) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            httpClient = createHttpsClient(httpClientBuilder);
        } else {
            httpClient = httpClientBuilder.build();
        }

        String retVal = null;
        HttpPost post = new HttpPost(url);
       
        RequestConfig.Builder builderConfig = RequestConfig.custom();
        // 连接超时
        //builderConfig.setConnectTimeout(30000);
        //builderConfig.setSocketTimeout(30000);
        //builderConfig.setConnectionRequestTimeout(30000);
        post.setConfig(builderConfig.build());
        if (headers != null) {
            for (String key : headers.keySet()) {
                post.setHeader(key, headers.get(key));
            }
        }
        try {
            post.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(post);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }

    /**
     * post 证书请求 链接和读取 超时 统一30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @return 响应内容
     */
    public static String certPost(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset) {
        return certPost(certLocalPath, certPassword, url, entity, charset, null, null, null);
    }

    /**
     * post 证书请求 链接和读取 超时统一 30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie存储器
     * @return 响应内容
     */
    public static String certPost(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, CookieStore cookieStore) {
        return certPost(certLocalPath, certPassword, url, entity, charset, null, cookieStore, null);
    }

    /**
     * post 证书请求 链接和读取 超时统一 30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @return 响应内容
     */
    public static String certPost(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, Map<String, String> headers) {
        return certPost(certLocalPath, certPassword, url, entity, charset, headers, null, null);
    }

    /**
     * post 证书请求 链接和读取 超时统一30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储
     * @param resHeaders
     *            请求头
     * @return 响应内容
     */
    public static String certPost(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, Map<String, String> headers,
                                  CookieStore cookieStore, List<Header> resHeaders) {
        /*
         * Assert.assertNotEmptyString(certLocalPath, "证书地址不可以为空");
         * Assert.assertNotEmptyString(certPassword, "证书密码不可以为空");
         */
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (Exception e) {
            // Assert.customException("加载证书出错");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(certLocalPath));
            keyStore.load(fis, certPassword.toCharArray());
        } catch (Exception e) {
            // Assert.customException("加载密钥出错");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
        } catch (Exception e) {
            // Assert.customException("加载证书出错");
        }
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
                                                                                               SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();
        String retVal = null;
        HttpPost post = new HttpPost(url);
        RequestConfig.Builder builderConfig = RequestConfig.custom();
        // 连接超时
        builderConfig.setConnectTimeout(25000);
        builderConfig.setSocketTimeout(25000);
        builderConfig.setConnectionRequestTimeout(25000);
        post.setConfig(builderConfig.build());
        if (headers != null) {
            for (String key : headers.keySet()) {
                post.setHeader(key, headers.get(key));
            }
        }
        try {
            post.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(post);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }

    /**
     * put 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @return 响应的内容
     */
    public static String put(String url, HttpEntity entity, Charset charset) {
        return post(url, entity, charset, null, null, null);
    }

    /**
     * put 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie存储器
     * @return 响应的内容
     */
    public static String put(String url, HttpEntity entity, Charset charset, CookieStore cookieStore) {
        return put(url, entity, charset, null, cookieStore, null);
    }

    /**
     * put 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @return 响应的内容
     */
    public static String put(String url, HttpEntity entity, Charset charset, Map<String, String> headers) {
        return put(url, entity, charset, headers, null, null);
    }

    /**
     * put 请求 链接和读取 超时 统一用的30秒
     * 
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储器
     * @param resHeaders
     *            响应头
     * @return 响应的内容
     */
    public static String put(String url, HttpEntity entity, Charset charset, Map<String, String> headers, CookieStore cookieStore, List<Header> resHeaders) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            httpClient = createHttpsClient(httpClientBuilder);
        } else {
            httpClient = httpClientBuilder.build();
        }

        String retVal = null;
        HttpPut put = new HttpPut(url);
        RequestConfig.Builder builderConfig = RequestConfig.custom();
        // 连接超时
        builderConfig.setConnectTimeout(30000);
        builderConfig.setSocketTimeout(30000);
        builderConfig.setConnectionRequestTimeout(30000);
        put.setConfig(builderConfig.build());
        if (headers != null) {
            for (String key : headers.keySet()) {
                put.setHeader(key, headers.get(key));
            }
        }
        try {
            put.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(put);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }

    /**
     * put 证书请求 链接和读取 超时 统一30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求实体
     * @param charset
     *            字符集
     * @return 响应内容
     */
    public static String certPut(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset) {
        return certPut(certLocalPath, certPassword, url, entity, charset, null, null, null);
    }

    /**
     * post 证书请求 链接和读取 超时统一 30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie存储器
     * @return 响应内容
     */
    public static String certPut(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, CookieStore cookieStore) {
        return certPut(certLocalPath, certPassword, url, entity, charset, null, cookieStore, null);
    }

    /**
     * put 证书请求 链接和读取 超时统一 30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @return 响应内容
     */
    public static String certPut(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, Map<String, String> headers) {
        return certPut(certLocalPath, certPassword, url, entity, charset, headers, null, null);
    }

    /**
     * put 证书请求 链接和读取 超时统一30秒
     * 
     * @param certLocalPath
     *            证书地址
     * @param certPassword
     *            证书密码
     * @param url
     *            请求地址
     * @param entity
     *            请求内容
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储
     * @param resHeaders
     *            请求头
     * @return 响应内容
     */
    public static String certPut(String certLocalPath, String certPassword, String url, HttpEntity entity, Charset charset, Map<String, String> headers,
                                 CookieStore cookieStore, List<Header> resHeaders) {
        /*
         * Assert.assertNotEmptyString(certLocalPath, "证书地址不可以为空");
         * Assert.assertNotEmptyString(certPassword, "证书密码不可以为空");
         */
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (Exception e) {
            // Assert.customException("加载证书出错");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(certLocalPath));
            keyStore.load(fis, certPassword.toCharArray());
        } catch (Exception e) {
            // Assert.customException("加载密钥出错");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
        } catch (Exception e) {
            // Assert.customException("加载证书出错");
        }
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
                                                                                               SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();
        String retVal = null;
        HttpPut put = new HttpPut(url);
        RequestConfig.Builder builderConfig = RequestConfig.custom();
        // 连接超时
        builderConfig.setConnectTimeout(25000);
        builderConfig.setSocketTimeout(25000);
        builderConfig.setConnectionRequestTimeout(25000);
        put.setConfig(builderConfig.build());
        if (headers != null) {
            for (String key : headers.keySet()) {
                put.setHeader(key, headers.get(key));
            }
        }
        try {
            put.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(put);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }

    
    public static String get(String url) {
        long beginTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        byte[] buffer = new byte[4096];
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性 connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            LOGGER.debug(String.format("在“%s”调用服务: %s ", CommonUtil.getCurrentDateTime(), url));
            connection.connect();
            is = connection.getInputStream();
            int len;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                sb.append(new String(buffer, 0, len, "utf-8"));
            }
        } catch (Exception e) {
            LOGGER.debug(String.format("调用“%s”出错。", url), e);
            throw new ApplicationException("调用cubesViewerService错误。");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e2) {
            }
        }
        long collepsedTime = System.currentTimeMillis() - beginTime;
        
        LOGGER.debug(String.format("耗时：“%s” 调用服务: %s ", collepsedTime, url));
        return sb.toString();
    }
    
    /**
     * get 请求 链接和读取超时 统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String get(String url, Charset charset, Map<String, String> params) {
        return get(url, charset, null, null, null, params);
    }

    /**
     * get 请求 链接和读取超时 统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie 存储
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String get(String url, Charset charset, CookieStore cookieStore, Map<String, String> params) {
        return get(url, charset, null, cookieStore, null, params);
    }

    /**
     * get 请求 链接和读取 超时统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String get(String url, Charset charset, Map<String, String> headers, Map<String, String> params) {
        return get(url, charset, headers, null, null, params);
    }

    /**
     * get请求 链接和读取 超时统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储
     * @param resHeaders
     *            响应头
     * @return 响应内容
     */
    public static String get(String url, Charset charset, Map<String, String> headers, CookieStore cookieStore, List<Header> resHeaders,
                             Map<String, String> params) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        // CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            httpClient = createHttpsClient(httpClientBuilder);
        } else {
            httpClient = httpClientBuilder.build();
        }
        String retVal = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.setParameter(key, params.get(key));
                }
            }
            HttpGet get = new HttpGet(builder.build());
            RequestConfig.Builder builderConfig = RequestConfig.custom();
            // 连接超时
            builderConfig.setConnectTimeout(30000);
            builderConfig.setSocketTimeout(30000);
            builderConfig.setConnectionRequestTimeout(30000);
            get.setConfig(builderConfig.build());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }
            HttpResponse httpResponse = httpClient.execute(get);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }

    /**
     * 下载文件
     * 
     * @param url
     *            请求路径
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储
     * @param params
     *            参数
     * @param fileName
     *            远程文件名
     */
    public static void download(String url, Map<String, String> headers, CookieStore cookieStore, Map<String, String> params, String fileName) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.setParameter(key, params.get(key));
                }
            }
            HttpGet get = new HttpGet(builder.build());
            RequestConfig.Builder builderConfig = RequestConfig.custom();
            // 连接超时
            builderConfig.setConnectTimeout(30000);
            builderConfig.setSocketTimeout(30000);
            builderConfig.setConnectionRequestTimeout(30000);
            get.setConfig(builderConfig.build());
            if (headers != null) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }
            HttpResponse httpResponse = httpClient.execute(get);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                /*
                 * HttpEntity httpEntity = httpResponse.getEntity(); InputStream
                 * is = null; OutputStream os = null;
                 * try { is = httpEntity.getContent(); os = new
                 * FileOutputStream(fileName); StreamUtil.io(is, os); } catch
                 * (Exception e) {
                 * } finally { StreamUtil.closeQuietly(is);
                 * StreamUtil.closeQuietly(os); }
                 */
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

    /**
     * delete 请求 链接和读取超时 统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String delete(String url, Charset charset, Map<String, String> params) {
        return delete(url, charset, null, null, null, params);
    }

    /**
     * delete 请求 链接和读取超时 统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param cookieStore
     *            cookie 存储
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String delete(String url, Charset charset, CookieStore cookieStore, Map<String, String> params) {
        return delete(url, charset, null, cookieStore, null, params);
    }

    /**
     * delete 请求 链接和读取 超时统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param params
     *            请求参数
     * @return 响应内容
     */
    public static String delete(String url, Charset charset, Map<String, String> headers, Map<String, String> params) {
        return delete(url, charset, headers, null, null, params);
    }

    /**
     * delete请求 链接和读取 超时统一30秒
     * 
     * @param url
     *            请求地址
     * @param charset
     *            字符集
     * @param headers
     *            请求头
     * @param cookieStore
     *            cookie存储
     * @param resHeaders
     *            响应头
     * @return 响应内容
     */
    public static String delete(String url, Charset charset, Map<String, String> headers, CookieStore cookieStore, List<Header> resHeaders,
                                Map<String, String> params) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (cookieStore != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient httpClient = httpClientBuilder.build();
        String retVal = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.setParameter(key, params.get(key));
                }
            }
            HttpDelete delete = new HttpDelete(builder.build());
            RequestConfig.Builder builderConfig = RequestConfig.custom();
            // 连接超时
            builderConfig.setConnectTimeout(30000);
            builderConfig.setSocketTimeout(30000);
            builderConfig.setConnectionRequestTimeout(30000);
            delete.setConfig(builderConfig.build());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    delete.setHeader(key, headers.get(key));
                }
            }
            HttpResponse httpResponse = httpClient.execute(delete);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                if (resHeaders != null) {
                    Header[] hs = httpResponse.getAllHeaders();
                    if (hs != null) {
                        resHeaders.addAll(Arrays.asList(hs));
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    retVal = EntityUtils.toString(httpEntity, charset);
                }
            } else {
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            return retVal;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return retVal;
    }
}
