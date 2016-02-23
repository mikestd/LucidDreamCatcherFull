package com.mikestudio.luciddreamcatcher;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TargetListActivity extends ListActivity implements AdapterView.OnItemLongClickListener {
    final static String STRING ="com.mikestudio.luciddreamcatcher.STRING";
    final static String IMAGE = "com.mikestudio.luciddreamcatcher.IMAGE";
    final static String SAVE = "settings";
    public SharedPreferences preferences;
   /* public Integer[] mTargetArray = new Integer[]{R.string.Bird,R.string.Trash,R.string.Sun,
    R.string.Plain,R.string.IceCream,R.string.Home,R.string.Dig,R.string.Cat};
    public Integer[]mImageArray = new Integer[] {R.mipmap.bird,
            R.mipmap.trash,
            R.mipmap.sun,
            R.mipmap.plain,
            R.mipmap.ice_cream,
            R.mipmap.home,
            R.mipmap.dig,
            R.mipmap.cat};*/
    public String mTargetString;
    public String [] s;
    public String mImageString = "add";
    public String[]img;
    private TargetAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTargetString = getString(R.string.Add);
        preferences = getSharedPreferences(SAVE, MODE_PRIVATE);
        if (preferences.contains("TargetArray")){
            mTargetString = preferences.getString("TargetArray", getString(R.string.Add));
            s = mTargetString.split("%");
        } else {
            s = mTargetString.split("%");
        }
        if (preferences.contains("ImageArray")){
            mImageString = preferences.getString("ImageArray", "add");
            img = mImageString.split("%");
        } else {
            img = mImageString.split("%");
        }
        mAdapter = new TargetAdapter(this);
        setListAdapter(mAdapter);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String tempStr = data.getStringExtra(ChoosePicDescriptionActivity.TEXT);
                String tempImg = data.getStringExtra(ChoosePicDescriptionActivity.SRC);
                mTargetString = mTargetString + "%" + tempStr;
                mImageString = mImageString + "%" + tempImg;
                s = mTargetString.split("%");
                img = mImageString.split("%");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("TargetArray", mTargetString);
                editor.putString("ImageArray", mImageString);
                editor.apply();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0){
            Intent intent = new Intent(this, ChoosePicDescriptionActivity.class);
            startActivityForResult(intent, 0);
        } else {
            String selection = mAdapter.getString(position);
            int resId = mAdapter.getImage(position);
            Intent answerIntent = new Intent();
            answerIntent.putExtra(STRING, selection);
            answerIntent.putExtra(IMAGE, resId);
            setResult(RESULT_OK, answerIntent);
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
        } else {
            mTargetString = mTargetString.replaceAll("%" + s[position], "");
            mImageString = mImageString.replaceAll("%" + img[position], "");
            s = mTargetString.split("%");
            img = mImageString.split("%");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("TargetArray", mTargetString);
            editor.putString("ImageArray", mImageString);
            editor.apply();
            mAdapter.notifyDataSetChanged();
        }

        return true;
    }

    public class TargetAdapter extends BaseAdapter{
        private LayoutInflater mLayoutInflater;
        public TargetAdapter(Context context){
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return s.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public String getString(int position){
            return s[position];
        }

        public int getImage (int position){
            return (getResources().getIdentifier(img[position], "mipmap", getPackageName()));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) convertView = mLayoutInflater.inflate(R.layout.activity_target_list,null);

            ImageView targetIcon = (ImageView)convertView.findViewById(R.id.imageViewIcon);
            targetIcon.setImageResource(getResources().getIdentifier(img[position], "mipmap", getPackageName()));
            TextView targetName = (TextView)convertView.findViewById(R.id.textViewTargetName);
            targetName.setText(s[position]);
            return convertView;
        }
    }
}
