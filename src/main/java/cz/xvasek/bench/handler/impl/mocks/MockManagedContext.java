package cz.xvasek.bench.handler.impl.mocks;

import io.quarkus.arc.ManagedContext;

public abstract class MockManagedContext implements ManagedContext {

    @Override
    public void activate(ContextState contextState) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
