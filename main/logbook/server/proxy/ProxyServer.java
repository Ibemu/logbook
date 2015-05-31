package logbook.server.proxy;

import java.net.BindException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * プロキシサーバーです
 *
 */
public final class ProxyServer extends Thread {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ProxyServer.class);
    }

    private final int port;
    private final Shell shell;

    public ProxyServer(int port, Shell shell) {
        this.port = port;
        this.shell = shell;
        this.setName("logbook_proxy_server");
    }

    @Override
    public void run() {
        try {
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(this.port);
            server.addConnector(connector);

            ServletHandler context = new ServletHandler();
            ServletHolder holder = new ServletHolder(new ReverseProxyServlet());
            holder.setInitParameter("maxThreads", "256");
            context.addServletWithMapping(holder, "/*");
            server.setHandler(context);

            try {
                try {
                    server.start();
                    try {
                        server.join();
                    } catch (InterruptedException e) {
                    }
                } catch (Exception e) {
                    this.handle(e);
                }
            } finally {
                server.stop();
            }
        } catch (Exception e) {
            LoggerHolder.LOG.fatal("サーバーの起動に失敗しました", e);
            throw new RuntimeException(e);
        }
    }

    private void handle(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("プロキシサーバーが予期せず終了しました").append("\r\n");
        sb.append("例外 : " + e.getClass().getName()).append("\r\n");
        sb.append("原因 : " + e.getMessage()).append("\r\n");
        if (e instanceof BindException) {
            sb.append("おそらく、二重起動か同じポートを使用しているアプリケーションがあります。").append("\r\n");
        }

        final String message = sb.toString();

        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                MessageBox box = new MessageBox(ProxyServer.this.shell, SWT.YES | SWT.ICON_ERROR);
                box.setText("プロキシサーバーが予期せず終了しました");
                box.setMessage(message);
                box.open();
            }
        });
    }
}
