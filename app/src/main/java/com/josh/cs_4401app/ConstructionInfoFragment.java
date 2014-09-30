package com.josh.cs_4401app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class ConstructionInfoFragment extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_construction_info, container, false);


        Spinner constructionPhaseSpinner = (Spinner) v.findViewById(R.id.outputConstructionPhase);
        ArrayAdapter<String> CPSadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.constructionPhases));
        CPSadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        constructionPhaseSpinner.setAdapter(CPSadapter);

        Spinner rampCrossSpinner = (Spinner) v.findViewById(R.id.outputRampCrosses);
        ArrayAdapter<String> RCSadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rampCrossOptions));
        RCSadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        rampCrossSpinner.setAdapter(RCSadapter);

        Spinner dwsSpinner = (Spinner) v.findViewById(R.id.outputDWSType);
        ArrayAdapter<String> DWSadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.typeDWS));
        DWSadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dwsSpinner.setAdapter(DWSadapter);

        return v;
    }

    protected void onCreate() {
        super.onStart();




    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.construction_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
