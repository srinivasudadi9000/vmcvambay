package m.srinivas.vmcvambay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.activation.DataHandler;

public class Splashscreen extends Activity {
    ImageView applogo_img;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    String allow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        applogo_img = (ImageView) findViewById(R.id.applogo_img);
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        applogo_img.startAnimation(slideUp);
       /* mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword("srinivasdadi9000@gmail.com", "Leeladadi@123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("success", "goodtask");
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(Splashscreen.this, "Email Already Exists.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });*/

        myRef = FirebaseDatabase.getInstance().getReference("vmcvambay");
        // myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot issuesnapshot : dataSnapshot.getChildren()) {
                    SharedPreferences.Editor s = getSharedPreferences("allow", MODE_PRIVATE).edit();
                    Log.d("superbinloop", issuesnapshot.getValue().toString());
                    if (issuesnapshot.getValue().toString().equals("{name={-LIaIzcFoOXjB4T9z7kb={name=daditrick}}}")) {
                        allow = "true";
                        s.putString("status", "true");
                        s.commit();
                    } else {
                        Log.d("superbinloop", "superra");
                        s.putString("status", "false");
                        s.commit();
                        allow = "false";
                        // System.exit(0);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("superb_dataerr", databaseError.toString());
            }
        });
              /* myRef = myRef.child("First").child("name");
               String id = myRef.push().getKey();
               EventItem aUser = new EventItem("dadi Nilsson");
               myRef.child(id).setValue(aUser);*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences validuser = getSharedPreferences("validuser",MODE_PRIVATE);
                if (validuser.getString("name","").equals("true")){
                    Intent plotsurvey = new Intent(Splashscreen.this,Plotsurvey.class);
                    startActivity(plotsurvey);
                }else {
                    Intent plotsurvey = new Intent(Splashscreen.this,Login.class);
                    startActivity(plotsurvey);
                }
            }
        },3000);


    }
}
