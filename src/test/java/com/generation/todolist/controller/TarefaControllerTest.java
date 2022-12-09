package com.generation.todolist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TarefaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	
	
	@Test
	@DisplayName("Criar nova Tarefa")
	public void deveCriarNovaTarefa() throws Exception {

		Tarefa tarefa = new Tarefa(0L, "Tarefa 01", "Tarefa numero 1", "João", LocalDate.now(), true);
		
		HttpEntity<Tarefa> corpoRequisicao = new HttpEntity<Tarefa>(tarefa);
		
		ResponseEntity<Tarefa> resposta = testRestTemplate
				.exchange("/tarefas", HttpMethod.POST, corpoRequisicao, Tarefa.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), resposta.getBody().getNome());
		
	}
	
	@Test
	@DisplayName("Listar uma Tarefa Específica")
	public void deveListarApenasUmaTarefa() {
		
		Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2", 
                                    "Maria", LocalDate.now(), true));
	
		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas/" + buscaTarefa.getId(), HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
	
	@Test
	@DisplayName("Listar todas tarefas")
	public void mostrarTodas() {

		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas/all", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}
	
//	@Test
//	@DisplayName("Buscar por nome")
//	public void buscarporNome() {
//		
//		Optional<Tarefa> novaTarefa = cadastrarTarefa(new Tarefa(0L, "Tarefa 03", "Tarefa numero 3", 
//                "Pedro", LocalDate.now(), true));
//		
//		
////		
////		Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 03", "Tarefa numero 3", 
////                "Pedro", LocalDate.now(), true));
////
//
//		ResponseEntity<Tarefa> resposta = testRestTemplate
//				.exchange("/tarefas/nome/"+novaTarefa.get().getNome(), HttpMethod.GET, null, Tarefa.class);
//
//		assertEquals(HttpStatus.OK, resposta.getStatusCode());
//	}
//	
//	public Optional<Tarefa> cadastrarTarefa(@RequestBody Tarefa tarefa) {
//
//		
//		
//		return Optional.of(tarefaRepository.save(tarefa));
//
//	}
	
	@Test
	@DisplayName("Deletar por ID") // Com ID dinamico
	public void deletarPorId() {
		Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 04", "Tarefa numero 4", 
                "Ricardo", LocalDate.now(), true));

		
		ResponseEntity<Tarefa> resposta = testRestTemplate
				.exchange("/tarefas/deletar/"+buscaTarefa.getId(), HttpMethod.DELETE, null, Tarefa.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	
	@Test
	@DisplayName("Buscar Usuário por ID -FIXO") // Com ID fixo
	public void deletarUser() {
		tarefaRepository.save(new Tarefa(0L, "Tarefa 05", "Tarefa numero 5", 
                "Antonio", LocalDate.now(), true));
		ResponseEntity<Tarefa> resposta = testRestTemplate
				.exchange("/tarefas/delete/{id}", HttpMethod.DELETE, null, Tarefa.class, 1);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	
	
}