package br.com.alura.adopetstore.service;

import br.com.alura.adopetstore.email.EmailRelatorioGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AgendamentoService {
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private EmailRelatorioGeral enviador;

    @Scheduled(cron = "0 00 19 * * *")
    public void envioEmailsAgendado(){
    var estoqueZerado = relatorioService.infoEstoque();
    var faturamento = relatorioService.faturamentoObtido();

        CompletableFuture.allOf(estoqueZerado, faturamento).join();

        try {
            enviador.enviar(estoqueZerado.get(), faturamento.get());
        } catch (InterruptedException |ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
