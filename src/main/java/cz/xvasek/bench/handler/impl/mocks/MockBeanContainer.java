package cz.xvasek.bench.handler.impl.mocks;

import cz.xvasek.bench.handler.impl.Bench;
import io.quarkus.arc.ManagedContext;
import io.quarkus.arc.runtime.BeanContainer;

import java.lang.annotation.Annotation;

public class MockBeanContainer implements BeanContainer {

    @Override
    public <T> Factory<T> instanceFactory(Class<T> TClass, Annotation... qualifiers) {
        return () -> (Instance<T>) () -> {
            if (Bench.Fun.class.isAssignableFrom(TClass)) {
                return (T) new Bench.Fun();
            }
            throw new UnsupportedOperationException("Can only instantiate Bench.Fun.");
        };
    }

    @Override
    public ManagedContext requestContext() {
        return MockFactory.managedContext();
    }
}
