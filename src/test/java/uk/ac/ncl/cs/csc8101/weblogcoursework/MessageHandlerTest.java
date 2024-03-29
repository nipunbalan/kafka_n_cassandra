package uk.ac.ncl.cs.csc8101.weblogcoursework;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by nipun on 2/12/2015.
 */
public class MessageHandlerTest {

    @Test
    public void parseRecord() {
        LogEntryParser msgHandler = new LogEntryParser();
        String inputRecord1 = "4444 [01/May/1998:02:58:10 +0000] \"GET /english/member/body.html HTTP/1.0\" 200 5097";
        String inputRecord2 = "129 [01/May/1998:02:58:10 +0000] \"GET /english/playing/images/play_mascot_header.gif HTTP/1.0\" 200 6555";

        String[] outputRecord1 = new String[3];
        String[] outputRecord2 = new String[3];
        outputRecord1 = msgHandler.parseRecord(inputRecord1);
        outputRecord2 = msgHandler.parseRecord(inputRecord2);
        for (int i = 0; i <= 2; i++) {
            if (outputRecord1 != null && outputRecord2 != null && outputRecord1[i] != null && outputRecord2[i] != null) {
                System.out.println(outputRecord1[i]);
                System.out.println(outputRecord2[i]);
            }
        }
        DateTimeFormatter df = DateTimeFormat.forPattern("dd/MMM/yyyy:hh:mm:ss Z");
        DateTime dt = df.withOffsetParsed().parseDateTime(outputRecord1[1]);
        System.out.print(dt.getMillis());
        assertEquals(3, outputRecord1.length);
        assertEquals("4444", outputRecord1[0]);
        assertEquals("01/May/1998:02:58:10 +0000", outputRecord1[1]);
        assertEquals("/english/member/body.html", outputRecord1[2]);

        assertEquals(3, outputRecord2.length);
        assertEquals("129", outputRecord2[0]);
        assertEquals("01/May/1998:02:58:10 +0000", outputRecord2[1]);
        assertEquals("/english/playing/images/play_mascot_header.gif", outputRecord2[2]);


    }


    @Test
    @Ignore
    public void testHandle(){

        MessageHandler msgHandler=new MessageHandler();
        String inputRecord1 = "4444 [01/May/1998:02:58:10 +0000] \"GET /english/member/body.html HTTP/1.0\" 200 5097";
        String inputRecord2 = "129 [01/May/1998:02:58:10 +0000] \"GET /english/playing/images/play_mascot_header.gif HTTP/1.0\" 200 6555";
        msgHandler.handle(inputRecord1);
        msgHandler.handle(inputRecord2);
        msgHandler.close();


    }

    @Test
    @Ignore
    public void testSession()
    {
//        final AtomicReference<SiteSession> expiredSession = new AtomicReference<>(null);
//        HashMap<String,SiteSession> sessions = new LinkedHashMap<String,SiteSession>() {
//            protected boolean removeEldestEntry(Map.Entry eldest) {
//                SiteSession siteSession = (SiteSession)eldest.getValue();
//                boolean shouldExpire = siteSession.isExpired();
//                if(shouldExpire) {
//                    expiredSession.set(siteSession);
//                }
//                return siteSession.isExpired();
//            }
//
//
//        };
//        SiteSession session = new SiteSession("a", 100, "testURL");
//        sessions.put("a", session);
//
//        session = new SiteSession("b", 100, "testURL2");
//        sessions.put("b", session);
//
//        session = new SiteSession("a", 101+SiteSession.MAX_IDLE_MS, "testURL");
//
//
//        sessions.put("a", session);
//
//        assertEquals(1, sessions.size());
//        assertNull(expiredSession.get());
//        sessions.put("b", new SiteSession("b", 101 + SiteSession.MAX_IDLE_MS, "testURL"));
//        assertEquals(1, sessions.size());
//        assertEquals(session, expiredSession.get());
    }

}
