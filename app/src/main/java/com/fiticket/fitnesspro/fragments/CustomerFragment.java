package com.fiticket.fitnesspro.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.CustomerJson;
import com.fiticket.fitnesspro.pojos.CustomerResponseJson;
import com.fiticket.fitnesspro.utils.Utilities;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by InFinItY on 11/27/2015.
 */
public class CustomerFragment extends Fragment implements Animation.AnimationListener {
    private static final String TAG = CustomerFragment.class.getSimpleName();
    private static final String MALE = "male";
    private static final String FEMALE = "female";
    private EditText nameCustomer;
    ImageView photoCustomer;
    private Context context;
    private ListView customerClassListView;
    private Button cancelButton;
    private CustomerJson customerDetail;
    private boolean showCustomerDetail;
    TextView editButton;
    Button saveButton;
    ImageView manButton,womanButton;
    private ImageView addImageButton;
    private ClassJson classDetail;
    private boolean showClaassDetail;
    private TextView className;
    String genderSelection;


    boolean genderIconSelection;

    private DatePickerDialog dobDatePickerDialog;
    int selectedItem;
    TextView mrMs;

    EditText name,dob,email,address,city,state,pincode,number;
    Animation animFadein;
    Animation animFadeout;

    int spinnerSelect;
    private SharedPreferences sPref;
    private Calendar calDob;
    private boolean dobSelected;
    private Long dobEpochValue;
    ProgressBar customerProgressBar;

    public void setSelectedClassDetail(ClassJson classDetail)
    {
        this.classDetail=classDetail;
        if(classDetail!=null)
        {
            className.setText(classDetail.getClassName());
        }
    }

    public void setCustomerDetail(CustomerJson customerDetail)
    {
        this.customerDetail=customerDetail;
    }
    public void setShowCustomerDetail(boolean showCustomerDetail)
    {
        this.showCustomerDetail=showCustomerDetail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        sPref=context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.customers_fragment, container, false);
        //mrMs=(TextView)view.findViewById(R.id.MrMs);+
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        customerProgressBar=(ProgressBar)view.findViewById(R.id.customerProgressBar);
        manButton=(ImageView) view.findViewById(R.id.btn1);
        womanButton=(ImageView) view.findViewById(R.id.btn2);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_in);

        // set animation listener
        animFadein.setAnimationListener(this);

        // load the animation
        animFadeout = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.fade_out);

        // set animation listener
        animFadeout.setAnimationListener(this);

        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*mrMs.setText(R.string.mr);*/
                manButton.startAnimation(animFadein);
                womanButton.startAnimation(animFadeout);
                genderIconSelection=true;
                genderSelection=MALE;
            }
        });
        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /* mrMs.setText(R.string.ms);*/
                genderIconSelection=true;
                womanButton.startAnimation(animFadein);
                manButton.startAnimation(animFadeout);
                genderSelection=FEMALE;
            }
        });


        name=(EditText)view.findViewById(R.id.nameCustomer);

        dob=(EditText)view.findViewById(R.id.dobCustomer);
        dob.setInputType(InputType.TYPE_NULL);

        /*addImageButton=(ImageView)view.findViewById(R.id.addImageButton);*/

        email=(EditText)view.findViewById(R.id.emailCustomer);

        address=(EditText)view.findViewById(R.id.addressVenue);

        city=(EditText)view.findViewById(R.id.cityCustomer);

        /*state=(EditText)view.findViewById(R.id.stateCustomer);

        pincode=(EditText)view.findViewById(R.id.pincodeCustomer);*/

        number=(EditText)view.findViewById(R.id.numberCustomer);


        editButton=(TextView)view.findViewById(R.id.editCustomerlistButton);
        editButton.setTypeface(font);

        saveButton = (Button) view.findViewById(R.id.addCustomerButton);

        /*className=(TextView)view.findViewById(R.id.ClassName);*/



        if (showCustomerDetail==true)
        {
            saveButton.setVisibility(View.GONE);
            SharedPreferences sPref=context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
            if(((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_TEACHER)&&((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_EMPLOYEE))
            {
                editButton.setVisibility(View.VISIBLE);
            }
            if((customerDetail.getGender())!=null)
            {
                if((customerDetail.getGender()).equals("male"))
                {
                    manButton.startAnimation(animFadein);
                    womanButton.startAnimation(animFadeout);
                    womanButton.setActivated(false);
                }
                else if((customerDetail.getGender()).equals("female"))
                {
                    womanButton.startAnimation(animFadein);
                    manButton.startAnimation(animFadeout);
                    manButton.setActivated(false);
                }
                else
                {
                    manButton.startAnimation(animFadeout);
                    womanButton.startAnimation(animFadeout);
                    womanButton.setActivated(false);
                }
            }

            name.setText(customerDetail.getCustomerName());
            name.setTag(name.getKeyListener());
            name.setKeyListener(null);
            dobEpochValue=customerDetail.getDob();
            dob.setText(Utilities.convertEpochToDate(dobEpochValue));
            dob.setTag(dob.getKeyListener());
            dob.setKeyListener(null);

            email.setText(customerDetail.getEmail());
            email.setTag(email.getKeyListener());
            email.setKeyListener(null);
            address.setText(customerDetail.getAddress());
            address.setTag(address.getKeyListener());
            address.setKeyListener(null);
            city.setText(customerDetail.getCity());
            city.setTag(city.getKeyListener());
            city.setKeyListener(null);

            number.setText(customerDetail.getContactNumber());
            number.setTag(number.getKeyListener());
            number.setKeyListener(null);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                    name.setKeyListener((KeyListener) name.getTag());
                    dob.setKeyListener((KeyListener) dob.getTag());
                    email.setKeyListener((KeyListener) email.getTag());
                    address.setKeyListener((KeyListener) address.getTag());
                    city.setKeyListener((KeyListener) city.getTag());
                    womanButton.setActivated(true);
                    manButton.setActivated(true);
                   /* state.setKeyListener((KeyListener) state.getTag());
                    pincode.setKeyListener((KeyListener) pincode.getTag());*/
                    number.setKeyListener((KeyListener) number.getTag());
                }
            });
        }

        /*String [] values =
                {"Mr.","Ms."};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> list = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_list,R.id.spinnerText, values);
        spinner.setAdapter(list);*/

        /*addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });*/



        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobSelected=true;
                final Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                dobDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        final Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, selectedyear);
                        c.set(Calendar.MONTH, selectedmonth);
                        c.set(Calendar.DAY_OF_MONTH, selectedday);
                        if (selectedyear<mYear) {
                            calDob = c;
                            java.text.DateFormat f = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                            dob.setText(f.format(c.getTime()));
                        }
                        else
                        {
                            Toast.makeText(context,"Enter a valid date",Toast.LENGTH_LONG).show();
                        }

                        // Date date = c.getTime();
                        // editDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
                dobDatePickerDialog.setTitle("Select date");
                dobDatePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean allvalid = true;

                if(genderIconSelection==false)
                {
                    Toast.makeText(context,"Select gender",Toast.LENGTH_SHORT).show();
                    allvalid = false;
                }
                if (name.getText().toString().length() == 0) {
                    name.setError("Name is required!");
                    allvalid = false;
                } else {
                    // do async task
                    name.setError(null);
                }
                if (dob.getText().toString().length() == 0) {
                    dob.setError("DOB is required!");
                    allvalid = false;
                } else {
                    // do async task
                    dob.setError(null);
                }
                if (email.getText().toString().length() == 0) {
                    allvalid = false;
                    email.setError("Email is required!");
                } else if (!isValidEmail(email.getText().toString())) {
                    // do async task
                    email.setError("Enter valid Email!");
                    allvalid = false;

                } else {
                    email.setError(null);
                }
                if (number.getText().toString().length() == 0) {
                    allvalid = false;
                    number.setError("Mobile Number is required!");
                } else {
                    // do async task
                    number.setError(null);
                }

                if (allvalid) {
                    customerProgressBar.setVisibility(View.VISIBLE);
                    CustomerJson createCustomer = new CustomerJson();
                    createCustomer.setGender(genderSelection);
                    createCustomer.setCustomerName(name.getText().toString());

                    createCustomer.setEmail(email.getText().toString());
                    createCustomer.setAddress(address.getText().toString());
                    createCustomer.setCity(city.getText().toString());
                    createCustomer.setContactNumber(number.getText().toString());
                    createCustomer.setTenantId(sPref.getInt(Constant.USER_TENANT_ID,0));


                    String url;
                    // To check if is editing(UPDATE) customer or adding(CREATE) customer
                    if(showCustomerDetail)
                    {
                        if (dobSelected==true)
                        {
                            createCustomer.setDob(calDob.getTimeInMillis());
                        }
                        else
                        {
                            createCustomer.setDob(dobEpochValue);
                        }
                        createCustomer.setCustomerId(customerDetail.getCustomerId());
                        url=Constant.UPDATE_CUSTOMER_URL;
                    }
                    else {
                        createCustomer.setDob(calDob.getTimeInMillis());
                        url=Constant.CREATE_CUSTOMER_URL;
                    }
                    /*Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(createCustomer, CustomerJson.class));
                        progressBar.setVisibility(View.VISIBLE);*/
                        triggerVolleyPostRequest(createCustomer,url);
                    /*} catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                }

            }
        });

        return view;
    }
    /*void showDialog() {
        DialogFragment newFragment=new CustomerClassSelectionFragment();
        newFragment.show(getActivity().getFragmentManager(),"dialog");
    }*/

    private boolean isValidEmail(String text) {
        if(text.contains("@")){
            return true;
        }
        return false;
    }

    private void triggerVolleyPostRequest(CustomerJson json,String url) {
        WebServices.sendPostRequest(context, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customerProgressBar.setVisibility(View.GONE);
                CustomerResponseJson responseJson = new Gson().fromJson(response.toString(), CustomerResponseJson.class);
                if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "volley response:" + response.toString());
                    Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customerProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "error occured:" + error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        },CustomerJson.class);
        /*Log.d(TAG,"Json"+json.toString());
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,url , json,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG,"volley response:"+response.toString());
                Toast.makeText(context,"added successfully",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "error occured:" + error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof AuthFailureError) {
                    //TODO

                }
                else if (error instanceof ServerError) {
                    //TODO

                }
                else if (error instanceof NetworkError) {
                    //TODO

                }
                else if (error instanceof ParseError) {
                    //TODO

                }
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);*/
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}