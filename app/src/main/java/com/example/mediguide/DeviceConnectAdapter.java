package com.example.mediguide;
//package com.example.recyclerview.multiselection;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.graphics.Color.*;
import static android.view.View.*;

public class DeviceConnectAdapter extends RecyclerView.Adapter<DeviceConnectAdapter.ViewHolder>{

    Activity activity;
    ArrayList<String> arrayList;
    TextView tvEmpty;
    ConnectViewModel connectViewModel;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();

    //create constructor
    public DeviceConnectAdapter(Activity activity,ArrayList<String> arrayList,TextView tvEmpty) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.tvEmpty = tvEmpty;
    }

    @NonNull
    @Override
    public DeviceConnectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_connect,parent,false);

        //Initialise view model
        connectViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ConnectViewModel.class);
        return new DeviceConnectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceConnectAdapter.ViewHolder holder, int position) {
        //set text view
        holder.textView.setText(arrayList.get(position));

        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Chect condition
                if(!isEnable){
                    //when action mode is not enable
                    //initialise action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            //initialise menu inflator
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            //inflate menu
                            menuInflater.inflate(R.menu.menu,menu);

                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            //when action mode is prepare
                            //set isEnable true
                            isEnable = true;
                            //create method
                            ClickItem(holder);

                            //set observer on get text method
                            connectViewModel.getText().observe((LifecycleOwner) activity, new Observer<String>(){
                                        @Override
                                        public void onChanged(String s) {
                                            //when text change
                                            //set text on action mdde title
                                            actionMode.setTitle(String.format("%s Selected",s));
                                        }
                                    });
                            //return true
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            //when click on action mode item
                            //get item id
                            int id = menuItem.getItemId();
                            //use switch condition
                            switch(id) {
                                case R.id.menu_delete:
                                    for(String s : selectList) {
                                        arrayList.remove(s);
                                    }
                                    //check condition
                                    if(arrayList.size() == 0) {
                                        tvEmpty.setVisibility(VISIBLE);
                                    }
                                    //finish action mode
                                    actionMode.finish();
                                    break;
                                case R.id.menu_select_all:
                                    if(selectList.size() == arrayList.size()){
                                        isSelectAll = false;
                                        //clear select array list
                                        selectList.clear();
                                    }
                                    else{
                                        //when all item is unselected
                                        isSelectAll = true;
                                        selectList.clear();
                                        //add all values in select array list
                                        selectList.addAll(arrayList);
                                    }
                                    //set text on view model
                                    connectViewModel.setText(String.valueOf(selectList.size()));
                                    //notify adapter
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            //when action mode is destroy
                            isEnable = false;
                            isSelectAll = false;
                            selectList.clear();
                            notifyDataSetChanged();
                        }
                    };
                    //start action mode
                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                }

                else{
                    //when action mode is already enable
                    ClickItem(holder);
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                //check condition
                if(isEnable){
                    //when action mode is enable
                    ClickItem(holder);
                }else{
                    //when action mode is not enable
                    Toast.makeText(activity,"You Clicked"+arrayList.get(holder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(isSelectAll){
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.ivCheckBox.setVisibility(GONE);
            holder.itemView.setBackgroundColor(TRANSPARENT);

        }
    }

    private void ClickItem(ViewHolder holder) {
        //get selected item value
        String s = arrayList.get(holder.getAdapterPosition());
        //chect condition
        if(holder.ivCheckBox.getVisibility() == GONE) {
            //when item is not selected
            //visible checkbox image
            holder.ivCheckBox.setVisibility(VISIBLE);

            //set background color
            holder.itemView.setBackgroundColor(LTGRAY);
            //add value in select array list
            selectList.add(s);
        }
        else{
            //when item selected
            //hide checkbox image
            holder.ivCheckBox.setVisibility(GONE);
            //set background color
            holder.itemView.setBackgroundColor(TRANSPARENT);
            //remove value in select array list
            selectList.remove(s);
        }

        //set text on view model
        connectViewModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Initialise variable
        TextView textView;
        ImageView ivCheckBox;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            //Assign varaibles
            textView = itemView.findViewById(R.id.text_view);
            ivCheckBox = itemView.findViewById(R.id.iv_check_box);
        }

    }
}
