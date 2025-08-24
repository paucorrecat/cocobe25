package com.gruixuts.cocobe25;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frg_inc_imatges#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frg_inc_imatges extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARXIU = "param_ARXIU";
    private static final String ARG_CARPETA = "param_CARPETA";

    private String Arxiu;
    private String CarpetaImatgesItem;   // El que es guarda al item (i ve com a paràmetre)
    private String CarpetaImatgesPath;   // El absolute path




    private Integer numImatge;   // l'index de la imatge que estem veient
    private String nomsImatge[] = {};
    static final int REQUEST_IMAGE_CAPTURE = 17;
    private String currentPhotoPath;
    private String currentPhotoName;
    private File imatge; // Foto que s'acaba de fer
    View view;
    private ActivityResultLauncher<Intent> captureImageResultLauncher;


    public frg_inc_imatges() {
        // Required empty public constructor
    }

    public static frg_inc_imatges newInstance(String arxiu, String carpeta) {
        frg_inc_imatges fragment = new frg_inc_imatges();
        Bundle args = new Bundle();
        args.putString(ARG_ARXIU, arxiu);
        args.putString(ARG_CARPETA, carpeta);
        fragment.setArguments(args);
        return fragment;
    }

    public void SetCarpeta(String Carpeta) {
        File parent = requireContext().getExternalFilesDir(Arxiu);
        if (Carpeta.length()==0) {
            Log.e("frgmIncImatges.SetCarpeta", " El paràmetre 'Carpeta' és de longitud 0");
        }
        CarpetaImatgesItem = Carpeta;

        File carpeta = new File(parent,Carpeta);
        if (!carpeta.exists()) { // Crear-la
            //MissatgeError("La cerpeta d'immatges " + CarpetaImatgesItem + " no existeix");
            numImatge = 0;
            //carpeta.mkdirs();
            nomsImatge = new String[0];
            Log.e("frgmIncImatges.SetCarpeta", "No trobada la carpeta " + Carpeta + " dins de " + parent.getAbsolutePath());
        } else {
            nomsImatge = carpeta.list();
            CarpetaImatgesPath = carpeta.getAbsolutePath();
        }
        if (nomsImatge.length != 0) {
            numImatge = 1;
//                    Drawable d = Drawable.createFromPath(CarpetaImatges + "/" + item.getImatges() + "/" + nomsImatge[numImatge - 1]);
            Drawable d = Drawable.createFromPath(carpeta.getAbsolutePath() + "/" + nomsImatge[numImatge - 1]);
            ((ImageView) view.findViewById(R.id.imgImatges)).setImageDrawable(d);
        } else {
            numImatge = 0;
            ((ImageView) view.findViewById(R.id.imgImatges)).setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("frgmIncImatges.onCreate","Entra");
        if (getArguments() != null) {
            Arxiu = getArguments().getString(ARG_ARXIU);
            CarpetaImatgesItem = getArguments().getString(ARG_CARPETA);
        } else {
            Log.e("frgmIncImatges.onCreate", "getArguments() == null");
        }

        Log.d("frgmIncImatges.onCreate","Carpeta"+CarpetaImatgesItem);

        captureImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Gestió de la captura de la imatge
                        Drawable d = Drawable.createFromPath(currentPhotoPath);
                        ((ImageView) getView().findViewById(R.id.imgImatges)).setImageDrawable(d);
                        numImatge = nomsImatge.length + 1;
                        nomsImatge = Arrays.copyOf(nomsImatge, numImatge);
                        nomsImatge[numImatge - 1] = currentPhotoName;
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Infla el layout del fragment
        Log.d("frgmIncImatges.onCreateView","Entra");
        view = inflater.inflate(R.layout.fragment_inc_imatges, container, false);

        // Configura els OnClickListeners per als ImageButtons
        ImageButton btnAntFoto = view.findViewById(R.id.cmdIncAntFoto);
        btnAntFoto.setOnClickListener(v -> fotoAnterior(v));

        ImageButton btnSegFoto = view.findViewById(R.id.cmdIncSegFoto);
        btnSegFoto.setOnClickListener(v -> fotoSeguent(v));

        ImageButton btnAddFoto = view.findViewById(R.id.cmdIncAddFoto);
        btnAddFoto.setOnClickListener(v -> fotoAfegeix(v));

        ImageButton btnEliFoto = view.findViewById(R.id.cmdIncEliFoto);
        btnEliFoto.setOnClickListener(v -> fotoElimina(v));

        // Configura un OnClickListener per l'ImageView
        ImageView imageView = view.findViewById(R.id.imgImatges);
        imageView.setOnClickListener(v -> openImage());

        SetCarpeta(CarpetaImatgesItem);
        return view;
    }

    private void openImage() {
        if (numImatge==0) { return;}
        File imageFile = new File(CarpetaImatgesPath + "/" + nomsImatge[numImatge - 1]);
        Log.d("openImage","Identificat fitxer: "+CarpetaImatgesPath + "/" + nomsImatge[numImatge - 1]);
        Uri imageUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                imageFile
        );
        Log.d("openImage","Uri Ok");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(imageUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d("openImage","Uri Ok");
        startActivity(intent);
        Log.d("openImage","Activitat llançara");
    }

    public void fotoSeguent(View v) {
        if (numImatge < nomsImatge.length && numImatge > 0) {
            numImatge++;
            Log.d("frgmIncImatges.fotoSeguent", "Carrega foto " + nomsImatge[numImatge - 1] + " de "+ CarpetaImatgesPath + "/");
            Drawable d = Drawable.createFromPath(CarpetaImatgesPath + "/" + nomsImatge[numImatge - 1]);
            ((ImageView) view.findViewById(R.id.imgImatges)).setImageDrawable(d);
        }
    }

    public void fotoAnterior(View v) {
        if (numImatge > 1) {
            numImatge--;
            Log.d("frgmIncImatges.fotoAnterior", "Carrega foto " + nomsImatge[numImatge - 1] + " de "+ CarpetaImatgesPath + "/");
            Drawable d = Drawable.createFromPath(CarpetaImatgesPath + "/" + nomsImatge[numImatge - 1]);
            ((ImageView) view.findViewById(R.id.imgImatges)).setImageDrawable(d);
        }
    }

    /* Versió ant.
    public void fotoAfegeix(View view) {
        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (CameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(CameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
     */

    // Versió 1 de chatGPT
    public void fotoAfegeix(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Aquí pots mostrar un missatge d'error a l'usuari si cal
                return; // Sortir de la funció si no es pot crear el fitxer
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().getPackageName() + ".fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureImageResultLauncher.launch(cameraIntent);
//                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Crear un nom de fitxer únic amb data i hora
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Crear un directori únic per a les imatges
        assert (CarpetaImatgesItem.length()!=0);  // Si no, que peti

        File storageDir = new File(requireContext().getExternalFilesDir(Arxiu), CarpetaImatgesItem);
        CarpetaImatgesPath=storageDir.getAbsolutePath();
        if (!storageDir.exists()) {
            storageDir.mkdirs(); // Crear el directori si no existeix
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Guardar el camí del fitxer creat
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoName = image.getName();
        return image;
    }
    // Fi versió 1 de chatGPT



    public void fotoElimina(View v) {
        // TODO later: ELiminar imatges
        Toast.makeText(requireContext(), "Opció encara no implementada", Toast.LENGTH_SHORT).show();
    }

}

