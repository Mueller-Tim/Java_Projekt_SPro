package ch.zhaw.spro.observerinterfaces;

/**
 * Most basic interface for being an observer
 */
public interface IsObserver {
    /**
     * This method is always called when an observed object changes
     */
    void update();
}
