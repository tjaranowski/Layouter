package pl.nosystems.android.layouter.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface ViewProvider {


    @NonNull
    View provideViewByName(@NonNull String name,
                           @NonNull Context context,
                           @Nullable AttributeSet attributeSet);
}
