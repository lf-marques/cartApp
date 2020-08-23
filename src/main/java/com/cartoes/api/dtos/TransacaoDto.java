package com.cartoes.api.dtos;
 
import javax.validation.constraints.NotEmpty;

import com.cartoes.api.entities.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class TransacaoDto {
 
   	private String id;
   	
   	@NotEmpty(message = "Cnpj não pode ser vazio.")
   	private String cnpj;
   	
   	@NotEmpty(message = "Valor não pode ser vazio.")
   	private String valor;
   	
   	@NotEmpty(message = "qtdParcelas não pode ser vazio.")
   	private String qtdParcelas;
   	
   	@NotEmpty(message = "Juros não pode ser vazio.")
   	private String juros;

   	@JsonProperty("cartao")
   	//@NotEmpty(message = "O número do cartão não pode ser vazio.")
   	private Cartao cartao;
   	
   	public String getId() {
     	return id;
   	}
   	
   	public void setId(String id) {
     	this.id = id;
   	}
   	
   	public String getCnpj() {
     	return cnpj;
   	}
   	
   	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
   	}
   	
   	public void setValor(String valor) {
     	this.valor = valor;
   	}
   	
   	public String getValor() {
     	return valor;
	}
   	
   	public String getJuros() {
     	return juros;
	}
	
	public void setJuros(String juros) {
     	this.juros = juros;
	}
   	
   	public String getQtdParcelas() {
 		return qtdParcelas;
   	}
   	
   	public void setQtdParcelas(String qtdParcelas) {
     	this.qtdParcelas = qtdParcelas;
   	}
   	
   	public Cartao getCartao() {
     	return cartao;
   	}
   	
   	public void setCartao(Cartao cartao) {
     	this.cartao= cartao;
   	}
         	
   	@Override
   	public String toString() {
     	return "Transacao[id=" + id + ","
               	+ "cnpj=" + cnpj + ","
               	+ "valor=" + valor + ","
                + "qtdParcelas=" + qtdParcelas + ","
               	+ "juros=" + juros + ","
               	+ "numero=" + cartao.getNumero() + "]";
   	}
 
}