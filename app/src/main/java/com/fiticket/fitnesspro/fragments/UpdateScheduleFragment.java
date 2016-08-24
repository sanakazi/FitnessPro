package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.CreateTimeSlotResponse;
import com.fiticket.fitnesspro.pojos.DeleteTimeslotResponseJson;
import com.fiticket.fitnesspro.pojos.GetTimeSlots;
import com.fiticket.fitnesspro.pojos.TimeslotJson;
import com.fiticket.fitnesspro.pojos.TimeslotList;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by InFinItY on 1/18/2016.
 */
public class UpdateScheduleFragment extends Fragment {
    private static final String TAG = UpdateScheduleFragment.class.getSimpleName();
    private EditText startTimeText;/*,startTimeText2,startTimeText3,startTimeText4,startTimeText5,startTimeText6*/
    private EditText endTimeText;/*,endTimeText2,endTimeText3,endTimeText4,endTimeText5,endTimeText6;*/
    private CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    private Button deleteSchedule,updateSchedule;
    private Button[] updateButtonArray=new Button[7];
    int slotCount =0;
    private LinearLayout[] dayLayouts;
    private int classid;
    private static final int INVALID_HOUR = 25;
    private static final int INVALID_MINUTE = 61;
    private int startHour=INVALID_HOUR;
    private int startMinute=INVALID_MINUTE;
    private Button addSlots;
    byte daysOfWeek=0b00000000;
    byte getDaysOfWeek;
    private static final byte SUNDAY=   0b00000001;
    private static final byte MONDAY=   0b00000010;
    private static final byte TUESDAY=  0b00000100;
    private static final byte WEDNESDAY=0b00001000;
    private static final byte THURSDAY= 0b00010000;
    private static final byte FRIDAY=   0b00100000;
    private static final byte SATURDAY= 0b01000000;
    private String url;
    Activity parentActivity;
    private boolean showScheduleDetail;
    ArrayList<TimeslotJson> timeslotList;
    private LinearLayout activeTimeSlot;
    private int timeSlotId;
    private EditText[] startTimeTxtArray=new EditText[7];
    private EditText[] endTimeTxtArray=new EditText[7];
    private ArrayList<LinearLayout> addedSlotsArray;
    private Button[] deleteButtonArray=new Button[7];
    private LinearLayout titleblue;


    public void setCreatedClassId(int classid) {
        this.classid=classid;
    }
    public void setShowScheduleDetail(boolean showScheduleDetail) {
        this.showScheduleDetail = showScheduleDetail;
    }

    static UpdateScheduleFragment newInstance()
    {
        UpdateScheduleFragment c = new UpdateScheduleFragment();
        return c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.schedule, container, false);
        titleblue=(LinearLayout)view.findViewById(R.id.titleblue);
        titleblue.setVisibility(View.GONE);
        dayLayouts= new LinearLayout[7];
        addedSlotsArray=new ArrayList<>();
        dayLayouts[0]=(LinearLayout)view.findViewById(R.id.slot1);
        dayLayouts[1]=(LinearLayout)view.findViewById(R.id.slot2);
        dayLayouts[2]=(LinearLayout)view.findViewById(R.id.slot3);
        dayLayouts[3]=(LinearLayout)view.findViewById(R.id.slot4);
        dayLayouts[4]=(LinearLayout)view.findViewById(R.id.slot5);
        dayLayouts[5]=(LinearLayout)view.findViewById(R.id.slot6);
        dayLayouts[6]=(LinearLayout)view.findViewById(R.id.slot7);
        updateSchedule=(Button)view.findViewById(R.id.updateSchedule);
        updateSchedule.setVisibility(View.GONE);

        addSlots=(Button)view.findViewById(R.id.addSlots);
        /*deleteSlots=(Button)view.findViewById(R.id.deleteSlots);*/

        //deleteSchedule=(Button)view.findViewById(R.id.deleteSchedule);



        for(int i=0;i<dayLayouts.length;i++){
            final int j=i;
            startTimeTxtArray[i]=(EditText)dayLayouts[i].findViewById(R.id.startTimeText);
            startTimeTxtArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setEndTime(startTimeTxtArray[j]);
                    //setStartTime(endTimeTxtArray[j]);
                    setStartTime(startTimeTxtArray[j]);
                }
            });

            endTimeTxtArray[i]=(EditText)dayLayouts[i].findViewById(R.id.endTimeText);
            endTimeTxtArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEndTime(endTimeTxtArray[j]);
                    //setEndTime(startTimeTxtArray[j]);
                    //setStartTime(endTimeTxtArray[j]);
                }
            });
            deleteButtonArray[i]=(Button)dayLayouts[i].findViewById(R.id.deleteSlotBtn);
            deleteButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(j);
                }
            });
        }


        updateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateButtonClick();
            }
        });

        addSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotCount < 7) {
                    slotCount++;
                    activeTimeSlot = dayLayouts[slotCount - 1];
                    activeTimeSlot.setVisibility(View.VISIBLE);
                    addedSlotsArray.add(activeTimeSlot);
                    updateSchedule.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(parentActivity, "maximum number of slots allowed is 7", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    private void onDeleteClicked(final int j) {
        if(j<timeslotList.size()) {
            String deleteUrl = Constant.DELETE_TIMESLOT_URL + timeslotList.get(j).getTimeslotId();
            WebServices.triggerVolleyDeleteRequest(parentActivity, deleteUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG,"Delete time slot response:"+response);
                    DeleteTimeslotResponseJson json= new Gson().fromJson(response,DeleteTimeslotResponseJson.class);
                    if(json.getStatusCode().equals("0")) {
                        getScheduleDetails(url);
                        dayLayouts[j].setVisibility(View.GONE);
                        findSlotViews(dayLayouts[j]);
                        startTimeText.setText("");
                        endTimeText.setText("");
                        setDaysUnchecked(dayLayouts[j]);
                        Toast.makeText(parentActivity,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(parentActivity,"Delete Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        }else{
            dayLayouts[j].setVisibility(View.GONE);
            findSlotViews(dayLayouts[j]);
            startTimeText.setText("");
            endTimeText.setText("");
            setDaysUnchecked(dayLayouts[j]);
            slotCount--;
            addedSlotsArray.remove(dayLayouts[j]);
            if(slotCount<1)
                updateSchedule.setVisibility(View.GONE);
        }

    }

    private void setDaysUnchecked(LinearLayout activeTimeSlot) {
        findSlotViews(activeTimeSlot);
        monday.setChecked(false);
        sunday.setChecked(false);
        tuesday.setChecked(false);
        wednesday.setChecked(false);
        thursday.setChecked(false);
        friday.setChecked(false);
        saturday.setChecked(false);

    }

    private void onUpdateButtonClick() {
        boolean allvalid = true;
        for (int i=0;i<slotCount;i++)
        {
            activeTimeSlot=dayLayouts[i];
            findSlotViews(activeTimeSlot);
            calculateDaysOfWeek(activeTimeSlot);
            if (startTimeText.getText().toString().length() == 0)
            {
                startTimeText.setError("Start time is required!");
                allvalid = false;
            }
            else {
                // do async task
                startTimeText.setError(null);
            }
            if(endTimeText.getText().toString().length()==0)
            {
                endTimeText.setError("End time is required");
                allvalid=false;
            }
            else
            {
                endTimeText.setError(null);
            }
            if(daysOfWeek==0)
            {
                Toast.makeText(parentActivity,"Choose atleast one day",Toast.LENGTH_LONG).show();
                allvalid=false;
            }
        }
        if (allvalid)
        {
            for(int i=0;i<timeslotList.size();i++) {
                findSlotViews(dayLayouts[i]);
                calculateDaysOfWeek(dayLayouts[i]);
                timeslotList.get(i).setStartTime(startTimeText.getText().toString());
                timeslotList.get(i).setClassId(classid);
                timeslotList.get(i).setEndTime(endTimeText.getText().toString());
                timeslotList.get(i).setDaysOfTheWeek(daysOfWeek);
                timeslotList.get(i).setTimeslotId(timeslotList.get(i).getTimeslotId());
            }
            url = Constant.UPDATE_SCHEDULE_URL;
            triggerPostVolleyRequest(url, timeslotList);
            if(!(addedSlotsArray.isEmpty()))
            {
                ArrayList<TimeslotJson> timeSlotArray= new ArrayList<>();
                for(int i=0;i<addedSlotsArray.size();i++) {
                    findSlotViews(addedSlotsArray.get(i));
                    calculateDaysOfWeek(addedSlotsArray.get(i));
                    TimeslotJson timeslotJson= new TimeslotJson();
                    timeslotJson.setStartTime(startTimeText.getText().toString());
                    timeslotJson.setClassId(classid);
                    timeslotJson.setEndTime(endTimeText.getText().toString());
                    timeslotJson.setDaysOfTheWeek(daysOfWeek);
                    timeSlotArray.add(timeslotJson);
                }
                url = Constant.CREATE_SCHEDULE_URL;
                triggerPostVolleyRequest(url, timeSlotArray);
            }
        }
    }

    private void calculateDaysOfWeek(LinearLayout activeTimeSlot) {
        findSlotViews(activeTimeSlot);
        daysOfWeek= sunday.isChecked()?(byte) (daysOfWeek|SUNDAY):daysOfWeek;
        daysOfWeek= monday.isChecked()?(byte) (daysOfWeek|MONDAY):daysOfWeek;
        daysOfWeek= tuesday.isChecked()?(byte) (daysOfWeek|TUESDAY):daysOfWeek;
        daysOfWeek= wednesday.isChecked()?(byte) (daysOfWeek|WEDNESDAY):daysOfWeek;
        daysOfWeek= thursday.isChecked()?(byte) (daysOfWeek|THURSDAY):daysOfWeek;
        daysOfWeek= friday.isChecked()?(byte) (daysOfWeek|FRIDAY):daysOfWeek;
        daysOfWeek= saturday.isChecked()?(byte) (daysOfWeek|SATURDAY):daysOfWeek;
    }

    private void triggerPostVolleyRequest(String url,ArrayList<TimeslotJson> timeSlotArray) {
        TimeslotList tjson=new TimeslotList();
        tjson.setTimeslotList(timeSlotArray);
        WebServices.sendPostRequest(parentActivity, url, tjson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "volley response:" + response.toString());
                CreateTimeSlotResponse responseJson = new Gson().fromJson(response.toString(), CreateTimeSlotResponse.class);
                if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    Toast.makeText(parentActivity, "Updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "error occured:" + error.toString());
                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(parentActivity, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, TimeslotList.class);
    }

    private void findSlotViews(LinearLayout activeTimeSlot) {
        startTimeText=(EditText)activeTimeSlot.findViewById(R.id.startTimeText);
        endTimeText=(EditText)activeTimeSlot.findViewById(R.id.endTimeText);
        monday=(CheckBox)activeTimeSlot.findViewById(R.id.monday);
        tuesday=(CheckBox)activeTimeSlot.findViewById(R.id.tuesday);
        wednesday=(CheckBox)activeTimeSlot.findViewById(R.id.wednesday);
        thursday=(CheckBox)activeTimeSlot.findViewById(R.id.thursday);
        friday=(CheckBox)activeTimeSlot.findViewById(R.id.friday);
        saturday=(CheckBox)activeTimeSlot.findViewById(R.id.saturday);
        sunday=(CheckBox)activeTimeSlot.findViewById(R.id.sunday);
    }

    private void setEndTime(final EditText endTimeTxtVw) {
        final Calendar mcurrentTime = Calendar.getInstance();
        final int hour = (startHour<INVALID_HOUR?startHour+1:mcurrentTime.get(Calendar.HOUR_OF_DAY));
        int minute = (startMinute<INVALID_MINUTE?startMinute:mcurrentTime.get(Calendar.MINUTE));

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (startHour<=selectedHour) {
                    String am_pm = "";

                    if (selectedHour < 12)
                        am_pm = "AM";
                    else
                        am_pm = "PM";
                    endTimeTxtVw.setText(String.format("%02d:%02d", selectedHour % 12 == 0 ? 12 : selectedHour % 12, selectedMinute) + am_pm);
                    //setEndTime.setText(selectedHour + ":" + selectedMinute+am_pm);
                }
                else
                    Toast.makeText(parentActivity,"Please enter a valid time",Toast.LENGTH_LONG).show();
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setStartTime(final EditText statTimeTxtVw) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour=selectedHour;
                startMinute=selectedMinute;
                String am_pm="";
                if (startHour<12)
                    am_pm = "AM";
                else
                    am_pm = "PM";

                statTimeTxtVw.setText(String.format("%02d:%02d", selectedHour%12==0?12:selectedHour%12, selectedMinute)+am_pm);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();
        if(showScheduleDetail)
        {
            url= Constant.GET_TIMESLOTS_URL+classid;
            getScheduleDetails(url);
        }

    }
    private void getScheduleDetails(String url) {
        WebServices.triggerVolleyGetRequest(parentActivity, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                GetTimeSlots getTimeSlots = gson.fromJson(response, GetTimeSlots.class);
                if (getTimeSlots.getStatusCode().equalsIgnoreCase(WebServices.SUCCESS_CODE)){
                    if (getTimeSlots != null) {
                        timeslotList = getTimeSlots.getData().getTimeslotList();
                        slotCount = timeslotList.size();
                        for (int i = 0; i < slotCount; i++) {
                            activeTimeSlot = dayLayouts[i];
                            activeTimeSlot.setVisibility(View.VISIBLE);
                            findSlotViews(activeTimeSlot);
                            startTimeText.setText(timeslotList.get(i).getStartTime());
                            endTimeText.setText(timeslotList.get(i).getEndTime());
                            getDaysOfWeek = timeslotList.get(i).getDaysOfTheWeek();
                            setdaysStatus(getDaysOfWeek, activeTimeSlot);
                            if (slotCount > 1)
                                deleteButtonArray[i].setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(parentActivity, "Add new slots", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(parentActivity, "No Time slots added", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GetTimeSlots getTimeSlots = gson.fromJson(response, GetTimeSlots.class);
                        if(getTimeSlots!=null) {
                            timeslotList = getTimeSlots.getData().getTimeslotList();
                            slotCount = timeslotList.size();
                            for (int i = 0; i < slotCount; i++) {
                                activeTimeSlot = dayLayouts[i];
                                activeTimeSlot.setVisibility(View.VISIBLE);
                                findSlotViews(activeTimeSlot);
                                startTimeText.setText(timeslotList.get(i).getStartTime());
                                endTimeText.setText(timeslotList.get(i).getEndTime());
                                getDaysOfWeek = timeslotList.get(i).getDaysOfTheWeek();
                                setdaysStatus(getDaysOfWeek, activeTimeSlot);
                                //If slot count is more than one, enable delete button in all slots else, do not show delete button
                                    deleteButtonArray[i].setVisibility(View.VISIBLE);
                            }
                            //If slot count is zero, do not show update button
                            if(slotCount>0)
                                updateSchedule.setVisibility(View.VISIBLE);
                            else
                                updateSchedule.setVisibility(View.GONE);
                        }
                        else
                        {
                            Toast.makeText(parentActivity, "Add new slots", Toast.LENGTH_LONG).show();
                        }

                       *//* *//*
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
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
                        // Handle error
                    }
                });
        MySingleton.getInstance(parentActivity).addToRequestQueue(stringRequest);*/
    }
    private void setdaysStatus(byte getDaysOfWeek, LinearLayout activeTimeSlot) {
        findSlotViews(activeTimeSlot);
        if((getDaysOfWeek & SUNDAY)!=0)
        {
           sunday.setChecked(true);
        }
        if ((getDaysOfWeek & MONDAY)!=0)
        {
            monday.setChecked(true);
        }
        if ((getDaysOfWeek & TUESDAY)!=0)
        {
            tuesday.setChecked(true);
        }
        if ((getDaysOfWeek & WEDNESDAY)!=0)
        {
            wednesday.setChecked(true);
        }
        if ((getDaysOfWeek & THURSDAY)!=0)
        {
            thursday.setChecked(true);
        }
        if((getDaysOfWeek & FRIDAY)!=0)
        {
            friday.setChecked(true);
        }
        if ((getDaysOfWeek & SATURDAY)!=0)
        {
            saturday.setChecked(true);
        }
    }
}