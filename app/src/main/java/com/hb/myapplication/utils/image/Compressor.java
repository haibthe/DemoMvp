package com.hb.myapplication.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by buihai on 8/28/17.
 */

public class Compressor {

    private int maxWidth = 612;
    private int maxHeight = 816;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private String destinationDirectoryPath;

    public Compressor(Context context) {
        destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
    }

    public Compressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Compressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Compressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public Compressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public Compressor setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    public File compressToFile(File imageFile, String compressedFileName) throws IOException {
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile) {
        return compressToFileAsFlowable(imageFile, imageFile.getName());
    }

    public Observable<File> compressToFileAsObservable(final File imageFile) {
        return compressToFileAsObservable(imageFile, imageFile.getName());
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile, final String compressedFileName) {
        return Flowable.defer(new Callable<Flowable<File>>() {
            @Override
            public Flowable<File> call() {
                try {
                    return Flowable.just(compressToFile(imageFile, compressedFileName));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }

    public Flowable<Bitmap> compressToBitmapAsFlowable(final File imageFile) {
        return Flowable.defer(new Callable<Flowable<Bitmap>>() {
            @Override
            public Flowable<Bitmap> call() {
                try {
                    return Flowable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }

    public Observable<Bitmap> compressToBitmapAsObservable(final File imageFile) {
        return Observable.defer(new Callable<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() throws Exception {
                try {
                    return Observable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public Observable<File> compressToFileAsObservable(final File imageFile, final String compressedFileName) {
//        return Observable.create(emitter -> {
//            try {
//                emitter.onNext(compressToFile(imageFile, compressedFileName));
//            } catch (IOException e) {
//                emitter.onError(e);
//            }
//        });
        return Observable.defer(new Callable<Observable<File>>() {
            @Override
            public Observable<File> call() {
                try {
                    return Observable.just(compressToFile(imageFile, compressedFileName));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
