package cz.xvasek.bench.handler.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.openjdk.jmh.infra.Blackhole;

public abstract class BlackHoleRoutingContext implements RoutingContext {

    private Blackhole blackHole;
    private HttpServerRequest request;
    private HttpServerResponse response;

    public BlackHoleRoutingContext(Blackhole blackHole, HttpServerRequest request, HttpServerResponse response) {
        this.blackHole = blackHole;
        this.request = request;
        this.response = response;
    }

    @Override
    public HttpServerRequest request() {
        return request;
    }

    @Override
    public HttpServerResponse response() {
        return response;
    }

    @Override
    public void fail(int i, Throwable t) {
        if (blackHole == null) {
            System.out.println("fail: <" + i + "," + t + ">");
            t.printStackTrace(System.err);
            return;
        }
        blackHole.consume(i);
        blackHole.consume(t);
    }

    @Override
    public void fail(int i) {
        if (blackHole == null) {
            System.out.println("fail: <" + i + ">");
            return;
        }
        blackHole.consume(i);
    }

    @Override
    public void fail(Throwable t) {
        if (blackHole == null) {
            System.out.println("fail: <" + t + ">");
            t.printStackTrace(System.err);
            return;
        }
        blackHole.consume(t);
    }
}
