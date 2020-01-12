package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class ViewProviderByReflection implements ViewProvider {

    @NonNull
    @Override
    public View provideViewByName(@NonNull String name, @NonNull Context context, @Nullable AttributeSet attributeSet) {
        try {
            Class rootClass = Class.forName(name);
            Constructor[] constructors = rootClass.getConstructors();

            for (Constructor c : constructors) {
                if (c.getParameterTypes().length == 2 // FIXME.......
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
