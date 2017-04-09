package dhiraj.com.weatherapp;

/**
 * Created by dhira on 05-04-2017.
 */

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    public class CurrentCityKeyUtil {
        static public class CurrentCityKeyJSONParser{
            static KeyDetails parseCurrentCityKey(String in) throws JSONException {
                JSONArray jsonArrayRoot=new JSONArray(in);
                JSONObject jsonObjectRoot=jsonArrayRoot.getJSONObject(0);
                KeyDetails keyDetails=new KeyDetails();
                keyDetails.setKey(jsonObjectRoot.getString("Key"));
                keyDetails.setCity(jsonObjectRoot.getString("EnglishName"));
                JSONObject jsonObjectCountry=jsonObjectRoot.getJSONObject("Country");
                keyDetails.setCountry(jsonObjectCountry.getString("ID"));
                return keyDetails;
            }
        }
    }
