package ian_ellis.flickrtask.observables;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Ian on 17/05/2015.
 */
public class BooleanBus {

    private final Subject<Boolean, Boolean> mBus = new SerializedSubject<>(PublishSubject.create());

    public void push(Boolean o) {
        mBus.onNext(o);
    }

    public Observable<Boolean> toObserverable() {
        return mBus;
    }
}
