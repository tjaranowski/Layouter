package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;


public interface ViewHierarchyElementReconstructor {

    /**
     * FIXME: Work In Progress
     *
     * @param element     element for which to reconstruct parameters
     * @param elementView view to which to apply paramenters
     * @param context context under which to reconstruct views
     */
    @MainThread
    void reconstruct(@NonNull ViewHierarchyElement element,
                     @NonNull View elementView,
                     @NonNull Context context);
}
