package com.sequencing.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sequencing.androidoauth.core.OAuth2Parameters;
import com.sequencing.androidoauth.core.connectto.ConnectToSQ;
import com.sequencing.androidoauth.core.connectto.SQConnectCallback;
import com.sequencing.androidoauth.core.connectto.SQConnectHandler;
import com.sequencing.androidoauth.core.importdata.AncestryImportHandler;
import com.sequencing.androidoauth.core.importdata.Import23AndMeHandler;
import com.sequencing.androidoauth.core.importdata.ImportCallback;
import com.sequencing.androidoauth.core.registration.SQRegistrationHandler;
import com.sequencing.fileselector.FileEntity;
import com.sequencing.fileselector.core.ISQFileCallback;
import com.sequencing.fileselector.core.SQUIFileSelectHandler;

import java.util.ArrayList;
import java.util.List;

public class SQApiActivity extends AppCompatActivity {

    private Button registrResetAccount;
    private Button btnFileSelector;
    private Button btnWebView;
    private Button btn23AndMeImport;
    private Button btnAncestryImport;
    private Button btnTestAppChains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqapi);

        registrResetAccount = (Button) findViewById(R.id.btnRegisterAccount);
        btnFileSelector = (Button) findViewById(R.id.btnFileSelector);
        btnWebView = (Button) findViewById(R.id.btnConnectToSequencing);
        btn23AndMeImport = (Button) findViewById(R.id.btn23);
        btnAncestryImport = (Button) findViewById(R.id.btnAncestry);
        btnTestAppChains = (Button) findViewById(R.id.btnAppChains);

        fileSelector();

        connectToSQ();

        registrationResetAccount();

        ancestryImport();

        init23AndMeImport();

        initTestAppChains();
    }

    private void initTestAppChains() {
        btnTestAppChains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestAppChainsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

    }

    private void connectToSQ() {
        SQConnectCallback sqConnectCallback = new SQConnectCallback() {
            @Override
            public void onSuccessConnect(String successMessage) {
                Toast.makeText(getApplicationContext(), "You has been connected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SQApiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }

            @Override
            public void onFailedConnect(String message) {
                Toast.makeText(getBaseContext(), "Error is happened", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SQApiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        };
        List<ConnectToSQ.File> files = new ArrayList<>();
        ConnectToSQ.File file = new ConnectToSQ.File()
                .setName("datafile1.dd")
                .setHashType("0")
                .setHashValue("0")
                .setSize("0")
                .setType("0")
                .setUrl("https://api.sequencing.com/download.ashx?id=0bea58b4-e28a-4cdf-b946-508f393878e4");

        files.add(file);

        ConnectToSQ connectToSQ = new ConnectToSQ.ConnectParametersBuilder()
                .withClientId("QfPiH2NPbn8eyilutdluPE6h_oudKebCaftGjQ14BpyJgSlJFLHlK_sjwBbHH3Qush4dXyD7IE0vQKs8e9xamw")
                .withEmail("omazurova@plexteq.com")
                .withFiles(files)
                .build();
        SQConnectHandler connectHandler = new SQConnectHandler(this);
        connectHandler.connectTo(btnWebView, sqConnectCallback, connectToSQ);
    }

    private void registrationResetAccount() {
        SQRegistrationHandler sqRegistrationHandler = new SQRegistrationHandler(this);
        sqRegistrationHandler.registerResetAccount(registrResetAccount, "RZw8FcGerU9e1hvS5E-iuMb8j8Qa9cxI-0vfXnVRGaMvMT3TcvJme-Pnmr635IoE434KXAjelp47BcWsCrhk0g");
    }

    private void fileSelector() {
        final SQUIFileSelectHandler fileSelectHandler = new SQUIFileSelectHandler(this);
        btnFileSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), new ISQFileCallback() {

                    @Override
                    public void onFileSelected(FileEntity entity, Activity activity) {
                        Toast toast = Toast.makeText(getApplicationContext(), entity.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }, "250444");
            }
        });

        btnFileSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), new ISQFileCallback() {

                    @Override
                    public void onFileSelected(FileEntity entity, Activity activity) {
                        Toast toast = Toast.makeText(getApplicationContext(), entity.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }, "250444", "shutterstock_v1126162.mp4");
            }
        });
    }

    private void ancestryImport() {
        ImportCallback importCallback = new ImportCallback() {
            @Override
            public void onSuccessImportStarted(String successMessage) {

            }

            @Override
            public void onFailedImport(String message) {

            }
        };
        AncestryImportHandler ancestryImportHandler = new AncestryImportHandler(this);
        ancestryImportHandler.importData(btnAncestryImport,  OAuth2Parameters.getInstance().getOauth().getToken().getAccessToken(), importCallback);
    }

    private void init23AndMeImport() {
        ImportCallback importCallback = new ImportCallback() {
            @Override
            public void onSuccessImportStarted(String successMessage) {
                Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailedImport(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        };
        Import23AndMeHandler import23AndMeHandler = new Import23AndMeHandler(this);
        import23AndMeHandler.importData(btn23AndMeImport, OAuth2Parameters.getInstance().getOauth().getToken().getAccessToken(), importCallback);
    }
}
