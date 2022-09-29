package com.example.disneytripplanner.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.disneytripplanner.databinding.FragmentRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationFragment extends Fragment {
    RegistrationFragment.RegistrationFragmentListener mListener;
    FragmentRegistrationBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "Registration Fragment";

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = binding.editTextRegistrationFirstName.getText().toString();
                String lName = binding.editTextRegistrationLastName.getText().toString();
                String email = binding.editTextRegistrationEmail.getText().toString();
                String password = binding.editTextRegistrationPassword.getText().toString();

                if (fName.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "First Name is required", Toast.LENGTH_SHORT).show();
                } else if (lName.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "OnRegister: thread = " + Thread.currentThread().getId());
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: Registration is Successful");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Log.d(TAG, "onComplete: User " + user);

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(fName + " " + lName)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser updatedUser = mAuth.getCurrentUser();
                                                            Log.d(TAG, "User Display Name = " + updatedUser.getDisplayName());
                                                            createUser();
                                                        }
                                                    }
                                                });
                                        mListener.goToHomePage();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Registration  Error")
                                                .setMessage(task.getException().getMessage())
                                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Log.d(TAG, "onClick: Ok clicked");
                                                    }
                                                });
                                        builder.create().show();
                                        Log.d(TAG, "onComplete: Registration Error" + task.getException().getMessage());
                                    }
                                }

                            });
                }
            }
        });
        
        binding.textViewCancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToLogin();
            }
        });

        return binding.getRoot();
    }

    private void createUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String fName = binding.editTextRegistrationFirstName.getText().toString();
        String lName = binding.editTextRegistrationLastName.getText().toString();
        String id = user.getUid();
        String email = user.getEmail();

        HashMap<String, Object> newUser = new HashMap<>();

        newUser.put("id", id);
        newUser.put("firstName", fName);
        newUser.put("lastName", lName);
        newUser.put("email", email);

        db.collection("users")
                .document(id)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "New user was successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error creating new user" + e);
                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (RegistrationFragment.RegistrationFragmentListener) context;
    }

    public interface RegistrationFragmentListener {
        void goToHomePage();
        void backToLogin();
    }
}