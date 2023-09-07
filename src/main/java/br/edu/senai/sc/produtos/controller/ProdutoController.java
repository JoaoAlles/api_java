package br.edu.senai.sc.produtos.controller;

import br.edu.senai.sc.produtos.entity.Produto;
import br.edu.senai.sc.produtos.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {
        ResponseEntity<?> response = produtoService.criarProduto(produto);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getBody());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        ResponseEntity<?> response = produtoService.atualizarProduto(id, produto);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getBody());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarDadosProduto(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        ResponseEntity<?> response = produtoService.atualizarDadosProduto(id, updates);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getBody());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirProduto(@PathVariable Long id) {
        ResponseEntity<?> response = produtoService.excluirProduto(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getBody());
        }
    }


    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/buscar")
    public List<Produto> buscarProdutosPorCampos(@RequestParam Map<String, String> queryParams) {
        return produtoService.buscarProdutosPorCampos(queryParams);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable Long id) {
        ResponseEntity<?> response = produtoService.buscarProdutoPorId(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getBody());
        }
    }
}
