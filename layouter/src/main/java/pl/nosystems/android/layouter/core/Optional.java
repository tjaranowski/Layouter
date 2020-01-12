package pl.nosystems.android.layouter.core;

import androidx.annotation.NonNull;

public interface Optional<T> {

    boolean isPresent();

    @NonNull
    <E extends Throwable> T getOrElseThrow(@NonNull Supplier<? extends E> exceptionSupplier) throws E;
}
