package com.aselsan.tums.realtimefeed;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import static java.util.Arrays.asList;

public class main {
    public static void main(String[] args) {
        try {
            FileOutputStream output = new FileOutputStream("D:\\a.txt");
            GtfsRealtime.FeedHeader feedHeader = GtfsRealtime.FeedHeader.newBuilder()
                    .setGtfsRealtimeVersion("2.0")
                    .setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
                    .setTimestamp(1656230726)
                    .build();



            GtfsRealtime.TripUpdate.StopTimeUpdate t = GtfsRealtime.TripUpdate.StopTimeUpdate.newBuilder()
                    .setStopSequence(1)
                    .setStopId("1")
                    .setArrival(GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder().setTime(1284457468L).build())
                    .build();


            Collection<GtfsRealtime.TripUpdate.StopTimeUpdate> allUpdates = asList(t);
            GtfsRealtime.TripUpdate u =	GtfsRealtime.TripUpdate.newBuilder()
                    .setTrip(GtfsRealtime.TripDescriptor.newBuilder()
                            .setTripId("1")
                            .setStartTime("12:34:56")
                            .build())
                    .addAllStopTimeUpdate(allUpdates)
                    .build();


            GtfsRealtime.FeedEntity feedEntity = GtfsRealtime.FeedEntity.newBuilder()
                    .setId("1")
                    .setTripUpdate(u)
                    .build();

            GtfsRealtime.FeedMessage feedMessage = GtfsRealtime.FeedMessage.newBuilder()
                    .setHeader(feedHeader)
                    .addAllEntity(asList(feedEntity))
                    .build();
            feedMessage.writeTo(output);
            output.close();
            System.out.println("dsd");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
