package pl.nosystems.android.layouter.glide;

import static java.util.Objects.requireNonNull;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;


public class GlideLayouterRequest {
    @NonNull
    private final Callable<ViewHierarchyElement> dataProvider;

    @NonNull
    private final ViewGroup target;

    @Nullable
    private final Consumer<View> callback;

    GlideLayouterRequest(@NonNull Callable<ViewHierarchyElement> dataProvider,
                         @NonNull ViewGroup target,
                         @Nullable Consumer<View> callback) {
        this.dataProvider = requireNonNull(dataProvider);
        this.target = requireNonNull(target);
        this.callback = callback;
    }

    @NonNull
    Callable<ViewHierarchyElement> getDataProvider() {
        return dataProvider;
    }

    @NonNull
    ViewGroup getTarget() {
        return target;
    }

    @Nullable
    public Consumer<View> getDoneCallback() {
        return callback;
    }
}
