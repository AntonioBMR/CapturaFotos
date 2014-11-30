package com.antonio.amdroid.capturafotos;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    private EditText etRuta, etNombre;
    private TextView tvRutaF;
    private RadioButton rbPublica, rbPrivada;
    private String ruta;
    private String nombre;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etRuta = (EditText) findViewById(R.id.etR);
        etNombre = (EditText) findViewById(R.id.etN);
        tvRutaF=(TextView)findViewById(R.id.textView);
        rbPublica = (RadioButton) findViewById(R.id.rbPublica);
        rbPrivada = (RadioButton) findViewById(R.id.rbPrivada);
        iv = (ImageView)findViewById(R.id.imgV);
    }

    public void guardar(View v) {
        if(etRuta.getText().toString().isEmpty()||etNombre.getText().toString().isEmpty()){
            tostada("ruta o nombre vacios");

        }else{
            HiloFacil hf = new HiloFacil();
            hf.execute();
        }
    }
    public void salir(View v){
         finish();
    }

    class HiloFacil extends AsyncTask<Object, Integer, String> {

        HiloFacil(String... p) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] params) {
            try {
                String dir = etRuta.getText().toString();
                URL url = new URL(dir);
                nombre = etNombre.getText().toString();
                URLConnection urlCon = url.openConnection();
                String tipo = dir.substring(dir.length()-3);
                if(tipo.equals("jpg")||tipo.equals("png")||tipo.equals("gif")){
                    InputStream is = urlCon.getInputStream();
                    if (rbPrivada.isChecked()) {
                        ruta = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +"/"+ nombre+"."+tipo;
                    } else {
                        ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                                +"/"+ nombre+"."+tipo;
                    }
                    FileOutputStream fos = new FileOutputStream(ruta);
                    byte[] array = new byte[2046];
                    int leido = is.read(array);
                    while (leido > 0) {
                        fos.write(array, 0, leido);
                        leido = is.read(array);
                    }
                    is.close();
                    fos.close();
                }else{
                    System.out.println("archivos no validos");
                }
            }catch (MalformedURLException e1) {
                e1.printStackTrace();
                System.out.println(e1.toString()+" error url");
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println(e1.toString()+" error IOException");

            } catch (Exception e) {
                System.out.println(e.toString()+" error otro");
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if(!ruta.isEmpty()){
                File imgFile = new File(ruta);
                if(imgFile.exists()){
                 iv.setImageURI(Uri.fromFile(imgFile));
                 tvRutaF.setText(ruta);
                 }
            }
          super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }
    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}