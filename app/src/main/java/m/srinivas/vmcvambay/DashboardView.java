package m.srinivas.vmcvambay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardView extends Activity implements View.OnClickListener{
RecyclerView recyler_dashboard;
    ArrayList<Drilldown> drilldowns;
    RecyclerView dashboardDril_list;
    ImageView back_img;
    TextView header_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_view);
        back_img = (ImageView) findViewById(R.id.back_img);
        header_tv = (TextView) findViewById(R.id.header_tv);
        header_tv.setText("Vambay Colony Plots List");
        recyler_dashboard = (RecyclerView) findViewById(R.id.recyler_dashboard);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyler_dashboard.setLayoutManager(layoutManager);

        drilldowns = new ArrayList<Drilldown>();
        recyler_dashboard.addOnItemTouchListener(new DrawerItemClickListener());
        SharedPreferences sharedPreferences = getSharedPreferences("Userinfo", MODE_PRIVATE);
        if (internet()){
            // Toast.makeText(getBaseContext(),"internet connected",Toast.LENGTH_SHORT).show();
            new DashboardView.getstatus(sharedPreferences.getString("intofficerid", null)).execute();

        }else {
            showDialog(DashboardView.this,"Please Check Your Internet Connection...!!","not");
        }

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.header_tv:
                header_tv.setText("Vambay Colony Plots List");
                break;
        }

    }

    private class DrawerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && gestureDetector.onTouchEvent(e)) {
                int position = rv.getChildAdapterPosition(child);
                //  Toast.makeText(getBaseContext(),drilldowns.get(position).getApplicationno(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardView.this,ViewPlot.class);
                 i.putExtra("intregid",drilldowns.get(position).getIntRegid().toString());
                startActivity(i);
               // finish();

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class getstatus extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        private ArrayList<NameValuePair> nameValuePairs;
        private JSONObject json;
        String id;

        public getstatus(String officerid) {
            this.id = officerid;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... arg0) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("intOfficerid", "1000"));
           // nameValuePairs.add(new BasicNameValuePair("stage", stage));
            json = JSONParser.makeServiceCall("http://104.217.254.77/BZAVC/ViewVambayDetailsService.aspx", 1, nameValuePairs);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();
            //  progress.dismiss();
            try {
                if (json.getString("status").equals("1")){
                    JSONArray jsonObject = json.getJSONArray("result");
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject value = jsonObject.getJSONObject(i);
                        String xx = String.valueOf(jsonObject.length());
                        //  Toast.makeText(getApplicationContext(), xx.toString(), Toast.LENGTH_SHORT).show();

                        drilldowns.add(new Drilldown(value.getString("RegNo"),value.getString("intRegid"),
                                value.getString("Areaname"),value.getString("Sector"),
                                value.getString("PlotNumber"),value.getString("OriginalName"),
                                value.getString("CurrentName")));

                    }
                    RecyclerView.Adapter adapter = new DrilldownRecycler(drilldowns,DashboardView.this);
                    recyler_dashboard.setAdapter(adapter);
                }else {
                    showDialog(DashboardView.this,"Server Busy At This Moment !!","no");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showDialog(DashboardView.this,"Server Busy At This Moment !!","no");
            }
        }
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
    public Boolean internet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }
    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear YOUR_APP_PACKAGE_GOES HERE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
