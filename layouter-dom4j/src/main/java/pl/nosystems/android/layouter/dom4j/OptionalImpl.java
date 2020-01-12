package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.Optional;
import pl.nosystems.android.layouter.core.Supplier;

import static java.util.Objects.requireNonNull;


final class OptionalImpl<T> implements Optional<T> {
    private final T t;

    OptionalImpl(T t) {
        this.t = t;
    }

    @Override
    public boolean isPresent() {
        return t != null;
    }

    @NonNull
    @Override
    public <E extends Throwable> T getOrElseThrow(@NonNull Supplier<? extends E> exceptionSupplier) throws E {
        if (!isPresent()) {
            throw exceptionSupplier.get();
        }
        return requireNonNull(t);
    }
}
