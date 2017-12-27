package io.github.scarger.jarsense.scan;

import io.github.scarger.jarsense.Bootstrap;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Synch on 2017-12-26.
 * Borrowed class from my other plugin ReferME :P
 */
public class Watcher{

    private Path path;
    private Bootstrap plugin;
    private Thread watchThread;

    public Watcher(String path, Bootstrap plugin){
        this.path = Paths.get(path);
        this.plugin = plugin;
    }

    public void start() {
        this.watchThread = new Thread(new WatchLoop(path,plugin));
        watchThread.start();
    }

    public void close(){
        this.watchThread.interrupt();
    }


}

