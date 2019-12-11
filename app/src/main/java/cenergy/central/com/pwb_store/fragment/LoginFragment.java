package cenergy.central.com.pwb_store.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.helpers.DialogHelper;
import cenergy.central.com.pwb_store.manager.ApiResponseCallback;
import cenergy.central.com.pwb_store.manager.HttpManagerMagento;
import cenergy.central.com.pwb_store.manager.bus.event.LoginSuccessBus;
import cenergy.central.com.pwb_store.manager.preferences.PreferenceManager;
import cenergy.central.com.pwb_store.model.APIError;
import cenergy.central.com.pwb_store.model.UserInformation;
import cenergy.central.com.pwb_store.realm.RealmController;
import cenergy.central.com.pwb_store.utils.DialogUtils;
import cenergy.central.com.pwb_store.view.PowerBuyEditText;
import cenergy.central.com.pwb_store.view.PowerBuyIconButton;

@SuppressWarnings("unused")
public class LoginFragment extends Fragment implements TextWatcher, View.OnClickListener {
    public static final String TAG = LoginFragment.class.getSimpleName();

    //View Members
    private PowerBuyEditText mEditTextUserName;
    private PowerBuyEditText mEditTextPassword;
    private PowerBuyIconButton mLoginButton;
    private ProgressDialog mProgressDialog;
    private String username;
    private String password;

    @SuppressWarnings("unused")
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        mEditTextUserName = rootView.findViewById(R.id.edit_text_username);
        mEditTextPassword = rootView.findViewById(R.id.edit_text_password);
        mLoginButton = rootView.findViewById(R.id.loginButton);
        mEditTextUserName.addTextChangedListener(this);
        mEditTextPassword.addTextChangedListener(this);
        checkLogin();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDialog(getContext());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    private void hideSoftKeyboard(View view) {
        // Check if no view has focus:
        if (view != null && getContext() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkLogin();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void checkLogin() {
        if(getContext() != null){
            if (!mEditTextUserName.getText().toString().isEmpty() && !mEditTextPassword.getText().toString().isEmpty()) {
                username = mEditTextUserName.getText().toString();
                password = mEditTextPassword.getText().toString();
                mLoginButton.setButtonDisable(false);
                mLoginButton.setOnClickListener(this);
            } else {
                mLoginButton.setButtonDisable(true);
                mLoginButton.setOnClickListener(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            hideSoftKeyboard(v);
            showProgressDialog();
            login();
        }
    }

    private void login() {
        if (getContext() != null) {
            HttpManagerMagento.Companion.getInstance(getContext()).userLogin(username, password,
                    new ApiResponseCallback<UserInformation>() {
                        @Override
                        public void success(@org.jetbrains.annotations.Nullable UserInformation response) {
                            if (response != null) {
                                if (checkUserLogin(response)) {
                                    dismissDialog();
                                    EventBus.getDefault().post(new LoginSuccessBus(true));
                                } else {
                                    dismissDialog();
                                    showAlertDialog("", getString(R.string.some_thing_wrong));
                                    userLogout();
                                }
                            }
                        }

                        @Override
                        public void failure(@NotNull APIError error) {
                            dismissDialog();
                            if(getContext() != null){
                                new DialogHelper(getContext()).showErrorLoginDialog(error);
                            }
                        }
                    });
        }
    }

//    private Boolean checkUserLogin(UserResponse userResponse){
//        return  userResponse.getUser().getStaffId() != null && !userResponse.getUser().getStaffId().equals("")
//                && !userResponse.getUser().getStaffId().equals("0") && userResponse.getUser().getStoreId() != null
//                && userResponse.getUser().getStoreId() != 0 && userResponse.getStore() != null
//                && userResponse.getStore().getPostalCode() != null && userResponse.getStore().getStoreId() != null
//                && userResponse.getStore().getStoreId() != 0;
//    }

    private Boolean checkUserLogin(UserInformation userInformation){
        return userInformation.getStore() != null && userInformation.getUser() != null &&
                userInformation.getUser().getStaffId() != null && !userInformation.getUser().getStaffId().equals("") &&
                !userInformation.getUser().getStaffId().equals("0") && !userInformation.getStore().getRetailerId().equals("");
    }

    private void clearData() {
        if (getContext() != null) {
            PreferenceManager preferenceManager = new PreferenceManager(getContext());
            preferenceManager.userLogout();
            RealmController.getInstance().userLogout();
        }
    }

    private void userLogout() {
        clearData();
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
