package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.fragments.VenueDialogFragment.AddVenueClickListener;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;
import com.fiticket.fitnesspro.pojos.CreateClassResponse;
import com.fiticket.fitnesspro.pojos.VenueJson;
import com.fiticket.fitnesspro.singleton.MySingleton;
import com.fiticket.fitnesspro.utils.Utilities;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by InFinItY on 11/16/2015.
 */
public class FragmentCreateClass extends android.support.v4.app.Fragment implements AddVenueClickListener{

    private static final int SELECT_PICTURE = 1;
    private static final int INVALID_HOUR = 25;
    private static final int INVALID_MINUTE = 61;
    private int startHour=INVALID_HOUR;
    private int startMinute=INVALID_MINUTE;
    private static final String TAG = FragmentCreateClass.class.getSimpleName();
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA = 1;
    private String selectedDate;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText startTime;
    private EditText endTime;
    private EditText name,price,maxCapacity;
    private TextView location;
    private NetworkImageView imageCreateClass;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private ImageButton addBtn;
    private Bitmap bitmap;
    Context context;

    private Button createButton;
    private TextView editButton;
    private Spinner categorySpinner;
    Calendar startCal, endCal;
    ProgressBar progressBar;
    private ClassJson classDetail;
    private boolean showClaassDetail;
    private TextView addVenueButton;
    private VenueJson venues;
    Spinner spinner;
    int selectedItem;
    String venuename;
    int classid;
    int venueId;
    onSaveButtonClicked mCallBack;
    private EditText classDesc;
    AppCompatActivity parentActivity;
    private RelativeLayout addVenue;
    //private TextView addSchedule;
    private boolean isUpdate;
    private ClassTypeJson[] classTypes;
    ImageLoader imageLoader;
    private boolean fromDateSelected;
    private boolean toDateSelected;
    private Long startDateEpoch;
    private Long endDateEpoch;
    private LinearLayout classtypeSpinnerView;
    private LinearLayout classTypeView;
    private EditText typeOfClass;

    public void setClassTypes(ClassTypeJson[] classTypes) {
        this.classTypes = classTypes;
    }

    @Override
    public void onVenueClick(VenueJson venues) {
        this.venues=venues;
        if(venues!=null)
        {
            location.setText(venues.getVenueName());
            venueId=venues.getVenueId();
        }
    }

    public interface onSaveButtonClicked
    {
        void showScheduleDialog(int classid);
    }


    public void setClassDetail(ClassJson classDetail, ClassTypeJson[] classTypes) {
        this.classDetail = classDetail;
        this.classTypes=classTypes;
    }
    public void setShowClaassDetail(boolean showClaassDetail) {
        this.showClaassDetail = showClaassDetail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {

            mCallBack=(onSaveButtonClicked)getActivity();
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ "must implement onButtonClicked");
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*selectedDate=getArguments().getString(MainActivity.DATE_STRING);*/
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            final View view= inflater.inflate(R.layout.fragment_create_class, container, false);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        imageCreateClass=(NetworkImageView) view.findViewById(R.id.imageCreateClass);

        /*addSchedule=(Button)view.findViewById(R.id.addSchedule);
        deleteSchedule=(Button)view.findViewById(R.id.deleteSchedule);*/

        /*String [] values =
                {"Add location"};

        ArrayAdapter<String> list = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_list, values);
        list.setDropDownViewResource(R.layout.spinner_list);
        spinner.setAdapter(list);*/
        classtypeSpinnerView=(LinearLayout)view.findViewById(R.id.classtypeSpinnerView);
        classTypeView=(LinearLayout)view.findViewById(R.id.classtypeView);
        typeOfClass=(EditText)view.findViewById(R.id.typeOfClass);

        addVenue=(RelativeLayout)view.findViewById(R.id.addVenue);

        spinner = (Spinner) view.findViewById(R.id.spinner1);

        classDesc=(EditText)view.findViewById(R.id.classDesc);

        addVenueButton=(TextView)view.findViewById(R.id.addVenueButton);


        name=(EditText)view.findViewById(R.id.nameCreateClass);

        progressBar=(ProgressBar)view.findViewById(R.id.createClassProgressBar);

        location=(TextView)view.findViewById(R.id.locationClass);

        price=(EditText)view.findViewById(R.id.priceCreateClass);

        maxCapacity=(EditText)view.findViewById(R.id.maximumCapacityCreateClass);

        fromDateEtxt = (EditText) view.findViewById(R.id.startDateText);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        //fromDateEtxt.setText(selectedDate);
        //addSchedule=(TextView)view.findViewById(R.id.addSchedule);


        editButton=(TextView)view.findViewById(R.id.createClassEditBtn);
        editButton.setTypeface(font);

        createButton = (Button) view.findViewById(R.id.saveCreateClassButton);

        toDateEtxt = (EditText) view.findViewById(R.id.endDateText);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        categorySpinner=(Spinner)view.findViewById(R.id.categorySpinner);

        if(showClaassDetail==true)
        {
            classTypeView.setVisibility(View.VISIBLE);
            classtypeSpinnerView.setVisibility(View.INVISIBLE);
            createButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            addVenueButton.setVisibility(View.GONE);
            addVenue.setEnabled(false);
            name.setText(classDetail.getClassName());
            name.setTag(name.getKeyListener());
            name.setKeyListener(null);

            typeOfClass.setText(classDetail.getClassType());
            typeOfClass.setTag(typeOfClass.getKeyListener());
            typeOfClass.setKeyListener(null);

            classDesc.setText(classDetail.getClassDesc());
            classDesc.setTag(classDesc.getKeyListener());
            classDesc.setKeyListener(null);

            startDateEpoch=classDetail.getStartDate();
            fromDateEtxt.setText(Utilities.convertEpochToDate(startDateEpoch));
            fromDateEtxt.setTag(fromDateEtxt.getKeyListener());
            fromDateEtxt.setKeyListener(null);

            endDateEpoch=classDetail.getEndDate();
            toDateEtxt.setText(Utilities.convertEpochToDate(endDateEpoch));
            toDateEtxt.setTag(toDateEtxt.getKeyListener());
            toDateEtxt.setKeyListener(null);

            price.setText("" + classDetail.getPrice());
            location.setText("" + classDetail.getVenueName());
            price.setTag(price.getKeyListener());
            price.setKeyListener(null);
            maxCapacity.setText("" + classDetail.getMaxCapacity());
            maxCapacity.setTag(maxCapacity.getKeyListener());
            maxCapacity.setKeyListener(null);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editButton.setVisibility(View.GONE);
                    classTypeView.setVisibility(View.INVISIBLE);
                    classtypeSpinnerView.setVisibility(View.VISIBLE);
                    addVenueButton.setVisibility(View.VISIBLE);
                    createButton.setVisibility(View.VISIBLE);
                    addVenueButton.setVisibility(View.VISIBLE);
                    name.setKeyListener((KeyListener) name.getTag());
                    addVenue.setEnabled(true);
                    /*startTime.setKeyListener((KeyListener) startTime.getTag());
                    endTime.setKeyListener((KeyListener) endTime.getTag());*/
                    categorySpinner.setEnabled(true);
                    classDesc.setKeyListener((KeyListener) classDesc.getTag());
                    fromDateEtxt.setKeyListener((KeyListener) fromDateEtxt.getTag());
                    toDateEtxt.setKeyListener((KeyListener) toDateEtxt.getTag());
                    price.setKeyListener((KeyListener) price.getTag());
                    maxCapacity.setKeyListener((KeyListener) maxCapacity.getTag());
                }
            });
            /*setScheduleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeSlotsDialog(classDetail.getClassId());

                }
            });*/

        }




        addVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVenueDialog();
            }
        });



        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDateSelected =true;
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                final Calendar fromcurrentDate = Calendar.getInstance();
                int mYear = fromcurrentDate.get(Calendar.YEAR);
                int mMonth = fromcurrentDate.get(Calendar.MONTH);
                int mDay = fromcurrentDate.get(Calendar.DAY_OF_MONTH);

                fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        final Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, selectedyear);
                        c.set(Calendar.MONTH, selectedmonth);
                        c.set(Calendar.DAY_OF_MONTH, selectedday);
                        startCal = c;

                        java.text.DateFormat f = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                        if (c.before(fromcurrentDate)) {
                            Toast.makeText(context, getResources().getString(R.string.wrong_date_warning), Toast.LENGTH_SHORT).show();
                        } else {
                            fromDateEtxt.setText(f.format(c.getTime()));
                        }


                        // Date date = c.getTime();
                        // editDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
                fromDatePickerDialog.setTitle("Select date");
                fromDatePickerDialog.show();
                fromDatePickerDialog.setCanceledOnTouchOutside(false);
            }
        });
        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDateSelected=true;
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        final Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, selectedyear);
                        c.set(Calendar.MONTH, selectedmonth);
                        c.set(Calendar.DAY_OF_MONTH, selectedday);
                        endCal=c;
                        java.text.DateFormat f = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                        if (c.before(mcurrentDate) && c.before(startCal)) {
                            Toast.makeText(context, getResources().getString(R.string.wrong_date_warning), Toast.LENGTH_SHORT).show();
                        } else {
                            toDateEtxt.setText(f.format(c.getTime()));
                        }
                        // Date date = c.getTime();
                        // editDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
                toDatePickerDialog.setTitle("Select date");
                toDatePickerDialog.show();
                toDatePickerDialog.setCanceledOnTouchOutside(false);
            }
        });


//        imageCreateClass.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                /*Intent intent = new Intent();
//                intent.setType("image*//*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*/
//
//                final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Add Photo!");
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (items[which].equals("Take Photo")) {
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, REQUEST_CAMERA);
//                        } else if (items[which].equals("Choose from Library")) {
//                            Intent intent = new Intent(
//                                    Intent.ACTION_PICK,
//                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            intent.setType("image/*");
//                            startActivityForResult(
//                                    Intent.createChooser(intent, "Select File"),
//                                    SELECT_FILE);
//                        } else if (items[which].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//
//                    }
//                });
//                builder.show();
//            }
//        });

        createButton.setOnClickListener(new View.OnClickListener() {
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
                if(classDesc.getText().toString().length()==0)
                {
                    classDesc.setError("class description required");
                    allvalid=false;
                }
                else
                {
                    classDesc.setError(null);
                }
                if(location.getText().toString().length() == 0 ) {
                    location.setError("Address is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    location.setError(null);
                }
                if(fromDateEtxt.getText().toString().length() == 0 ) {
                    fromDateEtxt.setError("Landmark is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    fromDateEtxt.setError(null);
                }
                if(toDateEtxt.getText().toString().length() == 0 ) {
                    toDateEtxt.setError("City is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    toDateEtxt.setError(null);
                }
                /*if(startTime.getText().toString().length() == 0 ) {
                    startTime.setError("State is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    startTime.setError(null);
                }*/
                /*if(endTime.getText().toString().length() == 0 ) {
                    endTime.setError("Mobile Number is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    endTime.setError(null);
                }*/
                if(maxCapacity.getText().toString().length() == 0 ) {
                    maxCapacity.setError("Pincode is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    maxCapacity.setError(null);
                }
                if(price.getText().toString().length() == 0 ) {
                    price.setError("Pincode is required!");
                    allvalid = false;
                }
                else {
                    // do async task
                    price.setError(null);
                }
                if(allvalid)
                {
                    progressBar.setVisibility(View.VISIBLE);

                    ClassJson createClass=new ClassJson();
                    createClass.setClassName(name.getText().toString());
                    createClass.setClassDesc(classDesc.getText().toString());

                    createClass.setPrice(Integer.parseInt(price.getText().toString()));
                    createClass.setMaxCapacity(Integer.parseInt(maxCapacity.getText().toString()));
                    createClass.setVenueId(venueId);
                    int classTypeId=categorySpinner.getSelectedItemPosition();
                    createClass.setClassTypeId(classTypeId+1);
                    String url;
                    // To check if is editing(UPDATE) customer or adding(CREATE) customer
                    if(showClaassDetail)
                    {
                        if (fromDateSelected ==true)
                        {
                            createClass.setStartDate(startCal.getTimeInMillis());

                        }
                        else {
                            //convertDateToEpoch(fromDateEtxt.getText().toString());
                            createClass.setStartDate(startDateEpoch);
                        }
                        if (toDateSelected==true)
                        {
                            createClass.setEndDate(endCal.getTimeInMillis());

                        }
                        else {
                            //convertDateToEpoch(toDateEtxt.getText().toString());
                            createClass.setEndDate(endDateEpoch);
                        }
                        createClass.setClassId(classDetail.getClassId());
                        url= Constant.UPDATE_CLASS_URL;
                    }
                    else
                    {
                        createClass.setStartDate(startCal.getTimeInMillis());
                        createClass.setEndDate(endCal.getTimeInMillis());
                        url=Constant.CREATE_CLASS_URL;
                    }
                    WebServices.sendPostRequest(parentActivity, url, createClass, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "volley response:" + response.toString());
                            CreateClassResponse responseJson = new Gson().fromJson(response.toString(), CreateClassResponse.class);
                            if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                                Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();

                                if (showClaassDetail != true) {
                                    Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                    showCreateTimeSlotsDialog(responseJson.getData().getClassId());
                                } else {
                                    Toast.makeText(context, "added successfully", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }

                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "error occured:" + error.toString());
                            error.printStackTrace();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, ClassJson.class);
                }
            }
        });
        return view;
    }

    /*private void convertDateToEpoch(String s) {
        String str = s;
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
        Date date= null;
        try {
            date = sdf.parse(str);
            epoch = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/


    private void showVenueDialog() {
        VenueDialogFragment newFragment=new VenueDialogFragment();
        newFragment.setVenueSelectedListener(this);
        newFragment.show(parentActivity.getFragmentManager(),"dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                imageCreateClass.setImageURI(selectedImageUri);
            }
            else if(requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
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
                }*/
                imageCreateClass.setImageBitmap(photo);
            }
        }
    }
    /*private void showTimeSlotsDialog(int classId) {
        UpdateScheduleFragment newFragment=new UpdateScheduleFragment();
        newFragment.setCreatedClassId(classId);
        newFragment.setShowScheduleDetail(true);
        newFragment.show(getActivity().getFragmentManager(),"dialog");
    }*/
    private void showCreateTimeSlotsDialog(int classId) {
        CreateScheduleFragment newFragment=new CreateScheduleFragment();
        newFragment.setCreatedClassId(classId);
        newFragment.show(parentActivity.getFragmentManager(),"dialog");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=(AppCompatActivity)getActivity();
        Typeface font = Typeface.createFromAsset(parentActivity.getAssets(), "fontawesome-webfont.ttf");
        imageLoader = MySingleton.getInstance(parentActivity).getImageLoader();

        String[] categories=new String[classTypes.length];
        addVenueButton.setTypeface(font);
        for(int i=0;i<classTypes.length;i++)
        {

            categories[i]=classTypes[i].getClassTypeName();
            ArrayAdapter<String> categoryAdapter= new ArrayAdapter<String>(parentActivity,android.R.layout.simple_spinner_item,
                    categories);
            categorySpinner.setAdapter(categoryAdapter);

        }
        //String[] categories= MySingleton.getInstance(parentActivity).getClassTypes();

        if(showClaassDetail){
            int selected=classDetail.getClassTypeId();
            categorySpinner.setSelection(selected-1);
            categorySpinner.setEnabled(false);
        }
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String url;
                url = classTypes[position].getClassTypeImgURL();
                imageCreateClass.setImageUrl(url, imageLoader);
                /*int tempClassTypeId = classDetail.getClassTypeId();

                for (int i = 0; i < classTypes.length; i++) {
                    if (classTypes[i].getClassTypeId() == tempClassTypeId) {
                        break;
                    }
                }*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
}