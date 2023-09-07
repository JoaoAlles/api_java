package br.edu.senai.sc.produtos.service;

import br.edu.senai.sc.produtos.entity.Produto;
import br.edu.senai.sc.produtos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@Service
public class ProdutoService {

    private ProdutoRepository produtoRepository;
    private final EntityManager entityManager;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, EntityManager entityManager) {
        this.produtoRepository = produtoRepository;
        this.entityManager = entityManager;
    }

    public ResponseEntity<?> criarProduto(Produto produto) {
        Produto novoProduto = produtoRepository.save(produto);

        if (novoProduto != null) {
            Map<String, String> mensagemDeSucesso = new HashMap<>();
            mensagemDeSucesso.put("mensagem", "Produto criado com sucesso!");
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagemDeSucesso);
        } else {
            Map<String, String> mensagemDeErro = new HashMap<>();
            mensagemDeErro.put("erro", "Falha ao criar o produto");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagemDeErro);
        }
    }

    public ResponseEntity<?> atualizarProduto(Long id, Produto produto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElse(null);

        if (produtoExistente != null) {
            produtoExistente.setCodigo(produto.getCodigo());
            produtoExistente.setNome(produto.getNome());
            produtoExistente.setDescricao(produto.getDescricao());
            produtoExistente.setPreco(produto.getPreco());

            Map<String, String> mensagemDeSucesso = new HashMap<>();
            mensagemDeSucesso.put("mensagem", "Produto alterado com sucesso!");
            return ResponseEntity.ok(mensagemDeSucesso);
        } else {
            Map<String, String> mensagemDeErro = new HashMap<>();
            mensagemDeErro.put("erro", "Produto com ID " + id + " não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemDeErro);
        }
    }


    public ResponseEntity<?> atualizarDadosProduto(Long id, Map<String, Object> updates) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElse(null);

        if (produtoExistente != null) {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "codigo":
                        produtoExistente.setCodigo((String) value);
                        break;
                    case "nome":
                        produtoExistente.setNome((String) value);
                        break;
                    case "descricao":
                        produtoExistente.setDescricao((String) value);
                        break;
                    case "preco":
                        produtoExistente.setPreco((Double) value);
                        break;
                }
            });

            produtoRepository.save(produtoExistente);
            Map<String, String> mensagemDeSucesso = new HashMap<>();
            mensagemDeSucesso.put("mensagem", "Produto alterado com sucesso!");
            return ResponseEntity.ok(mensagemDeSucesso);
        } else {
            Map<String, String> mensagemDeErro = new HashMap<>();
            mensagemDeErro.put("erro", "Produto com ID " + id + " não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemDeErro);
        }
    }

    public ResponseEntity<?> excluirProduto(Long id) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElse(null);

        if (produtoExistente != null) {
            produtoRepository.deleteById(id);
            Map<String, String> mensagemDeSucesso = new HashMap<>();
            mensagemDeSucesso.put("mensagem", "Produto excluído com sucesso!");
            return ResponseEntity.ok(mensagemDeSucesso);
        } else {
            Map<String, String> mensagemDeErro = new HashMap<>();
            mensagemDeErro.put("erro", "Produto com ID " + id + " não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemDeErro);
        }
    }


    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public List<Produto> buscarProdutosPorCampos(Map<String, String> queryParams) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
        Root<Produto> root = query.from(Produto.class);

        List<Predicate> predicates = queryParams.entrySet().stream()
                .map(entry -> {
                    String campo = entry.getKey();
                    String valor = entry.getValue();
                    return builder.like(root.get(campo), "%" + valor + "%");
                })
                .collect(Collectors.toList());

        Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);
        query.where(builder.or(predicatesArray));

        return entityManager.createQuery(query).getResultList();
    }

    public ResponseEntity<?> buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElse(null);

        if (produto != null) {
            return ResponseEntity.ok(produto);
        } else {
            Map<String, String> mensagemDeErro = new HashMap<>();
            mensagemDeErro.put("erro", "Produto com ID " + id + " não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemDeErro);
        }
    }
}

