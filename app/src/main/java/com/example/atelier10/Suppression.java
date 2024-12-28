package com.example.atelier10;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
//import com.samihadhri.firestore.Livre;
//import com.samihadhri.firestore.R;

public class Suppression extends AppCompatActivity {
    private FirebaseFirestore db;
    private Spinner spLivre;
    private Button btnSupprimer;
    private Button btnRetour;
    private ArrayAdapter<Livre> adpLivre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppression);
        initialiser();
        ecouteurs();
        remplir();
    }

    private void initialiser() {
        db = FirebaseFirestore.getInstance();
        spLivre = findViewById(R.id.spLivre);
        btnSupprimer = findViewById(R.id.btnSupprimer);
        btnRetour = findViewById(R.id.btnRetour);
        adpLivre = new ArrayAdapter<Livre>(this, android.R.layout.simple_list_item_1);
        adpLivre.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spLivre.setAdapter(adpLivre);
    }

    private void ecouteurs() {
        btnSupprimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                supprimerLivre();
            }
        });
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    protected void retourner() {
        finish();
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

    protected void supprimerLivre() {
        if(spLivre.getSelectedItemPosition()>=0) {
            Livre livre= (Livre) spLivre.getSelectedItem();

            db.collection("livres").document(livre.getId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Le livre a été supprimé avec succès
                            remplir();
                            Toast.makeText(getApplicationContext(), "Livre supprimé avec succès",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Erreur lors de la suppression du livre
                            Toast.makeText(getApplicationContext(), "Erreur lors de la suppression du livre", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
