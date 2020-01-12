package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * FIXME: Work In Progress (API)
 */
public class Layouter {


    private static class ContextAdapter extends ContextWrapper {

        public ContextAdapter(Context base) {
            super(base);
        }



        @Override
        public Theme getTheme() {
            final Theme theme = super.getTheme();
            final Object themeProxy = Proxy.newProxyInstance(
                    getClassLoader(),
                    Theme.class.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                            return method.invoke(theme, objects);
                        }
                    }
            );
            return (Theme) themeProxy;
        }
    }
    @NonNull
    private final ViewProvider viewProvider;

    Layouter() {
        viewProvider = new ViewProviderByReflection();
    }

    @MainThread
    public void parseElementIntoView(@NonNull ViewHierarchyElement element,
                                     @NonNull Iterable<ViewHierarchyElementReconstructor> reconstructors,
                                     @NonNull ViewGroup container,
                                     @NonNull Context context) {

        final Context adaptedContext = new ContextAdapter(context);

        final View elementView = createElementViewForElement(element, adaptedContext);

        for (ViewHierarchyElementReconstructor reconstructor : reconstructors) {
            reconstructor.reconstruct(element, elementView, adaptedContext);
        }

        if (elementView instanceof ViewGroup) {
            for (ViewHierarchyElement child : element.getChildren()) {
                parseElementIntoView(child, reconstructors, (ViewGroup) elementView, adaptedContext);
            }
        }

        container.addView(elementView);
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
