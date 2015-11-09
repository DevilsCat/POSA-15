package vandy.mooc.presenter;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloadAsyncTask extends AsyncTask<Uri, Void, Uri> {
    // Stores the instance of ImagePresenter to retrieve Context, pathname etc.
    private ImagePresenter mImagePresenter = null;
    
    // Stores the url which is used to download the image. (Uses to later failure indication)
    private Uri url = null;
    
    
    /**
     * Constructor
     * @param imagePresenter the caller object for retrieving Context etc.
     */
    public ImageDownloadAsyncTask(ImagePresenter imagePresenter) {
        this.mImagePresenter = imagePresenter;
    }
    
    /**
     * Downloads Image in background, returns a Uri to the location
     * of downloaded image.
     */
    @Override
    protected Uri doInBackground(Uri... urls) {
        url = urls[0];
        return mImagePresenter.getModel().downloadImage(
                    mImagePresenter.getApplicationContext(), 
                    url, 
                    mImagePresenter.getDirectoryPathname());
    }
    
    /**
     * Forwards downloaded to ImageProcessAsyncTask to process it.
     */
    @Override
    protected void onPostExecute(Uri pathToImageFile) {
        if (pathToImageFile == null) {  
            // Download failed, indicate presenter
            mImagePresenter.onProcessingComplete(url, pathToImageFile);
        } else {  
            // Chain this downloaded task to ImageProcessAsyncTask.
            new ImageProcessAsyncTask(url, mImagePresenter)
                .executeOnExecutor(
                    mImagePresenter.getImageOpsThreadPoolExecutor(), 
                    pathToImageFile);
        }
    }

}
