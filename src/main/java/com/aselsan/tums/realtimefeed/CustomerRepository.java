package com.aselsan.tums.realtimefeed;


interface CustomerRepository {

  CustomerProtos.Customer findById(int id);

  CustomerProtos.CustomerList findAll();

  GtfsRealtime.FeedMessage getUpdate();
}
