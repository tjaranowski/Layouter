package pl.nosystems.android.layouter.core;

import androidx.annotation.NonNull;


public interface ViewHierarchyElementAttribute {

    /**
     * @return attribute name without namespace prefix
     */
    @NonNull
    String getName();

    /**
     * @return namespace prefix for attribute
     */
    @NonNull
    String getNamespacePrefix();

    /**
     * @return value of attribute
     */
    @NonNull
    String getValue();
}
