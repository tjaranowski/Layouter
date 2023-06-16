package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;


public class Layouter {

    @NonNull
    private final ViewProvider viewProvider;

    Layouter() {
        viewProvider = new ViewProviderByReflection();
    }

    @MainThread
    public View parseElementIntoView(@NonNull ViewHierarchyElement element,
                                     @NonNull Iterable<ViewHierarchyElementReconstructor> reconstructors,
                                     @NonNull ViewGroup container,
                                     @NonNull Context context) {

        final View elementView = createElementViewForElement(element, context);

        for (ViewHierarchyElementReconstructor reconstructor : reconstructors) {
            reconstructor.reconstruct(element, elementView, context);
        }

        if (elementView instanceof ViewGroup) {
            for (ViewHierarchyElement child : element.getChildren()) {
                parseElementIntoView(child, reconstructors, (ViewGroup) elementView, context);
            }
        }

        container.addView(elementView);
        return elementView;
    }

    @NonNull
    private View createElementViewForElement(@NonNull ViewHierarchyElement viewHierarchyElement,
                                             @NonNull Context context) {

        return viewProvider.provideViewByName(
                viewHierarchyElement.getFullyQualifiedName(),
                context,
                null);
    }
}
