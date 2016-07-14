package com.sequencing.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sequencing.androidoauth.core.OAuth2Parameters;
import com.sequencing.appchains.AndroidAppChainsImpl;
import com.sequencing.appchains.AppChains;
import com.sequencing.appchains.DefaultAppChainsImpl.Report;
import com.sequencing.appchains.DefaultAppChainsImpl.Result;
import com.sequencing.appchains.DefaultAppChainsImpl.ResultType;
import com.sequencing.appchains.DefaultAppChainsImpl.TextResultValue;
import com.sequencing.fileselector.FileEntity;
import com.sequencing.fileselector.core.ISQFileCallback;
import com.sequencing.fileselector.core.SQUIFileSelectHandler;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestAppChainsActivity extends AppCompatActivity implements ISQFileCallback, View.OnClickListener {

    private static final String TAG = "TestAppChainsActivity";

    private SQUIFileSelectHandler fileSelectHandler;

    private Button btnSelectFile;
    private Button btnVitaminD;
    private Button btnMelanomaRisk;
    private TextView tvTitle;
    private TextView tvFileName;
    private TextView tvResult;

    private String selectedFileId;
    private FileEntity entity;

    private AsyncTaskChain9 asyncTaskChain9;
    private AsyncTaskChain88 asyncTaskChain88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_app_chains);

        btnSelectFile = (Button) findViewById(R.id.btnSelectFile);
        btnSelectFile.setOnClickListener(this);

        btnVitaminD = (Button) findViewById(R.id.btnVitaminD);
        btnVitaminD.setOnClickListener(this);

        btnMelanomaRisk = (Button) findViewById(R.id.btnMelanomaRisk);
        btnMelanomaRisk.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvFileName = (TextView) findViewById(R.id.tvFileName);
        tvResult = (TextView) findViewById(R.id.tvResult);

        fileSelectHandler = new SQUIFileSelectHandler(this);
    }

    @Override
    public void onFileSelected(FileEntity entity, Activity activity) {
        Log.i(TAG, "File " + entity.getFriendlyDesc1() + " has been selected");
        activity.finish();
        this.entity = entity;
        selectedFileId = entity.getId();

        tvFileName.setText(entity.getFriendlyDesc1() + " - " + entity.getFriendlyDesc2());

        btnVitaminD.setVisibility(View.VISIBLE);
        btnMelanomaRisk.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvFileName.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnSelectFile:
                fileSelectHandler.selectFile(OAuth2Parameters.getInstance().getOauth(), this, true, selectedFileId);
                tvResult.setVisibility(View.GONE);
            break;

            case R.id.btnVitaminD:
                tvResult.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Computing result...", Toast.LENGTH_LONG).show();

                asyncTaskChain88 = new AsyncTaskChain88();
                asyncTaskChain88.execute();
                break;

            case R.id.btnMelanomaRisk:
                tvResult.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Computing result...", Toast.LENGTH_LONG).show();

                asyncTaskChain9 = new AsyncTaskChain9();
                asyncTaskChain9.execute();
                break;
        }
    }

    private boolean hasVitD() {
            AppChains chains = new AndroidAppChainsImpl(OAuth2Parameters.getInstance().getOauth().getToken().getAccessToken(), "api.sequencing.com");
            Report resultChain88;
            try {
                resultChain88 = chains.getReport("StartApp", "Chain88", entity.getId());
            } catch (Exception e) {
                Toast.makeText(this, "Failure to get availability of vitamin D, please try again", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (resultChain88.isSucceeded() == false) {
                Toast.makeText(this, "Failure to get availability of vitamin D, please try again", Toast.LENGTH_SHORT).show();
                return false;
            }

            boolean result = false;
            for (Result r : resultChain88.getResults()) {
                ResultType type = r.getValue().getType();
                if (type == ResultType.TEXT) {
                    TextResultValue v = (TextResultValue) r.getValue();

                    if (r.getName().equals("result"))
                        result = v.getData().equals("No") ? false : true;
                }
            }

        return result;
    }

    private String getMelanomaRisk() {
        AppChains chains = new AndroidAppChainsImpl(OAuth2Parameters.getInstance().getOauth().getToken().getAccessToken(), "api.sequencing.com");
        Report resultChain9;

        try {
            resultChain9 = chains.getReport("StartApp", "Chain9", entity.getId());
        } catch (Exception e) {
            Toast.makeText(this, "Failure to get melanoma risk, please try again", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (resultChain9.isSucceeded() == false) {
            Toast.makeText(this, "Failure to get melanoma risk, please try again", Toast.LENGTH_SHORT).show();
            return null;
        }

        for (Result r : resultChain9.getResults()) {
            ResultType type = r.getValue().getType();
            if (type == ResultType.TEXT) {
                TextResultValue v = (TextResultValue) r.getValue();

                if (r.getName().equals("RiskDescription"))
                    return v.getData();
            }
        }

        return null;
    }

    class AsyncTaskChain9 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return getMelanomaRisk();
        }

        @Override
        protected void onPostExecute(String result) {
            tvResult.setVisibility(View.VISIBLE);
            tvResult.setText("Melanoma issue level is: " + result);
        }
    }

    class AsyncTaskChain88 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return hasVitD();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            tvResult.setVisibility(View.VISIBLE);
            if(result)
                tvResult.setText("There is issue with vitamin D");
            else
                tvResult.setText("There is no issue with vitamin D");
        }
    }
}
