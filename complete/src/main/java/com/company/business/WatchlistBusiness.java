package com.company.business;

import com.company.data.User;
import com.company.data.Watchlist;

import java.util.ArrayList;

public class WatchlistBusiness {

    public void createWatchlist(String title, String description, User user) {
        Watchlist watchlist = new Watchlist(title, description, user);
    }

    public void deleteWatchlist(String title, User user) {
        ArrayList<Watchlist> watchlists = Watchlist.getWatchlists();
        for (Watchlist thisWatchlist : watchlists) {
            boolean verified = user.equals(thisWatchlist.getCreator());
            if(verified) {
                watchlists.remove(thisWatchlist);
                Watchlist.setWatchlists(watchlists);
            }
        }
    }

    public void renameWatchlist(String title, User user) {
        ArrayList<Watchlist> watchlists = Watchlist.getWatchlists();
        for (Watchlist thisWatchlist : watchlists) {
            boolean verified = user.equals(thisWatchlist.getCreator());
            if(verified) {
                thisWatchlist.setTitle(title);
            }
        }
    }

    public void modifyDescriptionWatchlist(String description, User user) {
        ArrayList<Watchlist> watchlists = Watchlist.getWatchlists();
        for (Watchlist thisWatchlist : watchlists) {
            boolean verified = user.equals(thisWatchlist.getCreator());
            if(verified) {
                thisWatchlist.setDescription(description);
            }
        }
    }
}
