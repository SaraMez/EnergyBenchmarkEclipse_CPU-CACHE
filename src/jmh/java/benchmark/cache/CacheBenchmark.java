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
        for (int i = 0; i < size; i++) {
            fastList.add(i);
            intArrayList.add(i);
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
    public boolean search_fastList_contains() {
        return fastList.contains(size / 2);
    }

    @Benchmark
    public boolean search_immutableList_contains() {
        return immutableList.contains(size / 2);
    }

    @Benchmark
    public boolean search_intArrayList_contains() {
        return intArrayList.contains(size / 2);
    }

    @Benchmark
    public int search_fastList_detect() {
        Integer value = fastList.detect(each -> each == size / 2);
        return value == null ? -1 : value;
    }

    @Benchmark
    public int search_immutableList_detect() {
        Integer value = immutableList.detect(each -> each == size / 2);
        return value == null ? -1 : value;
    }

    @Benchmark
    public boolean search_intArrayList_anySatisfy() {
        return intArrayList.anySatisfy(each -> each == size / 2);
    }

    @Benchmark
    public boolean search_unifiedMap_containsKey() {
        return unifiedMap.containsKey(size / 2);
    }

    @Benchmark
    public boolean search_immutableMap_containsKey() {
        return immutableMap.containsKey(size / 2);
    }

    @Benchmark
    public boolean search_intIntHashMap_containsKey() {
        return intIntMap.containsKey(size / 2);
    }

    @Benchmark
    public int search_unifiedMap_get() {
        Integer value = unifiedMap.get(size / 2);
        return value == null ? -1 : value;
    }

    @Benchmark
    public int search_immutableMap_get() {
        Integer value = immutableMap.get(size / 2);
        return value == null ? -1 : value;
    }

    @Benchmark
    public int search_unifiedMap_getIfAbsent() {
        return unifiedMap.getIfAbsent(size / 2, () -> -1);
    }

    @Benchmark
    public int search_intIntHashMap_get() {
        return intIntMap.get(size / 2);
    }

    @Benchmark
    public int search_intObjectHashMap_get() {
        Integer value = intObjectMap.get(size / 2);
        return value == null ? -1 : value;
    }

    @Benchmark
    public boolean search_unifiedSet_contains() {
        return unifiedSet.contains(size / 2);
    }

    @Benchmark
    public boolean search_immutableSet_contains() {
        return immutableSet.contains(size / 2);
    }

    @Benchmark
    public boolean search_intHashSet_contains() {
        return intHashSet.contains(size / 2);
    }

    @Benchmark
    public boolean search_unifiedSet_anySatisfy() {
        return unifiedSet.anySatisfy(value -> value == size / 2);
    }

    @Benchmark
    public boolean search_intHashSet_anySatisfy() {
        return intHashSet.anySatisfy(value -> value == size / 2);
    }

    @Benchmark
    public int search_hashBag_occurrencesOf() {
        return hashBag.occurrencesOf(distinctValues / 2);
    }

    @Benchmark
    public int search_immutableBag_occurrencesOf() {
        return immutableBag.occurrencesOf(distinctValues / 2);
    }

    @Benchmark
    public boolean search_hashBag_contains() {
        return hashBag.contains(distinctValues / 2);
    }
}
