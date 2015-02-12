package uk.ac.ncl.cs.csc8101.weblogcoursework;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nipun on 2/12/2015.
 */
public class LogEntryParser {
    private final Logger logger = LoggerFactory.getLogger(LogEntryParser.class);
    private final DateTimeFormatter df = DateTimeFormat.forPattern("dd/MMM/yyyy:hh:mm:ss Z");


    public String[] parseRecord(String message) {
        try {
            String[] logEntry = new String[3];
            //      System.out.println("Test");
            int parseIndex = 0;
            int startIndex = 0;
            parseIndex = message.indexOf(" [");
            logEntry[0] = message.substring(startIndex, parseIndex);
            //    System.out.println(logEntry[0]);
            startIndex = parseIndex + 2;
            //     System.out.println("startIndex="+startIndex);
            parseIndex = message.indexOf("] ", startIndex);
            //       System.out.println("parseIndex="+parseIndex);
            // startIndex = parseIndex + 2;
            logEntry[1] = message.substring(startIndex, parseIndex);
            startIndex = parseIndex + 2;
            parseIndex = message.indexOf(" ", startIndex);
            startIndex = parseIndex + 1;
            parseIndex = message.indexOf(" ", startIndex);
            logEntry[2] = message.substring(startIndex, parseIndex);
            return logEntry;
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Parsing Error!\n");
            e.printStackTrace();
            return null;
        }
    }
    public long getTimeinMillis(String dateString) {
        DateTime dt = df.withOffsetParsed().parseDateTime(dateString);
        Long timeInMillis = dt.getMillis() / 1000;
        return timeInMillis;
    }
}
