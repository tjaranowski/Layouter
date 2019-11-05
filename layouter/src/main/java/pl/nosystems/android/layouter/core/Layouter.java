package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * FIXME: Work In Progress (API)
 */
@SuppressWarnings("WeakerAccess")
public class Layouter {

    @MainThread
    public static void parseElementIntoView(@NonNull ViewHierarchyElement element,
                                            @NonNull Iterable<ViewHierarchyElementReconstructor> reconstructors,
                                            @NonNull ViewGroup container,
                                            @NonNull Context context) {

        final View elementView = createElementViewForElement(element, context);

        for (ViewHierarchyElementReconstructor reconstructor : reconstructors) {
            reconstructor.reconstruct(element, elementView);
        }

        if (elementView instanceof ViewGroup) {
            for (ViewHierarchyElement child : element.getChildren()) {
                parseElementIntoView(child, reconstructors, (ViewGroup) elementView, context);
            }
        }

        container.addView(elementView);
    }

    @NonNull
    private static View createElementViewForElement(@NonNull ViewHierarchyElement viewHierarchyElement,
                                                    @NonNull Context context) {
        String rootName = viewHierarchyElement.getFullyQualifiedName();
        return createInstanceForViewName(rootName, context, null);
    }

    @NonNull
    private static View createInstanceForViewName(@NonNull String name,
                                                  @NonNull Context context,
                                                  @Nullable AttributeSet attributeSet) {
        try {
            Class rootClass = Class.forName(name);
            Constructor[] constructors = rootClass.getConstructors();

            for (Constructor c : constructors) {
                if (c.getParameterCount() == 2 // FIXME.......
                        && c.getParameterTypes()[0] == Context.class
                        && c.getParameterTypes()[1] == AttributeSet.class) {
                    return (View) c.newInstance(context, attributeSet);
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }

}
