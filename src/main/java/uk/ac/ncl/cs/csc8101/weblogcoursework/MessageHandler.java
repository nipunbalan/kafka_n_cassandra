/*
Copyright 2015 Red Hat, Inc. and/or its affiliates.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package uk.ac.ncl.cs.csc8101.weblogcoursework;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Integration point for application specific processing logic.
 *
 * @author Jonathan Halliday (jonathan.halliday@redhat.com)
 * @since 2015-01
 */
public class MessageHandler {

    private final static Cluster cluster;
    private final static Session session;
    private final static String keySpaceName = "csc8101";
    private final static String host = "127.0.0.1";
    private final static LogEntryParser logEntryParser = new LogEntryParser();
    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    private static final AtomicReference<SiteSession> expiredSession = new AtomicReference<>(null);
    private static HashMap<String, SiteSession> siteSessions = new LinkedHashMap<String, SiteSession>() {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            SiteSession siteSession = (SiteSession) eldest.getValue();
            boolean shouldExpire = siteSession.isExpired();
            if (shouldExpire) {
                expiredSession.set(siteSession);
            }
            return siteSession.isExpired();
        }
    };


    static {

        cluster = new Cluster.Builder()
                .addContactPoint(host)
                .build();

        final Session bootstrapSession = cluster.connect();
        final String keySpaceName = "csc8101";
        bootstrapSession.execute("CREATE KEYSPACE IF NOT EXISTS " + keySpaceName + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        bootstrapSession.close();

        session = cluster.connect(keySpaceName);

//        session.execute("CREATE TABLE IF NOT EXISTS SESSION_INFO (" +
//                " CLIENT_ID TEXT, " +
//                " START_TIME TIMESTAMP, " +
//                " END_TIME TIMESTAMP, " +
//                " DISTINCT_URLS BIGINT, " +
//                " HITS BIGINT, " +
//                " PRIMARY KEY(CLIENT_ID,START_TIME) " +
//                " )");
//
//        session.execute("CREATE TABLE IF NOT EXISTS  CLIENT_ACCESS ( " +
//                "CLIENT_ID TEXT," +
//                "URL_HLL BLOB," +
//                "PRIMARY KEY(CLIENT_ID)" +
//                ")");

        //TODO Add Table Creation Code
    }

    public static void close() {
        session.close();
        saveAllSessions();
        cluster.close();
    }

    public void flush() {
    }

    public void handle(String message) {

        System.out.println("Consumed: " + message);
//        String[] logEntry = logEntryParser.parseRecord(message);
//        if (logEntry == null) {
//            logger.warn("Invalid Log Entry, Skipping Record");
//            return;
//        }
//        if (siteSessions.containsKey(logEntry[0])) {
//            updateSession(logEntry);
//        } else {
//            newSession(logEntry);
//        }
//
//        saveSessionInfo(expiredSession.get());
//        expiredSession.set(null);


       SiteSession siteSession = new SiteSession("a", 100, "testURL");
        saveSessionInfo(siteSession);

    }

    public static boolean saveSessionInfo(SiteSession siteSession) {
        DateTime startTime = new DateTime(siteSession.getFirstHitMillis() * 1000L);
        DateTime endTime = new DateTime(siteSession.getLastHitMillis() * 1000L);
        Insert insert = QueryBuilder.insertInto(keySpaceName, "SESSION_INFO")
                .value("CLIENT_ID", siteSession.getId()).value("START_TIME", startTime)
                .value("END_TIME", endTime)
                .value("DISTINCT_URLS", siteSession.getHyperLogLog().cardinality())
                .value("HITS", siteSession.getHitCount());
        ResultSet results = session.execute(insert);
        return true;
    }

    // Update a session
    public boolean updateSession(String[] logEntry) {
        Long timeInMillis;
        try {
            timeInMillis = logEntryParser.getTimeinMillis(logEntry[1]);
        } catch (Exception e) {
            logger.warn("Date Parsing Error Skipping Record");
            return false;
        }
        siteSessions.get(logEntry[0]).update(timeInMillis, logEntry[2]);
        return true;
    }

    // Creating a newSession
    public static boolean newSession(String[] logEntry) {
        Long timeInMillis;
        try {
            timeInMillis = logEntryParser.getTimeinMillis(logEntry[1]);
        } catch (Exception e) {
            logger.warn("Date Parsing Error Skipping Record");
            return false;
        }
        SiteSession session = new SiteSession(logEntry[0], timeInMillis, logEntry[2]);
        return true;
    }

    // Save all sessions to table. Typically called at the end of processing
    public static boolean saveAllSessions() {
        Iterator<String> siteSessionIterator = siteSessions.keySet().iterator();

        while (siteSessionIterator.hasNext()) {
            saveSessionInfo(siteSessions.get(siteSessionIterator.next()));
        }
        return true;
    }



}
