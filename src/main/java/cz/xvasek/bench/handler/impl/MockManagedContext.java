package cz.xvasek.bench.handler.impl;

import io.quarkus.arc.ManagedContext;

public abstract class MockManagedContext implements ManagedContext {

    @Override
    public void activate(ContextState contextState) { }

    @Override
    public void deactivate() { }

    @Override
    public boolean isActive() {
        return false;
    }
}
