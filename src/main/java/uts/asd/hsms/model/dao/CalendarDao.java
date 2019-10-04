/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.asd.hsms.model.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import org.bson.types.ObjectId;
import uts.asd.hsms.model.Calendar;

/**
 *
 * @author MatthewHellmich
 */
public class CalendarDao {

    MongoClient mongoClient;
    DB database;
    DBCollection collection;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CalendarDao(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        database = mongoClient.getDB("heroku_r0hsk6vb");
        collection = database.getCollection("calendar");
    }

    public DB getDatabase() {
        return database;
    }

    public Calendar[] getCalendar() {
        DBCursor cursor = collection.find();
        Calendar[] calendars = new Calendar[cursor.count()];
        int count = 0;
        while (cursor.hasNext()) {
            DBObject result = cursor.next();
            ObjectId calendarIdResult = (ObjectId) result.get("_id");
            Date dateResult = (Date) result.get("date");
            String eventNameResult = (String) result.get("eventName");
            String descriptionResult = (String) result.get("description");
            String eventTagResult = (String) result.get("eventTag");
            calendars[count] = new Calendar(null, dateResult, eventNameResult, descriptionResult, eventTagResult);
            count++;

        }
        return calendars;
    }
    
        public Calendar[] getCalendar(ObjectId calendarId, Date date, String eventName, String description, String eventTag) {
        DBCursor cursor = collection.find();
        Calendar[] calendars = new Calendar[cursor.count()];
        int count = 0;
        while (cursor.hasNext()) {
            DBObject result = cursor.next();
            ObjectId calendarIdResult = (ObjectId) result.get("_id");
            Date dateResult = (Date) result.get("date");
            String eventNameResult = (String) result.get("eventName");
            String descriptionResult = (String) result.get("description");
            String eventTagResult = (String) result.get("eventTag");
            calendars[count] = new Calendar(calendarIdResult, dateResult, eventNameResult, descriptionResult, eventTagResult);
            count++;

        }
        return calendars;
    }

    public Calendar getCalendar(ObjectId calendarId) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", calendarId);
        DBCursor cursor = collection.find(query);
        DBObject result = cursor.one();
        if (result != null) {
            Date date = (Date) result.get("date");
            String eventName = (String) result.get("eventName");
            String description = (String) result.get("description");
            String eventTag = (String) result.get("eventTag");
            return new Calendar(calendarId, date, eventName, description, eventTag);
        }
        return null;
    }

    public boolean addCalendar(Calendar calendar) {
        try {
            BasicDBObject newRecord = new BasicDBObject();
            newRecord.put("date", calendar.getDate());
            newRecord.put("eventName", calendar.getEventName());
            newRecord.put("description", calendar.getDescription());
            newRecord.put("eventTag", calendar.getEventTag());
            collection.insert(newRecord);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean editCalendar(Calendar calendar) {
        try {
            BasicDBObject query = new BasicDBObject().append("_id", calendar.getCalendarId());
            BasicDBObject records = new BasicDBObject();
            BasicDBObject update = new BasicDBObject();
            if (calendar.getDate() != null) {
                records.append("date", calendar.getDate());
            }
            if (calendar.getEventName() != null) {
                records.append("eventName", calendar.getEventName());
            }
            if (calendar.getDescription() != null) {
                records.append("description", calendar.getDescription());
            }
            if (calendar.getEventTag() != null) {
                records.append("eventTag", calendar.getEventTag());
            }
            update.append("$set", records);
            collection.update(query, update);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean deleteCalendar(ObjectId calendarId) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", calendarId);
            collection.remove(query);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

//    public void deleteCalendar(ObjectId calendarId) {
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", calendarId);
//        collection.remove(query);
//    }
//    public void addcalendar(Date date, String eventName, String description, String eventTag) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}