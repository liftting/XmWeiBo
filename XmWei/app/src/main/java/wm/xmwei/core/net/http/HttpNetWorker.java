package wm.xmwei.core.net.http;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.ErrorCode;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.util.Utility;

/**
 *
 */
public class HttpNetWorker {

    private static final int CONNECT_TIMEOUT = 10 * 1000; // 连接超时
    private static final int READ_TIMEOUT = 10 * 1000; // 流读取超时时间
    private static final int DOWNLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int DOWNLOAD_READ_TIMEOUT = 60 * 1000;
    private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;

    public static String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param)
            throws XmWeiboException {
        switch (httpMethod) {
            case Post:
                return doPost(url, param);
            case Get:
                return doGet(url, param);
        }
        return "";
    }

    private static Proxy getProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort)) {
            return new Proxy(java.net.Proxy.Type.HTTP,
                    new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
        } else {
            return null;
        }
    }

    public static String doGet(String urlStr, Map<String, String> param) throws XmWeiboException {
        XmApplication globalContext = XmApplication.getInstance();
        String errorStr = globalContext.getString(R.string.timeout);
        globalContext = null;
        InputStream is = null;
        try {

            StringBuilder urlBuilder = new StringBuilder(urlStr);
            urlBuilder.append("?").append(Utility.encodeUrl(param));
            URL url = new URL(urlBuilder.toString());

            AppLogger.d("get request" + url);

            Proxy proxy = getProxy();
            HttpURLConnection urlConnection;
            if (proxy != null) {
                urlConnection = (HttpURLConnection) url.openConnection(proxy);
            } else {
                urlConnection = (HttpURLConnection) url.openConnection();
            }

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();

            return handleResponse(urlConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmWeiboException(errorStr, e);
        }
    }

    public static String doPost(String urlAddress, Map<String, String> param) throws XmWeiboException {
        XmApplication globalContext = XmApplication.getInstance();
        String errorStr = globalContext.getString(R.string.timeout);
        globalContext = null;
        try {
            URL url = new URL(urlAddress);
            Proxy proxy = getProxy();
            HttpsURLConnection uRLConnection;
            if (proxy != null) {
                uRLConnection = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                uRLConnection = (HttpsURLConnection) url.openConnection();
            }

            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setUseCaches(false);
            uRLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            uRLConnection.setReadTimeout(READ_TIMEOUT);
            uRLConnection.setInstanceFollowRedirects(false);
            uRLConnection.setRequestProperty("Connection", "Keep-Alive");
            uRLConnection.setRequestProperty("Charset", "UTF-8");
            uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            uRLConnection.connect();

            DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
            out.write(Utility.encodeUrl(param).getBytes());
            out.flush();
            out.close();
            return handleResponse(uRLConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmWeiboException(errorStr, e);
        }
    }

    /**
     * 流请求后，处理响应
     *
     * @param httpURLConnection
     * @return
     * @throws wm.xmwei.core.lib.support.error.XmWeiboException
     */
    private static String handleResponse(HttpURLConnection httpURLConnection) throws XmWeiboException {
        XmApplication globalContext = XmApplication.getInstance();
        String errorStr = globalContext.getString(R.string.timeout);
        globalContext = null;
        int status = 0;
        try {
            status = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
            throw new XmWeiboException(errorStr, e);
        }

        if (status != HttpURLConnection.HTTP_OK) {
            return handleError(httpURLConnection);
        }

        return parseResult(httpURLConnection);
    }

    /**
     * 流读取出错，
     *
     * @param urlConnection
     * @return
     * @throws wm.xmwei.core.lib.support.error.XmWeiboException
     */
    private static String handleError(HttpURLConnection urlConnection) throws XmWeiboException {

        String result = readError(urlConnection);
        String err = null;
        int errCode = 0;
        try {
            AppLogger.e("error=" + result);
            JSONObject json = new JSONObject(result);
            err = json.optString("error_description", "");
            if (TextUtils.isEmpty(err)) {
                err = json.getString("error");
            }
            errCode = json.getInt("error_code");
            XmWeiboException exception = new XmWeiboException();
            exception.setError_code(errCode);
            exception.setOriError(err);

            if (errCode == ErrorCode.EXPIRED_TOKEN || errCode == ErrorCode.INVALID_TOKEN) {
                //这里会发出 通知 TODO
            }

            throw exception;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String readError(HttpURLConnection urlConnection) throws XmWeiboException {
        InputStream is = null;
        BufferedReader buffer = null;
        XmApplication globalContext = XmApplication.getInstance();
        String errorStr = globalContext.getString(R.string.timeout);

        try {
            is = urlConnection.getErrorStream();

            if (is == null) {
                errorStr = globalContext.getString(R.string.unknown_sina_network_error);
                throw new XmWeiboException(errorStr);
            }

            String content_encode = urlConnection.getContentEncoding();

            if (!TextUtils.isEmpty(content_encode) && content_encode
                    .equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            AppLogger.d("error result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmWeiboException(errorStr, e);
        } finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            urlConnection.disconnect();
            globalContext = null;
        }
    }


    private static String parseResult(HttpURLConnection urlConnection) throws XmWeiboException {
        InputStream is = null;
        BufferedReader buffer = null;
        XmApplication globalContext = XmApplication.getInstance();
        String errorStr = globalContext.getString(R.string.timeout);
        globalContext = null;
        try {
            is = urlConnection.getInputStream();

            String content_encode = urlConnection.getContentEncoding();

            if (!TextUtils.isEmpty(content_encode) && content_encode
                    .equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
//            AppLogger.d("result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new XmWeiboException(errorStr, e);
        } finally {
            Utility.closeSilently(is);
            Utility.closeSilently(buffer);
            urlConnection.disconnect();
        }
    }

}
