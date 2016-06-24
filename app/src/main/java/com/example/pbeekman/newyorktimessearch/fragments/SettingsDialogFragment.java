package com.example.pbeekman.newyorktimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pbeekman.newyorktimessearch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/22/16.
 */

public class SettingsDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox arts;
    @BindView(R.id.cbSports) CheckBox sports;
    @BindView(R.id.cbEducation) CheckBox education;
    String date;

    public SettingsDialogFragment() {
    }

    public String getNewsDeskValues() {
        String s = "";
        if (arts.isChecked())
            s += " \"Arts\"";
        if (sports.isChecked())
            s += " \"Sports\"";
        if (education.isChecked())
            s += " \"Education\"";
        if (!s.equals(""))
            s = s.substring(1);
        return s;
    }

    public String getDate() {
        return date;
    }

    public String getSortOrder() {
        return spSortOrder.getSelectedItem().toString();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("DEBUG", "yyyyyyyyy");
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // Get the beginDate here from the calendar parsed to correct format
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
        date = format.format(c.getTime());
        etDate.setText(format2.format(c.getTime()));
    }

    public static SettingsDialogFragment newInstance(String title) {
        SettingsDialogFragment frag = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = "Search Settings";
        getDialog().setTitle(title);
        ButterKnife.bind(this, view);
        Button saveButton = (Button) view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        //etDate.se
        Button clearButton = (Button) view.findViewById(R.id.clear);
        clearButton.setBackgroundResource(R.drawable.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = "";
                etDate.setText("");
            }
        });

        date = "";
    }
}