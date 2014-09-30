package com.josh.cs_4401app;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SaveDataFragment extends DialogFragment implements View.OnClickListener{

    EditText outputFilename, outputFoldername;
    AlertDialog ad;
    int selected;
    String outputData;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_save_data,
                container, false);

        Button saveButton = (Button) view.findViewById(R.id.mySaveDataButton);
        outputFilename = (EditText) view.findViewById(R.id.outputFilename);
        outputFoldername = (EditText) view.findViewById(R.id.outputFoldername);
        saveButton.setOnClickListener(this);


        ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle("Folder exists");
        ad.setMessage("Folder name exists. Do you want to save to another folder instead?");
        ad.setButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selected = 1;
                dialog.dismiss();
            }

        });
        ad.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selected = 2;
                dialog.dismiss();
            }

        });




        return view;
    }

    @Override
    public void onClick(View v) {

        setupData();

        String foldername = (outputFoldername.getText().toString());
        String filename = (outputFilename.getText().toString() + ".txt");
        Writer mwriter;

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/" + foldername);


        if (!dir.exists()) {
            dir.mkdir();

        }

        try {

            File outputFile = new File(dir, filename);
            mwriter = new BufferedWriter(new FileWriter(outputFile));
            mwriter.write(outputData); // DATA WRITE TO FILE

            Toast.makeText(getActivity().getApplicationContext(),
                    "Successfully saved data to: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            mwriter.close();

        } catch (IOException e) {
            Log.w("write log", e.getMessage(), e);
            Toast.makeText(getActivity(), e.getMessage() + " Unable to write to external storage.", Toast.LENGTH_LONG).show();
        }


    }

    private String setupData(){

        outputData = "Output Data\n\n" +"Site ID: \n" +"Date: \n"+"Field Investigator 1: \n"+"Field Investigator 2: \n" + "District: \n"+"County: \n"+"Municipality: \n"
                +"Location Coordinates: \n"+"Ramp Type: \n";
        return outputData;
    }

}