package com.meilisearch.sdk.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class URLBuilderTest {

    private final URLBuilder classToTest = new URLBuilder();

    @BeforeEach
    void beforeEach() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void addSubroute() {
        classToTest.addSubroute("route");

        assertThat(classToTest.getRoutes().toString(), is(equalTo("/route")));
    }

    @Test
    void addSubrouteWithMultipleRoutes() {
        classToTest
                .addSubroute("route1")
                .addSubroute("route2")
                .addSubroute("route3")
                .addSubroute("route4");

        assertThat(classToTest.getRoutes().toString(), is(equalTo("/route1/route2/route3/route4")));
    }

    @Test
    void addParameterStringInt() {
        classToTest.addParameter("parameter1", 1);
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1")));

        classToTest.addParameter("parameter2", 2);
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1&parameter2=2")));

        classToTest.addParameter("parameter3", 3);
        assertThat(
                classToTest.getParams().toString(),
                is(equalTo("?parameter1=1&parameter2=2&parameter3=3")));
    }

    @Test
    void addParameterStringString() {
        classToTest.addParameter("parameter1", "1");
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1")));

        classToTest.addParameter("parameter2", "2");
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1&parameter2=2")));

        classToTest.addParameter("parameter3", "3");
        assertThat(
                classToTest.getParams().toString(),
                is(equalTo("?parameter1=1&parameter2=2&parameter3=3")));
    }

    @Test
    void addParameterStringStringArray() {
        classToTest.addParameter("parameter1", new String[] {"1", "a"});
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1,a")));

        classToTest.addParameter("parameter2", new String[] {"2", "b"});
        assertThat(
                classToTest.getParams().toString(), is(equalTo("?parameter1=1,a&parameter2=2,b")));

        classToTest.addParameter("parameter3", new String[] {"3", "c"});
        assertThat(
                classToTest.getParams().toString(),
                is(equalTo("?parameter1=1,a&parameter2=2,b&parameter3=3,c")));
    }

    @Test
    void addParameterStringIntArray() {
        classToTest.addParameter("parameter1", new int[] {1, 2});
        assertThat(classToTest.getParams().toString(), is(equalTo("?parameter1=1,2")));

        classToTest.addParameter("parameter2", new int[] {3, 4});
        assertThat(
                classToTest.getParams().toString(), is(equalTo("?parameter1=1,2&parameter2=3,4")));

        classToTest.addParameter("parameter3", new int[] {5, 6});
        assertThat(
                classToTest.getParams().toString(),
                is(equalTo("?parameter1=1,2&parameter2=3,4&parameter3=5,6")));
    }

    @Test
    void addParameterStringDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = format.parse("2042-01-30T10:30:00-05:00");

        classToTest.addParameter("parameter1", date);
        String parameterDate1 = classToTest.getParams().substring(12);
        assertDoesNotThrow(() -> format.parse(parameterDate1));
        assertEquals(classToTest.getParams().toString(), "?parameter1=2042-01-30T15:30:00Z");

        classToTest.addParameter("parameter2", date);
        String parameterDate2 = classToTest.getParams().substring(44);
        assertDoesNotThrow(() -> format.parse(parameterDate2));
        assertEquals(
                classToTest.getParams().toString(),
                "?parameter1=2042-01-30T15:30:00Z&parameter2=2042-01-30T15:30:00Z");
    }

    @Test
    void getURL() {
        assertThat(classToTest.getURL(), is(equalTo("")));

        classToTest.addSubroute("routes");
        classToTest.addParameter("parameter", "value");

        assertThat(classToTest.getURL(), is(equalTo("/routes?parameter=value")));
    }
}
