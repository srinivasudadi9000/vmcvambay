package m.srinivas.vmcvambay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 17-07-2017.
 */

public class DrilldownRecycler extends RecyclerView.Adapter<DrilldownRecycler.ViewHolder>{
    ArrayList<Drilldown> drilldowns;
    Context context;
    public DrilldownRecycler(ArrayList<Drilldown> drilldowns, DashboardView dashboardView) {
          this.context = dashboardView;this.drilldowns = drilldowns;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drilldownsingle,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.applicationname.setText(drilldowns.get(position).getOriginalName());
         holder.applicationno.setText(drilldowns.get(position).getRegNo());
    }

    @Override
    public int getItemCount() {
        return drilldowns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView applicationname,applicationno;
        public ViewHolder(View itemView) {
            super(itemView);
            applicationname = (TextView) itemView.findViewById(R.id.applicationname);
            applicationno = (TextView) itemView.findViewById(R.id.applicationno);

        }
    }

}
