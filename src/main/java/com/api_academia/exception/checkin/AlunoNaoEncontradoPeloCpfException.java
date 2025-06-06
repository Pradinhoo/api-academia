package com.api_academia.exception.checkin;

public class AlunoNaoEncontradoPeloCpfException extends RuntimeException {
  public AlunoNaoEncontradoPeloCpfException(String cpfAluno) {
    super(String.format(MensagensDeErroCheckIn.CPF_NAO_ENCONTRADO, cpfAluno));
  }
}
