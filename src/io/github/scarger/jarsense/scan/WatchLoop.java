package io.github.scarger.jarsense.scan;

import io.github.scarger.jarsense.Bootstrap;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by Synch on 2017-12-26.
 * nerfed version of WatchLoop class (from my plugin, ReferME)
 */
public class WatchLoop implements Runnable {

    private Path path;
    private Bootstrap plugin;

    WatchLoop(Path path, Bootstrap plugin){
        this.path = path;
        this.plugin = plugin;
    }

    //does the scan, the juicy stuffs h e r e
    private void doScan(WatchKey key){
        WatchEvent.Kind<?> kind;
        for (WatchEvent<?> watchEvent : key.pollEvents()) {
            // Get the type of the event
            kind = watchEvent.kind();
            if (ENTRY_CREATE == kind || ENTRY_MODIFY == kind) {
                //get the new file, knowing this type event involves path
                WatchEvent<Path> currEvent = (WatchEvent<Path>) watchEvent;
                Path child = path.resolve(currEvent.context());
                
                //check if it's a plugine (.jar, duhhhh... i thinnk -.-)
                if(child.toString().endsWith(".jar")){
                    plugin.changeFound();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                            plugin.getConfig().getString("refreshCommand"));
                }   
            }
        }
    }

    @Override
    public void run() {
        WatchKey key;
        
        try (WatchService service = path.getFileSystem().newWatchService()) {
            //register the path for a certain event so the watcher will watch for it
            path.register(service, ENTRY_CREATE, ENTRY_MODIFY);
            while (true) {
                try {
                    key = service.take();
                    doScan(key);
                    if (!key.reset()) {
                        break;
                    }
                    //sleep, we don't need it to reload instantly.
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){
                    System.out.println("Terminating Scanner..");
                    break;
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
