package pl.nosystems.android.layouter.core;

import android.view.View;

import androidx.annotation.NonNull;


public interface ViewHierarchyElementReconstructor {

    /**
     * FIXME: Work In Progress
     *
     * @param element     element for which to reconstruct parameters
     * @param elementView view to which to apply paramenters
     */
    void reconstruct(@NonNull ViewHierarchyElement element, @NonNull View elementView);
}
