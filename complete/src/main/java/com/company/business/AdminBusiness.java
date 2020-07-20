package com.company.business;

import com.company.data.PostgreSystemQueries;
import com.company.data.model.Email;
import com.company.data.model.Search;
import com.company.data.model.User;
import com.company.data.model.Watchlist;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminBusiness {

    public static HashMap<String, Object> getPrimaryQuery(String selection, HttpSession session) {

        ArrayList<String> dropDownOptions;
        HashMap<String, ArrayList<String>> queryResults;
        HashMap<String, Object> resultMap = new HashMap<>();

        switch (selection) {
            case "Users":

                ArrayList<User> users = PostgreSystemQueries.getAllUsers();

                ArrayList<String> userNames = getUserNames(users);
                dropDownOptions = getDropDownOptions(users);
                queryResults = getUserQueryResults(users);

                resultMap.put("returnValues", userNames);
                resultMap.put("dropDownOptions", dropDownOptions);
                resultMap.put("queryResults", queryResults);
                break;

            case "Lists":

                ArrayList<Watchlist> watchlists = PostgreSystemQueries.getAllLists();

                ArrayList<String> listTitles = getListTitles(watchlists);
                dropDownOptions = getDropDownOptions(watchlists);
                queryResults = getListQueryResults(watchlists);

                resultMap.put("returnValues", listTitles);
                resultMap.put("dropDownOptions", dropDownOptions);
                resultMap.put("queryResults", queryResults);
                break;

            case "Search results":

                ArrayList<Search> searches = PostgreSystemQueries.getAllSearches();

                ArrayList<String> searchTitles = getSearchTitles(searches);
                dropDownOptions = getDropDownOptions(searches);
                queryResults = getSearchQueryResults(searches);

                resultMap.put("returnValues", searchTitles);
                resultMap.put("dropDownOptions", dropDownOptions);
                resultMap.put("queryResults", queryResults);
                break;

            case "Messages":

                ArrayList<Email> emails = PostgreSystemQueries.getAllEmails();

                ArrayList<String> emailTypes = getEmailTypes(emails);
                dropDownOptions = getDropDownOptions(emails);
                queryResults = getEmailQueryResults(emails);

                resultMap.put("returnValues", emailTypes);
                resultMap.put("dropDownOptions", dropDownOptions);
                resultMap.put("queryResults", queryResults);
                break;
        }

        return resultMap;
    }

    public static ArrayList<String> getUserNames(ArrayList<User> users) {

        ArrayList<String> userNames = new ArrayList<>();

        for (User thisUser : users) {
            userNames.add(thisUser.getName());
        }

        return userNames;
    }

    public static ArrayList<String> getListTitles(ArrayList<Watchlist> watchlists) {

        ArrayList<String> listTitles = new ArrayList<>();

        for (Watchlist thisWatchlist : watchlists) {
            listTitles.add(thisWatchlist.getTitle());
        }

        return listTitles;
    }

    public static ArrayList<String> getSearchTitles(ArrayList<Search> searches) {

        ArrayList<String> searchTitles = new ArrayList<>();

        for (Search thisSearch : searches) {
            searchTitles.add(thisSearch.getTitle());
        }

        return searchTitles;
    }

    public static ArrayList<String> getEmailTypes(ArrayList<Email> emails) {

        ArrayList<String> emailTypes = new ArrayList<>();

        for (Email thisEmail : emails) {
            emailTypes.add(thisEmail.getType());
        }

        return emailTypes;
    }

    public static HashMap<String, ArrayList<String>> getUserQueryResults(ArrayList<User> users) {

        HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> role = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();
        ArrayList<String> emailAddress = new ArrayList<>();
        ArrayList<String> isValidated = new ArrayList<>();
        ArrayList<String> isRegistered = new ArrayList<>();
        ArrayList<String> validationAttempts = new ArrayList<>();
        ArrayList<String> token = new ArrayList<>();
        ArrayList<String> tokenExpiryDate = new ArrayList<>();

        if (users != null) {
            for (User thisUser : users) {
                id.add(String.valueOf(thisUser.getId()));
                role.add(String.valueOf(thisUser.getRole()));
                name.add(String.valueOf(thisUser.getName()));
                password.add(String.valueOf(thisUser.getPassword()));
                emailAddress.add(String.valueOf(thisUser.getEmailAddress()));
                isValidated.add(String.valueOf(thisUser.getValidated()));
                isRegistered.add(String.valueOf(thisUser.getRegistered()));
                validationAttempts.add(String.valueOf(thisUser.getValidationAttempts()));
                token.add(String.valueOf(thisUser.getToken()));
                tokenExpiryDate.add(String.valueOf(thisUser.getTokenExpiryDate()));
            }
        }

        resultMap.put("id", id);
        resultMap.put("role", role);
        resultMap.put("name", name);
        resultMap.put("password", password);
        resultMap.put("emailAddress", emailAddress);
        resultMap.put("isValidated", isValidated);
        resultMap.put("isRegistered", isRegistered);
        resultMap.put("validationAttempts", validationAttempts);
        resultMap.put("token", token);
        resultMap.put("tokenExpiryDate", tokenExpiryDate);

        return resultMap;
    }

    public static HashMap<String, ArrayList<String>> getListQueryResults(ArrayList<Watchlist> watchlists) {

        HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> listItems = new ArrayList<>();

        if (watchlists != null) {
            for (Watchlist thisWatchlist : watchlists) {
                id.add(String.valueOf(thisWatchlist.getId()));
                title.add(String.valueOf(thisWatchlist.getTitle()));
                description.add(String.valueOf(thisWatchlist.getDescription()));
                // Added listItems count for readability
                listItems.add(String.valueOf(thisWatchlist.getListItems().size()));
            }
        }

        resultMap.put("id", id);
        resultMap.put("title", title);
        resultMap.put("description", description);
        resultMap.put("listItems", listItems);

        return resultMap;
    }

    public static HashMap<String, ArrayList<String>> getSearchQueryResults(ArrayList<Search> searches) {

        HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> year = new ArrayList<>();
        ArrayList<String> creationDate = new ArrayList<>();

        if (searches != null) {
            for (Search thisSearch : searches) {
                id.add(String.valueOf(thisSearch.getId()));
                title.add(String.valueOf(thisSearch.getTitle()));
                year.add(String.valueOf(thisSearch.getYear()));
                creationDate.add(String.valueOf(thisSearch.getCreationDate()));
            }
        }

        resultMap.put("id", id);
        resultMap.put("title", title);
        resultMap.put("year", year);
        resultMap.put("creationDate", creationDate);

        return resultMap;
    }

    public static HashMap<String, ArrayList<String>> getEmailQueryResults(ArrayList<Email> emails) {

        HashMap<String, ArrayList<String>> resultMap = new HashMap<>();

        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> body = new ArrayList<>();
        ArrayList<String> emailAddress = new ArrayList<>();

        if (emails != null) {
            for (Email thisEmail : emails) {
                id.add(String.valueOf(thisEmail.getId()));
                type.add(String.valueOf(thisEmail.getType()));
                subject.add(String.valueOf(thisEmail.getSubject()));
                body.add(String.valueOf(thisEmail.getBody()));
                emailAddress.add(String.valueOf(thisEmail.getRecipient()));
            }
        }

        resultMap.put("id", id);
        resultMap.put("type", type);
        resultMap.put("recipient", emailAddress);
        resultMap.put("subject", subject);
        resultMap.put("body", body);

        return resultMap;
    }

    public static ArrayList<String> getDropDownOptions(ArrayList arrayList) {

        ArrayList<String> dropDownOptions = new ArrayList<>();

        if (arrayList != null && !arrayList.isEmpty()) {
            for (Field field : arrayList.get(0).getClass().getDeclaredFields()) {
                int modifier = field.getModifiers();
                String modifierName = Modifier.toString(modifier);
                if (modifierName.equals("private") || modifierName.equals("private final")) {
                    dropDownOptions.add(field.getName());
                }
            }
        }

        return dropDownOptions;
    }

    public static String saveRecord(String mappedValues, String loadedObject, String newQueryResults) {

        String returnMsg = "";

        switch (loadedObject) {
            case "Users":
                returnMsg = updateUser(mappedValues);
                break;
            case "Lists":
                returnMsg = updateList(mappedValues, newQueryResults);
                break;
            case "Search results":
                returnMsg = updateSearch(mappedValues);
                break;
            case "Messages":
                returnMsg = updateEmail(mappedValues);
                break;
        }

        return returnMsg;
    }

    public static String updateUser(String mappedValues) {

        JSONObject object = (JSONObject) JSONValue.parse(mappedValues);

        try {

            long id = Integer.parseInt(object.get("id").toString());
            String role = object.get("role").toString();
            String name = object.get("name").toString();
            String password = object.get("password").toString();
            String emailAddress = object.get("emailAddress").toString();
            Boolean isValidated = Boolean.valueOf(object.get("isValidated").toString());
            Boolean isRegistered = Boolean.valueOf(object.get("isRegistered").toString());
            int validationAttempts = Integer.parseInt(object.get("validationAttempts").toString());
            String token = object.get("token").toString();
            Instant tokenExpiryDate = Instant.parse(object.get("tokenExpiryDate").toString());

            User newUser = new User(id, role, name, password, emailAddress, isValidated, isRegistered, validationAttempts, token, tokenExpiryDate);
            int returnStatement = PostgreSystemQueries.updateUser(newUser);

            JSONObject msg = new JSONObject();

            if (returnStatement == 1) {
                return "true";
            } else {
                msg.put("msg", "Save unsuccessful! Please verify that your entered data validation is correct.");
                return msg.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String updateList(String mappedValues, String newQueryResults) {

        JSONObject valuesObject = (JSONObject) JSONValue.parse(mappedValues);
        JSONObject resultsObject = (JSONObject) JSONValue.parse(newQueryResults);

        int index = Integer.parseInt(valuesObject.get("id").toString());
        ArrayList titles = (ArrayList) resultsObject.get("title");
        String previousTitle = titles.get(index).toString();

        Watchlist watchlist = null;
        ArrayList<Watchlist> watchlists = PostgreSystemQueries.getAllLists();

        for (Watchlist thisWatchlist : watchlists) {
            boolean exists = thisWatchlist.getTitle().equals(previousTitle);
            if (exists) {
                watchlist = thisWatchlist;
            }
        }

        if (watchlist != null) {
            try {

                long id = Integer.parseInt(valuesObject.get("id").toString());
                String title = valuesObject.get("title").toString();
                String description = valuesObject.get("description").toString();
                ArrayList<Object> listItems = watchlist.getListItems();

                Watchlist newWatchlist = new Watchlist(id, title, description, listItems);
                int returnStatement = PostgreSystemQueries.updateList(newWatchlist);

                JSONObject msg = new JSONObject();

                if (returnStatement == 1) {
                    return "true";
                } else {
                    msg.put("msg", "Save unsuccessful! Please verify that your entered data validation is correct.");
                    return msg.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String updateSearch(String mappedValues) {

        JSONObject valuesObject = (JSONObject) JSONValue.parse(mappedValues);

        try {

            long id = Integer.parseInt(valuesObject.get("id").toString());
            String title = valuesObject.get("title").toString();
            String year = valuesObject.get("year").toString();
            int returnValue = 0;
            Instant creationDate = Instant.parse(valuesObject.get("creationDate").toString());

            Search newSearch = new Search(id, title, year, returnValue, creationDate);
            int returnStatement = PostgreSystemQueries.updateSearch(newSearch);

            JSONObject msg = new JSONObject();

            if (returnStatement == 1) {
                return "true";
            } else {
                msg.put("msg", "Save unsuccessful! Please verify that your entered data validation is correct.");
                return msg.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String updateEmail(String mappedValues) {

        JSONObject valuesObject = (JSONObject) JSONValue.parse(mappedValues);

        try {

            long id = Integer.parseInt(valuesObject.get("id").toString());
            String type = valuesObject.get("type").toString();
            String recipient = valuesObject.get("recipient").toString();
            String subject = valuesObject.get("subject").toString();
            String body = valuesObject.get("body").toString();

            Email newEmail = new Email(id, type, recipient, subject, body);
            User user = PostgreSystemQueries.getUserByEmailId(id);

            if (user != null) {

                int returnStatement = PostgreSystemQueries.updateEmail(newEmail, user);

                JSONObject msg = new JSONObject();

                if (returnStatement == 1) {
                    return "true";
                } else {
                    msg.put("msg", "Save unsuccessful! Please verify that your entered data validation is correct.");
                    return msg.toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String deleteRecord(String mappedValues, String loadedObject) {

        String returnMsg = "";

        switch (loadedObject) {
            case "Users":
                returnMsg = deleteUser(mappedValues);
                break;
            case "Lists":
                returnMsg = deleteList(mappedValues);
                break;
            case "Search results":
                returnMsg = deleteSearch(mappedValues);
                break;
            case "Messages":
                returnMsg = deleteEmail(mappedValues);
                break;
        }

        return returnMsg;
    }

    public static String deleteUser(String mappedValues) {

        JSONObject object = (JSONObject) JSONValue.parse(mappedValues);

        long id = Long.parseLong(object.get("id").toString());
        int returnStatement = PostgreSystemQueries.deleteUserById(id);

        JSONObject msg = new JSONObject();

        if (returnStatement == 1) {
            return "true";
        } else {
            msg.put("msg", "Oeps! An unknown error occurred ):");
            return msg.toString();
        }
    }

    public static String deleteList(String mappedValues) {

        JSONObject object = (JSONObject) JSONValue.parse(mappedValues);

        long id = Long.parseLong(object.get("id").toString());
        int returnStatement = PostgreSystemQueries.deleteListById(id);

        JSONObject msg = new JSONObject();

        if (returnStatement == 1) {
            return "true";
        } else {
            msg.put("msg", "Oeps! An unknown error occurred ):");
            return msg.toString();
        }
    }

    public static String deleteSearch(String mappedValues) {

        JSONObject object = (JSONObject) JSONValue.parse(mappedValues);

        long id = Long.parseLong(object.get("id").toString());
        int returnStatement = PostgreSystemQueries.deleteSearchById(id);

        JSONObject msg = new JSONObject();

        if (returnStatement == 1) {
            return "true";
        } else {
            msg.put("msg", "Oeps! An unknown error occurred ):");
            return msg.toString();
        }
    }

    public static String deleteEmail(String mappedValues) {

        JSONObject object = (JSONObject) JSONValue.parse(mappedValues);

        long id = Long.parseLong(object.get("id").toString());
        int returnStatement = PostgreSystemQueries.deleteEmailById(id);

        JSONObject msg = new JSONObject();

        if (returnStatement == 1) {
            return "true";
        } else {
            msg.put("msg", "Oeps! An unknown error occurred ):");
            return msg.toString();
        }
    }
}
