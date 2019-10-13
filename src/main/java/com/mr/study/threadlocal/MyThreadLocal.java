//package com.mr.study.threadlocal;
//
//import java.lang.ref.WeakReference;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.Supplier;
//
///**
// * @author zhanxp
// * @version 1.0 2019/8/1
// */
//public class MyThreadLocal<T> {
//    /**
//     * 用于保存该对象的hashCode，通过计算得到，该算法可以很大程度上避免hash冲突
//     */
//    private final int threadLocalHashCode = nextHashCode();
//
//    /**
//     * 下一个ThreadLocal的hash值
//     */
//    private static AtomicInteger nextHashCode =
//            new AtomicInteger();
//
//    /**
//     * 每次hash增加的值
//     */
//    private static final int HASH_INCREMENT = 0x61c88647;
//
//    /**
//     * 获取下一个hash值
//     *
//     * @return
//     */
//    private static int nextHashCode() {
//        return nextHashCode.getAndAdd(HASH_INCREMENT);
//    }
//
//    /**
//     * 在线程没有初始化值的时候返回的初始值
//     *
//     * @return
//     */
//    protected T initialValue() {
//        return null;
//    }
//
//    /**
//     * 通过supplier函数初始化值
//     *
//     * @param supplier
//     * @param <S>
//     * @return
//     */
//    public static <S> MyThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
//        return new MyThreadLocal.SuppliedMyThreadLocal<>(supplier);
//    }
//
//    /**
//     * Creates a thread local variable.
//     *
//     * @see #withInitial(java.util.function.Supplier)
//     */
//    public MyThreadLocal() {
//    }
//
//    /**
//     * 获取线程本地变量
//     *
//     * @return
//     */
//    public T get() {
//        //获取当前线程
//        Thread t = Thread.currentThread();
//        MyThreadLocal.MyThreadLocalMap map = getMap(t);
//        if (map != null) {
//            //如果map存在，则直接从map中获取值
//            MyThreadLocal.MyThreadLocalMap.Entry e = map.getEntry(this);
//            if (e != null) {
//                @SuppressWarnings("unchecked")
//                T result = (T) e.value;
//                return result;
//            }
//        }
//        //map不存在或者没值，返回初始化值
//        return setInitialValue();
//    }
//
//    /**
//     * Variant of set() to establish initialValue. Used instead
//     * of set() in case user has overridden the set() method.
//     *
//     * @return the initial value
//     */
//    private T setInitialValue() {
//        T value = initialValue();
//        Thread t = Thread.currentThread();
//        MyThreadLocal.MyThreadLocalMap map = getMap(t);
//        if (map != null)
//            map.set(this, value);
//        else
//            createMap(t, value);
//        return value;
//    }
//
//    /**
//     * Sets the current thread's copy of this thread-local variable
//     * to the specified value.  Most subclasses will have no need to
//     * override this method, relying solely on the {@link #initialValue}
//     * method to set the values of thread-locals.
//     *
//     * @param value the value to be stored in the current thread's copy of
//     *              this thread-local.
//     */
//    public void set(T value) {
//        Thread t = Thread.currentThread();
//        MyThreadLocal.MyThreadLocalMap map = getMap(t);
//        if (map != null)
//            map.set(this, value);
//        else
//            createMap(t, value);
//    }
//
//    /**
//     * Removes the current thread's value for this thread-local
//     * variable.  If this thread-local variable is subsequently
//     * {@linkplain #get read} by the current thread, its value will be
//     * reinitialized by invoking its {@link #initialValue} method,
//     * unless its value is {@linkplain #set set} by the current thread
//     * in the interim.  This may result in multiple invocations of the
//     * {@code initialValue} method in the current thread.
//     *
//     * @since 1.5
//     */
//    public void remove() {
//        MyThreadLocal.MyThreadLocalMap m = getMap(Thread.currentThread());
//        if (m != null) {
//            m.remove(this);
//        }
//    }
//
//    /**
//     * Get the map associated with a MyThreadLocal. Overridden in
//     * InheritableMyThreadLocal.
//     *
//     * @param t the current thread
//     * @return the map
//     */
//    MyThreadLocal.MyThreadLocalMap getMap(Thread t) {
//        //线程内部保存了一个MyThreadLocalMap变量，直接获取该变量，该变量是该线程独有的
//        return t.threadLocals;
//    }
//
//    /**
//     * Create the map associated with a MyThreadLocal. Overridden in
//     * InheritableMyThreadLocal.
//     *
//     * @param t          the current thread
//     * @param firstValue value for the initial entry of the map
//     */
//    void createMap(Thread t, T firstValue) {
//        t.threadLocals = new MyThreadLocal.MyThreadLocalMap(this, firstValue);
//    }
//
//    /**
//     * Factory method to create map of inherited thread locals.
//     * Designed to be called only from Thread constructor.
//     *
//     * @param parentMap the map associated with parent thread
//     * @return a map containing the parent's inheritable bindings
//     */
//    static MyThreadLocal.MyThreadLocalMap createInheritedMap(MyThreadLocal.MyThreadLocalMap parentMap) {
//        return new MyThreadLocal.MyThreadLocalMap(parentMap);
//    }
//
//    /**
//     * Method childValue is visibly defined in subclass
//     * InheritableMyThreadLocal, but is internally defined here for the
//     * sake of providing createInheritedMap factory method without
//     * needing to subclass the map class in InheritableMyThreadLocal.
//     * This technique is preferable to the alternative of embedding
//     * instanceof tests in methods.
//     */
//    T childValue(T parentValue) {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * An extension of MyThreadLocal that obtains its initial value from
//     * the specified {@code Supplier}.
//     */
//    static final class SuppliedMyThreadLocal<T> extends MyThreadLocal<T> {
//
//        private final Supplier<? extends T> supplier;
//
//        SuppliedMyThreadLocal(Supplier<? extends T> supplier) {
//            this.supplier = Objects.requireNonNull(supplier);
//        }
//
//        @Override
//        protected T initialValue() {
//            return supplier.get();
//        }
//    }
//
//    /**
//     * MyThreadLocalMap is a customized hash map suitable only for
//     * maintaining thread local values. No operations are exported
//     * outside of the MyThreadLocal class. The class is package private to
//     * allow declaration of fields in class Thread.  To help deal with
//     * very large and long-lived usages, the hash table entries use
//     * WeakReferences for keys. However, since reference queues are not
//     * used, stale entries are guaranteed to be removed only when
//     * the table starts running out of space.
//     */
//    static class MyThreadLocalMap {
//
//        /**
//         * The entries in this hash map extend WeakReference, using
//         * its main ref field as the key (which is always a
//         * MyThreadLocal object).  Note that null keys (i.e. entry.get()
//         * == null) mean that the key is no longer referenced, so the
//         * entry can be expunged from table.  Such entries are referred to
//         * as "stale entries" in the code that follows.
//         */
//        static class Entry extends WeakReference<MyThreadLocal<?>> {
//            /**
//             * The value associated with this MyThreadLocal.
//             */
//            Object value;
//
//            Entry(MyThreadLocal<?> k, Object v) {
//                super(k);
//                value = v;
//            }
//        }
//
//        /**
//         * The initial capacity -- MUST be a power of two.
//         */
//        private static final int INITIAL_CAPACITY = 16;
//
//        /**
//         * The table, resized as necessary.
//         * table.length MUST always be a power of two.
//         */
//        private MyThreadLocal.MyThreadLocalMap.Entry[] table;
//
//        /**
//         * The number of entries in the table.
//         */
//        private int size = 0;
//
//        /**
//         * The next size value at which to resize.
//         */
//        private int threshold; // Default to 0
//
//        /**
//         * Set the resize threshold to maintain at worst a 2/3 load factor.
//         */
//        private void setThreshold(int len) {
//            threshold = len * 2 / 3;
//        }
//
//        /**
//         * Increment i modulo len.
//         */
//        private static int nextIndex(int i, int len) {
//            return ((i + 1 < len) ? i + 1 : 0);
//        }
//
//        /**
//         * Decrement i modulo len.
//         */
//        private static int prevIndex(int i, int len) {
//            return ((i - 1 >= 0) ? i - 1 : len - 1);
//        }
//
//        /**
//         * Construct a new map initially containing (firstKey, firstValue).
//         * MyThreadLocalMaps are constructed lazily, so we only create
//         * one when we have at least one entry to put in it.
//         */
//        MyThreadLocalMap(MyThreadLocal<?> firstKey, Object firstValue) {
//            table = new MyThreadLocal.MyThreadLocalMap.Entry[INITIAL_CAPACITY];
//            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
//            table[i] = new MyThreadLocal.MyThreadLocalMap.Entry(firstKey, firstValue);
//            size = 1;
//            setThreshold(INITIAL_CAPACITY);
//        }
//
//        /**
//         * Construct a new map including all Inheritable MyThreadLocals
//         * from given parent map. Called only by createInheritedMap.
//         *
//         * @param parentMap the map associated with parent thread.
//         */
//        private MyThreadLocalMap(MyThreadLocal.MyThreadLocalMap parentMap) {
//            MyThreadLocal.MyThreadLocalMap.Entry[] parentTable = parentMap.table;
//            int len = parentTable.length;
//            setThreshold(len);
//            table = new MyThreadLocal.MyThreadLocalMap.Entry[len];
//
//            for (int j = 0; j < len; j++) {
//                MyThreadLocal.MyThreadLocalMap.Entry e = parentTable[j];
//                if (e != null) {
//                    @SuppressWarnings("unchecked")
//                    MyThreadLocal<Object> key = (MyThreadLocal<Object>) e.get();
//                    if (key != null) {
//                        Object value = key.childValue(e.value);
//                        MyThreadLocal.MyThreadLocalMap.Entry c = new MyThreadLocal.MyThreadLocalMap.Entry(key, value);
//                        int h = key.threadLocalHashCode & (len - 1);
//                        while (table[h] != null)
//                            h = nextIndex(h, len);
//                        table[h] = c;
//                        size++;
//                    }
//                }
//            }
//        }
//
//        /**
//         * 获取当前本地变量所对应的entry对象
//         *
//         * @param key
//         * @return
//         */
//        private MyThreadLocal.MyThreadLocalMap.Entry getEntry(MyThreadLocal<?> key) {
//            //计算所在数组中的未知
//            int i = key.threadLocalHashCode & (table.length - 1);
//            MyThreadLocal.MyThreadLocalMap.Entry e = table[i];
//            if (e != null && e.get() == key) {
//                //如果数组中当前位置的值存在，并且就是该线程变量，则返回当前值
//                return e;
//            } else {
//                //如果当前为空，或者冲突了，调用该方法找值
//                return getEntryAfterMiss(key, i, e);
//            }
//        }
//
//        /**
//         * Version of getEntry method for use when key is not found in
//         * its direct hash slot.
//         *
//         * @param key the thread local object
//         * @param i   the table index for key's hash code
//         * @param e   the entry at table[i]
//         * @return the entry associated with key, or null if no such
//         */
//        private MyThreadLocal.MyThreadLocalMap.Entry getEntryAfterMiss(MyThreadLocal<?> key, int i, MyThreadLocal.MyThreadLocalMap.Entry e) {
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//
//            //如果当前e不为空，且不是当前值，那么是hash冲突了，往后找就可以了，如果当前e为null，则说明不存在当前值，直接返回null
//            while (e != null) {
//                //获取threadLocal对象
//                MyThreadLocal<?> k = e.get();
//                //如果就是当前值，直接返回
//                if (k == key) {
//                    return e;
//                }
//                //如果当前k为空，说明threadLocal已经没有被引用了，这个时候应该清除k所对应的值，避免内存泄露
//                if (k == null) {
//                    expungeStaleEntry(i);
//                } else {
//                    //如果当前k不为空，并且不是当前值，则继续往下找
//                    i = nextIndex(i, len);
//                }
//                //当前值等于下一个值
//                e = tab[i];
//            }
//            return null;
//        }
//
//        /**
//         * Set the value associated with key.
//         *
//         * @param key   the thread local object
//         * @param value the value to be set
//         */
//        private void set(MyThreadLocal<?> key, Object value) {
//
//            // We don't use a fast path as with get() because it is at
//            // least as common to use set() to create new entries as
//            // it is to replace existing ones, in which case, a fast
//            // path would fail more often than not.
//
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//            //计算ThreadLocal的hash值
//            int i = key.threadLocalHashCode & (len - 1);
//
//            //初始化值应该填充再数组i位置，如果值已经存在了，则往下找，直到找到为空或当前值为止
//            for (MyThreadLocal.MyThreadLocalMap.Entry e = tab[i];
//                 e != null;
//                 e = tab[i = nextIndex(i, len)]) {
//                MyThreadLocal<?> k = e.get();
//
//                //如果key找到，那就直接替换掉当前的值
//                if (k == key) {
//                    e.value = value;
//                    return;
//                }
//
//                //如果key为null，说明threadLocal不在被引用，直接用当前的替换掉就可以了
//                if (k == null) {
//                    replaceStaleEntry(key, value, i);
//                    return;
//                }
//            }
//
//            //创建一个新的entry对象，对数组赋值
//            tab[i] = new MyThreadLocal.MyThreadLocalMap.Entry(key, value);
//            //操作值加1
//            int sz = ++size;
//            //清除空槽，如果没有引用被清楚，那么判断是否超过容量，超过了需要重新hash
//            if (!cleanSomeSlots(i, sz) && sz >= threshold) {
//                rehash();
//            }
//        }
//
//        /**
//         * Remove the entry for key.
//         */
//        private void remove(MyThreadLocal<?> key) {
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//            //获取数组中未hash冲突的位置
//            int i = key.threadLocalHashCode & (len - 1);
//            for (MyThreadLocal.MyThreadLocalMap.Entry e = tab[i];
//                 e != null;
//                 e = tab[i = nextIndex(i, len)]) {
//                //如果找到了当前值，则清空当前值，清空引用
//                if (e.get() == key) {
//                    e.clear();
//                    expungeStaleEntry(i);
//                    return;
//                }
//            }
//        }
//
//        /**
//         * Replace a stale entry encountered during a set operation
//         * with an entry for the specified key.  The value passed in
//         * the value parameter is stored in the entry, whether or not
//         * an entry already exists for the specified key.
//         * <p>
//         * As a side effect, this method expunges all stale entries in the
//         * "run" containing the stale entry.  (A run is a sequence of entries
//         * between two null slots.)
//         *
//         * @param key       the key
//         * @param value     the value to be associated with key
//         * @param staleSlot index of the first stale entry encountered while
//         *                  searching for key.
//         */
//        private void replaceStaleEntry(MyThreadLocal<?> key, Object value,
//                                       int staleSlot) {
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//            MyThreadLocal.MyThreadLocalMap.Entry e;
//
//            // Back up to check for prior stale entry in current run.
//            // We clean out whole runs at a time to avoid continual
//            // incremental rehashing due to garbage collector freeing
//            // up refs in bunches (i.e., whenever the collector runs).
//            //当前位置
//            int slotToExpunge = staleSlot;
//            //从当前位置往前找，找到数组值为null后的第一个localThread引用被清除的值
//            for (int i = prevIndex(staleSlot, len);
//                 (e = tab[i]) != null;
//                 i = prevIndex(i, len)) {
//                if (e.get() == null) {
//                    slotToExpunge = i;
//                }
//            }
//
//
//            //查找当前运行的key或者尾随的一个空槽，找到就停止
//            for (int i = nextIndex(staleSlot, len);
//                 (e = tab[i]) != null;
//                 i = nextIndex(i, len)) {
//                MyThreadLocal<?> k = e.get();
//
//                // If we find key, then we need to swap it
//                // with the stale entry to maintain hash table order.
//                // The newly stale slot, or any other stale slot
//                // encountered above it, can then be sent to expungeStaleEntry
//                // to remove or rehash all of the other entries in run.
//                // 如果找到当前需要存的值，则直接赋值
//                if (k == key) {
//                    e.value = value;
//
//                    //值互换
//                    tab[i] = tab[staleSlot];
//
//                    //staleSlot位置保存当前要插入的值
//                    tab[staleSlot] = e;
//
//                    // Start expunge at preceding stale entry if it exists
//                    //如果需要清除的是staleSlot，那么需要清除的下标为i（因为值已经发生了互换）
//                    if (slotToExpunge == staleSlot) {
//                        slotToExpunge = i;
//                    }
//                    //清除key为null的引用操作
//                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
//                    return;
//                }
//
//                // If we didn't find stale entry on backward scan, the
//                // first stale entry seen while scanning for key is the
//                // first still present in the run.
//                if (k == null && slotToExpunge == staleSlot) {
//                    slotToExpunge = i;
//                }
//            }
//
//            // If key not found, put new entry in stale slot
//            tab[staleSlot].value = null;
//            tab[staleSlot] = new MyThreadLocal.MyThreadLocalMap.Entry(key, value);
//
//            // If there are any other stale entries in run, expunge them
//            if (slotToExpunge != staleSlot) {
//                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
//            }
//        }
//
//        /**
//         * Expunge a stale entry by rehashing any possibly colliding entries
//         * lying between staleSlot and the next null slot.  This also expunges
//         * any other stale entries encountered before the trailing null.  See
//         * Knuth, Section 6.4
//         *
//         * @param staleSlot index of slot known to have null key
//         * @return the index of the next null slot after staleSlot
//         * (all between staleSlot and this slot will have been checked
//         * for expunging).
//         */
//        private int expungeStaleEntry(int staleSlot) {
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//
//            // expunge entry at staleSlot
//            //使当前value里面的值不被引用，可以回收
//            tab[staleSlot].value = null;
//            //空出数组中的位置
//            tab[staleSlot] = null;
//            //存在的值减1
//            size--;
//
//            // Rehash until we encounter null
//            MyThreadLocal.MyThreadLocalMap.Entry e;
//            int i;
//            //因为有值被删除，需要重新计算hash,至到找到数组中的值为空的为止
//            for (i = nextIndex(staleSlot, len);
//                 (e = tab[i]) != null;
//                 i = nextIndex(i, len)) {
//                MyThreadLocal<?> k = e.get();
//                //如果key为null，则清除引用
//                if (k == null) {
//                    e.value = null;
//                    tab[i] = null;
//                    size--;
//                } else {
//                    //如果key不为null，需要重新计算hash，放到数组中正确的位置
//                    int h = k.threadLocalHashCode & (len - 1);
//                    if (h != i) {
//                        //如果算出来的值和i不相等，则把i里面当前的值清空，然后把当前值填入合适的位置
//                        tab[i] = null;
//
//                        // Unlike Knuth 6.4 Algorithm R, we must scan until
//                        // null because multiple entries could have been stale.
//                        //判断数组h位置的值是否为空，如果不为空，则一直往下找，直到找到为空为止
//                        while (tab[h] != null) {
//                            h = nextIndex(h, len);
//                        }
//                        //赋值
//                        tab[h] = e;
//                    }
//                }
//            }
//            return i;
//        }
//
//        /**
//         * Heuristically scan some cells looking for stale entries.
//         * This is invoked when either a new element is added, or
//         * another stale one has been expunged. It performs a
//         * logarithmic number of scans, as a balance between no
//         * scanning (fast but retains garbage) and a number of scans
//         * proportional to number of elements, that would find all
//         * garbage but would cause some insertions to take O(n) time.
//         *
//         * @param i a position known NOT to hold a stale entry. The
//         *          scan starts at the element after i.
//         * @param n scan control: {@code log2(n)} cells are scanned,
//         *          unless a stale entry is found, in which case
//         *          {@code log2(table.length)-1} additional cells are scanned.
//         *          When called from insertions, this parameter is the number
//         *          of elements, but when from replaceStaleEntry, it is the
//         *          table length. (Note: all this could be changed to be either
//         *          more or less aggressive by weighting n instead of just
//         *          using straight log n. But this version is simple, fast, and
//         *          seems to work well.)
//         * @return true if any stale entries have been removed.
//         */
//        private boolean cleanSomeSlots(int i, int n) {
//            boolean removed = false;
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//            //循环找是否存在key为null，值不为空的对象，如果存在，则清除
//            do {
//                i = nextIndex(i, len);
//                MyThreadLocal.MyThreadLocalMap.Entry e = tab[i];
//                if (e != null && e.get() == null) {
//                    n = len;
//                    removed = true;
//                    i = expungeStaleEntry(i);
//                }
//            } while ((n >>>= 1) != 0);
//            return removed;
//        }
//
//        /**
//         * Re-pack and/or re-size the table. First scan the entire
//         * table removing stale entries. If this doesn't sufficiently
//         * shrink the size of the table, double the table size.
//         */
//        private void rehash() {
//            //先清一下所有引用，删除不必要的内存
//            expungeStaleEntries();
//
//            // Use lower threshold for doubling to avoid hysteresis
//            //如果当前大小超过容量的3/4，则重新调整大小
//            if (size >= threshold - threshold / 4) {
//                resize();
//            }
//        }
//
//        /**
//         * Double the capacity of the table.
//         */
//        private void resize() {
//            MyThreadLocal.MyThreadLocalMap.Entry[] oldTab = table;
//            int oldLen = oldTab.length;
//            int newLen = oldLen * 2;
//            MyThreadLocal.MyThreadLocalMap.Entry[] newTab = new MyThreadLocal.MyThreadLocalMap.Entry[newLen];
//            int count = 0;
//
//            for (int j = 0; j < oldLen; ++j) {
//                //获取旧的值
//                MyThreadLocal.MyThreadLocalMap.Entry e = oldTab[j];
//                if (e != null) {
//                    MyThreadLocal<?> k = e.get();
//                    //如果key的引用一句不存在，触发gc
//                    if (k == null) {
//                        e.value = null; // Help the GC
//                    } else {
//                        //否则计算hash值，保存至
//                        int h = k.threadLocalHashCode & (newLen - 1);
//                        while (newTab[h] != null)
//                            h = nextIndex(h, newLen);
//                        newTab[h] = e;
//                        count++;
//                    }
//                }
//            }
//
//            //调整新的容量
//            setThreshold(newLen);
//            //初始化新的容量大小
//            size = count;
//            //初始化新的表大小
//            table = newTab;
//        }
//
//        /**
//         * Expunge all stale entries in the table.
//         */
//        private void expungeStaleEntries() {
//            MyThreadLocal.MyThreadLocalMap.Entry[] tab = table;
//            int len = tab.length;
//            //遍历全表，删除不必要的引用，触发gc
//            for (int j = 0; j < len; j++) {
//                MyThreadLocal.MyThreadLocalMap.Entry e = tab[j];
//                if (e != null && e.get() == null) {
//                    expungeStaleEntry(j);
//                }
//            }
//        }
//    }
//}
