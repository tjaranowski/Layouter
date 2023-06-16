package pl.nosystems.android.layouter.core;

import androidx.annotation.NonNull;


/**
 * Represents element of parsed view hierarchy.
 */
public interface ViewHierarchyElement {

    @NonNull
    String getFullyQualifiedName();

    @NonNull
    Optional<ViewHierarchyElement> getParent();

    @NonNull
    Iterable<ViewHierarchyElement> getChildren();

    @NonNull
    Iterable<ViewHierarchyElementAttribute> getAttributes();
}
