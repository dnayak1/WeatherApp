package dhiraj.com.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dhira on 07-04-2017.
 */

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();

    }
    public static class SettingsFragment extends PreferenceFragment {
        Preference dialogPreference,currentCityPreference;
        CharSequence[] items={"Celsius","Fahrenheit"};
        SharedPreferences preferences;
        int index = -1;
        int selected;
        String savedCity,savedCountry,savedTemperature;
        EditText editTextSetCurrentCityName;
        EditText editTextSetCurrentCountryName;
        String buttonText="Set";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            preferences= this.getActivity().getSharedPreferences("locationPreferences", Context.MODE_PRIVATE);
            savedCity=preferences.getString("city","");
            savedCountry=preferences.getString("country","");
            savedTemperature=preferences.getString("temperature","");
            if(savedTemperature.equalsIgnoreCase("Fahrenheit")){
                index=1;
            }
            else if (savedTemperature.equalsIgnoreCase("Celsius")){
                index=0;
            }
            else{
                index=-1;
            }
            dialogPreference = (Preference) getPreferenceScreen().findPreference("listTemperature");
            currentCityPreference=getPreferenceScreen().findPreference("listCurrentCity");
            dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(final Preference preference) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose Temperature Unit")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setSingleChoiceItems(items, index , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selected=which;
                                }
                            })
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   if(selected>-1){
                                       if(index==1 && selected==0){
                                           Toast.makeText(getActivity(), "Temperature Unit has been changed from °F to °C", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(getActivity(), "Temperature Unit has been changed from °C to °F", Toast.LENGTH_SHORT).show();
                                       }
                                       index=selected;
                                       SharedPreferences.Editor editor=preferences.edit();
                                       editor.putString("temperature", String.valueOf(items[selected]));
                                       editor.apply();
                                   }

                                }
                            })
                            .show();
                    return true;
                }
            });
            currentCityPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater=getActivity().getLayoutInflater();
                    final View layout=inflater.inflate(R.layout.alert_layout,null);
                    builder.setView(layout);
                    editTextSetCurrentCityName= (EditText) layout.findViewById(R.id.editTextAlertCurrentCityName);
                    editTextSetCurrentCountryName= (EditText) layout.findViewById(R.id.editTextAlertCurrentCountryName);
                    if(savedCity!=null && !savedCity.isEmpty() && savedCountry!=null && !savedCountry.isEmpty()){
                        editTextSetCurrentCityName.setText(savedCity);
                        editTextSetCurrentCountryName.setText(savedCountry);
                        buttonText="Change";
                    }
                    builder.setTitle("Enter City Details");
                    builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            savedCity=editTextSetCurrentCityName.getText().toString().trim();
                            savedCountry=editTextSetCurrentCountryName.getText().toString().trim();
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("city", savedCity);
                            editor.putString("country",savedCountry);
                            editor.apply();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(MainActivity.RESULT_OK);
    }
}
