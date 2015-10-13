package vu.k.douniwan5788.nfcframework;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    private TextView tv;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private byte[] transceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);

        nfcAdapter = ((NfcManager) getSystemService(NFC_SERVICE)).getDefaultAdapter();

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
//            this.Show(intent.toString());
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] rsp = null;
            try {
//                IsoDep isoDep = IsoDep.get(tag);
//                if (null != isoDep) {
//                    isoDep.connect();
//                    isoDep.close();
//                }
                NfcA nfca = NfcA.get(tag);
                if (null != nfca) {
                    nfca.connect();

                    for (int i = 0; i <= 0xFF; ++i) {
                        tv.append(String.format("read page %02x\n", i));
                        rsp = nfca.transceive(new byte[]{0x30, (byte) i});
                        tv.append(bytesToHexString(rsp) + "\n\n");
                    }
                    nfca.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void Show(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
