package br.com.alura.adopetstore.service;

import br.com.alura.adopetstore.email.EmailRelatorioGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private EmailRelatorioGeral enviador;

    @Scheduled(cron = "0 42 12 * * *")
    public void envioEmailsAgendado(){
    var estoqueZerado = relatorioService.infoEstoque();
    var faturamento = relatorioService.faturamentoObtido();

    enviador.enviar(estoqueZerado, faturamento);

    }
}
