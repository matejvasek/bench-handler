package cz.xvasek.bench.handler;

import cz.xvasek.bench.handler.impl.Bench;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.enterprise.inject.spi.CDI;

@QuarkusMain
public class Main {
    public static void main(String[] args) {
        Quarkus.run(BenchApp.class, args);
    }

    public static class BenchApp implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            org.openjdk.jmh.Main.main(args);
            // in native mode JMH doesn't work but at least user can try to run code below
//            Bench.BenchmarkState state = new Bench.BenchmarkState();
//            state.init(null);
//            Bench b = new Bench();
//            for (int i = 0; i < 1_000_000; i++) {
//                b.benchBinary(state);
//            }
            return 0;
        }
    }
}
