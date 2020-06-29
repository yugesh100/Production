package com.reminder.remindme.ui.auth;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reminder.remindme.R;
import com.reminder.remindme.di.injector.Injectable;
import com.reminder.remindme.ui.MainActivity;
import com.reminder.remindme.viewmodel.UserViewModel;

import javax.inject.Inject;

//using login fragment for login
public class LoginFragment extends Fragment implements Injectable {

    private static final String TAG = LoginFragment.class.getSimpleName();

    TextInputLayout emailTIL;
    TextInputLayout passwordTIL;

    AppCompatEditText edtEmail;
    AppCompatEditText edtPassword;

    View signInBtn;
    TextView signUpLink;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailTIL = view.findViewById(R.id.emailTIL);
        passwordTIL = view.findViewById(R.id.passwordTIL);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        signInBtn = view.findViewById(R.id.signInButton);
        signUpLink = view.findViewById(R.id.createAccountLink);

        signInBtn.setOnClickListener(this::attemptLogin);

        signUpLink.setOnClickListener(v ->
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.authContainer, new SignUpFragment())
                        .addToBackStack("SignUp")
                        .commit());

        return view;
    }

    /**
     *
     * @param view which triggers the login
     */
    private void attemptLogin(View view) {
        emailTIL.setError(null);passwordTIL.setError(null);
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailTIL.setError("Email address could not be empty!");
            emailTIL.requestFocus();return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordTIL.setError("Password could not be empty!");
            passwordTIL.requestFocus();return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        userViewModel.login(email, password).observe(this, response -> {
            if (response == null) return;
            progressDialog.dismiss();
            if (response.isSuccessful()) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
