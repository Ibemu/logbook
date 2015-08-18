package logbook.server.proxy;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logbook.config.AppConfig;
import logbook.data.Data;
import logbook.data.DataQueue;
import logbook.data.DataType;
import logbook.data.UndefinedData;
import logbook.thread.ThreadManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.Callback;

/**
 * リバースプロキシ
 *
 */
public final class ReverseProxyServlet extends ProxyServlet {

    /** SerialVersionUID */
    private static final long serialVersionUID = -8052613366290303176L;

    @Override
    protected void sendProxyRequest(HttpServletRequest clientRequest, HttpServletResponse proxyResponse,
            Request proxyRequest) {
        proxyRequest.onRequestContent(new RequestContentListener(clientRequest));

        super.sendProxyRequest(clientRequest, proxyResponse, proxyRequest);
    }

    /*
     * レスポンスが帰ってきた
     */
    @Override
    protected void onResponseContent(HttpServletRequest request, HttpServletResponse response, Response proxyResponse,
            byte[] buffer, int offset, int length, Callback callback) {
        // フィルタークラスで必要かどうかを判別後、必要であれば内容をキャプチャする
        // 注意: 1回のリクエストで複数回の応答が帰ってくるので全ての応答をキャプチャする必要がある
        if (Filter.isNeed(request.getServerName(), response.getContentType())) {
            ByteArrayOutputStream stream = (ByteArrayOutputStream) request.getAttribute(Filter.RESPONSE_BODY);
            if (stream == null) {
                stream = new ByteArrayOutputStream();
                request.setAttribute(Filter.RESPONSE_BODY, stream);
            }
            // ストリームに書き込む
            stream.write(buffer, offset, length);
        }
        super.onResponseContent(request, response, proxyResponse, buffer, offset, length, callback);
    }

    /*
     * レスポンスが完了した
     */
    @Override
    protected void onProxyResponseSuccess(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse) {

        if (Filter.isNeed(request.getServerName(), response.getContentType())) {
            byte[] postField = (byte[]) request.getAttribute(Filter.REQUEST_BODY);
            ByteArrayOutputStream stream = (ByteArrayOutputStream) request.getAttribute(Filter.RESPONSE_BODY);
            if (stream != null) {
                UndefinedData data = new UndefinedData(request.getRequestURI(), postField, stream.toByteArray());
                Runnable task = new ParseDataTask(data, request.getServerName());
                ThreadManager.getExecutorService().submit(task);
            }
        }
        super.onProxyResponseSuccess(request, response, proxyResponse);
    }

    /*
     * 通信に失敗した
     */
    @Override
    protected void onProxyResponseFailure(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse,
            Throwable failure) {

        Logger logger = LogManager.getLogger(ReverseProxyServlet.class);

        logger.warn("通信に失敗しました", failure);
        logger.warn(request);
        logger.warn(proxyResponse);

        super.onProxyResponseFailure(request, response, proxyResponse, failure);
    }

    /*
     * HttpClientを作成する
     */
    @Override
    protected HttpClient newHttpClient() {
        HttpClient client = super.newHttpClient();
        // プロキシを設定する
        if (AppConfig.get().isUseProxy()) {
            // ポート
            int port = AppConfig.get().getProxyPort();
            // ホスト
            String host = AppConfig.get().getProxyHost();
            // 設定する
            client.getProxyConfiguration().getProxies().add(new HttpProxy(host, port));
        }
        return client;
    }

    /**
     * パースを別スレッドで行うためのタスク
     */
    private static final class ParseDataTask implements Runnable {
        /** jsonのパース前のデータ */
        private final UndefinedData undefined;
        /** サーバー名 */
        private final String serverName;

        /**
         * コンストラクター
         */
        public ParseDataTask(UndefinedData undefined, String serverName) {
            this.undefined = undefined;
            this.serverName = serverName;
        }

        @Override
        public void run() {
            Data data = this.undefined.toDefinedData();

            if (data.getDataType() != DataType.UNDEFINED) {
                // 定義済みのデータの場合にキューに追加する
                DataQueue.add(data);

                // サーバー名が不明の場合、サーバー名をセットする
                if (!Filter.isServerDetected()) {
                    Filter.setServerName(this.serverName);
                }
            }
        }
    }
}
