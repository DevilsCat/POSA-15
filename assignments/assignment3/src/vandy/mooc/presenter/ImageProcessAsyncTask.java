package vandy.mooc.presenter;

import android.net.Uri;
import android.os.AsyncTask;
import vandy.mooc.common.BitmapUtils;

public class ImageProcessAsyncTask extends AsyncTask<Uri, Void, Uri> {
    // Stores the instance of ImagePresenter to retrieve Context, pathname etc.
    ImagePresenter mImagePresenter = null;
    
    // Stores the url which is used to download the image. (Uses to later failure indication)
    private Uri url = null;
    
    //Stores the file path to downloaded image file. (Uses to later image processing).
    private Uri mPathToDownloadedImageFile = null;
    
    /**
     * Constructor
     * @param url The URL of the image to download.
     * @param imagePresenter The caller object.
     */
    public ImageProcessAsyncTask(Uri url, ImagePresenter imagePresenter) {
        this.mImagePresenter = imagePresenter;
        this.url = url;
    }
    
    /**
     * Performs background remote image downloading.
     * Returns {@link Uri} pointing to the location of processed image,
     * null if process fails.
     */
    @Override
    protected Uri doInBackground(Uri... pathsToImageFile) {
        mPathToDownloadedImageFile = pathsToImageFile[0];
        
        return BitmapUtils.grayScaleFilter(
                mImagePresenter.getApplicationContext(), 
                mPathToDownloadedImageFile, 
                mImagePresenter.getDirectoryPathname()
        );
    }
    
    /**
     * Deletes the downloaded image and call Presenter hook up method
     * to take action on process image.
     */
    @Override
    protected void onPostExecute(Uri pathToCompressedImageFile) {
        // Delete Original Image.
        BitmapUtils.deleteImageFromSDCard(mPathToDownloadedImageFile.toString());
        
        // Announce Presenter processing completed.
        mImagePresenter.onProcessingComplete(url, pathToCompressedImageFile);
    }
}
