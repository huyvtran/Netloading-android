package com.ketnoivantai.customers.common;

/**
 * Created by Dandoh on 11/6/15.
 */
public class MutableBoolean {
    private boolean isTrue;

    public MutableBoolean(boolean b) {
        this.isTrue = b;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setValue(boolean isTrue) {
        this.isTrue = isTrue;
    }
}
