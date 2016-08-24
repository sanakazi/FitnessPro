package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.pojos.LoginRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by InFinItY on 12/1/2015.
 */
public class LoginFragment extends Fragment {
    Context context;

    FragmentInteractionListner listener;
    private Button loginButton;
    private EditText userNameEdt,passwordEdt;
    Activity parentActivity;
    FragmentManager fragmentManager;
    private TextView usernameIcon,passwordIcon;



    public interface FragmentInteractionListner
    {
        void onLoginClicked(LoginRequest request);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {

            listener=(FragmentInteractionListner)getActivity();
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ "must implement onButtonClicked");
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.login_fragment, container, false);
        usernameIcon=(TextView)view.findViewById(R.id.contactIcon);
        passwordIcon=(TextView)view.findViewById(R.id.passwordIcon);
        userNameEdt=(EditText)view.findViewById(R.id.etEmail);
        passwordEdt=(EditText)view.findViewById(R.id.etPassword);
        loginButton= (Button)view.findViewById(R.id.btn_login);
        setOnClickListeners();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();
        Typeface font = Typeface.createFromAsset(parentActivity.getAssets(), "fontawesome-webfont.ttf");

        usernameIcon.setTypeface(font);

        passwordIcon.setTypeface(font);
        listener=(FragmentInteractionListner)parentActivity;
    }

    private void setOnClickListeners() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName= userNameEdt.getText().toString();

                String password= passwordEdt.getText().toString();

                if (isValidEntries(userName,password)) {
                    if (emailValidator(userName)){
                        LoginRequest request= new LoginRequest(userName,password);
                        listener.onLoginClicked(request);
                    }else{
                        Toast.makeText(parentActivity, "Enter Valid EmailId", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(parentActivity, "Enter Valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidEntries(String userName, String password) {
        if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
            return  true;
        }
        return false;
    }

}
