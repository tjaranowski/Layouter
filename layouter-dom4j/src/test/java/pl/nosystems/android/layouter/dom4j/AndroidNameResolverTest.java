package pl.nosystems.android.layouter.dom4j;

import android.view.ViewStub;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class AndroidNameResolverTest {
    private AndroidNameResolver androidNameResolver;

    @Before
    public void setUp() {
        androidNameResolver = new AndroidNameResolver();
    }

    @Test
    public void whenTextViewRequestedShouldReturnCorrectFQN() {

        // When
        final String textViewFQN = androidNameResolver.resolveName("TextView");


        // Then verify
        assertEquals(TextView.class.getName(), textViewFQN);
    }

    @Test
    public void whenViewStubRequestedShouldReturnCorrectFQN() {

        // When
        final String viewStubFQN = androidNameResolver.resolveName("ViewStub");


        // Then verify
        assertEquals(ViewStub.class.getName(), viewStubFQN);
    }

    @Test(expected = RuntimeException.class)
    public void whenNonExistentViewRequestedShouldThrow() {

        // When
        androidNameResolver.resolveName("NonExistentView");

        // Then should throw
    }
}