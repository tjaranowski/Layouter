package pl.nosystems.android.layouter.core;

import androidx.annotation.NonNull;


/**
 * Represents element of parsed view hierarchy.
 */
public interface ViewHierarchyElement {

    @NonNull
    String getFullyQualifiedName();

    /**
     * FIXME: Work In Progress
     *
     * @return iterable of children. If none, iterable will be empty.
     */
    @NonNull
    Iterable<ViewHierarchyElement> getChildren();

    /**
     * FIXME: Work In Progress  (API)
     *
     * @return
     */
    @NonNull
    Iterable<ViewHierarchyElementAttribute> getAttributes();
}
