package dev.rodni.ru.vktest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static android.widget.Toast.LENGTH_SHORT;
import static dev.rodni.ru.vktest.utils.NetworkUtils.generateURL;
import static dev.rodni.ru.vktest.utils.NetworkUtils.getResponseFromUrl;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.FRIENDS, VKScope.WALL};
    //ListView listoffriends;
    TextView resultText;
    Button searchButton;
    EditText searchText;

    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String firstName = null;
            String lastName = null;

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                JSONObject userInfo = jsonArray.getJSONObject(0);

                firstName = userInfo.getString("first_name");
                lastName = userInfo.getString("last_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String resultString = "Имя: " + firstName + "\n" + "Фамилия: " + lastName + ".";

            resultText.setText(resultString);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VKSdk.login(this,scope);

        resultText = (TextView) findViewById(R.id.result_text);
        searchButton = (Button) findViewById(R.id.button_search);
        searchText = (EditText) findViewById(R.id.search_text);


        searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable() && !searchText.getText().toString().equals("")) {

                        URL generatedURL = generateURL(searchText.getText().toString());

                        new VKQueryTask().execute(generatedURL);

                    } else if(isNetworkAvailable() && searchText.getText().toString().equals("")) {

                        Toast.makeText(MainActivity.this, "Введите id пользователя", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(MainActivity.this, "Подключите интернет соединение...", Toast.LENGTH_SHORT).show();

                    }
                }
            });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Success", LENGTH_SHORT).show();// Пользователь успешно авторизовался
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}


//------------------------------------------------------

//listoffriends = findViewById(R.id.listoffriends);

                /*VKRequest request = new VKApiGroups().getById(VKParameters.from("groups_ids", "180226135"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKList list = (VKList) response.parsedModel;

                        //System.out.print(response.responseString);
                        try {
                            //Toast.makeText(getApplicationContext(), list.get(0).fields.getInt("id"), LENGTH_SHORT).show();
                            VKRequest vkRequest1 = new VKApiWall()
                                    .get(VKParameters.from(VKApiConst.OWNER_ID, "-" + list.get(0).fields.getInt("id"),
                                            VKApiConst.COUNT, 100));
                            vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);

                                    Toast.makeText(getApplicationContext(), list.get(0).fields.getInt("id"), LENGTH_SHORT).show();
                                    System.out.print(response.responseString);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                        //for friends list
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first name, last name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKList list = (VKList) response.parsedModel;

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, list);

                        listoffriends.setAdapter(arrayAdapter);
                    }
                });*/
