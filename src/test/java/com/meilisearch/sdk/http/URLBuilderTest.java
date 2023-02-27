package com.meilisearch.sdk.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class URLBuilderTest {

    private final URLBuilder classToTest = new URLBuilder();

    @Test
    void addSubroute() {
        classToTest.addSubroute("route");

        assertEquals("/route", classToTest.getRoutes().toString());
    }

    @Test
    void addSubrouteWithMultipleRoutes() {
        classToTest
                .addSubroute("route1")
                .addSubroute("route2")
                .addSubroute("route3")
                .addSubroute("route4");

        assertEquals("/route1/route2/route3/route4", classToTest.getRoutes().toString());
    }

    @Test
    void addParameterStringInt() {
        classToTest.addParameter("parameter1", 1);
        assertEquals("?parameter1=1", classToTest.getParams().toString());

        classToTest.addParameter("parameter2", 2);
        assertEquals("?parameter1=1&parameter2=2", classToTest.getParams().toString());

        classToTest.addParameter("parameter3", 3);
        assertEquals("?parameter1=1&parameter2=2&parameter3=3", classToTest.getParams().toString());
    }

    @Test
    void addParameterStringString() {
        classToTest.addParameter("parameter1", "1");
        assertEquals("?parameter1=1", classToTest.getParams().toString());

        classToTest.addParameter("parameter2", "2");
        assertEquals("?parameter1=1&parameter2=2", classToTest.getParams().toString());

        classToTest.addParameter("parameter3", "3");
        assertEquals("?parameter1=1&parameter2=2&parameter3=3", classToTest.getParams().toString());
    }

    @Test
    void addParameterStringArray() {
        classToTest.addParameter("parameter1", new String[] {"1", "a"});
        assertEquals("?parameter1=1,a", classToTest.getParams().toString());

        classToTest.addParameter("parameter2", new String[] {"2", "b"});
        assertEquals("?parameter1=1,a&parameter2=2,b", classToTest.getParams().toString());

        classToTest.addParameter("parameter3", new String[] {"3", "c"});
        assertEquals(
                "?parameter1=1,a&parameter2=2,b&parameter3=3,c",
                classToTest.getParams().toString());
    }

    @Test
    void getURL() {
        assertEquals("", classToTest.getURL());

        classToTest.addSubroute("routes");
        classToTest.addParameter("parameter", "value");

        assertEquals("/routes?parameter=value", classToTest.getURL());
    }
}
