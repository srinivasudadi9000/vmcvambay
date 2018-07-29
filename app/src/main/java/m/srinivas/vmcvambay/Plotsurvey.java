package m.srinivas.vmcvambay;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import static android.content.ContentValues.TAG;

public class Plotsurvey extends Activity implements View.OnClickListener {
    Spinner categroy_no, ward;
    String cat_txt, ward_no_txt, selectmyimages, userChoosenTask;
    ArrayList<String> wardno = new ArrayList<String>();
    Button submit_btn, cancel_btn;
    ImageView patta_img, bankloan_img, house_img, family_img;
    private Bitmap bitmap;
    Bitmap scaledBitmap = null, scaledBitmap2 = null, scaledBitmap3 = null, scaledBitmap4 = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ProgressDialog progress;
    String latitude, longitude;
    GPSTracker gps;
    EditText areaname, plotnumber, obn, fhn, patta_no, con, fhn_con, aadharno, mobile_no, biometric_no, door_no, ration_no;
    ImageView logout_img, all_img;
    SharedPreferences ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plotsurvey);
        ss = getSharedPreferences("allow", MODE_PRIVATE);
        patta_img = (ImageView) findViewById(R.id.patta_img);
        patta_img.setOnClickListener(Plotsurvey.this);
        bankloan_img = (ImageView) findViewById(R.id.bankloan_img);
        bankloan_img.setOnClickListener(this);
        house_img = (ImageView) findViewById(R.id.house_img);
        house_img.setOnClickListener(this);
        family_img = (ImageView) findViewById(R.id.family_img);
        family_img.setOnClickListener(this);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);
        logout_img = (ImageView) findViewById(R.id.logout_img);
        logout_img.setOnClickListener(this);
        all_img = (ImageView) findViewById(R.id.all_img);
        all_img.setOnClickListener(this);

        areaname = (EditText) findViewById(R.id.areaname);
        plotnumber = (EditText) findViewById(R.id.plotnumber);
        obn = (EditText) findViewById(R.id.obn);
        fhn = (EditText) findViewById(R.id.fhn);
        patta_no = (EditText) findViewById(R.id.patta_no);
        con = (EditText) findViewById(R.id.con);
        fhn_con = (EditText) findViewById(R.id.fhn_con);
        aadharno = (EditText) findViewById(R.id.aadharno);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        biometric_no = (EditText) findViewById(R.id.biometric_no);
        door_no = (EditText) findViewById(R.id.door_no);
        ration_no = (EditText) findViewById(R.id.ration_no);
        Log.d("helloooo",ss.getString("status",""));
        if (ss.getString("status","").equals("true")){

        }else {
            Intent notallow = new Intent(Plotsurvey.this, AllowError.class);
            startActivity(notallow);
            finish();
        }
        categroy_no = (Spinner) findViewById(R.id.categroy_no);
        ward = (Spinner) findViewById(R.id.ward);
        ArrayAdapter<String> cat = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , getResources().getStringArray(R.array.categroy_no));
        categroy_no.setAdapter(cat);
        selectmyimages = "novalue";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }
        wardno.add("Ward No");
        for (int i = 1; i < 59; i++) {

            wardno.add(String.valueOf(i));
        }

        //get latitude and longitude
        gps = new GPSTracker(this);
        if (!gps.isGPSEnabled && !gps.isNetworkEnabled) {
            Log.d("networkd", "false");
            showSettingsAlert();
        } else {
            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            // Toast.makeText(getBaseContext(),latitude+" "+longitude  ,Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> plot_number = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
                , wardno);
        ward.setAdapter(plot_number);
        ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ward_no_txt = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ward_no_txt = "--Select--";
            }
        });
        categroy_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat_txt = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cat_txt = "--Select--";
            }
        });

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

              /*  Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                File finalFile = new File(getRealPathFromURI(tempUri));
                //  compressImage(finalFile.getAbsolutePath().toString());*/

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (selectmyimages) {
                    case "selectmyimage1":
                        //    uploadImage();
                        patta_img.setMaxWidth(150);
                        patta_img.setMaxHeight(150);
                        // new MainActivity.JSONParsedoitfast(scaledBitmap,"one").execute();
                        scaledBitmap = bitmap;
                        patta_img.setImageBitmap(bitmap);
                        break;
                    case "selectmyimage2":
                        //    uploadImage();
                        bankloan_img.setMaxWidth(150);
                        bankloan_img.setMaxHeight(150);
                        //   new MainActivity.JSONParsedoitfast(scaledBitmap2,"two").execute();
                        scaledBitmap2 = bitmap;
                        bankloan_img.setImageBitmap(bitmap);
                        break;
                    case "selectmyimage3":
                        //    uploadImage();
                        house_img.setMaxWidth(150);
                        house_img.setMaxHeight(150);
                        //  new MainActivity.JSONParsedoitfast(scaledBitmap3,"three").execute();
                        scaledBitmap3 = bitmap;
                        house_img.setImageBitmap(bitmap);
                        break;
                    case "selectmyimage4":
                        //    uploadImage();
                        family_img.setMaxWidth(150);
                        family_img.setMaxHeight(150);
                        //  new MainActivity.JSONParsedoitfast(scaledBitmap3,"three").execute();
                        scaledBitmap4 = bitmap;
                        family_img.setImageBitmap(bitmap);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("myimage", encodedImage.toString());
        return encodedImage;
    }

    private void uploadImage(final String remarks) {

        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);
                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(json.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject value = jsonArray.getJSONObject(i);
                        if (value.getString("CreateEvent").equals("success")) {
                            showDialog(Plotsurvey.this, "Successfully Grievance Record Updated ", "yes");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog(Plotsurvey.this, "Please Try Again ", "no");
                }

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                // Bitmap bitmap1 = params[0];
                String uploadImage1, uploadImage2, uploadImage3, uploadImage4;

                if (scaledBitmap == null) {
                    uploadImage1 = "nullimage";
                } else {
                    uploadImage1 = getStringImage(scaledBitmap);
                }
                if (scaledBitmap2 == null) {
                    uploadImage2 = "nullimage";
                } else {
                    uploadImage2 = getStringImage(scaledBitmap2);
                }
                if (scaledBitmap3 == null) {
                    uploadImage3 = "nullimage";
                } else {
                    uploadImage3 = getStringImage(scaledBitmap3);
                }
                if (scaledBitmap4 == null) {
                    uploadImage4 = "nullimage";
                } else {
                    uploadImage4 = getStringImage(scaledBitmap4);
                }
                String latitude = String.valueOf(gps.getLatitude());
                String longitude = String.valueOf(gps.getLongitude());

                // Toast.makeText(getBaseContext(),latitude+" "+longitude  ,Toast.LENGTH_SHORT).show();

                //SharedPreferences sharedPreferences1 = getSharedPreferences("app_info", MODE_PRIVATE);
                HashMap<String, String> data = new HashMap<>();

                // data.put(UPLOAD_KEY, uploadImage);
                /// data.put("intGrivanceid", sharedPreferences1.getString("intGrivanceid", ""));
                ///data.put("App_No", sharedPreferences1.getString("App_No", ""));
                //data.put("Status", App_status);
                //  data.put("Status", "Pending");
                //data.put("remarks", remarks);
                //data.put("remarks", "REMARKS");
                data.put("WardNo", ward_no_txt);
                data.put("AreaName", areaname.getText().toString());
                data.put("SectorName", cat_txt);
                data.put("PlotNumber", plotnumber.getText().toString());
                data.put("OriginalBenName", obn.getText().toString());
                data.put("OriginalBenFName", fhn.getText().toString());
                data.put("CurrentBenName", con.getText().toString());
                data.put("CurrentBenFathername", fhn_con.getText().toString());
                data.put("PattaNo", patta_no.getText().toString());
                data.put("AadharNumber", aadharno.getText().toString());
                data.put("MobileNumber", mobile_no.getText().toString());
                if (biometric_no.getText().toString().length() == 0) {
                    data.put("IsBiometricSurveyDone", "no");
                    data.put("BiometricID", "");
                } else {
                    data.put("IsBiometricSurveyDone", "yes");
                    data.put("BiometricID", biometric_no.getText().toString());
                }
                data.put("DoorNo", door_no.getText().toString());
                data.put("RationCardNo", ration_no.getText().toString());
                data.put("GLatitude", latitude);
                data.put("GLangitude", longitude);
                data.put("intOfficerid", "1000");
                data.put("PattaFileName", "PattaFileName");
                data.put("PattaFilePath", uploadImage1);
                data.put("BankLoanPaidName", "BankLoanPaidName");
                data.put("BankLoanPaidPath", uploadImage2);
                data.put("HousePhotoName", "HousePhotoName");
                data.put("HousePhotoPath", uploadImage3);
                data.put("ApplFamilyPhotoName", "ApplFamilyPhotoName");
                data.put("ApplFamilyPhotoPath", uploadImage4);

                System.out.print(data.toString());
                String result = rh.sendPostRequest("http://104.217.254.77/BZAVC/InsertVambayRegistration.aspx", data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Plotsurvey.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

      /*  Uri tempUri = getImageUri(updatestatus.this, thumbnail);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));*/
        bitmap = (Bitmap) data.getExtras().get("data");
        // compressImage(finalFile.getAbsolutePath().toString());
        // ivImage.setImageBitmap(scaledBitmap);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (selectmyimages) {
            case "selectmyimage1":
                // uploadImage();
                // new MainActivity.JSONParsedoitfast(scaledBitmap,"one").execute();
                patta_img.setMaxWidth(150);
                patta_img.setMaxHeight(150);
                patta_img.setImageBitmap(bitmap);
                scaledBitmap = bitmap;
                break;
            case "selectmyimage2":
                //  uploadImage();
                //   new MainActivity.JSONParsedoitfast(scaledBitmap2,"two").execute();
                bankloan_img.setMaxWidth(150);
                bankloan_img.setMaxHeight(150);
                bankloan_img.setImageBitmap(bitmap);
                scaledBitmap2 = bitmap;
                break;
            case "selectmyimage3":
                //   uploadImage();
                //  new MainActivity.JSONParsedoitfast(scaledBitmap3,"three").execute();
                house_img.setMaxWidth(150);
                house_img.setMaxHeight(150);
                house_img.setImageBitmap(bitmap);
                scaledBitmap3 = bitmap;
                break;
            case "selectmyimage4":
                //   uploadImage();
                //  new MainActivity.JSONParsedoitfast(scaledBitmap3,"three").execute();
                family_img.setMaxWidth(150);
                family_img.setMaxHeight(150);
                family_img.setImageBitmap(bitmap);
                scaledBitmap4 = bitmap;
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.patta_img:
                selectmyimages = "selectmyimage1";
                selectImage();
                break;
            case R.id.bankloan_img:
                selectmyimages = "selectmyimage2";
                selectImage();
                break;
            case R.id.house_img:
                selectmyimages = "selectmyimage3";
                selectImage();
                break;
            case R.id.family_img:
                selectmyimages = "selectmyimage4";
                selectImage();
                break;
            case R.id.all_img:
                Intent dashboard = new Intent(Plotsurvey.this, DashboardView.class);
                startActivity(dashboard);
                break;
            case R.id.logout_img:
                SharedPreferences ss = getSharedPreferences("validuser", MODE_PRIVATE);
                SharedPreferences.Editor ee = ss.edit();
                ee.putString("name", "false");
                ee.commit();
                Intent intent = new Intent(Plotsurvey.this, Splashscreen.class);
                startActivity(intent);
                finish();
                break;
            case R.id.submit_btn:
                if (cat_txt.equals("Category")) {
                    showDialog(this, "Please Select Category", "no");
                } else if (ward_no_txt.equals("Ward No")) {
                    showDialog(this, "Please Select Ward No", "no");
                } else if (areaname.getText().toString().length() == 0) {
                    showDialog(this, "Area Name Should Not Be Empty", "no");
                } else if (plotnumber.getText().toString().length() == 0) {
                    showDialog(this, "Plot Number Should Not Be Empty", "no");
                } else if (obn.getText().toString().length() == 0) {
                    showDialog(this, "Original Beneficiary Name Should Not Be Empty", "no");
                } else if (fhn.getText().toString().length() == 0) {
                    showDialog(this, "Original Father / Husband Name Should Not Be Empty", "no");
                } else if (patta_no.getText().toString().length() == 0) {
                    showDialog(this, "Patta Number Should Not Be Empty", "no");
                } else if (con.getText().toString().length() == 0) {
                    showDialog(this, "Current Occupier Name Should Not Be Empty", "no");
                } else if (fhn_con.getText().toString().length() == 0) {
                    showDialog(this, "Current Father / Husband Name Should Not Be Empty", "no");
                } else if (aadharno.getText().toString().length() == 0) {
                    showDialog(this, "Aadhar Number Should Not Be Empty", "no");
                } else if (mobile_no.getText().toString().length() == 0) {
                    showDialog(this, "Mobile Number Should Not Be Empty", "no");
                } else if (door_no.getText().toString().length() == 0) {
                    showDialog(this, "Door Number Should Not Be Empty", "no");
                } else if (ration_no.getText().toString().length() == 0) {
                    showDialog(this, "Ration Card Number Should Not Be Empty", "no");
                } else if (scaledBitmap == null) {
                    showDialog(this, "Please Take Patta Photo Copy", "no");
                } else if (scaledBitmap2 == null) {
                    showDialog(this, "Please Take Bank Load Paid Receipt", "no");
                } else if (scaledBitmap3 == null) {
                    showDialog(this, "Please Take Photo Of The House ", "no");
                } else if (scaledBitmap4 == null) {
                    showDialog(this, "Please Take Applicant Family Photo", "no");
                } else {
                    progress = new ProgressDialog(Plotsurvey.this);
                    progress.setMessage("Uploading data to server..");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();
                    if (internet()) {
                        if (String.valueOf(gps.getLatitude()).equals("0.00")) {
                            progress.dismiss();
                            showSettingsAlert();
                        } else {

                            uploadImage("uploaddata");
                        }
                    } else {
                        progress.dismiss();
                        showDialog(Plotsurvey.this, "Please Check Your Internet Connection Before submission ", "no");
                    }
                }


                break;
            case R.id.cancel_btn:
                Intent i = new Intent(Plotsurvey.this, Plotsurvey.class);
                startActivity(i);
                finish();
                break;

        }

    }

    public Boolean internet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Plotsurvey.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showDialog(Activity activity, String msg, final String status) {
        final Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        ImageView b = (ImageView) dialog.findViewById(R.id.b);
        if (status.equals("yes")) {
            b.setVisibility(View.VISIBLE);
        } else {
            b.setVisibility(View.GONE);
        }
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("yes")) {
                    Intent ii = new Intent(getApplicationContext(), Plotsurvey.class);
                    startActivity(ii);
                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }


}
