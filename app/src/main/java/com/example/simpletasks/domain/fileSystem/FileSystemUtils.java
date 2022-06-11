package com.example.simpletasks.domain.fileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for the file system utility class.
 */
public interface FileSystemUtils {
    /**
     * Get the file object to store an image to.
     *
     * @param storageDir the directory to store the image file in.
     * @return the file to store the image to
     * @throws IOException if the file could not be created
     */
    File createImageFile(File storageDir) throws IOException;

    /**
     * Get the file object to store a video to.
     *
     * @param storageDir the directory to store the video file in.
     * @return the file to store the video to
     * @throws IOException if the file could not be created
     */
    File createVideoFile(File storageDir) throws IOException;

    /**
     * Get the file object to store an audio recording to.
     *
     * @param storageDir the directory to store the audio file in.
     * @return the file to store the audio to
     * @throws IOException if the file could not be created
     */
    File createAudioFile(File storageDir) throws IOException;

    /**
     * Copy an input stream to a specified output
     *
     * @param input input stream
     * @param output output stream
     * @throws IOException if either source or target of the stream are invalid
     */
    void copyStream(InputStream input, OutputStream output) throws IOException;
}