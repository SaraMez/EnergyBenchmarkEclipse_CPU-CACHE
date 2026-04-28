package benchmark.cpu;

import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
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

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class CpuBenchmark {

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
    public FastList<Integer> insert_fastList() {
        FastList<Integer> list = new FastList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public ImmutableList<Integer> insert_immutableList() {
        FastList<Integer> list = new FastList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list.toImmutable();
    }

    @Benchmark
    public IntArrayList insert_intArrayList() {
        IntArrayList list = new IntArrayList(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public LongArrayList insert_longArrayList() {
        LongArrayList list = new LongArrayList(size);
        for (long i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public UnifiedMap<Integer, Integer> insert_unifiedMap() {
        UnifiedMap<Integer, Integer> map = new UnifiedMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            map.put(i, i * 3);
        }
        return map;
    }

    @Benchmark
    public ImmutableMap<Integer, Integer> insert_immutableMap() {
        UnifiedMap<Integer, Integer> map = new UnifiedMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            map.put(i, i * 3);
        }
        return map.toImmutable();
    }

    @Benchmark
    public IntIntHashMap insert_intIntHashMap() {
        IntIntHashMap map = new IntIntHashMap(size * 2);
        for (int i = 0; i < size; i++) {
            map.put(i, i * 3);
        }
        return map;
    }

    @Benchmark
    public IntObjectHashMap<Integer> insert_intObjectHashMap() {
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            map.put(i, i * 3);
        }
        return map;
    }

    @Benchmark
    public UnifiedSet<Integer> insert_unifiedSet() {
        UnifiedSet<Integer> set = new UnifiedSet<>(size * 2);
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    public ImmutableSet<Integer> insert_immutableSet() {
        UnifiedSet<Integer> set = new UnifiedSet<>(size * 2);
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        return set.toImmutable();
    }

    @Benchmark
    public IntHashSet insert_intHashSet() {
        IntHashSet set = new IntHashSet(size * 2);
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    public HashBag<Integer> insert_hashBag() {
        HashBag<Integer> bag = new HashBag<>(size);
        for (int i = 0; i < size; i++) {
            bag.add(i % distinctValues);
        }
        return bag;
    }

    @Benchmark
    public ImmutableBag<Integer> insert_immutableBag() {
        HashBag<Integer> bag = new HashBag<>(size);
        for (int i = 0; i < size; i++) {
            bag.add(i % distinctValues);
        }
        return bag.toImmutable();
    }

    @Benchmark
    public MutableList<Integer> delete_fastList_reject() {
        final int target = size / 2;
        return fastList.reject(each -> each == target);
    }

    @Benchmark
    public ImmutableList<Integer> delete_immutableList_reject() {
        final int target = size / 2;
        return immutableList.reject(each -> each == target);
    }

    @Benchmark
    public MutableIntList delete_intArrayList_reject() {
        final int target = size / 2;
        return intArrayList.reject(each -> each == target);
    }

    @Benchmark
    public MutableLongList delete_longArrayList_reject() {
        final long target = size / 2L;
        return longArrayList.reject(each -> each == target);
    }

    @Benchmark
    public MutableMap<Integer, Integer> delete_unifiedMap_reject() {
        final int target = size / 2;
        return unifiedMap.reject((key, value) -> key == target);
    }

    @Benchmark
    public ImmutableMap<Integer, Integer> delete_immutableMap_reject() {
        final int target = size / 2;
        return immutableMap.reject((key, value) -> key == target);
    }

    @Benchmark
    public MutableIntIntMap delete_intIntHashMap_reject() {
        final int target = size / 2;
        return intIntMap.reject((key, value) -> key == target);
    }

    @Benchmark
    public MutableIntObjectMap<Integer> delete_intObjectHashMap_reject() {
        final int target = size / 2;
        return intObjectMap.reject((key, value) -> key == target);
    }

    @Benchmark
    public MutableSet<Integer> delete_unifiedSet_reject() {
        final int target = size / 2;
        return unifiedSet.reject(value -> value == target);
    }

    @Benchmark
    public ImmutableSet<Integer> delete_immutableSet_reject() {
        final int target = size / 2;
        return immutableSet.reject(value -> value == target);
    }

    @Benchmark
    public MutableIntSet delete_intHashSet_reject() {
        final int target = size / 2;
        return intHashSet.reject(value -> value == target);
    }

    @Benchmark
    public MutableBag<Integer> delete_hashBag_reject() {
        final int target = distinctValues / 2;
        return hashBag.reject(value -> value == target);
    }

    @Benchmark
    public Object delete_immutableBag_reject() {
        final int target = distinctValues / 2;
        return immutableBag.reject(value -> value == target);
    }
}
