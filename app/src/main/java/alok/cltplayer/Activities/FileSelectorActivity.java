package alok.cltplayer.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;

import alok.cltplayer.R;
import alok.cltplayer.Utils.FileHandler;
import alok.cltplayer.Utils.FileOpener;

public class FileSelectorActivity extends ActionBarActivity{

    File root, currentFolder;
    ListView lv;
    ActionBar actionbar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);
        root = FileHandler.getRoot();
        if(!root.exists()){
            root.mkdir();
        }
        actionbar = getSupportActionBar();
        lv = (ListView) findViewById(R.id.listView);
        fill(root);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_file_selector, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fill(currentFolder.getParentFile());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fill(File dir){
        currentFolder = dir;
        actionbar.setTitle(currentFolder.getName());
        if(currentFolder.equals(root)){
            actionbar.setDisplayHomeAsUpEnabled(false);
        }else{
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        final FileSelectorArrayAdapter adapter = new FileSelectorArrayAdapter(this, dir.listFiles());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                final File selectedFile = adapter.files[position];
                if(selectedFile.isDirectory()){
                    fill(selectedFile);
                }else{
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(FileSelectorActivity.this, "Please wait",	"Loading File", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                File cacheFolder = getApplicationContext().getCacheDir();
                                String fileName = selectedFile.getName();
                                String decryptedFileName = fileName.split("\\.(?=[^\\.]+$)")[0];
                                File decryptedFile = File.createTempFile(decryptedFileName, "temp", cacheFolder);
                                FileHandler.fileXor(selectedFile, decryptedFile);
                                FileOpener.open(FileSelectorActivity.this, selectedFile);
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                            ringProgressDialog.dismiss();
                        }
                    }).start();
                }
            }
        });
    }

    class FileSelectorArrayAdapter extends ArrayAdapter<File>{
        private final Context context;
        public final File[] files;

        public FileSelectorArrayAdapter(Context _context, File[] _files){
            super(_context, R.layout.file_selector_list_item, _files);
            this.context = _context;
            this.files = _files;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.file_selector_list_item, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

            if(files[position].isDirectory()){
                imageView.setImageResource(R.drawable.icon_directory);
            }else{
                imageView.setImageResource(R.drawable.icon_play);
            }
            textView.setText(FileHandler.getFileName(files[position]));

            return rowView;
        }
    }
}

