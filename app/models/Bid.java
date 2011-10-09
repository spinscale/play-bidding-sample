package models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Email;
import play.db.jpa.Model;

@Entity
public class Bid extends Model {

    public Bid() {}

    public Bid(Auction auction, String bidder, Date bidTimestamp, BigDecimal bid) {
        this.auction = auction;
        this.bidder = bidder;
        this.bidTimestamp = bidTimestamp;
        this.bid = bid;
    }

    // TODO: Find correct entity type here
    public BigDecimal bid;

    public Date bidTimestamp;

    @Email
    public String bidder;

    @ManyToOne
    public Auction auction;
}
