package pl.nosystems.android.layouter.glide;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;

import static java.util.Objects.requireNonNull;


public abstract class GlideLayouterRequest {
    @NonNull
    private final Callable<ViewHierarchyElement> dataProvider;

    @NonNull
    private final ViewGroup target;

    GlideLayouterRequest(@NonNull Callable<ViewHierarchyElement> dataProvider,
                         @NonNull ViewGroup target) {
        this.dataProvider = requireNonNull(dataProvider);
        this.target = requireNonNull(target);
    }

    @NonNull
    Callable<ViewHierarchyElement> getDataProvider() {
        return dataProvider;
    }

    @NonNull
    ViewGroup getTarget() {
        return target;
    }
}
