package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;

import static java.util.Objects.requireNonNull;


final class ViewHierarchyElementAttributeImpl implements ViewHierarchyElementAttribute {
    @NonNull
    private final String name;
    @NonNull
    private final String namespacePrefix;
    @NonNull
    private final String value;

    ViewHierarchyElementAttributeImpl(@NonNull String name,
                                              @NonNull String namespacePrefix,
                                              @NonNull String value) {
        this.name = requireNonNull(name);
        this.namespacePrefix = requireNonNull(namespacePrefix);
        this.value = requireNonNull(value);
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    @NonNull
    @Override
    public String getValue() {
        return value;
    }
}