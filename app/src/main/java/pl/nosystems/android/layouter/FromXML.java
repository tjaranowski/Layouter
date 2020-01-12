package pl.nosystems.android.layouter;

import android.content.res.XmlResourceParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import pl.nosystems.android.layouter.core.Optional;
import pl.nosystems.android.layouter.core.Supplier;
import pl.nosystems.android.layouter.core.ViewHierarchyElement;
import pl.nosystems.android.layouter.core.ViewHierarchyElementAttribute;

import static java.util.Objects.requireNonNull;

public class FromXML {
    @NonNull
    private static final String ANDROID_ATTRIBUTE_NAMESPACE = "http://schemas.android.com/apk/res/android";


    private static class OptionalImpl<T> implements Optional<T> {
        @Nullable
        private final T value;

        private OptionalImpl(@Nullable T value) {
            this.value = value;
        }

        @NonNull
        static <T> Optional<T> ofNothing() {
            return new OptionalImpl<>(null);
        }

        static <T> Optional<T> ofValue(T t) {
            return new OptionalImpl<>(t);
        }

        @Override
        public boolean isPresent() {
            return value != null;
        }

        @NonNull
        @Override
        public <E extends Throwable> T getOrElseThrow(@NonNull Supplier<? extends E> exceptionSupplier) throws E {
            if(isPresent()) {
                return requireNonNull(value);
            }
            throw exceptionSupplier.get();
        }
    }

    private static class ViewHierarchyElementImpl implements ViewHierarchyElement {
        @NonNull
        private final String fullyQualifiedName;
        @NonNull
        private final Optional<ViewHierarchyElement> parentOptional;
        @NonNull
        private final List<ViewHierarchyElement> children;
        @NonNull
        private final List<ViewHierarchyElementAttribute> attributes;

        ViewHierarchyElementImpl(@NonNull String fullyQualifiedName,
                                 @NonNull Optional<ViewHierarchyElement> parentOptional,
                                 @NonNull List<ViewHierarchyElement> children,
                                 @NonNull List<ViewHierarchyElementAttribute> attributes) {
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

        @NonNull
        List<ViewHierarchyElement> getChildrenAsList() {
            return children;
        }

        @NonNull
        List<ViewHierarchyElementAttribute> getAttributesAsList() {
            return attributes;
        }
    }

    @NonNull
    private String resolveName(@NonNull String name) {
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

    ViewHierarchyElement parse(@NonNull XmlResourceParser xmlResourceParser) {
        ViewHierarchyElementImpl root = null;
        ViewHierarchyElementImpl currentElement = null;

        try {
            XmlPullParser parser = xmlResourceParser;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {

                    final String name = resolveName(parser.getName()); // Linear layout ?

                    if(currentElement == null && root == null) {
                        root = currentElement = new ViewHierarchyElementImpl(
                                name,
                                OptionalImpl.ofNothing(),
                                new ArrayList<>(),
                                new ArrayList<>()
                        );
                    } else if(currentElement == null) {
                        throw new RuntimeException("Internal error!");
                    } else {
                        ViewHierarchyElementImpl parent = currentElement;
                        currentElement = new ViewHierarchyElementImpl(
                                name,
                                OptionalImpl.ofValue(parent),
                                new ArrayList<>(),
                                new ArrayList<>()
                        );
                        parent.getChildrenAsList().add(currentElement);
                    }

                    final int attributeCount = parser.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        final String attributeName = parser.getAttributeName(i);
                        final String attributeValue = parser.getAttributeValue(i);

                        String attributeNamespace = parser.getAttributeNamespace(i);
                        if(attributeNamespace.equals(ANDROID_ATTRIBUTE_NAMESPACE)) {
                            attributeNamespace = "android";
                        }

                        String finalAttributeNamespace = attributeNamespace;
                        ViewHierarchyElementAttribute attribute = new ViewHierarchyElementAttribute() {
                            @NonNull
                            @Override
                            public String getName() {
                                return attributeName;
                            }

                            @NonNull
                            @Override
                            public String getNamespacePrefix() {
                                return finalAttributeNamespace;
                            }

                            @NonNull
                            @Override
                            public String getValue() {
                                return attributeValue;
                            }
                        };
                        currentElement.getAttributesAsList().add(attribute);
                    }



                } else if (eventType == XmlPullParser.END_TAG) {
                    final Optional<ViewHierarchyElement> parent = requireNonNull(currentElement).getParent();
                    if(parent.isPresent()) {
                        currentElement = (ViewHierarchyElementImpl) parent.getOrElseThrow(RuntimeException::new);
                    } else {
                        currentElement = null;
                    }

                }
                eventType = parser.next();
            }
            
            return root;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
