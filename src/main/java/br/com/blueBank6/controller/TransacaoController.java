package br.com.blueBank6.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.blueBank6.models.Conta;
import br.com.blueBank6.service.ContaService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.blueBank6.models.Transacao;
import br.com.blueBank6.service.TransacaoService;

@RestController
@RequestMapping(path = "/transacao")
public class TransacaoController {

	@Autowired
	private TransacaoRespostaDTO transacaoRespostaDTO;

	@Autowired
	private ContaService contaservice;

	@Autowired
	private TransacaoService transacaoservice;

	@PostMapping("/salvar")
	public ResponseEntity<Object> SalvarTransacao(@RequestBody Transacao transacao) {
		try {
			transacao.setData(LocalDateTime.now());
			transacaoservice.save(transacao);
			return new ResponseEntity<>("Transação efetuada com sucesso", HttpStatus.CREATED);
		} catch (Exception e) {
			String msg = e.getMessage();
			return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
		}
	}


	@GetMapping("/saldo/{contaId}")
	public BigDecimal mostraSaldo(@PathVariable Long contaId) {
		return contaservice.mostrarSaldo(contaId);
	}
  
	@GetMapping("/extrato/{contaId}")
	public List<String> extratojava(@PathVariable Long contaId) {
		Conta conta = contaservice.get(contaId);
		List<Transacao> transacoes = transacaoservice.buscarTransacoes(contaId);
		List<String> extrato = new ArrayList<>();
		for (Transacao i : transacoes) {
			if (i.getTipo().equals("transferir")) {
				extrato.add(transacaoRespostaDTO.converter(i));

			} else {
				extrato.add(transacaoRespostaDTO.converterSimples(i));
			}
		}
		return extrato;
	}
}