package controllers;

import java.util.List;

import models.Auction;
import play.Play;
import play.data.validation.Required;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        List<Auction> auctions = Auction.soonEnding();
        render(auctions);
    }

    public static void listAllAuctions() {
        List<Auction> auctions = Auction.findAll();
        renderTemplate("Application/index.html", auctions);
    }

    public static void login(@Required String name) {
        notFoundIfNull(name);
        session.put("name", name);
        ok();
    }

    public static void logout() {
        session.clear();
        index();
    }

    public static void show(Long id) {
        Auction auction = Auction.findById(id);
        notFoundIfNull(auction);

        render(auction);
    }

    public static void showImage(Long id) {
        Auction auction = Auction.findById(id);
        notFoundIfNull(auction);

        if (!auction.image.exists()) {
            renderBinary(Play.getVirtualFile("public/images/noimage.png").inputstream());
        }
        renderBinary(auction.image.get());
    }

}
