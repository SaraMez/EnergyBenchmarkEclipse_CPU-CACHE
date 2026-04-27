package benchmark.cache;

import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class CacheBenchmark {

    @Param({"1000", "10000", "100000", "1000000"})
    public int size;

    private FastList<Integer> fastList;
    private ImmutableList<Integer> immutableList;
    private IntArrayList intArrayList;
    private UnifiedMap<Integer, Integer> unifiedMap;
    private ImmutableMap<Integer, Integer> immutableMap;
    private IntIntHashMap intIntMap;
    private UnifiedSet<Integer> unifiedSet;
    private ImmutableSet<Integer> immutableSet;
    private IntHashSet intHashSet;
    private HashBag<Integer> hashBag;
    private ImmutableBag<Integer> immutableBag;
    private int distinctValues;

    @Setup(Level.Trial)
    public void setup() {
        fastList = new FastList<>(size);
        intArrayList = new IntArrayList(size);
        for (int i = 0; i < size; i++) {
            fastList.add(i);
            intArrayList.add(i);
        }
        immutableList = fastList.toImmutable();

        unifiedMap = new UnifiedMap<>(size * 2);
        intIntMap = new IntIntHashMap(size * 2);
        for (int i = 0; i < size; i++) {
            int value = i * 3;
            unifiedMap.put(i, value);
            intIntMap.put(i, value);
        }
        immutableMap = unifiedMap.toImmutable();

        unifiedSet = new UnifiedSet<>(size * 2);
        intHashSet = new IntHashSet(size * 2);
        for (int i = 0; i < size; i++) {
            unifiedSet.add(i);
            intHashSet.add(i);
        }
        immutableSet = unifiedSet.toImmutable();

        distinctValues = Math.max(1, Math.min(size, 1024));
        hashBag = new HashBag<>(size);
        for (int i = 0; i < size; i++) {
            hashBag.add(i % distinctValues);
        }
        immutableBag = hashBag.toImmutable();
    }

    @Benchmark
    public void cache_batch_reads(Blackhole blackhole) {
        int mid = size / 2;
        blackhole.consume(fastList.contains(mid));
        blackhole.consume(immutableList.detect(each -> each == mid));
        blackhole.consume(intArrayList.anySatisfy(each -> each == mid));

        blackhole.consume(unifiedMap.containsKey(mid));
        blackhole.consume(immutableMap.get(mid));
        blackhole.consume(intIntMap.get(mid));

        blackhole.consume(unifiedSet.contains(mid));
        blackhole.consume(immutableSet.contains(mid));
        blackhole.consume(intHashSet.anySatisfy(value -> value == mid));

        blackhole.consume(hashBag.occurrencesOf(distinctValues / 2));
        blackhole.consume(immutableBag.occurrencesOf(distinctValues / 2));
    }
}
