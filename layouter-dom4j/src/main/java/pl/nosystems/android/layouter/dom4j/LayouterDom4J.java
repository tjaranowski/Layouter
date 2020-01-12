package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class LayouterDom4J {
    @NonNull
    private final FullyQualifiedNameResolver fullyQualifiedNameResolver;

    LayouterDom4J() {
        this.fullyQualifiedNameResolver = new AndroidNameResolver();
    }


    @AnyThread
    @NonNull
    public ViewHierarchyElement createElementsFromDom4JDocument(@NonNull Document document) {
        final Element rootElement = document.getRootElement();

        if (rootElement == null) {
            throw new RuntimeException("Document is required to have root element but instance passed does not have one!");
        }

        return convertDom4JToInternal(null, rootElement);
    }


    @AnyThread
    @NonNull
    private ViewHierarchyElement convertDom4JToInternal(@Nullable final ViewHierarchyElement parent,
                                                        @NonNull Element element) {
        String name = element.getName();

        if (!isFullyQualifiedName(name)) {
            name = toFullyQualifiedName(name);
        }

        final List<ViewHierarchyElementAttribute> attributes = new ArrayList<>();
        for (final Attribute attribute : element.attributes()) {

            final String attributeName = attribute.getName();
            final String attributeNamespacePrefix = attribute.getNamespacePrefix();
            final String attributeValue = attribute.getValue();
            attributes.add(new ViewHierarchyElementAttribute() {
                @NonNull
                @Override
                public String getName() {
                    return attributeName;
                }

                @NonNull
                @Override
                public String getNamespacePrefix() {
                    return attributeNamespacePrefix;
                }

                @NonNull
                @Override
                public String getValue() {
                    return attributeValue;
                }
            });
        }

        final String finalName = name;
        final List<ViewHierarchyElement> children = new ArrayList<>();
        final ViewHierarchyElement viewHierarchyElement = new ViewHierarchyElementImpl(
                finalName,
                new OptionalImpl<>(parent),
                children,
                attributes);

        for (Element e : element.elements()) {
            children.add(convertDom4JToInternal(viewHierarchyElement, e));
        }
        return viewHierarchyElement;
    }

    @AnyThread
    @NonNull
    private String toFullyQualifiedName(@NonNull String name) {
        return fullyQualifiedNameResolver.resolveName(name);
    }

    private static boolean isFullyQualifiedName(@NonNull String name) {
        return name.contains(".");
    }
}
