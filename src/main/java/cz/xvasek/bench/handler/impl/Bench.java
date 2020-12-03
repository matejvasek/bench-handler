package cz.xvasek.bench.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.xvasek.bench.handler.impl.mocks.MockBeanContainer;
import cz.xvasek.bench.handler.impl.mocks.MockFactory;
import cz.xvasek.bench.handler.impl.mocks.MockSecureRandom;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.funqy.runtime.FunctionConstructor;
import io.quarkus.funqy.runtime.FunctionInvoker;
import io.quarkus.funqy.runtime.FunctionRecorder;
import io.quarkus.funqy.runtime.FunctionRegistry;
import io.quarkus.funqy.runtime.bindings.knative.events.VertxRequestHandler;
//import io.quarkus.qson.Qson;
//import io.quarkus.qson.deserializer.QsonParser;
//import io.quarkus.qson.generator.QsonMapper;
//import io.quarkus.qson.runtime.QuarkusQsonRegistry;
//import io.quarkus.qson.serializer.QsonObjectWriter;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.Executor;

import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_SOURCE;
import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_TYPE;

public class Bench {

    @RegisterForReflection
//    @Qson
    public static class Input {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RegisterForReflection
//    @Qson
    public static class Output {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RegisterForReflection
    public static class Fun {
        public Output fun(Input input) {
            Output o = new Output();
            o.setMessage(input.getMessage());
            return o;
        }
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private VertxRequestHandler knativeVertxRequestHandler;
        private io.quarkus.funqy.runtime.bindings.http.VertxRequestHandler httpVertxRequestHandler;
        private RoutingContext structRoutingContext;
        private RoutingContext binRoutingContext;
        private RoutingContext vanillaRoutingContext;

        @Setup(Level.Trial)
        public void init(Blackhole blackhole) {

            Method method = Arrays.stream(Fun.class.getMethods())
                    .filter(m -> "fun".equals(m.getName()))
                    .findFirst().orElse(null);
            FunctionInvoker functionInvoker = new FunctionInvoker("fun", Fun.class, method);

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectReader reader = objectMapper.readerFor(functionInvoker.getInputType());
            functionInvoker.getBindingContext().put(ObjectReader.class.getName(), reader);

            ObjectWriter writer = objectMapper.writerFor(functionInvoker.getOutputType());
            functionInvoker.getBindingContext().put(ObjectWriter.class.getName(), writer);

            functionInvoker.getBindingContext().put(RESPONSE_SOURCE, "/benchSource-out");
            functionInvoker.getBindingContext().put(RESPONSE_TYPE, "benchType-out");

            FunctionRegistry registry = new FunctionRegistry();
            registry.register(Fun.class, "fun", "fun");
//            QsonMapper qmapper = new QsonMapper();
//            QsonParser qreader = qmapper.parserFor(functionInvoker.getInputType()); //QuarkusQsonRegistry.getParser(functionInvoker.getInputType());
//            QsonObjectWriter qwriter = qmapper.writerFor(functionInvoker.getOutputType()); //QuarkusQsonRegistry.getWriter(functionInvoker.getOutputType());
//            System.err.println("r: " + qreader + ", w: " + qwriter);
            registry.invokers().forEach(fi -> {
                fi.getBindingContext().put(ObjectWriter.class.getName(), writer);
                fi.getBindingContext().put(ObjectReader.class.getName(), reader);
//                fi.getBindingContext().put(QsonParser.class.getName(), qreader);
//                fi.getBindingContext().put(QsonObjectWriter.class.getName(), qwriter);
            });
            FunctionRecorder.registry = registry;

            Executor executor = runnable -> runnable.run();

            Map<String, FunctionInvoker> typeTriggers = new HashMap<>();
            typeTriggers.put("benchType-in", functionInvoker);

            BeanContainer beanContainer = new MockBeanContainer();
            FunctionConstructor.CONTAINER = beanContainer;
            CDI.setCDIProvider(() -> MockFactory.cdi());

            knativeVertxRequestHandler = new VertxRequestHandler(
                    null,
                    "/",
                    beanContainer,
                    objectMapper,
                    null,
                    functionInvoker,
                    typeTriggers,
                    executor);

            httpVertxRequestHandler = new io.quarkus.funqy.runtime.bindings.http.VertxRequestHandler(
                    null,
                    beanContainer,
                    "/",
                    executor
            );

            Map<String, String> binHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            binHeaders.put("ce-id", "42");
            binHeaders.put("ce-specversion", "1.0");
            binHeaders.put("ce-source", "/benchSource-in");
            binHeaders.put("ce-type", "benchType-in");
            binHeaders.put("content-type", "application/json");
            Buffer binBody = Buffer.buffer("{ \"message\": \"Hello!\" }");

            String structured =  "{ \"id\" : \"1234\", " +
                    "  \"specversion\": \"1.0\", " +
                    "  \"source\": \"test\", " +
                    "  \"type\": \"tolower\", " +
                    "  \"datacontenttype\": \"application/json\", " +
                    "  \"data\": { \"message\" : \"Hello!\" }" +
                    "}";
            Map<String, String> structHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            structHeaders.put("Content-Type", "application/cloudevents+json");
            Buffer structBody = Buffer.buffer(structured);

            String path = "/";
            Map<String,String> params = Collections.emptyMap();

            HttpServerResponse response = MockFactory.httpServerResponse(blackhole);

            HttpServerRequest structRequest = MockFactory.httpServerRequest(structHeaders, HttpMethod.POST, params, structBody, path);
            structRoutingContext = MockFactory.routingContext(blackhole, structRequest, response);
            HttpServerRequest binRequest = MockFactory.httpServerRequest(binHeaders, HttpMethod.POST, params, binBody, path);
            binRoutingContext = MockFactory.routingContext(blackhole, binRequest, response);


            Map<String, String> vanillaHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            vanillaHeaders.put("Content-Type", "application/json");
            HttpServerRequest vanillaRequest = MockFactory.httpServerRequest(
                    vanillaHeaders,
                    HttpMethod.POST,
                    params,
                    Buffer.buffer("{ \"message\" : \"Hello!\" }"),
                    "/fun");
            vanillaRoutingContext = MockFactory.routingContext(blackhole, vanillaRequest, response);
        }
    }

    static {
        try {
            Class<?> holder = Arrays.stream(UUID.class.getDeclaredClasses())
                    .filter(x -> "Holder".equals(x.getSimpleName()))
                    .findFirst()
                    .orElse(null);
            Field numberGenerator = holder.getDeclaredField("numberGenerator");
            numberGenerator.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(numberGenerator, numberGenerator.getModifiers() & ~Modifier.FINAL);

            numberGenerator.set(null, new MockSecureRandom());
        } catch (Throwable t) {
            System.err.println("Failed to mock SecureRandom for UUID.");
        }
    }

//    @Benchmark
//    @BenchmarkMode(Mode.Throughput)
//    public void benchStructured(BenchmarkState benchmarkState) {
//        benchmarkState.vertxRequestHandler.handle(benchmarkState.structRoutingContext);
//    }
//
//    @Benchmark
//    @BenchmarkMode(Mode.Throughput)
//    public void benchBinary(BenchmarkState benchmarkState) {
//        benchmarkState.vertxRequestHandler.handle(benchmarkState.binRoutingContext);
//    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchVanilla(BenchmarkState benchmarkState) {
        benchmarkState.httpVertxRequestHandler.handle(benchmarkState.vanillaRoutingContext);
    }

}
