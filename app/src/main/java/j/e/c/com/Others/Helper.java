package j.e.c.com.Others;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import j.e.c.com.AppController;
import j.e.c.com.Models.ContractInfo;
import j.e.c.com.Models.Teacher2;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.R;
import j.e.c.com.SQLiteHandler;
import j.e.c.com.appConfig;
import j.e.c.com.schoolPanelFragment.HireFormOneFragment;

import static android.media.tv.TvTrackInfo.TYPE_VIDEO;

public class Helper {

    static  Teacher teacher;
    static School school;
    static Teacher2 t2;
    static ContractInfo contractInfo;

    public  static  boolean isTeacherComeFromAdapter = false;
    public  static  boolean isTeacherChating = true;
    public static final int IMAGE_REQUEST_CODE = 100;
    public static final int VIDEO_REQUEST_CODE = 101;
    public static final int CV_REQUEST_CODE = 102;
    public static final int MULTIPLE_IMAGE_CODE = 104;
    public static final int BP_REQUEST_CODE = 103;

    public static Teacher getTeacher(){
        if (teacher == null){
            teacher = new Teacher();
        }
        return teacher;
    }

   public  static  ContractInfo getContractInfo(){
        if (contractInfo == null){
            contractInfo = new ContractInfo();

        }
        return contractInfo;
   }

    public static void setTeacher(Teacher t){
        if (t==null){
            teacher = null;
            return;
        }
        if (teacher != null)
            teacher = null;
        teacher = new Teacher(t);
    }

    public static Teacher2 getTeacher2(){
        if (t2 == null){
            t2 = new Teacher2();
        }
        return t2;
    }

    public  static  School getSchool(){
        if (school == null){
            school = new School();
        }

        return school;
    }
    public  static  School getSchool(School schoolToBeCopy){
        if(school != null)
            school = null;
        school = new School(schoolToBeCopy);
        return school;
    }

    public  static  void setSchool(School schoolToBeCopy){
        if (schoolToBeCopy == null){
            school=null;
            return;
        }
        if(school != null)
            school = null;
        school = new School(schoolToBeCopy);
    }

    /*public static void copySchool(School school, School school2){
        school = new School(school2);
    }*/


    public static void fragmentTransaction(Fragment current, Fragment target){
        FragmentTransaction transaction = Objects.requireNonNull(current.getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, target).addToBackStack(null).commit();
    }
    public static void fragmentTransaction(Fragment current, Fragment target, boolean addToBackStack){
        FragmentTransaction transaction = Objects.requireNonNull(current.getActivity()).getSupportFragmentManager().beginTransaction();
        if (addToBackStack)
            transaction.replace(R.id.fragment_container, target).addToBackStack(null).commit();
        else
            transaction.replace(R.id.fragment_container, target).commit();
    }

    public static ArrayAdapter<CharSequence> getSimpleSpinnerAdapter(int dataArray, Context context){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, dataArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static ArrayAdapter<String> getAutoCompleteAdapter(Context context){
        String[] type = new String[] {"Bed-sitter", "Single", "1- Bedroom", "2- Bedroom","3- Bedroom"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, type);
        return adapter;
    }

    public static void setDate(TextInputLayout dateView){

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
            int mMonth = month + 1;
            String mDate = dayOfMonth + "-" + mMonth + "-" + year;
            Objects.requireNonNull(dateView.getEditText()).setText(mDate);
        };


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(dateView.getContext(), R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public static void openCamera(Fragment fragment, int requestCode) {
        Dexter.withContext(fragment.getContext()).withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fragment.startActivityForResult(intent, requestCode);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).withErrorListener(dexterError -> Toast.makeText(fragment.getContext(), dexterError.toString(), Toast.LENGTH_SHORT).show()).check();
    }

    public static void getFileFromStorage(Fragment fragment, int requestCode){
        Dexter.withContext(fragment.getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                switch (requestCode){
                    case CV_REQUEST_CODE:
                    case BP_REQUEST_CODE:
                    case IMAGE_REQUEST_CODE:
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        fragment.startActivityForResult(intent, requestCode);
                        break;
                    case VIDEO_REQUEST_CODE:
                        Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        fragment.startActivityForResult(videoIntent , VIDEO_REQUEST_CODE);
                        break;
                    case MULTIPLE_IMAGE_CODE:
                        Intent multiImage = new Intent();
                        multiImage.setType("image/*");
                        multiImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        multiImage.setAction(Intent.ACTION_PICK);
                        fragment.startActivityForResult(Intent.createChooser(multiImage,"Select Picture"), MULTIPLE_IMAGE_CODE);
                        break;

                }
                       /* */
            }


            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).withErrorListener(dexterError -> {
            Toast.makeText(fragment.getContext(), dexterError.toString(), Toast.LENGTH_SHORT).show();
        }).check();
    }
    public static String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 65, ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encode;
    }

    public  static void ShowSnackBar(View view,String Message){


        Snackbar snackbar = Snackbar
                .make(view, ""+Message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }


    public static boolean validateField(TextInputLayout textInputLayout){
        if (TextUtils.isEmpty(textInputLayout.getEditText().getText().toString())) {
            textInputLayout.getEditText().setError("Field can't be empty");
            return false;
        }else
            textInputLayout.setError(null);
        return true;
    }

    public static boolean validateField(AppCompatAutoCompleteTextView spinner){
        if (TextUtils.isEmpty(spinner.getText().toString())) {
            spinner.setError("");
            return false;
        }else
            spinner.setError(null);
        return true;
    }

    public static void fill(TextInputLayout textInputLayout){
        textInputLayout.getEditText().setText("ss");
    }
    public static void fill(AppCompatAutoCompleteTextView spinner){
       spinner.setText("ss");
    }


    public static void Toast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean areYouSure(Context context, String message){

        final boolean[] result = new boolean[1];
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(message);

        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            //What ever you want to do with the value
            result[0] = true;
            handler.sendMessage(handler.obtainMessage());
        });

        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            result[0] = false;
            handler.sendMessage(handler.obtainMessage());
        });

        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return result[0];
    }

    public static void popUpEditText(TextView targetView, String title){
        AlertDialog.Builder alert = new AlertDialog.Builder(targetView.getContext());
        final EditText edittext = new EditText(targetView.getContext());
        alert.setTitle(title);

        alert.setView(edittext);

        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            //What ever you want to do with the value
            if (!edittext.getText().toString().trim().equals(""))
                targetView.setText(edittext.getText().toString());
        });

        alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();
    }
    public static String getFileExtension(Activity activity, Uri fileUri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.getContentResolver().getType(fileUri));
    }
    public static void setTime(TextView timeView){
        // Get Current Time
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(timeView.getContext(),
                (view, hourOfDay, minute) -> {
                    String amPm, time;
                    if (hourOfDay<12){
                        amPm = "AM";
                    }else {
                        amPm = "PM";
                        hourOfDay -= 12;
                    }
                    if(hourOfDay == 0)
                        hourOfDay = 12;
                    if(minute<10)
                        time = hourOfDay + ":0" + minute + " " + amPm;
                    else time = hourOfDay + ":" + minute + " " + amPm;
                    timeView.setText(time);

                }, mHour, mMinute, false);
        timePickerDialog.show();

    }
    public static void alert(String message, Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(message);
        alert.setPositiveButton("OK", (dialog, whichButton) -> {
            //What ever you want to do with the value
        });;

        alert.show();
    }
    public static void showSettingsDialog(Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings(fragment);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private static void openSettings(Fragment fragment) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", fragment.getContext().getPackageName(), null);
        intent.setData(uri);
        fragment.startActivityForResult(intent, 101);
    }

    public static Bitmap getBitmapFromUri(Uri imageUri, Context context){
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


        Bitmap imageResize = ImageResizer.reduceBitmapSize(bitmap,200000);

        return imageResize;
    }

    public static String fetchUserId(Context context) {
        SQLiteHandler sqLiteHandler;
        sqLiteHandler = new SQLiteHandler(context);
        Cursor res = sqLiteHandler.getAllData();
        String id = null;
        while (res.moveToNext()) {
             id = res.getString(3);
        }
        return id;
    }

    public  static String AdminId = "1";


    public static void updateJobStatus(String phone, String status, Fragment context) {



        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_updateApplayTableRow,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //  Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            Boolean result = obj.getBoolean("result");

                            if (result){
                                context.onResume();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context.getContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // spinKitView.setVisibility(View.GONE);


                    }

                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("id", phone);
            params.put("status", status);


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public static void onBackPressedInFragment(Fragment fragment){

        View view = fragment.getView();

        if(view == null){
            return;
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    goBackFromFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    public static void goBackFromFragment(Fragment fragment){
        fragment.getFragmentManager().popBackStack();
        fragment.getFragmentManager().beginTransaction().remove(fragment).commit();
    }

}
