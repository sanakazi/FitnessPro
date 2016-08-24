package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.fiticket.fitnesspro.pojos.TimeslotJson;
import com.fiticket.fitnesspro.pojos.TimeslotList;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by InFinItY on 1/29/2016.
 */
public class CreateScheduleFragment extends DialogFragment {

    private static final String TAG = CreateScheduleFragment.class.getSimpleName();
    private LinearLayout[] dayLayouts;
    private EditText startTimeText;/*,startTimeText2,startTimeText3,startTimeText4,startTimeText5,startTimeText6*/
    private EditText endTimeText;/*,endTimeText2,endTimeText3,endTimeText4,endTimeText5,endTimeText6;*/
    private CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    private Button addSlots;
    private EditText[] startTimeTxtArray=new EditText[7];
    private EditText[] endTimeTxtArray=new EditText[7];
    private static final int INVALID_HOUR = 25;
    private static final int INVALID_MINUTE = 61;
    private int startHour=INVALID_HOUR;
    private int startMinute=INVALID_MINUTE;
    int slotCount =1;
    private LinearLayout activeTimeSlot;
    private ArrayList<LinearLayout> addedSlotsArray;
    private Activity parentActivity;
    private int classid;
    private Button deleteButton;
    private Button addSchedule;
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
    ProgressBar progressSchedule;

    public void setCreatedClassId(int classid) {
        this.classid=classid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.schedule, container, false);
        progressSchedule=(ProgressBar)view.findViewById(R.id.progressSchedule);
        addSchedule=(Button)view.findViewById(R.id.addSchedule);
        addSchedule.setVisibility(View.VISIBLE);
        dayLayouts= new LinearLayout[7];
        dayLayouts[0]=(LinearLayout)view.findViewById(R.id.slot1);
        dayLayouts[1]=(LinearLayout)view.findViewById(R.id.slot2);
        dayLayouts[2]=(LinearLayout)view.findViewById(R.id.slot3);
        dayLayouts[3]=(LinearLayout)view.findViewById(R.id.slot4);
        dayLayouts[4]=(LinearLayout)view.findViewById(R.id.slot5);
        dayLayouts[5]=(LinearLayout)view.findViewById(R.id.slot6);
        dayLayouts[6]=(LinearLayout)view.findViewById(R.id.slot7);
        if (slotCount==1)
        {
            activeTimeSlot=dayLayouts[0];
            activeTimeSlot.setVisibility(View.VISIBLE);
            findSlotViews(activeTimeSlot);
            deleteButton.setVisibility(View.GONE);
        }

        addSlots=(Button)view.findViewById(R.id.addSlots);

        for(int i=0;i<dayLayouts.length;i++){
            final int j=i;
            startTimeTxtArray[i]=(EditText)dayLayouts[i].findViewById(R.id.startTimeText);
            startTimeTxtArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStartTime(startTimeTxtArray[j]);
                }
            });

            endTimeTxtArray[i]=(EditText)dayLayouts[i].findViewById(R.id.endTimeText);
            endTimeTxtArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEndTime(endTimeTxtArray[j]);

                }
            });
        }
        addSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotCount < 7) {
                    deleteButton.setVisibility(View.GONE);
                    slotCount++;
                    activeTimeSlot = dayLayouts[slotCount - 1];
                    activeTimeSlot.setVisibility(View.VISIBLE);
                    //addedSlotsArray.add(activeTimeSlot);
                    findSlotViews(activeTimeSlot);
                    deleteButton.setVisibility(View.VISIBLE);
                    setDeleteBtnOnclickListener();
                } else {
                    Toast.makeText(parentActivity, "maximum number of slots allowed is 7", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressSchedule.setVisibility(View.VISIBLE);
                ArrayList<TimeslotJson> timeSlotArray= new ArrayList<>();
                for(int i=0;i<slotCount;i++)
                {
                    findSlotViews(dayLayouts[i]);
                    calculateDaysOfWeek(dayLayouts[i]);
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
        });
        return view;
    }

    private void setDeleteBtnOnclickListener() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slotCount>1)
                {

                    deleteButton.setVisibility(View.GONE);
                    slotCount--;
                    activeTimeSlot.setVisibility(View.GONE);
                    activeTimeSlot=dayLayouts[slotCount-1];
                    findSlotViews(activeTimeSlot);
                    deleteButton.setVisibility(View.VISIBLE);
                    setDeleteBtnOnclickListener();
                }
            }
        });
    }

    private void triggerPostVolleyRequest(String url,ArrayList<TimeslotJson> timeSlotArray) {
        TimeslotList tjson=new TimeslotList();
        tjson.setTimeslotList(timeSlotArray);
        WebServices.sendPostRequest(parentActivity, url, tjson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressSchedule.setVisibility(View.GONE);
                Log.d(TAG, "volley response:" + response.toString());
                CreateTimeSlotResponse responseJson = new Gson().fromJson(response.toString(), CreateTimeSlotResponse.class);
                if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    Toast.makeText(parentActivity, "added successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressSchedule.setVisibility(View.GONE);
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
        deleteButton=(Button)activeTimeSlot.findViewById(R.id.deleteSlotBtn);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();
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

    private void setEndTime(final EditText endTimeTxtVw) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = (startHour<INVALID_HOUR?startHour+1:mcurrentTime.get(Calendar.HOUR_OF_DAY));
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
}