package dhiraj.com.weatherapp;

/**
 * Created by dhira on 05-04-2017.
 */

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    public class CurrentCityKeyUtil {
        static public class CurrentCityKeyJSONParser{
            static String parseCurrentCityKey(String in) throws JSONException {
                JSONArray jsonArrayRoot=new JSONArray(in);
                JSONObject jsonObjectRoot=jsonArrayRoot.getJSONObject(0);
                String key=jsonObjectRoot.getString("Key");
                return key;
            }
        }
    }
