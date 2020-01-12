package pl.nosystems.android.layouter.dom4j;

import org.junit.Test;

import static org.junit.Assert.*;


public class LayouterDom4JBuilderTest {

    @Test
    public void whenBuildWithoutModificationsRequestedShouldReturnDefaultLayouterDom4J() {

        // Given
        final LayouterDom4JBuilder instance = LayouterDom4JBuilder.instance();


        // When
        final LayouterDom4J build = instance.build();


        // Then verify
        assertNotNull(build);
    }

}