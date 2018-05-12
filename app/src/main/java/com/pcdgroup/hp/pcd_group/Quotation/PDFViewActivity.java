package com.pcdgroup.hp.pcd_group.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.pcdgroup.hp.pcd_group.AdminLogin.AdminDashboard;
import com.pcdgroup.hp.pcd_group.Client.UpdateActivity;
import com.pcdgroup.hp.pcd_group.Global.GlobalVariable;
import com.pcdgroup.hp.pcd_group.Product.ViewImage;
import com.pcdgroup.hp.pcd_group.R;
import com.pcdgroup.hp.pcd_group.UserLoginRegister.UserDashbord;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

/**
 * @author Grasp
 *  @version 1.0 on 28-03-2018.
 */

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener {

    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PDFViewActivity";
    int position=-1;

    String saveName;
    Intent intent;
    GlobalVariable gblVar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        gblVar = GlobalVariable.getInstance();

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null) {

                saveName = extras.getString("FileName");
            }
        }
        init();

    }

    private void init(){
        pdfView= (PDFView)findViewById(R.id.pdfview);
        position = getIntent().getIntExtra("position",-1);
        displayFromSdcard();
    }
    private void displayFromSdcard() {

        pdfFileName =  saveName;

        File file = new File(pdfFileName);

        Log.e("File path",file.getAbsolutePath());
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }
    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    // print booksmark tree
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.home) {
            if (gblVar.AccessType.contains("Admin")) {

                intent = new Intent(this, AdminDashboard.class);

            }
            else if (gblVar.AccessType.contains("Manager")) {

                intent = new Intent(this, UserDashbord.class);

            }
            else if (gblVar.AccessType.contains("Client")) {

                intent = new Intent(this, UserDashbord.class);

            }
            else {

                intent = new Intent(this, ViewImage.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}