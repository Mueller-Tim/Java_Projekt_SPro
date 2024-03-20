package ch.zhaw.spro.observerinterfaces;

/**
 * Most basic interface for observing an object
 */
public interface IsObservable {

    /**
     * Add an observer that listens for updates
     *
     * @param observer The observer that should be added.
     */
    void addListener(IsObserver observer);
}
