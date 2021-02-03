package j.e.c.com.teacherPanelFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.Others.Helper;
import j.e.c.com.Others.Prefrence;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;

import static android.app.Activity.RESULT_OK;

public class TeacherMyProfileFragment extends Fragment {
    @BindView(R.id.profilePhoto)
    CircularImageView profilePhoto;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.idText)
    TextView idText;
    SQLiteHandler sqLiteHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_my_profile, container, false);
        ButterKnife.bind(this, view);
        sqLiteHandler = new SQLiteHandler(getContext());
        fetchUserInfo();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefrence.getProfileImage(getContext()) != null)
            profilePhoto.setImageBitmap(Prefrence.getProfileImage(getContext()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                profilePhoto.setImageURI(result.getUri());
                Prefrence.saveProfileImage(result.getUri(), getContext());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Helper.Toast(getContext(), result.getError().getMessage());
                //Exception error = result.getError();
            }
        }
    }

    @OnClick({R.id.backArrow, R.id.more, R.id.profile, R.id.name, R.id.id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.more:
                Helper.fragmentTransaction(this, new TeacherMoreFragment());
                break;
            case R.id.profile:
                CropImage.activity().start(getContext(), this);
                break;
            case R.id.name:
                Helper.popUpEditText(nameText, "Set new name...");
                break;
            case R.id.id:
                Helper.popUpEditText(idText, "Set New id...");
                break;
        }
    }

    private void fetchUserInfo() {
        Cursor res = sqLiteHandler.getAllData();
        while (res.moveToNext()) {
            nameText.setText(res.getString(1));
            idText.setText(res.getString(4));
///Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();
        }


    }
}
