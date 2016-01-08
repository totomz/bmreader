/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myideas.bancamarcheextractor;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represent a distinta
 *
 * @author tommaso
 */
public class Distinta {

    private static Logger log = LoggerFactory.getLogger(Distinta.class);

    private String tipoDisposizione;
    private String beneficiario;
    private LocalDate data;

    public static Distinta parse(Path file) {

        try (PDDocument doc = PDDocument.load(file.toFile())) {

            Distinta distinta = new Distinta();
            
            PDFTextStripper stripper = new PDFTextStripper();
            String contents = stripper.getText(doc);
            Stream<String> lines = Arrays.stream(contents.split(stripper.getLineSeparator()));
           
            log.debug("FILE:" + file.toString());
            log.debug(contents);
            
            lines.forEach(line ->{
            
                if(line.startsWith("Tipo disposizione")){
                    distinta.tipoDisposizione = line.replace("Tipo disposizione", "").trim().toLowerCase();
                }
                else if (line.startsWith("1 Esecuzione")) {
                    String[] p = line.split(" ");
                    
                    distinta.beneficiario = Arrays.stream(Arrays.copyOfRange(p, 4, p.length))
                            .map(String::toLowerCase)
                            .collect(Collectors.joining("_"));
                            
                    distinta.data = LocalDate.parse(p[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                
            });

            if(!isOk(distinta.beneficiario) || !isOk(distinta.tipoDisposizione) || distinta.data == null){
                throw new IOException("Parser failure for file " + file.toString());
            }
            
            return distinta;
        } catch (IOException e) {
            log.error("Error parsing PDF", e);
            return null;
        }
    }

    private static boolean isOk(String s){
        return s != null && s.length() > 0;
    }
    
    public String getTipoDisposizione() {
        return this.tipoDisposizione;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        
        return String.format("%s, %s, %S", tipoDisposizione, data, beneficiario);
    }
    
    

}
