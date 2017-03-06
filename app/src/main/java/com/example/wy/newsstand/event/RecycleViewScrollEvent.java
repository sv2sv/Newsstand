package com.example.wy.newsstand.event;

/**
 * Created by wy on 17-3-6.
 */

public class RecycleViewScrollEvent {
    private boolean isUp;

    public RecycleViewScrollEvent(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }
}
