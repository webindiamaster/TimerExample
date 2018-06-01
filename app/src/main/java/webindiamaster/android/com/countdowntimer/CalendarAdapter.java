package webindiamaster.android.com.countdowntimer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber=6;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;
    private ArrayList<String> items;
    public static List<String> day_string;
    private View previousView;
    public ArrayList<CalendarCollection>  date_collection_arr;
    public CalendarAdapter(Context context, GregorianCalendar monthCalendar,ArrayList<CalendarCollection> date_collection_arr) {
        this.date_collection_arr=date_collection_arr;
        CalendarAdapter.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }
    public int getCount() {
        return day_string.size();
    }
    public Object getItem(int position) {
        return day_string.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);
        }
        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("-");
         String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 15) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            dayView.setTextColor(Color.WHITE);
        }
        if (day_string.get(position).equals(curentDateString)) {
            v.setBackgroundColor(Color.CYAN);
        } else {
            v.setBackgroundColor(Color.parseColor("#343434"));
        }
        dayView.setText(gridvalue);
        String date = day_string.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        setEventView(v, position,dayView);
        return v;
    }

    public View setSelected(View view,int pos) {
        if (previousView != null) {
        previousView.setBackgroundColor(Color.parseColor("#343434"));
        }
        view.setBackgroundColor(Color.CYAN);
        int len=day_string.size();
        if (len>pos) {
            if (day_string.get(pos).equals(curentDateString)) {
            }else{
                previousView = view;
            }
        }
        return view;
    }

    public void refreshDays() {
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
       // maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP();
        calMaxP = maxP - (firstDay - 1);
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }
    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        return maxP;
    }
    public void setEventView(View v,int pos,TextView txt){
        int len=CalendarCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            CalendarCollection cal_obj=CalendarCollection.date_collection_arr.get(i);
            String date=cal_obj.date;
            String event=cal_obj.event_message;
            int len1=day_string.size();
            if (len1>pos) {
            if (day_string.get(pos).equals(date)) {
                v.setBackgroundColor(Color.parseColor("#343434"));
                switch (event){
                    case "Absent":
                        v.setBackgroundColor(context.getResources().getColor(R.color.absent));
                        break;
                    case "Leave":
                        v.setBackgroundColor(context.getResources().getColor(R.color.leave));
                        break;
                    case "Present":
                        v.setBackgroundColor(context.getResources().getColor(R.color.present));
                        break;
                    case "Holiday":
                        v.setBackgroundColor(context.getResources().getColor(R.color.holiday));
                        break;
                }
                    txt.setTextColor(Color.WHITE);
            }         
        }}
        
        
    
    }
    
    
public void getPositionList(String date,final Activity act){
        int len=CalendarCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            CalendarCollection cal_collection=CalendarCollection.date_collection_arr.get(i);
            String event_date=cal_collection.date;
            String event_message=cal_collection.event_message;
            if (date.equals(event_date)) {
                Toast.makeText(context, "You have event on this date: "+event_date, Toast.LENGTH_LONG).show();
             new AlertDialog.Builder(context)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setTitle("Date: "+event_date)
               .setMessage("Event: "+event_message)
                .setPositiveButton("OK",new android.content.DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) 
                {
                    act.finish();    
                }
                }).show();
            break;        
        }else{
            
            
        }}
        
        
    
    }

}

 