package me.collectmind.atomics;

/**
 * 模拟实现CAS指令
 * 只有期望值和实际值相等时，才会更新
 * @author monica
 * @date 2020/11/21
 */
public class SimulatedCAS {

    int count;

    synchronized int cas(int expect, int newValue){
        int curValue = count;
        if (curValue == expect) {
            count = newValue;
        }
        return curValue;
    }
}
