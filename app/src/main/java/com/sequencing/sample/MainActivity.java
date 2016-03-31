package com.sequencing.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.sequencing.androidoauth.core.ISQAuthCallback;
import com.sequencing.androidoauth.core.OAuth2Parameters;
import com.sequencing.androidoauth.core.SQUIoAuthHandler;
import com.sequencing.fileselector.FileEntity;
import com.sequencing.fileselector.core.ISQFileCallback;
import com.sequencing.fileselector.core.SQUIFileSelectHandler;
import com.sequencing.oauth.config.AuthenticationParameters;
import com.sequencing.oauth.core.Token;

public class MainActivity extends AppCompatActivity implements ISQAuthCallback, ISQFileCallback{

    private static final String TAG = "MainActivity";
    private FloatingActionButton fab;
    private Button btnSiginSequencing;
    private SQUIoAuthHandler ioAuthHandler;
    private SQUIFileSelectHandler fileSelectHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSiginSequencing = (Button) findViewById(R.id.btnSigninSequencing);


        AuthenticationParameters parameters = new AuthenticationParameters.ConfigurationBuilder()
                .withRedirectUri("authapp://Default/Authcallback")
                .withClientId("oAuth2 Demo ObjectiveC")
                .withClientSecret("RZw8FcGerU9e1hvS5E-iuMb8j8Qa9cxI-0vfXnVRGaMvMT3TcvJme-Pnmr635IoE434KXAjelp47BcWsCrhk0g")
                .build();

        ioAuthHandler = new SQUIoAuthHandler(this);
        ioAuthHandler.authenticate(btnSiginSequencing, this, parameters);

        fileSelectHandler = new SQUIFileSelectHandler(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), MainActivity.this);
            }
        });
        if (OAuth2Parameters.getInstance().getOauth().isAuthorized()) {
            fab.setVisibility(View.VISIBLE);
            Snackbar.make(fab, "Now you can run file selector", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthentication(Token token) {
        Log.i(TAG, "Authenticated");
        Toast.makeText(getApplicationContext(), "You has been authenticated", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void onFailedAuthentication(Exception e) {
        Log.w(TAG, "Failure to authenticate user");
    }

    @Override
    public void onFileSelected(FileEntity entity, Activity activity) {
        Log.i(TAG, "File has been selected");

        Toast.makeText(getApplicationContext(), entity.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}
