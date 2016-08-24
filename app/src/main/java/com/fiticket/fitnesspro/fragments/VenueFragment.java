package com.fiticket.fitnesspro.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.VenueJson;
import com.fiticket.fitnesspro.pojos.VenueResponseJson;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by InFinItY on 11/26/2015.
 */
public class VenueFragment extends Fragment {
    private static final String TAG = VenueFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Context context;
    private EditText name;
    private EditText address;
    private EditText landmark;
    private EditText city;
    private EditText area;
    private EditText pincode;
    private EditText number;
    private EditText latitude;
    private EditText longitude;
    private EditText emailVenue;
    private VenueJson venueDetail;
    private boolean showVenueDetail;
    Button saveButton;
    TextView editButton;
    ProgressBar progressBar;
    private ImageView imageCreateVenue;
    SharedPreferences sPref;
    private int count;
    private String[] venueImageStrings;
    private int currentListItem;
    private int totalListLength;

    public void setVenueDetail(VenueJson venueDetail, int i, int position)
    {
        this.venueDetail=venueDetail;
        this.currentListItem=position;
        this.totalListLength=i;
    }
    public void setShowVenueDetail(boolean showVenueDetail)
    {
        this.showVenueDetail=showVenueDetail;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.venue_fragment, container, false);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        sPref=context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);

        progressBar=(ProgressBar)view.findViewById(R.id.progressCreateVenue);

        emailVenue=(EditText)view.findViewById(R.id.emailVenue);

        name=(EditText)view.findViewById(R.id.nameVenue);

        address=(EditText)view.findViewById(R.id.addressVenue);

        landmark=(EditText)view.findViewById(R.id.landmarkVenue);

        latitude=(EditText)view.findViewById(R.id.latitudeVenue);

        longitude=(EditText)view.findViewById(R.id.longitudeVenue);

        city=(EditText)view.findViewById(R.id.cityVenue);

        area =(EditText)view.findViewById(R.id.areaVenue);

        pincode=(EditText)view.findViewById(R.id.pincodeVenue);

        number=(EditText)view.findViewById(R.id.numberVenue);

        saveButton = (Button) view.findViewById(R.id.saveVenueButton);

        editButton = (TextView) view.findViewById(R.id.editbtnVenue);
        editButton.setTypeface(font);

        imageCreateVenue = (ImageView) view.findViewById(R.id.imageCreateVenue);

        if(showVenueDetail==true)
        {
            imageCreateVenue.setEnabled(false);
            saveButton.setVisibility(View.GONE);
            /*int size = sPref.getInt("array_size", 0);
            venueImageStrings = new String[size];
            for(int i=0; i<size; i++) {
                String imageResource=sPref.getString("array_" + i, null);
                if (i==currentListItem&&imageResource!=null) {

                    imageCreateVenue.setImageBitmap(decodeBase64(imageResource));

                }
            }*/
            if(((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_TEACHER)&&((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_EMPLOYEE))
            {
                editButton.setVisibility(View.VISIBLE);
            }
            name.setText(venueDetail.getVenueName());
            name.setTag(name.getKeyListener());
            name.setKeyListener(null);
            emailVenue.setText(venueDetail.getEmail());
            emailVenue.setTag(emailVenue.getKeyListener());
            emailVenue.setKeyListener(null);
            address.setText(venueDetail.getAddress());
            address.setTag(address.getKeyListener());
            address.setKeyListener(null);
            city.setText(venueDetail.getCity());
            city.setTag(city.getKeyListener());
            city.setKeyListener(null);
            latitude.setText("" + venueDetail.getLatitude());
            latitude.setTag(latitude.getKeyListener());
            latitude.setKeyListener(null);
            longitude.setText("" + venueDetail.getLongitude());
            longitude.setTag(longitude.getKeyListener());
            longitude.setKeyListener(null);
            landmark.setText(venueDetail.getLandmark());
            landmark.setTag(landmark.getKeyListener());
            landmark.setKeyListener(null);
            pincode.setText("" + venueDetail.getPinCode());
            pincode.setTag(pincode.getKeyListener());
            pincode.setKeyListener(null);
            area.setText(venueDetail.getArea());
            area.setTag(area.getKeyListener());
            area.setKeyListener(null);
            number.setText(venueDetail.getContactNumber());
            number.setTag(number.getKeyListener());
            number.setKeyListener(null);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageCreateVenue.setEnabled(true);
                    editButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                    name.setKeyListener((KeyListener) name.getTag());
                    emailVenue.setKeyListener((KeyListener)emailVenue.getTag());
                    landmark.setKeyListener((KeyListener) landmark.getTag());
                    latitude.setKeyListener((KeyListener) latitude.getTag());
                    longitude.setKeyListener((KeyListener) longitude.getTag());
                    address.setKeyListener((KeyListener) address.getTag());
                    city.setKeyListener((KeyListener) city.getTag());
                    area.setKeyListener((KeyListener) area.getTag());
                    pincode.setKeyListener((KeyListener) pincode.getTag());
                    number.setKeyListener((KeyListener) number.getTag());
                }
            });

        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean allvalid = true;
                if (name.getText().toString().length() == 0)
                {
                    name.setError("Name is required!");

                    allvalid = false;
                }
                else {
                    // do async task
                    name.setError(null);
                }
                if (emailVenue.getText().toString().length()==0)
                {
                    emailVenue.setError("Email required");
                    allvalid=false;
                }
                else
                {
                    emailVenue.setError(null);
                }
                if(address.getText().toString().length() == 0 ) {
                    address.setError("Address is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    address.setError(null);
                }
                if(landmark.getText().toString().length() == 0 ) {
                    landmark.setError("Landmark is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    landmark.setError(null);
                }
                if(city.getText().toString().length() == 0 ) {
                    city.setError("City is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    city.setError(null);
                }
                if(area.getText().toString().length() == 0 ) {
                    area.setError("State is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    area.setError(null);
                }
                if(number.getText().toString().length() == 0 ) {
                    number.setError("Mobile Number is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    number.setError(null);
                }
                if(pincode.getText().toString().length() == 0 ) {
                    pincode.setError("Pincode is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    pincode.setError(null);
                }
                if(allvalid)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    VenueJson createVenue=new VenueJson();
                    createVenue.setVenueName(name.getText().toString());
                    createVenue.setEmail(emailVenue.getText().toString());
                    createVenue.setAddress(address.getText().toString());
                    createVenue.setCity(city.getText().toString());
                    createVenue.setLandmark(landmark.getText().toString());
                    createVenue.setLatitude(Double.parseDouble(latitude.getText().toString()));
                    createVenue.setLongitude(Double.parseDouble(longitude.getText().toString()));
                    createVenue.setArea(area.getText().toString());
                    createVenue.setPinCode(Integer.parseInt(pincode.getText().toString()));
                    createVenue.setContactNumber(number.getText().toString());
                    int tenantId=sPref.getInt(Constant.USER_TENANT_ID,0);
                    createVenue.setTenantId(tenantId);


                    String url;
                    // To check if is editing(UPDATE) customer or adding(CREATE) customer
                    if(showVenueDetail)
                    {
                        createVenue.setVenueId(venueDetail.getVenueId());
                        url= Constant.UPDATE_VENUE_URL;
                    }
                    else
                    {
                        url=Constant.CREATE_VENUE_URL;
                    }
                    WebServices.sendPostRequest(context, url, createVenue, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "volley response:" + response.toString());
                            VenueResponseJson venueResponseJson=new Gson().fromJson(response.toString(), VenueResponseJson.class);
                            if (venueResponseJson.getStatusCode().equals(WebServices.SUCCESS_CODE) ){
                                Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                            else
                            {
                                Toast.makeText(context, venueResponseJson.getStatusMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "error occured:" + error.toString());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                            }

                        }
                    },VenueJson.class);
                    /*Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(createVenue, VenueJson.class));
                        //progressBar.setVisibility(View.VISIBLE);
                        triggerVolleyPostRequest(jsonObject,url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }


            }
        });

        /*imageCreateVenue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                *//*Intent intent = new Intent();
                intent.setType("image*//**//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*//*

                final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else if (items[which].equals("Choose from Library")) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image*//*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    SELECT_FILE);
                        } else if (items[which].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
            }
        });*/

        return view;
    }

    /*private Bitmap decodeBase64(String imageResource) {
        byte[] decodedByte = Base64.decode(imageResource, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }*/

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                *//*Bitmap photo = (Bitmap) data.getExtras().get("data");
                SavedImagesToSharedPref(photo);
                imageCreateVenue.setImageBitmap(photo);*//*
                Uri selectedImageUri = data.getData();
                //Bitmap bitmap=BitmapFactory.decodeFile(selectedImageUri);
                imageCreateVenue.setImageURI(selectedImageUri);
            }
            else if(requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                *//*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*



                SavedImagesToSharedPref(photo);
                imageCreateVenue.setImageBitmap(photo);

            }
        }
    }

    private void SavedImagesToSharedPref(Bitmap photo) {
        //count=venueDetail.getVenueId();
       *//* venueImageStrings=new String[totalListLength];
        for (int i=0;i<totalListLength;i++)
        {
            if (i==(currentListItem))
            {
                venueImageStrings[i]=encodeTobase64(photo);
            }
        }*//*

        venueImageStrings=new String[totalListLength];
        for (int i=0;i<totalListLength;i++)
        {
            if (i==currentListItem)
            {
                venueImageStrings[i]=encodeTobase64(photo);
            }
        }
        SharedPreferences.Editor editor= sPref.edit();
        editor.putInt("array_size", totalListLength);
        for(int i=0;i< venueImageStrings.length; i++){
            if (i==currentListItem)
                editor.putString("array_" + i, venueImageStrings[i]);
            break;
        }
        editor.commit();
    }

    private String encodeTobase64(Bitmap photo) {
        Bitmap immage = photo;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }*/


}