package com.example.lcq.myapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.constant.Constants;
import com.example.lcq.myapp.widgets.LoadDataController;
import com.example.lcq.myapp.widgets.pdf.PdfDownLoadTools;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import java.io.File;

public class PdfActivity extends Activity {
    private PDFView mPdfView;
    private LoadDataController mLoadDataController;
    private int mFromType;
    private String mNameAddress;
    private Uri mNameUri;
    private String mTitle;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pdf);
        checkPermission();
        mLoadDataController = new LoadDataController((FrameLayout) findViewById(R.id.com_load_data));
        mLoadDataController.setContentView(R.layout.pdf_layout);
        mLoadDataController.setIcErrorResId(R.drawable.com_warning_gray_50);
        mLoadDataController.setOnClickReloadListener(new LoadDataController.OnClickReloadListener()
             {
                 @Override
                 public void onClickReload() {
                     loadData();
                 }
             }
        );
        mPdfView = (PDFView) mLoadDataController.findViewById(R.id.PDFView);
        initData();
        initTitle();
    }

    private void initTitle(){
        TextView title = (TextView) findViewById(R.id.tv_title);
        if(!TextUtils.isEmpty(mTitle)){
            title.setText(mTitle);
        }
    }

    public void onBack(View view) {
        finish();
    }

    private void initData(){
        Intent intent = getIntent();
        mFromType = intent.getIntExtra(Constants.PDF_FROM_KEY,0);
        mNameAddress = intent.getStringExtra(Constants.PDF_FROM_ADDRESS_KEY);
        mNameUri = intent.getData();
        mTitle = intent.getStringExtra(Constants.PDF_TITLE);
        loadData();
    }

    private void loadData(){
        switch (mFromType){
            case Constants.PDF_FROM_ASSERTS:
                loadDataFromAsserts(mNameAddress);
                break;
            case Constants.PDF_FROM_URI:
                loadDataFromUri(mNameUri);
                break;
            case Constants.PDF_FROM_URL:
                loadPdf(this,mNameAddress);
                break;
            case Constants.PDF_FROM_FILE:
                loadDataFromUri(Uri.parse(mNameAddress));
                break;
            default:
                break;
        }
    }

    private void loadPdf(Context context , String pdfUrl){
        if (sdCardIsAvailable()) {
            PdfDownLoadTools download=new PdfDownLoadTools();
            download.setOnLoadListener(new PdfDownLoadTools.OnLoadListener() {
                @Override
                public void showLoading() {
                    mLoadDataController.showLoading();
                }
                @Override
                public void loadSuccess(Uri uri) {
                    mLoadDataController.showContentView();
                    loadDataFromUri(uri);
                }
                @Override
                public void loadFail(int resId) {
                    mLoadDataController.showError(resId);
                }
            });
            String fileDir=context.getPackageName();
            //下载pdf文件并打开
            String fileName= pdfUrl.substring(pdfUrl.lastIndexOf("/")+1);
            File file = new File(PdfDownLoadTools.SDPATH + fileDir + "/" + fileName);
            download.setFile(file);
            //启动异步任务处理
            download.execute(pdfUrl,fileDir, fileName);
        }else{
            Toast.makeText(context, R.string.com_no_sd_card, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean sdCardIsAvailable() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) return false;
        return true;
    }

    private void loadDataFromUri(Uri uri){
        mPdfView.fromUri(uri)
                .defaultPage(0)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                    }
                })
                .enableAnnotationRendering(true)
                .swipeHorizontal(false)
                .spacing(10)
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        mLoadDataController.showError(R.string.com_load_fail);
                    }
                })
                .load();
    }

    private void loadDataFromAsserts(String name){
        mPdfView.fromAsset(name)
                .defaultPage(0)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                    }
                })
                .enableAnnotationRendering(true)
                .swipeHorizontal(false)
                .spacing(10)
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        mLoadDataController.showError(R.string.com_load_fail);
                    }
                })
                .load();
    }


    /*
  检查是否开通文件读写权限
   */
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
        }
    }
}
