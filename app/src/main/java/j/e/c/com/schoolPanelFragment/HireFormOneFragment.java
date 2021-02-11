package j.e.c.com.schoolPanelFragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.Models.School;
import j.e.c.com.Others.Helper;
import j.e.c.com.R;

import static android.app.Activity.RESULT_OK;

public class HireFormOneFragment extends Fragment {
    Bitmap bitmap;
    School school = Helper.getSchool();
    boolean seflToogle = false;

    @BindView(R.id.agentSpinner)
    AppCompatAutoCompleteTextView agentSpinner;
    @BindView(R.id.agentSpinnerParent)
    TextInputLayout agentSpinnerParent;
    @BindView(R.id.schoolLocationSpinner)
    AppCompatAutoCompleteTextView schoolLocationSpinner;
    @BindView(R.id.self)
    TextView self;
    @BindView(R.id.licenseImage)
    ImageView licenseImage;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.applicantName)
    TextInputLayout applicantName;
    @BindView(R.id.positionInSchool)
    TextInputLayout positionInSchool;
    @BindView(R.id.contact)
    TextInputLayout contact;
    @BindView(R.id.wechat)
    TextInputLayout wechat;
    RelativeLayout snackbar_action;
    @BindView(R.id.snackbar_action)
    RelativeLayout snackbarAction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_hire_form_one, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        agentSpinner.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.agentArray, getContext()));
        schoolLocationSpinner.setAdapter(Helper.getSimpleSpinnerAdapter(R.array.workSpace, getContext()));
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case Helper.IMAGE_REQUEST_CODE:
                    //Uri fiePath = data.getData();

                    //picture.setImageURI(fiePath);
                    bitmap = (Bitmap) data.getExtras().get("data");
                    picture.setImageBitmap(bitmap);
                    picture.setVisibility(View.VISIBLE);

                    school.setLicenseLiveImage(Helper.getStringImage(bitmap));
                    break;
                case Helper.CV_REQUEST_CODE:
                    try {
                        Uri pdfPath = data.getData();
                        licenseImage.setImageURI(pdfPath);
                        licenseImage.setVisibility(View.VISIBLE);
                        InputStream inputStream = getContext().getContentResolver().openInputStream(pdfPath);
                        byte[] pdfInBytes = new byte[inputStream.available()];
                        inputStream.read(pdfInBytes);

                        school.setLicenecePicture(Base64.encodeToString(pdfInBytes, Base64.DEFAULT));
                        //   Toast.makeText(getContext(),""+teacher.getFileEncodeUrl(),Toast.LENGTH_LONG).show();


                    } catch (Exception e) {

                        Helper.ShowSnackBar(snackbarAction,e.getMessage());
                    }
                    break;
            }
        }
    }


    @OnClick({R.id.backArrow, R.id.self, R.id.licensebtn, R.id.camera, R.id.fillBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                Helper.goBackFromFragment(this);
                break;
            case R.id.self:
                if (!seflToogle) {
                    self.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_react01));
                    self.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    agentSpinnerParent.setEnabled(false);
                    seflToogle = !seflToogle;
                } else {
                    self.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_rect02));
                    self.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    agentSpinnerParent.setEnabled(true);
                    seflToogle = !seflToogle;
                }
                break;
            case R.id.licensebtn:
                Helper.getFileFromStorage(this, Helper.CV_REQUEST_CODE);
                break;
            case R.id.camera:
                Helper.openCamera(this, Helper.IMAGE_REQUEST_CODE);
                break;
            case R.id.fillBtn:
                if (validateField())


                    setValues();
                break;
        }


    }

    private void setValues() {

        if (!seflToogle) {

            school.setAgentReference(agentSpinner.getText().toString().trim());
            //Toast.makeText(getContext(),""+school.getAgentReference(),Toast.LENGTH_LONG).show();
        }else {
            school.setAgentReference("Self");
           // Toast.makeText(getContext(),""+school.getAgentReference(),Toast.LENGTH_LONG).show();
        }

        school.setSchoolLocation(schoolLocationSpinner.getText().toString().trim());
        school.setApplicantName(applicantName.getEditText().getText().toString());
        school.setPostionInSchool(positionInSchool.getEditText().getText().toString());
        school.setContactNumber(contact.getEditText().getText().toString());
        school.setWeChatId(wechat.getEditText().getText().toString());
        Helper.isTeacherComeFromAdapter = false;
        Helper.fragmentTransaction(this, new HireFormTwoFragment(), false);
    }

    boolean validateField() {
        boolean b = true;
        if (!seflToogle && !Helper.validateField(agentSpinner))
            b = false;
        if (!Helper.validateField(schoolLocationSpinner))
            b = false;
        if (!Helper.validateField(applicantName))
            b = false;
        if (!Helper.validateField(positionInSchool))
            b = false;
        if (!Helper.validateField(contact))
            b = false;
        if (!Helper.validateField(wechat))
            b = false;

        return b;
    }

}
