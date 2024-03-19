# Threads e Spring Boot

O Spring Boot oferece uma maneira própria de lidar com a criação de threads, exigindo uma abordagem diferente, já que o uso de `synchronized` por si só pode não ser suficiente. Nestes casos, é recomendável utilizar recursos do Spring.

## Criando Lock no Spring

Ao lidar com concorrência em aplicações Spring, é possível implementar locks para garantir a consistência dos dados. Uma maneira comum de fazer isso é através de locks otimistas, onde há pouca possibilidade de conflitos entre processos.

### Exemplo de Utilização:

Para implementar um lock otimista usando Spring, pode-se utilizar a anotação `@Version` fornecida pela JPA (Java Persistence API). Esta anotação é utilizada para gerenciar a versão de uma entidade no banco de dados.

```java
// Aplicando a versão:

import javax.persistence.Version;

@Version
private Integer versao;
```
O uso da anotação @Version permite que o Spring gerencie automaticamente as versões das entidades, facilitando o controle de concorrência em operações de escrita no banco de dados.

Observação: É importante ressaltar que o lock otimista é mais adequado quando a possibilidade de concorrência entre processos é baixa. Para cenários com alta concorrência, pode ser necessário adotar estratégias de lock pessimista.

# Transações e Locks com Spring Data JPA

Quando trabalhamos com bancos de dados e programação, entender transações é crucial. No Spring Data JPA, o gerenciamento de transações é simplificado, deixando para o framework o cuidado das operações.

## Níveis de Isolamento

Os níveis de isolamento definem como as transações são tratadas quando ocorrem simultaneamente. Eles evitam problemas como leitura suja, não repetível e fantasma.

## Locks em Transações

- **Lock Pessimista:** Bloqueia dados para evitar alterações concorrentes.
- **Lock Otimista:** Permite leitura de dados, verificando alterações antes de concluir.

## Configuração com Spring Data JPA

Utilize `@Version` para lock otimista. Para lock pessimista, adicione `@Lock` no repositório:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
List<Livro> findByAutor(String autor);
```

# Resumo dos Problemas de Isolamento em Transações

Ao lidar com transações em bancos de dados, é essencial entender os problemas de isolamento que podem surgir. Abaixo estão alguns desses problemas:

- **Sujeira de Leitura (Dirty Read):** Uma transação lê dados que foram modificados por outra transação que ainda não foi concluída.

- **Leitura Não Repetível (Non-Repeatable Read):** Uma transação lê a mesma linha duas vezes e encontra dados diferentes porque outra transação alterou os dados entre as duas leituras.

- **Leitura Fantasma (Phantom Read):** Uma transação reexecuta uma consulta e obtém um conjunto de linhas diferente da primeira execução, porque outras transações inseriram ou excluíram linhas que satisfazem a condição da consulta.
# Níveis de Isolamento e Locks em Transações

Ao lidar com transações em bancos de dados, é importante considerar os níveis de isolamento e os tipos de locks disponíveis.

## Níveis de Isolamento

Existem quatro níveis principais de isolamento, cada um oferecendo um equilíbrio entre performance e proteção contra problemas de concorrência:

- **Sujeira de Leitura (Dirty Read)**
- **Leitura Não Repetível (Non-Repeatable Read)**
- **Leitura Fantasma (Phantom Read)**

## Locks em Transações

Existem dois tipos de locks:

- **Lock Pessimista:** Bloqueia os dados para evitar alterações concorrentes.
- **Lock Otimista:** Permite leitura de dados, verificando alterações antes de concluir.

## Configurando Locks

Para configurar um lock otimista com a JPA, utilizamos a anotação `@Version`. Para lock pessimista, podemos utilizar a anotação `@Lock` no repositório.

# Configurando Locks Pessimistas com Spring Data JPA

Quando lidamos com transações em bancos de dados, muitas vezes precisamos aplicar locks para garantir a integridade dos dados e evitar conflitos de concorrência. No Spring Data JPA, podemos configurar locks pessimistas facilmente.

## Utilizando a Anotação @Lock

Além do atributo anotado com `@Version`, podemos aplicar locks pessimistas utilizando a anotação `@Lock` no repositório, no método específico que desejamos bloquear.

```java
interface LivroRepository extends Repository<Livro, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Livro> findByAutor(String autor);
}
```

Neste exemplo, estamos buscando um livro pelo seu autor e aplicando um lock pessimista com PESSIMISTIC_WRITE. Isso significa que enquanto estivermos alterando o livro, ninguém mais pode alterá-lo.
Outros Tipos de Locks

Além do PESSIMISTIC_WRITE, existem outros tipos de locks (otimistas e pessimistas) que podemos usar com o LockModeType. Experimente cada um desses locks para entender suas variações e aplicabilidade.
Recomendação de Leitura

Para entender melhor os locks, recomenda-se a leitura do artigo Enabling Transaction Locks in Spring Data JPA. Lá, você poderá entender mais sobre locks e como configurar transações com locks, que é a parte encapsulada pelo Spring Data JPA.

Experimente cada tipo de lock para entender suas variações. Praticando, cada uma das mudanças fica mais clara, permitindo que você aplique esse conhecimento no dia a dia.

- Para podermos utilizar o Asynchronism (Assincronismo), ou seja, criar Threads diferentes, precisamos habilitar uma função na nossa classe principal.
```Java
@SpringBootApplication
@EnableAsync
public class AdopetStoreApplication {}

// Adiciono o async onde quero:
@Async
public void enviar(PedidoDTO dto, Usuario usuario){


```

Link para congifigurar e-mail: https://cursos.alura.com.br/course/java-threads-criar-gerenciar-aplicar-spring/task/151428

## Entendo melho o método asyn.

O Spring naturalmente tem um estrutura chamada bloqueante, em cada requisição é criada uma nova thread,  nessa sitaução o async faz a libreção das threads do Spring.

São envios paralelos, desovulpando a threads, deixando o código mais peformático.

## O Spring me permitir utiolizar threads e controla-las

Exemplo de como controlar, não esquecendo de passar a configuração configuration.

O método cria várias threads e os gerencia, conectanco com o banco de dados.    

```Java
@Configuration
public class AsyncConfiguration {
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //mínimo de threads
        executor.setCorePoolSize(3);
        //máximo de threads
        executor.setMaxPoolSize(3);
        //capacidade máxima da fila
        executor.setQueueCapacity(100);
        //nome da thread
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}


//passo o parâmetro onde quero mudar:    @Async("asyncExecutor")
```
- Um pool de threads é um objeto que trabalha no gerenciamento das threads. Por exemplo: suponha que você quer executar uma ação que precisa de 5 threads, porém, só tem 3 no sistema operacional. Você pode escolher como distribuir as tarefas da melhor forma possível.
- Para sabe mais:
- # Executors e Definição de Pools de Threads em Java

Este documento descreve os diferentes tipos de executors disponíveis em Java para gerenciar pools de threads e como definir suas configurações.

## SingleThreadExecutor

O SingleThreadExecutor executa uma tarefa de cada vez, útil para tarefas sequenciais.

```java

ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    System.out.println("Uma tarefa simples executada pelo SingleThreadExecutor!");
});
executor.shutdown();
```

FixedThreadPool

O FixedThreadPool permite especificar um número fixo de threads para executar tarefas simultaneamente.

```java

int numberOfThreads = 4;
ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
executor.execute(() -> System.out.println("Tarefa 1"));
        executor.execute(() -> System.out.println("Tarefa 2"));
// ... adicione mais tarefas conforme necessário
        executor.shutdown();

```
O CachedThreadPool ajusta dinamicamente o número de threads com base na demanda das tarefas.

```java
ExecutorService executor = Executors.newCachedThreadPool();
executor.execute(() -> System.out.println("Tarefa que pode precisar de muitos threads!"));
        executor.shutdown();
```

  ThreadPoolTaskExecutor
  Usado para configurações mais avançadas, e oferece mais métodos adicionais.
  
outro ponto: @EnableScheduling --> permite que tarefas sejam executados enquanto o programa continua rodando

- Método de agendamento de envio de e-mail:  @Scheduled(cron = "0 26 12 * * *") segundos minutos horas dia mes ano e o método sempre dever do tipo void e com parâmetros.


No artigo a seguir, obtemos várias informações sobre o agendamento de tarefas, inclusive sobre os diversos parâmetros do @Scheduled e as formas de utilizar o cron. A leitura vale muito a pena!

- https://www.alura.com.br/artigos/agendando-tarefas-com-scheduled-do-spring?_gl=1*s7hvgz*_ga*MTE1MzA5MDM2LjE2OTgzNjE0NDA.*_ga_1EPWSW3PCS*MTcxMDg2NDIwNy4zMDUuMS4xNzEwODY2OTk3LjAuMC4w*_fplc*RnpJTXdSQmYxJTJGRDQ4d2lySk9pUFV6eCUyRkRWRnN1Sk9DYmlLWDRJZmVpRDJqJTJCUUE1Y09POFZ6NHFYZ3JOZlA2NnFJWUpaRVMycHZnRlR4aE1JbG5LQTdqenA5eTUyaFl6cjUzd1ZSSyUyRmhJczdaa3NRRVRiOUFNJTJGb3huUklEdyUzRCUzRA..#usando-o-cron-no-scheduled

- Em casos em que eu quero passar algum parâmetro eu uso o CompletableFuture, Passando o parÂmetro dentro, e o retono co CompletableFuture.completedFuture(parâmetro)
```java
@Async
public CompletableFuture<RelatorioFaturamento> faturamentoObtido() {
  var dataOntem = LocalDate.now().minusDays(1);
  var faturamentoTotal = pedidoRepository.faturamentoTotalDoDia(dataOntem);

  var estatisticas = pedidoRepository.faturamentoTotalDoDiaPorCategoria(dataOntem);

  return CompletableFuture.completedFuture(new RelatorioFaturamento(faturamentoTotal, estatisticas));
  
  //atenção, boa prática de uso de método para melhorar aguardar que a threads estejam completas, feita onde é chamda, in casu, no agendamento service. 
  CompletableFuture.allOf(estoqueZerado, faturamento).join();
}
```

CompletableFuture:
Este documento aborda o uso do CompletableFuture em Java para recuperar retornos de threads. O CompletableFuture é uma extensão do conceito de Future, oferecendo mais métodos e facilitando o tratamento de exceções. Recomenda-se o uso do método join() para evitar erros de sincronização em operações assíncronas. É importante entender os métodos, tratamentos de erros e boas práticas ao utilizar CompletableFuture.
Link: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
- É uma boa prática utilizar método para sicronizar as threads.

