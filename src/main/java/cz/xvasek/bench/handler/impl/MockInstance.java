package cz.xvasek.bench.handler.impl;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;

import javax.enterprise.inject.Instance;

public abstract class MockInstance<U> implements Instance<U> {

    private Class uClass;

    public MockInstance(Class<U> uClass) {
        this.uClass = uClass;
    }

    @Override
    public boolean isUnsatisfied() {
        if (CurrentVertxRequest.class.isAssignableFrom(uClass)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isAmbiguous() {
        return false;
    }

    @Override
    public U get() {
        if (CurrentVertxRequest.class.isAssignableFrom(uClass)) {
            return (U) new CurrentVertxRequest();
        }
        throw new UnsupportedOperationException("Can only get CurrentVertxRequest.");
    }
}
