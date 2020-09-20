package com.cartoes.api.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cliente;
import com.cartoes.api.repositories.ClienteRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClienteServiceAscendenteTest {

	@MockBean
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	private Cliente criarCliente() throws ParseException {
		
		Cliente clienteTeste = new Cliente();
		
		clienteTeste.setNome("Nome Teste");
		clienteTeste.setCpf("05887098082");
		clienteTeste.setUf("pr");
		clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020"));
		
		return clienteTeste;
	}

	@Test
	public void testSalvar() throws ConsistenciaException, ParseException {

		clienteService.salvar(criarCliente());
		
	}

}