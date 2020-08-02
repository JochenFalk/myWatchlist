package com.company.presentation;

import com.company.business.AdminBusiness;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
public class AdminController {

    @GetMapping("/getPrimaryQuery")
    public HashMap<String, Object> getPrimaryQuery(HttpSession session,
                                                   @RequestParam(value = "selection") String selection) {

        if (session.getAttribute("role") != null) {
            if (session.getAttribute("role").toString().equals("Admin")) {
                return AdminBusiness.getPrimaryQuery(selection);
            }
        }

        return null;
    }

    @GetMapping("/saveRecord")
    public String saveRecord(HttpSession session,
                             @RequestParam(value = "mappedValues") String mappedValues,
                             @RequestParam(value = "loadedObject") String loadedObject,
                             @RequestParam(value = "newQueryResults") String newQueryResults,
                             @RequestParam(value = "itemIndex") int itemIndex){

        if (session.getAttribute("role") != null) {
            if (session.getAttribute("role").toString().equals("Admin")) {
                return AdminBusiness.saveRecord(mappedValues, loadedObject, newQueryResults, itemIndex);
            }
        }

        return null;
    }

    @GetMapping("/deleteRecord")
    public String deleteRecord(HttpSession session,
                               @RequestParam(value = "mappedValues") String mappedValues,
                               @RequestParam(value = "loadedObject") String loadedObject) {

        if (session.getAttribute("role") != null) {
            if (session.getAttribute("role").toString().equals("Admin")) {
                return AdminBusiness.deleteRecord(mappedValues, loadedObject);
            }
        }

        return null;
    }
}
