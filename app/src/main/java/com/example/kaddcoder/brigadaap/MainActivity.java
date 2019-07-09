package com.example.kaddcoder.brigadaap;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private AlertDialog popup;
    private ImageButton instagram_btn;
    private ImageButton gmail_btn;
    private Button sortir;
    private Button contact_btn;
    private Button entrar;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        entrar = findViewById(R.id.l2);
        contact_btn = (Button) findViewById(R.id.contact_button);
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactPopUp();
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn();

            }
        });

        TextView l1 = (TextView) findViewById(R.id.l1);
        Button l2 = (Button) findViewById(R.id.l2);
        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Toast.makeText(MainActivity.this, "Ja estaves dintre!", Toast.LENGTH_SHORT).show();
            Intent MenuIntent = new Intent(MainActivity.this, Menu.class);
            MainActivity.this.startActivity(MenuIntent);
            startActivity(MenuIntent);
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully
            Toast.makeText(MainActivity.this, account.getEmail() + " autenticat!", Toast.LENGTH_LONG).show();

            Intent MenuIntent = new Intent(MainActivity.this, Menu.class);
            MainActivity.this.startActivity(MenuIntent);
            startActivity(MenuIntent);

        } catch (ApiException e) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "Usuari no autenticat", Toast.LENGTH_LONG).show();
        }

    }


    private void contactPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.contact, null);
        builder.setView(customLayout);

        instagram_btn = (ImageButton) customLayout.findViewById(R.id.instagram);
        gmail_btn = (ImageButton) customLayout.findViewById(R.id.gmail);

        instagram_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/brigada_antipuercos_tgn");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });

        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_SENDTO)
                        .setType("text/plain")
                        .setData(Uri.parse("mailto:bap_tgn@gmail.com"))
                        .putExtra(Intent.EXTRA_SUBJECT, "Brigada Antipuercos Tarragona");

                try{
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No tens un client de correu instal·lat!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sortir = (Button) customLayout.findViewById(R.id.sortir);
        sortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });



        popup = builder.create();
        popup.show();


    }


}
