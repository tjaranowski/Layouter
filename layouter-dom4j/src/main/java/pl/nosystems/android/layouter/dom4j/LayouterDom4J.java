package pl.nosystems.android.layouter.dom4j;

import androidx.annotation.NonNull;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;


public class LayouterDom4J {

    @NonNull
    public static ViewHierarchyElement createElementsFromDom4JDocument(@NonNull Document document) {
        final Element rootElement = document.getRootElement();

        return convertDom4JToInternal(rootElement);
    }


    @NonNull
    private static ViewHierarchyElement convertDom4JToInternal(@NonNull Element element) {
        String name = element.getName();

        if (!isFullyQualifiedName(name)) {
            name = toFullyQualifiedName(name);
        }

        final List<ViewHierarchyElementAttribute> attributes = new ArrayList<>();
        for(final Attribute attribute : element.attributes()) {

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
        for(Element e : element.elements()) {
            children.add(convertDom4JToInternal(e));
        }
        return new ViewHierarchyElement() {
            @NonNull
            @Override
            public String getFullyQualifiedName() {
                return finalName;
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
        };
    }

    @NonNull
    private static String toFullyQualifiedName(@NonNull String name) {
        try {
            String tmpName = "android.widget." + name;
            Class.forName(tmpName);
            return tmpName;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            String tmpName = "android.view." + name;
            Class.forName(tmpName);
            return tmpName;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isFullyQualifiedName(@NonNull String name) {
        return name.contains(".");
    }

}
