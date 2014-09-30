package com.josh.cs_4401app;


import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import static com.josh.cs_4401app.R.id.dimTableScroll;


public class RampTypeChooserFragment extends Fragment implements SensorEventListener {


    Spinner rtSpinner;
    ImageView rampImage;
    ScrollView parentScrollView, childScrollView;
    int selectedRamp;
    TableLayout dimTable;
    Button sensorCalibrate, saveMeasurement, clearFields;
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    float pitch, roll, calibratedPitch = 0, adjustedRoll = 0, outputPitch;
    double outputGrade;
    TextView outputGradeDisplay;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ramp_type_chooser, container, false);

        parentScrollView = (ScrollView) v.findViewById(R.id.masterScroll);
        childScrollView = (ScrollView) v.findViewById(dimTableScroll);

        sensorCalibrate = (Button) v.findViewById(R.id.sensorCalibrate);
        saveMeasurement = (Button) v.findViewById(R.id.saveMeasurement);
        clearFields = (Button) v.findViewById(R.id.clearFields);
        outputGradeDisplay = (TextView) v.findViewById(R.id.outputGrade);

        try {
            mSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }

        rtSpinner = (Spinner) v.findViewById(R.id.selectRampType);
        ArrayAdapter<String> RTadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rampTypes));
        RTadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        rtSpinner.setAdapter(RTadapter);

        rtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                selectedRamp = rtSpinner.getSelectedItemPosition();
                rampImage = (ImageView) getActivity().findViewById(R.id.rampImageView);


                dimTable = (TableLayout) getActivity().findViewById(R.id.dimTable);
                dimTable.setPadding(25, 0, 0, 0);

                switch (selectedRamp)

                {
                    case 0: {
                        rampImage.setImageResource(R.drawable.oneramponecross);
                        dimTable.removeAllViews();
                        int resId = R.array.oneramponecross;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // one ramp, one crossing
                    case 1: {
                        rampImage.setImageResource(R.drawable.oneramptwocross);
                        dimTable.removeAllViews();
                        int resId = R.array.oneramptwocross;
                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // one ramp, two crossings
                    case 2: {
                        rampImage.setImageResource(R.drawable.accessiblepushbuttons);
                        dimTable.removeAllViews();
                        int resId = R.array.pushbuttons;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // accessible push buttons
                    case 3: {
                        rampImage.setImageResource(R.drawable.type1);
                        dimTable.removeAllViews();
                        int resId = R.array.type1;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 1
                    case 4: {
                        rampImage.setImageResource(R.drawable.type1a);
                        dimTable.removeAllViews();
                        int resId = R.array.type1a;

                        setupDimensionViews(resId);
                        break;
                    } // ramp type 1a
                    case 5: {
                        rampImage.setImageResource(R.drawable.type2);
                        dimTable.removeAllViews();
                        int resId = R.array.type2;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 2
                    case 6: {
                        rampImage.setImageResource(R.drawable.type3);
                        dimTable.removeAllViews();
                        int resId = R.array.type3;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 3
                    case 7: {
                        rampImage.setImageResource(R.drawable.type4);
                        dimTable.removeAllViews();
                        int resId = R.array.type4;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 4
                    case 8: {
                        rampImage.setImageResource(R.drawable.type4a);
                        dimTable.removeAllViews();
                        int resId = R.array.type4a;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 4a
                    case 9: {
                        rampImage.setImageResource(R.drawable.type5);
                        dimTable.removeAllViews();
                        int resId = R.array.type5;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // ramp type 5
                    case 10: {
                        rampImage.setImageResource(R.drawable.type6);
                        dimTable.removeAllViews();
                        int resId = R.array.type6;

                        setupDimensionViews(resId);
                        break;
                    } // ramp type 6
                    case 11: {
                        rampImage.setImageResource(R.drawable.blendedtransition);
                        dimTable.removeAllViews();
                        int resId = R.array.blendedtransition;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // blended transition
                    case 12: {
                        rampImage.setImageResource(R.drawable.typeamedian);
                        dimTable.removeAllViews();
                        int resId = R.array.typeAmedian;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // type a median
                    case 13: {
                        rampImage.setImageResource(R.drawable.typebmedian);
                        dimTable.removeAllViews();
                        int resId = R.array.typeBmedian;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // type b median
                    case 14: {
                        rampImage.setImageResource(0);
                        dimTable.removeAllViews();
                        int resId = R.array.nontypical;

                        setupDimensionViews(resId);
                        updateDimensionFields(resId);
                        break;
                    } // non-typical
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rampImage.setImageResource(R.drawable.ic_home);
            }


        });

        sensorCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calibratedPitch = pitch;
                adjustedRoll = roll;
                // adjPitchDisplay.setText(Float.toString(Math.abs(calibratedPitch)) + "   ");
                Toast.makeText(getActivity(), "Sensor Calibrated!", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    public void setupDimensionViews(int resId) {
        String[] dimString = getResources().getStringArray(resId);
        final int dimLength = dimString.length;

        for (int i = 0; i < dimLength; i++) {

            String j = dimString[i];
            TextView rowjview = new TextView(getActivity()); // set parameters for dimension (j)
            rowjview.setText(j + ": ");
            rowjview.setTextSize(20);
            EditText rampDimj = new EditText(getActivity());

            rampDimj.setId(300 + i);
            rampDimj.setInputType(0x00002002);
            rampDimj.setWidth(400);
            TextView rowjlabel = new TextView(getActivity());
            rowjlabel.setTextSize(20);
            if (j.equals("C") || j.equals("D") || j.equals("E") || j.equals("F") || j.equals("G") || j.equals("H") || j.equals("I")
                    || j.equals("Q") || j.equals("R") || j.equals("S") || j.equals("V") || j.equals("W")) {
                rowjlabel.setText(" %");
                // grade
            } else {
                rowjlabel.setText(" in.");
            }

            TableRow rowj = new TableRow(getActivity());
            rowj.addView(rowjview, 0);
            rowj.addView(rampDimj, 1);
            rowj.addView(rowjlabel, 2);
            dimTable.addView(rowj); // dimension/row (j)
        }


    }

    public void updateDimensionFields(int resId) {
        final String[] dimString = getResources().getStringArray(resId);
        final int dimLength = dimString.length;


        for (int i = 0; i < dimLength; i++) {

            String j = dimString[i];
            final EditText check = (EditText) getActivity().findViewById(300 + i);
            final View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)

                    {
                        saveMeasurement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                check.setText(String.format("%.2f", outputGrade));
                            }
                        });
                    }
                }

            };
            check.setOnFocusChangeListener(mFocusChangeListener);


        }

        clearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < dimLength; i++) {
                    final EditText check = (EditText) getActivity().findViewById(300 + i);
                    check.setText("");

                }

            }
        });


    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    private void update(float[] vectors) {
        final NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(5);
        formatter.setMaximumFractionDigits(5);

        float rads_to_Degrees = 57.29578F;

        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        pitch = orientation[1] * rads_to_Degrees;
        roll = orientation[2] * rads_to_Degrees; // roll will be implemented later on
        pitch = Math.abs(pitch);
        roll = Math.abs(roll);

        outputPitch = (calibratedPitch - pitch);
        outputGrade = (Math.tan(Math.toRadians(outputPitch)) * 100D);

        outputGradeDisplay.setText("Current grade " + String.format("%.2f", outputGrade) + "%");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (this.getUserVisibleHint()) {
            this.registerSensorListener();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.unregisterSensorListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterSensorListener();
    }

    public void onResume() {
        super.onResume();
        if (this.getUserVisibleHint()) {
            this.registerSensorListener();
        }
    }

    private void registerSensorListener() {
        mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
    }

    private void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }
}