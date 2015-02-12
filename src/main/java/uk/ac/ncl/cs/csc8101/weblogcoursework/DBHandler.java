package uk.ac.ncl.cs.csc8101.weblogcoursework;

import com.clearspring.analytics.stream.cardinality.HyperLogLog;

import java.io.IOException;

/**
 * Created by nipun on 2/11/2015.
 */
public class DBHandler {

    public static void main(String[] args) throws IOException {
        HyperLogLog hyperLogLog= new HyperLogLog(2);

        hyperLogLog.getBytes();


    }

    public boolean saveSessionInfo()
    {
        return true;
    }
}
