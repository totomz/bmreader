/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myideas.bancamarcheextractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tommaso
 */
public class Main {
 
    private static final String pathInput = "input";
    private static final String pathOutput = "output";

    private static Logger log = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) throws IOException {
    
        log.info("Clearing output folder");
        Files.list(Paths.get(pathOutput))
                .filter(file -> { return file.toString().endsWith("pdf");})
                .map(f ->{return f.toFile();})
                .forEach(file ->{file.delete();});
        
        long start = System.nanoTime();
        // Read all files in ./input
        Files.list(Paths.get(pathInput))
                .filter(file -> { return file.toString().endsWith("pdf");})
                .parallel()
                .forEach(file -> {
                    Distinta distinta = Distinta.parse(file);
                    
                    String newFileName = distinta.getData().format(DateTimeFormatter.BASIC_ISO_DATE)
                            + "-" + distinta.getTipoDisposizione() 
                            + "-"  + distinta.getBeneficiario() 
                            + ".pdf";
                    
                    log.info("Processing " + newFileName);
                    
            try {
                Files.copy(file, Paths.get(pathOutput + "/" + newFileName), StandardCopyOption.REPLACE_EXISTING);
            } 
            catch (IOException ex) {
                log.error("Error copying file " + file.toString(), ex);
            }
        });  
        
        log.info("Elapsed: " + (System.nanoTime() - start) + "ms");
    }
    
}
