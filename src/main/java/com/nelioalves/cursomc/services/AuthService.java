package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired 
	private BCryptPasswordEncoder pe;
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random(); 

	public void sendNewPassword(String email) {
		Cliente cliente = this.clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		String newPass = this.newPassword();
		cliente.setSenha(pe.encode(newPass));
		this.clienteRepository.save(cliente);
		this.emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i=0; i<10; i++) {
			vet[i] = this.randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = this.random.nextInt(3);
		if (opt == 0) { // gera um digito
			return (char) (this.random.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (this.random.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (this.random.nextInt(26) + 97);
		}
	}
}
