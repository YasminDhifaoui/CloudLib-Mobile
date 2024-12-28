package com.example.atelier10;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

//import com.google.firebase.firestore.FirebaseFirestore;
//import com.samihadhri.firestore.Livre;
//import com.samihadhri.firestore.R;

public class Modification extends AppCompatActivity {
    private FirebaseFirestore db;
    private Spinner spLivre;
    private EditText edTitre;
    private EditText edNbPage;
    private Button btnModifier;
    private Button btnRetour;
    private ArrayAdapter<Livre> adpLivre;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        initialiser();
        ecouteurs();
        remplir();
    }

    private void initialiser() {
        db = FirebaseFirestore.getInstance();
        spLivre = findViewById(R.id.spLivre);
        edTitre = findViewById(R.id.edTitre);
        edNbPage = findViewById(R.id.edNbPage);
        btnModifier = findViewById(R.id.btnModifier);
        btnRetour = findViewById(R.id.btnRetour);
        adpLivre = new ArrayAdapter<Livre>(this, android.R.layout.simple_list_item_1);
        adpLivre.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spLivre.setAdapter(adpLivre);
    }

    private void ecouteurs() {
        // TODO Auto-generated method stub
        btnModifier.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                modifierLivre();

            }
        });
        btnRetour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spLivre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                actualiser();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    protected void actualiser() {
        if (spLivre.getSelectedItemPosition() >= 0) {
            Livre l = (Livre) spLivre.getSelectedItem();
            edTitre.setText(l.getTitre());
            edNbPage.setText(l.getNbpage() + "");
        }
    }

    private void remplir() {
        adpLivre.clear();
        db.collection("livres")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String titre = document.getString("titre");
                                int nbpage = document.getDouble("nbpage").intValue();

                                Livre l = new Livre(id, titre, nbpage);
                                adpLivre.add(l);
                            }
                        } else {
                            // Erreur lors de la récupération des livres
                            Toast.makeText(getApplicationContext(), "Erreur lors de la récupération de la liste des livres", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    protected void modifierLivre() {
        Livre livre= (Livre) spLivre.getSelectedItem();

        Map<String, Object> map = new HashMap<>();
        map.put("titre", edTitre.getText().toString());
        map.put("nbpage", Integer.parseInt(edNbPage.getText().toString()));

        db.collection("livres").document(livre.getId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Le livre a été modifié avec succès
                        remplir();
                        Toast.makeText(getApplicationContext(), "Livre modifié avec succès",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de la modification du livre
                        Toast.makeText(getApplicationContext(), "Erreur lors de la modification du livre", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
