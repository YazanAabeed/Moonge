package v1.moonge.moonge;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import lists_adapter.accountBalanceList;

public class account_balance extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    accountBalanceList newAdapter;
    ArrayList<moonge_objects.account_balance> amountBalance;
    private final static int REQUEST_ENABLE_BT = 1;
    Handler bluetoothIn;
    private ConnectedThread mConnectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    String mLatitudeText;
    String mLongitudeText;
    String mFullAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_balance);

        amountBalance = new ArrayList<>();

        final Typeface RobotoBold = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Bold.ttf");
        final Typeface RobotoMedium = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Medium.ttf");

        final ListView account_data = (ListView) findViewById(R.id.accounts_amounts);

        final ProgressDialog progressDialog = new ProgressDialog(account_balance.this);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> getAmounts = ParseQuery.getQuery("account_balance");

        getAmounts.orderByDescending("createdAt");
        getAmounts.whereEqualTo("user", ParseUser.getCurrentUser());

        getAmounts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    account_data.setAdapter(null);

                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject amount = objects.get(i);

                        String name = amount.getString("name");
                        int Amount = amount.getNumber("amount").intValue();
                        String objectId = amount.getObjectId();

                        amountBalance.add(new moonge_objects.account_balance(Amount, name, objectId));
                    }

                    newAdapter = new accountBalanceList(account_balance.this, amountBalance);
                    account_data.setAdapter(newAdapter);
                } else {
                    Log.d("Error", "get account balance data: " + e.getMessage());
                }

                progressDialog.dismiss();
            }
        });

        account_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                View viewParent = (View) view.getParent();

                final moonge_objects.account_balance person = (moonge_objects.account_balance) parent.getItemAtPosition(position);
            }
        });

        Button addNewBalance = (Button) findViewById(R.id.add_new_balance);
        addNewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder newBalanceDialog = new AlertDialog.Builder(account_balance.this);

                final View newBalanceDialogView = getLayoutInflater().inflate(R.layout.add_new_account_balance_dialog, null);

                newBalanceDialog.setView(newBalanceDialogView).setCancelable(false);
                final AlertDialog balance = newBalanceDialog.create();
                balance.show();

                Button exitBalanceDialog = (Button) newBalanceDialogView.findViewById(R.id.exit_balance_dialog);

                exitBalanceDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        balance.dismiss();
                    }
                });

                Button addNewBalance = (Button) newBalanceDialogView.findViewById(R.id.add_new_balance);

                final EditText balance_name = (EditText) newBalanceDialogView.findViewById(R.id.account_balance_new_name);
                final EditText balance_amount = (EditText) newBalanceDialogView.findViewById(R.id.account_balance_new_ammount);


                balance_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String balanceNameTxt = balance_name.getText().toString();

                        if (balanceNameTxt.trim().isEmpty()) {
                            balance_name.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                        } else {
                            balance_name.setBackground(getResources().getDrawable(R.drawable.account_balance_fields));
                        }

                    }
                });

                balance_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String balanceAmountTxt = balance_amount.getText().toString();

                        if (balanceAmountTxt.trim().isEmpty()) {
                            balance_amount.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                        } else {
                            balance_amount.setBackground(getResources().getDrawable(R.drawable.account_balance_fields));
                        }

                    }
                });

                addNewBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String balanceNameTXT = balance_name.getText().toString();
                        balance_name.setBackground(getResources().getDrawable(R.drawable.account_balance_fields));

                        if (balanceNameTXT.trim().isEmpty()) {
                            balance_name.setError("Balance name is required", null);
                            balance_name.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                            return;
                        }

                        final String amountBalanceTXT = balance_amount.getText().toString();
                        balance_amount.setBackground(getResources().getDrawable(R.drawable.account_balance_fields));

                        if (amountBalanceTXT.trim().isEmpty()) {
                            balance_amount.setError("Balance Amount is required", null);
                            balance_amount.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                            return;
                        }


                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();


                        final ParseObject newAccountBalanceRow = new ParseObject("account_balance");
                        newAccountBalanceRow.put("name", balanceNameTXT);
                        newAccountBalanceRow.put("amount", Integer.parseInt(amountBalanceTXT));
                        newAccountBalanceRow.put("user", ParseUser.getCurrentUser());
                        newAccountBalanceRow.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    newAdapter.add(new moonge_objects.account_balance(Integer.parseInt(amountBalanceTXT), balanceNameTXT, newAccountBalanceRow.getObjectId()));
                                    newAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    balance.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        });


        Button logout_button = (Button) findViewById(R.id.logout);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(account_balance.this);

                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                });
            }
        });

        account_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                View viewParent = (View) view.getParent();

                final moonge_objects.account_balance person = (moonge_objects.account_balance) parent.getItemAtPosition(position);

                new AlertDialog.Builder(account_balance.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("account_balance");
                                query.getInBackground(person.getObjectId(), new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            // object will be your game score
                                            try {
                                                object.delete();
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        newAdapter.remove(person);
                                                        newAdapter.notifyDataSetChanged();
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                        } else {
                                            // something went wrong
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }

        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                final Timer mTimer = new Timer();
                final TimerTask mTimerTask = new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (mBluetoothAdapter.isDiscovering()) {
                                    mBluetoothAdapter.cancelDiscovery();
                                    //After cancel discovery, start discovery instead of wait another 10 seconds to re-execute this piece of code
                                    mBluetoothAdapter.startDiscovery();
                                } else {
                                    mBluetoothAdapter.startDiscovery();
                                }
                            }

                        });
                    }
                };

                mTimer.schedule(mTimerTask, 0, 30000);
            }
        } else {
            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                findBT();

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Toast.makeText(getBaseContext(), device.getName() + " is now connected", Toast.LENGTH_LONG).show();

                try {
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
                }catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Socket creation failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mConnectedThread = new ConnectedThread(mmSocket);
                mConnectedThread.start();
            }
        }
    };


    void findBT()
    {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                mmDevice = device;
            }
        }

        Toast.makeText(getApplicationContext(), mmDevice.getName() + " is connected", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_balance, menu);
        return true;
    }

    public void back() {
        super.onBackPressed();
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
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());

//            Geocoder geocoder;
//            List<android.location.Address> addresses;
//            geocoder = new Geocoder(this, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
//
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//
//                mFullAdress = address + " " + city + " " + country + " " + knownName;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private BluetoothSocket s;
        private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            s = socket;
            if (socket != null) {
                try {
                    socket.connect();

                    InputStream tmpIn = null;
                    OutputStream tmpOut = null;

                    try {
                        //Create I/O streams for connection
                        mmInStream = socket.getInputStream();
                        mmOutStream = socket.getOutputStream();

                        Toast.makeText(getApplicationContext(), "Stream in = " + mmInStream.toString() + " " + mmOutStream.toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    if (mmInStream != null && s != null) {

                        account_balance.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "Send now ", Toast.LENGTH_LONG).show();
                            }
                        });

                        SmsManager smsManager = SmsManager.getDefault();

                        for (moonge_objects.account_balance acc : amountBalance) {
                            smsManager.sendTextMessage("00972" + String.valueOf(acc.getAmount()), null, "Please help me! it " + acc.getName() + " i am in " + mFullAdress + " " + mLatitudeText + " " + mLongitudeText, null, null);
                        }

                        mBluetoothAdapter.cancelDiscovery();

                        break;
                    } else {
                        account_balance.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "Connection Failure because there is no input stream", Toast.LENGTH_LONG).show();
                            }
                        });

                        s.connect();

                        mmInStream = s.getInputStream();
                        mmOutStream = s.getOutputStream();
                    }
                } catch (final IOException e) {
                    account_balance.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Connection Failure " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                if (mmOutStream != null && s != null) {
                    mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                } else {
                    Toast.makeText(getBaseContext(), "Connection Failure " + s, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}