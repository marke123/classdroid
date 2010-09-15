
package com.mclear.classdroid.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Parsers {
    public static boolean parseAndCheckLoginWP(InputStream stream) {
        XmlPullParser parser = getParser(stream);
        boolean isOK = true;
        try {
            int eventType = parser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    if(parser.getName().equals("fault")){
                        isOK = false;
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOK;
    }

    private static XmlPullParser getParser(InputStream stream) {
        XmlPullParser parser = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(stream, HTTP.UTF_8);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return parser;
    }
}
