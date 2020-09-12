package com.cartoes.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cartoes.api.dtos.ClienteDto;
import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.ClienteService;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ClienteService clienteService;

	@MockBean
	private TransacaoService transacaoService;

	private List<Transacao> CriarTransacaoTestes() throws ParseException {

		Cliente clienteTeste = new Cliente();
		Cartao cartaoTeste = new Cartao();
		List<Transacao> lstTransacaoTeste = new ArrayList<Transacao>();
		Transacao transacaoTeste = new Transacao();

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
		lstTransacaoTeste.add(transacaoTeste);
		
		return lstTransacaoTeste;

	}

	@Test
	@WithMockUser
	public void testBuscarPorCartaoSucesso() throws Exception {

		List<Transacao> lstTransacao = CriarTransacaoTestes();
		BDDMockito.given(transacaoService.buscarPorCartao(Mockito.anyString())).willReturn(lstTransacao);

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/12345678910").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dados[0].cartao").value(lstTransacao.get(0).getCartao().getNumero()))
				.andExpect(jsonPath("$.dados[0].cnpj").value(lstTransacao.get(0).getCnpj()))
				.andExpect(jsonPath("$.dados[0].valor").value(lstTransacao.get(0).getValor()))
				.andExpect(jsonPath("$.dados[0].qtdParcelas").value(lstTransacao.get(0).getQtdParcelas()))
				.andExpect(jsonPath("$.dados[0].juros").value(lstTransacao.get(0).getJuros()))
				.andExpect(jsonPath("$.erros").isEmpty());

	}
	
	@Test
	@WithMockUser
	public void testBuscarPorCartaoInconsistencia() throws Exception {

		BDDMockito.given(transacaoService.buscarPorCartao((Mockito.anyString())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/0000").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	
	@Test
	@WithMockUser
	public void testSalvarSucesso() throws Exception {

		List<Transacao> lstTransacao = CriarTransacaoTestes();
		List<TransacaoDto> objEntrada = ConversaoUtils.ConverterListaTransacao(lstTransacao);

		String json = new ObjectMapper().writeValueAsString(objEntrada.get(0));

		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class))).willReturn(lstTransacao.get(0));

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(objEntrada.get(0).getId()))
				.andExpect(jsonPath("$.dados.cnpj").value(objEntrada.get(0).getCnpj()))
				.andExpect(jsonPath("$.dados.valor").value(objEntrada.get(0).getValor()))
				.andExpect(jsonPath("$.dados.qtdParcelas").value(objEntrada.get(0).getQtdParcelas()))
				.andExpect(jsonPath("$.dados.juros").value(objEntrada.get(0).getJuros()))
				.andExpect(jsonPath("$.dados.cartao").value(objEntrada.get(0).getNumeroCartao())).andExpect(jsonPath("$.erros").isEmpty());

	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setJuros("2");
		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("10.2");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("CNPJ não pode ser vazio."));

	}

	@Test
	@WithMockUser
	public void testSalvarCnpjInvalido() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setJuros("2");
		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("10.2");
		objEntrada.setCnpj("12345678911111");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros[0]").value("CNPJ inválido."));

	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("10.2");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Juros não pode ser vazio."));

	}
	

	@Test
	@WithMockUser
	public void testSalvarJurosInvalido() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setQtdParcelas("2");
		objEntrada.setValor("10.2");
		objEntrada.setJuros("12345");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros[0]").value("juros precisa ser preenchido com no máximo 4 caracteres númericos"));

	}
	
	@Test
	@WithMockUser
	public void testSalvarQtdParcelasEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setJuros("12");
		objEntrada.setValor("10.2");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("qtdParcelas não pode ser vazio."));

	}
	

	@Test
	@WithMockUser
	public void testSalvarQtdParcelasInvalido() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setJuros("12");
		objEntrada.setValor("10.2");
		objEntrada.setQtdParcelas("123");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros[0]").value("qtdParcelas precisa ser preenchido com no máximo 2 caracteres númericos"));

	}
	
	@Test
	@WithMockUser
	public void testSalvarValorEmBranco() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setJuros("12");
		objEntrada.setQtdParcelas("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Valor não pode ser vazio."));

	}
	

	@Test
	@WithMockUser
	public void testSalvarValorInvalido() throws Exception {

		TransacaoDto objEntrada = new TransacaoDto();

		objEntrada.setNumeroCartao("5555666677778884");
		objEntrada.setCnpj("87115546000176");
		objEntrada.setJuros("12");
		objEntrada.setQtdParcelas("1");
		objEntrada.setValor("12345678910");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros[0]").value("Valor precisa ser preenchido com no máximo 10 caracteres númericos."));

	}
}