package com.cartoes.api.dtos;
 
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.cartoes.api.entities.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class TransacaoDto {
 
   	private String id;
   	
   	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@Length(min = 14, max = 14,
   	message = "CNPJ precisa conter 14 caracteres.")
	@CNPJ( message = "CNPJ inválido.")
   	private String cnpj;
   	
   	@NotEmpty(message = "Valor não pode ser vazio.")
   	@Length(min = 1, max = 10,
   	message = "Valor precisa ser preenchido com no máximo 10 caracteres númericos.")
   	private String valor;
   	
   	@NotEmpty(message = "qtdParcelas não pode ser vazio.")
   	@Length(min = 0, max = 2,
   	message = "qtdParcelas precisa ser preenchido com no máximo 2 caracteres númericos")
   	private String qtdParcelas;
   	
   	@NotEmpty(message = "Juros não pode ser vazio.")
   	@Length(min = 0, max = 4,
   	message = "juros precisa ser preenchido com no máximo 4 caracteres númericos")
   	private String juros;

   	@JsonProperty("cartao")
   	//@NotEmpty(message = "O número do cartão não pode ser vazio.")
   	private String numeroCartao;
   	
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
   	
   	public String getNumeroCartao() {
     	return numeroCartao;
   	}
   	
   	public void setNumeroCartao(String numeroCartao) {
     	this.numeroCartao = numeroCartao;
   	}
         	
   	@Override
   	public String toString() {
     	return "Transacao[id=" + id + ","
               	+ "cnpj=" + cnpj + ","
               	+ "valor=" + valor + ","
                + "qtdParcelas=" + qtdParcelas + ","
               	+ "juros=" + juros + ","
               	+ "numero=" + numeroCartao + "]";
   	}
 
}