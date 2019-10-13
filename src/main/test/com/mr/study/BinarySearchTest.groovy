package com.mr.study

import com.mr.study.spock.BinarySearch
import com.mr.study.spock.Sum
import spock.lang.Specification
import spock.lang.Unroll

class BinarySearchTest extends Specification {
    def "testSearch"() {
        expect:
        BinarySearch.search(arr as int[], key) == result

        where:
        arr       | key | result
        []        | 1   | -1
        [1]       | 1   | 0
        [1]       | 2   | -1
        [3]       | 2   | -1
        [1, 2, 9] | 2   | 1
        [1, 2, 9] | 9   | 2
        [1, 2, 9] | 3   | -1
    }

    /**
     * testSearch的测试用例都写在where子句里。有时，里面的某个测试用例失败了，却难以查到是哪个失败了。
     * 这时候，可以使用Unroll注解，该注解会将where子句的每个测试用例转化为一个 @Test 独立测试方法来执行，
     * 这样就很容易找到错误的用例。 方法名还可以更可读些
     * @return
     */
    @Unroll
    def "testSearch(#key in #arr index=#result)"() {
        expect:
        BinarySearch.search(arr as int[], key) == result

        where:
        arr       | key | result
        []        | 1   | -1
        [1, 2, 9] | 9   | 2
        [1, 2, 9] | 3   | 0
    }

    def "get sum"() {
        expect:
        Sum.sum(1, 1) == 2
    }

    // Where Blocks 简单大小比较函数测试
    def "get max num"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 2 | 2
        1 | 2 | 2
        2 | 3 | 3
    }

    def "get min num"() {
        expect:
        Math.min(a, b) == c

        where:
        a | b | c
        1 | 2 | 1
        1 | 2 | 1
        2 | 3 | 2
    }

    // 上述例子实际会跑三次测试，相当于在for循环中执行三次测试，如果在方法前声明@Unroll，则会当成三个方法运行。
    @Unroll
    def "@Unroll test"() {
        expect:
        Math.min(a, b) == c

        where:
        a | b | c
        1 | 2 | 1
        1 | 2 | 1
        2 | 3 | 2
    }

    // where block另外两种数据定义方法
    def "where block data init method"() {
        expect:
        Math.max(a, b) == c

        where:
        a | _
        3 | _
        7 | _
        0 | _

        b << [5, 0, 0]

        c = a > b ? a : b
    }


    // When and Then Blocks
    def "When and Then Blocks"() {
        setup:
        def stack = new Stack();
        def em = "push me";

        when:
        stack.push(em);

        then:
        !stack.empty();
        stack.size() == 1;
        stack.peek() == em;
    }


    // mock应用
//    Publisher publisher = new Publisher()
//    Subscriber subscriber = Mock()
//    Subscriber subscriber2 = Mock()

    def setup() {
        publisher.subscribers.add(subscriber)
        publisher.subscribers.add(subscriber2)
    }

    def "should send messages to all subscribers"() {
        when:
        publisher.send("hello")

        then:
        1 * subscriber.receive("hello")
        1 * subscriber2.receive("hello")
    }


}
