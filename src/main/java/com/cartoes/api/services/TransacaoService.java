package com.cartoes.api.services;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;
 
@Service
public class TransacaoService {
 
   	private static final Logger log = LoggerFactory.getLogger(CartaoService.class);
 
   	@Autowired
   	private CartaoRepository cartaoRepository;
   	
   	@Autowired
   	private CartaoService cartaoService;
   	
   	@Autowired
   	private TransacaoRepository transacaoRepository;
 
   	public List<Transacao> buscarPorCartao(String cartaoNumero) throws ConsistenciaException {
 
         	log.info("Service: buscando as transações do cartão: {}", cartaoNumero);
 
         	Optional<List<Transacao>> transacoes = transacaoRepository.findByCartaoNumero(cartaoNumero);
         	
         	if (transacoes == null || !transacoes.isPresent() || transacoes.get().size() < 1) {
 
                	log.info("Service: Nenhuma transação do cartão: {} foi encontrado", cartaoNumero);
                	throw new ConsistenciaException("Nenhuma transação do cartão: {} foi encontrado", cartaoNumero);
 
         	}
 
         	return transacoes.get();
 
   	}
 
   	public Transacao salvar(Transacao transacao) throws ConsistenciaException, ParseException {
 
   			log.info("Service: salvando a transação: {}", transacao);
   			
   			if(transacao.getCartao().getId() > 0) {
            	throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluídas.");
   			}
   			
   			Optional<Cartao> cartaoOp = cartaoService.buscarPorNumero(transacao.getCartao().getNumero());
         	Cartao cartao = cartaoOp.get();
         	
         	if(cartao.getBloqueado()) {
         		
         		log.info("Service: {}", getMsgConsistencia("bloqueado"));
            	throw new ConsistenciaException(getMsgConsistencia("bloqueado"));
         	
         	}
         	
         	SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
         	Date dataValidade = dateFormat.parse(cartao.getDataValidade().toString());
         	transacao.setDataTransacao(dateFormat.parse(new Date().toString()));
         	
         	if(dataValidade.after(transacao.getDataTransacao())) {
         		log.info("Service: {}", getMsgConsistencia("vencido"));
            	throw new ConsistenciaException(getMsgConsistencia("vencido"));
         	}
 
         	try {
 
                	return transacaoRepository.save(transacao);
 
         	} catch (DataIntegrityViolationException e) {
 
                	log.info("{}", e.getMessage());
                	throw new ConsistenciaException("{}", e.getMessage());
                	
         	}
 
   	}
   	
   	protected String getMsgConsistencia(String motivo) {
   		return "Não é possível incluir transações para este cartão, pois o mesmo encontra-se " + motivo;
   	}
 
}