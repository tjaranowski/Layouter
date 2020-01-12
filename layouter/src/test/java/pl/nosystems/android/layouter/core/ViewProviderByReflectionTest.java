package pl.nosystems.android.layouter.core;

import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
public class ViewProviderByReflectionTest {
    private static final String TEXT_VIEW_FQN = TextView.class.getName();
    private static final String NON_EXISTENT_VIEW_FQN = "com.example.views.NonExistentView";

    @Test
    public void whenRequestedTextViewShouldReturnIt() {

        // Given
        ViewProviderByReflection viewProviderByReflection = new ViewProviderByReflection();


        // When
        final View view = viewProviderByReflection.provideViewByName(
                TEXT_VIEW_FQN,
                getApplicationContext(),
                null);


        // Then verify
        assertNotNull(view);
        assertEquals(view.getClass(), TextView.class);
    }

    @Test(expected = RuntimeException.class)
    public void whenRequestedViewDoesntExistShouldThrow() {

        // Given
        ViewProviderByReflection viewProviderByReflection = new ViewProviderByReflection();


        // When
        final View view = viewProviderByReflection.provideViewByName(
                NON_EXISTENT_VIEW_FQN,
                getApplicationContext(),
                null);


        // Then should throw
    }
}