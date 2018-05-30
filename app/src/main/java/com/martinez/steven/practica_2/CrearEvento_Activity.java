package com.martinez.steven.practica_2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.martinez.steven.practica_2.model.Eventos;
import com.martinez.steven.practica_2.model.EventosUsuarios;

import java.util.Calendar;

public class CrearEvento_Activity extends AppCompatActivity {

    EditText eDeporte, eHora, eFecha, eCancha, eDescripcion, ePuntoEncuentro;
    Calendar mCurrentDate;
    Button bCrearEvento;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    Bundle extras;
    String id_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        ePuntoEncuentro = findViewById(R.id.ePuntoEncuentro);
        eDescripcion = findViewById(R.id.eDescripcion);
        eDeporte = findViewById(R.id.eDeporte);
        eHora = findViewById(R.id.eHora);
        eFecha = findViewById(R.id.eFecha);
        eCancha = findViewById(R.id.eCancha);

        selectDeporte();
        selectHora();
        selectFecha();
        selectCancha();

        extras = getIntent().getExtras();

        if(extras != null) {
            id_user = extras.getString("id");
        }

        FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void selectCancha() {
        eCancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(CrearEvento_Activity.this);
                builderSingle.setIcon(R.drawable.com_facebook_button_icon);
                builderSingle.setTitle("Escoja una cancha");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CrearEvento_Activity.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("Cancha sintética");
                arrayAdapter.add("Cancha de fútbol");
                arrayAdapter.add("Placas bajo el metro");
                arrayAdapter.add("Placas junto a la piscina");
                arrayAdapter.add("Coliseo");
                arrayAdapter.add("Plazoleta central");
                arrayAdapter.add("Plaza Barrientos");


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
                        eCancha.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });
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
                        eHora.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
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
                arrayAdapter.add("Micro");
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

    public void OnClickButton_CrearEvento(View view) {
        if (camposLlenos(true)) {
            Eventos nuevoEvento = new Eventos(databaseReference.push().getKey(),
                    eDescripcion.getText().toString(),
                    eCancha.getText().toString(),
                    ePuntoEncuentro.getText().toString(),
                    eDeporte.getText().toString(),
                    eFecha.getText().toString(),
                    eHora.getText().toString());

            databaseReference.child("Eventos").child(nuevoEvento.getId()).setValue(nuevoEvento);
            Log.d("paso", "1 "+id_user);
            EventosUsuarios evenusers = new EventosUsuarios(databaseReference.push().getKey(),
                    id_user,
                    nuevoEvento.getId(),
                    id_user
                    );

            Log.d("paso", "3");
            databaseReference.child("EventosUsuarios").child(evenusers.getId()).setValue(evenusers);
            Log.d("paso", "2");
            Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean camposLlenos(boolean b) {
        if (eDescripcion.getText().toString().isEmpty() || eDeporte.getText().toString().isEmpty() || eHora.getText().toString().isEmpty()
                || eFecha.getText().toString().isEmpty() || ePuntoEncuentro.getText().toString().isEmpty()){
            return false;
        }else {
            return true;
        }
    }
}
