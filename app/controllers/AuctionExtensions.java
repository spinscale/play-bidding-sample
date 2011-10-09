package controllers;

import java.util.Date;

import models.Auction;
import play.templates.JavaExtensions;

public class AuctionExtensions extends JavaExtensions {

    public static long milliSecondsToEnd(Auction auction) {
        Date now = new Date();
        return auction.endingDate.getTime() - now.getTime();
    }
}
