package com.cartoes.api.utils;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
 
import com.cartoes.api.dtos.CartaoDto;
import com.cartoes.api.dtos.ClienteDto;
import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;
 
public class ConversaoUtils {
 
   	public static Cartao Converter(CartaoDto cartaoDto) throws ParseException {
 
         	Cartao cartao = new Cartao();
 
         	if (cartaoDto.getId() != null && cartaoDto.getId() != "")
                	cartao.setId(Integer.parseInt(cartaoDto.getId()));
 
         	cartao.setNumero(cartaoDto.getNumero());
         	cartao.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse(cartaoDto.getDataValidade()));
         	cartao.setBloqueado(Boolean.parseBoolean(cartaoDto.getBloqueado()));
 
         	Cliente cliente = new Cliente();
         	cliente.setId(Integer.parseInt(cartaoDto.getClienteId()));
 
         	cartao.setCliente(cliente);
 
         	return cartao;
 
   	}
   	
   	public static CartaoDto Converter(Cartao cartao) {
 
         	CartaoDto cartaoDto = new CartaoDto();
         	
         	cartaoDto.setId(String.valueOf(cartao.getId()));
         	cartaoDto.setNumero(cartao.getNumero());
         	cartaoDto.setDataValidade(cartao.getDataValidade().toString());
         	cartaoDto.setBloqueado(String.valueOf(cartao.getBloqueado()));
         	cartaoDto.setClienteId(String.valueOf(cartao.getCliente().getId()));
 
         	return cartaoDto;
 
   	}
   	
   	public static List<CartaoDto> ConverterLista(List<Cartao> lista){
         	
         	List<CartaoDto> lst = new ArrayList<CartaoDto>(lista.size());
         	
         	for (Cartao cartao : lista) {
                	lst.add(Converter(cartao));
         	}
         	
         	return lst;
         	
   	}
   	
   	public static List<TransacaoDto> ConverterListaTransacao(List<Transacao> lista){
         	
         	List<TransacaoDto> lst = new ArrayList<TransacaoDto>(lista.size());
         	
         	for (Transacao transacao : lista) {
                	lst.add(Converter(transacao));
         	}
         	
         	return lst;
         	
   	}
 
   	public static Cliente Converter(ClienteDto clienteDto) {
 
         	Cliente cliente = new Cliente();
 
         	if (clienteDto.getId() != null && clienteDto.getId() != "")
                	cliente.setId(Integer.parseInt(clienteDto.getId()));
 
         	cliente.setNome(clienteDto.getNome());
         	cliente.setCpf(clienteDto.getCpf());
         	cliente.setUf(clienteDto.getUf());
 
         	return cliente;
 
   	}
    
   	public static Transacao Converter(TransacaoDto transacaoDto) {
 
         	Transacao transacao = new Transacao();
 
         	if (transacaoDto.getId() != null && transacaoDto.getId() != "")
         		transacao.setId(Integer.parseInt(transacaoDto.getId()));
 
         	transacao.setJuros(Double.parseDouble(transacaoDto.getJuros()));
         	transacao.setQtdParcelas(Integer.parseInt(transacaoDto.getQtdParcelas()));
         	transacao.setValor(Double.parseDouble(transacaoDto.getValor()));
         	transacao.setCnpj(transacaoDto.getCnpj());
         	transacao.setCartao(transacaoDto.getCartao());
 
         	return transacao;
 
   	}
 
   	public static TransacaoDto Converter(Transacao transacao) {
 
   		TransacaoDto transacaoDto = new TransacaoDto();
   	 
     	transacaoDto.setId(String.valueOf(transacao.getId()));
     	transacaoDto.setJuros(String.valueOf(transacao.getJuros()));
     	transacaoDto.setQtdParcelas(String.valueOf(transacao.getQtdParcelas()));
     	transacaoDto.setValor(String.valueOf(transacao.getValor()));
     	transacaoDto.setCartao(transacao.getCartao());
     	transacaoDto.setCnpj(transacao.getCnpj());
     	
     	return transacaoDto;
 
   	}
   	
   	public static ClienteDto Converter(Cliente cliente) {
 
         	ClienteDto clienteDto = new ClienteDto();
 
         	clienteDto.setId(String.valueOf(cliente.getId()));
         	clienteDto.setNome(cliente.getNome());
         	clienteDto.setCpf(cliente.getCpf());
         	clienteDto.setUf(cliente.getUf());
 
         	return clienteDto;
 
   	}

}