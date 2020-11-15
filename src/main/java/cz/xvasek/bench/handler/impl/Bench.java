package cz.xvasek.bench.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.xvasek.bench.handler.impl.mocks.MockBeanContainer;
import cz.xvasek.bench.handler.impl.mocks.MockFactory;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.funqy.runtime.FunctionConstructor;
import io.quarkus.funqy.runtime.FunctionInvoker;
import io.quarkus.funqy.runtime.bindings.knative.events.VertxRequestHandler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_SOURCE;
import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_TYPE;

public class Bench {

    @RegisterForReflection
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
        private VertxRequestHandler vertxRequestHandler;
        private RoutingContext structRoutingContext;
        private RoutingContext binRoutingContext;

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

            Executor executor = runnable -> runnable.run();

            Map<String, FunctionInvoker> typeTriggers = new HashMap<>();
            typeTriggers.put("benchType-in", functionInvoker);

            BeanContainer beanContainer = new MockBeanContainer();
            FunctionConstructor.CONTAINER = beanContainer;
            CDI.setCDIProvider(() -> MockFactory.cdi());

            vertxRequestHandler = new VertxRequestHandler(
                    null,
                    "/",
                    beanContainer,
                    objectMapper,
                    null,
                    functionInvoker,
                    typeTriggers,
                    executor);

            Map<String, String> binHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            binHeaders.put("Ce-Id", "42");
            binHeaders.put("Ce-Specversion", "1.0");
            binHeaders.put("Ce-Source", "/benchSource-in");
            binHeaders.put("Ce-Type", "benchType-in");
            binHeaders.put("Content-Type", "application/json");
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
            Map<String,String> params = new HashMap<>();

            HttpServerResponse response = MockFactory.httpServerResponse(blackhole);

            HttpServerRequest structRequest = MockFactory.httpServerRequest(structHeaders, HttpMethod.POST, params, structBody, path);
            structRoutingContext = MockFactory.routingContext(blackhole, structRequest, response);
            HttpServerRequest binRequest = MockFactory.httpServerRequest(binHeaders, HttpMethod.POST, params, binBody, path);
            binRoutingContext = MockFactory.routingContext(blackhole, binRequest, response);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchStructured(BenchmarkState benchmarkState) {
        benchmarkState.vertxRequestHandler.handle(benchmarkState.structRoutingContext);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchBinary(BenchmarkState benchmarkState) {
        benchmarkState.vertxRequestHandler.handle(benchmarkState.binRoutingContext);
    }

}
