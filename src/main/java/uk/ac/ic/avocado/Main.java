package uk.ac.ic.avocado;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        final Configuration.Type environment = Configuration.getInstance().getCurrent();
        final HttpServer server = HttpServer.create(new InetSocketAddress(environment == Configuration.Type.PRODUCTION ? 8080 : 8081), 0);
        server.createContext("/test", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                final String message = String.format("This is a test originating from the %s environment.", environment.toString());
                exchange.sendResponseHeaders(200, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.close();
            }
        });
        server.setExecutor(null);
        server.start();
    }
}
