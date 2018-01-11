package com.info.majeur.majeurapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.info.majeur.majeurapp.models.Building;
import com.info.majeur.majeurapp.models.Light;
import com.info.majeur.majeurapp.models.Noise;
import com.info.majeur.majeurapp.models.Room;
import com.info.majeur.majeurapp.models.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public List<Building> buildings;
    public Building building;
    public List<Room> rooms;
    public Room room;
    public ImageButton imageButtonLight;
    public ImageButton imageButtonNoise;
    public SeekBar lightlevel;
    public SeekBar noiselevel;
    public Button buttonRefresh;
    public ScrollView scrollView;
    public ProgressBar progressBar;
    public Spinner spinnerBuilding;
    public Spinner spinnerRoom;
    public Response.Listener<JSONArray> response_buildings;
    public Response.Listener<JSONObject> response_room;
    public Response.ErrorListener errorListener;
    public String apirooms = "https://secret-chamber-54105.herokuapp.com/api/rooms/";
    public String apibulidings = "https://secret-chamber-54105.herokuapp.com/api/buildings";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButtonLight = findViewById(R.id.imageButtonLight);
        imageButtonNoise = findViewById(R.id.imageButtonNoise);
        lightlevel = findViewById(R.id.seekBarLight);
        noiselevel = findViewById(R.id.seekBarNoise);
        buttonRefresh = findViewById(R.id.buttonRef);
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        spinnerBuilding = findViewById(R.id.spinnerbuilding);
        spinnerRoom = findViewById(R.id.spinnerRoom);

        noiselevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.this.room.getNoise().getStatus().equals(Status.ON)) {
                    MainActivity.this.setroomdata(MainActivity.this.apirooms + room.getId() + "/noise/level/" + seekBar.getProgress());
                } else {
                    MainActivity.this.set();
                }

            }
        });

        lightlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.this.room.getLight().getStatus().equals(Status.ON)) {
                    MainActivity.this.setroomdata(MainActivity.this.apirooms + room.getId() + "/light/level/" + seekBar.getProgress());
                } else {
                    MainActivity.this.set();
                }
            }
        });

        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.building = MainActivity.this.buildings.get(i);
                MainActivity.this.rooms = MainActivity.this.building.getRooms();
                setspinnerRoom();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.room = MainActivity.this.rooms.get(i);
                set();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        response_room = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");

                    MainActivity.this.room.setId(Long.parseLong(id));

                    JSONObject light = response.getJSONObject("light");
                    String light_status = light.getString("status");
                    String light_level = light.getString("level");

                    MainActivity.this.room.getLight().setLevel(Integer.parseInt(light_level));
                    MainActivity.this.room.getLight().setId(Long.parseLong(light.getString("id")));
                    if(light_status.equals("ON")) {
                        MainActivity.this.room.getLight().setStatus(Status.ON);
                    } else {
                        MainActivity.this.room.getLight().setStatus(Status.OFF);
                    }

                    JSONObject noise = response.getJSONObject("noise");
                    String noise_status = noise.getString("status");
                    String noise_level = noise.getString("level");

                    MainActivity.this.room.getNoise().setLevel(Integer.parseInt(noise_level));
                    MainActivity.this.room.getNoise().setId(Long.parseLong(noise.getString("id")));
                    if(noise_status.equals("ON")) {
                        MainActivity.this.room.getNoise().setStatus(Status.ON);
                    } else {
                        MainActivity.this.room.getNoise().setStatus(Status.OFF);
                    }
                    MainActivity.this.set();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };



        response_buildings = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    MainActivity.this.buildings = new ArrayList<>();
                    for (int i = 0 ; i < response.length() ; i ++) {
                        Building building = new Building();
                        JSONObject buildingjson = response.getJSONObject(i);

                        String id = buildingjson.getString("id");
                        building.setId(Long.parseLong(id));

                        String name = buildingjson.getString("name");
                        building.setName(name);

                        JSONArray roomsjson = buildingjson.getJSONArray("rooms");
                        List<Room> rooms = new ArrayList<>();
                        for (int j = 0 ; j < roomsjson.length() ; j++) {
                            Room room = new Room(new Light(), new Noise(), "");
                            JSONObject roomJSONObject = roomsjson.getJSONObject(j);
                            room.setId(Long.parseLong(roomJSONObject.getString("id")));

                            String room_name = roomJSONObject.getString("name");
                            JSONObject light = roomJSONObject.getJSONObject("light");
                            String light_status = light.getString("status");
                            String light_level = light.getString("level");

                            room.setName(room_name);
                            room.getLight().setId(Long.parseLong(light.getString("id")));
                            room.getLight().setLevel(Integer.parseInt(light_level));
                            if (light_status.equalsIgnoreCase("ON")) {
                                room.getLight().setStatus(Status.ON);
                            } else {
                                room.getLight().setStatus(Status.OFF);
                            }

                            JSONObject noise = roomJSONObject.getJSONObject("noise");
                            String noise_status = noise.getString("status");
                            String noise_level = noise.getString("level");

                            room.getNoise().setId(Long.parseLong(light.getString("id")));
                            room.getNoise().setLevel(Integer.parseInt(noise_level));

                            if (noise_status.equalsIgnoreCase("ON")) {
                                room.getNoise().setStatus(Status.ON);
                            } else {
                                room.getNoise().setStatus(Status.OFF);
                            }
                            rooms.add(room);
                        }
                        building.setRooms(rooms);
                        MainActivity.this.buildings.add(building);
                        MainActivity.this.setrefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        requestQueue = Volley.newRequestQueue(this);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        getbuildings();
    }

    private void setrefresh() {
        building = buildings.get(0);
        rooms = building.getRooms();
        room = rooms.get(0);
        setspinnerBuilding();
        set();
    }

    private void setspinnerRoom() {
        List<String> ids = new ArrayList<>();
        for (Room room : rooms) {
            ids.add(room.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, ids);
        spinnerRoom.setAdapter(arrayAdapter);

    }

    private void setspinnerBuilding() {
        List<String> buildings_names = new ArrayList<>();
        for (Building building : buildings) {
            buildings_names.add(building.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, buildings_names);
        spinnerBuilding.setAdapter(arrayAdapter);
    }

    public void setroomdata(String url) {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, response_room, errorListener);
        requestQueue.add(jsonObjectRequest);
    }

    public void set() {
        if (room.getLight().getStatus().equals(Status.ON)) {
            imageButtonLight.setImageResource(R.mipmap.light);
            lightlevel.setProgress(room.getLight().getLevel());
        } else {
            imageButtonLight.setImageResource(R.mipmap.lightoff);
            lightlevel.setProgress(0);
        }
        if (room.getNoise().getStatus().equals(Status.ON)) {
            imageButtonNoise.setImageResource(R.mipmap.soundon);
            noiselevel.setProgress(room.getNoise().getLevel());
        } else {
            imageButtonNoise.setImageResource(R.mipmap.soundoff);
            noiselevel.setProgress(0);
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
    }

    public void switchlight(View view) {
        if(room.getLight().getStatus().equals(Status.ON)) {
            room.getLight().setStatus(Status.OFF);
        } else {
            room.getLight().setStatus(Status.ON);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apirooms + room.getId() + "/switch-light", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String on = null;
                        try {
                            on = response.getString("status");
                            if(on.equalsIgnoreCase("ON")) {
                                MainActivity.this.room.getLight().setStatus(Status.ON);
                            } else {
                                MainActivity.this.room.getLight().setStatus(Status.OFF);
                            }
                            MainActivity.this.set();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, errorListener);
        requestQueue.add(jsonObjectRequest);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
    }

    public void switchnoise(View view) {
        if(room.getNoise().getStatus().equals(Status.ON)) {
            room.getNoise().setStatus(Status.OFF);
        } else {
            room.getNoise().setStatus(Status.ON);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apirooms + room.getId() + "/switch-noise", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String on = null;
                        try {
                            on = response.getString("status");
                            if(on.equalsIgnoreCase("ON")) {
                                MainActivity.this.room.getNoise().setStatus(Status.ON);
                            } else {
                                MainActivity.this.room.getNoise().setStatus(Status.OFF);
                            }
                            MainActivity.this.set();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, errorListener);
        requestQueue.add(jsonObjectRequest);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

    }

    public void getbuildings() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apibulidings + "/all", null, response_buildings, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    public void refresh(View view) {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        getbuildings();
    }
}
