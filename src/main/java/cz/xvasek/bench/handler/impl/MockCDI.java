package cz.xvasek.bench.handler.impl;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;

public abstract class MockCDI extends CDI<Object> {

    @Override
    public <U> Instance<U> select(Class<U> uClass, Annotation... annotations) {
        return MockFactory.instance(uClass);
    }

}
