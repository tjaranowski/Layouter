package pl.nosystems.android.layouter.resources;

import android.content.res.XmlResourceParser;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.xmlpull.v1.XmlPullParser;

import pl.nosystems.android.layouter.core.ViewHierarchyElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class FromXMLTest {
    private static final String LINEAR_LAYOUT_FQN = LinearLayout.class.getName();
    private static final String SWITCH_FQN = Switch.class.getName();

    private FromXML fromXML;

    @Before
    public void setUp() {
        fromXML = new FromXML();
    }

    @Test
    public void foo() throws Exception {

        // Given
        XmlResourceParser xmlResourceParser = mock(XmlResourceParser.class);

        doReturn(
                XmlPullParser.START_DOCUMENT,
                XmlPullParser.START_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_DOCUMENT
        ).when(xmlResourceParser).getEventType();

        doReturn(
                XmlPullParser.START_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_DOCUMENT
        ).when(xmlResourceParser).next();

        doReturn(0).when(xmlResourceParser).getAttributeCount();
        doReturn(
                "LinearLayout",
                "LinearLayout"
        ).when(xmlResourceParser).getName();


        // When
        final ViewHierarchyElement parse = fromXML.parse(xmlResourceParser);


        // Then verify
        assertNotNull(parse);
        assertEquals(LINEAR_LAYOUT_FQN, parse.getFullyQualifiedName());
        assertFalse(parse.getChildren().iterator().hasNext());
        assertFalse(parse.getAttributes().iterator().hasNext());
    }

    @Test
    public void bar() throws Exception {

        // Given
        XmlResourceParser xmlResourceParser = mock(XmlResourceParser.class);

        doReturn(
                XmlPullParser.START_DOCUMENT,
                XmlPullParser.START_TAG,
                XmlPullParser.START_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_DOCUMENT
        ).when(xmlResourceParser).getEventType();

        doReturn(
                XmlPullParser.START_TAG,
                XmlPullParser.START_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_TAG,
                XmlPullParser.END_DOCUMENT
        ).when(xmlResourceParser).next();

        doReturn(0).when(xmlResourceParser).getAttributeCount();
        doReturn(
                "LinearLayout",
                "Switch",
                "Switch",
                "LinearLayout"
        ).when(xmlResourceParser).getName();


        // When
        final ViewHierarchyElement parse = fromXML.parse(xmlResourceParser);


        // Then verify
        assertNotNull(parse);
        assertEquals(LINEAR_LAYOUT_FQN, parse.getFullyQualifiedName());
        assertTrue(parse.getChildren().iterator().hasNext());
        assertFalse(parse.getAttributes().iterator().hasNext());

        final ViewHierarchyElement child = parse.getChildren().iterator().next();
        assertNotNull(child);
        assertEquals(SWITCH_FQN, child.getFullyQualifiedName());
        assertFalse(child.getChildren().iterator().hasNext());
        assertFalse(child.getAttributes().iterator().hasNext());
    }

}