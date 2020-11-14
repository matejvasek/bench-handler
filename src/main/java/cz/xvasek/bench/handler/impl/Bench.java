package cz.xvasek.bench.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.quarkus.arc.ManagedContext;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.funqy.runtime.FunctionConstructor;
import io.quarkus.funqy.runtime.FunctionInvoker;
import io.quarkus.funqy.runtime.bindings.knative.events.VertxRequestHandler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Locale;
import io.vertx.ext.web.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_SOURCE;
import static io.quarkus.funqy.runtime.bindings.knative.events.KnativeEventsBindingRecorder.RESPONSE_TYPE;

public class Bench {

    private static class BenchRoutingContext extends BlackHoleRoutingContext {

        public BenchRoutingContext(Blackhole blackHole, HttpServerRequest request, HttpServerResponse response) {
            super(blackHole, request, response);
        }

        @Override
        public void next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RoutingContext put(String s, Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T get(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T remove(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Object> data() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Vertx vertx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String mountPoint() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Route currentRoute() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String normalisedPath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Cookie getCookie(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RoutingContext addCookie(io.vertx.core.http.Cookie cookie) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RoutingContext addCookie(Cookie cookie) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Cookie removeCookie(String s, boolean b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int cookieCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Cookie> cookies() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, io.vertx.core.http.Cookie> cookieMap() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getBodyAsString() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getBodyAsString(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonObject getBodyAsJson() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonArray getBodyAsJsonArray() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Buffer getBody() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<FileUpload> fileUploads() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Session session() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSessionAccessed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public User user() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Throwable failure() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int statusCode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAcceptableContentType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ParsedHeaderValues parsedHeaders() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int addHeadersEndHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeHeadersEndHandler(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int addBodyEndHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeBodyEndHandler(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int addEndHandler(Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeEndHandler(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean failed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setBody(Buffer buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setSession(Session session) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setUser(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clearUser() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAcceptableContentType(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reroute(HttpMethod httpMethod, String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Locale> acceptableLocales() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, String> pathParams() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String pathParam(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MultiMap queryParams() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<String> queryParam(String s) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BenchRequest extends InMemoryFunqyRequest {
        public BenchRequest(Map<String, String> headers, HttpMethod method, Map<String,String> params, Buffer body, String path) {
            super(headers, method, params, body, path);
        }

        @Override
        public HttpServerRequest handler(Handler<Buffer> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest pause() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest resume() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest fetch(long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest endHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpVersion version() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String rawMethod() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSSL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String scheme() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String uri() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String query() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String host() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long bytesRead() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse response() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHeader(CharSequence charSequence) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getParam(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SocketAddress remoteAddress() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SocketAddress localAddress() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SSLSession sslSession() {
            throw new UnsupportedOperationException();
        }

        @Override
        public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
//            return new X509Certificate[0];
            throw new UnsupportedOperationException();
        }

        @Override
        public String absoluteURI() {
            throw new UnsupportedOperationException();
        }

        @Override
        public NetSocket netSocket() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest setExpectMultipart(boolean b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExpectMultipart() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest uploadHandler(Handler<HttpServerFileUpload> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MultiMap formAttributes() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFormAttribute(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServerWebSocket upgrade() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEnded() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest customFrameHandler(Handler<HttpFrame> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpConnection connection() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerRequest streamPriorityHandler(Handler<StreamPriority> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public io.vertx.core.http.Cookie getCookie(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int cookieCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, io.vertx.core.http.Cookie> cookieMap() {
            throw new UnsupportedOperationException();
        }
    }

    private static class BenchResponse extends BlackHoleResponse {

        public BenchResponse(Blackhole blackhole) {
            super(blackhole);
        }

        @Override
        public HttpServerResponse exceptionHandler(Handler<Throwable> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(Buffer buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(Buffer buffer, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void end(Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse setWriteQueueMaxSize(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean writeQueueFull() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse drainHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getStatusCode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStatusMessage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse setStatusMessage(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse setChunked(boolean b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isChunked() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MultiMap headers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putHeader(String s, Iterable<String> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putHeader(CharSequence charSequence, Iterable<CharSequence> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MultiMap trailers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putTrailer(String s, String s1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putTrailer(CharSequence charSequence, CharSequence charSequence1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putTrailer(String s, Iterable<String> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse putTrailer(CharSequence charSequence, Iterable<CharSequence> iterable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse closeHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse endHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(String s, String s1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(String s, String s1, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse write(String s, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse writeContinue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void end(String s, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void end(String s, String s1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void end(String s, String s1, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void end(Buffer buffer, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse sendFile(String s, long l, long l1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse sendFile(String s, long l, long l1, Handler<AsyncResult<Void>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean closed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean headWritten() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse headersEndHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse bodyEndHandler(Handler<Void> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long bytesWritten() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int streamId() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse push(HttpMethod httpMethod, String s, String s1,
                Handler<AsyncResult<HttpServerResponse>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse push(HttpMethod httpMethod, String s, MultiMap multiMap,
                Handler<AsyncResult<HttpServerResponse>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse push(HttpMethod httpMethod, String s, Handler<AsyncResult<HttpServerResponse>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse push(HttpMethod httpMethod, String s, String s1, MultiMap multiMap,
                Handler<AsyncResult<HttpServerResponse>> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset(long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse writeCustomFrame(int i, int i1, Buffer buffer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpServerResponse addCookie(io.vertx.core.http.Cookie cookie) {
            throw new UnsupportedOperationException();
        }

        @Override
        public io.vertx.core.http.Cookie removeCookie(String s, boolean b) {
            throw new UnsupportedOperationException();
        }
    }

    public static RoutingContext createBenchRoutingContext(
            Blackhole blackhole,
            Map<String, String> headers,
            HttpMethod method,
            Map<String,String> params,
            Buffer body,
            String path) {
        BenchRequest brq = new BenchRequest(headers, method, params, body, path);
        BenchResponse brs = new BenchResponse(blackhole);
        return new BenchRoutingContext(blackhole, brq, brs);
    }

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

    private static class BenchManagedContext implements ManagedContext {
        @Override
        public Class<? extends Annotation> getScope() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T get(Contextual<T> contextual) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public void activate() {

        }

        @Override
        public void activate(ContextState initialState) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deactivate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void terminate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void destroy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ContextState getState() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void destroy(Contextual<?> contextual) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BenchBeanContainer implements BeanContainer {

        @Override
        public <T> Factory<T> instanceFactory(Class<T> TClass, Annotation... qualifiers) {
            return () -> (Instance<T>) () -> {
                if (Fun.class.isAssignableFrom(TClass)) {
                    return (T) new Fun();
                }
                throw new UnsupportedOperationException();
            };
        }

        @Override
        public ManagedContext requestContext() {
            return new BenchManagedContext();
        }
    }

    private static CDI<Object> benchCDI() {
        return new CDI<Object>() {
            @Override
            public BeanManager getBeanManager() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Instance<Object> select(Annotation... annotations) {
                throw new UnsupportedOperationException();
            }

            @Override
            public <U> Instance<U> select(Class<U> UClass, Annotation... annotations) {
                return new Instance<U>() {
                    @Override
                    public Instance<U> select(Annotation... annotations) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public <U1 extends U> Instance<U1> select(Class<U1> aClass, Annotation... annotations) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public <U1 extends U> Instance<U1> select(TypeLiteral<U1> typeLiteral, Annotation... annotations) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public boolean isUnsatisfied() {
                        if (CurrentVertxRequest.class.isAssignableFrom(UClass)) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public boolean isAmbiguous() {
                        return false;
                    }

                    @Override
                    public void destroy(U u) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Iterator<U> iterator() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public U get() {
                        if (CurrentVertxRequest.class.isAssignableFrom(UClass)) {
                            return (U) new CurrentVertxRequest();
                        }
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public <U> Instance<U> select(TypeLiteral<U> typeLiteral, Annotation... annotations) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isUnsatisfied() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isAmbiguous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void destroy(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iterator() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object get() {
                throw new UnsupportedOperationException();
            }
        };
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

            BenchBeanContainer beanContainer = new BenchBeanContainer();
            FunctionConstructor.CONTAINER = beanContainer;
            CDI.setCDIProvider(Bench::benchCDI);

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

            HttpServerResponse response = new BenchResponse(blackhole);
            HttpServerRequest structRequest = new BenchRequest(structHeaders, HttpMethod.POST, params, structBody, path);
            structRoutingContext = new BenchRoutingContext(blackhole, structRequest, response);
            HttpServerRequest binRequest = new BenchRequest(binHeaders, HttpMethod.POST, params, binBody, path);
            binRoutingContext = new BenchRoutingContext(blackhole, binRequest, response);
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
