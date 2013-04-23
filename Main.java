package com.yarashiifansub.vertebro;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import org.apache.commons.io.FileUtils;

public class Main {

    public static JFrame frame;
    private static EmbeddedMediaPlayer mediaPlayer;
    private static long millis;

    public static void run(String mrl, long freq) throws AWTException, IOException, URISyntaxException {
        mediaPlayer.playMedia(mrl);
        mediaPlayer.parseMedia();
        millis = mediaPlayer.getLength();
        File dirc = new File(Frame.fileSave);
        FileUtils.cleanDirectory(dirc);
        new Robot().delay(5000);
        capture(freq);
    }

    public Main() throws MalformedURLException {

        frame = new JFrame("Capture des screenshots en cours... - LAISSEZ MOI AU PREMIER PLAN D:");
        frame.setLocation(50, 50);
        frame.setSize(1280 + 16, 720 + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] libvlcArgs = {"--no-video-title-show"};
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
        FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(frame);
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
        Canvas canvas = new Canvas();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        frame.add(canvas);
        frame.setVisible(true);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }, 0L, 1000L, TimeUnit.MILLISECONDS);
    }

    public static void capture(long freq) throws AWTException, IOException, URISyntaxException {
        long time = millis / freq;
        for (int i = 1; i <= (int) freq; i++) {
            new Robot().delay(100);
            mediaPlayer.setTime(time * i);
            frame.setTitle("Screen n°" + i);
            String name = Frame.getName(Frame.fileChoose);
            File file = new File(Frame.fileSave + "/" + name + "_" + i + ".png");
            new Robot().delay(100);
            BufferedImage bi = mediaPlayer.getVideoSurfaceContents();
            ImageIO.write(bi, "png", file);
        }
        System.exit(0);
    }
}
