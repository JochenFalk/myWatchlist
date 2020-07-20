package com.company.presentation;

import com.company.business.ListBusiness;
import com.company.data.model.Watchlist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
public class ListController {

//    @GetMapping("/addMovieToList")
//    public Boolean addMovieToList(HttpSession session,
//                                  @RequestParam(value = "objectList") String objectList,
//                                  @RequestParam(value = "objectMovie") String objectMovie) {
//        return WatchlistBusiness.addMovieToList(objectList, objectMovie, session);
//    }

    @GetMapping("/setPushedMovie")
    public Boolean setPushedMovie(HttpSession session,
                                  @RequestParam(value = "pushedMovie") String pushedMovie) {
        return ListBusiness.setPushedMovie(pushedMovie, session);
    }

    @GetMapping("/getPushedMovie")
    public String setPushedMovie(HttpSession session) {
        return ListBusiness.getPushedMovie(session);
    }

    @GetMapping("/setCurrentList")
    public Boolean setCurrentList(HttpSession session,
                                  @RequestParam(value = "listTitle") String listTitle) {
        return ListBusiness.setCurrentList(listTitle, session);
    }

    @GetMapping("/getCurrentList")
    public Watchlist getCurrentList(HttpSession session) {
        return ListBusiness.getCurrentList(session);
    }

    @GetMapping("/getListFromUser")
    public Watchlist getListFromUser(HttpSession session,
                             @RequestParam(value = "listTitle") String listTitle) {
        return ListBusiness.getListFromUser(listTitle, session);
    }

    @GetMapping("/getAllListsFromUser")
    public ArrayList getAllListsFromUser(HttpSession session) {
        return ListBusiness.getAllListsFromUser(session);
    }

    @GetMapping("/createList")
    public String createList(HttpSession session,
                              @RequestParam(value = "listTitle") String listTitle,
                              @RequestParam(value = "listDesc") String listDesc) {

        return ListBusiness.createList(listTitle, listDesc, session);
    }

    @GetMapping("/createListWithItems")
    public Boolean createListWithItems(HttpSession session,
                              @RequestParam(value = "listTitle") String listTitle,
                              @RequestParam(value = "listDesc") String listDesc,
                              @RequestParam(value = "listItems") String listItems) {

        return ListBusiness.createListWithItems(listTitle, listDesc, listItems, session);
    }

    @GetMapping("/updateListFromUser")
    public Boolean updateListFromUser(HttpSession session,
                                      @RequestParam(value = "listTitle") String listTitle,
                                      @RequestParam(value = "listItems") String listItems) {

        return ListBusiness.updateListFromUser(listTitle, listItems, session);
    }

    @GetMapping("/deleteListFromUser")
    public Boolean deleteListFromUser(HttpSession session,
                                      @RequestParam(value = "listTitle") String listTitle) {
        return ListBusiness.deleteListFromUser(listTitle, session);
    }

    @GetMapping("/getSimilar")
    public Watchlist getSimilar(@RequestParam(value = "movieId") int movieId) {
        return ListBusiness.getSimilar(movieId);
    }

    @GetMapping("/getSystemList")
    public Watchlist getSystemList(
            @RequestParam(value = "listTitle") String listTitle,
            @RequestParam(value = "userName") String userName) {

        return ListBusiness.getSystemList(listTitle, userName);
    }
}
