package pl.nosystems.android.layouter.dom4j;

import android.widget.TextView;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class LayouterDom4JTest {
    private Document document;

    private LayouterDom4J layouterDom4J;

    @Before
    public void setUp() {
        document = mock(Document.class);

        layouterDom4J = new LayouterDom4J();
    }

    @Test(expected = RuntimeException.class)
    public void whenDocumentHasNoRootElementThenShouldThrow() {

        // Given
        doReturn(null).when(document).getRootElement();


        // When
        layouterDom4J.createElementsFromDom4JDocument(document);


        // Then should throw
    }

    @Test
    public void givenTextViewOnlyWithoutAttributedShouldReturnCorrectViewHierarchyElement() {

        // Given
        Element element = mock(Element.class);
        doReturn(TextView.class.getName()).when(element).getName();
        doReturn(emptyList()).when(element).attributes();

        doReturn(element).when(document).getRootElement();


        // When
        final ViewHierarchyElement elementsFromDom4JDocument = layouterDom4J.createElementsFromDom4JDocument(document);


        // Then verify
        assertNotNull(elementsFromDom4JDocument);
        assertNotNull(elementsFromDom4JDocument.getAttributes());
        assertNotNull(elementsFromDom4JDocument.getChildren());

        assertFalse(elementsFromDom4JDocument.getAttributes().iterator().hasNext());
        assertFalse(elementsFromDom4JDocument.getChildren().iterator().hasNext());

        assertEquals(TextView.class.getName(), elementsFromDom4JDocument.getFullyQualifiedName());
    }

}