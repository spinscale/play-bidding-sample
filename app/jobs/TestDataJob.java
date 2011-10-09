package jobs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import models.Auction;
import models.Bid;
import play.Play;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

@OnApplicationStart
public class TestDataJob extends Job {

    @Override
    public void doJob() {
        if (Auction.count() == 0) {
            Bid.deleteAll();
            new Auction("Used BMW 318", "Nice car in a <b>very good</b> shape. Foto taken from <a href=\"http://www.flickr.com/photos/xrrr/54267872/\">here</a>", "a@a.de").save();
            new Auction("Play framework cookbook", "Wonderful read. Non native speaker.", "a@a.de").save();
            new Auction("Used matches", "In good shape. Dont lit anymore. They are great anyway. Foto taken from <a href=\"http://www.flickr.com/photos/theknowlesgallery/4536616950/\">here</a>", "b@b.de").save();
            new Auction("Apache OFBiz - The good parts", "An empty book. Was disappointed to find out the truth. Foto taken from <a href=\"http://www.flickr.com/photos/hirok/4404405566/\">here</a>", "b@b.de").save();
            new Auction("Rimowa backpack", "High quality backpack from rimowa. With lots of alloy. Foto taken from <a href=\"http://www.flickr.com/photos/siva0215/1184850902/\">here</a>", "b@b.de").save();
            updateEndingDateOfAllAuctions();
        }
    }

    private void updateEndingDateOfAllAuctions() {
        Calendar cal = Calendar.getInstance();
        List<Auction> auctions = Auction.findAll();
        for (Auction auction : auctions) {
            cal.add(Calendar.HOUR, 1 * 24);
            auction.endingDate = new Timestamp(cal.getTimeInMillis());

            VirtualFile image = Play.getVirtualFile("public/images/" + auction.id + ".jpg");
            if (image != null && image.exists()) {
                Blob imageBlob = new Blob();
                imageBlob.set(image.inputstream(), "image/jpg");
                auction.image = imageBlob;
            }
            auction.save();
        }

        // Set one auction in the past
        Auction auction = Auction.findById(1L);
        cal.add(Calendar.MONTH, -2);
        auction.endingDate = new Timestamp(cal.getTimeInMillis());
        auction.save();
    }
}
