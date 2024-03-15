package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // 1-3. 로그 찍기
@RestController
public class ArticleApiController {
//    @Autowired // 게시글 Repository 주입
//    private ArticleRepository articleRepository;

    @Autowired // 서비스 객체 주입
    private ArticleService articleService;

//    // GET
//    @GetMapping("/api/articles")
//    public List<Article> index() {
//        return articleRepository.findAll();
//    }
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleService.index();
    }

//    @GetMapping("/api/articles/{id}")
//    public Article show(@PathVariable Long id) {
//        return articleRepository.findById(id).orElse(null);
//    }

    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id) {
        return articleService.show(id);
    }

//    // POST
//    @PostMapping("/api/articles")
//    public Article create(@RequestBody ArticleForm dto) {
//        Article article = dto.toEntity();
//        return articleRepository.save(article);
//    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto) {
        Article created = articleService.create(dto);
        // 삼항 연산자
        // return (created != null) ? good : bad;
        // 조건이 참이면 good, 거짓이면 bad
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    // PATCH
//    @PatchMapping("/api/articles/{id}")
//    public ResponseEntity<Article> update(@PathVariable Long id, // 2-1. 반환형 수정
//                                 @RequestBody ArticleForm dto) {
//        // 1. DTO -> 엔티티 변환하기
//        Article article = dto.toEntity(); // 1-1. dto를 엔티티로 변환
//        log.info("id: {}, article: {}", id, article.toString()); // 1-2. 로그 찍기
//        // 2. 타깃 조회하기
//        Article target = articleRepository.findById(id).orElse(null);
//        // 3. 잘못된 요청 처리하기
//        if (target == null || id != article.getId()) { // 2-2. 잘못된 요청인지 판별
//            // 400, 잘못된 요청 응답
//            log.info("잘못된 요청! id: {}, article: {}", id, article, article.toString()); // 2-3. 로그 찍기
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        // 4. 업데이트 및 정상 응답(200)하기
//        Article updated = articleRepository.save(article); // 4-1. article 엔티티 DB에 저장
//        return ResponseEntity.status(HttpStatus.OK).body(updated); // 4-2. 정상 응답
//    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,
                                          @RequestBody ArticleForm dto) {
        Article updated = articleService.update(id, dto); // 1. 서비스를 통해 게시글 수정
        return (updated != null) ? // 2. 수정되면 정상, 안되면 오류 응답
                ResponseEntity.status(HttpStatus.OK).body(null) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    @PatchMapping("/api/articles/{id}/patch")
//    public ResponseEntity<Article> patch(@PathVariable Long id,
//                                          @RequestBody ArticleForm dto) {
//        // 1. DTO -> 엔티티 변환하기
//        Article article = dto.toEntity();
//        log.info("id: {}, article: {}", id, article.toString());
//        // 2. 타깃 조회하기
//        Article target = articleRepository.findById(id).orElse(null);
//        // 3. 잘못된 요청 처리하기
//        if (target == null || id != article.getId()) {
//            // 400, 잘못된 요청 응답
//            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        // 4. 업데이트 및 정상 응답(200)하기
//        target.patch(article); // 1. 기존 데이터에 새 데이터 붙이기
//        Article updated = articleRepository.save(target);
//        return ResponseEntity.status(HttpStatus.OK).body(updated);
//    }

//    // DELETE
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Article> delete(@PathVariable Long id) {
//        // 1. 대상 찾기
//        Article target = articleRepository.findById(id).orElse(null);
//        // 2. 잘못된 요청 처리
//        if (target == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        // 3. 대상 삭제
//        articleRepository.delete(target);
//        return ResponseEntity.status(HttpStatus.OK).build(); // build()대신 body(null)도 가능
//    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
        Article deleted = articleService.delete(id); // 1. 서비스를 통해 게시글 삭제
        return (deleted != null) ?                    // 2. 삭제 결과에 따라 응답 처리
                ResponseEntity.status(HttpStatus.OK).body(null) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/api/transaction-test") // 여러 게시글 생성 요청 접수
    public ResponseEntity<List<Article>> transactionTest
            (@RequestBody List<ArticleForm> dtos) { // transactionTest() 메서드 정의
        List<Article> createdList = articleService.createdArticles(dtos); // 서비스 호출
        return (createdList != null) ?              // 생성 결과에 따른 응답 처리
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
}
