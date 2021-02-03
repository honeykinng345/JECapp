package j.e.c.com.schoolPanelFragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import j.e.c.com.AppController;
import j.e.c.com.Models.ContractInfo;
import j.e.c.com.Models.School;
import j.e.c.com.Models.Teacher;
import j.e.c.com.Others.Helper;
import j.e.c.com.Others.ImageResizer;
import j.e.c.com.R;
import j.e.c.com.appConfig;
import j.e.c.com.commonFragments.PaymentFragment;
import j.e.c.com.teacherPanelFragments.HomeFragment;

import static android.app.Activity.RESULT_OK;

public class ContractFragment extends Fragment {

    View downloadContract;
    private  Bitmap bitmap;
    List<SlideModel> slideModels;

    @BindView(R.id.contractImages)
    ImageSlider contractImages;
    @BindView(R.id.paymentImg)
    ImageView paymentImg;
    ContractInfo contractInfo = Helper.getContractInfo();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        ButterKnife.bind(this, view);

        downloadContract = view.findViewById(R.id.download);
        slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.jeccontract1, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.jeccontract2, ScaleTypes.CENTER_INSIDE));
        contractImages.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        downloadContract.setOnClickListener(v -> {
            Helper.Toast(getContext(), "downloading...");
            CopyReadAssets();
            //downloadContract();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case Helper.MULTIPLE_IMAGE_CODE:
                    Uri c1 = data.getClipData().getItemAt(0).getUri();
                    Uri c2 = data.getClipData().getItemAt(1).getUri();
                    slideModels.clear();
                    slideModels.add(new SlideModel(String.valueOf(c1), ScaleTypes.CENTER_INSIDE));
                    slideModels.add(new SlideModel(String.valueOf(c2), ScaleTypes.CENTER_INSIDE));
                    contractImages.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
                    contractImages.setTag("d");
                    try {


                        Bitmap pic1 = Helper.getBitmapFromUri(c1, getContext());
                        Bitmap pic2 = Helper.getBitmapFromUri(c2, getContext());

                        contractInfo.setContract_Image(Helper.getStringImage(pic1));
                        contractInfo.setContract_Second_Image(Helper.getStringImage(pic2));



                        //  Toast.makeText(getContext(),""+teacher.getCvpic().length(),Toast.LENGTH_LONG).show();


                    } catch (Exception e) {

                       Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    break;
                case Helper.IMAGE_REQUEST_CODE:
                    Uri paymentImage = data.getData();
                    paymentImg.setImageURI(paymentImage);
                    Bitmap payment = Helper.getBitmapFromUri(paymentImage, getContext());
                    contractInfo.setPayment_Image(Helper.getStringImage(payment));

                    break;
            }
        }
    }

    @OnClick({R.id.backArrow, R.id.upload, R.id.submit, R.id.paymentbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStack();
                break;
            case R.id.upload:
                Helper.getFileFromStorage(this, Helper.MULTIPLE_IMAGE_CODE);
                break;
            case R.id.paymentbtn:
                Helper.getFileFromStorage(this, Helper.IMAGE_REQUEST_CODE);
                break;
            case R.id.submit:

                if (contractImages.getTag().equals("d")) {

                    SendContractInfo(Helper.getTeacher().getTid(),Helper.getSchool().getStid(),Helper.getSchool().getId(),contractInfo.getPayment_Image(),contractInfo.getContract_Image(),contractInfo.getContract_Second_Image());

                    /*AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    alert.setTitle("Please Pay First!");

                    alert.setPositiveButton("PAY", (dialog, whichButton) -> {
                        //What ever you want to do with the value
                        Helper.fragmentTransaction(this, new PaymentFragment());
                    });

                    alert.setNegativeButton("CANCEL", (dialog, whichButton) -> {
                        // what ever you want to do with No option.
                    });

                    alert.show();*/
                } else {
                   // Helper.alert("Please Upload Filled Contract First!", getContext());


                }


                break;
        }
    }

    private void SendContractInfo(String  tid, String stid, String jid, String payment_image, String contract_image, String contract_second_image) {

        String tag_string_req = "req_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, appConfig.URL_ContractInformation,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean error = obj.getBoolean("error");
                            //Toast.makeText(currentFragment.getContext(),""+response,Toast.LENGTH_LONG).show();
                            // Check for error node in json
                            if (!error) {
                                // user successfully logged in
                                // Create login session

                                Toast.makeText(getContext(),"Contract Is Submitted",Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


                            } else {
                                // Error in login. Get the error message
                                String errorMsg = obj.getString("error_msg");
                                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();

                            }

                            //getting the whole json object from the response


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // spinKitView.setVisibility(View.GONE);


                    }

                }) { @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<>();
            params.put("tid", tid);
            params.put("sid", stid);
            params.put("jid", jid);
            params.put("pimage", payment_image);
            params.put("contractImage", contract_image);
            params.put("contractSecondImage", contract_second_image);
            params.put("status", "0");


            return params;
        }

        };

        //adding our stringrequest to queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private void downloadContract() {

        Dexter.withContext(getContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.banner1);
                //contractImage.setImageBitmap(bm);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/JEC_images");
                myDir.mkdirs();

                String fname = "JECcontract" + ".png";

                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                if (permissionDeniedResponse.isPermanentlyDenied())
                    Helper.showSettingsDialog(ContractFragment.this);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(dexterError -> {
            Toast.makeText(getContext(), dexterError.toString(), Toast.LENGTH_SHORT).show();
        }).check();
    }

    private void CopyReadAssets() {
        AssetManager assetManager = getActivity().getAssets();
        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), "JECcontract.pdf");
        try {
            in = assetManager.open("JECcontract.pdf");
            out = getActivity().openFileOutput(file.getName(), getContext().MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/JECcontract.pdf"),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}