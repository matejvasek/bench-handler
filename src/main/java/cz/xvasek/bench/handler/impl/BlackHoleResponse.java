package cz.xvasek.bench.handler.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import org.openjdk.jmh.infra.Blackhole;

public abstract class BlackHoleResponse implements HttpServerResponse {
    Blackhole blackhole;

    public BlackHoleResponse(Blackhole blackhole) {
        this.blackhole = blackhole;
    }

    @Override
    public HttpServerResponse putHeader(String name, String value) {
        if (blackhole == null) {
            System.out.println("putHeader: <" + name + ": " + value + ">");
            return this;
        }
        blackhole.consume(name);
        blackhole.consume(value);
        return this;
    }

    @Override
    public HttpServerResponse putHeader(CharSequence name, CharSequence value) {
        if (blackhole == null) {
            System.out.println("putHeader: <" + name + ": " + value + ">");
            return this;
        }
        blackhole.consume(name);
        blackhole.consume(value);
        return this;
    }

    @Override
    public HttpServerResponse setStatusCode(int i) {
        if (blackhole == null) {
            System.out.println("setStatusCode: <" + i + ">");
            return this;
        }
        blackhole.consume(i);
        return this;
    }

    @Override
    public void end(Buffer buff) {
        if (blackhole == null) {
            System.out.println("body: <" + new String(buff.getBytes()) + ">");
            return;
        }
        blackhole.consume(buff);
    }

    @Override
    public void end(String writeValueAsString) {
        if (blackhole == null) {
            System.out.println("body: <" + writeValueAsString + ">");
            return;
        }
        blackhole.consume(writeValueAsString);
    }

    @Override
    public void end() {
    }

    @Override
    public boolean ended() {
        return false;
    }
}
