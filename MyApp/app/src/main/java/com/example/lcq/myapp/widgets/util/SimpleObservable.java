package com.example.lcq.myapp.widgets.util;

import java.util.Observable;

/**
 * Created by jingtuo on 2017/9/28.
 */

public class SimpleObservable extends Observable {

    @Override
    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }
}
