package benchmark.mixed;

import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.IntProcedure;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
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
public class MixedBenchmark {

    @Param({"1000", "10000", "100000", "1000000"})
    public int size;

    private FastList<Integer> fastList;
    private ImmutableList<Integer> immutableList;
    private IntArrayList intArrayList;
    private LongArrayList longArrayList;
    private UnifiedMap<Integer, Integer> unifiedMap;
    private ImmutableMap<Integer, Integer> immutableMap;
    private IntIntHashMap intIntMap;
    private IntObjectHashMap<Integer> intObjectMap;
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
        longArrayList = new LongArrayList(size);
        for (int i = 0; i < size; i++) {
            fastList.add(i);
            intArrayList.add(i);
            longArrayList.add(i);
        }
        immutableList = fastList.toImmutable();

        unifiedMap = new UnifiedMap<>(size * 2);
        intIntMap = new IntIntHashMap(size * 2);
        intObjectMap = new IntObjectHashMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            int value = i * 3;
            unifiedMap.put(i, value);
            intIntMap.put(i, value);
            intObjectMap.put(i, value);
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
    public void mixed_batch_traversal(Blackhole blackhole) {
        fastList.forEach((Procedure<Integer>) each -> blackhole.consume(each));
        immutableList.forEach((Procedure<Integer>) each -> blackhole.consume(each));
        blackhole.consume(fastList.injectInto(0L, (long acc, Integer value) -> acc + value));
        blackhole.consume(immutableList.injectInto(0L, (long acc, Integer value) -> acc + value));
        blackhole.consume(intArrayList.sum());
        blackhole.consume(longArrayList.sum());

        unifiedMap.forEachKeyValue((key, value) -> {
            blackhole.consume(key);
            blackhole.consume(value);
        });
        immutableMap.forEachKeyValue((key, value) -> {
            blackhole.consume(key);
            blackhole.consume(value);
        });
        intIntMap.forEachKeyValue((key, value) -> {
            blackhole.consume(key);
            blackhole.consume(value);
        });
        intObjectMap.forEachKeyValue((key, value) -> {
            blackhole.consume(key);
            blackhole.consume(value);
        });
        blackhole.consume(intIntMap.sum());

        unifiedSet.forEach((Procedure<Integer>) each -> blackhole.consume(each));
        immutableSet.forEach((Procedure<Integer>) each -> blackhole.consume(each));
        intHashSet.forEach((IntProcedure) each -> blackhole.consume(each));
        blackhole.consume(unifiedSet.injectInto(0L, (long acc, Integer value) -> acc + value));
        blackhole.consume(immutableSet.injectInto(0L, (long acc, Integer value) -> acc + value));
        blackhole.consume(intHashSet.sum());

        hashBag.forEach((Procedure<Integer>) each -> blackhole.consume(each));
        hashBag.forEachWithOccurrences((value, occurrences) -> {
            blackhole.consume(value);
            blackhole.consume(occurrences);
        });
        immutableBag.forEach((Procedure<Integer>) each -> blackhole.consume(each));
    }
}
