package com.example.prueba1.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prueba1.R;
import com.example.prueba1.model.TipoMantenimiento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroMantenimientoActivity extends AppCompatActivity {
    private ImageButton atras_mosaico;
    private EditText fecha_mantenimiento;
    private EditText fecha_prox_mantenimiento;
    private String dueDate="";
    private String nextdueDate="";
    private EditText inputkm_actual;
    private EditText inputkm_prox;
    private EditText inputnotas;
    private Button btnRegistrar;
    String id_tipo = "";

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_mantenimiento);

        // Referencia al Spinner
        Spinner spinnerTipoMantenimiento = findViewById(R.id.Tipo   );

        // Lista de mantenimientos
        List<TipoMantenimiento> tiposMantenimiento = new ArrayList<>();
        tiposMantenimiento.add(new TipoMantenimiento("tipo_1", "Aceite y Filtros"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_2", "Refrigeración"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_3", "Frenos"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_4", "Correas y Cadenas"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_5", "Bujías"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_6", "Neumáticos"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_7", "Sistema de Transmisión"));
        tiposMantenimiento.add(new TipoMantenimiento("tipo_8", "Sistema Eléctrico"));

        // Adaptador para el Spinner
        ArrayAdapter<TipoMantenimiento> adapter = new ArrayAdapter<TipoMantenimiento>(this, R.layout.spinner_item, tiposMantenimiento) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Este es el layout que se muestra cuando el dropdown está cerrado
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setText(tiposMantenimiento.get(position).getNombre());
                return label;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Este es el layout que se muestra en el dropdown
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setText(tiposMantenimiento.get(position).getNombre());
                return label;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoMantenimiento.setAdapter(adapter);

        spinnerTipoMantenimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TipoMantenimiento tipoSeleccionado = (TipoMantenimiento) parentView.getItemAtPosition(position);
                String id_tipoSeleccionado = tipoSeleccionado.getId_tipo();
                String nombreSeleccionado = tipoSeleccionado.getNombre();
                id_tipo = id_tipoSeleccionado;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Maneja el caso en que no se seleccione nada
            }
        });

        // Para BD
        firestore = FirebaseFirestore.getInstance();

        inputkm_actual = findViewById(R.id.Kim_actual);
        inputkm_prox = findViewById(R.id.Kim_prox);
        inputnotas = findViewById(R.id.Notas);

        // Inicializacion de boton para volver a pantalla anterior
        atras_mosaico = findViewById(R.id.Atras_mosaico);
        atras_mosaico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroMantenimientoActivity.this, MosaicoActivity.class);
                startActivity(intent);
            }
        });

        btnRegistrar = findViewById(R.id.BtnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String km_actual = inputkm_actual.getText().toString();
                String km_prox = inputkm_prox.getText().toString();
                String notas = inputnotas.getText().toString();
                if(id_tipo.isEmpty() || dueDate.isEmpty() || nextdueDate.isEmpty() || km_actual.isEmpty() || km_prox.isEmpty() || notas.isEmpty()){
                    Toast.makeText(RegistroMantenimientoActivity.this, "Todos los campos son obligatorios",Toast.LENGTH_SHORT).show();
                }else{
                    Map<String,Object> MantenimientoMap = new HashMap<>();
                    MantenimientoMap.put("id_tipo",id_tipo);
                    MantenimientoMap.put("fecha",dueDate);
                    MantenimientoMap.put("km_actual",km_actual);
                    MantenimientoMap.put("km_prox",km_prox);
                    MantenimientoMap.put("fecha_prox",nextdueDate);
                    MantenimientoMap.put("notas",notas);
                    /* taskMap.put("due",dueDate);
                    taskMap.put("time",time);
                    taskMap.put("status",0); */

                    firestore.collection("Mantenimiento").add(MantenimientoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistroMantenimientoActivity.this,"Mantenimiento guardado", Toast.LENGTH_SHORT).show();
                                finish(); // Cierra la actividad después de guardar
                            }else{
                                //Toast.makeText(RegistroMantenimientoActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                                Toast.makeText(RegistroMantenimientoActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistroMantenimientoActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración del TextView como un calendario
        fecha_mantenimiento = findViewById(R.id.Fecha_mantenimiento);
        fecha_mantenimiento.setFocusable(false);
        fecha_mantenimiento.setClickable(true);
        fecha_mantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatePicker(fecha_mantenimiento, true);
            }
        });

        fecha_prox_mantenimiento = findViewById(R.id.Fecha_prox_mantenimiento);
        fecha_mantenimiento.setFocusable(false);
        fecha_mantenimiento.setClickable(true);
        fecha_prox_mantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatePicker(fecha_prox_mantenimiento, false);
            }
        });
    }

    // Método reutilizable para mostrar el DatePickerDialog
    private void mostrarDatePicker(final EditText editText, final boolean isFirstDate) {
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroMantenimientoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1; // Los meses empiezan desde 0
                String selectedDate = dayOfMonth + "/" + month + "/" + year;
                editText.setText(selectedDate); // Establece la fecha en el TextView

                if (isFirstDate) {
                    dueDate = selectedDate; // Almacena la primera fecha
                } else {
                    nextdueDate = selectedDate; // Almacena la segunda fecha
                }
            }
        }, year, month, day);

        // Establecer la fecha mínima como la fecha actual
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
}