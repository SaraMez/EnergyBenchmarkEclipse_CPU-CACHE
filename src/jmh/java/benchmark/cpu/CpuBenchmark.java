package benchmark.cpu;

import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class CpuBenchmark {

    @Param({"1000", "10000", "100000", "1000000"})
    public int size;

    @Benchmark
    public void cpu_batch_mutations(Blackhole blackhole) {
        FastList<Integer> list = new FastList<>(size);
        IntArrayList primitiveList = new IntArrayList(size);
        UnifiedMap<Integer, Integer> map = new UnifiedMap<>(size * 2);
        UnifiedSet<Integer> set = new UnifiedSet<>(size * 2);
        HashBag<Integer> bag = new HashBag<>(size);
        int distinctValues = Math.max(1, Math.min(size, 1024));

        for (int i = 0; i < size; i++) {
            list.add(i);
            primitiveList.add(i);
            map.put(i, i * 3);
            set.add(i);
            bag.add(i % distinctValues);
        }

        int target = size / 2;
        blackhole.consume(list.reject(each -> each == target));
        blackhole.consume(primitiveList.reject(each -> each == target));
        blackhole.consume(map.reject((key, value) -> key == target));
        blackhole.consume(set.reject(value -> value == target));
        blackhole.consume(bag.reject(value -> value == distinctValues / 2));
    }
}
