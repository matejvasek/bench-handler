package cz.xvasek.bench.handler.impl;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.util.Map;

public abstract class InMemoryFunqyRequest implements HttpServerRequest {

    private MultiMap headers;
    private HttpMethod method;
    private MultiMap params;
    private Buffer body;
    private String path;

    public InMemoryFunqyRequest(Map<String, String> headers, HttpMethod method, Map<String,String> params, Buffer body, String path) {
        this.headers = new BasicReadOnlyMultiMap(headers);
        this.method = method;
        this.params = new BasicReadOnlyMultiMap(params);
        this.body = body;
        this.path = path;
    }

    @Override
    public String getHeader(String s) {
        return headers.get(s);
    }

    @Override
    public MultiMap headers() {
        return headers;
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public MultiMap params() {
        return params;
    }

    @Override
    public HttpServerRequest bodyHandler(Handler<Buffer> handler) {
        handler.handle(body);
        return this;
    }

    @Override
    public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
        return this;
    }

    @Override
    public String path() {
        return path;
    }
}
