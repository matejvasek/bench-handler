package cz.xvasek.bench.handler.impl;

import io.quarkus.arc.ManagedContext;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import javassist.Modifier;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.graalvm.nativeimage.ImageInfo;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.openjdk.jmh.infra.Blackhole;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MockFactory {
    private static final ProxyFactory factory = new ProxyFactory();;

    private static final MethodHandler handler = (self, thisMethod, proceed, args) -> {
        throw new UnsupportedOperationException("Operation not implemented in the mock object.");
    };

    private static final MethodFilter filterOutAbstract = method -> Modifier.isAbstract(method.getModifiers());

    private static final Class<HttpServerResponse> RESPONSE_CLASS;
    private static final Class<HttpServerRequest> REQUEST_CLASS;
    private static final Class<RoutingContext> ROUTING_CONTEXT_CLASS;
    private static final Class<Instance> MOCK_INSTANCE_CLASS;
    private static final Class<CDI> MOCK_CDI_CLASS;
    private static final Class<ManagedContext> MOCK_MANAGED_CONTEXT_CLASS;

    static {
        try {
            factory.setSuperclass(BlackHoleResponse.class);
            factory.setHandler(handler);
            factory.setFilter(filterOutAbstract);

            factory.setSuperclass(BlackHoleResponse.class);
            RESPONSE_CLASS = (Class<HttpServerResponse>) factory.createClass();

            factory.setSuperclass(InMemoryFunqyRequest.class);
            REQUEST_CLASS = (Class<HttpServerRequest>) factory.createClass();

            factory.setSuperclass(BlackHoleRoutingContext.class);
            ROUTING_CONTEXT_CLASS = (Class<RoutingContext>) factory.createClass();

            factory.setSuperclass(MockInstance.class);
            MOCK_INSTANCE_CLASS = (Class<Instance>) factory.createClass();

            factory.setSuperclass(MockCDI.class);
            MOCK_CDI_CLASS = (Class<CDI>) factory.createClass();

            factory.setSuperclass(MockManagedContext.class);
            MOCK_MANAGED_CONTEXT_CLASS = (Class<ManagedContext>) factory.createClass();

            if (ImageInfo.inImageCode()) {
                RuntimeReflection.register(RESPONSE_CLASS);
                RuntimeReflection.register(RESPONSE_CLASS.getDeclaredConstructor(Blackhole.class));

                RuntimeReflection.register(REQUEST_CLASS);
                RuntimeReflection.register(REQUEST_CLASS.getDeclaredConstructor(Map.class, HttpMethod.class, Map.class, Buffer.class, String.class));

                RuntimeReflection.register(ROUTING_CONTEXT_CLASS);
                RuntimeReflection.register(ROUTING_CONTEXT_CLASS.getDeclaredConstructor(Blackhole.class, HttpServerRequest.class, HttpServerResponse.class));

                RuntimeReflection.register(MOCK_INSTANCE_CLASS);
                RuntimeReflection.register(MOCK_INSTANCE_CLASS.getDeclaredConstructor(Class.class));

                RuntimeReflection.register(MOCK_CDI_CLASS);
                RuntimeReflection.register(MOCK_CDI_CLASS.getDeclaredConstructor());

                RuntimeReflection.register(MOCK_MANAGED_CONTEXT_CLASS);
                RuntimeReflection.register(MOCK_MANAGED_CONTEXT_CLASS.getDeclaredConstructor());
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static RoutingContext routingContext(Blackhole blackhole, HttpServerRequest request, HttpServerResponse response) {
        try {
            return ROUTING_CONTEXT_CLASS
                    .getConstructor(Blackhole.class, HttpServerRequest.class, HttpServerResponse.class)
                    .newInstance(blackhole, request, response);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static HttpServerResponse httpServerResponse(Blackhole blackhole) {
        try {
            return RESPONSE_CLASS
                    .getConstructor(Blackhole.class)
                    .newInstance(blackhole);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static HttpServerRequest httpServerRequest(Map<String, String> headers, HttpMethod method, Map<String,String> params, Buffer body, String path) {
        try {
            return REQUEST_CLASS
                    .getDeclaredConstructor(Map.class, HttpMethod.class, Map.class, Buffer.class, String.class)
                    .newInstance(headers, method, params, body, path);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static <U> Instance instance(Class<U> uClass) {
        try {
            return MOCK_INSTANCE_CLASS
                    .getConstructor(Class.class)
                    .newInstance(uClass);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static CDI<Object> cdi() {
        try {
            return MOCK_CDI_CLASS.getConstructor().newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static ManagedContext managedContext() {
        try {
            return MOCK_MANAGED_CONTEXT_CLASS.getConstructor().newInstance();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
