package com.example.tremp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

//user document - Contains all personal details about single user
// ,The ID of each user doc is the user's email
//users collection - Contains all user documents
public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
       }
       //convert from users collection to users list(linked list)
    public void getAllUsers(Long since, Model.GetAllUsersListener listener) {
        db.collection("users")
                .whereGreaterThanOrEqualTo(User.LAST_UPDATED,new Timestamp(since, 0))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<User> usersList = new LinkedList<User>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        User u = User.fromJson(doc.getData());
                        if (u != null) {
                            usersList.add(u);
                        }
                    }
                }else{

                }
                listener.onComplete(usersList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });

    }

    public void addUserToFirebaseDB(User user, Model.AddUserListener listener) {
        db.collection("users")
                .document(user.getUserEmail()).set(user.toJson())
                .addOnSuccessListener((successListener)-> {
                    listener.onComplete();
                })
                .addOnFailureListener((e)-> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void getUserByEmail(String userEmail, Model.GetUserByEmailListener listener) {
        DocumentReference docRef = db.collection("users").document(userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User u = User.fromJson(document.getData());
                        listener.onComplete(u);
                    } else {
                        listener.onComplete(null);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }
}


