package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.Optional;
import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;

import static java.util.Objects.requireNonNull;

final class ViewHierarchyElementImpl implements ViewHierarchyElement {
    @NonNull
    private final String fullyQualifiedName;
    @NonNull
    private final Optional<ViewHierarchyElement> parentOptional;
    @NonNull
    private final Iterable<ViewHierarchyElement> children;
    @NonNull
    private final Iterable<ViewHierarchyElementAttribute> attributes;

    ViewHierarchyElementImpl(@NonNull String fullyQualifiedName,
                                    @NonNull Optional<ViewHierarchyElement> parentOptional,
                                    @NonNull Iterable<ViewHierarchyElement> children,
                                    @NonNull Iterable<ViewHierarchyElementAttribute> attributes) {
        this.fullyQualifiedName = requireNonNull(fullyQualifiedName);
        this.parentOptional = requireNonNull(parentOptional);
        this.children = requireNonNull(children);
        this.attributes = requireNonNull(attributes);
    }

    @NonNull
    @Override
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    @NonNull
    @Override
    public Optional<ViewHierarchyElement> getParent() {
        return parentOptional;
    }

    @NonNull
    @Override
    public Iterable<ViewHierarchyElement> getChildren() {
        return children;
    }

    @NonNull
    @Override
    public Iterable<ViewHierarchyElementAttribute> getAttributes() {
        return attributes;
    }
}
