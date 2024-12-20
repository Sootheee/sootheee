package com.soothee.common.exception;

public class MissMethodMatchForErrorMsg extends IllegalArgumentException {
    public MissMethodMatchForErrorMsg() {
        super();
    }

    public MissMethodMatchForErrorMsg(MyErrorMsg msg) {
        super(msg.toString());
    }

    public MissMethodMatchForErrorMsg(String msg) {
        super(msg);
    }
}
