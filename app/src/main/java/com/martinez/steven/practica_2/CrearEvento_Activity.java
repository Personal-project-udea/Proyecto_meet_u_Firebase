package com.martinez.steven.practica_2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class CrearEvento_Activity extends AppCompatActivity {

    EditText eDeporte, eHora, eFecha;
    Calendar mCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        eDeporte = findViewById(R.id.eDeporte);
        eHora = findViewById(R.id.eHora);
        eFecha = findViewById(R.id.eFecha);
        selectDeporte();
        selectHora();
        selectFecha();



    }

    private void selectFecha() {
        eFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(CrearEvento_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        int month_correct = selectedMonth +1;
                        eFecha.setText(selectedDay+"/"+month_correct+"/"+selectedYear);

                        mCurrentDate.set(selectedYear,selectedMonth,selectedDay);
                    }
                },year,month,day);
                mDatePicker.show();

            }
        });
    }

    private void selectHora() {

        eHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CrearEvento_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eHora.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    private void selectDeporte() {
        eDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(CrearEvento_Activity.this);
                builderSingle.setIcon(R.drawable.com_facebook_button_icon);
                builderSingle.setTitle("Seleccione un deporte");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CrearEvento_Activity.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("Fútbol");
                arrayAdapter.add("Básquetbol");
                arrayAdapter.add("Micro Fútbol");
                arrayAdapter.add("Voleibol");


                builderSingle.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        eDeporte.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });
    }

}
