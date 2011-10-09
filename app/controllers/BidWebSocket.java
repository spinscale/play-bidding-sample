package controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import models.Auction;
import models.Bid;
import models.LiveBidding;
import play.Logger;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

import static play.libs.F.*;
import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.*;

public class BidWebSocket extends WebSocketController {

    public static void bid(Long auctionId) {
        if (session.get("name") == null) {
            disconnect();
        }

        ArchivedEventStream<String> writeBiddingStream = LiveBidding.getEventStream(auctionId);
        EventStream<String> readBiddingStream = writeBiddingStream.eventStream();

        while(inbound.isOpen()) {
            Either<WebSocketEvent,String> e = await(Promise.waitEither(
                    inbound.nextEvent(),
                    readBiddingStream.nextEvent()
                ));

            Auction auction = Auction.findById(auctionId);

            Date now = new Date();
            if (auction.endingDate.before(now)) {
                auction.end();
                disconnect();
            }

            Bid currentBid = auction.getCurrentBid();

            for(String bidFromThisCustomer: TextFrame.match(e._1)) {
                BigDecimal bidNumber = new BigDecimal(bidFromThisCustomer);
                if (currentBid == null || currentBid.bid.compareTo(bidNumber) < 0) { // Only add if the bid is the highest
                    Bid bid = new Bid(auction, session.get("name"), now, bidNumber).save();
                    String number = new DecimalFormat("0.00").format(bid.bid);
                    writeBiddingStream.publish(number);
                }
            }

            // The stream got updated, sent to the client
            for(String bidFromOtherCustomer: e._2) {
                outbound.send(bidFromOtherCustomer + " EUR");
            }
        }
    }
}
