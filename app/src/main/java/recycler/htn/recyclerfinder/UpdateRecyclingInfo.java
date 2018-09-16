package recycler.htn.recyclerfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateRecyclingInfo  {

    RecycleObject r;
    HashMap<String, String> types = new HashMap<>();
    public UpdateRecyclingInfo(RecycleObject r) {
        this.r = r;
    }

    public void createDialog() {
        Recycler.instance.data.getReference("types/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> values = (HashMap) dataSnapshot.getValue();
                for (String key : values.keySet()) {
                    HashMap<String, Object> vals = (HashMap) values.get(key);
                    types.put(vals.get("name").toString(), key);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Recycler.instance);
                LayoutInflater lf = Recycler.instance.getLayoutInflater();
                final View v = lf.inflate(R.layout.classifylayout, null);
                builder.setView(v);
                AlertDialog d = builder.setTitle("Help me learn").setMessage("By filling in these fields, you can help me learn about a new item!")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean rec = ((Switch) v.findViewById(R.id.recyclable)).isChecked();
                                boolean special = ((Switch) v.findViewById(R.id.special)).isChecked();
                                Spinner s = v.findViewById(R.id.typeSpinner);
                                String key = types.get(s.getSelectedItem().toString());
                                if(s.getSelectedItem().equals("Recycling Type")) key = "";
                                Recycler.instance.data.getReference("objects/" + r.mid.substring(3)).setValue(new RecycleObjectDB(rec, special, key));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                Log.e("Recycler", types.toString());
                Spinner spinner = (Spinner) v.findViewById(R.id.typeSpinner);
                ArrayList<String> t = new ArrayList<>();
                t.add("Recycling Type");
                t.addAll(types.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, t);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                d.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

}
