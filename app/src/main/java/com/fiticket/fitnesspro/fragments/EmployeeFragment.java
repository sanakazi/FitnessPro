package com.fiticket.fitnesspro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fiticket.fitnesspro.R;

/**
 * Created by InFinItY on 11/27/2015.
 */
public class EmployeeFragment extends Fragment {
    private EditText name;
    private EditText role;
    private EditText email;
    private EditText number;
    private EditText salary;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.employee_fragment, container, false);
        name=(EditText)view.findViewById(R.id.nameEmployee);
        role=(EditText)view.findViewById(R.id.roleEmployee);
        email=(EditText)view.findViewById(R.id.emailEmployee);
        number=(EditText)view.findViewById(R.id.numberEmployee);
        salary=(EditText)view.findViewById(R.id.salaryEmployee);
        String[] values =
                {"Mr.", "Mrs."};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> list = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_list, R.id.spinnerText, values);
        spinner.setAdapter(list);
        Button saveButton = (Button) view.findViewById(R.id.addEmployeeButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().length() == 0)
                    name.setError("Name is required!");
                else {
                    // do async task
                    name.setError(null);
                }
                if (role.getText().toString().length() == 0)
                    role.setError("Role is required!");
                else {
                    // do async task
                    role.setError(null);
                }
                if (email.getText().toString().length() == 0)
                    email.setError("Email is required!");
                else {
                    // do async task
                    email.setError(null);
                }
                if (number.getText().toString().length() == 0)
                    number.setError("Number is required!");
                else {
                    // do async task
                    number.setError(null);
                }
                if (salary.getText().toString().length() == 0)
                    salary.setError("Salary is required!");
                else {
                    // do async task
                    salary.setError(null);
                }
            }
        });
        return view;
    }

}