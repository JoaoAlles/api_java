package br.edu.senai.sc.produtos.repository;

import br.edu.senai.sc.produtos.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Produto findByCodigo(String codigo);
    List<Produto> findByNomeContaining(String nome);
}