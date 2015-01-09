package alok.cltplayer.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

public class FileOpener{

    public static void open(Context context, File url) throws IOException{
        Uri uri = Uri.fromFile(url);
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(url.toString().contains(".doc") || url.toString().contains(".docx")){
            intent.setDataAndType(uri, "application/msword"); //WORD
        }else if(url.toString().contains(".pdf")){
            intent.setDataAndType(uri, "application/pdf"); //PDF
        }else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")){
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint"); //PPT
        }else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")){
            intent.setDataAndType(uri, "application/vnd.ms-excel"); //EXCEL
        }else if(url.toString().contains(".wav") || url.toString().contains(".mp3")){
            intent.setDataAndType(uri, "audio/x-wav"); //MP3
        }else if(url.toString().contains(".gif")){
            intent.setDataAndType(uri, "image/gif"); //GIF
        }else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")){
            intent.setDataAndType(uri, "image/jpeg"); //JPG or PNG
        }else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")){
            intent.setDataAndType(uri, "video/*"); //VIDEO
        }else if(url.toString().contains(".qiz")){
            //OPEN QUIZ HERE
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(intent);
    }
}