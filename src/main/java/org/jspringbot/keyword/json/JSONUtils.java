package org.jspringbot.keyword.json;

import net.minidev.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(JSONUtils.class);

    private static JSONHelper JSON_HELPER;

    public static void setHelper(JSONHelper jsonHelper) {
        JSON_HELPER = jsonHelper;
    }

    private static JSONHelper getHelper() {
        if(JSON_HELPER != null) {
            return JSON_HELPER;
        }

        JSON_HELPER = ApplicationContextHolder.get().getBean(JSONHelper.class);

        return JSON_HELPER;
    }

    public static boolean isJSONValid(String test) {
        try {
            new org.json.JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static Object object(Object... args) throws TransformerException {
        List objs = objects(args);


        if(CollectionUtils.isNotEmpty(objs)) {
            return objs.iterator().next();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<String> propertyNames(String... args) throws TransformerException {
        String path = "*";
        LinkedHashMap object;

        if(args.length == 1 && String.class.isInstance(args[0])) {
            path = args[0];
        }


        object = (LinkedHashMap) getHelper().getJsonValue(path);

        if(object == null) {
            throw new IllegalStateException(String.format("Unable to find object keys given '%s'", path));
        }

        return new ArrayList<String>(object.keySet());
    }

    public static String type(Object item) throws TransformerException {
        if(Map.class.isInstance(item)) {
            return "object";
        } else if(List.class.isInstance(item)) {
            return "list";
        } else if(Number.class.isInstance(item)) {
            return "number";
        } else if(String.class.isInstance(item)) {
            return "string";
        } else if(Boolean.class.isInstance(item)) {
            return "boolean";
        } else if(item == null) {
            return "null";
        } else {
            return StringUtils.lowerCase(item.getClass().getSimpleName());
        }
    }


    public static List objects(Object... args) throws TransformerException {
        if(args.length == 2 && JSONObject.class.isInstance(args[0]) && String.class.isInstance(args[1])) {
            JSONObject jsonObject = (JSONObject) args[0];
            String jsonPath = (String) args[1];

            return getHelper().getJsonValues(jsonObject, jsonPath);
        } else if(String.class.isInstance(args[0])) {
            String jsonPath = (String) args[0];

            return getHelper().getJsonValues(jsonPath);
        }

        throw new IllegalArgumentException("Expected 'xml:objects(jsonObject, jsonPath)' or 'xml:objects(jsonPath)'.");
    }

    public static Object singleProperty(JSONObject obj, String propertyName) {
        LOG.keywordAppender().appendProperty("JSON Property Name", propertyName);

        Object propertyValue = obj.get(propertyName);

        LOG.keywordAppender().appendProperty("JSON Property Value", propertyValue);

        return propertyValue;
    }

    public static Object property(Object... args) throws TransformerException {
        if(args[0] == null && args.length == 2 && String.class.isInstance(args[1])) {
            return property(args[1]);
        } else if(JSONObject.class.isInstance(args[0]) && args.length == 2) {
            JSONObject obj = (JSONObject) args[0];
            String property = (String) args[1];

            return singleProperty(obj, property);
        } else if(String.class.isInstance(args[0]) && args.length == 2) {
            String jsonPath = (String) args[0];
            String property = (String) args[1];

            LOG.keywordAppender().appendProperty("JSON Path", jsonPath);

            List items = objects(jsonPath);

            if(CollectionUtils.isEmpty(items)) {
                throw new IllegalArgumentException(String.format("No objects found for jsonPath '%s'", jsonPath));
            }

            JSONObject obj = (JSONObject) items.iterator().next();
            return singleProperty(obj, property);
        } else if(String.class.isInstance(args[0]) && args.length == 1) {
            String jsonPath = (String) args[0];

            LOG.keywordAppender().appendProperty("JSON Path", jsonPath);

            return getHelper().getJsonValue(jsonPath);
        }

        throw new IllegalArgumentException("Expected 'json:property(propertyName)' or 'json:property(jsonObject, propertyName)' or 'json:property(jsonPath, propertyName)'.");
    }

}
