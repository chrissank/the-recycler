package recycler.htn.recyclerfinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RecyclingInfo {

    RecycleObject r;

    public RecyclingInfo(RecycleObject r) {
        this.r = r;
    }

    public void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Recycler.instance);
        Recycler.instance.data.getReference("objects/" + r.mid.substring(3)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, Object> objValues = (HashMap) dataSnapshot.getValue();
                if(objValues == null) {
                    builder.setView(R.layout.isunknown).setNegativeButton("Help me learn", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new UpdateRecyclingInfo(r).createDialog();
                        }
                    });
                } else {
                    if ((boolean) objValues.get("is_recyclable")) {
                        if ((boolean) objValues.get("special")) {
                            Recycler.instance.data.getReference("types/" + objValues.get("type")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final HashMap<String, Object> typeValues = (HashMap) dataSnapshot.getValue();
                                    AlertDialog d = builder.setView(R.layout.isspecial).setNegativeButton("Take me there!", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String url = "https://www.google.com/maps/search/" + typeValues.get("search");
                                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(url));
                                            Recycler.instance.startActivity(intent);
                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    }).create();
                                    d.show();
                                    d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                                    d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                                    d.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GREEN);;
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                }
                            });
                            return;
                        } else {
                            builder.setView(R.layout.isrecyclable);
                        }
                    } else {
                        builder.setView(R.layout.isnot);
                    }
                }
                AlertDialog d = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
                d.show();
                d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                d.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GREEN);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
