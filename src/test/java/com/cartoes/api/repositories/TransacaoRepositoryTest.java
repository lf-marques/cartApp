package com.cartoes.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Transacao;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoRepositoryTest {
	
	@Autowired
	private TransacaoRepository transacaoRepository;  
	@Autowired
	private CartaoRepository cartaoRepository;  
	@Autowired
	private ClienteRepository clienteRepository;  

	private Cliente clienteTeste;
	private Cartao cartaoTeste;
	private Transacao transacaoTeste;
	
	private void CriarTransacaoTestes() throws ParseException {

		clienteTeste = new Cliente();
		cartaoTeste = new Cartao();
		transacaoTeste = new Transacao();
		
		clienteTeste.setCpf("84558558007");
		clienteTeste.setNome("Luan Marques");
		clienteTeste.setUf("PR");
		clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("29/08/2020"));
		
		cartaoTeste.setNumero("12345678910");
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("05/12/2021"));
		cartaoTeste.setCliente(clienteTeste);
		
		transacaoTeste.setCnpj("22825185000106");
		transacaoTeste.setValor(100.50);
		transacaoTeste.setQtdParcelas(2);
		transacaoTeste.setJuros(2.3);
		transacaoTeste.setCartao(cartaoTeste);
		transacaoTeste.setDataTransacao(new SimpleDateFormat("dd/MM/yyyy").parse("29/08/2020"));
		
	}
	
	@Before
	public void setUp() throws Exception {
		
		CriarTransacaoTestes();
		clienteRepository.save(clienteTeste);
		cartaoRepository.save(cartaoTeste);
		transacaoRepository.save(transacaoTeste);
		
	}
	
	@After
	public void tearDown() throws Exception {

		clienteRepository.deleteAll();
		cartaoRepository.deleteAll();
		transacaoRepository.deleteAll();
		
	}
	
	@Test
	public void testFindById() {	
		
		Transacao transacao = transacaoRepository.findById(transacaoTeste.getId()).get();
		assertEquals(transacaoTeste.getId(), transacao.getId());
		
	}
	
	@Test
	public void findByCartaoNumero() {
		
		List<Transacao> lstTransacao = transacaoRepository.findByCartaoNumero(
				transacaoTeste.getCartao().getNumero()).get();
		
		if (lstTransacao.size() != 1) {
			fail();
		}
		
		Transacao transacao = lstTransacao.get(0);
		
		assertTrue(transacao.getCartao().getNumero().equals(transacaoTeste.getCartao().getNumero()));
		
	}

}