package com.muevetuweb.howmuchtocharge;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {
    private SeekBar skb1,skb2,skb3,skb4;
    private TextView lblItemDescription,lblResult,lblCosts;
    private EditText txfCosts;
    private float clientVal=1,prestigeVal=1,impactVal=1,competitionVal=1,costsVal=1;
    DecimalFormat formatoMoneda = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lblItemDescription = (TextView) findViewById(R.id.lblItemDescription);
        lblResult = (TextView) findViewById(R.id.lblResult);
        lblCosts = (TextView) findViewById(R.id.lblCostos);

        txfCosts = (EditText) findViewById(R.id.txfCosts);

        skb1 = (SeekBar) findViewById(R.id.barClient);
        skb2 = (SeekBar) findViewById(R.id.barPrestige);
        skb3 = (SeekBar) findViewById(R.id.barImpact);
        skb4 = (SeekBar) findViewById(R.id.barCompetition);

        skb1.setProgress(0);
        skb2.setProgress(0);
        skb3.setProgress(0);
        skb4.setProgress(0);

        skb1.setOnSeekBarChangeListener(listenerSeek());
        skb2.setOnSeekBarChangeListener(listenerSeek());
        skb3.setOnSeekBarChangeListener(listenerSeek());
        skb4.setOnSeekBarChangeListener(listenerSeek());

        lblCosts.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.costDescription,Toast.LENGTH_LONG).show();
            }
        });

        txfCosts.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String costs =txfCosts.getText().toString();
                if (!costs.isEmpty())
                    costsVal = Float.parseFloat(costs);
                String valorFormateado = formatoMoneda.format(Math.round(calculate()));
                lblResult.setText("Deberias cobrar : $" + valorFormateado);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public SeekBar.OnSeekBarChangeListener listenerSeek(){
        return new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String[] arrayDescriptions;

                String idSeekBar = getResources().getResourceName(seekBar.getId());
                idSeekBar = idSeekBar.substring(idSeekBar.indexOf("/")+1,idSeekBar.length());

                switch (idSeekBar){
                    case "barClient":
                        arrayDescriptions = getResources().getStringArray(R.array.ClientDescription);
                        break;
                    case "barPrestige":
                        arrayDescriptions = getResources().getStringArray(R.array.PrestigeDescription);
                        break;
                    case "barImpact":
                        arrayDescriptions = getResources().getStringArray(R.array.ImpactDescription);
                        break;
                    case "barCompetition":
                        arrayDescriptions = getResources().getStringArray(R.array.CompetitionDescription);
                        break;
                    default:
                        arrayDescriptions = getResources().getStringArray(R.array.Definitions);
                        break;
                }
                setValues(idSeekBar,progress);
                String description = arrayDescriptions[progress].toString();
                lblItemDescription.setText(description);

                String valorFormateado = formatoMoneda.format(Math.round(calculate()));
                lblResult.setText("Deberias cobrar : $" + valorFormateado);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };
    }

    public void setValues(String barId,int val){
        String[] arrayValues;
        String costs =txfCosts.getText().toString();
        if (!costs.isEmpty())
            costsVal = Float.parseFloat(costs);

        switch (barId){
            case "barClient":
                arrayValues = getResources().getStringArray(R.array.ClientType);
                clientVal = Float.parseFloat(arrayValues[val]);
                break;
            case "barPrestige":
                arrayValues = getResources().getStringArray(R.array.PrestigeValue);
                prestigeVal = Float.parseFloat(arrayValues[val]);
                break;
            case "barImpact":
                arrayValues = getResources().getStringArray(R.array.ProjectImpact);
                impactVal = Float.parseFloat(arrayValues[val]);
                break;
            case "barCompetition":
                arrayValues = getResources().getStringArray(R.array.Competition);
                competitionVal = Float.parseFloat(arrayValues[val]);
                break;
            default:

                break;
        }
    }

    public float calculate(){
        if(costsVal>1){
            float fc = (costsVal*clientVal*prestigeVal*impactVal)/competitionVal;
            return (fc<costsVal)?costsVal:fc;
        }
        return 0;
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
}
