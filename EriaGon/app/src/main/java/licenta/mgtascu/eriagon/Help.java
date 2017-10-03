package licenta.mgtascu.eriagon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Help extends AppCompatActivity {

    TextView outPut;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        outPut = (TextView) findViewById(R.id.outPut);
        backBtn = (Button) findViewById(R.id.backB);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        displayOutput();
    }


    public void displayOutput() {

        BufferedReader br = null;
        StringBuilder text = new StringBuilder();

        try {
            InputStream is = getAssets().open("output");
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "File is null!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
        outPut.setText(text);
        outPut.setMovementMethod(new ScrollingMovementMethod());
    }
}
