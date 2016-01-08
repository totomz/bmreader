/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myideas.bancamarcheextractor.pdfread;

import it.myideas.bancamarcheextractor.Distinta;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tommaso
 */
public class ReadPDF {

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
    }
    
    /**
     * Check that "Tipo Disposizione" is correclty read
     */
    //@Test
    public void testBonifico1() {
        Path bonificoPilotto = Paths.get("input/bonifico_pilotto.pdf");

        assertNotNull(bonificoPilotto);
        assertTrue(bonificoPilotto.toFile().exists());
        assertTrue(bonificoPilotto.toFile().canRead());

        System.out.println("Start ");
        Distinta distintaPilotto = Distinta.parse(bonificoPilotto);

        assertEquals("bonifico", distintaPilotto.getTipoDisposizione());
        assertEquals("condominio_via_camillo_pilotto", distintaPilotto.getBeneficiario());
        assertEquals(LocalDate.parse("2015-12-07"), distintaPilotto.getData());        
    }

    @Test
    public void testPagamento() {
        Path pagamento = Paths.get("input/pagamento_ema.pdf");

        assertNotNull(pagamento);
        assertTrue(pagamento.toFile().exists());
        assertTrue(pagamento.toFile().canRead());

        System.out.println("Start ");
        Distinta distinta = Distinta.parse(pagamento);

        assertEquals("bonifico", distinta.getTipoDisposizione());
        assertEquals("emanuele_masciarri", distinta.getBeneficiario());
        assertEquals(LocalDate.parse("2015-12-07"), distinta.getData());        
    }
    
}
