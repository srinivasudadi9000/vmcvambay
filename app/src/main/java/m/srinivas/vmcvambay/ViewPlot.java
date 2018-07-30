package m.srinivas.vmcvambay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewPlot extends Activity {
    ImageView app_family_img, house_img_copy, bankloan_img_copy, patta_img_copy, back_img;
    TextView ration_no_tv, door_no_tv, bio_tv, aadhar_no, mobile_number, con_f_tv, con_tv, patta_no_tv, fhn_tv,
            ob_name_tv, plot_no_tv, category_tv, areaname_tv, ward_no_tv,header_tv,officerid_tv;
    ProgressDialog progress;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher_round)
                .showImageForEmptyUri(R.drawable.clear)
                .showImageOnFail(R.drawable.dummy)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        officerid_tv = (TextView) findViewById(R.id.officerid_tv);
        ration_no_tv = (TextView) findViewById(R.id.ration_no_tv);
        door_no_tv = (TextView) findViewById(R.id.door_no_tv);
        bio_tv = (TextView) findViewById(R.id.bio_tv);
        con_f_tv = (TextView) findViewById(R.id.con_f_tv);
        con_tv = (TextView) findViewById(R.id.con_tv);
        patta_no_tv = (TextView) findViewById(R.id.patta_no_tv);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        fhn_tv = (TextView) findViewById(R.id.fhn_tv);
        aadhar_no = (TextView) findViewById(R.id.aadhar_no);
        ob_name_tv = (TextView) findViewById(R.id.ob_name_tv);
        plot_no_tv = (TextView) findViewById(R.id.plot_no_tv);
        category_tv = (TextView) findViewById(R.id.category_tv);
        areaname_tv = (TextView) findViewById(R.id.areaname_tv);
        ward_no_tv = (TextView) findViewById(R.id.ward_no_tv);
        header_tv = (TextView) findViewById(R.id.header_tv);
        header_tv.setText(" Vambay Colony Single Plot Details");
        back_img = (ImageView) findViewById(R.id.back_img);
        app_family_img = (ImageView) findViewById(R.id.app_family_img);
        house_img_copy = (ImageView) findViewById(R.id.house_img_copy);
        bankloan_img_copy = (ImageView) findViewById(R.id.bankloan_img_copy);
        patta_img_copy = (ImageView) findViewById(R.id.patta_img_copy);
        // logout = (TextView) findViewById(R.id.dashbord_logout);

        progress = new ProgressDialog(ViewPlot.this);
        progress.setMessage("Fetching data from server..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        if (internet()) {
            new ViewPlot.getstatus(getIntent().getStringExtra("intregid").toString()).execute();
            officerid_tv.setText(getIntent().getStringExtra("intregid").toString());
        } else {
            showDialog(ViewPlot.this,"Please Check Your Internet Connection..!!", "not");
        }

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class getstatus extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String id, officerid;

        public getstatus(String grievanceid) {
            this.id = grievanceid;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... arg0) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("intRegid", id));
            json = JSONParser.makeServiceCall("http://104.217.254.77/BZAVC/VambayFullDetailsService.aspx", 1, nameValuePairs);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();
            progress.dismiss();
            try {

                if (json.getString("ViewVambayFullDetails").equals("success")){
                    JSONArray jsonObject = json.getJSONArray("result");
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject value = jsonObject.getJSONObject(i);
                        //    Toast.makeText(getApplicationContext(), value.getString("intUserid").toString(), Toast.LENGTH_SHORT).show();
                        //application_no.setText(value.getString("RegNo"));
                        areaname_tv.setText(value.getString("Areaname"));
                        category_tv.setText(value.getString("Sector"));
                        mobile_number.setText(value.getString("PlotNumber"));
                        ob_name_tv.setText(value.getString("OriginalName"));
                        fhn_tv.setText(value.getString("OriginalFatherName"));
                        con_tv.setText(value.getString("CurrentName"));
                        con_f_tv.setText(value.getString("CurrentFatherName"));
                        plot_no_tv.setText(value.getString("Pattano"));
                        aadhar_no.setText(value.getString("Aadhar"));
                        mobile_number.setText(value.getString("MobileNo"));
                        door_no_tv.setText(value.getString("HDrno"));
                        patta_no_tv.setText(value.getString("Pattano"));
                        ration_no_tv.setText(value.getString("ARationcard"));

                        ward_no_tv.setText(value.getString("WardNo"));
                        bio_tv.setText(value.getString("BSurveyID"));

                       /* Picasso.with(ViewPlot.this)
                                .load("http://" + value.getString("HouseFile"))
                                .resize(100, 100)
                                //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(house_img_copy);
                        Picasso.with(ViewPlot.this)
                                .load("http://" + value.getString("BankLoan"))
                                .resize(100, 100)
                                //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(bankloan_img_copy);
                        Picasso.with(ViewPlot.this)
                                .load("http://" + value.getString("Patta"))
                                .resize(100, 100)
                                //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(patta_img_copy);
                        Picasso.with(ViewPlot.this)
                                .load("http://" + value.getString("House File"))
                                .resize(100, 100)
                                //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(app_family_img);*/

                        ImageLoader.getInstance()
                                .displayImage( value.getString("HouseFile")
                                        , house_img_copy, options);
                        ImageLoader.getInstance()
                                .displayImage(value.getString("BankLoan")
                                        , bankloan_img_copy, options);
                        ImageLoader.getInstance()
                                .displayImage( value.getString("Patta")
                                        , patta_img_copy, options);
                        ImageLoader.getInstance()
                                .displayImage(value.getString("House File")
                                        , app_family_img, options);



                    }
                }else {

                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
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
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

}
