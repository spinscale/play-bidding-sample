package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.Logger;
import play.data.validation.Email;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Auction extends Model {

    public Auction(String name, String description, String owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public String name;
    public String description;
    public Blob image;
    public Date endingDate;

    @Email
    public String owner;

    @Email
    public String winner;

    @OneToOne
    public Bid winningBid;

    public static List<Auction> soonEnding() {
        return find("endingDate > ? order by endingDate asc", new Date()).fetch(10);
    }

    public Bid getCurrentBid() {
        return Bid.find("auction = ? order by bidTimestamp desc", this).first();
    }

    public boolean isFinished() {
        return endingDate.before(new Date());
    }

    public void end() {
        if (winningBid != null) return;

        winningBid = getCurrentBid();
        if (winningBid == null) return;

        winner = winningBid.bidder;
        save();
    }
}
