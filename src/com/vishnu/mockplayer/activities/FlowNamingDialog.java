package com.vishnu.mockplayer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.vishnu.mockplayer.utilities.MockPlayerApplication;
import com.vishnu.mockplayer.R;

public class FlowNamingDialog extends DialogFragment{

    Activity activity;

    public FlowNamingDialog(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.activity_flow_naming, null);

        builder.setView(dialogLayout).setPositiveButton(R.string.name_set_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText flowName = (EditText) dialogLayout.findViewById(R.id.activity_name);
                String activityName = flowName.getText().toString();
                MockPlayerApplication application = MockPlayerApplication.getInstance();
                application.setMockName(activityName);
                Intent intent = new Intent(activity, FirstImageSelector.class);
                startActivity(intent);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FlowNamingDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Name your Mock");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
